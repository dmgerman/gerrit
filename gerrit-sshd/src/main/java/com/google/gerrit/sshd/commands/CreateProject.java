begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

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
name|CollectionsUtil
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
name|Project
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
name|GroupCache
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
name|ProjectCreatorGroups
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
name|ProjectOwnerGroups
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
name|ReplicationQueue
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
name|sshd
operator|.
name|BaseCommand
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Environment
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
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
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
name|io
operator|.
name|PrintWriter
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
name|HashSet
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

begin_comment
comment|/** Create a new project. **/
end_comment

begin_class
DECL|class|CreateProject
specifier|final
class|class
name|CreateProject
extends|extends
name|BaseCommand
block|{
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--name"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|aliases
operator|=
block|{
literal|"-n"
block|}
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"name of project to be created"
argument_list|)
DECL|field|projectName
specifier|private
name|String
name|projectName
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--owner"
argument_list|,
name|aliases
operator|=
block|{
literal|"-o"
block|}
argument_list|,
name|usage
operator|=
literal|"owner(s) of project"
argument_list|)
DECL|field|ownerIds
specifier|private
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ownerIds
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--parent"
argument_list|,
name|aliases
operator|=
block|{
literal|"-p"
block|}
argument_list|,
name|metaVar
operator|=
literal|"NAME"
argument_list|,
name|usage
operator|=
literal|"parent project"
argument_list|)
DECL|field|newParent
specifier|private
name|ProjectControl
name|newParent
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--permissions-only"
argument_list|,
name|usage
operator|=
literal|"create project for use only as parent"
argument_list|)
DECL|field|permissionsOnly
specifier|private
name|boolean
name|permissionsOnly
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--description"
argument_list|,
name|aliases
operator|=
block|{
literal|"-d"
block|}
argument_list|,
name|metaVar
operator|=
literal|"DESC"
argument_list|,
name|usage
operator|=
literal|"description of project"
argument_list|)
DECL|field|projectDescription
specifier|private
name|String
name|projectDescription
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--submit-type"
argument_list|,
name|aliases
operator|=
block|{
literal|"-t"
block|}
argument_list|,
name|usage
operator|=
literal|"project submit type\n"
operator|+
literal|"(default: MERGE_IF_NECESSARY)"
argument_list|)
DECL|field|submitType
specifier|private
name|SubmitType
name|submitType
init|=
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-contributor-agreements"
argument_list|,
name|aliases
operator|=
block|{
literal|"--ca"
block|}
argument_list|,
name|usage
operator|=
literal|"if contributor agreement is required"
argument_list|)
DECL|field|contributorAgreements
specifier|private
name|boolean
name|contributorAgreements
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-signed-off-by"
argument_list|,
name|aliases
operator|=
block|{
literal|"--so"
block|}
argument_list|,
name|usage
operator|=
literal|"if signed-off-by is required"
argument_list|)
DECL|field|signedOffBy
specifier|private
name|boolean
name|signedOffBy
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--use-content-merge"
argument_list|,
name|usage
operator|=
literal|"allow automatic conflict resolving within files"
argument_list|)
DECL|field|contentMerge
specifier|private
name|boolean
name|contentMerge
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--require-change-id"
argument_list|,
name|aliases
operator|=
block|{
literal|"--id"
block|}
argument_list|,
name|usage
operator|=
literal|"if change-id is required"
argument_list|)
DECL|field|requireChangeID
specifier|private
name|boolean
name|requireChangeID
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--branch"
argument_list|,
name|aliases
operator|=
block|{
literal|"-b"
block|}
argument_list|,
name|metaVar
operator|=
literal|"BRANCH"
argument_list|,
name|usage
operator|=
literal|"initial branch name\n"
operator|+
literal|"(default: master)"
argument_list|)
DECL|field|branch
specifier|private
name|String
name|branch
init|=
name|Constants
operator|.
name|MASTER
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--empty-commit"
argument_list|,
name|usage
operator|=
literal|"to create initial empty commit"
argument_list|)
DECL|field|createEmptyCommit
specifier|private
name|boolean
name|createEmptyCommit
decl_stmt|;
annotation|@
name|Inject
DECL|field|repoManager
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectCache
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|field|groupCache
specifier|private
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ProjectCreatorGroups
DECL|field|projectCreatorGroups
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|projectCreatorGroups
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|ProjectOwnerGroups
DECL|field|projectOwnerGroups
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|projectOwnerGroups
decl_stmt|;
annotation|@
name|Inject
DECL|field|currentUser
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
annotation|@
name|Inject
DECL|field|rq
specifier|private
name|ReplicationQueue
name|rq
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|GerritPersonIdent
DECL|field|serverIdent
specifier|private
name|PersonIdent
name|serverIdent
decl_stmt|;
annotation|@
name|Inject
DECL|field|metaDataUpdateFactory
name|MetaDataUpdate
operator|.
name|User
name|metaDataUpdateFactory
decl_stmt|;
DECL|field|nameKey
specifier|private
name|Project
operator|.
name|NameKey
name|nameKey
decl_stmt|;
annotation|@
name|Override
DECL|method|start (final Environment env)
specifier|public
name|void
name|start
parameter_list|(
specifier|final
name|Environment
name|env
parameter_list|)
block|{
name|startThread
argument_list|(
operator|new
name|CommandRunnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|PrintWriter
name|p
init|=
name|toPrintWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|parseCommandLine
argument_list|()
expr_stmt|;
try|try
block|{
name|validateParameters
argument_list|()
expr_stmt|;
name|nameKey
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|String
name|head
init|=
name|permissionsOnly
condition|?
name|GitRepositoryManager
operator|.
name|REF_CONFIG
else|:
name|branch
decl_stmt|;
specifier|final
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
try|try
block|{
name|rq
operator|.
name|replicateNewProject
argument_list|(
name|nameKey
argument_list|,
name|head
argument_list|)
expr_stmt|;
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
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|permissionsOnly
operator|&&
name|createEmptyCommit
condition|)
block|{
name|createEmptyCommit
argument_list|(
name|repo
argument_list|,
name|nameKey
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|p
operator|.
name|print
argument_list|(
literal|"Error when trying to create project: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
name|p
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createEmptyCommit (final Repository repo, final Project.NameKey project, final String ref)
specifier|private
name|void
name|createEmptyCommit
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|ref
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectInserter
name|oi
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
decl_stmt|;
try|try
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
name|RefUpdate
name|ru
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
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|id
argument_list|)
expr_stmt|;
specifier|final
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
name|rq
operator|.
name|scheduleUpdate
argument_list|(
name|project
argument_list|,
name|ref
argument_list|)
expr_stmt|;
break|break;
default|default:
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|result
operator|.
name|name
argument_list|()
argument_list|)
throw|;
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
name|projectName
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|oi
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createProjectConfig ()
specifier|private
name|void
name|createProjectConfig
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
try|try
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
name|config
operator|.
name|load
argument_list|(
name|md
argument_list|)
expr_stmt|;
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
name|projectDescription
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setSubmitType
argument_list|(
name|submitType
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseContributorAgreements
argument_list|(
name|contributorAgreements
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseSignedOffBy
argument_list|(
name|signedOffBy
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setUseContentMerge
argument_list|(
name|contentMerge
argument_list|)
expr_stmt|;
name|newProject
operator|.
name|setRequireChangeID
argument_list|(
name|requireChangeID
argument_list|)
expr_stmt|;
if|if
condition|(
name|newParent
operator|!=
literal|null
condition|)
block|{
name|newProject
operator|.
name|setParentName
argument_list|(
name|newParent
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
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
name|ownerIds
control|)
block|{
name|AccountGroup
name|accountGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|ownerId
argument_list|)
decl_stmt|;
name|GroupReference
name|group
init|=
name|config
operator|.
name|resolve
argument_list|(
name|accountGroup
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
name|md
operator|.
name|setMessage
argument_list|(
literal|"Created project\n"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create "
operator|+
name|projectName
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|projectCache
operator|.
name|onCreateProject
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
name|repoManager
operator|.
name|setProjectDescription
argument_list|(
name|nameKey
argument_list|,
name|projectDescription
argument_list|)
expr_stmt|;
name|rq
operator|.
name|scheduleUpdate
argument_list|(
name|nameKey
argument_list|,
name|GitRepositoryManager
operator|.
name|REF_CONFIG
argument_list|)
expr_stmt|;
block|}
DECL|method|validateParameters ()
specifier|private
name|void
name|validateParameters
parameter_list|()
throws|throws
name|Failure
block|{
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
comment|//
name|projectName
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|CollectionsUtil
operator|.
name|isAnyIncludedIn
argument_list|(
name|currentUser
operator|.
name|getEffectiveGroups
argument_list|()
argument_list|,
name|projectCreatorGroups
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: Not permitted to create "
operator|+
name|projectName
argument_list|)
throw|;
block|}
if|if
condition|(
name|ownerIds
operator|!=
literal|null
operator|&&
operator|!
name|ownerIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ownerIds
operator|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|(
operator|new
name|HashSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|(
name|ownerIds
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ownerIds
operator|=
operator|new
name|ArrayList
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
argument_list|(
name|projectOwnerGroups
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
name|branch
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
condition|)
block|{
name|branch
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
name|branch
expr_stmt|;
block|}
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
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"--branch \""
operator|+
name|branch
operator|+
literal|"\" is not a valid name"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

