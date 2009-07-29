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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
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
name|Ehcache
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
DECL|class|AbstractAdminCacheCommand
specifier|abstract
class|class
name|AbstractAdminCacheCommand
extends|extends
name|AbstractCommand
block|{
annotation|@
name|Inject
DECL|field|cacheMgr
specifier|protected
name|CacheManager
name|cacheMgr
decl_stmt|;
DECL|method|cacheNames ()
specifier|protected
name|SortedSet
argument_list|<
name|String
argument_list|>
name|cacheNames
parameter_list|()
block|{
specifier|final
name|SortedSet
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|TreeSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|names
operator|.
name|add
argument_list|(
literal|"groups"
argument_list|)
expr_stmt|;
name|names
operator|.
name|add
argument_list|(
literal|"projects"
argument_list|)
expr_stmt|;
name|names
operator|.
name|add
argument_list|(
literal|"accounts"
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Ehcache
name|c
range|:
name|getAllCaches
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|names
return|;
block|}
DECL|method|getAllCaches ()
specifier|protected
name|Ehcache
index|[]
name|getAllCaches
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|cacheNames
init|=
name|cacheMgr
operator|.
name|getCacheNames
argument_list|()
decl_stmt|;
name|Arrays
operator|.
name|sort
argument_list|(
name|cacheNames
argument_list|)
expr_stmt|;
specifier|final
name|Ehcache
index|[]
name|r
init|=
operator|new
name|Ehcache
index|[
name|cacheNames
operator|.
name|length
index|]
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
name|cacheNames
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|r
index|[
name|i
index|]
operator|=
name|cacheMgr
operator|.
name|getEhcache
argument_list|(
name|cacheNames
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

