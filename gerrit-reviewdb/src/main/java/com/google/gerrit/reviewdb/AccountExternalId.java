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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
package|;
end_package

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
name|Column
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
name|StringKey
import|;
end_import

begin_comment
comment|/** Association of an external account identifier to a local {@link Account}. */
end_comment

begin_class
DECL|class|AccountExternalId
specifier|public
specifier|final
class|class
name|AccountExternalId
block|{
comment|/**    * Scheme used for {@link AuthType#LDAP}, {@link AuthType#HTTP},    * {@link AuthType#HTTP_LDAP}, and {@link AuthType#LDAP_BIND} usernames.    *<p>    * The name {@code gerrit:} was a very poor choice.    */
DECL|field|SCHEME_GERRIT
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_GERRIT
init|=
literal|"gerrit:"
decl_stmt|;
comment|/** Scheme used for randomly created identities constructed by a UUID. */
DECL|field|SCHEME_UUID
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_UUID
init|=
literal|"uuid:"
decl_stmt|;
comment|/** Scheme used to represent only an email address. */
DECL|field|SCHEME_MAILTO
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_MAILTO
init|=
literal|"mailto:"
decl_stmt|;
comment|/** Scheme for the username used to authenticate an account, e.g. over SSH. */
DECL|field|SCHEME_USERNAME
specifier|public
specifier|static
specifier|final
name|String
name|SCHEME_USERNAME
init|=
literal|"username:"
decl_stmt|;
comment|/** Very old scheme from Gerrit Code Review 1.x imports. */
DECL|field|LEGACY_GAE
specifier|public
specifier|static
specifier|final
name|String
name|LEGACY_GAE
init|=
literal|"Google Account "
decl_stmt|;
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
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
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|externalId
specifier|protected
name|String
name|externalId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{     }
DECL|method|Key (String scheme, final String identity)
specifier|public
name|Key
parameter_list|(
name|String
name|scheme
parameter_list|,
specifier|final
name|String
name|identity
parameter_list|)
block|{
if|if
condition|(
operator|!
name|scheme
operator|.
name|endsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|scheme
operator|+=
literal|":"
expr_stmt|;
block|}
name|externalId
operator|=
name|scheme
operator|+
name|identity
expr_stmt|;
block|}
DECL|method|Key (final String e)
specifier|public
name|Key
parameter_list|(
specifier|final
name|String
name|e
parameter_list|)
block|{
name|externalId
operator|=
name|e
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|String
name|get
parameter_list|()
block|{
return|return
name|externalId
return|;
block|}
annotation|@
name|Override
DECL|method|set (String newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|String
name|newValue
parameter_list|)
block|{
name|externalId
operator|=
name|newValue
expr_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|emailAddress
specifier|protected
name|String
name|emailAddress
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|password
specifier|protected
name|String
name|password
decl_stmt|;
comment|/**<i>computed value</i> is this identity trusted by the site administrator? */
DECL|field|trusted
specifier|protected
name|boolean
name|trusted
decl_stmt|;
comment|/**<i>computed value</i> can this identity be removed from the account? */
DECL|field|canDelete
specifier|protected
name|boolean
name|canDelete
decl_stmt|;
DECL|method|AccountExternalId ()
specifier|protected
name|AccountExternalId
parameter_list|()
block|{   }
comment|/**    * Create a new binding to an external identity.    *    * @param who the account this binds to.    * @param k the binding key.    */
DECL|method|AccountExternalId (final Account.Id who, final AccountExternalId.Key k)
specifier|public
name|AccountExternalId
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|who
parameter_list|,
specifier|final
name|AccountExternalId
operator|.
name|Key
name|k
parameter_list|)
block|{
name|accountId
operator|=
name|who
expr_stmt|;
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|AccountExternalId
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/** Get local id of this account, to link with in other entities */
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
DECL|method|getExternalId ()
specifier|public
name|String
name|getExternalId
parameter_list|()
block|{
return|return
name|key
operator|.
name|externalId
return|;
block|}
DECL|method|getEmailAddress ()
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
name|emailAddress
return|;
block|}
DECL|method|setEmailAddress (final String e)
specifier|public
name|void
name|setEmailAddress
parameter_list|(
specifier|final
name|String
name|e
parameter_list|)
block|{
name|emailAddress
operator|=
name|e
expr_stmt|;
block|}
DECL|method|isScheme (final String scheme)
specifier|public
name|boolean
name|isScheme
parameter_list|(
specifier|final
name|String
name|scheme
parameter_list|)
block|{
specifier|final
name|String
name|id
init|=
name|getExternalId
argument_list|()
decl_stmt|;
return|return
name|id
operator|!=
literal|null
operator|&&
name|id
operator|.
name|startsWith
argument_list|(
name|scheme
argument_list|)
return|;
block|}
DECL|method|getSchemeRest ()
specifier|public
name|String
name|getSchemeRest
parameter_list|()
block|{
name|String
name|id
init|=
name|getExternalId
argument_list|()
decl_stmt|;
name|int
name|c
init|=
name|id
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
return|return
literal|0
operator|<
name|c
condition|?
name|id
operator|.
name|substring
argument_list|(
name|c
operator|+
literal|1
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String p)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|password
operator|=
name|p
expr_stmt|;
block|}
DECL|method|isTrusted ()
specifier|public
name|boolean
name|isTrusted
parameter_list|()
block|{
return|return
name|trusted
return|;
block|}
DECL|method|setTrusted (final boolean t)
specifier|public
name|void
name|setTrusted
parameter_list|(
specifier|final
name|boolean
name|t
parameter_list|)
block|{
name|trusted
operator|=
name|t
expr_stmt|;
block|}
DECL|method|canDelete ()
specifier|public
name|boolean
name|canDelete
parameter_list|()
block|{
return|return
name|canDelete
return|;
block|}
DECL|method|setCanDelete (final boolean t)
specifier|public
name|void
name|setCanDelete
parameter_list|(
specifier|final
name|boolean
name|t
parameter_list|)
block|{
name|canDelete
operator|=
name|t
expr_stmt|;
block|}
block|}
end_class

end_unit

