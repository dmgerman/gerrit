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
name|gerrit
operator|.
name|server
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
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|CacheModule
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
name|SelfPopulatingCache
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
name|Module
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|name
operator|.
name|Named
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
DECL|field|CACHE_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_NAME
init|=
literal|"diff"
decl_stmt|;
DECL|method|module ()
specifier|public
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
specifier|final
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|DiffCacheKey
argument_list|,
name|DiffCacheContent
argument_list|>
argument_list|>
name|type
init|=
operator|new
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|DiffCacheKey
argument_list|,
name|DiffCacheContent
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|disk
argument_list|(
name|type
argument_list|,
name|CACHE_NAME
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DiffCache
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|self
specifier|private
specifier|final
name|SelfPopulatingCache
argument_list|<
name|DiffCacheKey
argument_list|,
name|DiffCacheContent
argument_list|>
name|self
decl_stmt|;
annotation|@
name|Inject
DECL|method|DiffCache (final GerritServer gs, @Named(CACHE_NAME) final Cache<DiffCacheKey, DiffCacheContent> raw)
name|DiffCache
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
annotation|@
name|Named
argument_list|(
name|CACHE_NAME
argument_list|)
specifier|final
name|Cache
argument_list|<
name|DiffCacheKey
argument_list|,
name|DiffCacheContent
argument_list|>
name|raw
parameter_list|)
block|{
specifier|final
name|DiffCacheEntryFactory
name|f
init|=
operator|new
name|DiffCacheEntryFactory
argument_list|(
name|gs
argument_list|)
decl_stmt|;
name|self
operator|=
operator|new
name|SelfPopulatingCache
argument_list|<
name|DiffCacheKey
argument_list|,
name|DiffCacheContent
argument_list|>
argument_list|(
name|raw
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|DiffCacheContent
name|createEntry
parameter_list|(
specifier|final
name|DiffCacheKey
name|key
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|f
operator|.
name|createEntry
argument_list|(
name|key
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|get (final DiffCacheKey key)
specifier|public
name|DiffCacheContent
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
name|k
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

