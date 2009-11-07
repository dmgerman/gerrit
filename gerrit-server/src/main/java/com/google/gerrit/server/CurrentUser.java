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
name|AccountGroup
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
DECL|field|accessPath
specifier|private
specifier|final
name|AccessPath
name|accessPath
decl_stmt|;
DECL|field|authConfig
specifier|protected
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
DECL|method|CurrentUser (final AccessPath accessPath, final AuthConfig authConfig)
specifier|protected
name|CurrentUser
parameter_list|(
specifier|final
name|AccessPath
name|accessPath
parameter_list|,
specifier|final
name|AuthConfig
name|authConfig
parameter_list|)
block|{
name|this
operator|.
name|accessPath
operator|=
name|accessPath
expr_stmt|;
name|this
operator|.
name|authConfig
operator|=
name|authConfig
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
comment|/**    * Get the set of groups the user is currently a member of.    *<p>    * The returned set may be a subset of the user's actual groups; if the user's    * account is currently deemed to be untrusted then the effective group set is    * only the anonymous and registered user groups. To enable additional groups    * (and gain their granted permissions) the user must update their account to    * use only trusted authentication providers.    *    * @return active groups for this user.    */
DECL|method|getEffectiveGroups ()
specifier|public
specifier|abstract
name|Set
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|>
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
annotation|@
name|Deprecated
DECL|method|isAdministrator ()
specifier|public
specifier|final
name|boolean
name|isAdministrator
parameter_list|()
block|{
return|return
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|authConfig
operator|.
name|getAdministratorsGroup
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

