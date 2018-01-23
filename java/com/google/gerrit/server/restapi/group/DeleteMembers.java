begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|MethodNotAllowedException
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestModifyView
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
name|restapi
operator|.
name|UnprocessableEntityException
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|UserInitiated
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
name|GroupControl
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
name|GroupResource
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
name|MemberResource
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
name|InternalGroupUpdate
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
name|restapi
operator|.
name|account
operator|.
name|AccountsCollection
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
name|restapi
operator|.
name|group
operator|.
name|AddMembers
operator|.
name|Input
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|Singleton
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
name|HashSet
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteMembers
specifier|public
class|class
name|DeleteMembers
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|groupsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteMembers ( AccountsCollection accounts, Provider<ReviewDb> db, @UserInitiated Provider<GroupsUpdate> groupsUpdateProvider)
name|DeleteMembers
parameter_list|(
name|AccountsCollection
name|accounts
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
annotation|@
name|UserInitiated
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdateProvider
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|groupsUpdateProvider
operator|=
name|groupsUpdateProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|ResourceNotFoundException
block|{
name|GroupDescription
operator|.
name|Internal
name|internalGroup
init|=
name|resource
operator|.
name|asInternalGroup
argument_list|()
operator|.
name|orElseThrow
argument_list|(
name|MethodNotAllowedException
operator|::
operator|new
argument_list|)
decl_stmt|;
name|input
operator|=
name|Input
operator|.
name|init
argument_list|(
name|input
argument_list|)
expr_stmt|;
specifier|final
name|GroupControl
name|control
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canRemoveMember
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot delete members from group "
operator|+
name|internalGroup
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|membersToRemove
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|nameOrEmail
range|:
name|input
operator|.
name|members
control|)
block|{
name|Account
name|a
init|=
name|accounts
operator|.
name|parse
argument_list|(
name|nameOrEmail
argument_list|)
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|membersToRemove
operator|.
name|add
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|AccountGroup
operator|.
name|UUID
name|groupUuid
init|=
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
decl_stmt|;
try|try
block|{
name|removeGroupMembers
argument_list|(
name|groupUuid
argument_list|,
name|membersToRemove
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Group %s not found"
argument_list|,
name|groupUuid
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
DECL|method|removeGroupMembers (AccountGroup.UUID groupUuid, Set<Account.Id> accountIds)
specifier|private
name|void
name|removeGroupMembers
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
throws|,
name|NoSuchGroupException
throws|,
name|ConfigInvalidException
block|{
name|InternalGroupUpdate
name|groupUpdate
init|=
name|InternalGroupUpdate
operator|.
name|builder
argument_list|()
operator|.
name|setMemberModification
argument_list|(
name|memberIds
lambda|->
name|Sets
operator|.
name|difference
argument_list|(
name|memberIds
argument_list|,
name|accountIds
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|groupsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|updateGroup
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|groupUuid
argument_list|,
name|groupUpdate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Singleton
DECL|class|DeleteMember
specifier|static
class|class
name|DeleteMember
implements|implements
name|RestModifyView
argument_list|<
name|MemberResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|delete
specifier|private
specifier|final
name|Provider
argument_list|<
name|DeleteMembers
argument_list|>
name|delete
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteMember (Provider<DeleteMembers> delete)
name|DeleteMember
parameter_list|(
name|Provider
argument_list|<
name|DeleteMembers
argument_list|>
name|delete
parameter_list|)
block|{
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (MemberResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|MemberResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|ResourceNotFoundException
block|{
name|AddMembers
operator|.
name|Input
name|in
init|=
operator|new
name|AddMembers
operator|.
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|_oneMember
operator|=
name|resource
operator|.
name|getMember
argument_list|()
operator|.
name|getAccountId
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|delete
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|resource
argument_list|,
name|in
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

