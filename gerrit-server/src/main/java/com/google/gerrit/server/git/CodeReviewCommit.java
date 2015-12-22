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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Function
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
name|Ordering
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
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|errors
operator|.
name|MissingObjectException
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
name|ObjectReader
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
comment|/**    * Default ordering when merging multiple topologically-equivalent commits.    *<p>    * Operates only on these commits and does not take ancestry into account.    *<p>    * Use this in preference to the default order, which comes from {@link    * AnyObjectId} and only orders on SHA-1.    */
DECL|field|ORDER
specifier|public
specifier|static
specifier|final
name|Ordering
argument_list|<
name|CodeReviewCommit
argument_list|>
name|ORDER
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|onResultOf
argument_list|(
operator|new
name|Function
argument_list|<
name|CodeReviewCommit
argument_list|,
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
name|apply
parameter_list|(
name|CodeReviewCommit
name|in
parameter_list|)
block|{
return|return
name|in
operator|.
name|getPatchsetId
argument_list|()
operator|!=
literal|null
condition|?
name|in
operator|.
name|getPatchsetId
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
block|}
argument_list|)
operator|.
name|nullsFirst
argument_list|()
decl_stmt|;
DECL|method|newRevWalk (Repository repo)
specifier|public
specifier|static
name|CodeReviewRevWalk
name|newRevWalk
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
return|return
operator|new
name|CodeReviewRevWalk
argument_list|(
name|repo
argument_list|)
return|;
block|}
DECL|method|newRevWalk (ObjectReader reader)
specifier|public
specifier|static
name|CodeReviewRevWalk
name|newRevWalk
parameter_list|(
name|ObjectReader
name|reader
parameter_list|)
block|{
return|return
operator|new
name|CodeReviewRevWalk
argument_list|(
name|reader
argument_list|)
return|;
block|}
DECL|class|CodeReviewRevWalk
specifier|public
specifier|static
class|class
name|CodeReviewRevWalk
extends|extends
name|RevWalk
block|{
DECL|method|CodeReviewRevWalk (Repository repo)
specifier|private
name|CodeReviewRevWalk
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
name|super
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
DECL|method|CodeReviewRevWalk (ObjectReader reader)
specifier|private
name|CodeReviewRevWalk
parameter_list|(
name|ObjectReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCommit (AnyObjectId id)
specifier|protected
name|CodeReviewCommit
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
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|CodeReviewCommit
name|next
parameter_list|()
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
return|return
operator|(
name|CodeReviewCommit
operator|)
name|super
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|markStart (RevCommit c)
specifier|public
name|void
name|markStart
parameter_list|(
name|RevCommit
name|c
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
name|checkArgument
argument_list|(
name|c
operator|instanceof
name|CodeReviewCommit
argument_list|)
expr_stmt|;
name|super
operator|.
name|markStart
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|markUninteresting (final RevCommit c)
specifier|public
name|void
name|markUninteresting
parameter_list|(
specifier|final
name|RevCommit
name|c
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
name|checkArgument
argument_list|(
name|c
operator|instanceof
name|CodeReviewCommit
argument_list|)
expr_stmt|;
name|super
operator|.
name|markUninteresting
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|lookupCommit (AnyObjectId id)
specifier|public
name|CodeReviewCommit
name|lookupCommit
parameter_list|(
name|AnyObjectId
name|id
parameter_list|)
block|{
return|return
operator|(
name|CodeReviewCommit
operator|)
name|super
operator|.
name|lookupCommit
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parseCommit (AnyObjectId id)
specifier|public
name|CodeReviewCommit
name|parseCommit
parameter_list|(
name|AnyObjectId
name|id
parameter_list|)
throws|throws
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|IOException
block|{
return|return
operator|(
name|CodeReviewCommit
operator|)
name|super
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
return|;
block|}
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

