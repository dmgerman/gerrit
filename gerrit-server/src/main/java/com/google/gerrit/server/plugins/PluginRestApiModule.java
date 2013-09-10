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
import|import static
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
name|PluginResource
operator|.
name|PLUGIN_KIND
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
name|registration
operator|.
name|DynamicMap
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
name|RestApiModule
import|;
end_import

begin_class
DECL|class|PluginRestApiModule
specifier|public
class|class
name|PluginRestApiModule
extends|extends
name|RestApiModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
operator|new
name|PluginModule
argument_list|()
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|PluginsCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|DynamicMap
operator|.
name|mapOf
argument_list|(
name|binder
argument_list|()
argument_list|,
name|PLUGIN_KIND
argument_list|)
expr_stmt|;
name|put
argument_list|(
name|PLUGIN_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|InstallPlugin
operator|.
name|Overwrite
operator|.
name|class
argument_list|)
expr_stmt|;
name|delete
argument_list|(
name|PLUGIN_KIND
argument_list|)
operator|.
name|to
argument_list|(
name|DisablePlugin
operator|.
name|class
argument_list|)
expr_stmt|;
name|get
argument_list|(
name|PLUGIN_KIND
argument_list|,
literal|"status"
argument_list|)
operator|.
name|to
argument_list|(
name|GetStatus
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PLUGIN_KIND
argument_list|,
literal|"disable"
argument_list|)
operator|.
name|to
argument_list|(
name|DisablePlugin
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PLUGIN_KIND
argument_list|,
literal|"enable"
argument_list|)
operator|.
name|to
argument_list|(
name|EnablePlugin
operator|.
name|class
argument_list|)
expr_stmt|;
name|post
argument_list|(
name|PLUGIN_KIND
argument_list|,
literal|"reload"
argument_list|)
operator|.
name|to
argument_list|(
name|ReloadPlugin
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

