begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|hash
operator|.
name|Hashing
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
name|primitives
operator|.
name|Ints
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
name|Set
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
name|errors
operator|.
name|ConfigInvalidException
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|ExternalId
specifier|public
specifier|abstract
class|class
name|ExternalId
implements|implements
name|Serializable
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
DECL|field|EXTERNAL_ID_SECTION
specifier|private
specifier|static
specifier|final
name|String
name|EXTERNAL_ID_SECTION
init|=
literal|"externalId"
decl_stmt|;
DECL|field|ACCOUNT_ID_KEY
specifier|private
specifier|static
specifier|final
name|String
name|ACCOUNT_ID_KEY
init|=
literal|"accountId"
decl_stmt|;
DECL|field|EMAIL_KEY
specifier|private
specifier|static
specifier|final
name|String
name|EMAIL_KEY
init|=
literal|"email"
decl_stmt|;
DECL|field|PASSWORD_KEY
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD_KEY
init|=
literal|"password"
decl_stmt|;
comment|/**    * Scheme used for {@link AuthType#LDAP}, {@link AuthType#CLIENT_SSL_CERT_LDAP}, {@link    * AuthType#HTTP_LDAP}, and {@link AuthType#LDAP_BIND} usernames.    *    *<p>The name {@code gerrit:} was a very poor choice.    */
DECL|field|SCHEME_GERRIT
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_GERRIT
init|=
literal|"gerrit"
decl_stmt|;
comment|/** Scheme used for randomly created identities constructed by a UUID. */
DECL|field|SCHEME_UUID
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_UUID
init|=
literal|"uuid"
decl_stmt|;
comment|/** Scheme used to represent only an email address. */
DECL|field|SCHEME_MAILTO
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_MAILTO
init|=
literal|"mailto"
decl_stmt|;
comment|/** Scheme for the username used to authenticate an account, e.g. over SSH. */
DECL|field|SCHEME_USERNAME
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_USERNAME
init|=
literal|"username"
decl_stmt|;
comment|/** Scheme used for GPG public keys. */
DECL|field|SCHEME_GPGKEY
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_GPGKEY
init|=
literal|"gpgkey"
decl_stmt|;
comment|/** Scheme for external auth used during authentication, e.g. OAuth Token */
DECL|field|SCHEME_EXTERNAL
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_EXTERNAL
init|=
literal|"external"
decl_stmt|;
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
implements|implements
name|Serializable
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
DECL|method|create (@ullable String scheme, String id)
specifier|public
specifier|static
name|Key
name|create
parameter_list|(
annotation|@
name|Nullable
name|String
name|scheme
parameter_list|,
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ExternalId_Key
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|scheme
argument_list|)
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|from (AccountExternalId.Key externalIdKey)
specifier|public
specifier|static
name|ExternalId
operator|.
name|Key
name|from
parameter_list|(
name|AccountExternalId
operator|.
name|Key
name|externalIdKey
parameter_list|)
block|{
return|return
name|parse
argument_list|(
name|externalIdKey
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Parses an external ID key from a string in the format "scheme:id" or "id".      *      * @return the parsed external ID key      */
DECL|method|parse (String externalId)
specifier|public
specifier|static
name|Key
name|parse
parameter_list|(
name|String
name|externalId
parameter_list|)
block|{
name|int
name|c
init|=
name|externalId
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|1
operator|||
name|c
operator|>=
name|externalId
operator|.
name|length
argument_list|()
operator|-
literal|1
condition|)
block|{
return|return
name|create
argument_list|(
literal|null
argument_list|,
name|externalId
argument_list|)
return|;
block|}
return|return
name|create
argument_list|(
name|externalId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|c
argument_list|)
argument_list|,
name|externalId
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toAccountExternalIdKeys ( Collection<ExternalId.Key> extIdKeys)
specifier|public
specifier|static
name|Set
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|toAccountExternalIdKeys
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
operator|.
name|Key
argument_list|>
name|extIdKeys
parameter_list|)
block|{
return|return
name|extIdKeys
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|k
lambda|->
name|k
operator|.
name|asAccountExternalIdKey
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|scheme ()
specifier|public
specifier|abstract
annotation|@
name|Nullable
name|String
name|scheme
parameter_list|()
function_decl|;
DECL|method|id ()
specifier|public
specifier|abstract
name|String
name|id
parameter_list|()
function_decl|;
DECL|method|isScheme (String scheme)
specifier|public
name|boolean
name|isScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
return|return
name|scheme
operator|.
name|equals
argument_list|(
name|scheme
argument_list|()
argument_list|)
return|;
block|}
DECL|method|asAccountExternalIdKey ()
specifier|public
name|AccountExternalId
operator|.
name|Key
name|asAccountExternalIdKey
parameter_list|()
block|{
if|if
condition|(
name|scheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|scheme
argument_list|()
argument_list|,
name|id
argument_list|()
argument_list|)
return|;
block|}
return|return
operator|new
name|AccountExternalId
operator|.
name|Key
argument_list|(
name|id
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the SHA1 of the external ID that is used as note ID in the refs/meta/external-ids      * notes branch.      */
DECL|method|sha1 ()
specifier|public
name|ObjectId
name|sha1
parameter_list|()
block|{
return|return
name|ObjectId
operator|.
name|fromRaw
argument_list|(
name|Hashing
operator|.
name|sha1
argument_list|()
operator|.
name|hashString
argument_list|(
name|get
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
operator|.
name|asBytes
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Exports this external ID key as string with the format "scheme:id", or "id" id scheme is      * null.      *      *<p>This string representation is used as subsection name in the Git config file that stores      * the external ID.      */
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
if|if
condition|(
name|scheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|scheme
argument_list|()
operator|+
literal|":"
operator|+
name|id
argument_list|()
return|;
block|}
return|return
name|id
argument_list|()
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
name|get
argument_list|()
return|;
block|}
block|}
DECL|method|create (String scheme, String id, Account.Id accountId)
specifier|public
specifier|static
name|ExternalId
name|create
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|id
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ExternalId
argument_list|(
name|Key
operator|.
name|create
argument_list|(
name|scheme
argument_list|,
name|id
argument_list|)
argument_list|,
name|accountId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|create ( String scheme, String id, Account.Id accountId, @Nullable String email, @Nullable String hashedPassword)
specifier|public
specifier|static
name|ExternalId
name|create
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|id
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|,
annotation|@
name|Nullable
name|String
name|hashedPassword
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|Key
operator|.
name|create
argument_list|(
name|scheme
argument_list|,
name|id
argument_list|)
argument_list|,
name|accountId
argument_list|,
name|email
argument_list|,
name|hashedPassword
argument_list|)
return|;
block|}
DECL|method|create (Key key, Account.Id accountId)
specifier|public
specifier|static
name|ExternalId
name|create
parameter_list|(
name|Key
name|key
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|key
argument_list|,
name|accountId
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|create ( Key key, Account.Id accountId, @Nullable String email, @Nullable String hashedPassword)
specifier|public
specifier|static
name|ExternalId
name|create
parameter_list|(
name|Key
name|key
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|,
annotation|@
name|Nullable
name|String
name|hashedPassword
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ExternalId
argument_list|(
name|key
argument_list|,
name|accountId
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|email
argument_list|)
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|hashedPassword
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createWithPassword ( Key key, Account.Id accountId, @Nullable String email, String plainPassword)
specifier|public
specifier|static
name|ExternalId
name|createWithPassword
parameter_list|(
name|Key
name|key
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|,
name|String
name|plainPassword
parameter_list|)
block|{
name|plainPassword
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|plainPassword
argument_list|)
expr_stmt|;
name|String
name|hashedPassword
init|=
name|plainPassword
operator|!=
literal|null
condition|?
name|HashedPassword
operator|.
name|fromPassword
argument_list|(
name|plainPassword
argument_list|)
operator|.
name|encode
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|create
argument_list|(
name|key
argument_list|,
name|accountId
argument_list|,
name|email
argument_list|,
name|hashedPassword
argument_list|)
return|;
block|}
DECL|method|createUsername (String id, Account.Id accountId, String plainPassword)
specifier|public
specifier|static
name|ExternalId
name|createUsername
parameter_list|(
name|String
name|id
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|plainPassword
parameter_list|)
block|{
return|return
name|createWithPassword
argument_list|(
name|Key
operator|.
name|create
argument_list|(
name|SCHEME_USERNAME
argument_list|,
name|id
argument_list|)
argument_list|,
name|accountId
argument_list|,
literal|null
argument_list|,
name|plainPassword
argument_list|)
return|;
block|}
DECL|method|createWithEmail ( String scheme, String id, Account.Id accountId, @Nullable String email)
specifier|public
specifier|static
name|ExternalId
name|createWithEmail
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|id
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|)
block|{
return|return
name|createWithEmail
argument_list|(
name|Key
operator|.
name|create
argument_list|(
name|scheme
argument_list|,
name|id
argument_list|)
argument_list|,
name|accountId
argument_list|,
name|email
argument_list|)
return|;
block|}
DECL|method|createWithEmail (Key key, Account.Id accountId, @Nullable String email)
specifier|public
specifier|static
name|ExternalId
name|createWithEmail
parameter_list|(
name|Key
name|key
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Nullable
name|String
name|email
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ExternalId
argument_list|(
name|key
argument_list|,
name|accountId
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|email
argument_list|)
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|createEmail (Account.Id accountId, String email)
specifier|public
specifier|static
name|ExternalId
name|createEmail
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|email
parameter_list|)
block|{
return|return
name|createWithEmail
argument_list|(
name|SCHEME_MAILTO
argument_list|,
name|email
argument_list|,
name|accountId
argument_list|,
name|checkNotNull
argument_list|(
name|email
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Parses an external ID from a byte array that contain the external ID as an Git config file    * text.    *    *<p>The Git config must have exactly one externalId subsection with an accountId and optionally    * email and password:    *    *<pre>    * [externalId "username:jdoe"]    *   accountId = 1003407    *   email = jdoe@example.com    *   password = bcrypt:4:LCbmSBDivK/hhGVQMfkDpA==:XcWn0pKYSVU/UJgOvhidkEtmqCp6oKB7    *</pre>    */
DECL|method|parse (String noteId, byte[] raw)
specifier|public
specifier|static
name|ExternalId
name|parse
parameter_list|(
name|String
name|noteId
parameter_list|,
name|byte
index|[]
name|raw
parameter_list|)
throws|throws
name|ConfigInvalidException
block|{
name|Config
name|externalIdConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
try|try
block|{
name|externalIdConfig
operator|.
name|fromText
argument_list|(
operator|new
name|String
argument_list|(
name|raw
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
name|invalidConfig
argument_list|(
name|noteId
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|externalIdKeys
init|=
name|externalIdConfig
operator|.
name|getSubsections
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|externalIdKeys
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
name|invalidConfig
argument_list|(
name|noteId
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Expected exactly 1 %s section, found %d"
argument_list|,
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeys
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|externalIdKeyStr
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|externalIdKeys
argument_list|)
decl_stmt|;
name|Key
name|externalIdKey
init|=
name|Key
operator|.
name|parse
argument_list|(
name|externalIdKeyStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|externalIdKey
operator|==
literal|null
condition|)
block|{
throw|throw
name|invalidConfig
argument_list|(
name|noteId
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Invalid external id: %s"
argument_list|,
name|externalIdKeyStr
argument_list|)
argument_list|)
throw|;
block|}
name|String
name|accountIdStr
init|=
name|externalIdConfig
operator|.
name|getString
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeyStr
argument_list|,
name|ACCOUNT_ID_KEY
argument_list|)
decl_stmt|;
name|String
name|email
init|=
name|externalIdConfig
operator|.
name|getString
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeyStr
argument_list|,
name|EMAIL_KEY
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|externalIdConfig
operator|.
name|getString
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeyStr
argument_list|,
name|PASSWORD_KEY
argument_list|)
decl_stmt|;
if|if
condition|(
name|accountIdStr
operator|==
literal|null
condition|)
block|{
throw|throw
name|invalidConfig
argument_list|(
name|noteId
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Missing value for %s.%s.%s"
argument_list|,
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeyStr
argument_list|,
name|ACCOUNT_ID_KEY
argument_list|)
argument_list|)
throw|;
block|}
name|Integer
name|accountId
init|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|accountIdStr
argument_list|)
decl_stmt|;
if|if
condition|(
name|accountId
operator|==
literal|null
condition|)
block|{
throw|throw
name|invalidConfig
argument_list|(
name|noteId
argument_list|,
name|String
operator|.
name|format
argument_list|(
literal|"Value %s for %s.%s.%s is invalid, expected account ID"
argument_list|,
name|accountIdStr
argument_list|,
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKeyStr
argument_list|,
name|ACCOUNT_ID_KEY
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|new
name|AutoValue_ExternalId
argument_list|(
name|externalIdKey
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
name|accountId
argument_list|)
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|email
argument_list|)
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|password
argument_list|)
argument_list|)
return|;
block|}
DECL|method|invalidConfig (String noteId, String message)
specifier|private
specifier|static
name|ConfigInvalidException
name|invalidConfig
parameter_list|(
name|String
name|noteId
parameter_list|,
name|String
name|message
parameter_list|)
block|{
return|return
operator|new
name|ConfigInvalidException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Invalid external id config for note %s: %s"
argument_list|,
name|noteId
argument_list|,
name|message
argument_list|)
argument_list|)
return|;
block|}
DECL|method|from (AccountExternalId externalId)
specifier|public
specifier|static
name|ExternalId
name|from
parameter_list|(
name|AccountExternalId
name|externalId
parameter_list|)
block|{
if|if
condition|(
name|externalId
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
name|AutoValue_ExternalId
argument_list|(
name|ExternalId
operator|.
name|Key
operator|.
name|parse
argument_list|(
name|externalId
operator|.
name|getExternalId
argument_list|()
argument_list|)
argument_list|,
name|externalId
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|externalId
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|externalId
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|from (Collection<AccountExternalId> externalIds)
specifier|public
specifier|static
name|Set
argument_list|<
name|ExternalId
argument_list|>
name|from
parameter_list|(
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
if|if
condition|(
name|externalIds
operator|==
literal|null
condition|)
block|{
return|return
name|ImmutableSet
operator|.
name|of
argument_list|()
return|;
block|}
return|return
name|externalIds
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|ExternalId
operator|::
name|from
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|toAccountExternalIds (Collection<ExternalId> extIds)
specifier|public
specifier|static
name|Set
argument_list|<
name|AccountExternalId
argument_list|>
name|toAccountExternalIds
parameter_list|(
name|Collection
argument_list|<
name|ExternalId
argument_list|>
name|extIds
parameter_list|)
block|{
return|return
name|extIds
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|e
lambda|->
name|e
operator|.
name|asAccountExternalId
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|key ()
specifier|public
specifier|abstract
name|Key
name|key
parameter_list|()
function_decl|;
DECL|method|accountId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|accountId
parameter_list|()
function_decl|;
DECL|method|email ()
specifier|public
specifier|abstract
annotation|@
name|Nullable
name|String
name|email
parameter_list|()
function_decl|;
DECL|method|password ()
specifier|public
specifier|abstract
annotation|@
name|Nullable
name|String
name|password
parameter_list|()
function_decl|;
DECL|method|isScheme (String scheme)
specifier|public
name|boolean
name|isScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
return|return
name|key
argument_list|()
operator|.
name|isScheme
argument_list|(
name|scheme
argument_list|)
return|;
block|}
DECL|method|asAccountExternalId ()
specifier|public
name|AccountExternalId
name|asAccountExternalId
parameter_list|()
block|{
name|AccountExternalId
name|extId
init|=
operator|new
name|AccountExternalId
argument_list|(
name|accountId
argument_list|()
argument_list|,
name|key
argument_list|()
operator|.
name|asAccountExternalIdKey
argument_list|()
argument_list|)
decl_stmt|;
name|extId
operator|.
name|setEmailAddress
argument_list|(
name|email
argument_list|()
argument_list|)
expr_stmt|;
name|extId
operator|.
name|setPassword
argument_list|(
name|password
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|extId
return|;
block|}
comment|/**    * Exports this external ID as Git config file text.    *    *<p>The Git config has exactly one externalId subsection with an accountId and optionally email    * and password:    *    *<pre>    * [externalId "username:jdoe"]    *   accountId = 1003407    *   email = jdoe@example.com    *   password = bcrypt:4:LCbmSBDivK/hhGVQMfkDpA==:XcWn0pKYSVU/UJgOvhidkEtmqCp6oKB7    *</pre>    */
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|Config
name|c
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|writeToConfig
argument_list|(
name|c
argument_list|)
expr_stmt|;
return|return
name|c
operator|.
name|toText
argument_list|()
return|;
block|}
DECL|method|writeToConfig (Config c)
specifier|public
name|void
name|writeToConfig
parameter_list|(
name|Config
name|c
parameter_list|)
block|{
name|String
name|externalIdKey
init|=
name|key
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|c
operator|.
name|setInt
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKey
argument_list|,
name|ACCOUNT_ID_KEY
argument_list|,
name|accountId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|email
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setString
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKey
argument_list|,
name|EMAIL_KEY
argument_list|,
name|email
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|password
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setString
argument_list|(
name|EXTERNAL_ID_SECTION
argument_list|,
name|externalIdKey
argument_list|,
name|PASSWORD_KEY
argument_list|,
name|password
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

