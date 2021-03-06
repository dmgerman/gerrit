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
name|Histogram
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
name|Meter
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
name|Snapshot
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
name|Timer
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
name|ImmutableMap
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TreeMap
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

begin_class
DECL|class|MetricJson
class|class
name|MetricJson
block|{
DECL|field|description
name|String
name|description
decl_stmt|;
DECL|field|unit
name|String
name|unit
decl_stmt|;
DECL|field|constant
name|Boolean
name|constant
decl_stmt|;
DECL|field|rate
name|Boolean
name|rate
decl_stmt|;
DECL|field|gauge
name|Boolean
name|gauge
decl_stmt|;
DECL|field|cumulative
name|Boolean
name|cumulative
decl_stmt|;
DECL|field|count
name|Long
name|count
decl_stmt|;
DECL|field|value
name|Object
name|value
decl_stmt|;
DECL|field|rate_1m
name|Double
name|rate_1m
decl_stmt|;
DECL|field|rate_5m
name|Double
name|rate_5m
decl_stmt|;
DECL|field|rate_15m
name|Double
name|rate_15m
decl_stmt|;
DECL|field|rate_mean
name|Double
name|rate_mean
decl_stmt|;
DECL|field|p50
name|Double
name|p50
decl_stmt|;
DECL|field|p75
name|Double
name|p75
decl_stmt|;
DECL|field|p95
name|Double
name|p95
decl_stmt|;
DECL|field|p98
name|Double
name|p98
decl_stmt|;
DECL|field|p99
name|Double
name|p99
decl_stmt|;
DECL|field|p99_9
name|Double
name|p99_9
decl_stmt|;
DECL|field|min
name|Double
name|min
decl_stmt|;
DECL|field|avg
name|Double
name|avg
decl_stmt|;
DECL|field|max
name|Double
name|max
decl_stmt|;
DECL|field|sum
name|Double
name|sum
decl_stmt|;
DECL|field|std_dev
name|Double
name|std_dev
decl_stmt|;
DECL|field|fields
name|List
argument_list|<
name|FieldJson
argument_list|>
name|fields
decl_stmt|;
DECL|field|buckets
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|buckets
decl_stmt|;
DECL|method|MetricJson (Metric metric, ImmutableMap<String, String> atts, boolean dataOnly)
name|MetricJson
parameter_list|(
name|Metric
name|metric
parameter_list|,
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atts
parameter_list|,
name|boolean
name|dataOnly
parameter_list|)
block|{
if|if
condition|(
operator|!
name|dataOnly
condition|)
block|{
name|description
operator|=
name|atts
operator|.
name|get
argument_list|(
name|Description
operator|.
name|DESCRIPTION
argument_list|)
expr_stmt|;
name|unit
operator|=
name|atts
operator|.
name|get
argument_list|(
name|Description
operator|.
name|UNIT
argument_list|)
expr_stmt|;
name|constant
operator|=
name|toBool
argument_list|(
name|atts
argument_list|,
name|Description
operator|.
name|CONSTANT
argument_list|)
expr_stmt|;
name|rate
operator|=
name|toBool
argument_list|(
name|atts
argument_list|,
name|Description
operator|.
name|RATE
argument_list|)
expr_stmt|;
name|gauge
operator|=
name|toBool
argument_list|(
name|atts
argument_list|,
name|Description
operator|.
name|GAUGE
argument_list|)
expr_stmt|;
name|cumulative
operator|=
name|toBool
argument_list|(
name|atts
argument_list|,
name|Description
operator|.
name|CUMULATIVE
argument_list|)
expr_stmt|;
block|}
name|init
argument_list|(
name|metric
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
DECL|method|init (Metric metric, ImmutableMap<String, String> atts)
specifier|private
name|void
name|init
parameter_list|(
name|Metric
name|metric
parameter_list|,
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atts
parameter_list|)
block|{
if|if
condition|(
name|metric
operator|instanceof
name|BucketedMetric
condition|)
block|{
name|BucketedMetric
name|m
init|=
operator|(
name|BucketedMetric
operator|)
name|metric
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|getTotal
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|init
argument_list|(
name|m
operator|.
name|getTotal
argument_list|()
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
name|Field
argument_list|<
name|?
argument_list|>
index|[]
name|fieldList
init|=
name|m
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|fields
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|fieldList
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|Field
argument_list|<
name|?
argument_list|>
name|f
range|:
name|fieldList
control|)
block|{
name|fields
operator|.
name|add
argument_list|(
operator|new
name|FieldJson
argument_list|(
name|f
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|buckets
operator|=
name|makeBuckets
argument_list|(
name|fieldList
argument_list|,
name|m
operator|.
name|getCells
argument_list|()
argument_list|,
name|atts
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metric
operator|instanceof
name|Counter
condition|)
block|{
name|Counter
name|c
init|=
operator|(
name|Counter
operator|)
name|metric
decl_stmt|;
name|count
operator|=
name|c
operator|.
name|getCount
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metric
operator|instanceof
name|Gauge
condition|)
block|{
name|Gauge
argument_list|<
name|?
argument_list|>
name|g
init|=
operator|(
name|Gauge
argument_list|<
name|?
argument_list|>
operator|)
name|metric
decl_stmt|;
name|value
operator|=
name|g
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metric
operator|instanceof
name|Meter
condition|)
block|{
name|Meter
name|m
init|=
operator|(
name|Meter
operator|)
name|metric
decl_stmt|;
name|count
operator|=
name|m
operator|.
name|getCount
argument_list|()
expr_stmt|;
name|rate_1m
operator|=
name|m
operator|.
name|getOneMinuteRate
argument_list|()
expr_stmt|;
name|rate_5m
operator|=
name|m
operator|.
name|getFiveMinuteRate
argument_list|()
expr_stmt|;
name|rate_15m
operator|=
name|m
operator|.
name|getFifteenMinuteRate
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metric
operator|instanceof
name|Timer
condition|)
block|{
name|Timer
name|m
init|=
operator|(
name|Timer
operator|)
name|metric
decl_stmt|;
name|Snapshot
name|s
init|=
name|m
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|count
operator|=
name|m
operator|.
name|getCount
argument_list|()
expr_stmt|;
name|rate_1m
operator|=
name|m
operator|.
name|getOneMinuteRate
argument_list|()
expr_stmt|;
name|rate_5m
operator|=
name|m
operator|.
name|getFiveMinuteRate
argument_list|()
expr_stmt|;
name|rate_15m
operator|=
name|m
operator|.
name|getFifteenMinuteRate
argument_list|()
expr_stmt|;
name|double
name|div
init|=
name|Description
operator|.
name|getTimeUnit
argument_list|(
name|atts
operator|.
name|get
argument_list|(
name|Description
operator|.
name|UNIT
argument_list|)
argument_list|)
operator|.
name|toNanos
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|p50
operator|=
name|s
operator|.
name|getMedian
argument_list|()
operator|/
name|div
expr_stmt|;
name|p75
operator|=
name|s
operator|.
name|get75thPercentile
argument_list|()
operator|/
name|div
expr_stmt|;
name|p95
operator|=
name|s
operator|.
name|get95thPercentile
argument_list|()
operator|/
name|div
expr_stmt|;
name|p98
operator|=
name|s
operator|.
name|get98thPercentile
argument_list|()
operator|/
name|div
expr_stmt|;
name|p99
operator|=
name|s
operator|.
name|get99thPercentile
argument_list|()
operator|/
name|div
expr_stmt|;
name|p99_9
operator|=
name|s
operator|.
name|get999thPercentile
argument_list|()
operator|/
name|div
expr_stmt|;
name|min
operator|=
name|s
operator|.
name|getMin
argument_list|()
operator|/
name|div
expr_stmt|;
name|max
operator|=
name|s
operator|.
name|getMax
argument_list|()
operator|/
name|div
expr_stmt|;
name|std_dev
operator|=
name|s
operator|.
name|getStdDev
argument_list|()
operator|/
name|div
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metric
operator|instanceof
name|Histogram
condition|)
block|{
name|Histogram
name|m
init|=
operator|(
name|Histogram
operator|)
name|metric
decl_stmt|;
name|Snapshot
name|s
init|=
name|m
operator|.
name|getSnapshot
argument_list|()
decl_stmt|;
name|count
operator|=
name|m
operator|.
name|getCount
argument_list|()
expr_stmt|;
name|p50
operator|=
name|s
operator|.
name|getMedian
argument_list|()
expr_stmt|;
name|p75
operator|=
name|s
operator|.
name|get75thPercentile
argument_list|()
expr_stmt|;
name|p95
operator|=
name|s
operator|.
name|get95thPercentile
argument_list|()
expr_stmt|;
name|p98
operator|=
name|s
operator|.
name|get98thPercentile
argument_list|()
expr_stmt|;
name|p99
operator|=
name|s
operator|.
name|get99thPercentile
argument_list|()
expr_stmt|;
name|p99_9
operator|=
name|s
operator|.
name|get999thPercentile
argument_list|()
expr_stmt|;
name|min
operator|=
operator|(
name|double
operator|)
name|s
operator|.
name|getMin
argument_list|()
expr_stmt|;
name|avg
operator|=
name|s
operator|.
name|getMean
argument_list|()
expr_stmt|;
name|max
operator|=
operator|(
name|double
operator|)
name|s
operator|.
name|getMax
argument_list|()
expr_stmt|;
name|sum
operator|=
name|s
operator|.
name|getMean
argument_list|()
operator|*
name|m
operator|.
name|getCount
argument_list|()
expr_stmt|;
name|std_dev
operator|=
name|s
operator|.
name|getStdDev
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|toBool (ImmutableMap<String, String> atts, String key)
specifier|private
specifier|static
name|Boolean
name|toBool
parameter_list|(
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atts
parameter_list|,
name|String
name|key
parameter_list|)
block|{
return|return
name|Description
operator|.
name|TRUE_VALUE
operator|.
name|equals
argument_list|(
name|atts
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
condition|?
literal|true
else|:
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|makeBuckets ( Field<?>[] fields, Map<?, Metric> metrics, ImmutableMap<String, String> atts)
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|makeBuckets
parameter_list|(
name|Field
argument_list|<
name|?
argument_list|>
index|[]
name|fields
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|Metric
argument_list|>
name|metrics
parameter_list|,
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|atts
parameter_list|)
block|{
if|if
condition|(
name|fields
operator|.
name|length
operator|==
literal|1
condition|)
block|{
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|out
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|Metric
argument_list|>
name|e
range|:
name|metrics
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|out
operator|.
name|put
argument_list|(
name|fmt
operator|.
name|apply
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|,
operator|new
name|MetricJson
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|atts
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|out
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|Metric
argument_list|>
name|e
range|:
name|metrics
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ImmutableList
argument_list|<
name|Object
argument_list|>
name|keys
init|=
operator|(
name|ImmutableList
argument_list|<
name|Object
argument_list|>
operator|)
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|dst
init|=
name|out
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|fields
operator|.
name|length
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
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
name|i
index|]
operator|.
name|formatter
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|fmt
operator|.
name|apply
argument_list|(
name|keys
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|t
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|dst
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
expr_stmt|;
name|dst
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
name|dst
operator|=
name|t
expr_stmt|;
block|}
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
name|fields
operator|.
name|length
operator|-
literal|1
index|]
operator|.
name|formatter
argument_list|()
decl_stmt|;
name|dst
operator|.
name|put
argument_list|(
name|fmt
operator|.
name|apply
argument_list|(
name|keys
operator|.
name|get
argument_list|(
name|fields
operator|.
name|length
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|,
operator|new
name|MetricJson
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|atts
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|out
return|;
block|}
DECL|class|FieldJson
specifier|static
class|class
name|FieldJson
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|type
name|String
name|type
decl_stmt|;
DECL|field|description
name|String
name|description
decl_stmt|;
DECL|method|FieldJson (Field<?> field)
name|FieldJson
parameter_list|(
name|Field
argument_list|<
name|?
argument_list|>
name|field
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|field
operator|.
name|name
argument_list|()
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|field
operator|.
name|description
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|Enum
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|field
operator|.
name|valueType
argument_list|()
argument_list|)
condition|?
name|field
operator|.
name|valueType
argument_list|()
operator|.
name|getSimpleName
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

