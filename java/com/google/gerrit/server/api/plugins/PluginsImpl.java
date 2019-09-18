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
DECL|package|com.google.gerrit.server.api.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|plugins
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|ApiUtil
operator|.
name|asRestApiException
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
name|extensions
operator|.
name|api
operator|.
name|plugins
operator|.
name|InstallPluginInput
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
name|extensions
operator|.
name|api
operator|.
name|plugins
operator|.
name|PluginApi
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
name|extensions
operator|.
name|api
operator|.
name|plugins
operator|.
name|Plugins
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
name|extensions
operator|.
name|common
operator|.
name|PluginInfo
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
name|extensions
operator|.
name|restapi
operator|.
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|InstallPlugin
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
name|ListPlugins
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
name|PluginsCollection
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
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PluginsImpl
specifier|public
class|class
name|PluginsImpl
implements|implements
name|Plugins
block|{
DECL|field|plugins
specifier|private
specifier|final
name|PluginsCollection
name|plugins
decl_stmt|;
DECL|field|listProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListPlugins
argument_list|>
name|listProvider
decl_stmt|;
DECL|field|installProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|InstallPlugin
argument_list|>
name|installProvider
decl_stmt|;
DECL|field|pluginApi
specifier|private
specifier|final
name|PluginApiImpl
operator|.
name|Factory
name|pluginApi
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginsImpl ( PluginsCollection plugins, Provider<ListPlugins> listProvider, Provider<InstallPlugin> installProvider, PluginApiImpl.Factory pluginApi)
name|PluginsImpl
parameter_list|(
name|PluginsCollection
name|plugins
parameter_list|,
name|Provider
argument_list|<
name|ListPlugins
argument_list|>
name|listProvider
parameter_list|,
name|Provider
argument_list|<
name|InstallPlugin
argument_list|>
name|installProvider
parameter_list|,
name|PluginApiImpl
operator|.
name|Factory
name|pluginApi
parameter_list|)
block|{
name|this
operator|.
name|plugins
operator|=
name|plugins
expr_stmt|;
name|this
operator|.
name|listProvider
operator|=
name|listProvider
expr_stmt|;
name|this
operator|.
name|installProvider
operator|=
name|installProvider
expr_stmt|;
name|this
operator|.
name|pluginApi
operator|=
name|pluginApi
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|name (String name)
specifier|public
name|PluginApi
name|name
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|pluginApi
operator|.
name|create
argument_list|(
name|plugins
operator|.
name|parse
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|ListRequest
name|list
parameter_list|()
block|{
return|return
operator|new
name|ListRequest
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SortedMap
argument_list|<
name|String
argument_list|,
name|PluginInfo
argument_list|>
name|getAsMap
parameter_list|()
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|listProvider
operator|.
name|get
argument_list|()
operator|.
name|request
argument_list|(
name|this
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot list plugins"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
return|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|install ( String name, com.google.gerrit.extensions.common.InstallPluginInput input)
specifier|public
name|PluginApi
name|install
parameter_list|(
name|String
name|name
parameter_list|,
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
return|return
name|install
argument_list|(
name|name
argument_list|,
name|convertInput
argument_list|(
name|input
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|convertInput ( com.google.gerrit.extensions.common.InstallPluginInput input)
specifier|private
name|InstallPluginInput
name|convertInput
parameter_list|(
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
operator|.
name|InstallPluginInput
name|input
parameter_list|)
block|{
name|InstallPluginInput
name|result
init|=
operator|new
name|InstallPluginInput
argument_list|()
decl_stmt|;
name|result
operator|.
name|url
operator|=
name|input
operator|.
name|url
expr_stmt|;
name|result
operator|.
name|raw
operator|=
name|input
operator|.
name|raw
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|install (String name, InstallPluginInput input)
specifier|public
name|PluginApi
name|install
parameter_list|(
name|String
name|name
parameter_list|,
name|InstallPluginInput
name|input
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|Response
argument_list|<
name|PluginInfo
argument_list|>
name|created
init|=
name|installProvider
operator|.
name|get
argument_list|()
operator|.
name|setName
argument_list|(
name|name
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|input
argument_list|)
decl_stmt|;
return|return
name|pluginApi
operator|.
name|create
argument_list|(
name|plugins
operator|.
name|parse
argument_list|(
name|created
operator|.
name|value
argument_list|()
operator|.
name|id
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot install plugin"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

