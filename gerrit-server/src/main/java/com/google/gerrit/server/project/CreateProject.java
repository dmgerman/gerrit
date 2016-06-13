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
name|GroupDescription
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
name|events
operator|.
name|NewProjectCreatedListener
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
name|DynamicSet
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
name|RestModifyView
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
name|extensions
operator|.
name|restapi
operator|.
name|UnprocessableEntityException
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
name|GerritPersonIdent
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
name|config
operator|.
name|RepositoryConfig
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|git
operator|.
name|RepositoryCaseMismatchException
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
name|GroupsCollection
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
name|assistedinject
operator|.
name|Assisted
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|CommitBuilder
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
name|ObjectId
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
name|ObjectInserter
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
name|PersonIdent
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
name|RefUpdate
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
name|RefUpdate
operator|.
name|Result
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
name|transport
operator|.
name|ReceiveCommand
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

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|CREATE_PROJECT
argument_list|)
DECL|class|CreateProject
specifier|public
class|class
name|CreateProject
implements|implements
name|RestModifyView
argument_list|<
name|TopLevelResource
argument_list|,
name|ProjectInput
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String name)
name|CreateProject
name|create
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CreateProject
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|projectsCollection
specifier|private
specifier|final
name|Provider
argument_list|<
name|ProjectsCollection
argument_list|>
name|projectsCollection
decl_stmt|;
DECL|field|groupsCollection
specifier|private
specifier|final
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
name|groupsCollection
decl_stmt|;
DECL|field|projectCreationValidationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
name|projectCreationValidationListeners
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ProjectJson
name|json
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|createdListener
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|NewProjectCreatedListener
argument_list|>
name|createdListener
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|groupBackend
specifier|private
specifier|final
name|GroupBackend
name|groupBackend
decl_stmt|;
DECL|field|projectOwnerGroups
specifier|private
specifier|final
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
decl_stmt|;
DECL|field|metaDataUpdateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|referenceUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|referenceUpdated
decl_stmt|;
DECL|field|repositoryCfg
specifier|private
specifier|final
name|RepositoryConfig
name|repositoryCfg
decl_stmt|;
DECL|field|serverIdent
specifier|private
specifier|final
name|PersonIdent
name|serverIdent
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
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
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateProject (Provider<ProjectsCollection> projectsCollection, Provider<GroupsCollection> groupsCollection, ProjectJson json, DynamicSet<ProjectCreationValidationListener> projectCreationValidationListeners, ProjectControl.GenericFactory projectControlFactory, GitRepositoryManager repoManager, DynamicSet<NewProjectCreatedListener> createdListener, ProjectCache projectCache, GroupBackend groupBackend, ProjectOwnerGroupsProvider.Factory projectOwnerGroups, MetaDataUpdate.User metaDataUpdateFactory, GitReferenceUpdated referenceUpdated, RepositoryConfig repositoryCfg, @GerritPersonIdent PersonIdent serverIdent, Provider<CurrentUser> currentUser, Provider<PutConfig> putConfig, AllProjectsName allProjects, @Assisted String name)
name|CreateProject
parameter_list|(
name|Provider
argument_list|<
name|ProjectsCollection
argument_list|>
name|projectsCollection
parameter_list|,
name|Provider
argument_list|<
name|GroupsCollection
argument_list|>
name|groupsCollection
parameter_list|,
name|ProjectJson
name|json
parameter_list|,
name|DynamicSet
argument_list|<
name|ProjectCreationValidationListener
argument_list|>
name|projectCreationValidationListeners
parameter_list|,
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|DynamicSet
argument_list|<
name|NewProjectCreatedListener
argument_list|>
name|createdListener
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GroupBackend
name|groupBackend
parameter_list|,
name|ProjectOwnerGroupsProvider
operator|.
name|Factory
name|projectOwnerGroups
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
parameter_list|,
name|GitReferenceUpdated
name|referenceUpdated
parameter_list|,
name|RepositoryConfig
name|repositoryCfg
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|currentUser
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
annotation|@
name|Assisted
name|String
name|name
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
name|groupsCollection
operator|=
name|groupsCollection
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
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|createdListener
operator|=
name|createdListener
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|groupBackend
operator|=
name|groupBackend
expr_stmt|;
name|this
operator|.
name|projectOwnerGroups
operator|=
name|projectOwnerGroups
expr_stmt|;
name|this
operator|.
name|metaDataUpdateFactory
operator|=
name|metaDataUpdateFactory
expr_stmt|;
name|this
operator|.
name|referenceUpdated
operator|=
name|referenceUpdated
expr_stmt|;
name|this
operator|.
name|repositoryCfg
operator|=
name|repositoryCfg
expr_stmt|;
name|this
operator|.
name|serverIdent
operator|=
name|serverIdent
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
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
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (TopLevelResource resource, ProjectInput input)
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
name|ProjectInput
name|input
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|UnprocessableEntityException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
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
name|stripGitSuffix
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
argument_list|)
operator|.
name|getControl
argument_list|()
expr_stmt|;
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
name|groupsCollection
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
for|for
control|(
name|ProjectCreationValidationListener
name|l
range|:
name|projectCreationValidationListeners
control|)
block|{
try|try
block|{
name|l
operator|.
name|validateNewProject
argument_list|(
name|args
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
block|}
name|Project
name|p
init|=
name|createProject
argument_list|(
name|args
argument_list|)
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|pluginConfigValues
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|ProjectControl
name|projectControl
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|p
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|currentUser
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|PutConfig
operator|.
name|Input
name|in
init|=
operator|new
name|PutConfig
operator|.
name|Input
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
name|putConfig
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|projectControl
argument_list|,
name|in
argument_list|)
expr_stmt|;
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
name|p
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
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
name|p
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createProject (CreateProjectArgs args)
specifier|public
name|Project
name|createProject
parameter_list|(
name|CreateProjectArgs
name|args
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|nameKey
init|=
name|args
operator|.
name|getProject
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|head
init|=
name|args
operator|.
name|permissionsOnly
condition|?
name|RefNames
operator|.
name|REFS_CONFIG
else|:
name|args
operator|.
name|branch
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|repo
operator|.
name|getObjectDatabase
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"project \""
operator|+
name|nameKey
operator|+
literal|"\" exists"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
comment|// It does not exist, safe to ignore.
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|nameKey
argument_list|)
init|)
block|{
name|RefUpdate
name|u
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|Constants
operator|.
name|HEAD
argument_list|)
decl_stmt|;
name|u
operator|.
name|disableRefLog
argument_list|()
expr_stmt|;
name|u
operator|.
name|link
argument_list|(
name|head
argument_list|)
expr_stmt|;
name|createProjectConfig
argument_list|(
name|args
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|args
operator|.
name|permissionsOnly
operator|&&
name|args
operator|.
name|createEmptyCommit
condition|)
block|{
name|createEmptyCommits
argument_list|(
name|repo
argument_list|,
name|nameKey
argument_list|,
name|args
operator|.
name|branch
argument_list|)
expr_stmt|;
block|}
name|NewProjectCreatedListener
operator|.
name|Event
name|event
init|=
operator|new
name|NewProjectCreatedListener
operator|.
name|Event
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|nameKey
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getHeadName
parameter_list|()
block|{
return|return
name|head
return|;
block|}
block|}
decl_stmt|;
for|for
control|(
name|NewProjectCreatedListener
name|l
range|:
name|createdListener
control|)
block|{
try|try
block|{
name|l
operator|.
name|onNewProjectCreated
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failure in NewProjectCreatedListener"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
operator|.
name|getProject
argument_list|()
return|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryCaseMismatchException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Cannot create "
operator|+
name|nameKey
operator|.
name|get
argument_list|()
operator|+
literal|" because the name is already occupied by another project."
operator|+
literal|" The other project has the same name, only spelled in a"
operator|+
literal|" different case."
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|badName
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"invalid project name: "
operator|+
name|nameKey
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
literal|"Cannot create "
operator|+
name|nameKey
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|createProjectConfig (CreateProjectArgs args)
specifier|private
name|void
name|createProjectConfig
parameter_list|(
name|CreateProjectArgs
name|args
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
try|try
init|(
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
init|)
block|{
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|newProject
init|=
name|config
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|newProject
operator|.
name|setDescription
argument_list|(
name|args
operator|.
name|projectDescription
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setSubmitType
argument_list|(
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|args
operator|.
name|submitType
argument_list|,
name|repositoryCfg
operator|.
name|getDefaultSubmitType
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseContributorAgreements
argument_list|(
name|args
operator|.
name|contributorAgreements
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseSignedOffBy
argument_list|(
name|args
operator|.
name|signedOffBy
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseContentMerge
argument_list|(
name|args
operator|.
name|contentMerge
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setCreateNewChangeForAllNotInTarget
argument_list|(
name|args
operator|.
name|newChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setRequireChangeID
argument_list|(
name|args
operator|.
name|changeIdRequired
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|args
operator|.
name|maxObjectSizeLimit
argument_list|)
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|newParent
operator|!=
literal|null
condition|)
block|{
name|newProject
operator|.
name|setParentName
argument_list|(
name|args
operator|.
name|newParent
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|args
operator|.
name|ownerIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|AccessSection
name|all
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
name|AccessSection
operator|.
name|ALL
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|ownerId
range|:
name|args
operator|.
name|ownerIds
control|)
block|{
name|GroupDescription
operator|.
name|Basic
name|g
init|=
name|groupBackend
operator|.
name|get
argument_list|(
name|ownerId
argument_list|)
decl_stmt|;
if|if
condition|(
name|g
operator|!=
literal|null
condition|)
block|{
name|GroupReference
name|group
init|=
name|config
operator|.
name|resolve
argument_list|(
name|GroupReference
operator|.
name|forGroup
argument_list|(
name|g
argument_list|)
argument_list|)
decl_stmt|;
name|all
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|OWNER
argument_list|,
literal|true
argument_list|)
operator|.
name|add
argument_list|(
operator|new
name|PermissionRule
argument_list|(
name|group
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|md
operator|.
name|setMessage
argument_list|(
literal|"Created project\n"
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|onCreateProject
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
name|repoManager
operator|.
name|setProjectDescription
argument_list|(
name|args
operator|.
name|getProject
argument_list|()
argument_list|,
name|args
operator|.
name|projectDescription
argument_list|)
expr_stmt|;
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
DECL|method|createEmptyCommits (Repository repo, Project.NameKey project, List<String> refs)
specifier|private
name|void
name|createEmptyCommits
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ObjectInserter
name|oi
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|oi
operator|.
name|insert
argument_list|(
name|Constants
operator|.
name|OBJ_TREE
argument_list|,
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|metaDataUpdateFactory
operator|.
name|getUserPersonIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|serverIdent
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
literal|"Initial empty repository\n"
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|oi
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
decl_stmt|;
name|oi
operator|.
name|flush
argument_list|()
expr_stmt|;
for|for
control|(
name|String
name|ref
range|:
name|refs
control|)
block|{
name|RefUpdate
name|ru
init|=
name|repo
operator|.
name|updateRef
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|Result
name|result
init|=
name|ru
operator|.
name|update
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|result
condition|)
block|{
case|case
name|NEW
case|:
name|referenceUpdated
operator|.
name|fire
argument_list|(
name|project
argument_list|,
name|ru
argument_list|,
name|ReceiveCommand
operator|.
name|Type
operator|.
name|CREATE
argument_list|,
name|currentUser
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|FAST_FORWARD
case|:
case|case
name|FORCED
case|:
case|case
name|IO_FAILURE
case|:
case|case
name|LOCK_FAILURE
case|:
case|case
name|NOT_ATTEMPTED
case|:
case|case
name|NO_CHANGE
case|:
case|case
name|REJECTED
case|:
case|case
name|REJECTED_CURRENT_BRANCH
case|:
case|case
name|RENAMED
case|:
default|default:
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to create ref \"%s\": %s"
argument_list|,
name|ref
argument_list|,
name|result
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot create empty commit for "
operator|+
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
block|}
end_class

end_unit

