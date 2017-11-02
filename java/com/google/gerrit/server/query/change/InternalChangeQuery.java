begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
operator|.
name|Predicate
operator|.
name|and
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
operator|.
name|Predicate
operator|.
name|not
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
operator|.
name|Predicate
operator|.
name|or
import|;
end_import

begin_import
import|import static
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
name|ChangeStatusPredicate
operator|.
name|open
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
name|base
operator|.
name|Strings
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
name|index
operator|.
name|FieldDef
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
name|index
operator|.
name|IndexConfig
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
name|index
operator|.
name|query
operator|.
name|InternalQuery
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|Branch
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
name|index
operator|.
name|change
operator|.
name|ChangeIndexCollection
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

begin_comment
comment|/**  * Query wrapper for the change index.  *  *<p>Instances are one-time-use. Other singleton classes should inject a Provider rather than  * holding on to a single instance.  */
end_comment

begin_class
DECL|class|InternalChangeQuery
specifier|public
class|class
name|InternalChangeQuery
extends|extends
name|InternalQuery
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|ref (Branch.NameKey branch)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|ref
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
block|{
return|return
operator|new
name|RefPredicate
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|change (Change.Key key)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|change
parameter_list|(
name|Change
operator|.
name|Key
name|key
parameter_list|)
block|{
return|return
operator|new
name|ChangeIdPredicate
argument_list|(
name|key
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|project (Project.NameKey project)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
return|return
operator|new
name|ProjectPredicate
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|status (Change.Status status)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|status
parameter_list|(
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|forStatus
argument_list|(
name|status
argument_list|)
return|;
block|}
DECL|method|commit (String id)
specifier|private
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|commit
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
operator|new
name|CommitPredicate
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|notesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|InternalChangeQuery ( ChangeQueryProcessor queryProcessor, ChangeIndexCollection indexes, IndexConfig indexConfig, ChangeData.Factory changeDataFactory, ChangeNotes.Factory notesFactory)
name|InternalChangeQuery
parameter_list|(
name|ChangeQueryProcessor
name|queryProcessor
parameter_list|,
name|ChangeIndexCollection
name|indexes
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|notesFactory
parameter_list|)
block|{
name|super
argument_list|(
name|queryProcessor
argument_list|,
name|indexes
argument_list|,
name|indexConfig
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|notesFactory
operator|=
name|notesFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setLimit (int n)
specifier|public
name|InternalChangeQuery
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|super
operator|.
name|setLimit
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|enforceVisibility (boolean enforce)
specifier|public
name|InternalChangeQuery
name|enforceVisibility
parameter_list|(
name|boolean
name|enforce
parameter_list|)
block|{
name|super
operator|.
name|enforceVisibility
argument_list|(
name|enforce
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|SafeVarargs
annotation|@
name|Override
DECL|method|setRequestedFields (FieldDef<ChangeData, ?>.... fields)
specifier|public
specifier|final
name|InternalChangeQuery
name|setRequestedFields
parameter_list|(
name|FieldDef
argument_list|<
name|ChangeData
argument_list|,
name|?
argument_list|>
modifier|...
name|fields
parameter_list|)
block|{
name|super
operator|.
name|setRequestedFields
argument_list|(
name|fields
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|noFields ()
specifier|public
name|InternalChangeQuery
name|noFields
parameter_list|()
block|{
name|super
operator|.
name|noFields
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|byKey (Change.Key key)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byKey
parameter_list|(
name|Change
operator|.
name|Key
name|key
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|byKeyPrefix
argument_list|(
name|key
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|byKeyPrefix (String prefix)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byKeyPrefix
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
operator|new
name|ChangeIdPredicate
argument_list|(
name|prefix
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byLegacyChangeId (Change.Id id)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byLegacyChangeId
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
operator|new
name|LegacyChangeIdPredicate
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byLegacyChangeIds (Collection<Change.Id> ids)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byLegacyChangeIds
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
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|preds
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|preds
operator|.
name|add
argument_list|(
operator|new
name|LegacyChangeIdPredicate
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|query
argument_list|(
name|or
argument_list|(
name|preds
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byBranchKey (Branch.NameKey branch, Change.Key key)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byBranchKey
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Change
operator|.
name|Key
name|key
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|ref
argument_list|(
name|branch
argument_list|)
argument_list|,
name|project
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
name|change
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byProject (Project.NameKey project)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|project
argument_list|(
name|project
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byBranchOpen (Branch.NameKey branch)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byBranchOpen
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|ref
argument_list|(
name|branch
argument_list|)
argument_list|,
name|project
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
name|open
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byBranchNew (Branch.NameKey branch)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byBranchNew
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|ref
argument_list|(
name|branch
argument_list|)
argument_list|,
name|project
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
name|status
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byCommitsOnBranchNotMerged ( Repository repo, ReviewDb db, Branch.NameKey branch, Collection<String> hashes)
specifier|public
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|byCommitsOnBranchNotMerged
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
return|return
name|byCommitsOnBranchNotMerged
argument_list|(
name|repo
argument_list|,
name|db
argument_list|,
name|branch
argument_list|,
name|hashes
argument_list|,
comment|// Account for all commit predicates plus ref, project, status.
name|indexConfig
operator|.
name|maxTerms
argument_list|()
operator|-
literal|3
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|byCommitsOnBranchNotMerged ( Repository repo, ReviewDb db, Branch.NameKey branch, Collection<String> hashes, int indexLimit)
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|byCommitsOnBranchNotMerged
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|,
name|int
name|indexLimit
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
name|hashes
operator|.
name|size
argument_list|()
operator|>
name|indexLimit
condition|)
block|{
return|return
name|byCommitsOnBranchNotMergedFromDatabase
argument_list|(
name|repo
argument_list|,
name|db
argument_list|,
name|branch
argument_list|,
name|hashes
argument_list|)
return|;
block|}
return|return
name|byCommitsOnBranchNotMergedFromIndex
argument_list|(
name|branch
argument_list|,
name|hashes
argument_list|)
return|;
block|}
DECL|method|byCommitsOnBranchNotMergedFromDatabase ( Repository repo, ReviewDb db, Branch.NameKey branch, Collection<String> hashes)
specifier|private
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|byCommitsOnBranchNotMergedFromDatabase
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ReviewDb
name|db
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|changeIds
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|hashes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|lastPrefix
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|repo
operator|.
name|getRefDatabase
argument_list|()
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
name|String
name|r
init|=
name|ref
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|lastPrefix
operator|!=
literal|null
operator|&&
name|r
operator|.
name|startsWith
argument_list|(
name|lastPrefix
argument_list|)
operator|)
operator|||
operator|!
name|hashes
operator|.
name|contains
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Change
operator|.
name|Id
name|id
init|=
name|Change
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|r
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|changeIds
operator|.
name|add
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|lastPrefix
operator|=
name|r
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|r
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|ChangeNotes
argument_list|>
name|notes
init|=
name|notesFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|changeIds
argument_list|,
name|cn
lambda|->
block|{
name|Change
name|c
init|=
name|cn
operator|.
name|getChange
argument_list|()
decl_stmt|;
return|return
name|c
operator|.
name|getDest
argument_list|()
operator|.
name|equals
argument_list|(
name|branch
argument_list|)
operator|&&
name|c
operator|.
name|getStatus
argument_list|()
operator|!=
name|Change
operator|.
name|Status
operator|.
name|MERGED
return|;
block|}
argument_list|)
decl_stmt|;
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|notes
argument_list|,
name|n
lambda|->
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|n
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byCommitsOnBranchNotMergedFromIndex ( Branch.NameKey branch, Collection<String> hashes)
specifier|private
name|Iterable
argument_list|<
name|ChangeData
argument_list|>
name|byCommitsOnBranchNotMergedFromIndex
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|ref
argument_list|(
name|branch
argument_list|)
argument_list|,
name|project
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|,
name|not
argument_list|(
name|status
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|)
argument_list|)
argument_list|,
name|or
argument_list|(
name|commits
argument_list|(
name|hashes
argument_list|)
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|commits (Collection<String> hashes)
specifier|private
specifier|static
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|commits
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|commits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|hashes
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|hashes
control|)
block|{
name|commits
operator|.
name|add
argument_list|(
name|commit
argument_list|(
name|s
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|commits
return|;
block|}
DECL|method|byProjectOpen (Project.NameKey project)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectOpen
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|project
argument_list|(
name|project
argument_list|)
argument_list|,
name|open
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byTopicOpen (String topic)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byTopicOpen
parameter_list|(
name|String
name|topic
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
operator|new
name|ExactTopicPredicate
argument_list|(
name|topic
argument_list|)
argument_list|,
name|open
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byCommit (ObjectId id)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byCommit
parameter_list|(
name|ObjectId
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|byCommit
argument_list|(
name|id
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|byCommit (String hash)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byCommit
parameter_list|(
name|String
name|hash
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|commit
argument_list|(
name|hash
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byProjectCommit (Project.NameKey project, ObjectId id)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectCommit
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ObjectId
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|byProjectCommit
argument_list|(
name|project
argument_list|,
name|id
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|byProjectCommit (Project.NameKey project, String hash)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectCommit
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|hash
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
name|project
argument_list|(
name|project
argument_list|)
argument_list|,
name|commit
argument_list|(
name|hash
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byProjectCommits (Project.NameKey project, List<String> hashes)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectCommits
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|hashes
parameter_list|)
throws|throws
name|OrmException
block|{
name|int
name|n
init|=
name|indexConfig
operator|.
name|maxTerms
argument_list|()
operator|-
literal|1
decl_stmt|;
name|checkArgument
argument_list|(
name|hashes
operator|.
name|size
argument_list|()
operator|<=
name|n
argument_list|,
literal|"cannot exceed %s commits"
argument_list|,
name|n
argument_list|)
expr_stmt|;
return|return
name|query
argument_list|(
name|and
argument_list|(
name|project
argument_list|(
name|project
argument_list|)
argument_list|,
name|or
argument_list|(
name|commits
argument_list|(
name|hashes
argument_list|)
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byBranchCommit (String project, String branch, String hash)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byBranchCommit
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|branch
parameter_list|,
name|String
name|hash
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|query
argument_list|(
name|and
argument_list|(
operator|new
name|ProjectPredicate
argument_list|(
name|project
argument_list|)
argument_list|,
operator|new
name|RefPredicate
argument_list|(
name|branch
argument_list|)
argument_list|,
name|commit
argument_list|(
name|hash
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byBranchCommit (Branch.NameKey branch, String hash)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byBranchCommit
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|String
name|hash
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|byBranchCommit
argument_list|(
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|branch
operator|.
name|get
argument_list|()
argument_list|,
name|hash
argument_list|)
return|;
block|}
DECL|method|bySubmissionId (String cs)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|bySubmissionId
parameter_list|(
name|String
name|cs
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|cs
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
return|return
name|query
argument_list|(
operator|new
name|SubmissionIdPredicate
argument_list|(
name|cs
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byProjectGroups (Project.NameKey project, Collection<String> groups)
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|byProjectGroups
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|groups
parameter_list|)
throws|throws
name|OrmException
block|{
name|List
argument_list|<
name|GroupPredicate
argument_list|>
name|groupPredicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|groups
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|g
range|:
name|groups
control|)
block|{
name|groupPredicates
operator|.
name|add
argument_list|(
operator|new
name|GroupPredicate
argument_list|(
name|g
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|query
argument_list|(
name|and
argument_list|(
name|project
argument_list|(
name|project
argument_list|)
argument_list|,
name|or
argument_list|(
name|groupPredicates
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit
