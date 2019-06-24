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
name|Iterables
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
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|registration
operator|.
name|Extension
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

begin_comment
comment|/**  * Context for capturing performance log records. When the context is closed the performance log  * records are handed over to the registered {@link PerformanceLogger}s.  *  *<p>Capturing performance log records is disabled if there are no {@link PerformanceLogger}  * registered (in this case the captured performance log records would never be used).  *  *<p>It's important to enable capturing of performance log records in a context that ensures to  * consume the captured performance log records. Otherwise captured performance log records might  * leak into other requests that are executed by the same thread (if a thread pool is used to  * process requests).  */
end_comment

begin_class
DECL|class|PerformanceLogContext
specifier|public
class|class
name|PerformanceLogContext
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
comment|// Do not use PluginSetContext. PluginSetContext traces the plugin latency with a timer metric
comment|// which would result in a performance log and we don't want to log the performance of writing
comment|// a performance log in the performance log (endless loop).
DECL|field|performanceLoggers
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PerformanceLogger
argument_list|>
name|performanceLoggers
decl_stmt|;
DECL|field|oldPerformanceLogging
specifier|private
specifier|final
name|boolean
name|oldPerformanceLogging
decl_stmt|;
DECL|field|oldPerformanceLogRecords
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|oldPerformanceLogRecords
decl_stmt|;
DECL|method|PerformanceLogContext ( Config gerritConfig, DynamicSet<PerformanceLogger> performanceLoggers)
specifier|public
name|PerformanceLogContext
parameter_list|(
name|Config
name|gerritConfig
parameter_list|,
name|DynamicSet
argument_list|<
name|PerformanceLogger
argument_list|>
name|performanceLoggers
parameter_list|)
block|{
name|this
operator|.
name|performanceLoggers
operator|=
name|performanceLoggers
expr_stmt|;
comment|// Just in case remember the old state and reset performance log entries.
name|this
operator|.
name|oldPerformanceLogging
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
name|oldPerformanceLogRecords
operator|=
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getPerformanceLogRecords
argument_list|()
expr_stmt|;
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|clearPerformanceLogEntries
argument_list|()
expr_stmt|;
comment|// Do not create performance log entries if performance logging is disabled or if no
comment|// PerformanceLogger is registered.
name|boolean
name|enablePerformanceLogging
init|=
name|gerritConfig
operator|.
name|getBoolean
argument_list|(
literal|"tracing"
argument_list|,
literal|"performanceLogging"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|performanceLogging
argument_list|(
name|enablePerformanceLogging
operator|&&
operator|!
name|Iterables
operator|.
name|isEmpty
argument_list|(
name|performanceLoggers
operator|.
name|entries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|isPerformanceLogging
argument_list|()
condition|)
block|{
name|runEach
argument_list|(
name|performanceLoggers
argument_list|,
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|getPerformanceLogRecords
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Restore old state. Required to support nesting of PerformanceLogContext's.
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|performanceLogging
argument_list|(
name|oldPerformanceLogging
argument_list|)
expr_stmt|;
name|LoggingContext
operator|.
name|getInstance
argument_list|()
operator|.
name|setPerformanceLogRecords
argument_list|(
name|oldPerformanceLogRecords
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invokes all performance loggers.    *    *<p>Similar to how {@code com.google.gerrit.server.plugincontext.PluginContext} invokes plugins    * but without recording metrics for invoking {@link PerformanceLogger}s.    *    * @param performanceLoggers the performance loggers that should be invoked    * @param performanceLogRecords the performance log records that should be handed over to the    *     performance loggers    */
DECL|method|runEach ( DynamicSet<PerformanceLogger> performanceLoggers, ImmutableList<PerformanceLogRecord> performanceLogRecords)
specifier|private
specifier|static
name|void
name|runEach
parameter_list|(
name|DynamicSet
argument_list|<
name|PerformanceLogger
argument_list|>
name|performanceLoggers
parameter_list|,
name|ImmutableList
argument_list|<
name|PerformanceLogRecord
argument_list|>
name|performanceLogRecords
parameter_list|)
block|{
name|performanceLoggers
operator|.
name|entries
argument_list|()
operator|.
name|forEach
argument_list|(
name|p
lambda|->
block|{
try|try
init|(
name|TraceContext
name|traceContext
init|=
name|newPluginTrace
argument_list|(
name|p
argument_list|)
init|)
block|{
name|performanceLogRecords
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|r
operator|.
name|writeTo
argument_list|(
name|p
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Failure in %s of plugin %s"
argument_list|,
name|p
operator|.
name|get
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|p
operator|.
name|getPluginName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**    * Opens a trace context for a plugin that implements {@link PerformanceLogger}.    *    *<p>Basically the same as {@code    * com.google.gerrit.server.plugincontext.PluginContext#newTrace(Extension<T>)}. We have this    * method here to avoid a dependency on PluginContext which lives in    * "//java/com/google/gerrit/server". This package ("//java/com/google/gerrit/server/logging")    * should have as few dependencies as possible.    *    * @param extension performance logger extension    * @return the trace context    */
DECL|method|newPluginTrace (Extension<PerformanceLogger> extension)
specifier|private
specifier|static
name|TraceContext
name|newPluginTrace
parameter_list|(
name|Extension
argument_list|<
name|PerformanceLogger
argument_list|>
name|extension
parameter_list|)
block|{
return|return
name|TraceContext
operator|.
name|open
argument_list|()
operator|.
name|addPluginTag
argument_list|(
name|extension
operator|.
name|getPluginName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

