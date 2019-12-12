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
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
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

begin_comment
comment|/**  * Wrapper class so a Runnable can schedule itself onto the Gerrit Workqueue. Subclasses must  * implement the {@code run} method.  */
end_comment

begin_class
DECL|class|DefaultQueueOp
specifier|public
specifier|abstract
class|class
name|DefaultQueueOp
implements|implements
name|Runnable
block|{
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|method|DefaultQueueOp (WorkQueue wq)
specifier|protected
name|DefaultQueueOp
parameter_list|(
name|WorkQueue
name|wq
parameter_list|)
block|{
name|workQueue
operator|=
name|wq
expr_stmt|;
block|}
DECL|method|start (long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|start
parameter_list|(
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
DECL|method|startWithFixedDelay (long initialDelay, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|startWithFixedDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|this
argument_list|,
name|initialDelay
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
block|}
end_class

end_unit

