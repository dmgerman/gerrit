begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|Lists
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
name|acceptance
operator|.
name|InProcessProtocol
operator|.
name|Context
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
name|data
operator|.
name|Capable
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|client
operator|.
name|Project
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
name|AccessPath
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
name|RemotePeer
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
name|GerritRequestModule
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
name|git
operator|.
name|AsyncReceiveCommits
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
name|ChangeCache
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
name|ReceiveCommits
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
name|ReceivePackInitializer
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
name|TagCache
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
name|TransferConfig
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
name|VisibleRefFilter
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
name|validators
operator|.
name|UploadValidators
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
name|project
operator|.
name|NoSuchProjectException
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
name|project
operator|.
name|ProjectControl
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
name|RequestScopePropagator
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
name|AbstractModule
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
name|Module
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
name|Provides
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
name|servlet
operator|.
name|RequestScoped
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
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
name|transport
operator|.
name|PostReceiveHook
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
name|transport
operator|.
name|PostReceiveHookChain
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
name|transport
operator|.
name|PreUploadHook
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
name|transport
operator|.
name|PreUploadHookChain
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
name|transport
operator|.
name|ReceivePack
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
name|transport
operator|.
name|TestProtocol
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
name|transport
operator|.
name|UploadPack
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
name|transport
operator|.
name|resolver
operator|.
name|ReceivePackFactory
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
name|transport
operator|.
name|resolver
operator|.
name|ServiceNotAuthorizedException
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
name|transport
operator|.
name|resolver
operator|.
name|UploadPackFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|List
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

begin_class
DECL|class|InProcessProtocol
class|class
name|InProcessProtocol
extends|extends
name|TestProtocol
argument_list|<
name|Context
argument_list|>
block|{
DECL|method|module ()
specifier|static
name|Module
name|module
parameter_list|()
block|{
return|return
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|GerritRequestModule
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|RequestScopePropagator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|Propagator
operator|.
name|class
argument_list|)
expr_stmt|;
name|bindScope
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|,
name|InProcessProtocol
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|RemotePeer
name|SocketAddress
name|getSocketAddress
parameter_list|()
block|{
comment|// TODO(dborowitz): Could potentially fake this with thread ID or
comment|// something.
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"No remote peer in acceptance tests"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
DECL|field|REQUEST
specifier|private
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
literal|"Not in TestProtocol scope"
argument_list|)
throw|;
block|}
return|return
name|ctx
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
literal|"InProcessProtocol.REQUEST"
return|;
block|}
block|}
decl_stmt|;
DECL|class|Propagator
specifier|private
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
name|ctx
operator|.
name|newContinuingContext
argument_list|()
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
comment|// TODO(dborowitz): Merge this with AcceptanceTestRequestScope.
comment|/**    * Multi-purpose session/context object.    *<p>    * Confusingly, Gerrit has two ideas of what a "context" object is:    * one for Guice {@link RequestScoped}, and one for its own simplified    * version of request scoping using {@link ThreadLocalRequestContext}.    * This class provides both, in essence just delegating the {@code    * ThreadLocalRequestContext} scoping to the Guice scoping mechanism.    *<p>    * It is also used as the session type for {@code UploadPackFactory} and    * {@code ReceivePackFactory}, since, after all, it encapsulates all the    * information about a single request.    */
DECL|class|Context
specifier|static
class|class
name|Context
implements|implements
name|RequestContext
block|{
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
DECL|field|USER_KEY
specifier|private
specifier|static
specifier|final
name|Key
argument_list|<
name|CurrentUser
argument_list|>
name|USER_KEY
init|=
name|Key
operator|.
name|get
argument_list|(
name|CurrentUser
operator|.
name|class
argument_list|)
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
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|cleanup
specifier|private
specifier|final
name|RequestCleanup
name|cleanup
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
decl_stmt|;
DECL|method|Context (SchemaFactory<ReviewDb> schemaFactory, IdentifiedUser.GenericFactory userFactory, Account.Id accountId, Project.NameKey project)
name|Context
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|cleanup
operator|=
operator|new
name|RequestCleanup
argument_list|()
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
name|map
operator|.
name|put
argument_list|(
name|RC_KEY
argument_list|,
name|cleanup
argument_list|)
expr_stmt|;
name|IdentifiedUser
name|user
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|accountId
argument_list|)
decl_stmt|;
name|user
operator|.
name|setAccessPath
argument_list|(
name|AccessPath
operator|.
name|GIT
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|USER_KEY
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
DECL|method|newContinuingContext ()
specifier|private
name|Context
name|newContinuingContext
parameter_list|()
block|{
return|return
operator|new
name|Context
argument_list|(
name|schemaFactory
argument_list|,
name|userFactory
argument_list|,
name|accountId
argument_list|,
name|project
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCurrentUser ()
specifier|public
name|CurrentUser
name|getCurrentUser
parameter_list|()
block|{
return|return
name|get
argument_list|(
name|USER_KEY
argument_list|,
literal|null
argument_list|)
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
name|get
argument_list|(
name|DB_KEY
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|get (Key<T> key, Provider<T> creator)
specifier|private
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
block|}
DECL|class|Upload
specifier|private
specifier|static
class|class
name|Upload
implements|implements
name|UploadPackFactory
argument_list|<
name|Context
argument_list|>
block|{
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|tagCache
specifier|private
specifier|final
name|TagCache
name|tagCache
decl_stmt|;
DECL|field|changeCache
specifier|private
specifier|final
name|ChangeCache
name|changeCache
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
decl_stmt|;
DECL|field|transferConfig
specifier|private
specifier|final
name|TransferConfig
name|transferConfig
decl_stmt|;
DECL|field|preUploadHooks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PreUploadHook
argument_list|>
name|preUploadHooks
decl_stmt|;
DECL|field|uploadValidatorsFactory
specifier|private
specifier|final
name|UploadValidators
operator|.
name|Factory
name|uploadValidatorsFactory
decl_stmt|;
DECL|field|threadContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|threadContext
decl_stmt|;
annotation|@
name|Inject
DECL|method|Upload ( Provider<ReviewDb> dbProvider, Provider<CurrentUser> userProvider, TagCache tagCache, ChangeCache changeCache, ProjectControl.GenericFactory projectControlFactory, TransferConfig transferConfig, DynamicSet<PreUploadHook> preUploadHooks, UploadValidators.Factory uploadValidatorsFactory, ThreadLocalRequestContext threadContext)
name|Upload
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|TagCache
name|tagCache
parameter_list|,
name|ChangeCache
name|changeCache
parameter_list|,
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
parameter_list|,
name|TransferConfig
name|transferConfig
parameter_list|,
name|DynamicSet
argument_list|<
name|PreUploadHook
argument_list|>
name|preUploadHooks
parameter_list|,
name|UploadValidators
operator|.
name|Factory
name|uploadValidatorsFactory
parameter_list|,
name|ThreadLocalRequestContext
name|threadContext
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|tagCache
operator|=
name|tagCache
expr_stmt|;
name|this
operator|.
name|changeCache
operator|=
name|changeCache
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|transferConfig
operator|=
name|transferConfig
expr_stmt|;
name|this
operator|.
name|preUploadHooks
operator|=
name|preUploadHooks
expr_stmt|;
name|this
operator|.
name|uploadValidatorsFactory
operator|=
name|uploadValidatorsFactory
expr_stmt|;
name|this
operator|.
name|threadContext
operator|=
name|threadContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (Context req, final Repository repo)
specifier|public
name|UploadPack
name|create
parameter_list|(
name|Context
name|req
parameter_list|,
specifier|final
name|Repository
name|repo
parameter_list|)
throws|throws
name|ServiceNotAuthorizedException
block|{
comment|// Set the request context, but don't bother unsetting, since we don't
comment|// have an easy way to run code when this instance is done being used.
comment|// Each operation is run in its own thread, so we don't need to recover
comment|// its original context anyway.
name|threadContext
operator|.
name|setContext
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|current
operator|.
name|set
argument_list|(
name|req
argument_list|)
expr_stmt|;
try|try
block|{
name|ProjectControl
name|ctl
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|req
operator|.
name|project
argument_list|,
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|canRunUploadPack
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServiceNotAuthorizedException
argument_list|()
throw|;
block|}
name|UploadPack
name|up
init|=
operator|new
name|UploadPack
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|up
operator|.
name|setPackConfig
argument_list|(
name|transferConfig
operator|.
name|getPackConfig
argument_list|()
argument_list|)
expr_stmt|;
name|up
operator|.
name|setTimeout
argument_list|(
name|transferConfig
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|allRefsAreVisible
argument_list|()
condition|)
block|{
name|up
operator|.
name|setAdvertiseRefsHook
argument_list|(
operator|new
name|VisibleRefFilter
argument_list|(
name|tagCache
argument_list|,
name|changeCache
argument_list|,
name|repo
argument_list|,
name|ctl
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|PreUploadHook
argument_list|>
name|hooks
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|preUploadHooks
argument_list|)
decl_stmt|;
name|hooks
operator|.
name|add
argument_list|(
name|uploadValidatorsFactory
operator|.
name|create
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
argument_list|,
name|repo
argument_list|,
literal|"localhost-test"
argument_list|)
argument_list|)
expr_stmt|;
name|up
operator|.
name|setPreUploadHook
argument_list|(
name|PreUploadHookChain
operator|.
name|newChain
argument_list|(
name|hooks
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|up
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|class|Receive
specifier|private
specifier|static
class|class
name|Receive
implements|implements
name|ReceivePackFactory
argument_list|<
name|Context
argument_list|>
block|{
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
decl_stmt|;
DECL|field|factory
specifier|private
specifier|final
name|AsyncReceiveCommits
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|TransferConfig
name|config
decl_stmt|;
DECL|field|receivePackInitializers
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ReceivePackInitializer
argument_list|>
name|receivePackInitializers
decl_stmt|;
DECL|field|postReceiveHooks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PostReceiveHook
argument_list|>
name|postReceiveHooks
decl_stmt|;
DECL|field|threadContext
specifier|private
specifier|final
name|ThreadLocalRequestContext
name|threadContext
decl_stmt|;
annotation|@
name|Inject
DECL|method|Receive ( Provider<CurrentUser> userProvider, ProjectControl.GenericFactory projectControlFactory, AsyncReceiveCommits.Factory factory, TransferConfig config, DynamicSet<ReceivePackInitializer> receivePackInitializers, DynamicSet<PostReceiveHook> postReceiveHooks, ThreadLocalRequestContext threadContext)
name|Receive
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|ProjectControl
operator|.
name|GenericFactory
name|projectControlFactory
parameter_list|,
name|AsyncReceiveCommits
operator|.
name|Factory
name|factory
parameter_list|,
name|TransferConfig
name|config
parameter_list|,
name|DynamicSet
argument_list|<
name|ReceivePackInitializer
argument_list|>
name|receivePackInitializers
parameter_list|,
name|DynamicSet
argument_list|<
name|PostReceiveHook
argument_list|>
name|postReceiveHooks
parameter_list|,
name|ThreadLocalRequestContext
name|threadContext
parameter_list|)
block|{
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|receivePackInitializers
operator|=
name|receivePackInitializers
expr_stmt|;
name|this
operator|.
name|postReceiveHooks
operator|=
name|postReceiveHooks
expr_stmt|;
name|this
operator|.
name|threadContext
operator|=
name|threadContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (final Context req, Repository db)
specifier|public
name|ReceivePack
name|create
parameter_list|(
specifier|final
name|Context
name|req
parameter_list|,
name|Repository
name|db
parameter_list|)
throws|throws
name|ServiceNotAuthorizedException
block|{
comment|// Set the request context, but don't bother unsetting, since we don't
comment|// have an easy way to run code when this instance is done being used.
comment|// Each operation is run in its own thread, so we don't need to recover
comment|// its original context anyway.
name|threadContext
operator|.
name|setContext
argument_list|(
name|req
argument_list|)
expr_stmt|;
name|current
operator|.
name|set
argument_list|(
name|req
argument_list|)
expr_stmt|;
try|try
block|{
name|ProjectControl
name|ctl
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|req
operator|.
name|project
argument_list|,
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|canRunReceivePack
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ServiceNotAuthorizedException
argument_list|()
throw|;
block|}
name|ReceiveCommits
name|rc
init|=
name|factory
operator|.
name|create
argument_list|(
name|ctl
argument_list|,
name|db
argument_list|)
operator|.
name|getReceiveCommits
argument_list|()
decl_stmt|;
name|ReceivePack
name|rp
init|=
name|rc
operator|.
name|getReceivePack
argument_list|()
decl_stmt|;
name|Capable
name|r
init|=
name|rc
operator|.
name|canUpload
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|!=
name|Capable
operator|.
name|OK
condition|)
block|{
throw|throw
operator|new
name|ServiceNotAuthorizedException
argument_list|()
throw|;
block|}
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|ctl
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
name|rp
operator|.
name|setRefLogIdent
argument_list|(
name|user
operator|.
name|newRefLogIdent
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|config
operator|.
name|getMaxObjectSizeLimit
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ReceivePackInitializer
name|initializer
range|:
name|receivePackInitializers
control|)
block|{
name|initializer
operator|.
name|init
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|rp
argument_list|)
expr_stmt|;
block|}
name|rp
operator|.
name|setPostReceiveHook
argument_list|(
name|PostReceiveHookChain
operator|.
name|newChain
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|postReceiveHooks
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|rp
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchProjectException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Inject
DECL|method|InProcessProtocol (Upload uploadPackFactory, Receive receivePackFactory)
name|InProcessProtocol
parameter_list|(
name|Upload
name|uploadPackFactory
parameter_list|,
name|Receive
name|receivePackFactory
parameter_list|)
block|{
name|super
argument_list|(
name|uploadPackFactory
argument_list|,
name|receivePackFactory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

