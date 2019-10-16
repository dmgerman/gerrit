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
DECL|package|com.google.gerrit.server.account
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|data
operator|.
name|GroupReference
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
name|entities
operator|.
name|AccountGroup
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
name|ExtensionPoint
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
name|IdentifiedUser
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
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/** Implementations of GroupBackend provide lookup and membership accessors to a group system. */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|GroupBackend
specifier|public
interface|interface
name|GroupBackend
block|{
comment|/** @return {@code true} if the backend can operate on the UUID. */
DECL|method|handles (AccountGroup.UUID uuid)
name|boolean
name|handles
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
function_decl|;
comment|/**    * Looks up a group in the backend. If the group does not exist, null is returned.    *    * @param uuid the group identifier    * @return the group    */
annotation|@
name|Nullable
DECL|method|get (AccountGroup.UUID uuid)
name|GroupDescription
operator|.
name|Basic
name|get
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
function_decl|;
comment|/** @return suggestions for the group name sorted by name. */
DECL|method|suggest (String name, @Nullable ProjectState project)
name|Collection
argument_list|<
name|GroupReference
argument_list|>
name|suggest
parameter_list|(
name|String
name|name
parameter_list|,
annotation|@
name|Nullable
name|ProjectState
name|project
parameter_list|)
function_decl|;
comment|/** @return the group membership checker for the backend. */
DECL|method|membershipsOf (IdentifiedUser user)
name|GroupMembership
name|membershipsOf
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|)
function_decl|;
comment|/** @return {@code true} if the group with the given UUID is visible to all registered users. */
DECL|method|isVisibleToAll (AccountGroup.UUID uuid)
name|boolean
name|isVisibleToAll
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

