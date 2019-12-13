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
DECL|package|com.google.gerrit.server.index.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|group
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
name|entities
operator|.
name|AccountGroup
import|;
end_import

begin_comment
comment|/** Interface for indexing an internal Gerrit group. */
end_comment

begin_interface
DECL|interface|GroupIndexer
specifier|public
interface|interface
name|GroupIndexer
block|{
comment|/**    * Synchronously index a group.    *    * @param uuid group UUID to index.    */
DECL|method|index (AccountGroup.UUID uuid)
name|void
name|index
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
function_decl|;
comment|/**    * Synchronously reindex a group if it is stale.    *    * @param uuid group UUID to index.    * @return whether the group was reindexed    */
DECL|method|reindexIfStale (AccountGroup.UUID uuid)
name|boolean
name|reindexIfStale
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

