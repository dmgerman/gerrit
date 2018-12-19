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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
comment|/** Results of a query over entities. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|QueryResult
specifier|public
specifier|abstract
class|class
name|QueryResult
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|create ( @ullable String query, Predicate<T> predicate, int limit, List<T> entities)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|QueryResult
argument_list|<
name|T
argument_list|>
name|create
parameter_list|(
annotation|@
name|Nullable
name|String
name|query
parameter_list|,
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|,
name|int
name|limit
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|entities
parameter_list|)
block|{
name|boolean
name|more
decl_stmt|;
if|if
condition|(
name|entities
operator|.
name|size
argument_list|()
operator|>
name|limit
condition|)
block|{
name|more
operator|=
literal|true
expr_stmt|;
name|entities
operator|=
name|entities
operator|.
name|subList
argument_list|(
literal|0
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|more
operator|=
literal|false
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_QueryResult
argument_list|<>
argument_list|(
name|query
argument_list|,
name|predicate
argument_list|,
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|entities
argument_list|)
argument_list|,
name|more
argument_list|)
return|;
block|}
comment|/** @return the original query string, or null if the query was created programmatically. */
annotation|@
name|Nullable
DECL|method|query ()
specifier|public
specifier|abstract
name|String
name|query
parameter_list|()
function_decl|;
comment|/** @return the predicate after all rewriting and other modification by the query subsystem. */
DECL|method|predicate ()
specifier|public
specifier|abstract
name|Predicate
argument_list|<
name|T
argument_list|>
name|predicate
parameter_list|()
function_decl|;
comment|/** @return the query results. */
DECL|method|entities ()
specifier|public
specifier|abstract
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|entities
parameter_list|()
function_decl|;
comment|/**    * @return whether the query could be retried with a higher start/limit to produce more results.    *     Never true if {@link #entities()} is empty.    */
DECL|method|more ()
specifier|public
specifier|abstract
name|boolean
name|more
parameter_list|()
function_decl|;
block|}
end_class

end_unit

