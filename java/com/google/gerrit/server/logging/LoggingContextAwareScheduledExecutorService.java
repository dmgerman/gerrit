begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
name|Callable
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
name|ScheduledExecutorService
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
comment|/**  * A {@link ScheduledExecutorService} that copies the {@link LoggingContext} on executing a {@link  * Runnable} to the executing thread.  */
end_comment

begin_class
DECL|class|LoggingContextAwareScheduledExecutorService
specifier|public
class|class
name|LoggingContextAwareScheduledExecutorService
extends|extends
name|LoggingContextAwareExecutorService
implements|implements
name|ScheduledExecutorService
block|{
DECL|field|scheduledExecutorService
specifier|private
specifier|final
name|ScheduledExecutorService
name|scheduledExecutorService
decl_stmt|;
DECL|method|LoggingContextAwareScheduledExecutorService ( ScheduledExecutorService scheduledExecutorService)
specifier|public
name|LoggingContextAwareScheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|scheduledExecutorService
parameter_list|)
block|{
name|super
argument_list|(
name|scheduledExecutorService
argument_list|)
expr_stmt|;
name|this
operator|.
name|scheduledExecutorService
operator|=
name|scheduledExecutorService
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|schedule (Runnable command, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|scheduledExecutorService
operator|.
name|schedule
argument_list|(
name|LoggingContext
operator|.
name|copy
argument_list|(
name|command
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|schedule (Callable<V> callable, long delay, TimeUnit unit)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|ScheduledFuture
argument_list|<
name|V
argument_list|>
name|schedule
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|callable
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|scheduledExecutorService
operator|.
name|schedule
argument_list|(
name|LoggingContext
operator|.
name|copy
argument_list|(
name|callable
argument_list|)
argument_list|,
name|delay
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleAtFixedRate ( Runnable command, long initialDelay, long period, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleAtFixedRate
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|initialDelay
parameter_list|,
name|long
name|period
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|scheduledExecutorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|LoggingContext
operator|.
name|copy
argument_list|(
name|command
argument_list|)
argument_list|,
name|initialDelay
argument_list|,
name|period
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|scheduleWithFixedDelay ( Runnable command, long initialDelay, long delay, TimeUnit unit)
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduleWithFixedDelay
parameter_list|(
name|Runnable
name|command
parameter_list|,
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
name|scheduledExecutorService
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|LoggingContext
operator|.
name|copy
argument_list|(
name|command
argument_list|)
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

