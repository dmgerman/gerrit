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
name|List
import|;
end_import

begin_comment
comment|/**  * A search source builder allowing to easily build search source.  *  *<p>A trimmed down and modified version of org.elasticsearch.search.builder.SearchSourceBuilder.  */
end_comment

begin_class
DECL|class|SearchSourceBuilder
specifier|public
class|class
name|SearchSourceBuilder
block|{
DECL|field|querySourceBuilder
specifier|private
name|QuerySourceBuilder
name|querySourceBuilder
decl_stmt|;
DECL|field|from
specifier|private
name|int
name|from
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|fieldNames
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|fieldNames
decl_stmt|;
comment|/** Constructs a new search source builder. */
DECL|method|SearchSourceBuilder ()
specifier|public
name|SearchSourceBuilder
parameter_list|()
block|{}
comment|/** Constructs a new search source builder with a search query. */
DECL|method|query (QueryBuilder query)
specifier|public
name|SearchSourceBuilder
name|query
parameter_list|(
name|QueryBuilder
name|query
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|querySourceBuilder
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|querySourceBuilder
operator|=
operator|new
name|QuerySourceBuilder
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/** From index to start the search from. Defaults to<tt>0</tt>. */
DECL|method|from (int from)
specifier|public
name|SearchSourceBuilder
name|from
parameter_list|(
name|int
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** The number of search hits to return. Defaults to<tt>10</tt>. */
DECL|method|size (int size)
specifier|public
name|SearchSourceBuilder
name|size
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets the fields to load and return as part of the search request. If none are specified, the    * source of the document will be returned.    */
DECL|method|fields (List<String> fields)
specifier|public
name|SearchSourceBuilder
name|fields
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fieldNames
operator|=
name|fields
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
specifier|final
name|String
name|toString
parameter_list|()
block|{
try|try
block|{
name|XContentBuilder
name|builder
init|=
operator|new
name|XContentBuilder
argument_list|()
decl_stmt|;
name|toXContent
argument_list|(
name|builder
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|string
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
return|return
literal|""
return|;
block|}
block|}
DECL|method|toXContent (XContentBuilder builder)
specifier|private
name|void
name|toXContent
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
argument_list|()
expr_stmt|;
name|innerToXContent
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|builder
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
DECL|method|innerToXContent (XContentBuilder builder)
specifier|private
name|void
name|innerToXContent
parameter_list|(
name|XContentBuilder
name|builder
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|from
operator|!=
operator|-
literal|1
condition|)
block|{
name|builder
operator|.
name|field
argument_list|(
literal|"from"
argument_list|,
name|from
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|size
operator|!=
operator|-
literal|1
condition|)
block|{
name|builder
operator|.
name|field
argument_list|(
literal|"size"
argument_list|,
name|size
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|querySourceBuilder
operator|!=
literal|null
condition|)
block|{
name|querySourceBuilder
operator|.
name|innerToXContent
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fieldNames
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|fieldNames
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
literal|"fields"
argument_list|,
name|fieldNames
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|startArray
argument_list|(
literal|"fields"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|fieldName
range|:
name|fieldNames
control|)
block|{
name|builder
operator|.
name|value
argument_list|(
name|fieldName
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
block|}
end_class

end_unit

