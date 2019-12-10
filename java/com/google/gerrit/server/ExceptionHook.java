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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

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
name|annotations
operator|.
name|ExtensionPoint
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

begin_comment
comment|/**  * Allows implementors to control how certain exceptions should be handled.  *  *<p>This interface is intended to be implemented for multi-master setups to control the behavior  * for handling exceptions that are thrown by a lower layer that handles the consensus and  * synchronization between different server nodes. E.g. if an operation fails because consensus for  * a Git update could not be achieved (e.g. due to slow responding server nodes) this interface can  * be used to retry the request instead of failing it immediately.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ExceptionHook
specifier|public
interface|interface
name|ExceptionHook
block|{
comment|/**    * Whether an operation should be retried if it failed with the given throwable.    *    *<p>Only affects operations that are executed with {@link    * com.google.gerrit.server.update.RetryHelper}.    *    *<p>Should return {@code true} only for exceptions that are caused by temporary issues where a    * retry of the operation has a chance to succeed.    *    *<p>If {@code false} is returned the operation is still retried once to capture a trace, unless    * {@link #skipRetryWithTrace(Throwable)} skips the auto-retry.    *    * @param throwable throwable that was thrown while executing the operation    * @return whether the operation should be retried    */
DECL|method|shouldRetry (Throwable throwable)
specifier|default
name|boolean
name|shouldRetry
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
comment|/**    * Whether auto-retrying of an operation with tracing should be skipped for the given throwable.    *    *<p>Only affects operations that are executed with {@link    * com.google.gerrit.server.update.RetryHelper}.    *    *<p>This method is only called for exceptions for which the operation should not be retried    * ({@link #shouldRetry(Throwable)} returned {@code false}).    *    *<p>By default this method returns {@code false}, so that by default traces for unexpected    * exceptions are captured, which allows to investigate them.    *    *<p>Implementors may use this method to skip retry with tracing for exceptions that occur due to    * known causes that are permanent and where a trace is not needed for the investigation. For    * example, if an operation fails because persisted data is corrupt, it makes no sense to retry    * the operation with a trace, because the trace will not help with fixing the corrupt data.    *    *<p>This method is only invoked if retry with tracing is enabled on the server ({@code    * retry.retryWithTraceOnFailure} in {@code gerrit.config} is set to {@code true}).    *    * @param throwable throwable that was thrown while executing the operation    * @return whether auto-retrying of an operation with tracing should be skipped for the given    *     throwable    */
DECL|method|skipRetryWithTrace (Throwable throwable)
specifier|default
name|boolean
name|skipRetryWithTrace
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
comment|/**    * Formats the cause of an exception for use in metrics.    *    *<p>This method allows implementors to group exceptions that have the same cause into one metric    * bucket.    *    * @param throwable the exception cause    * @return formatted cause or {@link Optional#empty()} if no formatting was done    */
DECL|method|formatCause (Throwable throwable)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|formatCause
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**    * Returns a message that should be returned to the user.    *    *<p>This message is included into the HTTP response that is sent to the user.    *    * @param throwable throwable that was thrown while executing an operation    * @return error message that should be returned to the user, {@link Optional#empty()} if no    *     message should be returned to the user    */
DECL|method|getUserMessage (Throwable throwable)
specifier|default
name|Optional
argument_list|<
name|String
argument_list|>
name|getUserMessage
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
comment|/**    * Returns the HTTP status code that should be returned to the user.    *    *<p>If no value is returned ({@link Optional#empty()}) the HTTP status code defaults to {@code    * 500 Internal Server Error}.    *    *<p>{@link #getUserMessage(Throwable)} allows to define which message should be included into    * the body of the HTTP response.    *    *<p>Implementors may use this method to change the status code for certain exceptions (e.g.    * using this method it would be possible to return {@code 409 Conflict} for {@link    * com.google.gerrit.git.LockFailureException}s instead of {@code 500 Internal Server Error}).    *    * @param throwable throwable that was thrown while executing an operation    * @return HTTP status code that should be returned to the user, {@link Optional#empty()} if the    *     exception should result in {@code 500 Internal Server Error}    */
DECL|method|getStatusCode (Throwable throwable)
specifier|default
name|Optional
argument_list|<
name|Integer
argument_list|>
name|getStatusCode
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
block|}
end_interface

end_unit

