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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|client
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
comment|// TODO(aliceks): Add Javadoc descriptions to this file.
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
annotation|@
name|FunctionalInterface
DECL|interface|MemberModification
specifier|public
interface|interface
name|MemberModification
block|{
DECL|method|apply (ImmutableSet<Account.Id> in)
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
name|in
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
DECL|method|apply (ImmutableSet<AccountGroup.UUID> in)
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
name|in
parameter_list|)
function_decl|;
block|}
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
comment|// TODO(aliceks): Mention empty string (not null!) -> unset value in Javadoc.
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
DECL|method|getMemberModification ()
specifier|public
specifier|abstract
name|MemberModification
name|getMemberModification
parameter_list|()
function_decl|;
DECL|method|getSubgroupModification ()
specifier|public
specifier|abstract
name|SubgroupModification
name|getSubgroupModification
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
DECL|method|getMemberModification ()
specifier|abstract
name|MemberModification
name|getMemberModification
parameter_list|()
function_decl|;
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
DECL|method|getSubgroupModification ()
specifier|abstract
name|SubgroupModification
name|getSubgroupModification
parameter_list|()
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

