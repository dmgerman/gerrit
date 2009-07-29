begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Ehcache
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Statistics
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|CacheConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|WindowCacheStatAccessor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_comment
comment|/** Show the current cache states. */
end_comment

begin_class
DECL|class|AdminShowCaches
class|class
name|AdminShowCaches
extends|extends
name|AbstractAdminCacheCommand
block|{
DECL|field|p
name|PrintWriter
name|p
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
throws|,
name|UnsupportedEncodingException
block|{
name|assertIsAdministrator
argument_list|()
expr_stmt|;
name|p
operator|=
name|toPrintWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Ehcache
name|cache
range|:
name|getAllCaches
argument_list|()
control|)
block|{
specifier|final
name|CacheConfiguration
name|cfg
init|=
name|cache
operator|.
name|getCacheConfiguration
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|useDisk
init|=
name|cfg
operator|.
name|isDiskPersistent
argument_list|()
operator|||
name|cfg
operator|.
name|isOverflowToDisk
argument_list|()
decl_stmt|;
specifier|final
name|Statistics
name|stat
init|=
name|cache
operator|.
name|getStatistics
argument_list|()
decl_stmt|;
name|p
operator|.
name|print
argument_list|(
literal|"cache \""
operator|+
name|cache
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|useDisk
condition|)
block|{
name|p
operator|.
name|print
argument_list|(
literal|" (memory, disk)"
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|println
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|fItemCount
argument_list|(
literal|"items"
argument_list|,
name|stat
operator|.
name|getObjectCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|useDisk
condition|)
block|{
name|fItemCount
argument_list|(
literal|"items.memory"
argument_list|,
name|stat
operator|.
name|getMemoryStoreObjectCount
argument_list|()
argument_list|)
expr_stmt|;
name|fItemCount
argument_list|(
literal|"items.disk"
argument_list|,
name|stat
operator|.
name|getDiskStoreObjectCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|fItemCount
argument_list|(
literal|"evictions"
argument_list|,
name|stat
operator|.
name|getEvictionCount
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|long
name|total
init|=
name|stat
operator|.
name|getCacheHits
argument_list|()
operator|+
name|stat
operator|.
name|getCacheMisses
argument_list|()
decl_stmt|;
name|fTimeInterval
argument_list|(
literal|"ttl.idle"
argument_list|,
name|cfg
operator|.
name|getTimeToIdleSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|fTimeInterval
argument_list|(
literal|"ttl.live"
argument_list|,
name|cfg
operator|.
name|getTimeToLiveSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|fMilliseconds
argument_list|(
literal|"avg.get"
argument_list|,
name|stat
operator|.
name|getAverageGetTime
argument_list|()
argument_list|)
expr_stmt|;
name|fPercent
argument_list|(
literal|"hit%"
argument_list|,
name|stat
operator|.
name|getCacheHits
argument_list|()
argument_list|,
name|total
argument_list|)
expr_stmt|;
if|if
condition|(
name|useDisk
condition|)
block|{
name|fPercent
argument_list|(
literal|"hit%.memory"
argument_list|,
name|stat
operator|.
name|getInMemoryHits
argument_list|()
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|fPercent
argument_list|(
literal|"hit%.disk"
argument_list|,
name|stat
operator|.
name|getOnDiskHits
argument_list|()
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Runtime
name|r
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mMax
init|=
name|r
operator|.
name|maxMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mFree
init|=
name|r
operator|.
name|freeMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mTotal
init|=
name|r
operator|.
name|totalMemory
argument_list|()
decl_stmt|;
specifier|final
name|long
name|mInuse
init|=
name|mTotal
operator|-
name|mFree
decl_stmt|;
specifier|final
name|long
name|jgitBytes
init|=
name|WindowCacheStatAccessor
operator|.
name|getOpenBytes
argument_list|()
decl_stmt|;
name|p
operator|.
name|println
argument_list|(
literal|"JGit Buffer Cache:"
argument_list|)
expr_stmt|;
name|fItemCount
argument_list|(
literal|"open files"
argument_list|,
name|WindowCacheStatAccessor
operator|.
name|getOpenFiles
argument_list|()
argument_list|)
expr_stmt|;
name|fByteCount
argument_list|(
literal|"loaded"
argument_list|,
name|jgitBytes
argument_list|)
expr_stmt|;
name|fPercent
argument_list|(
literal|"mem%"
argument_list|,
name|jgitBytes
argument_list|,
name|mTotal
argument_list|)
expr_stmt|;
name|p
operator|.
name|println
argument_list|()
expr_stmt|;
name|p
operator|.
name|println
argument_list|(
literal|"JVM Heap:"
argument_list|)
expr_stmt|;
name|fByteCount
argument_list|(
literal|"max"
argument_list|,
name|mMax
argument_list|)
expr_stmt|;
name|fByteCount
argument_list|(
literal|"inuse"
argument_list|,
name|mInuse
argument_list|)
expr_stmt|;
name|fPercent
argument_list|(
literal|"mem%"
argument_list|,
name|mInuse
argument_list|,
name|mTotal
argument_list|)
expr_stmt|;
name|p
operator|.
name|println
argument_list|()
expr_stmt|;
name|p
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|fItemCount (final String name, final long value)
specifier|private
name|void
name|fItemCount
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|long
name|value
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s: %2$15d"
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fByteCount (final String name, double value)
specifier|private
name|void
name|fByteCount
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|double
name|value
parameter_list|)
block|{
name|String
name|suffix
init|=
literal|"bytes"
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
literal|"kb"
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
literal|"mb"
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
literal|"gb"
expr_stmt|;
block|}
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s: %2$6.2f %3$s"
argument_list|,
name|name
argument_list|,
name|value
argument_list|,
name|suffix
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fMilliseconds (final String name, final float ms)
specifier|private
name|void
name|fMilliseconds
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|float
name|ms
parameter_list|)
block|{
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s: %2$6.2f ms"
argument_list|,
name|name
argument_list|,
name|ms
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fTimeInterval (final String name, double ttl)
specifier|private
name|void
name|fTimeInterval
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|double
name|ttl
parameter_list|)
block|{
if|if
condition|(
name|ttl
operator|==
literal|0
condition|)
block|{
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s:        inf"
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|suffix
init|=
literal|"secs"
decl_stmt|;
if|if
condition|(
name|ttl
operator|>
literal|60
condition|)
block|{
name|ttl
operator|/=
literal|60
expr_stmt|;
name|suffix
operator|=
literal|"mins"
expr_stmt|;
block|}
if|if
condition|(
name|ttl
operator|>
literal|60
condition|)
block|{
name|ttl
operator|/=
literal|60
expr_stmt|;
name|suffix
operator|=
literal|"hrs"
expr_stmt|;
block|}
if|if
condition|(
name|ttl
operator|>
literal|24
condition|)
block|{
name|ttl
operator|/=
literal|24
expr_stmt|;
name|suffix
operator|=
literal|"days"
expr_stmt|;
block|}
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s: %2$6.2f %3$s"
argument_list|,
name|name
argument_list|,
name|ttl
argument_list|,
name|suffix
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|fPercent (final String name, final long value, final long total)
specifier|private
name|void
name|fPercent
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|long
name|value
parameter_list|,
specifier|final
name|long
name|total
parameter_list|)
block|{
specifier|final
name|long
name|pcent
init|=
literal|0
operator|<
name|total
condition|?
operator|(
literal|100
operator|*
name|value
operator|)
operator|/
name|total
else|:
literal|0
decl_stmt|;
name|p
operator|.
name|println
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"  %1$-12s: %2$3d%%"
argument_list|,
name|name
argument_list|,
operator|(
name|int
operator|)
name|pcent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

