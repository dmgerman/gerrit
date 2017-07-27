begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|base
operator|.
name|Strings
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
name|ImmutableList
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
name|ImmutableListMultimap
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
name|ImmutableSet
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
name|ListMultimap
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
name|common
operator|.
name|data
operator|.
name|SubmitRecord
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
name|Account
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
name|ChangeMessage
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
name|Comment
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetApproval
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
name|Project
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
name|RevId
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
name|ReviewerByEmailSet
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
name|ReviewerSet
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
name|ReviewerStatusUpdate
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
name|NoteDbChangeState
operator|.
name|PrimaryStorage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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

begin_comment
comment|/**  * Immutable state associated with a change meta ref at a given commit.  *  *<p>One instance is the output of a single {@link ChangeNotesParser}, and contains types required  * to support public methods on {@link ChangeNotes}. It is intended to be cached in-process.  *  *<p>Note that {@link ChangeNotes} contains more than just a single {@code ChangeNoteState}, such  * as per-draft information, so that class is not cached directly.  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|ChangeNotesState
specifier|public
specifier|abstract
class|class
name|ChangeNotesState
block|{
DECL|method|empty (Change change)
specifier|static
name|ChangeNotesState
name|empty
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ChangeNotesState
argument_list|(
literal|null
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
literal|null
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableSet
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ReviewerSet
operator|.
name|empty
argument_list|()
argument_list|,
name|ReviewerByEmailSet
operator|.
name|empty
argument_list|()
argument_list|,
name|ReviewerSet
operator|.
name|empty
argument_list|()
argument_list|,
name|ReviewerByEmailSet
operator|.
name|empty
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|create ( @ullable ObjectId metaId, Change.Id changeId, Change.Key changeKey, Timestamp createdOn, Timestamp lastUpdatedOn, Account.Id owner, String branch, @Nullable PatchSet.Id currentPatchSetId, String subject, @Nullable String topic, @Nullable String originalSubject, @Nullable String submissionId, @Nullable Account.Id assignee, @Nullable Change.Status status, @Nullable Set<Account.Id> pastAssignees, @Nullable Set<String> hashtags, Map<PatchSet.Id, PatchSet> patchSets, ListMultimap<PatchSet.Id, PatchSetApproval> approvals, ReviewerSet reviewers, ReviewerByEmailSet reviewersByEmail, ReviewerSet pendingReviewers, ReviewerByEmailSet pendingReviewersByEmail, List<Account.Id> allPastReviewers, List<ReviewerStatusUpdate> reviewerUpdates, List<SubmitRecord> submitRecords, List<ChangeMessage> allChangeMessages, ListMultimap<PatchSet.Id, ChangeMessage> changeMessagesByPatchSet, ListMultimap<RevId, Comment> publishedComments, @Nullable Timestamp readOnlyUntil, @Nullable Boolean isPrivate, @Nullable Boolean workInProgress, boolean hasReviewStarted, @Nullable Change.Id revertOf)
specifier|static
name|ChangeNotesState
name|create
parameter_list|(
annotation|@
name|Nullable
name|ObjectId
name|metaId
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Change
operator|.
name|Key
name|changeKey
parameter_list|,
name|Timestamp
name|createdOn
parameter_list|,
name|Timestamp
name|lastUpdatedOn
parameter_list|,
name|Account
operator|.
name|Id
name|owner
parameter_list|,
name|String
name|branch
parameter_list|,
annotation|@
name|Nullable
name|PatchSet
operator|.
name|Id
name|currentPatchSetId
parameter_list|,
name|String
name|subject
parameter_list|,
annotation|@
name|Nullable
name|String
name|topic
parameter_list|,
annotation|@
name|Nullable
name|String
name|originalSubject
parameter_list|,
annotation|@
name|Nullable
name|String
name|submissionId
parameter_list|,
annotation|@
name|Nullable
name|Account
operator|.
name|Id
name|assignee
parameter_list|,
annotation|@
name|Nullable
name|Change
operator|.
name|Status
name|status
parameter_list|,
annotation|@
name|Nullable
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|pastAssignees
parameter_list|,
annotation|@
name|Nullable
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
parameter_list|,
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
parameter_list|,
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|,
name|ReviewerSet
name|reviewers
parameter_list|,
name|ReviewerByEmailSet
name|reviewersByEmail
parameter_list|,
name|ReviewerSet
name|pendingReviewers
parameter_list|,
name|ReviewerByEmailSet
name|pendingReviewersByEmail
parameter_list|,
name|List
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|allPastReviewers
parameter_list|,
name|List
argument_list|<
name|ReviewerStatusUpdate
argument_list|>
name|reviewerUpdates
parameter_list|,
name|List
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
parameter_list|,
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|allChangeMessages
parameter_list|,
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|ChangeMessage
argument_list|>
name|changeMessagesByPatchSet
parameter_list|,
name|ListMultimap
argument_list|<
name|RevId
argument_list|,
name|Comment
argument_list|>
name|publishedComments
parameter_list|,
annotation|@
name|Nullable
name|Timestamp
name|readOnlyUntil
parameter_list|,
annotation|@
name|Nullable
name|Boolean
name|isPrivate
parameter_list|,
annotation|@
name|Nullable
name|Boolean
name|workInProgress
parameter_list|,
name|boolean
name|hasReviewStarted
parameter_list|,
annotation|@
name|Nullable
name|Change
operator|.
name|Id
name|revertOf
parameter_list|)
block|{
if|if
condition|(
name|hashtags
operator|==
literal|null
condition|)
block|{
name|hashtags
operator|=
name|ImmutableSet
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_ChangeNotesState
argument_list|(
name|metaId
argument_list|,
name|changeId
argument_list|,
operator|new
name|AutoValue_ChangeNotesState_ChangeColumns
argument_list|(
name|changeKey
argument_list|,
name|createdOn
argument_list|,
name|lastUpdatedOn
argument_list|,
name|owner
argument_list|,
name|branch
argument_list|,
name|currentPatchSetId
argument_list|,
name|subject
argument_list|,
name|topic
argument_list|,
name|originalSubject
argument_list|,
name|submissionId
argument_list|,
name|assignee
argument_list|,
name|status
argument_list|,
name|isPrivate
argument_list|,
name|workInProgress
argument_list|,
name|hasReviewStarted
argument_list|,
name|revertOf
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|pastAssignees
argument_list|)
argument_list|,
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|hashtags
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|patchSets
operator|.
name|entrySet
argument_list|()
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|approvals
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|,
name|reviewers
argument_list|,
name|reviewersByEmail
argument_list|,
name|pendingReviewers
argument_list|,
name|pendingReviewersByEmail
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|allPastReviewers
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|reviewerUpdates
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|submitRecords
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|allChangeMessages
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|changeMessagesByPatchSet
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|publishedComments
argument_list|)
argument_list|,
name|readOnlyUntil
argument_list|,
name|isPrivate
argument_list|,
name|workInProgress
argument_list|,
name|hasReviewStarted
argument_list|,
name|revertOf
argument_list|)
return|;
block|}
comment|/**    * Subset of Change columns that can be represented in NoteDb.    *    *<p>Notable exceptions include rowVersion and noteDbState, which are only make sense when read    * from NoteDb, so they cannot be cached.    *    *<p>Fields are in listed column order.    */
annotation|@
name|AutoValue
DECL|class|ChangeColumns
specifier|abstract
specifier|static
class|class
name|ChangeColumns
block|{
DECL|method|changeKey ()
specifier|abstract
name|Change
operator|.
name|Key
name|changeKey
parameter_list|()
function_decl|;
DECL|method|createdOn ()
specifier|abstract
name|Timestamp
name|createdOn
parameter_list|()
function_decl|;
DECL|method|lastUpdatedOn ()
specifier|abstract
name|Timestamp
name|lastUpdatedOn
parameter_list|()
function_decl|;
DECL|method|owner ()
specifier|abstract
name|Account
operator|.
name|Id
name|owner
parameter_list|()
function_decl|;
comment|// Project not included, as it's not stored anywhere in the meta ref.
DECL|method|branch ()
specifier|abstract
name|String
name|branch
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|currentPatchSetId ()
specifier|abstract
name|PatchSet
operator|.
name|Id
name|currentPatchSetId
parameter_list|()
function_decl|;
DECL|method|subject ()
specifier|abstract
name|String
name|subject
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|topic ()
specifier|abstract
name|String
name|topic
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|originalSubject ()
specifier|abstract
name|String
name|originalSubject
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|submissionId ()
specifier|abstract
name|String
name|submissionId
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|assignee ()
specifier|abstract
name|Account
operator|.
name|Id
name|assignee
parameter_list|()
function_decl|;
comment|// TODO(dborowitz): Use a sensible default other than null
annotation|@
name|Nullable
DECL|method|status ()
specifier|abstract
name|Change
operator|.
name|Status
name|status
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isPrivate ()
specifier|abstract
name|Boolean
name|isPrivate
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isWorkInProgress ()
specifier|abstract
name|Boolean
name|isWorkInProgress
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|hasReviewStarted ()
specifier|abstract
name|Boolean
name|hasReviewStarted
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|revertOf ()
specifier|abstract
name|Change
operator|.
name|Id
name|revertOf
parameter_list|()
function_decl|;
block|}
comment|// Only null if NoteDb is disabled.
annotation|@
name|Nullable
DECL|method|metaId ()
specifier|abstract
name|ObjectId
name|metaId
parameter_list|()
function_decl|;
DECL|method|changeId ()
specifier|abstract
name|Change
operator|.
name|Id
name|changeId
parameter_list|()
function_decl|;
comment|// Only null if NoteDb is disabled.
annotation|@
name|Nullable
DECL|method|columns ()
specifier|abstract
name|ChangeColumns
name|columns
parameter_list|()
function_decl|;
comment|// Other related to this Change.
DECL|method|pastAssignees ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|pastAssignees
parameter_list|()
function_decl|;
DECL|method|hashtags ()
specifier|abstract
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|hashtags
parameter_list|()
function_decl|;
DECL|method|patchSets ()
specifier|abstract
name|ImmutableList
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
argument_list|>
name|patchSets
parameter_list|()
function_decl|;
DECL|method|approvals ()
specifier|abstract
name|ImmutableList
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
argument_list|>
name|approvals
parameter_list|()
function_decl|;
DECL|method|reviewers ()
specifier|abstract
name|ReviewerSet
name|reviewers
parameter_list|()
function_decl|;
DECL|method|reviewersByEmail ()
specifier|abstract
name|ReviewerByEmailSet
name|reviewersByEmail
parameter_list|()
function_decl|;
DECL|method|pendingReviewers ()
specifier|abstract
name|ReviewerSet
name|pendingReviewers
parameter_list|()
function_decl|;
DECL|method|pendingReviewersByEmail ()
specifier|abstract
name|ReviewerByEmailSet
name|pendingReviewersByEmail
parameter_list|()
function_decl|;
DECL|method|allPastReviewers ()
specifier|abstract
name|ImmutableList
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|allPastReviewers
parameter_list|()
function_decl|;
DECL|method|reviewerUpdates ()
specifier|abstract
name|ImmutableList
argument_list|<
name|ReviewerStatusUpdate
argument_list|>
name|reviewerUpdates
parameter_list|()
function_decl|;
DECL|method|submitRecords ()
specifier|abstract
name|ImmutableList
argument_list|<
name|SubmitRecord
argument_list|>
name|submitRecords
parameter_list|()
function_decl|;
DECL|method|allChangeMessages ()
specifier|abstract
name|ImmutableList
argument_list|<
name|ChangeMessage
argument_list|>
name|allChangeMessages
parameter_list|()
function_decl|;
DECL|method|changeMessagesByPatchSet ()
specifier|abstract
name|ImmutableListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|ChangeMessage
argument_list|>
name|changeMessagesByPatchSet
parameter_list|()
function_decl|;
DECL|method|publishedComments ()
specifier|abstract
name|ImmutableListMultimap
argument_list|<
name|RevId
argument_list|,
name|Comment
argument_list|>
name|publishedComments
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|readOnlyUntil ()
specifier|abstract
name|Timestamp
name|readOnlyUntil
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isPrivate ()
specifier|abstract
name|Boolean
name|isPrivate
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|isWorkInProgress ()
specifier|abstract
name|Boolean
name|isWorkInProgress
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|hasReviewStarted ()
specifier|abstract
name|Boolean
name|hasReviewStarted
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|revertOf ()
specifier|abstract
name|Change
operator|.
name|Id
name|revertOf
parameter_list|()
function_decl|;
DECL|method|newChange (Project.NameKey project)
name|Change
name|newChange
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|ChangeColumns
name|c
init|=
name|checkNotNull
argument_list|(
name|columns
argument_list|()
argument_list|,
literal|"columns are required"
argument_list|)
decl_stmt|;
name|Change
name|change
init|=
operator|new
name|Change
argument_list|(
name|c
operator|.
name|changeKey
argument_list|()
argument_list|,
name|changeId
argument_list|()
argument_list|,
name|c
operator|.
name|owner
argument_list|()
argument_list|,
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
name|c
operator|.
name|branch
argument_list|()
argument_list|)
argument_list|,
name|c
operator|.
name|createdOn
argument_list|()
argument_list|)
decl_stmt|;
name|copyNonConstructorColumnsTo
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|change
operator|.
name|setNoteDbState
argument_list|(
name|NoteDbChangeState
operator|.
name|NOTE_DB_PRIMARY_STATE
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
DECL|method|copyColumnsTo (Change change)
name|void
name|copyColumnsTo
parameter_list|(
name|Change
name|change
parameter_list|)
throws|throws
name|IOException
block|{
name|ChangeColumns
name|c
init|=
name|columns
argument_list|()
decl_stmt|;
name|checkState
argument_list|(
name|c
operator|!=
literal|null
operator|&&
name|metaId
argument_list|()
operator|!=
literal|null
argument_list|,
literal|"missing columns or metaId in ChangeNotesState; is NoteDb enabled? %s"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|checkMetaId
argument_list|(
name|change
argument_list|)
expr_stmt|;
name|change
operator|.
name|setKey
argument_list|(
name|c
operator|.
name|changeKey
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setOwner
argument_list|(
name|c
operator|.
name|owner
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setDest
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|c
operator|.
name|branch
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|change
operator|.
name|setCreatedOn
argument_list|(
name|c
operator|.
name|createdOn
argument_list|()
argument_list|)
expr_stmt|;
name|copyNonConstructorColumnsTo
argument_list|(
name|change
argument_list|)
expr_stmt|;
block|}
DECL|method|checkMetaId (Change change)
specifier|private
name|void
name|checkMetaId
parameter_list|(
name|Change
name|change
parameter_list|)
throws|throws
name|IOException
block|{
name|NoteDbChangeState
name|state
init|=
name|NoteDbChangeState
operator|.
name|parse
argument_list|(
name|change
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return;
comment|// Can happen during small NoteDb tests.
block|}
elseif|else
if|if
condition|(
name|state
operator|.
name|getPrimaryStorage
argument_list|()
operator|==
name|PrimaryStorage
operator|.
name|NOTE_DB
condition|)
block|{
return|return;
block|}
name|checkState
argument_list|(
name|state
operator|.
name|getRefState
argument_list|()
operator|.
name|isPresent
argument_list|()
argument_list|,
literal|"expected RefState: %s"
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|ObjectId
name|idFromState
init|=
name|state
operator|.
name|getRefState
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|changeMetaId
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|idFromState
operator|.
name|equals
argument_list|(
name|metaId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"cannot copy ChangeNotesState into Change "
operator|+
name|changeId
argument_list|()
operator|+
literal|"; this ChangeNotesState was created from "
operator|+
name|metaId
argument_list|()
operator|+
literal|", but change requires state "
operator|+
name|idFromState
argument_list|)
throw|;
block|}
block|}
DECL|method|copyNonConstructorColumnsTo (Change change)
specifier|private
name|void
name|copyNonConstructorColumnsTo
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|ChangeColumns
name|c
init|=
name|checkNotNull
argument_list|(
name|columns
argument_list|()
argument_list|,
literal|"columns are required"
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|status
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|change
operator|.
name|setStatus
argument_list|(
name|c
operator|.
name|status
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|change
operator|.
name|setTopic
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|c
operator|.
name|topic
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|change
operator|.
name|setLastUpdatedOn
argument_list|(
name|c
operator|.
name|lastUpdatedOn
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setSubmissionId
argument_list|(
name|c
operator|.
name|submissionId
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setAssignee
argument_list|(
name|c
operator|.
name|assignee
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setPrivate
argument_list|(
name|c
operator|.
name|isPrivate
argument_list|()
operator|==
literal|null
condition|?
literal|false
else|:
name|c
operator|.
name|isPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setWorkInProgress
argument_list|(
name|c
operator|.
name|isWorkInProgress
argument_list|()
operator|==
literal|null
condition|?
literal|false
else|:
name|c
operator|.
name|isWorkInProgress
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setReviewStarted
argument_list|(
name|c
operator|.
name|hasReviewStarted
argument_list|()
operator|==
literal|null
condition|?
literal|false
else|:
name|c
operator|.
name|hasReviewStarted
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|.
name|setRevertOf
argument_list|(
name|c
operator|.
name|revertOf
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|patchSets
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|change
operator|.
name|setCurrentPatchSet
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|c
operator|.
name|subject
argument_list|()
argument_list|,
name|c
operator|.
name|originalSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// TODO(dborowitz): This should be an error, but for now it's required for
comment|// some tests to pass.
name|change
operator|.
name|clearCurrentPatchSet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

