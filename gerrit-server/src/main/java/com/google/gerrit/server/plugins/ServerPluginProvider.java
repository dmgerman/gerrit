begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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

begin_comment
comment|/**  * Provider of one Server plugin from one external file  *  *<p>Allows to load one plugin from one external file or one directory by declaring the ability to  * handle it.  *  *<p>In order to load multiple files into a single plugin, group them into a directory tree and  * then load the directory root as a single plugin.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|ServerPluginProvider
specifier|public
interface|interface
name|ServerPluginProvider
block|{
comment|/** Descriptor of the Plugin that ServerPluginProvider has to load. */
DECL|class|PluginDescription
class|class
name|PluginDescription
block|{
DECL|field|user
specifier|public
specifier|final
name|PluginUser
name|user
decl_stmt|;
DECL|field|canonicalUrl
specifier|public
specifier|final
name|String
name|canonicalUrl
decl_stmt|;
DECL|field|dataDir
specifier|public
specifier|final
name|Path
name|dataDir
decl_stmt|;
comment|/**      * Creates a new PluginDescription for ServerPluginProvider.      *      * @param user Gerrit user for interacting with plugins      * @param canonicalUrl plugin root Web URL      * @param dataDir directory for plugin data      */
DECL|method|PluginDescription (PluginUser user, String canonicalUrl, Path dataDir)
specifier|public
name|PluginDescription
parameter_list|(
name|PluginUser
name|user
parameter_list|,
name|String
name|canonicalUrl
parameter_list|,
name|Path
name|dataDir
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|canonicalUrl
operator|=
name|canonicalUrl
expr_stmt|;
name|this
operator|.
name|dataDir
operator|=
name|dataDir
expr_stmt|;
block|}
block|}
comment|/**    * Declares the availability to manage an external file or directory    *    * @param srcPath the external file or directory    * @return true if file or directory can be loaded into a Server Plugin    */
DECL|method|handles (Path srcPath)
name|boolean
name|handles
parameter_list|(
name|Path
name|srcPath
parameter_list|)
function_decl|;
comment|/**    * Returns the plugin name of an external file or directory    *    *<p>Should be called only if {@link #handles(Path) handles(srcFile)} returns true and thus    * srcFile is a supported plugin format. An IllegalArgumentException is thrown otherwise as    * srcFile is not a valid file format for extracting its plugin name.    *    * @param srcPath external file or directory    * @return plugin name    */
DECL|method|getPluginName (Path srcPath)
name|String
name|getPluginName
parameter_list|(
name|Path
name|srcPath
parameter_list|)
function_decl|;
comment|/**    * Loads an external file or directory into a Server plugin.    *    *<p>Should be called only if {@link #handles(Path) handles(srcFile)} returns true and thus    * srcFile is a supported plugin format. An IllegalArgumentException is thrown otherwise as    * srcFile is not a valid file format for extracting its plugin name.    *    * @param srcPath external file or directory    * @param snapshot snapshot of the external file    * @param pluginDescriptor descriptor of the ServerPlugin to load    * @throws InvalidPluginException if plugin is supposed to be handled but cannot be loaded for any    *     other reason    */
DECL|method|get (Path srcPath, FileSnapshot snapshot, PluginDescription pluginDescriptor)
name|ServerPlugin
name|get
parameter_list|(
name|Path
name|srcPath
parameter_list|,
name|FileSnapshot
name|snapshot
parameter_list|,
name|PluginDescription
name|pluginDescriptor
parameter_list|)
throws|throws
name|InvalidPluginException
function_decl|;
comment|/**    * Returns the plugin name of this provider.    *    *<p>Allows to identify which plugin provided the current ServerPluginProvider by returning the    * plugin name. Helpful for troubleshooting plugin loading problems.    *    * @return plugin name of this provider    */
DECL|method|getProviderPluginName ()
name|String
name|getProviderPluginName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

