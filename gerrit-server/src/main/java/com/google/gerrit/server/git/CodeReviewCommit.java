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
name|server
operator|.
name|notedb
operator|.
name|ChangeNotes
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
name|ChangeControl
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
name|AnyObjectId
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
name|Repository
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
name|revwalk
operator|.
name|RevWalk
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
specifier|public
class|class
name|CodeReviewCommit
extends|extends
name|RevCommit
block|{
DECL|method|newRevWalk (Repository repo)
specifier|public
specifier|static
name|RevWalk
name|newRevWalk
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
return|return
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|RevCommit
name|createCommit
parameter_list|(
name|AnyObjectId
name|id
parameter_list|)
block|{
return|return
operator|new
name|CodeReviewCommit
argument_list|(
name|id
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|error (final CommitMergeStatus s)
specifier|static
name|CodeReviewCommit
name|error
parameter_list|(
specifier|final
name|CommitMergeStatus
name|s
parameter_list|)
block|{
specifier|final
name|CodeReviewCommit
name|r
init|=
operator|new
name|CodeReviewCommit
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|statusCode
operator|=
name|s
expr_stmt|;
return|return
name|r
return|;
block|}
comment|/**    * Unique key of the PatchSet entity from the code review system.    *<p>    * This value is only available on commits that have a PatchSet represented in    * the code review system.    */
DECL|field|patchsetId
specifier|private
name|PatchSet
operator|.
name|Id
name|patchsetId
decl_stmt|;
comment|/** Change control for the change owner. */
DECL|field|control
specifier|private
name|ChangeControl
name|control
decl_stmt|;
comment|/**    * Ordinal position of this commit within the submit queue.    *<p>    * Only valid if {@link #patchsetId} is not null.    */
DECL|field|originalOrder
name|int
name|originalOrder
decl_stmt|;
comment|/**    * The result status for this commit.    *<p>    * Only valid if {@link #patchsetId} is not null.    */
DECL|field|statusCode
specifier|private
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
specifier|public
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
DECL|method|notes ()
specifier|public
name|ChangeNotes
name|notes
parameter_list|()
block|{
return|return
name|getControl
argument_list|()
operator|.
name|getNotes
argument_list|()
return|;
block|}
DECL|method|getStatusCode ()
specifier|public
name|CommitMergeStatus
name|getStatusCode
parameter_list|()
block|{
return|return
name|statusCode
return|;
block|}
DECL|method|setStatusCode (CommitMergeStatus statusCode)
specifier|public
name|void
name|setStatusCode
parameter_list|(
name|CommitMergeStatus
name|statusCode
parameter_list|)
block|{
name|this
operator|.
name|statusCode
operator|=
name|statusCode
expr_stmt|;
block|}
DECL|method|getPatchsetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getPatchsetId
parameter_list|()
block|{
return|return
name|patchsetId
return|;
block|}
DECL|method|setPatchsetId (PatchSet.Id patchsetId)
specifier|public
name|void
name|setPatchsetId
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchsetId
parameter_list|)
block|{
name|this
operator|.
name|patchsetId
operator|=
name|patchsetId
expr_stmt|;
block|}
DECL|method|copyFrom (final CodeReviewCommit src)
specifier|public
name|void
name|copyFrom
parameter_list|(
specifier|final
name|CodeReviewCommit
name|src
parameter_list|)
block|{
name|control
operator|=
name|src
operator|.
name|control
expr_stmt|;
name|patchsetId
operator|=
name|src
operator|.
name|patchsetId
expr_stmt|;
name|originalOrder
operator|=
name|src
operator|.
name|originalOrder
expr_stmt|;
name|statusCode
operator|=
name|src
operator|.
name|statusCode
expr_stmt|;
name|missing
operator|=
name|src
operator|.
name|missing
expr_stmt|;
block|}
DECL|method|change ()
specifier|public
name|Change
name|change
parameter_list|()
block|{
return|return
name|getControl
argument_list|()
operator|.
name|getChange
argument_list|()
return|;
block|}
DECL|method|getControl ()
specifier|public
name|ChangeControl
name|getControl
parameter_list|()
block|{
return|return
name|control
return|;
block|}
DECL|method|setControl (ChangeControl control)
specifier|public
name|void
name|setControl
parameter_list|(
name|ChangeControl
name|control
parameter_list|)
block|{
name|this
operator|.
name|control
operator|=
name|control
expr_stmt|;
block|}
block|}
end_class

end_unit

