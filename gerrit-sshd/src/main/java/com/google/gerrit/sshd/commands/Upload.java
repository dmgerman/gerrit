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
name|reviewdb
operator|.
name|ReviewDb
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
name|sshd
operator|.
name|AbstractGitCommand
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
name|Provider
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
name|UploadPack
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

begin_comment
comment|/** Publishes Git repositories over SSH using the Git upload-pack protocol. */
end_comment

begin_class
DECL|class|Upload
specifier|final
class|class
name|Upload
extends|extends
name|AbstractGitCommand
block|{
annotation|@
name|Inject
DECL|field|db
specifier|private
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
annotation|@
name|Inject
DECL|field|config
specifier|private
name|TransferConfig
name|config
decl_stmt|;
annotation|@
name|Override
DECL|method|runImpl ()
specifier|protected
name|void
name|runImpl
parameter_list|()
throws|throws
name|IOException
throws|,
name|Failure
block|{
if|if
condition|(
operator|!
name|projectControl
operator|.
name|canRunUploadPack
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"fatal: upload-pack not permitted on this server"
argument_list|)
throw|;
block|}
specifier|final
name|UploadPack
name|up
init|=
operator|new
name|UploadPack
argument_list|(
name|repo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projectControl
operator|.
name|allRefsAreVisible
argument_list|()
condition|)
block|{
name|up
operator|.
name|setRefFilter
argument_list|(
operator|new
name|VisibleRefFilter
argument_list|(
name|repo
argument_list|,
name|projectControl
argument_list|,
name|db
operator|.
name|get
argument_list|()
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|up
operator|.
name|setPackConfig
argument_list|(
name|config
operator|.
name|getPackConfig
argument_list|()
argument_list|)
expr_stmt|;
name|up
operator|.
name|setTimeout
argument_list|(
name|config
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|up
operator|.
name|upload
argument_list|(
name|in
argument_list|,
name|out
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

