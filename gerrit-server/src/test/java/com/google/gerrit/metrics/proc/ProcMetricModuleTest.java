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
DECL|package|com.google.gerrit.metrics.proc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|proc
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|Counter
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
name|gerrit
operator|.
name|common
operator|.
name|Version
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|Description
operator|.
name|FieldOrdering
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
name|dropwizard
operator|.
name|DropWizardMetricMaker
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Guice
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Injector
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_class
DECL|class|ProcMetricModuleTest
specifier|public
class|class
name|ProcMetricModuleTest
block|{
DECL|field|exception
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|exception
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
DECL|field|metrics
annotation|@
name|Inject
name|MetricMaker
name|metrics
decl_stmt|;
DECL|field|registry
annotation|@
name|Inject
name|MetricRegistry
name|registry
decl_stmt|;
annotation|@
name|Test
DECL|method|constantBuildLabel ()
specifier|public
name|void
name|constantBuildLabel
parameter_list|()
block|{
name|Gauge
argument_list|<
name|String
argument_list|>
name|buildLabel
init|=
name|gauge
argument_list|(
literal|"build/label"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|buildLabel
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|Version
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|procUptime ()
specifier|public
name|void
name|procUptime
parameter_list|()
block|{
name|Gauge
argument_list|<
name|Long
argument_list|>
name|birth
init|=
name|gauge
argument_list|(
literal|"proc/birth_timestamp"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|birth
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|isAtMost
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMicros
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Gauge
argument_list|<
name|Long
argument_list|>
name|uptime
init|=
name|gauge
argument_list|(
literal|"proc/uptime"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|uptime
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|isAtLeast
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|counter0 ()
specifier|public
name|void
name|counter0
parameter_list|()
block|{
name|Counter0
name|cntr
init|=
name|metrics
operator|.
name|newCounter
argument_list|(
literal|"test/count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"simple test"
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|)
decl_stmt|;
name|Counter
name|raw
init|=
name|get
argument_list|(
literal|"test/count"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|raw
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|increment
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|raw
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|incrementBy
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|raw
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|6
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|counter1 ()
specifier|public
name|void
name|counter1
parameter_list|()
block|{
name|Counter1
argument_list|<
name|String
argument_list|>
name|cntr
init|=
name|metrics
operator|.
name|newCounter
argument_list|(
literal|"test/count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"simple test"
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|,
name|Field
operator|.
name|ofString
argument_list|(
literal|"action"
argument_list|)
argument_list|)
decl_stmt|;
name|Counter
name|total
init|=
name|get
argument_list|(
literal|"test/count_total"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|increment
argument_list|(
literal|"passed"
argument_list|)
expr_stmt|;
name|Counter
name|passed
init|=
name|get
argument_list|(
literal|"test/count/passed"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|passed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|incrementBy
argument_list|(
literal|"failed"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|Counter
name|failed
init|=
name|get
argument_list|(
literal|"test/count/failed"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|passed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|failed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|counterPrefixFields ()
specifier|public
name|void
name|counterPrefixFields
parameter_list|()
block|{
name|Counter1
argument_list|<
name|String
argument_list|>
name|cntr
init|=
name|metrics
operator|.
name|newCounter
argument_list|(
literal|"test/count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"simple test"
argument_list|)
operator|.
name|setCumulative
argument_list|()
operator|.
name|setFieldOrdering
argument_list|(
name|FieldOrdering
operator|.
name|PREFIX_FIELDS_BASENAME
argument_list|)
argument_list|,
name|Field
operator|.
name|ofString
argument_list|(
literal|"action"
argument_list|)
argument_list|)
decl_stmt|;
name|Counter
name|total
init|=
name|get
argument_list|(
literal|"test/count_total"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|increment
argument_list|(
literal|"passed"
argument_list|)
expr_stmt|;
name|Counter
name|passed
init|=
name|get
argument_list|(
literal|"test/passed/count"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|passed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|cntr
operator|.
name|incrementBy
argument_list|(
literal|"failed"
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|Counter
name|failed
init|=
name|get
argument_list|(
literal|"test/failed/count"
argument_list|,
name|Counter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|total
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|passed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|failed
operator|.
name|getCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|callbackMetric0 ()
specifier|public
name|void
name|callbackMetric0
parameter_list|()
block|{
name|CallbackMetric0
argument_list|<
name|Long
argument_list|>
name|cntr
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"test/count"
argument_list|,
name|Long
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"simple test"
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|)
decl_stmt|;
name|AtomicInteger
name|invocations
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|metrics
operator|.
name|newTrigger
argument_list|(
name|cntr
argument_list|,
parameter_list|()
lambda|->
block|{
name|invocations
operator|.
name|getAndIncrement
argument_list|()
expr_stmt|;
name|cntr
operator|.
name|set
argument_list|(
literal|42L
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// Triggers run immediately with DropWizard binding.
name|assertThat
argument_list|(
name|invocations
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Gauge
argument_list|<
name|Long
argument_list|>
name|raw
init|=
name|gauge
argument_list|(
literal|"test/count"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|raw
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|42
argument_list|)
expr_stmt|;
comment|// Triggers are debounced to avoid being fired too frequently.
name|assertThat
argument_list|(
name|invocations
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidName1 ()
specifier|public
name|void
name|invalidName1
parameter_list|()
block|{
name|exception
operator|.
name|expect
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|metrics
operator|.
name|newCounter
argument_list|(
literal|"invalid name"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"fail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|invalidName2 ()
specifier|public
name|void
name|invalidName2
parameter_list|()
block|{
name|exception
operator|.
name|expect
argument_list|(
name|IllegalArgumentException
operator|.
name|class
argument_list|)
expr_stmt|;
name|metrics
operator|.
name|newCounter
argument_list|(
literal|"invalid/ name"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"fail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"cast"
block|}
argument_list|)
DECL|method|gauge (String name)
specifier|private
parameter_list|<
name|V
parameter_list|>
name|Gauge
argument_list|<
name|V
argument_list|>
name|gauge
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|(
name|Gauge
argument_list|<
name|V
argument_list|>
operator|)
name|get
argument_list|(
name|name
argument_list|,
name|Gauge
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|get (String name, Class<M> type)
specifier|private
parameter_list|<
name|M
extends|extends
name|Metric
parameter_list|>
name|M
name|get
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|M
argument_list|>
name|type
parameter_list|)
block|{
name|Metric
name|m
init|=
name|registry
operator|.
name|getMetrics
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|m
argument_list|)
operator|.
name|named
argument_list|(
name|name
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|m
argument_list|)
operator|.
name|named
argument_list|(
name|name
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|type
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|M
name|result
init|=
operator|(
name|M
operator|)
name|m
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|Injector
name|injector
init|=
name|Guice
operator|.
name|createInjector
argument_list|(
operator|new
name|DropWizardMetricMaker
operator|.
name|ApiModule
argument_list|()
argument_list|)
decl_stmt|;
name|LifecycleManager
name|mgr
init|=
operator|new
name|LifecycleManager
argument_list|()
decl_stmt|;
name|mgr
operator|.
name|add
argument_list|(
name|injector
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|start
argument_list|()
expr_stmt|;
name|injector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

