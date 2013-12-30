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
name|base
operator|.
name|Function
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
name|Atomics
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
name|CheckedFuture
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
name|ListeningExecutorService
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|reviewdb
operator|.
name|server
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
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
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
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|server
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
name|server
operator|.
name|SchemaFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|OutOfScopeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|ProvisionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|util
operator|.
name|Providers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Helper for (re)indexing a change document.  *<p>  * Indexing is run in the background, as it may require substantial work to  * compute some of the fields and/or update the index.  */
end_comment

begin_class
DECL|class|ChangeIndexer
specifier|public
class|class
name|ChangeIndexer
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChangeIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ChangeIndex index)
name|ChangeIndexer
name|create
parameter_list|(
name|ChangeIndex
name|index
parameter_list|)
function_decl|;
DECL|method|create (IndexCollection indexes)
name|ChangeIndexer
name|create
parameter_list|(
name|IndexCollection
name|indexes
parameter_list|)
function_decl|;
block|}
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|Function
argument_list|<
name|Exception
argument_list|,
name|IOException
argument_list|>
name|MAPPER
init|=
operator|new
name|Function
argument_list|<
name|Exception
argument_list|,
name|IOException
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|IOException
name|apply
parameter_list|(
name|Exception
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|instanceof
name|IOException
condition|)
block|{
return|return
operator|(
name|IOException
operator|)
name|in
return|;
block|}
elseif|else
if|if
condition|(
name|in
operator|instanceof
name|ExecutionException
operator|&&
name|in
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IOException
condition|)
block|{
return|return
operator|(
name|IOException
operator|)
name|in
operator|.
name|getCause
argument_list|()
return|;
block|}
else|else
block|{
return|return
operator|new
name|IOException
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
block|}
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|IndexCollection
name|indexes
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|ChangeIndex
name|index
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|context
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ListeningExecutorService
name|executor
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ChangeIndexer (@ndexExecutor ListeningExecutorService executor, SchemaFactory<ReviewDb> schemaFactory, ChangeData.Factory changeDataFactory, ThreadLocalRequestContext context, @Assisted ChangeIndex index)
name|ChangeIndexer
parameter_list|(
annotation|@
name|IndexExecutor
name|ListeningExecutorService
name|executor
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|ThreadLocalRequestContext
name|context
parameter_list|,
annotation|@
name|Assisted
name|ChangeIndex
name|index
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|ChangeIndexer (@ndexExecutor ListeningExecutorService executor, SchemaFactory<ReviewDb> schemaFactory, ChangeData.Factory changeDataFactory, ThreadLocalRequestContext context, @Assisted IndexCollection indexes)
name|ChangeIndexer
parameter_list|(
annotation|@
name|IndexExecutor
name|ListeningExecutorService
name|executor
parameter_list|,
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|ThreadLocalRequestContext
name|context
parameter_list|,
annotation|@
name|Assisted
name|IndexCollection
name|indexes
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|index
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
block|}
comment|/**    * Start indexing a change.    *    * @param id change to index.    * @return future for the indexing task.    */
DECL|method|indexAsync (Change.Id id)
specifier|public
name|CheckedFuture
argument_list|<
name|?
argument_list|,
name|IOException
argument_list|>
name|indexAsync
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|executor
operator|!=
literal|null
condition|?
name|submit
argument_list|(
operator|new
name|Task
argument_list|(
name|id
argument_list|,
literal|false
argument_list|)
argument_list|)
else|:
name|Futures
operator|.
expr|<
name|Object
operator|,
name|IOException
operator|>
name|immediateCheckedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**    * Synchronously index a change.    *    * @param cd change to index.    */
DECL|method|index (ChangeData cd)
specifier|public
name|void
name|index
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|ChangeIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
name|i
operator|.
name|replace
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Synchronously index a change.    *    * @param change change to index.    * @param db review database.    */
DECL|method|index (ReviewDb db, Change change)
specifier|public
name|void
name|index
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
name|change
parameter_list|)
throws|throws
name|IOException
block|{
name|index
argument_list|(
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|change
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Start deleting a change.    *    * @param id change to delete.    * @return future for the deleting task.    */
DECL|method|deleteAsync (Change.Id id)
specifier|public
name|CheckedFuture
argument_list|<
name|?
argument_list|,
name|IOException
argument_list|>
name|deleteAsync
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|executor
operator|!=
literal|null
condition|?
name|submit
argument_list|(
operator|new
name|Task
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
argument_list|)
else|:
name|Futures
operator|.
expr|<
name|Object
operator|,
name|IOException
operator|>
name|immediateCheckedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
comment|/**    * Synchronously delete a change.    *    * @param cd change to delete.    */
DECL|method|delete (ChangeData cd)
specifier|public
name|void
name|delete
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|ChangeIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
name|i
operator|.
name|delete
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Synchronously delete a change.    *    * @param change change to delete.    * @param db review database.    */
DECL|method|delete (ReviewDb db, Change change)
specifier|public
name|void
name|delete
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Change
name|change
parameter_list|)
throws|throws
name|IOException
block|{
name|delete
argument_list|(
name|changeDataFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|change
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getWriteIndexes ()
specifier|private
name|Collection
argument_list|<
name|ChangeIndex
argument_list|>
name|getWriteIndexes
parameter_list|()
block|{
return|return
name|indexes
operator|!=
literal|null
condition|?
name|indexes
operator|.
name|getWriteIndexes
argument_list|()
else|:
name|Collections
operator|.
name|singleton
argument_list|(
name|index
argument_list|)
return|;
block|}
DECL|method|submit (Callable<?> task)
specifier|private
name|CheckedFuture
argument_list|<
name|?
argument_list|,
name|IOException
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|?
argument_list|>
name|task
parameter_list|)
block|{
return|return
name|Futures
operator|.
name|makeChecked
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
argument_list|,
name|MAPPER
argument_list|)
return|;
block|}
DECL|class|Task
specifier|private
class|class
name|Task
implements|implements
name|Callable
argument_list|<
name|Void
argument_list|>
block|{
DECL|field|id
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|delete
specifier|private
specifier|final
name|boolean
name|delete
decl_stmt|;
DECL|method|Task (Change.Id id, boolean delete)
specifier|private
name|Task
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|boolean
name|delete
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|delete
operator|=
name|delete
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
specifier|final
name|AtomicReference
argument_list|<
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
name|dbRef
init|=
name|Atomics
operator|.
name|newReference
argument_list|()
decl_stmt|;
name|RequestContext
name|newCtx
init|=
operator|new
name|RequestContext
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
init|=
name|dbRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|db
operator|=
name|Providers
operator|.
name|of
argument_list|(
name|schemaFactory
operator|.
name|open
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|ProvisionException
name|pe
init|=
operator|new
name|ProvisionException
argument_list|(
literal|"error opening ReviewDb"
argument_list|)
decl_stmt|;
name|pe
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|pe
throw|;
block|}
name|dbRef
operator|.
name|set
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
return|return
name|db
return|;
block|}
annotation|@
name|Override
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"No user during ChangeIndexer"
argument_list|)
throw|;
block|}
block|}
decl_stmt|;
name|RequestContext
name|oldCtx
init|=
name|context
operator|.
name|setContext
argument_list|(
name|newCtx
argument_list|)
decl_stmt|;
try|try
block|{
name|ChangeData
name|cd
init|=
name|changeDataFactory
operator|.
name|create
argument_list|(
name|newCtx
operator|.
name|getReviewDbProvider
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|delete
condition|)
block|{
for|for
control|(
name|ChangeIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
name|i
operator|.
name|delete
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|ChangeIndex
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
name|i
operator|.
name|replace
argument_list|(
name|cd
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|context
operator|.
name|setContext
argument_list|(
name|oldCtx
argument_list|)
expr_stmt|;
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
init|=
name|dbRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|!=
literal|null
condition|)
block|{
name|db
operator|.
name|get
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to index change %d"
argument_list|,
name|id
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"index-change-"
operator|+
name|id
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

