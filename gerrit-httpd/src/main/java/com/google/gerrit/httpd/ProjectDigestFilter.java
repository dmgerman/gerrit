begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|SC_FORBIDDEN
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
name|SC_INTERNAL_SERVER_ERROR
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
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|HashMap
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
name|Map
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
name|ServletContext
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

begin_comment
comment|/**  * Authenticates the current user by HTTP digest authentication.  *<p>  * The current HTTP request is authenticated by looking up the username from the  * Authorization header and checking the digest response against the stored  * password. This filter is intended only to protect the {@link GitOverHttpServlet}  * and its handled URLs, which provide remote repository access over HTTP.  *  * @see<a href="http://www.ietf.org/rfc/rfc2617.txt">RFC 2617</a>  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ProjectDigestFilter
class|class
name|ProjectDigestFilter
implements|implements
name|Filter
block|{
DECL|field|REALM_NAME
specifier|public
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
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
decl_stmt|;
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|tokens
specifier|private
specifier|final
name|SignedToken
name|tokens
decl_stmt|;
DECL|field|context
specifier|private
name|ServletContext
name|context
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectDigestFilter (@anonicalWebUrl @ullable Provider<String> urlProvider, Provider<WebSession> session, AccountCache accountCache, @GerritServerConfig Config config)
name|ProjectDigestFilter
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
name|Provider
argument_list|<
name|WebSession
argument_list|>
name|session
parameter_list|,
name|AccountCache
name|accountCache
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
throws|throws
name|XsrfException
block|{
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|tokens
operator|=
operator|new
name|SignedToken
argument_list|(
operator|(
name|int
operator|)
name|SECONDS
operator|.
name|convert
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
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
block|{
name|context
operator|=
name|config
operator|.
name|getServletContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{   }
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
name|req
argument_list|,
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
specifier|final
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
operator|==
literal|null
operator|||
operator|!
name|hdr
operator|.
name|startsWith
argument_list|(
literal|"Digest "
argument_list|)
condition|)
block|{
comment|// Allow an anonymous connection through, or it might be using a
comment|// session cookie instead of digest authentication.
return|return
literal|true
return|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
init|=
name|parseAuthorization
argument_list|(
name|hdr
argument_list|)
decl_stmt|;
specifier|final
name|String
name|user
init|=
name|p
operator|.
name|get
argument_list|(
literal|"username"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|realm
init|=
name|p
operator|.
name|get
argument_list|(
literal|"realm"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|nonce
init|=
name|p
operator|.
name|get
argument_list|(
literal|"nonce"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|uri
init|=
name|p
operator|.
name|get
argument_list|(
literal|"uri"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|response
init|=
name|p
operator|.
name|get
argument_list|(
literal|"response"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|qop
init|=
name|p
operator|.
name|get
argument_list|(
literal|"qop"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|nc
init|=
name|p
operator|.
name|get
argument_list|(
literal|"nc"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|cnonce
init|=
name|p
operator|.
name|get
argument_list|(
literal|"cnonce"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|method
init|=
name|req
operator|.
name|getMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
comment|//
operator|||
name|realm
operator|==
literal|null
comment|//
operator|||
name|nonce
operator|==
literal|null
comment|//
operator|||
name|uri
operator|==
literal|null
comment|//
operator|||
name|response
operator|==
literal|null
comment|//
operator|||
operator|!
literal|"auth"
operator|.
name|equals
argument_list|(
name|qop
argument_list|)
comment|//
operator|||
operator|!
name|REALM_NAME
operator|.
name|equals
argument_list|(
name|realm
argument_list|)
condition|)
block|{
name|context
operator|.
name|log
argument_list|(
literal|"Invalid header: "
operator|+
name|AUTHORIZATION
operator|+
literal|": "
operator|+
name|hdr
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_FORBIDDEN
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|String
name|username
init|=
name|user
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"userNameToLowerCase"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|username
operator|=
name|username
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AccountState
name|who
init|=
name|accountCache
operator|.
name|getByUsername
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|who
operator|==
literal|null
operator|||
operator|!
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|isActive
argument_list|()
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
specifier|final
name|String
name|passwd
init|=
name|who
operator|.
name|getPassword
argument_list|(
name|username
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwd
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
specifier|final
name|String
name|A1
init|=
name|user
operator|+
literal|":"
operator|+
name|realm
operator|+
literal|":"
operator|+
name|passwd
decl_stmt|;
specifier|final
name|String
name|A2
init|=
name|method
operator|+
literal|":"
operator|+
name|uri
decl_stmt|;
specifier|final
name|String
name|expect
init|=
name|KD
argument_list|(
name|H
argument_list|(
name|A1
argument_list|)
argument_list|,
name|nonce
operator|+
literal|":"
operator|+
name|nc
operator|+
literal|":"
operator|+
name|cnonce
operator|+
literal|":"
operator|+
name|qop
operator|+
literal|":"
operator|+
name|H
argument_list|(
name|A2
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|expect
operator|.
name|equals
argument_list|(
name|response
argument_list|)
condition|)
block|{
try|try
block|{
if|if
condition|(
name|tokens
operator|.
name|checkToken
argument_list|(
name|nonce
argument_list|,
literal|""
argument_list|)
operator|!=
literal|null
condition|)
block|{
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
name|who
operator|.
name|getAccount
argument_list|()
operator|.
name|getId
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
else|else
block|{
name|rsp
operator|.
name|stale
operator|=
literal|true
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
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
name|context
operator|.
name|log
argument_list|(
literal|"Error validating nonce for digest authentication"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rsp
operator|.
name|sendError
argument_list|(
name|SC_INTERNAL_SERVER_ERROR
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
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
DECL|method|H (String data)
specifier|private
specifier|static
name|String
name|H
parameter_list|(
name|String
name|data
parameter_list|)
block|{
try|try
block|{
name|MessageDigest
name|md
init|=
name|newMD5
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|LHEX
argument_list|(
name|md
operator|.
name|digest
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"UTF-8 encoding not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|KD (String secret, String data)
specifier|private
specifier|static
name|String
name|KD
parameter_list|(
name|String
name|secret
parameter_list|,
name|String
name|data
parameter_list|)
block|{
try|try
block|{
name|MessageDigest
name|md
init|=
name|newMD5
argument_list|()
decl_stmt|;
name|md
operator|.
name|update
argument_list|(
name|secret
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
operator|(
name|byte
operator|)
literal|':'
argument_list|)
expr_stmt|;
name|md
operator|.
name|update
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|LHEX
argument_list|(
name|md
operator|.
name|digest
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"UTF-8 encoding not available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|newMD5 ()
specifier|private
specifier|static
name|MessageDigest
name|newMD5
parameter_list|()
block|{
try|try
block|{
return|return
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"No MD5 available"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|field|LHEX
specifier|private
specifier|static
specifier|final
name|char
index|[]
name|LHEX
init|=
block|{
literal|'0'
block|,
literal|'1'
block|,
literal|'2'
block|,
literal|'3'
block|,
literal|'4'
block|,
literal|'5'
block|,
literal|'6'
block|,
literal|'7'
block|,
literal|'8'
block|,
literal|'9'
block|,
comment|//
literal|'a'
block|,
literal|'b'
block|,
literal|'c'
block|,
literal|'d'
block|,
literal|'e'
block|,
literal|'f'
block|}
decl_stmt|;
DECL|method|LHEX (byte[] bin)
specifier|private
specifier|static
name|String
name|LHEX
parameter_list|(
name|byte
index|[]
name|bin
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|(
name|bin
operator|.
name|length
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bin
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|byte
name|b
init|=
name|bin
index|[
name|i
index|]
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|LHEX
index|[
operator|(
name|b
operator|>>>
literal|4
operator|)
operator|&
literal|0x0f
index|]
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|LHEX
index|[
name|b
operator|&
literal|0x0f
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|parseAuthorization (String auth)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|parseAuthorization
parameter_list|(
name|String
name|auth
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|next
init|=
literal|"Digest "
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|next
operator|<
name|auth
operator|.
name|length
argument_list|()
condition|)
block|{
if|if
condition|(
name|next
operator|<
name|auth
operator|.
name|length
argument_list|()
operator|&&
name|auth
operator|.
name|charAt
argument_list|(
name|next
argument_list|)
operator|==
literal|','
condition|)
block|{
name|next
operator|++
expr_stmt|;
block|}
while|while
condition|(
name|next
operator|<
name|auth
operator|.
name|length
argument_list|()
operator|&&
name|Character
operator|.
name|isWhitespace
argument_list|(
name|auth
operator|.
name|charAt
argument_list|(
name|next
argument_list|)
argument_list|)
condition|)
block|{
name|next
operator|++
expr_stmt|;
block|}
name|int
name|eq
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|'='
argument_list|,
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
name|eq
operator|<
literal|0
operator|||
name|eq
operator|+
literal|1
operator|==
name|auth
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
specifier|final
name|String
name|name
init|=
name|auth
operator|.
name|substring
argument_list|(
name|next
argument_list|,
name|eq
argument_list|)
decl_stmt|;
specifier|final
name|String
name|value
decl_stmt|;
if|if
condition|(
name|auth
operator|.
name|charAt
argument_list|(
name|eq
operator|+
literal|1
argument_list|)
operator|==
literal|'"'
condition|)
block|{
name|int
name|dq
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|,
name|eq
operator|+
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|dq
operator|<
literal|0
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
name|value
operator|=
name|auth
operator|.
name|substring
argument_list|(
name|eq
operator|+
literal|2
argument_list|,
name|dq
argument_list|)
expr_stmt|;
name|next
operator|=
name|dq
operator|+
literal|1
expr_stmt|;
block|}
else|else
block|{
name|int
name|space
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|,
name|eq
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|comma
init|=
name|auth
operator|.
name|indexOf
argument_list|(
literal|','
argument_list|,
name|eq
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|space
operator|<
literal|0
condition|)
name|space
operator|=
name|auth
operator|.
name|length
argument_list|()
expr_stmt|;
if|if
condition|(
name|comma
operator|<
literal|0
condition|)
name|comma
operator|=
name|auth
operator|.
name|length
argument_list|()
expr_stmt|;
specifier|final
name|int
name|e
init|=
name|Math
operator|.
name|min
argument_list|(
name|space
argument_list|,
name|comma
argument_list|)
decl_stmt|;
name|value
operator|=
name|auth
operator|.
name|substring
argument_list|(
name|eq
operator|+
literal|1
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|next
operator|=
name|e
operator|+
literal|1
expr_stmt|;
block|}
name|p
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|p
return|;
block|}
DECL|method|newNonce ()
specifier|private
name|String
name|newNonce
parameter_list|()
block|{
try|try
block|{
return|return
name|tokens
operator|.
name|newToken
argument_list|(
literal|""
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot generate new nonce"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|class|Response
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
DECL|field|req
specifier|private
specifier|final
name|HttpServletRequest
name|req
decl_stmt|;
DECL|field|stale
name|Boolean
name|stale
decl_stmt|;
DECL|method|Response (HttpServletRequest req, HttpServletResponse rsp)
name|Response
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|rsp
parameter_list|)
block|{
name|super
argument_list|(
name|rsp
argument_list|)
expr_stmt|;
name|this
operator|.
name|req
operator|=
name|req
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
literal|"Digest"
argument_list|)
expr_stmt|;
name|v
operator|.
name|append
argument_list|(
literal|" realm=\""
operator|+
name|REALM_NAME
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|String
name|url
init|=
name|urlProvider
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|url
operator|=
name|req
operator|.
name|getContextPath
argument_list|()
expr_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
block|}
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|", domain=\""
operator|+
name|url
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
name|v
operator|.
name|append
argument_list|(
literal|", qop=\"auth\""
argument_list|)
expr_stmt|;
if|if
condition|(
name|stale
operator|!=
literal|null
condition|)
block|{
name|v
operator|.
name|append
argument_list|(
literal|", stale="
operator|+
name|stale
argument_list|)
expr_stmt|;
block|}
name|v
operator|.
name|append
argument_list|(
literal|", nonce=\""
operator|+
name|newNonce
argument_list|()
operator|+
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

