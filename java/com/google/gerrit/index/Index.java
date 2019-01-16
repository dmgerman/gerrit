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
name|exceptions
operator|.
name|StorageException
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
name|FieldBundle
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
name|Optional
import|;
end_import

begin_comment
comment|/**  * Secondary index implementation for arbitrary documents.  *  *<p>Documents are inserted into the index and are queried by converting special {@link  * com.google.gerrit.index.query.Predicate} instances into index-aware predicates that use the index  * search results as a source.  *  *<p>Implementations must be thread-safe and should batch inserts/updates where appropriate.  */
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
comment|/**    * Update a document in the index.    *    *<p>Semantically equivalent to deleting the document and reinserting it with new field values. A    * document that does not already exist is created. Results may not be immediately visible to    * searchers, but should be visible within a reasonable amount of time.    *    * @param obj document object    * @throws IOException    */
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
comment|/**    * Delete a document from the index by key.    *    * @param key document key    * @throws IOException    */
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
comment|/**    * Convert the given operator predicate into a source searching the index and returning only the    * documents matching that predicate.    *    *<p>This method may be called multiple times for variations on the same predicate or multiple    * predicate subtrees in the course of processing a single query, so it should not have any side    * effects (e.g. starting a search in the background).    *    * @param p the predicate to match. Must be a tree containing only AND, OR, or NOT predicates as    *     internal nodes, and {@link IndexPredicate}s as leaves.    * @param opts query options not implied by the predicate, such as start and limit.    * @return a source of documents matching the predicate, returned in a defined order depending on    *     the type of documents.    * @throws QueryParseException if the predicate could not be converted to an indexed data source.    */
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
comment|/**    * Get a single document from the index.    *    * @param key document key.    * @param opts query options. Options that do not make sense in the context of a single document,    *     such as start, will be ignored.    * @return a single document if present.    * @throws IOException    */
DECL|method|get (K key, QueryOptions opts)
specifier|default
name|Optional
argument_list|<
name|V
argument_list|>
name|get
parameter_list|(
name|K
name|key
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|IOException
block|{
name|opts
operator|=
name|opts
operator|.
name|withStart
argument_list|(
literal|0
argument_list|)
operator|.
name|withLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|V
argument_list|>
name|results
decl_stmt|;
try|try
block|{
name|results
operator|=
name|getSource
argument_list|(
name|keyPredicate
argument_list|(
name|key
argument_list|)
argument_list|,
name|opts
argument_list|)
operator|.
name|read
argument_list|()
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected QueryParseException during get()"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Multiple results found in index for key "
operator|+
name|key
operator|+
literal|": "
operator|+
name|results
argument_list|)
throw|;
block|}
return|return
name|results
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
block|}
comment|/**    * Get a single raw document from the index.    *    * @param key document key.    * @param opts query options. Options that do not make sense in the context of a single document,    *     such as start, will be ignored.    * @return an abstraction of a raw index document to retrieve fields from.    * @throws IOException    */
DECL|method|getRaw (K key, QueryOptions opts)
specifier|default
name|Optional
argument_list|<
name|FieldBundle
argument_list|>
name|getRaw
parameter_list|(
name|K
name|key
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|IOException
block|{
name|opts
operator|=
name|opts
operator|.
name|withStart
argument_list|(
literal|0
argument_list|)
operator|.
name|withLimit
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|FieldBundle
argument_list|>
name|results
decl_stmt|;
try|try
block|{
name|results
operator|=
name|getSource
argument_list|(
name|keyPredicate
argument_list|(
name|key
argument_list|)
argument_list|,
name|opts
argument_list|)
operator|.
name|readRaw
argument_list|()
operator|.
name|toList
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueryParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unexpected QueryParseException during get()"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|StorageException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|results
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Multiple results found in index for key "
operator|+
name|key
operator|+
literal|": "
operator|+
name|results
argument_list|)
throw|;
block|}
return|return
name|results
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
block|}
comment|/**    * Get a predicate that looks up a single document by key.    *    * @param key document key.    * @return a single predicate.    */
DECL|method|keyPredicate (K key)
name|Predicate
argument_list|<
name|V
argument_list|>
name|keyPredicate
parameter_list|(
name|K
name|key
parameter_list|)
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

