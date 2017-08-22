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
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|RegistrationHandle
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
comment|/**  * Records elapsed time for an operation or span.  *  *<p>Typical usage in a try-with-resources block:  *  *<pre>  * try (Timer0.Context ctx = timer.start()) {  * }  *</pre>  */
end_comment

begin_class
DECL|class|Timer0
specifier|public
specifier|abstract
class|class
name|Timer0
implements|implements
name|RegistrationHandle
block|{
DECL|class|Context
specifier|public
specifier|static
class|class
name|Context
extends|extends
name|TimerContext
block|{
DECL|field|timer
specifier|private
specifier|final
name|Timer0
name|timer
decl_stmt|;
DECL|method|Context (Timer0 timer)
name|Context
parameter_list|(
name|Timer0
name|timer
parameter_list|)
block|{
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|record (long elapsed)
specifier|public
name|void
name|record
parameter_list|(
name|long
name|elapsed
parameter_list|)
block|{
name|timer
operator|.
name|record
argument_list|(
name|elapsed
argument_list|,
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Begin a timer for the current block, value will be recorded when closed.    *    * @return timer context    */
DECL|method|start ()
specifier|public
name|Context
name|start
parameter_list|()
block|{
return|return
operator|new
name|Context
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**    * Record a value in the distribution.    *    * @param value value to record    * @param unit time unit of the value    */
DECL|method|record (long value, TimeUnit unit)
specifier|public
specifier|abstract
name|void
name|record
parameter_list|(
name|long
name|value
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
function_decl|;
block|}
end_class

end_unit

