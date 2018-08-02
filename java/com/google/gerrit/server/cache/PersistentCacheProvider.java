begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|common
operator|.
name|Nullable
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
name|time
operator|.
name|Duration
import|;
end_import

begin_class
DECL|class|PersistentCacheProvider
class|class
name|PersistentCacheProvider
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|CacheProvider
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|implements
name|Provider
argument_list|<
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
implements|,
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
implements|,
name|PersistentCacheDef
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|version
specifier|private
name|int
name|version
decl_stmt|;
DECL|field|diskLimit
specifier|private
name|long
name|diskLimit
decl_stmt|;
DECL|field|keySerializer
specifier|private
name|CacheSerializer
argument_list|<
name|K
argument_list|>
name|keySerializer
decl_stmt|;
DECL|field|valueSerializer
specifier|private
name|CacheSerializer
argument_list|<
name|V
argument_list|>
name|valueSerializer
decl_stmt|;
DECL|field|persistentCacheFactory
specifier|private
name|PersistentCacheFactory
name|persistentCacheFactory
decl_stmt|;
DECL|method|PersistentCacheProvider ( CacheModule module, String name, TypeLiteral<K> keyType, TypeLiteral<V> valType)
name|PersistentCacheProvider
parameter_list|(
name|CacheModule
name|module
parameter_list|,
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
name|super
argument_list|(
name|module
argument_list|,
name|name
argument_list|,
name|keyType
argument_list|,
name|valType
argument_list|)
expr_stmt|;
name|version
operator|=
operator|-
literal|1
expr_stmt|;
name|diskLimit
operator|=
literal|128
operator|<<
literal|20
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setPersistentCacheFactory (@ullable PersistentCacheFactory factory)
name|void
name|setPersistentCacheFactory
parameter_list|(
annotation|@
name|Nullable
name|PersistentCacheFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|persistentCacheFactory
operator|=
name|factory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|maximumWeight (long weight)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|maximumWeight
parameter_list|(
name|long
name|weight
parameter_list|)
block|{
return|return
operator|(
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|maximumWeight
argument_list|(
name|weight
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|expireAfterWrite (Duration duration)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|expireAfterWrite
parameter_list|(
name|Duration
name|duration
parameter_list|)
block|{
return|return
operator|(
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|expireAfterWrite
argument_list|(
name|duration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|loader (Class<? extends CacheLoader<K, V>> clazz)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|(
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
name|clazz
parameter_list|)
block|{
return|return
operator|(
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|loader
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|expireFromMemoryAfterAccess (Duration duration)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|expireFromMemoryAfterAccess
parameter_list|(
name|Duration
name|duration
parameter_list|)
block|{
return|return
operator|(
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|expireFromMemoryAfterAccess
argument_list|(
name|duration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|weigher (Class<? extends Weigher<K, V>> clazz)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|weigher
parameter_list|(
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
name|clazz
parameter_list|)
block|{
return|return
operator|(
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
operator|)
name|super
operator|.
name|weigher
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|version (int version)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|version
parameter_list|(
name|int
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|keySerializer (CacheSerializer<K> keySerializer)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|keySerializer
parameter_list|(
name|CacheSerializer
argument_list|<
name|K
argument_list|>
name|keySerializer
parameter_list|)
block|{
name|this
operator|.
name|keySerializer
operator|=
name|keySerializer
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|valueSerializer (CacheSerializer<V> valueSerializer)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|valueSerializer
parameter_list|(
name|CacheSerializer
argument_list|<
name|V
argument_list|>
name|valueSerializer
parameter_list|)
block|{
name|this
operator|.
name|valueSerializer
operator|=
name|valueSerializer
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|diskLimit (long limit)
specifier|public
name|PersistentCacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|diskLimit
parameter_list|(
name|long
name|limit
parameter_list|)
block|{
name|checkNotFrozen
argument_list|()
expr_stmt|;
name|diskLimit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|diskLimit ()
specifier|public
name|long
name|diskLimit
parameter_list|()
block|{
return|return
name|diskLimit
return|;
block|}
annotation|@
name|Override
DECL|method|version ()
specifier|public
name|int
name|version
parameter_list|()
block|{
return|return
name|version
return|;
block|}
annotation|@
name|Override
DECL|method|keySerializer ()
specifier|public
name|CacheSerializer
argument_list|<
name|K
argument_list|>
name|keySerializer
parameter_list|()
block|{
return|return
name|keySerializer
return|;
block|}
annotation|@
name|Override
DECL|method|valueSerializer ()
specifier|public
name|CacheSerializer
argument_list|<
name|V
argument_list|>
name|valueSerializer
parameter_list|()
block|{
return|return
name|valueSerializer
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|get
parameter_list|()
block|{
if|if
condition|(
name|persistentCacheFactory
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|get
argument_list|()
return|;
block|}
name|checkState
argument_list|(
name|version
operator|>=
literal|0
argument_list|,
literal|"version is required"
argument_list|)
expr_stmt|;
name|checkSerializer
argument_list|(
name|keyType
argument_list|()
argument_list|,
name|keySerializer
argument_list|,
literal|"key"
argument_list|)
expr_stmt|;
name|checkSerializer
argument_list|(
name|valueType
argument_list|()
argument_list|,
name|valueSerializer
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|freeze
argument_list|()
expr_stmt|;
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ldr
init|=
name|loader
argument_list|()
decl_stmt|;
return|return
name|ldr
operator|!=
literal|null
condition|?
name|persistentCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|,
name|ldr
argument_list|)
else|:
name|persistentCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|checkSerializer ( TypeLiteral<T> type, CacheSerializer<T> serializer, String name)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|checkSerializer
parameter_list|(
name|TypeLiteral
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|CacheSerializer
argument_list|<
name|T
argument_list|>
name|serializer
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|checkState
argument_list|(
name|serializer
operator|!=
literal|null
argument_list|,
literal|"%sSerializer is required"
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|serializer
operator|instanceof
name|JavaCacheSerializer
condition|)
block|{
name|checkState
argument_list|(
name|Serializable
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
operator|.
name|getRawType
argument_list|()
argument_list|)
argument_list|,
literal|"%s type %s must implement Serializable"
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

