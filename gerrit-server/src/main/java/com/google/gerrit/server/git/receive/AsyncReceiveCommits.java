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
DECL|package|com.google.gerrit.server.git.receive
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
operator|.
name|receive
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
name|SetMultimap
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
name|restapi
operator|.
name|AuthException
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
name|config
operator|.
name|ConfigUtil
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
name|HackPushNegotiateHook
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
name|MultiProgressMonitor
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
name|ProjectRunnable
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
name|notedb
operator|.
name|ReviewerStateInternal
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|ContributorAgreementsChecker
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
name|project
operator|.
name|ProjectState
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
name|query
operator|.
name|change
operator|.
name|InternalChangeQuery
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
name|MagicBranch
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
name|PrivateModule
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
name|Singleton
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|FactoryModuleBuilder
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
name|name
operator|.
name|Named
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|concurrent
operator|.
name|ExecutionException
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
name|ExecutorService
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
name|AdvertiseRefsHook
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
name|AdvertiseRefsHookChain
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
name|PreReceiveHook
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
name|ReceiveCommand
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
name|ReceiveCommand
operator|.
name|Result
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/** Hook that delegates to {@link ReceiveCommits} in a worker thread. */
end_comment

begin_class
DECL|class|AsyncReceiveCommits
specifier|public
class|class
name|AsyncReceiveCommits
implements|implements
name|PreReceiveHook
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AsyncReceiveCommits
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|TIMEOUT_NAME
specifier|private
specifier|static
specifier|final
name|String
name|TIMEOUT_NAME
init|=
literal|"ReceiveCommitsOverallTimeout"
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ( ProjectControl projectControl, Repository repository, @Nullable MessageSender messageSender, SetMultimap<ReviewerStateInternal, Account.Id> extraReviewers)
name|AsyncReceiveCommits
name|create
parameter_list|(
name|ProjectControl
name|projectControl
parameter_list|,
name|Repository
name|repository
parameter_list|,
annotation|@
name|Nullable
name|MessageSender
name|messageSender
parameter_list|,
name|SetMultimap
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|extraReviewers
parameter_list|)
function_decl|;
block|}
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|PrivateModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|AsyncReceiveCommits
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|expose
argument_list|(
name|AsyncReceiveCommits
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Don't expose the binding for ReceiveCommits.Factory. All callers should
comment|// be using AsyncReceiveCommits.Factory instead.
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|ReceiveCommits
operator|.
name|Factory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|Named
argument_list|(
name|TIMEOUT_NAME
argument_list|)
DECL|method|getTimeoutMillis (@erritServerConfig Config cfg)
name|long
name|getTimeoutMillis
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
return|return
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|cfg
argument_list|,
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"timeout"
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
operator|.
name|toMillis
argument_list|(
literal|4
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
block|}
DECL|class|Worker
specifier|private
class|class
name|Worker
implements|implements
name|ProjectRunnable
block|{
DECL|field|progress
specifier|final
name|MultiProgressMonitor
name|progress
decl_stmt|;
DECL|field|commands
specifier|private
specifier|final
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
decl_stmt|;
DECL|field|rc
specifier|private
specifier|final
name|ReceiveCommits
name|rc
decl_stmt|;
DECL|method|Worker (Collection<ReceiveCommand> commands)
specifier|private
name|Worker
parameter_list|(
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
name|this
operator|.
name|commands
operator|=
name|commands
expr_stmt|;
name|rc
operator|=
name|factory
operator|.
name|create
argument_list|(
name|projectControl
argument_list|,
name|rp
argument_list|,
name|allRefsWatcher
argument_list|,
name|extraReviewers
argument_list|)
expr_stmt|;
name|rc
operator|.
name|init
argument_list|()
expr_stmt|;
name|rc
operator|.
name|setMessageSender
argument_list|(
name|messageSender
argument_list|)
expr_stmt|;
name|progress
operator|=
operator|new
name|MultiProgressMonitor
argument_list|(
operator|new
name|MessageSenderOutputStream
argument_list|()
argument_list|,
literal|"Processing changes"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|rc
operator|.
name|processCommands
argument_list|(
name|commands
argument_list|,
name|progress
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectNameKey ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectNameKey
parameter_list|()
block|{
return|return
name|rc
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRemoteName ()
specifier|public
name|String
name|getRemoteName
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|hasCustomizedPrint ()
specifier|public
name|boolean
name|hasCustomizedPrint
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"receive-commits"
return|;
block|}
DECL|method|sendMessages ()
name|void
name|sendMessages
parameter_list|()
block|{
name|rc
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
block|}
DECL|class|MessageSenderOutputStream
specifier|private
class|class
name|MessageSenderOutputStream
extends|extends
name|OutputStream
block|{
annotation|@
name|Override
DECL|method|write (int b)
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
block|{
name|rc
operator|.
name|getMessageSender
argument_list|()
operator|.
name|sendBytes
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|(
name|byte
operator|)
name|b
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] what, int off, int len)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|what
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
block|{
name|rc
operator|.
name|getMessageSender
argument_list|()
operator|.
name|sendBytes
argument_list|(
name|what
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|write (byte[] what)
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|what
parameter_list|)
block|{
name|rc
operator|.
name|getMessageSender
argument_list|()
operator|.
name|sendBytes
argument_list|(
name|what
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|rc
operator|.
name|getMessageSender
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|field|factory
specifier|private
specifier|final
name|ReceiveCommits
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|rp
specifier|private
specifier|final
name|ReceivePack
name|rp
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|scopePropagator
specifier|private
specifier|final
name|RequestScopePropagator
name|scopePropagator
decl_stmt|;
DECL|field|receiveConfig
specifier|private
specifier|final
name|ReceiveConfig
name|receiveConfig
decl_stmt|;
DECL|field|contributorAgreements
specifier|private
specifier|final
name|ContributorAgreementsChecker
name|contributorAgreements
decl_stmt|;
DECL|field|timeoutMillis
specifier|private
specifier|final
name|long
name|timeoutMillis
decl_stmt|;
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
name|projectControl
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|messageSender
specifier|private
specifier|final
name|MessageSender
name|messageSender
decl_stmt|;
DECL|field|extraReviewers
specifier|private
specifier|final
name|SetMultimap
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|extraReviewers
decl_stmt|;
DECL|field|allRefsWatcher
specifier|private
specifier|final
name|AllRefsWatcher
name|allRefsWatcher
decl_stmt|;
annotation|@
name|Inject
DECL|method|AsyncReceiveCommits ( ReceiveCommits.Factory factory, PermissionBackend permissionBackend, VisibleRefFilter.Factory refFilterFactory, Provider<InternalChangeQuery> queryProvider, @ReceiveCommitsExecutor ExecutorService executor, RequestScopePropagator scopePropagator, ReceiveConfig receiveConfig, TransferConfig transferConfig, Provider<LazyPostReceiveHookChain> lazyPostReceive, ContributorAgreementsChecker contributorAgreements, @Named(TIMEOUT_NAME) long timeoutMillis, @Assisted ProjectControl projectControl, @Assisted Repository repo, @Assisted @Nullable MessageSender messageSender, @Assisted SetMultimap<ReviewerStateInternal, Account.Id> extraReviewers)
name|AsyncReceiveCommits
parameter_list|(
name|ReceiveCommits
operator|.
name|Factory
name|factory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|VisibleRefFilter
operator|.
name|Factory
name|refFilterFactory
parameter_list|,
name|Provider
argument_list|<
name|InternalChangeQuery
argument_list|>
name|queryProvider
parameter_list|,
annotation|@
name|ReceiveCommitsExecutor
name|ExecutorService
name|executor
parameter_list|,
name|RequestScopePropagator
name|scopePropagator
parameter_list|,
name|ReceiveConfig
name|receiveConfig
parameter_list|,
name|TransferConfig
name|transferConfig
parameter_list|,
name|Provider
argument_list|<
name|LazyPostReceiveHookChain
argument_list|>
name|lazyPostReceive
parameter_list|,
name|ContributorAgreementsChecker
name|contributorAgreements
parameter_list|,
annotation|@
name|Named
argument_list|(
name|TIMEOUT_NAME
argument_list|)
name|long
name|timeoutMillis
parameter_list|,
annotation|@
name|Assisted
name|ProjectControl
name|projectControl
parameter_list|,
annotation|@
name|Assisted
name|Repository
name|repo
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|MessageSender
name|messageSender
parameter_list|,
annotation|@
name|Assisted
name|SetMultimap
argument_list|<
name|ReviewerStateInternal
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|extraReviewers
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|scopePropagator
operator|=
name|scopePropagator
expr_stmt|;
name|this
operator|.
name|receiveConfig
operator|=
name|receiveConfig
expr_stmt|;
name|this
operator|.
name|contributorAgreements
operator|=
name|contributorAgreements
expr_stmt|;
name|this
operator|.
name|timeoutMillis
operator|=
name|timeoutMillis
expr_stmt|;
name|this
operator|.
name|projectControl
operator|=
name|projectControl
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|messageSender
operator|=
name|messageSender
expr_stmt|;
name|this
operator|.
name|extraReviewers
operator|=
name|extraReviewers
expr_stmt|;
name|IdentifiedUser
name|user
init|=
name|projectControl
operator|.
name|getUser
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
decl_stmt|;
name|ProjectState
name|state
init|=
name|projectControl
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|rp
operator|=
operator|new
name|ReceivePack
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowCreates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowDeletes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowNonFastForwards
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|transferConfig
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|transferConfig
operator|.
name|getEffectiveMaxObjectSizeLimit
argument_list|(
name|state
argument_list|)
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setCheckReceivedObjects
argument_list|(
name|state
operator|.
name|getConfig
argument_list|()
operator|.
name|getCheckReceivedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setRefFilter
argument_list|(
operator|new
name|ReceiveRefFilter
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAllowPushOptions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setPreReceiveHook
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setPostReceiveHook
argument_list|(
name|lazyPostReceive
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// If the user lacks READ permission, some references may be filtered and hidden from view.
comment|// Check objects mentioned inside the incoming pack file are reachable from visible refs.
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|projectName
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|rp
operator|.
name|setCheckReferencedObjectsAreReachable
argument_list|(
name|receiveConfig
operator|.
name|checkReferencedObjectsAreReachable
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|AdvertiseRefsHook
argument_list|>
name|advHooks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|allRefsWatcher
operator|=
operator|new
name|AllRefsWatcher
argument_list|()
expr_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
name|allRefsWatcher
argument_list|)
expr_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
name|refFilterFactory
operator|.
name|create
argument_list|(
name|state
argument_list|,
name|repo
argument_list|)
operator|.
name|setShowMetadata
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommitsAdvertiseRefsHook
argument_list|(
name|queryProvider
argument_list|,
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
name|advHooks
operator|.
name|add
argument_list|(
operator|new
name|HackPushNegotiateHook
argument_list|()
argument_list|)
expr_stmt|;
name|rp
operator|.
name|setAdvertiseRefsHook
argument_list|(
name|AdvertiseRefsHookChain
operator|.
name|newChain
argument_list|(
name|advHooks
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** Determine if the user can upload commits. */
DECL|method|canUpload ()
specifier|public
name|Capable
name|canUpload
parameter_list|()
throws|throws
name|IOException
block|{
name|Capable
name|result
init|=
name|projectControl
operator|.
name|canPushToAtLeastOneRef
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|!=
name|Capable
operator|.
name|OK
condition|)
block|{
return|return
name|result
return|;
block|}
try|try
block|{
name|contributorAgreements
operator|.
name|check
argument_list|(
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|projectControl
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
operator|new
name|Capable
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
return|;
block|}
if|if
condition|(
name|receiveConfig
operator|.
name|checkMagicRefs
condition|)
block|{
return|return
name|MagicBranch
operator|.
name|checkMagicBranchRefs
argument_list|(
name|repo
argument_list|,
name|projectControl
operator|.
name|getProject
argument_list|()
argument_list|)
return|;
block|}
return|return
name|Capable
operator|.
name|OK
return|;
block|}
annotation|@
name|Override
DECL|method|onPreReceive (ReceivePack rp, Collection<ReceiveCommand> commands)
specifier|public
name|void
name|onPreReceive
parameter_list|(
name|ReceivePack
name|rp
parameter_list|,
name|Collection
argument_list|<
name|ReceiveCommand
argument_list|>
name|commands
parameter_list|)
block|{
name|Worker
name|w
init|=
operator|new
name|Worker
argument_list|(
name|commands
argument_list|)
decl_stmt|;
try|try
block|{
name|w
operator|.
name|progress
operator|.
name|waitFor
argument_list|(
name|executor
operator|.
name|submit
argument_list|(
name|scopePropagator
operator|.
name|wrap
argument_list|(
name|w
argument_list|)
argument_list|)
argument_list|,
name|timeoutMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error in ReceiveCommits while processing changes for project {}"
argument_list|,
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|rp
operator|.
name|sendError
argument_list|(
literal|"internal error while processing changes"
argument_list|)
expr_stmt|;
comment|// ReceiveCommits has tried its best to catch errors, so anything at this
comment|// point is very bad.
for|for
control|(
name|ReceiveCommand
name|c
range|:
name|commands
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getResult
argument_list|()
operator|==
name|Result
operator|.
name|NOT_ATTEMPTED
condition|)
block|{
name|c
operator|.
name|setResult
argument_list|(
name|Result
operator|.
name|REJECTED_OTHER_REASON
argument_list|,
literal|"internal error"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|w
operator|.
name|sendMessages
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getReceivePack ()
specifier|public
name|ReceivePack
name|getReceivePack
parameter_list|()
block|{
return|return
name|rp
return|;
block|}
block|}
end_class

end_unit

