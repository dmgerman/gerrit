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
DECL|package|com.google.gerrit.acceptance.api.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
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
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
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
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toSet
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|CheckProjectInput
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
name|CheckProjectInput
operator|.
name|AutoCloseableChangesCheckInput
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
name|CheckProjectResultInfo
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
name|ChangeStatus
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
name|common
operator|.
name|ChangeInfo
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
name|server
operator|.
name|project
operator|.
name|ProjectsConsistencyChecker
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
name|Test
import|;
end_import

begin_class
DECL|class|CheckProjectIT
specifier|public
class|class
name|CheckProjectIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|serverSideTestRepo
specifier|private
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|serverSideTestRepo
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
name|serverSideTestRepo
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
operator|(
name|InMemoryRepository
operator|)
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noProblem ()
specifier|public
name|void
name|noProblem
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
decl_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|detectAutoCloseableChangeByCommit ()
specifier|public
name|void
name|detectAutoCloseableChangeByCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|commit
init|=
name|pushCommitWithoutChangeIdForReview
argument_list|()
decl_stmt|;
name|ChangeInfo
name|change
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
literal|"commit:"
operator|+
name|commit
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
literal|"refs/heads/master"
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|testRepo
operator|.
name|getRevWalk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|commit
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|_number
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|change
operator|.
name|_number
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|_number
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fixAutoCloseableChangeByCommit ()
specifier|public
name|void
name|fixAutoCloseableChangeByCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|commit
init|=
name|pushCommitWithoutChangeIdForReview
argument_list|()
decl_stmt|;
name|ChangeInfo
name|change
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
literal|"commit:"
operator|+
name|commit
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
literal|"refs/heads/master"
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|commit
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|_number
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|fix
operator|=
literal|true
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|change
operator|.
name|_number
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|_number
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|detectAutoCloseableChangeByChangeId ()
specifier|public
name|void
name|detectAutoCloseableChangeByChangeId
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
decl_stmt|;
name|RevCommit
name|amendedCommit
init|=
name|serverSideTestRepo
operator|.
name|amend
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fixAutoCloseableChangeByChangeId ()
specifier|public
name|void
name|fixAutoCloseableChangeByChangeId
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
decl_stmt|;
name|RevCommit
name|amendedCommit
init|=
name|serverSideTestRepo
operator|.
name|amend
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|fix
operator|=
literal|true
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|maxCommits ()
specifier|public
name|void
name|maxCommits
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
decl_stmt|;
name|RevCommit
name|amendedCommit
init|=
name|serverSideTestRepo
operator|.
name|amend
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|serverSideTestRepo
operator|.
name|commit
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|fix
operator|=
literal|true
expr_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|maxCommits
operator|=
literal|1
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|maxCommits
operator|=
literal|2
expr_stmt|;
name|checkResult
operator|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipCommits ()
specifier|public
name|void
name|skipCommits
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|branch
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|branch
argument_list|()
decl_stmt|;
name|RevCommit
name|amendedCommit
init|=
name|serverSideTestRepo
operator|.
name|amend
argument_list|(
name|r
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|serverSideTestRepo
operator|.
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|update
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|serverSideTestRepo
operator|.
name|commit
argument_list|(
name|amendedCommit
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
name|branch
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|fix
operator|=
literal|true
expr_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|maxCommits
operator|=
literal|1
expr_stmt|;
name|CheckProjectResultInfo
name|checkResult
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|NEW
argument_list|)
expr_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|skipCommits
operator|=
literal|1
expr_stmt|;
name|checkResult
operator|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|checkResult
operator|.
name|autoCloseableChangesCheckResult
operator|.
name|autoCloseableChanges
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|_number
argument_list|)
operator|.
name|collect
argument_list|(
name|toSet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|info
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|MERGED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noBranch ()
specifier|public
name|void
name|noBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckProjectInput
name|input
init|=
operator|new
name|CheckProjectInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|=
operator|new
name|AutoCloseableChangesCheckInput
argument_list|()
expr_stmt|;
name|BadRequestException
name|thrown
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"branch is required"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonExistingBranch ()
specifier|public
name|void
name|nonExistingBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
literal|"non-existing"
argument_list|)
decl_stmt|;
name|UnprocessableEntityException
name|thrown
init|=
name|assertThrows
argument_list|(
name|UnprocessableEntityException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"branch 'non-existing' not found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|branchPrefixCanBeOmitted ()
specifier|public
name|void
name|branchPrefixCanBeOmitted
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|setLimitForMaxCommits ()
specifier|public
name|void
name|setLimitForMaxCommits
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|maxCommits
operator|=
name|ProjectsConsistencyChecker
operator|.
name|AUTO_CLOSE_MAX_COMMITS_LIMIT
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|tooLargeMaxCommits ()
specifier|public
name|void
name|tooLargeMaxCommits
parameter_list|()
throws|throws
name|Exception
block|{
name|CheckProjectInput
name|input
init|=
name|checkProjectInputForAutoCloseableCheck
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|maxCommits
operator|=
name|ProjectsConsistencyChecker
operator|.
name|AUTO_CLOSE_MAX_COMMITS_LIMIT
operator|+
literal|1
expr_stmt|;
name|BadRequestException
name|thrown
init|=
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|input
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|contains
argument_list|(
literal|"max commits can at most be set to "
operator|+
name|ProjectsConsistencyChecker
operator|.
name|AUTO_CLOSE_MAX_COMMITS_LIMIT
argument_list|)
expr_stmt|;
block|}
DECL|method|pushCommitWithoutChangeIdForReview ()
specifier|private
name|RevCommit
name|pushCommitWithoutChangeIdForReview
parameter_list|()
throws|throws
name|Exception
block|{
name|setRequireChangeId
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|RevCommit
name|commit
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"A change"
argument_list|)
operator|.
name|author
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|)
operator|.
name|committer
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|testRepo
operator|.
name|getDate
argument_list|()
argument_list|)
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|)
expr_stmt|;
return|return
name|commit
return|;
block|}
DECL|method|checkProjectInputForAutoCloseableCheck (String branch)
specifier|private
specifier|static
name|CheckProjectInput
name|checkProjectInputForAutoCloseableCheck
parameter_list|(
name|String
name|branch
parameter_list|)
block|{
name|CheckProjectInput
name|input
init|=
operator|new
name|CheckProjectInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|=
operator|new
name|AutoCloseableChangesCheckInput
argument_list|()
expr_stmt|;
name|input
operator|.
name|autoCloseableChangesCheck
operator|.
name|branch
operator|=
name|branch
expr_stmt|;
return|return
name|input
return|;
block|}
block|}
end_class

end_unit

