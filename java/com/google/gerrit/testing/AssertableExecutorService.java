begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|ForwardingExecutorService
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
name|MoreExecutors
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
name|ExecutorService
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
name|Future
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
name|AtomicInteger
import|;
end_import

begin_comment
comment|/**  * Forwards all calls to a direct executor making it so that the submitted {@link Runnable}s run  * synchronously. Holds a count of the number of tasks that were executed.  */
end_comment

begin_class
DECL|class|AssertableExecutorService
specifier|public
class|class
name|AssertableExecutorService
extends|extends
name|ForwardingExecutorService
block|{
DECL|field|delegate
specifier|private
specifier|final
name|ExecutorService
name|delegate
init|=
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
decl_stmt|;
DECL|field|numInteractions
specifier|private
specifier|final
name|AtomicInteger
name|numInteractions
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|delegate ()
specifier|protected
name|ExecutorService
name|delegate
parameter_list|()
block|{
return|return
name|delegate
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Callable<T> task)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|task
parameter_list|)
block|{
name|numInteractions
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task)
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|)
block|{
name|numInteractions
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, T result)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Future
argument_list|<
name|T
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|,
name|T
name|result
parameter_list|)
block|{
name|numInteractions
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|super
operator|.
name|submit
argument_list|(
name|task
argument_list|,
name|result
argument_list|)
return|;
block|}
comment|/** Asserts and resets the number of executions this executor observed. */
DECL|method|assertInteractions (int expectedNumInteractions)
specifier|public
name|void
name|assertInteractions
parameter_list|(
name|int
name|expectedNumInteractions
parameter_list|)
block|{
name|assertThat
argument_list|(
name|numInteractions
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|named
argument_list|(
literal|"expectedRunnablesSubmittedOnExecutor"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedNumInteractions
argument_list|)
expr_stmt|;
name|numInteractions
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

