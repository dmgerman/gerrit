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
DECL|package|com.google.gerrit.server.ssh
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ssh
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
name|ssh
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
name|servlet
operator|.
name|SessionScoped
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
name|session
operator|.
name|AbstractSession
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
name|PublickeyAuthenticator
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
name|session
operator|.
name|ServerSession
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
DECL|class|SshDaemonModule
specifier|public
class|class
name|SshDaemonModule
extends|extends
name|FactoryModule
block|{
DECL|field|NAME
specifier|private
specifier|static
specifier|final
name|String
name|NAME
init|=
literal|"Gerrit Code Review"
decl_stmt|;
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
name|SessionScoped
operator|.
name|class
argument_list|,
name|SshScopes
operator|.
name|SESSION
argument_list|)
expr_stmt|;
name|bindScope
argument_list|(
name|RequestScoped
operator|.
name|class
argument_list|,
name|SshScopes
operator|.
name|REQUEST
argument_list|)
expr_stmt|;
name|configureSessionScope
argument_list|()
expr_stmt|;
name|configureRequestScope
argument_list|()
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
name|NAME
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
name|install
argument_list|(
operator|new
name|DefaultCommandModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|configureSessionScope ()
specifier|private
name|void
name|configureSessionScope
parameter_list|()
block|{
name|bind
argument_list|(
name|ServerSession
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|Provider
argument_list|<
name|ServerSession
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ServerSession
name|get
parameter_list|()
block|{
return|return
name|SshScopes
operator|.
name|getContext
argument_list|()
operator|.
name|session
return|;
block|}
block|}
argument_list|)
operator|.
name|in
argument_list|(
name|SshScopes
operator|.
name|SESSION
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|AbstractSession
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ServerSession
operator|.
name|class
argument_list|)
operator|.
name|in
argument_list|(
name|SshScopes
operator|.
name|SESSION
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
operator|new
name|Provider
argument_list|<
name|SocketAddress
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SocketAddress
name|get
parameter_list|()
block|{
return|return
name|SshScopes
operator|.
name|getContext
argument_list|()
operator|.
name|session
operator|.
name|getAttribute
argument_list|(
name|SshUtil
operator|.
name|REMOTE_PEER
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|in
argument_list|(
name|SshScopes
operator|.
name|SESSION
argument_list|)
expr_stmt|;
block|}
DECL|method|configureRequestScope ()
specifier|private
name|void
name|configureRequestScope
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
name|IdentifiedUser
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
name|SshScopes
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
name|to
argument_list|(
name|IdentifiedUser
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

