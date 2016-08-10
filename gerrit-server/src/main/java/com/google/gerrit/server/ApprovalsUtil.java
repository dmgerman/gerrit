begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|notedb
operator|.
name|ReviewerStateInternal
operator|.
name|CC
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
name|notedb
operator|.
name|ReviewerStateInternal
operator|.
name|REVIEWER
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Function
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
name|Predicate
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
name|Iterables
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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|Ordering
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
name|LabelTypes
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
name|common
operator|.
name|data
operator|.
name|PermissionRange
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
name|LabelId
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
name|PatchSetInfo
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
name|server
operator|.
name|ReviewDb
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
name|ChangeNotes
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
name|ChangeUpdate
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
name|NotesMigration
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
name|ReviewerStateInternal
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
name|ChangeControl
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
name|util
operator|.
name|LabelVote
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Objects
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

begin_comment
comment|/**  * Utility functions to manipulate patchset approvals.  *<p>  * Approvals are overloaded, they represent both approvals and reviewers  * which should be CCed on a change.  To ensure that reviewers are not lost  * there must always be an approval on each patchset for each reviewer,  * even if the reviewer hasn't actually given a score to the change.  To  * mark the "no score" case, a dummy approval, which may live in any of  * the available categories, with a score of 0 is used.  *<p>  * The methods in this class only modify the gwtorm database.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ApprovalsUtil
specifier|public
class|class
name|ApprovalsUtil
block|{
DECL|field|SORT_APPROVALS
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|PatchSetApproval
argument_list|>
name|SORT_APPROVALS
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|PatchSetApproval
argument_list|,
name|Timestamp
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Timestamp
name|apply
parameter_list|(
name|PatchSetApproval
name|a
parameter_list|)
block|{
return|return
name|a
operator|.
name|getGranted
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|method|sortApprovals ( Iterable<PatchSetApproval> approvals)
specifier|public
specifier|static
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|sortApprovals
parameter_list|(
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|)
block|{
return|return
name|SORT_APPROVALS
operator|.
name|sortedCopy
argument_list|(
name|approvals
argument_list|)
return|;
block|}
DECL|method|filterApprovals ( Iterable<PatchSetApproval> psas, final Account.Id accountId)
specifier|private
specifier|static
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|filterApprovals
parameter_list|(
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|psas
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
return|return
name|Iterables
operator|.
name|filter
argument_list|(
name|psas
argument_list|,
operator|new
name|Predicate
argument_list|<
name|PatchSetApproval
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|PatchSetApproval
name|input
parameter_list|)
block|{
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|input
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|accountId
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|copier
specifier|private
specifier|final
name|ApprovalCopier
name|copier
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|ApprovalsUtil (NotesMigration migration, ApprovalCopier copier)
specifier|public
name|ApprovalsUtil
parameter_list|(
name|NotesMigration
name|migration
parameter_list|,
name|ApprovalCopier
name|copier
parameter_list|)
block|{
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|copier
operator|=
name|copier
expr_stmt|;
block|}
comment|/**    * Get all reviewers for a change.    *    * @param db review database.    * @param notes change notes.    * @return reviewers for the change.    * @throws OrmException if reviewers for the change could not be read.    */
DECL|method|getReviewers (ReviewDb db, ChangeNotes notes)
specifier|public
name|ReviewerSet
name|getReviewers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|ReviewerSet
operator|.
name|fromApprovals
argument_list|(
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getReviewers
argument_list|()
return|;
block|}
comment|/**    * Get all reviewers and CCed accounts for a change.    *    * @param allApprovals all approvals to consider; must all belong to the same    *     change.    * @return reviewers for the change.    * @throws OrmException if reviewers for the change could not be read.    */
DECL|method|getReviewers (ChangeNotes notes, Iterable<PatchSetApproval> allApprovals)
specifier|public
name|ReviewerSet
name|getReviewers
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|allApprovals
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|ReviewerSet
operator|.
name|fromApprovals
argument_list|(
name|allApprovals
argument_list|)
return|;
block|}
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getReviewers
argument_list|()
return|;
block|}
comment|/**    * Get updates to reviewer set.    * Always returns empty list for ReviewDb.    *    * @param notes change notes.    * @return reviewer updates for the change.    * @throws OrmException if reviewer updates for the change could not be read.    */
DECL|method|getReviewerUpdates (ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|ReviewerStatusUpdate
argument_list|>
name|getReviewerUpdates
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getReviewerUpdates
argument_list|()
return|;
block|}
DECL|method|addReviewers (ReviewDb db, ChangeUpdate update, LabelTypes labelTypes, Change change, PatchSet ps, PatchSetInfo info, Iterable<Account.Id> wantReviewers, Collection<Account.Id> existingReviewers)
specifier|public
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|PatchSetInfo
name|info
parameter_list|,
name|Iterable
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|wantReviewers
parameter_list|,
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|existingReviewers
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|addReviewers
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|labelTypes
argument_list|,
name|change
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|ps
operator|.
name|isDraft
argument_list|()
argument_list|,
name|info
operator|.
name|getAuthor
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|,
name|info
operator|.
name|getCommitter
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|,
name|wantReviewers
argument_list|,
name|existingReviewers
argument_list|)
return|;
block|}
DECL|method|addReviewers (ReviewDb db, ChangeNotes notes, ChangeUpdate update, LabelTypes labelTypes, Change change, Iterable<Account.Id> wantReviewers)
specifier|public
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|Change
name|change
parameter_list|,
name|Iterable
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|wantReviewers
parameter_list|)
throws|throws
name|OrmException
block|{
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|existingReviewers
decl_stmt|;
if|if
condition|(
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
comment|// If using NoteDB, we only want reviewers in the REVIEWER state.
name|existingReviewers
operator|=
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getReviewers
argument_list|()
operator|.
name|byState
argument_list|(
name|REVIEWER
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Prior to NoteDB, we gather all reviewers regardless of state.
name|existingReviewers
operator|=
name|getReviewers
argument_list|(
name|db
argument_list|,
name|notes
argument_list|)
operator|.
name|all
argument_list|()
expr_stmt|;
block|}
comment|// Existing reviewers should include pending additions in the REVIEWER
comment|// state, taken from ChangeUpdate.
name|existingReviewers
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|existingReviewers
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ReviewerStateInternal
argument_list|>
name|entry
range|:
name|update
operator|.
name|getReviewers
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
name|REVIEWER
condition|)
block|{
name|existingReviewers
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|addReviewers
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|labelTypes
argument_list|,
name|change
argument_list|,
name|psId
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|wantReviewers
argument_list|,
name|existingReviewers
argument_list|)
return|;
block|}
DECL|method|addReviewers (ReviewDb db, ChangeUpdate update, LabelTypes labelTypes, Change change, PatchSet.Id psId, boolean isDraft, Account.Id authorId, Account.Id committerId, Iterable<Account.Id> wantReviewers, Collection<Account.Id> existingReviewers)
specifier|private
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|boolean
name|isDraft
parameter_list|,
name|Account
operator|.
name|Id
name|authorId
parameter_list|,
name|Account
operator|.
name|Id
name|committerId
parameter_list|,
name|Iterable
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|wantReviewers
parameter_list|,
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|existingReviewers
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|LabelType
argument_list|>
name|allTypes
init|=
name|labelTypes
operator|.
name|getLabelTypes
argument_list|()
decl_stmt|;
if|if
condition|(
name|allTypes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|need
init|=
name|Sets
operator|.
name|newLinkedHashSet
argument_list|(
name|wantReviewers
argument_list|)
decl_stmt|;
if|if
condition|(
name|authorId
operator|!=
literal|null
operator|&&
operator|!
name|isDraft
condition|)
block|{
name|need
operator|.
name|add
argument_list|(
name|authorId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|committerId
operator|!=
literal|null
operator|&&
operator|!
name|isDraft
condition|)
block|{
name|need
operator|.
name|add
argument_list|(
name|committerId
argument_list|)
expr_stmt|;
block|}
name|need
operator|.
name|remove
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|need
operator|.
name|removeAll
argument_list|(
name|existingReviewers
argument_list|)
expr_stmt|;
if|if
condition|(
name|need
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|cells
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|need
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|LabelId
name|labelId
init|=
name|Iterables
operator|.
name|getLast
argument_list|(
name|allTypes
argument_list|)
operator|.
name|getLabelId
argument_list|()
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|account
range|:
name|need
control|)
block|{
name|cells
operator|.
name|add
argument_list|(
operator|new
name|PatchSetApproval
argument_list|(
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|psId
argument_list|,
name|account
argument_list|,
name|labelId
argument_list|)
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|,
name|update
operator|.
name|getWhen
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|putReviewer
argument_list|(
name|account
argument_list|,
name|REVIEWER
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|insert
argument_list|(
name|cells
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|cells
argument_list|)
return|;
block|}
comment|/**    * Adds accounts to a change as reviewers in the CC state.    *    * @param notes change notes.    * @param update change update.    * @param wantCCs accounts to CC.    * @return whether a change was made.    * @throws OrmException    */
DECL|method|addCcs (ChangeNotes notes, ChangeUpdate update, Collection<Account.Id> wantCCs)
specifier|public
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|addCcs
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|wantCCs
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|addCcs
argument_list|(
name|update
argument_list|,
name|wantCCs
argument_list|,
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getReviewers
argument_list|()
argument_list|)
return|;
block|}
DECL|method|addCcs (ChangeUpdate update, Collection<Account.Id> wantCCs, ReviewerSet existingReviewers)
specifier|private
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|addCcs
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|,
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|wantCCs
parameter_list|,
name|ReviewerSet
name|existingReviewers
parameter_list|)
block|{
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|need
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|(
name|wantCCs
argument_list|)
decl_stmt|;
name|need
operator|.
name|removeAll
argument_list|(
name|existingReviewers
operator|.
name|all
argument_list|()
argument_list|)
expr_stmt|;
name|need
operator|.
name|removeAll
argument_list|(
name|update
operator|.
name|getReviewers
argument_list|()
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|account
range|:
name|need
control|)
block|{
name|update
operator|.
name|putReviewer
argument_list|(
name|account
argument_list|,
name|CC
argument_list|)
expr_stmt|;
block|}
return|return
name|need
return|;
block|}
DECL|method|addApprovals (ReviewDb db, ChangeUpdate update, LabelTypes labelTypes, PatchSet ps, ChangeControl changeCtl, Map<String, Short> approvals)
specifier|public
name|void
name|addApprovals
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|ChangeControl
name|changeCtl
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|approvals
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|checkApprovals
argument_list|(
name|approvals
argument_list|,
name|changeCtl
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|cells
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|approvals
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|Date
name|ts
init|=
name|update
operator|.
name|getWhen
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|vote
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|LabelType
name|lt
init|=
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|vote
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|cells
operator|.
name|add
argument_list|(
operator|new
name|PatchSetApproval
argument_list|(
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|ps
operator|.
name|getUploader
argument_list|()
argument_list|,
name|lt
operator|.
name|getLabelId
argument_list|()
argument_list|)
argument_list|,
name|vote
operator|.
name|getValue
argument_list|()
argument_list|,
name|ts
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|putApproval
argument_list|(
name|vote
operator|.
name|getKey
argument_list|()
argument_list|,
name|vote
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|insert
argument_list|(
name|cells
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkLabel (LabelTypes labelTypes, String name, Short value)
specifier|public
specifier|static
name|void
name|checkLabel
parameter_list|(
name|LabelTypes
name|labelTypes
parameter_list|,
name|String
name|name
parameter_list|,
name|Short
name|value
parameter_list|)
block|{
name|LabelType
name|label
init|=
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"label \"%s\" is not a configured label"
argument_list|,
name|name
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|label
operator|.
name|getValue
argument_list|(
name|value
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"label \"%s\": %d is not a valid value"
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|checkApprovals (Map<String, Short> approvals, ChangeControl changeCtl)
specifier|private
specifier|static
name|void
name|checkApprovals
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|approvals
parameter_list|,
name|ChangeControl
name|changeCtl
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|vote
range|:
name|approvals
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|name
init|=
name|vote
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Short
name|value
init|=
name|vote
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|PermissionRange
name|range
init|=
name|changeCtl
operator|.
name|getRange
argument_list|(
name|Permission
operator|.
name|forLabel
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|range
operator|==
literal|null
operator|||
operator|!
name|range
operator|.
name|contains
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"applying label \"%s\": %d is restricted"
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|byChange (ReviewDb db, ChangeNotes notes)
specifier|public
name|ListMultimap
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|byChange
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
name|ImmutableListMultimap
operator|.
name|Builder
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|result
init|=
name|ImmutableListMultimap
operator|.
name|builder
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|psa
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|psa
argument_list|)
expr_stmt|;
block|}
return|return
name|result
operator|.
name|build
argument_list|()
return|;
block|}
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getApprovals
argument_list|()
return|;
block|}
DECL|method|byPatchSet (ReviewDb db, ChangeControl ctl, PatchSet.Id psId)
specifier|public
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|sortApprovals
argument_list|(
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|psId
argument_list|)
argument_list|)
return|;
block|}
return|return
name|copier
operator|.
name|getForPatchSet
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|,
name|psId
argument_list|)
return|;
block|}
DECL|method|byPatchSetUser (ReviewDb db, ChangeControl ctl, PatchSet.Id psId, Account.Id accountId)
specifier|public
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSetUser
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeControl
name|ctl
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|sortApprovals
argument_list|(
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|byPatchSetUser
argument_list|(
name|psId
argument_list|,
name|accountId
argument_list|)
argument_list|)
return|;
block|}
return|return
name|filterApprovals
argument_list|(
name|byPatchSet
argument_list|(
name|db
argument_list|,
name|ctl
argument_list|,
name|psId
argument_list|)
argument_list|,
name|accountId
argument_list|)
return|;
block|}
DECL|method|getSubmitter (ReviewDb db, ChangeNotes notes, PatchSet.Id c)
specifier|public
name|PatchSetApproval
name|getSubmitter
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|// Submit approval is never copied, so bypass expensive byPatchSet call.
return|return
name|getSubmitter
argument_list|(
name|c
argument_list|,
name|byChange
argument_list|(
name|db
argument_list|,
name|notes
argument_list|)
operator|.
name|get
argument_list|(
name|c
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getSubmitter (PatchSet.Id c, Iterable<PatchSetApproval> approvals)
specifier|public
specifier|static
name|PatchSetApproval
name|getSubmitter
parameter_list|(
name|PatchSet
operator|.
name|Id
name|c
parameter_list|,
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
parameter_list|)
block|{
if|if
condition|(
name|c
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PatchSetApproval
name|submitter
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|approvals
control|)
block|{
if|if
condition|(
name|a
operator|.
name|getPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|c
argument_list|)
operator|&&
name|a
operator|.
name|getValue
argument_list|()
operator|>
literal|0
operator|&&
name|a
operator|.
name|isLegacySubmit
argument_list|()
condition|)
block|{
if|if
condition|(
name|submitter
operator|==
literal|null
operator|||
name|a
operator|.
name|getGranted
argument_list|()
operator|.
name|compareTo
argument_list|(
name|submitter
operator|.
name|getGranted
argument_list|()
argument_list|)
operator|>
literal|0
condition|)
block|{
name|submitter
operator|=
name|a
expr_stmt|;
block|}
block|}
block|}
return|return
name|submitter
return|;
block|}
DECL|method|renderMessageWithApprovals (int patchSetId, Map<String, Short> n, Map<String, PatchSetApproval> c)
specifier|public
specifier|static
name|String
name|renderMessageWithApprovals
parameter_list|(
name|int
name|patchSetId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|n
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|PatchSetApproval
argument_list|>
name|c
parameter_list|)
block|{
name|StringBuilder
name|msgs
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Uploaded patch set "
operator|+
name|patchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|n
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|boolean
name|first
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|e
range|:
name|n
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|containsKey
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|&&
name|c
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|.
name|getValue
argument_list|()
operator|==
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|first
condition|)
block|{
name|msgs
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|first
operator|=
literal|false
expr_stmt|;
block|}
name|msgs
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|LabelVote
operator|.
name|create
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|format
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|msgs
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

