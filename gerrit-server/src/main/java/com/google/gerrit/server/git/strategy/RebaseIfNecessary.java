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
name|change
operator|.
name|PatchSetInserter
operator|.
name|ValidatePolicy
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
name|RebaseChange
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
name|MergeConflictException
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
name|git
operator|.
name|MergeTip
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
name|RebaseSorter
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
name|InvalidChangeOperationException
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
DECL|class|RebaseIfNecessary
specifier|public
class|class
name|RebaseIfNecessary
extends|extends
name|SubmitStrategy
block|{
DECL|field|patchSetInfoFactory
specifier|private
specifier|final
name|PatchSetInfoFactory
name|patchSetInfoFactory
decl_stmt|;
DECL|field|rebaseChange
specifier|private
specifier|final
name|RebaseChange
name|rebaseChange
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
DECL|method|RebaseIfNecessary (SubmitStrategy.Arguments args, PatchSetInfoFactory patchSetInfoFactory, RebaseChange rebaseChange)
name|RebaseIfNecessary
parameter_list|(
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|,
name|PatchSetInfoFactory
name|patchSetInfoFactory
parameter_list|,
name|RebaseChange
name|rebaseChange
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
name|rebaseChange
operator|=
name|rebaseChange
expr_stmt|;
name|this
operator|.
name|newCommits
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|_run (final CodeReviewCommit branchTip, final Collection<CodeReviewCommit> toMerge)
specifier|protected
name|MergeTip
name|_run
parameter_list|(
specifier|final
name|CodeReviewCommit
name|branchTip
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|MergeException
block|{
name|MergeTip
name|mergeTip
init|=
operator|new
name|MergeTip
argument_list|(
name|branchTip
argument_list|,
name|toMerge
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sorted
init|=
name|sort
argument_list|(
name|toMerge
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|sorted
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|CodeReviewCommit
name|n
init|=
name|sorted
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// The branch is unborn. Take a fast-forward resolution to
comment|// create the branch.
comment|//
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_MERGE
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|n
argument_list|,
name|n
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
comment|// we cannot obtain a delta for the rebase to apply.
comment|//
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CANNOT_REBASE_ROOT
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
if|if
condition|(
name|args
operator|.
name|mergeUtil
operator|.
name|canFastForward
argument_list|(
name|args
operator|.
name|mergeSorter
argument_list|,
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|n
argument_list|)
condition|)
block|{
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_MERGE
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|n
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
try|try
block|{
name|IdentifiedUser
name|uploader
init|=
name|args
operator|.
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|args
operator|.
name|mergeUtil
operator|.
name|getSubmitter
argument_list|(
name|n
argument_list|)
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
name|newPatchSet
init|=
name|rebaseChange
operator|.
name|rebase
argument_list|(
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
name|n
operator|.
name|getPatchsetId
argument_list|()
argument_list|,
name|n
operator|.
name|change
argument_list|()
argument_list|,
name|uploader
argument_list|,
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|args
operator|.
name|mergeUtil
argument_list|,
name|args
operator|.
name|serverIdent
operator|.
name|get
argument_list|()
argument_list|,
literal|false
argument_list|,
name|ValidatePolicy
operator|.
name|NONE
argument_list|)
decl_stmt|;
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
name|getControl
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
name|newPatchSet
operator|.
name|getId
argument_list|()
argument_list|,
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// rebaseChange.rebase() may already have copied some approvals,
comment|// use upsert, not insert, to avoid constraint violation on database
name|args
operator|.
name|db
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|upsert
argument_list|(
name|approvals
argument_list|)
expr_stmt|;
name|CodeReviewCommit
name|newTip
init|=
operator|(
name|CodeReviewCommit
operator|)
name|args
operator|.
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|newPatchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|newTip
argument_list|,
name|newTip
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
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|newPatchSet
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|n
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
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
name|uploader
argument_list|)
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|.
name|setPatchsetId
argument_list|(
name|newPatchSet
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_REBASE
argument_list|)
expr_stmt|;
name|newCommits
operator|.
name|put
argument_list|(
name|newPatchSet
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|)
expr_stmt|;
name|setRefLogIdent
argument_list|(
name|args
operator|.
name|mergeUtil
operator|.
name|getSubmitter
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MergeConflictException
name|e
parameter_list|)
block|{
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|REBASE_MERGE_CONFLICT
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
decl||
name|OrmException
decl||
name|IOException
decl||
name|InvalidChangeOperationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MergeException
argument_list|(
literal|"Cannot rebase "
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
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|getParentCount
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// There are multiple parents, so this is a merge commit. We
comment|// don't want to rebase the merge as clients can't easily
comment|// rebase their history with that merge present and replaced
comment|// by an equivalent merge with a different first parent. So
comment|// instead behave as though MERGE_IF_NECESSARY was configured.
comment|//
try|try
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
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|n
argument_list|)
condition|)
block|{
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|n
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
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
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|n
argument_list|)
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
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
operator|.
name|getCurrentTip
argument_list|()
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
catch|catch
parameter_list|(
name|IOException
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
name|args
operator|.
name|alreadyAccepted
operator|.
name|add
argument_list|(
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|mergeTip
return|;
block|}
DECL|method|sort (Collection<CodeReviewCommit> toSort)
specifier|private
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sort
parameter_list|(
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toSort
parameter_list|)
throws|throws
name|MergeException
block|{
try|try
block|{
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|result
init|=
operator|new
name|RebaseSorter
argument_list|(
name|args
operator|.
name|rw
argument_list|,
name|args
operator|.
name|alreadyAccepted
argument_list|,
name|args
operator|.
name|canMergeFlag
argument_list|)
operator|.
name|sort
argument_list|(
name|toSort
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|result
argument_list|,
name|CodeReviewCommit
operator|.
name|ORDER
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MergeException
argument_list|(
literal|"Commit sorting failed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
DECL|method|dryRun (CodeReviewCommit mergeTip, CodeReviewCommit toMerge)
specifier|public
name|boolean
name|dryRun
parameter_list|(
name|CodeReviewCommit
name|mergeTip
parameter_list|,
name|CodeReviewCommit
name|toMerge
parameter_list|)
throws|throws
name|MergeException
block|{
return|return
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
name|toMerge
argument_list|)
operator|&&
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

