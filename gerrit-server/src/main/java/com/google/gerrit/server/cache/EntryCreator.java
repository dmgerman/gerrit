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

begin_comment
comment|/**  * Creates a cache entry on demand when its not found.  *  * @param<K> type of the cache's key.  * @param<V> type of the cache's value element.  */
end_comment

begin_class
DECL|class|EntryCreator
specifier|public
specifier|abstract
class|class
name|EntryCreator
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/**    * Invoked on a cache miss, to compute the cache entry.    *    * @param key entry whose content needs to be obtained.    * @return new cache content. The caller will automatically put this object    *         into the cache.    * @throws Exception the cache content cannot be computed. No entry will be    *         stored in the cache, and {@link #missing(Object)} will be invoked    *         instead. Future requests for the same key will retry this method.    */
DECL|method|createEntry (K key)
specifier|public
specifier|abstract
name|V
name|createEntry
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/** Invoked when {@link #createEntry(Object)} fails, by default return null. */
DECL|method|missing (K key)
specifier|public
name|V
name|missing
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

