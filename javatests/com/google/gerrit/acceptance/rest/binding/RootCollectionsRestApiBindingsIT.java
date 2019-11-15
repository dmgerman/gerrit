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
operator|.
name|execute
import|;
end_import

begin_import
import|import static
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
operator|.
name|Method
operator|.
name|GET
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_NOT_FOUND
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
name|RestCall
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
comment|/**  * Tests for checking the bindings of the root REST API.  *  *<p>These tests only verify that the root REST endpoints are correctly bound, they do no test the  * functionality of the root REST endpoints.  */
end_comment

begin_class
DECL|class|RootCollectionsRestApiBindingsIT
specifier|public
class|class
name|RootCollectionsRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
comment|/** Root REST endpoints to be tested, the URLs contain no placeholders. */
DECL|field|ROOT_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|ROOT_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/access/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/new-account"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|builder
argument_list|(
name|GET
argument_list|,
literal|"/config/"
argument_list|)
comment|// GET /config/ is not implemented
operator|.
name|expectedResponseCode
argument_list|(
name|SC_NOT_FOUND
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/changes/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/groups/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/groups/new-group"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/plugins/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/plugins/new-plugin"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/projects/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/projects/new-project"
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
DECL|method|rootEndpoints ()
specifier|public
name|void
name|rootEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|ROOT_ENDPOINTS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

