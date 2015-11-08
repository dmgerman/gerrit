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
name|common
operator|.
name|base
operator|.
name|Supplier
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
name|collect
operator|.
name|ImmutableSet
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
name|Set
import|;
end_import

begin_comment
comment|/** Factory to create metrics for monitoring. */
end_comment

begin_class
DECL|class|MetricMaker
specifier|public
specifier|abstract
class|class
name|MetricMaker
block|{
comment|/** Metric whose value increments during the life of the process. */
DECL|method|newCounter (String name, Description desc)
specifier|public
specifier|abstract
name|Counter
name|newCounter
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|)
function_decl|;
comment|/** Metric recording time spent on an operation. */
DECL|method|newTimer (String name, Description desc)
specifier|public
specifier|abstract
name|Timer
name|newTimer
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|)
function_decl|;
comment|/**    * Instantaneous reading of a value.    *    *<pre>    * metricMaker.newCallbackMetric(&quot;memory&quot;,    *     new Description(&quot;Total bytes of memory used&quot;)    *        .setGauge()    *        .setUnit(Units.BYTES),    *     new Supplier&lt;Long&gt;() {    *       public Long get() {    *         return Runtime.getRuntime().totalMemory();    *       }    *     });    *</pre>    *    * @param name unique name of the metric.    * @param valueClass type of value recorded by the metric.    * @param desc description of the metric.    * @param trigger function to compute the value of the metric.    */
DECL|method|newCallbackMetric (String name, Class<V> valueClass, Description desc, final Supplier<V> trigger)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|void
name|newCallbackMetric
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueClass
parameter_list|,
name|Description
name|desc
parameter_list|,
specifier|final
name|Supplier
argument_list|<
name|V
argument_list|>
name|trigger
parameter_list|)
block|{
specifier|final
name|CallbackMetric
argument_list|<
name|V
argument_list|>
name|metric
init|=
name|newCallbackMetric
argument_list|(
name|name
argument_list|,
name|valueClass
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|newTrigger
argument_list|(
name|metric
argument_list|,
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|metric
operator|.
name|set
argument_list|(
name|trigger
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/** Instantaneous reading of a particular value. */
DECL|method|newCallbackMetric (String name, Class<V> valueClass, Description desc)
specifier|public
specifier|abstract
parameter_list|<
name|V
parameter_list|>
name|CallbackMetric
argument_list|<
name|V
argument_list|>
name|newCallbackMetric
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueClass
parameter_list|,
name|Description
name|desc
parameter_list|)
function_decl|;
comment|/** Connect logic to populate a previously created {@link CallbackMetric}. */
DECL|method|newTrigger (CallbackMetric<?> metric1, Runnable trigger)
specifier|public
name|RegistrationHandle
name|newTrigger
parameter_list|(
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric1
parameter_list|,
name|Runnable
name|trigger
parameter_list|)
block|{
return|return
name|newTrigger
argument_list|(
name|ImmutableSet
operator|.
expr|<
name|CallbackMetric
argument_list|<
name|?
argument_list|>
operator|>
name|of
argument_list|(
name|metric1
argument_list|)
operator|,
name|trigger
argument_list|)
return|;
block|}
DECL|method|newTrigger (CallbackMetric<?> metric1, CallbackMetric<?> metric2, Runnable trigger)
specifier|public
name|RegistrationHandle
name|newTrigger
parameter_list|(
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric1
parameter_list|,
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric2
parameter_list|,
name|Runnable
name|trigger
parameter_list|)
block|{
return|return
name|newTrigger
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|metric1
argument_list|,
name|metric2
argument_list|)
argument_list|,
name|trigger
argument_list|)
return|;
block|}
DECL|method|newTrigger (CallbackMetric<?> metric1, CallbackMetric<?> metric2, CallbackMetric<?> metric3, Runnable trigger)
specifier|public
name|RegistrationHandle
name|newTrigger
parameter_list|(
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric1
parameter_list|,
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric2
parameter_list|,
name|CallbackMetric
argument_list|<
name|?
argument_list|>
name|metric3
parameter_list|,
name|Runnable
name|trigger
parameter_list|)
block|{
return|return
name|newTrigger
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|metric1
argument_list|,
name|metric2
argument_list|,
name|metric3
argument_list|)
argument_list|,
name|trigger
argument_list|)
return|;
block|}
DECL|method|newTrigger (Set<CallbackMetric<?>> metrics, Runnable trigger)
specifier|public
specifier|abstract
name|RegistrationHandle
name|newTrigger
parameter_list|(
name|Set
argument_list|<
name|CallbackMetric
argument_list|<
name|?
argument_list|>
argument_list|>
name|metrics
parameter_list|,
name|Runnable
name|trigger
parameter_list|)
function_decl|;
block|}
end_class

end_unit

