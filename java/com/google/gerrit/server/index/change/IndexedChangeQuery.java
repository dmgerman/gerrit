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
DECL|package|com.google.gerrit.server.index.change
package|package
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
import|import static
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
name|ChangeField
operator|.
name|CHANGE
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
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|PROJECT
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
name|ImmutableSet
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
name|IndexedQuery
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
name|QueryOptions
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
name|DataSource
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
name|IndexPredicate
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
name|Matchable
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
name|index
operator|.
name|query
operator|.
name|QueryParseException
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
name|ChangeDataSource
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
name|Iterator
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
comment|/**  * Wrapper combining an {@link IndexPredicate} together with a {@link ChangeDataSource} that returns  * matching results from the index.  *  *<p>Appropriate to return as the rootmost predicate that can be processed using the secondary  * index; such predicates must also implement {@link ChangeDataSource} to be chosen by the query  * processor.  */
end_comment

begin_class
DECL|class|IndexedChangeQuery
specifier|public
class|class
name|IndexedChangeQuery
extends|extends
name|IndexedQuery
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeData
argument_list|>
implements|implements
name|ChangeDataSource
implements|,
name|Matchable
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|oneResult ()
specifier|public
specifier|static
name|QueryOptions
name|oneResult
parameter_list|()
block|{
return|return
name|createOptions
argument_list|(
name|IndexConfig
operator|.
name|createDefault
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createOptions ( IndexConfig config, int start, int limit, Set<String> fields)
specifier|public
specifier|static
name|QueryOptions
name|createOptions
parameter_list|(
name|IndexConfig
name|config
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|limit
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
comment|// Always include project since it is needed to load the change from NoteDb.
if|if
condition|(
operator|!
name|fields
operator|.
name|contains
argument_list|(
name|CHANGE
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
operator|!
name|fields
operator|.
name|contains
argument_list|(
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|fields
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|fields
argument_list|)
expr_stmt|;
name|fields
operator|.
name|add
argument_list|(
name|PROJECT
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|QueryOptions
operator|.
name|create
argument_list|(
name|config
argument_list|,
name|start
argument_list|,
name|limit
argument_list|,
name|fields
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|convertOptions (QueryOptions opts)
specifier|static
name|QueryOptions
name|convertOptions
parameter_list|(
name|QueryOptions
name|opts
parameter_list|)
block|{
name|opts
operator|=
name|opts
operator|.
name|convertForBackend
argument_list|()
expr_stmt|;
return|return
name|IndexedChangeQuery
operator|.
name|createOptions
argument_list|(
name|opts
operator|.
name|config
argument_list|()
argument_list|,
name|opts
operator|.
name|start
argument_list|()
argument_list|,
name|opts
operator|.
name|limit
argument_list|()
argument_list|,
name|opts
operator|.
name|fields
argument_list|()
argument_list|)
return|;
block|}
DECL|field|fromSource
specifier|private
specifier|final
name|Map
argument_list|<
name|ChangeData
argument_list|,
name|DataSource
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|fromSource
decl_stmt|;
DECL|method|IndexedChangeQuery (ChangeIndex index, Predicate<ChangeData> pred, QueryOptions opts)
specifier|public
name|IndexedChangeQuery
parameter_list|(
name|ChangeIndex
name|index
parameter_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|pred
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|index
argument_list|,
name|pred
argument_list|,
name|convertOptions
argument_list|(
name|opts
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|fromSource
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|read ()
specifier|public
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|read
parameter_list|()
throws|throws
name|OrmException
block|{
specifier|final
name|DataSource
argument_list|<
name|ChangeData
argument_list|>
name|currSource
init|=
name|source
decl_stmt|;
specifier|final
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
name|rs
init|=
name|currSource
operator|.
name|read
argument_list|()
decl_stmt|;
return|return
operator|new
name|ResultSet
argument_list|<
name|ChangeData
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Iterator
argument_list|<
name|ChangeData
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|Iterables
operator|.
name|transform
argument_list|(
name|rs
argument_list|,
name|cd
lambda|->
block|{
name|fromSource
operator|.
name|put
argument_list|(
name|cd
argument_list|,
name|currSource
argument_list|)
expr_stmt|;
return|return
name|cd
return|;
block|}
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ChangeData
argument_list|>
name|toList
parameter_list|()
block|{
name|List
argument_list|<
name|ChangeData
argument_list|>
name|r
init|=
name|rs
operator|.
name|toList
argument_list|()
decl_stmt|;
for|for
control|(
name|ChangeData
name|cd
range|:
name|r
control|)
block|{
name|fromSource
operator|.
name|put
argument_list|(
name|cd
argument_list|,
name|currSource
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|source
operator|!=
literal|null
operator|&&
name|fromSource
operator|.
name|get
argument_list|(
name|cd
argument_list|)
operator|==
name|source
condition|)
block|{
return|return
literal|true
return|;
block|}
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|pred
init|=
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|pred
operator|.
name|isMatchable
argument_list|()
argument_list|,
literal|"match invoked, but child predicate %s doesn't implement %s"
argument_list|,
name|pred
argument_list|,
name|Matchable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pred
operator|.
name|asMatchable
argument_list|()
operator|.
name|match
argument_list|(
name|cd
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
comment|// Index queries are assumed to be cheaper than any other type of query, so
comment|// so try to make sure they get picked. Note that pred's cost may be higher
comment|// because it doesn't know whether it's being used in an index query or not.
return|return
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|hasChange ()
specifier|public
name|boolean
name|hasChange
parameter_list|()
block|{
return|return
name|index
operator|.
name|getSchema
argument_list|()
operator|.
name|hasField
argument_list|(
name|ChangeField
operator|.
name|CHANGE
argument_list|)
return|;
block|}
block|}
end_class

end_unit
