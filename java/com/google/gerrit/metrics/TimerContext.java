begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.metrics
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
package|;
end_package

begin_class
DECL|class|TimerContext
specifier|abstract
class|class
name|TimerContext
implements|implements
name|AutoCloseable
block|{
DECL|field|startNanos
specifier|private
specifier|final
name|long
name|startNanos
decl_stmt|;
DECL|field|stopped
specifier|private
name|boolean
name|stopped
decl_stmt|;
DECL|method|TimerContext ()
name|TimerContext
parameter_list|()
block|{
name|this
operator|.
name|startNanos
operator|=
name|System
operator|.
name|nanoTime
argument_list|()
expr_stmt|;
block|}
comment|/**    * Record the elapsed time to the timer.    *    * @param elapsed Elapsed time in nanoseconds.    */
DECL|method|record (long elapsed)
specifier|public
specifier|abstract
name|void
name|record
parameter_list|(
name|long
name|elapsed
parameter_list|)
function_decl|;
comment|/** @return the start time in system time nanoseconds. */
DECL|method|getStartTime ()
specifier|public
name|long
name|getStartTime
parameter_list|()
block|{
return|return
name|startNanos
return|;
block|}
comment|/**    * Stop the timer and record the elapsed time.    *    * @return the elapsed time in nanoseconds.    * @throws IllegalStateException if the timer is already stopped.    */
DECL|method|stop ()
specifier|public
name|long
name|stop
parameter_list|()
block|{
if|if
condition|(
operator|!
name|stopped
condition|)
block|{
name|stopped
operator|=
literal|true
expr_stmt|;
name|long
name|elapsed
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startNanos
decl_stmt|;
name|record
argument_list|(
name|elapsed
argument_list|)
expr_stmt|;
return|return
name|elapsed
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Already stopped"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
operator|!
name|stopped
condition|)
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

