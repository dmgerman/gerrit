begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|notedb
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth8
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|TruthJUnit
operator|.
name|assume
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
name|extensions
operator|.
name|client
operator|.
name|ListChangesOption
operator|.
name|MESSAGES
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
name|toList
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
name|common
operator|.
name|collect
operator|.
name|Streams
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
name|AbstractDaemonTest
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
name|PushOneCommit
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
name|TimeUtil
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
name|changes
operator|.
name|ReviewInput
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
name|server
operator|.
name|project
operator|.
name|NoSuchChangeException
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
name|update
operator|.
name|BatchUpdate
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
name|update
operator|.
name|BatchUpdateListener
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
name|update
operator|.
name|BatchUpdateOp
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
name|update
operator|.
name|ChangeContext
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
name|update
operator|.
name|RepoContext
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
name|update
operator|.
name|RetryHelper
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
name|testing
operator|.
name|ConfigSuite
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
name|Optional
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
name|Callable
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
name|atomic
operator|.
name|AtomicInteger
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|RevSort
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|NoteDbOnlyIT
specifier|public
class|class
name|NoteDbOnlyIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|defaultConfig ()
specifier|public
specifier|static
name|Config
name|defaultConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
comment|// Avoid spurious timeouts during intentional retries due to overloaded test machines.
name|cfg
operator|.
name|setString
argument_list|(
literal|"noteDb"
argument_list|,
literal|null
argument_list|,
literal|"retryTimeout"
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
operator|+
literal|"s"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
DECL|field|retryHelper
annotation|@
name|Inject
specifier|private
name|RetryHelper
name|retryHelper
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|disableChangeReviewDb
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateChangeFailureRollsBackRefUpdate ()
specifier|public
name|void
name|updateChangeFailureRollsBackRefUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|disableChangeReviewDb
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|master
init|=
literal|"refs/heads/master"
decl_stmt|;
name|String
name|backup
init|=
literal|"refs/backup/master"
decl_stmt|;
name|ObjectId
name|master1
init|=
name|getRef
argument_list|(
name|master
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|getRef
argument_list|(
name|backup
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
comment|// Toy op that copies the value of refs/heads/master to refs/backup/master.
name|BatchUpdateOp
name|backupMasterOp
init|=
operator|new
name|BatchUpdateOp
argument_list|()
block|{
name|ObjectId
name|newId
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|updateRepo
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectId
name|oldId
init|=
name|ctx
operator|.
name|getRepoView
argument_list|()
operator|.
name|getRef
argument_list|(
name|backup
argument_list|)
operator|.
name|orElse
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
decl_stmt|;
name|newId
operator|=
name|ctx
operator|.
name|getRepoView
argument_list|()
operator|.
name|getRef
argument_list|(
name|master
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|addRefUpdate
argument_list|(
name|oldId
argument_list|,
name|newId
argument_list|,
name|backup
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
block|{
name|ctx
operator|.
name|getUpdate
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
operator|.
name|setChangeMessage
argument_list|(
literal|"Backed up master branch to "
operator|+
name|newId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|newBatchUpdate
argument_list|(
name|batchUpdateFactory
argument_list|)
init|)
block|{
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|backupMasterOp
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|// Ensure backupMasterOp worked.
name|assertThat
argument_list|(
name|getRef
argument_list|(
name|backup
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMessages
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Backed up master branch to "
operator|+
name|master1
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// Advance master by submitting the change.
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|ObjectId
name|master2
init|=
name|getRef
argument_list|(
name|master
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|master2
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|int
name|msgCount
init|=
name|getMessages
argument_list|(
name|id
argument_list|)
operator|.
name|size
argument_list|()
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|newBatchUpdate
argument_list|(
name|batchUpdateFactory
argument_list|)
init|)
block|{
comment|// This time, we attempt to back up master, but we fail during updateChange.
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|backupMasterOp
argument_list|)
expr_stmt|;
name|String
name|msg
init|=
literal|"Change is bad"
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|BatchUpdateOp
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected ResourceConflictException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If updateChange hadn't failed, backup would have been updated to master2.
name|assertThat
argument_list|(
name|getRef
argument_list|(
name|backup
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMessages
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
name|msgCount
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|retryOnLockFailureWithAtomicUpdates ()
specifier|public
name|void
name|retryOnLockFailureWithAtomicUpdates
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|notesMigration
operator|.
name|disableChangeReviewDb
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|master
init|=
literal|"refs/heads/master"
decl_stmt|;
name|ObjectId
name|initial
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
name|project
argument_list|)
init|)
block|{
name|ensureAtomicTransactions
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|initial
operator|=
name|repo
operator|.
name|exactRef
argument_list|(
name|master
argument_list|)
operator|.
name|getObjectId
argument_list|()
expr_stmt|;
block|}
name|AtomicInteger
name|updateRepoCalledCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|AtomicInteger
name|updateChangeCalledCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|AtomicInteger
name|afterUpdateReposCalledCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|retryHelper
operator|.
name|execute
argument_list|(
name|batchUpdateFactory
lambda|->
block|{
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|newBatchUpdate
argument_list|(
name|batchUpdateFactory
argument_list|)
init|)
block|{
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|UpdateRefAndAddMessageOp
argument_list|(
name|updateRepoCalledCount
argument_list|,
name|updateChangeCalledCount
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|(
operator|new
name|ConcurrentWritingListener
argument_list|(
name|afterUpdateReposCalledCount
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
literal|"Done"
return|;
block|}
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Done"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|updateRepoCalledCount
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|afterUpdateReposCalledCount
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|updateChangeCalledCount
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|messages
init|=
name|getMessages
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|messages
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|UpdateRefAndAddMessageOp
operator|.
name|CHANGE_MESSAGE
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|Collections
operator|.
name|frequency
argument_list|(
name|messages
argument_list|,
name|UpdateRefAndAddMessageOp
operator|.
name|CHANGE_MESSAGE
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
comment|// Op lost the race, so the other writer's commit happened first. Then op retried and wrote
comment|// its commit with the other writer's commit as parent.
name|assertThat
argument_list|(
name|commitMessages
argument_list|(
name|repo
argument_list|,
name|initial
argument_list|,
name|repo
operator|.
name|exactRef
argument_list|(
name|master
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|ConcurrentWritingListener
operator|.
name|MSG_PREFIX
operator|+
literal|"1"
argument_list|,
name|UpdateRefAndAddMessageOp
operator|.
name|COMMIT_MESSAGE
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|missingChange ()
specifier|public
name|void
name|missingChange
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1234567
argument_list|)
decl_stmt|;
name|assertNoSuchChangeException
argument_list|(
parameter_list|()
lambda|->
name|notesFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
name|assertNoSuchChangeException
argument_list|(
parameter_list|()
lambda|->
name|notesFactory
operator|.
name|createChecked
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoSuchChangeException (Callable<?> callable)
specifier|private
name|void
name|assertNoSuchChangeException
parameter_list|(
name|Callable
argument_list|<
name|?
argument_list|>
name|callable
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|callable
operator|.
name|call
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"expected NoSuchChangeException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
DECL|class|ConcurrentWritingListener
specifier|private
class|class
name|ConcurrentWritingListener
implements|implements
name|BatchUpdateListener
block|{
DECL|field|MSG_PREFIX
specifier|static
specifier|final
name|String
name|MSG_PREFIX
init|=
literal|"Other writer "
decl_stmt|;
DECL|field|calledCount
specifier|private
specifier|final
name|AtomicInteger
name|calledCount
decl_stmt|;
DECL|method|ConcurrentWritingListener (AtomicInteger calledCount)
specifier|private
name|ConcurrentWritingListener
parameter_list|(
name|AtomicInteger
name|calledCount
parameter_list|)
block|{
name|this
operator|.
name|calledCount
operator|=
name|calledCount
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterUpdateRepos ()
specifier|public
name|void
name|afterUpdateRepos
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Reopen repo and update ref, to simulate a concurrent write in another
comment|// thread. Only do this the first time the listener is called.
if|if
condition|(
name|calledCount
operator|.
name|getAndIncrement
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return;
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
name|project
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
init|;
name|ObjectInserter
name|ins
operator|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|String
name|master
init|=
literal|"refs/heads/master"
decl_stmt|;
name|ObjectId
name|oldId
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|master
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|ObjectId
name|newId
init|=
name|newCommit
argument_list|(
name|rw
argument_list|,
name|ins
argument_list|,
name|oldId
argument_list|,
name|MSG_PREFIX
operator|+
name|calledCount
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|ins
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
name|master
argument_list|)
decl_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|oldId
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|newId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ru
operator|.
name|update
argument_list|(
name|rw
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|RefUpdate
operator|.
name|Result
operator|.
name|FAST_FORWARD
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|UpdateRefAndAddMessageOp
specifier|private
class|class
name|UpdateRefAndAddMessageOp
implements|implements
name|BatchUpdateOp
block|{
DECL|field|COMMIT_MESSAGE
specifier|static
specifier|final
name|String
name|COMMIT_MESSAGE
init|=
literal|"A commit"
decl_stmt|;
DECL|field|CHANGE_MESSAGE
specifier|static
specifier|final
name|String
name|CHANGE_MESSAGE
init|=
literal|"A change message"
decl_stmt|;
DECL|field|updateRepoCalledCount
specifier|private
specifier|final
name|AtomicInteger
name|updateRepoCalledCount
decl_stmt|;
DECL|field|updateChangeCalledCount
specifier|private
specifier|final
name|AtomicInteger
name|updateChangeCalledCount
decl_stmt|;
DECL|method|UpdateRefAndAddMessageOp ( AtomicInteger updateRepoCalledCount, AtomicInteger updateChangeCalledCount)
specifier|private
name|UpdateRefAndAddMessageOp
parameter_list|(
name|AtomicInteger
name|updateRepoCalledCount
parameter_list|,
name|AtomicInteger
name|updateChangeCalledCount
parameter_list|)
block|{
name|this
operator|.
name|updateRepoCalledCount
operator|=
name|updateRepoCalledCount
expr_stmt|;
name|this
operator|.
name|updateChangeCalledCount
operator|=
name|updateChangeCalledCount
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateRepo (RepoContext ctx)
specifier|public
name|void
name|updateRepo
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|master
init|=
literal|"refs/heads/master"
decl_stmt|;
name|ObjectId
name|oldId
init|=
name|ctx
operator|.
name|getRepoView
argument_list|()
operator|.
name|getRef
argument_list|(
name|master
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|ObjectId
name|newId
init|=
name|newCommit
argument_list|(
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|,
name|ctx
operator|.
name|getInserter
argument_list|()
argument_list|,
name|oldId
argument_list|,
name|COMMIT_MESSAGE
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|addRefUpdate
argument_list|(
name|oldId
argument_list|,
name|newId
argument_list|,
name|master
argument_list|)
expr_stmt|;
name|updateRepoCalledCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateChange (ChangeContext ctx)
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|getUpdate
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
operator|.
name|setChangeMessage
argument_list|(
name|CHANGE_MESSAGE
argument_list|)
expr_stmt|;
name|updateChangeCalledCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|newCommit (RevWalk rw, ObjectInserter ins, ObjectId parent, String msg)
specifier|private
name|ObjectId
name|newCommit
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|ObjectId
name|parent
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
name|PersonIdent
name|ident
init|=
name|serverIdent
operator|.
name|get
argument_list|()
decl_stmt|;
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setParentId
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setTreeId
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|parent
argument_list|)
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|ident
argument_list|)
expr_stmt|;
return|return
name|ins
operator|.
name|insert
argument_list|(
name|Constants
operator|.
name|OBJ_COMMIT
argument_list|,
name|cb
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newBatchUpdate (BatchUpdate.Factory buf)
specifier|private
name|BatchUpdate
name|newBatchUpdate
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|buf
parameter_list|)
block|{
return|return
name|buf
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRef (String name)
specifier|private
name|Optional
argument_list|<
name|ObjectId
argument_list|>
name|getRef
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
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
name|project
argument_list|)
init|)
block|{
return|return
name|Optional
operator|.
name|ofNullable
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|Ref
operator|::
name|getObjectId
argument_list|)
return|;
block|}
block|}
DECL|method|getMessages (Change.Id id)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getMessages
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|get
argument_list|(
name|MESSAGES
argument_list|)
operator|.
name|messages
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|m
lambda|->
name|m
operator|.
name|message
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|commitMessages ( Repository repo, ObjectId fromExclusive, ObjectId toInclusive)
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|commitMessages
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ObjectId
name|fromExclusive
parameter_list|,
name|ObjectId
name|toInclusive
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|toInclusive
argument_list|)
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
name|fromExclusive
argument_list|)
argument_list|)
expr_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|)
expr_stmt|;
name|rw
operator|.
name|setRetainBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|Streams
operator|.
name|stream
argument_list|(
name|rw
argument_list|)
operator|.
name|map
argument_list|(
name|RevCommit
operator|::
name|getShortMessage
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|ensureAtomicTransactions (Repository repo)
specifier|private
name|void
name|ensureAtomicTransactions
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|repo
operator|instanceof
name|InMemoryRepository
condition|)
block|{
operator|(
operator|(
name|InMemoryRepository
operator|)
name|repo
operator|)
operator|.
name|setPerformsAtomicTransactions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|performsAtomicTransactions
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"performsAtomicTransactions on %s"
argument_list|,
name|repo
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

