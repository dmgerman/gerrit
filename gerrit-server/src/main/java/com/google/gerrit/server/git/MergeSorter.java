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
name|server
operator|.
name|git
operator|.
name|CodeReviewCommit
operator|.
name|CodeReviewRevWalk
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
name|strategy
operator|.
name|CommitMergeStatus
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|RevCommitList
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
name|RevFlag
import|;
end_import

begin_class
DECL|class|MergeSorter
specifier|public
class|class
name|MergeSorter
block|{
DECL|field|rw
specifier|private
specifier|final
name|CodeReviewRevWalk
name|rw
decl_stmt|;
DECL|field|canMergeFlag
specifier|private
specifier|final
name|RevFlag
name|canMergeFlag
decl_stmt|;
DECL|field|accepted
specifier|private
specifier|final
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|accepted
decl_stmt|;
DECL|field|incoming
specifier|private
specifier|final
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|incoming
decl_stmt|;
DECL|method|MergeSorter ( CodeReviewRevWalk rw, Set<RevCommit> alreadyAccepted, RevFlag canMergeFlag, Set<CodeReviewCommit> incoming)
specifier|public
name|MergeSorter
parameter_list|(
name|CodeReviewRevWalk
name|rw
parameter_list|,
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|alreadyAccepted
parameter_list|,
name|RevFlag
name|canMergeFlag
parameter_list|,
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|incoming
parameter_list|)
block|{
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
name|this
operator|.
name|canMergeFlag
operator|=
name|canMergeFlag
expr_stmt|;
name|this
operator|.
name|accepted
operator|=
name|alreadyAccepted
expr_stmt|;
name|this
operator|.
name|incoming
operator|=
name|incoming
expr_stmt|;
block|}
DECL|method|sort (final Collection<CodeReviewCommit> toMerge)
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sort
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|CodeReviewCommit
argument_list|>
name|toMerge
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|heads
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|CodeReviewCommit
argument_list|>
name|sort
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|toMerge
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|sort
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|CodeReviewCommit
name|n
init|=
name|removeOne
argument_list|(
name|sort
argument_list|)
decl_stmt|;
name|rw
operator|.
name|resetRetain
argument_list|(
name|canMergeFlag
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|n
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|accepted
control|)
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|CodeReviewCommit
name|c
decl_stmt|;
name|RevCommitList
argument_list|<
name|RevCommit
argument_list|>
name|contents
init|=
operator|new
name|RevCommitList
argument_list|<>
argument_list|()
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|c
operator|.
name|has
argument_list|(
name|canMergeFlag
argument_list|)
operator|||
operator|!
name|incoming
operator|.
name|contains
argument_list|(
name|c
argument_list|)
condition|)
block|{
comment|// We cannot merge n as it would bring something we
comment|// aren't permitted to merge at this time. Drop n.
comment|//
name|n
operator|.
name|setStatusCode
argument_list|(
name|CommitMergeStatus
operator|.
name|MISSING_DEPENDENCY
argument_list|)
expr_stmt|;
break|break;
block|}
name|contents
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|n
operator|.
name|getStatusCode
argument_list|()
operator|==
name|CommitMergeStatus
operator|.
name|MISSING_DEPENDENCY
condition|)
block|{
continue|continue;
block|}
comment|// Anything reachable through us is better merged by just
comment|// merging us directly. So prune our ancestors out and let
comment|// us merge instead.
comment|//
name|sort
operator|.
name|removeAll
argument_list|(
name|contents
argument_list|)
expr_stmt|;
name|heads
operator|.
name|removeAll
argument_list|(
name|contents
argument_list|)
expr_stmt|;
name|heads
operator|.
name|add
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
return|return
name|heads
return|;
block|}
DECL|method|removeOne (Collection<T> c)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|removeOne
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|c
parameter_list|)
block|{
specifier|final
name|Iterator
argument_list|<
name|T
argument_list|>
name|i
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|final
name|T
name|r
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

