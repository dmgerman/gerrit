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
DECL|package|com.google.gerrit.acceptance.rest
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
name|truth
operator|.
name|Truth8
operator|.
name|assertThat
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
name|server
operator|.
name|group
operator|.
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
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
name|RestResponse
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
name|GlobalCapability
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
name|ProjectCacheImpl
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
name|restapi
operator|.
name|config
operator|.
name|ListTasks
operator|.
name|TaskInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|Optional
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
comment|/**  * Tests for checking the bindings of the config REST API.  *  *<p>These tests only verify that the config REST endpoints are correctly bound, they do no test  * the functionality of the config REST endpoints (for details see JavaDoc on {@link  * AbstractRestApiBindingsTest}).  */
end_comment

begin_class
DECL|class|ConfigRestApiBindingsIT
specifier|public
class|class
name|ConfigRestApiBindingsIT
extends|extends
name|AbstractRestApiBindingsTest
block|{
comment|/**    * Config REST endpoints to be tested, the URLs contain no placeholders since the only supported    * config identifier ('server') can be hard-coded.    */
DECL|field|CONFIG_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|CONFIG_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/version"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/info"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/preferences"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/config/server/preferences"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/preferences.diff"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/config/server/preferences.diff"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/preferences.edit"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/config/server/preferences.edit"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/top-menus"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/config/server/email.confirm"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/config/server/check.consistency"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/config/server/reload"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/summary"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/capabilities"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/caches"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/config/server/caches"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/tasks"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Cache REST endpoints to be tested, the URLs contain a placeholder for the cache identifier.    * Since there is only supported a single supported config identifier ('server') it can be    * hard-coded.    */
DECL|field|CACHE_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|CACHE_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/caches/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Task REST endpoints to be tested, the URLs contain a placeholder for the task identifier. Since    * there is only supported a single supported config identifier ('server') it can be hard-coded.    */
DECL|field|TASK_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|TASK_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/config/server/tasks/%s"
argument_list|)
argument_list|,
comment|// Task deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/config/server/tasks/%s"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|configEndpoints ()
specifier|public
name|void
name|configEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 'Access Database' is needed for the '/config/server/check.consistency' REST endpoint
name|allowGlobalCapabilities
argument_list|(
name|REGISTERED_USERS
argument_list|,
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|CONFIG_ENDPOINTS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cacheEndpoints ()
specifier|public
name|void
name|cacheEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|CACHE_ENDPOINTS
argument_list|,
name|ProjectCacheImpl
operator|.
name|CACHE_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|taskEndpoints ()
specifier|public
name|void
name|taskEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/tasks/"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|result
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|TaskInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|id
init|=
name|result
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|t
lambda|->
literal|"Log File Compressor"
operator|.
name|equals
argument_list|(
name|t
operator|.
name|command
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|t
lambda|->
name|t
operator|.
name|id
argument_list|)
operator|.
name|findFirst
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|id
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|execute
argument_list|(
name|TASK_ENDPOINTS
argument_list|,
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

