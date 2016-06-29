begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|LinkedListMultimap
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
name|Lists
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
name|Multimap
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
name|Constants
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
name|Ref
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
name|RefDatabase
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|LinkedList
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

begin_comment
comment|/**  * Resolve in which tags and branches a commit is included.  */
end_comment

begin_class
DECL|class|IncludedInResolver
specifier|public
class|class
name|IncludedInResolver
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IncludedInResolver
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|resolve (final Repository repo, final RevWalk rw, final RevCommit commit)
specifier|public
specifier|static
name|Result
name|resolve
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|RevWalk
name|rw
parameter_list|,
specifier|final
name|RevCommit
name|commit
parameter_list|)
throws|throws
name|IOException
block|{
name|RevFlag
name|flag
init|=
name|newFlag
argument_list|(
name|rw
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|IncludedInResolver
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|commit
argument_list|,
name|flag
argument_list|)
operator|.
name|resolve
argument_list|()
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|disposeFlag
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|includedInOne (final Repository repo, final RevWalk rw, final RevCommit commit, final Collection<Ref> refs)
specifier|public
specifier|static
name|boolean
name|includedInOne
parameter_list|(
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|RevWalk
name|rw
parameter_list|,
specifier|final
name|RevCommit
name|commit
parameter_list|,
specifier|final
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
name|RevFlag
name|flag
init|=
name|newFlag
argument_list|(
name|rw
argument_list|)
decl_stmt|;
try|try
block|{
return|return
operator|new
name|IncludedInResolver
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|commit
argument_list|,
name|flag
argument_list|)
operator|.
name|includedInOne
argument_list|(
name|refs
argument_list|)
return|;
block|}
finally|finally
block|{
name|rw
operator|.
name|disposeFlag
argument_list|(
name|flag
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|newFlag (RevWalk rw)
specifier|private
specifier|static
name|RevFlag
name|newFlag
parameter_list|(
name|RevWalk
name|rw
parameter_list|)
block|{
return|return
name|rw
operator|.
name|newFlag
argument_list|(
literal|"CONTAINS_TARGET"
argument_list|)
return|;
block|}
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|rw
specifier|private
specifier|final
name|RevWalk
name|rw
decl_stmt|;
DECL|field|target
specifier|private
specifier|final
name|RevCommit
name|target
decl_stmt|;
DECL|field|containsTarget
specifier|private
specifier|final
name|RevFlag
name|containsTarget
decl_stmt|;
DECL|field|commitToRef
specifier|private
name|Multimap
argument_list|<
name|RevCommit
argument_list|,
name|String
argument_list|>
name|commitToRef
decl_stmt|;
DECL|field|tipsByCommitTime
specifier|private
name|List
argument_list|<
name|RevCommit
argument_list|>
name|tipsByCommitTime
decl_stmt|;
DECL|method|IncludedInResolver (Repository repo, RevWalk rw, RevCommit target, RevFlag containsTarget)
specifier|private
name|IncludedInResolver
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevCommit
name|target
parameter_list|,
name|RevFlag
name|containsTarget
parameter_list|)
block|{
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|rw
operator|=
name|rw
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|containsTarget
operator|=
name|containsTarget
expr_stmt|;
block|}
DECL|method|resolve ()
specifier|private
name|Result
name|resolve
parameter_list|()
throws|throws
name|IOException
block|{
name|RefDatabase
name|refDb
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Ref
argument_list|>
name|tags
init|=
name|refDb
operator|.
name|getRefs
argument_list|(
name|Constants
operator|.
name|R_TAGS
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Ref
argument_list|>
name|branches
init|=
name|refDb
operator|.
name|getRefs
argument_list|(
name|Constants
operator|.
name|R_HEADS
argument_list|)
operator|.
name|values
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Ref
argument_list|>
name|allTagsAndBranches
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|tags
operator|.
name|size
argument_list|()
operator|+
name|branches
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|allTagsAndBranches
operator|.
name|addAll
argument_list|(
name|tags
argument_list|)
expr_stmt|;
name|allTagsAndBranches
operator|.
name|addAll
argument_list|(
name|branches
argument_list|)
expr_stmt|;
name|parseCommits
argument_list|(
name|allTagsAndBranches
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|allMatchingTagsAndBranches
init|=
name|includedIn
argument_list|(
name|tipsByCommitTime
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|Result
name|detail
init|=
operator|new
name|Result
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setBranches
argument_list|(
name|getMatchingRefNames
argument_list|(
name|allMatchingTagsAndBranches
argument_list|,
name|branches
argument_list|)
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setTags
argument_list|(
name|getMatchingRefNames
argument_list|(
name|allMatchingTagsAndBranches
argument_list|,
name|tags
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|detail
return|;
block|}
DECL|method|includedInOne (final Collection<Ref> refs)
specifier|private
name|boolean
name|includedInOne
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
name|parseCommits
argument_list|(
name|refs
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RevCommit
argument_list|>
name|before
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|RevCommit
argument_list|>
name|after
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|partition
argument_list|(
name|before
argument_list|,
name|after
argument_list|)
expr_stmt|;
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
comment|// It is highly likely that the target is reachable from the "after" set
comment|// Within the "before" set we are trying to handle cases arising from clock skew
return|return
operator|!
name|includedIn
argument_list|(
name|after
argument_list|,
literal|1
argument_list|)
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|includedIn
argument_list|(
name|before
argument_list|,
literal|1
argument_list|)
operator|.
name|isEmpty
argument_list|()
return|;
block|}
comment|/**    * Resolves which tip refs include the target commit.    */
DECL|method|includedIn (final Collection<RevCommit> tips, int limit)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|includedIn
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|RevCommit
argument_list|>
name|tips
parameter_list|,
name|int
name|limit
parameter_list|)
throws|throws
name|IOException
throws|,
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|RevCommit
name|tip
range|:
name|tips
control|)
block|{
name|boolean
name|commitFound
init|=
literal|false
decl_stmt|;
name|rw
operator|.
name|resetRetain
argument_list|(
name|RevFlag
operator|.
name|UNINTERESTING
argument_list|,
name|containsTarget
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|tip
argument_list|)
expr_stmt|;
for|for
control|(
name|RevCommit
name|commit
range|:
name|rw
control|)
block|{
if|if
condition|(
name|commit
operator|.
name|equals
argument_list|(
name|target
argument_list|)
operator|||
name|commit
operator|.
name|has
argument_list|(
name|containsTarget
argument_list|)
condition|)
block|{
name|commitFound
operator|=
literal|true
expr_stmt|;
name|tip
operator|.
name|add
argument_list|(
name|containsTarget
argument_list|)
expr_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|commitToRef
operator|.
name|get
argument_list|(
name|tip
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|commitFound
condition|)
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|tip
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|0
operator|<
name|limit
operator|&&
name|limit
operator|<
name|result
operator|.
name|size
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**    * Partition the reference tips into two sets:    *<ul>    *<li> before = commits with time<  target.getCommitTime()    *<li> after  = commits with time>= target.getCommitTime()    *</ul>    *    * Each of the before/after lists is sorted by the the commit time.    *    * @param before    * @param after    */
DECL|method|partition (final List<RevCommit> before, final List<RevCommit> after)
specifier|private
name|void
name|partition
parameter_list|(
specifier|final
name|List
argument_list|<
name|RevCommit
argument_list|>
name|before
parameter_list|,
specifier|final
name|List
argument_list|<
name|RevCommit
argument_list|>
name|after
parameter_list|)
block|{
name|int
name|insertionPoint
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|tipsByCommitTime
argument_list|,
name|target
argument_list|,
operator|new
name|Comparator
argument_list|<
name|RevCommit
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|RevCommit
name|c1
parameter_list|,
name|RevCommit
name|c2
parameter_list|)
block|{
return|return
name|c1
operator|.
name|getCommitTime
argument_list|()
operator|-
name|c2
operator|.
name|getCommitTime
argument_list|()
return|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|insertionPoint
operator|<
literal|0
condition|)
block|{
name|insertionPoint
operator|=
operator|-
operator|(
name|insertionPoint
operator|+
literal|1
operator|)
expr_stmt|;
block|}
if|if
condition|(
literal|0
operator|<
name|insertionPoint
condition|)
block|{
name|before
operator|.
name|addAll
argument_list|(
name|tipsByCommitTime
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|insertionPoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|insertionPoint
operator|<
name|tipsByCommitTime
operator|.
name|size
argument_list|()
condition|)
block|{
name|after
operator|.
name|addAll
argument_list|(
name|tipsByCommitTime
operator|.
name|subList
argument_list|(
name|insertionPoint
argument_list|,
name|tipsByCommitTime
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Returns the short names of refs which are as well in the matchingRefs list    * as well as in the allRef list.    */
DECL|method|getMatchingRefNames (Set<String> matchingRefs, Collection<Ref> allRefs)
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getMatchingRefNames
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|matchingRefs
parameter_list|,
name|Collection
argument_list|<
name|Ref
argument_list|>
name|allRefs
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|refNames
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|matchingRefs
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Ref
name|r
range|:
name|allRefs
control|)
block|{
if|if
condition|(
name|matchingRefs
operator|.
name|contains
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|refNames
operator|.
name|add
argument_list|(
name|Repository
operator|.
name|shortenRefName
argument_list|(
name|r
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|refNames
return|;
block|}
comment|/**    * Parse commit of ref and store the relation between ref and commit.    */
DECL|method|parseCommits (final Collection<Ref> refs)
specifier|private
name|void
name|parseCommits
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|commitToRef
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|commitToRef
operator|=
name|LinkedListMultimap
operator|.
name|create
argument_list|()
expr_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|refs
control|)
block|{
specifier|final
name|RevCommit
name|commit
decl_stmt|;
try|try
block|{
name|commit
operator|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|notCommit
parameter_list|)
block|{
comment|// Its OK for a tag reference to point to a blob or a tree, this
comment|// is common in the Linux kernel or git.git repository.
comment|//
continue|continue;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|notHere
parameter_list|)
block|{
comment|// Log the problem with this branch, but keep processing.
comment|//
name|log
operator|.
name|warn
argument_list|(
literal|"Reference "
operator|+
name|ref
operator|.
name|getName
argument_list|()
operator|+
literal|" in "
operator|+
name|repo
operator|.
name|getDirectory
argument_list|()
operator|+
literal|" points to dangling object "
operator|+
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|commitToRef
operator|.
name|put
argument_list|(
name|commit
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|tipsByCommitTime
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|commitToRef
operator|.
name|keySet
argument_list|()
argument_list|)
expr_stmt|;
name|sortOlderFirst
argument_list|(
name|tipsByCommitTime
argument_list|)
expr_stmt|;
block|}
DECL|method|sortOlderFirst (final List<RevCommit> tips)
specifier|private
name|void
name|sortOlderFirst
parameter_list|(
specifier|final
name|List
argument_list|<
name|RevCommit
argument_list|>
name|tips
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|tips
argument_list|,
operator|new
name|Comparator
argument_list|<
name|RevCommit
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|RevCommit
name|c1
parameter_list|,
name|RevCommit
name|c2
parameter_list|)
block|{
return|return
name|c1
operator|.
name|getCommitTime
argument_list|()
operator|-
name|c2
operator|.
name|getCommitTime
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|class|Result
specifier|public
specifier|static
class|class
name|Result
block|{
DECL|field|branches
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|branches
decl_stmt|;
DECL|field|tags
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|tags
decl_stmt|;
DECL|method|Result ()
specifier|public
name|Result
parameter_list|()
block|{     }
DECL|method|setBranches (final List<String> b)
specifier|public
name|void
name|setBranches
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|b
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|branches
operator|=
name|b
expr_stmt|;
block|}
DECL|method|getBranches ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBranches
parameter_list|()
block|{
return|return
name|branches
return|;
block|}
DECL|method|setTags (final List<String> t)
specifier|public
name|void
name|setTags
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|t
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|tags
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getTags ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
block|}
block|}
end_class

end_unit

