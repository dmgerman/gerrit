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
name|server
operator|.
name|data
operator|.
name|RefUpdateAttribute
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
name|events
operator|.
name|RefEvent
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
name|events
operator|.
name|RefUpdatedEvent
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|SubmitByMergeAlwaysIT
specifier|public
class|class
name|SubmitByMergeAlwaysIT
extends|extends
name|AbstractSubmitByMerge
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
name|MERGE_ALWAYS
return|;
block|}
annotation|@
name|Test
DECL|method|submitWithMergeIfFastForwardPossible ()
specifier|public
name|void
name|submitWithMergeIfFastForwardPossible
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|oldHead
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
name|head
init|=
name|getRemoteHead
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|head
operator|.
name|getParentCount
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
name|head
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldHead
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|head
operator|.
name|getParent
argument_list|(
literal|1
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
name|assertPersonEquals
argument_list|(
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|head
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|head
operator|.
name|getCommitterIdent
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
comment|// Submit a change so that the remote head advances
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
literal|"b"
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
comment|// The remote head should now be a merge of the previous head
comment|// and "Change 2"
name|RevCommit
name|headAfterFirstSubmit
init|=
name|getRemoteLog
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|headAfterFirstSubmit
operator|.
name|getParent
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
name|headAfterFirstSubmit
operator|.
name|getParent
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
name|initialHead
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|headAfterFirstSubmit
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
name|initialHead
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Submit two changes at the same time
name|PushOneCommit
operator|.
name|Result
name|change3
init|=
name|createChange
argument_list|(
literal|"Change 3"
argument_list|,
literal|"c"
argument_list|,
literal|"c"
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|change4
init|=
name|createChange
argument_list|(
literal|"Change 4"
argument_list|,
literal|"d"
argument_list|,
literal|"d"
argument_list|)
decl_stmt|;
name|approve
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
name|submit
argument_list|(
name|change4
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Submitting change 4 should result in change 3 also being submitted
name|assertMerged
argument_list|(
name|change3
operator|.
name|getChangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// The remote head should now be a merge of the new head after
comment|// the previous submit, and "Change 4".
name|RevCommit
name|headAfterSecondSubmit
init|=
name|getRemoteLog
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|headAfterSecondSubmit
operator|.
name|getParent
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
name|change4
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
name|headAfterSecondSubmit
operator|.
name|getParent
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
name|headAfterFirstSubmit
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|headAfterSecondSubmit
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
name|headAfterFirstSubmit
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|headAfterSecondSubmit
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|assertPersonEquals
argument_list|(
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|headAfterSecondSubmit
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
comment|// The two submit operations should have resulted in two ref-update events
name|List
argument_list|<
name|RefEvent
argument_list|>
name|refUpdates
init|=
name|eventRecorder
operator|.
name|getRefUpdates
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|RefUpdateAttribute
name|refUpdate
init|=
operator|(
call|(
name|RefUpdatedEvent
call|)
argument_list|(
name|refUpdates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|)
operator|.
name|refUpdate
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|refUpdate
operator|.
name|oldRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|initialHead
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|refUpdate
operator|.
name|newRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterFirstSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|refUpdate
operator|=
operator|(
call|(
name|RefUpdatedEvent
call|)
argument_list|(
name|refUpdates
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|)
operator|.
name|refUpdate
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|refUpdate
operator|.
name|oldRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterFirstSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|refUpdate
operator|.
name|newRev
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|headAfterSecondSubmit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

