begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|gerrit
operator|.
name|client
operator|.
name|Gerrit
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
name|client
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
name|client
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|rpc
operator|.
name|Common
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
name|client
operator|.
name|rpc
operator|.
name|Common
operator|.
name|CurrentAccountImpl
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
name|ActiveCall
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
name|ValidToken
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
name|gwtorm
operator|.
name|client
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
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
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
name|client
operator|.
name|Transaction
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
name|servlet
operator|.
name|RequestScoped
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|MessageFormat
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
DECL|class|GerritCall
specifier|public
class|class
name|GerritCall
extends|extends
name|ActiveCall
block|{
static|static
block|{
name|Common
operator|.
name|setCurrentAccountImpl
argument_list|(
operator|new
name|CurrentAccountImpl
argument_list|()
block|{
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
specifier|final
name|GerritCall
name|c
init|=
name|GerritJsonServlet
operator|.
name|getCurrentCall
argument_list|()
decl_stmt|;
return|return
name|c
operator|!=
literal|null
condition|?
name|c
operator|.
name|getAccountId
argument_list|()
else|:
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|field|server
specifier|private
specifier|final
name|GerritServer
name|server
decl_stmt|;
DECL|field|schema
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|field|accountRead
specifier|private
name|boolean
name|accountRead
decl_stmt|;
DECL|field|accountId
specifier|private
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|rememberAccount
specifier|private
name|boolean
name|rememberAccount
decl_stmt|;
annotation|@
name|Inject
DECL|method|GerritCall (final GerritServer gs, final SchemaFactory<ReviewDb> sf, final HttpServletRequest i, final HttpServletResponse o)
name|GerritCall
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
specifier|final
name|HttpServletRequest
name|i
parameter_list|,
specifier|final
name|HttpServletResponse
name|o
parameter_list|)
block|{
name|super
argument_list|(
name|i
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|schema
operator|=
name|sf
expr_stmt|;
name|server
operator|=
name|gs
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (final Throwable error)
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|error
parameter_list|)
block|{
if|if
condition|(
name|error
operator|instanceof
name|OrmException
condition|)
block|{
name|onInternalFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
name|initAccount
argument_list|()
expr_stmt|;
return|return
name|accountId
operator|!=
literal|null
condition|?
name|accountId
operator|.
name|toString
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
name|initAccount
argument_list|()
expr_stmt|;
return|return
name|accountId
return|;
block|}
DECL|method|initAccount ()
specifier|private
name|void
name|initAccount
parameter_list|()
block|{
if|if
condition|(
name|accountRead
condition|)
block|{
return|return;
block|}
name|accountRead
operator|=
literal|true
expr_stmt|;
name|ValidToken
name|accountInfo
decl_stmt|;
name|accountInfo
operator|=
name|getCookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|,
name|server
operator|.
name|getAccountToken
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|accountInfo
operator|==
literal|null
condition|)
block|{
switch|switch
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|HTTP
case|:
if|if
condition|(
name|assumeHttp
argument_list|()
condition|)
block|{
return|return;
block|}
break|break;
block|}
if|if
condition|(
name|getCookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// The cookie is bogus, but it was sent. Send an expired cookie
comment|// back to clear it out of the browser's cookie store.
comment|//
name|removeCookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
try|try
block|{
specifier|final
name|AccountCookie
name|cookie
init|=
name|AccountCookie
operator|.
name|parse
argument_list|(
name|accountInfo
argument_list|)
decl_stmt|;
name|accountId
operator|=
name|cookie
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
name|rememberAccount
operator|=
name|cookie
operator|.
name|isRemember
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Whoa, did we change our cookie format or something? This should
comment|// never happen on a valid acocunt token, but discard it anyway.
comment|//
name|removeCookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|)
expr_stmt|;
name|accountInfo
operator|=
literal|null
expr_stmt|;
name|accountId
operator|=
literal|null
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|accountInfo
operator|.
name|needsRefresh
argument_list|()
condition|)
block|{
comment|// The cookie is valid, but its getting stale. Update it with a
comment|// newer date so it doesn't expire on an active user.
comment|//
name|setAccountCookie
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|assumeHttp ()
specifier|private
name|boolean
name|assumeHttp
parameter_list|()
block|{
specifier|final
name|String
name|hdr
init|=
name|server
operator|.
name|getLoginHttpHeader
argument_list|()
decl_stmt|;
name|String
name|user
decl_stmt|;
if|if
condition|(
name|hdr
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|hdr
argument_list|)
operator|&&
operator|!
literal|"Authorization"
operator|.
name|equalsIgnoreCase
argument_list|(
name|hdr
argument_list|)
condition|)
block|{
name|user
operator|=
name|getHttpServletRequest
argument_list|()
operator|.
name|getHeader
argument_list|(
name|hdr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
name|getHttpServletRequest
argument_list|()
operator|.
name|getRemoteUser
argument_list|()
expr_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
comment|// If the container didn't do the authentication we might
comment|// have done it in the front-end web server. Try to split
comment|// the identity out of the Authorization header and honor it.
comment|//
name|user
operator|=
name|getHttpServletRequest
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Authorization"
argument_list|)
expr_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|user
operator|.
name|startsWith
argument_list|(
literal|"Basic "
argument_list|)
condition|)
block|{
name|user
operator|=
operator|new
name|String
argument_list|(
name|Base64
operator|.
name|decode
argument_list|(
name|user
operator|.
name|substring
argument_list|(
literal|"Basic "
operator|.
name|length
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|user
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|user
operator|=
name|user
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|user
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|user
operator|.
name|startsWith
argument_list|(
literal|"Digest "
argument_list|)
operator|&&
name|user
operator|.
name|contains
argument_list|(
literal|"username=\""
argument_list|)
condition|)
block|{
name|user
operator|=
name|user
operator|.
name|substring
argument_list|(
name|user
operator|.
name|indexOf
argument_list|(
literal|"username=\""
argument_list|)
operator|+
literal|10
argument_list|)
expr_stmt|;
name|user
operator|=
name|user
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|user
operator|.
name|indexOf
argument_list|(
literal|'"'
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|user
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|user
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|eid
init|=
literal|"gerrit:"
operator|+
name|user
decl_stmt|;
specifier|final
name|List
argument_list|<
name|AccountExternalId
argument_list|>
name|matches
init|=
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|byExternal
argument_list|(
name|eid
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// Account exists, connect to it again.
comment|//
specifier|final
name|AccountExternalId
name|e
init|=
name|matches
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|e
operator|.
name|setLastUsedOn
argument_list|()
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|accountId
operator|=
name|e
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
name|rememberAccount
operator|=
literal|false
expr_stmt|;
name|setAccountCookie
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|matches
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// No account, automatically initialize a new one.
comment|//
specifier|final
name|Transaction
name|txn
init|=
name|db
operator|.
name|beginTransaction
argument_list|()
decl_stmt|;
specifier|final
name|Account
operator|.
name|Id
name|nid
init|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|db
operator|.
name|nextAccountId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Account
name|a
init|=
operator|new
name|Account
argument_list|(
name|nid
argument_list|)
decl_stmt|;
name|a
operator|.
name|setFullName
argument_list|(
name|user
argument_list|)
expr_stmt|;
specifier|final
name|String
name|efmt
init|=
name|server
operator|.
name|getEmailFormat
argument_list|()
decl_stmt|;
if|if
condition|(
name|efmt
operator|!=
literal|null
operator|&&
name|efmt
operator|.
name|contains
argument_list|(
literal|"{0}"
argument_list|)
condition|)
block|{
name|a
operator|.
name|setPreferredEmail
argument_list|(
name|MessageFormat
operator|.
name|format
argument_list|(
name|efmt
argument_list|,
name|user
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AccountExternalId
name|e
init|=
operator|new
name|AccountExternalId
argument_list|(
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|nid
argument_list|,
name|eid
argument_list|)
argument_list|)
decl_stmt|;
name|e
operator|.
name|setEmailAddress
argument_list|(
name|a
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setLastUsedOn
argument_list|()
expr_stmt|;
name|db
operator|.
name|accounts
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|a
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|db
operator|.
name|accountExternalIds
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|e
argument_list|)
argument_list|,
name|txn
argument_list|)
expr_stmt|;
name|txn
operator|.
name|commit
argument_list|()
expr_stmt|;
name|accountId
operator|=
name|nid
expr_stmt|;
name|rememberAccount
operator|=
literal|false
expr_stmt|;
name|setAccountCookie
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{     }
return|return
literal|false
return|;
block|}
DECL|method|setAccountCookie ()
specifier|private
name|void
name|setAccountCookie
parameter_list|()
block|{
specifier|final
name|AccountCookie
name|ac
init|=
operator|new
name|AccountCookie
argument_list|(
name|accountId
argument_list|,
name|rememberAccount
argument_list|)
decl_stmt|;
name|String
name|val
decl_stmt|;
name|int
name|age
decl_stmt|;
try|try
block|{
name|val
operator|=
name|server
operator|.
name|getAccountToken
argument_list|()
operator|.
name|newToken
argument_list|(
name|ac
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|age
operator|=
name|ac
operator|.
name|isRemember
argument_list|()
condition|?
name|server
operator|.
name|getSessionAge
argument_list|()
else|:
operator|-
literal|1
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
name|val
operator|=
literal|""
expr_stmt|;
name|age
operator|=
literal|0
expr_stmt|;
block|}
name|setCookie
argument_list|(
name|Gerrit
operator|.
name|ACCOUNT_COOKIE
argument_list|,
name|val
argument_list|,
name|age
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

