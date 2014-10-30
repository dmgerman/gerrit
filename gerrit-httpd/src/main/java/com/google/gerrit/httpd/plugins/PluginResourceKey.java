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
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|httpd
operator|.
name|resources
operator|.
name|ResourceKey
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
name|plugins
operator|.
name|Plugin
import|;
end_import

begin_class
DECL|class|PluginResourceKey
specifier|final
class|class
name|PluginResourceKey
implements|implements
name|ResourceKey
block|{
DECL|field|plugin
specifier|private
specifier|final
name|Plugin
operator|.
name|CacheKey
name|plugin
decl_stmt|;
DECL|field|resource
specifier|private
specifier|final
name|String
name|resource
decl_stmt|;
DECL|method|PluginResourceKey (Plugin p, String r)
name|PluginResourceKey
parameter_list|(
name|Plugin
name|p
parameter_list|,
name|String
name|r
parameter_list|)
block|{
name|this
operator|.
name|plugin
operator|=
name|p
operator|.
name|getCacheKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|resource
operator|=
name|r
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|weigh ()
specifier|public
name|int
name|weigh
parameter_list|()
block|{
return|return
name|resource
operator|.
name|length
argument_list|()
operator|*
literal|2
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
name|plugin
operator|.
name|hashCode
argument_list|()
operator|*
literal|31
operator|+
name|resource
operator|.
name|hashCode
argument_list|()
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
name|PluginResourceKey
condition|)
block|{
name|PluginResourceKey
name|rk
init|=
operator|(
name|PluginResourceKey
operator|)
name|other
decl_stmt|;
return|return
name|plugin
operator|==
name|rk
operator|.
name|plugin
operator|&&
name|resource
operator|.
name|equals
argument_list|(
name|rk
operator|.
name|resource
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

