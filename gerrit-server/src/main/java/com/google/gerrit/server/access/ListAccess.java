begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.access
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|access
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
name|Iterables
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
name|Lists
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
name|Maps
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
name|common
operator|.
name|ProjectInfo
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|NoSuchProjectException
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
name|Provider
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|Set
import|;
end_import

begin_class
DECL|class|ListAccess
specifier|public
class|class
name|ListAccess
implements|implements
name|RestReadView
argument_list|<
name|TopLevelResource
argument_list|>
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"PROJECT"
argument_list|,
name|usage
operator|=
literal|"projects for which the access rights should be returned"
argument_list|)
DECL|field|projects
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
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
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|projectJson
specifier|private
specifier|final
name|ProjectJson
name|projectJson
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|groupControlFactory
specifier|private
specifier|final
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListAccess (Provider<CurrentUser> self, ProjectControl.GenericFactory projectControlFactory, ProjectCache projectCache, ProjectJson projectJson, MetaDataUpdate.Server metaDataUpdateFactory, GroupControl.Factory groupControlFactory, GroupBackend groupBackend, GroupJson groupJson, AllProjectsName allProjectsName)
specifier|public
name|ListAccess
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|self
parameter_list|,
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ProjectJson
name|projectJson
parameter_list|,
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
parameter_list|,
name|GroupControl
operator|.
name|Factory
name|groupControlFactory
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|GroupJson
name|groupJson
parameter_list|,
name|AllProjectsName
name|allProjectsName
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
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|projectJson
operator|=
name|projectJson
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|groupControlFactory
operator|=
name|groupControlFactory
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ProjectAccessInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|ResourceConflictException
throws|,
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ProjectAccessInfo
argument_list|>
name|access
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|projects
control|)
block|{
name|Project
operator|.
name|NameKey
name|projectName
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|p
argument_list|)
decl_stmt|;
name|ProjectControl
name|pc
init|=
name|open
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|ProjectConfig
name|config
decl_stmt|;
try|try
block|{
comment|// Load the current configuration from the repository, ensuring it's the most
comment|// recent version available. If it differs from what was in the project
comment|// state, force a cache flush now.
comment|//
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
try|try
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
name|pc
operator|=
name|open
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
name|pc
operator|.
name|getProjectState
argument_list|()
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
name|pc
operator|=
name|open
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
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|p
argument_list|)
throw|;
block|}
name|access
operator|.
name|put
argument_list|(
name|p
argument_list|,
operator|new
name|ProjectAccessInfo
argument_list|(
name|pc
argument_list|,
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|access
return|;
block|}
DECL|method|open (Project.NameKey projectName)
specifier|private
name|ProjectControl
name|open
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
try|try
block|{
return|return
name|projectControlFactory
operator|.
name|validateFor
argument_list|(
name|projectName
argument_list|,
name|ProjectControl
operator|.
name|OWNER
operator||
name|ProjectControl
operator|.
name|VISIBLE
argument_list|,
name|self
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|class|ProjectAccessInfo
specifier|public
class|class
name|ProjectAccessInfo
block|{
DECL|field|revision
specifier|public
name|String
name|revision
decl_stmt|;
DECL|field|inheritsFrom
specifier|public
name|ProjectInfo
name|inheritsFrom
decl_stmt|;
DECL|field|local
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|AccessSectionInfo
argument_list|>
name|local
decl_stmt|;
DECL|field|isOwner
specifier|public
name|Boolean
name|isOwner
decl_stmt|;
DECL|field|ownerOf
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|ownerOf
decl_stmt|;
DECL|field|canUpload
specifier|public
name|Boolean
name|canUpload
decl_stmt|;
DECL|field|canAdd
specifier|public
name|Boolean
name|canAdd
decl_stmt|;
DECL|field|configVisible
specifier|public
name|Boolean
name|configVisible
decl_stmt|;
DECL|method|ProjectAccessInfo (ProjectControl pc, ProjectConfig config)
specifier|public
name|ProjectAccessInfo
parameter_list|(
name|ProjectControl
name|pc
parameter_list|,
name|ProjectConfig
name|config
parameter_list|)
block|{
specifier|final
name|RefControl
name|metaConfigControl
init|=
name|pc
operator|.
name|controlForRef
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
name|local
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|ownerOf
operator|=
name|Sets
operator|.
name|newHashSet
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|Boolean
argument_list|>
name|visibleGroups
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
name|pc
operator|.
name|isOwner
argument_list|()
condition|)
block|{
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|AccessSectionInfo
argument_list|(
name|section
argument_list|)
argument_list|)
expr_stmt|;
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
name|metaConfigControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|local
operator|.
name|put
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|AccessSectionInfo
argument_list|(
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
name|RefControl
name|rc
init|=
name|pc
operator|.
name|controlForRef
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|rc
operator|.
name|isOwner
argument_list|()
condition|)
block|{
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|AccessSectionInfo
argument_list|(
name|section
argument_list|)
argument_list|)
expr_stmt|;
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
name|metaConfigControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|AccessSectionInfo
argument_list|(
name|section
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|rc
operator|.
name|isVisible
argument_list|()
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
name|group
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
name|group
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|Boolean
name|canSeeGroup
init|=
name|visibleGroups
operator|.
name|get
argument_list|(
name|group
argument_list|)
decl_stmt|;
if|if
condition|(
name|canSeeGroup
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|canSeeGroup
operator|=
name|groupControlFactory
operator|.
name|controlFor
argument_list|(
name|group
argument_list|)
operator|.
name|isVisible
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchGroupException
name|e
parameter_list|)
block|{
name|canSeeGroup
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
name|visibleGroups
operator|.
name|put
argument_list|(
name|group
argument_list|,
name|canSeeGroup
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|canSeeGroup
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
name|local
operator|.
name|put
argument_list|(
name|name
argument_list|,
operator|new
name|AccessSectionInfo
argument_list|(
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
name|ownerOf
operator|.
name|isEmpty
argument_list|()
operator|&&
name|pc
operator|.
name|isOwnerAnyRef
argument_list|()
condition|)
block|{
comment|// Special case: If the section list is empty, this project has no current
comment|// access control information. Rely on what ProjectControl determines
comment|// is ownership, which probably means falling back to site administrators.
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
name|pc
operator|.
name|getProjectState
argument_list|()
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
name|pc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
operator|.
name|equals
argument_list|(
name|allProjectsName
argument_list|)
condition|)
block|{
if|if
condition|(
name|pc
operator|.
name|isOwner
argument_list|()
condition|)
block|{
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
block|}
name|isOwner
operator|=
name|toBoolean
argument_list|(
name|pc
operator|.
name|isOwner
argument_list|()
argument_list|)
expr_stmt|;
name|canUpload
operator|=
name|toBoolean
argument_list|(
name|pc
operator|.
name|isOwner
argument_list|()
operator|||
operator|(
name|metaConfigControl
operator|.
name|isVisible
argument_list|()
operator|&&
name|metaConfigControl
operator|.
name|canUpload
argument_list|()
operator|)
argument_list|)
expr_stmt|;
name|canAdd
operator|=
name|toBoolean
argument_list|(
name|pc
operator|.
name|canAddRefs
argument_list|()
argument_list|)
expr_stmt|;
name|configVisible
operator|=
name|pc
operator|.
name|isOwner
argument_list|()
operator|||
name|metaConfigControl
operator|.
name|isVisible
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|AccessSectionInfo
specifier|public
class|class
name|AccessSectionInfo
block|{
DECL|field|permissions
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PermissionInfo
argument_list|>
name|permissions
decl_stmt|;
DECL|method|AccessSectionInfo (AccessSection section)
specifier|public
name|AccessSectionInfo
parameter_list|(
name|AccessSection
name|section
parameter_list|)
block|{
name|permissions
operator|=
name|Maps
operator|.
name|newHashMap
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
name|permissions
operator|.
name|put
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|PermissionInfo
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|PermissionInfo
specifier|public
class|class
name|PermissionInfo
block|{
DECL|field|label
specifier|public
name|String
name|label
decl_stmt|;
DECL|field|exclusive
specifier|public
name|Boolean
name|exclusive
decl_stmt|;
DECL|field|rules
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|PermissionRuleInfo
argument_list|>
name|rules
decl_stmt|;
DECL|method|PermissionInfo (Permission permission)
specifier|public
name|PermissionInfo
parameter_list|(
name|Permission
name|permission
parameter_list|)
block|{
name|label
operator|=
name|permission
operator|.
name|getLabel
argument_list|()
expr_stmt|;
name|exclusive
operator|=
name|toBoolean
argument_list|(
name|permission
operator|.
name|getExclusiveGroup
argument_list|()
argument_list|)
expr_stmt|;
name|rules
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
for|for
control|(
name|PermissionRule
name|r
range|:
name|permission
operator|.
name|getRules
argument_list|()
control|)
block|{
name|rules
operator|.
name|put
argument_list|(
name|r
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|PermissionRuleInfo
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|PermissionRuleInfo
specifier|public
class|class
name|PermissionRuleInfo
block|{
DECL|field|action
specifier|public
name|PermissionRule
operator|.
name|Action
name|action
decl_stmt|;
DECL|field|force
specifier|public
name|Boolean
name|force
decl_stmt|;
DECL|field|min
specifier|public
name|Integer
name|min
decl_stmt|;
DECL|field|max
specifier|public
name|Integer
name|max
decl_stmt|;
DECL|method|PermissionRuleInfo (PermissionRule rule)
specifier|public
name|PermissionRuleInfo
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
name|action
operator|=
name|rule
operator|.
name|getAction
argument_list|()
expr_stmt|;
name|force
operator|=
name|toBoolean
argument_list|(
name|rule
operator|.
name|getForce
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasRange
argument_list|(
name|rule
argument_list|)
condition|)
block|{
name|min
operator|=
name|rule
operator|.
name|getMin
argument_list|()
expr_stmt|;
name|max
operator|=
name|rule
operator|.
name|getMax
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|hasRange (PermissionRule rule)
specifier|private
name|boolean
name|hasRange
parameter_list|(
name|PermissionRule
name|rule
parameter_list|)
block|{
return|return
operator|(
operator|!
operator|(
name|rule
operator|.
name|getMin
argument_list|()
operator|==
literal|null
operator|||
name|rule
operator|.
name|getMin
argument_list|()
operator|==
literal|0
operator|)
operator|)
operator|||
operator|(
operator|!
operator|(
name|rule
operator|.
name|getMax
argument_list|()
operator|==
literal|null
operator|||
name|rule
operator|.
name|getMax
argument_list|()
operator|==
literal|0
operator|)
operator|)
return|;
block|}
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

