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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|CacheBasedWebSession
operator|.
name|MAX_AGE_MINUTES
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readFixInt64
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readString
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|readVarInt32
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeBytes
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeFixInt64
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeString
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
name|ioutil
operator|.
name|BasicSerialization
operator|.
name|writeVarInt32
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
name|MILLISECONDS
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
name|MINUTES
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
name|config
operator|.
name|ConfigUtil
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
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
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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

begin_class
annotation|@
name|Singleton
DECL|class|WebSessionManager
class|class
name|WebSessionManager
block|{
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
name|WebSessionManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CACHE_NAME
specifier|static
specifier|final
name|String
name|CACHE_NAME
init|=
literal|"web_sessions"
decl_stmt|;
DECL|method|now ()
specifier|static
name|long
name|now
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
DECL|field|sessionMaxAgeMillis
specifier|private
specifier|final
name|long
name|sessionMaxAgeMillis
decl_stmt|;
DECL|field|prng
specifier|private
specifier|final
name|SecureRandom
name|prng
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Cache
argument_list|<
name|String
argument_list|,
name|Val
argument_list|>
name|self
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebSessionManager (@erritServerConfig Config cfg, @Named(CACHE_NAME) final Cache<String, Val> cache)
name|WebSessionManager
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|Named
argument_list|(
name|CACHE_NAME
argument_list|)
specifier|final
name|Cache
argument_list|<
name|String
argument_list|,
name|Val
argument_list|>
name|cache
parameter_list|)
block|{
name|prng
operator|=
operator|new
name|SecureRandom
argument_list|()
expr_stmt|;
name|self
operator|=
name|cache
expr_stmt|;
name|sessionMaxAgeMillis
operator|=
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"cache"
argument_list|,
name|CACHE_NAME
argument_list|,
literal|"maxAge"
argument_list|,
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|MAX_AGE_MINUTES
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|sessionMaxAgeMillis
operator|<
name|MINUTES
operator|.
name|toMillis
argument_list|(
literal|5
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"cache.%s.maxAge is set to %d milliseconds;"
operator|+
literal|" it should be at least 5 minutes."
argument_list|,
name|CACHE_NAME
argument_list|,
name|sessionMaxAgeMillis
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createKey (final Account.Id who)
name|Key
name|createKey
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|)
block|{
return|return
operator|new
name|Key
argument_list|(
name|newUniqueToken
argument_list|(
name|who
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newUniqueToken (final Account.Id who)
specifier|private
name|String
name|newUniqueToken
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|)
block|{
try|try
block|{
specifier|final
name|int
name|nonceLen
init|=
literal|20
decl_stmt|;
specifier|final
name|ByteArrayOutputStream
name|buf
decl_stmt|;
specifier|final
name|byte
index|[]
name|rnd
init|=
operator|new
name|byte
index|[
name|nonceLen
index|]
decl_stmt|;
name|prng
operator|.
name|nextBytes
argument_list|(
name|rnd
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|3
operator|+
name|nonceLen
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|buf
argument_list|,
operator|(
name|int
operator|)
name|Val
operator|.
name|serialVersionUID
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|buf
argument_list|,
name|who
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|writeBytes
argument_list|(
name|buf
argument_list|,
name|rnd
argument_list|)
expr_stmt|;
return|return
name|CookieBase64
operator|.
name|encode
argument_list|(
name|buf
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Cannot produce new account cookie"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createVal (final Key key, final Val val)
name|Val
name|createVal
parameter_list|(
specifier|final
name|Key
name|key
parameter_list|,
specifier|final
name|Val
name|val
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|who
init|=
name|val
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|remember
init|=
name|val
operator|.
name|isPersistentCookie
argument_list|()
decl_stmt|;
specifier|final
name|AccountExternalId
operator|.
name|Key
name|lastLogin
init|=
name|val
operator|.
name|getExternalId
argument_list|()
decl_stmt|;
return|return
name|createVal
argument_list|(
name|key
argument_list|,
name|who
argument_list|,
name|remember
argument_list|,
name|lastLogin
argument_list|,
name|val
operator|.
name|sessionId
argument_list|,
name|val
operator|.
name|auth
argument_list|)
return|;
block|}
DECL|method|createVal (final Key key, final Account.Id who, final boolean remember, final AccountExternalId.Key lastLogin, String sid, String auth)
name|Val
name|createVal
parameter_list|(
specifier|final
name|Key
name|key
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|,
specifier|final
name|boolean
name|remember
parameter_list|,
specifier|final
name|AccountExternalId
operator|.
name|Key
name|lastLogin
parameter_list|,
name|String
name|sid
parameter_list|,
name|String
name|auth
parameter_list|)
block|{
comment|// Refresh the cookie every hour or when it is half-expired.
comment|// This reduces the odds that the user session will be kicked
comment|// early but also avoids us needing to refresh the cookie on
comment|// every single request.
comment|//
specifier|final
name|long
name|halfAgeRefresh
init|=
name|sessionMaxAgeMillis
operator|>>>
literal|1
decl_stmt|;
specifier|final
name|long
name|minRefresh
init|=
name|MILLISECONDS
operator|.
name|convert
argument_list|(
literal|1
argument_list|,
name|HOURS
argument_list|)
decl_stmt|;
specifier|final
name|long
name|refresh
init|=
name|Math
operator|.
name|min
argument_list|(
name|halfAgeRefresh
argument_list|,
name|minRefresh
argument_list|)
decl_stmt|;
specifier|final
name|long
name|now
init|=
name|now
argument_list|()
decl_stmt|;
specifier|final
name|long
name|refreshCookieAt
init|=
name|now
operator|+
name|refresh
decl_stmt|;
specifier|final
name|long
name|expiresAt
init|=
name|now
operator|+
name|sessionMaxAgeMillis
decl_stmt|;
if|if
condition|(
name|sid
operator|==
literal|null
condition|)
block|{
name|sid
operator|=
name|newUniqueToken
argument_list|(
name|who
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|auth
operator|==
literal|null
condition|)
block|{
name|auth
operator|=
name|newUniqueToken
argument_list|(
name|who
argument_list|)
expr_stmt|;
block|}
name|Val
name|val
init|=
operator|new
name|Val
argument_list|(
name|who
argument_list|,
name|refreshCookieAt
argument_list|,
name|remember
argument_list|,
name|lastLogin
argument_list|,
name|expiresAt
argument_list|,
name|sid
argument_list|,
name|auth
argument_list|)
decl_stmt|;
name|self
operator|.
name|put
argument_list|(
name|key
operator|.
name|token
argument_list|,
name|val
argument_list|)
expr_stmt|;
return|return
name|val
return|;
block|}
DECL|method|getCookieAge (final Val val)
name|int
name|getCookieAge
parameter_list|(
specifier|final
name|Val
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|.
name|isPersistentCookie
argument_list|()
condition|)
block|{
comment|// Client may store the cookie until we would remove it from our
comment|// own cache, after which it will certainly be invalid.
comment|//
return|return
operator|(
name|int
operator|)
name|MILLISECONDS
operator|.
name|toSeconds
argument_list|(
name|sessionMaxAgeMillis
argument_list|)
return|;
block|}
else|else
block|{
comment|// Client should not store the cookie, as the user asked for us
comment|// to not remember them long-term. Sending -1 as the age will
comment|// cause the cookie to be only for this "browser session", which
comment|// is usually until the user exits their browser.
comment|//
return|return
operator|-
literal|1
return|;
block|}
block|}
DECL|method|get (final Key key)
name|Val
name|get
parameter_list|(
specifier|final
name|Key
name|key
parameter_list|)
block|{
name|Val
name|val
init|=
name|self
operator|.
name|getIfPresent
argument_list|(
name|key
operator|.
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
operator|&&
name|val
operator|.
name|expiresAt
operator|<=
name|now
argument_list|()
condition|)
block|{
name|self
operator|.
name|invalidate
argument_list|(
name|key
operator|.
name|token
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|val
return|;
block|}
DECL|method|destroy (final Key key)
name|void
name|destroy
parameter_list|(
specifier|final
name|Key
name|key
parameter_list|)
block|{
name|self
operator|.
name|invalidate
argument_list|(
name|key
operator|.
name|token
argument_list|)
expr_stmt|;
block|}
DECL|class|Key
specifier|static
specifier|final
class|class
name|Key
block|{
DECL|field|token
specifier|private
specifier|transient
name|String
name|token
decl_stmt|;
DECL|method|Key (final String t)
name|Key
parameter_list|(
specifier|final
name|String
name|t
parameter_list|)
block|{
name|token
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getToken ()
name|String
name|getToken
parameter_list|()
block|{
return|return
name|token
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|token
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
return|return
name|obj
operator|instanceof
name|Key
operator|&&
name|token
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|Key
operator|)
name|obj
operator|)
operator|.
name|token
argument_list|)
return|;
block|}
block|}
DECL|class|Val
specifier|static
specifier|final
class|class
name|Val
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2L
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|transient
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|refreshCookieAt
specifier|private
specifier|transient
name|long
name|refreshCookieAt
decl_stmt|;
DECL|field|persistentCookie
specifier|private
specifier|transient
name|boolean
name|persistentCookie
decl_stmt|;
DECL|field|externalId
specifier|private
specifier|transient
name|AccountExternalId
operator|.
name|Key
name|externalId
decl_stmt|;
DECL|field|expiresAt
specifier|private
specifier|transient
name|long
name|expiresAt
decl_stmt|;
DECL|field|sessionId
specifier|private
specifier|transient
name|String
name|sessionId
decl_stmt|;
DECL|field|auth
specifier|private
specifier|transient
name|String
name|auth
decl_stmt|;
DECL|method|Val (final Account.Id accountId, final long refreshCookieAt, final boolean persistentCookie, final AccountExternalId.Key externalId, final long expiresAt, final String sessionId, final String auth)
name|Val
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
specifier|final
name|long
name|refreshCookieAt
parameter_list|,
specifier|final
name|boolean
name|persistentCookie
parameter_list|,
specifier|final
name|AccountExternalId
operator|.
name|Key
name|externalId
parameter_list|,
specifier|final
name|long
name|expiresAt
parameter_list|,
specifier|final
name|String
name|sessionId
parameter_list|,
specifier|final
name|String
name|auth
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|refreshCookieAt
operator|=
name|refreshCookieAt
expr_stmt|;
name|this
operator|.
name|persistentCookie
operator|=
name|persistentCookie
expr_stmt|;
name|this
operator|.
name|externalId
operator|=
name|externalId
expr_stmt|;
name|this
operator|.
name|expiresAt
operator|=
name|expiresAt
expr_stmt|;
name|this
operator|.
name|sessionId
operator|=
name|sessionId
expr_stmt|;
name|this
operator|.
name|auth
operator|=
name|auth
expr_stmt|;
block|}
DECL|method|getAccountId ()
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
DECL|method|getExternalId ()
name|AccountExternalId
operator|.
name|Key
name|getExternalId
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
DECL|method|getSessionId ()
name|String
name|getSessionId
parameter_list|()
block|{
return|return
name|sessionId
return|;
block|}
DECL|method|getAuth ()
name|String
name|getAuth
parameter_list|()
block|{
return|return
name|auth
return|;
block|}
DECL|method|needsCookieRefresh ()
name|boolean
name|needsCookieRefresh
parameter_list|()
block|{
return|return
name|refreshCookieAt
operator|<=
name|now
argument_list|()
return|;
block|}
DECL|method|isPersistentCookie ()
name|boolean
name|isPersistentCookie
parameter_list|()
block|{
return|return
name|persistentCookie
return|;
block|}
DECL|method|writeObject (final ObjectOutputStream out)
specifier|private
name|void
name|writeObject
parameter_list|(
specifier|final
name|ObjectOutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|writeFixInt64
argument_list|(
name|out
argument_list|,
name|refreshCookieAt
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|writeVarInt32
argument_list|(
name|out
argument_list|,
name|persistentCookie
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|externalId
operator|!=
literal|null
condition|)
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|writeString
argument_list|(
name|out
argument_list|,
name|externalId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sessionId
operator|!=
literal|null
condition|)
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|writeString
argument_list|(
name|out
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
block|}
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|writeFixInt64
argument_list|(
name|out
argument_list|,
name|expiresAt
argument_list|)
expr_stmt|;
if|if
condition|(
name|auth
operator|!=
literal|null
condition|)
block|{
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|7
argument_list|)
expr_stmt|;
name|writeString
argument_list|(
name|out
argument_list|,
name|auth
argument_list|)
expr_stmt|;
block|}
name|writeVarInt32
argument_list|(
name|out
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|readObject (final ObjectInputStream in)
specifier|private
name|void
name|readObject
parameter_list|(
specifier|final
name|ObjectInputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|PARSE
label|:
for|for
control|(
init|;
condition|;
control|)
block|{
specifier|final
name|int
name|tag
init|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|tag
condition|)
block|{
case|case
literal|0
case|:
break|break
name|PARSE
break|;
case|case
literal|1
case|:
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|readVarInt32
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|2
case|:
name|refreshCookieAt
operator|=
name|readFixInt64
argument_list|(
name|in
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|3
case|:
name|persistentCookie
operator|=
name|readVarInt32
argument_list|(
name|in
argument_list|)
operator|!=
literal|0
expr_stmt|;
continue|continue;
case|case
literal|4
case|:
name|externalId
operator|=
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|readString
argument_list|(
name|in
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|5
case|:
name|sessionId
operator|=
name|readString
argument_list|(
name|in
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|6
case|:
name|expiresAt
operator|=
name|readFixInt64
argument_list|(
name|in
argument_list|)
expr_stmt|;
continue|continue;
case|case
literal|7
case|:
name|auth
operator|=
name|readString
argument_list|(
name|in
argument_list|)
expr_stmt|;
continue|continue;
default|default:
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown tag found in object: "
operator|+
name|tag
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|expiresAt
operator|==
literal|0
condition|)
block|{
name|expiresAt
operator|=
name|refreshCookieAt
operator|+
name|TimeUnit
operator|.
name|HOURS
operator|.
name|toMillis
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

