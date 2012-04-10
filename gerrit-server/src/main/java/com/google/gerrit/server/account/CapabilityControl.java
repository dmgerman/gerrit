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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Function
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
name|base
operator|.
name|Predicates
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
name|GroupReference
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
name|reviewdb
operator|.
name|client
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
specifier|static
interface|interface
name|Factory
block|{
DECL|method|create (CurrentUser user)
specifier|public
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
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PermissionRule
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/** Identity of the user the control will compute for. */
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
comment|/** @return true if the user can administer this server. */
DECL|method|canAdministrateServer ()
specifier|public
name|boolean
name|canAdministrateServer
parameter_list|()
block|{
if|if
condition|(
name|canAdministrateServer
operator|==
literal|null
condition|)
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
return|return
name|canAdministrateServer
return|;
block|}
comment|/** @return true if the user can create an account for another user. */
DECL|method|canCreateAccount ()
specifier|public
name|boolean
name|canCreateAccount
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_ACCOUNT
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can create a group. */
DECL|method|canCreateGroup ()
specifier|public
name|boolean
name|canCreateGroup
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_GROUP
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can create a group. */
DECL|method|canCreateProject ()
specifier|public
name|boolean
name|canCreateProject
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
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
name|Predicates
operator|.
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
comment|/** @return true if the user can kill any running task. */
DECL|method|canKillTask ()
specifier|public
name|boolean
name|canKillTask
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|KILL_TASK
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can view the server caches. */
DECL|method|canViewCaches ()
specifier|public
name|boolean
name|canViewCaches
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can flush the server's caches. */
DECL|method|canFlushCaches ()
specifier|public
name|boolean
name|canFlushCaches
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can view open connections. */
DECL|method|canViewConnections ()
specifier|public
name|boolean
name|canViewConnections
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_CONNECTIONS
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can view the entire queue. */
DECL|method|canViewQueue ()
specifier|public
name|boolean
name|canViewQueue
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|VIEW_QUEUE
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
block|}
comment|/** @return true if the user can force replication to any configured destination. */
DECL|method|canStartReplication ()
specifier|public
name|boolean
name|canStartReplication
parameter_list|()
block|{
return|return
name|canPerform
argument_list|(
name|GlobalCapability
operator|.
name|START_REPLICATION
argument_list|)
operator|||
name|canAdministrateServer
argument_list|()
return|;
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
name|isGenericGroup
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
else|else
block|{
return|return
name|QueueProvider
operator|.
name|QueueType
operator|.
name|INTERACTIVE
return|;
block|}
block|}
DECL|method|isGenericGroup (GroupReference group)
specifier|private
specifier|static
name|boolean
name|isGenericGroup
parameter_list|(
name|GroupReference
name|group
parameter_list|)
block|{
return|return
name|AccountGroup
operator|.
name|ANONYMOUS_USERS
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
operator|||
name|AccountGroup
operator|.
name|REGISTERED_USERS
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getUUID
argument_list|()
argument_list|)
return|;
block|}
comment|/** True if the user has this permission. Works only for non labels. */
DECL|method|canPerform (String permissionName)
specifier|public
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
if|if
condition|(
name|rules
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|effective
operator|.
name|put
argument_list|(
name|permissionName
argument_list|,
name|rules
argument_list|)
expr_stmt|;
return|return
name|rules
return|;
block|}
name|GroupMembership
name|groups
init|=
name|user
operator|.
name|getEffectiveGroups
argument_list|()
decl_stmt|;
if|if
condition|(
name|rules
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
if|if
condition|(
operator|!
name|match
argument_list|(
name|groups
argument_list|,
name|rules
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
name|rules
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
name|rules
argument_list|)
expr_stmt|;
return|return
name|rules
return|;
block|}
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|mine
init|=
operator|new
name|ArrayList
argument_list|<
name|PermissionRule
argument_list|>
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
operator|new
name|Predicate
argument_list|<
name|PermissionRule
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
name|rule
operator|.
name|getAction
argument_list|()
operator|==
name|Action
operator|.
name|ALLOW
return|;
block|}
block|}
decl_stmt|;
DECL|method|matchAny (Iterable<PermissionRule> rules, Predicate<PermissionRule> predicate)
specifier|private
name|boolean
name|matchAny
parameter_list|(
name|Iterable
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
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ids
init|=
name|Iterables
operator|.
name|transform
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|rules
argument_list|,
name|predicate
argument_list|)
argument_list|,
operator|new
name|Function
argument_list|<
name|PermissionRule
argument_list|,
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|apply
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|ids
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
block|}
end_class

end_unit

