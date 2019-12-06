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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

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
name|server
operator|.
name|ExceptionHook
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
comment|/**  * An action that is executed with retrying.  *  *<p>Instances of this class are created via {@link RetryHelper} (see {@link  * RetryHelper#action(ActionType, String, Action)}, {@link RetryHelper#accountUpdate(String,  * Action)}, {@link RetryHelper#changeUpdate(String, Action)}, {@link  * RetryHelper#groupUpdate(String, Action)}, {@link RetryHelper#pluginUpdate(String, Action)},  * {@link RetryHelper#indexQuery(String, Action)}).  *  *<p>Which exceptions cause a retry is controlled by {@link ExceptionHook#shouldRetry(Throwable)}.  * In addition callers can specify additional exception that should cause a retry via {@link  * #retryOn(Predicate)}.  */
end_comment

begin_class
DECL|class|RetryableAction
specifier|public
class|class
name|RetryableAction
parameter_list|<
name|T
parameter_list|>
block|{
DECL|enum|ActionType
specifier|public
enum|enum
name|ActionType
block|{
DECL|enumConstant|ACCOUNT_UPDATE
name|ACCOUNT_UPDATE
block|,
DECL|enumConstant|CHANGE_UPDATE
name|CHANGE_UPDATE
block|,
DECL|enumConstant|GROUP_UPDATE
name|GROUP_UPDATE
block|,
DECL|enumConstant|INDEX_QUERY
name|INDEX_QUERY
block|,
DECL|enumConstant|PLUGIN_UPDATE
name|PLUGIN_UPDATE
block|,
DECL|enumConstant|REST_READ_REQUEST
name|REST_READ_REQUEST
block|,
DECL|enumConstant|REST_WRITE_REQUEST
name|REST_WRITE_REQUEST
block|,   }
annotation|@
name|FunctionalInterface
DECL|interface|Action
specifier|public
interface|interface
name|Action
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|call ()
name|T
name|call
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
DECL|field|retryHelper
specifier|private
specifier|final
name|RetryHelper
name|retryHelper
decl_stmt|;
DECL|field|actionType
specifier|private
specifier|final
name|ActionType
name|actionType
decl_stmt|;
DECL|field|action
specifier|private
specifier|final
name|Action
argument_list|<
name|T
argument_list|>
name|action
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|RetryHelper
operator|.
name|Options
operator|.
name|Builder
name|options
init|=
name|RetryHelper
operator|.
name|options
argument_list|()
decl_stmt|;
DECL|field|exceptionPredicates
specifier|private
specifier|final
name|List
argument_list|<
name|Predicate
argument_list|<
name|Throwable
argument_list|>
argument_list|>
name|exceptionPredicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|RetryableAction ( RetryHelper retryHelper, ActionType actionType, String actionName, Action<T> action)
name|RetryableAction
parameter_list|(
name|RetryHelper
name|retryHelper
parameter_list|,
name|ActionType
name|actionType
parameter_list|,
name|String
name|actionName
parameter_list|,
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
block|{
name|this
operator|.
name|retryHelper
operator|=
name|requireNonNull
argument_list|(
name|retryHelper
argument_list|,
literal|"retryHelper"
argument_list|)
expr_stmt|;
name|this
operator|.
name|actionType
operator|=
name|requireNonNull
argument_list|(
name|actionType
argument_list|,
literal|"actionType"
argument_list|)
expr_stmt|;
name|this
operator|.
name|action
operator|=
name|requireNonNull
argument_list|(
name|action
argument_list|,
literal|"action"
argument_list|)
expr_stmt|;
name|options
operator|.
name|caller
argument_list|(
name|requireNonNull
argument_list|(
name|actionName
argument_list|,
literal|"actionName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds an additional condition that should trigger retries.    *    *<p>For some exceptions retrying is enabled globally (see {@link    * ExceptionHook#shouldRetry(Throwable)}). Conditions for those exceptions do not need to be    * specified here again.    *    *<p>This method can be invoked multiple times to add further conditions that should trigger    * retries.    *    * @param exceptionPredicate predicate that decides if the action should be retried for a given    *     exception    * @return this instance to enable chaining of calls    */
DECL|method|retryOn (Predicate<Throwable> exceptionPredicate)
specifier|public
name|RetryableAction
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
name|exceptionPredicates
operator|.
name|add
argument_list|(
name|exceptionPredicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Sets a condition that should trigger auto-retry with tracing.    *    *<p>This condition is only relevant if an exception occurs that doesn't trigger (normal) retry.    *    *<p>Auto-retry with tracing automatically captures traces for unexpected exceptions so that they    * can be investigated.    *    *<p>Every call of this method overwrites any previously set condition for auto-retry with    * tracing.    *    * @param exceptionPredicate predicate that decides if the action should be retried with tracing    *     for a given exception    * @return this instance to enable chaining of calls    */
DECL|method|retryWithTrace (Predicate<Throwable> exceptionPredicate)
specifier|public
name|RetryableAction
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
name|options
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
comment|/**    * Sets a callback that is invoked when auto-retry with tracing is triggered.    *    *<p>Via the callback callers can find out with trace ID was used for the retry.    *    *<p>Every call of this method overwrites any previously set trace ID consumer.    *    * @param traceIdConsumer trace ID consumer    * @return this instance to enable chaining of calls    */
DECL|method|onAutoTrace (Consumer<String> traceIdConsumer)
specifier|public
name|RetryableAction
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
name|options
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
comment|/**    * Sets a listener that is invoked when the action is retried.    *    *<p>Every call of this method overwrites any previously set listener.    *    * @param retryListener retry listener    * @return this instance to enable chaining of calls    */
DECL|method|listener (RetryListener retryListener)
specifier|public
name|RetryableAction
argument_list|<
name|T
argument_list|>
name|listener
parameter_list|(
name|RetryListener
name|retryListener
parameter_list|)
block|{
name|options
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
comment|/**    * Increases the default timeout by the given multiplier.    *    *<p>Every call of this method overwrites any previously set timeout.    *    * @param multiplier multiplier for the default timeout    * @return this instance to enable chaining of calls    */
DECL|method|defaultTimeoutMultiplier (int multiplier)
specifier|public
name|RetryableAction
argument_list|<
name|T
argument_list|>
name|defaultTimeoutMultiplier
parameter_list|(
name|int
name|multiplier
parameter_list|)
block|{
name|options
operator|.
name|timeout
argument_list|(
name|retryHelper
operator|.
name|getDefaultTimeout
argument_list|(
name|actionType
argument_list|)
operator|.
name|multipliedBy
argument_list|(
name|multiplier
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**    * Executes this action with retry.    *    * @return the result of the action    */
DECL|method|call ()
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
return|return
name|retryHelper
operator|.
name|execute
argument_list|(
name|actionType
argument_list|,
name|action
argument_list|,
name|options
operator|.
name|build
argument_list|()
argument_list|,
name|t
lambda|->
name|exceptionPredicates
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|p
lambda|->
name|p
operator|.
name|test
argument_list|(
name|t
argument_list|)
argument_list|)
argument_list|)
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
name|Exception
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|t
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

