begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|i18n
operator|.
name|client
operator|.
name|Constants
import|;
end_import

begin_interface
DECL|interface|ChangeConstants
specifier|public
interface|interface
name|ChangeConstants
extends|extends
name|Constants
block|{
DECL|method|statusLongNew ()
name|String
name|statusLongNew
parameter_list|()
function_decl|;
DECL|method|statusLongMerged ()
name|String
name|statusLongMerged
parameter_list|()
function_decl|;
DECL|method|statusLongAbandoned ()
name|String
name|statusLongAbandoned
parameter_list|()
function_decl|;
DECL|method|statusLongDraft ()
name|String
name|statusLongDraft
parameter_list|()
function_decl|;
DECL|method|submittable ()
name|String
name|submittable
parameter_list|()
function_decl|;
DECL|method|readyToSubmit ()
name|String
name|readyToSubmit
parameter_list|()
function_decl|;
DECL|method|mergeConflict ()
name|String
name|mergeConflict
parameter_list|()
function_decl|;
DECL|method|notCurrent ()
name|String
name|notCurrent
parameter_list|()
function_decl|;
DECL|method|isPrivate ()
name|String
name|isPrivate
parameter_list|()
function_decl|;
DECL|method|isWorkInProgress ()
name|String
name|isWorkInProgress
parameter_list|()
function_decl|;
DECL|method|changeEdit ()
name|String
name|changeEdit
parameter_list|()
function_decl|;
DECL|method|myDashboardTitle ()
name|String
name|myDashboardTitle
parameter_list|()
function_decl|;
DECL|method|unknownDashboardTitle ()
name|String
name|unknownDashboardTitle
parameter_list|()
function_decl|;
DECL|method|incomingReviews ()
name|String
name|incomingReviews
parameter_list|()
function_decl|;
DECL|method|outgoingReviews ()
name|String
name|outgoingReviews
parameter_list|()
function_decl|;
DECL|method|recentlyClosed ()
name|String
name|recentlyClosed
parameter_list|()
function_decl|;
DECL|method|changeTableColumnSubject ()
name|String
name|changeTableColumnSubject
parameter_list|()
function_decl|;
DECL|method|changeTableColumnSize ()
name|String
name|changeTableColumnSize
parameter_list|()
function_decl|;
DECL|method|changeTableColumnStatus ()
name|String
name|changeTableColumnStatus
parameter_list|()
function_decl|;
DECL|method|changeTableColumnOwner ()
name|String
name|changeTableColumnOwner
parameter_list|()
function_decl|;
DECL|method|changeTableColumnAssignee ()
name|String
name|changeTableColumnAssignee
parameter_list|()
function_decl|;
DECL|method|changeTableColumnProject ()
name|String
name|changeTableColumnProject
parameter_list|()
function_decl|;
DECL|method|changeTableColumnBranch ()
name|String
name|changeTableColumnBranch
parameter_list|()
function_decl|;
DECL|method|changeTableColumnLastUpdate ()
name|String
name|changeTableColumnLastUpdate
parameter_list|()
function_decl|;
DECL|method|changeTableColumnID ()
name|String
name|changeTableColumnID
parameter_list|()
function_decl|;
DECL|method|changeTableNone ()
name|String
name|changeTableNone
parameter_list|()
function_decl|;
DECL|method|changeTableNotMergeable ()
name|String
name|changeTableNotMergeable
parameter_list|()
function_decl|;
DECL|method|changeItemHelp ()
name|String
name|changeItemHelp
parameter_list|()
function_decl|;
DECL|method|changeTableStar ()
name|String
name|changeTableStar
parameter_list|()
function_decl|;
DECL|method|changeTablePagePrev ()
name|String
name|changeTablePagePrev
parameter_list|()
function_decl|;
DECL|method|changeTablePageNext ()
name|String
name|changeTablePageNext
parameter_list|()
function_decl|;
DECL|method|upToChangeList ()
name|String
name|upToChangeList
parameter_list|()
function_decl|;
DECL|method|keyReloadChange ()
name|String
name|keyReloadChange
parameter_list|()
function_decl|;
DECL|method|keyNextPatchSet ()
name|String
name|keyNextPatchSet
parameter_list|()
function_decl|;
DECL|method|keyPreviousPatchSet ()
name|String
name|keyPreviousPatchSet
parameter_list|()
function_decl|;
DECL|method|keyReloadSearch ()
name|String
name|keyReloadSearch
parameter_list|()
function_decl|;
DECL|method|keyPublishComments ()
name|String
name|keyPublishComments
parameter_list|()
function_decl|;
DECL|method|keyEditTopic ()
name|String
name|keyEditTopic
parameter_list|()
function_decl|;
DECL|method|keyAddReviewers ()
name|String
name|keyAddReviewers
parameter_list|()
function_decl|;
DECL|method|keyExpandAllMessages ()
name|String
name|keyExpandAllMessages
parameter_list|()
function_decl|;
DECL|method|keyCollapseAllMessages ()
name|String
name|keyCollapseAllMessages
parameter_list|()
function_decl|;
DECL|method|patchTableColumnName ()
name|String
name|patchTableColumnName
parameter_list|()
function_decl|;
DECL|method|patchTableColumnComments ()
name|String
name|patchTableColumnComments
parameter_list|()
function_decl|;
DECL|method|patchTableColumnSize ()
name|String
name|patchTableColumnSize
parameter_list|()
function_decl|;
DECL|method|commitMessage ()
name|String
name|commitMessage
parameter_list|()
function_decl|;
DECL|method|mergeList ()
name|String
name|mergeList
parameter_list|()
function_decl|;
DECL|method|patchTablePrev ()
name|String
name|patchTablePrev
parameter_list|()
function_decl|;
DECL|method|patchTableNext ()
name|String
name|patchTableNext
parameter_list|()
function_decl|;
DECL|method|patchTableOpenDiff ()
name|String
name|patchTableOpenDiff
parameter_list|()
function_decl|;
DECL|method|approvalTableEditAssigneeHint ()
name|String
name|approvalTableEditAssigneeHint
parameter_list|()
function_decl|;
DECL|method|approvalTableAddReviewerHint ()
name|String
name|approvalTableAddReviewerHint
parameter_list|()
function_decl|;
DECL|method|approvalTableAddManyReviewersConfirmationDialogTitle ()
name|String
name|approvalTableAddManyReviewersConfirmationDialogTitle
parameter_list|()
function_decl|;
DECL|method|changeInfoBlockUploaded ()
name|String
name|changeInfoBlockUploaded
parameter_list|()
function_decl|;
DECL|method|changeInfoBlockUpdated ()
name|String
name|changeInfoBlockUpdated
parameter_list|()
function_decl|;
DECL|method|messageNoAuthor ()
name|String
name|messageNoAuthor
parameter_list|()
function_decl|;
DECL|method|sideBySide ()
name|String
name|sideBySide
parameter_list|()
function_decl|;
DECL|method|unifiedDiff ()
name|String
name|unifiedDiff
parameter_list|()
function_decl|;
DECL|method|buttonRevertChangeSend ()
name|String
name|buttonRevertChangeSend
parameter_list|()
function_decl|;
DECL|method|headingRevertMessage ()
name|String
name|headingRevertMessage
parameter_list|()
function_decl|;
DECL|method|revertChangeTitle ()
name|String
name|revertChangeTitle
parameter_list|()
function_decl|;
DECL|method|buttonCherryPickChangeSend ()
name|String
name|buttonCherryPickChangeSend
parameter_list|()
function_decl|;
DECL|method|headingCherryPickBranch ()
name|String
name|headingCherryPickBranch
parameter_list|()
function_decl|;
DECL|method|cherryPickCommitMessage ()
name|String
name|cherryPickCommitMessage
parameter_list|()
function_decl|;
DECL|method|cherryPickTitle ()
name|String
name|cherryPickTitle
parameter_list|()
function_decl|;
DECL|method|moveChangeSend ()
name|String
name|moveChangeSend
parameter_list|()
function_decl|;
DECL|method|headingMoveBranch ()
name|String
name|headingMoveBranch
parameter_list|()
function_decl|;
DECL|method|moveChangeMessage ()
name|String
name|moveChangeMessage
parameter_list|()
function_decl|;
DECL|method|moveTitle ()
name|String
name|moveTitle
parameter_list|()
function_decl|;
DECL|method|buttonRebaseChangeSend ()
name|String
name|buttonRebaseChangeSend
parameter_list|()
function_decl|;
DECL|method|rebaseConfirmMessage ()
name|String
name|rebaseConfirmMessage
parameter_list|()
function_decl|;
DECL|method|rebaseNotPossibleMessage ()
name|String
name|rebaseNotPossibleMessage
parameter_list|()
function_decl|;
DECL|method|rebasePlaceholderMessage ()
name|String
name|rebasePlaceholderMessage
parameter_list|()
function_decl|;
DECL|method|rebaseTitle ()
name|String
name|rebaseTitle
parameter_list|()
function_decl|;
DECL|method|baseDiffItem ()
name|String
name|baseDiffItem
parameter_list|()
function_decl|;
DECL|method|autoMerge ()
name|String
name|autoMerge
parameter_list|()
function_decl|;
DECL|method|pagedChangeListPrev ()
name|String
name|pagedChangeListPrev
parameter_list|()
function_decl|;
DECL|method|pagedChangeListNext ()
name|String
name|pagedChangeListNext
parameter_list|()
function_decl|;
DECL|method|submitFailed ()
name|String
name|submitFailed
parameter_list|()
function_decl|;
DECL|method|votable ()
name|String
name|votable
parameter_list|()
function_decl|;
DECL|method|pushCertMissing ()
name|String
name|pushCertMissing
parameter_list|()
function_decl|;
DECL|method|pushCertBad ()
name|String
name|pushCertBad
parameter_list|()
function_decl|;
DECL|method|pushCertOk ()
name|String
name|pushCertOk
parameter_list|()
function_decl|;
DECL|method|pushCertTrusted ()
name|String
name|pushCertTrusted
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

