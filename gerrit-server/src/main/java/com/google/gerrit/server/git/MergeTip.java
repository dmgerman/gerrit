begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
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
name|Maps
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
name|Map
import|;
end_import

begin_comment
comment|/**  * Class describing a merge tip during merge operation.  */
end_comment

begin_class
DECL|class|MergeTip
specifier|public
class|class
name|MergeTip
block|{
DECL|field|branchTip
specifier|private
name|CodeReviewCommit
name|branchTip
decl_stmt|;
DECL|field|mergeResults
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|mergeResults
decl_stmt|;
comment|/**    * @param initial Tip before the merge operation.    * @param toMerge List of CodeReview commits to be merged in merge operation.    */
DECL|method|MergeTip (CodeReviewCommit initial, Collection<CodeReviewCommit> toMerge)
specifier|public
name|MergeTip
parameter_list|(
name|CodeReviewCommit
name|initial
parameter_list|,
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
block|{
name|this
operator|.
name|mergeResults
operator|=
name|Maps
operator|.
name|newHashMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|branchTip
operator|=
name|initial
expr_stmt|;
comment|// Assume fast-forward merge until opposite is proven.
for|for
control|(
name|CodeReviewCommit
name|commit
range|:
name|toMerge
control|)
block|{
name|mergeResults
operator|.
name|put
argument_list|(
name|commit
operator|.
name|getName
argument_list|()
argument_list|,
name|commit
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Moves this MergeTip to newTip and appends mergeResult.    *    * @param newTip The new tip    * @param mergedFrom The result of the merge of newTip    */
DECL|method|moveTipTo (CodeReviewCommit newTip, String mergedFrom)
specifier|public
name|void
name|moveTipTo
parameter_list|(
name|CodeReviewCommit
name|newTip
parameter_list|,
name|String
name|mergedFrom
parameter_list|)
block|{
name|branchTip
operator|=
name|newTip
expr_stmt|;
name|mergeResults
operator|.
name|put
argument_list|(
name|mergedFrom
argument_list|,
name|newTip
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * The merge results of all the merges of this merge operation.    *    * @return The merge results of the merge operation Map<<sha1 of the commit to    *         be merged>,<sha1 of the merge result>>    */
DECL|method|getMergeResults ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMergeResults
parameter_list|()
block|{
return|return
name|this
operator|.
name|mergeResults
return|;
block|}
comment|/**    *    * @return The current tip of the current merge operation.    */
DECL|method|getCurrentTip ()
specifier|public
name|CodeReviewCommit
name|getCurrentTip
parameter_list|()
block|{
return|return
name|branchTip
return|;
block|}
block|}
end_class

end_unit

