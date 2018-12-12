begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|firstNonNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|Attempt
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
name|RetryException
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
name|github
operator|.
name|rholder
operator|.
name|retry
operator|.
name|Retryer
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
name|RetryerBuilder
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
name|StopStrategies
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
name|WaitStrategies
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
name|WaitStrategy
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Predicate
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
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|flogger
operator|.
name|FluentLogger
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
name|common
operator|.
name|Nullable
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|LockFailureException
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
name|metrics
operator|.
name|Counter1
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
name|metrics
operator|.
name|Description
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
name|metrics
operator|.
name|Field
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
name|metrics
operator|.
name|Histogram1
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
name|metrics
operator|.
name|MetricMaker
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
name|config
operator|.
name|GerritServerConfig
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|RetryHelper
specifier|public
class|class
name|RetryHelper
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
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
block|}
comment|/**    * Options for retrying a single operation.    *    *<p>This class is similar in function to upstream's {@link RetryerBuilder}, but it exists as its    * own class in Gerrit for several reasons:    *    *<ul>    *<li>Gerrit needs to support defaults for some of the options, such as a default timeout.    *       {@code RetryerBuilder} doesn't support calling the same setter multiple times, so doing    *       this with {@code RetryerBuilder} directly would not be easy.    *<li>Gerrit explicitly does not want callers to have full control over all possible options,    *       so this class exposes a curated subset.    *</ul>    */
annotation|@
name|AutoValue
DECL|class|Options
specifier|public
specifier|abstract
specifier|static
class|class
name|Options
block|{
annotation|@
name|Nullable
DECL|method|listener ()
specifier|abstract
name|RetryListener
name|listener
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|timeout ()
specifier|abstract
name|Duration
name|timeout
parameter_list|()
function_decl|;
annotation|@
name|AutoValue
operator|.
name|Builder
DECL|class|Builder
specifier|public
specifier|abstract
specifier|static
class|class
name|Builder
block|{
DECL|method|listener (RetryListener listener)
specifier|public
specifier|abstract
name|Builder
name|listener
parameter_list|(
name|RetryListener
name|listener
parameter_list|)
function_decl|;
DECL|method|timeout (Duration timeout)
specifier|public
specifier|abstract
name|Builder
name|timeout
parameter_list|(
name|Duration
name|timeout
parameter_list|)
function_decl|;
DECL|method|build ()
specifier|public
specifier|abstract
name|Options
name|build
parameter_list|()
function_decl|;
block|}
block|}
annotation|@
name|VisibleForTesting
annotation|@
name|Singleton
DECL|class|Metrics
specifier|public
specifier|static
class|class
name|Metrics
block|{
DECL|field|attemptCounts
specifier|final
name|Histogram1
argument_list|<
name|ActionType
argument_list|>
name|attemptCounts
decl_stmt|;
DECL|field|timeoutCount
specifier|final
name|Counter1
argument_list|<
name|ActionType
argument_list|>
name|timeoutCount
decl_stmt|;
annotation|@
name|Inject
DECL|method|Metrics (MetricMaker metricMaker)
name|Metrics
parameter_list|(
name|MetricMaker
name|metricMaker
parameter_list|)
block|{
name|Field
argument_list|<
name|ActionType
argument_list|>
name|view
init|=
name|Field
operator|.
name|ofEnum
argument_list|(
name|ActionType
operator|.
name|class
argument_list|,
literal|"action_type"
argument_list|)
decl_stmt|;
name|attemptCounts
operator|=
name|metricMaker
operator|.
name|newHistogram
argument_list|(
literal|"action/retry_attempt_counts"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Distribution of number of attempts made by RetryHelper to execute an action"
operator|+
literal|" (1 == single attempt, no retry)"
argument_list|)
operator|.
name|setCumulative
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"attempts"
argument_list|)
argument_list|,
name|view
argument_list|)
expr_stmt|;
name|timeoutCount
operator|=
name|metricMaker
operator|.
name|newCounter
argument_list|(
literal|"action/retry_timeout_count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Number of action executions of RetryHelper that ultimately timed out"
argument_list|)
operator|.
name|setCumulative
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"timeouts"
argument_list|)
argument_list|,
name|view
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|options ()
specifier|public
specifier|static
name|Options
operator|.
name|Builder
name|options
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_RetryHelper_Options
operator|.
name|Builder
argument_list|()
return|;
block|}
DECL|method|defaults ()
specifier|private
specifier|static
name|Options
name|defaults
parameter_list|()
block|{
return|return
name|options
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
DECL|field|metrics
specifier|private
specifier|final
name|Metrics
name|metrics
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|defaultTimeouts
specifier|private
specifier|final
name|Map
argument_list|<
name|ActionType
argument_list|,
name|Duration
argument_list|>
name|defaultTimeouts
decl_stmt|;
DECL|field|waitStrategy
specifier|private
specifier|final
name|WaitStrategy
name|waitStrategy
decl_stmt|;
DECL|field|overwriteDefaultRetryerStrategySetup
annotation|@
name|Nullable
specifier|private
specifier|final
name|Consumer
argument_list|<
name|RetryerBuilder
argument_list|<
name|?
argument_list|>
argument_list|>
name|overwriteDefaultRetryerStrategySetup
decl_stmt|;
annotation|@
name|Inject
DECL|method|RetryHelper ( @erritServerConfig Config cfg, Metrics metrics, BatchUpdate.AssistedFactory batchUpdateFactory)
name|RetryHelper
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|Metrics
name|metrics
parameter_list|,
name|BatchUpdate
operator|.
name|AssistedFactory
name|batchUpdateFactory
parameter_list|)
block|{
name|this
argument_list|(
name|cfg
argument_list|,
name|metrics
argument_list|,
name|batchUpdateFactory
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|RetryHelper ( @erritServerConfig Config cfg, Metrics metrics, BatchUpdate.AssistedFactory batchUpdateFactory, @Nullable Consumer<RetryerBuilder<?>> overwriteDefaultRetryerStrategySetup)
specifier|public
name|RetryHelper
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
name|Metrics
name|metrics
parameter_list|,
name|BatchUpdate
operator|.
name|AssistedFactory
name|batchUpdateFactory
parameter_list|,
annotation|@
name|Nullable
name|Consumer
argument_list|<
name|RetryerBuilder
argument_list|<
name|?
argument_list|>
argument_list|>
name|overwriteDefaultRetryerStrategySetup
parameter_list|)
block|{
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
operator|new
name|BatchUpdate
operator|.
name|Factory
argument_list|(
name|batchUpdateFactory
argument_list|)
expr_stmt|;
name|Duration
name|defaultTimeout
init|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|cfg
operator|.
name|getTimeUnit
argument_list|(
literal|"retry"
argument_list|,
literal|null
argument_list|,
literal|"timeout"
argument_list|,
name|SECONDS
operator|.
name|toMillis
argument_list|(
literal|20
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|)
decl_stmt|;
name|this
operator|.
name|defaultTimeouts
operator|=
name|Maps
operator|.
name|newEnumMap
argument_list|(
name|ActionType
operator|.
name|class
argument_list|)
expr_stmt|;
name|Arrays
operator|.
name|stream
argument_list|(
name|ActionType
operator|.
name|values
argument_list|()
argument_list|)
operator|.
name|forEach
argument_list|(
name|at
lambda|->
name|defaultTimeouts
operator|.
name|put
argument_list|(
name|at
argument_list|,
name|Duration
operator|.
name|ofMillis
argument_list|(
name|cfg
operator|.
name|getTimeUnit
argument_list|(
literal|"retry"
argument_list|,
name|at
operator|.
name|name
argument_list|()
argument_list|,
literal|"timeout"
argument_list|,
name|SECONDS
operator|.
name|toMillis
argument_list|(
name|defaultTimeout
operator|.
name|getSeconds
argument_list|()
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|waitStrategy
operator|=
name|WaitStrategies
operator|.
name|join
argument_list|(
name|WaitStrategies
operator|.
name|exponentialWait
argument_list|(
name|cfg
operator|.
name|getTimeUnit
argument_list|(
literal|"retry"
argument_list|,
literal|null
argument_list|,
literal|"maxWait"
argument_list|,
name|SECONDS
operator|.
name|toMillis
argument_list|(
literal|5
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|,
name|WaitStrategies
operator|.
name|randomWait
argument_list|(
literal|50
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|overwriteDefaultRetryerStrategySetup
operator|=
name|overwriteDefaultRetryerStrategySetup
expr_stmt|;
block|}
DECL|method|getDefaultTimeout (ActionType actionType)
specifier|public
name|Duration
name|getDefaultTimeout
parameter_list|(
name|ActionType
name|actionType
parameter_list|)
block|{
return|return
name|defaultTimeouts
operator|.
name|get
argument_list|(
name|actionType
argument_list|)
return|;
block|}
DECL|method|execute ( ActionType actionType, Action<T> action, Predicate<Throwable> exceptionPredicate)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ActionType
name|actionType
parameter_list|,
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|,
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|execute
argument_list|(
name|actionType
argument_list|,
name|action
argument_list|,
name|defaults
argument_list|()
argument_list|,
name|exceptionPredicate
argument_list|)
return|;
block|}
DECL|method|execute ( ActionType actionType, Action<T> action, Options opts, Predicate<Throwable> exceptionPredicate)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ActionType
name|actionType
parameter_list|,
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|,
name|Options
name|opts
parameter_list|,
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
return|return
name|executeWithAttemptAndTimeoutCount
argument_list|(
name|actionType
argument_list|,
name|action
argument_list|,
name|opts
argument_list|,
name|exceptionPredicate
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
DECL|method|execute (ChangeAction<T> changeAction)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ChangeAction
argument_list|<
name|T
argument_list|>
name|changeAction
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
return|return
name|execute
argument_list|(
name|changeAction
argument_list|,
name|defaults
argument_list|()
argument_list|)
return|;
block|}
DECL|method|execute (ChangeAction<T> changeAction, Options opts)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ChangeAction
argument_list|<
name|T
argument_list|>
name|changeAction
parameter_list|,
name|Options
name|opts
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
try|try
block|{
return|return
name|execute
argument_list|(
name|ActionType
operator|.
name|CHANGE_UPDATE
argument_list|,
parameter_list|()
lambda|->
name|changeAction
operator|.
name|call
argument_list|(
name|updateFactory
argument_list|)
argument_list|,
name|opts
argument_list|,
name|t
lambda|->
block|{
lambda|if (t instanceof UpdateException
argument_list|)
block|{
name|t
operator|=
name|t
operator|.
name|getCause
argument_list|()
block|;             }
return|return
name|t
operator|instanceof
name|LockFailureException
return|;
block|}
block|)
function|;
block|}
end_class

begin_catch
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
end_catch

begin_comment
unit|}
comment|/**    * Executes an action and records the number of attempts and the timeout as metrics.    *    * @param actionType the type of the action    * @param action the action which should be executed and retried on failure    * @param opts options for retrying the action on failure    * @param exceptionPredicate predicate to control on which exception the action should be retried    * @return the result of executing the action    * @throws Throwable any error or exception that made the action fail, callers are expected to    *     catch and inspect this Throwable to decide carefully whether it should be re-thrown    */
end_comment

begin_function
DECL|method|executeWithAttemptAndTimeoutCount ( ActionType actionType, Action<T> action, Options opts, Predicate<Throwable> exceptionPredicate)
unit|private
parameter_list|<
name|T
parameter_list|>
name|T
name|executeWithAttemptAndTimeoutCount
parameter_list|(
name|ActionType
name|actionType
parameter_list|,
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|,
name|Options
name|opts
parameter_list|,
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
throws|throws
name|Throwable
block|{
name|MetricListener
name|listener
init|=
operator|new
name|MetricListener
argument_list|()
decl_stmt|;
try|try
block|{
name|RetryerBuilder
argument_list|<
name|T
argument_list|>
name|retryerBuilder
init|=
name|createRetryerBuilder
argument_list|(
name|actionType
argument_list|,
name|opts
argument_list|,
name|exceptionPredicate
argument_list|)
decl_stmt|;
name|retryerBuilder
operator|.
name|withRetryListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
return|return
name|executeWithTimeoutCount
argument_list|(
name|actionType
argument_list|,
name|action
argument_list|,
name|retryerBuilder
operator|.
name|build
argument_list|()
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|listener
operator|.
name|getAttemptCount
argument_list|()
operator|>
literal|1
condition|)
block|{
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"%s was attempted %d times"
argument_list|,
name|actionType
argument_list|,
name|listener
operator|.
name|getAttemptCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|metrics
operator|.
name|attemptCounts
operator|.
name|record
argument_list|(
name|actionType
argument_list|,
name|listener
operator|.
name|getAttemptCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_comment
comment|/**    * Executes an action and records the timeout as metric.    *    * @param actionType the type of the action    * @param action the action which should be executed and retried on failure    * @param retryer the retryer    * @return the result of executing the action    * @throws Throwable any error or exception that made the action fail, callers are expected to    *     catch and inspect this Throwable to decide carefully whether it should be re-thrown    */
end_comment

begin_function
DECL|method|executeWithTimeoutCount (ActionType actionType, Action<T> action, Retryer<T> retryer)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|executeWithTimeoutCount
parameter_list|(
name|ActionType
name|actionType
parameter_list|,
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|,
name|Retryer
argument_list|<
name|T
argument_list|>
name|retryer
parameter_list|)
throws|throws
name|Throwable
block|{
try|try
block|{
return|return
name|retryer
operator|.
name|call
argument_list|(
name|action
operator|::
name|call
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|RetryException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|instanceof
name|RetryException
condition|)
block|{
name|metrics
operator|.
name|timeoutCount
operator|.
name|increment
argument_list|(
name|actionType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
end_function

begin_function
DECL|method|createRetryerBuilder ( ActionType actionType, Options opts, Predicate<Throwable> exceptionPredicate)
specifier|private
parameter_list|<
name|O
parameter_list|>
name|RetryerBuilder
argument_list|<
name|O
argument_list|>
name|createRetryerBuilder
parameter_list|(
name|ActionType
name|actionType
parameter_list|,
name|Options
name|opts
parameter_list|,
name|Predicate
argument_list|<
name|Throwable
argument_list|>
name|exceptionPredicate
parameter_list|)
block|{
name|RetryerBuilder
argument_list|<
name|O
argument_list|>
name|retryerBuilder
init|=
name|RetryerBuilder
operator|.
expr|<
name|O
operator|>
name|newBuilder
argument_list|()
operator|.
name|retryIfException
argument_list|(
name|exceptionPredicate
argument_list|)
decl_stmt|;
if|if
condition|(
name|opts
operator|.
name|listener
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|retryerBuilder
operator|.
name|withRetryListener
argument_list|(
name|opts
operator|.
name|listener
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|overwriteDefaultRetryerStrategySetup
operator|!=
literal|null
condition|)
block|{
name|overwriteDefaultRetryerStrategySetup
operator|.
name|accept
argument_list|(
name|retryerBuilder
argument_list|)
expr_stmt|;
return|return
name|retryerBuilder
return|;
block|}
return|return
name|retryerBuilder
operator|.
name|withStopStrategy
argument_list|(
name|StopStrategies
operator|.
name|stopAfterDelay
argument_list|(
name|firstNonNull
argument_list|(
name|opts
operator|.
name|timeout
argument_list|()
argument_list|,
name|getDefaultTimeout
argument_list|(
name|actionType
argument_list|)
argument_list|)
operator|.
name|toMillis
argument_list|()
argument_list|,
name|MILLISECONDS
argument_list|)
argument_list|)
operator|.
name|withWaitStrategy
argument_list|(
name|waitStrategy
argument_list|)
return|;
block|}
end_function

begin_class
DECL|class|MetricListener
specifier|private
specifier|static
class|class
name|MetricListener
implements|implements
name|RetryListener
block|{
DECL|field|attemptCount
specifier|private
name|long
name|attemptCount
decl_stmt|;
DECL|method|MetricListener ()
name|MetricListener
parameter_list|()
block|{
name|attemptCount
operator|=
literal|1
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onRetry (Attempt<V> attempt)
specifier|public
parameter_list|<
name|V
parameter_list|>
name|void
name|onRetry
parameter_list|(
name|Attempt
argument_list|<
name|V
argument_list|>
name|attempt
parameter_list|)
block|{
name|attemptCount
operator|=
name|attempt
operator|.
name|getAttemptNumber
argument_list|()
expr_stmt|;
block|}
DECL|method|getAttemptCount ()
name|long
name|getAttemptCount
parameter_list|()
block|{
return|return
name|attemptCount
return|;
block|}
block|}
end_class

unit|}
end_unit

