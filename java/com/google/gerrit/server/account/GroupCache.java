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
name|server
operator|.
name|group
operator|.
name|InternalGroup
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/** Tracks group objects in memory for efficient access. */
end_comment

begin_interface
DECL|interface|GroupCache
specifier|public
interface|interface
name|GroupCache
block|{
comment|/**    * Looks up an internal group by its ID.    *    * @param groupId the ID of the internal group    * @return an {@code Optional} of the internal group, or an empty {@code Optional} if no internal    *     group with this ID exists on this server or an error occurred during lookup    */
DECL|method|get (AccountGroup.Id groupId)
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
function_decl|;
comment|/**    * Looks up an internal group by its name.    *    * @param name the name of the internal group    * @return an {@code Optional} of the internal group, or an empty {@code Optional} if no internal    *     group with this name exists on this server or an error occurred during lookup    */
DECL|method|get (AccountGroup.NameKey name)
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
function_decl|;
comment|/**    * Looks up an internal group by its UUID.    *    * @param groupUuid the UUID of the internal group    * @return an {@code Optional} of the internal group, or an empty {@code Optional} if no internal    *     group with this UUID exists on this server or an error occurred during lookup    */
DECL|method|get (AccountGroup.UUID groupUuid)
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|get
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
comment|/**    * Removes the association of the given ID with a group.    *    *<p>The next call to {@link #get(AccountGroup.Id)} won't provide a cached value.    *    *<p>It's safe to call this method if no association exists.    *    *<p><strong>Note:</strong>This method doesn't touch any associations between names/UUIDs and    * groups!    *    * @param groupId the ID of a possibly associated group    */
DECL|method|evict (AccountGroup.Id groupId)
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
function_decl|;
comment|/**    * Removes the association of the given name with a group.    *    *<p>The next call to {@link #get(AccountGroup.NameKey)} won't provide a cached value.    *    *<p>It's safe to call this method if no association exists.    *    *<p><strong>Note:</strong>This method doesn't touch any associations between IDs/UUIDs and    * groups!    *    * @param groupName the name of a possibly associated group    */
DECL|method|evict (AccountGroup.NameKey groupName)
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|groupName
parameter_list|)
function_decl|;
comment|/**    * Removes the association of the given UUID with a group.    *    *<p>The next call to {@link #get(AccountGroup.UUID)} won't provide a cached value.    *    *<p>It's safe to call this method if no association exists.    *    *<p><strong>Note:</strong>This method doesn't touch any associations between names/IDs and    * groups!    *    * @param groupUuid the UUID of a possibly associated group    */
DECL|method|evict (AccountGroup.UUID groupUuid)
name|void
name|evict
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

