begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|checkState
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
name|index
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
name|server
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
name|server
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

begin_class
DECL|class|QueryProcessor
specifier|public
class|class
name|QueryProcessor
block|{
DECL|field|queryBuilder
specifier|private
specifier|final
name|ChangeQueryBuilder
name|queryBuilder
decl_stmt|;
DECL|field|queryRewriter
specifier|private
specifier|final
name|ChangeQueryRewriter
name|queryRewriter
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|limitFromCaller
specifier|private
name|int
name|limitFromCaller
decl_stmt|;
DECL|field|start
specifier|private
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
annotation|@
name|Inject
DECL|method|QueryProcessor (ChangeQueryBuilder queryBuilder, Provider<CurrentUser> userProvider, ChangeQueryRewriter queryRewriter)
name|QueryProcessor
parameter_list|(
name|ChangeQueryBuilder
name|queryBuilder
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|ChangeQueryRewriter
name|queryRewriter
parameter_list|)
block|{
name|this
operator|.
name|queryBuilder
operator|=
name|queryBuilder
expr_stmt|;
name|this
operator|.
name|queryRewriter
operator|=
name|queryRewriter
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
block|}
DECL|method|getQueryBuilder ()
specifier|public
name|ChangeQueryBuilder
name|getQueryBuilder
parameter_list|()
block|{
return|return
name|queryBuilder
return|;
block|}
DECL|method|enforceVisibility (boolean enforce)
specifier|public
name|QueryProcessor
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
DECL|method|setStart (int n)
specifier|public
name|QueryProcessor
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
comment|/**    * Query for changes that match the query string.    *    * @see #queryChanges(List)    * @param queryString the query string to parse.    * @return results of the query.    */
DECL|method|queryByString (String queryString)
specifier|public
name|QueryResult
name|queryByString
parameter_list|(
name|String
name|queryString
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
return|return
name|queryByStrings
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|queryString
argument_list|)
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * Perform multiple queries over a list of query strings.    *    * @see #queryChanges(List)    * @param queryStrings the query strings to parse.    * @return results of the queries, one list per input query.    */
DECL|method|queryByStrings (List<String> queryStrings)
specifier|public
name|List
argument_list|<
name|QueryResult
argument_list|>
name|queryByStrings
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|queryStrings
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|queries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|queryStrings
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|qs
range|:
name|queryStrings
control|)
block|{
name|queries
operator|.
name|add
argument_list|(
name|queryBuilder
operator|.
name|parse
argument_list|(
name|qs
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|queryChanges
argument_list|(
name|queries
argument_list|)
return|;
block|}
comment|/**    * Query for changes that match a structured query.    *    * @see #queryChanges(List)    * @param query the query.    * @return results of the query.    */
DECL|method|queryChanges (Predicate<ChangeData> query)
specifier|public
name|QueryResult
name|queryChanges
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|query
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
return|return
name|queryChanges
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
comment|/*    * Perform multiple queries over a list of query strings.    *<p>    * If a limit was specified using {@link #setLimit(int)} this method may    * return up to {@code limit + 1} results, allowing the caller to determine if    * there are more than {@code limit} matches and suggest to its own caller    * that the query could be retried with {@link #setStart(int)}.    *    * @param queries the queries.    * @return results of the queries, one list per input query.    */
DECL|method|queryChanges (List<Predicate<ChangeData>> queries)
specifier|public
name|List
argument_list|<
name|QueryResult
argument_list|>
name|queryChanges
parameter_list|(
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|queries
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
return|return
name|queryChanges
argument_list|(
literal|null
argument_list|,
name|queries
argument_list|)
return|;
block|}
static|static
block|{
comment|// In addition to this assumption, this queryChanges assumes the basic
comment|// rewrites do not touch visibleto predicates either.
name|checkState
argument_list|(
operator|!
name|IsVisibleToPredicate
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|IndexPredicate
operator|.
name|class
argument_list|)
argument_list|,
literal|"QueryProcessor assumes visibleto is not used by the index rewriter."
argument_list|)
expr_stmt|;
block|}
DECL|method|queryChanges (List<String> queryStrings, List<Predicate<ChangeData>> queries)
specifier|private
name|List
argument_list|<
name|QueryResult
argument_list|>
name|queryChanges
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
name|ChangeData
argument_list|>
argument_list|>
name|queries
parameter_list|)
throws|throws
name|OrmException
throws|,
name|QueryParseException
block|{
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|visibleToMe
init|=
name|enforceVisibility
condition|?
name|queryBuilder
operator|.
name|is_visible
argument_list|()
else|:
literal|null
decl_stmt|;
name|int
name|cnt
init|=
name|queries
operator|.
name|size
argument_list|()
decl_stmt|;
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
name|ChangeData
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
name|ChangeDataSource
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
name|ChangeData
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
comment|// Always bump limit by 1, even if this results in exceeding the permitted
comment|// max for this user. The only way to see if there are more changes is to
comment|// ask for one more result from the query.
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|s
init|=
name|queryRewriter
operator|.
name|rewrite
argument_list|(
name|q
argument_list|,
name|start
argument_list|,
name|limit
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
name|q
operator|=
name|Predicate
operator|.
name|and
argument_list|(
name|queryBuilder
operator|.
name|status_open
argument_list|()
argument_list|,
name|q
argument_list|)
expr_stmt|;
name|s
operator|=
name|queryRewriter
operator|.
name|rewrite
argument_list|(
name|q
argument_list|,
name|start
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|s
operator|instanceof
name|ChangeDataSource
operator|)
condition|)
block|{
throw|throw
operator|new
name|QueryParseException
argument_list|(
literal|"invalid query: "
operator|+
name|s
argument_list|)
throw|;
block|}
if|if
condition|(
name|enforceVisibility
condition|)
block|{
name|s
operator|=
operator|new
name|AndSource
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|s
argument_list|,
name|visibleToMe
argument_list|)
argument_list|,
name|start
argument_list|)
expr_stmt|;
block|}
name|predicates
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|sources
operator|.
name|add
argument_list|(
operator|(
name|ChangeDataSource
operator|)
name|s
argument_list|)
expr_stmt|;
block|}
comment|// Run each query asynchronously, if supported.
name|List
argument_list|<
name|ResultSet
argument_list|<
name|ChangeData
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
name|ChangeDataSource
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
return|return
name|out
return|;
block|}
DECL|method|isDisabled ()
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
name|userProvider
operator|.
name|get
argument_list|()
operator|.
name|getCapabilities
argument_list|()
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
DECL|method|getEffectiveLimit (Predicate<ChangeData> p)
specifier|private
name|int
name|getEffectiveLimit
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
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
literal|3
argument_list|)
decl_stmt|;
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
name|Integer
name|limitFromPredicate
init|=
name|LimitPredicate
operator|.
name|getLimit
argument_list|(
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
return|return
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|min
argument_list|(
name|possibleLimits
argument_list|)
return|;
block|}
block|}
end_class

end_unit

