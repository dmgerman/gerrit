begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|server
operator|.
name|account
operator|.
name|CapabilityControl
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_comment
comment|/** User identity for plugin code that needs an identity. */
end_comment

begin_class
DECL|class|PluginUser
specifier|public
class|class
name|PluginUser
extends|extends
name|InternalUser
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String pluginName)
name|PluginUser
name|create
parameter_list|(
name|String
name|pluginName
parameter_list|)
function_decl|;
block|}
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginUser ( CapabilityControl.Factory capabilityControlFactory, @Assisted String pluginName)
specifier|protected
name|PluginUser
parameter_list|(
name|CapabilityControl
operator|.
name|Factory
name|capabilityControlFactory
parameter_list|,
annotation|@
name|Assisted
name|String
name|pluginName
parameter_list|)
block|{
name|super
argument_list|(
name|capabilityControlFactory
argument_list|)
expr_stmt|;
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
literal|"plugin "
operator|+
name|pluginName
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
literal|"PluginUser["
operator|+
name|pluginName
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

