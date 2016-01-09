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
name|MergeTip
import|;
end_import

begin_class
DECL|class|MarkCleanMergesOp
class|class
name|MarkCleanMergesOp
extends|extends
name|BatchUpdate
operator|.
name|Op
block|{
DECL|method|anyChangeId (Iterable<CodeReviewCommit> commits)
specifier|static
name|Change
operator|.
name|Id
name|anyChangeId
parameter_list|(
name|Iterable
argument_list|<
name|CodeReviewCommit
argument_list|>
name|commits
parameter_list|)
block|{
for|for
control|(
name|CodeReviewCommit
name|c
range|:
name|commits
control|)
block|{
if|if
condition|(
name|c
operator|.
name|change
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|c
operator|.
name|change
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no CodeReviewCommits have changes: "
operator|+
name|commits
argument_list|)
throw|;
block|}
DECL|field|args
specifier|private
specifier|final
name|SubmitStrategy
operator|.
name|Arguments
name|args
decl_stmt|;
DECL|field|mergeTip
specifier|private
specifier|final
name|MergeTip
name|mergeTip
decl_stmt|;
DECL|method|MarkCleanMergesOp (SubmitStrategy.Arguments args, MergeTip mergeTip)
name|MarkCleanMergesOp
parameter_list|(
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|,
name|MergeTip
name|mergeTip
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
name|this
operator|.
name|mergeTip
operator|=
name|mergeTip
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postUpdate (Context ctx)
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|IntegrationException
block|{
comment|// TODO(dborowitz): args.rw is needed because it's a CodeReviewRevWalk.
comment|// When hoisting BatchUpdate into MergeOp, we will need to teach
comment|// BatchUpdate how to produce CodeReviewRevWalks.
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
end_class

end_unit

