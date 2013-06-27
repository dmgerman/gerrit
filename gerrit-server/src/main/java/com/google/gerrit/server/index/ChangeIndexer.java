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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ListeningScheduledExecutorService
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * Helper for (re)indexing a change document.  *<p>  * Indexing is run in the background, as it may require substantial work to  * compute some of the fields and/or update the index.  */
end_comment

begin_class
DECL|class|ChangeIndexer
specifier|public
specifier|abstract
class|class
name|ChangeIndexer
block|{
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
comment|/** Instance indicating secondary index is disabled. */
DECL|field|DISABLED
specifier|public
specifier|static
specifier|final
name|ChangeIndexer
name|DISABLED
init|=
operator|new
name|ChangeIndexer
argument_list|(
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|index
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
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
specifier|public
name|Callable
argument_list|<
name|Void
argument_list|>
name|indexTask
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
return|;
block|}
block|}
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ListeningScheduledExecutorService
name|executor
decl_stmt|;
DECL|method|ChangeIndexer (ListeningScheduledExecutorService executor)
specifier|protected
name|ChangeIndexer
parameter_list|(
name|ListeningScheduledExecutorService
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
comment|/**    * Start indexing a change.    *    * @param change change to index.    * @return future for the indexing task.    */
DECL|method|index (Change change)
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|index
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|index
argument_list|(
operator|new
name|ChangeData
argument_list|(
name|change
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Start indexing a change.    *    * @param change change to index.    * @param prop propagator to wrap any created runnables in.    * @return future for the indexing task.    */
DECL|method|index (ChangeData cd)
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|index
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|executor
operator|.
name|submit
argument_list|(
name|indexTask
argument_list|(
name|cd
argument_list|)
argument_list|)
return|;
block|}
DECL|method|indexTask (ChangeData cd)
specifier|public
specifier|abstract
name|Callable
argument_list|<
name|Void
argument_list|>
name|indexTask
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
function_decl|;
block|}
end_class

end_unit

