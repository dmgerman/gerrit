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
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Stopwatch
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
name|Strings
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
name|HashBasedTable
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
name|ImmutableMap
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
name|Table
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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|TimeUnit
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

begin_comment
comment|/**  * TraceContext that allows to set logging tags and enforce logging.  *  *<p>The logging tags are attached to all log entries that are triggered while the trace context is  * open. If force logging is enabled all logs that are triggered while the trace context is open are  * written to the log file regardless of the configured log level.  *  *<pre>  * try (TraceContext traceContext = TraceContext.open()  *         .addTag("tag-name", "tag-value")  *         .forceLogging()) {  *     // This gets logged as: A log [CONTEXT forced=true tag-name="tag-value" ]  *     // Since force logging is enabled this gets logged independently of the configured log  *     // level.  *     logger.atFinest().log("A log");  *  *     // do stuff  * }  *</pre>  *  *<p>The logging tags and the force logging flag are stored in the {@link LoggingContext}. {@link  * LoggingContextAwareExecutorService}, {@link LoggingContextAwareScheduledExecutorService} and the  * executor in {@link com.google.gerrit.server.git.WorkQueue} ensure that the logging context is  * automatically copied to background threads.  *  *<p>On close of the trace context newly set tags are unset. Force logging is disabled on close if  * it got enabled while the trace context was open.  *  *<p>Trace contexts can be nested:  *  *<pre>  * // Initially there are no tags  * logger.atSevere().log("log without tag");  *  * // a tag can be set by opening a trace context  * try (TraceContext ctx = TraceContext.open().addTag("tag1", "value1")) {  *   logger.atSevere().log("log with tag1=value1");  *  *   // while a trace context is open further tags can be added.  *   ctx.addTag("tag2", "value2")  *   logger.atSevere().log("log with tag1=value1 and tag2=value2");  *  *   // also by opening another trace context a another tag can be added  *   try (TraceContext ctx2 = TraceContext.open().addTag("tag3", "value3")) {  *     logger.atSevere().log("log with tag1=value1, tag2=value2 and tag3=value3");  *  *     // it's possible to have the same tag name with multiple values  *     ctx2.addTag("tag3", "value3a")  *     logger.atSevere().log("log with tag1=value1, tag2=value2, tag3=value3 and tag3=value3a");  *  *     // adding a tag with the same name and value as an existing tag has no effect  *     try (TraceContext ctx3 = TraceContext.open().addTag("tag3", "value3a")) {  *       logger.atSevere().log("log with tag1=value1, tag2=value2, tag3=value3 and tag3=value3a");  *     }  *  *     // closing ctx3 didn't remove tag3=value3a since it was already set before opening ctx3  *     logger.atSevere().log("log with tag1=value1, tag2=value2, tag3=value3 and tag3=value3a");  *   }  *  *   // closing ctx2 removed tag3=value3 and tag3-value3a  *   logger.atSevere().log("with tag1=value1 and tag2=value2");  * }  *  * // closing ctx1 removed tag1=value1 and tag2=value2  * logger.atSevere().log("log without tag");  *</pre>  */
end_comment

begin_class
DECL|class|TraceContext
specifier|public
class|class
name|TraceContext
implements|implements
name|AutoCloseable
block|{
DECL|field|PLUGIN_TAG
specifier|private
specifier|static
specifier|final
name|String
name|PLUGIN_TAG
init|=
literal|"PLUGIN"
decl_stmt|;
DECL|method|open ()
specifier|public
specifier|static
name|TraceContext
name|open
parameter_list|()
block|{
return|return
operator|new
name|TraceContext
argument_list|()
return|;
block|}
comment|/**    * Opens a new trace context for request tracing.    *    *<ul>    *<li>sets a tag with a trace ID    *<li>enables force logging    *</ul>    *    *<p>if no trace ID is provided a new trace ID is only generated if request tracing was not    * started yet. If request tracing was already started the given {@code traceIdConsumer} is    * invoked with the existing trace ID and no new logging tag is set.    *    *<p>No-op if {@code trace} is {@code false}.    *    * @param trace whether tracing should be started    * @param traceId trace ID that should be used for tracing, if {@code null} a trace ID is    *     generated    * @param traceIdConsumer consumer for the trace ID, should be used to return the generated trace    *     ID to the client, not invoked if {@code trace} is {@code false}    * @return the trace context    */
DECL|method|newTrace ( boolean trace, @Nullable String traceId, TraceIdConsumer traceIdConsumer)
specifier|public
specifier|static
name|TraceContext
name|newTrace
parameter_list|(
name|boolean
name|trace
parameter_list|,
annotation|@
name|Nullable
name|String
name|traceId
parameter_list|,
name|TraceIdConsumer
name|traceIdConsumer
parameter_list|)
block|{
if|if
condition|(
operator|!
name|trace
condition|)
block|{
comment|// Create an empty trace context.
return|return
name|open
argument_list|()
return|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|traceId
argument_list|)
condition|)
block|{
name|traceIdConsumer
operator|.
name|accept
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
operator|.
name|name
argument_list|()
argument_list|,
name|traceId
argument_list|)
expr_stmt|;
return|return
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
argument_list|,
name|traceId
argument_list|)
operator|.
name|forceLogging
argument_list|()
return|;
block|}
name|Optional
argument_list|<
name|String
argument_list|>
name|existingTraceId
init|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getTagsAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|findAny
argument_list|()
decl_stmt|;
if|if
condition|(
name|existingTraceId
operator|.
name|isPresent
argument_list|()
condition|)
block|{
comment|// request tracing was already started, no need to generate a new trace ID
name|traceIdConsumer
operator|.
name|accept
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
operator|.
name|name
argument_list|()
argument_list|,
name|existingTraceId
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|open
argument_list|()
return|;
block|}
name|RequestId
name|newTraceId
init|=
operator|new
name|RequestId
argument_list|()
decl_stmt|;
name|traceIdConsumer
operator|.
name|accept
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
operator|.
name|name
argument_list|()
argument_list|,
name|newTraceId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|open
argument_list|()
operator|.
name|addTag
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
argument_list|,
name|newTraceId
argument_list|)
operator|.
name|forceLogging
argument_list|()
return|;
block|}
annotation|@
name|FunctionalInterface
DECL|interface|TraceIdConsumer
specifier|public
interface|interface
name|TraceIdConsumer
block|{
DECL|method|accept (String tagName, String traceId)
name|void
name|accept
parameter_list|(
name|String
name|tagName
parameter_list|,
name|String
name|traceId
parameter_list|)
function_decl|;
block|}
comment|/**    * Opens a new timer that logs the time for an operation if request tracing is enabled.    *    * @param operation the name of operation the is being performed    * @return the trace timer    */
DECL|method|newTimer (String operation)
specifier|public
specifier|static
name|TraceTimer
name|newTimer
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
return|return
operator|new
name|TraceTimer
argument_list|(
name|requireNonNull
argument_list|(
name|operation
argument_list|,
literal|"operation is required"
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Opens a new timer that logs the time for an operation if request tracing is enabled.    *    * @param operation the name of operation the is being performed    * @param metadata metadata    * @return the trace timer    */
DECL|method|newTimer (String operation, Metadata metadata)
specifier|public
specifier|static
name|TraceTimer
name|newTimer
parameter_list|(
name|String
name|operation
parameter_list|,
name|Metadata
name|metadata
parameter_list|)
block|{
return|return
operator|new
name|TraceTimer
argument_list|(
name|requireNonNull
argument_list|(
name|operation
argument_list|,
literal|"operation is required"
argument_list|)
argument_list|,
name|requireNonNull
argument_list|(
name|metadata
argument_list|,
literal|"metadata is required"
argument_list|)
argument_list|)
return|;
block|}
DECL|class|TraceTimer
specifier|public
specifier|static
class|class
name|TraceTimer
implements|implements
name|AutoCloseable
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
DECL|field|doneLogFn
specifier|private
specifier|final
name|Consumer
argument_list|<
name|Long
argument_list|>
name|doneLogFn
decl_stmt|;
DECL|field|stopwatch
specifier|private
specifier|final
name|Stopwatch
name|stopwatch
decl_stmt|;
DECL|method|TraceTimer (String operation)
specifier|private
name|TraceTimer
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
argument_list|(
parameter_list|()
lambda|->
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Starting timer for %s"
argument_list|,
name|operation
argument_list|)
argument_list|,
name|elapsedMs
lambda|->
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addPerformanceLogRecord
argument_list|(
parameter_list|()
lambda|->
name|PerformanceLogRecord
operator|.
name|create
argument_list|(
name|operation
argument_list|,
name|elapsedMs
argument_list|)
argument_list|)
argument_list|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"%s done (%d ms)"
argument_list|,
name|operation
argument_list|,
name|elapsedMs
argument_list|)
argument_list|;
block|}
block|)
class|;
block|}
end_class

begin_constructor
DECL|method|TraceTimer (String operation, Metadata metadata)
specifier|private
name|TraceTimer
parameter_list|(
name|String
name|operation
parameter_list|,
name|Metadata
name|metadata
parameter_list|)
block|{
name|this
argument_list|(
parameter_list|()
lambda|->
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Starting timer for %s (%s)"
argument_list|,
name|operation
argument_list|,
name|metadata
operator|.
name|toStringForLoggingLazy
argument_list|()
argument_list|)
argument_list|,
name|elapsedMs
lambda|->
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addPerformanceLogRecord
argument_list|(
parameter_list|()
lambda|->
name|PerformanceLogRecord
operator|.
name|create
argument_list|(
name|operation
argument_list|,
name|elapsedMs
argument_list|,
name|metadata
argument_list|)
argument_list|)
argument_list|;
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"%s (%s) done (%d ms)"
argument_list|,
name|operation
argument_list|,
name|metadata
operator|.
name|toStringForLoggingLazy
argument_list|()
argument_list|,
name|elapsedMs
argument_list|)
argument_list|;
block|}
end_constructor

begin_empty_stmt
unit|)
empty_stmt|;
end_empty_stmt

begin_expr_stmt
unit|}      private
DECL|method|TraceTimer (Runnable startLogFn, Consumer<Long> doneLogFn)
name|TraceTimer
argument_list|(
name|Runnable
name|startLogFn
argument_list|,
name|Consumer
argument_list|<
name|Long
argument_list|>
name|doneLogFn
argument_list|)
block|{
name|startLogFn
operator|.
name|run
argument_list|()
block|;
name|this
operator|.
name|doneLogFn
operator|=
name|doneLogFn
block|;
name|this
operator|.
name|stopwatch
operator|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
block|;     }
expr|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
argument_list|()
block|{
name|stopwatch
operator|.
name|stop
argument_list|()
block|;
name|doneLogFn
operator|.
name|accept
argument_list|(
name|stopwatch
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
block|;     }
end_expr_stmt

begin_comment
unit|}
comment|// Table<TAG_NAME, TAG_VALUE, REMOVE_ON_CLOSE>
end_comment

begin_decl_stmt
DECL|field|tags
unit|private
specifier|final
name|Table
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|Boolean
argument_list|>
name|tags
init|=
name|HashBasedTable
operator|.
name|create
argument_list|()
decl_stmt|;
end_decl_stmt

begin_decl_stmt
DECL|field|stopForceLoggingOnClose
specifier|private
name|boolean
name|stopForceLoggingOnClose
decl_stmt|;
end_decl_stmt

begin_constructor
DECL|method|TraceContext ()
specifier|private
name|TraceContext
parameter_list|()
block|{}
end_constructor

begin_function
DECL|method|addTag (RequestId.Type requestId, Object tagValue)
specifier|public
name|TraceContext
name|addTag
parameter_list|(
name|RequestId
operator|.
name|Type
name|requestId
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
return|return
name|addTag
argument_list|(
name|requireNonNull
argument_list|(
name|requestId
argument_list|,
literal|"request ID is required"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
name|tagValue
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|addTag (String tagName, Object tagValue)
specifier|public
name|TraceContext
name|addTag
parameter_list|(
name|String
name|tagName
parameter_list|,
name|Object
name|tagValue
parameter_list|)
block|{
name|String
name|name
init|=
name|requireNonNull
argument_list|(
name|tagName
argument_list|,
literal|"tag name is required"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|requireNonNull
argument_list|(
name|tagValue
argument_list|,
literal|"tag value is required"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|tags
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|addTag
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
end_function

begin_function
DECL|method|getTags ()
specifier|public
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getTags
parameter_list|()
block|{
name|ImmutableMap
operator|.
name|Builder
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tagMap
init|=
name|ImmutableMap
operator|.
name|builder
argument_list|()
decl_stmt|;
name|tags
operator|.
name|cellSet
argument_list|()
operator|.
name|forEach
argument_list|(
name|c
lambda|->
name|tagMap
operator|.
name|put
argument_list|(
name|c
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|c
operator|.
name|getColumnKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|tagMap
operator|.
name|build
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|addPluginTag (String pluginName)
specifier|public
name|TraceContext
name|addPluginTag
parameter_list|(
name|String
name|pluginName
parameter_list|)
block|{
return|return
name|addTag
argument_list|(
name|PLUGIN_TAG
argument_list|,
name|pluginName
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|forceLogging ()
specifier|public
name|TraceContext
name|forceLogging
parameter_list|()
block|{
if|if
condition|(
name|stopForceLoggingOnClose
condition|)
block|{
return|return
name|this
return|;
block|}
name|stopForceLoggingOnClose
operator|=
operator|!
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|forceLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
end_function

begin_function
DECL|method|isTracing ()
specifier|public
name|boolean
name|isTracing
parameter_list|()
block|{
return|return
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|isLoggingForced
argument_list|()
return|;
block|}
end_function

begin_function
DECL|method|getTraceId ()
specifier|public
name|Optional
argument_list|<
name|String
argument_list|>
name|getTraceId
parameter_list|()
block|{
return|return
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getTagsAsMap
argument_list|()
operator|.
name|get
argument_list|(
name|RequestId
operator|.
name|Type
operator|.
name|TRACE_ID
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|findFirst
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
for|for
control|(
name|Table
operator|.
name|Cell
argument_list|<
name|String
argument_list|,
name|String
argument_list|,
name|Boolean
argument_list|>
name|cell
range|:
name|tags
operator|.
name|cellSet
argument_list|()
control|)
block|{
if|if
condition|(
name|cell
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|removeTag
argument_list|(
name|cell
operator|.
name|getRowKey
argument_list|()
argument_list|,
name|cell
operator|.
name|getColumnKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|stopForceLoggingOnClose
condition|)
block|{
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|forceLogging
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_function

unit|}
end_unit

