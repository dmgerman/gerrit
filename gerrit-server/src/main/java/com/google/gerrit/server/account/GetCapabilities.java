begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
import|;
end_import

begin_import
import|import static
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
operator|.
name|CREATE_ACCOUNT
import|;
end_import

begin_import
import|import static
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
operator|.
name|CREATE_GROUP
import|;
end_import

begin_import
import|import static
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
operator|.
name|CREATE_PROJECT
import|;
end_import

begin_import
import|import static
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
operator|.
name|EMAIL_REVIEWERS
import|;
end_import

begin_import
import|import static
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
operator|.
name|FLUSH_CACHES
import|;
end_import

begin_import
import|import static
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
operator|.
name|KILL_TASK
import|;
end_import

begin_import
import|import static
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
operator|.
name|MAINTAIN_SERVER
import|;
end_import

begin_import
import|import static
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
operator|.
name|MODIFY_ACCOUNT
import|;
end_import

begin_import
import|import static
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
operator|.
name|PRIORITY
import|;
end_import

begin_import
import|import static
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
operator|.
name|RUN_GC
import|;
end_import

begin_import
import|import static
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
operator|.
name|STREAM_EVENTS
import|;
end_import

begin_import
import|import static
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
operator|.
name|VIEW_ALL_ACCOUNTS
import|;
end_import

begin_import
import|import static
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
operator|.
name|VIEW_CACHES
import|;
end_import

begin_import
import|import static
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
operator|.
name|VIEW_CONNECTIONS
import|;
end_import

begin_import
import|import static
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
operator|.
name|VIEW_PLUGINS
import|;
end_import

begin_import
import|import static
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
operator|.
name|VIEW_QUEUE
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
name|extensions
operator|.
name|config
operator|.
name|CapabilityDefinition
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
name|registration
operator|.
name|DynamicMap
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
name|BinaryResult
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
name|OptionUtil
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
name|OutputFormat
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
name|AccountResource
operator|.
name|Capability
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
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|GetCapabilities
class|class
name|GetCapabilities
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-q"
argument_list|,
name|metaVar
operator|=
literal|"CAP"
argument_list|,
name|usage
operator|=
literal|"Capability to inspect"
argument_list|)
DECL|method|addQuery (String name)
name|void
name|addQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|query
operator|==
literal|null
condition|)
block|{
name|query
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|Iterables
operator|.
name|addAll
argument_list|(
name|query
argument_list|,
name|OptionUtil
operator|.
name|splitOptionValue
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|field|query
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|query
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
DECL|field|pluginCapabilities
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|CapabilityDefinition
argument_list|>
name|pluginCapabilities
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetCapabilities (Provider<CurrentUser> self, DynamicMap<CapabilityDefinition> pluginCapabilities)
name|GetCapabilities
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|DynamicMap
argument_list|<
name|CapabilityDefinition
argument_list|>
name|pluginCapabilities
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|pluginCapabilities
operator|=
name|pluginCapabilities
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource resource)
specifier|public
name|Object
name|apply
parameter_list|(
name|AccountResource
name|resource
parameter_list|)
throws|throws
name|AuthException
block|{
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|resource
operator|.
name|getUser
argument_list|()
operator|&&
operator|!
name|self
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"restricted to administrator"
argument_list|)
throw|;
block|}
name|CapabilityControl
name|cc
init|=
name|resource
operator|.
name|getUser
argument_list|()
operator|.
name|getCapabilities
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|have
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|GlobalCapability
operator|.
name|getAllNames
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|PRIORITY
argument_list|)
operator|&&
name|want
argument_list|(
name|name
argument_list|)
operator|&&
name|cc
operator|.
name|canPerform
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|GlobalCapability
operator|.
name|hasRange
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|have
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|Range
argument_list|(
name|cc
operator|.
name|getRange
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|have
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|String
name|pluginName
range|:
name|pluginCapabilities
operator|.
name|plugins
argument_list|()
control|)
block|{
for|for
control|(
name|String
name|capability
range|:
name|pluginCapabilities
operator|.
name|byPlugin
argument_list|(
name|pluginName
argument_list|)
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|String
operator|.
name|format
argument_list|(
literal|"%s-%s"
argument_list|,
name|pluginName
argument_list|,
name|capability
argument_list|)
decl_stmt|;
if|if
condition|(
name|want
argument_list|(
name|name
argument_list|)
operator|&&
name|cc
operator|.
name|canPerform
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|have
operator|.
name|put
argument_list|(
name|name
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|have
operator|.
name|put
argument_list|(
name|ACCESS_DATABASE
argument_list|,
name|cc
operator|.
name|canAccessDatabase
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|CREATE_ACCOUNT
argument_list|,
name|cc
operator|.
name|canCreateAccount
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|CREATE_GROUP
argument_list|,
name|cc
operator|.
name|canCreateGroup
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|CREATE_PROJECT
argument_list|,
name|cc
operator|.
name|canCreateProject
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|EMAIL_REVIEWERS
argument_list|,
name|cc
operator|.
name|canEmailReviewers
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|FLUSH_CACHES
argument_list|,
name|cc
operator|.
name|canFlushCaches
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|KILL_TASK
argument_list|,
name|cc
operator|.
name|canKillTask
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|MAINTAIN_SERVER
argument_list|,
name|cc
operator|.
name|canMaintainServer
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|MODIFY_ACCOUNT
argument_list|,
name|cc
operator|.
name|canModifyAccount
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|RUN_GC
argument_list|,
name|cc
operator|.
name|canRunGC
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|STREAM_EVENTS
argument_list|,
name|cc
operator|.
name|canStreamEvents
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|VIEW_ALL_ACCOUNTS
argument_list|,
name|cc
operator|.
name|canViewAllAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|VIEW_CACHES
argument_list|,
name|cc
operator|.
name|canViewCaches
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|VIEW_CONNECTIONS
argument_list|,
name|cc
operator|.
name|canViewConnections
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|VIEW_PLUGINS
argument_list|,
name|cc
operator|.
name|canViewPlugins
argument_list|()
argument_list|)
expr_stmt|;
name|have
operator|.
name|put
argument_list|(
name|VIEW_QUEUE
argument_list|,
name|cc
operator|.
name|canViewQueue
argument_list|()
argument_list|)
expr_stmt|;
name|QueueProvider
operator|.
name|QueueType
name|queue
init|=
name|cc
operator|.
name|getQueueType
argument_list|()
decl_stmt|;
if|if
condition|(
name|queue
operator|!=
name|QueueProvider
operator|.
name|QueueType
operator|.
name|INTERACTIVE
operator|||
operator|(
name|query
operator|!=
literal|null
operator|&&
name|query
operator|.
name|contains
argument_list|(
name|PRIORITY
argument_list|)
operator|)
condition|)
block|{
name|have
operator|.
name|put
argument_list|(
name|PRIORITY
argument_list|,
name|queue
argument_list|)
expr_stmt|;
block|}
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|itr
init|=
name|have
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itr
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|want
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|instanceof
name|Boolean
operator|&&
operator|!
operator|(
operator|(
name|Boolean
operator|)
name|e
operator|.
name|getValue
argument_list|()
operator|)
condition|)
block|{
name|itr
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|OutputFormat
operator|.
name|JSON
operator|.
name|newGson
argument_list|()
operator|.
name|toJsonTree
argument_list|(
name|have
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|want (String name)
specifier|private
name|boolean
name|want
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|query
operator|==
literal|null
operator|||
name|query
operator|.
name|contains
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
return|;
block|}
DECL|class|Range
specifier|private
specifier|static
class|class
name|Range
block|{
DECL|field|range
specifier|private
specifier|transient
name|PermissionRange
name|range
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|min
specifier|private
name|int
name|min
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|max
specifier|private
name|int
name|max
decl_stmt|;
DECL|method|Range (PermissionRange r)
name|Range
parameter_list|(
name|PermissionRange
name|r
parameter_list|)
block|{
name|range
operator|=
name|r
expr_stmt|;
name|min
operator|=
name|r
operator|.
name|getMin
argument_list|()
expr_stmt|;
name|max
operator|=
name|r
operator|.
name|getMax
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|range
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|CheckOne
specifier|static
class|class
name|CheckOne
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
operator|.
name|Capability
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (Capability resource)
specifier|public
name|BinaryResult
name|apply
parameter_list|(
name|Capability
name|resource
parameter_list|)
block|{
return|return
name|BinaryResult
operator|.
name|create
argument_list|(
literal|"ok\n"
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

