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
name|acceptance
operator|.
name|RestResponse
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
name|RestSession
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
name|ReviewerState
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
name|AccountInfo
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
name|common
operator|.
name|LabelInfo
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
name|ResourceNotFoundException
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
name|testutil
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
name|gerrit
operator|.
name|testutil
operator|.
name|NoteDbMode
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
name|Collection
import|;
end_import

begin_class
DECL|class|DraftChangeIT
specifier|public
class|class
name|DraftChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|allowDraftsDisabled ()
specifier|public
specifier|static
name|Config
name|allowDraftsDisabled
parameter_list|()
block|{
return|return
name|allowDraftsDisabledConfig
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|deleteChange ()
specifier|public
name|void
name|deleteChange
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|changeId
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|triplet
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
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
name|RestResponse
name|response
init|=
name|deleteChange
argument_list|(
name|changeId
argument_list|,
name|adminSession
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Change is not a draft: "
operator|+
name|c
operator|.
name|_number
argument_list|)
expr_stmt|;
name|response
operator|.
name|assertConflict
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDraftChange ()
specifier|public
name|void
name|deleteDraftChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|changeId
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|triplet
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|deleteChange
argument_list|(
name|changeId
argument_list|,
name|adminSession
argument_list|)
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|publishDraftChange ()
specifier|public
name|void
name|publishDraftChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|changeId
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|triplet
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|revisions
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentRevision
argument_list|)
operator|.
name|draft
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|publishChange
argument_list|(
name|changeId
argument_list|)
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
name|c
operator|=
name|get
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
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
name|assertThat
argument_list|(
name|c
operator|.
name|revisions
operator|.
name|get
argument_list|(
name|c
operator|.
name|currentRevision
argument_list|)
operator|.
name|draft
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|publishDraftPatchSet ()
specifier|public
name|void
name|publishDraftPatchSet
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|changeId
decl_stmt|;
name|ChangeInfo
name|c
init|=
name|get
argument_list|(
name|triplet
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|id
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|triplet
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|c
operator|.
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
name|publishPatchSet
argument_list|(
name|changeId
argument_list|)
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|get
argument_list|(
name|triplet
argument_list|)
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
DECL|method|createDraftChangeWhenDraftsNotAllowed ()
specifier|public
name|void
name|createDraftChangeWhenDraftsNotAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|r
operator|.
name|assertErrorStatus
argument_list|(
literal|"draft workflow is disabled"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listApprovalsOnDraftChange ()
specifier|public
name|void
name|listApprovalsOnDraftChange
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isAllowDrafts
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|triplet
init|=
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"~master~"
operator|+
name|changeId
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|triplet
argument_list|)
operator|.
name|addReviewer
argument_list|(
name|user
operator|.
name|fullName
argument_list|)
expr_stmt|;
name|ChangeInfo
name|info
init|=
name|get
argument_list|(
name|triplet
argument_list|)
decl_stmt|;
name|LabelInfo
name|label
init|=
name|info
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_accountId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|user
operator|.
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|value
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ReviewerState
name|rs
init|=
name|NoteDbMode
operator|.
name|readWrite
argument_list|()
condition|?
name|ReviewerState
operator|.
name|REVIEWER
else|:
name|ReviewerState
operator|.
name|CC
decl_stmt|;
name|Collection
argument_list|<
name|AccountInfo
argument_list|>
name|ccs
init|=
name|info
operator|.
name|reviewers
operator|.
name|get
argument_list|(
name|rs
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ccs
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ccs
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|_accountId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|user
operator|.
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|triplet
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|recommend
argument_list|()
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|label
operator|=
name|get
argument_list|(
name|triplet
argument_list|)
operator|.
name|labels
operator|.
name|get
argument_list|(
literal|"Code-Review"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|_accountId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|user
operator|.
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|label
operator|.
name|all
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|value
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteChange (String changeId, RestSession s)
specifier|private
specifier|static
name|RestResponse
name|deleteChange
parameter_list|(
name|String
name|changeId
parameter_list|,
name|RestSession
name|s
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|s
operator|.
name|delete
argument_list|(
literal|"/changes/"
operator|+
name|changeId
argument_list|)
return|;
block|}
DECL|method|publishChange (String changeId)
specifier|private
name|RestResponse
name|publishChange
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/publish"
argument_list|)
return|;
block|}
DECL|method|publishPatchSet (String changeId)
specifier|private
name|RestResponse
name|publishPatchSet
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
name|PatchSet
name|patchSet
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byKeyPrefix
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
return|return
name|adminSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/revisions/"
operator|+
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/publish"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

