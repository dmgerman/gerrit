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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|FluentIterable
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
name|PluginData
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|ConsoleUI
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitFlags
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
name|pgm
operator|.
name|init
operator|.
name|api
operator|.
name|InitStep
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
name|SitePaths
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
name|JarPluginProvider
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
name|Injector
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|Manifest
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|InitPlugins
specifier|public
class|class
name|InitPlugins
implements|implements
name|InitStep
block|{
DECL|field|PLUGIN_DIR
specifier|public
specifier|static
specifier|final
name|String
name|PLUGIN_DIR
init|=
literal|"WEB-INF/plugins/"
decl_stmt|;
DECL|field|JAR
specifier|public
specifier|static
specifier|final
name|String
name|JAR
init|=
literal|".jar"
decl_stmt|;
DECL|method|listPlugins ( SitePaths site, PluginsDistribution pluginsDistribution)
specifier|public
specifier|static
name|List
argument_list|<
name|PluginData
argument_list|>
name|listPlugins
parameter_list|(
name|SitePaths
name|site
parameter_list|,
name|PluginsDistribution
name|pluginsDistribution
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|listPlugins
argument_list|(
name|site
argument_list|,
literal|false
argument_list|,
name|pluginsDistribution
argument_list|)
return|;
block|}
DECL|method|listPluginsAndRemoveTempFiles ( SitePaths site, PluginsDistribution pluginsDistribution)
specifier|public
specifier|static
name|List
argument_list|<
name|PluginData
argument_list|>
name|listPluginsAndRemoveTempFiles
parameter_list|(
name|SitePaths
name|site
parameter_list|,
name|PluginsDistribution
name|pluginsDistribution
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|listPlugins
argument_list|(
name|site
argument_list|,
literal|true
argument_list|,
name|pluginsDistribution
argument_list|)
return|;
block|}
DECL|method|listPlugins ( final SitePaths site, final boolean deleteTempPluginFile, PluginsDistribution pluginsDistribution)
specifier|private
specifier|static
name|List
argument_list|<
name|PluginData
argument_list|>
name|listPlugins
parameter_list|(
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|boolean
name|deleteTempPluginFile
parameter_list|,
name|PluginsDistribution
name|pluginsDistribution
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|List
argument_list|<
name|PluginData
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|pluginsDistribution
operator|.
name|foreach
argument_list|(
operator|new
name|PluginsDistribution
operator|.
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|InputStream
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|tmpPlugin
init|=
name|JarPluginProvider
operator|.
name|storeInTemp
argument_list|(
name|pluginName
argument_list|,
name|in
argument_list|,
name|site
argument_list|)
decl_stmt|;
name|String
name|pluginVersion
init|=
name|getVersion
argument_list|(
name|tmpPlugin
argument_list|)
decl_stmt|;
if|if
condition|(
name|deleteTempPluginFile
condition|)
block|{
name|Files
operator|.
name|delete
argument_list|(
name|tmpPlugin
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|add
argument_list|(
operator|new
name|PluginData
argument_list|(
name|pluginName
argument_list|,
name|pluginVersion
argument_list|,
name|tmpPlugin
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|result
argument_list|)
operator|.
name|toSortedList
argument_list|(
operator|new
name|Comparator
argument_list|<
name|PluginData
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|PluginData
name|a
parameter_list|,
name|PluginData
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|name
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|name
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
DECL|field|initFlags
specifier|private
specifier|final
name|InitFlags
name|initFlags
decl_stmt|;
DECL|field|pluginLoader
specifier|private
specifier|final
name|InitPluginStepsLoader
name|pluginLoader
decl_stmt|;
DECL|field|pluginsDistribution
specifier|private
specifier|final
name|PluginsDistribution
name|pluginsDistribution
decl_stmt|;
DECL|field|postRunInjector
specifier|private
name|Injector
name|postRunInjector
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitPlugins ( final ConsoleUI ui, final SitePaths site, InitFlags initFlags, InitPluginStepsLoader pluginLoader, PluginsDistribution pluginsDistribution)
name|InitPlugins
parameter_list|(
specifier|final
name|ConsoleUI
name|ui
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
name|InitFlags
name|initFlags
parameter_list|,
name|InitPluginStepsLoader
name|pluginLoader
parameter_list|,
name|PluginsDistribution
name|pluginsDistribution
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
name|this
operator|.
name|initFlags
operator|=
name|initFlags
expr_stmt|;
name|this
operator|.
name|pluginLoader
operator|=
name|pluginLoader
expr_stmt|;
name|this
operator|.
name|pluginsDistribution
operator|=
name|pluginsDistribution
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Plugins"
argument_list|)
expr_stmt|;
name|installPlugins
argument_list|()
expr_stmt|;
name|initPlugins
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postRun ()
specifier|public
name|void
name|postRun
parameter_list|()
throws|throws
name|Exception
block|{
name|postInitPlugins
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Inject
argument_list|(
name|optional
operator|=
literal|true
argument_list|)
DECL|method|setPostRunInjector (Injector injector)
name|void
name|setPostRunInjector
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|postRunInjector
operator|=
name|injector
expr_stmt|;
block|}
DECL|method|installPlugins ()
specifier|private
name|void
name|installPlugins
parameter_list|()
throws|throws
name|IOException
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Installing plugins.\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PluginData
argument_list|>
name|plugins
init|=
name|listPlugins
argument_list|(
name|site
argument_list|,
name|pluginsDistribution
argument_list|)
decl_stmt|;
for|for
control|(
name|PluginData
name|plugin
range|:
name|plugins
control|)
block|{
name|String
name|pluginName
init|=
name|plugin
operator|.
name|name
decl_stmt|;
try|try
block|{
specifier|final
name|Path
name|tmpPlugin
init|=
name|plugin
operator|.
name|pluginPath
decl_stmt|;
name|Path
name|p
init|=
name|site
operator|.
name|plugins_dir
operator|.
name|resolve
argument_list|(
name|plugin
operator|.
name|name
operator|+
literal|".jar"
argument_list|)
decl_stmt|;
name|boolean
name|upgrade
init|=
name|Files
operator|.
name|exists
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|initFlags
operator|.
name|installPlugins
operator|.
name|contains
argument_list|(
name|pluginName
argument_list|)
operator|||
name|initFlags
operator|.
name|installAllPlugins
operator|||
name|ui
operator|.
name|yesno
argument_list|(
name|upgrade
argument_list|,
literal|"Install plugin %s version %s"
argument_list|,
name|pluginName
argument_list|,
name|plugin
operator|.
name|version
argument_list|)
operator|)
condition|)
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|tmpPlugin
argument_list|)
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|upgrade
condition|)
block|{
specifier|final
name|String
name|installedPluginVersion
init|=
name|getVersion
argument_list|(
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|ui
operator|.
name|yesno
argument_list|(
name|upgrade
argument_list|,
literal|"%s %s is already installed, overwrite it"
argument_list|,
name|plugin
operator|.
name|name
argument_list|,
name|installedPluginVersion
argument_list|)
condition|)
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|tmpPlugin
argument_list|)
expr_stmt|;
continue|continue;
block|}
try|try
block|{
name|Files
operator|.
name|delete
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to delete plugin "
operator|+
name|pluginName
operator|+
literal|": "
operator|+
name|p
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
try|try
block|{
name|Files
operator|.
name|move
argument_list|(
name|tmpPlugin
argument_list|,
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
name|upgrade
condition|)
block|{
comment|// or update that is not an upgrade
name|ui
operator|.
name|message
argument_list|(
literal|"Updated %s to %s\n"
argument_list|,
name|plugin
operator|.
name|name
argument_list|,
name|plugin
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Installed %s %s\n"
argument_list|,
name|plugin
operator|.
name|name
argument_list|,
name|plugin
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to install plugin "
operator|+
name|pluginName
operator|+
literal|": "
operator|+
name|tmpPlugin
operator|.
name|toAbsolutePath
argument_list|()
operator|+
literal|" -> "
operator|+
name|p
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|plugin
operator|.
name|pluginPath
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|plugins
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"No plugins found to install.\n"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initPlugins ()
specifier|private
name|void
name|initPlugins
parameter_list|()
throws|throws
name|Exception
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Initializing plugins.\n"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|InitStep
argument_list|>
name|initSteps
init|=
name|pluginLoader
operator|.
name|getInitSteps
argument_list|()
decl_stmt|;
if|if
condition|(
name|initSteps
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"No plugins found with init steps.\n"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|InitStep
name|initStep
range|:
name|initSteps
control|)
block|{
name|initStep
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|postInitPlugins ()
specifier|private
name|void
name|postInitPlugins
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|InitStep
name|initStep
range|:
name|pluginLoader
operator|.
name|getInitSteps
argument_list|()
control|)
block|{
name|postRunInjector
operator|.
name|injectMembers
argument_list|(
name|initStep
argument_list|)
expr_stmt|;
name|initStep
operator|.
name|postRun
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getVersion (Path plugin)
specifier|private
specifier|static
name|String
name|getVersion
parameter_list|(
name|Path
name|plugin
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|JarFile
name|jarFile
init|=
operator|new
name|JarFile
argument_list|(
name|plugin
operator|.
name|toFile
argument_list|()
argument_list|)
init|)
block|{
name|Manifest
name|manifest
init|=
name|jarFile
operator|.
name|getManifest
argument_list|()
decl_stmt|;
name|Attributes
name|main
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
return|return
name|main
operator|.
name|getValue
argument_list|(
name|Attributes
operator|.
name|Name
operator|.
name|IMPLEMENTATION_VERSION
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
