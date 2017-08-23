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
name|metrics
operator|.
name|Counter0
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
name|Histogram0
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
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|notedb
operator|.
name|NotesMigration
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
name|concurrent
operator|.
name|ExecutionException
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
DECL|interface|Action
specifier|public
interface|interface
name|Action
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|call (BatchUpdate.Factory updateFactory)
name|T
name|call
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|)
throws|throws
name|Exception
function_decl|;
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
name|Singleton
DECL|class|Metrics
specifier|private
specifier|static
class|class
name|Metrics
block|{
DECL|field|attemptCounts
specifier|final
name|Histogram0
name|attemptCounts
decl_stmt|;
DECL|field|timeoutCount
specifier|final
name|Counter0
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
name|attemptCounts
operator|=
name|metricMaker
operator|.
name|newHistogram
argument_list|(
literal|"batch_update/retry_attempt_counts"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Distribution of number of attempts made by RetryHelper"
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
argument_list|)
expr_stmt|;
name|timeoutCount
operator|=
name|metricMaker
operator|.
name|newCounter
argument_list|(
literal|"batch_update/retry_timeout_count"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Number of executions of RetryHelper that ultimately timed out"
argument_list|)
operator|.
name|setCumulative
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"timeouts"
argument_list|)
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
specifier|public
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
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
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
DECL|field|defaultTimeout
specifier|private
specifier|final
name|Duration
name|defaultTimeout
decl_stmt|;
DECL|field|waitStrategy
specifier|private
specifier|final
name|WaitStrategy
name|waitStrategy
decl_stmt|;
annotation|@
name|Inject
DECL|method|RetryHelper ( @erritServerConfig Config cfg, Metrics metrics, NotesMigration migration, ReviewDbBatchUpdate.AssistedFactory reviewDbBatchUpdateFactory, NoteDbBatchUpdate.AssistedFactory noteDbBatchUpdateFactory)
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
name|NotesMigration
name|migration
parameter_list|,
name|ReviewDbBatchUpdate
operator|.
name|AssistedFactory
name|reviewDbBatchUpdateFactory
parameter_list|,
name|NoteDbBatchUpdate
operator|.
name|AssistedFactory
name|noteDbBatchUpdateFactory
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
name|migration
operator|=
name|migration
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
name|migration
argument_list|,
name|reviewDbBatchUpdateFactory
argument_list|,
name|noteDbBatchUpdateFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|defaultTimeout
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
name|cfg
operator|.
name|getTimeUnit
argument_list|(
literal|"noteDb"
argument_list|,
literal|null
argument_list|,
literal|"retryTimeout"
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
literal|"noteDb"
argument_list|,
literal|null
argument_list|,
literal|"retryMaxWait"
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
block|}
DECL|method|getDefaultTimeout ()
specifier|public
name|Duration
name|getDefaultTimeout
parameter_list|()
block|{
return|return
name|defaultTimeout
return|;
block|}
DECL|method|execute (Action<T> action)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
return|return
name|execute
argument_list|(
name|action
argument_list|,
name|defaults
argument_list|()
argument_list|)
return|;
block|}
DECL|method|execute (Action<T> action, Options opts)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|Action
argument_list|<
name|T
argument_list|>
name|action
parameter_list|,
name|Options
name|opts
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
name|MetricListener
name|listener
init|=
literal|null
decl_stmt|;
try|try
block|{
name|RetryerBuilder
argument_list|<
name|T
argument_list|>
name|builder
init|=
name|RetryerBuilder
operator|.
name|newBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|migration
operator|.
name|disableChangeReviewDb
argument_list|()
condition|)
block|{
name|listener
operator|=
operator|new
name|MetricListener
argument_list|(
name|opts
operator|.
name|listener
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|withRetryListener
argument_list|(
name|listener
argument_list|)
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
name|defaultTimeout
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
operator|.
name|retryIfException
argument_list|(
name|RetryHelper
operator|::
name|isLockFailure
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Either we aren't full-NoteDb, or the underlying ref storage doesn't support atomic
comment|// transactions. Either way, retrying a partially-failed operation is not idempotent, so
comment|// don't do it automatically. Let the end user decide whether they want to retry.
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
operator|.
name|call
argument_list|(
parameter_list|()
lambda|->
name|action
operator|.
name|call
argument_list|(
name|updateFactory
argument_list|)
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
argument_list|()
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
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
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
name|e
operator|.
name|getCause
argument_list|()
argument_list|,
name|RestApiException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|UpdateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|listener
operator|!=
literal|null
condition|)
block|{
name|metrics
operator|.
name|attemptCounts
operator|.
name|record
argument_list|(
name|listener
operator|.
name|getAttemptCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isLockFailure (Throwable t)
specifier|private
specifier|static
name|boolean
name|isLockFailure
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|t
operator|instanceof
name|UpdateException
condition|)
block|{
name|t
operator|=
name|t
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
return|return
name|t
operator|instanceof
name|LockFailureException
return|;
block|}
DECL|class|MetricListener
specifier|private
specifier|static
class|class
name|MetricListener
implements|implements
name|RetryListener
block|{
DECL|field|delegate
specifier|private
specifier|final
name|RetryListener
name|delegate
decl_stmt|;
DECL|field|attemptCount
specifier|private
name|long
name|attemptCount
decl_stmt|;
DECL|method|MetricListener (@ullable RetryListener delegate)
name|MetricListener
parameter_list|(
annotation|@
name|Nullable
name|RetryListener
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
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
if|if
condition|(
name|delegate
operator|!=
literal|null
condition|)
block|{
name|delegate
operator|.
name|onRetry
argument_list|(
name|attempt
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

