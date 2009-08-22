begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|AnyObjectId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|spearce
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
name|List
import|;
end_import

begin_comment
comment|/** Extended commit entity with code review specific metadata. */
end_comment

begin_class
DECL|class|CodeReviewCommit
class|class
name|CodeReviewCommit
extends|extends
name|RevCommit
block|{
comment|/**    * Unique key of the PatchSet entity from the code review system.    *<p>    * This value is only available on commits that have a PatchSet represented in    * the code review system and whose PatchSet is in the current submit queue.    * Merge commits created during the merge or commits that aren't in the submit    * queue will keep this member null.    */
DECL|field|patchsetId
name|PatchSet
operator|.
name|Id
name|patchsetId
decl_stmt|;
comment|/** Unique key of the change itself. */
DECL|field|changeKey
name|Change
operator|.
name|Key
name|changeKey
decl_stmt|;
comment|/**    * Ordinal position of this commit within the submit queue.    *<p>    * Only valid if {@link #patchsetId} is not null.    */
DECL|field|originalOrder
name|int
name|originalOrder
decl_stmt|;
comment|/**    * The result status for this commit.    *<p>    * Only valid if {@link #patchsetId} is not null.    */
DECL|field|statusCode
name|CommitMergeStatus
name|statusCode
decl_stmt|;
comment|/** Commits which are missing ancestors of this commit. */
DECL|field|missing
name|List
argument_list|<
name|CodeReviewCommit
argument_list|>
name|missing
decl_stmt|;
DECL|method|CodeReviewCommit (final AnyObjectId id)
name|CodeReviewCommit
parameter_list|(
specifier|final
name|AnyObjectId
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

