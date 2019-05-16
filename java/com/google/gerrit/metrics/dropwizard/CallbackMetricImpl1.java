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
name|MetricRegistry
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
name|CallbackMetric1
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
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/** Optimized version of {@link BucketedCallback} for single dimension. */
end_comment

begin_class
DECL|class|CallbackMetricImpl1
class|class
name|CallbackMetricImpl1
parameter_list|<
name|F1
parameter_list|,
name|V
parameter_list|>
extends|extends
name|BucketedCallback
argument_list|<
name|V
argument_list|>
block|{
DECL|method|CallbackMetricImpl1 ( DropWizardMetricMaker metrics, MetricRegistry registry, String name, Class<V> valueClass, Description desc, Field<F1> field1)
name|CallbackMetricImpl1
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
name|valueClass
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|)
block|{
name|super
argument_list|(
name|metrics
argument_list|,
name|registry
argument_list|,
name|name
argument_list|,
name|valueClass
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|)
expr_stmt|;
block|}
DECL|method|create ()
name|CallbackMetric1
argument_list|<
name|F1
argument_list|,
name|V
argument_list|>
name|create
parameter_list|()
block|{
return|return
operator|new
name|Impl1
argument_list|()
return|;
block|}
DECL|class|Impl1
specifier|private
specifier|final
class|class
name|Impl1
extends|extends
name|CallbackMetric1
argument_list|<
name|F1
argument_list|,
name|V
argument_list|>
implements|implements
name|CallbackMetricGlue
block|{
annotation|@
name|Override
DECL|method|beginSet ()
specifier|public
name|void
name|beginSet
parameter_list|()
block|{
name|doBeginSet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|set (F1 field1, V value)
specifier|public
name|void
name|set
parameter_list|(
name|F1
name|field1
parameter_list|,
name|V
name|value
parameter_list|)
block|{
name|BucketedCallback
argument_list|<
name|V
argument_list|>
operator|.
name|ValueGauge
name|cell
init|=
name|getOrCreate
argument_list|(
name|field1
argument_list|)
decl_stmt|;
name|cell
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|cell
operator|.
name|set
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|prune ()
specifier|public
name|void
name|prune
parameter_list|()
block|{
name|doPrune
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|endSet ()
specifier|public
name|void
name|endSet
parameter_list|()
block|{
name|doEndSet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|forceCreate (F1 field1)
specifier|public
name|void
name|forceCreate
parameter_list|(
name|F1
name|field1
parameter_list|)
block|{
name|getOrCreate
argument_list|(
name|field1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|register (Runnable t)
specifier|public
name|void
name|register
parameter_list|(
name|Runnable
name|t
parameter_list|)
block|{
name|trigger
operator|=
name|t
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|doRemove
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|name (Object field1)
name|String
name|name
parameter_list|(
name|Object
name|field1
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|fmt
init|=
operator|(
name|Function
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
operator|)
name|fields
index|[
literal|0
index|]
operator|.
name|formatter
argument_list|()
decl_stmt|;
return|return
name|fmt
operator|.
name|apply
argument_list|(
name|field1
argument_list|)
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'-'
argument_list|)
return|;
block|}
block|}
end_class

end_unit

