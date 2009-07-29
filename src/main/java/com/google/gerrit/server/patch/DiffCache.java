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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
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
name|server
operator|.
name|GerritServer
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
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Cache
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
name|CacheException
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
name|Element
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
name|constructs
operator|.
name|blocking
operator|.
name|SelfPopulatingCache
import|;
end_import

begin_comment
comment|/** Provides the {@link DiffCacheContent}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|DiffCache
specifier|public
class|class
name|DiffCache
block|{
DECL|field|self
specifier|private
specifier|final
name|SelfPopulatingCache
name|self
decl_stmt|;
annotation|@
name|Inject
DECL|method|DiffCache (final CacheManager mgr, final GerritServer gs)
name|DiffCache
parameter_list|(
specifier|final
name|CacheManager
name|mgr
parameter_list|,
specifier|final
name|GerritServer
name|gs
parameter_list|)
block|{
specifier|final
name|Cache
name|dc
init|=
name|mgr
operator|.
name|getCache
argument_list|(
literal|"diff"
argument_list|)
decl_stmt|;
name|self
operator|=
operator|new
name|SelfPopulatingCache
argument_list|(
name|dc
argument_list|,
operator|new
name|DiffCacheEntryFactory
argument_list|(
name|gs
argument_list|)
argument_list|)
expr_stmt|;
name|mgr
operator|.
name|replaceCacheWithDecoratedCache
argument_list|(
name|dc
argument_list|,
name|self
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|self
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|get (final DiffCacheKey key)
specifier|public
name|Element
name|get
parameter_list|(
specifier|final
name|DiffCacheKey
name|key
parameter_list|)
block|{
return|return
name|self
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|put (final DiffCacheKey k, final DiffCacheContent c)
specifier|public
name|void
name|put
parameter_list|(
specifier|final
name|DiffCacheKey
name|k
parameter_list|,
specifier|final
name|DiffCacheContent
name|c
parameter_list|)
block|{
name|self
operator|.
name|put
argument_list|(
operator|new
name|Element
argument_list|(
name|k
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|IllegalStateException
throws|,
name|CacheException
block|{
name|self
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

