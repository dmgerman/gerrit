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
name|ImmutableSetMultimap
import|;
end_import

begin_comment
comment|/**  * Wrapper for a {@link Runnable} that copies the {@link LoggingContext} from the current thread to  * the thread that executes the runnable.  *  *<p>The state of the logging context that is copied to the thread that executes the runnable is  * fixed at the creation time of this wrapper. If the runnable is submitted to an executor and is  * executed later this means that changes that are done to the logging context in between creating  * and executing the runnable do not apply.  *  *<p>Example:  *  *<pre>  *   try (TraceContext traceContext = TraceContext.newTrace(true, ...)) {  *     executor  *         .submit(new LoggingContextAwareRunnable(  *             () -> {  *               // Tracing is enabled since the runnable is created within the TraceContext.  *               // Tracing is even enabled if the executor runs the runnable only after the  *               // TraceContext was closed.  *  *               // The tag "foo=bar" is not set, since it was added to the logging context only  *               // after this runnable was created.  *  *               // do stuff  *             }))  *         .get();  *     traceContext.addTag("foo", "bar");  *   }  *</pre>  *  * @see LoggingContextAwareCallable  */
end_comment

begin_class
DECL|class|LoggingContextAwareRunnable
specifier|public
class|class
name|LoggingContextAwareRunnable
implements|implements
name|Runnable
block|{
DECL|field|runnable
specifier|private
specifier|final
name|Runnable
name|runnable
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
DECL|method|LoggingContextAwareRunnable (Runnable runnable)
name|LoggingContextAwareRunnable
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|this
operator|.
name|runnable
operator|=
name|runnable
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
block|}
DECL|method|unwrap ()
specifier|public
name|Runnable
name|unwrap
parameter_list|()
block|{
return|return
name|runnable
return|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
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
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
return|return;
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
try|try
block|{
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
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
block|}
block|}
block|}
end_class

end_unit

