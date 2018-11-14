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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
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
name|extensions
operator|.
name|annotations
operator|.
name|PluginName
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

begin_comment
comment|/**  * Specifies JavaScript to dynamically load into the web UI.  *  *<p>To automatically register (instead of writing a Guice module), declare the intention with  * {@code @Listen}, extend the correct class and define a constructor to configure the correct  * resource:  *  *<pre>  *&#064;Listen  * class MyJs extends JavaScriptPlugin {  *   MyJs() {  *     super(&quot;hello.js&quot;);  *   }  * }  *</pre>  *  * @see JavaScriptPlugin  */
end_comment

begin_class
annotation|@
name|ExtensionPoint
DECL|class|WebUiPlugin
specifier|public
specifier|abstract
class|class
name|WebUiPlugin
block|{
DECL|method|js (String scriptName)
specifier|public
specifier|static
specifier|final
name|JavaScriptPlugin
name|js
parameter_list|(
name|String
name|scriptName
parameter_list|)
block|{
return|return
operator|new
name|JavaScriptPlugin
argument_list|(
name|scriptName
argument_list|)
return|;
block|}
DECL|field|pluginName
specifier|private
name|String
name|pluginName
decl_stmt|;
comment|/** @return installed name of the plugin that provides this UI feature. */
DECL|method|getPluginName ()
specifier|public
specifier|final
name|String
name|getPluginName
parameter_list|()
block|{
return|return
name|pluginName
return|;
block|}
annotation|@
name|Inject
DECL|method|setPluginName (@luginName String pluginName)
name|void
name|setPluginName
parameter_list|(
annotation|@
name|PluginName
name|String
name|pluginName
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
block|}
comment|/** @return path to initialization script within the plugin's JAR. */
DECL|method|getJavaScriptResourcePath ()
specifier|public
specifier|abstract
name|String
name|getJavaScriptResourcePath
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getJavaScriptResourcePath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

