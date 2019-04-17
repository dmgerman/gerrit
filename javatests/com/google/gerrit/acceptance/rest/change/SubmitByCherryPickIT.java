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
name|extensions
operator|.
name|client
operator|.
name|ListChangesOption
operator|.
name|CURRENT_REVISION
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
name|acceptance
operator|.
name|TestProjectInput
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
name|FooterConstants
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
name|SubmitInput
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
name|client
operator|.
name|ListChangesOption
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
name|registration
operator|.
name|DynamicSet
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
name|registration
operator|.
name|RegistrationHandle
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
name|ChangeMessageModifier
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
name|submit
operator|.
name|CommitMergeStatus
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
DECL|class|SubmitByCherryPickIT
specifier|public
class|class
name|SubmitByCherryPickIT
extends|extends
name|AbstractSubmit
block|{
DECL|field|changeMessageModifiers
annotation|@
name|Inject
specifier|private
name|DynamicSet
argument_list|<
name|ChangeMessageModifier
argument_list|>
name|changeMessageModifiers
decl_stmt|;
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
name|CHERRY_PICK
return|;
block|}
annotation|@
name|Test
DECL|method|submitWithCherryPickIfFastForwardPossible ()
specifier|public
name|void
name|submitWithCherryPickIfFastForwardPossible
parameter_list|()
throws|throws
name|Exception
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
name|assertCherryPick
argument_list|(
name|testRepo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RevCommit
name|newHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|newHead
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change
operator|.
name|getCommit
argument_list|()
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|newHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitWithCherryPick ()
specifier|public
name|void
name|submitWithCherryPick
parameter_list|()
throws|throws
name|Exception
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
name|submit
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertCherryPick
argument_list|(
name|testRepo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RevCommit
name|newHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|newHead
operator|.
name|getParentCount
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|newHead
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterFirstSubmit
argument_list|)
expr_stmt|;
name|assertCurrentRevision
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|,
name|newHead
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
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|admin
operator|.
name|newIdent
argument_list|()
argument_list|,
name|newHead
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
name|newHead
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|newHead
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
argument_list|,
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeMessageOnSubmit ()
specifier|public
name|void
name|changeMessageOnSubmit
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|change
init|=
name|createChange
argument_list|()
decl_stmt|;
name|RegistrationHandle
name|handle
init|=
name|changeMessageModifiers
operator|.
name|add
argument_list|(
literal|"gerrit"
argument_list|,
parameter_list|(
name|newCommitMessage
parameter_list|,
name|original
parameter_list|,
name|mergeTip
parameter_list|,
name|destination
parameter_list|)
lambda|->
name|newCommitMessage
operator|+
literal|"Custom: "
operator|+
name|destination
operator|.
name|branch
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|handle
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|fetch
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|get
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|CURRENT_REVISION
argument_list|)
decl_stmt|;
name|RevCommit
name|c
init|=
name|testRepo
operator|.
name|getRevWalk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|info
operator|.
name|currentRevision
argument_list|)
argument_list|)
decl_stmt|;
name|testRepo
operator|.
name|getRevWalk
argument_list|()
operator|.
name|parseBody
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getFooterLines
argument_list|(
literal|"Custom"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|getFooterLines
argument_list|(
name|FooterConstants
operator|.
name|REVIEWED_ON
argument_list|)
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|useContentMerge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
DECL|method|submitWithContentMerge ()
specifier|public
name|void
name|submitWithContentMerge
parameter_list|()
throws|throws
name|Exception
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
literal|"aaa\nbbb\nccc\n"
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
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"aaa\nbbb\nccc\nddd\n"
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterSecondSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|change
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"bbb\nccc\n"
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertCherryPick
argument_list|(
name|testRepo
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterThirdSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|headAfterThirdSubmit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterSecondSubmit
argument_list|)
expr_stmt|;
name|assertApproved
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertCurrentRevision
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|,
name|headAfterThirdSubmit
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
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterSecondSubmit
argument_list|,
name|headAfterSecondSubmit
argument_list|,
name|headAfterThirdSubmit
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
argument_list|,
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterSecondSubmit
operator|.
name|name
argument_list|()
argument_list|,
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterThirdSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|useContentMerge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
DECL|method|submitWithContentMerge_Conflict ()
specifier|public
name|void
name|submitWithContentMerge_Conflict
parameter_list|()
throws|throws
name|Exception
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
name|newHead
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
literal|"a.txt"
argument_list|,
literal|"other content"
argument_list|)
decl_stmt|;
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
literal|": Change could not be "
operator|+
literal|"merged due to a path conflict. Please rebase the change locally and "
operator|+
literal|"upload the rebased commit for review."
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
name|newHead
argument_list|)
expr_stmt|;
name|assertCurrentRevision
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|,
name|change2
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoSubmitter
argument_list|(
name|change2
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
name|newHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitOutOfOrder ()
specifier|public
name|void
name|submitOutOfOrder
parameter_list|()
throws|throws
name|Exception
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
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"b.txt"
argument_list|,
literal|"other content"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"c.txt"
argument_list|,
literal|"different content"
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertCherryPick
argument_list|(
name|testRepo
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterSecondSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|headAfterSecondSubmit
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterFirstSubmit
argument_list|)
expr_stmt|;
name|assertApproved
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertCurrentRevision
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|,
name|headAfterSecondSubmit
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
name|assertSubmitter
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterSecondSubmit
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
argument_list|,
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterSecondSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitOutOfOrder_Conflict ()
specifier|public
name|void
name|submitOutOfOrder_Conflict
parameter_list|()
throws|throws
name|Exception
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
name|newHead
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
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"b.txt"
argument_list|,
literal|"other content"
argument_list|)
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"b.txt"
argument_list|,
literal|"different content"
argument_list|)
decl_stmt|;
name|submitWithConflict
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|"Failed to submit 1 change due to the following problems:\n"
operator|+
literal|"Change "
operator|+
name|change3
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|": Change could not be "
operator|+
literal|"merged due to a path conflict. Please rebase the change locally and "
operator|+
literal|"upload the rebased commit for review."
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
name|newHead
argument_list|)
expr_stmt|;
name|assertCurrentRevision
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
argument_list|,
name|change3
operator|.
name|getCommit
argument_list|()
argument_list|)
expr_stmt|;
name|assertNoSubmitter
argument_list|(
name|change3
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
name|newHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitMultipleChanges ()
specifier|public
name|void
name|submitMultipleChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
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
name|change
init|=
name|createChange
argument_list|(
literal|"Change 1"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
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
literal|"c"
argument_list|,
literal|"c"
argument_list|)
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
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"d"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|approve
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|approve
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RevCommit
argument_list|>
name|log
init|=
name|getRemoteLog
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change3
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
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
name|assertNew
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNew
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitDependentNonConflictingChangesOutOfOrder ()
specifier|public
name|void
name|submitDependentNonConflictingChangesOutOfOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
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
name|change
init|=
name|createChange
argument_list|(
literal|"Change 1"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|change2
operator|.
name|getCommit
argument_list|()
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
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
comment|// Submit succeeds; change2 is successfully cherry-picked onto head.
name|submit
argument_list|(
name|change2
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
comment|// Submit succeeds; change is successfully cherry-picked onto head
comment|// (which was change2's cherry-pick).
name|submit
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|headAfterSecondSubmit
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
comment|// change is the new tip.
name|List
argument_list|<
name|RevCommit
argument_list|>
name|log
init|=
name|getRemoteLog
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|change2
operator|.
name|getCommit
argument_list|()
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|2
argument_list|)
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
argument_list|(
name|initialHead
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterFirstSubmit
argument_list|,
name|headAfterSecondSubmit
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterFirstSubmit
operator|.
name|name
argument_list|()
argument_list|,
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterSecondSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|submitDependentConflictingChangesOutOfOrder ()
specifier|public
name|void
name|submitDependentConflictingChangesOutOfOrder
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
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
name|change
init|=
name|createChange
argument_list|(
literal|"Change 1"
argument_list|,
literal|"b"
argument_list|,
literal|"b1"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"b"
argument_list|,
literal|"b2"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|change2
operator|.
name|getCommit
argument_list|()
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
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
comment|// Submit fails; change2 contains the delta "b1" -> "b2", which cannot be
comment|// applied against tip.
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
literal|": Change could not be "
operator|+
literal|"merged due to a path conflict. Please rebase the change locally and "
operator|+
literal|"upload the rebased commit for review."
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info3
init|=
name|get
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|ListChangesOption
operator|.
name|MESSAGES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info3
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
comment|// Tip has not changed.
name|List
argument_list|<
name|RevCommit
argument_list|>
name|log
init|=
name|getRemoteLog
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|assertNoSubmitter
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
literal|1
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
DECL|method|submitSubsetOfDependentChanges ()
specifier|public
name|void
name|submitSubsetOfDependentChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
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
name|change
init|=
name|createChange
argument_list|(
literal|"Change 1"
argument_list|,
literal|"b"
argument_list|,
literal|"b"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change2
init|=
name|createChange
argument_list|(
literal|"Change 2"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"e"
argument_list|,
literal|"e"
argument_list|)
decl_stmt|;
comment|// Out of the above, only submit change 3. Changes 1 and 2 are not
comment|// related to change 3 by topic or ancestor (due to cherrypicking!)
name|approve
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|RevCommit
name|newHead
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertNew
argument_list|(
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertNew
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefUpdatedEvents
argument_list|(
name|initialHead
argument_list|,
name|newHead
argument_list|)
expr_stmt|;
name|assertChangeMergedEvents
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|newHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|useContentMerge
operator|=
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
DECL|method|submitIdenticalTree ()
specifier|public
name|void
name|submitIdenticalTree
parameter_list|()
throws|throws
name|Exception
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
argument_list|(
literal|"Change 1"
argument_list|,
literal|"a.txt"
argument_list|,
literal|"a"
argument_list|)
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
literal|"a.txt"
argument_list|,
literal|"a"
argument_list|)
decl_stmt|;
name|submit
argument_list|(
name|change1
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
name|assertThat
argument_list|(
name|headAfterFirstSubmit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Change 1"
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
operator|new
name|SubmitInput
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
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
name|ChangeInfo
name|info2
init|=
name|get
argument_list|(
name|change2
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|MESSAGES
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info2
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
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getLast
argument_list|(
name|info2
operator|.
name|messages
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|CommitMergeStatus
operator|.
name|SKIPPED_IDENTICAL_TREE
operator|.
name|getDescription
argument_list|()
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
name|change1
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|headAfterFirstSubmit
operator|.
name|name
argument_list|()
argument_list|,
name|change2
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
block|}
end_class

end_unit

