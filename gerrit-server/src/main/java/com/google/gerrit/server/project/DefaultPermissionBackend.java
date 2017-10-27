begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|account
operator|.
name|CapabilityCollection
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
name|FailedPermissionBackend
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
name|PermissionBackend
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|EnumSet
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
name|Set
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DefaultPermissionBackend
specifier|public
class|class
name|DefaultPermissionBackend
extends|extends
name|PermissionBackend
block|{
DECL|field|IS_ADMIN
specifier|private
specifier|static
specifier|final
name|CurrentUser
operator|.
name|PropertyKey
argument_list|<
name|Boolean
argument_list|>
name|IS_ADMIN
init|=
name|CurrentUser
operator|.
name|PropertyKey
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultPermissionBackend (ProjectCache projectCache)
name|DefaultPermissionBackend
parameter_list|(
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
DECL|method|capabilities ()
specifier|private
name|CapabilityCollection
name|capabilities
parameter_list|()
block|{
return|return
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getCapabilityCollection
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|user (CurrentUser user)
specifier|public
name|WithUser
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|WithUserImpl
argument_list|(
name|checkNotNull
argument_list|(
name|user
argument_list|,
literal|"user"
argument_list|)
argument_list|)
return|;
block|}
DECL|class|WithUserImpl
class|class
name|WithUserImpl
extends|extends
name|WithUser
block|{
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
DECL|field|admin
specifier|private
name|Boolean
name|admin
decl_stmt|;
DECL|method|WithUserImpl (CurrentUser user)
name|WithUserImpl
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|checkNotNull
argument_list|(
name|user
argument_list|,
literal|"user"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|project (Project.NameKey project)
specifier|public
name|ForProject
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
try|try
block|{
name|ProjectState
name|state
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|!=
literal|null
condition|)
block|{
return|return
name|state
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
operator|.
name|asForProject
argument_list|()
operator|.
name|database
argument_list|(
name|db
argument_list|)
return|;
block|}
return|return
name|FailedPermissionBackend
operator|.
name|project
argument_list|(
literal|"not found"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
name|FailedPermissionBackend
operator|.
name|project
argument_list|(
literal|"unavailable"
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|check (GlobalOrPluginPermission perm)
specifier|public
name|void
name|check
parameter_list|(
name|GlobalOrPluginPermission
name|perm
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
operator|!
name|can
argument_list|(
name|perm
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|perm
operator|.
name|describeForException
argument_list|()
operator|+
literal|" not permitted"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|test (Collection<T> permSet)
specifier|public
parameter_list|<
name|T
extends|extends
name|GlobalOrPluginPermission
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|test
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|permSet
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|ok
init|=
name|newSet
argument_list|(
name|permSet
argument_list|)
decl_stmt|;
for|for
control|(
name|T
name|perm
range|:
name|permSet
control|)
block|{
if|if
condition|(
name|can
argument_list|(
name|perm
argument_list|)
condition|)
block|{
name|ok
operator|.
name|add
argument_list|(
name|perm
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ok
return|;
block|}
DECL|method|can (GlobalOrPluginPermission perm)
specifier|private
name|boolean
name|can
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
name|has
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
name|isAdmin
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
name|isAdmin
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
name|has
argument_list|(
name|perm
operator|.
name|permissionName
argument_list|()
argument_list|)
operator|||
name|can
argument_list|(
name|GlobalPermission
operator|.
name|MAINTAIN_SERVER
argument_list|)
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
name|VIEW_ALL_ACCOUNTS
case|:
case|case
name|VIEW_CONNECTIONS
case|:
case|case
name|VIEW_PLUGINS
case|:
return|return
name|has
argument_list|(
name|perm
operator|.
name|permissionName
argument_list|()
argument_list|)
operator|||
name|isAdmin
argument_list|()
return|;
case|case
name|ACCESS_DATABASE
case|:
case|case
name|RUN_AS
case|:
return|return
name|has
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
DECL|method|isAdmin ()
specifier|private
name|boolean
name|isAdmin
parameter_list|()
block|{
if|if
condition|(
name|admin
operator|==
literal|null
condition|)
block|{
name|admin
operator|=
name|computeAdmin
argument_list|()
expr_stmt|;
block|}
return|return
name|admin
return|;
block|}
DECL|method|computeAdmin ()
specifier|private
name|Boolean
name|computeAdmin
parameter_list|()
block|{
name|Boolean
name|r
init|=
name|user
operator|.
name|get
argument_list|(
name|IS_ADMIN
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|user
operator|.
name|isImpersonating
argument_list|()
condition|)
block|{
name|r
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|user
operator|instanceof
name|PeerDaemonUser
condition|)
block|{
name|r
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
name|allow
argument_list|(
name|capabilities
argument_list|()
operator|.
name|administrateServer
argument_list|)
expr_stmt|;
block|}
name|user
operator|.
name|put
argument_list|(
name|IS_ADMIN
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|canEmailReviewers ()
specifier|private
name|boolean
name|canEmailReviewers
parameter_list|()
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|email
init|=
name|capabilities
argument_list|()
operator|.
name|emailReviewers
decl_stmt|;
return|return
name|allow
argument_list|(
name|email
argument_list|)
operator|||
name|notDenied
argument_list|(
name|email
argument_list|)
return|;
block|}
DECL|method|has (String permissionName)
specifier|private
name|boolean
name|has
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
return|return
name|allow
argument_list|(
name|capabilities
argument_list|()
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|allow (Collection<PermissionRule> rules)
specifier|private
name|boolean
name|allow
parameter_list|(
name|Collection
argument_list|<
name|PermissionRule
argument_list|>
name|rules
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
name|rules
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
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
argument_list|)
operator|.
name|map
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
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|notDenied (Collection<PermissionRule> rules)
specifier|private
name|boolean
name|notDenied
parameter_list|(
name|Collection
argument_list|<
name|PermissionRule
argument_list|>
name|rules
parameter_list|)
block|{
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|denied
init|=
name|rules
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|r
lambda|->
name|r
operator|.
name|getAction
argument_list|()
operator|!=
name|Action
operator|.
name|ALLOW
argument_list|)
operator|.
name|map
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
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|denied
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|denied
argument_list|)
return|;
block|}
block|}
DECL|method|newSet (Collection<T> permSet)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|GlobalOrPluginPermission
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|newSet
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|permSet
parameter_list|)
block|{
if|if
condition|(
name|permSet
operator|instanceof
name|EnumSet
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
name|Set
argument_list|<
name|T
argument_list|>
name|s
init|=
operator|(
operator|(
name|EnumSet
operator|)
name|permSet
operator|)
operator|.
name|clone
argument_list|()
decl_stmt|;
name|s
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|s
return|;
block|}
return|return
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|permSet
operator|.
name|size
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

