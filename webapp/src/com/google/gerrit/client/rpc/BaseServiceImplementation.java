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
DECL|package|com.google.gerrit.client.rpc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|rpc
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
name|client
operator|.
name|reviewdb
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|rpc
operator|.
name|AsyncCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|SchemaFactory
import|;
end_import

begin_comment
comment|/** Support for services which require a {@link ReviewDb} instance. */
end_comment

begin_class
DECL|class|BaseServiceImplementation
specifier|public
class|class
name|BaseServiceImplementation
block|{
DECL|field|schema
specifier|protected
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
decl_stmt|;
DECL|method|BaseServiceImplementation (final SchemaFactory<ReviewDb> rdf)
specifier|protected
name|BaseServiceImplementation
parameter_list|(
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|rdf
parameter_list|)
block|{
name|schema
operator|=
name|rdf
expr_stmt|;
block|}
comment|/**    * Executes<code>action.run</code> with an active ReviewDb connection.    *<p>    * A database handle is automatically opened and closed around the action's    * {@link Action#run(ReviewDb)} method. OrmExceptions are caught and passed    * into the onFailure method of the callback.    *     * @param<T> type of result the callback expects.    * @param callback the callback that will receive the result.    * @param action the action logic to perform.    */
DECL|method|run (final AsyncCallback<T> callback, final Action<T> action)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|run
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|,
specifier|final
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
block|{
try|try
block|{
specifier|final
name|ReviewDb
name|db
init|=
name|schema
operator|.
name|open
argument_list|()
decl_stmt|;
specifier|final
name|T
name|r
decl_stmt|;
try|try
block|{
name|r
operator|=
name|action
operator|.
name|run
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|onSuccess
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Failure
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onFailure
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Exception whose cause is passed into onFailure. */
DECL|class|Failure
specifier|public
specifier|static
class|class
name|Failure
extends|extends
name|Exception
block|{
DECL|method|Failure (final Throwable why)
specifier|public
name|Failure
parameter_list|(
specifier|final
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|why
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Arbitrary action to run with a database connection. */
DECL|interface|Action
specifier|public
specifier|static
interface|interface
name|Action
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Perform this action, returning the onSuccess value.      *       * @param db an open database handle to be used by this connection.      * @return he value to pass to {@link AsyncCallback#onSuccess(Object)}.      * @throws OrmException any schema based action failed.      * @throws Failure cause is given to      *         {@link AsyncCallback#onFailure(Throwable)}.      */
DECL|method|run (ReviewDb db)
name|T
name|run
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
throws|throws
name|OrmException
throws|,
name|Failure
function_decl|;
block|}
block|}
end_class

end_unit

