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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
operator|.
name|LoggingContext
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
name|server
operator|.
name|logging
operator|.
name|PerformanceLogRecord
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
comment|/**  * Records elapsed time for an operation or span.  *  *<p>Typical usage in a try-with-resources block:  *  *<pre>  * try (Timer1.Context ctx = timer.start(field)) {  * }  *</pre>  *  * @param<F1> type of the field.  */
end_comment

begin_class
DECL|class|Timer1
specifier|public
specifier|abstract
class|class
name|Timer1
parameter_list|<
name|F1
parameter_list|>
implements|implements
name|RegistrationHandle
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
DECL|class|Context
specifier|public
specifier|static
class|class
name|Context
parameter_list|<
name|F1
parameter_list|>
extends|extends
name|TimerContext
block|{
DECL|field|timer
specifier|private
specifier|final
name|Timer1
argument_list|<
name|F1
argument_list|>
name|timer
decl_stmt|;
DECL|field|fieldValue
specifier|private
specifier|final
name|F1
name|fieldValue
decl_stmt|;
DECL|method|Context (Timer1<F1> timer, F1 fieldValue)
name|Context
parameter_list|(
name|Timer1
argument_list|<
name|F1
argument_list|>
name|timer
parameter_list|,
name|F1
name|fieldValue
parameter_list|)
block|{
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
name|this
operator|.
name|fieldValue
operator|=
name|fieldValue
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
name|fieldValue
argument_list|,
name|elapsed
argument_list|,
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|name
specifier|protected
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|field
specifier|protected
specifier|final
name|Field
argument_list|<
name|F1
argument_list|>
name|field
decl_stmt|;
DECL|method|Timer1 (String name, Field<F1> field)
specifier|public
name|Timer1
parameter_list|(
name|String
name|name
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
block|}
comment|/**    * Begin a timer for the current block, value will be recorded when closed.    *    * @param fieldValue bucket to record the timer    * @return timer context    */
DECL|method|start (F1 fieldValue)
specifier|public
name|Context
argument_list|<
name|F1
argument_list|>
name|start
parameter_list|(
name|F1
name|fieldValue
parameter_list|)
block|{
return|return
operator|new
name|Context
argument_list|<>
argument_list|(
name|this
argument_list|,
name|fieldValue
argument_list|)
return|;
block|}
comment|/**    * Record a value in the distribution.    *    * @param fieldValue bucket to record the timer    * @param value value to record    * @param unit time unit of the value    */
DECL|method|record (F1 fieldValue, long value, TimeUnit unit)
specifier|public
specifier|final
name|void
name|record
parameter_list|(
name|F1
name|fieldValue
parameter_list|,
name|long
name|value
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|long
name|durationMs
init|=
name|unit
operator|.
name|toMillis
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addPerformanceLogRecord
argument_list|(
parameter_list|()
lambda|->
name|PerformanceLogRecord
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|durationMs
argument_list|,
name|field
operator|.
name|name
argument_list|()
argument_list|,
name|fieldValue
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|atFinest
argument_list|()
operator|.
name|log
argument_list|(
literal|"%s (%s = %s) took %dms"
argument_list|,
name|name
argument_list|,
name|field
operator|.
name|name
argument_list|()
argument_list|,
name|fieldValue
argument_list|,
name|durationMs
argument_list|)
expr_stmt|;
name|doRecord
argument_list|(
name|fieldValue
argument_list|,
name|value
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
comment|/**    * Record a value in the distribution.    *    * @param fieldValue bucket to record the timer    * @param value value to record    * @param unit time unit of the value    */
DECL|method|doRecord (F1 fieldValue, long value, TimeUnit unit)
specifier|protected
specifier|abstract
name|void
name|doRecord
parameter_list|(
name|F1
name|fieldValue
parameter_list|,
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

