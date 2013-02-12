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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Scanner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelExec
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
import|;
end_import

begin_class
DECL|class|SshSession
specifier|public
class|class
name|SshSession
block|{
DECL|field|account
specifier|private
specifier|final
name|TestAccount
name|account
decl_stmt|;
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
DECL|method|SshSession (TestAccount account)
specifier|public
name|SshSession
parameter_list|(
name|TestAccount
name|account
parameter_list|)
block|{
name|this
operator|.
name|account
operator|=
name|account
expr_stmt|;
block|}
DECL|method|exec (String command)
specifier|public
name|String
name|exec
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|JSchException
throws|,
name|IOException
block|{
name|ChannelExec
name|channel
init|=
operator|(
name|ChannelExec
operator|)
name|getSession
argument_list|()
operator|.
name|openChannel
argument_list|(
literal|"exec"
argument_list|)
decl_stmt|;
try|try
block|{
name|channel
operator|.
name|setCommand
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setInputStream
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|InputStream
name|in
init|=
name|channel
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Scanner
name|s
init|=
operator|new
name|Scanner
argument_list|(
name|in
argument_list|)
operator|.
name|useDelimiter
argument_list|(
literal|"\\A"
argument_list|)
decl_stmt|;
return|return
name|s
operator|.
name|hasNext
argument_list|()
condition|?
name|s
operator|.
name|next
argument_list|()
else|:
literal|""
return|;
block|}
finally|finally
block|{
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|session
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getSession ()
specifier|private
name|Session
name|getSession
parameter_list|()
throws|throws
name|JSchException
block|{
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
name|jsch
operator|.
name|addIdentity
argument_list|(
literal|"KeyPair"
argument_list|,
name|account
operator|.
name|privateKey
argument_list|()
argument_list|,
name|account
operator|.
name|sshKey
operator|.
name|getPublicKeyBlob
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|session
operator|=
name|jsch
operator|.
name|getSession
argument_list|(
name|account
operator|.
name|username
argument_list|,
literal|"localhost"
argument_list|,
literal|29418
argument_list|)
expr_stmt|;
name|session
operator|.
name|setConfig
argument_list|(
literal|"StrictHostKeyChecking"
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|session
operator|.
name|connect
argument_list|()
expr_stmt|;
block|}
return|return
name|session
return|;
block|}
block|}
end_class

end_unit

