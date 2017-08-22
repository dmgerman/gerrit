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

begin_comment
comment|/**  * Metric whose value is supplied when the trigger is invoked.  *  * @param<F1> type of the field.  * @param<V> type of the metric value, typically Integer or Long.  */
end_comment

begin_class
DECL|class|CallbackMetric1
specifier|public
specifier|abstract
class|class
name|CallbackMetric1
parameter_list|<
name|F1
parameter_list|,
name|V
parameter_list|>
implements|implements
name|CallbackMetric
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Supply the current value of the metric.    *    * @param field1 bucket to increment.    * @param value current value.    */
DECL|method|set (F1 field1, V value)
specifier|public
specifier|abstract
name|void
name|set
parameter_list|(
name|F1
name|field1
parameter_list|,
name|V
name|value
parameter_list|)
function_decl|;
comment|/**    * Ensure a zeroed metric is created for the field value.    *    * @param field1 bucket to create.    */
DECL|method|forceCreate (F1 field1)
specifier|public
specifier|abstract
name|void
name|forceCreate
parameter_list|(
name|F1
name|field1
parameter_list|)
function_decl|;
comment|/** Prune any submetrics that were not assigned during this trigger. */
annotation|@
name|Override
DECL|method|prune ()
specifier|public
name|void
name|prune
parameter_list|()
block|{}
block|}
end_class

end_unit

