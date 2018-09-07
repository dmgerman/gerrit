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
import|import static
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
name|externalids
operator|.
name|ExternalId
operator|.
name|SCHEME_MAILTO
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
name|account
operator|.
name|externalids
operator|.
name|ExternalId
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
name|externalids
operator|.
name|ExternalIds
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
name|externalids
operator|.
name|ExternalIdsUpdate
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
name|group
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
DECL|field|seq
specifier|private
specifier|final
name|Sequences
name|seq
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
DECL|field|accountsUpdate
specifier|private
specifier|final
name|AccountsUpdate
operator|.
name|User
name|accountsUpdate
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
DECL|field|externalIds
specifier|private
specifier|final
name|ExternalIds
name|externalIds
decl_stmt|;
DECL|field|externalIdsUpdateFactory
specifier|private
specifier|final
name|ExternalIdsUpdate
operator|.
name|User
name|externalIdsUpdateFactory
decl_stmt|;
DECL|field|groupsUpdate
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdate
decl_stmt|;
DECL|field|validator
specifier|private
specifier|final
name|OutgoingEmailValidator
name|validator
decl_stmt|;
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateAccount ( ReviewDb db, Sequences seq, GroupsCollection groupsCollection, VersionedAuthorizedKeys.Accessor authorizedKeys, SshKeyCache sshKeyCache, AccountsUpdate.User accountsUpdate, AccountLoader.Factory infoLoader, DynamicSet<AccountExternalIdCreator> externalIdCreators, ExternalIds externalIds, ExternalIdsUpdate.User externalIdsUpdateFactory, @UserInitiated Provider<GroupsUpdate> groupsUpdate, OutgoingEmailValidator validator, @Assisted String username)
name|CreateAccount
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Sequences
name|seq
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
name|AccountsUpdate
operator|.
name|User
name|accountsUpdate
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
name|ExternalIds
name|externalIds
parameter_list|,
name|ExternalIdsUpdate
operator|.
name|User
name|externalIdsUpdateFactory
parameter_list|,
annotation|@
name|UserInitiated
name|Provider
argument_list|<
name|GroupsUpdate
argument_list|>
name|groupsUpdate
parameter_list|,
name|OutgoingEmailValidator
name|validator
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
name|seq
operator|=
name|seq
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
name|accountsUpdate
operator|=
name|accountsUpdate
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
name|externalIds
operator|=
name|externalIds
expr_stmt|;
name|this
operator|.
name|externalIdsUpdateFactory
operator|=
name|externalIdsUpdateFactory
expr_stmt|;
name|this
operator|.
name|groupsUpdate
operator|=
name|groupsUpdate
expr_stmt|;
name|this
operator|.
name|validator
operator|=
name|validator
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
DECL|method|apply (TopLevelResource rsrc, @Nullable AccountInput input)
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
annotation|@
name|Nullable
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
return|return
name|apply
argument_list|(
name|input
operator|!=
literal|null
condition|?
name|input
else|:
operator|new
name|AccountInput
argument_list|()
argument_list|)
return|;
block|}
DECL|method|apply (AccountInput input)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
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
name|ExternalId
operator|.
name|isValidUsername
argument_list|(
name|username
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Invalid username '"
operator|+
name|username
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
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
name|seq
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|ExternalId
name|extUser
init|=
name|ExternalId
operator|.
name|createUsername
argument_list|(
name|username
argument_list|,
name|id
argument_list|,
name|input
operator|.
name|httpPassword
argument_list|)
decl_stmt|;
if|if
condition|(
name|externalIds
operator|.
name|get
argument_list|(
name|extUser
operator|.
name|key
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
name|externalIds
operator|.
name|get
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_MAILTO
argument_list|,
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
name|validator
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
name|ExternalId
argument_list|>
name|extIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|extIds
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
name|extIds
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
name|ExternalIdsUpdate
name|externalIdsUpdate
init|=
name|externalIdsUpdateFactory
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|externalIdsUpdate
operator|.
name|insert
argument_list|(
name|extIds
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
try|try
block|{
name|externalIdsUpdate
operator|.
name|insert
argument_list|(
name|ExternalId
operator|.
name|createEmail
argument_list|(
name|id
argument_list|,
name|input
operator|.
name|email
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
name|externalIdsUpdate
operator|.
name|delete
argument_list|(
name|extUser
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ConfigInvalidException
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
name|accountsUpdate
operator|.
name|create
argument_list|()
operator|.
name|insert
argument_list|(
name|id
argument_list|,
name|a
lambda|->
block|{
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
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
range|:
name|groups
control|)
block|{
try|try
block|{
name|groupsUpdate
operator|.
name|get
argument_list|()
operator|.
name|addGroupMember
argument_list|(
name|db
argument_list|,
name|groupUuid
argument_list|,
name|id
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
name|UnprocessableEntityException
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
name|UUID
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
name|UUID
argument_list|>
name|groupUuids
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
name|GroupDescription
operator|.
name|Internal
name|internalGroup
init|=
name|groupsCollection
operator|.
name|parseInternal
argument_list|(
name|g
argument_list|)
decl_stmt|;
name|groupUuids
operator|.
name|add
argument_list|(
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|groupUuids
return|;
block|}
block|}
end_class

end_unit

