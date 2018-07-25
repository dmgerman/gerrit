begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.config
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
name|config
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
name|Truth
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
import|import static
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
name|PostCaches
operator|.
name|Operation
operator|.
name|FLUSH
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
name|restapi
operator|.
name|config
operator|.
name|PostCaches
operator|.
name|Operation
operator|.
name|FLUSH_ALL
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
name|restapi
operator|.
name|config
operator|.
name|ListCaches
operator|.
name|CacheInfo
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
name|PostCaches
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CacheOperationsIT
specifier|public
class|class
name|CacheOperationsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|flushAll ()
specifier|public
name|void
name|flushAll
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
literal|"/config/server/caches/project_list"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|CacheInfo
name|cacheInfo
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
name|CacheInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH_ALL
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/project_list"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|cacheInfo
operator|=
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
name|CacheInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushAll_Forbidden ()
specifier|public
name|void
name|flushAll_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH_ALL
argument_list|)
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushAll_BadRequest ()
specifier|public
name|void
name|flushAll_BadRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH_ALL
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"projects"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flush ()
specifier|public
name|void
name|flush
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
literal|"/config/server/caches/project_list"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|CacheInfo
name|cacheInfo
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
name|CacheInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/projects"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|cacheInfo
operator|=
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
name|CacheInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|1
argument_list|)
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"accounts"
argument_list|,
literal|"project_list"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/project_list"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|cacheInfo
operator|=
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
name|CacheInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/projects"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|cacheInfo
operator|=
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
name|CacheInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flush_Forbidden ()
specifier|public
name|void
name|flush_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"projects"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flush_BadRequest ()
specifier|public
name|void
name|flush_BadRequest
parameter_list|()
throws|throws
name|Exception
block|{
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|)
argument_list|)
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flush_UnprocessableEntity ()
specifier|public
name|void
name|flush_UnprocessableEntity
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
literal|"/config/server/caches/projects"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|CacheInfo
name|cacheInfo
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
name|CacheInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"projects"
argument_list|,
literal|"unprocessable"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertUnprocessableEntity
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/projects"
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|cacheInfo
operator|=
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
name|CacheInfo
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cacheInfo
operator|.
name|entries
operator|.
name|mem
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
operator|(
name|long
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushWebSessions_Forbidden ()
specifier|public
name|void
name|flushWebSessions_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|allowGlobalCapabilities
argument_list|(
name|REGISTERED_USERS
argument_list|,
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
expr_stmt|;
try|try
block|{
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"projects"
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/"
argument_list|,
operator|new
name|PostCaches
operator|.
name|Input
argument_list|(
name|FLUSH
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"web_sessions"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|removeGlobalCapabilities
argument_list|(
name|REGISTERED_USERS
argument_list|,
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|VIEW_CACHES
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

