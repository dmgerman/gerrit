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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|Sets
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
name|flogger
operator|.
name|FluentLogger
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
name|primitives
operator|.
name|Shorts
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
name|BadRequestException
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|LabelPermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|ProjectCache
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
name|io
operator|.
name|IOException
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
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate patchset approvals.  *  *<p>Approvals are overloaded, they represent both approvals and reviewers which should be CCed on  * a change. To ensure that reviewers are not lost there must always be an approval on each patchset  * for each reviewer, even if the reviewer hasn't actually given a score to the change. To mark the  * "no score" case, a dummy approval, which may live in any of the available categories, with a  * score of 0 is used.  *  *<p>The methods in this class only modify the gwtorm database.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ApprovalsUtil
specifier|public
class|class
name|ApprovalsUtil
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|newApproval ( PatchSet.Id psId, CurrentUser user, LabelId labelId, int value, Date when)
specifier|public
specifier|static
name|PatchSetApproval
name|newApproval
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|LabelId
name|labelId
parameter_list|,
name|int
name|value
parameter_list|,
name|Date
name|when
parameter_list|)
block|{
name|PatchSetApproval
name|psa
init|=
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
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|labelId
argument_list|)
argument_list|,
name|Shorts
operator|.
name|checkedCast
argument_list|(
name|value
argument_list|)
argument_list|,
name|when
argument_list|)
decl_stmt|;
name|user
operator|.
name|updateRealAccountId
argument_list|(
name|psa
operator|::
name|setRealAccountId
argument_list|)
expr_stmt|;
return|return
name|psa
return|;
block|}
DECL|method|filterApprovals ( Iterable<PatchSetApproval> psas, Account.Id accountId)
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
name|a
lambda|->
name|Objects
operator|.
name|equals
argument_list|(
name|a
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|accountId
argument_list|)
argument_list|)
return|;
block|}
DECL|field|copier
specifier|private
specifier|final
name|ApprovalCopier
name|copier
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|ApprovalsUtil ( ApprovalCopier copier, PermissionBackend permissionBackend, ProjectCache projectCache)
specifier|public
name|ApprovalsUtil
parameter_list|(
name|ApprovalCopier
name|copier
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|copier
operator|=
name|copier
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
comment|/**    * Get all reviewers for a change.    *    * @param notes change notes.    * @return reviewers for the change.    * @throws OrmException if reviewers for the change could not be read.    */
DECL|method|getReviewers (ChangeNotes notes)
specifier|public
name|ReviewerSet
name|getReviewers
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
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
comment|/**    * Get all reviewers and CCed accounts for a change.    *    * @param allApprovals all approvals to consider; must all belong to the same change.    * @return reviewers for the change.    * @throws OrmException if reviewers for the change could not be read.    */
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
comment|/**    * Get updates to reviewer set.    *    * @param notes change notes.    * @return reviewer updates for the change.    * @throws OrmException if reviewer updates for the change could not be read.    */
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
DECL|method|addReviewers ( ChangeUpdate update, LabelTypes labelTypes, Change change, PatchSet ps, PatchSetInfo info, Iterable<Account.Id> wantReviewers, Collection<Account.Id> existingReviewers)
specifier|public
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
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
block|{
return|return
name|addReviewers
argument_list|(
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
DECL|method|addReviewers ( ChangeNotes notes, ChangeUpdate update, LabelTypes labelTypes, Change change, Iterable<Account.Id> wantReviewers)
specifier|public
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
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
name|update
argument_list|,
name|labelTypes
argument_list|,
name|change
argument_list|,
name|psId
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
DECL|method|addReviewers ( ChangeUpdate update, LabelTypes labelTypes, Change change, PatchSet.Id psId, Account.Id authorId, Account.Id committerId, Iterable<Account.Id> wantReviewers, Collection<Account.Id> existingReviewers)
specifier|private
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|addReviewers
parameter_list|(
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
name|canSee
argument_list|(
name|update
operator|.
name|getNotes
argument_list|()
argument_list|,
name|authorId
argument_list|)
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
name|canSee
argument_list|(
name|update
operator|.
name|getNotes
argument_list|()
argument_list|,
name|committerId
argument_list|)
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
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|cells
argument_list|)
return|;
block|}
DECL|method|canSee (ChangeNotes notes, Account.Id accountId)
specifier|private
name|boolean
name|canSee
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|notes
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|statePermitsRead
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|accountId
argument_list|)
operator|.
name|change
argument_list|(
name|notes
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|PermissionBackendException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failed to check if account %d can see change %d"
argument_list|,
name|accountId
operator|.
name|get
argument_list|()
argument_list|,
name|notes
operator|.
name|getChangeId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
comment|/**    * Adds accounts to a change as reviewers in the CC state.    *    * @param notes change notes.    * @param update change update.    * @param wantCCs accounts to CC.    * @return whether a change was made.    * @throws OrmException    */
DECL|method|addCcs ( ChangeNotes notes, ChangeUpdate update, Collection<Account.Id> wantCCs)
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
DECL|method|addCcs ( ChangeUpdate update, Collection<Account.Id> wantCCs, ReviewerSet existingReviewers)
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
comment|/**    * Adds approvals to ChangeUpdate for a new patch set, and writes to NoteDb.    *    * @param update change update.    * @param labelTypes label types for the containing project.    * @param ps patch set being approved.    * @param user user adding approvals.    * @param approvals approvals to add.    * @throws RestApiException    * @throws OrmException    */
DECL|method|addApprovalsForNewPatchSet ( ChangeUpdate update, LabelTypes labelTypes, PatchSet ps, CurrentUser user, Map<String, Short> approvals)
specifier|public
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|addApprovalsForNewPatchSet
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|CurrentUser
name|user
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
name|RestApiException
throws|,
name|OrmException
throws|,
name|PermissionBackendException
block|{
name|Account
operator|.
name|Id
name|accountId
init|=
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|accountId
operator|.
name|equals
argument_list|(
name|ps
operator|.
name|getUploader
argument_list|()
argument_list|)
argument_list|,
literal|"expected user %s to match patch set uploader %s"
argument_list|,
name|accountId
argument_list|,
name|ps
operator|.
name|getUploader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|approvals
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
name|checkApprovals
argument_list|(
name|approvals
argument_list|,
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|change
argument_list|(
name|update
operator|.
name|getNotes
argument_list|()
argument_list|)
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
name|newApproval
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|user
argument_list|,
name|lt
operator|.
name|getLabelId
argument_list|()
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
block|}
for|for
control|(
name|PatchSetApproval
name|psa
range|:
name|cells
control|)
block|{
name|update
operator|.
name|putApproval
argument_list|(
name|psa
operator|.
name|getLabel
argument_list|()
argument_list|,
name|psa
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|cells
return|;
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
throws|throws
name|BadRequestException
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
name|BadRequestException
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
name|BadRequestException
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
DECL|method|checkApprovals ( Map<String, Short> approvals, PermissionBackend.ForChange forChange)
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
name|PermissionBackend
operator|.
name|ForChange
name|forChange
parameter_list|)
throws|throws
name|AuthException
throws|,
name|PermissionBackendException
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
try|try
block|{
name|forChange
operator|.
name|check
argument_list|(
operator|new
name|LabelPermission
operator|.
name|WithValue
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AuthException
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
DECL|method|byChange (ChangeNotes notes)
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
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
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
DECL|method|byPatchSet ( ChangeNotes notes, PatchSet.Id psId, @Nullable RevWalk rw, @Nullable Config repoConfig)
specifier|public
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSet
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
annotation|@
name|Nullable
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|Config
name|repoConfig
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|copier
operator|.
name|getForPatchSet
argument_list|(
name|notes
argument_list|,
name|psId
argument_list|,
name|rw
argument_list|,
name|repoConfig
argument_list|)
return|;
block|}
DECL|method|byPatchSetUser ( ChangeNotes notes, PatchSet.Id psId, Account.Id accountId, @Nullable RevWalk rw, @Nullable Config repoConfig)
specifier|public
name|Iterable
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSetUser
parameter_list|(
name|ChangeNotes
name|notes
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
parameter_list|,
annotation|@
name|Nullable
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|Config
name|repoConfig
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|filterApprovals
argument_list|(
name|byPatchSet
argument_list|(
name|notes
argument_list|,
name|psId
argument_list|,
name|rw
argument_list|,
name|repoConfig
argument_list|)
argument_list|,
name|accountId
argument_list|)
return|;
block|}
DECL|method|getSubmitter (ChangeNotes notes, PatchSet.Id c)
specifier|public
name|PatchSetApproval
name|getSubmitter
parameter_list|(
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
DECL|method|renderMessageWithApprovals ( int patchSetId, Map<String, Short> n, Map<String, PatchSetApproval> c)
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

