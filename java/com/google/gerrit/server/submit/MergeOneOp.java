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
DECL|package|com.google.gerrit.server.submit
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|submit
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
name|submit
operator|.
name|CommitMergeStatus
operator|.
name|EMPTY_COMMIT
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
name|entities
operator|.
name|BooleanProjectConfig
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
name|update
operator|.
name|RepoContext
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

begin_class
DECL|class|MergeOneOp
class|class
name|MergeOneOp
extends|extends
name|SubmitStrategyOp
block|{
DECL|method|MergeOneOp (SubmitStrategy.Arguments args, CodeReviewCommit toMerge)
name|MergeOneOp
parameter_list|(
name|SubmitStrategy
operator|.
name|Arguments
name|args
parameter_list|,
name|CodeReviewCommit
name|toMerge
parameter_list|)
block|{
name|super
argument_list|(
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
name|PersonIdent
name|caller
init|=
name|ctx
operator|.
name|getIdentifiedUser
argument_list|()
operator|.
name|newCommitterIdent
argument_list|(
name|args
operator|.
name|serverIdent
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
if|if
condition|(
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"cannot merge commit "
operator|+
name|toMerge
operator|.
name|name
argument_list|()
operator|+
literal|" onto a null tip; expected at least one fast-forward prior to"
operator|+
literal|" this operation"
argument_list|)
throw|;
block|}
name|CodeReviewCommit
name|merged
init|=
name|args
operator|.
name|mergeUtil
operator|.
name|mergeOneCommit
argument_list|(
name|caller
argument_list|,
name|args
operator|.
name|serverIdent
argument_list|,
name|args
operator|.
name|rw
argument_list|,
name|ctx
operator|.
name|getInserter
argument_list|()
argument_list|,
name|ctx
operator|.
name|getRepoView
argument_list|()
operator|.
name|getConfig
argument_list|()
argument_list|,
name|args
operator|.
name|destBranch
argument_list|,
name|args
operator|.
name|mergeTip
operator|.
name|getCurrentTip
argument_list|()
argument_list|,
name|toMerge
argument_list|)
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|project
operator|.
name|is
argument_list|(
name|BooleanProjectConfig
operator|.
name|REJECT_EMPTY_COMMIT
argument_list|)
operator|&&
name|merged
operator|.
name|getTree
argument_list|()
operator|.
name|equals
argument_list|(
name|merged
operator|.
name|getParent
argument_list|(
literal|0
argument_list|)
operator|.
name|getTree
argument_list|()
argument_list|)
condition|)
block|{
name|toMerge
operator|.
name|setStatusCode
argument_list|(
name|EMPTY_COMMIT
argument_list|)
expr_stmt|;
return|return;
block|}
name|args
operator|.
name|mergeTip
operator|.
name|moveTipTo
argument_list|(
name|amendGitlink
argument_list|(
name|merged
argument_list|)
argument_list|,
name|toMerge
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

