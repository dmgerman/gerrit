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
DECL|package|com.google.gerrit.server.cache
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
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
name|cache
operator|.
name|Cache
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
name|cache
operator|.
name|CacheStats
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
name|DynamicMap
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
name|Extension
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
name|PluginName
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
name|server
operator|.
name|logging
operator|.
name|Metadata
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
name|Singleton
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
annotation|@
name|Singleton
DECL|class|CacheMetrics
specifier|public
class|class
name|CacheMetrics
block|{
annotation|@
name|Inject
DECL|method|CacheMetrics (MetricMaker metrics, DynamicMap<Cache<?, ?>> cacheMap)
specifier|public
name|CacheMetrics
parameter_list|(
name|MetricMaker
name|metrics
parameter_list|,
name|DynamicMap
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheMap
parameter_list|)
block|{
name|Field
argument_list|<
name|String
argument_list|>
name|F_NAME
init|=
name|Field
operator|.
name|ofString
argument_list|(
literal|"cache_name"
argument_list|,
name|Metadata
operator|.
name|Builder
operator|::
name|cacheName
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|CallbackMetric1
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|memEnt
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"caches/memory_cached"
argument_list|,
name|Long
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Memory entries"
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"entries"
argument_list|)
argument_list|,
name|F_NAME
argument_list|)
decl_stmt|;
name|CallbackMetric1
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
name|memHit
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"caches/memory_hit_ratio"
argument_list|,
name|Double
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Memory hit ratio"
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"percent"
argument_list|)
argument_list|,
name|F_NAME
argument_list|)
decl_stmt|;
name|CallbackMetric1
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|memEvict
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"caches/memory_eviction_count"
argument_list|,
name|Long
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Memory eviction count"
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"evicted entries"
argument_list|)
argument_list|,
name|F_NAME
argument_list|)
decl_stmt|;
name|CallbackMetric1
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|perDiskEnt
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"caches/disk_cached"
argument_list|,
name|Long
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Disk entries used by persistent cache"
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"entries"
argument_list|)
argument_list|,
name|F_NAME
argument_list|)
decl_stmt|;
name|CallbackMetric1
argument_list|<
name|String
argument_list|,
name|Double
argument_list|>
name|perDiskHit
init|=
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"caches/disk_hit_ratio"
argument_list|,
name|Double
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Disk hit ratio for persistent cache"
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"percent"
argument_list|)
argument_list|,
name|F_NAME
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|CallbackMetric
argument_list|<
name|?
argument_list|>
argument_list|>
name|cacheMetrics
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
name|memEnt
argument_list|,
name|memHit
argument_list|,
name|memEvict
argument_list|,
name|perDiskEnt
argument_list|,
name|perDiskHit
argument_list|)
decl_stmt|;
name|metrics
operator|.
name|newTrigger
argument_list|(
name|cacheMetrics
argument_list|,
parameter_list|()
lambda|->
block|{
for|for
control|(
name|Extension
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|e
range|:
name|cacheMap
control|)
block|{
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|c
init|=
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|metricNameOf
argument_list|(
name|e
argument_list|)
decl_stmt|;
name|CacheStats
name|cstats
init|=
name|c
operator|.
name|stats
argument_list|()
decl_stmt|;
name|memEnt
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|c
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|memHit
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|cstats
operator|.
name|hitRate
argument_list|()
operator|*
literal|100
argument_list|)
expr_stmt|;
name|memEvict
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|cstats
operator|.
name|evictionCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|c
operator|instanceof
name|PersistentCache
condition|)
block|{
name|PersistentCache
operator|.
name|DiskStats
name|d
init|=
operator|(
operator|(
name|PersistentCache
operator|)
name|c
operator|)
operator|.
name|diskStats
argument_list|()
decl_stmt|;
name|perDiskEnt
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|d
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|perDiskHit
operator|.
name|set
argument_list|(
name|name
argument_list|,
name|hitRatio
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|cacheMetrics
operator|.
name|forEach
argument_list|(
name|CallbackMetric
operator|::
name|prune
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|hitRatio (PersistentCache.DiskStats d)
specifier|private
specifier|static
name|double
name|hitRatio
parameter_list|(
name|PersistentCache
operator|.
name|DiskStats
name|d
parameter_list|)
block|{
if|if
condition|(
name|d
operator|.
name|requestCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|100
return|;
block|}
return|return
operator|(
operator|(
name|double
operator|)
name|d
operator|.
name|hitCount
argument_list|()
operator|/
name|d
operator|.
name|requestCount
argument_list|()
operator|*
literal|100
operator|)
return|;
block|}
DECL|method|metricNameOf (Extension<Cache<?, ?>> e)
specifier|private
specifier|static
name|String
name|metricNameOf
parameter_list|(
name|Extension
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|e
parameter_list|)
block|{
if|if
condition|(
name|PluginName
operator|.
name|GERRIT
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getPluginName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|e
operator|.
name|getExportName
argument_list|()
return|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"plugin/%s/%s"
argument_list|,
name|e
operator|.
name|getPluginName
argument_list|()
argument_list|,
name|e
operator|.
name|getExportName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

