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
DECL|package|com.google.gerrit.server.restapi.account
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
name|SCHEME_USERNAME
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|exceptions
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
name|exceptions
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
name|IdString
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
name|RestCollectionCreateView
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
name|AccountResource
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
name|AccountsUpdate
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
name|VersionedAuthorizedKeys
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
name|DuplicateExternalIdKeyException
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
name|group
operator|.
name|GroupResolver
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
name|notedb
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
name|permissions
operator|.
name|PermissionBackendException
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
name|plugincontext
operator|.
name|PluginSetContext
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
annotation|@
name|Singleton
DECL|class|CreateAccount
specifier|public
class|class
name|CreateAccount
implements|implements
name|RestCollectionCreateView
argument_list|<
name|TopLevelResource
argument_list|,
name|AccountResource
argument_list|,
name|AccountInput
argument_list|>
block|{
DECL|field|seq
specifier|private
specifier|final
name|Sequences
name|seq
decl_stmt|;
DECL|field|groupResolver
specifier|private
specifier|final
name|GroupResolver
name|groupResolver
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
DECL|field|accountsUpdateProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
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
name|PluginSetContext
argument_list|<
name|AccountExternalIdCreator
argument_list|>
name|externalIdCreators
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
annotation|@
name|Inject
DECL|method|CreateAccount ( Sequences seq, GroupResolver groupResolver, VersionedAuthorizedKeys.Accessor authorizedKeys, SshKeyCache sshKeyCache, @UserInitiated Provider<AccountsUpdate> accountsUpdateProvider, AccountLoader.Factory infoLoader, PluginSetContext<AccountExternalIdCreator> externalIdCreators, @UserInitiated Provider<GroupsUpdate> groupsUpdate, OutgoingEmailValidator validator)
name|CreateAccount
parameter_list|(
name|Sequences
name|seq
parameter_list|,
name|GroupResolver
name|groupResolver
parameter_list|,
name|VersionedAuthorizedKeys
operator|.
name|Accessor
name|authorizedKeys
parameter_list|,
name|SshKeyCache
name|sshKeyCache
parameter_list|,
annotation|@
name|UserInitiated
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|infoLoader
parameter_list|,
name|PluginSetContext
argument_list|<
name|AccountExternalIdCreator
argument_list|>
name|externalIdCreators
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
parameter_list|)
block|{
name|this
operator|.
name|seq
operator|=
name|seq
expr_stmt|;
name|this
operator|.
name|groupResolver
operator|=
name|groupResolver
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
name|accountsUpdateProvider
operator|=
name|accountsUpdateProvider
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
block|}
annotation|@
name|Override
DECL|method|apply ( TopLevelResource rsrc, IdString id, @Nullable AccountInput input)
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
name|IdString
name|id
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
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
return|return
name|apply
argument_list|(
name|id
argument_list|,
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
DECL|method|apply (IdString id, AccountInput input)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
name|IdString
name|id
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
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|String
name|username
init|=
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
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
name|accountId
init|=
name|Account
operator|.
name|id
argument_list|(
name|seq
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
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
name|extIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|createEmail
argument_list|(
name|accountId
argument_list|,
name|input
operator|.
name|email
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|extIds
operator|.
name|add
argument_list|(
name|ExternalId
operator|.
name|createUsername
argument_list|(
name|username
argument_list|,
name|accountId
argument_list|,
name|input
operator|.
name|httpPassword
argument_list|)
argument_list|)
expr_stmt|;
name|externalIdCreators
operator|.
name|runEach
argument_list|(
name|c
lambda|->
name|extIds
operator|.
name|addAll
argument_list|(
name|c
operator|.
name|create
argument_list|(
name|accountId
argument_list|,
name|username
argument_list|,
name|input
operator|.
name|email
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|insert
argument_list|(
literal|"Create Account via API"
argument_list|,
name|accountId
argument_list|,
name|u
lambda|->
name|u
operator|.
name|setFullName
argument_list|(
name|input
operator|.
name|name
argument_list|)
operator|.
name|setPreferredEmail
argument_list|(
name|input
operator|.
name|email
argument_list|)
operator|.
name|addExternalIds
argument_list|(
name|extIds
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DuplicateExternalIdKeyException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getDuplicateKey
argument_list|()
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"username '"
operator|+
name|e
operator|.
name|getDuplicateKey
argument_list|()
operator|.
name|id
argument_list|()
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getDuplicateKey
argument_list|()
operator|.
name|isScheme
argument_list|(
name|SCHEME_MAILTO
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
literal|"email '"
operator|+
name|e
operator|.
name|getDuplicateKey
argument_list|()
operator|.
name|id
argument_list|()
operator|+
literal|"' already exists"
argument_list|)
throw|;
block|}
else|else
block|{
comment|// AccountExternalIdCreator returned an external ID that already exists
throw|throw
name|e
throw|;
block|}
block|}
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
name|addGroupMember
argument_list|(
name|groupUuid
argument_list|,
name|accountId
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
name|accountId
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
name|accountId
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
name|groupResolver
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
DECL|method|addGroupMember (AccountGroup.UUID groupUuid, Account.Id accountId)
specifier|private
name|void
name|addGroupMember
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupUuid
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
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
name|union
argument_list|(
name|memberIds
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|(
name|accountId
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|groupsUpdate
operator|.
name|get
argument_list|()
operator|.
name|updateGroup
argument_list|(
name|groupUuid
argument_list|,
name|groupUpdate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

