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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
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
name|change
operator|.
name|ChangeDataSource
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
comment|/**  * Secondary index implementation for change documents.  *<p>  * {@link ChangeData} objects are inserted into the index and are queried by  * converting special {@link com.google.gerrit.server.query.Predicate} instances  * into index-aware predicates that use the index search results as a source.  *<p>  * Implementations must be thread-safe and should batch inserts/updates where  * appropriate.  */
end_comment

begin_interface
DECL|interface|ChangeIndex
specifier|public
interface|interface
name|ChangeIndex
block|{
comment|/** Instance indicating secondary index is disabled. */
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|ChangeIndex
name|DISABLED
init|=
operator|new
name|ChangeIndex
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|insert
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|replace
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|delete
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|QueryParseException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
comment|/**    * Insert a change document into the index.    *<p>    * Results may not be immediately visible to searchers, but should be visible    * within a reasonable amount of time.    *    * @param cd change document with all index fields prepopulated; see    *     {@link ChangeData#fillIndexFields}.    *    * @throws IOException if the change could not be inserted.    */
DECL|method|insert (ChangeData cd)
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|insert
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Update a change document in the index.    *<p>    * Semantically equivalent to deleting the document and reinserting it with    * new field values. Results may not be immediately visible to searchers, but    * should be visible within a reasonable amount of time.    *    * @param cd change document with all index fields prepopulated; see    *     {@link ChangeData#fillIndexFields}.    *    * @throws IOException    */
DECL|method|replace (ChangeData cd)
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|replace
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Delete a change document from the index.    *    * @param cd change document.    *    * @throws IOException    */
DECL|method|delete (ChangeData cd)
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|delete
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Convert the given operator predicate into a source searching the index and    * returning only the documents matching that predicate.    *    * @param p the predicate to match. Must be a tree containing only AND, OR,    *     or NOT predicates as internal nodes, and {@link IndexPredicate}s as    *     leaves.    * @return a source of documents matching the predicate.    *    * @throws QueryParseException if the predicate could not be converted to an    *     indexed data source.    */
DECL|method|getSource (Predicate<ChangeData> p)
specifier|public
name|ChangeDataSource
name|getSource
parameter_list|(
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|p
parameter_list|)
throws|throws
name|QueryParseException
function_decl|;
block|}
end_interface

end_unit

