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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|common
operator|.
name|TimeUtil
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|CurrentUser
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
name|IdentifiedUser
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
name|RequestCleanup
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
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|util
operator|.
name|Providers
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

begin_comment
comment|/** Guice scopes for state during an SSH connection. */
end_comment

begin_class
DECL|class|SshScope
specifier|public
class|class
name|SshScope
block|{
DECL|field|RC_KEY
specifier|private
specifier|static
specifier|final
name|Key
argument_list|<
name|RequestCleanup
argument_list|>
name|RC_KEY
init|=
name|Key
operator|.
name|get
argument_list|(
name|RequestCleanup
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DB_KEY
specifier|private
specifier|static
specifier|final
name|Key
argument_list|<
name|RequestScopedReviewDbProvider
argument_list|>
name|DB_KEY
init|=
name|Key
operator|.
name|get
argument_list|(
name|RequestScopedReviewDbProvider
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|Context
class|class
name|Context
implements|implements
name|RequestContext
block|{
DECL|field|cleanup
specifier|private
specifier|final
name|RequestCleanup
name|cleanup
init|=
operator|new
name|RequestCleanup
argument_list|()
decl_stmt|;
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
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|session
specifier|private
specifier|final
name|SshSession
name|session
decl_stmt|;
DECL|field|commandLine
specifier|private
specifier|final
name|String
name|commandLine
decl_stmt|;
DECL|field|created
specifier|final
name|long
name|created
decl_stmt|;
DECL|field|started
specifier|volatile
name|long
name|started
decl_stmt|;
DECL|field|finished
specifier|volatile
name|long
name|finished
decl_stmt|;
DECL|method|Context (SchemaFactory<ReviewDb> sf, final SshSession s, final String c, final long at)
specifier|private
name|Context
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
specifier|final
name|SshSession
name|s
parameter_list|,
specifier|final
name|String
name|c
parameter_list|,
specifier|final
name|long
name|at
parameter_list|)
block|{
name|schemaFactory
operator|=
name|sf
expr_stmt|;
name|session
operator|=
name|s
expr_stmt|;
name|commandLine
operator|=
name|c
expr_stmt|;
name|created
operator|=
name|started
operator|=
name|finished
operator|=
name|at
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|RC_KEY
argument_list|,
name|cleanup
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|DB_KEY
argument_list|,
operator|new
name|RequestScopedReviewDbProvider
argument_list|(
name|schemaFactory
argument_list|,
name|Providers
operator|.
name|of
argument_list|(
name|cleanup
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Context (Context p, SshSession s, String c)
specifier|private
name|Context
parameter_list|(
name|Context
name|p
parameter_list|,
name|SshSession
name|s
parameter_list|,
name|String
name|c
parameter_list|)
block|{
name|this
argument_list|(
name|p
operator|.
name|schemaFactory
argument_list|,
name|s
argument_list|,
name|c
argument_list|,
name|p
operator|.
name|created
argument_list|)
expr_stmt|;
name|started
operator|=
name|p
operator|.
name|started
expr_stmt|;
name|finished
operator|=
name|p
operator|.
name|finished
expr_stmt|;
block|}
DECL|method|getCommandLine ()
name|String
name|getCommandLine
parameter_list|()
block|{
return|return
name|commandLine
return|;
block|}
DECL|method|getSession ()
name|SshSession
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
name|CurrentUser
name|user
init|=
name|session
operator|.
name|getUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
operator|&&
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|IdentifiedUser
name|identifiedUser
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|identifiedUser
operator|.
name|setAccessPath
argument_list|(
name|user
operator|.
name|getAccessPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|identifiedUser
return|;
block|}
return|return
name|user
return|;
block|}
annotation|@
name|Override
DECL|method|getReviewDbProvider ()
specifier|public
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|getReviewDbProvider
parameter_list|()
block|{
return|return
operator|(
name|RequestScopedReviewDbProvider
operator|)
name|map
operator|.
name|get
argument_list|(
name|DB_KEY
argument_list|)
return|;
block|}
DECL|method|get (Key<T> key, Provider<T> creator)
specifier|synchronized
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
DECL|method|subContext (SshSession newSession, String newCommandLine)
specifier|synchronized
name|Context
name|subContext
parameter_list|(
name|SshSession
name|newSession
parameter_list|,
name|String
name|newCommandLine
parameter_list|)
block|{
name|Context
name|ctx
init|=
operator|new
name|Context
argument_list|(
name|this
argument_list|,
name|newSession
argument_list|,
name|newCommandLine
argument_list|)
decl_stmt|;
name|ctx
operator|.
name|cleanup
operator|.
name|add
argument_list|(
name|cleanup
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
block|}
DECL|class|ContextProvider
specifier|static
class|class
name|ContextProvider
implements|implements
name|Provider
argument_list|<
name|Context
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Context
name|get
parameter_list|()
block|{
return|return
name|requireContext
argument_list|()
return|;
block|}
block|}
DECL|class|SshSessionProvider
specifier|public
specifier|static
class|class
name|SshSessionProvider
implements|implements
name|Provider
argument_list|<
name|SshSession
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|SshSession
name|get
parameter_list|()
block|{
return|return
name|requireContext
argument_list|()
operator|.
name|getSession
argument_list|()
return|;
block|}
block|}
DECL|class|Propagator
specifier|static
class|class
name|Propagator
extends|extends
name|ThreadLocalRequestScopePropagator
argument_list|<
name|Context
argument_list|>
block|{
DECL|field|sshScope
specifier|private
specifier|final
name|SshScope
name|sshScope
decl_stmt|;
annotation|@
name|Inject
DECL|method|Propagator ( SshScope sshScope, ThreadLocalRequestContext local, Provider<RequestScopedReviewDbProvider> dbProviderProvider)
name|Propagator
parameter_list|(
name|SshScope
name|sshScope
parameter_list|,
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
name|this
operator|.
name|sshScope
operator|=
name|sshScope
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
comment|// The cleanup is not chained, since the RequestScopePropagator executors
comment|// the Context's cleanup when finished executing.
return|return
name|sshScope
operator|.
name|newContinuingContext
argument_list|(
name|ctx
argument_list|)
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
DECL|field|local
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|local
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|RequestFactory
name|userFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshScope (ThreadLocalRequestContext local, IdentifiedUser.RequestFactory userFactory)
name|SshScope
parameter_list|(
name|ThreadLocalRequestContext
name|local
parameter_list|,
name|IdentifiedUser
operator|.
name|RequestFactory
name|userFactory
parameter_list|)
block|{
name|this
operator|.
name|local
operator|=
name|local
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
block|}
DECL|method|newContext (SchemaFactory<ReviewDb> sf, SshSession s, String cmd)
name|Context
name|newContext
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|sf
parameter_list|,
name|SshSession
name|s
parameter_list|,
name|String
name|cmd
parameter_list|)
block|{
return|return
operator|new
name|Context
argument_list|(
name|sf
argument_list|,
name|s
argument_list|,
name|cmd
argument_list|,
name|TimeUtil
operator|.
name|nowMs
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newContinuingContext (Context ctx)
specifier|private
name|Context
name|newContinuingContext
parameter_list|(
name|Context
name|ctx
parameter_list|)
block|{
return|return
operator|new
name|Context
argument_list|(
name|ctx
argument_list|,
name|ctx
operator|.
name|getSession
argument_list|()
argument_list|,
name|ctx
operator|.
name|getCommandLine
argument_list|()
argument_list|)
return|;
block|}
DECL|method|set (Context ctx)
name|Context
name|set
parameter_list|(
name|Context
name|ctx
parameter_list|)
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
name|local
operator|.
name|setContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
return|return
name|old
return|;
block|}
comment|/** Returns exactly one instance per command executed. */
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
literal|"SshScopes.REQUEST"
return|;
block|}
block|}
decl_stmt|;
block|}
end_class

end_unit

