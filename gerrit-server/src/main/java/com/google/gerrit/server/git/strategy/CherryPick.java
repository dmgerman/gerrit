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
DECL|package|com.google.gerrit.server.git.strategy
package|package
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
name|strategy
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
name|Lists
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetAncestor
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
name|ChangeUtil
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
name|IdentifiedUser
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|CodeReviewCommit
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
name|CommitMergeStatus
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
name|MergeException
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
name|patch
operator|.
name|PatchSetInfoFactory
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
name|NoSuchChangeException
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
name|TimeUtil
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
name|lib
operator|.
name|PersonIdent
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
name|RefUpdate
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
DECL|class|CherryPick
specifier|public
class|class
name|CherryPick
extends|extends
name|SubmitStrategy
block|{
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|newCommits
specifier|private
specifier|final
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|newCommits
decl_stmt|;
DECL|method|CherryPick (final SubmitStrategy.Arguments args, final PatchSetInfoFactory patchSetInfoFactory, final GitReferenceUpdated gitRefUpdated)
name|CherryPick
parameter_list|(
specifier|final
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|,
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|this
operator|.
name|patchSetInfoFactory
operator|=
name|patchSetInfoFactory
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
expr_stmt|;
name|this
operator|.
name|newCommits
operator|=
operator|new
name|HashMap
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|_run (CodeReviewCommit mergeTip, final List<CodeReviewCommit> toMerge)
specifier|protected
name|CodeReviewCommit
name|_run
parameter_list|(
name|CodeReviewCommit
name|mergeTip
parameter_list|,
specifier|final
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|MergeException
block|{
while|while
condition|(
operator|!
name|toMerge
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|CodeReviewCommit
name|n
init|=
name|toMerge
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|mergeTip
operator|==
literal|null
condition|)
block|{
comment|// The branch is unborn. Take a fast-forward resolution to
comment|// create the branch.
comment|//
name|mergeTip
operator|=
name|n
expr_stmt|;
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_MERGE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|getParentCount
argument_list|()
operator|==
literal|0
condition|)
block|{
comment|// Refuse to merge a root commit into an existing branch,
comment|// we cannot obtain a delta for the cherry-pick to apply.
comment|//
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CANNOT_CHERRY_PICK_ROOT
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|getParentCount
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// If there is only one parent, a cherry-pick can be done by
comment|// taking the delta relative to that one parent and redoing
comment|// that on the current merge tip.
comment|//
name|mergeTip
operator|=
name|writeCherryPickCommit
argument_list|(
name|mergeTip
argument_list|,
name|n
argument_list|)
expr_stmt|;
if|if
condition|(
name|mergeTip
operator|!=
literal|null
condition|)
block|{
name|newCommits
operator|.
name|put
argument_list|(
name|mergeTip
operator|.
name|getPatchsetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|mergeTip
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|PATH_CONFLICT
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// There are multiple parents, so this is a merge commit. We
comment|// don't want to cherry-pick the merge as clients can't easily
comment|// rebase their history with that merge present and replaced
comment|// by an equivalent merge with a different first parent. So
comment|// instead behave as though MERGE_IF_NECESSARY was configured.
comment|//
if|if
condition|(
operator|!
name|args
operator|.
name|mergeUtil
operator|.
name|hasMissingDependencies
argument_list|(
name|args
operator|.
name|mergeSorter
argument_list|,
name|n
argument_list|)
condition|)
block|{
if|if
condition|(
name|args
operator|.
name|rw
operator|.
name|isMergedInto
argument_list|(
name|mergeTip
argument_list|,
name|n
argument_list|)
condition|)
block|{
name|mergeTip
operator|=
name|n
expr_stmt|;
block|}
else|else
block|{
name|mergeTip
operator|=
name|args
operator|.
name|mergeUtil
operator|.
name|mergeOneCommit
argument_list|(
name|args
operator|.
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
name|args
operator|.
name|repo
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|args
operator|.
name|inserter
argument_list|,
name|args
operator|.
name|canMergeFlag
argument_list|,
name|args
operator|.
name|destBranch
argument_list|,
name|mergeTip
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
specifier|final
name|PatchSetApproval
name|submitApproval
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|markCleanMerges
argument_list|(
name|args
operator|.
name|rw
argument_list|,
name|args
operator|.
name|canMergeFlag
argument_list|,
name|mergeTip
argument_list|,
name|args
operator|.
name|alreadyAccepted
argument_list|)
decl_stmt|;
name|setRefLogIdent
argument_list|(
name|submitApproval
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// One or more dependencies were not met. The status was
comment|// already marked on the commit so we have nothing further
comment|// to perform at this time.
comment|//
block|}
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
decl||
name|IOException
decl||
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MergeException
argument_list|(
literal|"Cannot merge "
operator|+
name|n
operator|.
name|name
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|mergeTip
return|;
block|}
DECL|method|writeCherryPickCommit (CodeReviewCommit mergeTip, CodeReviewCommit n)
specifier|private
name|CodeReviewCommit
name|writeCherryPickCommit
parameter_list|(
name|CodeReviewCommit
name|mergeTip
parameter_list|,
name|CodeReviewCommit
name|n
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
throws|,
name|NoSuchChangeException
block|{
name|args
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|n
argument_list|)
expr_stmt|;
specifier|final
name|PatchSetApproval
name|submitAudit
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|getSubmitter
argument_list|(
name|n
argument_list|)
decl_stmt|;
name|IdentifiedUser
name|cherryPickUser
decl_stmt|;
name|PersonIdent
name|cherryPickCommitterIdent
decl_stmt|;
if|if
condition|(
name|submitAudit
operator|!=
literal|null
condition|)
block|{
name|cherryPickUser
operator|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|submitAudit
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cherryPickCommitterIdent
operator|=
name|cherryPickUser
operator|.
name|newCommitterIdent
argument_list|(
name|submitAudit
operator|.
name|getGranted
argument_list|()
argument_list|,
name|args
operator|.
name|serverIdent
operator|.
name|get
argument_list|()
operator|.
name|getTimeZone
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cherryPickUser
operator|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|n
operator|.
name|change
argument_list|()
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|cherryPickCommitterIdent
operator|=
name|args
operator|.
name|serverIdent
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
specifier|final
name|String
name|cherryPickCmtMsg
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|createCherryPickCommitMessage
argument_list|(
name|n
argument_list|)
decl_stmt|;
specifier|final
name|CodeReviewCommit
name|newCommit
init|=
operator|(
name|CodeReviewCommit
operator|)
name|args
operator|.
name|mergeUtil
operator|.
name|createCherryPickFromCommit
argument_list|(
name|args
operator|.
name|repo
argument_list|,
name|args
operator|.
name|inserter
argument_list|,
name|mergeTip
argument_list|,
name|n
argument_list|,
name|cherryPickCommitterIdent
argument_list|,
name|cherryPickCmtMsg
argument_list|,
name|args
operator|.
name|rw
argument_list|)
decl_stmt|;
if|if
condition|(
name|newCommit
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PatchSet
operator|.
name|Id
name|id
init|=
name|ChangeUtil
operator|.
name|nextPatchSetId
argument_list|(
name|args
operator|.
name|repo
argument_list|,
name|n
operator|.
name|change
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|PatchSet
name|ps
init|=
operator|new
name|PatchSet
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|ps
operator|.
name|setCreatedOn
argument_list|(
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setUploader
argument_list|(
name|cherryPickUser
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|ps
operator|.
name|setRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|newCommit
operator|.
name|getId
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|RefUpdate
name|ru
decl_stmt|;
name|args
operator|.
name|db
operator|.
name|changes
argument_list|()
operator|.
name|beginTransaction
argument_list|(
name|n
operator|.
name|change
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|insertAncestors
argument_list|(
name|args
operator|.
name|db
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|newCommit
argument_list|)
expr_stmt|;
name|args
operator|.
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|ps
argument_list|)
argument_list|)
expr_stmt|;
name|n
operator|.
name|change
argument_list|()
operator|.
name|setCurrentPatchSet
argument_list|(
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|newCommit
argument_list|,
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|args
operator|.
name|db
operator|.
name|changes
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|n
operator|.
name|change
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|PatchSetApproval
argument_list|>
name|approvals
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|args
operator|.
name|approvalsUtil
operator|.
name|byPatchSet
argument_list|(
name|args
operator|.
name|db
argument_list|,
name|n
operator|.
name|notes
argument_list|()
argument_list|,
name|n
operator|.
name|getPatchsetId
argument_list|()
argument_list|)
control|)
block|{
name|approvals
operator|.
name|add
argument_list|(
operator|new
name|PatchSetApproval
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|args
operator|.
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|insert
argument_list|(
name|approvals
argument_list|)
expr_stmt|;
name|ru
operator|=
name|args
operator|.
name|repo
operator|.
name|updateRef
argument_list|(
name|ps
operator|.
name|getRefName
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
name|ru
operator|.
name|setNewObjectId
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
name|ru
operator|.
name|disableRefLog
argument_list|()
expr_stmt|;
if|if
condition|(
name|ru
operator|.
name|update
argument_list|(
name|args
operator|.
name|rw
argument_list|)
operator|!=
name|RefUpdate
operator|.
name|Result
operator|.
name|NEW
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to create ref %s in %s: %s"
argument_list|,
name|ps
operator|.
name|getRefName
argument_list|()
argument_list|,
name|n
operator|.
name|change
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|ru
operator|.
name|getResult
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|args
operator|.
name|db
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|args
operator|.
name|db
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
name|gitRefUpdated
operator|.
name|fire
argument_list|(
name|n
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|ru
argument_list|)
expr_stmt|;
name|newCommit
operator|.
name|copyFrom
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|newCommit
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_PICK
argument_list|)
expr_stmt|;
name|newCommit
operator|.
name|setControl
argument_list|(
name|args
operator|.
name|changeControlFactory
operator|.
name|controlFor
argument_list|(
name|n
operator|.
name|change
argument_list|()
argument_list|,
name|cherryPickUser
argument_list|)
argument_list|)
expr_stmt|;
name|newCommits
operator|.
name|put
argument_list|(
name|newCommit
operator|.
name|getPatchsetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|newCommit
argument_list|)
expr_stmt|;
name|setRefLogIdent
argument_list|(
name|submitAudit
argument_list|)
expr_stmt|;
return|return
name|newCommit
return|;
block|}
DECL|method|insertAncestors (ReviewDb db, PatchSet.Id id, RevCommit src)
specifier|private
specifier|static
name|void
name|insertAncestors
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|RevCommit
name|src
parameter_list|)
throws|throws
name|OrmException
block|{
specifier|final
name|int
name|cnt
init|=
name|src
operator|.
name|getParentCount
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PatchSetAncestor
argument_list|>
name|toInsert
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchSetAncestor
argument_list|>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|p
init|=
literal|0
init|;
name|p
operator|<
name|cnt
condition|;
name|p
operator|++
control|)
block|{
name|PatchSetAncestor
name|a
decl_stmt|;
name|a
operator|=
operator|new
name|PatchSetAncestor
argument_list|(
operator|new
name|PatchSetAncestor
operator|.
name|Id
argument_list|(
name|id
argument_list|,
name|p
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|a
operator|.
name|setAncestorRevision
argument_list|(
operator|new
name|RevId
argument_list|(
name|src
operator|.
name|getParent
argument_list|(
name|p
argument_list|)
operator|.
name|getId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|toInsert
operator|.
name|add
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|insert
argument_list|(
name|toInsert
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getNewCommits ()
specifier|public
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|CodeReviewCommit
argument_list|>
name|getNewCommits
parameter_list|()
block|{
return|return
name|newCommits
return|;
block|}
annotation|@
name|Override
DECL|method|dryRun (final CodeReviewCommit mergeTip, final CodeReviewCommit toMerge)
specifier|public
name|boolean
name|dryRun
parameter_list|(
specifier|final
name|CodeReviewCommit
name|mergeTip
parameter_list|,
specifier|final
name|CodeReviewCommit
name|toMerge
parameter_list|)
throws|throws
name|MergeException
block|{
return|return
name|args
operator|.
name|mergeUtil
operator|.
name|canCherryPick
argument_list|(
name|args
operator|.
name|mergeSorter
argument_list|,
name|args
operator|.
name|repo
argument_list|,
name|mergeTip
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|toMerge
argument_list|)
return|;
block|}
block|}
end_class

end_unit

