begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
operator|.
name|group
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|errors
operator|.
name|NoSuchGroupException
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|GerritPersonIdent
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
name|Sequences
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
name|ServerInitiated
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
name|GroupUUID
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
operator|.
name|Groups
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
name|db
operator|.
name|GroupsUpdate
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
name|db
operator|.
name|InternalGroupCreation
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
name|db
operator|.
name|InternalGroupUpdate
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmDuplicateKeyException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|Inject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
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
name|PersonIdent
import|;
end_import

begin_comment
comment|/**  * The implementation of {@code GroupOperations}.  *  *<p>There is only one implementation of {@code GroupOperations}. Nevertheless, we keep the  * separation between interface and implementation to enhance clarity.  */
end_comment

begin_class
DECL|class|GroupOperationsImpl
specifier|public
class|class
name|GroupOperationsImpl
implements|implements
name|GroupOperations
block|{
DECL|field|groups
specifier|private
specifier|final
name|Groups
name|groups
decl_stmt|;
DECL|field|groupsUpdate
specifier|private
specifier|final
name|GroupsUpdate
name|groupsUpdate
decl_stmt|;
DECL|field|seq
specifier|private
specifier|final
name|Sequences
name|seq
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|GroupOperationsImpl ( Groups groups, @ServerInitiated GroupsUpdate groupsUpdate, Sequences seq, @GerritPersonIdent PersonIdent serverIdent)
specifier|public
name|GroupOperationsImpl
parameter_list|(
name|Groups
name|groups
parameter_list|,
annotation|@
name|ServerInitiated
name|GroupsUpdate
name|groupsUpdate
parameter_list|,
name|Sequences
name|seq
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|groups
expr_stmt|;
name|this
operator|.
name|groupsUpdate
operator|=
name|groupsUpdate
expr_stmt|;
name|this
operator|.
name|seq
operator|=
name|seq
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|group (AccountGroup.UUID groupUuid)
specifier|public
name|MoreGroupOperations
name|group
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
return|return
operator|new
name|MoreGroupOperationsImpl
argument_list|(
name|groupUuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newGroup ()
specifier|public
name|TestGroupCreation
operator|.
name|Builder
name|newGroup
parameter_list|()
block|{
return|return
name|TestGroupCreation
operator|.
name|builder
argument_list|(
name|this
operator|::
name|createNewGroup
argument_list|)
return|;
block|}
DECL|method|createNewGroup (TestGroupCreation groupCreation)
specifier|private
name|AccountGroup
operator|.
name|UUID
name|createNewGroup
parameter_list|(
name|TestGroupCreation
name|groupCreation
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|IOException
throws|,
name|OrmException
block|{
name|InternalGroupCreation
name|internalGroupCreation
init|=
name|toInternalGroupCreation
argument_list|(
name|groupCreation
argument_list|)
decl_stmt|;
name|InternalGroupUpdate
name|internalGroupUpdate
init|=
name|toInternalGroupUpdate
argument_list|(
name|groupCreation
argument_list|)
decl_stmt|;
name|InternalGroup
name|internalGroup
init|=
name|groupsUpdate
operator|.
name|createGroup
argument_list|(
name|internalGroupCreation
argument_list|,
name|internalGroupUpdate
argument_list|)
decl_stmt|;
return|return
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
DECL|method|toInternalGroupCreation (TestGroupCreation groupCreation)
specifier|private
name|InternalGroupCreation
name|toInternalGroupCreation
parameter_list|(
name|TestGroupCreation
name|groupCreation
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
operator|.
name|Id
name|groupId
init|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|(
name|seq
operator|.
name|nextGroupId
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|groupName
init|=
name|groupCreation
operator|.
name|name
argument_list|()
operator|.
name|orElse
argument_list|(
literal|"group-with-id-"
operator|+
name|groupId
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|GroupUUID
operator|.
name|make
argument_list|(
name|groupName
argument_list|,
name|serverIdent
argument_list|)
decl_stmt|;
name|AccountGroup
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|groupName
argument_list|)
decl_stmt|;
return|return
name|InternalGroupCreation
operator|.
name|builder
argument_list|()
operator|.
name|setId
argument_list|(
name|groupId
argument_list|)
operator|.
name|setGroupUUID
argument_list|(
name|groupUuid
argument_list|)
operator|.
name|setNameKey
argument_list|(
name|nameKey
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|toInternalGroupUpdate (TestGroupCreation groupCreation)
specifier|private
specifier|static
name|InternalGroupUpdate
name|toInternalGroupUpdate
parameter_list|(
name|TestGroupCreation
name|groupCreation
parameter_list|)
block|{
name|InternalGroupUpdate
operator|.
name|Builder
name|builder
init|=
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
decl_stmt|;
name|groupCreation
operator|.
name|description
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setDescription
argument_list|)
expr_stmt|;
name|groupCreation
operator|.
name|ownerGroupUuid
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setOwnerGroupUUID
argument_list|)
expr_stmt|;
name|groupCreation
operator|.
name|visibleToAll
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setVisibleToAll
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setMemberModification
argument_list|(
name|originalMembers
lambda|->
name|groupCreation
operator|.
name|members
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSubgroupModification
argument_list|(
name|originalSubgroups
lambda|->
name|groupCreation
operator|.
name|subgroups
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|class|MoreGroupOperationsImpl
specifier|private
class|class
name|MoreGroupOperationsImpl
implements|implements
name|MoreGroupOperations
block|{
DECL|field|groupUuid
specifier|private
specifier|final
name|AccountGroup
operator|.
name|UUID
name|groupUuid
decl_stmt|;
DECL|method|MoreGroupOperationsImpl (AccountGroup.UUID groupUuid)
name|MoreGroupOperationsImpl
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|)
block|{
name|this
operator|.
name|groupUuid
operator|=
name|groupUuid
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exists ()
specifier|public
name|boolean
name|exists
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|groups
operator|.
name|getGroup
argument_list|(
name|groupUuid
argument_list|)
operator|.
name|isPresent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|TestGroup
name|get
parameter_list|()
throws|throws
name|Exception
block|{
name|Optional
argument_list|<
name|InternalGroup
argument_list|>
name|group
init|=
name|groups
operator|.
name|getGroup
argument_list|(
name|groupUuid
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|group
operator|.
name|isPresent
argument_list|()
argument_list|,
literal|"Tried to get non-existing test group"
argument_list|)
expr_stmt|;
return|return
name|toTestGroup
argument_list|(
name|group
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toTestGroup (InternalGroup internalGroup)
specifier|private
name|TestGroup
name|toTestGroup
parameter_list|(
name|InternalGroup
name|internalGroup
parameter_list|)
block|{
return|return
name|TestGroup
operator|.
name|builder
argument_list|()
operator|.
name|groupUuid
argument_list|(
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
operator|.
name|groupId
argument_list|(
name|internalGroup
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|nameKey
argument_list|(
name|internalGroup
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|description
argument_list|(
name|Optional
operator|.
name|ofNullable
argument_list|(
name|internalGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
argument_list|)
operator|.
name|ownerGroupUuid
argument_list|(
name|internalGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
operator|.
name|visibleToAll
argument_list|(
name|internalGroup
operator|.
name|isVisibleToAll
argument_list|()
argument_list|)
operator|.
name|createdOn
argument_list|(
name|internalGroup
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
operator|.
name|members
argument_list|(
name|internalGroup
operator|.
name|getMembers
argument_list|()
argument_list|)
operator|.
name|subgroups
argument_list|(
name|internalGroup
operator|.
name|getSubgroups
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|forUpdate ()
specifier|public
name|TestGroupUpdate
operator|.
name|Builder
name|forUpdate
parameter_list|()
block|{
return|return
name|TestGroupUpdate
operator|.
name|builder
argument_list|(
name|this
operator|::
name|updateGroup
argument_list|)
return|;
block|}
DECL|method|updateGroup (TestGroupUpdate groupUpdate)
specifier|private
name|void
name|updateGroup
parameter_list|(
name|TestGroupUpdate
name|groupUpdate
parameter_list|)
throws|throws
name|OrmDuplicateKeyException
throws|,
name|NoSuchGroupException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|InternalGroupUpdate
name|internalGroupUpdate
init|=
name|toInternalGroupUpdate
argument_list|(
name|groupUpdate
argument_list|)
decl_stmt|;
name|groupsUpdate
operator|.
name|updateGroup
argument_list|(
name|groupUuid
argument_list|,
name|internalGroupUpdate
argument_list|)
expr_stmt|;
block|}
DECL|method|toInternalGroupUpdate (TestGroupUpdate groupUpdate)
specifier|private
name|InternalGroupUpdate
name|toInternalGroupUpdate
parameter_list|(
name|TestGroupUpdate
name|groupUpdate
parameter_list|)
block|{
name|InternalGroupUpdate
operator|.
name|Builder
name|builder
init|=
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
decl_stmt|;
name|groupUpdate
operator|.
name|name
argument_list|()
operator|.
name|map
argument_list|(
name|AccountGroup
operator|.
name|NameKey
operator|::
operator|new
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setName
argument_list|)
expr_stmt|;
name|groupUpdate
operator|.
name|description
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setDescription
argument_list|)
expr_stmt|;
name|groupUpdate
operator|.
name|ownerGroupUuid
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setOwnerGroupUUID
argument_list|)
expr_stmt|;
name|groupUpdate
operator|.
name|visibleToAll
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|setVisibleToAll
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setMemberModification
argument_list|(
name|groupUpdate
operator|.
name|memberModification
argument_list|()
operator|::
name|apply
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setSubgroupModification
argument_list|(
name|groupUpdate
operator|.
name|subgroupModification
argument_list|()
operator|::
name|apply
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
