begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
operator|.
name|config
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
name|collect
operator|.
name|ComparisonChain
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|config
operator|.
name|ConfigResource
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
name|WorkQueue
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
name|WorkQueue
operator|.
name|ProjectTask
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
name|WorkQueue
operator|.
name|Task
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
name|gerrit
operator|.
name|server
operator|.
name|permissions
operator|.
name|ProjectPermission
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
name|ProjectState
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
name|util
operator|.
name|IdGenerator
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
name|Provider
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
name|Comparator
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ListTasks
specifier|public
class|class
name|ListTasks
implements|implements
name|RestReadView
argument_list|<
name|ConfigResource
argument_list|>
block|{
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListTasks ( PermissionBackend permissionBackend, WorkQueue workQueue, Provider<CurrentUser> self, ProjectCache projectCache)
specifier|public
name|ListTasks
parameter_list|(
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|WorkQueue
name|workQueue
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|workQueue
operator|=
name|workQueue
expr_stmt|;
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ConfigResource resource)
specifier|public
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|apply
parameter_list|(
name|ConfigResource
name|resource
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
block|{
name|CurrentUser
name|user
init|=
name|self
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Authentication required"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|allTasks
init|=
name|getTasks
argument_list|()
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_QUEUE
argument_list|)
expr_stmt|;
return|return
name|allTasks
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
comment|// Fall through to filter tasks.
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|visibilityCache
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|visibleTasks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|TaskInfo
name|task
range|:
name|allTasks
control|)
block|{
if|if
condition|(
name|task
operator|.
name|projectName
operator|!=
literal|null
condition|)
block|{
name|Boolean
name|visible
init|=
name|visibilityCache
operator|.
name|get
argument_list|(
name|task
operator|.
name|projectName
argument_list|)
decl_stmt|;
if|if
condition|(
name|visible
operator|==
literal|null
condition|)
block|{
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|task
operator|.
name|projectName
argument_list|)
decl_stmt|;
name|ProjectState
name|state
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
operator|||
operator|!
name|state
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
name|visible
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|nameKey
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|ACCESS
argument_list|)
expr_stmt|;
name|visible
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|visible
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|visibilityCache
operator|.
name|put
argument_list|(
name|task
operator|.
name|projectName
argument_list|,
name|visible
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|visible
condition|)
block|{
name|visibleTasks
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|visibleTasks
return|;
block|}
DECL|method|getTasks ()
specifier|private
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|getTasks
parameter_list|()
block|{
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|taskInfos
init|=
name|workQueue
operator|.
name|getTaskInfos
argument_list|(
name|TaskInfo
operator|::
operator|new
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|taskInfos
argument_list|,
operator|new
name|Comparator
argument_list|<
name|TaskInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|TaskInfo
name|a
parameter_list|,
name|TaskInfo
name|b
parameter_list|)
block|{
return|return
name|ComparisonChain
operator|.
name|start
argument_list|()
operator|.
name|compare
argument_list|(
name|a
operator|.
name|state
operator|.
name|ordinal
argument_list|()
argument_list|,
name|b
operator|.
name|state
operator|.
name|ordinal
argument_list|()
argument_list|)
operator|.
name|compare
argument_list|(
name|a
operator|.
name|delay
argument_list|,
name|b
operator|.
name|delay
argument_list|)
operator|.
name|compare
argument_list|(
name|a
operator|.
name|command
argument_list|,
name|b
operator|.
name|command
argument_list|)
operator|.
name|result
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|taskInfos
return|;
block|}
DECL|class|TaskInfo
specifier|public
specifier|static
class|class
name|TaskInfo
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|state
specifier|public
name|Task
operator|.
name|State
name|state
decl_stmt|;
DECL|field|startTime
specifier|public
name|Timestamp
name|startTime
decl_stmt|;
DECL|field|delay
specifier|public
name|long
name|delay
decl_stmt|;
DECL|field|command
specifier|public
name|String
name|command
decl_stmt|;
DECL|field|remoteName
specifier|public
name|String
name|remoteName
decl_stmt|;
DECL|field|projectName
specifier|public
name|String
name|projectName
decl_stmt|;
DECL|field|queueName
specifier|public
name|String
name|queueName
decl_stmt|;
DECL|method|TaskInfo (Task<?> task)
specifier|public
name|TaskInfo
parameter_list|(
name|Task
argument_list|<
name|?
argument_list|>
name|task
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|IdGenerator
operator|.
name|format
argument_list|(
name|task
operator|.
name|getTaskId
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|task
operator|.
name|getState
argument_list|()
expr_stmt|;
name|this
operator|.
name|startTime
operator|=
operator|new
name|Timestamp
argument_list|(
name|task
operator|.
name|getStartTime
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|task
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|this
operator|.
name|command
operator|=
name|task
operator|.
name|toString
argument_list|()
expr_stmt|;
name|this
operator|.
name|queueName
operator|=
name|task
operator|.
name|getQueueName
argument_list|()
expr_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|ProjectTask
condition|)
block|{
name|ProjectTask
argument_list|<
name|?
argument_list|>
name|projectTask
init|=
operator|(
operator|(
name|ProjectTask
argument_list|<
name|?
argument_list|>
operator|)
name|task
operator|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|name
init|=
name|projectTask
operator|.
name|getProjectNameKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|projectName
operator|=
name|name
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|remoteName
operator|=
name|projectTask
operator|.
name|getRemoteName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

