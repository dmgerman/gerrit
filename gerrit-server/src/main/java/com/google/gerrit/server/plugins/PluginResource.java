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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|restapi
operator|.
name|RestView
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
name|TypeLiteral
import|;
end_import

begin_class
DECL|class|PluginResource
specifier|public
class|class
name|PluginResource
implements|implements
name|RestResource
block|{
DECL|field|PLUGIN_KIND
specifier|public
specifier|static
specifier|final
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|PluginResource
argument_list|>
argument_list|>
name|PLUGIN_KIND
init|=
operator|new
name|TypeLiteral
argument_list|<
name|RestView
argument_list|<
name|PluginResource
argument_list|>
argument_list|>
argument_list|()
block|{}
decl_stmt|;
DECL|field|plugin
specifier|private
specifier|final
name|Plugin
name|plugin
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|method|PluginResource (Plugin plugin)
name|PluginResource
parameter_list|(
name|Plugin
name|plugin
parameter_list|)
block|{
name|this
operator|.
name|plugin
operator|=
name|plugin
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|plugin
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
DECL|method|PluginResource (String name)
name|PluginResource
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|plugin
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getPlugin ()
specifier|public
name|Plugin
name|getPlugin
parameter_list|()
block|{
return|return
name|plugin
return|;
block|}
block|}
end_class

end_unit

