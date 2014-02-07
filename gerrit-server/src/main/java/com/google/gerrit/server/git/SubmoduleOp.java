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
name|Change
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
DECL|class|SubmoduleOp
specifier|public
class|class
name|SubmoduleOp
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Branch.NameKey destBranch, RevCommit mergeTip, RevWalk rw, Repository db, Project destProject, List<Change> submitted, Map<Change.Id, CodeReviewCommit> commits)
name|SubmoduleOp
name|create
parameter_list|(
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
name|RevCommit
name|mergeTip
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Repository
name|db
parameter_list|,
name|Project
name|destProject
parameter_list|,
name|List
argument_list|<
name|Change
argument_list|>
name|submitted
parameter_list|,
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|commits
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
DECL|field|destBranch
specifier|private
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
decl_stmt|;
DECL|field|mergeTip
specifier|private
name|RevCommit
name|mergeTip
decl_stmt|;
DECL|field|rw
specifier|private
name|RevWalk
name|rw
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
DECL|field|schema
specifier|private
name|ReviewDb
name|schema
decl_stmt|;
DECL|field|db
specifier|private
name|Repository
name|db
decl_stmt|;
DECL|field|destProject
specifier|private
name|Project
name|destProject
decl_stmt|;
DECL|field|submitted
specifier|private
name|List
argument_list|<
name|Change
argument_list|>
name|submitted
decl_stmt|;
DECL|field|commits
specifier|private
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|commits
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
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
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
annotation|@
name|Inject
DECL|method|SubmoduleOp (@ssisted final Branch.NameKey destBranch, @Assisted RevCommit mergeTip, @Assisted RevWalk rw, @CanonicalWebUrl @Nullable final Provider<String> urlProvider, final SchemaFactory<ReviewDb> sf, @Assisted Repository db, @Assisted Project destProject, @Assisted List<Change> submitted, @Assisted final Map<Change.Id, CodeReviewCommit> commits, @GerritPersonIdent final PersonIdent myIdent, GitRepositoryManager repoManager, GitReferenceUpdated gitRefUpdated)
specifier|public
name|SubmoduleOp
parameter_list|(
annotation|@
name|Assisted
specifier|final
name|Branch
operator|.
name|NameKey
name|destBranch
parameter_list|,
annotation|@
name|Assisted
name|RevCommit
name|mergeTip
parameter_list|,
annotation|@
name|Assisted
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|CanonicalWebUrl
annotation|@
name|Nullable
specifier|final
name|Provider
argument_list|<
name|String
argument_list|>
name|urlProvider
parameter_list|,
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
annotation|@
name|Assisted
name|Repository
name|db
parameter_list|,
annotation|@
name|Assisted
name|Project
name|destProject
parameter_list|,
annotation|@
name|Assisted
name|List
argument_list|<
name|Change
argument_list|>
name|submitted
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|commits
parameter_list|,
annotation|@
name|GerritPersonIdent
specifier|final
name|PersonIdent
name|myIdent
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|)
block|{
name|this
operator|.
name|destBranch
operator|=
name|destBranch
expr_stmt|;
name|this
operator|.
name|mergeTip
operator|=
name|mergeTip
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
name|this
operator|.
name|urlProvider
operator|=
name|urlProvider
expr_stmt|;
name|this
operator|.
name|schemaFactory
operator|=
name|sf
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|destProject
operator|=
name|destProject
expr_stmt|;
name|this
operator|.
name|submitted
operator|=
name|submitted
expr_stmt|;
name|this
operator|.
name|commits
operator|=
name|commits
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
name|updatedSubscribers
operator|=
operator|new
name|HashSet
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|update ()
specifier|public
name|void
name|update
parameter_list|()
throws|throws
name|SubmoduleException
block|{
try|try
block|{
name|schema
operator|=
name|schemaFactory
operator|.
name|open
argument_list|()
expr_stmt|;
name|updateSubmoduleSubscriptions
argument_list|()
expr_stmt|;
name|updateSuperProjects
argument_list|(
name|destBranch
argument_list|,
name|rw
argument_list|,
name|mergeTip
operator|.
name|getId
argument_list|()
operator|.
name|toObjectId
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SubmoduleException
argument_list|(
literal|"Cannot open database"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|schema
operator|.
name|close
argument_list|()
expr_stmt|;
name|schema
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
DECL|method|updateSubmoduleSubscriptions ()
specifier|private
name|void
name|updateSubmoduleSubscriptions
parameter_list|()
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
literal|"Cannot establish canonical web url used to access gerrit."
operator|+
literal|" It should be provided in gerrit.config file."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
specifier|final
name|TreeWalk
name|tw
init|=
name|TreeWalk
operator|.
name|forPath
argument_list|(
name|db
argument_list|,
name|GIT_MODULES
argument_list|,
name|mergeTip
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
name|db
argument_list|,
name|mergeTip
argument_list|,
name|GIT_MODULES
argument_list|)
decl_stmt|;
specifier|final
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
specifier|final
name|Branch
operator|.
name|NameKey
name|target
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|destProject
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|destBranch
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|oldSubscriptions
init|=
operator|new
name|HashSet
argument_list|<
name|SubmoduleSubscription
argument_list|>
argument_list|(
name|schema
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|bySuperProject
argument_list|(
name|destBranch
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|newSubscriptions
init|=
operator|new
name|SubmoduleSectionParser
argument_list|(
name|bbc
argument_list|,
name|thisServer
argument_list|,
name|target
argument_list|,
name|repoManager
argument_list|)
operator|.
name|parseAllSections
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|alreadySubscribeds
init|=
operator|new
name|HashSet
argument_list|<
name|SubmoduleSubscription
argument_list|>
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
name|schema
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
name|schema
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
DECL|method|updateSuperProjects (final Branch.NameKey updatedBranch, RevWalk myRw, final ObjectId mergedCommit, final String msg)
specifier|private
name|void
name|updateSuperProjects
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|updatedBranch
parameter_list|,
name|RevWalk
name|myRw
parameter_list|,
specifier|final
name|ObjectId
name|mergedCommit
parameter_list|,
specifier|final
name|String
name|msg
parameter_list|)
throws|throws
name|SubmoduleException
block|{
try|try
block|{
specifier|final
name|List
argument_list|<
name|SubmoduleSubscription
argument_list|>
name|subscribers
init|=
name|schema
operator|.
name|submoduleSubscriptions
argument_list|()
operator|.
name|bySubmodule
argument_list|(
name|updatedBranch
argument_list|)
operator|.
name|toList
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|subscribers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|msgbuf
init|=
name|msg
decl_stmt|;
if|if
condition|(
name|msgbuf
operator|==
literal|null
condition|)
block|{
comment|// Initialize the message buffer
name|msgbuf
operator|=
literal|""
expr_stmt|;
comment|// The first updatedBranch on a cascade event of automatic
comment|// updates of repos is added to updatedSubscribers set so
comment|// if we face a situation having
comment|// submodule-a(master)-->super(master)-->submodule-a(master),
comment|// it will be detected we have a circular subscription
comment|// when updateSuperProjects is called having as updatedBranch
comment|// the super(master) value.
name|updatedSubscribers
operator|.
name|add
argument_list|(
name|updatedBranch
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Change
name|chg
range|:
name|submitted
control|)
block|{
specifier|final
name|CodeReviewCommit
name|c
init|=
name|commits
operator|.
name|get
argument_list|(
name|chg
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
operator|&&
operator|(
name|c
operator|.
name|getStatusCode
argument_list|()
operator|==
name|CommitMergeStatus
operator|.
name|CLEAN_MERGE
operator|||
name|c
operator|.
name|getStatusCode
argument_list|()
operator|==
name|CommitMergeStatus
operator|.
name|CLEAN_PICK
operator|||
name|c
operator|.
name|getStatusCode
argument_list|()
operator|==
name|CommitMergeStatus
operator|.
name|CLEAN_REBASE
operator|)
condition|)
block|{
name|msgbuf
operator|+=
literal|"\n"
expr_stmt|;
name|msgbuf
operator|+=
name|c
operator|.
name|getFullMessage
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// update subscribers of this module
for|for
control|(
specifier|final
name|SubmoduleSubscription
name|s
range|:
name|subscribers
control|)
block|{
if|if
condition|(
operator|!
name|updatedSubscribers
operator|.
name|add
argument_list|(
name|s
operator|.
name|getSuperProject
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Possible circular subscription involving "
operator|+
name|s
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ObjectId
argument_list|>
name|modules
init|=
operator|new
name|HashMap
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ObjectId
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|modules
operator|.
name|put
argument_list|(
name|updatedBranch
argument_list|,
name|mergedCommit
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|String
argument_list|>
name|paths
init|=
operator|new
name|HashMap
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|String
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|paths
operator|.
name|put
argument_list|(
name|updatedBranch
argument_list|,
name|s
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|updateGitlinks
argument_list|(
name|s
operator|.
name|getSuperProject
argument_list|()
argument_list|,
name|myRw
argument_list|,
name|modules
argument_list|,
name|paths
argument_list|,
name|msgbuf
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
name|e
throw|;
block|}
block|}
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
DECL|method|updateGitlinks (final Branch.NameKey subscriber, RevWalk myRw, final Map<Branch.NameKey, ObjectId> modules, final Map<Branch.NameKey, String> paths, final String msg)
specifier|private
name|void
name|updateGitlinks
parameter_list|(
specifier|final
name|Branch
operator|.
name|NameKey
name|subscriber
parameter_list|,
name|RevWalk
name|myRw
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ObjectId
argument_list|>
name|modules
parameter_list|,
specifier|final
name|Map
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|String
argument_list|>
name|paths
parameter_list|,
specifier|final
name|String
name|msg
parameter_list|)
throws|throws
name|SubmoduleException
block|{
name|PersonIdent
name|author
init|=
literal|null
decl_stmt|;
specifier|final
name|StringBuilder
name|msgbuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|"Updated "
operator|+
name|subscriber
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Repository
name|pdb
init|=
literal|null
decl_stmt|;
name|RevWalk
name|recRw
init|=
literal|null
decl_stmt|;
try|try
block|{
name|boolean
name|sameAuthorForAll
init|=
literal|true
decl_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ObjectId
argument_list|>
name|me
range|:
name|modules
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|RevCommit
name|c
init|=
name|myRw
operator|.
name|parseCommit
argument_list|(
name|me
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|"\nProject: "
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
name|me
operator|.
name|getKey
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
literal|"  "
argument_list|)
operator|.
name|append
argument_list|(
name|me
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msgbuf
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|modules
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|msg
operator|!=
literal|null
condition|)
block|{
name|msgbuf
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msgbuf
operator|.
name|append
argument_list|(
name|c
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|msgbuf
operator|.
name|append
argument_list|(
literal|"\n"
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
name|c
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
name|c
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
block|}
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
name|pdb
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|subscriber
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|pdb
operator|.
name|getRef
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
specifier|final
name|ObjectId
name|currentCommitId
init|=
name|pdb
operator|.
name|getRef
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
name|DirCache
name|dc
init|=
name|readTree
argument_list|(
name|pdb
argument_list|,
name|pdb
operator|.
name|getRef
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
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|Branch
operator|.
name|NameKey
argument_list|,
name|ObjectId
argument_list|>
name|me
range|:
name|modules
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ed
operator|.
name|add
argument_list|(
operator|new
name|PathEdit
argument_list|(
name|paths
operator|.
name|get
argument_list|(
name|me
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
block|{
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
name|me
operator|.
name|getValue
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|ed
operator|.
name|finish
argument_list|()
expr_stmt|;
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
specifier|final
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
name|recRw
operator|=
operator|new
name|RevWalk
argument_list|(
name|pdb
argument_list|)
expr_stmt|;
comment|// Recursive call: update subscribers of the subscriber
name|updateSuperProjects
argument_list|(
name|subscriber
argument_list|,
name|recRw
argument_list|,
name|commitId
argument_list|,
name|msgbuf
operator|.
name|toString
argument_list|()
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
literal|"Cannot update gitlinks for "
operator|+
name|subscriber
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|recRw
operator|!=
literal|null
condition|)
block|{
name|recRw
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pdb
operator|!=
literal|null
condition|)
block|{
name|pdb
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
specifier|final
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|pdb
argument_list|)
decl_stmt|;
try|try
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
finally|finally
block|{
name|rw
operator|.
name|release
argument_list|()
expr_stmt|;
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

