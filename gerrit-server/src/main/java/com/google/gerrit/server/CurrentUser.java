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
name|reviewdb
operator|.
name|client
operator|.
name|AccountProjectWatch
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
name|Change
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
name|CapabilityControl
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
name|GroupMembership
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

begin_comment
comment|/**  * Information about the currently logged in user.  *<p>  * This is a {@link RequestScoped} property managed by Guice.  *  * @see AnonymousUser  * @see IdentifiedUser  */
end_comment

begin_class
DECL|class|CurrentUser
specifier|public
specifier|abstract
class|class
name|CurrentUser
block|{
DECL|field|capabilityControlFactory
specifier|private
specifier|final
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
decl_stmt|;
DECL|field|accessPath
specifier|private
name|AccessPath
name|accessPath
init|=
name|AccessPath
operator|.
name|UNKNOWN
decl_stmt|;
DECL|field|capabilities
specifier|private
name|CapabilityControl
name|capabilities
decl_stmt|;
DECL|method|CurrentUser (CapabilityControl.Factory capabilityControlFactory)
specifier|protected
name|CurrentUser
parameter_list|(
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
parameter_list|)
block|{
name|this
operator|.
name|capabilityControlFactory
operator|=
name|capabilityControlFactory
expr_stmt|;
block|}
comment|/** How this user is accessing the Gerrit Code Review application. */
DECL|method|getAccessPath ()
specifier|public
specifier|final
name|AccessPath
name|getAccessPath
parameter_list|()
block|{
return|return
name|accessPath
return|;
block|}
DECL|method|setAccessPath (AccessPath path)
specifier|public
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
comment|/**    * Identity of the authenticated user.    *<p>    * In the normal case where a user authenticates as themselves    * {@code getRealUser() == this}.    *<p>    * If {@code X-Gerrit-RunAs} or {@code suexec} was used this method returns    * the identity of the account that has permission to act on behalf of this    * user.    */
DECL|method|getRealUser ()
specifier|public
name|CurrentUser
name|getRealUser
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**    * Get the set of groups the user is currently a member of.    *<p>    * The returned set may be a subset of the user's actual groups; if the user's    * account is currently deemed to be untrusted then the effective group set is    * only the anonymous and registered user groups. To enable additional groups    * (and gain their granted permissions) the user must update their account to    * use only trusted authentication providers.    *    * @return active groups for this user.    */
DECL|method|getEffectiveGroups ()
specifier|public
specifier|abstract
name|GroupMembership
name|getEffectiveGroups
parameter_list|()
function_decl|;
comment|/** Set of changes starred by this user. */
DECL|method|getStarredChanges ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|getStarredChanges
parameter_list|()
function_decl|;
comment|/** Filters selecting changes the user wants to monitor. */
DECL|method|getNotificationFilters ()
specifier|public
specifier|abstract
name|Collection
argument_list|<
name|AccountProjectWatch
argument_list|>
name|getNotificationFilters
parameter_list|()
function_decl|;
comment|/** Unique name of the user on this server, if one has been assigned. */
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/** Capabilities available to this user account. */
DECL|method|getCapabilities ()
specifier|public
name|CapabilityControl
name|getCapabilities
parameter_list|()
block|{
if|if
condition|(
name|capabilities
operator|==
literal|null
condition|)
block|{
name|capabilities
operator|=
name|capabilityControlFactory
operator|.
name|create
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|capabilities
return|;
block|}
comment|/** Check if user is the IdentifiedUser */
DECL|method|isIdentifiedUser ()
specifier|public
name|boolean
name|isIdentifiedUser
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/** Check if the CurrentUser is an InternalUser. */
DECL|method|isInternalUser ()
specifier|public
name|boolean
name|isInternalUser
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

