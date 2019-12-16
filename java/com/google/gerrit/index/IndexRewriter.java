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
DECL|package|com.google.gerrit.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
package|;
end_package

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

begin_comment
comment|/**  * Rewriter to sanitize queries before they are sent to the index. The idea here is that the  * rewriter swaps out predicates so that the query can be processed by the index.  */
end_comment

begin_interface
DECL|interface|IndexRewriter
specifier|public
interface|interface
name|IndexRewriter
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**    * Returns a sanitized version of the provided predicate. Uses {@link QueryOptions} to enforce    * index-specific limits such as {@code maxTerms}.    */
DECL|method|rewrite (Predicate<T> in, QueryOptions opts)
name|Predicate
argument_list|<
name|T
argument_list|>
name|rewrite
parameter_list|(
name|Predicate
argument_list|<
name|T
argument_list|>
name|in
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
function_decl|;
block|}
end_interface

end_unit

