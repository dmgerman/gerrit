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
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
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
name|git
operator|.
name|BatchUpdate
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
name|server
operator|.
name|git
operator|.
name|BatchUpdate
operator|.
name|RepoContext
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
name|IntegrationException
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
name|MergeIdenticalTreeException
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
name|transport
operator|.
name|ReceiveCommand
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
name|List
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
DECL|method|CherryPick (SubmitStrategy.Arguments args)
name|CherryPick
parameter_list|(
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildOps ( Collection<CodeReviewCommit> toMerge)
specifier|public
name|List
argument_list|<
name|SubmitStrategyOp
argument_list|>
name|buildOps
parameter_list|(
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
block|{
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sorted
init|=
name|CodeReviewCommit
operator|.
name|ORDER
operator|.
name|sortedCopy
argument_list|(
name|toMerge
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmitStrategyOp
argument_list|>
name|ops
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|sorted
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|true
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
name|first
operator|&&
name|args
operator|.
name|mergeTip
operator|.
name|getInitialTip
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ops
operator|.
name|add
argument_list|(
operator|new
name|CherryPickUnbornRootOp
argument_list|(
name|n
argument_list|)
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
name|ops
operator|.
name|add
argument_list|(
operator|new
name|CherryPickRootOp
argument_list|(
name|n
argument_list|)
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
name|ops
operator|.
name|add
argument_list|(
operator|new
name|CherryPickOneOp
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ops
operator|.
name|add
argument_list|(
operator|new
name|CherryPickMultipleParentsOp
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|first
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|ops
return|;
block|}
DECL|class|CherryPickUnbornRootOp
specifier|private
class|class
name|CherryPickUnbornRootOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|CherryPickUnbornRootOp (CodeReviewCommit toMerge)
specifier|private
name|CherryPickUnbornRootOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|CherryPick
operator|.
name|this
operator|.
name|args
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateRepoImpl (RepoContext ctx)
specifier|protected
name|void
name|updateRepoImpl
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
block|{
comment|// The branch is unborn. Take fast-forward resolution to create the
comment|// branch.
name|args
operator|.
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|toMerge
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_MERGE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|CherryPickRootOp
specifier|private
class|class
name|CherryPickRootOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|CherryPickRootOp (CodeReviewCommit toMerge)
specifier|private
name|CherryPickRootOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|CherryPick
operator|.
name|this
operator|.
name|args
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateRepoImpl (RepoContext ctx)
specifier|public
name|void
name|updateRepoImpl
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
block|{
comment|// Refuse to merge a root commit into an existing branch, we cannot obtain
comment|// a delta for the cherry-pick to apply.
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CANNOT_CHERRY_PICK_ROOT
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|CherryPickOneOp
specifier|private
class|class
name|CherryPickOneOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|field|psId
specifier|private
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|newCommit
specifier|private
name|CodeReviewCommit
name|newCommit
decl_stmt|;
DECL|field|patchSetInfo
specifier|private
name|PatchSetInfo
name|patchSetInfo
decl_stmt|;
DECL|method|CherryPickOneOp (CodeReviewCommit toMerge)
specifier|private
name|CherryPickOneOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|CherryPick
operator|.
name|this
operator|.
name|args
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateRepoImpl (RepoContext ctx)
specifier|protected
name|void
name|updateRepoImpl
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|IOException
block|{
comment|// If there is only one parent, a cherry-pick can be done by taking the
comment|// delta relative to that one parent and redoing that on the current merge
comment|// tip.
name|args
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|toMerge
argument_list|)
expr_stmt|;
name|psId
operator|=
name|ChangeUtil
operator|.
name|nextPatchSetId
argument_list|(
name|args
operator|.
name|repo
argument_list|,
name|toMerge
operator|.
name|change
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|cherryPickCmtMsg
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|createCherryPickCommitMessage
argument_list|(
name|toMerge
argument_list|)
decl_stmt|;
name|PersonIdent
name|committer
init|=
name|args
operator|.
name|caller
operator|.
name|newCommitterIdent
argument_list|(
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|args
operator|.
name|serverIdent
operator|.
name|getTimeZone
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|newCommit
operator|=
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
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|toMerge
argument_list|,
name|committer
argument_list|,
name|cherryPickCmtMsg
argument_list|,
name|args
operator|.
name|rw
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MergeConflictException
name|mce
parameter_list|)
block|{
comment|// Keep going in the case of a single merge failure; the goal is to
comment|// cherry-pick as many commits as possible.
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|PATH_CONFLICT
argument_list|)
expr_stmt|;
return|return;
block|}
catch|catch
parameter_list|(
name|MergeIdenticalTreeException
name|mie
parameter_list|)
block|{
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|ALREADY_MERGED
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// Initial copy doesn't have new patch set ID since change hasn't been
comment|// updated yet.
name|newCommit
operator|.
name|copyFrom
argument_list|(
name|toMerge
argument_list|)
expr_stmt|;
name|newCommit
operator|.
name|setPatchsetId
argument_list|(
name|psId
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
name|args
operator|.
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|newCommit
argument_list|,
name|newCommit
argument_list|)
expr_stmt|;
name|args
operator|.
name|commits
operator|.
name|put
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|addRefUpdate
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|newCommit
argument_list|,
name|psId
operator|.
name|toRefName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|patchSetInfo
operator|=
name|args
operator|.
name|patchSetInfoFactory
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|,
name|newCommit
argument_list|,
name|psId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateChangeImpl (ChangeContext ctx)
specifier|public
name|PatchSet
name|updateChangeImpl
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|OrmException
throws|,
name|NoSuchChangeException
throws|,
name|IOException
block|{
name|checkState
argument_list|(
name|newCommit
operator|!=
literal|null
argument_list|,
literal|"no new commit produced by CherryPick of %s, expected to fail fast"
argument_list|,
name|toMerge
operator|.
name|change
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|PatchSet
name|prevPs
init|=
name|args
operator|.
name|psUtil
operator|.
name|current
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
name|newPs
init|=
name|args
operator|.
name|psUtil
operator|.
name|insert
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctx
operator|.
name|getRevWalk
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
argument_list|,
name|psId
argument_list|,
name|newCommit
argument_list|,
literal|false
argument_list|,
name|prevPs
operator|!=
literal|null
condition|?
name|prevPs
operator|.
name|getGroups
argument_list|()
else|:
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setCurrentPatchSet
argument_list|(
name|patchSetInfo
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|saveChange
argument_list|()
expr_stmt|;
comment|// Don't copy approvals, as this is already taken care of by
comment|// SubmitStrategyOp.
name|newCommit
operator|.
name|setControl
argument_list|(
name|ctx
operator|.
name|getControl
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|newPs
return|;
block|}
block|}
DECL|class|CherryPickMultipleParentsOp
specifier|private
class|class
name|CherryPickMultipleParentsOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|CherryPickMultipleParentsOp (CodeReviewCommit toMerge)
specifier|private
name|CherryPickMultipleParentsOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|CherryPick
operator|.
name|this
operator|.
name|args
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateRepoImpl (RepoContext ctx)
specifier|public
name|void
name|updateRepoImpl
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|IntegrationException
throws|,
name|IOException
block|{
if|if
condition|(
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
condition|)
block|{
comment|// One or more dependencies were not met. The status was already marked
comment|// on the commit so we have nothing further to perform at this time.
return|return;
block|}
comment|// There are multiple parents, so this is a merge commit. We don't want
comment|// to cherry-pick the merge as clients can't easily rebase their history
comment|// with that merge present and replaced by an equivalent merge with a
comment|// different first parent. So instead behave as though MERGE_IF_NECESSARY
comment|// was configured.
name|MergeTip
name|mergeTip
init|=
name|args
operator|.
name|mergeTip
decl_stmt|;
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
name|toMerge
argument_list|)
condition|)
block|{
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|toMerge
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PersonIdent
name|myIdent
init|=
operator|new
name|PersonIdent
argument_list|(
name|args
operator|.
name|serverIdent
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
decl_stmt|;
name|CodeReviewCommit
name|result
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|mergeOneCommit
argument_list|(
name|myIdent
argument_list|,
name|myIdent
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
name|destBranch
argument_list|,
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|toMerge
argument_list|)
decl_stmt|;
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|result
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
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
expr_stmt|;
block|}
block|}
DECL|method|dryRun (SubmitDryRun.Arguments args, CodeReviewCommit mergeTip, CodeReviewCommit toMerge)
specifier|static
name|boolean
name|dryRun
parameter_list|(
name|SubmitDryRun
operator|.
name|Arguments
name|args
parameter_list|,
name|CodeReviewCommit
name|mergeTip
parameter_list|,
name|CodeReviewCommit
name|toMerge
parameter_list|)
throws|throws
name|IntegrationException
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

