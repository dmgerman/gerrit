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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
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
name|events
operator|.
name|LifecycleListener
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
name|metrics
operator|.
name|CallbackMetric
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
name|CallbackMetric0
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
name|Counter0
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
name|Counter1
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
name|Counter2
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
name|Counter3
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|Histogram0
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
name|Histogram1
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
name|Histogram2
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
name|Histogram3
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
name|MetricMaker
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
name|Timer0
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
name|Timer1
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
name|Timer2
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
name|Timer3
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_class
DECL|class|PluginMetricMaker
specifier|public
class|class
name|PluginMetricMaker
extends|extends
name|MetricMaker
implements|implements
name|LifecycleListener
block|{
DECL|field|root
specifier|private
specifier|final
name|MetricMaker
name|root
decl_stmt|;
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
DECL|field|cleanup
specifier|private
specifier|final
name|Set
argument_list|<
name|RegistrationHandle
argument_list|>
name|cleanup
decl_stmt|;
DECL|method|PluginMetricMaker (MetricMaker root, String prefix)
specifier|public
name|PluginMetricMaker
parameter_list|(
name|MetricMaker
name|root
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
name|this
operator|.
name|prefix
operator|=
name|prefix
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|prefix
else|:
name|prefix
operator|+
literal|"/"
expr_stmt|;
name|cleanup
operator|=
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newCounter (String name, Description desc)
specifier|public
name|Counter0
name|newCounter
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|)
block|{
name|Counter0
name|m
init|=
name|root
operator|.
name|newCounter
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newCounter (String name, Description desc, Field<F1> field1)
specifier|public
parameter_list|<
name|F1
parameter_list|>
name|Counter1
argument_list|<
name|F1
argument_list|>
name|newCounter
parameter_list|(
name|String
name|name
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
name|Counter1
argument_list|<
name|F1
argument_list|>
name|m
init|=
name|root
operator|.
name|newCounter
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newCounter ( String name, Description desc, Field<F1> field1, Field<F2> field2)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|>
name|Counter2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|newCounter
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|)
block|{
name|Counter2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|m
init|=
name|root
operator|.
name|newCounter
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newCounter ( String name, Description desc, Field<F1> field1, Field<F2> field2, Field<F3> field3)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|,
name|F3
parameter_list|>
name|Counter3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|newCounter
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|,
name|Field
argument_list|<
name|F3
argument_list|>
name|field3
parameter_list|)
block|{
name|Counter3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|m
init|=
name|root
operator|.
name|newCounter
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|,
name|field3
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newTimer (String name, Description desc)
specifier|public
name|Timer0
name|newTimer
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|)
block|{
name|Timer0
name|m
init|=
name|root
operator|.
name|newTimer
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newTimer (String name, Description desc, Field<F1> field1)
specifier|public
parameter_list|<
name|F1
parameter_list|>
name|Timer1
argument_list|<
name|F1
argument_list|>
name|newTimer
parameter_list|(
name|String
name|name
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
name|Timer1
argument_list|<
name|F1
argument_list|>
name|m
init|=
name|root
operator|.
name|newTimer
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newTimer ( String name, Description desc, Field<F1> field1, Field<F2> field2)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|>
name|Timer2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|newTimer
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|)
block|{
name|Timer2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|m
init|=
name|root
operator|.
name|newTimer
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newTimer ( String name, Description desc, Field<F1> field1, Field<F2> field2, Field<F3> field3)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|,
name|F3
parameter_list|>
name|Timer3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|newTimer
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|,
name|Field
argument_list|<
name|F3
argument_list|>
name|field3
parameter_list|)
block|{
name|Timer3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|m
init|=
name|root
operator|.
name|newTimer
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|,
name|field3
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newHistogram (String name, Description desc)
specifier|public
name|Histogram0
name|newHistogram
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|)
block|{
name|Histogram0
name|m
init|=
name|root
operator|.
name|newHistogram
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newHistogram (String name, Description desc, Field<F1> field1)
specifier|public
parameter_list|<
name|F1
parameter_list|>
name|Histogram1
argument_list|<
name|F1
argument_list|>
name|newHistogram
parameter_list|(
name|String
name|name
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
name|Histogram1
argument_list|<
name|F1
argument_list|>
name|m
init|=
name|root
operator|.
name|newHistogram
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newHistogram ( String name, Description desc, Field<F1> field1, Field<F2> field2)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|>
name|Histogram2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|newHistogram
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|)
block|{
name|Histogram2
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|>
name|m
init|=
name|root
operator|.
name|newHistogram
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newHistogram ( String name, Description desc, Field<F1> field1, Field<F2> field2, Field<F3> field3)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|F2
parameter_list|,
name|F3
parameter_list|>
name|Histogram3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|newHistogram
parameter_list|(
name|String
name|name
parameter_list|,
name|Description
name|desc
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|,
name|Field
argument_list|<
name|F2
argument_list|>
name|field2
parameter_list|,
name|Field
argument_list|<
name|F3
argument_list|>
name|field3
parameter_list|)
block|{
name|Histogram3
argument_list|<
name|F1
argument_list|,
name|F2
argument_list|,
name|F3
argument_list|>
name|m
init|=
name|root
operator|.
name|newHistogram
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|,
name|field2
argument_list|,
name|field3
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newCallbackMetric ( String name, Class<V> valueClass, Description desc)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|CallbackMetric0
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
block|{
name|CallbackMetric0
argument_list|<
name|V
argument_list|>
name|m
init|=
name|root
operator|.
name|newCallbackMetric
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|valueClass
argument_list|,
name|desc
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newCallbackMetric ( String name, Class<V> valueClass, Description desc, Field<F1> field1)
specifier|public
parameter_list|<
name|F1
parameter_list|,
name|V
parameter_list|>
name|CallbackMetric1
argument_list|<
name|F1
argument_list|,
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
parameter_list|,
name|Field
argument_list|<
name|F1
argument_list|>
name|field1
parameter_list|)
block|{
name|CallbackMetric1
argument_list|<
name|F1
argument_list|,
name|V
argument_list|>
name|m
init|=
name|root
operator|.
name|newCallbackMetric
argument_list|(
name|prefix
operator|+
name|name
argument_list|,
name|valueClass
argument_list|,
name|desc
argument_list|,
name|field1
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
annotation|@
name|Override
DECL|method|newTrigger (Set<CallbackMetric<?>> metrics, Runnable trigger)
specifier|public
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
block|{
specifier|final
name|RegistrationHandle
name|handle
init|=
name|root
operator|.
name|newTrigger
argument_list|(
name|metrics
argument_list|,
name|trigger
argument_list|)
decl_stmt|;
name|cleanup
operator|.
name|add
argument_list|(
name|handle
argument_list|)
expr_stmt|;
return|return
parameter_list|()
lambda|->
block|{
name|handle
operator|.
name|remove
argument_list|()
expr_stmt|;
name|cleanup
operator|.
name|remove
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
synchronized|synchronized
init|(
name|cleanup
init|)
block|{
name|Iterator
argument_list|<
name|RegistrationHandle
argument_list|>
name|itr
init|=
name|cleanup
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|itr
operator|.
name|next
argument_list|()
operator|.
name|remove
argument_list|()
expr_stmt|;
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

