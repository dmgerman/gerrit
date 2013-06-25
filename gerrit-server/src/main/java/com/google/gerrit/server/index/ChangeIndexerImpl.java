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
comment|// limitations under the License.package com.google.gerrit.server.git;
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
name|RequestScopePropagator
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
name|Inject
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
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * Helper for (re)indexing a change document.  *<p>  * Indexing is run in the background, as it may require substantial work to  * compute some of the fields and/or update the index.  */
end_comment

begin_class
DECL|class|ChangeIndexerImpl
specifier|public
class|class
name|ChangeIndexerImpl
implements|implements
name|ChangeIndexer
block|{
DECL|field|executor
specifier|private
specifier|final
name|ListeningScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|ChangeIndex
name|index
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeIndexerImpl (@ndexExecutor ListeningScheduledExecutorService executor, ChangeIndex index)
name|ChangeIndexerImpl
parameter_list|(
annotation|@
name|IndexExecutor
name|ListeningScheduledExecutorService
name|executor
parameter_list|,
name|ChangeIndex
name|index
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
annotation|@
name|Override
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
name|change
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|index (Change change, RequestScopePropagator prop)
specifier|public
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|index
parameter_list|(
name|Change
name|change
parameter_list|,
name|RequestScopePropagator
name|prop
parameter_list|)
block|{
name|Callable
argument_list|<
name|?
argument_list|>
name|task
init|=
operator|new
name|Task
argument_list|(
name|change
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|!=
literal|null
condition|)
block|{
name|task
operator|=
name|prop
operator|.
name|wrap
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
return|return
name|executor
operator|.
name|submit
argument_list|(
name|task
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
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|method|Task (Change change)
specifier|private
name|Task
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
name|this
operator|.
name|change
operator|=
name|change
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
name|IOException
block|{
name|index
operator|.
name|replace
argument_list|(
operator|new
name|ChangeData
argument_list|(
name|change
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
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
name|change
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

