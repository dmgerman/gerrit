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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|base
operator|.
name|Joiner
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
name|SetMultimap
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
name|Change
operator|.
name|Status
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
operator|.
name|NameKey
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
name|RefNames
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
name|git
operator|.
name|GroupCollector
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
name|NoSuchChangeException
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
name|RevObject
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
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_class
DECL|class|Schema_108
specifier|public
class|class
name|Schema_108
extends|extends
name|SchemaVersion
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|Schema_108 (Provider<Schema_107> prior, GitRepositoryManager repoManager)
name|Schema_108
parameter_list|(
name|Provider
argument_list|<
name|Schema_107
argument_list|>
name|prior
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|super
argument_list|(
name|prior
argument_list|)
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|migrateData (ReviewDb db, UpdateUI ui)
specifier|protected
name|void
name|migrateData
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Listing all changes ..."
argument_list|)
expr_stmt|;
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|openByProject
init|=
name|getOpenChangesByProject
argument_list|(
name|db
argument_list|,
name|ui
argument_list|)
decl_stmt|;
name|ui
operator|.
name|message
argument_list|(
literal|"done"
argument_list|)
expr_stmt|;
name|ui
operator|.
name|message
argument_list|(
literal|"Updating groups for open changes ..."
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
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
name|Change
operator|.
name|Id
argument_list|>
argument_list|>
name|e
range|:
name|openByProject
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
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
name|e
operator|.
name|getKey
argument_list|()
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
name|updateProjectGroups
argument_list|(
name|db
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|,
operator|(
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
operator|)
name|e
operator|.
name|getValue
argument_list|()
argument_list|,
name|ui
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|NoSuchChangeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|err
argument_list|)
throw|;
block|}
if|if
condition|(
operator|++
name|i
operator|%
literal|100
operator|==
literal|0
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"  done "
operator|+
name|i
operator|+
literal|" projects ..."
argument_list|)
expr_stmt|;
block|}
block|}
name|ui
operator|.
name|message
argument_list|(
literal|"done"
argument_list|)
expr_stmt|;
block|}
DECL|method|updateProjectGroups (ReviewDb db, Repository repo, RevWalk rw, Set<Change.Id> changes, UpdateUI ui)
specifier|private
name|void
name|updateProjectGroups
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changes
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
comment|// Match sorting in ReceiveCommits.
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
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
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|RefDatabase
name|refdb
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|refdb
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
control|)
block|{
name|RevCommit
name|c
init|=
name|maybeParseCommit
argument_list|(
name|rw
argument_list|,
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ui
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|rw
operator|.
name|markUninteresting
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|ListMultimap
argument_list|<
name|ObjectId
argument_list|,
name|Ref
argument_list|>
name|changeRefsBySha
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
name|ListMultimap
argument_list|<
name|ObjectId
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|patchSetsBySha
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
name|Ref
name|ref
range|:
name|refdb
operator|.
name|getRefs
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
name|ObjectId
name|id
init|=
name|ref
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
if|if
condition|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|id
operator|=
name|id
operator|.
name|copy
argument_list|()
expr_stmt|;
name|changeRefsBySha
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|PatchSet
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|psId
operator|!=
literal|null
operator|&&
name|changes
operator|.
name|contains
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
name|patchSetsBySha
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|psId
argument_list|)
expr_stmt|;
name|RevCommit
name|c
init|=
name|maybeParseCommit
argument_list|(
name|rw
argument_list|,
name|id
argument_list|,
name|ui
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
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
block|}
name|GroupCollector
name|collector
init|=
name|GroupCollector
operator|.
name|createForSchemaUpgradeOnly
argument_list|(
name|changeRefsBySha
argument_list|,
name|db
argument_list|)
decl_stmt|;
name|RevCommit
name|c
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
name|collector
operator|.
name|visit
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|updateGroups
argument_list|(
name|db
argument_list|,
name|collector
argument_list|,
name|patchSetsBySha
argument_list|)
expr_stmt|;
block|}
DECL|method|updateGroups (ReviewDb db, GroupCollector collector, ListMultimap<ObjectId, PatchSet.Id> patchSetsBySha)
specifier|private
specifier|static
name|void
name|updateGroups
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|GroupCollector
name|collector
parameter_list|,
name|ListMultimap
argument_list|<
name|ObjectId
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
name|patchSetsBySha
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
name|patchSets
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|toMap
argument_list|(
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetsBySha
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ObjectId
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|e
range|:
name|collector
operator|.
name|getGroups
argument_list|()
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|PatchSet
operator|.
name|Id
name|psId
range|:
name|patchSetsBySha
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
control|)
block|{
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
name|ps
operator|!=
literal|null
condition|)
block|{
name|ps
operator|.
name|setGroups
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|update
argument_list|(
name|patchSets
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getOpenChangesByProject ( ReviewDb db, UpdateUI ui)
specifier|private
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|getOpenChangesByProject
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|OrmException
block|{
name|SortedSet
argument_list|<
name|NameKey
argument_list|>
name|projects
init|=
name|repoManager
operator|.
name|list
argument_list|()
decl_stmt|;
name|SortedSet
argument_list|<
name|NameKey
argument_list|>
name|nonExistentProjects
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|()
decl_stmt|;
name|SetMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|openByProject
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|hashSetValues
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
for|for
control|(
name|Change
name|c
range|:
name|db
operator|.
name|changes
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|Status
name|status
init|=
name|c
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|status
operator|!=
literal|null
operator|&&
name|status
operator|.
name|isClosed
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|NameKey
name|projectKey
init|=
name|c
operator|.
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|projects
operator|.
name|contains
argument_list|(
name|projectKey
argument_list|)
condition|)
block|{
name|nonExistentProjects
operator|.
name|add
argument_list|(
name|projectKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// The old "submitted" state is not supported anymore
comment|// (thus status is null) but it was an opened state and needs
comment|// to be migrated as such
name|openByProject
operator|.
name|put
argument_list|(
name|projectKey
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|nonExistentProjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Detected open changes referring to the following non-existent projects:"
argument_list|)
expr_stmt|;
name|ui
operator|.
name|message
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|nonExistentProjects
argument_list|)
argument_list|)
expr_stmt|;
name|ui
operator|.
name|message
argument_list|(
literal|"It is highly recommended to remove\n"
operator|+
literal|"the obsolete open changes, comments and patch-sets from your DB.\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|openByProject
return|;
block|}
DECL|method|maybeParseCommit (RevWalk rw, ObjectId id, UpdateUI ui)
specifier|private
specifier|static
name|RevCommit
name|maybeParseCommit
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectId
name|id
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|RevObject
name|obj
init|=
name|rw
operator|.
name|parseAny
argument_list|(
name|id
argument_list|)
decl_stmt|;
return|return
operator|(
name|obj
operator|instanceof
name|RevCommit
operator|)
condition|?
operator|(
name|RevCommit
operator|)
name|obj
else|:
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|MissingObjectException
name|moe
parameter_list|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Missing object: "
operator|+
name|id
operator|.
name|getName
argument_list|()
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

