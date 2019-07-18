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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toSet
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
name|Sets
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
name|Streams
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
name|common
operator|.
name|AvatarInfo
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
name|DynamicItem
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
name|avatar
operator|.
name|AvatarProvider
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
name|GlobalPermission
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
name|PermissionBackend
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
name|inject
operator|.
name|AbstractModule
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
name|Collection
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
name|EnumSet
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
name|Objects
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
DECL|class|InternalAccountDirectory
specifier|public
class|class
name|InternalAccountDirectory
extends|extends
name|AccountDirectory
block|{
DECL|field|ID_ONLY
specifier|static
specifier|final
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|ID_ONLY
init|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|FillOptions
operator|.
name|ID
argument_list|)
argument_list|)
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|AccountDirectory
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|InternalAccountDirectory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|avatar
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|AvatarProvider
argument_list|>
name|avatar
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
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
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|InternalAccountDirectory ( AccountCache accountCache, DynamicItem<AvatarProvider> avatar, IdentifiedUser.GenericFactory userFactory, Provider<CurrentUser> self, PermissionBackend permissionBackend)
name|InternalAccountDirectory
parameter_list|(
name|AccountCache
name|accountCache
parameter_list|,
name|DynamicItem
argument_list|<
name|AvatarProvider
argument_list|>
name|avatar
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|)
block|{
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|avatar
operator|=
name|avatar
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|fillAccountInfo (Iterable<? extends AccountInfo> in, Set<FillOptions> options)
specifier|public
name|void
name|fillAccountInfo
parameter_list|(
name|Iterable
argument_list|<
name|?
extends|extends
name|AccountInfo
argument_list|>
name|in
parameter_list|,
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|options
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
if|if
condition|(
name|options
operator|.
name|equals
argument_list|(
name|ID_ONLY
argument_list|)
condition|)
block|{
return|return;
block|}
name|boolean
name|canModifyAccount
init|=
literal|false
decl_stmt|;
name|Account
operator|.
name|Id
name|currentUserId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|currentUserId
operator|=
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|MODIFY_ACCOUNT
argument_list|)
expr_stmt|;
name|canModifyAccount
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|canModifyAccount
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|fillOptionsWithoutSecondaryEmails
init|=
name|Sets
operator|.
name|difference
argument_list|(
name|options
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|FillOptions
operator|.
name|SECONDARY_EMAILS
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
init|=
name|Streams
operator|.
name|stream
argument_list|(
name|in
argument_list|)
operator|.
name|map
argument_list|(
name|a
lambda|->
name|Account
operator|.
name|id
argument_list|(
name|a
operator|.
name|_accountId
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|accountStates
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|ids
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountInfo
name|info
range|:
name|in
control|)
block|{
name|Account
operator|.
name|Id
name|id
init|=
name|Account
operator|.
name|id
argument_list|(
name|info
operator|.
name|_accountId
argument_list|)
decl_stmt|;
name|AccountState
name|state
init|=
name|accountStates
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|SECONDARY_EMAILS
argument_list|)
operator|||
name|Objects
operator|.
name|equals
argument_list|(
name|currentUserId
argument_list|,
name|state
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|||
name|canModifyAccount
condition|)
block|{
name|fill
argument_list|(
name|info
argument_list|,
name|accountStates
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// user is not allowed to see secondary emails
name|fill
argument_list|(
name|info
argument_list|,
name|accountStates
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|,
name|fillOptionsWithoutSecondaryEmails
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|info
operator|.
name|_accountId
operator|=
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|ID
argument_list|)
condition|?
name|id
operator|.
name|get
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|fill (AccountInfo info, AccountState accountState, Set<FillOptions> options)
specifier|private
name|void
name|fill
parameter_list|(
name|AccountInfo
name|info
parameter_list|,
name|AccountState
name|accountState
parameter_list|,
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|options
parameter_list|)
block|{
name|Account
name|account
init|=
name|accountState
operator|.
name|getAccount
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|ID
argument_list|)
condition|)
block|{
name|info
operator|.
name|_accountId
operator|=
name|account
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Was previously set to look up account for filling.
name|info
operator|.
name|_accountId
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|NAME
argument_list|)
condition|)
block|{
name|info
operator|.
name|name
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|account
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|name
operator|==
literal|null
condition|)
block|{
name|info
operator|.
name|name
operator|=
name|accountState
operator|.
name|getUserName
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|EMAIL
argument_list|)
condition|)
block|{
name|info
operator|.
name|email
operator|=
name|account
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|SECONDARY_EMAILS
argument_list|)
condition|)
block|{
name|info
operator|.
name|secondaryEmails
operator|=
name|getSecondaryEmails
argument_list|(
name|account
argument_list|,
name|accountState
operator|.
name|getExternalIds
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|USERNAME
argument_list|)
condition|)
block|{
name|info
operator|.
name|username
operator|=
name|accountState
operator|.
name|getUserName
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|STATUS
argument_list|)
condition|)
block|{
name|info
operator|.
name|status
operator|=
name|account
operator|.
name|getStatus
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|options
operator|.
name|contains
argument_list|(
name|FillOptions
operator|.
name|AVATARS
argument_list|)
condition|)
block|{
name|AvatarProvider
name|ap
init|=
name|avatar
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|ap
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|avatars
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|IdentifiedUser
name|user
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
comment|// PolyGerrit UI uses the following sizes for avatars:
comment|// - 32px for avatars next to names e.g. on the dashboard. This is also Gerrit's default.
comment|// - 56px for the user's own avatar in the menu
comment|// - 100ox for other user's avatars on dashboards
comment|// - 120px for the user's own profile settings page
name|addAvatar
argument_list|(
name|ap
argument_list|,
name|info
argument_list|,
name|user
argument_list|,
name|AvatarInfo
operator|.
name|DEFAULT_SIZE
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|info
operator|.
name|avatars
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addAvatar
argument_list|(
name|ap
argument_list|,
name|info
argument_list|,
name|user
argument_list|,
literal|56
argument_list|)
expr_stmt|;
name|addAvatar
argument_list|(
name|ap
argument_list|,
name|info
argument_list|,
name|user
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|addAvatar
argument_list|(
name|ap
argument_list|,
name|info
argument_list|,
name|user
argument_list|,
literal|120
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|getSecondaryEmails (Account account, Collection<ExternalId> externalIds)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSecondaryEmails
parameter_list|(
name|Account
name|account
parameter_list|,
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
return|return
name|ExternalId
operator|.
name|getEmails
argument_list|(
name|externalIds
argument_list|)
operator|.
name|filter
argument_list|(
name|e
lambda|->
operator|!
name|e
operator|.
name|equals
argument_list|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
argument_list|)
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addAvatar ( AvatarProvider provider, AccountInfo account, IdentifiedUser user, int size)
specifier|private
specifier|static
name|void
name|addAvatar
parameter_list|(
name|AvatarProvider
name|provider
parameter_list|,
name|AccountInfo
name|account
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
name|int
name|size
parameter_list|)
block|{
name|String
name|url
init|=
name|provider
operator|.
name|getUrl
argument_list|(
name|user
argument_list|,
name|size
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|AvatarInfo
name|avatar
init|=
operator|new
name|AvatarInfo
argument_list|()
decl_stmt|;
name|avatar
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|avatar
operator|.
name|height
operator|=
name|size
expr_stmt|;
name|account
operator|.
name|avatars
operator|.
name|add
argument_list|(
name|avatar
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

