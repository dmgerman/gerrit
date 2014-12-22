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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Results of a query over changes. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|QueryResult
specifier|public
specifier|abstract
class|class
name|QueryResult
block|{
DECL|method|create (String query, Predicate<ChangeData> predicate, int limit, List<ChangeData> changes)
specifier|static
name|QueryResult
name|create
parameter_list|(
name|String
name|query
parameter_list|,
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|predicate
parameter_list|,
name|int
name|limit
parameter_list|,
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|)
block|{
name|boolean
name|moreChanges
decl_stmt|;
if|if
condition|(
name|changes
operator|.
name|size
argument_list|()
operator|>
name|limit
condition|)
block|{
name|moreChanges
operator|=
literal|true
expr_stmt|;
name|changes
operator|=
name|changes
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
name|moreChanges
operator|=
literal|false
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_QueryResult
argument_list|(
name|query
argument_list|,
name|predicate
argument_list|,
name|changes
argument_list|,
name|moreChanges
argument_list|)
return|;
block|}
comment|/** @return the original query string. */
DECL|method|query ()
specifier|public
specifier|abstract
name|String
name|query
parameter_list|()
function_decl|;
comment|/**    * @return the predicate after all rewriting and other modification by the    *     query subsystem.    */
DECL|method|predicate ()
specifier|public
specifier|abstract
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|predicate
parameter_list|()
function_decl|;
comment|/** @return the query results. */
DECL|method|changes ()
specifier|public
specifier|abstract
name|List
argument_list|<
name|ChangeData
argument_list|>
name|changes
parameter_list|()
function_decl|;
comment|/**    * @return whether the query could be retried with    *     {@link QueryProcessor#setStart(int)} to produce more results. Never    *     true if {@link #changes()} is empty.    */
DECL|method|moreChanges ()
specifier|public
specifier|abstract
name|boolean
name|moreChanges
parameter_list|()
function_decl|;
block|}
end_class

end_unit

