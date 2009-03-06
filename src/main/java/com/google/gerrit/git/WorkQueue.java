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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
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

begin_class
DECL|class|WorkQueue
specifier|public
class|class
name|WorkQueue
block|{
DECL|field|pool
specifier|private
specifier|static
name|ScheduledThreadPoolExecutor
name|pool
decl_stmt|;
DECL|method|schedule (final Runnable task, final long delay, final TimeUnit unit)
specifier|public
specifier|static
specifier|synchronized
name|void
name|schedule
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|,
specifier|final
name|long
name|delay
parameter_list|,
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
if|if
condition|(
name|pool
operator|==
literal|null
condition|)
block|{
name|pool
operator|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pool
operator|.
name|setKeepAliveTime
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|pool
operator|.
name|setMaximumPoolSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
name|pool
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
DECL|method|terminate ()
specifier|public
specifier|static
name|void
name|terminate
parameter_list|()
block|{
specifier|final
name|ScheduledThreadPoolExecutor
name|p
init|=
name|shutdown
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|boolean
name|isTerminated
decl_stmt|;
do|do
block|{
try|try
block|{
name|isTerminated
operator|=
name|p
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
name|isTerminated
operator|=
literal|false
expr_stmt|;
block|}
block|}
do|while
condition|(
operator|!
name|isTerminated
condition|)
do|;
block|}
block|}
DECL|method|shutdown ()
specifier|private
specifier|static
specifier|synchronized
name|ScheduledThreadPoolExecutor
name|shutdown
parameter_list|()
block|{
specifier|final
name|ScheduledThreadPoolExecutor
name|p
init|=
name|pool
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|pool
operator|=
literal|null
expr_stmt|;
return|return
name|p
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

