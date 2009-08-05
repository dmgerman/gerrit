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
DECL|package|com.google.gerrit.client.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|StringKey
argument_list|<
name|Account
operator|.
name|Id
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
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
DECL|field|externalId
specifier|protected
name|String
name|externalId
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Account.Id a, final String e)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|String
name|e
parameter_list|)
block|{
name|accountId
operator|=
name|a
expr_stmt|;
name|externalId
operator|=
name|e
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Account
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|accountId
return|;
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
comment|/**    * Select the most recently used identity from a list of identities.    *    * @param all all known identities    * @return most recently used login identity; null if none matches.    */
DECL|method|mostRecent (Collection<AccountExternalId> all)
specifier|public
specifier|static
name|AccountExternalId
name|mostRecent
parameter_list|(
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|all
parameter_list|)
block|{
name|AccountExternalId
name|mostRecent
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|all
control|)
block|{
specifier|final
name|Timestamp
name|lastUsed
init|=
name|e
operator|.
name|getLastUsedOn
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastUsed
operator|==
literal|null
condition|)
block|{
comment|// Identities without logins have never been used, so
comment|// they can't be the most recent.
comment|//
continue|continue;
block|}
if|if
condition|(
name|e
operator|.
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mailto:"
argument_list|)
condition|)
block|{
comment|// Don't ever consider an email address as a "recent login"
comment|//
continue|continue;
block|}
if|if
condition|(
name|mostRecent
operator|==
literal|null
operator|||
name|lastUsed
operator|.
name|getTime
argument_list|()
operator|>
name|mostRecent
operator|.
name|getLastUsedOn
argument_list|()
operator|.
name|getTime
argument_list|()
condition|)
block|{
name|mostRecent
operator|=
name|e
expr_stmt|;
block|}
block|}
return|return
name|mostRecent
return|;
block|}
annotation|@
name|Column
argument_list|(
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
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|lastUsedOn
specifier|protected
name|Timestamp
name|lastUsedOn
decl_stmt|;
comment|/**<i>computed value</i> is this identity trusted by the site administrator? */
DECL|field|trusted
specifier|protected
name|boolean
name|trusted
decl_stmt|;
DECL|method|AccountExternalId ()
specifier|protected
name|AccountExternalId
parameter_list|()
block|{   }
comment|/**    * Create a new binding to an external identity.    *    * @param k the binding key.    */
DECL|method|AccountExternalId (final AccountExternalId.Key k)
specifier|public
name|AccountExternalId
parameter_list|(
specifier|final
name|AccountExternalId
operator|.
name|Key
name|k
parameter_list|)
block|{
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
name|key
operator|.
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
DECL|method|getLastUsedOn ()
specifier|public
name|Timestamp
name|getLastUsedOn
parameter_list|()
block|{
return|return
name|lastUsedOn
return|;
block|}
DECL|method|setLastUsedOn ()
specifier|public
name|void
name|setLastUsedOn
parameter_list|()
block|{
name|lastUsedOn
operator|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|canUserDelete ()
specifier|public
name|boolean
name|canUserDelete
parameter_list|()
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
name|OPENID
case|:
if|if
condition|(
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Google Account "
argument_list|)
condition|)
block|{
comment|// Don't allow users to delete legacy google account tokens.
comment|// Administrators will do it when cleaning the database.
comment|//
return|return
literal|false
return|;
block|}
break|break;
case|case
name|HTTP
case|:
if|if
condition|(
name|getExternalId
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"gerrit:"
argument_list|)
condition|)
block|{
comment|// Don't allow users to delete a gerrit: token, as this is
comment|// a Gerrit generated value for single-sign-on configurations
comment|// not using OpenID.
comment|//
return|return
literal|false
return|;
block|}
break|break;
block|}
return|return
literal|true
return|;
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
block|}
end_class

end_unit

