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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ArrayListMultimap
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
name|Maps
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|reviewdb
operator|.
name|client
operator|.
name|PatchSetAncestor
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
name|server
operator|.
name|ReviewDb
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
name|change
operator|.
name|GetRelated
operator|.
name|ChangeAndCommit
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
name|change
operator|.
name|GetRelated
operator|.
name|RelatedInfo
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
name|project
operator|.
name|ChangeControl
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
name|ProjectControl
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
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
name|RepositoryNotFoundException
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
name|RevSort
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
comment|/** Implementation of {@link GetRelated} using {@link PatchSetAncestor}s. */
end_comment

begin_class
DECL|class|GetRelatedByAncestors
class|class
name|GetRelatedByAncestors
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
name|GetRelated
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|gitMgr
specifier|private
specifier|final
name|GitRepositoryManager
name|gitMgr
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|queryProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetRelatedByAncestors (GitRepositoryManager gitMgr, Provider<ReviewDb> db, Provider<InternalChangeQuery> queryProvider)
name|GetRelatedByAncestors
parameter_list|(
name|GitRepositoryManager
name|gitMgr
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|)
block|{
name|this
operator|.
name|gitMgr
operator|=
name|gitMgr
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|queryProvider
operator|=
name|queryProvider
expr_stmt|;
block|}
DECL|method|getRelated (RevisionResource rsrc)
specifier|public
name|RelatedInfo
name|getRelated
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
throws|,
name|OrmException
block|{
try|try
init|(
name|Repository
name|git
init|=
name|gitMgr
operator|.
name|openRepository
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|RelatedInfo
name|info
init|=
operator|new
name|RelatedInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|changes
operator|=
name|walk
argument_list|(
name|rsrc
argument_list|,
name|rw
argument_list|,
name|ref
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
DECL|method|walk (RevisionResource rsrc, RevWalk rw, Ref ref)
specifier|private
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|walk
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Ref
name|ref
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|changes
init|=
name|allOpenChanges
argument_list|(
name|rsrc
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
init|=
name|allPatchSets
argument_list|(
name|rsrc
argument_list|,
name|changes
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|PatchSet
argument_list|>
name|commits
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|p
range|:
name|patchSets
operator|.
name|values
argument_list|()
control|)
block|{
name|commits
operator|.
name|put
argument_list|(
name|p
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
name|RevCommit
name|rev
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|TOPO
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|rev
argument_list|)
expr_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getObjectId
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IncorrectObjectTypeException
name|notCommit
parameter_list|)
block|{
comment|// Ignore and treat as new branch.
block|}
block|}
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|added
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|parents
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|RevCommit
name|c
init|;
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
condition|;
control|)
block|{
name|PatchSet
name|p
init|=
name|commits
operator|.
name|get
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|Change
name|g
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|g
operator|=
name|changes
operator|.
name|get
argument_list|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
operator|.
name|change
argument_list|()
expr_stmt|;
name|added
operator|.
name|add
argument_list|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|parents
operator|.
name|add
argument_list|(
operator|new
name|ChangeAndCommit
argument_list|(
name|g
argument_list|,
name|p
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|list
init|=
name|children
argument_list|(
name|rsrc
argument_list|,
name|rw
argument_list|,
name|changes
argument_list|,
name|patchSets
argument_list|,
name|added
argument_list|)
decl_stmt|;
name|list
operator|.
name|addAll
argument_list|(
name|parents
argument_list|)
expr_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|ChangeAndCommit
name|r
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|commit
operator|!=
literal|null
operator|&&
name|r
operator|.
name|commit
operator|.
name|commit
operator|.
name|equals
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
return|return
name|list
return|;
block|}
DECL|method|allOpenChanges (RevisionResource rsrc)
specifier|private
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|allOpenChanges
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|ChangeData
operator|.
name|asMap
argument_list|(
name|queryProvider
operator|.
name|get
argument_list|()
operator|.
name|byBranchOpen
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getDest
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|allPatchSets (RevisionResource rsrc, Collection<ChangeData> cds)
specifier|private
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|allPatchSets
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|Collection
argument_list|<
name|ChangeData
argument_list|>
name|cds
parameter_list|)
throws|throws
name|OrmException
block|{
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|r
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|cds
operator|.
name|size
argument_list|()
operator|*
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|cds
control|)
block|{
for|for
control|(
name|PatchSet
name|p
range|:
name|cd
operator|.
name|patchSets
argument_list|()
control|)
block|{
name|r
operator|.
name|put
argument_list|(
name|p
operator|.
name|getId
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|rsrc
operator|.
name|getEdit
argument_list|()
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|r
operator|.
name|put
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|method|children (RevisionResource rsrc, RevWalk rw, Map<Change.Id, ChangeData> changes, Map<PatchSet.Id, PatchSet> patchSets, Set<Change.Id> added)
specifier|private
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|children
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|changes
parameter_list|,
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
parameter_list|,
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|added
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
comment|// children is a map of parent commit name to PatchSet built on it.
name|Multimap
argument_list|<
name|String
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|children
init|=
name|allChildren
argument_list|(
name|changes
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
name|RevFlag
name|seenCommit
init|=
name|rw
operator|.
name|newFlag
argument_list|(
literal|"seenCommit"
argument_list|)
decl_stmt|;
name|LinkedList
argument_list|<
name|String
argument_list|>
name|q
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
name|seedQueue
argument_list|(
name|rsrc
argument_list|,
name|rw
argument_list|,
name|seenCommit
argument_list|,
name|patchSets
argument_list|,
name|q
argument_list|)
expr_stmt|;
name|ProjectControl
name|projectCtl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
operator|.
name|getProjectControl
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|seenChange
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ChangeAndCommit
argument_list|>
name|graph
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|q
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|id
init|=
name|q
operator|.
name|remove
argument_list|()
decl_stmt|;
comment|// For every matching change find the most recent patch set.
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|matches
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
operator|.
name|Id
name|psId
range|:
name|children
operator|.
name|get
argument_list|(
name|id
argument_list|)
control|)
block|{
name|PatchSet
operator|.
name|Id
name|e
init|=
name|matches
operator|.
name|get
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|(
name|e
operator|==
literal|null
operator|||
name|e
operator|.
name|get
argument_list|()
operator|<
name|psId
operator|.
name|get
argument_list|()
operator|)
operator|&&
name|isVisible
argument_list|(
name|projectCtl
argument_list|,
name|changes
argument_list|,
name|patchSets
argument_list|,
name|psId
argument_list|)
condition|)
block|{
name|matches
operator|.
name|put
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|psId
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|e
range|:
name|matches
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ChangeData
name|cd
init|=
name|changes
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
name|ps
init|=
name|patchSets
operator|.
name|get
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cd
operator|==
literal|null
operator|||
name|ps
operator|==
literal|null
operator|||
operator|!
name|seenChange
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|has
argument_list|(
name|seenCommit
argument_list|)
condition|)
block|{
name|c
operator|.
name|add
argument_list|(
name|seenCommit
argument_list|)
expr_stmt|;
name|q
operator|.
name|addFirst
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|added
operator|.
name|add
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
name|rw
operator|.
name|parseBody
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|graph
operator|.
name|add
argument_list|(
operator|new
name|ChangeAndCommit
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|,
name|ps
argument_list|,
name|c
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Collections
operator|.
name|reverse
argument_list|(
name|graph
argument_list|)
expr_stmt|;
return|return
name|graph
return|;
block|}
DECL|method|isVisible (ProjectControl projectCtl, Map<Change.Id, ChangeData> changes, Map<PatchSet.Id, PatchSet> patchSets, PatchSet.Id psId)
specifier|private
name|boolean
name|isVisible
parameter_list|(
name|ProjectControl
name|projectCtl
parameter_list|,
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
name|changes
parameter_list|,
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
name|ChangeData
name|cd
init|=
name|changes
operator|.
name|get
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSet
name|ps
init|=
name|patchSets
operator|.
name|get
argument_list|(
name|psId
argument_list|)
decl_stmt|;
if|if
condition|(
name|cd
operator|!=
literal|null
operator|&&
name|ps
operator|!=
literal|null
condition|)
block|{
comment|// Related changes are in the same project, so reuse the existing
comment|// ProjectControl.
name|ChangeControl
name|ctl
init|=
name|projectCtl
operator|.
name|controlFor
argument_list|(
name|cd
operator|.
name|change
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ctl
operator|.
name|isVisible
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|&&
name|ctl
operator|.
name|isPatchVisible
argument_list|(
name|ps
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|seedQueue (RevisionResource rsrc, RevWalk rw, RevFlag seenCommit, Map<PatchSet.Id, PatchSet> patchSets, LinkedList<String> q)
specifier|private
name|void
name|seedQueue
parameter_list|(
name|RevisionResource
name|rsrc
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|RevFlag
name|seenCommit
parameter_list|,
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|PatchSet
argument_list|>
name|patchSets
parameter_list|,
name|LinkedList
argument_list|<
name|String
argument_list|>
name|q
parameter_list|)
throws|throws
name|IOException
block|{
name|RevCommit
name|tip
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|rsrc
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|tip
operator|.
name|add
argument_list|(
name|seenCommit
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|tip
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|Change
operator|.
name|Id
name|cId
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSet
name|p
range|:
name|patchSets
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|cId
operator|.
name|equals
argument_list|(
name|p
operator|.
name|getId
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|p
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|c
operator|.
name|has
argument_list|(
name|seenCommit
argument_list|)
condition|)
block|{
name|c
operator|.
name|add
argument_list|(
name|seenCommit
argument_list|)
expr_stmt|;
name|q
operator|.
name|add
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot read patch set %d of %d"
argument_list|,
name|p
operator|.
name|getPatchSetId
argument_list|()
argument_list|,
name|cId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|allChildren (Collection<Change.Id> ids)
specifier|private
name|Multimap
argument_list|<
name|String
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|allChildren
parameter_list|(
name|Collection
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|ids
parameter_list|)
throws|throws
name|OrmException
block|{
name|ReviewDb
name|db
init|=
name|dbProvider
operator|.
name|get
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ResultSet
argument_list|<
name|PatchSetAncestor
argument_list|>
argument_list|>
name|t
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Id
name|id
range|:
name|ids
control|)
block|{
name|t
operator|.
name|add
argument_list|(
name|db
operator|.
name|patchSetAncestors
argument_list|()
operator|.
name|byChange
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Multimap
argument_list|<
name|String
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|r
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|ResultSet
argument_list|<
name|PatchSetAncestor
argument_list|>
name|rs
range|:
name|t
control|)
block|{
for|for
control|(
name|PatchSetAncestor
name|a
range|:
name|rs
control|)
block|{
name|r
operator|.
name|put
argument_list|(
name|a
operator|.
name|getAncestorRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|a
operator|.
name|getPatchSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

