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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toMap
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
name|MultimapBuilder
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
name|entities
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
name|entities
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
name|entities
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
name|extensions
operator|.
name|restapi
operator|.
name|AuthException
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|ProjectCache
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
name|ProjectState
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
name|Singleton
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
name|Deque
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
name|LinkedHashSet
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
name|Objects
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

begin_class
annotation|@
name|Singleton
DECL|class|RelatedChangesSorter
class|class
name|RelatedChangesSorter
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|RelatedChangesSorter ( GitRepositoryManager repoManager, PermissionBackend permissionBackend, ProjectCache projectCache)
name|RelatedChangesSorter
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
DECL|method|sort (List<ChangeData> in, PatchSet startPs)
specifier|public
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|sort
parameter_list|(
name|List
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|,
name|PatchSet
name|startPs
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
block|{
name|checkArgument
argument_list|(
operator|!
name|in
operator|.
name|isEmpty
argument_list|()
argument_list|,
literal|"Input may not be empty"
argument_list|)
expr_stmt|;
comment|// Map of all patch sets, keyed by commit SHA-1.
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|PatchSetData
argument_list|>
name|byId
init|=
name|collectById
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|PatchSetData
name|start
init|=
name|byId
operator|.
name|get
argument_list|(
name|startPs
operator|.
name|commitId
argument_list|()
argument_list|)
decl_stmt|;
name|requireNonNull
argument_list|(
name|start
argument_list|,
parameter_list|()
lambda|->
name|String
operator|.
name|format
argument_list|(
literal|"commit %s of patch set %s not found in %s"
argument_list|,
name|startPs
operator|.
name|commitId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|startPs
operator|.
name|id
argument_list|()
argument_list|,
name|byId
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toMap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|e
lambda|->
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|patchSet
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// Map of patch set -> immediate parent.
name|ListMultimap
argument_list|<
name|PatchSetData
argument_list|,
name|PatchSetData
argument_list|>
name|parents
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
literal|3
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// Map of patch set -> immediate children.
name|ListMultimap
argument_list|<
name|PatchSetData
argument_list|,
name|PatchSetData
argument_list|>
name|children
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
literal|3
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
comment|// All other patch sets of the same change as startPs.
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|otherPatchSetsOfStart
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|PatchSetData
name|thisPsd
init|=
name|requireNonNull
argument_list|(
name|byId
operator|.
name|get
argument_list|(
name|ps
operator|.
name|commitId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|cd
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|start
operator|.
name|id
argument_list|()
argument_list|)
operator|&&
operator|!
name|ps
operator|.
name|id
argument_list|()
operator|.
name|equals
argument_list|(
name|start
operator|.
name|psId
argument_list|()
argument_list|)
condition|)
block|{
name|otherPatchSetsOfStart
operator|.
name|add
argument_list|(
name|thisPsd
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|RevCommit
name|p
range|:
name|thisPsd
operator|.
name|commit
argument_list|()
operator|.
name|getParents
argument_list|()
control|)
block|{
name|PatchSetData
name|parentPsd
init|=
name|byId
operator|.
name|get
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentPsd
operator|!=
literal|null
condition|)
block|{
name|parents
operator|.
name|put
argument_list|(
name|thisPsd
argument_list|,
name|parentPsd
argument_list|)
expr_stmt|;
name|children
operator|.
name|put
argument_list|(
name|parentPsd
argument_list|,
name|thisPsd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|Collection
argument_list|<
name|PatchSetData
argument_list|>
name|ancestors
init|=
name|walkAncestors
argument_list|(
name|parents
argument_list|,
name|start
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|descendants
init|=
name|walkDescendants
argument_list|(
name|children
argument_list|,
name|start
argument_list|,
name|otherPatchSetsOfStart
argument_list|,
name|ancestors
argument_list|)
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
name|ancestors
operator|.
name|size
argument_list|()
operator|+
name|descendants
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|Lists
operator|.
name|reverse
argument_list|(
name|descendants
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|addAll
argument_list|(
name|ancestors
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|collectById (List<ChangeData> in)
specifier|private
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|PatchSetData
argument_list|>
name|collectById
parameter_list|(
name|List
argument_list|<
name|ChangeData
argument_list|>
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|in
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|ObjectId
argument_list|,
name|PatchSetData
argument_list|>
name|result
init|=
name|Maps
operator|.
name|newHashMapWithExpectedSize
argument_list|(
name|in
operator|.
name|size
argument_list|()
operator|*
literal|3
argument_list|)
decl_stmt|;
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
literal|true
argument_list|)
expr_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|in
control|)
block|{
name|checkArgument
argument_list|(
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|equals
argument_list|(
name|project
argument_list|)
argument_list|,
literal|"Expected change %s in project %s, found %s"
argument_list|,
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|project
argument_list|,
name|cd
operator|.
name|change
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
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
name|RevCommit
name|c
init|=
name|rw
operator|.
name|parseCommit
argument_list|(
name|ps
operator|.
name|commitId
argument_list|()
argument_list|)
decl_stmt|;
name|PatchSetData
name|psd
init|=
name|PatchSetData
operator|.
name|create
argument_list|(
name|cd
argument_list|,
name|ps
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|result
operator|.
name|put
argument_list|(
name|ps
operator|.
name|commitId
argument_list|()
argument_list|,
name|psd
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|walkAncestors ( ListMultimap<PatchSetData, PatchSetData> parents, PatchSetData start)
specifier|private
name|Collection
argument_list|<
name|PatchSetData
argument_list|>
name|walkAncestors
parameter_list|(
name|ListMultimap
argument_list|<
name|PatchSetData
argument_list|,
name|PatchSetData
argument_list|>
name|parents
parameter_list|,
name|PatchSetData
name|start
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
name|LinkedHashSet
argument_list|<
name|PatchSetData
argument_list|>
name|result
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|PatchSetData
argument_list|>
name|pending
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|pending
operator|.
name|add
argument_list|(
name|start
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|pending
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PatchSetData
name|psd
init|=
name|pending
operator|.
name|remove
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|contains
argument_list|(
name|psd
argument_list|)
operator|||
operator|!
name|isVisible
argument_list|(
name|psd
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|result
operator|.
name|add
argument_list|(
name|psd
argument_list|)
expr_stmt|;
name|pending
operator|.
name|addAll
argument_list|(
name|Lists
operator|.
name|reverse
argument_list|(
name|parents
operator|.
name|get
argument_list|(
name|psd
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|walkDescendants ( ListMultimap<PatchSetData, PatchSetData> children, PatchSetData start, List<PatchSetData> otherPatchSetsOfStart, Iterable<PatchSetData> ancestors)
specifier|private
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|walkDescendants
parameter_list|(
name|ListMultimap
argument_list|<
name|PatchSetData
argument_list|,
name|PatchSetData
argument_list|>
name|children
parameter_list|,
name|PatchSetData
name|start
parameter_list|,
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|otherPatchSetsOfStart
parameter_list|,
name|Iterable
argument_list|<
name|PatchSetData
argument_list|>
name|ancestors
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|alreadyEmittedChanges
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|addAllChangeIds
argument_list|(
name|alreadyEmittedChanges
argument_list|,
name|ancestors
argument_list|)
expr_stmt|;
comment|// Prefer descendants found by following the original patch set passed in.
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|result
init|=
name|walkDescendentsImpl
argument_list|(
name|alreadyEmittedChanges
argument_list|,
name|children
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|start
argument_list|)
argument_list|)
decl_stmt|;
name|addAllChangeIds
argument_list|(
name|alreadyEmittedChanges
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// Then, go back and add new indirect descendants found by following any
comment|// other patch sets of start. These show up after all direct descendants,
comment|// because we wouldn't know where in the walk to insert them.
name|result
operator|.
name|addAll
argument_list|(
name|walkDescendentsImpl
argument_list|(
name|alreadyEmittedChanges
argument_list|,
name|children
argument_list|,
name|otherPatchSetsOfStart
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|addAllChangeIds ( Collection<Change.Id> changeIds, Iterable<PatchSetData> psds)
specifier|private
specifier|static
name|void
name|addAllChangeIds
parameter_list|(
name|Collection
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changeIds
parameter_list|,
name|Iterable
argument_list|<
name|PatchSetData
argument_list|>
name|psds
parameter_list|)
block|{
for|for
control|(
name|PatchSetData
name|psd
range|:
name|psds
control|)
block|{
name|changeIds
operator|.
name|add
argument_list|(
name|psd
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|walkDescendentsImpl ( Set<Change.Id> alreadyEmittedChanges, ListMultimap<PatchSetData, PatchSetData> children, List<PatchSetData> start)
specifier|private
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|walkDescendentsImpl
parameter_list|(
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|alreadyEmittedChanges
parameter_list|,
name|ListMultimap
argument_list|<
name|PatchSetData
argument_list|,
name|PatchSetData
argument_list|>
name|children
parameter_list|,
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|start
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
if|if
condition|(
name|start
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
name|maxPatchSetIds
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|PatchSetData
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PatchSetData
argument_list|>
name|allPatchSets
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Deque
argument_list|<
name|PatchSetData
argument_list|>
name|pending
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
name|pending
operator|.
name|addAll
argument_list|(
name|start
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|pending
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|PatchSetData
name|psd
init|=
name|pending
operator|.
name|remove
argument_list|()
decl_stmt|;
if|if
condition|(
name|seen
operator|.
name|contains
argument_list|(
name|psd
argument_list|)
operator|||
operator|!
name|isVisible
argument_list|(
name|psd
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|seen
operator|.
name|add
argument_list|(
name|psd
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|alreadyEmittedChanges
operator|.
name|contains
argument_list|(
name|psd
operator|.
name|id
argument_list|()
argument_list|)
condition|)
block|{
comment|// Don't emit anything for changes that were previously emitted, even
comment|// though different patch sets might show up later. However, do
comment|// continue walking through them for the purposes of finding indirect
comment|// descendants.
name|PatchSet
operator|.
name|Id
name|oldMax
init|=
name|maxPatchSetIds
operator|.
name|get
argument_list|(
name|psd
operator|.
name|id
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldMax
operator|==
literal|null
operator|||
name|psd
operator|.
name|psId
argument_list|()
operator|.
name|get
argument_list|()
operator|>
name|oldMax
operator|.
name|get
argument_list|()
condition|)
block|{
name|maxPatchSetIds
operator|.
name|put
argument_list|(
name|psd
operator|.
name|id
argument_list|()
argument_list|,
name|psd
operator|.
name|psId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|allPatchSets
operator|.
name|add
argument_list|(
name|psd
argument_list|)
expr_stmt|;
block|}
comment|// Depth-first search with newest children first.
for|for
control|(
name|PatchSetData
name|child
range|:
name|children
operator|.
name|get
argument_list|(
name|psd
argument_list|)
control|)
block|{
name|pending
operator|.
name|addFirst
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If we saw the same change multiple times, prefer the latest patch set.
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
name|allPatchSets
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PatchSetData
name|psd
range|:
name|allPatchSets
control|)
block|{
if|if
condition|(
name|requireNonNull
argument_list|(
name|maxPatchSetIds
operator|.
name|get
argument_list|(
name|psd
operator|.
name|id
argument_list|()
argument_list|)
argument_list|)
operator|.
name|equals
argument_list|(
name|psd
operator|.
name|psId
argument_list|()
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|psd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|isVisible (PatchSetData psd)
specifier|private
name|boolean
name|isVisible
parameter_list|(
name|PatchSetData
name|psd
parameter_list|)
throws|throws
name|PermissionBackendException
throws|,
name|IOException
block|{
name|PermissionBackend
operator|.
name|WithUser
name|perm
init|=
name|permissionBackend
operator|.
name|currentUser
argument_list|()
decl_stmt|;
try|try
block|{
name|perm
operator|.
name|change
argument_list|(
name|psd
operator|.
name|data
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
name|ProjectState
name|state
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|psd
operator|.
name|data
argument_list|()
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|state
operator|!=
literal|null
operator|&&
name|state
operator|.
name|statePermitsRead
argument_list|()
return|;
block|}
annotation|@
name|AutoValue
DECL|class|PatchSetData
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
name|AutoValue_RelatedChangesSorter_PatchSetData
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
DECL|method|psId ()
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|()
block|{
return|return
name|patchSet
argument_list|()
operator|.
name|id
argument_list|()
return|;
block|}
DECL|method|id ()
name|Change
operator|.
name|Id
name|id
parameter_list|()
block|{
return|return
name|psId
argument_list|()
operator|.
name|changeId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
specifier|final
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|patchSet
argument_list|()
operator|.
name|id
argument_list|()
argument_list|,
name|commit
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
specifier|final
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|obj
operator|instanceof
name|PatchSetData
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|PatchSetData
name|o
init|=
operator|(
name|PatchSetData
operator|)
name|obj
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|patchSet
argument_list|()
operator|.
name|id
argument_list|()
argument_list|,
name|o
operator|.
name|patchSet
argument_list|()
operator|.
name|id
argument_list|()
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|commit
argument_list|()
argument_list|,
name|o
operator|.
name|commit
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

