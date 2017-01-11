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
import|import static
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
operator|.
name|CommitMergeStatus
operator|.
name|SKIPPED_IDENTICAL_TREE
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
name|ImmutableSet
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
name|change
operator|.
name|RebaseChangeOp
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
name|Context
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
name|git
operator|.
name|validators
operator|.
name|CommitValidators
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
name|revwalk
operator|.
name|RevCommit
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

begin_comment
comment|/**  * This strategy covers RebaseAlways and RebaseIfNecessary ones.  */
end_comment

begin_class
DECL|class|RebaseSubmitStrategy
specifier|public
class|class
name|RebaseSubmitStrategy
extends|extends
name|SubmitStrategy
block|{
DECL|field|rebaseAlways
specifier|private
specifier|final
name|boolean
name|rebaseAlways
decl_stmt|;
DECL|method|RebaseSubmitStrategy (SubmitStrategy.Arguments args, boolean rebaseAlways)
name|RebaseSubmitStrategy
parameter_list|(
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|,
name|boolean
name|rebaseAlways
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|this
operator|.
name|rebaseAlways
operator|=
name|rebaseAlways
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
throws|throws
name|IntegrationException
block|{
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sorted
init|=
name|sort
argument_list|(
name|toMerge
argument_list|,
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
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
for|for
control|(
name|CodeReviewCommit
name|c
range|:
name|sorted
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getParentCount
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// Since there is a merge commit, sort and prune again using
comment|// MERGE_IF_NECESSARY semantics to avoid creating duplicate
comment|// commits.
comment|//
name|sorted
operator|=
name|args
operator|.
name|mergeUtil
operator|.
name|reduceToMinimalMerge
argument_list|(
name|args
operator|.
name|mergeSorter
argument_list|,
name|sorted
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
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
comment|// TODO(tandrii): Cherry-Pick strategy does this too, but it's wrong
comment|// and can be fixed.
name|ops
operator|.
name|add
argument_list|(
operator|new
name|FastForwardOp
argument_list|(
name|args
argument_list|,
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
name|RebaseRootOp
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
name|RebaseOneOp
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
name|RebaseMultipleParentsOp
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
DECL|class|RebaseRootOp
specifier|private
class|class
name|RebaseRootOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|RebaseRootOp (CodeReviewCommit toMerge)
specifier|private
name|RebaseRootOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|RebaseSubmitStrategy
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
name|CANNOT_REBASE_ROOT
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|RebaseOneOp
specifier|private
class|class
name|RebaseOneOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|field|rebaseOp
specifier|private
name|RebaseChangeOp
name|rebaseOp
decl_stmt|;
DECL|field|newCommit
specifier|private
name|CodeReviewCommit
name|newCommit
decl_stmt|;
DECL|field|newPatchSetId
specifier|private
name|PatchSet
operator|.
name|Id
name|newPatchSetId
decl_stmt|;
DECL|method|RebaseOneOp (CodeReviewCommit toMerge)
specifier|private
name|RebaseOneOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|RebaseSubmitStrategy
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
name|InvalidChangeOperationException
throws|,
name|RestApiException
throws|,
name|IOException
throws|,
name|OrmException
block|{
comment|// TODO(dborowitz): args.rw is needed because it's a CodeReviewRevWalk.
comment|// When hoisting BatchUpdate into MergeOp, we will need to teach
comment|// BatchUpdate how to produce CodeReviewRevWalks.
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
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|toMerge
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|rebaseAlways
condition|)
block|{
name|args
operator|.
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|amendGitlink
argument_list|(
name|toMerge
argument_list|)
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
name|acceptMergeTip
argument_list|(
name|args
operator|.
name|mergeTip
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// RebaseAlways means we modify commit message.
name|args
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|toMerge
argument_list|)
expr_stmt|;
name|newPatchSetId
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
name|RevCommit
name|mergeTip
init|=
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
decl_stmt|;
name|args
operator|.
name|rw
operator|.
name|parseBody
argument_list|(
name|mergeTip
argument_list|)
expr_stmt|;
name|String
name|cherryPickCmtMsg
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|createCommitMessageOnSubmit
argument_list|(
name|toMerge
argument_list|,
name|mergeTip
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
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MergeConflictException
name|mce
parameter_list|)
block|{
comment|// Unlike in Cherry-pick case, this should never happen.
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|REBASE_MERGE_CONFLICT
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"MergeConflictException on message edit must not happen"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|MergeIdenticalTreeException
name|mie
parameter_list|)
block|{
comment|// this should not happen
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|SKIPPED_IDENTICAL_TREE
argument_list|)
expr_stmt|;
return|return;
block|}
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
name|newPatchSetId
operator|.
name|toRefName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Stale read of patch set is ok; see comments in RebaseChangeOp.
name|PatchSet
name|origPs
init|=
name|args
operator|.
name|psUtil
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|toMerge
operator|.
name|getControl
argument_list|()
operator|.
name|getNotes
argument_list|()
argument_list|,
name|toMerge
operator|.
name|getPatchsetId
argument_list|()
argument_list|)
decl_stmt|;
name|rebaseOp
operator|=
name|args
operator|.
name|rebaseFactory
operator|.
name|create
argument_list|(
name|toMerge
operator|.
name|getControl
argument_list|()
argument_list|,
name|origPs
argument_list|,
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|setFireRevisionCreated
argument_list|(
literal|false
argument_list|)
comment|// Bypass approval copier since SubmitStrategyOp copy all approvals
comment|// later anyway.
operator|.
name|setCopyApprovals
argument_list|(
literal|false
argument_list|)
operator|.
name|setValidatePolicy
argument_list|(
name|CommitValidators
operator|.
name|Policy
operator|.
name|NONE
argument_list|)
operator|.
name|setCheckAddPatchSetPermission
argument_list|(
literal|false
argument_list|)
comment|// RebaseAlways should set always modify commit message like
comment|// Cherry-Pick strategy.
operator|.
name|setDetailedCommitMessage
argument_list|(
name|rebaseAlways
argument_list|)
comment|// Do not post message after inserting new patchset because there
comment|// will be one about change being merged already.
operator|.
name|setPostMessage
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|rebaseOp
operator|.
name|updateRepo
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MergeConflictException
decl||
name|NoSuchChangeException
name|e
parameter_list|)
block|{
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|REBASE_MERGE_CONFLICT
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IntegrationException
argument_list|(
literal|"Cannot rebase "
operator|+
name|toMerge
operator|.
name|name
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|newCommit
operator|=
name|args
operator|.
name|rw
operator|.
name|parseCommit
argument_list|(
name|rebaseOp
operator|.
name|getRebasedCommit
argument_list|()
argument_list|)
expr_stmt|;
name|newPatchSetId
operator|=
name|rebaseOp
operator|.
name|getPatchSetId
argument_list|()
expr_stmt|;
block|}
name|newCommit
operator|=
name|amendGitlink
argument_list|(
name|newCommit
argument_list|)
expr_stmt|;
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
name|newPatchSetId
argument_list|)
expr_stmt|;
name|newCommit
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|CLEAN_REBASE
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
name|commitStatus
operator|.
name|put
argument_list|(
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|)
expr_stmt|;
name|acceptMergeTip
argument_list|(
name|args
operator|.
name|mergeTip
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
name|NoSuchChangeException
throws|,
name|ResourceConflictException
throws|,
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
name|newCommit
operator|==
literal|null
condition|)
block|{
name|checkState
argument_list|(
operator|!
name|rebaseAlways
argument_list|,
literal|"RebaseAlways must never fast forward"
argument_list|)
expr_stmt|;
comment|// otherwise, took the fast-forward option, nothing to do.
return|return
literal|null
return|;
block|}
name|PatchSet
name|newPs
decl_stmt|;
if|if
condition|(
name|rebaseOp
operator|!=
literal|null
condition|)
block|{
name|rebaseOp
operator|.
name|updateChange
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|newPs
operator|=
name|rebaseOp
operator|.
name|getPatchSet
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// CherryPick
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
name|newPs
operator|=
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
name|newPatchSetId
argument_list|)
argument_list|,
name|newPatchSetId
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
name|ImmutableList
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|setCurrentPatchSet
argument_list|(
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
name|newPatchSetId
argument_list|)
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|postUpdateImpl (Context ctx)
specifier|public
name|void
name|postUpdateImpl
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|rebaseOp
operator|!=
literal|null
condition|)
block|{
name|rebaseOp
operator|.
name|postUpdate
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|RebaseMultipleParentsOp
specifier|private
class|class
name|RebaseMultipleParentsOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|RebaseMultipleParentsOp (CodeReviewCommit toMerge)
specifier|private
name|RebaseMultipleParentsOp
parameter_list|(
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
name|RebaseSubmitStrategy
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
comment|// There are multiple parents, so this is a merge commit. We don't want
comment|// to rebase the merge as clients can't easily rebase their history with
comment|// that merge present and replaced by an equivalent merge with a different
comment|// first parent. So instead behave as though MERGE_IF_NECESSARY was
comment|// configured.
comment|// TODO(tandrii): this is not in spirit of RebaseAlways strategy because
comment|// the commit messages can not be modified in the process. It's also
comment|// possible to implement rebasing of merge commits. E.g., the Cherry Pick
comment|// REST endpoint already supports cherry-picking of merge commits.
comment|// For now, users of RebaseAlways strategy for whom changed commit footers
comment|// are important would be well advised to prohibit uploading patches with
comment|// merge commits.
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
operator|&&
operator|!
name|args
operator|.
name|submoduleOp
operator|.
name|hasSubscription
argument_list|(
name|args
operator|.
name|destBranch
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
name|CodeReviewCommit
name|newTip
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|mergeOneCommit
argument_list|(
name|args
operator|.
name|serverIdent
argument_list|,
name|args
operator|.
name|serverIdent
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
name|amendGitlink
argument_list|(
name|newTip
argument_list|)
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
name|RevCommit
name|initialTip
init|=
name|mergeTip
operator|.
name|getInitialTip
argument_list|()
decl_stmt|;
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
name|initialTip
operator|==
literal|null
condition|?
name|ImmutableSet
operator|.
expr|<
name|RevCommit
operator|>
name|of
argument_list|()
else|:
name|ImmutableSet
operator|.
name|of
argument_list|(
name|initialTip
argument_list|)
argument_list|)
expr_stmt|;
name|acceptMergeTip
argument_list|(
name|mergeTip
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|acceptMergeTip (MergeTip mergeTip)
specifier|private
name|void
name|acceptMergeTip
parameter_list|(
name|MergeTip
name|mergeTip
parameter_list|)
block|{
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
DECL|method|sort (Collection<CodeReviewCommit> toSort, RevCommit initialTip)
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
parameter_list|,
name|RevCommit
name|initialTip
parameter_list|)
throws|throws
name|IntegrationException
block|{
try|try
block|{
return|return
operator|new
name|RebaseSorter
argument_list|(
name|args
operator|.
name|rw
argument_list|,
name|initialTip
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
name|IntegrationException
argument_list|(
literal|"Commit sorting failed"
argument_list|,
name|e
argument_list|)
throw|;
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
comment|// Test for merge instead of cherry pick to avoid false negatives
comment|// on commit chains.
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
name|canMerge
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
name|toMerge
argument_list|)
return|;
block|}
block|}
end_class

end_unit

