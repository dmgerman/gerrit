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
DECL|package|com.google.gerrit.plugin.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|plugin
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|EntryPoint
import|;
end_import

begin_comment
comment|/**  * Base class for writing Gerrit Web UI plugins  *  * Writing a plugin:  *<ol>  *<li>Declare subtype of Plugin</li>  *<li>Bind WebUiPlugin to GwtPlugin implementation in Gerrit-Module</li>  *</ol>  */
end_comment

begin_class
DECL|class|PluginEntryPoint
specifier|public
specifier|abstract
class|class
name|PluginEntryPoint
implements|implements
name|EntryPoint
block|{
comment|/**    * The plugin entry point method, called automatically by loading    * a module that declares an implementing class as an entry point.    */
DECL|method|onPluginLoad ()
specifier|public
specifier|abstract
name|void
name|onPluginLoad
parameter_list|()
function_decl|;
DECL|method|onModuleLoad ()
specifier|public
specifier|final
name|void
name|onModuleLoad
parameter_list|()
block|{
name|Plugin
name|self
init|=
name|Plugin
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|onPluginLoad
argument_list|()
expr_stmt|;
name|self
operator|.
name|_initialized
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|self
operator|.
name|_loaded
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

