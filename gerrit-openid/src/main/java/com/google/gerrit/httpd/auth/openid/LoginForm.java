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
DECL|package|com.google.gerrit.httpd.auth.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|auth
operator|.
name|openid
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|MoreObjects
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
name|ImmutableMap
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
name|PageLinks
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
name|auth
operator|.
name|openid
operator|.
name|OpenIdUrls
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
name|auth
operator|.
name|oauth
operator|.
name|OAuthServiceProvider
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicMap
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
name|Url
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
name|httpd
operator|.
name|HtmlDomUtil
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
name|httpd
operator|.
name|LoginUrlToken
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
name|httpd
operator|.
name|template
operator|.
name|SiteHeaderFooter
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
name|GerritServerConfig
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
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|Cookie
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_comment
comment|/** Handles OpenID based login flow. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|LoginForm
class|class
name|LoginForm
extends|extends
name|HttpServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LoginForm
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ALL_PROVIDERS
specifier|private
specifier|static
specifier|final
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|ALL_PROVIDERS
init|=
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"launchpad"
argument_list|,
name|OpenIdUrls
operator|.
name|URL_LAUNCHPAD
argument_list|,
literal|"yahoo"
argument_list|,
name|OpenIdUrls
operator|.
name|URL_YAHOO
argument_list|)
decl_stmt|;
DECL|field|suggestProviders
specifier|private
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|suggestProviders
decl_stmt|;
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
DECL|field|oauthSessionProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|OAuthSessionOverOpenID
argument_list|>
name|oauthSessionProvider
decl_stmt|;
DECL|field|impl
specifier|private
specifier|final
name|OpenIdServiceImpl
name|impl
decl_stmt|;
DECL|field|maxRedirectUrlLength
specifier|private
specifier|final
name|int
name|maxRedirectUrlLength
decl_stmt|;
DECL|field|ssoUrl
specifier|private
specifier|final
name|String
name|ssoUrl
decl_stmt|;
DECL|field|header
specifier|private
specifier|final
name|SiteHeaderFooter
name|header
decl_stmt|;
DECL|field|currentUserProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUserProvider
decl_stmt|;
DECL|field|oauthServiceProviders
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|OAuthServiceProvider
argument_list|>
name|oauthServiceProviders
decl_stmt|;
annotation|@
name|Inject
DECL|method|LoginForm ( @anonicalWebUrl @ullable Provider<String> urlProvider, @GerritServerConfig Config config, AuthConfig authConfig, OpenIdServiceImpl impl, SiteHeaderFooter header, Provider<OAuthSessionOverOpenID> oauthSessionProvider, Provider<CurrentUser> currentUserProvider, DynamicMap<OAuthServiceProvider> oauthServiceProviders)
name|LoginForm
parameter_list|(
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|AuthConfig
name|authConfig
parameter_list|,
name|OpenIdServiceImpl
name|impl
parameter_list|,
name|SiteHeaderFooter
name|header
parameter_list|,
name|Provider
argument_list|<
name|OAuthSessionOverOpenID
argument_list|>
name|oauthSessionProvider
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUserProvider
parameter_list|,
name|DynamicMap
argument_list|<
name|OAuthServiceProvider
argument_list|>
name|oauthServiceProviders
parameter_list|)
block|{
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
name|this
operator|.
name|impl
operator|=
name|impl
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|this
operator|.
name|maxRedirectUrlLength
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"openid"
argument_list|,
literal|"maxRedirectUrlLength"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|this
operator|.
name|oauthSessionProvider
operator|=
name|oauthSessionProvider
expr_stmt|;
name|this
operator|.
name|currentUserProvider
operator|=
name|currentUserProvider
expr_stmt|;
name|this
operator|.
name|oauthServiceProviders
operator|=
name|oauthServiceProviders
expr_stmt|;
if|if
condition|(
name|urlProvider
operator|==
literal|null
operator|||
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|urlProvider
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"gerrit.canonicalWebUrl must be set in gerrit.config"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authConfig
operator|.
name|getAuthType
argument_list|()
operator|==
name|AuthType
operator|.
name|OPENID_SSO
condition|)
block|{
name|suggestProviders
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|()
expr_stmt|;
name|ssoUrl
operator|=
name|authConfig
operator|.
name|getOpenIdSsoUrl
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|providers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|ALL_PROVIDERS
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|impl
operator|.
name|isAllowedOpenID
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|providers
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|suggestProviders
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|ssoUrl
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse res)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ssoUrl
operator|!=
literal|null
condition|)
block|{
name|String
name|token
init|=
name|LoginUrlToken
operator|.
name|getToken
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|SignInMode
name|mode
decl_stmt|;
if|if
condition|(
name|PageLinks
operator|.
name|REGISTER
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|mode
operator|=
name|SignInMode
operator|.
name|REGISTER
expr_stmt|;
name|token
operator|=
name|PageLinks
operator|.
name|MINE
expr_stmt|;
block|}
else|else
block|{
name|mode
operator|=
name|SignInMode
operator|.
name|SIGN_IN
expr_stmt|;
block|}
name|discover
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
literal|false
argument_list|,
name|ssoUrl
argument_list|,
literal|false
argument_list|,
name|token
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|id
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|doPost
argument_list|(
name|req
argument_list|,
name|res
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|link
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"link"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|sendForm
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doPost (HttpServletRequest req, HttpServletResponse res)
specifier|protected
name|void
name|doPost
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|link
init|=
name|req
operator|.
name|getParameter
argument_list|(
literal|"link"
argument_list|)
operator|!=
literal|null
decl_stmt|;
name|String
name|id
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"id"
argument_list|)
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sendForm
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|id
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
operator|&&
operator|!
name|id
operator|.
name|startsWith
argument_list|(
literal|"https://"
argument_list|)
condition|)
block|{
name|id
operator|=
literal|"http://"
operator|+
name|id
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|ssoUrl
operator|!=
literal|null
operator|&&
operator|!
name|ssoUrl
operator|.
name|equals
argument_list|(
name|id
argument_list|)
operator|)
operator|||
operator|!
name|impl
operator|.
name|isAllowedOpenID
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|sendForm
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
literal|"OpenID provider not permitted by site policy."
argument_list|)
expr_stmt|;
return|return;
block|}
name|boolean
name|remember
init|=
literal|"1"
operator|.
name|equals
argument_list|(
name|req
operator|.
name|getParameter
argument_list|(
literal|"rememberme"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|token
init|=
name|LoginUrlToken
operator|.
name|getToken
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|SignInMode
name|mode
decl_stmt|;
if|if
condition|(
name|link
condition|)
block|{
name|mode
operator|=
name|SignInMode
operator|.
name|LINK_IDENTIY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|PageLinks
operator|.
name|REGISTER
operator|.
name|equals
argument_list|(
name|token
argument_list|)
condition|)
block|{
name|mode
operator|=
name|SignInMode
operator|.
name|REGISTER
expr_stmt|;
name|token
operator|=
name|PageLinks
operator|.
name|MINE
expr_stmt|;
block|}
else|else
block|{
name|mode
operator|=
name|SignInMode
operator|.
name|SIGN_IN
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"mode \"{}\""
argument_list|,
name|mode
argument_list|)
expr_stmt|;
name|OAuthServiceProvider
name|oauthProvider
init|=
name|lookupOAuthServiceProvider
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|oauthProvider
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"OpenId provider \"{}\""
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|discover
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
name|id
argument_list|,
name|remember
argument_list|,
name|token
argument_list|,
name|mode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"OAuth provider \"{}\""
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|OAuthSessionOverOpenID
name|oauthSession
init|=
name|oauthSessionProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|currentUserProvider
operator|.
name|get
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
operator|&&
name|oauthSession
operator|.
name|isLoggedIn
argument_list|()
condition|)
block|{
name|oauthSession
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|isGerritLogin
argument_list|(
name|req
argument_list|)
operator|||
name|oauthSession
operator|.
name|isOAuthFinal
argument_list|(
name|req
argument_list|)
operator|)
condition|)
block|{
name|oauthSession
operator|.
name|setServiceProvider
argument_list|(
name|oauthProvider
argument_list|)
expr_stmt|;
name|oauthSession
operator|.
name|setLinkMode
argument_list|(
name|link
argument_list|)
expr_stmt|;
name|oauthSession
operator|.
name|login
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|oauthProvider
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|discover ( HttpServletRequest req, HttpServletResponse res, boolean link, String id, boolean remember, String token, SignInMode mode)
specifier|private
name|void
name|discover
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|boolean
name|link
parameter_list|,
name|String
name|id
parameter_list|,
name|boolean
name|remember
parameter_list|,
name|String
name|token
parameter_list|,
name|SignInMode
name|mode
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|ssoUrl
operator|!=
literal|null
condition|)
block|{
name|remember
operator|=
literal|false
expr_stmt|;
block|}
name|DiscoveryResult
name|r
init|=
name|impl
operator|.
name|discover
argument_list|(
name|req
argument_list|,
name|id
argument_list|,
name|mode
argument_list|,
name|remember
argument_list|,
name|token
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|r
operator|.
name|status
condition|)
block|{
case|case
name|VALID
case|:
name|redirect
argument_list|(
name|r
argument_list|,
name|res
argument_list|)
expr_stmt|;
break|break;
case|case
name|NO_PROVIDER
case|:
name|sendForm
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
literal|"Provider is not supported, or was incorrectly entered."
argument_list|)
expr_stmt|;
break|break;
case|case
name|ERROR
case|:
name|sendForm
argument_list|(
name|req
argument_list|,
name|res
argument_list|,
name|link
argument_list|,
literal|"Unable to connect with OpenID provider."
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
DECL|method|redirect (DiscoveryResult r, HttpServletResponse res)
specifier|private
name|void
name|redirect
parameter_list|(
name|DiscoveryResult
name|r
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|StringBuilder
name|url
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|url
operator|.
name|append
argument_list|(
name|r
operator|.
name|providerUrl
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|providerArgs
operator|!=
literal|null
operator|&&
operator|!
name|r
operator|.
name|providerArgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|arg
range|:
name|r
operator|.
name|providerArgs
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|url
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|url
operator|.
name|append
argument_list|(
literal|'&'
argument_list|)
expr_stmt|;
block|}
name|url
operator|.
name|append
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|arg
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|arg
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|url
operator|.
name|length
argument_list|()
operator|<=
name|maxRedirectUrlLength
condition|)
block|{
name|res
operator|.
name|sendRedirect
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|Document
name|doc
init|=
name|HtmlDomUtil
operator|.
name|parseFile
argument_list|(
name|LoginForm
operator|.
name|class
argument_list|,
literal|"RedirectForm.html"
argument_list|)
decl_stmt|;
name|Element
name|form
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"redirect_form"
argument_list|)
decl_stmt|;
name|form
operator|.
name|setAttribute
argument_list|(
literal|"action"
argument_list|,
name|r
operator|.
name|providerUrl
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|providerArgs
operator|!=
literal|null
operator|&&
operator|!
name|r
operator|.
name|providerArgs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|arg
range|:
name|r
operator|.
name|providerArgs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Element
name|in
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"input"
argument_list|)
decl_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
literal|"hidden"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|arg
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|arg
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|form
operator|.
name|appendChild
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
name|sendHtml
argument_list|(
name|res
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
DECL|method|sendForm ( HttpServletRequest req, HttpServletResponse res, boolean link, @Nullable String errorMessage)
specifier|private
name|void
name|sendForm
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|,
name|boolean
name|link
parameter_list|,
annotation|@
name|Nullable
name|String
name|errorMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|self
init|=
name|req
operator|.
name|getRequestURI
argument_list|()
decl_stmt|;
name|String
name|cancel
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|urlProvider
operator|!=
literal|null
condition|?
name|urlProvider
operator|.
name|get
argument_list|()
else|:
literal|"/"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|cancel
operator|+=
name|LoginUrlToken
operator|.
name|getToken
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|header
operator|.
name|parse
argument_list|(
name|LoginForm
operator|.
name|class
argument_list|,
literal|"LoginForm.html"
argument_list|)
decl_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"hostName"
argument_list|)
operator|.
name|setTextContent
argument_list|(
name|req
operator|.
name|getServerName
argument_list|()
argument_list|)
expr_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"login_form"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"action"
argument_list|,
name|self
argument_list|)
expr_stmt|;
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"cancel_link"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|cancel
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|link
operator|||
name|ssoUrl
operator|!=
literal|null
condition|)
block|{
name|Element
name|input
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"f_link"
argument_list|)
decl_stmt|;
name|input
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
name|String
name|last
init|=
name|getLastId
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|!=
literal|null
condition|)
block|{
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"f_openid"
argument_list|)
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|last
argument_list|)
expr_stmt|;
block|}
name|Element
name|emsg
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"error_message"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|errorMessage
argument_list|)
condition|)
block|{
name|emsg
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|emsg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|emsg
operator|.
name|setTextContent
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|ALL_PROVIDERS
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Element
name|div
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"provider_"
operator|+
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|div
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
operator|!
name|suggestProviders
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|div
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|div
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Element
name|a
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|div
argument_list|,
literal|"id_"
operator|+
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|a
operator|==
literal|null
condition|)
block|{
name|div
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|div
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|StringBuilder
name|u
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|u
operator|.
name|append
argument_list|(
name|self
argument_list|)
operator|.
name|append
argument_list|(
name|a
operator|.
name|getAttribute
argument_list|(
literal|"href"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|link
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"&link"
argument_list|)
expr_stmt|;
block|}
name|a
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// OAuth: Add plugin based providers
name|Element
name|providers
init|=
name|HtmlDomUtil
operator|.
name|find
argument_list|(
name|doc
argument_list|,
literal|"providers"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|plugins
init|=
name|oauthServiceProviders
operator|.
name|plugins
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pluginName
range|:
name|plugins
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|m
init|=
name|oauthServiceProviders
operator|.
name|byPlugin
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|e
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|addProvider
argument_list|(
name|providers
argument_list|,
name|link
argument_list|,
name|pluginName
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|sendHtml
argument_list|(
name|res
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
DECL|method|sendHtml (HttpServletResponse res, Document doc)
specifier|private
name|void
name|sendHtml
parameter_list|(
name|HttpServletResponse
name|res
parameter_list|,
name|Document
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|bin
init|=
name|HtmlDomUtil
operator|.
name|toUTF8
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|res
operator|.
name|setStatus
argument_list|(
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentType
argument_list|(
literal|"text/html"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCharacterEncoding
argument_list|(
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setContentLength
argument_list|(
name|bin
operator|.
name|length
argument_list|)
expr_stmt|;
try|try
init|(
name|ServletOutputStream
name|out
init|=
name|res
operator|.
name|getOutputStream
argument_list|()
init|)
block|{
name|out
operator|.
name|write
argument_list|(
name|bin
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addProvider ( Element form, boolean link, String pluginName, String id, String serviceName)
specifier|private
specifier|static
name|void
name|addProvider
parameter_list|(
name|Element
name|form
parameter_list|,
name|boolean
name|link
parameter_list|,
name|String
name|pluginName
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|serviceName
parameter_list|)
block|{
name|Element
name|div
init|=
name|form
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"div"
argument_list|)
decl_stmt|;
name|div
operator|.
name|setAttribute
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Element
name|hyperlink
init|=
name|form
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|StringBuilder
name|u
init|=
operator|new
name|StringBuilder
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"?id=%s_%s"
argument_list|,
name|pluginName
argument_list|,
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|link
condition|)
block|{
name|u
operator|.
name|append
argument_list|(
literal|"&link"
argument_list|)
expr_stmt|;
block|}
name|hyperlink
operator|.
name|setAttribute
argument_list|(
literal|"href"
argument_list|,
name|u
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|hyperlink
operator|.
name|setTextContent
argument_list|(
name|serviceName
operator|+
literal|" ("
operator|+
name|pluginName
operator|+
literal|" plugin)"
argument_list|)
expr_stmt|;
name|div
operator|.
name|appendChild
argument_list|(
name|hyperlink
argument_list|)
expr_stmt|;
name|form
operator|.
name|appendChild
argument_list|(
name|div
argument_list|)
expr_stmt|;
block|}
DECL|method|lookupOAuthServiceProvider (String providerId)
specifier|private
name|OAuthServiceProvider
name|lookupOAuthServiceProvider
parameter_list|(
name|String
name|providerId
parameter_list|)
block|{
if|if
condition|(
name|providerId
operator|.
name|startsWith
argument_list|(
literal|"http://"
argument_list|)
condition|)
block|{
name|providerId
operator|=
name|providerId
operator|.
name|substring
argument_list|(
literal|"http://"
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|plugins
init|=
name|oauthServiceProviders
operator|.
name|plugins
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|pluginName
range|:
name|plugins
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|m
init|=
name|oauthServiceProviders
operator|.
name|byPlugin
argument_list|(
name|pluginName
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Provider
argument_list|<
name|OAuthServiceProvider
argument_list|>
argument_list|>
name|e
range|:
name|m
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|providerId
operator|.
name|equals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s_%s"
argument_list|,
name|pluginName
argument_list|,
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getLastId (HttpServletRequest req)
specifier|private
specifier|static
name|String
name|getLastId
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
name|Cookie
index|[]
name|cookies
init|=
name|req
operator|.
name|getCookies
argument_list|()
decl_stmt|;
if|if
condition|(
name|cookies
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Cookie
name|c
range|:
name|cookies
control|)
block|{
if|if
condition|(
name|OpenIdUrls
operator|.
name|LASTID_COOKIE
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|c
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isGerritLogin (HttpServletRequest request)
specifier|private
specifier|static
name|boolean
name|isGerritLogin
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|getRequestURI
argument_list|()
operator|.
name|indexOf
argument_list|(
name|OAuthSessionOverOpenID
operator|.
name|GERRIT_LOGIN
argument_list|)
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

