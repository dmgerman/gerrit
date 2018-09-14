begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|httpd
operator|.
name|ProjectBasicAuthFilter
operator|.
name|authenticationFailedMsg
import|;
end_import

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
import|import static
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_UNAUTHORIZED
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
name|Iterables
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
name|flogger
operator|.
name|FluentLogger
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
name|OAuthLoginProvider
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
name|registration
operator|.
name|Extension
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
name|AccessPath
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
name|AuthResult
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
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLDecoder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterChain
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|FilterConfig
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponseWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
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

begin_comment
comment|/**  * Authenticates the current user with an OAuth2 server.  *  * @see<a href="https://tools.ietf.org/rfc/rfc6750.txt">RFC 6750</a>  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ProjectOAuthFilter
class|class
name|ProjectOAuthFilter
implements|implements
name|Filter
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|REALM_NAME
specifier|private
specifier|static
specifier|final
name|String
name|REALM_NAME
init|=
literal|"Gerrit Code Review"
decl_stmt|;
DECL|field|AUTHORIZATION
specifier|private
specifier|static
specifier|final
name|String
name|AUTHORIZATION
init|=
literal|"Authorization"
decl_stmt|;
DECL|field|BASIC
specifier|private
specifier|static
specifier|final
name|String
name|BASIC
init|=
literal|"Basic "
decl_stmt|;
DECL|field|GIT_COOKIE_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|GIT_COOKIE_PREFIX
init|=
literal|"git-"
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|loginProviders
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|OAuthLoginProvider
argument_list|>
name|loginProviders
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|accountManager
specifier|private
specifier|final
name|AccountManager
name|accountManager
decl_stmt|;
DECL|field|gitOAuthProvider
specifier|private
specifier|final
name|String
name|gitOAuthProvider
decl_stmt|;
DECL|field|userNameToLowerCase
specifier|private
specifier|final
name|boolean
name|userNameToLowerCase
decl_stmt|;
DECL|field|defaultAuthPlugin
specifier|private
name|String
name|defaultAuthPlugin
decl_stmt|;
DECL|field|defaultAuthProvider
specifier|private
name|String
name|defaultAuthProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectOAuthFilter ( DynamicItem<WebSession> session, DynamicMap<OAuthLoginProvider> pluginsProvider, AccountCache accountCache, AccountManager accountManager, @GerritServerConfig Config gerritConfig)
name|ProjectOAuthFilter
parameter_list|(
name|DynamicItem
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|,
name|DynamicMap
argument_list|<
name|OAuthLoginProvider
argument_list|>
name|pluginsProvider
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
name|AccountManager
name|accountManager
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|gerritConfig
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|loginProviders
operator|=
name|pluginsProvider
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|accountManager
operator|=
name|accountManager
expr_stmt|;
name|this
operator|.
name|gitOAuthProvider
operator|=
name|gerritConfig
operator|.
name|getString
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"gitOAuthProvider"
argument_list|)
expr_stmt|;
name|this
operator|.
name|userNameToLowerCase
operator|=
name|gerritConfig
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"userNameToLowerCase"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init (FilterConfig config)
specifier|public
name|void
name|init
parameter_list|(
name|FilterConfig
name|config
parameter_list|)
throws|throws
name|ServletException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|gitOAuthProvider
argument_list|)
condition|)
block|{
name|pickOnlyProvider
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|pickConfiguredProvider
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|doFilter (ServletRequest request, ServletResponse response, FilterChain chain)
specifier|public
name|void
name|doFilter
parameter_list|(
name|ServletRequest
name|request
parameter_list|,
name|ServletResponse
name|response
parameter_list|,
name|FilterChain
name|chain
parameter_list|)
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|HttpServletRequest
name|req
init|=
operator|(
name|HttpServletRequest
operator|)
name|request
decl_stmt|;
name|Response
name|rsp
init|=
operator|new
name|Response
argument_list|(
operator|(
name|HttpServletResponse
operator|)
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|verify
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
condition|)
block|{
name|chain
operator|.
name|doFilter
argument_list|(
name|req
argument_list|,
name|rsp
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|verify (HttpServletRequest req, Response rsp)
specifier|private
name|boolean
name|verify
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|Response
name|rsp
parameter_list|)
throws|throws
name|IOException
block|{
name|AuthInfo
name|authInfo
decl_stmt|;
comment|// first check if there is a BASIC authentication header
name|String
name|hdr
init|=
name|req
operator|.
name|getHeader
argument_list|(
name|AUTHORIZATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|hdr
operator|!=
literal|null
operator|&&
name|hdr
operator|.
name|startsWith
argument_list|(
name|BASIC
argument_list|)
condition|)
block|{
name|authInfo
operator|=
name|extractAuthInfo
argument_list|(
name|hdr
argument_list|,
name|encoding
argument_list|(
name|req
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|authInfo
operator|==
literal|null
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// if there is no BASIC authentication header, check if there is
comment|// a cookie starting with the prefix "git-"
name|Cookie
name|cookie
init|=
name|findGitCookie
argument_list|(
name|req
argument_list|)
decl_stmt|;
if|if
condition|(
name|cookie
operator|!=
literal|null
condition|)
block|{
name|authInfo
operator|=
name|extractAuthInfo
argument_list|(
name|cookie
argument_list|)
expr_stmt|;
if|if
condition|(
name|authInfo
operator|==
literal|null
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
comment|// if there is no authentication information at all, it might be
comment|// an anonymous connection, or there might be a session cookie
return|return
literal|true
return|;
block|}
block|}
comment|// if there is authentication information but no secret => 401
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|authInfo
operator|.
name|tokenOrSecret
argument_list|)
condition|)
block|{
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Optional
argument_list|<
name|AccountState
argument_list|>
name|who
init|=
name|accountCache
operator|.
name|getByUsername
argument_list|(
name|authInfo
operator|.
name|username
argument_list|)
operator|.
name|filter
argument_list|(
name|a
lambda|->
name|a
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|who
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
name|authenticationFailedMsg
argument_list|(
name|authInfo
operator|.
name|username
argument_list|,
name|req
argument_list|)
operator|+
literal|": account inactive or not provisioned in Gerrit"
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|Account
name|account
init|=
name|who
operator|.
name|get
argument_list|()
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|AuthRequest
name|authRequest
init|=
name|AuthRequest
operator|.
name|forExternalUser
argument_list|(
name|authInfo
operator|.
name|username
argument_list|)
decl_stmt|;
name|authRequest
operator|.
name|setEmailAddress
argument_list|(
name|account
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
name|authRequest
operator|.
name|setDisplayName
argument_list|(
name|account
operator|.
name|getFullName
argument_list|()
argument_list|)
expr_stmt|;
name|authRequest
operator|.
name|setPassword
argument_list|(
name|authInfo
operator|.
name|tokenOrSecret
argument_list|)
expr_stmt|;
name|authRequest
operator|.
name|setAuthPlugin
argument_list|(
name|authInfo
operator|.
name|pluginName
argument_list|)
expr_stmt|;
name|authRequest
operator|.
name|setAuthProvider
argument_list|(
name|authInfo
operator|.
name|exportName
argument_list|)
expr_stmt|;
try|try
block|{
name|AuthResult
name|authResult
init|=
name|accountManager
operator|.
name|authenticate
argument_list|(
name|authRequest
argument_list|)
decl_stmt|;
name|WebSession
name|ws
init|=
name|session
operator|.
name|get
argument_list|()
decl_stmt|;
name|ws
operator|.
name|setUserAccountId
argument_list|(
name|authResult
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|ws
operator|.
name|setAccessPathOk
argument_list|(
name|AccessPath
operator|.
name|GIT
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ws
operator|.
name|setAccessPathOk
argument_list|(
name|AccessPath
operator|.
name|REST_API
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AccountException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
name|authenticationFailedMsg
argument_list|(
name|authInfo
operator|.
name|username
argument_list|,
name|req
argument_list|)
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_UNAUTHORIZED
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**    * Picks the only installed OAuth provider. If there is a multiude of providers available, the    * actual provider must be determined from the authentication request.    *    * @throws ServletException if there is no {@code OAuthLoginProvider} installed at all.    */
DECL|method|pickOnlyProvider ()
specifier|private
name|void
name|pickOnlyProvider
parameter_list|()
throws|throws
name|ServletException
block|{
try|try
block|{
name|Extension
argument_list|<
name|OAuthLoginProvider
argument_list|>
name|loginProvider
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|loginProviders
argument_list|)
decl_stmt|;
name|defaultAuthPlugin
operator|=
name|loginProvider
operator|.
name|getPluginName
argument_list|()
expr_stmt|;
name|defaultAuthProvider
operator|=
name|loginProvider
operator|.
name|getExportName
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchElementException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"No OAuth login provider installed"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// multiple providers found => do not pick any
block|}
block|}
comment|/**    * Picks the {@code OAuthLoginProvider} configured with<tt>auth.gitOAuthProvider</tt>.    *    * @throws ServletException if the configured provider was not found.    */
DECL|method|pickConfiguredProvider ()
specifier|private
name|void
name|pickConfiguredProvider
parameter_list|()
throws|throws
name|ServletException
block|{
name|int
name|splitPos
init|=
name|gitOAuthProvider
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitPos
operator|<
literal|1
operator|||
name|splitPos
operator|==
name|gitOAuthProvider
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
comment|// no colon at all or leading/trailing colon: malformed providerId
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"OAuth login provider configuration is"
operator|+
literal|" invalid: Must be of the form pluginName:providerName"
argument_list|)
throw|;
block|}
name|defaultAuthPlugin
operator|=
name|gitOAuthProvider
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitPos
argument_list|)
expr_stmt|;
name|defaultAuthProvider
operator|=
name|gitOAuthProvider
operator|.
name|substring
argument_list|(
name|splitPos
operator|+
literal|1
argument_list|)
expr_stmt|;
name|OAuthLoginProvider
name|provider
init|=
name|loginProviders
operator|.
name|get
argument_list|(
name|defaultAuthPlugin
argument_list|,
name|defaultAuthProvider
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ServletException
argument_list|(
literal|"Configured OAuth login provider "
operator|+
name|gitOAuthProvider
operator|+
literal|" wasn't installed"
argument_list|)
throw|;
block|}
block|}
DECL|method|extractAuthInfo (String hdr, String encoding)
specifier|private
name|AuthInfo
name|extractAuthInfo
parameter_list|(
name|String
name|hdr
parameter_list|,
name|String
name|encoding
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|byte
index|[]
name|decoded
init|=
name|Base64
operator|.
name|decodeBase64
argument_list|(
name|hdr
operator|.
name|substring
argument_list|(
name|BASIC
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|usernamePassword
init|=
operator|new
name|String
argument_list|(
name|decoded
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|int
name|splitPos
init|=
name|usernamePassword
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitPos
operator|<
literal|1
operator|||
name|splitPos
operator|==
name|usernamePassword
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|AuthInfo
argument_list|(
name|usernamePassword
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitPos
argument_list|)
argument_list|,
name|usernamePassword
operator|.
name|substring
argument_list|(
name|splitPos
operator|+
literal|1
argument_list|)
argument_list|,
name|defaultAuthPlugin
argument_list|,
name|defaultAuthProvider
argument_list|)
return|;
block|}
DECL|method|extractAuthInfo (Cookie cookie)
specifier|private
name|AuthInfo
name|extractAuthInfo
parameter_list|(
name|Cookie
name|cookie
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|String
name|username
init|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|cookie
operator|.
name|getName
argument_list|()
operator|.
name|substring
argument_list|(
name|GIT_COOKIE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|cookie
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|int
name|splitPos
init|=
name|value
operator|.
name|lastIndexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitPos
operator|<
literal|1
operator|||
name|splitPos
operator|==
name|value
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
comment|// no providerId in the cookie value => assume default provider
comment|// note: a leading/trailing at sign is considered to belong to
comment|// the access token rather than being a separator
return|return
operator|new
name|AuthInfo
argument_list|(
name|username
argument_list|,
name|cookie
operator|.
name|getValue
argument_list|()
argument_list|,
name|defaultAuthPlugin
argument_list|,
name|defaultAuthProvider
argument_list|)
return|;
block|}
name|String
name|token
init|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitPos
argument_list|)
decl_stmt|;
name|String
name|providerId
init|=
name|value
operator|.
name|substring
argument_list|(
name|splitPos
operator|+
literal|1
argument_list|)
decl_stmt|;
name|splitPos
operator|=
name|providerId
operator|.
name|lastIndexOf
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
if|if
condition|(
name|splitPos
operator|<
literal|1
operator|||
name|splitPos
operator|==
name|providerId
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
comment|// no colon at all or leading/trailing colon: malformed providerId
return|return
literal|null
return|;
block|}
name|String
name|pluginName
init|=
name|providerId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitPos
argument_list|)
decl_stmt|;
name|String
name|exportName
init|=
name|providerId
operator|.
name|substring
argument_list|(
name|splitPos
operator|+
literal|1
argument_list|)
decl_stmt|;
name|OAuthLoginProvider
name|provider
init|=
name|loginProviders
operator|.
name|get
argument_list|(
name|pluginName
argument_list|,
name|exportName
argument_list|)
decl_stmt|;
if|if
condition|(
name|provider
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|AuthInfo
argument_list|(
name|username
argument_list|,
name|token
argument_list|,
name|pluginName
argument_list|,
name|exportName
argument_list|)
return|;
block|}
DECL|method|encoding (HttpServletRequest req)
specifier|private
specifier|static
name|String
name|encoding
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|req
operator|.
name|getCharacterEncoding
argument_list|()
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|findGitCookie (HttpServletRequest req)
specifier|private
specifier|static
name|Cookie
name|findGitCookie
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
name|cookie
range|:
name|cookies
control|)
block|{
if|if
condition|(
name|cookie
operator|.
name|getName
argument_list|()
operator|.
name|startsWith
argument_list|(
name|GIT_COOKIE_PREFIX
argument_list|)
condition|)
block|{
return|return
name|cookie
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|class|AuthInfo
specifier|private
class|class
name|AuthInfo
block|{
DECL|field|username
specifier|private
specifier|final
name|String
name|username
decl_stmt|;
DECL|field|tokenOrSecret
specifier|private
specifier|final
name|String
name|tokenOrSecret
decl_stmt|;
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|exportName
specifier|private
specifier|final
name|String
name|exportName
decl_stmt|;
DECL|method|AuthInfo (String username, String tokenOrSecret, String pluginName, String exportName)
specifier|private
name|AuthInfo
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|tokenOrSecret
parameter_list|,
name|String
name|pluginName
parameter_list|,
name|String
name|exportName
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|userNameToLowerCase
condition|?
name|username
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
else|:
name|username
expr_stmt|;
name|this
operator|.
name|tokenOrSecret
operator|=
name|tokenOrSecret
expr_stmt|;
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|exportName
operator|=
name|exportName
expr_stmt|;
block|}
block|}
DECL|class|Response
specifier|private
specifier|static
class|class
name|Response
extends|extends
name|HttpServletResponseWrapper
block|{
DECL|field|WWW_AUTHENTICATE
specifier|private
specifier|static
specifier|final
name|String
name|WWW_AUTHENTICATE
init|=
literal|"WWW-Authenticate"
decl_stmt|;
DECL|method|Response (HttpServletResponse rsp)
name|Response
parameter_list|(
name|HttpServletResponse
name|rsp
parameter_list|)
block|{
name|super
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
block|}
DECL|method|status (int sc)
specifier|private
name|void
name|status
parameter_list|(
name|int
name|sc
parameter_list|)
block|{
if|if
condition|(
name|sc
operator|==
name|SC_UNAUTHORIZED
condition|)
block|{
name|StringBuilder
name|v
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|v
operator|.
name|append
argument_list|(
name|BASIC
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
literal|"realm=\""
argument_list|)
operator|.
name|append
argument_list|(
name|REALM_NAME
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
name|WWW_AUTHENTICATE
argument_list|,
name|v
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|containsHeader
argument_list|(
name|WWW_AUTHENTICATE
argument_list|)
condition|)
block|{
name|setHeader
argument_list|(
name|WWW_AUTHENTICATE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|sendError (int sc, String msg)
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|status
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|super
operator|.
name|sendError
argument_list|(
name|sc
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|sendError (int sc)
specifier|public
name|void
name|sendError
parameter_list|(
name|int
name|sc
parameter_list|)
throws|throws
name|IOException
block|{
name|status
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|super
operator|.
name|sendError
argument_list|(
name|sc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|setStatus (int sc, String sm)
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|,
name|String
name|sm
parameter_list|)
block|{
name|status
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|super
operator|.
name|setStatus
argument_list|(
name|sc
argument_list|,
name|sm
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setStatus (int sc)
specifier|public
name|void
name|setStatus
parameter_list|(
name|int
name|sc
parameter_list|)
block|{
name|status
argument_list|(
name|sc
argument_list|)
expr_stmt|;
name|super
operator|.
name|setStatus
argument_list|(
name|sc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

