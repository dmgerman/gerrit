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
name|base
operator|.
name|MoreObjects
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
name|Strings
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
name|extensions
operator|.
name|annotations
operator|.
name|RequiresCapability
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
name|projects
operator|.
name|ConfigInput
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
name|projects
operator|.
name|ProjectInput
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
name|client
operator|.
name|InheritableBoolean
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
name|client
operator|.
name|SubmitType
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
name|BadRequestException
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
name|IdString
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
name|Response
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
name|RestApiException
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
name|RestCollectionCreateView
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
name|ProjectUtil
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
name|config
operator|.
name|AllUsersName
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
name|ProjectOwnerGroupsProvider
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
name|GroupResolver
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
name|plugincontext
operator|.
name|PluginItemContext
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|ProjectNameLockManager
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
name|validators
operator|.
name|ProjectCreationValidationListener
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
name|validators
operator|.
name|ValidationException
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
name|Collections
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
name|concurrent
operator|.
name|locks
operator|.
name|Lock
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
name|Repository
import|;
end_import

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
annotation|@
name|Singleton
DECL|class|CreateProject
specifier|public
class|class
name|CreateProject
implements|implements
name|RestCollectionCreateView
argument_list|<
name|TopLevelResource
argument_list|,
name|ProjectResource
argument_list|,
name|ProjectInput
argument_list|>
block|{
DECL|field|projectsCollection
specifier|private
specifier|final
name|Provider
argument_list|<
name|ProjectsCollection
argument_list|>
name|projectsCollection
decl_stmt|;
DECL|field|groupResolver
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupResolver
argument_list|>
name|groupResolver
decl_stmt|;
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
DECL|field|projectCreationValidationListeners
name|projectCreationValidationListeners
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ProjectJson
name|json
decl_stmt|;
DECL|field|projectOwnerGroups
specifier|private
specifier|final
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
decl_stmt|;
DECL|field|putConfig
specifier|private
specifier|final
name|Provider
argument_list|<
name|PutConfig
argument_list|>
name|putConfig
decl_stmt|;
DECL|field|allProjects
specifier|private
specifier|final
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|allUsers
specifier|private
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|lockManager
specifier|private
specifier|final
name|PluginItemContext
argument_list|<
name|ProjectNameLockManager
argument_list|>
name|lockManager
decl_stmt|;
DECL|field|projectCreator
specifier|private
specifier|final
name|ProjectCreator
name|projectCreator
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateProject ( ProjectCreator projectCreator, Provider<ProjectsCollection> projectsCollection, Provider<GroupResolver> groupResolver, ProjectJson json, PluginSetContext<ProjectCreationValidationListener> projectCreationValidationListeners, ProjectOwnerGroupsProvider.Factory projectOwnerGroups, Provider<PutConfig> putConfig, AllProjectsName allProjects, AllUsersName allUsers, PluginItemContext<ProjectNameLockManager> lockManager)
name|CreateProject
parameter_list|(
name|ProjectCreator
name|projectCreator
parameter_list|,
name|Provider
argument_list|<
name|ProjectsCollection
argument_list|>
name|projectsCollection
parameter_list|,
name|Provider
argument_list|<
name|GroupResolver
argument_list|>
name|groupResolver
parameter_list|,
name|ProjectJson
name|json
parameter_list|,
name|PluginSetContext
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
name|projectCreationValidationListeners
parameter_list|,
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
parameter_list|,
name|Provider
argument_list|<
name|PutConfig
argument_list|>
name|putConfig
parameter_list|,
name|AllProjectsName
name|allProjects
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|,
name|PluginItemContext
argument_list|<
name|ProjectNameLockManager
argument_list|>
name|lockManager
parameter_list|)
block|{
name|this
operator|.
name|projectsCollection
operator|=
name|projectsCollection
expr_stmt|;
name|this
operator|.
name|projectCreator
operator|=
name|projectCreator
expr_stmt|;
name|this
operator|.
name|groupResolver
operator|=
name|groupResolver
expr_stmt|;
name|this
operator|.
name|projectCreationValidationListeners
operator|=
name|projectCreationValidationListeners
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|projectOwnerGroups
operator|=
name|projectOwnerGroups
expr_stmt|;
name|this
operator|.
name|putConfig
operator|=
name|putConfig
expr_stmt|;
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
name|this
operator|.
name|lockManager
operator|=
name|lockManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource, IdString id, ProjectInput input)
specifier|public
name|Response
argument_list|<
name|ProjectInfo
argument_list|>
name|apply
parameter_list|(
name|TopLevelResource
name|resource
parameter_list|,
name|IdString
name|id
parameter_list|,
name|ProjectInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|String
name|name
init|=
name|id
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|ProjectInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|equals
argument_list|(
name|input
operator|.
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"name must match URL"
argument_list|)
throw|;
block|}
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
name|ProjectUtil
operator|.
name|sanitizeProjectName
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|parentName
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|parent
argument_list|)
argument_list|,
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|args
operator|.
name|newParent
operator|=
name|projectsCollection
operator|.
name|get
argument_list|()
operator|.
name|parse
argument_list|(
name|parentName
argument_list|,
literal|false
argument_list|)
operator|.
name|getNameKey
argument_list|()
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|newParent
operator|.
name|equals
argument_list|(
name|allUsers
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot inherit from '%s' project"
argument_list|,
name|allUsers
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|args
operator|.
name|createEmptyCommit
operator|=
name|input
operator|.
name|createEmptyCommit
expr_stmt|;
name|args
operator|.
name|permissionsOnly
operator|=
name|input
operator|.
name|permissionsOnly
expr_stmt|;
name|args
operator|.
name|projectDescription
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|description
argument_list|)
expr_stmt|;
name|args
operator|.
name|submitType
operator|=
name|input
operator|.
name|submitType
expr_stmt|;
name|args
operator|.
name|branch
operator|=
name|normalizeBranchNames
argument_list|(
name|input
operator|.
name|branches
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|owners
operator|==
literal|null
operator|||
name|input
operator|.
name|owners
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|args
operator|.
name|ownerIds
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|projectOwnerGroups
operator|.
name|create
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|args
operator|.
name|ownerIds
operator|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|input
operator|.
name|owners
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|owner
range|:
name|input
operator|.
name|owners
control|)
block|{
name|args
operator|.
name|ownerIds
operator|.
name|add
argument_list|(
name|groupResolver
operator|.
name|get
argument_list|()
operator|.
name|parse
argument_list|(
name|owner
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|args
operator|.
name|contributorAgreements
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|useContributorAgreements
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|signedOffBy
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|useSignedOffBy
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|contentMerge
operator|=
name|input
operator|.
name|submitType
operator|==
name|SubmitType
operator|.
name|FAST_FORWARD_ONLY
condition|?
name|InheritableBoolean
operator|.
name|FALSE
else|:
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|useContentMerge
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|newChangeForAllNotInTarget
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|createNewChangeForAllNotInTarget
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|changeIdRequired
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|requireChangeId
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|rejectEmptyCommit
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|rejectEmptyCommit
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|enableSignedPush
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|enableSignedPush
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
name|args
operator|.
name|requireSignedPush
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|input
operator|.
name|requireSignedPush
argument_list|,
name|InheritableBoolean
operator|.
name|INHERIT
argument_list|)
expr_stmt|;
try|try
block|{
name|args
operator|.
name|maxObjectSizeLimit
operator|=
name|ProjectConfig
operator|.
name|validMaxObjectSizeLimit
argument_list|(
name|input
operator|.
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|Lock
name|nameLock
init|=
name|lockManager
operator|.
name|call
argument_list|(
name|lockManager
lambda|->
name|lockManager
operator|.
name|getLock
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|nameLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
try|try
block|{
name|projectCreationValidationListeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|validateNewProject
argument_list|(
name|args
argument_list|)
argument_list|,
name|ValidationException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ValidationException
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
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|ProjectState
name|projectState
init|=
name|projectCreator
operator|.
name|createProject
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|requireNonNull
argument_list|(
name|projectState
argument_list|,
parameter_list|()
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"failed to create project %s"
argument_list|,
name|args
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|pluginConfigValues
operator|!=
literal|null
condition|)
block|{
name|ConfigInput
name|in
init|=
operator|new
name|ConfigInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|pluginConfigValues
operator|=
name|input
operator|.
name|pluginConfigValues
expr_stmt|;
name|in
operator|.
name|description
operator|=
name|args
operator|.
name|projectDescription
expr_stmt|;
name|putConfig
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|projectState
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|created
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|projectState
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|nameLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|normalizeBranchNames (List<String> branches)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|normalizeBranchNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|branches
parameter_list|)
throws|throws
name|BadRequestException
block|{
if|if
condition|(
name|branches
operator|==
literal|null
operator|||
name|branches
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
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
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|normalizedBranches
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|branch
range|:
name|branches
control|)
block|{
while|while
condition|(
name|branch
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|branch
operator|=
name|branch
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|branch
operator|=
name|RefNames
operator|.
name|fullName
argument_list|(
name|branch
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|branch
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Branch \"%s\" is not a valid name."
argument_list|,
name|branch
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|normalizedBranches
operator|.
name|contains
argument_list|(
name|branch
argument_list|)
condition|)
block|{
name|normalizedBranches
operator|.
name|add
argument_list|(
name|branch
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|normalizedBranches
return|;
block|}
block|}
end_class

end_unit

