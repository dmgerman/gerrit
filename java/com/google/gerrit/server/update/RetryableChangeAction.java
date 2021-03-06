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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|rholder
operator|.
name|retry
operator|.
name|RetryListener
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
name|base
operator|.
name|Throwables
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
import|;
end_import

begin_comment
comment|/**  * A change action that is executed with retrying.  *  *<p>Instances of this class are created via {@link RetryHelper#changeUpdate(String,  * ChangeAction)}.  *  *<p>In contrast to normal {@link RetryableAction.Action}s that are called via {@link  * RetryableAction} {@link ChangeAction}s get a {@link BatchUpdate.Factory} provided.  *  *<p>In addition when a change action is called any exception that is not an unchecked exception  * and neither {@link UpdateException} nor {@link RestApiException} get wrapped into an {@link  * UpdateException}.  */
end_comment

begin_class
DECL|class|RetryableChangeAction
specifier|public
class|class
name|RetryableChangeAction
parameter_list|<
name|T
parameter_list|>
extends|extends
name|RetryableAction
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|FunctionalInterface
DECL|interface|ChangeAction
specifier|public
interface|interface
name|ChangeAction
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|call (BatchUpdate.Factory batchUpdateFactory)
name|T
name|call
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
DECL|method|RetryableChangeAction ( RetryHelper retryHelper, BatchUpdate.Factory updateFactory, String actionName, ChangeAction<T> changeAction)
name|RetryableChangeAction
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|String
name|actionName
parameter_list|,
name|ChangeAction
argument_list|<
name|T
argument_list|>
name|changeAction
parameter_list|)
block|{
name|super
argument_list|(
name|retryHelper
argument_list|,
name|ActionType
operator|.
name|CHANGE_UPDATE
argument_list|,
name|actionName
argument_list|,
parameter_list|()
lambda|->
name|changeAction
operator|.
name|call
argument_list|(
name|updateFactory
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|retryOn (Predicate<Throwable> exceptionPredicate)
specifier|public
name|RetryableChangeAction
argument_list|<
name|T
argument_list|>
name|retryOn
parameter_list|(
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
block|{
name|super
operator|.
name|retryOn
argument_list|(
name|exceptionPredicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|retryWithTrace (Predicate<Throwable> exceptionPredicate)
specifier|public
name|RetryableChangeAction
argument_list|<
name|T
argument_list|>
name|retryWithTrace
parameter_list|(
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
block|{
name|super
operator|.
name|retryWithTrace
argument_list|(
name|exceptionPredicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|onAutoTrace (Consumer<String> traceIdConsumer)
specifier|public
name|RetryableChangeAction
argument_list|<
name|T
argument_list|>
name|onAutoTrace
parameter_list|(
name|Consumer
argument_list|<
name|String
argument_list|>
name|traceIdConsumer
parameter_list|)
block|{
name|super
operator|.
name|onAutoTrace
argument_list|(
name|traceIdConsumer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|listener (RetryListener retryListener)
specifier|public
name|RetryableChangeAction
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|(
name|RetryListener
name|retryListener
parameter_list|)
block|{
name|super
operator|.
name|listener
argument_list|(
name|retryListener
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|defaultTimeoutMultiplier (int multiplier)
specifier|public
name|RetryableChangeAction
argument_list|<
name|T
argument_list|>
name|defaultTimeoutMultiplier
parameter_list|(
name|int
name|multiplier
parameter_list|)
block|{
name|super
operator|.
name|defaultTimeoutMultiplier
argument_list|(
name|multiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|UpdateException
throws|,
name|RestApiException
block|{
try|try
block|{
return|return
name|super
operator|.
name|call
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|t
argument_list|,
name|UpdateException
operator|.
name|class
argument_list|)
expr_stmt|;
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|t
argument_list|,
name|RestApiException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UpdateException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

