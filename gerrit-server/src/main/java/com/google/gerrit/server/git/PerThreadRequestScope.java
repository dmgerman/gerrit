begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|config
operator|.
name|RequestScopedReviewDbProvider
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
name|util
operator|.
name|RequestContext
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
name|util
operator|.
name|ThreadLocalRequestContext
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
name|util
operator|.
name|ThreadLocalRequestScopePropagator
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
name|Key
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
name|Provider
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
name|HashMap
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
name|Callable
import|;
end_import

begin_class
DECL|class|PerThreadRequestScope
specifier|public
class|class
name|PerThreadRequestScope
block|{
DECL|interface|Scoper
specifier|public
interface|interface
name|Scoper
block|{
DECL|method|scope (Callable<T> callable)
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|scope
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
function_decl|;
block|}
DECL|class|Context
specifier|private
specifier|static
class|class
name|Context
block|{
DECL|field|map
specifier|private
specifier|final
name|Map
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
DECL|method|Context ()
specifier|private
name|Context
parameter_list|()
block|{
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|get (Key<T> key, Provider<T> creator)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
name|Provider
argument_list|<
name|T
argument_list|>
name|creator
parameter_list|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|T
name|t
init|=
operator|(
name|T
operator|)
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
condition|)
block|{
name|t
operator|=
name|creator
operator|.
name|get
argument_list|()
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
return|return
name|t
return|;
block|}
block|}
DECL|class|Propagator
specifier|public
specifier|static
class|class
name|Propagator
extends|extends
name|ThreadLocalRequestScopePropagator
argument_list|<
name|Context
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|Propagator (ThreadLocalRequestContext local, Provider<RequestScopedReviewDbProvider> dbProviderProvider)
name|Propagator
parameter_list|(
name|ThreadLocalRequestContext
name|local
parameter_list|,
name|Provider
argument_list|<
name|RequestScopedReviewDbProvider
argument_list|>
name|dbProviderProvider
parameter_list|)
block|{
name|super
argument_list|(
name|REQUEST
argument_list|,
name|current
argument_list|,
name|local
argument_list|,
name|dbProviderProvider
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|continuingContext (Context ctx)
specifier|protected
name|Context
name|continuingContext
parameter_list|(
name|Context
name|ctx
parameter_list|)
block|{
return|return
operator|new
name|Context
argument_list|()
return|;
block|}
DECL|method|scope (RequestContext requestContext, Callable<T> callable)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Callable
argument_list|<
name|T
argument_list|>
name|scope
parameter_list|(
name|RequestContext
name|requestContext
parameter_list|,
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
specifier|final
name|Context
name|ctx
init|=
operator|new
name|Context
argument_list|()
decl_stmt|;
specifier|final
name|Callable
argument_list|<
name|T
argument_list|>
name|wrapped
init|=
name|context
argument_list|(
name|requestContext
argument_list|,
name|cleanup
argument_list|(
name|callable
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|Callable
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|old
init|=
name|current
operator|.
name|get
argument_list|()
decl_stmt|;
name|current
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|wrapped
operator|.
name|call
argument_list|()
return|;
block|}
finally|finally
block|{
name|current
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
DECL|field|current
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|Context
argument_list|>
name|current
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|requireContext ()
specifier|private
specifier|static
name|Context
name|requireContext
parameter_list|()
block|{
specifier|final
name|Context
name|ctx
init|=
name|current
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|ctx
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"Not in command/request"
argument_list|)
throw|;
block|}
return|return
name|ctx
return|;
block|}
DECL|field|REQUEST
specifier|public
specifier|static
specifier|final
name|Scope
name|REQUEST
init|=
operator|new
name|Scope
argument_list|()
block|{
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Provider
argument_list|<
name|T
argument_list|>
name|scope
parameter_list|(
specifier|final
name|Key
argument_list|<
name|T
argument_list|>
name|key
parameter_list|,
specifier|final
name|Provider
argument_list|<
name|T
argument_list|>
name|creator
parameter_list|)
block|{
return|return
operator|new
name|Provider
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|T
name|get
parameter_list|()
block|{
return|return
name|requireContext
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|,
name|creator
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s[%s]"
argument_list|,
name|creator
argument_list|,
name|REQUEST
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"PerThreadRequestScope.REQUEST"
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

