begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|SshScopes
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|SocketSessionConfig
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
name|FactoryManager
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
name|util
operator|.
name|Buffer
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_class
DECL|class|GerritServerSession
class|class
name|GerritServerSession
extends|extends
name|ServerSession
block|{
DECL|field|sessionContext
specifier|final
name|Context
name|sessionContext
decl_stmt|;
DECL|method|GerritServerSession (FactoryManager server, IoSession io, boolean keepAlive)
name|GerritServerSession
parameter_list|(
name|FactoryManager
name|server
parameter_list|,
name|IoSession
name|io
parameter_list|,
name|boolean
name|keepAlive
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|server
argument_list|,
name|io
argument_list|)
expr_stmt|;
if|if
condition|(
name|io
operator|.
name|getConfig
argument_list|()
operator|instanceof
name|SocketSessionConfig
condition|)
block|{
specifier|final
name|SocketSessionConfig
name|c
init|=
operator|(
name|SocketSessionConfig
operator|)
name|io
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|c
operator|.
name|setKeepAlive
argument_list|(
name|keepAlive
argument_list|)
expr_stmt|;
block|}
name|setAttribute
argument_list|(
name|SshUtil
operator|.
name|REMOTE_PEER
argument_list|,
name|io
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
name|SshUtil
operator|.
name|ACTIVE
argument_list|,
operator|new
name|ArrayList
argument_list|<
name|AbstractCommand
argument_list|>
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|sessionContext
operator|=
operator|new
name|Context
argument_list|(
name|this
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleMessage (final Buffer buffer)
specifier|protected
name|void
name|handleMessage
parameter_list|(
specifier|final
name|Buffer
name|buffer
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|SshScopes
operator|.
name|current
operator|.
name|set
argument_list|(
name|sessionContext
argument_list|)
expr_stmt|;
name|super
operator|.
name|handleMessage
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|SshScopes
operator|.
name|current
operator|.
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

