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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|NoSuchFileException
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
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_comment
comment|/**  * Scans the plugin returning classes and resources.  *  *<p>Gerrit uses the scanner to automatically discover the classes and resources exported by the  * plugin for auto discovery of exported SSH commands, Servlets and listeners.  */
end_comment

begin_interface
DECL|interface|PluginContentScanner
specifier|public
interface|interface
name|PluginContentScanner
block|{
comment|/** Scanner without resources. */
DECL|field|EMPTY
name|PluginContentScanner
name|EMPTY
init|=
operator|new
name|PluginContentScanner
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Manifest
name|getManifest
parameter_list|()
throws|throws
name|IOException
block|{
return|return
operator|new
name|Manifest
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|scan
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Iterable
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
throws|throws
name|InvalidPluginException
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Optional
argument_list|<
name|PluginEntry
argument_list|>
name|getEntry
parameter_list|(
name|String
name|resourcePath
parameter_list|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|(
name|PluginEntry
name|entry
parameter_list|)
throws|throws
name|IOException
block|{
throw|throw
operator|new
name|NoSuchFileException
argument_list|(
literal|"Empty plugin"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|PluginEntry
argument_list|>
name|entries
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyEnumeration
argument_list|()
return|;
block|}
block|}
decl_stmt|;
comment|/**    * Plugin class extension meta-data    *    *<p>Class name and annotation value of the class provided by a plugin to extend an existing    * extension point in Gerrit.    */
DECL|class|ExtensionMetaData
class|class
name|ExtensionMetaData
block|{
DECL|field|className
specifier|public
specifier|final
name|String
name|className
decl_stmt|;
DECL|field|annotationValue
specifier|public
specifier|final
name|String
name|annotationValue
decl_stmt|;
DECL|method|ExtensionMetaData (String className, String annotationValue)
specifier|public
name|ExtensionMetaData
parameter_list|(
name|String
name|className
parameter_list|,
name|String
name|annotationValue
parameter_list|)
block|{
name|this
operator|.
name|className
operator|=
name|className
expr_stmt|;
name|this
operator|.
name|annotationValue
operator|=
name|annotationValue
expr_stmt|;
block|}
block|}
comment|/**    * Return the plugin meta-data manifest    *    * @return Manifest of the plugin or null if plugin has no meta-data    * @throws IOException if an I/O problem occurred whilst accessing the Manifest    */
DECL|method|getManifest ()
name|Manifest
name|getManifest
parameter_list|()
throws|throws
name|IOException
function_decl|;
comment|/**    * Scans the plugin for declared public annotated classes    *    * @param pluginName the plugin name    * @param annotations annotations declared by the plugin classes    * @return map of annotations and associated plugin classes found    * @throws InvalidPluginException if the plugin is not valid or corrupted    */
DECL|method|scan ( String pluginName, Iterable<Class<? extends Annotation>> annotations)
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Iterable
argument_list|<
name|ExtensionMetaData
argument_list|>
argument_list|>
name|scan
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Iterable
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|>
name|annotations
parameter_list|)
throws|throws
name|InvalidPluginException
function_decl|;
comment|/**    * Return the plugin resource associated to a path    *    * @param resourcePath full path of the resource inside the plugin package    * @return the resource object or Optional.absent() if the resource was not found    * @throws IOException if there was a problem retrieving the resource    */
DECL|method|getEntry (String resourcePath)
name|Optional
argument_list|<
name|PluginEntry
argument_list|>
name|getEntry
parameter_list|(
name|String
name|resourcePath
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Return the InputStream of the resource entry    *    * @param entry resource entry inside the plugin package    * @return the resource input stream    * @throws IOException if there was an I/O problem accessing the resource    */
DECL|method|getInputStream (PluginEntry entry)
name|InputStream
name|getInputStream
parameter_list|(
name|PluginEntry
name|entry
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Return all the resources inside a plugin    *    * @return the enumeration of all resources found    */
DECL|method|entries ()
name|Enumeration
argument_list|<
name|PluginEntry
argument_list|>
name|entries
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

