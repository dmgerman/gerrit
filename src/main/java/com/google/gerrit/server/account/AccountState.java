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
name|AccountGroup
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
DECL|field|actualGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|actualGroups
decl_stmt|;
DECL|field|effectiveGroups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|effectiveGroups
decl_stmt|;
DECL|field|emails
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|emails
decl_stmt|;
DECL|method|AccountState (final Account a, final Set<AccountGroup.Id> actual, final Set<AccountGroup.Id> effective, final Set<String> e)
name|AccountState
parameter_list|(
specifier|final
name|Account
name|a
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|actual
parameter_list|,
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|effective
parameter_list|,
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|e
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|a
expr_stmt|;
name|this
operator|.
name|actualGroups
operator|=
name|actual
expr_stmt|;
name|this
operator|.
name|effectiveGroups
operator|=
name|effective
expr_stmt|;
name|this
operator|.
name|emails
operator|=
name|e
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
return|return
name|emails
return|;
block|}
comment|/**    * Get the set of groups the user has been declared a member of.    *<p>    * The returned set is the complete set of the user's groups. This can be a    * superset of {@link #getEffectiveGroups()} if the user's account is not    * sufficiently trusted to enable additional access.    *    * @return active groups for this user.    */
DECL|method|getActualGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getActualGroups
parameter_list|()
block|{
return|return
name|actualGroups
return|;
block|}
comment|/**    * Get the set of groups the user is currently a member of.    *<p>    * The returned set may be a subset of {@link #getActualGroups()}. If the    * user's account is currently deemed to be untrusted then the effective group    * set is only the anonymous and registered user groups. To enable additional    * groups (and gain their granted permissions) the user must update their    * account to use only trusted authentication providers.    *    * @return active groups for this user.    */
DECL|method|getEffectiveGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
name|getEffectiveGroups
parameter_list|()
block|{
return|return
name|effectiveGroups
return|;
block|}
block|}
end_class

end_unit

