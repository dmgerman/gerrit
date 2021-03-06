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
DECL|package|com.google.gerrit.acceptance.rest.project
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
name|project
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
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RemoteRefUpdate
operator|.
name|Status
operator|.
name|REJECTED_OTHER_REASON
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
name|Iterables
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BanCommitInput
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
name|project
operator|.
name|BanCommit
operator|.
name|BanResultInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevCommit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RemoteRefUpdate
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
DECL|class|BanCommitIT
specifier|public
class|class
name|BanCommitIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|banCommit ()
specifier|public
name|void
name|banCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|c
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"some content"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/ban/"
argument_list|,
name|BanCommitInput
operator|.
name|fromCommits
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|BanResultInfo
name|info
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
name|BanResultInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|info
operator|.
name|newlyBanned
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|c
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|alreadyBanned
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|ignored
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|RemoteRefUpdate
name|u
init|=
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
operator|.
name|getRemoteUpdate
argument_list|(
literal|"refs/heads/master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|u
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|u
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"contains banned commit"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|banAlreadyBannedCommit ()
specifier|public
name|void
name|banAlreadyBannedCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/ban/"
argument_list|,
name|BanCommitInput
operator|.
name|fromCommits
argument_list|(
literal|"a8a477efffbbf3b44169bb9a1d3a334cbbd9aa96"
argument_list|)
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/ban/"
argument_list|,
name|BanCommitInput
operator|.
name|fromCommits
argument_list|(
literal|"a8a477efffbbf3b44169bb9a1d3a334cbbd9aa96"
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|BanResultInfo
name|info
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
name|BanResultInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|info
operator|.
name|alreadyBanned
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"a8a477efffbbf3b44169bb9a1d3a334cbbd9aa96"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|newlyBanned
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|ignored
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|banCommit_Forbidden ()
specifier|public
name|void
name|banCommit_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|userRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/ban/"
argument_list|,
name|BanCommitInput
operator|.
name|fromCommits
argument_list|(
literal|"a8a477efffbbf3b44169bb9a1d3a334cbbd9aa96"
argument_list|)
argument_list|)
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

