begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|base
operator|.
name|Preconditions
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
name|base
operator|.
name|Strings
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
name|gerrit
operator|.
name|extensions
operator|.
name|annotations
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
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
DECL|class|CacheProvider
class|class
name|CacheProvider
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
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
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
DECL|field|module
specifier|private
specifier|final
name|CacheModule
name|module
decl_stmt|;
DECL|field|name
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|keyType
specifier|private
specifier|final
name|TypeLiteral
argument_list|<
name|K
argument_list|>
name|keyType
decl_stmt|;
DECL|field|valType
specifier|private
specifier|final
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valType
decl_stmt|;
DECL|field|persist
specifier|private
name|boolean
name|persist
decl_stmt|;
DECL|field|maximumWeight
specifier|private
name|long
name|maximumWeight
decl_stmt|;
DECL|field|diskLimit
specifier|private
name|long
name|diskLimit
decl_stmt|;
DECL|field|expireAfterWrite
specifier|private
name|Long
name|expireAfterWrite
decl_stmt|;
DECL|field|loader
specifier|private
name|Provider
argument_list|<
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|loader
decl_stmt|;
DECL|field|weigher
specifier|private
name|Provider
argument_list|<
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
argument_list|>
name|weigher
decl_stmt|;
DECL|field|plugin
specifier|private
name|String
name|plugin
decl_stmt|;
DECL|field|memoryCacheFactory
specifier|private
name|MemoryCacheFactory
name|memoryCacheFactory
decl_stmt|;
DECL|field|persistentCacheFactory
specifier|private
name|PersistentCacheFactory
name|persistentCacheFactory
decl_stmt|;
DECL|field|frozen
specifier|private
name|boolean
name|frozen
decl_stmt|;
DECL|method|CacheProvider (CacheModule module, String name, TypeLiteral<K> keyType, TypeLiteral<V> valType)
name|CacheProvider
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
name|this
operator|.
name|module
operator|=
name|module
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
name|this
operator|.
name|valType
operator|=
name|valType
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setPluginName (@luginName String pluginName)
name|void
name|setPluginName
parameter_list|(
annotation|@
name|PluginName
name|String
name|pluginName
parameter_list|)
block|{
name|this
operator|.
name|plugin
operator|=
name|pluginName
expr_stmt|;
block|}
annotation|@
name|Inject
DECL|method|setMemoryCacheFactory (MemoryCacheFactory factory)
name|void
name|setMemoryCacheFactory
parameter_list|(
name|MemoryCacheFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|memoryCacheFactory
operator|=
name|factory
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
DECL|method|persist (boolean p)
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|persist
parameter_list|(
name|boolean
name|p
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|persist
operator|=
name|p
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|maximumWeight (long weight)
specifier|public
name|CacheBinding
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
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|maximumWeight
operator|=
name|weight
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|diskLimit (long limit)
specifier|public
name|CacheBinding
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
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|Preconditions
operator|.
name|checkState
argument_list|(
name|persist
argument_list|,
literal|"diskLimit supported for persistent caches only"
argument_list|)
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
DECL|method|expireAfterWrite (long duration, TimeUnit unit)
specifier|public
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|expireAfterWrite
parameter_list|(
name|long
name|duration
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|expireAfterWrite
operator|=
name|SECONDS
operator|.
name|convert
argument_list|(
name|duration
argument_list|,
name|unit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|loader (Class<? extends CacheLoader<K, V>> impl)
specifier|public
name|CacheBinding
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
name|impl
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|loader
operator|=
name|module
operator|.
name|bindCacheLoader
argument_list|(
name|this
argument_list|,
name|impl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|weigher (Class<? extends Weigher<K, V>> impl)
specifier|public
name|CacheBinding
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
name|impl
parameter_list|)
block|{
name|Preconditions
operator|.
name|checkState
argument_list|(
operator|!
name|frozen
argument_list|,
literal|"binding frozen, cannot be modified"
argument_list|)
expr_stmt|;
name|weigher
operator|=
name|module
operator|.
name|bindWeigher
argument_list|(
name|this
argument_list|,
name|impl
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|name ()
specifier|public
name|String
name|name
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|plugin
argument_list|)
condition|)
block|{
return|return
name|plugin
operator|+
literal|"."
operator|+
name|name
return|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Override
DECL|method|keyType ()
specifier|public
name|TypeLiteral
argument_list|<
name|K
argument_list|>
name|keyType
parameter_list|()
block|{
return|return
name|keyType
return|;
block|}
annotation|@
name|Override
DECL|method|valueType ()
specifier|public
name|TypeLiteral
argument_list|<
name|V
argument_list|>
name|valueType
parameter_list|()
block|{
return|return
name|valType
return|;
block|}
annotation|@
name|Override
DECL|method|maximumWeight ()
specifier|public
name|long
name|maximumWeight
parameter_list|()
block|{
return|return
name|maximumWeight
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
if|if
condition|(
name|diskLimit
operator|>
literal|0
condition|)
block|{
return|return
name|diskLimit
return|;
block|}
return|return
literal|128
operator|<<
literal|20
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|expireAfterWrite (TimeUnit unit)
specifier|public
name|Long
name|expireAfterWrite
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|expireAfterWrite
operator|!=
literal|null
condition|?
name|unit
operator|.
name|convert
argument_list|(
name|expireAfterWrite
argument_list|,
name|SECONDS
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|weigher ()
specifier|public
name|Weigher
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|weigher
parameter_list|()
block|{
return|return
name|weigher
operator|!=
literal|null
condition|?
name|weigher
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|loader ()
specifier|public
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|()
block|{
return|return
name|loader
operator|!=
literal|null
condition|?
name|loader
operator|.
name|get
argument_list|()
else|:
literal|null
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
name|frozen
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|ldr
init|=
name|loader
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|persist
operator|&&
name|persistentCacheFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|persistentCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|,
name|ldr
argument_list|)
return|;
block|}
return|return
name|memoryCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|,
name|ldr
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|persist
operator|&&
name|persistentCacheFactory
operator|!=
literal|null
condition|)
block|{
return|return
name|persistentCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|memoryCacheFactory
operator|.
name|build
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
