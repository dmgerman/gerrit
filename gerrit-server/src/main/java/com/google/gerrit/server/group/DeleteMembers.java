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
DECL|package|com.google.gerrit.server.group
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
name|audit
operator|.
name|AuditService
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
name|client
operator|.
name|AccountGroupMember
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
name|CurrentUser
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
name|AccountCache
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
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
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|auditService
specifier|private
specifier|final
name|AuditService
name|auditService
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteMembers ( AccountsCollection accounts, AccountCache accountCache, Provider<ReviewDb> db, Provider<CurrentUser> self, AuditService auditService)
name|DeleteMembers
parameter_list|(
name|AccountsCollection
name|accounts
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|AuditService
name|auditService
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
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
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
block|{
name|AccountGroup
name|internalGroup
init|=
name|resource
operator|.
name|toAccountGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|internalGroup
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|()
throw|;
block|}
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
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountGroupMember
argument_list|>
name|members
init|=
name|getMembers
argument_list|(
name|internalGroup
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|toRemove
init|=
operator|new
name|ArrayList
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
literal|"Cannot delete member: "
operator|+
name|a
operator|.
name|getFullName
argument_list|()
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroupMember
name|m
init|=
name|members
operator|.
name|remove
argument_list|(
name|a
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|toRemove
operator|.
name|add
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
block|}
name|writeAudits
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|delete
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupMember
name|m
range|:
name|toRemove
control|)
block|{
name|accountCache
operator|.
name|evict
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
DECL|method|writeAudits (List<AccountGroupMember> toRemove)
specifier|private
name|void
name|writeAudits
parameter_list|(
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|toRemove
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|me
init|=
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|auditService
operator|.
name|dispatchDeleteAccountsFromGroup
argument_list|(
name|me
argument_list|,
name|toRemove
argument_list|)
expr_stmt|;
block|}
DECL|method|getMembers (AccountGroup.Id groupId)
specifier|private
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountGroupMember
argument_list|>
name|getMembers
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountGroupMember
argument_list|>
name|members
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountGroupMember
name|m
range|:
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byGroup
argument_list|(
name|groupId
argument_list|)
control|)
block|{
name|members
operator|.
name|put
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
return|return
name|members
return|;
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
name|DeleteMember
operator|.
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{}
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

