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
name|ActionInfo
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|transport
operator|.
name|PushResult
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
DECL|class|SubmitByFastForwardIT
specifier|public
class|class
name|SubmitByFastForwardIT
extends|extends
name|AbstractSubmit
block|{
annotation|@
name|Override
DECL|method|getSubmitType ()
specifier|protected
name|SubmitType
name|getSubmitType
parameter_list|()
block|{
return|return
name|SubmitType
operator|.
name|FAST_FORWARD_ONLY
return|;
block|}
annotation|@
name|Test
DECL|method|submitWithFastForward ()
specifier|public
name|void
name|submitWithFastForward
parameter_list|()
throws|throws
name|Throwable
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|updatedHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|updatedHead
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|updatedHead
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|updatedHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|updatedHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitMultipleChangesWithFastForward ()
specifier|public
name|void
name|submitMultipleChangesWithFastForward
parameter_list|()
throws|throws
name|Throwable
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|change
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|id2
init|=
name|change2
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|id3
init|=
name|change3
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|approve
argument_list|(
name|id1
argument_list|)
expr_stmt|;
name|approve
argument_list|(
name|id2
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|id3
argument_list|)
expr_stmt|;
name|RevCommit
name|updatedHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|updatedHead
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change3
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|updatedHead
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change2
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|updatedHead
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|updatedHead
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|assertSubmittedTogether
argument_list|(
name|id1
argument_list|,
name|id3
argument_list|,
name|id2
argument_list|,
name|id1
argument_list|)
expr_stmt|;
name|assertSubmittedTogether
argument_list|(
name|id2
argument_list|,
name|id3
argument_list|,
name|id2
argument_list|,
name|id1
argument_list|)
expr_stmt|;
name|assertSubmittedTogether
argument_list|(
name|id3
argument_list|,
name|id3
argument_list|,
name|id2
argument_list|,
name|id1
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|updatedHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|id1
argument_list|,
name|updatedHead
operator|.
name|name
argument_list|()
argument_list|,
name|id2
argument_list|,
name|updatedHead
operator|.
name|name
argument_list|()
argument_list|,
name|id3
argument_list|,
name|updatedHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitTwoChangesWithFastForward_missingDependency ()
specifier|public
name|void
name|submitTwoChangesWithFastForward_missingDependency
parameter_list|()
throws|throws
name|Throwable
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change1
init|=
name|createChange
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|id1
init|=
name|change1
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|changeId
argument_list|()
decl_stmt|;
name|submitWithConflict
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|"Failed to submit 2 changes due to the following problems:\n"
operator|+
literal|"Change "
operator|+
name|id1
operator|+
literal|": needs Code-Review"
argument_list|)
expr_stmt|;
name|RevCommit
name|updatedHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|updatedHead
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|initialHead
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|()
expr_stmt|;
name|assertChangeMergedEvents
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitFastForwardNotPossible_Conflict ()
specifier|public
name|void
name|submitFastForwardNotPossible_Conflict
parameter_list|()
throws|throws
name|Throwable
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|(
literal|"Change 1"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"content"
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterFirstSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"b.txt"
argument_list|,
literal|"other content"
argument_list|)
decl_stmt|;
name|approve
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ActionInfo
argument_list|>
name|actions
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
literal|1
argument_list|)
operator|.
name|actions
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|actions
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"submit"
argument_list|)
expr_stmt|;
name|ActionInfo
name|info
init|=
name|actions
operator|.
name|get
argument_list|(
literal|"submit"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|enabled
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|submitWithConflict
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|"Failed to submit 1 change due to the following problems:\n"
operator|+
literal|"Change "
operator|+
name|change2
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|": Project policy requires "
operator|+
literal|"all submissions to be a fast-forward. Please rebase the change "
operator|+
literal|"locally and upload again for review."
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterFirstSubmit
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|headAfterFirstSubmit
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterFirstSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitSameCommitsAsInExperimentalBranch ()
specifier|public
name|void
name|submitSameCommitsAsInExperimentalBranch
parameter_list|()
throws|throws
name|Throwable
block|{
name|RevCommit
name|initialHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/heads/*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|project
argument_list|,
literal|"refs/heads/experimental"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|)
expr_stmt|;
name|RevCommit
name|c1
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|message
argument_list|(
literal|"commit at tip"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id1
init|=
name|GitUtil
operator|.
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c1
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|PushResult
name|r1
init|=
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r1
operator|.
name|getRemoteUpdate
argument_list|(
literal|"refs/for/master"
argument_list|)
operator|.
name|getNewObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|PushResult
name|r2
init|=
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/experimental"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r2
operator|.
name|getRemoteUpdate
argument_list|(
literal|"refs/heads/experimental"
argument_list|)
operator|.
name|getNewObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|id1
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|c1
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertSubmitter
argument_list|(
name|id1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|headAfterSubmit
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|id1
argument_list|,
name|headAfterSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

