begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|PluginUser
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
name|PluginGuiceEnvironment
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
name|TestServerPlugin
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|LightweightPluginDaemonTest
specifier|public
class|class
name|LightweightPluginDaemonTest
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|env
annotation|@
name|Inject
specifier|private
name|PluginGuiceEnvironment
name|env
decl_stmt|;
DECL|field|pluginUserFactory
annotation|@
name|Inject
specifier|private
name|PluginUser
operator|.
name|Factory
name|pluginUserFactory
decl_stmt|;
DECL|field|plugin
specifier|private
name|TestServerPlugin
name|plugin
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|TestPlugin
name|testPlugin
init|=
name|getTestPlugin
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|testPlugin
operator|.
name|name
argument_list|()
decl_stmt|;
name|plugin
operator|=
operator|new
name|TestServerPlugin
argument_list|(
name|name
argument_list|,
name|canonicalWebUrl
operator|.
name|get
argument_list|()
operator|+
literal|"plugins/"
operator|+
name|name
argument_list|,
name|pluginUserFactory
operator|.
name|create
argument_list|(
name|name
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|testPlugin
operator|.
name|sysModule
argument_list|()
argument_list|,
name|testPlugin
operator|.
name|httpModule
argument_list|()
argument_list|,
name|testPlugin
operator|.
name|sshModule
argument_list|()
argument_list|,
name|tempSiteDir
operator|.
name|newFolder
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
name|plugin
operator|.
name|start
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|env
operator|.
name|onStartPlugin
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|plugin
operator|!=
literal|null
condition|)
block|{
comment|// plugin will be null if the plugin test requires ssh, but the command
comment|// line flag says we are running tests without ssh as the assume()
comment|// statement in AbstractDaemonTest will prevent the execution of setUp()
comment|// in this class
name|plugin
operator|.
name|stop
argument_list|(
name|env
argument_list|)
expr_stmt|;
name|env
operator|.
name|onStopPlugin
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getTestPlugin (Class<?> clazz)
specifier|private
specifier|static
name|TestPlugin
name|getTestPlugin
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|)
block|{
for|for
control|(
init|;
name|clazz
operator|!=
literal|null
condition|;
name|clazz
operator|=
name|clazz
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
if|if
condition|(
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|TestPlugin
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|clazz
operator|.
name|getAnnotation
argument_list|(
name|TestPlugin
operator|.
name|class
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"TestPlugin annotation missing"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

