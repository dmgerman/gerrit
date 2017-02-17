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
name|common
operator|.
name|TimeUtil
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
name|GlobalCapability
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
name|GroupDescriptions
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
name|InvalidSshKeyException
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
name|RequiresCapability
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
name|api
operator|.
name|accounts
operator|.
name|AccountInput
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
name|registration
operator|.
name|DynamicSet
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
name|BadRequestException
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
name|ResourceConflictException
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
name|TopLevelResource
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
name|AccountExternalId
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
name|api
operator|.
name|accounts
operator|.
name|AccountExternalIdCreator
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
name|GroupsCollection
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
name|index
operator|.
name|account
operator|.
name|AccountIndexer
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
name|mail
operator|.
name|send
operator|.
name|OutgoingEmailValidator
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
name|ssh
operator|.
name|SshKeyCache
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
name|assistedinject
operator|.
name|Assisted
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
name|Collections
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
name|List
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
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_ACCOUNT
argument_list|)
DECL|class|CreateAccount
specifier|public
class|class
name|CreateAccount
implements|implements
name|RestModifyView
argument_list|<
name|TopLevelResource
argument_list|,
name|AccountInput
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String username)
name|CreateAccount
name|create
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
block|}
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
decl_stmt|;
DECL|field|groupsCollection
specifier|private
specifier|final
name|GroupsCollection
name|groupsCollection
decl_stmt|;
DECL|field|authorizedKeys
specifier|private
specifier|final
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
decl_stmt|;
DECL|field|sshKeyCache
specifier|private
specifier|final
name|SshKeyCache
name|sshKeyCache
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|indexer
specifier|private
specifier|final
name|AccountIndexer
name|indexer
decl_stmt|;
DECL|field|byEmailCache
specifier|private
specifier|final
name|AccountByEmailCache
name|byEmailCache
decl_stmt|;
DECL|field|infoLoader
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|infoLoader
decl_stmt|;
DECL|field|externalIdCreators
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|AccountExternalIdCreator
argument_list|>
name|externalIdCreators
decl_stmt|;
DECL|field|auditService
specifier|private
specifier|final
name|AuditService
name|auditService
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateAccount ( ReviewDb db, Provider<IdentifiedUser> currentUser, GroupsCollection groupsCollection, VersionedAuthorizedKeys.Accessor authorizedKeys, SshKeyCache sshKeyCache, AccountCache accountCache, AccountIndexer indexer, AccountByEmailCache byEmailCache, AccountLoader.Factory infoLoader, DynamicSet<AccountExternalIdCreator> externalIdCreators, AuditService auditService, @Assisted String username)
name|CreateAccount
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|currentUser
parameter_list|,
name|GroupsCollection
name|groupsCollection
parameter_list|,
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
parameter_list|,
name|SshKeyCache
name|sshKeyCache
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|AccountIndexer
name|indexer
parameter_list|,
name|AccountByEmailCache
name|byEmailCache
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|infoLoader
parameter_list|,
name|DynamicSet
argument_list|<
name|AccountExternalIdCreator
argument_list|>
name|externalIdCreators
parameter_list|,
name|AuditService
name|auditService
parameter_list|,
annotation|@
name|Assisted
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|groupsCollection
operator|=
name|groupsCollection
expr_stmt|;
name|this
operator|.
name|authorizedKeys
operator|=
name|authorizedKeys
expr_stmt|;
name|this
operator|.
name|sshKeyCache
operator|=
name|sshKeyCache
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
name|this
operator|.
name|byEmailCache
operator|=
name|byEmailCache
expr_stmt|;
name|this
operator|.
name|infoLoader
operator|=
name|infoLoader
expr_stmt|;
name|this
operator|.
name|externalIdCreators
operator|=
name|externalIdCreators
expr_stmt|;
name|this
operator|.
name|auditService
operator|=
name|auditService
expr_stmt|;
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource rsrc, AccountInput input)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|rsrc
parameter_list|,
name|AccountInput
name|input
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|UnprocessableEntityException
throws|,
name|OrmException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|AccountInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|username
operator|!=
literal|null
operator|&&
operator|!
name|username
operator|.
name|equals
argument_list|(
name|input
operator|.
name|username
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"username must match URL"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|username
operator|.
name|matches
argument_list|(
name|Account
operator|.
name|USER_NAME_PATTERN
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Username '"
operator|+
name|username
operator|+
literal|"' must contain only letters, numbers, _, - or ."
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groups
init|=
name|parseGroups
argument_list|(
name|input
operator|.
name|groups
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|id
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|AccountExternalId
name|extUser
init|=
operator|new
name|AccountExternalId
argument_list|(
name|id
argument_list|,
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
argument_list|,
name|username
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|httpPassword
operator|!=
literal|null
condition|)
block|{
name|extUser
operator|.
name|setPassword
argument_list|(
name|input
operator|.
name|httpPassword
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|get
argument_list|(
name|extUser
operator|.
name|getKey
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"username '"
operator|+
name|username
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|email
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|get
argument_list|(
name|getEmailKey
argument_list|(
name|input
operator|.
name|email
argument_list|)
argument_list|)
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"email '"
operator|+
name|input
operator|.
name|email
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|OutgoingEmailValidator
operator|.
name|isValid
argument_list|(
name|input
operator|.
name|email
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid email address"
argument_list|)
throw|;
block|}
block|}
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|externalIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|externalIds
operator|.
name|add
argument_list|(
name|extUser
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountExternalIdCreator
name|c
range|:
name|externalIdCreators
control|)
block|{
name|externalIds
operator|.
name|addAll
argument_list|(
name|c
operator|.
name|create
argument_list|(
name|id
argument_list|,
name|username
argument_list|,
name|input
operator|.
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|externalIds
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmDuplicateKeyException
name|duplicateKey
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"username '"
operator|+
name|username
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|email
operator|!=
literal|null
condition|)
block|{
name|AccountExternalId
name|extMailto
init|=
operator|new
name|AccountExternalId
argument_list|(
name|id
argument_list|,
name|getEmailKey
argument_list|(
name|input
operator|.
name|email
argument_list|)
argument_list|)
decl_stmt|;
name|extMailto
operator|.
name|setEmailAddress
argument_list|(
name|input
operator|.
name|email
argument_list|)
expr_stmt|;
try|try
block|{
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extMailto
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmDuplicateKeyException
name|duplicateKey
parameter_list|)
block|{
try|try
block|{
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|delete
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extUser
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|cleanupError
parameter_list|)
block|{
comment|// Ignored
block|}
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"email '"
operator|+
name|input
operator|.
name|email
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
block|}
name|Account
name|a
init|=
operator|new
name|Account
argument_list|(
name|id
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
decl_stmt|;
name|a
operator|.
name|setFullName
argument_list|(
name|input
operator|.
name|name
argument_list|)
expr_stmt|;
name|a
operator|.
name|setPreferredEmail
argument_list|(
name|input
operator|.
name|email
argument_list|)
expr_stmt|;
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|Id
name|groupId
range|:
name|groups
control|)
block|{
name|AccountGroupMember
name|m
init|=
operator|new
name|AccountGroupMember
argument_list|(
operator|new
name|AccountGroupMember
operator|.
name|Key
argument_list|(
name|id
argument_list|,
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
name|auditService
operator|.
name|dispatchAddAccountsToGroup
argument_list|(
name|currentUser
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|m
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|sshKey
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|authorizedKeys
operator|.
name|addKey
argument_list|(
name|id
argument_list|,
name|input
operator|.
name|sshKey
argument_list|)
expr_stmt|;
name|sshKeyCache
operator|.
name|evict
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidSshKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|accountCache
operator|.
name|evictByUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
name|byEmailCache
operator|.
name|evict
argument_list|(
name|input
operator|.
name|email
argument_list|)
expr_stmt|;
name|indexer
operator|.
name|index
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|AccountLoader
name|loader
init|=
name|infoLoader
operator|.
name|create
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|AccountInfo
name|info
init|=
name|loader
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|loader
operator|.
name|fill
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|created
argument_list|(
name|info
argument_list|)
return|;
block|}
DECL|method|parseGroups (List<String> groups)
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|parseGroups
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|UnprocessableEntityException
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|groupIds
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|groups
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|g
range|:
name|groups
control|)
block|{
name|groupIds
operator|.
name|add
argument_list|(
name|GroupDescriptions
operator|.
name|toAccountGroup
argument_list|(
name|groupsCollection
operator|.
name|parseInternal
argument_list|(
name|g
argument_list|)
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|groupIds
return|;
block|}
DECL|method|getEmailKey (String email)
specifier|private
name|AccountExternalId
operator|.
name|Key
name|getEmailKey
parameter_list|(
name|String
name|email
parameter_list|)
block|{
return|return
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
argument_list|,
name|email
argument_list|)
return|;
block|}
block|}
end_class

end_unit

