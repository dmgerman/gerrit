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
name|common
operator|.
name|base
operator|.
name|Strings
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
name|Lists
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
name|Nullable
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
name|registration
operator|.
name|RegistrationHandle
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
name|registration
operator|.
name|ReloadableRegistrationHandle
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|util
operator|.
name|RequestContext
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
name|Module
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|FileSnapshot
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
name|Manifest
import|;
end_import

begin_class
DECL|class|ServerPlugin
specifier|public
class|class
name|ServerPlugin
extends|extends
name|Plugin
block|{
comment|/** Unique key that changes whenever a plugin reloads. */
DECL|class|CacheKey
specifier|public
specifier|static
specifier|final
class|class
name|CacheKey
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|CacheKey (String name)
name|CacheKey
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|int
name|id
init|=
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Plugin[%s@%x]"
argument_list|,
name|name
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
DECL|field|manifest
specifier|private
specifier|final
name|Manifest
name|manifest
decl_stmt|;
DECL|field|scanner
specifier|private
specifier|final
name|PluginContentScanner
name|scanner
decl_stmt|;
DECL|field|dataDir
specifier|private
specifier|final
name|Path
name|dataDir
decl_stmt|;
DECL|field|pluginCanonicalWebUrl
specifier|private
specifier|final
name|String
name|pluginCanonicalWebUrl
decl_stmt|;
DECL|field|classLoader
specifier|private
specifier|final
name|ClassLoader
name|classLoader
decl_stmt|;
DECL|field|sysModule
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|sysModule
decl_stmt|;
DECL|field|sshModule
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|sshModule
decl_stmt|;
DECL|field|httpModule
specifier|private
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|httpModule
decl_stmt|;
DECL|field|sysInjector
specifier|private
name|Injector
name|sysInjector
decl_stmt|;
DECL|field|sshInjector
specifier|private
name|Injector
name|sshInjector
decl_stmt|;
DECL|field|httpInjector
specifier|private
name|Injector
name|httpInjector
decl_stmt|;
DECL|field|serverManager
specifier|private
name|LifecycleManager
name|serverManager
decl_stmt|;
DECL|field|reloadableHandles
specifier|private
name|List
argument_list|<
name|ReloadableRegistrationHandle
argument_list|<
name|?
argument_list|>
argument_list|>
name|reloadableHandles
decl_stmt|;
DECL|method|ServerPlugin (String name, String pluginCanonicalWebUrl, PluginUser pluginUser, Path srcJar, FileSnapshot snapshot, PluginContentScanner scanner, Path dataDir, ClassLoader classLoader)
specifier|public
name|ServerPlugin
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|pluginCanonicalWebUrl
parameter_list|,
name|PluginUser
name|pluginUser
parameter_list|,
name|Path
name|srcJar
parameter_list|,
name|FileSnapshot
name|snapshot
parameter_list|,
name|PluginContentScanner
name|scanner
parameter_list|,
name|Path
name|dataDir
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|super
argument_list|(
name|name
argument_list|,
name|srcJar
argument_list|,
name|pluginUser
argument_list|,
name|snapshot
argument_list|,
name|Plugin
operator|.
name|getApiType
argument_list|(
name|getPluginManifest
argument_list|(
name|scanner
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|pluginCanonicalWebUrl
operator|=
name|pluginCanonicalWebUrl
expr_stmt|;
name|this
operator|.
name|scanner
operator|=
name|scanner
expr_stmt|;
name|this
operator|.
name|dataDir
operator|=
name|dataDir
expr_stmt|;
name|this
operator|.
name|classLoader
operator|=
name|classLoader
expr_stmt|;
name|this
operator|.
name|manifest
operator|=
name|getPluginManifest
argument_list|(
name|scanner
argument_list|)
expr_stmt|;
name|loadGuiceModules
argument_list|(
name|manifest
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
DECL|method|loadGuiceModules (Manifest manifest, ClassLoader classLoader)
specifier|private
name|void
name|loadGuiceModules
parameter_list|(
name|Manifest
name|manifest
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
name|Attributes
name|main
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
name|String
name|sysName
init|=
name|main
operator|.
name|getValue
argument_list|(
literal|"Gerrit-Module"
argument_list|)
decl_stmt|;
name|String
name|sshName
init|=
name|main
operator|.
name|getValue
argument_list|(
literal|"Gerrit-SshModule"
argument_list|)
decl_stmt|;
name|String
name|httpName
init|=
name|main
operator|.
name|getValue
argument_list|(
literal|"Gerrit-HttpModule"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|sshName
argument_list|)
operator|&&
name|getApiType
argument_list|()
operator|!=
name|Plugin
operator|.
name|ApiType
operator|.
name|PLUGIN
condition|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Using Gerrit-SshModule requires Gerrit-ApiType: %s"
argument_list|,
name|Plugin
operator|.
name|ApiType
operator|.
name|PLUGIN
argument_list|)
argument_list|)
throw|;
block|}
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
name|sshModule
operator|=
name|load
argument_list|(
name|sshName
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|load (String name, ClassLoader pluginLoader)
specifier|private
specifier|static
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
name|load
parameter_list|(
name|String
name|name
parameter_list|,
name|ClassLoader
name|pluginLoader
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|false
argument_list|,
name|pluginLoader
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Module
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Class %s does not implement %s"
argument_list|,
name|name
argument_list|,
name|Module
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
operator|(
name|Class
argument_list|<
name|?
extends|extends
name|Module
argument_list|>
operator|)
name|clazz
return|;
block|}
DECL|method|getSrcJar ()
name|Path
name|getSrcJar
parameter_list|()
block|{
return|return
name|getSrcFile
argument_list|()
return|;
block|}
DECL|method|getDataDir ()
name|Path
name|getDataDir
parameter_list|()
block|{
return|return
name|dataDir
return|;
block|}
DECL|method|getPluginCanonicalWebUrl ()
name|String
name|getPluginCanonicalWebUrl
parameter_list|()
block|{
return|return
name|pluginCanonicalWebUrl
return|;
block|}
DECL|method|getPluginManifest (PluginContentScanner scanner)
specifier|private
specifier|static
name|Manifest
name|getPluginManifest
parameter_list|(
name|PluginContentScanner
name|scanner
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
try|try
block|{
return|return
name|scanner
operator|.
name|getManifest
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
literal|"Cannot get plugin manifest"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getVersion ()
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
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
annotation|@
name|Override
DECL|method|canReload ()
specifier|protected
name|boolean
name|canReload
parameter_list|()
block|{
name|Attributes
name|main
init|=
name|manifest
operator|.
name|getMainAttributes
argument_list|()
decl_stmt|;
name|String
name|v
init|=
name|main
operator|.
name|getValue
argument_list|(
literal|"Gerrit-ReloadMode"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|v
argument_list|)
operator|||
literal|"reload"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
literal|"restart"
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
else|else
block|{
name|PluginLoader
operator|.
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Plugin %s has invalid Gerrit-ReloadMode %s; assuming restart"
argument_list|,
name|getName
argument_list|()
argument_list|,
name|v
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|start (PluginGuiceEnvironment env)
specifier|protected
name|void
name|start
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
throws|throws
name|Exception
block|{
name|RequestContext
name|oldContext
init|=
name|env
operator|.
name|enter
argument_list|(
name|this
argument_list|)
decl_stmt|;
try|try
block|{
name|startPlugin
argument_list|(
name|env
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|env
operator|.
name|exit
argument_list|(
name|oldContext
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|startPlugin (PluginGuiceEnvironment env)
specifier|private
name|void
name|startPlugin
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
throws|throws
name|Exception
block|{
name|Injector
name|root
init|=
name|newRootInjector
argument_list|(
name|env
argument_list|)
decl_stmt|;
name|serverManager
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
name|AutoRegisterModules
name|auto
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|sysModule
operator|==
literal|null
operator|&&
name|sshModule
operator|==
literal|null
operator|&&
name|httpModule
operator|==
literal|null
condition|)
block|{
name|auto
operator|=
operator|new
name|AutoRegisterModules
argument_list|(
name|getName
argument_list|()
argument_list|,
name|env
argument_list|,
name|scanner
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
name|auto
operator|.
name|discover
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sysModule
operator|!=
literal|null
condition|)
block|{
name|sysInjector
operator|=
name|root
operator|.
name|createChildInjector
argument_list|(
name|root
operator|.
name|getInstance
argument_list|(
name|sysModule
argument_list|)
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|sysInjector
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|auto
operator|!=
literal|null
operator|&&
name|auto
operator|.
name|sysModule
operator|!=
literal|null
condition|)
block|{
name|sysInjector
operator|=
name|root
operator|.
name|createChildInjector
argument_list|(
name|auto
operator|.
name|sysModule
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|sysInjector
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sysInjector
operator|=
name|root
expr_stmt|;
block|}
if|if
condition|(
name|env
operator|.
name|hasSshModule
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|getApiType
argument_list|()
operator|==
name|ApiType
operator|.
name|PLUGIN
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|env
operator|.
name|getSshModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sshModule
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|sshModule
argument_list|)
argument_list|)
expr_stmt|;
name|sshInjector
operator|=
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|sshInjector
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|auto
operator|!=
literal|null
operator|&&
name|auto
operator|.
name|sshModule
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|auto
operator|.
name|sshModule
argument_list|)
expr_stmt|;
name|sshInjector
operator|=
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|sshInjector
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|env
operator|.
name|hasHttpModule
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
if|if
condition|(
name|getApiType
argument_list|()
operator|==
name|ApiType
operator|.
name|PLUGIN
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|env
operator|.
name|getHttpModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpModule
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|httpModule
argument_list|)
argument_list|)
expr_stmt|;
name|httpInjector
operator|=
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|httpInjector
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|auto
operator|!=
literal|null
operator|&&
name|auto
operator|.
name|httpModule
operator|!=
literal|null
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|auto
operator|.
name|httpModule
argument_list|)
expr_stmt|;
name|httpInjector
operator|=
name|sysInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
expr_stmt|;
name|serverManager
operator|.
name|add
argument_list|(
name|httpInjector
argument_list|)
expr_stmt|;
block|}
block|}
name|serverManager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|newRootInjector (final PluginGuiceEnvironment env)
specifier|private
name|Injector
name|newRootInjector
parameter_list|(
specifier|final
name|PluginGuiceEnvironment
name|env
parameter_list|)
block|{
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|getApiType
argument_list|()
operator|==
name|ApiType
operator|.
name|PLUGIN
condition|)
block|{
name|modules
operator|.
name|add
argument_list|(
name|env
operator|.
name|getSysModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ServerPluginInfoModule
argument_list|(
name|this
argument_list|,
name|env
operator|.
name|getServerMetrics
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Guice
operator|.
name|createInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|stop (PluginGuiceEnvironment env)
specifier|protected
name|void
name|stop
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
block|{
if|if
condition|(
name|serverManager
operator|!=
literal|null
condition|)
block|{
name|RequestContext
name|oldContext
init|=
name|env
operator|.
name|enter
argument_list|(
name|this
argument_list|)
decl_stmt|;
try|try
block|{
name|serverManager
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|env
operator|.
name|exit
argument_list|(
name|oldContext
argument_list|)
expr_stmt|;
block|}
name|serverManager
operator|=
literal|null
expr_stmt|;
name|sysInjector
operator|=
literal|null
expr_stmt|;
name|sshInjector
operator|=
literal|null
expr_stmt|;
name|httpInjector
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSysInjector ()
specifier|public
name|Injector
name|getSysInjector
parameter_list|()
block|{
return|return
name|sysInjector
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getSshInjector ()
specifier|public
name|Injector
name|getSshInjector
parameter_list|()
block|{
return|return
name|sshInjector
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getHttpInjector ()
specifier|public
name|Injector
name|getHttpInjector
parameter_list|()
block|{
return|return
name|httpInjector
return|;
block|}
annotation|@
name|Override
DECL|method|add (RegistrationHandle handle)
specifier|public
name|void
name|add
parameter_list|(
name|RegistrationHandle
name|handle
parameter_list|)
block|{
if|if
condition|(
name|serverManager
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|handle
operator|instanceof
name|ReloadableRegistrationHandle
condition|)
block|{
if|if
condition|(
name|reloadableHandles
operator|==
literal|null
condition|)
block|{
name|reloadableHandles
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
block|}
name|reloadableHandles
operator|.
name|add
argument_list|(
operator|(
name|ReloadableRegistrationHandle
argument_list|<
name|?
argument_list|>
operator|)
name|handle
argument_list|)
expr_stmt|;
block|}
name|serverManager
operator|.
name|add
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
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
name|scanner
return|;
block|}
block|}
end_class

end_unit

