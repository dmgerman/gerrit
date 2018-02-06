begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|sshd
operator|.
name|AbstractGitCommand
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PacketLineOut
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
name|ServiceNotEnabledException
import|;
end_import

begin_comment
comment|/* Receive command when running in slave mode. */
end_comment

begin_class
DECL|class|ReceiveSlaveMode
specifier|public
class|class
name|ReceiveSlaveMode
extends|extends
name|AbstractGitCommand
block|{
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|UnloggedFailure
throws|,
name|IOException
block|{
name|ServiceNotEnabledException
name|ex
init|=
operator|new
name|ServiceNotEnabledException
argument_list|()
decl_stmt|;
name|PacketLineOut
name|packetOut
init|=
operator|new
name|PacketLineOut
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|packetOut
operator|.
name|setFlushOnEnd
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|packetOut
operator|.
name|writeString
argument_list|(
literal|"ERR "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|packetOut
operator|.
name|end
argument_list|()
expr_stmt|;
throw|throw
name|die
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

