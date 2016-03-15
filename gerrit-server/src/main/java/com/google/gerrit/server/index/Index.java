begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Secondary index implementation for arbitrary documents.  *<p>  * Documents are inserted into the index and are queried by converting special  * {@link com.google.gerrit.server.query.Predicate} instances into index-aware  * predicates that use the index search results as a source.  *<p>  * Implementations must be thread-safe and should batch inserts/updates where  * appropriate.  */
end_comment

begin_interface
DECL|interface|Index
specifier|public
interface|interface
name|Index
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
block|{
comment|/** @return the schema version used by this index. */
DECL|method|getSchema ()
name|Schema
argument_list|<
name|V
argument_list|>
name|getSchema
parameter_list|()
function_decl|;
comment|/** Close this index. */
DECL|method|close ()
name|void
name|close
parameter_list|()
function_decl|;
comment|/**    * Update a document in the index.    *<p>    * Semantically equivalent to deleting the document and reinserting it with    * new field values. A document that does not already exist is created. Results    * may not be immediately visible to searchers, but should be visible within a    * reasonable amount of time.    *    * @param obj document object    *    * @throws IOException    */
DECL|method|replace (V obj)
name|void
name|replace
parameter_list|(
name|V
name|obj
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Delete a document from the index by key.    *    * @param key document key    *    * @throws IOException    */
DECL|method|delete (K key)
name|void
name|delete
parameter_list|(
name|K
name|key
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Delete all documents from the index.    *    * @throws IOException    */
DECL|method|deleteAll ()
name|void
name|deleteAll
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Convert the given operator predicate into a source searching the index and    * returning only the documents matching that predicate.    *<p>    * This method may be called multiple times for variations on the same    * predicate or multiple predicate subtrees in the course of processing a    * single query, so it should not have any side effects (e.g. starting a    * search in the background).    *    * @param p the predicate to match. Must be a tree containing only AND, OR,    *     or NOT predicates as internal nodes, and {@link IndexPredicate}s as    *     leaves.    * @param opts query options not implied by the predicate, such as start and    *     limit.    * @return a source of documents matching the predicate, returned in a    *     defined order depending on the type of documents.    *    * @throws QueryParseException if the predicate could not be converted to an    *     indexed data source.    */
DECL|method|getSource (Predicate<V> p, QueryOptions opts)
name|DataSource
argument_list|<
name|V
argument_list|>
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|V
argument_list|>
name|p
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
function_decl|;
comment|/**    * Mark whether this index is up-to-date and ready to serve reads.    *    * @param ready whether the index is ready    * @throws IOException    */
DECL|method|markReady (boolean ready)
name|void
name|markReady
parameter_list|(
name|boolean
name|ready
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

