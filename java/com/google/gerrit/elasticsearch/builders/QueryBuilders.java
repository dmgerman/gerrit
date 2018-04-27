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

begin_comment
comment|/**  * A static factory for simple "import static" usage. A trimmed down version of {@link  * org.elasticsearch.index.query.QueryBuilders} for this very package.  */
end_comment

begin_class
DECL|class|QueryBuilders
specifier|public
specifier|abstract
class|class
name|QueryBuilders
block|{
comment|/** A query that match on all documents. */
DECL|method|matchAllQuery ()
specifier|public
specifier|static
name|MatchAllQueryBuilder
name|matchAllQuery
parameter_list|()
block|{
return|return
operator|new
name|MatchAllQueryBuilder
argument_list|()
return|;
block|}
comment|/**    * Creates a text query with type "PHRASE" for the provided field name and text.    *    * @param name The field name.    * @param text The query text (to be analyzed).    */
DECL|method|matchPhraseQuery (String name, Object text)
specifier|public
specifier|static
name|MatchQueryBuilder
name|matchPhraseQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|text
parameter_list|)
block|{
return|return
operator|new
name|MatchQueryBuilder
argument_list|(
name|name
argument_list|,
name|text
argument_list|)
operator|.
name|type
argument_list|(
name|MatchQueryBuilder
operator|.
name|Type
operator|.
name|PHRASE
argument_list|)
return|;
block|}
comment|/**    * Creates a match query with type "PHRASE_PREFIX" for the provided field name and text.    *    * @param name The field name.    * @param text The query text (to be analyzed).    */
DECL|method|matchPhrasePrefixQuery (String name, Object text)
specifier|public
specifier|static
name|MatchQueryBuilder
name|matchPhrasePrefixQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|text
parameter_list|)
block|{
return|return
operator|new
name|MatchQueryBuilder
argument_list|(
name|name
argument_list|,
name|text
argument_list|)
operator|.
name|type
argument_list|(
name|MatchQueryBuilder
operator|.
name|Type
operator|.
name|PHRASE_PREFIX
argument_list|)
return|;
block|}
comment|/**    * A Query that matches documents containing a term.    *    * @param name The name of the field    * @param value The value of the term    */
DECL|method|termQuery (String name, String value)
specifier|public
specifier|static
name|TermQueryBuilder
name|termQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|TermQueryBuilder
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * A Query that matches documents containing a term.    *    * @param name The name of the field    * @param value The value of the term    */
DECL|method|termQuery (String name, int value)
specifier|public
specifier|static
name|TermQueryBuilder
name|termQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
return|return
operator|new
name|TermQueryBuilder
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
comment|/**    * A Query that matches documents within an range of terms.    *    * @param name The field name    */
DECL|method|rangeQuery (String name)
specifier|public
specifier|static
name|RangeQueryBuilder
name|rangeQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|RangeQueryBuilder
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**    * A Query that matches documents containing terms with a specified regular expression.    *    * @param name The name of the field    * @param regexp The regular expression    */
DECL|method|regexpQuery (String name, String regexp)
specifier|public
specifier|static
name|RegexpQueryBuilder
name|regexpQuery
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|regexp
parameter_list|)
block|{
return|return
operator|new
name|RegexpQueryBuilder
argument_list|(
name|name
argument_list|,
name|regexp
argument_list|)
return|;
block|}
comment|/** A Query that matches documents matching boolean combinations of other queries. */
DECL|method|boolQuery ()
specifier|public
specifier|static
name|BoolQueryBuilder
name|boolQuery
parameter_list|()
block|{
return|return
operator|new
name|BoolQueryBuilder
argument_list|()
return|;
block|}
comment|/**    * A filter to filter only documents where a field exists in them.    *    * @param name The name of the field    */
DECL|method|existsQuery (String name)
specifier|public
specifier|static
name|ExistsQueryBuilder
name|existsQuery
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ExistsQueryBuilder
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|QueryBuilders ()
specifier|private
name|QueryBuilders
parameter_list|()
block|{}
block|}
end_class

end_unit

