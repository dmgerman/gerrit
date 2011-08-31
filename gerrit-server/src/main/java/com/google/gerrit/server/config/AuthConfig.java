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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|common
operator|.
name|auth
operator|.
name|openid
operator|.
name|OpenIdProviderPattern
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
name|AuthType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|SignedToken
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_comment
comment|/** Authentication related settings from {@code gerrit.config}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AuthConfig
specifier|public
class|class
name|AuthConfig
block|{
DECL|field|authType
specifier|private
specifier|final
name|AuthType
name|authType
decl_stmt|;
DECL|field|httpHeader
specifier|private
specifier|final
name|String
name|httpHeader
decl_stmt|;
DECL|field|trustContainerAuth
specifier|private
specifier|final
name|boolean
name|trustContainerAuth
decl_stmt|;
DECL|field|logoutUrl
specifier|private
specifier|final
name|String
name|logoutUrl
decl_stmt|;
DECL|field|trustedOpenIDs
specifier|private
specifier|final
name|List
argument_list|<
name|OpenIdProviderPattern
argument_list|>
name|trustedOpenIDs
decl_stmt|;
DECL|field|allowedOpenIDs
specifier|private
specifier|final
name|List
argument_list|<
name|OpenIdProviderPattern
argument_list|>
name|allowedOpenIDs
decl_stmt|;
DECL|field|cookiePath
specifier|private
specifier|final
name|String
name|cookiePath
decl_stmt|;
DECL|field|cookieSecure
specifier|private
specifier|final
name|boolean
name|cookieSecure
decl_stmt|;
DECL|field|emailReg
specifier|private
specifier|final
name|SignedToken
name|emailReg
decl_stmt|;
DECL|field|allowGoogleAccountUpgrade
specifier|private
specifier|final
name|boolean
name|allowGoogleAccountUpgrade
decl_stmt|;
annotation|@
name|Inject
DECL|method|AuthConfig (@erritServerConfig final Config cfg)
name|AuthConfig
parameter_list|(
annotation|@
name|GerritServerConfig
specifier|final
name|Config
name|cfg
parameter_list|)
throws|throws
name|XsrfException
block|{
name|authType
operator|=
name|toType
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
name|httpHeader
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"httpheader"
argument_list|)
expr_stmt|;
name|logoutUrl
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"logouturl"
argument_list|)
expr_stmt|;
name|trustedOpenIDs
operator|=
name|toPatterns
argument_list|(
name|cfg
argument_list|,
literal|"trustedOpenID"
argument_list|)
expr_stmt|;
name|allowedOpenIDs
operator|=
name|toPatterns
argument_list|(
name|cfg
argument_list|,
literal|"allowedOpenID"
argument_list|)
expr_stmt|;
name|cookiePath
operator|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"cookiepath"
argument_list|)
expr_stmt|;
name|cookieSecure
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"cookiesecure"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|trustContainerAuth
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"trustContainerAuth"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|String
name|key
init|=
name|cfg
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"registerEmailPrivateKey"
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
operator|&&
operator|!
name|key
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|int
name|age
init|=
operator|(
name|int
operator|)
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"maxRegisterEmailTokenAge"
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|convert
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|DAYS
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|emailReg
operator|=
operator|new
name|SignedToken
argument_list|(
name|age
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|emailReg
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|authType
operator|==
name|AuthType
operator|.
name|OPENID
condition|)
block|{
name|allowGoogleAccountUpgrade
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"allowgoogleaccountupgrade"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|allowGoogleAccountUpgrade
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|toPatterns (Config cfg, String name)
specifier|private
specifier|static
name|List
argument_list|<
name|OpenIdProviderPattern
argument_list|>
name|toPatterns
parameter_list|(
name|Config
name|cfg
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|s
init|=
name|cfg
operator|.
name|getStringList
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|s
operator|=
operator|new
name|String
index|[]
block|{
literal|"http://"
block|,
literal|"https://"
block|}
expr_stmt|;
block|}
name|List
argument_list|<
name|OpenIdProviderPattern
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|OpenIdProviderPattern
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pattern
range|:
name|s
control|)
block|{
name|r
operator|.
name|add
argument_list|(
name|OpenIdProviderPattern
operator|.
name|create
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|method|toType (final Config cfg)
specifier|private
specifier|static
name|AuthType
name|toType
parameter_list|(
specifier|final
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|ConfigUtil
operator|.
name|getEnum
argument_list|(
name|cfg
argument_list|,
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
name|AuthType
operator|.
name|OPENID
argument_list|)
return|;
block|}
comment|/** Type of user authentication used by this Gerrit server. */
DECL|method|getAuthType ()
specifier|public
name|AuthType
name|getAuthType
parameter_list|()
block|{
return|return
name|authType
return|;
block|}
DECL|method|getLoginHttpHeader ()
specifier|public
name|String
name|getLoginHttpHeader
parameter_list|()
block|{
return|return
name|httpHeader
return|;
block|}
DECL|method|getLogoutURL ()
specifier|public
name|String
name|getLogoutURL
parameter_list|()
block|{
return|return
name|logoutUrl
return|;
block|}
DECL|method|getCookiePath ()
specifier|public
name|String
name|getCookiePath
parameter_list|()
block|{
return|return
name|cookiePath
return|;
block|}
DECL|method|getCookieSecure ()
specifier|public
name|boolean
name|getCookieSecure
parameter_list|()
block|{
return|return
name|cookieSecure
return|;
block|}
DECL|method|getEmailRegistrationToken ()
specifier|public
name|SignedToken
name|getEmailRegistrationToken
parameter_list|()
block|{
return|return
name|emailReg
return|;
block|}
DECL|method|isAllowGoogleAccountUpgrade ()
specifier|public
name|boolean
name|isAllowGoogleAccountUpgrade
parameter_list|()
block|{
return|return
name|allowGoogleAccountUpgrade
return|;
block|}
comment|/** OpenID identities which the server permits for authentication. */
DECL|method|getAllowedOpenIDs ()
specifier|public
name|List
argument_list|<
name|OpenIdProviderPattern
argument_list|>
name|getAllowedOpenIDs
parameter_list|()
block|{
return|return
name|allowedOpenIDs
return|;
block|}
comment|/** Whether git-over-http should trust authentication done by container. */
DECL|method|isTrustContainerAuth ()
specifier|public
name|boolean
name|isTrustContainerAuth
parameter_list|()
block|{
return|return
name|trustContainerAuth
return|;
block|}
DECL|method|isIdentityTrustable (final Collection<AccountExternalId> ids)
specifier|public
name|boolean
name|isIdentityTrustable
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
parameter_list|)
block|{
switch|switch
condition|(
name|getAuthType
argument_list|()
condition|)
block|{
case|case
name|DEVELOPMENT_BECOME_ANY_ACCOUNT
case|:
case|case
name|HTTP
case|:
case|case
name|HTTP_LDAP
case|:
case|case
name|LDAP
case|:
case|case
name|LDAP_BIND
case|:
case|case
name|CLIENT_SSL_CERT_LDAP
case|:
comment|// Its safe to assume yes for an HTTP authentication type, as the
comment|// only way in is through some external system that the admin trusts
comment|//
return|return
literal|true
return|;
case|case
name|OPENID
case|:
comment|// All identities must be trusted in order to trust the account.
comment|//
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|ids
control|)
block|{
if|if
condition|(
operator|!
name|isTrusted
argument_list|(
name|e
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
default|default:
comment|// Assume not, we don't understand the login format.
comment|//
return|return
literal|false
return|;
block|}
block|}
DECL|method|isTrusted (final AccountExternalId id)
specifier|private
name|boolean
name|isTrusted
parameter_list|(
specifier|final
name|AccountExternalId
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|LEGACY_GAE
argument_list|)
condition|)
block|{
comment|// Assume this is a trusted token, its a legacy import from
comment|// a fairly well respected provider and only takes effect if
comment|// the administrator has the import still enabled
comment|//
return|return
name|isAllowGoogleAccountUpgrade
argument_list|()
return|;
block|}
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_MAILTO
argument_list|)
condition|)
block|{
comment|// mailto identities are created by sending a unique validation
comment|// token to the address and asking them to come back to the site
comment|// with that token.
comment|//
return|return
literal|true
return|;
block|}
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_UUID
argument_list|)
condition|)
block|{
comment|// UUID identities are absolutely meaningless and cannot be
comment|// constructed through any normal login process we use.
comment|//
return|return
literal|true
return|;
block|}
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
comment|// We can trust their username, its local to our server only.
comment|//
return|return
literal|true
return|;
block|}
for|for
control|(
specifier|final
name|OpenIdProviderPattern
name|p
range|:
name|trustedOpenIDs
control|)
block|{
if|if
condition|(
name|p
operator|.
name|matches
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

