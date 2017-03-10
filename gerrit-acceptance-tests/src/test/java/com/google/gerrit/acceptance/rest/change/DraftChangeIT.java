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
name|common
operator|.
name|RevisionInfo
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
name|MethodNotAllowedException
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
name|server
operator|.
name|notedb
operator|.
name|PatchSetState
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
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
DECL|field|updateFactory
annotation|@
name|Inject
specifier|private
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
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
name|adminRestSession
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
DECL|method|deleteDraftChangeOfAnotherUser ()
specifier|public
name|void
name|deleteDraftChangeOfAnotherUser
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
name|changeResult
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|changeResult
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|String
name|changeId
init|=
name|changeResult
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|changeResult
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// The user needs to be able to see the draft change (which reviewers can).
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
name|addReviewer
argument_list|(
name|user
operator|.
name|fullName
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
name|String
operator|.
name|format
argument_list|(
literal|"Deleting change %s is not permitted"
argument_list|,
name|id
argument_list|)
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
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|cloneAs
operator|=
literal|"user"
argument_list|)
DECL|method|deleteDraftChangeWhenDraftsNotAllowedAsNormalUser ()
specifier|public
name|void
name|deleteDraftChangeWhenDraftsNotAllowedAsNormalUser
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
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// We can't create a draft change while the draft workflow is disabled.
comment|// For this reason, we create a normal change and modify the database.
name|PushOneCommit
operator|.
name|Result
name|changeResult
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|changeResult
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|markChangeAsDraft
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setDraftStatusOfPatchSetsOfChange
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|changeId
init|=
name|changeResult
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Draft workflow is disabled"
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
name|delete
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|cloneAs
operator|=
literal|"user"
argument_list|)
DECL|method|deleteDraftChangeWhenDraftsNotAllowedAsAdmin ()
specifier|public
name|void
name|deleteDraftChangeWhenDraftsNotAllowedAsAdmin
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
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// We can't create a draft change while the draft workflow is disabled.
comment|// For this reason, we create a normal change and modify the database.
name|PushOneCommit
operator|.
name|Result
name|changeResult
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|user
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|)
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|changeResult
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|markChangeAsDraft
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setDraftStatusOfPatchSetsOfChange
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|changeId
init|=
name|changeResult
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
comment|// Grant those permissions to admins.
name|grant
argument_list|(
name|Permission
operator|.
name|VIEW_DRAFTS
argument_list|,
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|grant
argument_list|(
name|Permission
operator|.
name|DELETE_DRAFTS
argument_list|,
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
try|try
block|{
name|setApiUser
argument_list|(
name|admin
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
name|delete
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|removePermission
argument_list|(
name|Permission
operator|.
name|DELETE_DRAFTS
argument_list|,
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|removePermission
argument_list|(
name|Permission
operator|.
name|VIEW_DRAFTS
argument_list|,
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
block|}
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|query
argument_list|(
name|changeId
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteDraftChangeWithNonDraftPatchSet ()
specifier|public
name|void
name|deleteDraftChangeWithNonDraftPatchSet
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
name|changeResult
init|=
name|createDraftChange
argument_list|()
decl_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|changeResult
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|setDraftStatusOfPatchSetsOfChange
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|String
name|changeId
init|=
name|changeResult
operator|.
name|getChangeId
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
name|String
operator|.
name|format
argument_list|(
literal|"Cannot delete draft change %s: patch set 1 is not a draft"
argument_list|,
name|id
argument_list|)
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
name|delete
argument_list|()
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
name|ReviewerState
operator|.
name|REVIEWER
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
name|adminRestSession
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
name|adminRestSession
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
DECL|method|markChangeAsDraft (Change.Id id)
specifier|private
name|void
name|markChangeAsDraft
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|BatchUpdate
name|batchUpdate
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|atrScope
operator|.
name|get
argument_list|()
operator|.
name|getUser
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|batchUpdate
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|MarkChangeAsDraftUpdateOp
argument_list|()
argument_list|)
expr_stmt|;
name|batchUpdate
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|ChangeStatus
name|changeStatus
init|=
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
argument_list|()
operator|.
name|status
decl_stmt|;
name|assertThat
argument_list|(
name|changeStatus
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeStatus
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
block|}
DECL|method|setDraftStatusOfPatchSetsOfChange (Change.Id id, boolean draftStatus)
specifier|private
name|void
name|setDraftStatusOfPatchSetsOfChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|boolean
name|draftStatus
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|BatchUpdate
name|batchUpdate
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|project
argument_list|,
name|atrScope
operator|.
name|get
argument_list|()
operator|.
name|getUser
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|batchUpdate
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
operator|new
name|DraftStatusOfPatchSetsUpdateOp
argument_list|(
name|draftStatus
argument_list|)
argument_list|)
expr_stmt|;
name|batchUpdate
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|Boolean
name|expectedDraftStatus
init|=
name|draftStatus
condition|?
name|Boolean
operator|.
name|TRUE
else|:
literal|null
decl_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|patchSetDraftStatuses
init|=
name|getPatchSetDraftStatuses
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|patchSetDraftStatuses
operator|.
name|forEach
argument_list|(
name|status
lambda|->
name|assertThat
argument_list|(
name|status
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedDraftStatus
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getPatchSetDraftStatuses (Change.Id id)
specifier|private
name|List
argument_list|<
name|Boolean
argument_list|>
name|getPatchSetDraftStatuses
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|RevisionInfo
argument_list|>
name|revisionInfos
init|=
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
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|ALL_REVISIONS
argument_list|)
argument_list|)
operator|.
name|revisions
operator|.
name|values
argument_list|()
decl_stmt|;
return|return
name|revisionInfos
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|revisionInfo
lambda|->
name|revisionInfo
operator|.
name|draft
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|class|MarkChangeAsDraftUpdateOp
specifier|private
class|class
name|MarkChangeAsDraftUpdateOp
extends|extends
name|BatchUpdate
operator|.
name|Op
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
name|Change
name|change
init|=
name|ctx
operator|.
name|getChange
argument_list|()
decl_stmt|;
comment|// Change status in database.
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
comment|// Change status in NoteDb.
name|PatchSet
operator|.
name|Id
name|currentPatchSetId
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|getUpdate
argument_list|(
name|currentPatchSetId
argument_list|)
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|class|DraftStatusOfPatchSetsUpdateOp
specifier|private
class|class
name|DraftStatusOfPatchSetsUpdateOp
extends|extends
name|BatchUpdate
operator|.
name|Op
block|{
DECL|field|draftStatus
specifier|private
specifier|final
name|boolean
name|draftStatus
decl_stmt|;
DECL|method|DraftStatusOfPatchSetsUpdateOp (boolean draftStatus)
name|DraftStatusOfPatchSetsUpdateOp
parameter_list|(
name|boolean
name|draftStatus
parameter_list|)
block|{
name|this
operator|.
name|draftStatus
operator|=
name|draftStatus
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
name|Collection
argument_list|<
name|PatchSet
argument_list|>
name|patchSets
init|=
name|psUtil
operator|.
name|byChange
argument_list|(
name|db
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
comment|// Change status in database.
name|patchSets
operator|.
name|forEach
argument_list|(
name|patchSet
lambda|->
name|patchSet
operator|.
name|setDraft
argument_list|(
name|draftStatus
argument_list|)
argument_list|)
expr_stmt|;
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|update
argument_list|(
name|patchSets
argument_list|)
expr_stmt|;
comment|// Change status in NoteDb.
name|PatchSetState
name|patchSetState
init|=
name|draftStatus
condition|?
name|PatchSetState
operator|.
name|DRAFT
else|:
name|PatchSetState
operator|.
name|PUBLISHED
decl_stmt|;
name|patchSets
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|PatchSet
operator|::
name|getId
argument_list|)
operator|.
name|map
argument_list|(
name|ctx
operator|::
name|getUpdate
argument_list|)
operator|.
name|forEach
argument_list|(
name|changeUpdate
lambda|->
name|changeUpdate
operator|.
name|setPatchSetState
argument_list|(
name|patchSetState
argument_list|)
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

