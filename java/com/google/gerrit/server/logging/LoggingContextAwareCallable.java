begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
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
name|collect
operator|.
name|ImmutableList
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
name|ImmutableSetMultimap
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
comment|/**  * Wrapper for a {@link Callable} that copies the {@link LoggingContext} from the current thread to  * the thread that executes the callable.  *  *<p>The state of the logging context that is copied to the thread that executes the callable is  * fixed at the creation time of this wrapper. If the callable is submitted to an executor and is  * executed later this means that changes that are done to the logging context in between creating  * and executing the callable do not apply.  *  *<p>See {@link LoggingContextAwareRunnable} for an example.  *  * @see LoggingContextAwareRunnable  */
end_comment

begin_class
DECL|class|LoggingContextAwareCallable
class|class
name|LoggingContextAwareCallable
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Callable
argument_list|<
name|T
argument_list|>
block|{
DECL|field|callable
specifier|private
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
decl_stmt|;
DECL|field|callingThread
specifier|private
specifier|final
name|Thread
name|callingThread
decl_stmt|;
DECL|field|tags
specifier|private
specifier|final
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tags
decl_stmt|;
DECL|field|forceLogging
specifier|private
specifier|final
name|boolean
name|forceLogging
decl_stmt|;
DECL|field|performanceLogging
specifier|private
specifier|final
name|boolean
name|performanceLogging
decl_stmt|;
DECL|field|mutablePerformanceLogRecords
specifier|private
specifier|final
name|MutablePerformanceLogRecords
name|mutablePerformanceLogRecords
decl_stmt|;
comment|/**    * Creates a LoggingContextAwareCallable that wraps the given {@link Callable}.    *    * @param callable Callable that should be wrapped.    * @param mutablePerformanceLogRecords instance of {@link MutablePerformanceLogRecords} to which    *     performance log records that are created from the runnable are added    */
DECL|method|LoggingContextAwareCallable ( Callable<T> callable, MutablePerformanceLogRecords mutablePerformanceLogRecords)
name|LoggingContextAwareCallable
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|,
name|MutablePerformanceLogRecords
name|mutablePerformanceLogRecords
parameter_list|)
block|{
name|this
operator|.
name|callable
operator|=
name|callable
expr_stmt|;
name|this
operator|.
name|callingThread
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
expr_stmt|;
name|this
operator|.
name|tags
operator|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getTagsAsMap
argument_list|()
expr_stmt|;
name|this
operator|.
name|forceLogging
operator|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|isLoggingForced
argument_list|()
expr_stmt|;
name|this
operator|.
name|performanceLogging
operator|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|isPerformanceLogging
argument_list|()
expr_stmt|;
name|this
operator|.
name|mutablePerformanceLogRecords
operator|=
name|mutablePerformanceLogRecords
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|callingThread
operator|.
name|equals
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
condition|)
block|{
comment|// propagation of logging context is not needed
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
comment|// propagate logging context
name|LoggingContext
name|loggingCtx
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|oldTags
init|=
name|loggingCtx
operator|.
name|getTagsAsMap
argument_list|()
decl_stmt|;
name|boolean
name|oldForceLogging
init|=
name|loggingCtx
operator|.
name|isLoggingForced
argument_list|()
decl_stmt|;
name|boolean
name|oldPerformanceLogging
init|=
name|loggingCtx
operator|.
name|isPerformanceLogging
argument_list|()
decl_stmt|;
name|ImmutableList
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|oldPerformanceLogRecords
init|=
name|loggingCtx
operator|.
name|getPerformanceLogRecords
argument_list|()
decl_stmt|;
name|loggingCtx
operator|.
name|setTags
argument_list|(
name|tags
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|forceLogging
argument_list|(
name|forceLogging
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|performanceLogging
argument_list|(
name|performanceLogging
argument_list|)
expr_stmt|;
comment|// For the performance log records use the {@link MutablePerformanceLogRecords} instance from
comment|// the logging context of the calling thread in the logging context of the new thread. This way
comment|// performance log records that are created from the new thread are available from the logging
comment|// context of the calling thread. This is important since performance log records are processed
comment|// only at the end of the request and performance log records that are created in another thread
comment|// should not get lost.
name|loggingCtx
operator|.
name|setMutablePerformanceLogRecords
argument_list|(
name|mutablePerformanceLogRecords
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|callable
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
name|loggingCtx
operator|.
name|setTags
argument_list|(
name|oldTags
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|forceLogging
argument_list|(
name|oldForceLogging
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|performanceLogging
argument_list|(
name|oldPerformanceLogging
argument_list|)
expr_stmt|;
name|loggingCtx
operator|.
name|setPerformanceLogRecords
argument_list|(
name|oldPerformanceLogRecords
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

