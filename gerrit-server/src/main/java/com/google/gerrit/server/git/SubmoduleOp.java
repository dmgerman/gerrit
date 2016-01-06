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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|CanonicalWebUrl
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
name|util
operator|.
name|SubmoduleSectionParser
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
name|BlobBasedConfig
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
name|treewalk
operator|.
name|TreeWalk
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|Collections
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
DECL|field|GIT_MODULES
specifier|private
specifier|static
specifier|final
name|String
name|GIT_MODULES
init|=
literal|".gitmodules"
decl_stmt|;
DECL|field|urlProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|updatedSubscribers
specifier|private
specifier|final
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|updatedSubscribers
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
DECL|field|subSecParserFactory
specifier|private
specifier|final
name|SubmoduleSectionParser
operator|.
name|Factory
name|subSecParserFactory
decl_stmt|;
DECL|field|verboseSuperProject
specifier|private
specifier|final
name|boolean
name|verboseSuperProject
decl_stmt|;
annotation|@
name|Inject
DECL|method|SubmoduleOp ( @anonicalWebUrl @ullable Provider<String> urlProvider, @GerritPersonIdent PersonIdent myIdent, @GerritServerConfig Config cfg, GitRepositoryManager repoManager, GitReferenceUpdated gitRefUpdated, @Nullable Account account, ChangeHooks changeHooks, SubmoduleSectionParser.Factory subSecParserFactory)
specifier|public
name|SubmoduleOp
parameter_list|(
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
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
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|,
annotation|@
name|Nullable
name|Account
name|account
parameter_list|,
name|ChangeHooks
name|changeHooks
parameter_list|,
name|SubmoduleSectionParser
operator|.
name|Factory
name|subSecParserFactory
parameter_list|)
block|{
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
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
name|subSecParserFactory
operator|=
name|subSecParserFactory
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
name|updatedSubscribers
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|updateSubmoduleSubscriptions (ReviewDb db, Set<Branch.NameKey> branches)
name|void
name|updateSubmoduleSubscriptions
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Set
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|branches
parameter_list|)
throws|throws
name|SubmoduleException
block|{
for|for
control|(
name|Branch
operator|.
name|NameKey
name|branch
range|:
name|branches
control|)
block|{
name|updateSubmoduleSubscriptions
argument_list|(
name|db
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateSubmoduleSubscriptions (ReviewDb db, Branch.NameKey destBranch)
name|void
name|updateSubmoduleSubscriptions
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|)
throws|throws
name|SubmoduleException
block|{
if|if
condition|(
name|urlProvider
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Cannot establish canonical web url used "
operator|+
literal|"to access gerrit. It should be provided in gerrit.config file."
argument_list|)
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
name|destBranch
operator|.
name|getParentKey
argument_list|()
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
name|ObjectId
name|id
init|=
name|repo
operator|.
name|resolve
argument_list|(
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Cannot resolve submodule destination branch "
operator|+
name|destBranch
argument_list|)
expr_stmt|;
block|}
name|RevCommit
name|commit
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|oldSubscriptions
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|db
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|bySuperProject
argument_list|(
name|destBranch
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|newSubscriptions
decl_stmt|;
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|repo
argument_list|,
name|GIT_MODULES
argument_list|,
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|tw
operator|!=
literal|null
operator|&&
operator|(
name|FileMode
operator|.
name|REGULAR_FILE
operator|.
name|equals
argument_list|(
name|tw
operator|.
name|getRawMode
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|||
name|FileMode
operator|.
name|EXECUTABLE_FILE
operator|.
name|equals
argument_list|(
name|tw
operator|.
name|getRawMode
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|)
condition|)
block|{
name|BlobBasedConfig
name|bbc
init|=
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|repo
argument_list|,
name|commit
argument_list|,
name|GIT_MODULES
argument_list|)
decl_stmt|;
name|String
name|thisServer
init|=
operator|new
name|URI
argument_list|(
name|urlProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|newSubscriptions
operator|=
name|subSecParserFactory
operator|.
name|create
argument_list|(
name|bbc
argument_list|,
name|thisServer
argument_list|,
name|destBranch
argument_list|)
operator|.
name|parseAllSections
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newSubscriptions
operator|=
name|Collections
operator|.
name|emptySet
argument_list|()
expr_stmt|;
block|}
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|alreadySubscribeds
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmoduleSubscription
name|s
range|:
name|newSubscriptions
control|)
block|{
if|if
condition|(
name|oldSubscriptions
operator|.
name|contains
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|alreadySubscribeds
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|oldSubscriptions
operator|.
name|removeAll
argument_list|(
name|newSubscriptions
argument_list|)
expr_stmt|;
name|newSubscriptions
operator|.
name|removeAll
argument_list|(
name|alreadySubscribeds
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|oldSubscriptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|db
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|delete
argument_list|(
name|oldSubscriptions
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|newSubscriptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|db
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|insert
argument_list|(
name|newSubscriptions
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Database problem at update of subscriptions table from "
operator|+
name|GIT_MODULES
operator|+
literal|" file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Problem at update of subscriptions table: "
operator|+
name|GIT_MODULES
operator|+
literal|" config file is invalid."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Problem at update of subscriptions table from "
operator|+
name|GIT_MODULES
operator|+
literal|"."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Incorrect gerrit canonical web url provided in gerrit.config file."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateSuperProjects (ReviewDb db, Collection<Branch.NameKey> updatedBranches)
specifier|protected
name|void
name|updateSuperProjects
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Collection
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
name|updatedBranches
parameter_list|)
throws|throws
name|SubmoduleException
block|{
try|try
block|{
comment|// These (repo/branch) will be updated later with all the given
comment|// individual submodule subscriptions
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
for|for
control|(
name|SubmoduleSubscription
name|sub
range|:
name|db
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|bySubmodule
argument_list|(
name|updatedBranch
argument_list|)
control|)
block|{
name|targets
operator|.
name|put
argument_list|(
name|sub
operator|.
name|getSuperProject
argument_list|()
argument_list|,
name|sub
argument_list|)
expr_stmt|;
block|}
block|}
name|updatedSubscribers
operator|.
name|addAll
argument_list|(
name|updatedBranches
argument_list|)
expr_stmt|;
comment|// Update subscribers.
for|for
control|(
name|Branch
operator|.
name|NameKey
name|dest
range|:
name|targets
operator|.
name|keySet
argument_list|()
control|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|updatedSubscribers
operator|.
name|add
argument_list|(
name|dest
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Possible circular subscription involving "
operator|+
name|dest
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateGitlinks
argument_list|(
name|db
argument_list|,
name|dest
argument_list|,
name|targets
operator|.
name|get
argument_list|(
name|dest
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SubmoduleException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot update gitlinks for "
operator|+
name|dest
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|logAndThrowSubmoduleException
argument_list|(
literal|"Cannot read subscription records"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Update the submodules in one branch of one repository.    *    * @param subscriber the branch of the repository which should be changed.    * @param updates submodule updates which should be updated to.    * @throws SubmoduleException    */
DECL|method|updateGitlinks (ReviewDb db, Branch.NameKey subscriber, Collection<SubmoduleSubscription> updates)
specifier|private
name|void
name|updateGitlinks
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
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
literal|"Updated git submodules\n\n"
argument_list|)
decl_stmt|;
name|boolean
name|sameAuthorForAll
init|=
literal|true
decl_stmt|;
try|try
init|(
name|Repository
name|pdb
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|subscriber
operator|.
name|getParentKey
argument_list|()
argument_list|)
init|)
block|{
if|if
condition|(
name|pdb
operator|.
name|exactRef
argument_list|(
name|subscriber
operator|.
name|get
argument_list|()
argument_list|)
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
name|pdb
argument_list|,
name|pdb
operator|.
name|exactRef
argument_list|(
name|subscriber
operator|.
name|get
argument_list|()
argument_list|)
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
init|(
name|Repository
name|subrepo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|s
operator|.
name|getSubmodule
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
name|CodeReviewCommit
operator|.
name|newRevWalk
argument_list|(
name|subrepo
argument_list|)
init|)
block|{
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
name|rw
operator|.
name|parseCommit
argument_list|(
name|updateTo
argument_list|)
decl_stmt|;
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
name|rw
operator|.
name|markStart
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markUninteresting
argument_list|(
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
name|rw
control|)
block|{
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
name|logAndThrowSubmoduleException
argument_list|(
literal|"Could not perform a revwalk to "
operator|+
literal|"create superproject commit message"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
name|pdb
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
name|pdb
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
name|pdb
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
comment|// Recursive call: update subscribers of the subscriber
name|updateSuperProjects
argument_list|(
name|db
argument_list|,
name|Sets
operator|.
name|newHashSet
argument_list|(
name|subscriber
argument_list|)
argument_list|)
expr_stmt|;
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
DECL|method|readTree (final Repository pdb, final Ref branch)
specifier|private
specifier|static
name|DirCache
name|readTree
parameter_list|(
specifier|final
name|Repository
name|pdb
parameter_list|,
specifier|final
name|Ref
name|branch
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|pdb
argument_list|)
init|)
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
name|pdb
operator|.
name|newObjectReader
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
block|}
DECL|method|logAndThrowSubmoduleException (final String errorMsg, final Exception e)
specifier|private
specifier|static
name|void
name|logAndThrowSubmoduleException
parameter_list|(
specifier|final
name|String
name|errorMsg
parameter_list|,
specifier|final
name|Exception
name|e
parameter_list|)
throws|throws
name|SubmoduleException
block|{
name|log
operator|.
name|error
argument_list|(
name|errorMsg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SubmoduleException
argument_list|(
name|errorMsg
argument_list|,
name|e
argument_list|)
throw|;
block|}
DECL|method|logAndThrowSubmoduleException (final String errorMsg)
specifier|private
specifier|static
name|void
name|logAndThrowSubmoduleException
parameter_list|(
specifier|final
name|String
name|errorMsg
parameter_list|)
throws|throws
name|SubmoduleException
block|{
name|log
operator|.
name|error
argument_list|(
name|errorMsg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SubmoduleException
argument_list|(
name|errorMsg
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

