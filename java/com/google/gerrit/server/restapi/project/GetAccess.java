begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
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
name|project
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
name|server
operator|.
name|permissions
operator|.
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
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
name|server
operator|.
name|permissions
operator|.
name|ProjectPermission
operator|.
name|CREATE_REF
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
name|server
operator|.
name|permissions
operator|.
name|RefPermission
operator|.
name|CREATE_CHANGE
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
name|server
operator|.
name|permissions
operator|.
name|RefPermission
operator|.
name|READ
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
name|server
operator|.
name|permissions
operator|.
name|RefPermission
operator|.
name|WRITE_CONFIG
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
name|toMap
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
name|AccessSection
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
name|RefConfigSection
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
name|WebLinkInfoCommon
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
name|errors
operator|.
name|NoSuchGroupException
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
name|AccessSectionInfo
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
name|PermissionInfo
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
name|PermissionRuleInfo
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
name|ProjectAccessInfo
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
name|common
operator|.
name|GroupInfo
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
name|common
operator|.
name|WebLinkInfo
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
name|ResourceConflictException
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
name|ResourceNotFoundException
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
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
name|WebLinks
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
name|GroupBackend
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
name|GroupControl
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
name|AllProjectsName
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
name|MetaDataUpdate
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
name|ProjectConfig
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
name|permissions
operator|.
name|RefPermission
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
name|ProjectJson
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
name|ProjectResource
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
name|restapi
operator|.
name|group
operator|.
name|GroupJson
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|ArrayList
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
name|HashSet
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|GetAccess
specifier|public
class|class
name|GetAccess
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|GetAccess
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** Marker value used in {@code Map<?, GroupInfo>} for groups not visible to current user. */
DECL|field|INVISIBLE_SENTINEL
specifier|private
specifier|static
specifier|final
name|GroupInfo
name|INVISIBLE_SENTINEL
init|=
operator|new
name|GroupInfo
argument_list|()
decl_stmt|;
DECL|field|ACTION_TYPE
specifier|public
specifier|static
specifier|final
name|ImmutableBiMap
argument_list|<
name|PermissionRule
operator|.
name|Action
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
argument_list|>
name|ACTION_TYPE
init|=
name|ImmutableBiMap
operator|.
name|of
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|ALLOW
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
operator|.
name|ALLOW
argument_list|,
name|PermissionRule
operator|.
name|Action
operator|.
name|BATCH
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
operator|.
name|BATCH
argument_list|,
name|PermissionRule
operator|.
name|Action
operator|.
name|BLOCK
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
operator|.
name|BLOCK
argument_list|,
name|PermissionRule
operator|.
name|Action
operator|.
name|DENY
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
operator|.
name|DENY
argument_list|,
name|PermissionRule
operator|.
name|Action
operator|.
name|INTERACTIVE
argument_list|,
name|PermissionRuleInfo
operator|.
name|Action
operator|.
name|INTERACTIVE
argument_list|)
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|projectJson
specifier|private
specifier|final
name|ProjectJson
name|projectJson
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|groupJson
specifier|private
specifier|final
name|GroupJson
name|groupJson
decl_stmt|;
DECL|field|webLinks
specifier|private
specifier|final
name|WebLinks
name|webLinks
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetAccess ( Provider<CurrentUser> self, PermissionBackend permissionBackend, GroupControl.Factory groupControlFactory, AllProjectsName allProjectsName, ProjectCache projectCache, MetaDataUpdate.Server metaDataUpdateFactory, ProjectJson projectJson, GroupBackend groupBackend, GroupJson groupJson, WebLinks webLinks)
specifier|public
name|GetAccess
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
parameter_list|,
name|ProjectJson
name|projectJson
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|GroupJson
name|groupJson
parameter_list|,
name|WebLinks
name|webLinks
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|projectJson
operator|=
name|projectJson
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|groupJson
operator|=
name|groupJson
expr_stmt|;
name|this
operator|.
name|webLinks
operator|=
name|webLinks
expr_stmt|;
block|}
DECL|method|apply (Project.NameKey nameKey)
specifier|public
name|ProjectAccessInfo
name|apply
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|OrmException
block|{
name|ProjectState
name|state
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|nameKey
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|apply
argument_list|(
operator|new
name|ProjectResource
argument_list|(
name|state
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource rsrc)
specifier|public
name|ProjectAccessInfo
name|apply
parameter_list|(
name|ProjectResource
name|rsrc
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|OrmException
block|{
comment|// Load the current configuration from the repository, ensuring it's the most
comment|// recent version available. If it differs from what was in the project
comment|// state, force a cache flush now.
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|rsrc
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|ProjectAccessInfo
name|info
init|=
operator|new
name|ProjectAccessInfo
argument_list|()
decl_stmt|;
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|PermissionBackend
operator|.
name|ForProject
name|perm
init|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|ProjectConfig
name|config
decl_stmt|;
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
init|)
block|{
name|config
operator|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|info
operator|.
name|configWebLinks
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
comment|// WebLinks operates in terms of the data types used in the GWT UI. Once the GWT UI is gone,
comment|// WebLinks should be fixed to use the extension data types.
for|for
control|(
name|WebLinkInfoCommon
name|wl
range|:
name|webLinks
operator|.
name|getFileHistoryLinks
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|,
name|config
operator|.
name|getRevision
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|ProjectConfig
operator|.
name|PROJECT_CONFIG
argument_list|)
control|)
block|{
name|info
operator|.
name|configWebLinks
operator|.
name|add
argument_list|(
operator|new
name|WebLinkInfo
argument_list|(
name|wl
operator|.
name|name
argument_list|,
name|wl
operator|.
name|imageUrl
argument_list|,
name|wl
operator|.
name|url
argument_list|,
name|wl
operator|.
name|target
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|updateGroupNames
argument_list|(
name|groupBackend
argument_list|)
condition|)
block|{
name|md
operator|.
name|setMessage
argument_list|(
literal|"Update group names\n"
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|projectState
operator|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|perm
operator|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getRevision
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getRevision
argument_list|()
operator|.
name|equals
argument_list|(
name|projectState
operator|.
name|getConfig
argument_list|()
operator|.
name|getRevision
argument_list|()
argument_list|)
condition|)
block|{
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|projectState
operator|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|perm
operator|=
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|rsrc
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
comment|// The following implementation must match the ProjectAccessFactory JSON RPC endpoint.
name|info
operator|.
name|local
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|info
operator|.
name|ownerOf
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|GroupInfo
argument_list|>
name|visibleGroups
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|canReadConfig
init|=
name|check
argument_list|(
name|perm
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|READ
argument_list|)
decl_stmt|;
name|boolean
name|canWriteConfig
init|=
name|check
argument_list|(
name|perm
argument_list|,
name|ProjectPermission
operator|.
name|WRITE_CONFIG
argument_list|)
decl_stmt|;
comment|// Check if the project state permits read only when the user is not allowed to write the config
comment|// (=owner). This is so that the owner can still read (and in the next step write) the project's
comment|// config to set the project state to any state that is not HIDDEN.
if|if
condition|(
operator|!
name|canWriteConfig
condition|)
block|{
name|projectState
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|AccessSection
name|section
range|:
name|config
operator|.
name|getAccessSections
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|section
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|canWriteConfig
condition|)
block|{
name|info
operator|.
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|createAccessSection
argument_list|(
name|visibleGroups
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|ownerOf
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|canReadConfig
condition|)
block|{
name|info
operator|.
name|local
operator|.
name|put
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|,
name|createAccessSection
argument_list|(
name|visibleGroups
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|RefConfigSection
operator|.
name|isValid
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|check
argument_list|(
name|perm
argument_list|,
name|name
argument_list|,
name|WRITE_CONFIG
argument_list|)
condition|)
block|{
name|info
operator|.
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|createAccessSection
argument_list|(
name|visibleGroups
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|ownerOf
operator|.
name|add
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|canReadConfig
condition|)
block|{
name|info
operator|.
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|createAccessSection
argument_list|(
name|visibleGroups
argument_list|,
name|section
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|check
argument_list|(
name|perm
argument_list|,
name|name
argument_list|,
name|READ
argument_list|)
condition|)
block|{
comment|// Filter the section to only add rules describing groups that
comment|// are visible to the current-user. This includes any group the
comment|// user is a member of, as well as groups they own or that
comment|// are visible to all users.
name|AccessSection
name|dst
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Permission
name|srcPerm
range|:
name|section
operator|.
name|getPermissions
argument_list|()
control|)
block|{
name|Permission
name|dstPerm
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PermissionRule
name|srcRule
range|:
name|srcPerm
operator|.
name|getRules
argument_list|()
control|)
block|{
name|AccountGroup
operator|.
name|UUID
name|groupId
init|=
name|srcRule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupId
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|GroupInfo
name|group
init|=
name|loadGroup
argument_list|(
name|visibleGroups
argument_list|,
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|!=
name|INVISIBLE_SENTINEL
condition|)
block|{
if|if
condition|(
name|dstPerm
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|dst
operator|==
literal|null
condition|)
block|{
name|dst
operator|=
operator|new
name|AccessSection
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|info
operator|.
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|createAccessSection
argument_list|(
name|visibleGroups
argument_list|,
name|dst
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|dstPerm
operator|=
name|dst
operator|.
name|getPermission
argument_list|(
name|srcPerm
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|dstPerm
operator|.
name|add
argument_list|(
name|srcRule
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
if|if
condition|(
name|info
operator|.
name|ownerOf
operator|.
name|isEmpty
argument_list|()
operator|&&
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|test
argument_list|(
name|GlobalPermission
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
condition|)
block|{
comment|// Special case: If the section list is empty, this project has no current
comment|// access control information. Fall back to site administrators.
name|info
operator|.
name|ownerOf
operator|.
name|add
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getRevision
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|revision
operator|=
name|config
operator|.
name|getRevision
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|ProjectState
name|parent
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|projectState
operator|.
name|parents
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|info
operator|.
name|inheritsFrom
operator|=
name|projectJson
operator|.
name|format
argument_list|(
name|parent
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectName
operator|.
name|equals
argument_list|(
name|allProjectsName
argument_list|)
operator|&&
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|testOrFalse
argument_list|(
name|ADMINISTRATE_SERVER
argument_list|)
condition|)
block|{
name|info
operator|.
name|ownerOf
operator|.
name|add
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|isOwner
operator|=
name|toBoolean
argument_list|(
name|canWriteConfig
argument_list|)
expr_stmt|;
name|info
operator|.
name|canUpload
operator|=
name|toBoolean
argument_list|(
name|projectState
operator|.
name|statePermitsWrite
argument_list|()
operator|&&
operator|(
name|canWriteConfig
operator|||
operator|(
name|canReadConfig
operator|&&
name|perm
operator|.
name|ref
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|testOrFalse
argument_list|(
name|CREATE_CHANGE
argument_list|)
operator|)
operator|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|canAdd
operator|=
name|toBoolean
argument_list|(
name|perm
operator|.
name|testOrFalse
argument_list|(
name|CREATE_REF
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|configVisible
operator|=
name|canReadConfig
operator|||
name|canWriteConfig
expr_stmt|;
name|info
operator|.
name|groups
operator|=
name|visibleGroups
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getValue
argument_list|()
operator|!=
name|INVISIBLE_SENTINEL
argument_list|)
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|e
lambda|->
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|loadGroup (Map<AccountGroup.UUID, GroupInfo> visibleGroups, AccountGroup.UUID id)
specifier|private
name|GroupInfo
name|loadGroup
parameter_list|(
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|GroupInfo
argument_list|>
name|visibleGroups
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
name|GroupInfo
name|group
init|=
name|visibleGroups
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|GroupControl
name|control
init|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|group
operator|=
name|INVISIBLE_SENTINEL
expr_stmt|;
if|if
condition|(
name|control
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|group
operator|=
name|groupJson
operator|.
name|format
argument_list|(
name|control
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|group
operator|.
name|id
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"NoSuchGroupException; ignoring group "
operator|+
name|id
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|group
operator|=
name|INVISIBLE_SENTINEL
expr_stmt|;
block|}
name|visibleGroups
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
return|return
name|group
return|;
block|}
DECL|method|check (PermissionBackend.ForProject ctx, String ref, RefPermission perm)
specifier|private
specifier|static
name|boolean
name|check
parameter_list|(
name|PermissionBackend
operator|.
name|ForProject
name|ctx
parameter_list|,
name|String
name|ref
parameter_list|,
name|RefPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
try|try
block|{
name|ctx
operator|.
name|ref
argument_list|(
name|ref
argument_list|)
operator|.
name|check
argument_list|(
name|perm
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|denied
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|check (PermissionBackend.ForProject ctx, ProjectPermission perm)
specifier|private
specifier|static
name|boolean
name|check
parameter_list|(
name|PermissionBackend
operator|.
name|ForProject
name|ctx
parameter_list|,
name|ProjectPermission
name|perm
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
try|try
block|{
name|ctx
operator|.
name|check
argument_list|(
name|perm
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|denied
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|createAccessSection ( Map<AccountGroup.UUID, GroupInfo> groups, AccessSection section)
specifier|private
name|AccessSectionInfo
name|createAccessSection
parameter_list|(
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|GroupInfo
argument_list|>
name|groups
parameter_list|,
name|AccessSection
name|section
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccessSectionInfo
name|accessSectionInfo
init|=
operator|new
name|AccessSectionInfo
argument_list|()
decl_stmt|;
name|accessSectionInfo
operator|.
name|permissions
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Permission
name|p
range|:
name|section
operator|.
name|getPermissions
argument_list|()
control|)
block|{
name|PermissionInfo
name|pInfo
init|=
operator|new
name|PermissionInfo
argument_list|(
name|p
operator|.
name|getLabel
argument_list|()
argument_list|,
name|p
operator|.
name|getExclusiveGroup
argument_list|()
condition|?
literal|true
else|:
literal|null
argument_list|)
decl_stmt|;
name|pInfo
operator|.
name|rules
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|PermissionRule
name|r
range|:
name|p
operator|.
name|getRules
argument_list|()
control|)
block|{
name|PermissionRuleInfo
name|info
init|=
operator|new
name|PermissionRuleInfo
argument_list|(
name|ACTION_TYPE
operator|.
name|get
argument_list|(
name|r
operator|.
name|getAction
argument_list|()
argument_list|)
argument_list|,
name|r
operator|.
name|getForce
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|hasRange
argument_list|()
condition|)
block|{
name|info
operator|.
name|max
operator|=
name|r
operator|.
name|getMax
argument_list|()
expr_stmt|;
name|info
operator|.
name|min
operator|=
name|r
operator|.
name|getMin
argument_list|()
expr_stmt|;
block|}
name|AccountGroup
operator|.
name|UUID
name|group
init|=
name|r
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
decl_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|pInfo
operator|.
name|rules
operator|.
name|put
argument_list|(
name|group
operator|.
name|get
argument_list|()
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|loadGroup
argument_list|(
name|groups
argument_list|,
name|group
argument_list|)
expr_stmt|;
block|}
block|}
name|accessSectionInfo
operator|.
name|permissions
operator|.
name|put
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|,
name|pInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|accessSectionInfo
return|;
block|}
DECL|method|toBoolean (boolean value)
specifier|private
specifier|static
name|Boolean
name|toBoolean
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
return|return
name|value
condition|?
literal|true
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

