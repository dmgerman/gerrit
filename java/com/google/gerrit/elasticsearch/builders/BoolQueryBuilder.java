begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project, 2009-2015 Elasticsearch
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
DECL|package|com.google.gerrit.elasticsearch.builders
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|builders
package|;
end_package

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
name|List
import|;
end_import

begin_comment
comment|/**  * A Query that matches documents matching boolean combinations of other queries. A trimmed down  * version of org.elasticsearch.index.query.BoolQueryBuilder for this very package.  */
end_comment

begin_class
DECL|class|BoolQueryBuilder
specifier|public
class|class
name|BoolQueryBuilder
extends|extends
name|QueryBuilder
block|{
DECL|field|mustClauses
specifier|private
specifier|final
name|List
argument_list|<
name|QueryBuilder
argument_list|>
name|mustClauses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|mustNotClauses
specifier|private
specifier|final
name|List
argument_list|<
name|QueryBuilder
argument_list|>
name|mustNotClauses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|filterClauses
specifier|private
specifier|final
name|List
argument_list|<
name|QueryBuilder
argument_list|>
name|filterClauses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|shouldClauses
specifier|private
specifier|final
name|List
argument_list|<
name|QueryBuilder
argument_list|>
name|shouldClauses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**    * Adds a query that<b>must</b> appear in the matching documents and will contribute to scoring.    */
DECL|method|must (QueryBuilder queryBuilder)
specifier|public
name|BoolQueryBuilder
name|must
parameter_list|(
name|QueryBuilder
name|queryBuilder
parameter_list|)
block|{
name|mustClauses
operator|.
name|add
argument_list|(
name|queryBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Adds a query that<b>must not</b> appear in the matching documents and will not contribute to    * scoring.    */
DECL|method|mustNot (QueryBuilder queryBuilder)
specifier|public
name|BoolQueryBuilder
name|mustNot
parameter_list|(
name|QueryBuilder
name|queryBuilder
parameter_list|)
block|{
name|mustNotClauses
operator|.
name|add
argument_list|(
name|queryBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Adds a query that<i>should</i> appear in the matching documents. For a boolean query with no    *<tt>MUST</tt> clauses one or more<code>SHOULD</code> clauses must match a document for the    * BooleanQuery to match.    */
DECL|method|should (QueryBuilder queryBuilder)
specifier|public
name|BoolQueryBuilder
name|should
parameter_list|(
name|QueryBuilder
name|queryBuilder
parameter_list|)
block|{
name|shouldClauses
operator|.
name|add
argument_list|(
name|queryBuilder
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|doXContent (XContentBuilder builder)
specifier|protected
name|void
name|doXContent
parameter_list|(
name|XContentBuilder
name|builder
parameter_list|)
throws|throws
name|IOException
block|{
name|builder
operator|.
name|startObject
argument_list|(
literal|"bool"
argument_list|)
expr_stmt|;
name|doXArrayContent
argument_list|(
literal|"must"
argument_list|,
name|mustClauses
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|doXArrayContent
argument_list|(
literal|"filter"
argument_list|,
name|filterClauses
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|doXArrayContent
argument_list|(
literal|"must_not"
argument_list|,
name|mustNotClauses
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|doXArrayContent
argument_list|(
literal|"should"
argument_list|,
name|shouldClauses
argument_list|,
name|builder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
DECL|method|doXArrayContent (String field, List<QueryBuilder> clauses, XContentBuilder builder)
specifier|private
name|void
name|doXArrayContent
parameter_list|(
name|String
name|field
parameter_list|,
name|List
argument_list|<
name|QueryBuilder
argument_list|>
name|clauses
parameter_list|,
name|XContentBuilder
name|builder
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|clauses
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|clauses
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|builder
operator|.
name|field
argument_list|(
name|field
argument_list|)
expr_stmt|;
name|clauses
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toXContent
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|startArray
argument_list|(
name|field
argument_list|)
expr_stmt|;
for|for
control|(
name|QueryBuilder
name|clause
range|:
name|clauses
control|)
block|{
name|clause
operator|.
name|toXContent
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|endArray
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

