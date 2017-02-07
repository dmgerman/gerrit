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
name|CacheLoader
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
name|LoadingCache
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
name|Weigher
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
name|Exports
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
name|config
operator|.
name|FactoryModule
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
name|Key
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
name|Scopes
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
name|Names
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
name|util
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_comment
comment|/** Miniature DSL to support binding {@link Cache} instances in Guice. */
end_comment

begin_class
DECL|class|CacheModule
specifier|public
specifier|abstract
class|class
name|CacheModule
extends|extends
name|FactoryModule
block|{
DECL|field|ANY_CACHE
specifier|private
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
name|ANY_CACHE
init|=
operator|new
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
argument_list|>
argument_list|()
block|{}
empty_stmt|;
comment|/**    * Declare a named in-memory cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|cache (String name, Class<K> keyType, Class<V> valType)
specifier|protected
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
return|return
name|cache
argument_list|(
name|name
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|keyType
argument_list|)
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|valType
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Declare a named in-memory cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|cache (String name, Class<K> keyType, TypeLiteral<V> valType)
specifier|protected
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
return|return
name|cache
argument_list|(
name|name
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|keyType
argument_list|)
argument_list|,
name|valType
argument_list|)
return|;
block|}
comment|/**    * Declare a named in-memory cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|cache ( String name, TypeLiteral<K> keyType, TypeLiteral<V> valType)
specifier|protected
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|cache
parameter_list|(
name|String
name|name
parameter_list|,
name|TypeLiteral
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
name|Type
name|type
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Cache
operator|.
name|class
argument_list|,
name|keyType
operator|.
name|getType
argument_list|()
argument_list|,
name|valType
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|key
init|=
operator|(
name|Key
argument_list|<
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|type
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|CacheProvider
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|m
init|=
operator|new
name|CacheProvider
argument_list|<>
argument_list|(
name|this
argument_list|,
name|name
argument_list|,
name|keyType
argument_list|,
name|valType
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|key
argument_list|)
operator|.
name|toProvider
argument_list|(
name|m
argument_list|)
operator|.
name|asEagerSingleton
argument_list|()
expr_stmt|;
name|bind
argument_list|(
name|ANY_CACHE
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|m
operator|.
name|maximumWeight
argument_list|(
literal|1024
argument_list|)
return|;
block|}
DECL|method|bindCacheLoader ( CacheProvider<K, V> m, Class<? extends CacheLoader<K, V>> impl)
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Provider
argument_list|<
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|bindCacheLoader
parameter_list|(
name|CacheProvider
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|m
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|impl
parameter_list|)
block|{
name|Type
name|type
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Cache
operator|.
name|class
argument_list|,
name|m
operator|.
name|keyType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|m
operator|.
name|valueType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|Type
name|loadingType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|LoadingCache
operator|.
name|class
argument_list|,
name|m
operator|.
name|keyType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|m
operator|.
name|valueType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|Type
name|loaderType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|CacheLoader
operator|.
name|class
argument_list|,
name|m
operator|.
name|keyType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|m
operator|.
name|valueType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|key
init|=
operator|(
name|Key
argument_list|<
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|type
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|m
operator|.
name|name
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|loadingKey
init|=
operator|(
name|Key
argument_list|<
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|loadingType
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|m
operator|.
name|name
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|loaderKey
init|=
operator|(
name|Key
argument_list|<
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|loaderType
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|m
operator|.
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|loaderKey
argument_list|)
operator|.
name|to
argument_list|(
name|impl
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|loadingKey
argument_list|)
operator|.
name|to
argument_list|(
name|key
argument_list|)
expr_stmt|;
return|return
name|getProvider
argument_list|(
name|loaderKey
argument_list|)
return|;
block|}
DECL|method|bindWeigher ( CacheProvider<K, V> m, Class<? extends Weigher<K, V>> impl)
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Provider
argument_list|<
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|bindWeigher
parameter_list|(
name|CacheProvider
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|m
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|impl
parameter_list|)
block|{
name|Type
name|weigherType
init|=
name|Types
operator|.
name|newParameterizedType
argument_list|(
name|Weigher
operator|.
name|class
argument_list|,
name|m
operator|.
name|keyType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|,
name|m
operator|.
name|valueType
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Key
argument_list|<
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|key
init|=
operator|(
name|Key
argument_list|<
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
operator|)
name|Key
operator|.
name|get
argument_list|(
name|weigherType
argument_list|,
name|Names
operator|.
name|named
argument_list|(
name|m
operator|.
name|name
argument_list|)
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|key
argument_list|)
operator|.
name|to
argument_list|(
name|impl
argument_list|)
operator|.
name|in
argument_list|(
name|Scopes
operator|.
name|SINGLETON
argument_list|)
expr_stmt|;
return|return
name|getProvider
argument_list|(
name|key
argument_list|)
return|;
block|}
comment|/**    * Declare a named in-memory/on-disk cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|persist ( String name, Class<K> keyType, Class<V> valType)
specifier|protected
parameter_list|<
name|K
extends|extends
name|Serializable
parameter_list|,
name|V
extends|extends
name|Serializable
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|persist
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|Class
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
return|return
name|persist
argument_list|(
name|name
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|keyType
argument_list|)
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|valType
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Declare a named in-memory/on-disk cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|persist ( String name, Class<K> keyType, TypeLiteral<V> valType)
specifier|protected
parameter_list|<
name|K
extends|extends
name|Serializable
parameter_list|,
name|V
extends|extends
name|Serializable
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|persist
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
return|return
name|persist
argument_list|(
name|name
argument_list|,
name|TypeLiteral
operator|.
name|get
argument_list|(
name|keyType
argument_list|)
argument_list|,
name|valType
argument_list|)
return|;
block|}
comment|/**    * Declare a named in-memory/on-disk cache.    *    * @param<K> type of key used to lookup entries.    * @param<V> type of value stored by the cache.    * @return binding to describe the cache.    */
DECL|method|persist ( String name, TypeLiteral<K> keyType, TypeLiteral<V> valType)
specifier|protected
parameter_list|<
name|K
extends|extends
name|Serializable
parameter_list|,
name|V
extends|extends
name|Serializable
parameter_list|>
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|persist
parameter_list|(
name|String
name|name
parameter_list|,
name|TypeLiteral
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|,
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valType
parameter_list|)
block|{
return|return
operator|(
operator|(
name|CacheProvider
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|cache
argument_list|(
name|name
argument_list|,
name|keyType
argument_list|,
name|valType
argument_list|)
operator|)
operator|.
name|persist
argument_list|(
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

