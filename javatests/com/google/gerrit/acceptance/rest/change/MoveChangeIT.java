begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|change
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
name|GitUtil
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
name|NoHttpd
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
name|data
operator|.
name|LabelFunction
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
name|LabelType
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|MoveInput
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
name|api
operator|.
name|projects
operator|.
name|BranchInput
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
name|AuthException
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
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
name|testing
operator|.
name|Util
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
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|MoveChangeIT
specifier|public
class|class
name|MoveChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|moveChangeWithShortRef ()
specifier|public
name|void
name|moveChangeWithShortRef
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a different branch using short ref name
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeWithFullRef ()
specifier|public
name|void
name|moveChangeWithFullRef
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a different branch using full ref name
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeWithMessage ()
specifier|public
name|void
name|moveChangeWithMessage
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Provide a message using --message flag
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|String
name|moveMessage
init|=
literal|"Moving for the move test"
decl_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|,
name|moveMessage
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
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
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|StringBuilder
name|expectedMessage
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|expectedMessage
operator|.
name|append
argument_list|(
literal|"Change destination moved from master to moveTest"
argument_list|)
expr_stmt|;
name|expectedMessage
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|expectedMessage
operator|.
name|append
argument_list|(
name|moveMessage
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|messages
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedMessage
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeToSameRefAsCurrent ()
specifier|public
name|void
name|moveChangeToSameRefAsCurrent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to the branch same as change's destination
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Change is already destined for the specified branch"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
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
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeToSameChangeId ()
specifier|public
name|void
name|moveChangeToSameChangeId
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a branch with existing change with same change ID
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|int
name|changeNum
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|createChange
argument_list|(
name|newBranch
operator|.
name|get
argument_list|()
argument_list|,
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Destination "
operator|+
name|newBranch
operator|.
name|getShortName
argument_list|()
operator|+
literal|" has a different change with same change key "
operator|+
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|changeNum
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeToNonExistentRef ()
specifier|public
name|void
name|moveChangeToNonExistentRef
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a non-existing branch
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"does_not_exist"
argument_list|)
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Destination "
operator|+
name|newBranch
operator|.
name|get
argument_list|()
operator|+
literal|" not found in the project"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveClosedChange ()
specifier|public
name|void
name|moveClosedChange
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move a change which is not open
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|merge
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Change is merged"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveMergeCommitChange ()
specifier|public
name|void
name|moveMergeCommitChange
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move a change which has a merge commit as the current PS
comment|// Create a merge commit and push for review
name|PushOneCommit
operator|.
name|Result
name|r1
init|=
name|createChange
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r2
init|=
name|createChange
argument_list|()
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
operator|.
name|CommitBuilder
name|commitBuilder
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
name|insertChangeId
argument_list|()
decl_stmt|;
name|commitBuilder
operator|.
name|parent
argument_list|(
name|r1
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|parent
argument_list|(
name|r2
operator|.
name|getCommit
argument_list|()
argument_list|)
operator|.
name|message
argument_list|(
literal|"Move change Merge Commit"
argument_list|)
operator|.
name|author
argument_list|(
name|admin
operator|.
name|getIdent
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
name|getIdent
argument_list|()
argument_list|,
name|testRepo
operator|.
name|getDate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RevCommit
name|c
init|=
name|commitBuilder
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Try to move the merge commit to another branch
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r1
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Merge commit cannot be moved"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|GitUtil
operator|.
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeToBranchWithoutUploadPerms ()
specifier|public
name|void
name|moveChangeToBranchWithoutUploadPerms
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a destination where user doesn't have upload permissions
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"blocked_branch"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|block
argument_list|(
literal|"refs/for/"
operator|+
name|newBranch
operator|.
name|get
argument_list|()
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|systemGroupBackend
operator|.
name|getGroup
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"move not permitted"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeFromBranchWithoutAbandonPerms ()
specifier|public
name|void
name|moveChangeFromBranchWithoutAbandonPerms
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change for which user does not have abandon permissions
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
name|block
argument_list|(
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
name|get
argument_list|()
argument_list|,
name|Permission
operator|.
name|ABANDON
argument_list|,
name|systemGroupBackend
operator|.
name|getGroup
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"move not permitted"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeToBranchThatContainsCurrentCommit ()
specifier|public
name|void
name|moveChangeToBranchThatContainsCurrentCommit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change to a branch for which current PS revision is reachable from
comment|// tip
comment|// Create a change
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|int
name|changeNum
init|=
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
comment|// Create a branch with that same commit
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|BranchInput
name|bi
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|bi
operator|.
name|revision
operator|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|newBranch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|create
argument_list|(
name|bi
argument_list|)
expr_stmt|;
comment|// Try to move the change to the branch with the same commit
name|exception
operator|.
name|expect
argument_list|(
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Current patchset revision is reachable from tip of "
operator|+
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|changeNum
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeWithCurrentPatchSetLocked ()
specifier|public
name|void
name|moveChangeWithCurrentPatchSetLocked
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Move change that is locked
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Branch
operator|.
name|NameKey
name|newBranch
init|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
literal|"moveTest"
argument_list|)
decl_stmt|;
name|createBranch
argument_list|(
name|newBranch
argument_list|)
expr_stmt|;
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|LabelType
name|patchSetLock
init|=
name|Util
operator|.
name|patchSetLock
argument_list|()
decl_stmt|;
name|u
operator|.
name|getConfig
argument_list|()
operator|.
name|getLabelSections
argument_list|()
operator|.
name|put
argument_list|(
name|patchSetLock
operator|.
name|getName
argument_list|()
argument_list|,
name|patchSetLock
argument_list|)
expr_stmt|;
name|AccountGroup
operator|.
name|UUID
name|registeredUsers
init|=
name|systemGroupBackend
operator|.
name|getGroup
argument_list|(
name|REGISTERED_USERS
argument_list|)
operator|.
name|getUUID
argument_list|()
decl_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|forLabel
argument_list|(
name|patchSetLock
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
name|registeredUsers
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/heads/*"
argument_list|,
name|Permission
operator|.
name|LABEL
operator|+
literal|"Patch-Set-Lock"
argument_list|)
expr_stmt|;
name|revision
argument_list|(
name|r
argument_list|)
operator|.
name|review
argument_list|(
operator|new
name|ReviewInput
argument_list|()
operator|.
name|label
argument_list|(
literal|"Patch-Set-Lock"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"move not permitted"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|moveChangeOnlyKeepVetoVotes ()
specifier|public
name|void
name|moveChangeOnlyKeepVetoVotes
parameter_list|()
throws|throws
name|Exception
block|{
comment|// A vote for a label will be kept after moving if the label's function is *WithBlock and the
comment|// vote holds the minimum value.
name|createBranch
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|codeReviewLabel
init|=
literal|"Code-Review"
decl_stmt|;
comment|// 'Code-Review' uses 'MaxWithBlock' function.
name|String
name|testLabelA
init|=
literal|"Label-A"
decl_stmt|;
name|String
name|testLabelB
init|=
literal|"Label-B"
decl_stmt|;
name|String
name|testLabelC
init|=
literal|"Label-C"
decl_stmt|;
name|configLabel
argument_list|(
name|testLabelA
argument_list|,
name|LabelFunction
operator|.
name|ANY_WITH_BLOCK
argument_list|)
expr_stmt|;
name|configLabel
argument_list|(
name|testLabelB
argument_list|,
name|LabelFunction
operator|.
name|MAX_NO_BLOCK
argument_list|)
expr_stmt|;
name|configLabel
argument_list|(
name|testLabelC
argument_list|,
name|LabelFunction
operator|.
name|NO_BLOCK
argument_list|)
expr_stmt|;
name|AccountGroup
operator|.
name|UUID
name|registered
init|=
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
decl_stmt|;
try|try
init|(
name|ProjectConfigUpdate
name|u
init|=
name|updateProject
argument_list|(
name|project
argument_list|)
init|)
block|{
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|forLabel
argument_list|(
name|testLabelA
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|,
operator|+
literal|1
argument_list|,
name|registered
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|forLabel
argument_list|(
name|testLabelB
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|,
operator|+
literal|1
argument_list|,
name|registered
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|Util
operator|.
name|allow
argument_list|(
name|u
operator|.
name|getConfig
argument_list|()
argument_list|,
name|Permission
operator|.
name|forLabel
argument_list|(
name|testLabelC
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|,
operator|+
literal|1
argument_list|,
name|registered
argument_list|,
literal|"refs/heads/*"
argument_list|)
expr_stmt|;
name|u
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|reject
argument_list|()
argument_list|)
expr_stmt|;
name|amendChange
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|label
argument_list|(
name|testLabelA
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|input
operator|.
name|label
argument_list|(
name|testLabelB
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|input
operator|.
name|label
argument_list|(
name|testLabelC
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewer
argument_list|(
name|admin
operator|.
name|email
argument_list|)
operator|.
name|votes
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|codeReviewLabel
argument_list|,
name|testLabelA
argument_list|,
name|testLabelB
argument_list|,
name|testLabelC
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewer
argument_list|(
name|admin
operator|.
name|email
argument_list|)
operator|.
name|votes
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// Move the change to the 'foo' branch.
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|branch
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"master"
argument_list|)
expr_stmt|;
name|move
argument_list|(
name|changeId
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|branch
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|// 'Code-Review -2' and 'Label-A -1' will be kept.
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewer
argument_list|(
name|admin
operator|.
name|email
argument_list|)
operator|.
name|votes
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
comment|// Move the change back to 'master'.
name|move
argument_list|(
name|changeId
argument_list|,
literal|"master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|branch
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|reviewer
argument_list|(
name|admin
operator|.
name|email
argument_list|)
operator|.
name|votes
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|(
name|short
operator|)
operator|-
literal|2
argument_list|,
operator|(
name|short
operator|)
operator|-
literal|1
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|move (int changeNum, String destination)
specifier|private
name|void
name|move
parameter_list|(
name|int
name|changeNum
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|RestApiException
block|{
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeNum
argument_list|)
operator|.
name|move
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
DECL|method|move (String changeId, String destination)
specifier|private
name|void
name|move
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|RestApiException
block|{
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|move
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
DECL|method|move (String changeId, String destination, String message)
specifier|private
name|void
name|move
parameter_list|(
name|String
name|changeId
parameter_list|,
name|String
name|destination
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
block|{
name|MoveInput
name|in
init|=
operator|new
name|MoveInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|destinationBranch
operator|=
name|destination
expr_stmt|;
name|in
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|move
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|createChange (String branch, String changeId)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|createChange
parameter_list|(
name|String
name|branch
parameter_list|,
name|String
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
name|changeId
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/"
operator|+
name|branch
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

