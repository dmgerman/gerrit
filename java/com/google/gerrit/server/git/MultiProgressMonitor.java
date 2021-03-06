begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|NANOSECONDS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CancellationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ProgressMonitor
import|;
end_import

begin_comment
comment|/**  * Progress reporting interface that multiplexes multiple sub-tasks.  *  *<p>Output is of the format:  *  *<pre>  *   Task: subA: 1, subB: 75% (3/4) (-)\r  *   Task: subA: 2, subB: 75% (3/4), subC: 1 (\)\r  *   Task: subA: 2, subB: 100% (4/4), subC: 1 (|)\r  *   Task: subA: 4, subB: 100% (4/4), subC: 4, done    \n  *</pre>  *  *<p>Callers should try to keep task and sub-task descriptions short, since the output should fit  * on one terminal line. (Note that git clients do not accept terminal control characters, so true  * multi-line progress messages would be impossible.)  */
end_comment

begin_class
DECL|class|MultiProgressMonitor
specifier|public
class|class
name|MultiProgressMonitor
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
comment|/** Constant indicating the total work units cannot be predicted. */
DECL|field|UNKNOWN
specifier|public
specifier|static
specifier|final
name|int
name|UNKNOWN
init|=
literal|0
decl_stmt|;
DECL|field|SPINNER_STATES
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|SPINNER_STATES
init|=
operator|new
name|char
index|[]
block|{
literal|'-'
block|,
literal|'\\'
block|,
literal|'|'
block|,
literal|'/'
block|}
decl_stmt|;
DECL|field|NO_SPINNER
specifier|private
specifier|static
specifier|final
name|char
name|NO_SPINNER
init|=
literal|' '
decl_stmt|;
comment|/** Handle for a sub-task. */
DECL|class|Task
specifier|public
class|class
name|Task
implements|implements
name|ProgressMonitor
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|total
specifier|private
specifier|final
name|int
name|total
decl_stmt|;
DECL|field|count
specifier|private
name|int
name|count
decl_stmt|;
DECL|field|lastPercent
specifier|private
name|int
name|lastPercent
decl_stmt|;
DECL|method|Task (String subTaskName, int totalWork)
name|Task
parameter_list|(
name|String
name|subTaskName
parameter_list|,
name|int
name|totalWork
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|subTaskName
expr_stmt|;
name|this
operator|.
name|total
operator|=
name|totalWork
expr_stmt|;
block|}
comment|/**      * Indicate that work has been completed on this sub-task.      *      *<p>Must be called from a worker thread.      *      * @param completed number of work units completed.      */
annotation|@
name|Override
DECL|method|update (int completed)
specifier|public
name|void
name|update
parameter_list|(
name|int
name|completed
parameter_list|)
block|{
name|boolean
name|w
init|=
literal|false
decl_stmt|;
synchronized|synchronized
init|(
name|MultiProgressMonitor
operator|.
name|this
init|)
block|{
name|count
operator|+=
name|completed
expr_stmt|;
if|if
condition|(
name|total
operator|!=
name|UNKNOWN
condition|)
block|{
name|int
name|percent
init|=
name|count
operator|*
literal|100
operator|/
name|total
decl_stmt|;
if|if
condition|(
name|percent
operator|>
name|lastPercent
condition|)
block|{
name|lastPercent
operator|=
name|percent
expr_stmt|;
name|w
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|w
condition|)
block|{
name|wakeUp
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Indicate that this sub-task is finished.      *      *<p>Must be called from a worker thread.      */
DECL|method|end ()
specifier|public
name|void
name|end
parameter_list|()
block|{
if|if
condition|(
name|total
operator|==
name|UNKNOWN
operator|&&
name|getCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|wakeUp
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|start (int totalTasks)
specifier|public
name|void
name|start
parameter_list|(
name|int
name|totalTasks
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|beginTask (String title, int totalWork)
specifier|public
name|void
name|beginTask
parameter_list|(
name|String
name|title
parameter_list|,
name|int
name|totalWork
parameter_list|)
block|{}
annotation|@
name|Override
DECL|method|endTask ()
specifier|public
name|void
name|endTask
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|isCancelled ()
specifier|public
name|boolean
name|isCancelled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|getCount ()
specifier|public
name|int
name|getCount
parameter_list|()
block|{
synchronized|synchronized
init|(
name|MultiProgressMonitor
operator|.
name|this
init|)
block|{
return|return
name|count
return|;
block|}
block|}
block|}
DECL|field|out
specifier|private
specifier|final
name|OutputStream
name|out
decl_stmt|;
DECL|field|taskName
specifier|private
specifier|final
name|String
name|taskName
decl_stmt|;
DECL|field|tasks
specifier|private
specifier|final
name|List
argument_list|<
name|Task
argument_list|>
name|tasks
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|spinnerIndex
specifier|private
name|int
name|spinnerIndex
decl_stmt|;
DECL|field|spinnerState
specifier|private
name|char
name|spinnerState
init|=
name|NO_SPINNER
decl_stmt|;
DECL|field|done
specifier|private
name|boolean
name|done
decl_stmt|;
DECL|field|write
specifier|private
name|boolean
name|write
init|=
literal|true
decl_stmt|;
DECL|field|maxIntervalNanos
specifier|private
specifier|final
name|long
name|maxIntervalNanos
decl_stmt|;
comment|/**    * Create a new progress monitor for multiple sub-tasks.    *    * @param out stream for writing progress messages.    * @param taskName name of the overall task.    */
DECL|method|MultiProgressMonitor (OutputStream out, String taskName)
specifier|public
name|MultiProgressMonitor
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|String
name|taskName
parameter_list|)
block|{
name|this
argument_list|(
name|out
argument_list|,
name|taskName
argument_list|,
literal|500
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a new progress monitor for multiple sub-tasks.    *    * @param out stream for writing progress messages.    * @param taskName name of the overall task.    * @param maxIntervalTime maximum interval between progress messages.    * @param maxIntervalUnit time unit for progress interval.    */
DECL|method|MultiProgressMonitor ( OutputStream out, String taskName, long maxIntervalTime, TimeUnit maxIntervalUnit)
specifier|public
name|MultiProgressMonitor
parameter_list|(
name|OutputStream
name|out
parameter_list|,
name|String
name|taskName
parameter_list|,
name|long
name|maxIntervalTime
parameter_list|,
name|TimeUnit
name|maxIntervalUnit
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|taskName
operator|=
name|taskName
expr_stmt|;
name|maxIntervalNanos
operator|=
name|NANOSECONDS
operator|.
name|convert
argument_list|(
name|maxIntervalTime
argument_list|,
name|maxIntervalUnit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Wait for a task managed by a {@link Future}, with no timeout.    *    * @see #waitFor(Future, long, TimeUnit)    */
DECL|method|waitFor (Future<?> workerFuture)
specifier|public
name|void
name|waitFor
parameter_list|(
name|Future
argument_list|<
name|?
argument_list|>
name|workerFuture
parameter_list|)
throws|throws
name|ExecutionException
block|{
name|waitFor
argument_list|(
name|workerFuture
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * Wait for a task managed by a {@link Future}.    *    *<p>Must be called from the main thread,<em>not</em> a worker thread. Once a worker thread    * calls {@link #end()}, the future has an additional {@code maxInterval} to finish before it is    * forcefully cancelled and {@link ExecutionException} is thrown.    *    * @param workerFuture a future that returns when worker threads are finished.    * @param timeoutTime overall timeout for the task; the future is forcefully cancelled if the task    *     exceeds the timeout. Non-positive values indicate no timeout.    * @param timeoutUnit unit for overall task timeout.    * @throws ExecutionException if this thread or a worker thread was interrupted, the worker was    *     cancelled, or timed out waiting for a worker to call {@link #end()}.    */
DECL|method|waitFor (Future<?> workerFuture, long timeoutTime, TimeUnit timeoutUnit)
specifier|public
name|void
name|waitFor
parameter_list|(
name|Future
argument_list|<
name|?
argument_list|>
name|workerFuture
parameter_list|,
name|long
name|timeoutTime
parameter_list|,
name|TimeUnit
name|timeoutUnit
parameter_list|)
throws|throws
name|ExecutionException
block|{
name|long
name|overallStart
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|long
name|deadline
decl_stmt|;
name|String
name|detailMessage
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|timeoutTime
operator|>
literal|0
condition|)
block|{
name|deadline
operator|=
name|overallStart
operator|+
name|NANOSECONDS
operator|.
name|convert
argument_list|(
name|timeoutTime
argument_list|,
name|timeoutUnit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deadline
operator|=
literal|0
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|this
init|)
block|{
name|long
name|left
init|=
name|maxIntervalNanos
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
try|try
block|{
name|NANOSECONDS
operator|.
name|timedWait
argument_list|(
name|this
argument_list|,
name|left
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Send an update on every wakeup (manual or spurious), but only move
comment|// the spinner every maxInterval.
name|long
name|now
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
if|if
condition|(
name|deadline
operator|>
literal|0
operator|&&
name|now
operator|>
name|deadline
condition|)
block|{
name|workerFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|workerFuture
operator|.
name|isCancelled
argument_list|()
condition|)
block|{
name|detailMessage
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"(timeout %sms, cancelled)"
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|now
operator|-
name|deadline
argument_list|,
name|NANOSECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"MultiProgressMonitor worker killed after %sms: %s"
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|now
operator|-
name|overallStart
argument_list|,
name|NANOSECONDS
argument_list|)
argument_list|,
name|detailMessage
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|left
operator|-=
name|now
operator|-
name|start
expr_stmt|;
if|if
condition|(
name|left
operator|<=
literal|0
condition|)
block|{
name|moveSpinner
argument_list|()
expr_stmt|;
name|left
operator|=
name|maxIntervalNanos
expr_stmt|;
block|}
name|sendUpdate
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|done
operator|&&
name|workerFuture
operator|.
name|isDone
argument_list|()
condition|)
block|{
comment|// The worker may not have called end() explicitly, which is likely a
comment|// programming error.
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"MultiProgressMonitor worker did not call end() before returning"
argument_list|)
expr_stmt|;
name|end
argument_list|()
expr_stmt|;
block|}
block|}
name|sendDone
argument_list|()
expr_stmt|;
block|}
comment|// The loop exits as soon as the worker calls end(), but we give it another
comment|// maxInterval to finish up and return.
try|try
block|{
name|workerFuture
operator|.
name|get
argument_list|(
name|maxIntervalNanos
argument_list|,
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|CancellationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|detailMessage
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|workerFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ExecutionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|wakeUp ()
specifier|private
specifier|synchronized
name|void
name|wakeUp
parameter_list|()
block|{
name|notifyAll
argument_list|()
expr_stmt|;
block|}
comment|/**    * Begin a sub-task.    *    * @param subTask sub-task name.    * @param subTaskWork total work units in sub-task, or {@link #UNKNOWN}.    * @return sub-task handle.    */
DECL|method|beginSubTask (String subTask, int subTaskWork)
specifier|public
name|Task
name|beginSubTask
parameter_list|(
name|String
name|subTask
parameter_list|,
name|int
name|subTaskWork
parameter_list|)
block|{
name|Task
name|task
init|=
operator|new
name|Task
argument_list|(
name|subTask
argument_list|,
name|subTaskWork
argument_list|)
decl_stmt|;
name|tasks
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
return|return
name|task
return|;
block|}
comment|/**    * End the overall task.    *    *<p>Must be called from a worker thread.    */
DECL|method|end ()
specifier|public
specifier|synchronized
name|void
name|end
parameter_list|()
block|{
name|done
operator|=
literal|true
expr_stmt|;
name|wakeUp
argument_list|()
expr_stmt|;
block|}
DECL|method|sendDone ()
specifier|private
name|void
name|sendDone
parameter_list|()
block|{
name|spinnerState
operator|=
name|NO_SPINNER
expr_stmt|;
name|StringBuilder
name|s
init|=
name|format
argument_list|()
decl_stmt|;
name|boolean
name|any
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Task
name|t
range|:
name|tasks
control|)
block|{
if|if
condition|(
name|t
operator|.
name|count
operator|!=
literal|0
condition|)
block|{
name|any
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|any
condition|)
block|{
name|s
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|s
operator|.
name|append
argument_list|(
literal|" done    \n"
argument_list|)
expr_stmt|;
name|send
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|moveSpinner ()
specifier|private
name|void
name|moveSpinner
parameter_list|()
block|{
name|spinnerIndex
operator|=
operator|(
name|spinnerIndex
operator|+
literal|1
operator|)
operator|%
name|SPINNER_STATES
operator|.
name|length
expr_stmt|;
name|spinnerState
operator|=
name|SPINNER_STATES
index|[
name|spinnerIndex
index|]
expr_stmt|;
block|}
DECL|method|sendUpdate ()
specifier|private
name|void
name|sendUpdate
parameter_list|()
block|{
name|send
argument_list|(
name|format
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|format ()
specifier|private
name|StringBuilder
name|format
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
literal|"\r"
argument_list|)
operator|.
name|append
argument_list|(
name|taskName
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tasks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Task
name|t
range|:
name|tasks
control|)
block|{
name|int
name|count
init|=
name|t
operator|.
name|getCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|first
condition|)
block|{
name|s
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|s
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|t
operator|.
name|name
argument_list|)
condition|)
block|{
name|s
operator|.
name|append
argument_list|(
name|t
operator|.
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|t
operator|.
name|total
operator|==
name|UNKNOWN
condition|)
block|{
name|s
operator|.
name|append
argument_list|(
name|count
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|s
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%d%% (%d/%d)"
argument_list|,
name|count
operator|*
literal|100
operator|/
name|t
operator|.
name|total
argument_list|,
name|count
argument_list|,
name|t
operator|.
name|total
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|spinnerState
operator|!=
name|NO_SPINNER
condition|)
block|{
comment|// Don't output a spinner until the alarm fires for the first time.
name|s
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
operator|.
name|append
argument_list|(
name|spinnerState
argument_list|)
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
return|return
name|s
return|;
block|}
DECL|method|send (StringBuilder s)
specifier|private
name|void
name|send
parameter_list|(
name|StringBuilder
name|s
parameter_list|)
block|{
if|if
condition|(
name|write
condition|)
block|{
try|try
block|{
name|out
operator|.
name|write
argument_list|(
name|Constants
operator|.
name|encode
argument_list|(
name|s
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Sending progress to client failed. Stop sending updates for task %s"
argument_list|,
name|taskName
argument_list|)
expr_stmt|;
name|write
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

