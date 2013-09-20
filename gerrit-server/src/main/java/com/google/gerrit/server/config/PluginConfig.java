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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
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
name|Objects
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
name|lib
operator|.
name|Config
import|;
end_import

begin_class
DECL|class|PluginConfig
specifier|public
class|class
name|PluginConfig
block|{
DECL|field|PLUGIN
specifier|private
specifier|static
specifier|final
name|String
name|PLUGIN
init|=
literal|"plugin"
decl_stmt|;
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|Config
name|cfg
decl_stmt|;
DECL|method|PluginConfig (String pluginName, Config cfg)
specifier|public
name|PluginConfig
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
expr_stmt|;
block|}
DECL|method|getString (String name)
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getString
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|getString (String name, String defaultValue)
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|defaultValue
parameter_list|)
block|{
return|return
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getStringList (String name)
specifier|public
name|String
index|[]
name|getStringList
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getStringList
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|getInt (String name, int defaultValue)
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getInt
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getLong (String name, long defaultValue)
specifier|public
name|long
name|getLong
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|defaultValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getLong
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getBoolean (String name, boolean defaultValue)
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getBoolean
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getEnum (String name, T defaultValue)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|T
name|getEnum
parameter_list|(
name|String
name|name
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getEnum
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getEnum (T[] all, String name, T defaultValue)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|T
name|getEnum
parameter_list|(
name|T
index|[]
name|all
parameter_list|,
name|String
name|name
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
return|return
name|cfg
operator|.
name|getEnum
argument_list|(
name|all
argument_list|,
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
block|}
end_class

end_unit

