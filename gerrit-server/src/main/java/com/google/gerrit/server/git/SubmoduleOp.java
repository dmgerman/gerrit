begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|HashMultimap
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
name|Multimap
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
name|ChangeHooks
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
name|Nullable
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
name|SubscribeSection
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
name|Account
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
name|Branch
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
name|reviewdb
operator|.
name|client
operator|.
name|SubmoduleSubscription
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
name|config
operator|.
name|GerritServerConfig
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
name|MergeOpRepoManager
operator|.
name|OpenRepo
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
name|ProjectState
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
name|eclipse
operator|.
name|jgit
operator|.
name|dircache
operator|.
name|DirCache
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
name|dircache
operator|.
name|DirCacheBuilder
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
name|dircache
operator|.
name|DirCacheEditor
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
name|dircache
operator|.
name|DirCacheEditor
operator|.
name|DeletePath
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
name|dircache
operator|.
name|DirCacheEditor
operator|.
name|PathEdit
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
name|dircache
operator|.
name|DirCacheEntry
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
name|IncorrectObjectTypeException
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
name|MissingObjectException
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
name|FileMode
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
name|transport
operator|.
name|RefSpec
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
name|Collection
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
name|Set
import|;
end_import

begin_class
DECL|class|SubmoduleOp
specifier|public
class|class
name|SubmoduleOp
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
name|SubmoduleOp
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|gitmodulesFactory
specifier|private
specifier|final
name|GitModules
operator|.
name|Factory
name|gitmodulesFactory
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|projectStateFactory
specifier|private
specifier|final
name|ProjectState
operator|.
name|Factory
name|projectStateFactory
decl_stmt|;
DECL|field|account
specifier|private
specifier|final
name|Account
name|account
decl_stmt|;
DECL|field|changeHooks
specifier|private
specifier|final
name|ChangeHooks
name|changeHooks
decl_stmt|;
DECL|field|verboseSuperProject
specifier|private
specifier|final
name|boolean
name|verboseSuperProject
decl_stmt|;
DECL|field|enableSuperProjectSubscriptions
specifier|private
specifier|final
name|boolean
name|enableSuperProjectSubscriptions
decl_stmt|;
DECL|field|updateId
specifier|private
name|String
name|updateId
decl_stmt|;
annotation|@
name|Inject
DECL|method|SubmoduleOp ( GitModules.Factory gitmodulesFactory, @GerritPersonIdent PersonIdent myIdent, @GerritServerConfig Config cfg, GitReferenceUpdated gitRefUpdated, ProjectCache projectCache, ProjectState.Factory projectStateFactory, @Nullable Account account, ChangeHooks changeHooks)
specifier|public
name|SubmoduleOp
parameter_list|(
name|GitModules
operator|.
name|Factory
name|gitmodulesFactory
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|myIdent
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|ProjectState
operator|.
name|Factory
name|projectStateFactory
parameter_list|,
annotation|@
name|Nullable
name|Account
name|account
parameter_list|,
name|ChangeHooks
name|changeHooks
parameter_list|)
block|{
name|this
operator|.
name|gitmodulesFactory
operator|=
name|gitmodulesFactory
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|projectStateFactory
operator|=
name|projectStateFactory
expr_stmt|;
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
name|this
operator|.
name|changeHooks
operator|=
name|changeHooks
expr_stmt|;
name|this
operator|.
name|verboseSuperProject
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"submodule"
argument_list|,
literal|"verboseSuperprojectUpdate"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|enableSuperProjectSubscriptions
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"submodule"
argument_list|,
literal|"enableSuperProjectSubscriptions"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|getDestinationBranches (Branch.NameKey src, SubscribeSection s, MergeOpRepoManager orm)
specifier|public
name|Collection
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|getDestinationBranches
parameter_list|(
name|Branch
operator|.
name|NameKey
name|src
parameter_list|,
name|SubscribeSection
name|s
parameter_list|,
name|MergeOpRepoManager
name|orm
parameter_list|)
throws|throws
name|IOException
block|{
name|Collection
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|logDebug
argument_list|(
literal|"Inspecting SubscribeSection "
operator|+
name|s
argument_list|)
expr_stmt|;
for|for
control|(
name|RefSpec
name|r
range|:
name|s
operator|.
name|getRefSpecs
argument_list|()
control|)
block|{
name|logDebug
argument_list|(
literal|"Inspecting ref "
operator|+
name|r
argument_list|)
expr_stmt|;
if|if
condition|(
name|r
operator|.
name|matchSource
argument_list|(
name|src
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|r
operator|.
name|getDestination
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// no need to care for wildcard, as we matched already
try|try
block|{
name|orm
operator|.
name|openRepo
argument_list|(
name|s
operator|.
name|getProject
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
name|e
parameter_list|)
block|{
comment|// A project listed a non existent project to be allowed
comment|// to subscribe to it. Allow this for now.
continue|continue;
block|}
name|OpenRepo
name|or
init|=
name|orm
operator|.
name|getRepo
argument_list|(
name|s
operator|.
name|getProject
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|or
operator|.
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|RefNames
operator|.
name|REFS_HEADS
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
name|ret
operator|.
name|add
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|s
operator|.
name|getProject
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|r
operator|.
name|isWildcard
argument_list|()
condition|)
block|{
comment|// refs/heads/*:refs/heads/*
name|ret
operator|.
name|add
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|s
operator|.
name|getProject
argument_list|()
argument_list|,
name|r
operator|.
name|expandFromSource
argument_list|(
name|src
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getDestination
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// e.g. refs/heads/master:refs/heads/stable
name|ret
operator|.
name|add
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|s
operator|.
name|getProject
argument_list|()
argument_list|,
name|r
operator|.
name|getDestination
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|logDebug
argument_list|(
literal|"Returning possible branches: "
operator|+
name|ret
operator|+
literal|"for project "
operator|+
name|s
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
DECL|method|superProjectSubscriptionsForSubmoduleBranch ( Branch.NameKey branch, MergeOpRepoManager orm)
name|superProjectSubscriptionsForSubmoduleBranch
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|MergeOpRepoManager
name|orm
parameter_list|)
throws|throws
name|IOException
block|{
name|logDebug
argument_list|(
literal|"Calculating possible superprojects for "
operator|+
name|branch
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|ret
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|branch
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
for|for
control|(
name|SubscribeSection
name|s
range|:
name|projectStateFactory
operator|.
name|create
argument_list|(
name|cfg
argument_list|)
operator|.
name|getSubscribeSections
argument_list|(
name|branch
argument_list|)
control|)
block|{
name|logDebug
argument_list|(
literal|"Checking subscribe section "
operator|+
name|s
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
init|=
name|getDestinationBranches
argument_list|(
name|branch
argument_list|,
name|s
argument_list|,
name|orm
argument_list|)
decl_stmt|;
for|for
control|(
name|Branch
operator|.
name|NameKey
name|targetBranch
range|:
name|branches
control|)
block|{
name|GitModules
name|m
init|=
name|gitmodulesFactory
operator|.
name|create
argument_list|(
name|targetBranch
argument_list|,
name|updateId
argument_list|,
name|orm
argument_list|)
decl_stmt|;
for|for
control|(
name|SubmoduleSubscription
name|ss
range|:
name|m
operator|.
name|subscribedTo
argument_list|(
name|branch
argument_list|)
control|)
block|{
name|logDebug
argument_list|(
literal|"Checking SubmoduleSubscription "
operator|+
name|ss
argument_list|)
expr_stmt|;
if|if
condition|(
name|projectCache
operator|.
name|get
argument_list|(
name|ss
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|logDebug
argument_list|(
literal|"Adding SubmoduleSubscription "
operator|+
name|ss
argument_list|)
expr_stmt|;
name|ret
operator|.
name|add
argument_list|(
name|ss
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|logDebug
argument_list|(
literal|"Calculated superprojects for "
operator|+
name|branch
operator|+
literal|" are "
operator|+
name|ret
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
DECL|method|updateSuperProjects ( Collection<Branch.NameKey> updatedBranches, String updateId, MergeOpRepoManager orm)
specifier|protected
name|void
name|updateSuperProjects
parameter_list|(
name|Collection
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|updatedBranches
parameter_list|,
name|String
name|updateId
parameter_list|,
name|MergeOpRepoManager
name|orm
parameter_list|)
throws|throws
name|SubmoduleException
block|{
if|if
condition|(
operator|!
name|enableSuperProjectSubscriptions
condition|)
block|{
name|logDebug
argument_list|(
literal|"Updating superprojects disabled"
argument_list|)
expr_stmt|;
return|return;
block|}
name|this
operator|.
name|updateId
operator|=
name|updateId
expr_stmt|;
name|logDebug
argument_list|(
literal|"Updating superprojects"
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|SubmoduleSubscription
argument_list|>
name|targets
init|=
name|HashMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|Branch
operator|.
name|NameKey
name|updatedBranch
range|:
name|updatedBranches
control|)
block|{
name|logDebug
argument_list|(
literal|"Now processing "
operator|+
name|updatedBranch
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|checkedTargets
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|targetsToProcess
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|targetsToProcess
operator|.
name|add
argument_list|(
name|updatedBranch
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|targetsToProcess
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|newTargets
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Branch
operator|.
name|NameKey
name|b
range|:
name|targetsToProcess
control|)
block|{
try|try
block|{
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|subs
init|=
name|superProjectSubscriptionsForSubmoduleBranch
argument_list|(
name|b
argument_list|,
name|orm
argument_list|)
decl_stmt|;
for|for
control|(
name|SubmoduleSubscription
name|sub
range|:
name|subs
control|)
block|{
name|Branch
operator|.
name|NameKey
name|dst
init|=
name|sub
operator|.
name|getSuperProject
argument_list|()
decl_stmt|;
name|targets
operator|.
name|put
argument_list|(
name|dst
argument_list|,
name|sub
argument_list|)
expr_stmt|;
name|newTargets
operator|.
name|add
argument_list|(
name|dst
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot find superprojects for "
operator|+
name|b
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
name|logDebug
argument_list|(
literal|"adding to done "
operator|+
name|targetsToProcess
argument_list|)
expr_stmt|;
name|checkedTargets
operator|.
name|addAll
argument_list|(
name|targetsToProcess
argument_list|)
expr_stmt|;
name|logDebug
argument_list|(
literal|"completely done with "
operator|+
name|checkedTargets
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|intersection
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|checkedTargets
argument_list|)
decl_stmt|;
name|intersection
operator|.
name|retainAll
argument_list|(
name|newTargets
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|intersection
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Possible circular subscription involving "
operator|+
name|updatedBranch
argument_list|)
throw|;
block|}
name|targetsToProcess
operator|=
name|newTargets
expr_stmt|;
block|}
block|}
for|for
control|(
name|Branch
operator|.
name|NameKey
name|dst
range|:
name|targets
operator|.
name|keySet
argument_list|()
control|)
block|{
try|try
block|{
name|updateGitlinks
argument_list|(
name|dst
argument_list|,
name|targets
operator|.
name|get
argument_list|(
name|dst
argument_list|)
argument_list|,
name|orm
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SubmoduleException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot update gitlinks for "
operator|+
name|dst
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**    * Update the submodules in one branch of one repository.    *    * @param subscriber the branch of the repository which should be changed.    * @param updates submodule updates which should be updated to.    * @throws SubmoduleException    */
DECL|method|updateGitlinks (Branch.NameKey subscriber, Collection<SubmoduleSubscription> updates, MergeOpRepoManager orm)
specifier|private
name|void
name|updateGitlinks
parameter_list|(
name|Branch
operator|.
name|NameKey
name|subscriber
parameter_list|,
name|Collection
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|updates
parameter_list|,
name|MergeOpRepoManager
name|orm
parameter_list|)
throws|throws
name|SubmoduleException
block|{
name|PersonIdent
name|author
init|=
literal|null
decl_stmt|;
name|StringBuilder
name|msgbuf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Update git submodules\n\n"
argument_list|)
decl_stmt|;
name|boolean
name|sameAuthorForAll
init|=
literal|true
decl_stmt|;
try|try
block|{
name|orm
operator|.
name|openRepo
argument_list|(
name|subscriber
operator|.
name|getParentKey
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot access superproject"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|OpenRepo
name|or
init|=
name|orm
operator|.
name|getRepo
argument_list|(
name|subscriber
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Ref
name|r
init|=
name|or
operator|.
name|repo
operator|.
name|exactRef
argument_list|(
name|subscriber
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"The branch was probably deleted from the subscriber repository"
argument_list|)
throw|;
block|}
name|DirCache
name|dc
init|=
name|readTree
argument_list|(
name|r
argument_list|,
name|or
operator|.
name|rw
argument_list|)
decl_stmt|;
name|DirCacheEditor
name|ed
init|=
name|dc
operator|.
name|editor
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmoduleSubscription
name|s
range|:
name|updates
control|)
block|{
try|try
block|{
name|orm
operator|.
name|openRepo
argument_list|(
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot access submodule"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|OpenRepo
name|subOr
init|=
name|orm
operator|.
name|getRepo
argument_list|(
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|Repository
name|subrepo
init|=
name|subOr
operator|.
name|repo
decl_stmt|;
name|Ref
name|ref
init|=
name|subrepo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|==
literal|null
condition|)
block|{
name|ed
operator|.
name|add
argument_list|(
operator|new
name|DeletePath
argument_list|(
name|s
operator|.
name|getPath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
specifier|final
name|ObjectId
name|updateTo
init|=
name|ref
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|RevCommit
name|newCommit
init|=
name|subOr
operator|.
name|rw
operator|.
name|parseCommit
argument_list|(
name|updateTo
argument_list|)
decl_stmt|;
name|subOr
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
if|if
condition|(
name|author
operator|==
literal|null
condition|)
block|{
name|author
operator|=
name|newCommit
operator|.
name|getAuthorIdent
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|author
operator|.
name|equals
argument_list|(
name|newCommit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
condition|)
block|{
name|sameAuthorForAll
operator|=
literal|false
expr_stmt|;
block|}
name|DirCacheEntry
name|dce
init|=
name|dc
operator|.
name|getEntry
argument_list|(
name|s
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectId
name|oldId
decl_stmt|;
if|if
condition|(
name|dce
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|dce
operator|.
name|getFileMode
argument_list|()
operator|.
name|equals
argument_list|(
name|FileMode
operator|.
name|GITLINK
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Requested to update gitlink "
operator|+
name|s
operator|.
name|getPath
argument_list|()
operator|+
literal|" in "
operator|+
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|" but entry "
operator|+
literal|"doesn't have gitlink file mode."
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|oldId
operator|=
name|dce
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// This submodule did not exist before. We do not want to add
comment|// the full submodule history to the commit message, so omit it.
name|oldId
operator|=
name|updateTo
expr_stmt|;
block|}
name|ed
operator|.
name|add
argument_list|(
operator|new
name|PathEdit
argument_list|(
name|s
operator|.
name|getPath
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|apply
parameter_list|(
name|DirCacheEntry
name|ent
parameter_list|)
block|{
name|ent
operator|.
name|setFileMode
argument_list|(
name|FileMode
operator|.
name|GITLINK
argument_list|)
expr_stmt|;
name|ent
operator|.
name|setObjectId
argument_list|(
name|updateTo
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|verboseSuperProject
condition|)
block|{
name|msgbuf
operator|.
name|append
argument_list|(
literal|"Project: "
operator|+
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|" "
operator|+
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|" "
operator|+
name|updateTo
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
try|try
block|{
name|subOr
operator|.
name|rw
operator|.
name|markStart
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
name|subOr
operator|.
name|rw
operator|.
name|markUninteresting
argument_list|(
name|subOr
operator|.
name|rw
operator|.
name|parseCommit
argument_list|(
name|oldId
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|subOr
operator|.
name|rw
control|)
block|{
name|subOr
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
name|c
operator|.
name|getFullMessage
argument_list|()
operator|+
literal|"\n\n"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Could not perform a revwalk to "
operator|+
literal|"create superproject commit message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
name|ed
operator|.
name|finish
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|sameAuthorForAll
operator|||
name|author
operator|==
literal|null
condition|)
block|{
name|author
operator|=
name|myIdent
expr_stmt|;
block|}
name|ObjectInserter
name|oi
init|=
name|or
operator|.
name|repo
operator|.
name|newObjectInserter
argument_list|()
decl_stmt|;
name|ObjectId
name|tree
init|=
name|dc
operator|.
name|writeTree
argument_list|(
name|oi
argument_list|)
decl_stmt|;
name|ObjectId
name|currentCommitId
init|=
name|or
operator|.
name|repo
operator|.
name|exactRef
argument_list|(
name|subscriber
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|CommitBuilder
name|commit
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|commit
operator|.
name|setTreeId
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setParentIds
argument_list|(
operator|new
name|ObjectId
index|[]
block|{
name|currentCommitId
block|}
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setAuthor
argument_list|(
name|author
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setCommitter
argument_list|(
name|myIdent
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setMessage
argument_list|(
name|msgbuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|oi
operator|.
name|insert
argument_list|(
name|commit
argument_list|)
expr_stmt|;
name|oi
operator|.
name|flush
argument_list|()
expr_stmt|;
name|ObjectId
name|commitId
init|=
name|oi
operator|.
name|idFor
argument_list|(
name|Constants
operator|.
name|OBJ_COMMIT
argument_list|,
name|commit
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|RefUpdate
name|rfu
init|=
name|or
operator|.
name|repo
operator|.
name|updateRef
argument_list|(
name|subscriber
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|rfu
operator|.
name|setForceUpdate
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|rfu
operator|.
name|setNewObjectId
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
name|rfu
operator|.
name|setExpectedOldObjectId
argument_list|(
name|currentCommitId
argument_list|)
expr_stmt|;
name|rfu
operator|.
name|setRefLogMessage
argument_list|(
literal|"Submit to "
operator|+
name|subscriber
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|rfu
operator|.
name|update
argument_list|()
condition|)
block|{
case|case
name|NEW
case|:
case|case
name|FAST_FORWARD
case|:
name|gitRefUpdated
operator|.
name|fire
argument_list|(
name|subscriber
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|rfu
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|changeHooks
operator|.
name|doRefUpdatedHook
argument_list|(
name|subscriber
argument_list|,
name|rfu
argument_list|,
name|account
argument_list|)
expr_stmt|;
comment|// TODO since this is performed "in the background" no mail will be
comment|// sent to inform users about the updated branch
break|break;
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
throw|throw
operator|new
name|IOException
argument_list|(
name|rfu
operator|.
name|getResult
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot update gitlinks for "
operator|+
name|subscriber
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|readTree (final Ref branch, RevWalk rw)
specifier|private
specifier|static
name|DirCache
name|readTree
parameter_list|(
specifier|final
name|Ref
name|branch
parameter_list|,
name|RevWalk
name|rw
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
specifier|final
name|DirCache
name|dc
init|=
name|DirCache
operator|.
name|newInCore
argument_list|()
decl_stmt|;
specifier|final
name|DirCacheBuilder
name|b
init|=
name|dc
operator|.
name|builder
argument_list|()
decl_stmt|;
name|b
operator|.
name|addTree
argument_list|(
operator|new
name|byte
index|[
literal|0
index|]
argument_list|,
comment|// no prefix path
name|DirCacheEntry
operator|.
name|STAGE_0
argument_list|,
comment|// standard stage
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|rw
operator|.
name|parseTree
argument_list|(
name|branch
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|finish
argument_list|()
expr_stmt|;
return|return
name|dc
return|;
block|}
DECL|method|logDebug (String msg, Object... args)
specifier|private
name|void
name|logDebug
parameter_list|(
name|String
name|msg
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"["
operator|+
name|updateId
operator|+
literal|"]"
operator|+
name|msg
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

