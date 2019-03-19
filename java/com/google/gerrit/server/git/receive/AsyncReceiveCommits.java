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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|NANOSECONDS
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|UsedAt
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|metrics
operator|.
name|Counter0
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
name|metrics
operator|.
name|Description
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
name|metrics
operator|.
name|Description
operator|.
name|Units
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
name|metrics
operator|.
name|Field
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
name|metrics
operator|.
name|Histogram1
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
name|metrics
operator|.
name|MetricMaker
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
name|metrics
operator|.
name|Timer1
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
name|Change
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
name|config
operator|.
name|ReceiveCommitsExecutor
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
name|DefaultAdvertiseRefsHook
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
name|PermissionBackend
operator|.
name|RefFilterOptions
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

begin_comment
comment|/**  * Hook that delegates to {@link ReceiveCommits} in a worker thread.  *  *<p>Since the work that {@link ReceiveCommits} does may take a long, potentially unbounded amount  * of time, it runs in the background so it can be monitored for timeouts and cancelled, and have  * stalls reported to the user from the main thread.  */
end_comment

begin_class
DECL|class|AsyncReceiveCommits
specifier|public
class|class
name|AsyncReceiveCommits
implements|implements
name|PreReceiveHook
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
DECL|method|create ( ProjectState projectState, IdentifiedUser user, Repository repository, @Nullable MessageSender messageSender)
name|AsyncReceiveCommits
name|create
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|IdentifiedUser
name|user
parameter_list|,
name|Repository
name|repository
parameter_list|,
annotation|@
name|Nullable
name|MessageSender
name|messageSender
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
name|install
argument_list|(
operator|new
name|FactoryModuleBuilder
argument_list|()
operator|.
name|build
argument_list|(
name|BranchCommitValidator
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
name|receiveCommits
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
name|receiveCommits
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
name|receiveCommits
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
name|receiveCommits
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
name|receiveCommits
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
name|receiveCommits
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
name|receiveCommits
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
DECL|enum|PushType
specifier|private
enum|enum
name|PushType
block|{
DECL|enumConstant|CREATE_REPLACE
name|CREATE_REPLACE
block|,
DECL|enumConstant|NORMAL
name|NORMAL
block|,
DECL|enumConstant|AUTOCLOSE
name|AUTOCLOSE
block|,   }
annotation|@
name|Singleton
DECL|class|Metrics
specifier|private
specifier|static
class|class
name|Metrics
block|{
DECL|field|changes
specifier|private
specifier|final
name|Histogram1
argument_list|<
name|PushType
argument_list|>
name|changes
decl_stmt|;
DECL|field|latencyPerChange
specifier|private
specifier|final
name|Timer1
argument_list|<
name|PushType
argument_list|>
name|latencyPerChange
decl_stmt|;
DECL|field|latencyPerPush
specifier|private
specifier|final
name|Timer1
argument_list|<
name|PushType
argument_list|>
name|latencyPerPush
decl_stmt|;
DECL|field|timeouts
specifier|private
specifier|final
name|Counter0
name|timeouts
decl_stmt|;
annotation|@
name|Inject
DECL|method|Metrics (MetricMaker metricMaker)
name|Metrics
parameter_list|(
name|MetricMaker
name|metricMaker
parameter_list|)
block|{
name|changes
operator|=
name|metricMaker
operator|.
name|newHistogram
argument_list|(
literal|"receivecommits/changes_per_push"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"number of changes uploaded in a single push."
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|,
name|Field
operator|.
name|ofEnum
argument_list|(
name|PushType
operator|.
name|class
argument_list|,
literal|"type"
argument_list|,
literal|"type of push (create/replace, autoclose)"
argument_list|)
argument_list|)
expr_stmt|;
name|latencyPerChange
operator|=
name|metricMaker
operator|.
name|newTimer
argument_list|(
literal|"receivecommits/latency_per_push_per_change"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Processing delay per push divided by the number of changes in said push. "
operator|+
literal|"(Only includes pushes which contain changes.)"
argument_list|)
operator|.
name|setUnit
argument_list|(
name|Units
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|,
name|Field
operator|.
name|ofEnum
argument_list|(
name|PushType
operator|.
name|class
argument_list|,
literal|"type"
argument_list|,
literal|"type of push (create/replace, autoclose)"
argument_list|)
argument_list|)
expr_stmt|;
name|latencyPerPush
operator|=
name|metricMaker
operator|.
name|newTimer
argument_list|(
literal|"receivecommits/latency_per_push"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"processing delay for a processing single push"
argument_list|)
operator|.
name|setUnit
argument_list|(
name|Units
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|setCumulative
argument_list|()
argument_list|,
name|Field
operator|.
name|ofEnum
argument_list|(
name|PushType
operator|.
name|class
argument_list|,
literal|"type"
argument_list|,
literal|"type of push (create/replace, autoclose, normal)"
argument_list|)
argument_list|)
expr_stmt|;
name|timeouts
operator|=
name|metricMaker
operator|.
name|newCounter
argument_list|(
literal|"receivecommits/timeout"
argument_list|,
operator|new
name|Description
argument_list|(
literal|"rate of push timeouts"
argument_list|)
operator|.
name|setRate
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|field|metrics
specifier|private
specifier|final
name|Metrics
name|metrics
decl_stmt|;
DECL|field|receiveCommits
specifier|private
specifier|final
name|ReceiveCommits
name|receiveCommits
decl_stmt|;
DECL|field|resultChangeIds
specifier|private
specifier|final
name|ResultChangeIds
name|resultChangeIds
decl_stmt|;
DECL|field|perm
specifier|private
specifier|final
name|PermissionBackend
operator|.
name|ForProject
name|perm
decl_stmt|;
DECL|field|receivePack
specifier|private
specifier|final
name|ReceivePack
name|receivePack
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
DECL|field|projectState
specifier|private
specifier|final
name|ProjectState
name|projectState
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|allRefsWatcher
specifier|private
specifier|final
name|AllRefsWatcher
name|allRefsWatcher
decl_stmt|;
annotation|@
name|Inject
DECL|method|AsyncReceiveCommits ( ReceiveCommits.Factory factory, PermissionBackend permissionBackend, Provider<InternalChangeQuery> queryProvider, @ReceiveCommitsExecutor ExecutorService executor, RequestScopePropagator scopePropagator, ReceiveConfig receiveConfig, TransferConfig transferConfig, Provider<LazyPostReceiveHookChain> lazyPostReceive, ContributorAgreementsChecker contributorAgreements, Metrics metrics, @Named(TIMEOUT_NAME) long timeoutMillis, @Assisted ProjectState projectState, @Assisted IdentifiedUser user, @Assisted Repository repo, @Assisted @Nullable MessageSender messageSender)
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
name|Metrics
name|metrics
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
name|ProjectState
name|projectState
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
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
parameter_list|)
throws|throws
name|PermissionBackendException
block|{
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
name|projectState
operator|=
name|projectState
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repo
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|projectState
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|receivePack
operator|=
operator|new
name|ReceivePack
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setAllowCreates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setAllowDeletes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setAllowNonFastForwards
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setRefLogIdent
argument_list|(
name|user
operator|.
name|newRefLogIdent
argument_list|()
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setTimeout
argument_list|(
name|transferConfig
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setMaxObjectSizeLimit
argument_list|(
name|projectState
operator|.
name|getEffectiveMaxObjectSizeLimit
argument_list|()
operator|.
name|value
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setCheckReceivedObjects
argument_list|(
name|projectState
operator|.
name|getConfig
argument_list|()
operator|.
name|getCheckReceivedObjects
argument_list|()
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setRefFilter
argument_list|(
operator|new
name|ReceiveRefFilter
argument_list|()
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setAllowPushOptions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|receivePack
operator|.
name|setPreReceiveHook
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|receivePack
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
name|this
operator|.
name|perm
operator|=
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
expr_stmt|;
try|try
block|{
name|projectState
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
name|this
operator|.
name|perm
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
decl||
name|ResourceConflictException
name|e
parameter_list|)
block|{
name|receivePack
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
operator|new
name|DefaultAdvertiseRefsHook
argument_list|(
name|perm
argument_list|,
name|RefFilterOptions
operator|.
name|builder
argument_list|()
operator|.
name|setFilterMeta
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
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
name|receivePack
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
name|resultChangeIds
operator|=
operator|new
name|ResultChangeIds
argument_list|()
expr_stmt|;
name|receiveCommits
operator|=
name|factory
operator|.
name|create
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|,
name|receivePack
argument_list|,
name|allRefsWatcher
argument_list|,
name|messageSender
argument_list|,
name|resultChangeIds
argument_list|)
expr_stmt|;
name|receiveCommits
operator|.
name|init
argument_list|()
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
throws|,
name|PermissionBackendException
block|{
try|try
block|{
name|perm
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|PUSH_AT_LEAST_ONE_REF
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
literal|"Upload denied for project '"
operator|+
name|projectState
operator|.
name|getName
argument_list|()
operator|+
literal|"'"
argument_list|)
return|;
block|}
try|try
block|{
name|contributorAgreements
operator|.
name|check
argument_list|(
name|projectState
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|user
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
name|projectState
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
if|if
condition|(
name|commands
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getResult
argument_list|()
operator|!=
name|Result
operator|.
name|NOT_ATTEMPTED
argument_list|)
condition|)
block|{
comment|// Stop processing when command was already processed by previously invoked
comment|// pre-receive hooks
return|return;
block|}
name|long
name|startNanos
init|=
name|System
operator|.
name|nanoTime
argument_list|()
decl_stmt|;
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
name|metrics
operator|.
name|timeouts
operator|.
name|increment
argument_list|()
expr_stmt|;
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
literal|"Error in ReceiveCommits while processing changes for project %s"
argument_list|,
name|projectState
operator|.
name|getName
argument_list|()
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
name|long
name|deltaNanos
init|=
name|System
operator|.
name|nanoTime
argument_list|()
operator|-
name|startNanos
decl_stmt|;
name|int
name|totalChanges
init|=
literal|0
decl_stmt|;
name|PushType
name|pushType
decl_stmt|;
if|if
condition|(
name|resultChangeIds
operator|.
name|isMagicPush
argument_list|()
condition|)
block|{
name|pushType
operator|=
name|PushType
operator|.
name|CREATE_REPLACE
expr_stmt|;
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|created
init|=
name|resultChangeIds
operator|.
name|get
argument_list|(
name|ResultChangeIds
operator|.
name|Key
operator|.
name|CREATED
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|replaced
init|=
name|resultChangeIds
operator|.
name|get
argument_list|(
name|ResultChangeIds
operator|.
name|Key
operator|.
name|REPLACED
argument_list|)
decl_stmt|;
name|metrics
operator|.
name|changes
operator|.
name|record
argument_list|(
name|pushType
argument_list|,
name|created
operator|.
name|size
argument_list|()
operator|+
name|replaced
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|totalChanges
operator|=
name|replaced
operator|.
name|size
argument_list|()
operator|+
name|created
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|autoclosed
init|=
name|resultChangeIds
operator|.
name|get
argument_list|(
name|ResultChangeIds
operator|.
name|Key
operator|.
name|AUTOCLOSED
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|autoclosed
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pushType
operator|=
name|PushType
operator|.
name|AUTOCLOSE
expr_stmt|;
name|metrics
operator|.
name|changes
operator|.
name|record
argument_list|(
name|pushType
argument_list|,
name|autoclosed
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|totalChanges
operator|=
name|autoclosed
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|pushType
operator|=
name|PushType
operator|.
name|NORMAL
expr_stmt|;
block|}
block|}
if|if
condition|(
name|totalChanges
operator|>
literal|0
condition|)
block|{
name|metrics
operator|.
name|latencyPerChange
operator|.
name|record
argument_list|(
name|pushType
argument_list|,
name|deltaNanos
operator|/
name|totalChanges
argument_list|,
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
name|metrics
operator|.
name|latencyPerPush
operator|.
name|record
argument_list|(
name|pushType
argument_list|,
name|deltaNanos
argument_list|,
name|NANOSECONDS
argument_list|)
expr_stmt|;
block|}
comment|/** Returns the Change.Ids that were processed in onPreReceive */
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|method|getResultChangeIds ()
specifier|public
name|ResultChangeIds
name|getResultChangeIds
parameter_list|()
block|{
return|return
name|resultChangeIds
return|;
block|}
DECL|method|getReceivePack ()
specifier|public
name|ReceivePack
name|getReceivePack
parameter_list|()
block|{
return|return
name|receivePack
return|;
block|}
block|}
end_class

end_unit

