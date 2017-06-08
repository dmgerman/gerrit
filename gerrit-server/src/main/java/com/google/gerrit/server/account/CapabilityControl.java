begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
name|Predicates
operator|.
name|not
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
name|Predicate
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
name|FluentIterable
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
name|data
operator|.
name|GlobalCapability
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
name|data
operator|.
name|PermissionRange
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
name|data
operator|.
name|PermissionRule
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
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|api
operator|.
name|access
operator|.
name|GlobalOrPluginPermission
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
name|api
operator|.
name|access
operator|.
name|PluginPermission
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
name|CurrentUser
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
name|PeerDaemonUser
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
name|git
operator|.
name|QueueProvider
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
name|group
operator|.
name|SystemGroupBackend
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
name|permissions
operator|.
name|GlobalPermission
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
name|permissions
operator|.
name|PermissionBackendException
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
name|project
operator|.
name|ChangeControl
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
name|project
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
name|server
operator|.
name|project
operator|.
name|ProjectControl
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
name|project
operator|.
name|RefControl
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Access control management for server-wide capabilities. */
end_comment

begin_class
DECL|class|CapabilityControl
specifier|public
class|class
name|CapabilityControl
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser user)
name|CapabilityControl
name|create
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
function_decl|;
block|}
DECL|field|capabilities
specifier|private
specifier|final
name|CapabilityCollection
name|capabilities
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|effective
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
name|effective
decl_stmt|;
DECL|field|canAdministrateServer
specifier|private
name|Boolean
name|canAdministrateServer
decl_stmt|;
DECL|field|canEmailReviewers
specifier|private
name|Boolean
name|canEmailReviewers
decl_stmt|;
annotation|@
name|Inject
DECL|method|CapabilityControl (ProjectCache projectCache, @Assisted CurrentUser currentUser)
name|CapabilityControl
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|,
annotation|@
name|Assisted
name|CurrentUser
name|currentUser
parameter_list|)
block|{
name|capabilities
operator|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getCapabilityCollection
argument_list|()
expr_stmt|;
name|user
operator|=
name|currentUser
expr_stmt|;
name|effective
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/** Identity of the user the control will compute for. */
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
comment|/**    *<b>Do not use.</b> Determine if the user can administer this server.    *    *<p>This method is visible only for the benefit of the following transitional classes:    *    *<ul>    *<li>{@link ProjectControl}    *<li>{@link RefControl}    *<li>{@link ChangeControl}    *<li>{@link GroupControl}    *</ul>    *    * Other callers should not use this method, as it is slated to go away.    *    * @return true if the user can administer this server.    */
DECL|method|isAdmin_DoNotUse ()
specifier|public
name|boolean
name|isAdmin_DoNotUse
parameter_list|()
block|{
if|if
condition|(
name|canAdministrateServer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|user
operator|.
name|getRealUser
argument_list|()
operator|!=
name|user
condition|)
block|{
name|canAdministrateServer
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|canAdministrateServer
operator|=
name|user
operator|instanceof
name|PeerDaemonUser
operator|||
name|matchAny
argument_list|(
name|capabilities
operator|.
name|administrateServer
argument_list|,
name|ALLOWED_RULE
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|canAdministrateServer
return|;
block|}
comment|/** @return true if the user can email reviewers. */
DECL|method|canEmailReviewers ()
specifier|public
name|boolean
name|canEmailReviewers
parameter_list|()
block|{
if|if
condition|(
name|canEmailReviewers
operator|==
literal|null
condition|)
block|{
name|canEmailReviewers
operator|=
name|matchAny
argument_list|(
name|capabilities
operator|.
name|emailReviewers
argument_list|,
name|ALLOWED_RULE
argument_list|)
operator|||
operator|!
name|matchAny
argument_list|(
name|capabilities
operator|.
name|emailReviewers
argument_list|,
name|not
argument_list|(
name|ALLOWED_RULE
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|canEmailReviewers
return|;
block|}
comment|/** @return true if the user can view all accounts. */
DECL|method|canViewAllAccounts ()
specifier|public
name|boolean
name|canViewAllAccounts
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_ALL_ACCOUNTS
argument_list|)
operator|||
name|isAdmin_DoNotUse
argument_list|()
return|;
block|}
comment|/** @return true if the user can access the database (with gsql). */
DECL|method|canAccessDatabase ()
specifier|public
name|boolean
name|canAccessDatabase
parameter_list|()
block|{
try|try
block|{
return|return
name|doCanForDefaultPermissionBackend
argument_list|(
name|GlobalPermission
operator|.
name|ACCESS_DATABASE
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/** @return which priority queue the user's tasks should be submitted to. */
DECL|method|getQueueType ()
specifier|public
name|QueueProvider
operator|.
name|QueueType
name|getQueueType
parameter_list|()
block|{
comment|// If a non-generic group (that is not Anonymous Users or Registered Users)
comment|// grants us INTERACTIVE permission, use the INTERACTIVE queue even if
comment|// BATCH was otherwise granted. This allows site administrators to grant
comment|// INTERACTIVE to Registered Users, and BATCH to 'CI Servers' and have
comment|// the 'CI Servers' actually use the BATCH queue while everyone else gets
comment|// to use the INTERACTIVE queue without additional grants.
comment|//
name|GroupMembership
name|groups
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|boolean
name|batch
init|=
literal|false
decl_stmt|;
for|for
control|(
name|PermissionRule
name|r
range|:
name|capabilities
operator|.
name|priority
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|groups
argument_list|,
name|r
argument_list|)
condition|)
block|{
switch|switch
condition|(
name|r
operator|.
name|getAction
argument_list|()
condition|)
block|{
case|case
name|INTERACTIVE
case|:
if|if
condition|(
operator|!
name|SystemGroupBackend
operator|.
name|isAnonymousOrRegistered
argument_list|(
name|r
operator|.
name|getGroup
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|QueueProvider
operator|.
name|QueueType
operator|.
name|INTERACTIVE
return|;
block|}
break|break;
case|case
name|BATCH
case|:
name|batch
operator|=
literal|true
expr_stmt|;
break|break;
case|case
name|ALLOW
case|:
case|case
name|BLOCK
case|:
case|case
name|DENY
case|:
break|break;
block|}
block|}
block|}
if|if
condition|(
name|batch
condition|)
block|{
comment|// If any of our groups matched to the BATCH queue, use it.
return|return
name|QueueProvider
operator|.
name|QueueType
operator|.
name|BATCH
return|;
block|}
return|return
name|QueueProvider
operator|.
name|QueueType
operator|.
name|INTERACTIVE
return|;
block|}
comment|/** @return true if the user has this permission. */
DECL|method|canPerform (String permissionName)
specifier|private
name|boolean
name|canPerform
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
return|return
operator|!
name|access
argument_list|(
name|permissionName
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** @return true if the user has a permission rule specifying the range. */
DECL|method|hasExplicitRange (String permission)
specifier|public
name|boolean
name|hasExplicitRange
parameter_list|(
name|String
name|permission
parameter_list|)
block|{
return|return
name|GlobalCapability
operator|.
name|hasRange
argument_list|(
name|permission
argument_list|)
operator|&&
operator|!
name|access
argument_list|(
name|permission
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/** The range of permitted values associated with a label permission. */
DECL|method|getRange (String permission)
specifier|public
name|PermissionRange
name|getRange
parameter_list|(
name|String
name|permission
parameter_list|)
block|{
if|if
condition|(
name|GlobalCapability
operator|.
name|hasRange
argument_list|(
name|permission
argument_list|)
condition|)
block|{
return|return
name|toRange
argument_list|(
name|permission
argument_list|,
name|access
argument_list|(
name|permission
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|toRange (String permissionName, List<PermissionRule> ruleList)
specifier|private
specifier|static
name|PermissionRange
name|toRange
parameter_list|(
name|String
name|permissionName
parameter_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|ruleList
parameter_list|)
block|{
name|int
name|min
init|=
literal|0
decl_stmt|;
name|int
name|max
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|ruleList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PermissionRange
operator|.
name|WithDefaults
name|defaultRange
init|=
name|GlobalCapability
operator|.
name|getRange
argument_list|(
name|permissionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultRange
operator|!=
literal|null
condition|)
block|{
name|min
operator|=
name|defaultRange
operator|.
name|getDefaultMin
argument_list|()
expr_stmt|;
name|max
operator|=
name|defaultRange
operator|.
name|getDefaultMax
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|PermissionRule
name|rule
range|:
name|ruleList
control|)
block|{
name|min
operator|=
name|Math
operator|.
name|min
argument_list|(
name|min
argument_list|,
name|rule
operator|.
name|getMin
argument_list|()
argument_list|)
expr_stmt|;
name|max
operator|=
name|Math
operator|.
name|max
argument_list|(
name|max
argument_list|,
name|rule
operator|.
name|getMax
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PermissionRange
argument_list|(
name|permissionName
argument_list|,
name|min
argument_list|,
name|max
argument_list|)
return|;
block|}
comment|/** Rules for the given permission, or the empty list. */
DECL|method|access (String permissionName)
specifier|private
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|access
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|rules
init|=
name|effective
operator|.
name|get
argument_list|(
name|permissionName
argument_list|)
decl_stmt|;
if|if
condition|(
name|rules
operator|!=
literal|null
condition|)
block|{
return|return
name|rules
return|;
block|}
name|rules
operator|=
name|capabilities
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|)
expr_stmt|;
name|GroupMembership
name|groups
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|mine
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|rules
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PermissionRule
name|rule
range|:
name|rules
control|)
block|{
if|if
condition|(
name|match
argument_list|(
name|groups
argument_list|,
name|rule
argument_list|)
condition|)
block|{
name|mine
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mine
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|mine
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
name|effective
operator|.
name|put
argument_list|(
name|permissionName
argument_list|,
name|mine
argument_list|)
expr_stmt|;
return|return
name|mine
return|;
block|}
DECL|field|ALLOWED_RULE
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|PermissionRule
argument_list|>
name|ALLOWED_RULE
init|=
name|r
lambda|->
name|r
operator|.
name|getAction
argument_list|()
operator|==
name|Action
operator|.
name|ALLOW
decl_stmt|;
DECL|method|matchAny (Collection<PermissionRule> rules, Predicate<PermissionRule> predicate)
specifier|private
name|boolean
name|matchAny
parameter_list|(
name|Collection
argument_list|<
name|PermissionRule
argument_list|>
name|rules
parameter_list|,
name|Predicate
argument_list|<
name|PermissionRule
argument_list|>
name|predicate
parameter_list|)
block|{
return|return
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|FluentIterable
operator|.
name|from
argument_list|(
name|rules
argument_list|)
operator|.
name|filter
argument_list|(
name|predicate
argument_list|)
operator|.
name|transform
argument_list|(
name|r
lambda|->
name|r
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|match (GroupMembership groups, PermissionRule rule)
specifier|private
specifier|static
name|boolean
name|match
parameter_list|(
name|GroupMembership
name|groups
parameter_list|,
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
name|groups
operator|.
name|contains
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|)
return|;
block|}
comment|/** Do not use unless inside DefaultPermissionBackend. */
DECL|method|doCanForDefaultPermissionBackend (GlobalOrPluginPermission perm)
specifier|public
name|boolean
name|doCanForDefaultPermissionBackend
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
if|if
condition|(
name|perm
operator|instanceof
name|GlobalPermission
condition|)
block|{
return|return
name|can
argument_list|(
operator|(
name|GlobalPermission
operator|)
name|perm
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|perm
operator|instanceof
name|PluginPermission
condition|)
block|{
name|PluginPermission
name|pluginPermission
init|=
operator|(
name|PluginPermission
operator|)
name|perm
decl_stmt|;
return|return
name|canPerform
argument_list|(
name|pluginPermission
operator|.
name|permissionName
argument_list|()
argument_list|)
operator|||
operator|(
name|pluginPermission
operator|.
name|fallBackToAdmin
argument_list|()
operator|&&
name|isAdmin_DoNotUse
argument_list|()
operator|)
return|;
block|}
throw|throw
operator|new
name|PermissionBackendException
argument_list|(
name|perm
operator|+
literal|" unsupported"
argument_list|)
throw|;
block|}
DECL|method|can (GlobalPermission perm)
specifier|private
name|boolean
name|can
parameter_list|(
name|GlobalPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
switch|switch
condition|(
name|perm
condition|)
block|{
case|case
name|ADMINISTRATE_SERVER
case|:
return|return
name|isAdmin_DoNotUse
argument_list|()
return|;
case|case
name|EMAIL_REVIEWERS
case|:
return|return
name|canEmailReviewers
argument_list|()
return|;
case|case
name|VIEW_ALL_ACCOUNTS
case|:
return|return
name|canViewAllAccounts
argument_list|()
return|;
case|case
name|FLUSH_CACHES
case|:
case|case
name|KILL_TASK
case|:
case|case
name|RUN_GC
case|:
case|case
name|VIEW_CACHES
case|:
case|case
name|VIEW_QUEUE
case|:
return|return
name|canPerform
argument_list|(
name|perm
operator|.
name|permissionName
argument_list|()
argument_list|)
operator|||
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
argument_list|)
operator|||
name|isAdmin_DoNotUse
argument_list|()
return|;
case|case
name|CREATE_ACCOUNT
case|:
case|case
name|CREATE_GROUP
case|:
case|case
name|CREATE_PROJECT
case|:
case|case
name|MAINTAIN_SERVER
case|:
case|case
name|MODIFY_ACCOUNT
case|:
case|case
name|STREAM_EVENTS
case|:
case|case
name|VIEW_CONNECTIONS
case|:
case|case
name|VIEW_PLUGINS
case|:
return|return
name|canPerform
argument_list|(
name|perm
operator|.
name|permissionName
argument_list|()
argument_list|)
operator|||
name|isAdmin_DoNotUse
argument_list|()
return|;
case|case
name|ACCESS_DATABASE
case|:
case|case
name|RUN_AS
case|:
return|return
name|canPerform
argument_list|(
name|perm
operator|.
name|permissionName
argument_list|()
argument_list|)
return|;
block|}
throw|throw
operator|new
name|PermissionBackendException
argument_list|(
name|perm
operator|+
literal|" unsupported"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

