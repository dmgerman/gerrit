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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|HOURS
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
name|WebSessionManager
operator|.
name|Key
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
name|WebSessionManager
operator|.
name|Val
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
name|AnonymousUser
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
name|cache
operator|.
name|Cache
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
name|cache
operator|.
name|CacheModule
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
name|cache
operator|.
name|EvictionPolicy
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
name|Module
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
name|TypeLiteral
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
name|servlet
operator|.
name|RequestScoped
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

begin_class
annotation|@
name|RequestScoped
DECL|class|WebSession
specifier|public
specifier|final
class|class
name|WebSession
block|{
DECL|field|ACCOUNT_COOKIE
specifier|private
specifier|static
specifier|final
name|String
name|ACCOUNT_COOKIE
init|=
literal|"GerritAccount"
decl_stmt|;
DECL|method|module ()
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|CacheModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
specifier|final
name|String
name|cacheName
init|=
name|WebSessionManager
operator|.
name|CACHE_NAME
decl_stmt|;
specifier|final
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|Key
argument_list|,
name|Val
argument_list|>
argument_list|>
name|type
init|=
operator|new
name|TypeLiteral
argument_list|<
name|Cache
argument_list|<
name|Key
argument_list|,
name|Val
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
name|disk
argument_list|(
name|type
argument_list|,
name|cacheName
argument_list|)
comment|//
operator|.
name|memoryLimit
argument_list|(
literal|1024
argument_list|)
comment|// reasonable default for many sites
operator|.
name|maxAge
argument_list|(
literal|12
argument_list|,
name|HOURS
argument_list|)
comment|// expire sessions if they are inactive
operator|.
name|evictionPolicy
argument_list|(
name|EvictionPolicy
operator|.
name|LRU
argument_list|)
comment|// keep most recently used
expr_stmt|;
name|bind
argument_list|(
name|WebSessionManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|WebSession
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|field|request
specifier|private
specifier|final
name|HttpServletRequest
name|request
decl_stmt|;
DECL|field|response
specifier|private
specifier|final
name|HttpServletResponse
name|response
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|WebSessionManager
name|manager
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|field|anonymousProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousProvider
decl_stmt|;
DECL|field|identified
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|identified
decl_stmt|;
DECL|field|accessPath
specifier|private
name|AccessPath
name|accessPath
init|=
name|AccessPath
operator|.
name|WEB_UI
decl_stmt|;
DECL|field|outCookie
specifier|private
name|Cookie
name|outCookie
decl_stmt|;
DECL|field|key
specifier|private
name|Key
name|key
decl_stmt|;
DECL|field|val
specifier|private
name|Val
name|val
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebSession (final HttpServletRequest request, final HttpServletResponse response, final WebSessionManager manager, final AuthConfig authConfig, final Provider<AnonymousUser> anonymousProvider, final IdentifiedUser.RequestFactory identified)
name|WebSession
parameter_list|(
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
specifier|final
name|HttpServletResponse
name|response
parameter_list|,
specifier|final
name|WebSessionManager
name|manager
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|AnonymousUser
argument_list|>
name|anonymousProvider
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|identified
parameter_list|)
block|{
name|this
operator|.
name|request
operator|=
name|request
expr_stmt|;
name|this
operator|.
name|response
operator|=
name|response
expr_stmt|;
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
expr_stmt|;
name|this
operator|.
name|anonymousProvider
operator|=
name|anonymousProvider
expr_stmt|;
name|this
operator|.
name|identified
operator|=
name|identified
expr_stmt|;
specifier|final
name|String
name|cookie
init|=
name|readCookie
argument_list|()
decl_stmt|;
if|if
condition|(
name|cookie
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
operator|new
name|Key
argument_list|(
name|cookie
argument_list|)
expr_stmt|;
name|val
operator|=
name|manager
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|key
operator|=
literal|null
expr_stmt|;
name|val
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|isSignedIn
argument_list|()
operator|&&
name|val
operator|.
name|needsCookieRefresh
argument_list|()
condition|)
block|{
comment|// Cookie is more than half old. Send the cookie again to the
comment|// client with an updated expiration date. We don't dare to
comment|// change the key token here because there may be other RPCs
comment|// queued up in the browser whose xsrfKey would not get updated
comment|// with the new token, causing them to fail.
comment|//
name|val
operator|=
name|manager
operator|.
name|createVal
argument_list|(
name|key
argument_list|,
name|val
argument_list|)
expr_stmt|;
name|saveCookie
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|readCookie ()
specifier|private
name|String
name|readCookie
parameter_list|()
block|{
specifier|final
name|Cookie
index|[]
name|all
init|=
name|request
operator|.
name|getCookies
argument_list|()
decl_stmt|;
if|if
condition|(
name|all
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Cookie
name|c
range|:
name|all
control|)
block|{
if|if
condition|(
name|ACCOUNT_COOKIE
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
specifier|final
name|String
name|v
init|=
name|c
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|v
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|?
name|v
else|:
literal|null
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isSignedIn ()
specifier|public
name|boolean
name|isSignedIn
parameter_list|()
block|{
return|return
name|val
operator|!=
literal|null
return|;
block|}
DECL|method|getToken ()
specifier|public
name|String
name|getToken
parameter_list|()
block|{
return|return
name|isSignedIn
argument_list|()
condition|?
name|val
operator|.
name|getXsrfToken
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|isTokenValid (final String inputToken)
specifier|public
name|boolean
name|isTokenValid
parameter_list|(
specifier|final
name|String
name|inputToken
parameter_list|)
block|{
return|return
name|isSignedIn
argument_list|()
comment|//
operator|&&
name|val
operator|.
name|getXsrfToken
argument_list|()
operator|!=
literal|null
comment|//
operator|&&
name|val
operator|.
name|getXsrfToken
argument_list|()
operator|.
name|equals
argument_list|(
name|inputToken
argument_list|)
return|;
block|}
DECL|method|getLastLoginExternalId ()
specifier|public
name|AccountExternalId
operator|.
name|Key
name|getLastLoginExternalId
parameter_list|()
block|{
return|return
name|val
operator|!=
literal|null
condition|?
name|val
operator|.
name|getExternalId
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getCurrentUser ()
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
if|if
condition|(
name|isSignedIn
argument_list|()
condition|)
block|{
return|return
name|identified
operator|.
name|create
argument_list|(
name|accessPath
argument_list|,
name|val
operator|.
name|getAccountId
argument_list|()
argument_list|)
return|;
block|}
return|return
name|anonymousProvider
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|login (final AuthResult res, final boolean rememberMe)
specifier|public
name|void
name|login
parameter_list|(
specifier|final
name|AuthResult
name|res
parameter_list|,
specifier|final
name|boolean
name|rememberMe
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|id
init|=
name|res
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|AccountExternalId
operator|.
name|Key
name|identity
init|=
name|res
operator|.
name|getExternalId
argument_list|()
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|destroy
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|key
operator|=
name|manager
operator|.
name|createKey
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|val
operator|=
name|manager
operator|.
name|createVal
argument_list|(
name|key
argument_list|,
name|id
argument_list|,
name|rememberMe
argument_list|,
name|identity
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|saveCookie
argument_list|()
expr_stmt|;
block|}
comment|/** Change the access path from the default of {@link AccessPath#WEB_UI}. */
DECL|method|setAccessPath (AccessPath path)
name|void
name|setAccessPath
parameter_list|(
name|AccessPath
name|path
parameter_list|)
block|{
name|accessPath
operator|=
name|path
expr_stmt|;
block|}
comment|/** Set the user account for this current request only. */
DECL|method|setUserAccountId (Account.Id id)
name|void
name|setUserAccountId
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|key
operator|=
operator|new
name|Key
argument_list|(
literal|"id:"
operator|+
name|id
argument_list|)
expr_stmt|;
name|val
operator|=
operator|new
name|Val
argument_list|(
name|id
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|logout ()
specifier|public
name|void
name|logout
parameter_list|()
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|manager
operator|.
name|destroy
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|key
operator|=
literal|null
expr_stmt|;
name|val
operator|=
literal|null
expr_stmt|;
name|saveCookie
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|saveCookie ()
specifier|private
name|void
name|saveCookie
parameter_list|()
block|{
specifier|final
name|String
name|token
decl_stmt|;
specifier|final
name|int
name|ageSeconds
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|token
operator|=
literal|""
expr_stmt|;
name|ageSeconds
operator|=
literal|0
comment|/* erase at client */
expr_stmt|;
block|}
else|else
block|{
name|token
operator|=
name|key
operator|.
name|getToken
argument_list|()
expr_stmt|;
name|ageSeconds
operator|=
name|manager
operator|.
name|getCookieAge
argument_list|(
name|val
argument_list|)
expr_stmt|;
block|}
name|String
name|path
init|=
name|authConfig
operator|.
name|getCookiePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|path
operator|=
name|request
operator|.
name|getContextPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|path
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|path
operator|=
literal|"/"
expr_stmt|;
block|}
block|}
if|if
condition|(
name|outCookie
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cookie "
operator|+
name|ACCOUNT_COOKIE
operator|+
literal|" was set"
argument_list|)
throw|;
block|}
name|outCookie
operator|=
operator|new
name|Cookie
argument_list|(
name|ACCOUNT_COOKIE
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|outCookie
operator|.
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|outCookie
operator|.
name|setMaxAge
argument_list|(
name|ageSeconds
argument_list|)
expr_stmt|;
name|outCookie
operator|.
name|setSecure
argument_list|(
name|authConfig
operator|.
name|getCookieSecure
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|addCookie
argument_list|(
name|outCookie
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

