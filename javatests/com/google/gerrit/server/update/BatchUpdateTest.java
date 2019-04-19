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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|Truth
operator|.
name|assert_
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
name|SubmitRecord
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
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
name|change
operator|.
name|ChangeInserter
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
name|change
operator|.
name|PatchSetInserter
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
name|logging
operator|.
name|RequestId
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
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|ChangeUpdate
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
name|notedb
operator|.
name|NoteDbUpdateManager
operator|.
name|TooManyUpdatesException
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
name|notedb
operator|.
name|Sequences
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
name|time
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
name|testing
operator|.
name|GerritBaseTests
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
name|InMemoryTestEnvironment
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
name|junit
operator|.
name|TestRepository
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
name|Rule
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
DECL|class|BatchUpdateTest
specifier|public
class|class
name|BatchUpdateTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|MAX_UPDATES
specifier|private
specifier|static
specifier|final
name|int
name|MAX_UPDATES
init|=
literal|4
decl_stmt|;
annotation|@
name|Rule
DECL|field|testEnvironment
specifier|public
name|InMemoryTestEnvironment
name|testEnvironment
init|=
operator|new
name|InMemoryTestEnvironment
argument_list|(
parameter_list|()
lambda|->
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setInt
argument_list|(
literal|"change"
argument_list|,
literal|null
argument_list|,
literal|"maxUpdates"
argument_list|,
name|MAX_UPDATES
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
argument_list|)
decl_stmt|;
DECL|field|batchUpdateFactory
annotation|@
name|Inject
specifier|private
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|changeInserterFactory
annotation|@
name|Inject
specifier|private
name|ChangeInserter
operator|.
name|Factory
name|changeInserterFactory
decl_stmt|;
DECL|field|changeNotesFactory
annotation|@
name|Inject
specifier|private
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|repoManager
annotation|@
name|Inject
specifier|private
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|patchSetInserterFactory
annotation|@
name|Inject
specifier|private
name|PatchSetInserter
operator|.
name|Factory
name|patchSetInserterFactory
decl_stmt|;
DECL|field|user
annotation|@
name|Inject
specifier|private
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|sequences
annotation|@
name|Inject
specifier|private
name|Sequences
name|sequences
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|repo
specifier|private
name|TestRepository
argument_list|<
name|Repository
argument_list|>
name|repo
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
name|project
operator|=
name|Project
operator|.
name|nameKey
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Repository
name|inMemoryRepo
init|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|repo
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|inMemoryRepo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addRefUpdateFromFastForwardCommit ()
specifier|public
name|void
name|addRefUpdateFromFastForwardCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|masterCommit
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"master"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|RevCommit
name|branchCommit
init|=
name|repo
operator|.
name|branch
argument_list|(
literal|"branch"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|parent
argument_list|(
name|masterCommit
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|addRepoOnlyOp
argument_list|(
operator|new
name|RepoOnlyOp
argument_list|()
block|{
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
name|Exception
block|{
name|ctx
operator|.
name|addRefUpdate
argument_list|(
name|masterCommit
operator|.
name|getId
argument_list|()
argument_list|,
name|branchCommit
operator|.
name|getId
argument_list|()
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|repo
operator|.
name|getRepository
argument_list|()
operator|.
name|exactRef
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|branchCommit
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotExceedMaxUpdates ()
specifier|public
name|void
name|cannotExceedMaxUpdates
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|MAX_UPDATES
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|AddMessageOp
argument_list|(
literal|"Excessive update"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assert_
argument_list|()
operator|.
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
name|TooManyUpdatesException
operator|.
name|message
argument_list|(
name|id
argument_list|,
name|MAX_UPDATES
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotExceedMaxUpdatesCountingMultipleChangeUpdatesInSingleBatch ()
specifier|public
name|void
name|cannotExceedMaxUpdatesCountingMultipleChangeUpdatesInSingleBatch
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithTwoPatchSets
argument_list|(
name|MAX_UPDATES
operator|-
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|AddMessageOp
argument_list|(
literal|"Update on PS1"
argument_list|,
name|PatchSet
operator|.
name|id
argument_list|(
name|id
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|AddMessageOp
argument_list|(
literal|"Update on PS2"
argument_list|,
name|PatchSet
operator|.
name|id
argument_list|(
name|id
argument_list|,
literal|2
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
name|assert_
argument_list|()
operator|.
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
name|TooManyUpdatesException
operator|.
name|message
argument_list|(
name|id
argument_list|,
name|MAX_UPDATES
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceedingMaxUpdatesAllowedWithCompleteNoOp ()
specifier|public
name|void
name|exceedingMaxUpdatesAllowedWithCompleteNoOp
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|MAX_UPDATES
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
block|{
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceedingMaxUpdatesAllowedWithNoOpAfterPopulatingUpdate ()
specifier|public
name|void
name|exceedingMaxUpdatesAllowedWithNoOpAfterPopulatingUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|MAX_UPDATES
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
literal|"No-op"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceedingMaxUpdatesAllowedWithSubmit ()
specifier|public
name|void
name|exceedingMaxUpdatesAllowedWithSubmit
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|MAX_UPDATES
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|SubmitOp
argument_list|()
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
operator|+
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exceedingMaxUpdatesAllowedWithSubmitAfterOtherOp ()
specifier|public
name|void
name|exceedingMaxUpdatesAllowedWithSubmitAfterOtherOp
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithTwoPatchSets
argument_list|(
name|MAX_UPDATES
operator|-
literal|1
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|AddMessageOp
argument_list|(
literal|"Message on PS1"
argument_list|,
name|PatchSet
operator|.
name|id
argument_list|(
name|id
argument_list|,
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|SubmitOp
argument_list|()
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
operator|+
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
comment|// Not possible to write a variant of this test that submits first and adds a message second in
comment|// the same batch, since submit always comes last.
annotation|@
name|Test
DECL|method|exceedingMaxUpdatesAllowedWithAbandon ()
specifier|public
name|void
name|exceedingMaxUpdatesAllowedWithAbandon
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|MAX_UPDATES
argument_list|)
decl_stmt|;
name|ObjectId
name|oldMetaId
init|=
name|getMetaId
argument_list|(
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
block|{
name|ChangeUpdate
name|update
init|=
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
decl_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Abandon"
argument_list|)
expr_stmt|;
name|update
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MAX_UPDATES
operator|+
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getMetaId
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|oldMetaId
argument_list|)
expr_stmt|;
block|}
DECL|method|createChangeWithUpdates (int totalUpdates)
specifier|private
name|Change
operator|.
name|Id
name|createChangeWithUpdates
parameter_list|(
name|int
name|totalUpdates
parameter_list|)
throws|throws
name|Exception
block|{
name|checkArgument
argument_list|(
name|totalUpdates
operator|>
literal|0
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|totalUpdates
operator|<=
name|MAX_UPDATES
argument_list|)
expr_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|sequences
operator|.
name|nextChangeId
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|insertChange
argument_list|(
name|changeInserterFactory
operator|.
name|create
argument_list|(
name|id
argument_list|,
name|repo
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"Change"
argument_list|)
operator|.
name|insertChangeId
argument_list|()
operator|.
name|create
argument_list|()
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|2
init|;
name|i
operator|<=
name|totalUpdates
condition|;
name|i
operator|++
control|)
block|{
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|AddMessageOp
argument_list|(
literal|"Update "
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|totalUpdates
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
DECL|method|createChangeWithTwoPatchSets (int totalUpdates)
specifier|private
name|Change
operator|.
name|Id
name|createChangeWithTwoPatchSets
parameter_list|(
name|int
name|totalUpdates
parameter_list|)
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|createChangeWithUpdates
argument_list|(
name|totalUpdates
operator|-
literal|1
argument_list|)
decl_stmt|;
name|ChangeNotes
name|notes
init|=
name|changeNotesFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|id
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|ObjectId
name|commitId
init|=
name|repo
operator|.
name|amend
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|notes
operator|.
name|getCurrentPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
operator|.
name|message
argument_list|(
literal|"PS2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|patchSetInserterFactory
operator|.
name|create
argument_list|(
name|notes
argument_list|,
name|PatchSet
operator|.
name|id
argument_list|(
name|id
argument_list|,
literal|2
argument_list|)
argument_list|,
name|commitId
argument_list|)
operator|.
name|setMessage
argument_list|(
literal|"Add PS2"
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|getUpdateCount
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|totalUpdates
argument_list|)
expr_stmt|;
return|return
name|id
return|;
block|}
DECL|class|AddMessageOp
specifier|private
specifier|static
class|class
name|AddMessageOp
implements|implements
name|BatchUpdateOp
block|{
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|field|psId
annotation|@
name|Nullable
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|method|AddMessageOp (String message)
name|AddMessageOp
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AddMessageOp (String message, PatchSet.Id psId)
name|AddMessageOp
parameter_list|(
name|String
name|message
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|psId
operator|=
name|psId
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
name|PatchSet
operator|.
name|Id
name|psIdToUpdate
init|=
name|psId
decl_stmt|;
if|if
condition|(
name|psIdToUpdate
operator|==
literal|null
condition|)
block|{
name|psIdToUpdate
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|checkState
argument_list|(
name|ctx
operator|.
name|getNotes
argument_list|()
operator|.
name|getPatchSets
argument_list|()
operator|.
name|containsKey
argument_list|(
name|psIdToUpdate
argument_list|)
argument_list|,
literal|"%s not in %s"
argument_list|,
name|psIdToUpdate
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
operator|.
name|getPatchSets
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psIdToUpdate
argument_list|)
operator|.
name|setChangeMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|getUpdateCount (Change.Id changeId)
specifier|private
name|int
name|getUpdateCount
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|changeNotesFactory
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|)
operator|.
name|getUpdateCount
argument_list|()
return|;
block|}
DECL|method|getMetaId (Change.Id changeId)
specifier|private
name|ObjectId
name|getMetaId
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|repo
operator|.
name|getRepository
argument_list|()
operator|.
name|exactRef
argument_list|(
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|getObjectId
argument_list|()
return|;
block|}
DECL|class|SubmitOp
specifier|private
specifier|static
class|class
name|SubmitOp
implements|implements
name|BatchUpdateOp
block|{
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
name|SubmitRecord
name|sr
init|=
operator|new
name|SubmitRecord
argument_list|()
decl_stmt|;
name|sr
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|SubmitRecord
operator|.
name|Label
name|cr
init|=
operator|new
name|SubmitRecord
operator|.
name|Label
argument_list|()
decl_stmt|;
name|cr
operator|.
name|status
operator|=
name|SubmitRecord
operator|.
name|Label
operator|.
name|Status
operator|.
name|OK
expr_stmt|;
name|cr
operator|.
name|appliedBy
operator|=
name|ctx
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
name|cr
operator|.
name|label
operator|=
literal|"Code-Review"
expr_stmt|;
name|sr
operator|.
name|labels
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|cr
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
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
decl_stmt|;
name|update
operator|.
name|merge
argument_list|(
operator|new
name|RequestId
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|sr
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
literal|"Submitted"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

