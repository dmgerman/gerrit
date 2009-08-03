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
name|client
operator|.
name|data
operator|.
name|ProjectCache
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
name|ApprovalCategory
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
name|ProjectRight
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
name|SystemConfig
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
DECL|field|systemConfig
specifier|protected
specifier|final
name|SystemConfig
name|systemConfig
decl_stmt|;
DECL|method|CurrentUser (final SystemConfig cfg)
specifier|protected
name|CurrentUser
parameter_list|(
specifier|final
name|SystemConfig
name|cfg
parameter_list|)
block|{
name|systemConfig
operator|=
name|cfg
expr_stmt|;
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
name|systemConfig
operator|.
name|adminGroupId
argument_list|)
return|;
block|}
annotation|@
name|Deprecated
DECL|method|canPerform (final ProjectCache.Entry e, final ApprovalCategory.Id actionId, final short requireValue)
specifier|public
name|boolean
name|canPerform
parameter_list|(
specifier|final
name|ProjectCache
operator|.
name|Entry
name|e
parameter_list|,
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|actionId
parameter_list|,
specifier|final
name|short
name|requireValue
parameter_list|)
block|{
if|if
condition|(
name|e
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|int
name|val
init|=
name|Integer
operator|.
name|MIN_VALUE
decl_stmt|;
for|for
control|(
specifier|final
name|ProjectRight
name|pr
range|:
name|e
operator|.
name|getRights
argument_list|()
control|)
block|{
if|if
condition|(
name|actionId
operator|.
name|equals
argument_list|(
name|pr
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
operator|&&
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|pr
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|val
argument_list|<
literal|0
operator|&&
name|pr
operator|.
name|getMaxValue
operator|(
operator|)
argument_list|>
literal|0
condition|)
block|{
comment|// If one of the user's groups had denied them access, but
comment|// this group grants them access, prefer the grant over
comment|// the denial. We have to break the tie somehow and we
comment|// prefer being "more open" to being "more closed".
comment|//
name|val
operator|=
name|pr
operator|.
name|getMaxValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// Otherwise we use the largest value we can get.
comment|//
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|pr
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|val
operator|==
name|Integer
operator|.
name|MIN_VALUE
operator|&&
name|actionId
operator|.
name|canInheritFromWildProject
argument_list|()
condition|)
block|{
for|for
control|(
specifier|final
name|ProjectRight
name|pr
range|:
name|Common
operator|.
name|getProjectCache
argument_list|()
operator|.
name|getWildcardRights
argument_list|()
control|)
block|{
if|if
condition|(
name|actionId
operator|.
name|equals
argument_list|(
name|pr
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
operator|&&
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|pr
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|val
operator|=
name|Math
operator|.
name|max
argument_list|(
name|pr
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|val
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|val
operator|>=
name|requireValue
return|;
block|}
block|}
end_class

end_unit

