begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|AccountState
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
name|GroupBackend
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
name|GroupMembership
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
name|ListGroupMembership
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
name|Realm
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
name|AnonymousCowardName
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
name|config
operator|.
name|CanonicalWebUrl
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
name|DisableReverseDnsLookup
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
name|SystemGroupBackend
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
name|OutOfScopeException
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
name|ProvisionException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|util
operator|.
name|Providers
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|SystemReader
import|;
end_import

begin_comment
comment|/** An authenticated user. */
end_comment

begin_class
DECL|class|IdentifiedUser
specifier|public
class|class
name|IdentifiedUser
extends|extends
name|CurrentUser
block|{
comment|/** Create an IdentifiedUser, ignoring any per-request state. */
annotation|@
name|Singleton
DECL|class|GenericFactory
specifier|public
specifier|static
class|class
name|GenericFactory
block|{
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|canonicalUrl
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|disableReverseDnsLookup
specifier|private
specifier|final
name|Boolean
name|disableReverseDnsLookup
decl_stmt|;
annotation|@
name|Inject
DECL|method|GenericFactory ( AuthConfig authConfig, Realm realm, @AnonymousCowardName String anonymousCowardName, @CanonicalWebUrl Provider<String> canonicalUrl, @DisableReverseDnsLookup Boolean disableReverseDnsLookup, AccountCache accountCache, GroupBackend groupBackend)
specifier|public
name|GenericFactory
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|Realm
name|realm
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
parameter_list|,
annotation|@
name|DisableReverseDnsLookup
name|Boolean
name|disableReverseDnsLookup
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
name|this
operator|.
name|canonicalUrl
operator|=
name|canonicalUrl
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|disableReverseDnsLookup
operator|=
name|disableReverseDnsLookup
expr_stmt|;
block|}
DECL|method|create (AccountState state)
specifier|public
name|IdentifiedUser
name|create
parameter_list|(
name|AccountState
name|state
parameter_list|)
block|{
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|canonicalUrl
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
operator|(
name|SocketAddress
operator|)
literal|null
argument_list|)
argument_list|,
name|state
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|create (Account.Id id)
specifier|public
name|IdentifiedUser
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|create
argument_list|(
operator|(
name|SocketAddress
operator|)
literal|null
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|create (SocketAddress remotePeer, Account.Id id)
specifier|public
name|IdentifiedUser
name|create
parameter_list|(
name|SocketAddress
name|remotePeer
parameter_list|,
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|runAs
argument_list|(
name|remotePeer
argument_list|,
name|id
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|runAs ( SocketAddress remotePeer, Account.Id id, @Nullable CurrentUser caller)
specifier|public
name|IdentifiedUser
name|runAs
parameter_list|(
name|SocketAddress
name|remotePeer
parameter_list|,
name|Account
operator|.
name|Id
name|id
parameter_list|,
annotation|@
name|Nullable
name|CurrentUser
name|caller
parameter_list|)
block|{
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|canonicalUrl
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|remotePeer
argument_list|)
argument_list|,
name|id
argument_list|,
name|caller
argument_list|)
return|;
block|}
block|}
comment|/**    * Create an IdentifiedUser, relying on current request state.    *    *<p>Can only be used from within a module that has defined request scoped {@code @RemotePeer    * SocketAddress} and {@code ReviewDb} providers.    */
annotation|@
name|Singleton
DECL|class|RequestFactory
specifier|public
specifier|static
class|class
name|RequestFactory
block|{
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|canonicalUrl
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|disableReverseDnsLookup
specifier|private
specifier|final
name|Boolean
name|disableReverseDnsLookup
decl_stmt|;
DECL|field|remotePeerProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|RequestFactory ( AuthConfig authConfig, Realm realm, @AnonymousCowardName String anonymousCowardName, @CanonicalWebUrl Provider<String> canonicalUrl, AccountCache accountCache, GroupBackend groupBackend, @DisableReverseDnsLookup Boolean disableReverseDnsLookup, @RemotePeer Provider<SocketAddress> remotePeerProvider)
name|RequestFactory
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|Realm
name|realm
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
annotation|@
name|CanonicalWebUrl
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
annotation|@
name|DisableReverseDnsLookup
name|Boolean
name|disableReverseDnsLookup
parameter_list|,
annotation|@
name|RemotePeer
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
parameter_list|)
block|{
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
name|this
operator|.
name|canonicalUrl
operator|=
name|canonicalUrl
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|disableReverseDnsLookup
operator|=
name|disableReverseDnsLookup
expr_stmt|;
name|this
operator|.
name|remotePeerProvider
operator|=
name|remotePeerProvider
expr_stmt|;
block|}
DECL|method|create (Account.Id id)
specifier|public
name|IdentifiedUser
name|create
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|canonicalUrl
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|remotePeerProvider
argument_list|,
name|id
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|runAs (Account.Id id, CurrentUser caller)
specifier|public
name|IdentifiedUser
name|runAs
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|CurrentUser
name|caller
parameter_list|)
block|{
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|canonicalUrl
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|remotePeerProvider
argument_list|,
name|id
argument_list|,
name|caller
argument_list|)
return|;
block|}
block|}
DECL|field|registeredGroups
specifier|private
specifier|static
specifier|final
name|GroupMembership
name|registeredGroups
init|=
operator|new
name|ListGroupMembership
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
argument_list|,
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|canonicalUrl
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|realm
specifier|private
specifier|final
name|Realm
name|realm
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|disableReverseDnsLookup
specifier|private
specifier|final
name|Boolean
name|disableReverseDnsLookup
decl_stmt|;
DECL|field|validEmails
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|validEmails
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
DECL|field|realUser
specifier|private
specifier|final
name|CurrentUser
name|realUser
decl_stmt|;
comment|// Must be final since cached properties depend on it.
DECL|field|remotePeerProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|state
specifier|private
name|AccountState
name|state
decl_stmt|;
DECL|field|loadedAllEmails
specifier|private
name|boolean
name|loadedAllEmails
decl_stmt|;
DECL|field|invalidEmails
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|invalidEmails
decl_stmt|;
DECL|field|effectiveGroups
specifier|private
name|GroupMembership
name|effectiveGroups
decl_stmt|;
DECL|field|properties
specifier|private
name|Map
argument_list|<
name|PropertyKey
argument_list|<
name|Object
argument_list|>
argument_list|,
name|Object
argument_list|>
name|properties
decl_stmt|;
DECL|method|IdentifiedUser ( AuthConfig authConfig, Realm realm, String anonymousCowardName, Provider<String> canonicalUrl, AccountCache accountCache, GroupBackend groupBackend, Boolean disableReverseDnsLookup, @Nullable Provider<SocketAddress> remotePeerProvider, AccountState state, @Nullable CurrentUser realUser)
specifier|private
name|IdentifiedUser
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|Realm
name|realm
parameter_list|,
name|String
name|anonymousCowardName
parameter_list|,
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|Boolean
name|disableReverseDnsLookup
parameter_list|,
annotation|@
name|Nullable
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
parameter_list|,
name|AccountState
name|state
parameter_list|,
annotation|@
name|Nullable
name|CurrentUser
name|realUser
parameter_list|)
block|{
name|this
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|canonicalUrl
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|remotePeerProvider
argument_list|,
name|state
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|realUser
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
expr_stmt|;
block|}
DECL|method|IdentifiedUser ( AuthConfig authConfig, Realm realm, String anonymousCowardName, Provider<String> canonicalUrl, AccountCache accountCache, GroupBackend groupBackend, Boolean disableReverseDnsLookup, @Nullable Provider<SocketAddress> remotePeerProvider, Account.Id id, @Nullable CurrentUser realUser)
specifier|private
name|IdentifiedUser
parameter_list|(
name|AuthConfig
name|authConfig
parameter_list|,
name|Realm
name|realm
parameter_list|,
name|String
name|anonymousCowardName
parameter_list|,
name|Provider
argument_list|<
name|String
argument_list|>
name|canonicalUrl
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|Boolean
name|disableReverseDnsLookup
parameter_list|,
annotation|@
name|Nullable
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeerProvider
parameter_list|,
name|Account
operator|.
name|Id
name|id
parameter_list|,
annotation|@
name|Nullable
name|CurrentUser
name|realUser
parameter_list|)
block|{
name|this
operator|.
name|canonicalUrl
operator|=
name|canonicalUrl
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
name|this
operator|.
name|disableReverseDnsLookup
operator|=
name|disableReverseDnsLookup
expr_stmt|;
name|this
operator|.
name|remotePeerProvider
operator|=
name|remotePeerProvider
expr_stmt|;
name|this
operator|.
name|accountId
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|realUser
operator|=
name|realUser
operator|!=
literal|null
condition|?
name|realUser
else|:
name|this
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRealUser ()
specifier|public
name|CurrentUser
name|getRealUser
parameter_list|()
block|{
return|return
name|realUser
return|;
block|}
annotation|@
name|Override
DECL|method|isImpersonating ()
specifier|public
name|boolean
name|isImpersonating
parameter_list|()
block|{
if|if
condition|(
name|realUser
operator|==
name|this
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|realUser
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
if|if
condition|(
name|realUser
operator|.
name|getAccountId
argument_list|()
operator|.
name|equals
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
comment|// Impersonating another copy of this user is allowed.
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|state ()
specifier|public
name|AccountState
name|state
parameter_list|()
block|{
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
name|state
operator|=
name|accountCache
operator|.
name|get
argument_list|(
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|state
return|;
block|}
annotation|@
name|Override
DECL|method|asIdentifiedUser ()
specifier|public
name|IdentifiedUser
name|asIdentifiedUser
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
comment|/** @return the user's user name; null if one has not been selected/assigned. */
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|.
name|getUserName
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|getAccount ()
specifier|public
name|Account
name|getAccount
parameter_list|()
block|{
return|return
name|state
argument_list|()
operator|.
name|getAccount
argument_list|()
return|;
block|}
DECL|method|hasEmailAddress (String email)
specifier|public
name|boolean
name|hasEmailAddress
parameter_list|(
name|String
name|email
parameter_list|)
block|{
if|if
condition|(
name|validEmails
operator|.
name|contains
argument_list|(
name|email
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|invalidEmails
operator|!=
literal|null
operator|&&
name|invalidEmails
operator|.
name|contains
argument_list|(
name|email
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|realm
operator|.
name|hasEmailAddress
argument_list|(
name|this
argument_list|,
name|email
argument_list|)
condition|)
block|{
name|validEmails
operator|.
name|add
argument_list|(
name|email
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|invalidEmails
operator|==
literal|null
condition|)
block|{
name|invalidEmails
operator|=
name|Sets
operator|.
name|newTreeSet
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
block|}
name|invalidEmails
operator|.
name|add
argument_list|(
name|email
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
DECL|method|getEmailAddresses ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEmailAddresses
parameter_list|()
block|{
if|if
condition|(
operator|!
name|loadedAllEmails
condition|)
block|{
name|validEmails
operator|.
name|addAll
argument_list|(
name|realm
operator|.
name|getEmailAddresses
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|loadedAllEmails
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|validEmails
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getAccount
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getNameEmail ()
specifier|public
name|String
name|getNameEmail
parameter_list|()
block|{
return|return
name|getAccount
argument_list|()
operator|.
name|getNameEmail
argument_list|(
name|anonymousCowardName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEffectiveGroups ()
specifier|public
name|GroupMembership
name|getEffectiveGroups
parameter_list|()
block|{
if|if
condition|(
name|effectiveGroups
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|authConfig
operator|.
name|isIdentityTrustable
argument_list|(
name|state
argument_list|()
operator|.
name|getExternalIds
argument_list|()
argument_list|)
condition|)
block|{
name|effectiveGroups
operator|=
name|groupBackend
operator|.
name|membershipsOf
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|effectiveGroups
operator|=
name|registeredGroups
expr_stmt|;
block|}
block|}
return|return
name|effectiveGroups
return|;
block|}
DECL|method|newRefLogIdent ()
specifier|public
name|PersonIdent
name|newRefLogIdent
parameter_list|()
block|{
return|return
name|newRefLogIdent
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|,
name|TimeZone
operator|.
name|getDefault
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newRefLogIdent (Date when, TimeZone tz)
specifier|public
name|PersonIdent
name|newRefLogIdent
parameter_list|(
name|Date
name|when
parameter_list|,
name|TimeZone
name|tz
parameter_list|)
block|{
specifier|final
name|Account
name|ua
init|=
name|getAccount
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|ua
operator|.
name|getFullName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|name
operator|=
name|ua
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|name
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
name|String
name|user
init|=
name|getUserName
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
name|user
operator|=
literal|""
expr_stmt|;
block|}
name|user
operator|=
name|user
operator|+
literal|"|account-"
operator|+
name|ua
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
operator|new
name|PersonIdent
argument_list|(
name|name
argument_list|,
name|user
operator|+
literal|"@"
operator|+
name|guessHost
argument_list|()
argument_list|,
name|when
argument_list|,
name|tz
argument_list|)
return|;
block|}
DECL|method|newCommitterIdent (Date when, TimeZone tz)
specifier|public
name|PersonIdent
name|newCommitterIdent
parameter_list|(
name|Date
name|when
parameter_list|,
name|TimeZone
name|tz
parameter_list|)
block|{
specifier|final
name|Account
name|ua
init|=
name|getAccount
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|ua
operator|.
name|getFullName
argument_list|()
decl_stmt|;
name|String
name|email
init|=
name|ua
operator|.
name|getPreferredEmail
argument_list|()
decl_stmt|;
if|if
condition|(
name|email
operator|==
literal|null
operator|||
name|email
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No preferred email is configured. Use a generic identity so we
comment|// don't leak an address the user may have given us, but doesn't
comment|// necessarily want to publish through Git records.
comment|//
name|String
name|user
init|=
name|getUserName
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
operator|||
name|user
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|user
operator|=
literal|"account-"
operator|+
name|ua
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
name|String
name|host
decl_stmt|;
if|if
condition|(
name|canonicalUrl
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|host
operator|=
operator|new
name|URL
argument_list|(
name|canonicalUrl
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|host
operator|=
name|SystemReader
operator|.
name|getInstance
argument_list|()
operator|.
name|getHostname
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|host
operator|=
name|SystemReader
operator|.
name|getInstance
argument_list|()
operator|.
name|getHostname
argument_list|()
expr_stmt|;
block|}
name|email
operator|=
name|user
operator|+
literal|"@"
operator|+
name|host
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
operator|||
name|name
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|int
name|at
init|=
name|email
operator|.
name|indexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|at
condition|)
block|{
name|name
operator|=
name|email
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|at
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PersonIdent
argument_list|(
name|name
argument_list|,
name|email
argument_list|,
name|when
argument_list|,
name|tz
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"IdentifiedUser[account "
operator|+
name|getAccountId
argument_list|()
operator|+
literal|"]"
return|;
block|}
comment|/** Check if user is the IdentifiedUser */
annotation|@
name|Override
DECL|method|isIdentifiedUser ()
specifier|public
name|boolean
name|isIdentifiedUser
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|get (PropertyKey<T> key)
specifier|public
specifier|synchronized
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|PropertyKey
argument_list|<
name|T
argument_list|>
name|key
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|value
init|=
operator|(
name|T
operator|)
name|properties
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|value
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**    * Store a property for later retrieval.    *    * @param key unique property key.    * @param value value to store; or {@code null} to clear the value.    */
annotation|@
name|Override
DECL|method|put (PropertyKey<T> key, @Nullable T value)
specifier|public
specifier|synchronized
parameter_list|<
name|T
parameter_list|>
name|void
name|put
parameter_list|(
name|PropertyKey
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
annotation|@
name|Nullable
name|T
name|value
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|properties
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|PropertyKey
argument_list|<
name|Object
argument_list|>
name|k
init|=
operator|(
name|PropertyKey
argument_list|<
name|Object
argument_list|>
operator|)
name|key
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|k
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|properties
operator|.
name|remove
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns a materialized copy of the user with all dependencies.    *    *<p>Invoke all providers and factories of dependent objects and store the references to a copy    * of the current identified user.    *    * @return copy of the identified user    */
DECL|method|materializedCopy ()
specifier|public
name|IdentifiedUser
name|materializedCopy
parameter_list|()
block|{
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
name|remotePeer
decl_stmt|;
try|try
block|{
name|remotePeer
operator|=
name|Providers
operator|.
name|of
argument_list|(
name|remotePeerProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OutOfScopeException
decl||
name|ProvisionException
name|e
parameter_list|)
block|{
name|remotePeer
operator|=
operator|new
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SocketAddress
name|get
parameter_list|()
block|{
throw|throw
name|e
throw|;
block|}
block|}
expr_stmt|;
block|}
return|return
operator|new
name|IdentifiedUser
argument_list|(
name|authConfig
argument_list|,
name|realm
argument_list|,
name|anonymousCowardName
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|canonicalUrl
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|accountCache
argument_list|,
name|groupBackend
argument_list|,
name|disableReverseDnsLookup
argument_list|,
name|remotePeer
argument_list|,
name|state
argument_list|,
name|realUser
argument_list|)
return|;
block|}
DECL|method|guessHost ()
specifier|private
name|String
name|guessHost
parameter_list|()
block|{
name|String
name|host
init|=
literal|null
decl_stmt|;
name|SocketAddress
name|remotePeer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|remotePeer
operator|=
name|remotePeerProvider
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OutOfScopeException
decl||
name|ProvisionException
name|e
parameter_list|)
block|{
comment|// Leave null.
block|}
if|if
condition|(
name|remotePeer
operator|instanceof
name|InetSocketAddress
condition|)
block|{
name|InetSocketAddress
name|sa
init|=
operator|(
name|InetSocketAddress
operator|)
name|remotePeer
decl_stmt|;
name|InetAddress
name|in
init|=
name|sa
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|host
operator|=
name|in
operator|!=
literal|null
condition|?
name|getHost
argument_list|(
name|in
argument_list|)
else|:
name|sa
operator|.
name|getHostName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
return|return
literal|"unknown"
return|;
block|}
return|return
name|host
return|;
block|}
DECL|method|getHost (InetAddress in)
specifier|private
name|String
name|getHost
parameter_list|(
name|InetAddress
name|in
parameter_list|)
block|{
if|if
condition|(
name|Boolean
operator|.
name|FALSE
operator|.
name|equals
argument_list|(
name|disableReverseDnsLookup
argument_list|)
condition|)
block|{
return|return
name|in
operator|.
name|getCanonicalHostName
argument_list|()
return|;
block|}
return|return
name|in
operator|.
name|getHostAddress
argument_list|()
return|;
block|}
block|}
end_class

end_unit

