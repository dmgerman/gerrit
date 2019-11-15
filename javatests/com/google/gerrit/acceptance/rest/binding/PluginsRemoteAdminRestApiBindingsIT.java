begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.binding
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|binding
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|ImmutableList
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|config
operator|.
name|GerritConfig
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestApiCallHelper
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestCall
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
name|RawInputUtil
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
name|api
operator|.
name|plugins
operator|.
name|InstallPluginInput
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
name|RawInput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests for checking the remote administration bindings of the plugins REST API.  *  *<p>These tests only verify that the plugin REST endpoints are correctly bound, they do no test  * the functionality of the plugin REST endpoints.  */
end_comment

begin_class
DECL|class|PluginsRemoteAdminRestApiBindingsIT
specifier|public
class|class
name|PluginsRemoteAdminRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
comment|/**    * Plugin REST endpoints to be tested, each URL contains a placeholder for the plugin identifier.    */
DECL|field|PLUGIN_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|PLUGIN_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|put
argument_list|(
literal|"/plugins/%s"
argument_list|)
argument_list|,
comment|// For GET requests prefixing the view name with 'gerrit~' is required.
name|RestCall
operator|.
name|get
argument_list|(
literal|"/plugins/%s/gerrit~status"
argument_list|)
argument_list|,
comment|// POST (and PUT) requests don't require the 'gerrit~' prefix in front of the view name.
name|RestCall
operator|.
name|post
argument_list|(
literal|"/plugins/%s/gerrit~enable"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/plugins/%s/gerrit~disable"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/plugins/%s/gerrit~reload"
argument_list|)
argument_list|,
comment|// Plugin deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/plugins/%s"
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|JS_PLUGIN
specifier|private
specifier|static
specifier|final
name|String
name|JS_PLUGIN
init|=
literal|"Gerrit.install(function(self){});\n"
decl_stmt|;
DECL|field|JS_PLUGIN_CONTENT
specifier|private
specifier|static
specifier|final
name|RawInput
name|JS_PLUGIN_CONTENT
init|=
name|RawInputUtil
operator|.
name|create
argument_list|(
name|JS_PLUGIN
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"plugins.allowRemoteAdmin"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|pluginEndpoints ()
specifier|public
name|void
name|pluginEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pluginName
init|=
literal|"my-plugin"
decl_stmt|;
name|installPlugin
argument_list|(
name|pluginName
argument_list|)
expr_stmt|;
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|PLUGIN_ENDPOINTS
argument_list|,
name|pluginName
argument_list|)
expr_stmt|;
block|}
DECL|method|installPlugin (String pluginName)
specifier|private
name|void
name|installPlugin
parameter_list|(
name|String
name|pluginName
parameter_list|)
throws|throws
name|Exception
block|{
name|InstallPluginInput
name|input
init|=
operator|new
name|InstallPluginInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|raw
operator|=
name|JS_PLUGIN_CONTENT
expr_stmt|;
name|gApi
operator|.
name|plugins
argument_list|()
operator|.
name|install
argument_list|(
name|pluginName
operator|+
literal|".js"
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

