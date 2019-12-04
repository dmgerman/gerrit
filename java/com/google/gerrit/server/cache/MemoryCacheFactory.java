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

begin_interface
DECL|interface|MemoryCacheFactory
specifier|public
interface|interface
name|MemoryCacheFactory
block|{
DECL|method|build (CacheDef<K, V> def, CacheBackend backend)
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|Cache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|(
name|CacheDef
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|def
parameter_list|,
name|CacheBackend
name|backend
parameter_list|)
function_decl|;
DECL|method|build ( CacheDef<K, V> def, CacheLoader<K, V> loader, CacheBackend backend)
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
name|LoadingCache
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|build
parameter_list|(
name|CacheDef
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|def
parameter_list|,
name|CacheLoader
argument_list|<
name|K
argument_list|,
name|V
argument_list|>
name|loader
parameter_list|,
name|CacheBackend
name|backend
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

