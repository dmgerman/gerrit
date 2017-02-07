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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|HashMap
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

begin_comment
comment|/**  * Class describing a merge tip during merge operation.  *  *<p>The current tip of a {@link MergeTip} may be null if the merge operation is against an unborn  * branch, and has not yet been attempted. This is distinct from a null {@link MergeTip} instance,  * which may be used to indicate that a merge failed or another error state.  */
end_comment

begin_class
DECL|class|MergeTip
specifier|public
class|class
name|MergeTip
block|{
DECL|field|initialTip
specifier|private
name|CodeReviewCommit
name|initialTip
decl_stmt|;
DECL|field|branchTip
specifier|private
name|CodeReviewCommit
name|branchTip
decl_stmt|;
DECL|field|mergeResults
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|ObjectId
argument_list|>
name|mergeResults
decl_stmt|;
comment|/**    * @param initialTip tip before the merge operation; may be null, indicating an unborn branch.    * @param toMerge list of commits to be merged in merge operation; may not be null or empty.    */
DECL|method|MergeTip (@ullable CodeReviewCommit initialTip, Collection<CodeReviewCommit> toMerge)
specifier|public
name|MergeTip
parameter_list|(
annotation|@
name|Nullable
name|CodeReviewCommit
name|initialTip
parameter_list|,
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|toMerge
argument_list|,
literal|"toMerge may not be null"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|toMerge
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"toMerge may not be empty"
argument_list|)
expr_stmt|;
name|this
operator|.
name|initialTip
operator|=
name|initialTip
expr_stmt|;
name|this
operator|.
name|branchTip
operator|=
name|initialTip
expr_stmt|;
name|this
operator|.
name|mergeResults
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
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
name|copy
argument_list|()
argument_list|,
name|commit
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * @return the initial tip of the branch before the merge operation started; may be null,    *     indicating a previously unborn branch.    */
DECL|method|getInitialTip ()
specifier|public
name|CodeReviewCommit
name|getInitialTip
parameter_list|()
block|{
return|return
name|initialTip
return|;
block|}
comment|/**    * Moves this MergeTip to newTip and appends mergeResult.    *    * @param newTip The new tip; may not be null.    * @param mergedFrom The result of the merge of {@code newTip}.    */
DECL|method|moveTipTo (CodeReviewCommit newTip, ObjectId mergedFrom)
specifier|public
name|void
name|moveTipTo
parameter_list|(
name|CodeReviewCommit
name|newTip
parameter_list|,
name|ObjectId
name|mergedFrom
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|newTip
operator|!=
literal|null
argument_list|)
expr_stmt|;
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
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * The merge results of all the merges of this merge operation.    *    * @return The merge results of the merge operation as a map of SHA-1 to be merged to SHA-1 of the    *     merge result.    */
DECL|method|getMergeResults ()
specifier|public
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|ObjectId
argument_list|>
name|getMergeResults
parameter_list|()
block|{
return|return
name|mergeResults
return|;
block|}
comment|/**    * @return The current tip of the current merge operation; may be null, indicating an unborn    *     branch.    */
annotation|@
name|Nullable
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

