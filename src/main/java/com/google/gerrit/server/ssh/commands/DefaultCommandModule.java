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
DECL|package|com.google.gerrit.server.ssh.commands
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
name|server
operator|.
name|ssh
operator|.
name|CommandModule
import|;
end_import

begin_comment
comment|/** Register the basic commands any Gerrit server should support. */
end_comment

begin_class
DECL|class|DefaultCommandModule
specifier|public
class|class
name|DefaultCommandModule
extends|extends
name|CommandModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|command
argument_list|(
literal|"gerrit-flush-caches"
argument_list|)
operator|.
name|to
argument_list|(
name|AdminFlushCaches
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-ls-projects"
argument_list|)
operator|.
name|to
argument_list|(
name|ListProjects
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Receive
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-replicate"
argument_list|)
operator|.
name|to
argument_list|(
name|AdminReplicate
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-show-caches"
argument_list|)
operator|.
name|to
argument_list|(
name|AdminShowCaches
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-show-connections"
argument_list|)
operator|.
name|to
argument_list|(
name|AdminShowConnections
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-show-queue"
argument_list|)
operator|.
name|to
argument_list|(
name|AdminShowQueue
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-upload-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Upload
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"scp"
argument_list|)
operator|.
name|to
argument_list|(
name|ScpCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"git-upload-pack"
argument_list|,
literal|"gerrit-upload-pack"
argument_list|)
expr_stmt|;
name|alias
argument_list|(
literal|"git-receive-pack"
argument_list|,
literal|"gerrit-receive-pack"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

