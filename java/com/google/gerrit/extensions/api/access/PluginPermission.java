begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|checkNotNull
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

begin_comment
comment|/** A global capability type permission used by a plugin. */
end_comment

begin_class
DECL|class|PluginPermission
specifier|public
class|class
name|PluginPermission
implements|implements
name|GlobalOrPluginPermission
block|{
DECL|field|pluginName
specifier|private
specifier|final
name|String
name|pluginName
decl_stmt|;
DECL|field|capability
specifier|private
specifier|final
name|String
name|capability
decl_stmt|;
DECL|field|fallBackToAdmin
specifier|private
specifier|final
name|boolean
name|fallBackToAdmin
decl_stmt|;
DECL|method|PluginPermission (String pluginName, String capability)
specifier|public
name|PluginPermission
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|capability
parameter_list|)
block|{
name|this
argument_list|(
name|pluginName
argument_list|,
name|capability
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|PluginPermission (String pluginName, String capability, boolean fallBackToAdmin)
specifier|public
name|PluginPermission
parameter_list|(
name|String
name|pluginName
parameter_list|,
name|String
name|capability
parameter_list|,
name|boolean
name|fallBackToAdmin
parameter_list|)
block|{
name|this
operator|.
name|pluginName
operator|=
name|checkNotNull
argument_list|(
name|pluginName
argument_list|,
literal|"pluginName"
argument_list|)
expr_stmt|;
name|this
operator|.
name|capability
operator|=
name|checkNotNull
argument_list|(
name|capability
argument_list|,
literal|"capability"
argument_list|)
expr_stmt|;
name|this
operator|.
name|fallBackToAdmin
operator|=
name|fallBackToAdmin
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
DECL|method|capability ()
specifier|public
name|String
name|capability
parameter_list|()
block|{
return|return
name|capability
return|;
block|}
DECL|method|fallBackToAdmin ()
specifier|public
name|boolean
name|fallBackToAdmin
parameter_list|()
block|{
return|return
name|fallBackToAdmin
return|;
block|}
annotation|@
name|Override
DECL|method|permissionName ()
specifier|public
name|String
name|permissionName
parameter_list|()
block|{
return|return
name|pluginName
operator|+
literal|'-'
operator|+
name|capability
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
name|capability
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
name|capability
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
name|PluginPermission
condition|)
block|{
name|PluginPermission
name|b
init|=
operator|(
name|PluginPermission
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
name|capability
operator|.
name|equals
argument_list|(
name|b
operator|.
name|capability
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
literal|"PluginPermission[plugin="
operator|+
name|pluginName
operator|+
literal|", capability="
operator|+
name|capability
operator|+
literal|']'
return|;
block|}
block|}
end_class

end_unit

