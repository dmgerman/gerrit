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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
import|;
end_import

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
name|ImmutableList
import|;
end_import

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
name|Sets
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
name|IoUtil
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
name|PageLinks
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
name|index
operator|.
name|project
operator|.
name|ProjectSchemaDefinitions
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
name|BaseInit
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
name|Browser
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
name|InitPlugins
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
name|util
operator|.
name|ErrorLogFile
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
name|GerritServerConfigModule
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
name|SitePath
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
name|ioutil
operator|.
name|HostPlatform
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
name|securestore
operator|.
name|SecureStoreClassName
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
name|AbstractModule
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
name|Guice
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
name|Module
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
name|util
operator|.
name|Providers
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
name|Collections
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
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_comment
comment|/** Initialize a new Gerrit installation. */
end_comment

begin_class
DECL|class|Init
specifier|public
class|class
name|Init
extends|extends
name|BaseInit
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--batch"
argument_list|,
name|aliases
operator|=
block|{
literal|"-b"
block|}
argument_list|,
name|usage
operator|=
literal|"Batch mode; skip interactive prompting"
argument_list|)
DECL|field|batchMode
specifier|private
name|boolean
name|batchMode
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--delete-caches"
argument_list|,
name|usage
operator|=
literal|"Delete all persistent caches without asking"
argument_list|)
DECL|field|deleteCaches
specifier|private
name|boolean
name|deleteCaches
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-auto-start"
argument_list|,
name|usage
operator|=
literal|"Don't automatically start daemon after init"
argument_list|)
DECL|field|noAutoStart
specifier|private
name|boolean
name|noAutoStart
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--no-reindex"
argument_list|,
name|usage
operator|=
literal|"Don't automatically reindex any entities"
argument_list|)
DECL|field|noReindex
specifier|private
name|boolean
name|noReindex
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--skip-plugins"
argument_list|,
name|usage
operator|=
literal|"Don't install plugins"
argument_list|)
DECL|field|skipPlugins
specifier|private
name|boolean
name|skipPlugins
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--list-plugins"
argument_list|,
name|usage
operator|=
literal|"List available plugins"
argument_list|)
DECL|field|listPlugins
specifier|private
name|boolean
name|listPlugins
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--install-plugin"
argument_list|,
name|usage
operator|=
literal|"Install given plugin without asking"
argument_list|)
DECL|field|installPlugins
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|installPlugins
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--install-all-plugins"
argument_list|,
name|usage
operator|=
literal|"Install all plugins from war without asking"
argument_list|)
DECL|field|installAllPlugins
specifier|private
name|boolean
name|installAllPlugins
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--secure-store-lib"
argument_list|,
name|usage
operator|=
literal|"Path to jar providing SecureStore implementation class"
argument_list|)
DECL|field|secureStoreLib
specifier|private
name|String
name|secureStoreLib
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--dev"
argument_list|,
name|usage
operator|=
literal|"Setup site with default options suitable for developers"
argument_list|)
DECL|field|dev
specifier|private
name|boolean
name|dev
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--skip-all-downloads"
argument_list|,
name|usage
operator|=
literal|"Don't download libraries"
argument_list|)
DECL|field|skipAllDownloads
specifier|private
name|boolean
name|skipAllDownloads
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--skip-download"
argument_list|,
name|usage
operator|=
literal|"Don't download given library"
argument_list|)
DECL|field|skippedDownloads
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|skippedDownloads
decl_stmt|;
DECL|field|browser
annotation|@
name|Inject
name|Browser
name|browser
decl_stmt|;
DECL|method|Init ()
specifier|public
name|Init
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|WarDistribution
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|Init (Path sitePath)
specifier|public
name|Init
parameter_list|(
name|Path
name|sitePath
parameter_list|)
block|{
name|super
argument_list|(
name|sitePath
argument_list|,
literal|true
argument_list|,
operator|new
name|WarDistribution
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|batchMode
operator|=
literal|true
expr_stmt|;
name|noAutoStart
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeInit (SiteInit init)
specifier|protected
name|boolean
name|beforeInit
parameter_list|(
name|SiteInit
name|init
parameter_list|)
throws|throws
name|Exception
block|{
name|ErrorLogFile
operator|.
name|errorOnlyConsole
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|skipPlugins
condition|)
block|{
specifier|final
name|List
argument_list|<
name|PluginData
argument_list|>
name|plugins
init|=
name|InitPlugins
operator|.
name|listPluginsAndRemoveTempFiles
argument_list|(
name|init
operator|.
name|site
argument_list|,
name|pluginsDistribution
argument_list|)
decl_stmt|;
name|ConsoleUI
name|ui
init|=
name|ConsoleUI
operator|.
name|getInstance
argument_list|(
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|installAllPlugins
operator|&&
operator|!
name|nullOrEmpty
argument_list|(
name|installPlugins
argument_list|)
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Cannot use --install-plugin together with --install-all-plugins.\n"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|verifyInstallPluginList
argument_list|(
name|ui
argument_list|,
name|plugins
argument_list|)
expr_stmt|;
if|if
condition|(
name|listPlugins
condition|)
block|{
if|if
condition|(
operator|!
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
literal|"Available plugins:\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|PluginData
name|plugin
range|:
name|plugins
control|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|" * %s version %s\n"
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
else|else
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"No plugins found.\n"
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|afterInit (SiteRun run)
specifier|protected
name|void
name|afterInit
parameter_list|(
name|SiteRun
name|run
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|Path
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SitePath
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|getSitePath
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Browser
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|SecureStoreClassName
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
name|Providers
operator|.
name|of
argument_list|(
name|getConfiguredSecureStoreClass
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|GerritServerConfigModule
argument_list|()
argument_list|)
expr_stmt|;
name|Guice
operator|.
name|createInjector
argument_list|(
name|modules
argument_list|)
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|run
operator|.
name|flags
operator|.
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"container"
argument_list|,
literal|"slave"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|reindexProjects
argument_list|()
expr_stmt|;
block|}
name|start
argument_list|(
name|run
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getInstallPlugins ()
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getInstallPlugins
parameter_list|()
block|{
return|return
name|installPlugins
return|;
block|}
annotation|@
name|Override
DECL|method|installAllPlugins ()
specifier|protected
name|boolean
name|installAllPlugins
parameter_list|()
block|{
return|return
name|installAllPlugins
return|;
block|}
annotation|@
name|Override
DECL|method|getConsoleUI ()
specifier|protected
name|ConsoleUI
name|getConsoleUI
parameter_list|()
block|{
return|return
name|ConsoleUI
operator|.
name|getInstance
argument_list|(
name|batchMode
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getAutoStart ()
specifier|protected
name|boolean
name|getAutoStart
parameter_list|()
block|{
return|return
operator|!
name|noAutoStart
return|;
block|}
annotation|@
name|Override
DECL|method|getDeleteCaches ()
specifier|protected
name|boolean
name|getDeleteCaches
parameter_list|()
block|{
return|return
name|deleteCaches
return|;
block|}
annotation|@
name|Override
DECL|method|skipPlugins ()
specifier|protected
name|boolean
name|skipPlugins
parameter_list|()
block|{
return|return
name|skipPlugins
return|;
block|}
annotation|@
name|Override
DECL|method|isDev ()
specifier|protected
name|boolean
name|isDev
parameter_list|()
block|{
return|return
name|dev
return|;
block|}
annotation|@
name|Override
DECL|method|skipAllDownloads ()
specifier|protected
name|boolean
name|skipAllDownloads
parameter_list|()
block|{
return|return
name|skipAllDownloads
return|;
block|}
annotation|@
name|Override
DECL|method|getSkippedDownloads ()
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getSkippedDownloads
parameter_list|()
block|{
return|return
name|skippedDownloads
operator|!=
literal|null
condition|?
name|skippedDownloads
else|:
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSecureStoreLib ()
specifier|protected
name|String
name|getSecureStoreLib
parameter_list|()
block|{
return|return
name|secureStoreLib
return|;
block|}
DECL|method|start (SiteRun run)
name|void
name|start
parameter_list|(
name|SiteRun
name|run
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|run
operator|.
name|flags
operator|.
name|autoStart
condition|)
block|{
if|if
condition|(
name|HostPlatform
operator|.
name|isWin32
argument_list|()
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Automatic startup not supported on Win32."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|startDaemon
argument_list|(
name|run
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|run
operator|.
name|ui
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|browser
operator|.
name|open
argument_list|(
name|PageLinks
operator|.
name|ADMIN_PROJECTS
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|startDaemon (SiteRun run)
name|void
name|startDaemon
parameter_list|(
name|SiteRun
name|run
parameter_list|)
block|{
name|String
index|[]
name|argv
init|=
block|{
name|run
operator|.
name|site
operator|.
name|gerrit_sh
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
block|,
literal|"start"
block|}
decl_stmt|;
name|Process
name|proc
decl_stmt|;
try|try
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Executing "
operator|+
name|argv
index|[
literal|0
index|]
operator|+
literal|" "
operator|+
name|argv
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|proc
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|argv
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: cannot start Gerrit: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
name|proc
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Ignored
block|}
name|IoUtil
operator|.
name|copyWithThread
argument_list|(
name|proc
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|IoUtil
operator|.
name|copyWithThread
argument_list|(
name|proc
operator|.
name|getErrorStream
argument_list|()
argument_list|,
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
try|try
block|{
name|int
name|rc
init|=
name|proc
operator|.
name|waitFor
argument_list|()
decl_stmt|;
if|if
condition|(
name|rc
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"error: cannot start Gerrit: exit status "
operator|+
name|rc
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// retry
block|}
block|}
block|}
DECL|method|verifyInstallPluginList (ConsoleUI ui, List<PluginData> plugins)
specifier|private
name|void
name|verifyInstallPluginList
parameter_list|(
name|ConsoleUI
name|ui
parameter_list|,
name|List
argument_list|<
name|PluginData
argument_list|>
name|plugins
parameter_list|)
block|{
if|if
condition|(
name|nullOrEmpty
argument_list|(
name|installPlugins
argument_list|)
condition|)
block|{
return|return;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|missing
init|=
name|Sets
operator|.
name|newHashSet
argument_list|(
name|installPlugins
argument_list|)
decl_stmt|;
name|plugins
operator|.
name|stream
argument_list|()
operator|.
name|forEach
argument_list|(
name|p
lambda|->
name|missing
operator|.
name|remove
argument_list|(
name|p
operator|.
name|name
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ui
operator|.
name|message
argument_list|(
literal|"Cannot find plugin(s): %s\n"
argument_list|,
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|missing
argument_list|)
argument_list|)
expr_stmt|;
name|listPlugins
operator|=
literal|true
expr_stmt|;
block|}
block|}
DECL|method|reindexProjects ()
specifier|private
name|void
name|reindexProjects
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|noReindex
condition|)
block|{
return|return;
block|}
comment|// Reindex all projects, so that we bootstrap the project index for new installations
name|List
argument_list|<
name|String
argument_list|>
name|reindexArgs
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"--site-path"
argument_list|,
name|getSitePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"--threads"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|"--index"
argument_list|,
name|ProjectSchemaDefinitions
operator|.
name|NAME
argument_list|)
decl_stmt|;
name|getConsoleUI
argument_list|()
operator|.
name|message
argument_list|(
literal|"Init complete, reindexing projects with:"
argument_list|)
expr_stmt|;
name|getConsoleUI
argument_list|()
operator|.
name|message
argument_list|(
literal|" reindex "
operator|+
name|reindexArgs
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|" "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Reindex
name|reindexPgm
init|=
operator|new
name|Reindex
argument_list|()
decl_stmt|;
name|reindexPgm
operator|.
name|main
argument_list|(
name|reindexArgs
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|nullOrEmpty (List<?> list)
specifier|private
specifier|static
name|boolean
name|nullOrEmpty
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

