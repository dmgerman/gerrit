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
name|flogger
operator|.
name|FluentLogger
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
name|entities
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
name|entities
operator|.
name|BooleanProjectConfig
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
name|entities
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
name|entities
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
name|git
operator|.
name|LockFailureException
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
name|IdentifiedUser
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
name|AbstractNoNotifyEvent
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
name|List
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

begin_class
DECL|class|ProjectCreator
specifier|public
class|class
name|ProjectCreator
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|createdListeners
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|NewProjectCreatedListener
argument_list|>
name|createdListeners
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
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
decl_stmt|;
DECL|field|identifiedUser
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
decl_stmt|;
DECL|field|projectConfigFactory
specifier|private
specifier|final
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectCreator ( GitRepositoryManager repoManager, PluginSetContext<NewProjectCreatedListener> createdListeners, ProjectCache projectCache, GroupBackend groupBackend, MetaDataUpdate.User metaDataUpdateFactory, GitReferenceUpdated referenceUpdated, RepositoryConfig repositoryCfg, @GerritPersonIdent Provider<PersonIdent> serverIdent, Provider<IdentifiedUser> identifiedUser, ProjectConfig.Factory projectConfigFactory)
name|ProjectCreator
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|PluginSetContext
argument_list|<
name|NewProjectCreatedListener
argument_list|>
name|createdListeners
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GroupBackend
name|groupBackend
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
name|Provider
argument_list|<
name|PersonIdent
argument_list|>
name|serverIdent
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|identifiedUser
parameter_list|,
name|ProjectConfig
operator|.
name|Factory
name|projectConfigFactory
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|createdListeners
operator|=
name|createdListeners
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
name|identifiedUser
operator|=
name|identifiedUser
expr_stmt|;
name|this
operator|.
name|projectConfigFactory
operator|=
name|projectConfigFactory
expr_stmt|;
block|}
DECL|method|createProject (CreateProjectArgs args)
specifier|public
name|ProjectState
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
name|fire
argument_list|(
name|nameKey
argument_list|,
name|head
argument_list|)
expr_stmt|;
return|return
name|projectCache
operator|.
name|get
argument_list|(
name|nameKey
argument_list|)
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
name|msg
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
name|projectConfigFactory
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
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTRIBUTOR_AGREEMENTS
argument_list|,
name|args
operator|.
name|contributorAgreements
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_SIGNED_OFF_BY
argument_list|,
name|args
operator|.
name|signedOffBy
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|USE_CONTENT_MERGE
argument_list|,
name|args
operator|.
name|contentMerge
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|CREATE_NEW_CHANGE_FOR_ALL_NOT_IN_TARGET
argument_list|,
name|args
operator|.
name|newChangeForAllNotInTarget
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_CHANGE_ID
argument_list|,
name|args
operator|.
name|changeIdRequired
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REJECT_EMPTY_COMMIT
argument_list|,
name|args
operator|.
name|rejectEmptyCommit
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
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|ENABLE_SIGNED_PUSH
argument_list|,
name|args
operator|.
name|enableSignedPush
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setBooleanConfig
argument_list|(
name|BooleanProjectConfig
operator|.
name|REQUIRE_SIGNED_PUSH
argument_list|,
name|args
operator|.
name|requireSignedPush
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
name|md
operator|.
name|getRepository
argument_list|()
operator|.
name|setGitwebDescription
argument_list|(
name|args
operator|.
name|projectDescription
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
operator|.
name|get
argument_list|()
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
name|identifiedUser
operator|.
name|get
argument_list|()
operator|.
name|state
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|LOCK_FAILURE
case|:
throw|throw
operator|new
name|LockFailureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to create ref \"%s\""
argument_list|,
name|ref
argument_list|)
argument_list|,
name|ru
argument_list|)
throw|;
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
case|case
name|REJECTED_MISSING_OBJECT
case|:
case|case
name|REJECTED_OTHER_REASON
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
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Cannot create empty commit for %s"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|fire (Project.NameKey name, String head)
specifier|private
name|void
name|fire
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|,
name|String
name|head
parameter_list|)
block|{
if|if
condition|(
name|createdListeners
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|ProjectCreator
operator|.
name|Event
name|event
init|=
operator|new
name|ProjectCreator
operator|.
name|Event
argument_list|(
name|name
argument_list|,
name|head
argument_list|)
decl_stmt|;
name|createdListeners
operator|.
name|runEach
argument_list|(
name|l
lambda|->
name|l
operator|.
name|onNewProjectCreated
argument_list|(
name|event
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|Event
specifier|static
class|class
name|Event
extends|extends
name|AbstractNoNotifyEvent
implements|implements
name|NewProjectCreatedListener
operator|.
name|Event
block|{
DECL|field|name
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|name
decl_stmt|;
DECL|field|head
specifier|private
specifier|final
name|String
name|head
decl_stmt|;
DECL|method|Event (Project.NameKey name, String head)
name|Event
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|,
name|String
name|head
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|head
operator|=
name|head
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|name
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getHeadName ()
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
block|}
end_class

end_unit

