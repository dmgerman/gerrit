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
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/** Represents the plugins packaged in the Gerrit distribution */
end_comment

begin_interface
DECL|interface|PluginsDistribution
specifier|public
interface|interface
name|PluginsDistribution
block|{
DECL|interface|Processor
specifier|public
interface|interface
name|Processor
block|{
comment|/**      * @param pluginName the name of the plugin (without the .jar extension)      * @param in the content of the plugin .jar file. Implementors don't have to close this stream.      * @throws IOException implementations will typically propagate any IOException caused by      *     dealing with the InputStream back to the caller      */
DECL|method|process (String pluginName, InputStream in)
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
function_decl|;
block|}
comment|/**    * Iterate over plugins package in the Gerrit distribution    *    * @param processor invoke for each plugin via its process method    * @throws FileNotFoundException if the location of the plugins couldn't be determined    * @throws IOException in case of any other IO error caused by reading the plugin input stream    */
DECL|method|foreach (Processor processor)
name|void
name|foreach
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|IOException
function_decl|;
comment|/**    * List plugins included in the Gerrit distribution    *    * @return list of plugins names included in the Gerrit distribution    * @throws FileNotFoundException if the location of the plugins couldn't be determined    */
DECL|method|listPluginNames ()
name|List
argument_list|<
name|String
argument_list|>
name|listPluginNames
parameter_list|()
throws|throws
name|FileNotFoundException
function_decl|;
block|}
end_interface

end_unit

