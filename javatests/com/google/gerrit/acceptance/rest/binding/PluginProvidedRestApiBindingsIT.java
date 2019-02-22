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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
operator|.
name|SC_OK
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
name|inject
operator|.
name|Singleton
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
name|servlet
operator|.
name|ServletModule
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServlet
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
comment|/**  * Tests for checking plugin-provided REST API bindings.  *  *<p>These tests only verify that the plugin-provided REST endpoints are correctly bound, they do  * not test the functionality of the plugin REST endpoints.  */
end_comment

begin_class
DECL|class|PluginProvidedRestApiBindingsIT
specifier|public
class|class
name|PluginProvidedRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
comment|/**    * Plugin REST endpoints bound by {@link MyPluginModule} with Guice serlvet definitions.    *    *<p>Each URL contains a placeholder for the plugin identifier.    *    *<p>Currently does not include any resource or documentation URLs, since those would require    * installing a plugin from a jar, which is trickier than just defining a module in this file.    */
DECL|field|SERVER_TOP_LEVEL_PLUGIN_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|SERVER_TOP_LEVEL_PLUGIN_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/plugins/%s/hello"
argument_list|)
argument_list|)
decl_stmt|;
DECL|class|MyPluginModule
specifier|static
class|class
name|MyPluginModule
extends|extends
name|ServletModule
block|{
annotation|@
name|Override
DECL|method|configureServlets ()
specifier|public
name|void
name|configureServlets
parameter_list|()
block|{
name|serve
argument_list|(
literal|"/hello"
argument_list|)
operator|.
name|with
argument_list|(
name|HelloServlet
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Singleton
DECL|class|HelloServlet
specifier|static
class|class
name|HelloServlet
extends|extends
name|HttpServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Override
DECL|method|doGet (HttpServletRequest req, HttpServletResponse res)
specifier|protected
name|void
name|doGet
parameter_list|(
name|HttpServletRequest
name|req
parameter_list|,
name|HttpServletResponse
name|res
parameter_list|)
throws|throws
name|IOException
block|{
name|res
operator|.
name|setStatus
argument_list|(
name|SC_OK
argument_list|)
expr_stmt|;
name|res
operator|.
name|getWriter
argument_list|()
operator|.
name|println
argument_list|(
literal|"Hello world"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|serverPluginTopLevelEndpoints ()
specifier|public
name|void
name|serverPluginTopLevelEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pluginName
init|=
literal|"my-plugin"
decl_stmt|;
try|try
init|(
name|AutoCloseable
name|ignored
init|=
name|installPlugin
argument_list|(
name|pluginName
argument_list|,
literal|null
argument_list|,
name|MyPluginModule
operator|.
name|class
argument_list|,
literal|null
argument_list|)
init|)
block|{
name|RestApiCallHelper
operator|.
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|SERVER_TOP_LEVEL_PLUGIN_ENDPOINTS
argument_list|,
name|pluginName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

