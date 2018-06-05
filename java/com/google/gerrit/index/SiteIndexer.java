begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Stopwatch
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
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
name|io
operator|.
name|PrintWriter
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
name|RejectedExecutionException
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
name|atomic
operator|.
name|AtomicBoolean
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|io
operator|.
name|NullOutputStream
import|;
end_import

begin_class
DECL|class|SiteIndexer
specifier|public
specifier|abstract
class|class
name|SiteIndexer
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|,
name|I
extends|extends
name|Index
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
parameter_list|>
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
DECL|class|Result
specifier|public
specifier|static
class|class
name|Result
block|{
DECL|field|elapsedNanos
specifier|private
specifier|final
name|long
name|elapsedNanos
decl_stmt|;
DECL|field|success
specifier|private
specifier|final
name|boolean
name|success
decl_stmt|;
DECL|field|done
specifier|private
specifier|final
name|int
name|done
decl_stmt|;
DECL|field|failed
specifier|private
specifier|final
name|int
name|failed
decl_stmt|;
DECL|method|Result (Stopwatch sw, boolean success, int done, int failed)
specifier|public
name|Result
parameter_list|(
name|Stopwatch
name|sw
parameter_list|,
name|boolean
name|success
parameter_list|,
name|int
name|done
parameter_list|,
name|int
name|failed
parameter_list|)
block|{
name|this
operator|.
name|elapsedNanos
operator|=
name|sw
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
name|this
operator|.
name|success
operator|=
name|success
expr_stmt|;
name|this
operator|.
name|done
operator|=
name|done
expr_stmt|;
name|this
operator|.
name|failed
operator|=
name|failed
expr_stmt|;
block|}
DECL|method|success ()
specifier|public
name|boolean
name|success
parameter_list|()
block|{
return|return
name|success
return|;
block|}
DECL|method|doneCount ()
specifier|public
name|int
name|doneCount
parameter_list|()
block|{
return|return
name|done
return|;
block|}
DECL|method|failedCount ()
specifier|public
name|int
name|failedCount
parameter_list|()
block|{
return|return
name|failed
return|;
block|}
DECL|method|elapsed (TimeUnit timeUnit)
specifier|public
name|long
name|elapsed
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
return|return
name|timeUnit
operator|.
name|convert
argument_list|(
name|elapsedNanos
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
return|;
block|}
block|}
DECL|field|totalWork
specifier|protected
name|int
name|totalWork
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|progressOut
specifier|protected
name|OutputStream
name|progressOut
init|=
name|NullOutputStream
operator|.
name|INSTANCE
decl_stmt|;
DECL|field|verboseWriter
specifier|protected
name|PrintWriter
name|verboseWriter
init|=
operator|new
name|PrintWriter
argument_list|(
name|NullOutputStream
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
DECL|method|setTotalWork (int num)
specifier|public
name|void
name|setTotalWork
parameter_list|(
name|int
name|num
parameter_list|)
block|{
name|totalWork
operator|=
name|num
expr_stmt|;
block|}
DECL|method|setProgressOut (OutputStream out)
specifier|public
name|void
name|setProgressOut
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|progressOut
operator|=
name|checkNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|setVerboseOut (OutputStream out)
specifier|public
name|void
name|setVerboseOut
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
name|verboseWriter
operator|=
operator|new
name|PrintWriter
argument_list|(
name|checkNotNull
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|indexAll (I index)
specifier|public
specifier|abstract
name|Result
name|indexAll
parameter_list|(
name|I
name|index
parameter_list|)
function_decl|;
DECL|method|addErrorListener ( ListenableFuture<?> future, String desc, ProgressMonitor progress, AtomicBoolean ok)
specifier|protected
specifier|final
name|void
name|addErrorListener
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
parameter_list|,
name|String
name|desc
parameter_list|,
name|ProgressMonitor
name|progress
parameter_list|,
name|AtomicBoolean
name|ok
parameter_list|)
block|{
name|future
operator|.
name|addListener
argument_list|(
operator|new
name|ErrorListener
argument_list|(
name|future
argument_list|,
name|desc
argument_list|,
name|progress
argument_list|,
name|ok
argument_list|)
argument_list|,
name|MoreExecutors
operator|.
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ErrorListener
specifier|private
specifier|static
class|class
name|ErrorListener
implements|implements
name|Runnable
block|{
DECL|field|future
specifier|private
specifier|final
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|desc
specifier|private
specifier|final
name|String
name|desc
decl_stmt|;
DECL|field|progress
specifier|private
specifier|final
name|ProgressMonitor
name|progress
decl_stmt|;
DECL|field|ok
specifier|private
specifier|final
name|AtomicBoolean
name|ok
decl_stmt|;
DECL|method|ErrorListener ( ListenableFuture<?> future, String desc, ProgressMonitor progress, AtomicBoolean ok)
specifier|private
name|ErrorListener
parameter_list|(
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
parameter_list|,
name|String
name|desc
parameter_list|,
name|ProgressMonitor
name|progress
parameter_list|,
name|AtomicBoolean
name|ok
parameter_list|)
block|{
name|this
operator|.
name|future
operator|=
name|future
expr_stmt|;
name|this
operator|.
name|desc
operator|=
name|desc
expr_stmt|;
name|this
operator|.
name|progress
operator|=
name|progress
expr_stmt|;
name|this
operator|.
name|ok
operator|=
name|ok
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
comment|// Server shutdown, don't spam the logs.
name|failSilently
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|failAndThrow
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
comment|// Can't join with RuntimeException because "RuntimeException |
comment|// Error" becomes Throwable, which messes with signatures.
name|failAndThrow
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
synchronized|synchronized
init|(
name|progress
init|)
block|{
name|progress
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|failSilently ()
specifier|private
name|void
name|failSilently
parameter_list|()
block|{
name|ok
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|fail (Throwable t)
specifier|private
name|void
name|fail
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|t
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to index %s"
argument_list|,
name|desc
argument_list|)
expr_stmt|;
name|ok
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|failAndThrow (RuntimeException e)
specifier|private
name|void
name|failAndThrow
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
DECL|method|failAndThrow (Error e)
specifier|private
name|void
name|failAndThrow
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

