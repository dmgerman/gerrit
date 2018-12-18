begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|OutOfScopeException
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
name|Scope
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
comment|/**  * {@link RequestScopePropagator} implementation for request scopes based on a {@link ThreadLocal}  * context.  *  * @param<C> "context" type stored in the {@link ThreadLocal}.  */
end_comment

begin_class
DECL|class|ThreadLocalRequestScopePropagator
specifier|public
specifier|abstract
class|class
name|ThreadLocalRequestScopePropagator
parameter_list|<
name|C
parameter_list|>
extends|extends
name|RequestScopePropagator
block|{
DECL|field|threadLocal
specifier|private
specifier|final
name|ThreadLocal
argument_list|<
name|C
argument_list|>
name|threadLocal
decl_stmt|;
DECL|method|ThreadLocalRequestScopePropagator ( Scope scope, ThreadLocal<C> threadLocal, ThreadLocalRequestContext local)
specifier|protected
name|ThreadLocalRequestScopePropagator
parameter_list|(
name|Scope
name|scope
parameter_list|,
name|ThreadLocal
argument_list|<
name|C
argument_list|>
name|threadLocal
parameter_list|,
name|ThreadLocalRequestContext
name|local
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|,
name|local
argument_list|)
expr_stmt|;
name|this
operator|.
name|threadLocal
operator|=
name|threadLocal
expr_stmt|;
block|}
comment|/** @see RequestScopePropagator#wrap(Callable) */
annotation|@
name|Override
DECL|method|wrapImpl (Callable<T> callable)
specifier|protected
specifier|final
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|wrapImpl
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
name|C
name|ctx
init|=
name|continuingContext
argument_list|(
name|requireContext
argument_list|()
argument_list|)
decl_stmt|;
return|return
parameter_list|()
lambda|->
block|{
name|C
name|old
init|=
name|threadLocal
operator|.
name|get
argument_list|()
decl_stmt|;
name|threadLocal
operator|.
name|set
argument_list|(
name|ctx
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
if|if
condition|(
name|old
operator|!=
literal|null
condition|)
block|{
name|threadLocal
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|threadLocal
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
DECL|method|requireContext ()
specifier|private
name|C
name|requireContext
parameter_list|()
block|{
name|C
name|context
init|=
name|threadLocal
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"Cannot access scoped object"
argument_list|)
throw|;
block|}
return|return
name|context
return|;
block|}
comment|/**    * Returns a new context object based on the passed in context that has no request scoped objects    * initialized.    *    *<p>Note that some code paths expect request-scoped objects like {@code CurrentUser} to be    * constructible starting from just the context object returned by this method. For example, in    * the SSH scope, the context includes the {@code SshSession}, which is used by {@code    * SshCurrentUserProvider} to construct a new {@code CurrentUser} in the new thread.    *    * @param ctx the context to continue.    * @return a new context.    */
DECL|method|continuingContext (C ctx)
specifier|protected
specifier|abstract
name|C
name|continuingContext
parameter_list|(
name|C
name|ctx
parameter_list|)
function_decl|;
block|}
end_class

end_unit

