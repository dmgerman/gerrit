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
DECL|package|com.google.gerrit.extensions.api.changes
package|package
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
package|;
end_package

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
name|Sets
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
name|ChangeMessageInfo
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
name|CommentInfo
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
name|CommitMessageInput
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
name|EditInfo
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
name|MergePatchSetInput
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
name|PureRevertInfo
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
name|RobotCommentInfo
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
name|SuggestedReviewerInfo
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
name|NotImplementedException
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_interface
DECL|interface|ChangeApi
specifier|public
interface|interface
name|ChangeApi
block|{
DECL|method|id ()
name|String
name|id
parameter_list|()
function_decl|;
comment|/**    * Look up the current revision for the change.    *    *<p><strong>Note:</strong> This method eagerly reads the revision. Methods that mutate the    * revision do not necessarily re-read the revision. Therefore, calling a getter method on an    * instance after calling a mutation method on that same instance is not guaranteed to reflect the    * mutation. It is not recommended to store references to {@code RevisionApi} instances.    *    * @return API for accessing the revision.    * @throws RestApiException if an error occurred.    */
DECL|method|current ()
specifier|default
name|RevisionApi
name|current
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|revision
argument_list|(
literal|"current"
argument_list|)
return|;
block|}
comment|/**    * Look up a revision of a change by number.    *    * @see #current()    */
DECL|method|revision (int id)
specifier|default
name|RevisionApi
name|revision
parameter_list|(
name|int
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|revision
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Look up a revision of a change by commit SHA-1 or other supported revision string.    *    * @see #current()    */
DECL|method|revision (String id)
name|RevisionApi
name|revision
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up the reviewer of the change.    *    *<p>    *    * @param id ID of the account, can be a string of the format "Full Name    *&lt;mail@example.com&gt;", just the email address, a full name if it is unique, an account    *     ID, a user name or 'self' for the calling user.    * @return API for accessing the reviewer.    * @throws RestApiException if id is not account ID or is a user that isn't known to be a reviewer    *     for this change.    */
DECL|method|reviewer (String id)
name|ReviewerApi
name|reviewer
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|abandon ()
specifier|default
name|void
name|abandon
parameter_list|()
throws|throws
name|RestApiException
block|{
name|abandon
argument_list|(
operator|new
name|AbandonInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|abandon (AbandonInput in)
name|void
name|abandon
parameter_list|(
name|AbandonInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|restore ()
specifier|default
name|void
name|restore
parameter_list|()
throws|throws
name|RestApiException
block|{
name|restore
argument_list|(
operator|new
name|RestoreInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|restore (RestoreInput in)
name|void
name|restore
parameter_list|(
name|RestoreInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|move (String destination)
specifier|default
name|void
name|move
parameter_list|(
name|String
name|destination
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
name|move
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|move (MoveInput in)
name|void
name|move
parameter_list|(
name|MoveInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|setPrivate (boolean value, @Nullable String message)
name|void
name|setPrivate
parameter_list|(
name|boolean
name|value
parameter_list|,
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|setPrivate (boolean value)
specifier|default
name|void
name|setPrivate
parameter_list|(
name|boolean
name|value
parameter_list|)
throws|throws
name|RestApiException
block|{
name|setPrivate
argument_list|(
name|value
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|setWorkInProgress (@ullable String message)
name|void
name|setWorkInProgress
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|setReadyForReview (@ullable String message)
name|void
name|setReadyForReview
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|setWorkInProgress ()
specifier|default
name|void
name|setWorkInProgress
parameter_list|()
throws|throws
name|RestApiException
block|{
name|setWorkInProgress
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|setReadyForReview ()
specifier|default
name|void
name|setReadyForReview
parameter_list|()
throws|throws
name|RestApiException
block|{
name|setReadyForReview
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * Ignore or un-ignore this change.    *    * @param ignore ignore the change if true    */
DECL|method|ignore (boolean ignore)
name|void
name|ignore
parameter_list|(
name|boolean
name|ignore
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Check if this change is ignored.    *    * @return true if the change is ignored    */
DECL|method|ignored ()
name|boolean
name|ignored
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Mark this change as reviewed/unreviewed.    *    * @param reviewed flag to decide if this change should be marked as reviewed ({@code true}) or    *     unreviewed ({@code false})    */
DECL|method|markAsReviewed (boolean reviewed)
name|void
name|markAsReviewed
parameter_list|(
name|boolean
name|reviewed
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Create a new change that reverts this change.    *    * @see Changes#id(int)    */
DECL|method|revert ()
specifier|default
name|ChangeApi
name|revert
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|revert
argument_list|(
operator|new
name|RevertInput
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Create a new change that reverts this change.    *    * @see Changes#id(int)    */
DECL|method|revert (RevertInput in)
name|ChangeApi
name|revert
parameter_list|(
name|RevertInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a merge patch set for the change. */
DECL|method|createMergePatchSet (MergePatchSetInput in)
name|ChangeInfo
name|createMergePatchSet
parameter_list|(
name|MergePatchSetInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|submittedTogether ()
specifier|default
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|submittedTogether
parameter_list|()
throws|throws
name|RestApiException
block|{
name|SubmittedTogetherInfo
name|info
init|=
name|submittedTogether
argument_list|(
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
argument_list|,
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|SubmittedTogetherOption
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|info
operator|.
name|changes
return|;
block|}
DECL|method|submittedTogether (EnumSet<SubmittedTogetherOption> options)
specifier|default
name|SubmittedTogetherInfo
name|submittedTogether
parameter_list|(
name|EnumSet
argument_list|<
name|SubmittedTogetherOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|submittedTogether
argument_list|(
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
argument_list|,
name|options
argument_list|)
return|;
block|}
DECL|method|submittedTogether ( EnumSet<ListChangesOption> listOptions, EnumSet<SubmittedTogetherOption> submitOptions)
name|SubmittedTogetherInfo
name|submittedTogether
parameter_list|(
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|listOptions
parameter_list|,
name|EnumSet
argument_list|<
name|SubmittedTogetherOption
argument_list|>
name|submitOptions
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Publishes a draft change. */
annotation|@
name|Deprecated
DECL|method|publish ()
specifier|default
name|void
name|publish
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"draft workflow is discontinued"
argument_list|)
throw|;
block|}
comment|/** Rebase the current revision of a change using default options. */
DECL|method|rebase ()
specifier|default
name|void
name|rebase
parameter_list|()
throws|throws
name|RestApiException
block|{
name|rebase
argument_list|(
operator|new
name|RebaseInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** Rebase the current revision of a change. */
DECL|method|rebase (RebaseInput in)
name|void
name|rebase
parameter_list|(
name|RebaseInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Deletes a change. */
DECL|method|delete ()
name|void
name|delete
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|topic ()
name|String
name|topic
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|topic (String topic)
name|void
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|includedIn ()
name|IncludedInInfo
name|includedIn
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|addReviewer (String reviewer)
specifier|default
name|AddReviewerResult
name|addReviewer
parameter_list|(
name|String
name|reviewer
parameter_list|)
throws|throws
name|RestApiException
block|{
name|AddReviewerInput
name|in
init|=
operator|new
name|AddReviewerInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|reviewer
operator|=
name|reviewer
expr_stmt|;
return|return
name|addReviewer
argument_list|(
name|in
argument_list|)
return|;
block|}
DECL|method|addReviewer (AddReviewerInput in)
name|AddReviewerResult
name|addReviewer
parameter_list|(
name|AddReviewerInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|suggestReviewers ()
name|SuggestedReviewersRequest
name|suggestReviewers
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|suggestReviewers (String query)
specifier|default
name|SuggestedReviewersRequest
name|suggestReviewers
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|suggestReviewers
argument_list|()
operator|.
name|withQuery
argument_list|(
name|query
argument_list|)
return|;
block|}
comment|/**    * Retrieve reviewers ({@code ReviewerState.REVIEWER} and {@code ReviewerState.CC}) on the change.    */
DECL|method|reviewers ()
name|List
argument_list|<
name|ReviewerInfo
argument_list|>
name|reviewers
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|get (EnumSet<ListChangesOption> options)
name|ChangeInfo
name|get
parameter_list|(
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|get (Iterable<ListChangesOption> options)
specifier|default
name|ChangeInfo
name|get
parameter_list|(
name|Iterable
argument_list|<
name|ListChangesOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
name|Sets
operator|.
name|newEnumSet
argument_list|(
name|options
argument_list|,
name|ListChangesOption
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
DECL|method|get (ListChangesOption... options)
specifier|default
name|ChangeInfo
name|get
parameter_list|(
name|ListChangesOption
modifier|...
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|options
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * {@link #get(ListChangesOption...)} with all options included, except for the following.    *    *<ul>    *<li>{@code CHECK} is omitted, to skip consistency checks.    *<li>{@code SKIP_MERGEABLE} is omitted, so the {@code mergeable} bit<em>is</em> set.    *</ul>    */
DECL|method|get ()
specifier|default
name|ChangeInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
name|EnumSet
operator|.
name|complementOf
argument_list|(
name|EnumSet
operator|.
name|of
argument_list|(
name|ListChangesOption
operator|.
name|CHECK
argument_list|,
name|ListChangesOption
operator|.
name|SKIP_MERGEABLE
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/** {@link #get(ListChangesOption...)} with no options included. */
DECL|method|info ()
specifier|default
name|ChangeInfo
name|info
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
name|EnumSet
operator|.
name|noneOf
argument_list|(
name|ListChangesOption
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Retrieve change edit when exists.    *    * @deprecated Replaced by {@link ChangeApi#edit()} in combination with {@link    *     ChangeEditApi#get()}.    */
annotation|@
name|Deprecated
DECL|method|getEdit ()
specifier|default
name|EditInfo
name|getEdit
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|edit
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**    * Provides access to an API regarding the change edit of this change.    *    * @return a {@code ChangeEditApi} for the change edit of this change    * @throws RestApiException if the API isn't accessible    */
DECL|method|edit ()
name|ChangeEditApi
name|edit
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Create a new patch set with a new commit message. */
DECL|method|setMessage (String message)
specifier|default
name|void
name|setMessage
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
block|{
name|CommitMessageInput
name|in
init|=
operator|new
name|CommitMessageInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|setMessage
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
comment|/** Create a new patch set with a new commit message. */
DECL|method|setMessage (CommitMessageInput in)
name|void
name|setMessage
parameter_list|(
name|CommitMessageInput
name|in
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Set hashtags on a change */
DECL|method|setHashtags (HashtagsInput input)
name|void
name|setHashtags
parameter_list|(
name|HashtagsInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Get hashtags on a change.    *    * @return hashtags    * @throws RestApiException    */
DECL|method|getHashtags ()
name|Set
argument_list|<
name|String
argument_list|>
name|getHashtags
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Set the assignee of a change. */
DECL|method|setAssignee (AssigneeInput input)
name|AccountInfo
name|setAssignee
parameter_list|(
name|AssigneeInput
name|input
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/** Get the assignee of a change. */
DECL|method|getAssignee ()
name|AccountInfo
name|getAssignee
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Get all past assignees. */
DECL|method|getPastAssignees ()
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|getPastAssignees
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Delete the assignee of a change.    *    * @return the assignee that was deleted, or null if there was no assignee.    */
DECL|method|deleteAssignee ()
name|AccountInfo
name|deleteAssignee
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Get all published comments on a change.    *    * @return comments in a map keyed by path; comments have the {@code revision} field set to    *     indicate their patch set.    * @throws RestApiException    */
DECL|method|comments ()
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|comments
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Get all robot comments on a change.    *    * @return robot comments in a map keyed by path; robot comments have the {@code revision} field    *     set to indicate their patch set.    * @throws RestApiException    */
DECL|method|robotComments ()
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RobotCommentInfo
argument_list|>
argument_list|>
name|robotComments
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Get all draft comments for the current user on a change.    *    * @return drafts in a map keyed by path; comments have the {@code revision} field set to indicate    *     their patch set.    * @throws RestApiException    */
DECL|method|drafts ()
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|drafts
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|check ()
name|ChangeInfo
name|check
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|check (FixInput fix)
name|ChangeInfo
name|check
parameter_list|(
name|FixInput
name|fix
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|method|index ()
name|void
name|index
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Check if this change is a pure revert of the change stored in revertOf. */
DECL|method|pureRevert ()
name|PureRevertInfo
name|pureRevert
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/** Check if this change is a pure revert of claimedOriginal (SHA1 in 40 digit hex). */
DECL|method|pureRevert (String claimedOriginal)
name|PureRevertInfo
name|pureRevert
parameter_list|(
name|String
name|claimedOriginal
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Get all messages of a change with detailed account info.    *    * @return a list of messages sorted by their creation time.    * @throws RestApiException    */
DECL|method|messages ()
name|List
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
comment|/**    * Look up a change message of a change by its id.    *    * @param id the id of the change message. In NoteDb, this id is the {@code ObjectId} of a commit    *     on the change meta branch.    * @return API for accessing a change message.    * @throws RestApiException if the id is invalid.    */
DECL|method|message (String id)
name|ChangeMessageApi
name|message
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
function_decl|;
DECL|class|SuggestedReviewersRequest
specifier|abstract
class|class
name|SuggestedReviewersRequest
block|{
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|limit
specifier|private
name|int
name|limit
decl_stmt|;
DECL|method|get ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|SuggestedReviewerInfo
argument_list|>
name|get
parameter_list|()
throws|throws
name|RestApiException
function_decl|;
DECL|method|withQuery (String query)
specifier|public
name|SuggestedReviewersRequest
name|withQuery
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withLimit (int limit)
specifier|public
name|SuggestedReviewersRequest
name|withLimit
parameter_list|(
name|int
name|limit
parameter_list|)
block|{
name|this
operator|.
name|limit
operator|=
name|limit
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getQuery ()
specifier|public
name|String
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
DECL|method|getLimit ()
specifier|public
name|int
name|getLimit
parameter_list|()
block|{
return|return
name|limit
return|;
block|}
block|}
comment|/**    * A default implementation which allows source compatibility when adding new methods to the    * interface.    */
DECL|class|NotImplemented
class|class
name|NotImplemented
implements|implements
name|ChangeApi
block|{
annotation|@
name|Override
DECL|method|id ()
specifier|public
name|String
name|id
parameter_list|()
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|reviewer (String id)
specifier|public
name|ReviewerApi
name|reviewer
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|revision (String id)
specifier|public
name|RevisionApi
name|revision
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|abandon (AbandonInput in)
specifier|public
name|void
name|abandon
parameter_list|(
name|AbandonInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|restore (RestoreInput in)
specifier|public
name|void
name|restore
parameter_list|(
name|RestoreInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|move (MoveInput in)
specifier|public
name|void
name|move
parameter_list|(
name|MoveInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setPrivate (boolean value, @Nullable String message)
specifier|public
name|void
name|setPrivate
parameter_list|(
name|boolean
name|value
parameter_list|,
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setWorkInProgress (String message)
specifier|public
name|void
name|setWorkInProgress
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setReadyForReview (String message)
specifier|public
name|void
name|setReadyForReview
parameter_list|(
name|String
name|message
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|revert (RevertInput in)
specifier|public
name|ChangeApi
name|revert
parameter_list|(
name|RevertInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|rebase (RebaseInput in)
specifier|public
name|void
name|rebase
parameter_list|(
name|RebaseInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|delete ()
specifier|public
name|void
name|delete
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|topic ()
specifier|public
name|String
name|topic
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|topic (String topic)
specifier|public
name|void
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|includedIn ()
specifier|public
name|IncludedInInfo
name|includedIn
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|addReviewer (AddReviewerInput in)
specifier|public
name|AddReviewerResult
name|addReviewer
parameter_list|(
name|AddReviewerInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|suggestReviewers ()
specifier|public
name|SuggestedReviewersRequest
name|suggestReviewers
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|suggestReviewers (String query)
specifier|public
name|SuggestedReviewersRequest
name|suggestReviewers
parameter_list|(
name|String
name|query
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|reviewers ()
specifier|public
name|List
argument_list|<
name|ReviewerInfo
argument_list|>
name|reviewers
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|get (EnumSet<ListChangesOption> options)
specifier|public
name|ChangeInfo
name|get
parameter_list|(
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setMessage (CommitMessageInput in)
specifier|public
name|void
name|setMessage
parameter_list|(
name|CommitMessageInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|edit ()
specifier|public
name|ChangeEditApi
name|edit
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setHashtags (HashtagsInput input)
specifier|public
name|void
name|setHashtags
parameter_list|(
name|HashtagsInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getHashtags ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getHashtags
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setAssignee (AssigneeInput input)
specifier|public
name|AccountInfo
name|setAssignee
parameter_list|(
name|AssigneeInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getAssignee ()
specifier|public
name|AccountInfo
name|getAssignee
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getPastAssignees ()
specifier|public
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|getPastAssignees
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteAssignee ()
specifier|public
name|AccountInfo
name|deleteAssignee
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|comments ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|comments
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|robotComments ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RobotCommentInfo
argument_list|>
argument_list|>
name|robotComments
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|drafts ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|CommentInfo
argument_list|>
argument_list|>
name|drafts
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|check ()
specifier|public
name|ChangeInfo
name|check
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|check (FixInput fix)
specifier|public
name|ChangeInfo
name|check
parameter_list|(
name|FixInput
name|fix
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|index ()
specifier|public
name|void
name|index
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submittedTogether ()
specifier|public
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|submittedTogether
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submittedTogether (EnumSet<SubmittedTogetherOption> options)
specifier|public
name|SubmittedTogetherInfo
name|submittedTogether
parameter_list|(
name|EnumSet
argument_list|<
name|SubmittedTogetherOption
argument_list|>
name|options
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|submittedTogether ( EnumSet<ListChangesOption> a, EnumSet<SubmittedTogetherOption> b)
specifier|public
name|SubmittedTogetherInfo
name|submittedTogether
parameter_list|(
name|EnumSet
argument_list|<
name|ListChangesOption
argument_list|>
name|a
parameter_list|,
name|EnumSet
argument_list|<
name|SubmittedTogetherOption
argument_list|>
name|b
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createMergePatchSet (MergePatchSetInput in)
specifier|public
name|ChangeInfo
name|createMergePatchSet
parameter_list|(
name|MergePatchSetInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|ignore (boolean ignore)
specifier|public
name|void
name|ignore
parameter_list|(
name|boolean
name|ignore
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|ignored ()
specifier|public
name|boolean
name|ignored
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|markAsReviewed (boolean reviewed)
specifier|public
name|void
name|markAsReviewed
parameter_list|(
name|boolean
name|reviewed
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|pureRevert ()
specifier|public
name|PureRevertInfo
name|pureRevert
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|pureRevert (String claimedOriginal)
specifier|public
name|PureRevertInfo
name|pureRevert
parameter_list|(
name|String
name|claimedOriginal
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|messages ()
specifier|public
name|List
argument_list|<
name|ChangeMessageInfo
argument_list|>
name|messages
parameter_list|()
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|message (String id)
specifier|public
name|ChangeMessageApi
name|message
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
throw|throw
operator|new
name|NotImplementedException
argument_list|()
throw|;
block|}
block|}
block|}
end_interface

end_unit

