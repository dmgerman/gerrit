begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * Result from any data store query function.  *  * @param<T> type of entity being returned by the query.  */
end_comment

begin_interface
DECL|interface|ResultSet
specifier|public
interface|interface
name|ResultSet
parameter_list|<
name|T
parameter_list|>
extends|extends
name|Iterable
argument_list|<
name|T
argument_list|>
block|{
comment|/**    * Obtain an iterator to loop through the results.    *    *<p>The iterator can be obtained only once. When the iterator completes (<code>hasNext()</code>    * returns false) {@link #close()} will be automatically called.    */
annotation|@
name|Override
DECL|method|iterator ()
name|Iterator
argument_list|<
name|T
argument_list|>
name|iterator
parameter_list|()
function_decl|;
comment|/**    * Materialize all results as a single list.    *    *<p>Prior to returning {@link #close()} is invoked. This method must not be combined with {@link    * #iterator()} on the same instance.    *    * @return mutable list of the complete results.    */
DECL|method|toList ()
name|List
argument_list|<
name|T
argument_list|>
name|toList
parameter_list|()
function_decl|;
comment|/**    * Close the result, discarding any further results.    *    *<p>This method may be invoked more than once. Its main use is to stop obtaining results before    * the iterator has finished.    */
DECL|method|close ()
name|void
name|close
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

