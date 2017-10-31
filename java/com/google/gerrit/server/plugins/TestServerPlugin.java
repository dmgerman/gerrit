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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
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

begin_class
DECL|class|TestServerPlugin
specifier|public
class|class
name|TestServerPlugin
extends|extends
name|ServerPlugin
block|{
DECL|field|classLoader
specifier|private
specifier|final
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|field|sysName
specifier|private
name|String
name|sysName
decl_stmt|;
DECL|field|httpName
specifier|private
name|String
name|httpName
decl_stmt|;
DECL|field|sshName
specifier|private
name|String
name|sshName
decl_stmt|;
DECL|method|TestServerPlugin ( String name, String pluginCanonicalWebUrl, PluginUser user, ClassLoader classloader, String sysName, String httpName, String sshName)
specifier|public
name|TestServerPlugin
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pluginCanonicalWebUrl
parameter_list|,
name|PluginUser
name|user
parameter_list|,
name|ClassLoader
name|classloader
parameter_list|,
name|String
name|sysName
parameter_list|,
name|String
name|httpName
parameter_list|,
name|String
name|sshName
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|super
argument_list|(
name|name
argument_list|,
name|pluginCanonicalWebUrl
argument_list|,
name|user
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|classloader
argument_list|)
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
name|classloader
expr_stmt|;
name|this
operator|.
name|sysName
operator|=
name|sysName
expr_stmt|;
name|this
operator|.
name|httpName
operator|=
name|httpName
expr_stmt|;
name|this
operator|.
name|sshName
operator|=
name|sshName
expr_stmt|;
name|loadGuiceModules
argument_list|()
expr_stmt|;
block|}
DECL|method|loadGuiceModules ()
specifier|private
name|void
name|loadGuiceModules
parameter_list|()
throws|throws
name|InvalidPluginException
block|{
try|try
block|{
name|this
operator|.
name|sysModule
operator|=
name|load
argument_list|(
name|sysName
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpModule
operator|=
name|load
argument_list|(
name|httpName
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
name|this
operator|.
name|sshModule
operator|=
name|load
argument_list|(
name|sshName
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
literal|"Unable to load plugin Guice Modules"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
literal|"1.0"
return|;
block|}
annotation|@
name|Override
DECL|method|canReload ()
specifier|protected
name|boolean
name|canReload
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
comment|// Widen access modifier in derived class
DECL|method|start (PluginGuiceEnvironment env)
specifier|public
name|void
name|start
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|start
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
comment|// Widen access modifier in derived class
DECL|method|stop (PluginGuiceEnvironment env)
specifier|public
name|void
name|stop
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
block|{
name|super
operator|.
name|stop
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getContentScanner ()
specifier|public
name|PluginContentScanner
name|getContentScanner
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

