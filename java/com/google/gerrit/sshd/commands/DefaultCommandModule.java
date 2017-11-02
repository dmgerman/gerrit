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
name|client
operator|.
name|CoreDownloadSchemes
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
name|DownloadConfig
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
name|CommandModule
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
name|CommandName
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
name|Commands
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
name|DispatchCommandProvider
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
name|SuExec
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
name|plugin
operator|.
name|LfsPluginAuthCommand
import|;
end_import

begin_comment
comment|/** Register the commands a Gerrit server supports. */
end_comment

begin_class
DECL|class|DefaultCommandModule
specifier|public
class|class
name|DefaultCommandModule
extends|extends
name|CommandModule
block|{
DECL|field|downloadConfig
specifier|private
specifier|final
name|DownloadConfig
name|downloadConfig
decl_stmt|;
DECL|field|lfsPluginAuthModule
specifier|private
specifier|final
name|LfsPluginAuthCommand
operator|.
name|Module
name|lfsPluginAuthModule
decl_stmt|;
DECL|method|DefaultCommandModule ( boolean slave, DownloadConfig downloadCfg, LfsPluginAuthCommand.Module module)
specifier|public
name|DefaultCommandModule
parameter_list|(
name|boolean
name|slave
parameter_list|,
name|DownloadConfig
name|downloadCfg
parameter_list|,
name|LfsPluginAuthCommand
operator|.
name|Module
name|module
parameter_list|)
block|{
name|slaveMode
operator|=
name|slave
expr_stmt|;
name|downloadConfig
operator|=
name|downloadCfg
expr_stmt|;
name|lfsPluginAuthModule
operator|=
name|module
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|CommandName
name|git
init|=
name|Commands
operator|.
name|named
argument_list|(
literal|"git"
argument_list|)
decl_stmt|;
name|CommandName
name|gerrit
init|=
name|Commands
operator|.
name|named
argument_list|(
literal|"gerrit"
argument_list|)
decl_stmt|;
name|CommandName
name|logging
init|=
name|Commands
operator|.
name|named
argument_list|(
name|gerrit
argument_list|,
literal|"logging"
argument_list|)
decl_stmt|;
name|CommandName
name|plugin
init|=
name|Commands
operator|.
name|named
argument_list|(
name|gerrit
argument_list|,
literal|"plugin"
argument_list|)
decl_stmt|;
name|CommandName
name|testSubmit
init|=
name|Commands
operator|.
name|named
argument_list|(
name|gerrit
argument_list|,
literal|"test-submit"
argument_list|)
decl_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|gerrit
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|AproposCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|BanCommitCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|CloseConnection
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|FlushCaches
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ListProjectsCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ListMembersCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ListGroupsCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|LsUserRefs
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|Query
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ShowCaches
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ShowConnections
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ShowQueue
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|StreamEvents
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|VersionCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|GarbageCollectionCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
literal|"plugin"
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|plugin
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|plugin
argument_list|,
name|PluginLsCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|plugin
argument_list|,
name|PluginEnableCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|plugin
argument_list|,
name|PluginInstallCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|plugin
argument_list|,
name|PluginReloadCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|plugin
argument_list|,
name|PluginRemoveCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|alias
argument_list|(
name|plugin
argument_list|,
literal|"add"
argument_list|,
name|PluginInstallCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|alias
argument_list|(
name|plugin
argument_list|,
literal|"rm"
argument_list|,
name|PluginRemoveCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|git
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|git
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"ps"
argument_list|)
operator|.
name|to
argument_list|(
name|ShowQueue
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"kill"
argument_list|)
operator|.
name|to
argument_list|(
name|KillCommand
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
comment|// Honor the legacy hyphenated forms as aliases for the non-hyphenated forms
if|if
condition|(
name|sshEnabled
argument_list|()
condition|)
block|{
name|command
argument_list|(
literal|"git-upload-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|git
argument_list|,
literal|"upload-pack"
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|git
argument_list|,
literal|"upload-pack"
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
literal|"git-upload-archive"
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|git
argument_list|,
literal|"upload-archive"
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|git
argument_list|,
literal|"upload-archive"
argument_list|)
operator|.
name|to
argument_list|(
name|UploadArchive
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|command
argument_list|(
literal|"suexec"
argument_list|)
operator|.
name|to
argument_list|(
name|SuExec
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|ShowCaches
operator|.
name|StartupListener
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|CreateAccountCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|CreateGroupCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|CreateProjectCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|SetHeadCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|AdminQueryShell
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|slaveMode
condition|)
block|{
name|command
argument_list|(
literal|"git-receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|NotSupportedInSlaveModeFailureCommand
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
name|NotSupportedInSlaveModeFailureCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|git
argument_list|,
literal|"receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|NotSupportedInSlaveModeFailureCommand
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|sshEnabled
argument_list|()
condition|)
block|{
name|command
argument_list|(
literal|"git-receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|git
argument_list|,
literal|"receive-pack"
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
literal|"gerrit-receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|git
argument_list|,
literal|"receive-pack"
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|git
argument_list|,
literal|"receive-pack"
argument_list|)
operator|.
name|to
argument_list|(
name|Commands
operator|.
name|key
argument_list|(
name|gerrit
argument_list|,
literal|"receive-pack"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|command
argument_list|(
name|gerrit
argument_list|,
literal|"test-submit"
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|testSubmit
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|command
argument_list|(
name|gerrit
argument_list|,
name|Receive
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|RenameGroupCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|ReviewCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|SetProjectCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|SetReviewersCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|SetMembersCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|CreateBranchCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|SetAccountCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|gerrit
argument_list|,
name|AdminSetParent
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|testSubmit
argument_list|,
name|TestSubmitRuleCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|testSubmit
argument_list|,
name|TestSubmitTypeCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|logging
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|DispatchCommandProvider
argument_list|(
name|logging
argument_list|)
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|logging
argument_list|,
name|SetLoggingLevelCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|command
argument_list|(
name|logging
argument_list|,
name|ListLoggingLevelCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|alias
argument_list|(
name|logging
argument_list|,
literal|"ls"
argument_list|,
name|ListLoggingLevelCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|alias
argument_list|(
name|logging
argument_list|,
literal|"set"
argument_list|,
name|SetLoggingLevelCommand
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|lfsPluginAuthModule
argument_list|)
expr_stmt|;
block|}
DECL|method|sshEnabled ()
specifier|private
name|boolean
name|sshEnabled
parameter_list|()
block|{
return|return
name|downloadConfig
operator|.
name|getDownloadSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|CoreDownloadSchemes
operator|.
name|SSH
argument_list|)
return|;
block|}
block|}
end_class

end_unit
