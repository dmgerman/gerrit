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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|IncludedInDetail
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
name|ArrayList
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
name|HashMap
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
name|IncludedInDetail
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
name|Set
argument_list|<
name|Ref
argument_list|>
name|tags
init|=
operator|new
name|HashSet
argument_list|<
name|Ref
argument_list|>
argument_list|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
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
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Ref
argument_list|>
name|branches
init|=
operator|new
name|HashSet
argument_list|<
name|Ref
argument_list|>
argument_list|(
name|repo
operator|.
name|getRefDatabase
argument_list|()
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
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Ref
argument_list|>
name|allTagsAndBranches
init|=
operator|new
name|HashSet
argument_list|<
name|Ref
argument_list|>
argument_list|()
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
name|Set
argument_list|<
name|Ref
argument_list|>
name|allMatchingTagsAndBranches
init|=
name|includedIn
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|commit
argument_list|,
name|allTagsAndBranches
argument_list|)
decl_stmt|;
name|IncludedInDetail
name|detail
init|=
operator|new
name|IncludedInDetail
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
comment|/**    * Resolves which tip refs include the target commit.    */
DECL|method|includedIn (final Repository repo, final RevWalk rw, final RevCommit target, final Set<Ref> tipRefs)
specifier|private
specifier|static
name|Set
argument_list|<
name|Ref
argument_list|>
name|includedIn
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
name|target
parameter_list|,
specifier|final
name|Set
argument_list|<
name|Ref
argument_list|>
name|tipRefs
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
name|Ref
argument_list|>
name|result
init|=
operator|new
name|HashSet
argument_list|<
name|Ref
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|RevCommit
argument_list|,
name|Set
argument_list|<
name|Ref
argument_list|>
argument_list|>
name|tipsAndCommits
init|=
name|parseCommits
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|tipRefs
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|RevCommit
argument_list|>
name|tips
init|=
operator|new
name|ArrayList
argument_list|<
name|RevCommit
argument_list|>
argument_list|(
name|tipsAndCommits
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
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
name|Set
argument_list|<
name|RevCommit
argument_list|>
name|targetReachableFrom
init|=
operator|new
name|HashSet
argument_list|<
name|RevCommit
argument_list|>
argument_list|()
decl_stmt|;
name|targetReachableFrom
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
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
name|targetReachableFrom
operator|.
name|contains
argument_list|(
name|commit
argument_list|)
condition|)
block|{
name|commitFound
operator|=
literal|true
expr_stmt|;
name|targetReachableFrom
operator|.
name|add
argument_list|(
name|tip
argument_list|)
expr_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|tipsAndCommits
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
block|}
return|return
name|result
return|;
block|}
comment|/**    * Returns the short names of refs which are as well in the matchingRefs list    * as well as in the allRef list.    */
DECL|method|getMatchingRefNames (Set<Ref> matchingRefs, Set<Ref> allRefs)
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
name|Ref
argument_list|>
name|matchingRefs
parameter_list|,
name|Set
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
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Ref
name|matchingRef
range|:
name|matchingRefs
control|)
block|{
if|if
condition|(
name|allRefs
operator|.
name|contains
argument_list|(
name|matchingRef
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
name|matchingRef
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
DECL|method|parseCommits (final Repository repo, final RevWalk rw, final Set<Ref> refs)
specifier|private
specifier|static
name|Map
argument_list|<
name|RevCommit
argument_list|,
name|Set
argument_list|<
name|Ref
argument_list|>
argument_list|>
name|parseCommits
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
name|Set
argument_list|<
name|Ref
argument_list|>
name|refs
parameter_list|)
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|RevCommit
argument_list|,
name|Set
argument_list|<
name|Ref
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<
name|RevCommit
argument_list|,
name|Set
argument_list|<
name|Ref
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
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
name|Set
argument_list|<
name|Ref
argument_list|>
name|relatedRefs
init|=
name|result
operator|.
name|get
argument_list|(
name|commit
argument_list|)
decl_stmt|;
if|if
condition|(
name|relatedRefs
operator|==
literal|null
condition|)
block|{
name|relatedRefs
operator|=
operator|new
name|HashSet
argument_list|<
name|Ref
argument_list|>
argument_list|()
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|commit
argument_list|,
name|relatedRefs
argument_list|)
expr_stmt|;
block|}
name|relatedRefs
operator|.
name|add
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

