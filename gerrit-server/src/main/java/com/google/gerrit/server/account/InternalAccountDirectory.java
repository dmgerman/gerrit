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
annotation|@
name|Inject
DECL|method|InternalAccountDirectory ( AccountCache accountCache, DynamicItem<AvatarProvider> avatar, IdentifiedUser.GenericFactory userFactory)
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
name|DirectoryException
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
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|info
operator|.
name|_accountId
argument_list|)
decl_stmt|;
name|AccountState
name|state
init|=
name|accountCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|fill
argument_list|(
name|info
argument_list|,
name|state
operator|.
name|getAccount
argument_list|()
argument_list|,
name|state
operator|.
name|getExternalIds
argument_list|()
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fill ( AccountInfo info, Account account, @Nullable Collection<AccountExternalId> externalIds, Set<FillOptions> options)
specifier|private
name|void
name|fill
parameter_list|(
name|AccountInfo
name|info
parameter_list|,
name|Account
name|account
parameter_list|,
annotation|@
name|Nullable
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|externalIds
parameter_list|,
name|Set
argument_list|<
name|FillOptions
argument_list|>
name|options
parameter_list|)
block|{
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
name|account
operator|.
name|getUserName
argument_list|()
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
name|externalIds
operator|!=
literal|null
condition|?
name|getSecondaryEmails
argument_list|(
name|account
argument_list|,
name|externalIds
argument_list|)
else|:
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
name|USERNAME
argument_list|)
condition|)
block|{
name|info
operator|.
name|username
operator|=
name|externalIds
operator|!=
literal|null
condition|?
name|AccountState
operator|.
name|getUserName
argument_list|(
name|externalIds
argument_list|)
else|:
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
argument_list|(
literal|3
argument_list|)
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
comment|// GWT UI uses DEFAULT_SIZE (26px).
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
comment|// PolyGerrit UI prefers 32px and 100px.
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
if|if
condition|(
literal|32
operator|!=
name|AvatarInfo
operator|.
name|DEFAULT_SIZE
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
literal|32
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|100
operator|!=
name|AvatarInfo
operator|.
name|DEFAULT_SIZE
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
literal|100
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|getSecondaryEmails ( Account account, Collection<AccountExternalId> externalIds)
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
name|AccountExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|emails
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|AccountState
operator|.
name|getEmails
argument_list|(
name|externalIds
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|emails
operator|.
name|remove
argument_list|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|emails
argument_list|)
expr_stmt|;
return|return
name|emails
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

