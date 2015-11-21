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
comment|/**  * Metric whose value is supplied when the trigger is invoked.  *  *<pre>  *   CallbackMetric0&lt;Long&gt; hits = metricMaker.newCallbackMetric("hits", ...);  *   CallbackMetric0&lt;Long&gt; total = metricMaker.newCallbackMetric("total", ...);  *   metricMaker.newTrigger(hits, total, new Runnable() {  *     public void run() {  *       hits.set(1);  *       total.set(5);  *     }  *   });  *</pre>  *  * @param<V> type of the metric value, typically Integer or Long.  */
end_comment

begin_class
DECL|class|CallbackMetric0
specifier|public
specifier|abstract
class|class
name|CallbackMetric0
parameter_list|<
name|V
parameter_list|>
implements|implements
name|CallbackMetric
argument_list|<
name|V
argument_list|>
block|{
comment|/**    * Supply the current value of the metric.    *    * @param value current value.    */
DECL|method|set (V value)
specifier|public
specifier|abstract
name|void
name|set
parameter_list|(
name|V
name|value
parameter_list|)
function_decl|;
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

