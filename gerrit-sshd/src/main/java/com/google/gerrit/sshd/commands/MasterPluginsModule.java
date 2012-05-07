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
name|common
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
name|common
operator|.
name|PluginLoader
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
name|inject
operator|.
name|Inject
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|MasterPluginsModule
specifier|public
class|class
name|MasterPluginsModule
extends|extends
name|CommandModule
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
name|MasterPluginsModule
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|pluginLoader
specifier|private
name|PluginLoader
name|pluginLoader
decl_stmt|;
annotation|@
name|Inject
DECL|method|MasterPluginsModule (PluginLoader loader)
name|MasterPluginsModule
parameter_list|(
name|PluginLoader
name|loader
parameter_list|)
block|{
name|pluginLoader
operator|=
name|loader
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
name|Collection
argument_list|<
name|Plugin
argument_list|>
name|plugins
init|=
name|pluginLoader
operator|.
name|getPlugins
argument_list|()
decl_stmt|;
for|for
control|(
name|Plugin
name|p
range|:
name|plugins
control|)
block|{
if|if
condition|(
name|PluginCommandModule
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|p
operator|.
name|moduleClass
argument_list|)
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|Class
argument_list|<
name|PluginCommandModule
argument_list|>
name|c
init|=
operator|(
name|Class
argument_list|<
name|PluginCommandModule
argument_list|>
operator|)
name|p
operator|.
name|moduleClass
decl_stmt|;
try|try
block|{
name|PluginCommandModule
name|module
init|=
name|c
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|module
operator|.
name|initSshModule
argument_list|(
name|p
operator|.
name|name
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|module
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InstantiationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Initialization of plugin module '"
operator|+
name|p
operator|.
name|name
operator|+
literal|"' failed"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Initialization of plugin module '"
operator|+
name|p
operator|.
name|name
operator|+
literal|"' failed"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

