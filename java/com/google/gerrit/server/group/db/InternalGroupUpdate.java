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
DECL|package|com.google.gerrit.server.group.db
package|package
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
name|db
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|collect
operator|.
name|ImmutableSet
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
name|sql
operator|.
name|Timestamp
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Definition of an update to a group.  *  *<p>An {@code InternalGroupUpdate} only specifies the modifications which should be applied to a  * group. Each of the modifications and hence each call on {@link InternalGroupUpdate.Builder} is  * optional.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|InternalGroupUpdate
specifier|public
specifier|abstract
class|class
name|InternalGroupUpdate
block|{
comment|/** Representation of a member modification as defined by {@link #apply(ImmutableSet)}. */
annotation|@
name|FunctionalInterface
DECL|interface|MemberModification
specifier|public
interface|interface
name|MemberModification
block|{
comment|/**      * Applies the modification to the given members.      *      * @param originalMembers current members of the group. If used for a group creation, this set      *     is empty.      * @return the desired resulting members (not the diff of the members!)      */
DECL|method|apply (ImmutableSet<Account.Id> originalMembers)
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|apply
parameter_list|(
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|originalMembers
parameter_list|)
function_decl|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|SubgroupModification
specifier|public
interface|interface
name|SubgroupModification
block|{
comment|/**      * Applies the modification to the given subgroups.      *      * @param originalSubgroups current subgroups of the group. If used for a group creation, this      *     set is empty.      * @return the desired resulting subgroups (not the diff of the subgroups!)      */
DECL|method|apply (ImmutableSet<AccountGroup.UUID> originalSubgroups)
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|apply
parameter_list|(
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|originalSubgroups
parameter_list|)
function_decl|;
block|}
comment|/** Defines the new name of the group. If not specified, the name remains unchanged. */
DECL|method|getName ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|NameKey
argument_list|>
name|getName
parameter_list|()
function_decl|;
comment|/**    * Defines the new description of the group. If not specified, the description remains unchanged.    *    *<p><strong>Note:</strong>Passing the empty string unsets the description.    */
DECL|method|getDescription ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|getDescription
parameter_list|()
function_decl|;
comment|/** Defines the new owner of the group. If not specified, the owner remains unchanged. */
DECL|method|getOwnerGroupUUID ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getOwnerGroupUUID
parameter_list|()
function_decl|;
comment|/**    * Defines the new state of the 'visibleToAll' flag of the group. If not specified, the flag    * remains unchanged.    */
DECL|method|getVisibleToAll ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Boolean
argument_list|>
name|getVisibleToAll
parameter_list|()
function_decl|;
comment|/**    * Defines how the members of the group should be modified. By default (that is if nothing is    * specified), the members remain unchanged.    *    * @return a {@link MemberModification} which gets the current members of the group as input and    *     outputs the desired resulting members    */
DECL|method|getMemberModification ()
specifier|public
specifier|abstract
name|MemberModification
name|getMemberModification
parameter_list|()
function_decl|;
comment|/**    * Defines how the subgroups of the group should be modified. By default (that is if nothing is    * specified), the subgroups remain unchanged.    *    * @return a {@link SubgroupModification} which gets the current subgroups of the group as input    *     and outputs the desired resulting subgroups    */
DECL|method|getSubgroupModification ()
specifier|public
specifier|abstract
name|SubgroupModification
name|getSubgroupModification
parameter_list|()
function_decl|;
comment|/**    * Defines the {@code Timestamp} to be used for the NoteDb commits of the update. If not    * specified, the current {@code Timestamp} when creating the commit will be used.    *    *<p>If this {@code InternalGroupUpdate} is passed next to an {@link InternalGroupCreation}    * during a group creation, this {@code Timestamp} is used for the NoteDb commits of the new    * group. Hence, the {@link com.google.gerrit.server.group.InternalGroup#getCreatedOn()    * InternalGroup#getCreatedOn()} field will match this {@code Timestamp}.    *    *<p><strong>Note:</strong>{@code Timestamp}s of NoteDb commits for groups are used for events    * in the audit log. For this reason, specifying this field will have an effect on the resulting    * audit log.    */
DECL|method|getUpdatedOn ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|Timestamp
argument_list|>
name|getUpdatedOn
parameter_list|()
function_decl|;
DECL|method|toBuilder ()
specifier|public
specifier|abstract
name|Builder
name|toBuilder
parameter_list|()
function_decl|;
DECL|method|builder ()
specifier|public
specifier|static
name|Builder
name|builder
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_InternalGroupUpdate
operator|.
name|Builder
argument_list|()
operator|.
name|setMemberModification
argument_list|(
name|in
lambda|->
name|in
argument_list|)
operator|.
name|setSubgroupModification
argument_list|(
name|in
lambda|->
name|in
argument_list|)
return|;
block|}
comment|/** A builder for an {@link InternalGroupUpdate}. */
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
comment|/** @see #getName() */
DECL|method|setName (AccountGroup.NameKey name)
specifier|public
specifier|abstract
name|Builder
name|setName
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|)
function_decl|;
comment|/** @see #getDescription() */
DECL|method|setDescription (String description)
specifier|public
specifier|abstract
name|Builder
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
function_decl|;
comment|/** @see #getOwnerGroupUUID() */
DECL|method|setOwnerGroupUUID (AccountGroup.UUID ownerGroupUUID)
specifier|public
specifier|abstract
name|Builder
name|setOwnerGroupUUID
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|ownerGroupUUID
parameter_list|)
function_decl|;
comment|/** @see #getVisibleToAll() */
DECL|method|setVisibleToAll (boolean visibleToAll)
specifier|public
specifier|abstract
name|Builder
name|setVisibleToAll
parameter_list|(
name|boolean
name|visibleToAll
parameter_list|)
function_decl|;
comment|/** @see #getMemberModification() */
DECL|method|setMemberModification (MemberModification memberModification)
specifier|public
specifier|abstract
name|Builder
name|setMemberModification
parameter_list|(
name|MemberModification
name|memberModification
parameter_list|)
function_decl|;
comment|/**      * Returns the currently defined {@link MemberModification} for the prospective {@link      * InternalGroupUpdate}.      *      *<p>This modification can be tweaked further and passed to {@link      * #setMemberModification(InternalGroupUpdate.MemberModification)} in order to combine multiple      * member additions, deletions, or other modifications into one update.      */
DECL|method|getMemberModification ()
specifier|public
specifier|abstract
name|MemberModification
name|getMemberModification
parameter_list|()
function_decl|;
comment|/** @see #getSubgroupModification() */
DECL|method|setSubgroupModification (SubgroupModification subgroupModification)
specifier|public
specifier|abstract
name|Builder
name|setSubgroupModification
parameter_list|(
name|SubgroupModification
name|subgroupModification
parameter_list|)
function_decl|;
comment|/**      * Returns the currently defined {@link SubgroupModification} for the prospective {@link      * InternalGroupUpdate}.      *      *<p>This modification can be tweaked further and passed to {@link      * #setSubgroupModification(InternalGroupUpdate.SubgroupModification)} in order to combine      * multiple subgroup additions, deletions, or other modifications into one update.      */
DECL|method|getSubgroupModification ()
specifier|public
specifier|abstract
name|SubgroupModification
name|getSubgroupModification
parameter_list|()
function_decl|;
comment|/** @see #getUpdatedOn() */
DECL|method|setUpdatedOn (Timestamp timestamp)
specifier|public
specifier|abstract
name|Builder
name|setUpdatedOn
parameter_list|(
name|Timestamp
name|timestamp
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|InternalGroupUpdate
name|build
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

