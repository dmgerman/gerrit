begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
operator|.
name|Plugin
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
name|plugins
operator|.
name|StartPluginListener
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
name|Key
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
name|Singleton
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|SshPluginStarterCallback
class|class
name|SshPluginStarterCallback
implements|implements
name|StartPluginListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SshPluginStarterCallback
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|root
specifier|private
specifier|final
name|DispatchCommandProvider
name|root
decl_stmt|;
annotation|@
name|Inject
DECL|method|SshPluginStarterCallback ( @ommandNameCommands.ROOT) DispatchCommandProvider root)
name|SshPluginStarterCallback
parameter_list|(
annotation|@
name|CommandName
argument_list|(
name|Commands
operator|.
name|ROOT
argument_list|)
name|DispatchCommandProvider
name|root
parameter_list|)
block|{
name|this
operator|.
name|root
operator|=
name|root
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStartPlugin (Plugin plugin)
specifier|public
name|void
name|onStartPlugin
parameter_list|(
name|Plugin
name|plugin
parameter_list|)
block|{
if|if
condition|(
name|plugin
operator|.
name|getSshInjector
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Key
argument_list|<
name|Command
argument_list|>
name|key
init|=
name|Commands
operator|.
name|key
argument_list|(
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Provider
argument_list|<
name|Command
argument_list|>
name|cmd
decl_stmt|;
try|try
block|{
name|cmd
operator|=
name|plugin
operator|.
name|getSshInjector
argument_list|()
operator|.
name|getProvider
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Plugin %s does not define command"
argument_list|,
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return;
block|}
name|plugin
operator|.
name|add
argument_list|(
name|root
operator|.
name|register
argument_list|(
name|Commands
operator|.
name|named
argument_list|(
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|cmd
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

