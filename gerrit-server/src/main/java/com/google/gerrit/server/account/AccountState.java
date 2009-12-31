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
name|gerrit
operator|.
name|reviewdb
operator|.
name|AccountExternalId
operator|.
name|SCHEME_USERNAME
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
name|reviewdb
operator|.
name|AccountGroup
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
name|HashSet
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

begin_class
DECL|class|AccountState
specifier|public
class|class
name|AccountState
block|{
DECL|field|account
specifier|private
specifier|final
name|Account
name|account
decl_stmt|;
DECL|field|internalGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|internalGroups
decl_stmt|;
DECL|field|externalIds
specifier|private
specifier|final
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|externalIds
decl_stmt|;
DECL|method|AccountState (final Account account, final Set<AccountGroup.Id> actualGroups, final Collection<AccountExternalId> externalIds)
specifier|public
name|AccountState
parameter_list|(
specifier|final
name|Account
name|account
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|actualGroups
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|externalIds
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
name|this
operator|.
name|internalGroups
operator|=
name|actualGroups
expr_stmt|;
name|this
operator|.
name|externalIds
operator|=
name|externalIds
expr_stmt|;
name|this
operator|.
name|account
operator|.
name|setUserName
argument_list|(
name|getUserName
argument_list|(
name|externalIds
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Get the cached account metadata. */
DECL|method|getAccount ()
specifier|public
name|Account
name|getAccount
parameter_list|()
block|{
return|return
name|account
return|;
block|}
comment|/**    * Get the username, if one has been declared for this user.    *<p>    * The username is the {@link AccountExternalId} using the scheme    * {@link AccountExternalId#SCHEME_USERNAME}.    */
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|account
operator|.
name|getUserName
argument_list|()
return|;
block|}
comment|/**    * All email addresses registered to this account.    *<p>    * Gerrit is "reasonably certain" that the returned email addresses actually    * belong to the user of the account. Some emails may have been obtained from    * the authentication provider, which in the case of OpenID may be trusting    * the provider to have validated the address. Other emails may have been    * validated by Gerrit directly.    */
DECL|method|getEmailAddresses ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getEmailAddresses
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|emails
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountExternalId
name|e
range|:
name|externalIds
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getEmailAddress
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|e
operator|.
name|getEmailAddress
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|emails
operator|.
name|add
argument_list|(
name|e
operator|.
name|getEmailAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|emails
return|;
block|}
comment|/** The external identities that identify the account holder. */
DECL|method|getExternalIds ()
specifier|public
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|getExternalIds
parameter_list|()
block|{
return|return
name|externalIds
return|;
block|}
comment|/** The set of groups maintained directly within the Gerrit database. */
DECL|method|getInternalGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getInternalGroups
parameter_list|()
block|{
return|return
name|internalGroups
return|;
block|}
DECL|method|getUserName (Collection<AccountExternalId> ids)
specifier|private
specifier|static
name|String
name|getUserName
parameter_list|(
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|ids
parameter_list|)
block|{
for|for
control|(
name|AccountExternalId
name|id
range|:
name|ids
control|)
block|{
if|if
condition|(
name|id
operator|.
name|isScheme
argument_list|(
name|SCHEME_USERNAME
argument_list|)
condition|)
block|{
return|return
name|id
operator|.
name|getSchemeRest
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

