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
name|account
operator|.
name|externalids
operator|.
name|ExternalIdCacheImpl
operator|.
name|AllExternalIds
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
name|account
operator|.
name|externalids
operator|.
name|ExternalIdCacheImpl
operator|.
name|Loader
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
name|inject
operator|.
name|TypeLiteral
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
name|cache
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
comment|// in the latest value, hence the maximum cache weight is set to 1.
comment|// This can lead to extra cache loads in case of the following race:
comment|// 1. thread 1 reads the notes ref at revision A
comment|// 2. thread 2 updates the notes ref to revision B and stores the derived value
comment|//    for B in the cache
comment|// 3. thread 1 attempts to read the data for revision A from the cache, and misses
comment|// 4. later threads attempt to read at B
comment|// In this race unneeded reloads are done in step 3 (reload from revision A) and
comment|// step 4 (reload from revision B, because the value for revision B was lost when the
comment|// reload from revision A was done, since the cache can hold only one entry).
comment|// These reloads could be avoided by increasing the cache size to 2. However the race
comment|// window between reading the ref and looking it up in the cache is small so that
comment|// it's rare that this race happens. Therefore it's not worth to double the memory
comment|// usage of this cache, just to avoid this.
operator|.
name|maximumWeight
argument_list|(
literal|1
argument_list|)
operator|.
name|loader
argument_list|(
name|Loader
operator|.
name|class
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

