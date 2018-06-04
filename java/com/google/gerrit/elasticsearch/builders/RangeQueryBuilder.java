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

begin_comment
comment|/**  * A Query that matches documents within an range of terms. A trimmed down version of  * org.elasticsearch.index.query.RangeQueryBuilder for this very package.  */
end_comment

begin_class
DECL|class|RangeQueryBuilder
specifier|public
class|class
name|RangeQueryBuilder
extends|extends
name|QueryBuilder
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|from
specifier|private
name|Object
name|from
decl_stmt|;
DECL|field|to
specifier|private
name|Object
name|to
decl_stmt|;
DECL|field|includeLower
specifier|private
name|boolean
name|includeLower
init|=
literal|true
decl_stmt|;
DECL|field|includeUpper
specifier|private
name|boolean
name|includeUpper
init|=
literal|true
decl_stmt|;
comment|/**    * A Query that matches documents within an range of terms.    *    * @param name The field name    */
DECL|method|RangeQueryBuilder (String name)
name|RangeQueryBuilder
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/** The from part of the range query. Null indicates unbounded. */
DECL|method|gt (Object from)
specifier|public
name|RangeQueryBuilder
name|gt
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|includeLower
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** The from part of the range query. Null indicates unbounded. */
DECL|method|gte (Object from)
specifier|public
name|RangeQueryBuilder
name|gte
parameter_list|(
name|Object
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|includeLower
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** The from part of the range query. Null indicates unbounded. */
DECL|method|gte (int from)
specifier|public
name|RangeQueryBuilder
name|gte
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
name|this
operator|.
name|includeLower
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** The to part of the range query. Null indicates unbounded. */
DECL|method|lte (Object to)
specifier|public
name|RangeQueryBuilder
name|lte
parameter_list|(
name|Object
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
name|this
operator|.
name|includeUpper
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/** The to part of the range query. Null indicates unbounded. */
DECL|method|lte (int to)
specifier|public
name|RangeQueryBuilder
name|lte
parameter_list|(
name|int
name|to
parameter_list|)
block|{
name|this
operator|.
name|to
operator|=
name|to
expr_stmt|;
name|this
operator|.
name|includeUpper
operator|=
literal|true
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
literal|"range"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|startObject
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|builder
operator|.
name|field
argument_list|(
literal|"from"
argument_list|,
name|from
argument_list|)
expr_stmt|;
name|builder
operator|.
name|field
argument_list|(
literal|"to"
argument_list|,
name|to
argument_list|)
expr_stmt|;
name|builder
operator|.
name|field
argument_list|(
literal|"include_lower"
argument_list|,
name|includeLower
argument_list|)
expr_stmt|;
name|builder
operator|.
name|field
argument_list|(
literal|"include_upper"
argument_list|,
name|includeUpper
argument_list|)
expr_stmt|;
name|builder
operator|.
name|endObject
argument_list|()
expr_stmt|;
name|builder
operator|.
name|endObject
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

