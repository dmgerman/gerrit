begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query
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
name|Lists
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
name|Index
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
name|IndexCollection
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
name|server
operator|.
name|index
operator|.
name|Schema
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
comment|/**  * Execute a single query over a secondary index, for use by Gerrit internals.  *<p>  * By default, visibility of returned entities is not enforced (unlike in {@link  * QueryProcessor}). The methods in this class are not typically used by  * user-facing paths, but rather by internal callers that need to process all  * matching results.  */
end_comment

begin_class
DECL|class|InternalQuery
specifier|public
class|class
name|InternalQuery
parameter_list|<
name|T
parameter_list|>
block|{
DECL|field|queryProcessor
specifier|private
specifier|final
name|QueryProcessor
argument_list|<
name|T
argument_list|>
name|queryProcessor
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|IndexCollection
argument_list|<
name|?
argument_list|,
name|T
argument_list|,
name|?
extends|extends
name|Index
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
argument_list|>
name|indexes
decl_stmt|;
DECL|field|indexConfig
specifier|protected
specifier|final
name|IndexConfig
name|indexConfig
decl_stmt|;
DECL|method|InternalQuery (QueryProcessor<T> queryProcessor, IndexCollection<?, T, ? extends Index<?, T>> indexes, IndexConfig indexConfig)
specifier|protected
name|InternalQuery
parameter_list|(
name|QueryProcessor
argument_list|<
name|T
argument_list|>
name|queryProcessor
parameter_list|,
name|IndexCollection
argument_list|<
name|?
argument_list|,
name|T
argument_list|,
name|?
extends|extends
name|Index
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
argument_list|>
name|indexes
parameter_list|,
name|IndexConfig
name|indexConfig
parameter_list|)
block|{
name|this
operator|.
name|queryProcessor
operator|=
name|queryProcessor
operator|.
name|enforceVisibility
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|indexConfig
operator|=
name|indexConfig
expr_stmt|;
block|}
DECL|method|setLimit (int n)
specifier|public
name|InternalQuery
argument_list|<
name|T
argument_list|>
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|queryProcessor
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
DECL|method|enforceVisibility (boolean enforce)
specifier|public
name|InternalQuery
argument_list|<
name|T
argument_list|>
name|enforceVisibility
parameter_list|(
name|boolean
name|enforce
parameter_list|)
block|{
name|queryProcessor
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
DECL|method|setRequestedFields (Set<String> fields)
specifier|public
name|InternalQuery
argument_list|<
name|T
argument_list|>
name|setRequestedFields
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
name|queryProcessor
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
DECL|method|noFields ()
specifier|public
name|InternalQuery
argument_list|<
name|T
argument_list|>
name|noFields
parameter_list|()
block|{
name|queryProcessor
operator|.
name|setRequestedFields
argument_list|(
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|query (Predicate<T> p)
specifier|public
name|List
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|p
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|queryProcessor
operator|.
name|query
argument_list|(
name|p
argument_list|)
operator|.
name|entities
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Run multiple queries in parallel.    *<p>    * If a limit was specified using {@link #setLimit(int)}, that limit is    * applied to each query independently.    *    * @param queries list of queries.    * @return results of the queries, one list of results per input query, in the    *     same order as the input.    */
DECL|method|query (List<Predicate<T>> queries)
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|T
argument_list|>
argument_list|>
name|query
parameter_list|(
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|queries
parameter_list|)
throws|throws
name|OrmException
block|{
try|try
block|{
return|return
name|Lists
operator|.
name|transform
argument_list|(
name|queryProcessor
operator|.
name|query
argument_list|(
name|queries
argument_list|)
argument_list|,
name|QueryResult
operator|::
name|entities
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|schema ()
specifier|protected
name|Schema
argument_list|<
name|T
argument_list|>
name|schema
parameter_list|()
block|{
name|Index
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
name|index
init|=
name|indexes
operator|!=
literal|null
condition|?
name|indexes
operator|.
name|getSearchIndex
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|index
operator|!=
literal|null
condition|?
name|index
operator|.
name|getSchema
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

