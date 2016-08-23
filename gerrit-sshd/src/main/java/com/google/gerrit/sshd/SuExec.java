begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|base
operator|.
name|Throwables
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
name|util
operator|.
name|concurrent
operator|.
name|Atomics
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
name|config
operator|.
name|AuthConfig
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
name|SshScope
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
name|inject
operator|.
name|Inject
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
name|Command
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
name|Environment
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
name|Argument
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
name|Option
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
name|ArrayList
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
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_comment
comment|/**  * Executes any other command as a different user identity.  *<p>  * The calling user must be authenticated as a {@link PeerDaemonUser}, which  * usually requires public key authentication using this daemon's private host  * key, or a key on this daemon's peer host key ring.  */
end_comment

begin_class
DECL|class|SuExec
specifier|public
specifier|final
class|class
name|SuExec
extends|extends
name|BaseCommand
block|{
DECL|field|sshScope
specifier|private
specifier|final
name|SshScope
name|sshScope
decl_stmt|;
DECL|field|dispatcher
specifier|private
specifier|final
name|DispatchCommandProvider
name|dispatcher
decl_stmt|;
DECL|field|enableRunAs
specifier|private
name|boolean
name|enableRunAs
decl_stmt|;
DECL|field|caller
specifier|private
name|CurrentUser
name|caller
decl_stmt|;
DECL|field|session
specifier|private
name|SshSession
name|session
decl_stmt|;
DECL|field|userFactory
specifier|private
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|callingContext
specifier|private
name|SshScope
operator|.
name|Context
name|callingContext
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--as"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
DECL|field|accountId
specifier|private
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--from"
argument_list|)
DECL|field|peerAddress
specifier|private
name|SocketAddress
name|peerAddress
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|multiValued
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"COMMAND"
argument_list|)
DECL|field|args
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|args
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|atomicCmd
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|Command
argument_list|>
name|atomicCmd
decl_stmt|;
annotation|@
name|Inject
DECL|method|SuExec (final SshScope sshScope, @CommandName(Commands.ROOT) final DispatchCommandProvider dispatcher, final CurrentUser caller, final SshSession session, final IdentifiedUser.GenericFactory userFactory, final SshScope.Context callingContext, AuthConfig config)
name|SuExec
parameter_list|(
specifier|final
name|SshScope
name|sshScope
parameter_list|,
annotation|@
name|CommandName
argument_list|(
name|Commands
operator|.
name|ROOT
argument_list|)
specifier|final
name|DispatchCommandProvider
name|dispatcher
parameter_list|,
specifier|final
name|CurrentUser
name|caller
parameter_list|,
specifier|final
name|SshSession
name|session
parameter_list|,
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
specifier|final
name|SshScope
operator|.
name|Context
name|callingContext
parameter_list|,
name|AuthConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|sshScope
operator|=
name|sshScope
expr_stmt|;
name|this
operator|.
name|dispatcher
operator|=
name|dispatcher
expr_stmt|;
name|this
operator|.
name|caller
operator|=
name|caller
expr_stmt|;
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|callingContext
operator|=
name|callingContext
expr_stmt|;
name|this
operator|.
name|enableRunAs
operator|=
name|config
operator|.
name|isRunAsEnabled
argument_list|()
expr_stmt|;
name|atomicCmd
operator|=
name|Atomics
operator|.
name|newReference
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start (Environment env)
specifier|public
name|void
name|start
parameter_list|(
name|Environment
name|env
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|checkCanRunAs
argument_list|()
expr_stmt|;
name|parseCommandLine
argument_list|()
expr_stmt|;
specifier|final
name|Context
name|ctx
init|=
name|callingContext
operator|.
name|subContext
argument_list|(
name|newSession
argument_list|()
argument_list|,
name|join
argument_list|(
name|args
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Context
name|old
init|=
name|sshScope
operator|.
name|set
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|BaseCommand
name|cmd
init|=
name|dispatcher
operator|.
name|get
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|setArguments
argument_list|(
name|args
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|args
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|provideStateTo
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|atomicCmd
operator|.
name|set
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
name|cmd
operator|.
name|start
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|sshScope
operator|.
name|set
argument_list|(
name|old
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|UnloggedFailure
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|msg
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|msg
operator|+=
literal|"\n"
expr_stmt|;
block|}
name|err
operator|.
name|write
argument_list|(
name|msg
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|err
operator|.
name|flush
argument_list|()
expr_stmt|;
name|onExit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkCanRunAs ()
specifier|private
name|void
name|checkCanRunAs
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
if|if
condition|(
name|caller
operator|instanceof
name|PeerDaemonUser
condition|)
block|{
comment|// OK.
block|}
elseif|else
if|if
condition|(
operator|!
name|enableRunAs
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"suexec disabled by auth.enableRunAs = false"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|caller
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canRunAs
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"suexec not permitted"
argument_list|)
throw|;
block|}
block|}
DECL|method|newSession ()
specifier|private
name|SshSession
name|newSession
parameter_list|()
block|{
specifier|final
name|SocketAddress
name|peer
decl_stmt|;
if|if
condition|(
name|peerAddress
operator|==
literal|null
condition|)
block|{
name|peer
operator|=
name|session
operator|.
name|getRemoteAddress
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|peer
operator|=
name|peerAddress
expr_stmt|;
block|}
if|if
condition|(
name|caller
operator|instanceof
name|PeerDaemonUser
condition|)
block|{
name|caller
operator|=
literal|null
expr_stmt|;
block|}
return|return
operator|new
name|SshSession
argument_list|(
name|session
argument_list|,
name|peer
argument_list|,
name|userFactory
operator|.
name|runAs
argument_list|(
name|peer
argument_list|,
name|accountId
argument_list|,
name|caller
argument_list|)
argument_list|)
return|;
block|}
DECL|method|join (List<String> args)
specifier|private
specifier|static
name|String
name|join
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|args
parameter_list|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|a
range|:
name|args
control|)
block|{
if|if
condition|(
name|r
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|a
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
block|{
name|Command
name|cmd
init|=
name|atomicCmd
operator|.
name|getAndSet
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|cmd
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|throwIfUnchecked
argument_list|(
name|e
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

