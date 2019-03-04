begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.access
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|access
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|MoreObjects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_comment
comment|/** Repository permissions defined by plugins. */
end_comment

begin_class
DECL|class|PluginProjectPermission
specifier|public
specifier|final
class|class
name|PluginProjectPermission
implements|implements
name|CoreOrPluginProjectPermission
block|{
DECL|field|PLUGIN_PERMISSION_NAME_PATTERN_STRING
specifier|public
specifier|static
specifier|final
name|String
name|PLUGIN_PERMISSION_NAME_PATTERN_STRING
init|=
literal|"[a-zA-Z]+"
decl_stmt|;
DECL|field|PLUGIN_PERMISSION_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PLUGIN_PERMISSION_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^"
operator|+
name|PLUGIN_PERMISSION_NAME_PATTERN_STRING
operator|+
literal|"$"
argument_list|)
decl_stmt|;
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|permission
specifier|private
specifier|final
name|String
name|permission
decl_stmt|;
DECL|method|PluginProjectPermission (String pluginName, String permission)
specifier|public
name|PluginProjectPermission
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|permission
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|pluginName
argument_list|,
literal|"pluginName"
argument_list|)
expr_stmt|;
name|requireNonNull
argument_list|(
name|permission
argument_list|,
literal|"permission"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|isValidPluginPermissionName
argument_list|(
name|permission
argument_list|)
argument_list|,
literal|"invalid plugin permission name: "
argument_list|,
name|permission
argument_list|)
expr_stmt|;
name|this
operator|.
name|pluginName
operator|=
name|pluginName
expr_stmt|;
name|this
operator|.
name|permission
operator|=
name|permission
expr_stmt|;
block|}
DECL|method|pluginName ()
specifier|public
name|String
name|pluginName
parameter_list|()
block|{
return|return
name|pluginName
return|;
block|}
DECL|method|permission ()
specifier|public
name|String
name|permission
parameter_list|()
block|{
return|return
name|permission
return|;
block|}
annotation|@
name|Override
DECL|method|describeForException ()
specifier|public
name|String
name|describeForException
parameter_list|()
block|{
return|return
name|permission
operator|+
literal|" for plugin "
operator|+
name|pluginName
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|pluginName
argument_list|,
name|permission
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object other)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|other
operator|instanceof
name|PluginProjectPermission
condition|)
block|{
name|PluginProjectPermission
name|b
init|=
operator|(
name|PluginProjectPermission
operator|)
name|other
decl_stmt|;
return|return
name|pluginName
operator|.
name|equals
argument_list|(
name|b
operator|.
name|pluginName
argument_list|)
operator|&&
name|permission
operator|.
name|equals
argument_list|(
name|b
operator|.
name|permission
argument_list|)
return|;
block|}
return|return
literal|false
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
name|MoreObjects
operator|.
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"pluginName"
argument_list|,
name|pluginName
argument_list|)
operator|.
name|add
argument_list|(
literal|"permission"
argument_list|,
name|permission
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**    * Checks if a given name is valid to be used for plugin permissions.    *    * @param name a name string.    * @return whether the name is valid as a plugin permission.    */
DECL|method|isValidPluginPermissionName (String name)
specifier|private
specifier|static
name|boolean
name|isValidPluginPermissionName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|PLUGIN_PERMISSION_PATTERN
operator|.
name|matcher
argument_list|(
name|name
argument_list|)
operator|.
name|matches
argument_list|()
return|;
block|}
block|}
end_class

end_unit

