begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|annotations
operator|.
name|RequiresCapability
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
name|restapi
operator|.
name|RestReadView
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
name|cache
operator|.
name|PersistentCache
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

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
annotation|@
name|Singleton
DECL|class|ListCaches
specifier|public
class|class
name|ListCaches
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|cacheMap
specifier|private
specifier|final
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
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListCaches (DynamicMap<Cache<?, ?>> cacheMap)
specifier|public
name|ListCaches
parameter_list|(
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
name|this
operator|.
name|cacheMap
operator|=
name|cacheMap
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource rsrc)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|CacheInfo
argument_list|>
name|apply
parameter_list|(
name|ConfigResource
name|rsrc
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|CacheInfo
argument_list|>
name|cacheInfos
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DynamicMap
operator|.
name|Entry
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
name|cacheInfos
operator|.
name|put
argument_list|(
name|cacheNameOf
argument_list|(
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
argument_list|,
operator|new
name|CacheInfo
argument_list|(
name|e
operator|.
name|getProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cacheInfos
return|;
block|}
DECL|method|cacheNameOf (String plugin, String name)
specifier|private
specifier|static
name|String
name|cacheNameOf
parameter_list|(
name|String
name|plugin
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"gerrit"
operator|.
name|equals
argument_list|(
name|plugin
argument_list|)
condition|)
block|{
return|return
name|name
return|;
block|}
else|else
block|{
return|return
name|plugin
operator|+
literal|"-"
operator|+
name|name
return|;
block|}
block|}
DECL|enum|CacheType
specifier|public
enum|enum
name|CacheType
block|{
DECL|enumConstant|MEM
DECL|enumConstant|DISK
name|MEM
block|,
name|DISK
block|;   }
DECL|class|CacheInfo
specifier|public
specifier|static
class|class
name|CacheInfo
block|{
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|public
name|CacheType
name|type
decl_stmt|;
DECL|field|entries
specifier|public
name|EntriesInfo
name|entries
decl_stmt|;
DECL|field|averageGet
specifier|public
name|String
name|averageGet
decl_stmt|;
DECL|field|hitRatio
specifier|public
name|HitRatioInfo
name|hitRatio
decl_stmt|;
DECL|method|CacheInfo (Cache<?,?> cache)
specifier|public
name|CacheInfo
parameter_list|(
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cache
parameter_list|)
block|{
name|CacheStats
name|stat
init|=
name|cache
operator|.
name|stats
argument_list|()
decl_stmt|;
name|entries
operator|=
operator|new
name|EntriesInfo
argument_list|()
expr_stmt|;
name|entries
operator|.
name|setMem
argument_list|(
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|averageGet
operator|=
name|duration
argument_list|(
name|stat
operator|.
name|averageLoadPenalty
argument_list|()
argument_list|)
expr_stmt|;
name|hitRatio
operator|=
operator|new
name|HitRatioInfo
argument_list|()
expr_stmt|;
name|hitRatio
operator|.
name|setMem
argument_list|(
name|stat
operator|.
name|hitCount
argument_list|()
argument_list|,
name|stat
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|instanceof
name|PersistentCache
condition|)
block|{
name|type
operator|=
name|CacheType
operator|.
name|DISK
expr_stmt|;
name|PersistentCache
operator|.
name|DiskStats
name|diskStats
init|=
operator|(
operator|(
name|PersistentCache
operator|)
name|cache
operator|)
operator|.
name|diskStats
argument_list|()
decl_stmt|;
name|entries
operator|.
name|setDisk
argument_list|(
name|diskStats
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|entries
operator|.
name|setSpace
argument_list|(
name|diskStats
operator|.
name|space
argument_list|()
argument_list|)
expr_stmt|;
name|hitRatio
operator|.
name|setDisk
argument_list|(
name|diskStats
operator|.
name|hitCount
argument_list|()
argument_list|,
name|diskStats
operator|.
name|requestCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|duration (double ns)
specifier|private
specifier|static
name|String
name|duration
parameter_list|(
name|double
name|ns
parameter_list|)
block|{
if|if
condition|(
name|ns
operator|<
literal|0.5
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|suffix
init|=
literal|"ns"
decl_stmt|;
if|if
condition|(
name|ns
operator|>=
literal|1000.0
condition|)
block|{
name|ns
operator|/=
literal|1000.0
expr_stmt|;
name|suffix
operator|=
literal|"us"
expr_stmt|;
block|}
if|if
condition|(
name|ns
operator|>=
literal|1000.0
condition|)
block|{
name|ns
operator|/=
literal|1000.0
expr_stmt|;
name|suffix
operator|=
literal|"ms"
expr_stmt|;
block|}
if|if
condition|(
name|ns
operator|>=
literal|1000.0
condition|)
block|{
name|ns
operator|/=
literal|1000.0
expr_stmt|;
name|suffix
operator|=
literal|"s"
expr_stmt|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%4.1f%s"
argument_list|,
name|ns
argument_list|,
name|suffix
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
DECL|class|EntriesInfo
specifier|public
specifier|static
class|class
name|EntriesInfo
block|{
DECL|field|mem
specifier|public
name|Long
name|mem
decl_stmt|;
DECL|field|disk
specifier|public
name|Long
name|disk
decl_stmt|;
DECL|field|space
specifier|public
name|String
name|space
decl_stmt|;
DECL|method|setMem (long mem)
specifier|public
name|void
name|setMem
parameter_list|(
name|long
name|mem
parameter_list|)
block|{
name|this
operator|.
name|mem
operator|=
name|mem
operator|!=
literal|0
condition|?
name|mem
else|:
literal|null
expr_stmt|;
block|}
DECL|method|setDisk (long disk)
specifier|public
name|void
name|setDisk
parameter_list|(
name|long
name|disk
parameter_list|)
block|{
name|this
operator|.
name|disk
operator|=
name|disk
operator|!=
literal|0
condition|?
name|disk
else|:
literal|null
expr_stmt|;
block|}
DECL|method|setSpace (double value)
specifier|public
name|void
name|setSpace
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|space
operator|=
name|bytes
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
DECL|method|bytes (double value)
specifier|private
specifier|static
name|String
name|bytes
parameter_list|(
name|double
name|value
parameter_list|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|String
name|suffix
init|=
literal|"k"
decl_stmt|;
if|if
condition|(
name|value
operator|>
literal|1024
condition|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|suffix
operator|=
literal|"m"
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|>
literal|1024
condition|)
block|{
name|value
operator|/=
literal|1024
expr_stmt|;
name|suffix
operator|=
literal|"g"
expr_stmt|;
block|}
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$6.2f%2$s"
argument_list|,
name|value
argument_list|,
name|suffix
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
DECL|class|HitRatioInfo
specifier|public
specifier|static
class|class
name|HitRatioInfo
block|{
DECL|field|mem
specifier|public
name|Integer
name|mem
decl_stmt|;
DECL|field|disk
specifier|public
name|Integer
name|disk
decl_stmt|;
DECL|method|setMem (long value, long total)
specifier|public
name|void
name|setMem
parameter_list|(
name|long
name|value
parameter_list|,
name|long
name|total
parameter_list|)
block|{
name|mem
operator|=
name|percent
argument_list|(
name|value
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
DECL|method|setDisk (long value, long total)
specifier|public
name|void
name|setDisk
parameter_list|(
name|long
name|value
parameter_list|,
name|long
name|total
parameter_list|)
block|{
name|disk
operator|=
name|percent
argument_list|(
name|value
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
DECL|method|percent (long value, long total)
specifier|private
specifier|static
name|Integer
name|percent
parameter_list|(
name|long
name|value
parameter_list|,
name|long
name|total
parameter_list|)
block|{
if|if
condition|(
name|total
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
call|(
name|int
call|)
argument_list|(
operator|(
literal|100
operator|*
name|value
operator|)
operator|/
name|total
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

