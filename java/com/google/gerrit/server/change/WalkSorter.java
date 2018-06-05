begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Iterables
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
name|ListMultimap
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
name|MultimapBuilder
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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|GitRepositoryManager
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|ArrayDeque
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|List
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
name|RevFlag
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

begin_comment
comment|/**  * Helper to sort {@link ChangeData}s based on {@link RevWalk} ordering.  *  *<p>Split changes by project, and map each change to a single commit based on the latest patch  * set. The set of patch sets considered may be limited by calling {@link  * #includePatchSets(Iterable)}. Perform a standard {@link RevWalk} on each project repository, do  * an approximate topo sort, and record the order in which each change's commit is seen.  *  *<p>Once an order within each project is determined, groups of changes are sorted based on the  * project name. This is slightly more stable than sorting on something like the commit or change  * timestamp, as it will not unexpectedly reorder large groups of changes on subsequent calls if one  * of the changes was updated.  */
end_comment

begin_class
DECL|class|WalkSorter
specifier|public
class|class
name|WalkSorter
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|PROJECT_LIST_SORTER
specifier|private
specifier|static
specifier|final
name|Ordering
argument_list|<
name|List
argument_list|<
name|PatchSetData
argument_list|>
argument_list|>
name|PROJECT_LIST_SORTER
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|nullsFirst
argument_list|()
operator|.
name|onResultOf
argument_list|(
parameter_list|(
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|in
parameter_list|)
lambda|->
block|{
if|if
condition|(
name|in
operator|==
literal|null
operator|||
name|in
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|in
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|data
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
argument_list|)
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|includePatchSets
specifier|private
specifier|final
name|Set
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|includePatchSets
decl_stmt|;
DECL|field|retainBody
specifier|private
name|boolean
name|retainBody
decl_stmt|;
annotation|@
name|Inject
DECL|method|WalkSorter (GitRepositoryManager repoManager)
name|WalkSorter
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|includePatchSets
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|includePatchSets (Iterable<PatchSet.Id> patchSets)
specifier|public
name|WalkSorter
name|includePatchSets
parameter_list|(
name|Iterable
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|patchSets
parameter_list|)
block|{
name|Iterables
operator|.
name|addAll
argument_list|(
name|includePatchSets
argument_list|,
name|patchSets
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setRetainBody (boolean retainBody)
specifier|public
name|WalkSorter
name|setRetainBody
parameter_list|(
name|boolean
name|retainBody
parameter_list|)
block|{
name|this
operator|.
name|retainBody
operator|=
name|retainBody
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|sort (Iterable<ChangeData> in)
specifier|public
name|Iterable
argument_list|<
name|PatchSetData
argument_list|>
name|sort
parameter_list|(
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ChangeData
argument_list|>
name|byProject
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|in
control|)
block|{
name|byProject
operator|.
name|put
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|cd
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|List
argument_list|<
name|PatchSetData
argument_list|>
argument_list|>
name|sortedByProject
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|byProject
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Collection
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|e
range|:
name|byProject
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|sortedByProject
operator|.
name|add
argument_list|(
name|sortProject
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|sortedByProject
argument_list|,
name|PROJECT_LIST_SORTER
argument_list|)
expr_stmt|;
return|return
name|Iterables
operator|.
name|concat
argument_list|(
name|sortedByProject
argument_list|)
return|;
block|}
DECL|method|sortProject (Project.NameKey project, Collection<ChangeData> in)
specifier|private
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|sortProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Collection
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|rw
operator|.
name|setRetainBody
argument_list|(
name|retainBody
argument_list|)
expr_stmt|;
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|PatchSetData
argument_list|>
name|byCommit
init|=
name|byCommit
argument_list|(
name|rw
argument_list|,
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|byCommit
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|byCommit
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|(
name|byCommit
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|)
return|;
block|}
comment|// Walk from all patch set SHA-1s, and terminate as soon as we've found
comment|// everything we're looking for. This is equivalent to just sorting the
comment|// list of commits by the RevWalk's configured order.
comment|//
comment|// Partially topo sort the list, ensuring no parent is emitted before a
comment|// direct child that is also in the input set. This preserves the stable,
comment|// expected sort in the case where many commits share the same timestamp,
comment|// e.g. a quick rebase. It also avoids JGit's topo sort, which slurps all
comment|// interesting commits at the beginning, which is a problem since we don't
comment|// know which commits to mark as uninteresting. Finding a reasonable set
comment|// of commits to mark uninteresting (the "rootmost" set) is at least as
comment|// difficult as just implementing this partial topo sort ourselves.
comment|//
comment|// (This is slightly less efficient than JGit's topo sort, which uses a
comment|// private in-degree field in RevCommit rather than multimaps. We assume
comment|// the input size is small enough that this is not an issue.)
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|commits
init|=
name|byCommit
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|RevCommit
argument_list|>
name|children
init|=
name|collectChildren
argument_list|(
name|commits
argument_list|)
decl_stmt|;
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|RevCommit
argument_list|>
name|pending
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|RevCommit
argument_list|>
name|todo
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|RevFlag
name|done
init|=
name|rw
operator|.
name|newFlag
argument_list|(
literal|"done"
argument_list|)
decl_stmt|;
name|markStart
argument_list|(
name|rw
argument_list|,
name|commits
argument_list|)
expr_stmt|;
name|int
name|expected
init|=
name|commits
operator|.
name|size
argument_list|()
decl_stmt|;
name|int
name|found
init|=
literal|0
decl_stmt|;
name|RevCommit
name|c
decl_stmt|;
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|expected
argument_list|)
decl_stmt|;
while|while
condition|(
name|found
operator|<
name|expected
operator|&&
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
name|commits
operator|.
name|contains
argument_list|(
name|c
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|todo
operator|.
name|clear
argument_list|()
expr_stmt|;
name|todo
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|todo
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Sanity check: we can't pop more than N pending commits, otherwise
comment|// we have an infinite loop due to programmer error or something.
name|checkState
argument_list|(
operator|++
name|i
operator|<=
name|commits
operator|.
name|size
argument_list|()
argument_list|,
literal|"Too many pending steps while sorting %s"
argument_list|,
name|commits
argument_list|)
expr_stmt|;
name|RevCommit
name|t
init|=
name|todo
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|has
argument_list|(
name|done
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|boolean
name|ready
init|=
literal|true
decl_stmt|;
for|for
control|(
name|RevCommit
name|child
range|:
name|children
operator|.
name|get
argument_list|(
name|t
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|child
operator|.
name|has
argument_list|(
name|done
argument_list|)
condition|)
block|{
name|pending
operator|.
name|put
argument_list|(
name|child
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|ready
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ready
condition|)
block|{
name|found
operator|+=
name|emit
argument_list|(
name|t
argument_list|,
name|byCommit
argument_list|,
name|result
argument_list|,
name|done
argument_list|)
expr_stmt|;
name|todo
operator|.
name|addAll
argument_list|(
name|pending
operator|.
name|get
argument_list|(
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
block|}
DECL|method|collectChildren (Set<RevCommit> commits)
specifier|private
specifier|static
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|RevCommit
argument_list|>
name|collectChildren
parameter_list|(
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|commits
parameter_list|)
block|{
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|RevCommit
argument_list|>
name|children
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|RevCommit
name|c
range|:
name|commits
control|)
block|{
for|for
control|(
name|RevCommit
name|p
range|:
name|c
operator|.
name|getParents
argument_list|()
control|)
block|{
if|if
condition|(
name|commits
operator|.
name|contains
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|children
operator|.
name|put
argument_list|(
name|p
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|children
return|;
block|}
DECL|method|emit ( RevCommit c, ListMultimap<RevCommit, PatchSetData> byCommit, List<PatchSetData> result, RevFlag done)
specifier|private
specifier|static
name|int
name|emit
parameter_list|(
name|RevCommit
name|c
parameter_list|,
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|PatchSetData
argument_list|>
name|byCommit
parameter_list|,
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|result
parameter_list|,
name|RevFlag
name|done
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|has
argument_list|(
name|done
argument_list|)
condition|)
block|{
return|return
literal|0
return|;
block|}
name|c
operator|.
name|add
argument_list|(
name|done
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|PatchSetData
argument_list|>
name|psds
init|=
name|byCommit
operator|.
name|get
argument_list|(
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|psds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|result
operator|.
name|addAll
argument_list|(
name|psds
argument_list|)
expr_stmt|;
return|return
literal|1
return|;
block|}
return|return
literal|0
return|;
block|}
DECL|method|byCommit (RevWalk rw, Collection<ChangeData> in)
specifier|private
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|PatchSetData
argument_list|>
name|byCommit
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|Collection
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|ListMultimap
argument_list|<
name|RevCommit
argument_list|,
name|PatchSetData
argument_list|>
name|byCommit
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|(
name|in
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|arrayListValues
argument_list|(
literal|1
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|in
control|)
block|{
name|PatchSet
name|maxPs
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PatchSet
name|ps
range|:
name|cd
operator|.
name|patchSets
argument_list|()
control|)
block|{
if|if
condition|(
name|shouldInclude
argument_list|(
name|ps
argument_list|)
operator|&&
operator|(
name|maxPs
operator|==
literal|null
operator|||
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|>
name|maxPs
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
operator|)
condition|)
block|{
name|maxPs
operator|=
name|ps
expr_stmt|;
block|}
block|}
if|if
condition|(
name|maxPs
operator|==
literal|null
condition|)
block|{
continue|continue;
comment|// No patch sets matched.
block|}
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|maxPs
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|byCommit
operator|.
name|put
argument_list|(
name|c
argument_list|,
name|PatchSetData
operator|.
name|create
argument_list|(
name|cd
argument_list|,
name|maxPs
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
decl||
name|IncorrectObjectTypeException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"missing commit %s for patch set %s"
argument_list|,
name|id
operator|.
name|name
argument_list|()
argument_list|,
name|maxPs
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|byCommit
return|;
block|}
DECL|method|shouldInclude (PatchSet ps)
specifier|private
name|boolean
name|shouldInclude
parameter_list|(
name|PatchSet
name|ps
parameter_list|)
block|{
return|return
name|includePatchSets
operator|.
name|isEmpty
argument_list|()
operator|||
name|includePatchSets
operator|.
name|contains
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|markStart (RevWalk rw, Iterable<RevCommit> commits)
specifier|private
specifier|static
name|void
name|markStart
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|Iterable
argument_list|<
name|RevCommit
argument_list|>
name|commits
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|RevCommit
name|c
range|:
name|commits
control|)
block|{
name|rw
operator|.
name|markStart
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AutoValue
DECL|class|PatchSetData
specifier|public
specifier|abstract
specifier|static
class|class
name|PatchSetData
block|{
annotation|@
name|VisibleForTesting
DECL|method|create (ChangeData cd, PatchSet ps, RevCommit commit)
specifier|static
name|PatchSetData
name|create
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|PatchSet
name|ps
parameter_list|,
name|RevCommit
name|commit
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_WalkSorter_PatchSetData
argument_list|(
name|cd
argument_list|,
name|ps
argument_list|,
name|commit
argument_list|)
return|;
block|}
DECL|method|data ()
specifier|public
specifier|abstract
name|ChangeData
name|data
parameter_list|()
function_decl|;
DECL|method|patchSet ()
specifier|abstract
name|PatchSet
name|patchSet
parameter_list|()
function_decl|;
DECL|method|commit ()
specifier|abstract
name|RevCommit
name|commit
parameter_list|()
function_decl|;
block|}
block|}
end_class

end_unit

