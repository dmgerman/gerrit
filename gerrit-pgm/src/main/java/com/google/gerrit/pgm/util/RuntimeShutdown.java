begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_class
DECL|class|RuntimeShutdown
specifier|public
class|class
name|RuntimeShutdown
block|{
DECL|field|cb
specifier|private
specifier|static
specifier|final
name|ShutdownCallback
name|cb
init|=
operator|new
name|ShutdownCallback
argument_list|()
decl_stmt|;
comment|/** Add a task to be performed when graceful shutdown is requested. */
DECL|method|add (final Runnable task)
specifier|public
specifier|static
name|void
name|add
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|)
block|{
if|if
condition|(
operator|!
name|cb
operator|.
name|add
argument_list|(
name|task
argument_list|)
condition|)
block|{
comment|// If the shutdown has already begun we cannot enqueue a new
comment|// task. Instead trigger the task in the caller, without any
comment|// of our locks held.
comment|//
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Wait for the JVM shutdown to occur. */
DECL|method|waitFor ()
specifier|public
specifier|static
name|void
name|waitFor
parameter_list|()
block|{
name|cb
operator|.
name|waitForShutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|RuntimeShutdown ()
specifier|private
name|RuntimeShutdown
parameter_list|()
block|{   }
DECL|class|ShutdownCallback
specifier|private
specifier|static
class|class
name|ShutdownCallback
extends|extends
name|Thread
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShutdownCallback
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tasks
specifier|private
specifier|final
name|List
argument_list|<
name|Runnable
argument_list|>
name|tasks
init|=
operator|new
name|ArrayList
argument_list|<
name|Runnable
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|shutdownStarted
specifier|private
name|boolean
name|shutdownStarted
decl_stmt|;
DECL|field|shutdownComplete
specifier|private
name|boolean
name|shutdownComplete
decl_stmt|;
DECL|method|ShutdownCallback ()
name|ShutdownCallback
parameter_list|()
block|{
name|setName
argument_list|(
literal|"ShutdownCallback"
argument_list|)
expr_stmt|;
block|}
DECL|method|add (final Runnable newTask)
name|boolean
name|add
parameter_list|(
specifier|final
name|Runnable
name|newTask
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
operator|!
name|shutdownStarted
operator|&&
operator|!
name|shutdownComplete
condition|)
block|{
if|if
condition|(
name|tasks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|addShutdownHook
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|tasks
operator|.
name|add
argument_list|(
name|newTask
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
comment|// We don't permit adding a task once shutdown has started.
comment|//
return|return
literal|false
return|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Graceful shutdown requested"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Runnable
argument_list|>
name|taskList
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|shutdownStarted
operator|=
literal|true
expr_stmt|;
name|taskList
operator|=
name|tasks
expr_stmt|;
block|}
for|for
control|(
name|Runnable
name|task
range|:
name|taskList
control|)
block|{
try|try
block|{
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cleanup task failed"
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Shutdown complete"
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|shutdownComplete
operator|=
literal|true
expr_stmt|;
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|waitForShutdown ()
name|void
name|waitForShutdown
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
while|while
condition|(
operator|!
name|shutdownComplete
condition|)
block|{
try|try
block|{
name|wait
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Thread "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" interrupted while waiting for graceful shutdown;"
operator|+
literal|" ignoring interrupt request"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

