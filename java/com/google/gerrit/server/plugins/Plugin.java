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
name|inject
operator|.
name|Injector
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

begin_class
DECL|class|Plugin
specifier|public
specifier|abstract
class|class
name|Plugin
block|{
DECL|enum|ApiType
specifier|public
enum|enum
name|ApiType
block|{
DECL|enumConstant|EXTENSION
name|EXTENSION
block|,
DECL|enumConstant|PLUGIN
name|PLUGIN
block|,
DECL|enumConstant|JS
name|JS
block|}
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
DECL|method|getApiType (Manifest manifest)
specifier|static
name|ApiType
name|getApiType
parameter_list|(
name|Manifest
name|manifest
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
name|v
init|=
name|main
operator|.
name|getValue
argument_list|(
literal|"Gerrit-ApiType"
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
name|ApiType
operator|.
name|EXTENSION
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|ApiType
operator|.
name|EXTENSION
return|;
block|}
elseif|else
if|if
condition|(
name|ApiType
operator|.
name|PLUGIN
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|ApiType
operator|.
name|PLUGIN
return|;
block|}
elseif|else
if|if
condition|(
name|ApiType
operator|.
name|JS
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|ApiType
operator|.
name|JS
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPluginException
argument_list|(
literal|"Invalid Gerrit-ApiType: "
operator|+
name|v
argument_list|)
throw|;
block|}
block|}
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|srcFile
specifier|private
specifier|final
name|Path
name|srcFile
decl_stmt|;
DECL|field|apiType
specifier|private
specifier|final
name|ApiType
name|apiType
decl_stmt|;
DECL|field|disabled
specifier|private
specifier|final
name|boolean
name|disabled
decl_stmt|;
DECL|field|cacheKey
specifier|private
specifier|final
name|CacheKey
name|cacheKey
decl_stmt|;
DECL|field|pluginUser
specifier|private
specifier|final
name|PluginUser
name|pluginUser
decl_stmt|;
DECL|field|snapshot
specifier|private
specifier|final
name|FileSnapshot
name|snapshot
decl_stmt|;
DECL|field|cleanupHandle
specifier|private
name|CleanupHandle
name|cleanupHandle
decl_stmt|;
DECL|field|manager
specifier|protected
name|LifecycleManager
name|manager
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
DECL|method|Plugin ( String name, Path srcPath, PluginUser pluginUser, FileSnapshot snapshot, ApiType apiType)
specifier|public
name|Plugin
parameter_list|(
name|String
name|name
parameter_list|,
name|Path
name|srcPath
parameter_list|,
name|PluginUser
name|pluginUser
parameter_list|,
name|FileSnapshot
name|snapshot
parameter_list|,
name|ApiType
name|apiType
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|srcFile
operator|=
name|srcPath
expr_stmt|;
name|this
operator|.
name|apiType
operator|=
name|apiType
expr_stmt|;
name|this
operator|.
name|snapshot
operator|=
name|snapshot
expr_stmt|;
name|this
operator|.
name|pluginUser
operator|=
name|pluginUser
expr_stmt|;
name|this
operator|.
name|cacheKey
operator|=
operator|new
name|Plugin
operator|.
name|CacheKey
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|this
operator|.
name|disabled
operator|=
name|srcPath
operator|!=
literal|null
operator|&&
name|srcPath
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|".disabled"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCleanupHandle ()
specifier|public
name|CleanupHandle
name|getCleanupHandle
parameter_list|()
block|{
return|return
name|cleanupHandle
return|;
block|}
DECL|method|setCleanupHandle (CleanupHandle cleanupHandle)
specifier|public
name|void
name|setCleanupHandle
parameter_list|(
name|CleanupHandle
name|cleanupHandle
parameter_list|)
block|{
name|this
operator|.
name|cleanupHandle
operator|=
name|cleanupHandle
expr_stmt|;
block|}
DECL|method|getPluginUser ()
name|PluginUser
name|getPluginUser
parameter_list|()
block|{
return|return
name|pluginUser
return|;
block|}
DECL|method|getSrcFile ()
specifier|public
name|Path
name|getSrcFile
parameter_list|()
block|{
return|return
name|srcFile
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|Nullable
DECL|method|getVersion ()
specifier|public
specifier|abstract
name|String
name|getVersion
parameter_list|()
function_decl|;
DECL|method|getApiType ()
specifier|public
name|ApiType
name|getApiType
parameter_list|()
block|{
return|return
name|apiType
return|;
block|}
DECL|method|getCacheKey ()
specifier|public
name|Plugin
operator|.
name|CacheKey
name|getCacheKey
parameter_list|()
block|{
return|return
name|cacheKey
return|;
block|}
DECL|method|isDisabled ()
specifier|public
name|boolean
name|isDisabled
parameter_list|()
block|{
return|return
name|disabled
return|;
block|}
DECL|method|start (PluginGuiceEnvironment env)
specifier|protected
specifier|abstract
name|void
name|start
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|stop (PluginGuiceEnvironment env)
specifier|protected
specifier|abstract
name|void
name|stop
parameter_list|(
name|PluginGuiceEnvironment
name|env
parameter_list|)
function_decl|;
DECL|method|getContentScanner ()
specifier|public
specifier|abstract
name|PluginContentScanner
name|getContentScanner
parameter_list|()
function_decl|;
DECL|method|getSysInjector ()
specifier|public
specifier|abstract
name|Injector
name|getSysInjector
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|getSshInjector ()
specifier|public
specifier|abstract
name|Injector
name|getSshInjector
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|getHttpInjector ()
specifier|public
specifier|abstract
name|Injector
name|getHttpInjector
parameter_list|()
function_decl|;
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
name|manager
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
operator|new
name|ArrayList
argument_list|<>
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
name|manager
operator|.
name|add
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getReloadableHandles ()
name|List
argument_list|<
name|ReloadableRegistrationHandle
argument_list|<
name|?
argument_list|>
argument_list|>
name|getReloadableHandles
parameter_list|()
block|{
if|if
condition|(
name|reloadableHandles
operator|!=
literal|null
condition|)
block|{
return|return
name|reloadableHandles
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Plugin ["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
DECL|method|canReload ()
specifier|protected
specifier|abstract
name|boolean
name|canReload
parameter_list|()
function_decl|;
DECL|method|isModified (Path jar)
name|boolean
name|isModified
parameter_list|(
name|Path
name|jar
parameter_list|)
block|{
return|return
name|snapshot
operator|.
name|isModified
argument_list|(
name|jar
operator|.
name|toFile
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

