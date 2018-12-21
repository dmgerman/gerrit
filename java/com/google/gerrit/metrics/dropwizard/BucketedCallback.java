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
DECL|package|com.google.gerrit.metrics.dropwizard
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|dropwizard
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Gauge
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Metric
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|ImmutableList
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
name|Maps
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
name|metrics
operator|.
name|Description
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
name|metrics
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/** Abstract callback metric broken down into buckets. */
end_comment

begin_class
DECL|class|BucketedCallback
specifier|abstract
class|class
name|BucketedCallback
parameter_list|<
name|V
parameter_list|>
implements|implements
name|BucketedMetric
block|{
DECL|field|metrics
specifier|private
specifier|final
name|DropWizardMetricMaker
name|metrics
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|MetricRegistry
name|registry
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|ordering
specifier|private
specifier|final
name|Description
operator|.
name|FieldOrdering
name|ordering
decl_stmt|;
DECL|field|fields
specifier|protected
specifier|final
name|Field
argument_list|<
name|?
argument_list|>
index|[]
name|fields
decl_stmt|;
DECL|field|zero
specifier|private
specifier|final
name|V
name|zero
decl_stmt|;
DECL|field|cells
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|ValueGauge
argument_list|>
name|cells
decl_stmt|;
DECL|field|trigger
specifier|protected
specifier|volatile
name|Runnable
name|trigger
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|method|BucketedCallback ( DropWizardMetricMaker metrics, MetricRegistry registry, String name, Class<V> valueType, Description desc, Field<?>... fields)
name|BucketedCallback
parameter_list|(
name|DropWizardMetricMaker
name|metrics
parameter_list|,
name|MetricRegistry
name|registry
parameter_list|,
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valueType
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|?
argument_list|>
modifier|...
name|fields
parameter_list|)
block|{
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|ordering
operator|=
name|desc
operator|.
name|getFieldOrdering
argument_list|()
expr_stmt|;
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
name|this
operator|.
name|zero
operator|=
name|CallbackMetricImpl0
operator|.
name|zeroFor
argument_list|(
name|valueType
argument_list|)
expr_stmt|;
name|this
operator|.
name|cells
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|doRemove ()
name|void
name|doRemove
parameter_list|()
block|{
for|for
control|(
name|Object
name|key
range|:
name|cells
operator|.
name|keySet
argument_list|()
control|)
block|{
name|registry
operator|.
name|remove
argument_list|(
name|submetric
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|metrics
operator|.
name|remove
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|doBeginSet ()
name|void
name|doBeginSet
parameter_list|()
block|{
for|for
control|(
name|ValueGauge
name|g
range|:
name|cells
operator|.
name|values
argument_list|()
control|)
block|{
name|g
operator|.
name|set
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|doPrune ()
name|void
name|doPrune
parameter_list|()
block|{
name|cells
operator|.
name|entrySet
argument_list|()
operator|.
name|removeIf
argument_list|(
name|objectValueGaugeEntry
lambda|->
operator|!
name|objectValueGaugeEntry
operator|.
name|getValue
argument_list|()
operator|.
name|set
argument_list|)
expr_stmt|;
block|}
DECL|method|doEndSet ()
name|void
name|doEndSet
parameter_list|()
block|{
for|for
control|(
name|ValueGauge
name|g
range|:
name|cells
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|g
operator|.
name|set
condition|)
block|{
name|g
operator|.
name|value
operator|=
name|zero
expr_stmt|;
block|}
block|}
block|}
DECL|method|getOrCreate (Object f1, Object f2)
name|ValueGauge
name|getOrCreate
parameter_list|(
name|Object
name|f1
parameter_list|,
name|Object
name|f2
parameter_list|)
block|{
return|return
name|getOrCreate
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|f1
argument_list|,
name|f2
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getOrCreate (Object f1, Object f2, Object f3)
name|ValueGauge
name|getOrCreate
parameter_list|(
name|Object
name|f1
parameter_list|,
name|Object
name|f2
parameter_list|,
name|Object
name|f3
parameter_list|)
block|{
return|return
name|getOrCreate
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|f1
argument_list|,
name|f2
argument_list|,
name|f3
argument_list|)
argument_list|)
return|;
block|}
DECL|method|getOrCreate (Object key)
name|ValueGauge
name|getOrCreate
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|ValueGauge
name|c
init|=
name|cells
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
return|return
name|c
return|;
block|}
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|c
operator|=
name|cells
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
name|c
operator|=
operator|new
name|ValueGauge
argument_list|()
expr_stmt|;
name|registry
operator|.
name|register
argument_list|(
name|submetric
argument_list|(
name|key
argument_list|)
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|cells
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
block|}
DECL|method|submetric (Object key)
specifier|private
name|String
name|submetric
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|DropWizardMetricMaker
operator|.
name|name
argument_list|(
name|ordering
argument_list|,
name|name
argument_list|,
name|name
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
DECL|method|name (Object key)
specifier|abstract
name|String
name|name
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|getTotal ()
specifier|public
name|Metric
name|getTotal
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getFields ()
specifier|public
name|Field
argument_list|<
name|?
argument_list|>
index|[]
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
annotation|@
name|Override
DECL|method|getCells ()
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Metric
argument_list|>
name|getCells
parameter_list|()
block|{
return|return
name|Maps
operator|.
name|transformValues
argument_list|(
name|cells
argument_list|,
name|in
lambda|->
name|in
argument_list|)
return|;
block|}
DECL|class|ValueGauge
specifier|final
class|class
name|ValueGauge
implements|implements
name|Gauge
argument_list|<
name|V
argument_list|>
block|{
DECL|field|value
specifier|volatile
name|V
name|value
init|=
name|zero
decl_stmt|;
DECL|field|set
name|boolean
name|set
decl_stmt|;
annotation|@
name|Override
DECL|method|getValue ()
specifier|public
name|V
name|getValue
parameter_list|()
block|{
name|Runnable
name|t
init|=
name|trigger
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|t
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
return|return
name|value
return|;
block|}
block|}
block|}
end_class

end_unit

