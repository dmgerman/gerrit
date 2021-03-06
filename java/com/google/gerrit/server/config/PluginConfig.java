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
name|MoreObjects
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
name|Iterables
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
name|data
operator|.
name|GroupReference
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
name|project
operator|.
name|ProjectConfig
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
name|project
operator|.
name|ProjectState
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
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
name|Config
name|cfg
decl_stmt|;
DECL|field|projectConfig
specifier|private
specifier|final
name|ProjectConfig
name|projectConfig
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
argument_list|(
name|pluginName
argument_list|,
name|cfg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|PluginConfig (String pluginName, Config cfg, ProjectConfig projectConfig)
specifier|public
name|PluginConfig
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|Config
name|cfg
parameter_list|,
name|ProjectConfig
name|projectConfig
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
name|this
operator|.
name|projectConfig
operator|=
name|projectConfig
expr_stmt|;
block|}
DECL|method|withInheritance (ProjectState.Factory projectStateFactory)
name|PluginConfig
name|withInheritance
parameter_list|(
name|ProjectState
operator|.
name|Factory
name|projectStateFactory
parameter_list|)
block|{
if|if
condition|(
name|projectConfig
operator|==
literal|null
condition|)
block|{
return|return
name|this
return|;
block|}
name|ProjectState
name|state
init|=
name|projectStateFactory
operator|.
name|create
argument_list|(
name|projectConfig
argument_list|)
decl_stmt|;
name|ProjectState
name|parent
init|=
name|Iterables
operator|.
name|getFirst
argument_list|(
name|state
operator|.
name|parents
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|PluginConfig
name|parentPluginConfig
init|=
name|parent
operator|.
name|getConfig
argument_list|()
operator|.
name|getPluginConfig
argument_list|(
name|pluginName
argument_list|)
operator|.
name|withInheritance
argument_list|(
name|projectStateFactory
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|allNames
init|=
name|cfg
operator|.
name|getNames
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|)
decl_stmt|;
name|cfg
operator|=
name|copyConfig
argument_list|(
name|cfg
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|name
range|:
name|parentPluginConfig
operator|.
name|cfg
operator|.
name|getNames
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|allNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|parentPluginConfig
operator|.
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
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|GroupReference
name|groupRef
init|=
name|parentPluginConfig
operator|.
name|projectConfig
operator|.
name|getGroup
argument_list|(
name|GroupReference
operator|.
name|extractGroupName
argument_list|(
name|value
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|groupRef
operator|!=
literal|null
condition|)
block|{
name|projectConfig
operator|.
name|resolve
argument_list|(
name|groupRef
argument_list|)
expr_stmt|;
block|}
block|}
name|cfg
operator|.
name|setStringList
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|this
return|;
block|}
DECL|method|copyConfig (Config cfg)
specifier|private
specifier|static
name|Config
name|copyConfig
parameter_list|(
name|Config
name|cfg
parameter_list|)
block|{
name|Config
name|copiedCfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
try|try
block|{
name|copiedCfg
operator|.
name|fromText
argument_list|(
name|cfg
operator|.
name|toText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
comment|// cannot happen
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|copiedCfg
return|;
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
if|if
condition|(
name|defaultValue
operator|==
literal|null
condition|)
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
return|return
name|MoreObjects
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
DECL|method|setString (String name, String value)
specifier|public
name|void
name|setString
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|setString
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setStringList (String name, List<String> values)
specifier|public
name|void
name|setStringList
parameter_list|(
name|String
name|name
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
if|if
condition|(
name|values
operator|==
literal|null
operator|||
name|values
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|setStringList
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setInt (String name, int value)
specifier|public
name|void
name|setInt
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
block|{
name|cfg
operator|.
name|setInt
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
DECL|method|setLong (String name, long value)
specifier|public
name|void
name|setLong
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|value
parameter_list|)
block|{
name|cfg
operator|.
name|setLong
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
DECL|method|setBoolean (String name, boolean value)
specifier|public
name|void
name|setBoolean
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
block|{
name|cfg
operator|.
name|setBoolean
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
DECL|method|setEnum (String name, T value)
specifier|public
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|void
name|setEnum
parameter_list|(
name|String
name|name
parameter_list|,
name|T
name|value
parameter_list|)
block|{
name|cfg
operator|.
name|setEnum
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
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
DECL|method|unset (String name)
specifier|public
name|void
name|unset
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|cfg
operator|.
name|unset
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|getNames ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getNames
parameter_list|()
block|{
return|return
name|cfg
operator|.
name|getNames
argument_list|(
name|PLUGIN
argument_list|,
name|pluginName
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|getGroupReference (String name)
specifier|public
name|GroupReference
name|getGroupReference
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|projectConfig
operator|.
name|getGroup
argument_list|(
name|GroupReference
operator|.
name|extractGroupName
argument_list|(
name|getString
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setGroupReference (String name, GroupReference value)
specifier|public
name|void
name|setGroupReference
parameter_list|(
name|String
name|name
parameter_list|,
name|GroupReference
name|value
parameter_list|)
block|{
name|GroupReference
name|groupRef
init|=
name|projectConfig
operator|.
name|resolve
argument_list|(
name|value
argument_list|)
decl_stmt|;
name|setString
argument_list|(
name|name
argument_list|,
name|groupRef
operator|.
name|toConfigValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

