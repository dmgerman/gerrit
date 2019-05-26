begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|entities
operator|.
name|Account
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/** Tracks group inclusions in memory for efficient access. */
end_comment

begin_interface
DECL|interface|GroupIncludeCache
specifier|public
interface|interface
name|GroupIncludeCache
block|{
comment|/**    * Returns the UUIDs of all groups of which the specified account is a direct member.    *    * @param memberId the ID of the account    * @return the UUIDs of all groups having the account as member    */
DECL|method|getGroupsWithMember (Account.Id memberId)
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getGroupsWithMember
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
function_decl|;
comment|/**    * Returns the parent groups of a subgroup.    *    * @param groupId the UUID of the subgroup    * @return the UUIDs of all direct parent groups    */
DECL|method|parentGroupsOf (AccountGroup.UUID groupId)
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|parentGroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
function_decl|;
comment|/** @return set of any UUIDs that are not internal groups. */
DECL|method|allExternalMembers ()
name|Collection
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|allExternalMembers
parameter_list|()
function_decl|;
DECL|method|evictGroupsWithMember (Account.Id memberId)
name|void
name|evictGroupsWithMember
parameter_list|(
name|Account
operator|.
name|Id
name|memberId
parameter_list|)
function_decl|;
DECL|method|evictParentGroupsOf (AccountGroup.UUID groupId)
name|void
name|evictParentGroupsOf
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

