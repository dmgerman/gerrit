begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|ChangeEditResource
operator|.
name|CHANGE_EDIT_KIND
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
name|change
operator|.
name|ChangeResource
operator|.
name|CHANGE_KIND
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
name|change
operator|.
name|CommentResource
operator|.
name|COMMENT_KIND
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
name|change
operator|.
name|DraftCommentResource
operator|.
name|DRAFT_COMMENT_KIND
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
name|change
operator|.
name|FileResource
operator|.
name|FILE_KIND
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
name|change
operator|.
name|ReviewerResource
operator|.
name|REVIEWER_KIND
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
name|change
operator|.
name|RevisionResource
operator|.
name|REVISION_KIND
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
name|change
operator|.
name|VoteResource
operator|.
name|VOTE_KIND
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
name|DynamicMap
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
name|RestApiModule
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
name|account
operator|.
name|AccountLoader
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
name|Reviewed
operator|.
name|DeleteReviewed
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
name|Reviewed
operator|.
name|PutReviewed
import|;
end_import

begin_class
DECL|class|Module
specifier|public
class|class
name|Module
extends|extends
name|RestApiModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|ChangesCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Revisions
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Reviewers
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DraftComments
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Comments
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Files
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Votes
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|CHANGE_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|COMMENT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|DRAFT_COMMENT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|FILE_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|REVIEWER_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|REVISION_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|CHANGE_EDIT_KIND
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|VOTE_KIND
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetChange
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"detail"
argument_list|)
operator|.
name|to
argument_list|(
name|GetDetail
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"topic"
argument_list|)
operator|.
name|to
argument_list|(
name|GetTopic
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"in"
argument_list|)
operator|.
name|to
argument_list|(
name|IncludedIn
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"hashtags"
argument_list|)
operator|.
name|to
argument_list|(
name|GetHashtags
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"comments"
argument_list|)
operator|.
name|to
argument_list|(
name|ListChangeComments
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"drafts"
argument_list|)
operator|.
name|to
argument_list|(
name|ListChangeDrafts
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"check"
argument_list|)
operator|.
name|to
argument_list|(
name|Check
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"check"
argument_list|)
operator|.
name|to
argument_list|(
name|Check
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"topic"
argument_list|)
operator|.
name|to
argument_list|(
name|PutTopic
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"topic"
argument_list|)
operator|.
name|to
argument_list|(
name|PutTopic
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|CHANGE_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteDraftChange
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"abandon"
argument_list|)
operator|.
name|to
argument_list|(
name|Abandon
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"hashtags"
argument_list|)
operator|.
name|to
argument_list|(
name|PostHashtags
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"publish"
argument_list|)
operator|.
name|to
argument_list|(
name|PublishDraftPatchSet
operator|.
name|CurrentRevision
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"restore"
argument_list|)
operator|.
name|to
argument_list|(
name|Restore
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"revert"
argument_list|)
operator|.
name|to
argument_list|(
name|Revert
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"submit"
argument_list|)
operator|.
name|to
argument_list|(
name|Submit
operator|.
name|CurrentRevision
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"submitted_together"
argument_list|)
operator|.
name|to
argument_list|(
name|SubmittedTogether
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"rebase"
argument_list|)
operator|.
name|to
argument_list|(
name|Rebase
operator|.
name|CurrentRevision
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"index"
argument_list|)
operator|.
name|to
argument_list|(
name|Index
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"rebuild.notedb"
argument_list|)
operator|.
name|to
argument_list|(
name|Rebuild
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"move"
argument_list|)
operator|.
name|to
argument_list|(
name|Move
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"reviewers"
argument_list|)
operator|.
name|to
argument_list|(
name|PostReviewers
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"suggest_reviewers"
argument_list|)
operator|.
name|to
argument_list|(
name|SuggestChangeReviewers
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"reviewers"
argument_list|)
operator|.
name|to
argument_list|(
name|Reviewers
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVIEWER_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetReviewer
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|REVIEWER_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteReviewer
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|REVIEWER_KIND
argument_list|,
literal|"votes"
argument_list|)
operator|.
name|to
argument_list|(
name|Votes
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|VOTE_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteVote
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"revisions"
argument_list|)
operator|.
name|to
argument_list|(
name|Revisions
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"actions"
argument_list|)
operator|.
name|to
argument_list|(
name|GetRevisionActions
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"cherrypick"
argument_list|)
operator|.
name|to
argument_list|(
name|CherryPick
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"commit"
argument_list|)
operator|.
name|to
argument_list|(
name|GetCommit
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|REVISION_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteDraftPatchSet
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"mergeable"
argument_list|)
operator|.
name|to
argument_list|(
name|Mergeable
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"publish"
argument_list|)
operator|.
name|to
argument_list|(
name|PublishDraftPatchSet
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"related"
argument_list|)
operator|.
name|to
argument_list|(
name|GetRelated
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"review"
argument_list|)
operator|.
name|to
argument_list|(
name|GetReview
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"review"
argument_list|)
operator|.
name|to
argument_list|(
name|PostReview
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"submit"
argument_list|)
operator|.
name|to
argument_list|(
name|Submit
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"rebase"
argument_list|)
operator|.
name|to
argument_list|(
name|Rebase
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"patch"
argument_list|)
operator|.
name|to
argument_list|(
name|GetPatch
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"submit_type"
argument_list|)
operator|.
name|to
argument_list|(
name|TestSubmitType
operator|.
name|Get
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"test.submit_rule"
argument_list|)
operator|.
name|to
argument_list|(
name|TestSubmitRule
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"test.submit_type"
argument_list|)
operator|.
name|to
argument_list|(
name|TestSubmitType
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"archive"
argument_list|)
operator|.
name|to
argument_list|(
name|GetArchive
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"drafts"
argument_list|)
operator|.
name|to
argument_list|(
name|DraftComments
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"drafts"
argument_list|)
operator|.
name|to
argument_list|(
name|CreateDraftComment
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|DRAFT_COMMENT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetDraftComment
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|DRAFT_COMMENT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|PutDraftComment
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|DRAFT_COMMENT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteDraftComment
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"comments"
argument_list|)
operator|.
name|to
argument_list|(
name|Comments
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|COMMENT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|GetComment
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|REVISION_KIND
argument_list|,
literal|"files"
argument_list|)
operator|.
name|to
argument_list|(
name|Files
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|FILE_KIND
argument_list|,
literal|"reviewed"
argument_list|)
operator|.
name|to
argument_list|(
name|PutReviewed
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|FILE_KIND
argument_list|,
literal|"reviewed"
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteReviewed
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|FILE_KIND
argument_list|,
literal|"content"
argument_list|)
operator|.
name|to
argument_list|(
name|GetContent
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|FILE_KIND
argument_list|,
literal|"download"
argument_list|)
operator|.
name|to
argument_list|(
name|DownloadContent
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|FILE_KIND
argument_list|,
literal|"diff"
argument_list|)
operator|.
name|to
argument_list|(
name|GetDiff
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit"
argument_list|)
operator|.
name|to
argument_list|(
name|DeleteChangeEdit
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit:publish"
argument_list|)
operator|.
name|to
argument_list|(
name|PublishChangeEdit
operator|.
name|class
argument_list|)
expr_stmt|;
name|child
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit:rebase"
argument_list|)
operator|.
name|to
argument_list|(
name|RebaseChangeEdit
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit:message"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|EditMessage
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_KIND
argument_list|,
literal|"edit:message"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|GetMessage
operator|.
name|class
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|CHANGE_EDIT_KIND
argument_list|,
literal|"/"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|Put
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|CHANGE_EDIT_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|DeleteContent
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_EDIT_KIND
argument_list|,
literal|"/"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|Get
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|CHANGE_EDIT_KIND
argument_list|,
literal|"meta"
argument_list|)
operator|.
name|to
argument_list|(
name|ChangeEdits
operator|.
name|GetMeta
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|AccountLoader
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeEdits
operator|.
name|Create
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeEdits
operator|.
name|DeleteFile
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeInserter
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|EmailReviewComments
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|PatchSetInserter
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|RebaseChangeOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ReviewerResource
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|SetHashtagsOp
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

