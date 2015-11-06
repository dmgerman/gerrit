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
name|config
operator|.
name|ListCaches
operator|.
name|CacheInfo
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
DECL|class|FlushCacheIT
specifier|public
class|class
name|FlushCacheIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|flushCache ()
specifier|public
name|void
name|flushCache
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/groups"
argument_list|)
decl_stmt|;
name|CacheInfo
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
name|CacheInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
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
name|adminSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/groups/flush"
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
name|adminSession
operator|.
name|get
argument_list|(
literal|"/config/server/caches/groups"
argument_list|)
expr_stmt|;
name|result
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
name|result
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
DECL|method|flushCache_Forbidden ()
specifier|public
name|void
name|flushCache_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|userSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/accounts/flush"
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushCache_NotFound ()
specifier|public
name|void
name|flushCache_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|adminSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/nonExisting/flush"
argument_list|)
operator|.
name|assertNotFound
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushCacheWithGerritPrefix ()
specifier|public
name|void
name|flushCacheWithGerritPrefix
parameter_list|()
throws|throws
name|Exception
block|{
name|adminSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/gerrit-accounts/flush"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushWebSessionsCache ()
specifier|public
name|void
name|flushWebSessionsCache
parameter_list|()
throws|throws
name|Exception
block|{
name|adminSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/web_sessions/flush"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|flushWebSessionsCache_Forbidden ()
specifier|public
name|void
name|flushWebSessionsCache_Forbidden
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
name|VIEW_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
expr_stmt|;
try|try
block|{
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/accounts/flush"
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
name|userSession
operator|.
name|post
argument_list|(
literal|"/config/server/caches/web_sessions/flush"
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
name|VIEW_CACHES
argument_list|,
name|GlobalCapability
operator|.
name|FLUSH_CACHES
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

