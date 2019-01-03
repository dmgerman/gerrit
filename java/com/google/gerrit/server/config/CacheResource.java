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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
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
name|Provider
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

begin_class
DECL|class|CacheResource
specifier|public
class|class
name|CacheResource
extends|extends
name|ConfigResource
block|{
DECL|field|CACHE_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|CacheResource
argument_list|>
argument_list|>
name|CACHE_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|CacheResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|cacheProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheProvider
decl_stmt|;
DECL|method|CacheResource (String pluginName, String cacheName, Provider<Cache<?, ?>> cacheProvider)
specifier|public
name|CacheResource
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|Provider
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|cacheProvider
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|cacheNameOf
argument_list|(
name|pluginName
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheProvider
operator|=
name|cacheProvider
expr_stmt|;
block|}
DECL|method|CacheResource (String pluginName, String cacheName, Cache<?, ?> cache)
specifier|public
name|CacheResource
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|cache
parameter_list|)
block|{
name|this
argument_list|(
name|pluginName
argument_list|,
name|cacheName
argument_list|,
parameter_list|()
lambda|->
name|cache
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
name|name
return|;
block|}
DECL|method|getCache ()
specifier|public
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|cacheProvider
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|cacheNameOf (String plugin, String name)
specifier|public
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
name|PluginName
operator|.
name|GERRIT
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
return|return
name|plugin
operator|+
literal|"-"
operator|+
name|name
return|;
block|}
block|}
end_class

end_unit

