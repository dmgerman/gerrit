begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|checkState
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
name|ImmutableBiMap
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
name|ImmutableMap
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
name|Permission
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
name|Optional
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
comment|/**  * Mappings from {@link com.google.gerrit.extensions.api.access.GerritPermission} enum instances to  * the permission names used by {@link DefaultPermissionBackend}.  *  *<p>These should be considered implementation details of {@code DefaultPermissionBackend}; a  * backend that doesn't respect the default permission model will not need to consult these.  * However, implementations may also choose to respect certain aspects of the default permission  * model, so this class is provided as public to aid those implementations.  */
end_comment

begin_class
DECL|class|DefaultPermissionMappings
specifier|public
class|class
name|DefaultPermissionMappings
block|{
DECL|field|CAPABILITIES
specifier|private
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|GlobalPermission
argument_list|,
name|String
argument_list|>
name|CAPABILITIES
init|=
name|ImmutableBiMap
operator|.
expr|<
name|GlobalPermission
decl_stmt|,
name|String
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|ACCESS_DATABASE
argument_list|,
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|,
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|CREATE_ACCOUNT
argument_list|,
name|GlobalCapability
operator|.
name|CREATE_ACCOUNT
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|CREATE_GROUP
argument_list|,
name|GlobalCapability
operator|.
name|CREATE_GROUP
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|CREATE_PROJECT
argument_list|,
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|EMAIL_REVIEWERS
argument_list|,
name|GlobalCapability
operator|.
name|EMAIL_REVIEWERS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|FLUSH_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|KILL_TASK
argument_list|,
name|GlobalCapability
operator|.
name|KILL_TASK
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|MAINTAIN_SERVER
argument_list|,
name|GlobalCapability
operator|.
name|MAINTAIN_SERVER
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|MODIFY_ACCOUNT
argument_list|,
name|GlobalCapability
operator|.
name|MODIFY_ACCOUNT
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|RUN_AS
argument_list|,
name|GlobalCapability
operator|.
name|RUN_AS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|RUN_GC
argument_list|,
name|GlobalCapability
operator|.
name|RUN_GC
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|STREAM_EVENTS
argument_list|,
name|GlobalCapability
operator|.
name|STREAM_EVENTS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_ALL_ACCOUNTS
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_ALL_ACCOUNTS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_CONNECTIONS
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_CONNECTIONS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_PLUGINS
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_PLUGINS
argument_list|)
decl|.
name|put
argument_list|(
name|GlobalPermission
operator|.
name|VIEW_QUEUE
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_QUEUE
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
static|static
block|{
name|checkMapContainsAllEnumValues
argument_list|(
name|CAPABILITIES
argument_list|,
name|GlobalPermission
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|field|PROJECT_PERMISSIONS
specifier|private
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|ProjectPermission
argument_list|,
name|String
argument_list|>
name|PROJECT_PERMISSIONS
init|=
name|ImmutableBiMap
operator|.
expr|<
name|ProjectPermission
decl_stmt|,
name|String
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|ProjectPermission
operator|.
name|READ
argument_list|,
name|Permission
operator|.
name|READ
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|field|REF_PERMISSIONS
specifier|private
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|RefPermission
argument_list|,
name|String
argument_list|>
name|REF_PERMISSIONS
init|=
name|ImmutableBiMap
operator|.
expr|<
name|RefPermission
decl_stmt|,
name|String
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|READ
argument_list|,
name|Permission
operator|.
name|READ
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|CREATE
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|DELETE
argument_list|,
name|Permission
operator|.
name|DELETE
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|UPDATE
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|FORGE_AUTHOR
argument_list|,
name|Permission
operator|.
name|FORGE_AUTHOR
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|FORGE_COMMITTER
argument_list|,
name|Permission
operator|.
name|FORGE_COMMITTER
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|FORGE_SERVER
argument_list|,
name|Permission
operator|.
name|FORGE_SERVER
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|CREATE_TAG
argument_list|,
name|Permission
operator|.
name|CREATE_TAG
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|CREATE_SIGNED_TAG
argument_list|,
name|Permission
operator|.
name|CREATE_SIGNED_TAG
argument_list|)
decl|.
name|put
argument_list|(
name|RefPermission
operator|.
name|READ_PRIVATE_CHANGES
argument_list|,
name|Permission
operator|.
name|VIEW_PRIVATE_CHANGES
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
DECL|method|checkMapContainsAllEnumValues ( ImmutableMap<T, String> actual, Class<T> clazz)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|void
name|checkMapContainsAllEnumValues
parameter_list|(
name|ImmutableMap
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|actual
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|Set
argument_list|<
name|T
argument_list|>
name|expected
init|=
name|EnumSet
operator|.
name|allOf
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|actual
operator|.
name|keySet
argument_list|()
operator|.
name|equals
argument_list|(
name|expected
argument_list|)
argument_list|,
literal|"all %s values must be defined, found: %s"
argument_list|,
name|clazz
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|actual
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|globalPermissionName (GlobalPermission globalPermission)
specifier|public
specifier|static
name|String
name|globalPermissionName
parameter_list|(
name|GlobalPermission
name|globalPermission
parameter_list|)
block|{
return|return
name|checkNotNull
argument_list|(
name|CAPABILITIES
operator|.
name|get
argument_list|(
name|globalPermission
argument_list|)
argument_list|)
return|;
block|}
DECL|method|globalPermission (String capabilityName)
specifier|public
specifier|static
name|Optional
argument_list|<
name|GlobalPermission
argument_list|>
name|globalPermission
parameter_list|(
name|String
name|capabilityName
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|CAPABILITIES
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|capabilityName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|pluginPermissionName (PluginPermission pluginPermission)
specifier|public
specifier|static
name|String
name|pluginPermissionName
parameter_list|(
name|PluginPermission
name|pluginPermission
parameter_list|)
block|{
return|return
name|pluginPermission
operator|.
name|pluginName
argument_list|()
operator|+
literal|'-'
operator|+
name|pluginPermission
operator|.
name|capability
argument_list|()
return|;
block|}
DECL|method|globalOrPluginPermissionName (GlobalOrPluginPermission permission)
specifier|public
specifier|static
name|String
name|globalOrPluginPermissionName
parameter_list|(
name|GlobalOrPluginPermission
name|permission
parameter_list|)
block|{
return|return
name|permission
operator|instanceof
name|GlobalPermission
condition|?
name|globalPermissionName
argument_list|(
operator|(
name|GlobalPermission
operator|)
name|permission
argument_list|)
else|:
name|pluginPermissionName
argument_list|(
operator|(
name|PluginPermission
operator|)
name|permission
argument_list|)
return|;
block|}
DECL|method|projectPermissionName (ProjectPermission projectPermission)
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|projectPermissionName
parameter_list|(
name|ProjectPermission
name|projectPermission
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|PROJECT_PERMISSIONS
operator|.
name|get
argument_list|(
name|projectPermission
argument_list|)
argument_list|)
return|;
block|}
DECL|method|projectPermission (String permissionName)
specifier|public
specifier|static
name|Optional
argument_list|<
name|ProjectPermission
argument_list|>
name|projectPermission
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|PROJECT_PERMISSIONS
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|permissionName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|refPermissionName (RefPermission refPermission)
specifier|public
specifier|static
name|Optional
argument_list|<
name|String
argument_list|>
name|refPermissionName
parameter_list|(
name|RefPermission
name|refPermission
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|REF_PERMISSIONS
operator|.
name|get
argument_list|(
name|refPermission
argument_list|)
argument_list|)
return|;
block|}
DECL|method|refPermission (String permissionName)
specifier|public
specifier|static
name|Optional
argument_list|<
name|RefPermission
argument_list|>
name|refPermission
parameter_list|(
name|String
name|permissionName
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|REF_PERMISSIONS
operator|.
name|inverse
argument_list|()
operator|.
name|get
argument_list|(
name|permissionName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|DefaultPermissionMappings ()
specifier|private
name|DefaultPermissionMappings
parameter_list|()
block|{}
block|}
end_class

end_unit

