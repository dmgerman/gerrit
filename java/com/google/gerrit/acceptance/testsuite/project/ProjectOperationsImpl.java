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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_CONFIG
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
name|project
operator|.
name|ProjectConfig
operator|.
name|PROJECT_CONFIG
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|ImmutableList
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectCreation
operator|.
name|Builder
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|TestCapability
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|TestLabelPermission
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
name|acceptance
operator|.
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|TestPermission
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
name|GitRepositoryManager
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
name|meta
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
name|project
operator|.
name|CreateProjectArgs
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
name|project
operator|.
name|ProjectCreator
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
name|Collections
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|RandomStringUtils
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
name|lib
operator|.
name|Config
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
name|lib
operator|.
name|Constants
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
name|lib
operator|.
name|ObjectLoader
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
name|lib
operator|.
name|Ref
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
name|lib
operator|.
name|Repository
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
name|revwalk
operator|.
name|RevCommit
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
name|revwalk
operator|.
name|RevTree
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
name|revwalk
operator|.
name|RevWalk
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
name|treewalk
operator|.
name|TreeWalk
import|;
end_import

begin_class
DECL|class|ProjectOperationsImpl
specifier|public
class|class
name|ProjectOperationsImpl
implements|implements
name|ProjectOperations
block|{
DECL|field|allProjectsName
specifier|private
specifier|final
name|AllProjectsName
name|allProjectsName
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|projectConfigFactory
specifier|private
specifier|final
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
decl_stmt|;
DECL|field|projectCreator
specifier|private
specifier|final
name|ProjectCreator
name|projectCreator
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectOperationsImpl ( AllProjectsName allProjectsName, GitRepositoryManager repoManager, MetaDataUpdate.Server metaDataUpdateFactory, ProjectCache projectCache, ProjectConfig.Factory projectConfigFactory, ProjectCreator projectCreator)
name|ProjectOperationsImpl
parameter_list|(
name|AllProjectsName
name|allProjectsName
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
parameter_list|,
name|ProjectCreator
name|projectCreator
parameter_list|)
block|{
name|this
operator|.
name|allProjectsName
operator|=
name|allProjectsName
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|projectConfigFactory
operator|=
name|projectConfigFactory
expr_stmt|;
name|this
operator|.
name|projectCreator
operator|=
name|projectCreator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newProject ()
specifier|public
name|Builder
name|newProject
parameter_list|()
block|{
return|return
name|TestProjectCreation
operator|.
name|builder
argument_list|(
name|this
operator|::
name|createNewProject
argument_list|)
return|;
block|}
DECL|method|createNewProject (TestProjectCreation projectCreation)
specifier|private
name|Project
operator|.
name|NameKey
name|createNewProject
parameter_list|(
name|TestProjectCreation
name|projectCreation
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
name|projectCreation
operator|.
name|name
argument_list|()
operator|.
name|orElse
argument_list|(
name|RandomStringUtils
operator|.
name|randomAlphabetic
argument_list|(
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|CreateProjectArgs
name|args
init|=
operator|new
name|CreateProjectArgs
argument_list|()
decl_stmt|;
name|args
operator|.
name|setProjectName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|args
operator|.
name|branch
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Constants
operator|.
name|R_HEADS
operator|+
name|Constants
operator|.
name|MASTER
argument_list|)
expr_stmt|;
name|args
operator|.
name|createEmptyCommit
operator|=
name|projectCreation
operator|.
name|createEmptyCommit
argument_list|()
operator|.
name|orElse
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|projectCreation
operator|.
name|parent
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|p
lambda|->
name|args
operator|.
name|newParent
operator|=
name|p
argument_list|)
expr_stmt|;
comment|// ProjectCreator wants non-null owner IDs.
name|args
operator|.
name|ownerIds
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|projectCreation
operator|.
name|submitType
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|st
lambda|->
name|args
operator|.
name|submitType
operator|=
name|st
argument_list|)
expr_stmt|;
name|projectCreator
operator|.
name|createProject
argument_list|(
name|args
argument_list|)
expr_stmt|;
return|return
name|Project
operator|.
name|nameKey
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|project (Project.NameKey key)
specifier|public
name|ProjectOperations
operator|.
name|PerProjectOperations
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|key
parameter_list|)
block|{
return|return
operator|new
name|PerProjectOperations
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|allProjectsForUpdate ()
specifier|public
name|TestProjectUpdate
operator|.
name|Builder
name|allProjectsForUpdate
parameter_list|()
block|{
return|return
name|project
argument_list|(
name|allProjectsName
argument_list|)
operator|.
name|forUpdate
argument_list|()
return|;
block|}
DECL|class|PerProjectOperations
specifier|private
class|class
name|PerProjectOperations
implements|implements
name|ProjectOperations
operator|.
name|PerProjectOperations
block|{
DECL|field|nameKey
name|Project
operator|.
name|NameKey
name|nameKey
decl_stmt|;
DECL|method|PerProjectOperations (Project.NameKey nameKey)
name|PerProjectOperations
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
name|this
operator|.
name|nameKey
operator|=
name|nameKey
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHead (String branch)
specifier|public
name|RevCommit
name|getHead
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
return|return
name|requireNonNull
argument_list|(
name|headOrNull
argument_list|(
name|branch
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hasHead (String branch)
specifier|public
name|boolean
name|hasHead
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
return|return
name|headOrNull
argument_list|(
name|branch
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|forUpdate ()
specifier|public
name|TestProjectUpdate
operator|.
name|Builder
name|forUpdate
parameter_list|()
block|{
return|return
name|TestProjectUpdate
operator|.
name|builder
argument_list|(
name|nameKey
argument_list|,
name|allProjectsName
argument_list|,
name|this
operator|::
name|updateProject
argument_list|)
return|;
block|}
DECL|method|updateProject (TestProjectUpdate projectUpdate)
specifier|private
name|void
name|updateProject
parameter_list|(
name|TestProjectUpdate
name|projectUpdate
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
try|try
init|(
name|MetaDataUpdate
name|metaDataUpdate
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|nameKey
argument_list|)
init|)
block|{
name|ProjectConfig
name|projectConfig
init|=
name|projectConfigFactory
operator|.
name|read
argument_list|(
name|metaDataUpdate
argument_list|)
decl_stmt|;
name|removePermissions
argument_list|(
name|projectConfig
argument_list|,
name|projectUpdate
operator|.
name|removedPermissions
argument_list|()
argument_list|)
expr_stmt|;
name|addCapabilities
argument_list|(
name|projectConfig
argument_list|,
name|projectUpdate
operator|.
name|addedCapabilities
argument_list|()
argument_list|)
expr_stmt|;
name|addPermissions
argument_list|(
name|projectConfig
argument_list|,
name|projectUpdate
operator|.
name|addedPermissions
argument_list|()
argument_list|)
expr_stmt|;
name|addLabelPermissions
argument_list|(
name|projectConfig
argument_list|,
name|projectUpdate
operator|.
name|addedLabelPermissions
argument_list|()
argument_list|)
expr_stmt|;
name|setExclusiveGroupPermissions
argument_list|(
name|projectConfig
argument_list|,
name|projectUpdate
operator|.
name|exclusiveGroupPermissions
argument_list|()
argument_list|)
expr_stmt|;
name|projectConfig
operator|.
name|commit
argument_list|(
name|metaDataUpdate
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
block|}
DECL|method|removePermissions ( ProjectConfig projectConfig, ImmutableList<TestProjectUpdate.TestPermissionKey> removedPermissions)
specifier|private
name|void
name|removePermissions
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|,
name|ImmutableList
argument_list|<
name|TestProjectUpdate
operator|.
name|TestPermissionKey
argument_list|>
name|removedPermissions
parameter_list|)
block|{
for|for
control|(
name|TestProjectUpdate
operator|.
name|TestPermissionKey
name|p
range|:
name|removedPermissions
control|)
block|{
name|Permission
name|permission
init|=
name|projectConfig
operator|.
name|getAccessSection
argument_list|(
name|p
operator|.
name|section
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|p
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|group
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|GroupReference
name|group
init|=
operator|new
name|GroupReference
argument_list|(
name|p
operator|.
name|group
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|p
operator|.
name|group
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|group
operator|=
name|projectConfig
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|permission
operator|.
name|removeRule
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|permission
operator|.
name|clearRules
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|addCapabilities ( ProjectConfig projectConfig, ImmutableList<TestCapability> addedCapabilities)
specifier|private
name|void
name|addCapabilities
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|,
name|ImmutableList
argument_list|<
name|TestCapability
argument_list|>
name|addedCapabilities
parameter_list|)
block|{
for|for
control|(
name|TestCapability
name|c
range|:
name|addedCapabilities
control|)
block|{
name|PermissionRule
name|rule
init|=
name|newRule
argument_list|(
name|projectConfig
argument_list|,
name|c
operator|.
name|group
argument_list|()
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setRange
argument_list|(
name|c
operator|.
name|min
argument_list|()
argument_list|,
name|c
operator|.
name|max
argument_list|()
argument_list|)
expr_stmt|;
name|projectConfig
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addPermissions ( ProjectConfig projectConfig, ImmutableList<TestPermission> addedPermissions)
specifier|private
name|void
name|addPermissions
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|,
name|ImmutableList
argument_list|<
name|TestPermission
argument_list|>
name|addedPermissions
parameter_list|)
block|{
for|for
control|(
name|TestPermission
name|p
range|:
name|addedPermissions
control|)
block|{
name|PermissionRule
name|rule
init|=
name|newRule
argument_list|(
name|projectConfig
argument_list|,
name|p
operator|.
name|group
argument_list|()
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setAction
argument_list|(
name|p
operator|.
name|action
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setForce
argument_list|(
name|p
operator|.
name|force
argument_list|()
argument_list|)
expr_stmt|;
name|projectConfig
operator|.
name|getAccessSection
argument_list|(
name|p
operator|.
name|ref
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|p
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addLabelPermissions ( ProjectConfig projectConfig, ImmutableList<TestLabelPermission> addedLabelPermissions)
specifier|private
name|void
name|addLabelPermissions
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|,
name|ImmutableList
argument_list|<
name|TestLabelPermission
argument_list|>
name|addedLabelPermissions
parameter_list|)
block|{
for|for
control|(
name|TestLabelPermission
name|p
range|:
name|addedLabelPermissions
control|)
block|{
name|PermissionRule
name|rule
init|=
name|newRule
argument_list|(
name|projectConfig
argument_list|,
name|p
operator|.
name|group
argument_list|()
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setAction
argument_list|(
name|p
operator|.
name|action
argument_list|()
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setRange
argument_list|(
name|p
operator|.
name|min
argument_list|()
argument_list|,
name|p
operator|.
name|max
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|permissionName
init|=
name|p
operator|.
name|impersonation
argument_list|()
condition|?
name|Permission
operator|.
name|forLabelAs
argument_list|(
name|p
operator|.
name|name
argument_list|()
argument_list|)
else|:
name|Permission
operator|.
name|forLabel
argument_list|(
name|p
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|Permission
name|permission
init|=
name|projectConfig
operator|.
name|getAccessSection
argument_list|(
name|p
operator|.
name|ref
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|permission
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setExclusiveGroupPermissions ( ProjectConfig projectConfig, ImmutableMap<TestProjectUpdate.TestPermissionKey, Boolean> exclusiveGroupPermissions)
specifier|private
name|void
name|setExclusiveGroupPermissions
parameter_list|(
name|ProjectConfig
name|projectConfig
parameter_list|,
name|ImmutableMap
argument_list|<
name|TestProjectUpdate
operator|.
name|TestPermissionKey
argument_list|,
name|Boolean
argument_list|>
name|exclusiveGroupPermissions
parameter_list|)
block|{
name|exclusiveGroupPermissions
operator|.
name|forEach
argument_list|(
parameter_list|(
name|key
parameter_list|,
name|exclusive
parameter_list|)
lambda|->
name|projectConfig
operator|.
name|getAccessSection
argument_list|(
name|key
operator|.
name|section
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|getPermission
argument_list|(
name|key
operator|.
name|name
argument_list|()
argument_list|,
literal|true
argument_list|)
operator|.
name|setExclusiveGroup
argument_list|(
name|exclusive
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|headOrNull (String branch)
specifier|private
name|RevCommit
name|headOrNull
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
if|if
condition|(
operator|!
name|branch
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_REFS
argument_list|)
condition|)
block|{
name|branch
operator|=
name|RefNames
operator|.
name|REFS_HEADS
operator|+
name|branch
expr_stmt|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|nameKey
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|Ref
name|r
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|branch
argument_list|)
decl_stmt|;
return|return
name|r
operator|==
literal|null
condition|?
literal|null
else|:
name|rw
operator|.
name|parseCommit
argument_list|(
name|r
operator|.
name|getObjectId
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getProjectConfig ()
specifier|public
name|ProjectConfig
name|getProjectConfig
parameter_list|()
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|nameKey
argument_list|)
init|)
block|{
name|ProjectConfig
name|projectConfig
init|=
name|projectConfigFactory
operator|.
name|create
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
name|projectConfig
operator|.
name|load
argument_list|(
name|nameKey
argument_list|,
name|repo
argument_list|)
expr_stmt|;
return|return
name|projectConfig
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getConfig ()
specifier|public
name|Config
name|getConfig
parameter_list|()
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|nameKey
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|REFS_CONFIG
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Config
argument_list|()
return|;
block|}
name|RevTree
name|tree
init|=
name|rw
operator|.
name|parseTree
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|PROJECT_CONFIG
argument_list|,
name|tree
argument_list|)
decl_stmt|;
if|if
condition|(
name|tw
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Config
argument_list|()
return|;
block|}
name|ObjectLoader
name|loader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|open
argument_list|(
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|text
init|=
operator|new
name|String
argument_list|(
name|loader
operator|.
name|getCachedBytes
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|fromText
argument_list|(
name|text
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|newRule (ProjectConfig project, AccountGroup.UUID groupUUID)
specifier|private
specifier|static
name|PermissionRule
name|newRule
parameter_list|(
name|ProjectConfig
name|project
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|groupUUID
parameter_list|)
block|{
name|GroupReference
name|group
init|=
operator|new
name|GroupReference
argument_list|(
name|groupUUID
argument_list|,
name|groupUUID
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|group
operator|=
name|project
operator|.
name|resolve
argument_list|(
name|group
argument_list|)
expr_stmt|;
return|return
operator|new
name|PermissionRule
argument_list|(
name|group
argument_list|)
return|;
block|}
block|}
end_class

end_unit

