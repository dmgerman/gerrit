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
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_comment
comment|/** Configure a persistent cache declared within a {@link CacheModule} instance. */
end_comment

begin_interface
DECL|interface|PersistentCacheBinding
specifier|public
interface|interface
name|PersistentCacheBinding
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
extends|extends
name|CacheBinding
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
block|{
annotation|@
name|Override
DECL|method|maximumWeight (long weight)
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
function_decl|;
annotation|@
name|Override
DECL|method|expireAfterWrite (Duration duration)
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
function_decl|;
annotation|@
name|Override
DECL|method|loader (Class<? extends CacheLoader<K, V>> clazz)
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
function_decl|;
annotation|@
name|Override
DECL|method|weigher (Class<? extends Weigher<K, V>> clazz)
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
function_decl|;
DECL|method|version (int version)
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
function_decl|;
comment|/**    * Set the total on-disk limit of the cache.    *    *<p>If 0 or negative, persistence for the cache is disabled by default, but may still be    * overridden in the config.    */
DECL|method|diskLimit (long limit)
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
function_decl|;
DECL|method|keySerializer (CacheSerializer<K> keySerializer)
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
function_decl|;
DECL|method|valueSerializer (CacheSerializer<V> valueSerializer)
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
function_decl|;
block|}
end_interface

end_unit

