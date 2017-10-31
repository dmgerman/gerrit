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

begin_comment
comment|/**  * Metric whose value increments during the life of the process.  *  *<p>Suitable uses are "total requests handled", "bytes sent", etc. Use {@link  * Description#setRate()} to suggest the monitoring system should also track the rate of increments  * if this is of interest.  *  *<p>For an instantaneous read of a value that can change over time (e.g. "memory in use") use a  * {@link CallbackMetric}.  *  * @param<F1> type of the field.  * @param<F2> type of the field.  * @param<F3> type of the field.  */
end_comment

begin_class
DECL|class|Counter3
specifier|public
specifier|abstract
class|class
name|Counter3
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|,
name|F3
parameter_list|>
implements|implements
name|RegistrationHandle
block|{
comment|/**    * Increment the counter by one event.    *    * @param field1 bucket to increment.    * @param field2 bucket to increment.    * @param field3 bucket to increment.    */
DECL|method|increment (F1 field1, F2 field2, F3 field3)
specifier|public
name|void
name|increment
parameter_list|(
name|F1
name|field1
parameter_list|,
name|F2
name|field2
parameter_list|,
name|F3
name|field3
parameter_list|)
block|{
name|incrementBy
argument_list|(
name|field1
argument_list|,
name|field2
argument_list|,
name|field3
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**    * Increment the counter by a specified amount.    *    * @param field1 bucket to increment.    * @param field2 bucket to increment.    * @param field3 bucket to increment.    * @param value value to increment by, must be&gt;= 0.    */
DECL|method|incrementBy (F1 field1, F2 field2, F3 field3, long value)
specifier|public
specifier|abstract
name|void
name|incrementBy
parameter_list|(
name|F1
name|field1
parameter_list|,
name|F2
name|field2
parameter_list|,
name|F3
name|field3
parameter_list|,
name|long
name|value
parameter_list|)
function_decl|;
block|}
end_class

end_unit

