begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|future
operator|.
name|CloseFuture
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
name|future
operator|.
name|SshFutureListener
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
name|io
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
name|sshd
operator|.
name|server
operator|.
name|ServerFactoryManager
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

begin_comment
comment|/* Expose addition of close session listeners */
end_comment

begin_class
DECL|class|GerritServerSession
class|class
name|GerritServerSession
extends|extends
name|ServerSession
block|{
DECL|method|GerritServerSession (ServerFactoryManager server, IoSession ioSession)
name|GerritServerSession
parameter_list|(
name|ServerFactoryManager
name|server
parameter_list|,
name|IoSession
name|ioSession
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|server
argument_list|,
name|ioSession
argument_list|)
expr_stmt|;
block|}
DECL|method|addCloseSessionListener (SshFutureListener<CloseFuture> l)
name|void
name|addCloseSessionListener
parameter_list|(
name|SshFutureListener
argument_list|<
name|CloseFuture
argument_list|>
name|l
parameter_list|)
block|{
name|closeFuture
operator|.
name|addListener
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

