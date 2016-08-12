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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|Lists
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
name|common
operator|.
name|AccountInfo
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
name|DefaultInput
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
name|client
operator|.
name|AuthType
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
name|AccountException
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
name|AccountLoader
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
name|AccountManager
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
name|AccountResolver
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
name|AuthRequest
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
name|config
operator|.
name|AuthConfig
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
name|Collection
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|AddMembers
specifier|public
class|class
name|AddMembers
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
annotation|@
name|DefaultInput
DECL|field|_oneMember
name|String
name|_oneMember
decl_stmt|;
DECL|field|members
name|List
argument_list|<
name|String
argument_list|>
name|members
decl_stmt|;
DECL|method|fromMembers (List<String> members)
specifier|public
specifier|static
name|Input
name|fromMembers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|members
parameter_list|)
block|{
name|Input
name|in
init|=
operator|new
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|members
operator|=
name|members
expr_stmt|;
return|return
name|in
return|;
block|}
DECL|method|init (Input in)
specifier|static
name|Input
name|init
parameter_list|(
name|Input
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
operator|new
name|Input
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|members
operator|==
literal|null
condition|)
block|{
name|in
operator|.
name|members
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|in
operator|.
name|_oneMember
argument_list|)
condition|)
block|{
name|in
operator|.
name|members
operator|.
name|add
argument_list|(
name|in
operator|.
name|_oneMember
argument_list|)
expr_stmt|;
block|}
return|return
name|in
return|;
block|}
block|}
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
decl_stmt|;
DECL|field|accountManager
specifier|private
specifier|final
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|authType
specifier|private
specifier|final
name|AuthType
name|authType
decl_stmt|;
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
decl_stmt|;
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|infoFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|infoFactory
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
DECL|field|auditService
specifier|private
specifier|final
name|AuditService
name|auditService
decl_stmt|;
annotation|@
name|Inject
DECL|method|AddMembers (Provider<IdentifiedUser> self, AccountManager accountManager, AuthConfig authConfig, AccountsCollection accounts, AccountResolver accountResolver, AccountCache accountCache, AccountLoader.Factory infoFactory, Provider<ReviewDb> db, AuditService auditService)
name|AddMembers
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
parameter_list|,
name|AccountManager
name|accountManager
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
name|AccountsCollection
name|accounts
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|infoFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AuditService
name|auditService
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|accountManager
operator|=
name|accountManager
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
expr_stmt|;
name|this
operator|.
name|authType
operator|=
name|authConfig
operator|.
name|getAuthType
argument_list|()
expr_stmt|;
name|this
operator|.
name|accounts
operator|=
name|accounts
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|infoFactory
operator|=
name|infoFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, Input input)
specifier|public
name|List
argument_list|<
name|AccountInfo
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
name|GroupControl
name|control
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|newMemberIds
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
name|findAccount
argument_list|(
name|nameOrEmail
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|a
operator|.
name|isActive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Account Inactive: %s"
argument_list|,
name|nameOrEmail
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|control
operator|.
name|canAddMember
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Cannot add member: "
operator|+
name|a
operator|.
name|getFullName
argument_list|()
argument_list|)
throw|;
block|}
name|newMemberIds
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
name|addMembers
argument_list|(
name|internalGroup
operator|.
name|getId
argument_list|()
argument_list|,
name|newMemberIds
argument_list|)
expr_stmt|;
return|return
name|toAccountInfoList
argument_list|(
name|newMemberIds
argument_list|)
return|;
block|}
DECL|method|findAccount (String nameOrEmail)
specifier|private
name|Account
name|findAccount
parameter_list|(
name|String
name|nameOrEmail
parameter_list|)
throws|throws
name|AuthException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
throws|,
name|IOException
block|{
try|try
block|{
return|return
name|accounts
operator|.
name|parse
argument_list|(
name|nameOrEmail
argument_list|)
operator|.
name|getAccount
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e
parameter_list|)
block|{
comment|// might be because the account does not exist or because the account is
comment|// not visible
switch|switch
condition|(
name|authType
condition|)
block|{
case|case
name|HTTP_LDAP
case|:
case|case
name|CLIENT_SSL_CERT_LDAP
case|:
case|case
name|LDAP
case|:
if|if
condition|(
name|accountResolver
operator|.
name|find
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|nameOrEmail
argument_list|)
operator|==
literal|null
condition|)
block|{
comment|// account does not exist, try to create it
name|Account
name|a
init|=
name|createAccountByLdap
argument_list|(
name|nameOrEmail
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|!=
literal|null
condition|)
block|{
return|return
name|a
return|;
block|}
block|}
break|break;
case|case
name|CUSTOM_EXTENSION
case|:
case|case
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
case|:
case|case
name|HTTP
case|:
case|case
name|LDAP_BIND
case|:
case|case
name|OAUTH
case|:
case|case
name|OPENID
case|:
case|case
name|OPENID_SSO
case|:
default|default:
block|}
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|addMembers (AccountGroup.Id groupId, Collection<? extends Account.Id> newMemberIds)
specifier|public
name|void
name|addMembers
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|Account
operator|.
name|Id
argument_list|>
name|newMemberIds
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountGroupMember
argument_list|>
name|newAccountGroupMembers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accId
range|:
name|newMemberIds
control|)
block|{
if|if
condition|(
operator|!
name|newAccountGroupMembers
operator|.
name|containsKey
argument_list|(
name|accId
argument_list|)
condition|)
block|{
name|AccountGroupMember
operator|.
name|Key
name|key
init|=
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|accId
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
name|AccountGroupMember
name|m
init|=
name|db
operator|.
name|get
argument_list|()
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|==
literal|null
condition|)
block|{
name|m
operator|=
operator|new
name|AccountGroupMember
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|newAccountGroupMembers
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
block|}
block|}
if|if
condition|(
operator|!
name|newAccountGroupMembers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|auditService
operator|.
name|dispatchAddAccountsToGroup
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|newAccountGroupMembers
operator|.
name|values
argument_list|()
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
name|insert
argument_list|(
name|newAccountGroupMembers
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroupMember
name|m
range|:
name|newAccountGroupMembers
operator|.
name|values
argument_list|()
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
block|}
block|}
DECL|method|createAccountByLdap (String user)
specifier|private
name|Account
name|createAccountByLdap
parameter_list|(
name|String
name|user
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|user
operator|.
name|matches
argument_list|(
name|Account
operator|.
name|USER_NAME_PATTERN
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|AuthRequest
name|req
init|=
name|AuthRequest
operator|.
name|forUser
argument_list|(
name|user
argument_list|)
decl_stmt|;
name|req
operator|.
name|setSkipAuthentication
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|accountCache
operator|.
name|get
argument_list|(
name|accountManager
operator|.
name|authenticate
argument_list|(
name|req
argument_list|)
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|getAccount
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|toAccountInfoList (Set<Account.Id> accountIds)
specifier|private
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|toAccountInfoList
parameter_list|(
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
block|{
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|AccountLoader
name|loader
init|=
name|infoFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|accId
range|:
name|accountIds
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|loader
operator|.
name|get
argument_list|(
name|accId
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|loader
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|class|PutMember
specifier|static
class|class
name|PutMember
implements|implements
name|RestModifyView
argument_list|<
name|GroupResource
argument_list|,
name|PutMember
operator|.
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|static
class|class
name|Input
block|{     }
DECL|field|put
specifier|private
specifier|final
name|AddMembers
name|put
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|PutMember (AddMembers put, String id)
name|PutMember
parameter_list|(
name|AddMembers
name|put
parameter_list|,
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|put
operator|=
name|put
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (GroupResource resource, PutMember.Input input)
specifier|public
name|AccountInfo
name|apply
parameter_list|(
name|GroupResource
name|resource
parameter_list|,
name|PutMember
operator|.
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|MethodNotAllowedException
throws|,
name|ResourceNotFoundException
throws|,
name|OrmException
throws|,
name|IOException
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
name|id
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|list
init|=
name|put
operator|.
name|apply
argument_list|(
name|resource
argument_list|,
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|()
throw|;
block|}
catch|catch
parameter_list|(
name|UnprocessableEntityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Singleton
DECL|class|UpdateMember
specifier|static
class|class
name|UpdateMember
implements|implements
name|RestModifyView
argument_list|<
name|MemberResource
argument_list|,
name|PutMember
operator|.
name|Input
argument_list|>
block|{
DECL|field|get
specifier|private
specifier|final
name|GetMember
name|get
decl_stmt|;
annotation|@
name|Inject
DECL|method|UpdateMember (GetMember get)
name|UpdateMember
parameter_list|(
name|GetMember
name|get
parameter_list|)
block|{
name|this
operator|.
name|get
operator|=
name|get
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (MemberResource resource, PutMember.Input input)
specifier|public
name|AccountInfo
name|apply
parameter_list|(
name|MemberResource
name|resource
parameter_list|,
name|PutMember
operator|.
name|Input
name|input
parameter_list|)
throws|throws
name|OrmException
block|{
comment|// Do nothing, the user is already a member.
return|return
name|get
operator|.
name|apply
argument_list|(
name|resource
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

