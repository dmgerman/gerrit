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
import|import static
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Scopes
operator|.
name|SINGLETON
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|AccountGroup
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
name|PatchSet
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
name|PeerDaemonUser
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
name|config
operator|.
name|FactoryModule
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
name|git
operator|.
name|WorkQueue
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
name|ssh
operator|.
name|SshInfo
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
name|sshd
operator|.
name|args4j
operator|.
name|AccountGroupIdHandler
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
name|sshd
operator|.
name|args4j
operator|.
name|AccountIdHandler
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
name|sshd
operator|.
name|args4j
operator|.
name|PatchSetIdHandler
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
name|sshd
operator|.
name|args4j
operator|.
name|ProjectControlHandler
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
name|sshd
operator|.
name|args4j
operator|.
name|SocketAddressHandler
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
name|sshd
operator|.
name|commands
operator|.
name|DefaultCommandModule
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
name|sshd
operator|.
name|commands
operator|.
name|QueryShell
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
name|util
operator|.
name|cli
operator|.
name|CmdLineParser
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
name|util
operator|.
name|cli
operator|.
name|OptionHandlerFactory
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
name|util
operator|.
name|cli
operator|.
name|OptionHandlerUtil
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
name|Scopes
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
name|TypeLiteral
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
name|FactoryProvider
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|KeyPairProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|CommandFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|PasswordAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|PublickeyAuthenticator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|OptionHandler
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

begin_comment
comment|/** Configures standard dependencies for {@link SshDaemon}. */
end_comment

begin_class
DECL|class|SshModule
specifier|public
class|class
name|SshModule
extends|extends
name|FactoryModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bindScope
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|,
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|configureRequestScope
argument_list|()
expr_stmt|;
name|configureCmdLineParser
argument_list|()
expr_stmt|;
name|install
argument_list|(
name|SshKeyCacheImpl
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshLog
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshInfo
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|SshDaemon
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|DispatchCommand
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|QueryShell
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|PeerDaemonUser
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|DispatchCommandProvider
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Commands
operator|.
name|CMD_ROOT
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
literal|""
argument_list|,
name|Commands
operator|.
name|CMD_ROOT
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CommandFactoryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CommandFactory
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|CommandFactoryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|WorkQueue
operator|.
name|Executor
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|StreamCommandExecutor
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|StreamCommandExecutorProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|QueueProvider
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|CommandExecutorQueueProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PublickeyAuthenticator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DatabasePubKeyAuth
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PasswordAuthenticator
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|DatabasePasswordAuth
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|KeyPairProvider
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|HostKeyProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SINGLETON
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|TransferConfig
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DefaultCommandModule
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|LifecycleModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|SshLog
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|SshDaemon
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|configureRequestScope ()
specifier|private
name|void
name|configureRequestScope
parameter_list|()
block|{
name|bind
argument_list|(
name|SshScope
operator|.
name|Context
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SshScope
operator|.
name|ContextProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SshSession
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SshScope
operator|.
name|SshSessionProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|RemotePeer
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SshRemotePeerProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|CurrentUser
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SshCurrentUserProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|SshIdentifiedUserProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|WorkQueue
operator|.
name|Executor
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|CommandExecutor
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|CommandExecutorProvider
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScope
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|GerritRequestModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|configureCmdLineParser ()
specifier|private
name|void
name|configureCmdLineParser
parameter_list|()
block|{
name|factory
argument_list|(
name|CmdLineParser
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|,
name|AccountIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|AccountGroup
operator|.
name|Id
operator|.
name|class
argument_list|,
name|AccountGroupIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|PatchSet
operator|.
name|Id
operator|.
name|class
argument_list|,
name|PatchSetIdHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|ProjectControl
operator|.
name|class
argument_list|,
name|ProjectControlHandler
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerOptionHandler
argument_list|(
name|SocketAddress
operator|.
name|class
argument_list|,
name|SocketAddressHandler
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|registerOptionHandler (Class<T> type, Class<? extends OptionHandler<T>> impl)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|void
name|registerOptionHandler
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
name|impl
parameter_list|)
block|{
specifier|final
name|Key
argument_list|<
name|OptionHandlerFactory
argument_list|<
name|T
argument_list|>
argument_list|>
name|key
init|=
name|OptionHandlerUtil
operator|.
name|keyFor
argument_list|(
name|type
argument_list|)
decl_stmt|;
specifier|final
name|TypeLiteral
argument_list|<
name|OptionHandlerFactory
argument_list|<
name|T
argument_list|>
argument_list|>
name|factoryType
init|=
operator|new
name|TypeLiteral
argument_list|<
name|OptionHandlerFactory
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
specifier|final
name|TypeLiteral
argument_list|<
name|?
extends|extends
name|OptionHandler
argument_list|<
name|T
argument_list|>
argument_list|>
name|implType
init|=
name|TypeLiteral
operator|.
name|get
argument_list|(
name|impl
argument_list|)
decl_stmt|;
name|bind
argument_list|(
name|key
argument_list|)
operator|.
name|toProvider
argument_list|(
name|FactoryProvider
operator|.
name|newFactory
argument_list|(
name|factoryType
argument_list|,
name|implType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

