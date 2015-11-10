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
name|base
operator|.
name|CharMatcher
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
name|api
operator|.
name|changes
operator|.
name|SubmitInput
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
name|server
operator|.
name|change
operator|.
name|Submit
operator|.
name|TestSubmitInput
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
name|MergeOp
operator|.
name|CommitStatus
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|SubmitStrategyListener
specifier|public
class|class
name|SubmitStrategyListener
extends|extends
name|BatchUpdate
operator|.
name|Listener
block|{
DECL|field|strategies
specifier|private
specifier|final
name|Collection
argument_list|<
name|SubmitStrategy
argument_list|>
name|strategies
decl_stmt|;
DECL|field|commits
specifier|private
specifier|final
name|CommitStatus
name|commits
decl_stmt|;
DECL|field|failAfterRefUpdates
specifier|private
specifier|final
name|boolean
name|failAfterRefUpdates
decl_stmt|;
DECL|method|SubmitStrategyListener (SubmitInput input, Collection<SubmitStrategy> strategies, CommitStatus commits)
specifier|public
name|SubmitStrategyListener
parameter_list|(
name|SubmitInput
name|input
parameter_list|,
name|Collection
argument_list|<
name|SubmitStrategy
argument_list|>
name|strategies
parameter_list|,
name|CommitStatus
name|commits
parameter_list|)
block|{
name|this
operator|.
name|strategies
operator|=
name|strategies
expr_stmt|;
name|this
operator|.
name|commits
operator|=
name|commits
expr_stmt|;
if|if
condition|(
name|input
operator|instanceof
name|TestSubmitInput
condition|)
block|{
name|failAfterRefUpdates
operator|=
operator|(
operator|(
name|TestSubmitInput
operator|)
name|input
operator|)
operator|.
name|failAfterRefUpdates
expr_stmt|;
block|}
else|else
block|{
name|failAfterRefUpdates
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|afterUpdateRepos ()
specifier|public
name|void
name|afterUpdateRepos
parameter_list|()
throws|throws
name|ResourceConflictException
block|{
try|try
block|{
name|markCleanMerges
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|alreadyMerged
init|=
name|checkCommitStatus
argument_list|()
decl_stmt|;
name|findUnmergedChanges
argument_list|(
name|alreadyMerged
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IntegrationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|afterRefUpdates ()
specifier|public
name|void
name|afterRefUpdates
parameter_list|()
throws|throws
name|ResourceConflictException
block|{
if|if
condition|(
name|failAfterRefUpdates
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Failing after ref updates"
argument_list|)
throw|;
block|}
for|for
control|(
name|SubmitStrategy
name|strategy
range|:
name|strategies
control|)
block|{
name|SubmitStrategy
operator|.
name|Arguments
name|args
init|=
name|strategy
operator|.
name|args
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|.
name|equals
argument_list|(
name|args
operator|.
name|mergeTip
operator|.
name|getInitialTip
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
block|}
block|}
DECL|method|findUnmergedChanges (List<Change.Id> alreadyMerged)
specifier|private
name|void
name|findUnmergedChanges
parameter_list|(
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|alreadyMerged
parameter_list|)
throws|throws
name|ResourceConflictException
throws|,
name|IntegrationException
block|{
for|for
control|(
name|SubmitStrategy
name|strategy
range|:
name|strategies
control|)
block|{
if|if
condition|(
name|strategy
operator|instanceof
name|CherryPick
condition|)
block|{
comment|// Might have picked a subset of changes, can't do this sanity check.
continue|continue;
block|}
name|SubmitStrategy
operator|.
name|Arguments
name|args
init|=
name|strategy
operator|.
name|args
decl_stmt|;
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|unmerged
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|findUnmergedChanges
argument_list|(
name|args
operator|.
name|commits
operator|.
name|getChangeIds
argument_list|(
name|args
operator|.
name|destBranch
argument_list|)
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|args
operator|.
name|canMergeFlag
argument_list|,
name|args
operator|.
name|mergeTip
operator|.
name|getInitialTip
argument_list|()
argument_list|,
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|alreadyMerged
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Id
name|id
range|:
name|unmerged
control|)
block|{
name|commits
operator|.
name|problem
argument_list|(
name|id
argument_list|,
literal|"internal error: change not reachable from new branch tip"
argument_list|)
expr_stmt|;
block|}
block|}
name|commits
operator|.
name|maybeFailVerbose
argument_list|()
expr_stmt|;
block|}
DECL|method|markCleanMerges ()
specifier|private
name|void
name|markCleanMerges
parameter_list|()
throws|throws
name|IntegrationException
block|{
for|for
control|(
name|SubmitStrategy
name|strategy
range|:
name|strategies
control|)
block|{
name|SubmitStrategy
operator|.
name|Arguments
name|args
init|=
name|strategy
operator|.
name|args
decl_stmt|;
name|RevCommit
name|initialTip
init|=
name|args
operator|.
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
name|args
operator|.
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
block|}
block|}
DECL|method|checkCommitStatus ()
specifier|private
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|checkCommitStatus
parameter_list|()
throws|throws
name|ResourceConflictException
block|{
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|alreadyMerged
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|commits
operator|.
name|getChangeIds
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Id
name|id
range|:
name|commits
operator|.
name|getChangeIds
argument_list|()
control|)
block|{
name|CodeReviewCommit
name|commit
init|=
name|commits
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|CommitMergeStatus
name|s
init|=
name|commit
operator|!=
literal|null
condition|?
name|commit
operator|.
name|getStatusCode
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
name|commits
operator|.
name|problem
argument_list|(
name|id
argument_list|,
literal|"internal error: change not processed by merge strategy"
argument_list|)
expr_stmt|;
continue|continue;
block|}
switch|switch
condition|(
name|s
condition|)
block|{
case|case
name|CLEAN_MERGE
case|:
case|case
name|CLEAN_REBASE
case|:
case|case
name|CLEAN_PICK
case|:
case|case
name|SKIPPED_IDENTICAL_TREE
case|:
break|break;
comment|// Merge strategy accepted this change.
case|case
name|ALREADY_MERGED
case|:
comment|// Already an ancestor of tip.
name|alreadyMerged
operator|.
name|add
argument_list|(
name|commit
operator|.
name|getPatchsetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|PATH_CONFLICT
case|:
case|case
name|REBASE_MERGE_CONFLICT
case|:
case|case
name|MANUAL_RECURSIVE_MERGE
case|:
case|case
name|CANNOT_CHERRY_PICK_ROOT
case|:
case|case
name|CANNOT_REBASE_ROOT
case|:
case|case
name|NOT_FAST_FORWARD
case|:
comment|// TODO(dborowitz): Reformat these messages to be more appropriate for
comment|// short problem descriptions.
name|commits
operator|.
name|problem
argument_list|(
name|id
argument_list|,
name|CharMatcher
operator|.
name|is
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|collapseFrom
argument_list|(
name|s
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|' '
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|MISSING_DEPENDENCY
case|:
name|commits
operator|.
name|problem
argument_list|(
name|id
argument_list|,
literal|"depends on change that was not submitted"
argument_list|)
expr_stmt|;
break|break;
default|default:
name|commits
operator|.
name|problem
argument_list|(
name|id
argument_list|,
literal|"unspecified merge failure: "
operator|+
name|s
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|commits
operator|.
name|maybeFailVerbose
argument_list|()
expr_stmt|;
return|return
name|alreadyMerged
return|;
block|}
annotation|@
name|Override
DECL|method|afterUpdateChanges ()
specifier|public
name|void
name|afterUpdateChanges
parameter_list|()
throws|throws
name|ResourceConflictException
block|{
name|commits
operator|.
name|maybeFail
argument_list|(
literal|"Error updating status"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

