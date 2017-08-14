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
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|Throwables
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
name|Ordering
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
name|common
operator|.
name|Nullable
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
name|common
operator|.
name|data
operator|.
name|GlobalCapability
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
name|IndexRewriter
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
name|SchemaDefinitions
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
name|LimitPredicate
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
name|index
operator|.
name|query
operator|.
name|QueryResult
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
name|metrics
operator|.
name|Description
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
name|metrics
operator|.
name|Field
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
name|metrics
operator|.
name|MetricMaker
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
name|metrics
operator|.
name|Timer1
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
name|CurrentUser
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
name|account
operator|.
name|AccountLimits
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
name|OrmRuntimeException
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|IntStream
import|;
end_import

begin_class
DECL|class|QueryProcessor
specifier|public
specifier|abstract
class|class
name|QueryProcessor
parameter_list|<
name|T
parameter_list|>
block|{
annotation|@
name|Singleton
DECL|class|Metrics
specifier|protected
specifier|static
class|class
name|Metrics
block|{
DECL|field|executionTime
specifier|final
name|Timer1
argument_list|<
name|String
argument_list|>
name|executionTime
decl_stmt|;
annotation|@
name|Inject
DECL|method|Metrics (MetricMaker metricMaker)
name|Metrics
parameter_list|(
name|MetricMaker
name|metricMaker
parameter_list|)
block|{
name|Field
argument_list|<
name|String
argument_list|>
name|index
init|=
name|Field
operator|.
name|ofString
argument_list|(
literal|"index"
argument_list|,
literal|"index name"
argument_list|)
decl_stmt|;
name|executionTime
operator|=
name|metricMaker
operator|.
name|newTimer
argument_list|(
literal|"query/query_latency"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Successful query latency, accumulated over the life of the process"
argument_list|)
operator|.
name|setCumulative
argument_list|()
operator|.
name|setUnit
argument_list|(
name|Description
operator|.
name|Units
operator|.
name|MILLISECONDS
argument_list|)
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|userProvider
specifier|protected
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|limitsFactory
specifier|private
specifier|final
name|AccountLimits
operator|.
name|Factory
name|limitsFactory
decl_stmt|;
DECL|field|metrics
specifier|private
specifier|final
name|Metrics
name|metrics
decl_stmt|;
DECL|field|schemaDef
specifier|private
specifier|final
name|SchemaDefinitions
argument_list|<
name|T
argument_list|>
name|schemaDef
decl_stmt|;
DECL|field|indexConfig
specifier|private
specifier|final
name|IndexConfig
name|indexConfig
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
DECL|field|rewriter
specifier|private
specifier|final
name|IndexRewriter
argument_list|<
name|T
argument_list|>
name|rewriter
decl_stmt|;
DECL|field|limitField
specifier|private
specifier|final
name|String
name|limitField
decl_stmt|;
DECL|field|start
specifier|protected
name|int
name|start
decl_stmt|;
DECL|field|enforceVisibility
specifier|private
name|boolean
name|enforceVisibility
init|=
literal|true
decl_stmt|;
DECL|field|limitFromCaller
specifier|private
name|int
name|limitFromCaller
decl_stmt|;
DECL|field|requestedFields
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|requestedFields
decl_stmt|;
DECL|method|QueryProcessor ( Provider<CurrentUser> userProvider, AccountLimits.Factory limitsFactory, Metrics metrics, SchemaDefinitions<T> schemaDef, IndexConfig indexConfig, IndexCollection<?, T, ? extends Index<?, T>> indexes, IndexRewriter<T> rewriter, String limitField)
specifier|protected
name|QueryProcessor
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|AccountLimits
operator|.
name|Factory
name|limitsFactory
parameter_list|,
name|Metrics
name|metrics
parameter_list|,
name|SchemaDefinitions
argument_list|<
name|T
argument_list|>
name|schemaDef
parameter_list|,
name|IndexConfig
name|indexConfig
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
name|IndexRewriter
argument_list|<
name|T
argument_list|>
name|rewriter
parameter_list|,
name|String
name|limitField
parameter_list|)
block|{
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|limitsFactory
operator|=
name|limitsFactory
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|this
operator|.
name|schemaDef
operator|=
name|schemaDef
expr_stmt|;
name|this
operator|.
name|indexConfig
operator|=
name|indexConfig
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|rewriter
operator|=
name|rewriter
expr_stmt|;
name|this
operator|.
name|limitField
operator|=
name|limitField
expr_stmt|;
block|}
DECL|method|setStart (int n)
specifier|public
name|QueryProcessor
argument_list|<
name|T
argument_list|>
name|setStart
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|start
operator|=
name|n
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Specify whether to enforce visibility by filtering out results that are not visible to the    * user.    *    *<p>Enforcing visibility may have performance consequences, as the index system may need to    * post-filter a large number of results to fill even a modest limit.    *    *<p>If visibility is enforced, the user's {@code queryLimit} global capability is also used to    * bound the total number of results. If this capability is non-positive, this results in the    * entire query processor being {@link #isDisabled() disabled}.    *    * @param enforce whether to enforce visibility.    * @return this.    */
DECL|method|enforceVisibility (boolean enforce)
specifier|public
name|QueryProcessor
argument_list|<
name|T
argument_list|>
name|enforceVisibility
parameter_list|(
name|boolean
name|enforce
parameter_list|)
block|{
name|enforceVisibility
operator|=
name|enforce
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setLimit (int n)
specifier|public
name|QueryProcessor
argument_list|<
name|T
argument_list|>
name|setLimit
parameter_list|(
name|int
name|n
parameter_list|)
block|{
name|limitFromCaller
operator|=
name|n
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setRequestedFields (Set<String> fields)
specifier|public
name|QueryProcessor
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
name|requestedFields
operator|=
name|fields
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Query for entities that match a structured query.    *    * @see #query(List)    * @param query the query.    * @return results of the query.    */
DECL|method|query (Predicate<T> query)
specifier|public
name|QueryResult
argument_list|<
name|T
argument_list|>
name|query
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|query
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
return|return
name|query
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|query
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * Perform multiple queries in parallel.    *    *<p>If querying is disabled, short-circuits the index and returns empty results. Callers that    * wish to distinguish this case from a query returning no results from the index may call {@link    * #isDisabled()} themselves.    *    * @param queries list of queries.    * @return results of the queries, one QueryResult per input query, in the same order as the    *     input.    */
DECL|method|query (List<Predicate<T>> queries)
specifier|public
name|List
argument_list|<
name|QueryResult
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
throws|,
name|QueryParseException
block|{
try|try
block|{
return|return
name|query
argument_list|(
literal|null
argument_list|,
name|queries
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmRuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|QueryParseException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|query ( @ullable List<String> queryStrings, List<Predicate<T>> queries)
specifier|private
name|List
argument_list|<
name|QueryResult
argument_list|<
name|T
argument_list|>
argument_list|>
name|query
parameter_list|(
annotation|@
name|Nullable
name|List
argument_list|<
name|String
argument_list|>
name|queryStrings
parameter_list|,
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
throws|,
name|QueryParseException
block|{
name|long
name|startNanos
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
name|int
name|cnt
init|=
name|queries
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|queryStrings
operator|!=
literal|null
condition|)
block|{
name|int
name|qs
init|=
name|queryStrings
operator|.
name|size
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|qs
operator|==
name|cnt
argument_list|,
literal|"got %s query strings but %s predicates"
argument_list|,
name|qs
argument_list|,
name|cnt
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cnt
operator|==
literal|0
condition|)
block|{
return|return
name|ImmutableList
operator|.
name|of
argument_list|()
return|;
block|}
if|if
condition|(
name|isDisabled
argument_list|()
condition|)
block|{
return|return
name|disabledResults
argument_list|(
name|queryStrings
argument_list|,
name|queries
argument_list|)
return|;
block|}
comment|// Parse and rewrite all queries.
name|List
argument_list|<
name|Integer
argument_list|>
name|limits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|predicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DataSource
argument_list|<
name|T
argument_list|>
argument_list|>
name|sources
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
for|for
control|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|q
range|:
name|queries
control|)
block|{
name|int
name|limit
init|=
name|getEffectiveLimit
argument_list|(
name|q
argument_list|)
decl_stmt|;
name|limits
operator|.
name|add
argument_list|(
name|limit
argument_list|)
expr_stmt|;
if|if
condition|(
name|limit
operator|==
name|getBackendSupportedLimit
argument_list|()
condition|)
block|{
name|limit
operator|--
expr_stmt|;
block|}
name|int
name|page
init|=
operator|(
name|start
operator|/
name|limit
operator|)
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|page
operator|>
name|indexConfig
operator|.
name|maxPages
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"Cannot go beyond page "
operator|+
name|indexConfig
operator|.
name|maxPages
argument_list|()
operator|+
literal|" of results"
argument_list|)
throw|;
block|}
comment|// Always bump limit by 1, even if this results in exceeding the permitted
comment|// max for this user. The only way to see if there are more entities is to
comment|// ask for one more result from the query.
name|QueryOptions
name|opts
init|=
name|createOptions
argument_list|(
name|indexConfig
argument_list|,
name|start
argument_list|,
name|limit
operator|+
literal|1
argument_list|,
name|getRequestedFields
argument_list|()
argument_list|)
decl_stmt|;
name|Predicate
argument_list|<
name|T
argument_list|>
name|pred
init|=
name|rewriter
operator|.
name|rewrite
argument_list|(
name|q
argument_list|,
name|opts
argument_list|)
decl_stmt|;
if|if
condition|(
name|enforceVisibility
condition|)
block|{
name|pred
operator|=
name|enforceVisibility
argument_list|(
name|pred
argument_list|)
expr_stmt|;
block|}
name|predicates
operator|.
name|add
argument_list|(
name|pred
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|DataSource
argument_list|<
name|T
argument_list|>
name|s
init|=
operator|(
name|DataSource
argument_list|<
name|T
argument_list|>
operator|)
name|pred
decl_stmt|;
name|sources
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
comment|// Run each query asynchronously, if supported.
name|List
argument_list|<
name|ResultSet
argument_list|<
name|T
argument_list|>
argument_list|>
name|matches
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
for|for
control|(
name|DataSource
argument_list|<
name|T
argument_list|>
name|s
range|:
name|sources
control|)
block|{
name|matches
operator|.
name|add
argument_list|(
name|s
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|QueryResult
argument_list|<
name|T
argument_list|>
argument_list|>
name|out
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|cnt
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cnt
condition|;
name|i
operator|++
control|)
block|{
name|out
operator|.
name|add
argument_list|(
name|QueryResult
operator|.
name|create
argument_list|(
name|queryStrings
operator|!=
literal|null
condition|?
name|queryStrings
operator|.
name|get
argument_list|(
name|i
argument_list|)
else|:
literal|null
argument_list|,
name|predicates
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|limits
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|matches
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Only measure successful queries that actually touched the index.
name|metrics
operator|.
name|executionTime
operator|.
name|record
argument_list|(
name|schemaDef
operator|.
name|getName
argument_list|()
argument_list|,
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startNanos
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
DECL|method|disabledResults ( List<String> queryStrings, List<Predicate<T>> queries)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ImmutableList
argument_list|<
name|QueryResult
argument_list|<
name|T
argument_list|>
argument_list|>
name|disabledResults
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|queryStrings
parameter_list|,
name|List
argument_list|<
name|Predicate
argument_list|<
name|T
argument_list|>
argument_list|>
name|queries
parameter_list|)
block|{
return|return
name|IntStream
operator|.
name|range
argument_list|(
literal|0
argument_list|,
name|queries
operator|.
name|size
argument_list|()
argument_list|)
operator|.
name|mapToObj
argument_list|(
name|i
lambda|->
name|QueryResult
operator|.
name|create
argument_list|(
name|queryStrings
operator|!=
literal|null
condition|?
name|queryStrings
operator|.
name|get
argument_list|(
name|i
argument_list|)
else|:
literal|null
argument_list|,
name|queries
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
literal|0
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createOptions ( IndexConfig indexConfig, int start, int limit, Set<String> requestedFields)
specifier|protected
name|QueryOptions
name|createOptions
parameter_list|(
name|IndexConfig
name|indexConfig
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
name|requestedFields
parameter_list|)
block|{
return|return
name|QueryOptions
operator|.
name|create
argument_list|(
name|indexConfig
argument_list|,
name|start
argument_list|,
name|limit
argument_list|,
name|requestedFields
argument_list|)
return|;
block|}
comment|/**    * Invoked after the query was rewritten. Subclasses must overwrite this method to filter out    * results that are not visible to the calling user.    *    * @param pred the query    * @return the modified query    */
DECL|method|enforceVisibility (Predicate<T> pred)
specifier|protected
specifier|abstract
name|Predicate
argument_list|<
name|T
argument_list|>
name|enforceVisibility
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|pred
parameter_list|)
function_decl|;
DECL|method|getRequestedFields ()
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getRequestedFields
parameter_list|()
block|{
if|if
condition|(
name|requestedFields
operator|!=
literal|null
condition|)
block|{
return|return
name|requestedFields
return|;
block|}
name|Index
argument_list|<
name|?
argument_list|,
name|T
argument_list|>
name|index
init|=
name|indexes
operator|.
name|getSearchIndex
argument_list|()
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
operator|.
name|getStoredFields
argument_list|()
operator|.
name|keySet
argument_list|()
else|:
name|ImmutableSet
operator|.
expr|<
name|String
operator|>
name|of
argument_list|()
return|;
block|}
comment|/**    * Check whether querying should be disabled.    *    *<p>Currently, the only condition that can disable the whole query processor is if both {@link    * #enforceVisibility(boolean) visibility is enforced} and the user has a non-positive maximum    * value for the {@code queryLimit} capability.    *    *<p>If querying is disabled, all calls to {@link #query(Predicate)} and {@link #query(List)}    * will return empty results. This method can be used if callers wish to distinguish this case    * from a query returning no results from the index.    *    * @return true if querying should be disabled.    */
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|getPermittedLimit
argument_list|()
operator|<=
literal|0
return|;
block|}
DECL|method|getPermittedLimit ()
specifier|private
name|int
name|getPermittedLimit
parameter_list|()
block|{
if|if
condition|(
name|enforceVisibility
condition|)
block|{
return|return
name|limitsFactory
operator|.
name|create
argument_list|(
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getRange
argument_list|(
name|GlobalCapability
operator|.
name|QUERY_LIMIT
argument_list|)
operator|.
name|getMax
argument_list|()
return|;
block|}
return|return
name|Integer
operator|.
name|MAX_VALUE
return|;
block|}
DECL|method|getBackendSupportedLimit ()
specifier|private
name|int
name|getBackendSupportedLimit
parameter_list|()
block|{
return|return
name|indexConfig
operator|.
name|maxLimit
argument_list|()
return|;
block|}
DECL|method|getEffectiveLimit (Predicate<T> p)
specifier|private
name|int
name|getEffectiveLimit
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|p
parameter_list|)
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|possibleLimits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|possibleLimits
operator|.
name|add
argument_list|(
name|getBackendSupportedLimit
argument_list|()
argument_list|)
expr_stmt|;
name|possibleLimits
operator|.
name|add
argument_list|(
name|getPermittedLimit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|limitFromCaller
operator|>
literal|0
condition|)
block|{
name|possibleLimits
operator|.
name|add
argument_list|(
name|limitFromCaller
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|limitField
operator|!=
literal|null
condition|)
block|{
name|Integer
name|limitFromPredicate
init|=
name|LimitPredicate
operator|.
name|getLimit
argument_list|(
name|limitField
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|limitFromPredicate
operator|!=
literal|null
condition|)
block|{
name|possibleLimits
operator|.
name|add
argument_list|(
name|limitFromPredicate
argument_list|)
expr_stmt|;
block|}
block|}
name|int
name|result
init|=
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|possibleLimits
argument_list|)
decl_stmt|;
comment|// Should have short-circuited from #query or thrown some other exception before getting here.
name|checkState
argument_list|(
name|result
operator|>
literal|0
argument_list|,
literal|"effective limit should be positive"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

