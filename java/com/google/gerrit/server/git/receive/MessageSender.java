begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|gerrit
operator|.
name|common
operator|.
name|UsedAt
import|;
end_import

begin_comment
comment|/**  * Interface used by {@link ReceiveCommits} for send messages over the wire during {@code  * receive-pack}.  */
end_comment

begin_interface
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|GOOGLE
argument_list|)
DECL|interface|MessageSender
specifier|public
interface|interface
name|MessageSender
block|{
DECL|method|sendMessage (String what)
name|void
name|sendMessage
parameter_list|(
name|String
name|what
parameter_list|)
function_decl|;
DECL|method|sendError (String what)
name|void
name|sendError
parameter_list|(
name|String
name|what
parameter_list|)
function_decl|;
DECL|method|sendBytes (byte[] what)
name|void
name|sendBytes
parameter_list|(
name|byte
index|[]
name|what
parameter_list|)
function_decl|;
DECL|method|sendBytes (byte[] what, int off, int len)
name|void
name|sendBytes
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
function_decl|;
DECL|method|flush ()
name|void
name|flush
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

