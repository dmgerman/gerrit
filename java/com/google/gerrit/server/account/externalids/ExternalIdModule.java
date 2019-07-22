begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account.externalids
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
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
name|serialize
operator|.
name|ObjectIdCacheSerializer
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
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
DECL|class|ExternalIdModule
specifier|public
class|class
name|ExternalIdModule
extends|extends
name|CacheModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|persist
argument_list|(
name|ExternalIdCacheImpl
operator|.
name|CACHE_NAME
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|AllExternalIds
argument_list|>
argument_list|()
block|{}
argument_list|)
comment|// The cached data is potentially pretty large and we are always only interested
comment|// in the latest value. However, due to a race condition, it is possible for different
comment|// threads to observe different values of the meta ref, and hence request different keys
comment|// from the cache. Extend the cache size by 1 to cover this case, but expire the extra
comment|// object after a short period of time, since it may be a potentially large amount of
comment|// memory.
comment|// When loading a new value because the primary data advanced, we want to leverage the old
comment|// cache state to recompute only what changed. This doesn't affect cache size though as
comment|// Guava calls the loader first and evicts later on.
operator|.
name|maximumWeight
argument_list|(
literal|2
argument_list|)
operator|.
name|expireFromMemoryAfterAccess
argument_list|(
name|Duration
operator|.
name|ofMinutes
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|loader
argument_list|(
name|ExternalIdCacheLoader
operator|.
name|class
argument_list|)
operator|.
name|diskLimit
argument_list|(
operator|-
literal|1
argument_list|)
operator|.
name|version
argument_list|(
literal|1
argument_list|)
operator|.
name|keySerializer
argument_list|(
name|ObjectIdCacheSerializer
operator|.
name|INSTANCE
argument_list|)
operator|.
name|valueSerializer
argument_list|(
name|AllExternalIds
operator|.
name|Serializer
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ExternalIdCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ExternalIdCache
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ExternalIdCacheImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

