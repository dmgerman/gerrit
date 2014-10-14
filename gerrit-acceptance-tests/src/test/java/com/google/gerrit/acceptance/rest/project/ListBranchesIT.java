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
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|createProject
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
name|project
operator|.
name|BranchAssert
operator|.
name|assertBranches
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
name|project
operator|.
name|Util
operator|.
name|block
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|Lists
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
name|PushOneCommit
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
name|Permission
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|git
operator|.
name|ProjectConfig
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
name|ListBranches
operator|.
name|BranchInfo
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
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
name|api
operator|.
name|errors
operator|.
name|GitAPIException
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
name|java
operator|.
name|util
operator|.
name|Collections
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

begin_class
DECL|class|ListBranchesIT
specifier|public
class|class
name|ListBranchesIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|listBranchesOfNonExistingProject_NotFound ()
specifier|public
name|void
name|listBranchesOfNonExistingProject_NotFound
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|GET
argument_list|(
literal|"/projects/non-existing/branches"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesOfNonVisibleProject_NotFound ()
specifier|public
name|void
name|listBranchesOfNonVisibleProject_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/*"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|userSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesOfEmptyProject ()
specifier|public
name|void
name|listBranchesOfEmptyProject
parameter_list|()
throws|throws
name|IOException
throws|,
name|JSchException
block|{
name|Project
operator|.
name|NameKey
name|emptyProject
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"empty"
argument_list|)
decl_stmt|;
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|emptyProject
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|emptyProject
operator|.
name|get
argument_list|()
operator|+
literal|"/branches"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|expected
init|=
name|Lists
operator|.
name|asList
argument_list|(
operator|new
name|BranchInfo
argument_list|(
literal|"refs/meta/config"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BranchInfo
index|[]
block|{
operator|new
name|BranchInfo
argument_list|(
literal|"HEAD"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|assertBranches
argument_list|(
name|expected
argument_list|,
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranches ()
specifier|public
name|void
name|listBranches
parameter_list|()
throws|throws
name|IOException
throws|,
name|GitAPIException
block|{
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|String
name|masterCommit
init|=
name|git
operator|.
name|getRepository
argument_list|()
operator|.
name|getRef
argument_list|(
literal|"master"
argument_list|)
operator|.
name|getTarget
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|String
name|devCommit
init|=
name|git
operator|.
name|getRepository
argument_list|()
operator|.
name|getRef
argument_list|(
literal|"master"
argument_list|)
operator|.
name|getTarget
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|expected
init|=
name|Lists
operator|.
name|asList
argument_list|(
operator|new
name|BranchInfo
argument_list|(
literal|"refs/meta/config"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BranchInfo
index|[]
block|{
operator|new
name|BranchInfo
argument_list|(
literal|"HEAD"
argument_list|,
literal|"master"
argument_list|,
literal|false
argument_list|)
block|,
operator|new
name|BranchInfo
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|masterCommit
argument_list|,
literal|false
argument_list|)
block|,
operator|new
name|BranchInfo
argument_list|(
literal|"refs/heads/dev"
argument_list|,
name|devCommit
argument_list|,
literal|true
argument_list|)
block|}
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|result
init|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertBranches
argument_list|(
name|expected
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// verify correct sorting
name|assertEquals
argument_list|(
literal|"HEAD"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/meta/config"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/dev"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesSomeHidden ()
specifier|public
name|void
name|listBranchesSomeHidden
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|String
name|masterCommit
init|=
name|git
operator|.
name|getRepository
argument_list|()
operator|.
name|getRef
argument_list|(
literal|"master"
argument_list|)
operator|.
name|getTarget
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches"
argument_list|)
decl_stmt|;
comment|// refs/meta/config is hidden since user is no project owner
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|expected
init|=
name|Lists
operator|.
name|asList
argument_list|(
operator|new
name|BranchInfo
argument_list|(
literal|"HEAD"
argument_list|,
literal|"master"
argument_list|,
literal|false
argument_list|)
argument_list|,
operator|new
name|BranchInfo
index|[]
block|{
operator|new
name|BranchInfo
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|masterCommit
argument_list|,
literal|false
argument_list|)
block|,         }
argument_list|)
decl_stmt|;
name|assertBranches
argument_list|(
name|expected
argument_list|,
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesHeadHidden ()
specifier|public
name|void
name|listBranchesHeadHidden
parameter_list|()
throws|throws
name|Exception
block|{
name|blockRead
argument_list|(
name|project
argument_list|,
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|String
name|devCommit
init|=
name|git
operator|.
name|getRepository
argument_list|()
operator|.
name|getRef
argument_list|(
literal|"master"
argument_list|)
operator|.
name|getTarget
argument_list|()
operator|.
name|getObjectId
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches"
argument_list|)
decl_stmt|;
comment|// refs/meta/config is hidden since user is no project owner
name|assertBranches
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|BranchInfo
argument_list|(
literal|"refs/heads/dev"
argument_list|,
name|devCommit
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|,
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesUsingPagination ()
specifier|public
name|void
name|listBranchesUsingPagination
parameter_list|()
throws|throws
name|Exception
block|{
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|)
expr_stmt|;
comment|// using only limit
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?n=4"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|result
init|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HEAD"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/meta/config"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
comment|// limit higher than total number of branches
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?n=25"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"HEAD"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/meta/config"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
comment|// using skip only
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?s=2"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
comment|// skip more branches than the number of available branches
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?s=7"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// using skip and limit
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?s=2&n=2"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listBranchesUsingFilter ()
specifier|public
name|void
name|listBranchesUsingFilter
parameter_list|()
throws|throws
name|Exception
block|{
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|)
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|)
expr_stmt|;
comment|//using substring
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?m=some"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|result
init|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?m=Branch"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
comment|//using regex
name|r
operator|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches?r=.*ast.*r"
argument_list|)
expr_stmt|;
name|result
operator|=
name|toBranchInfoList
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|ref
argument_list|)
expr_stmt|;
block|}
DECL|method|GET (String endpoint)
specifier|private
name|RestResponse
name|GET
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|adminSession
operator|.
name|get
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|blockRead (Project.NameKey project, String ref)
specifier|private
name|void
name|blockRead
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|project
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|block
argument_list|(
name|cfg
argument_list|,
name|Permission
operator|.
name|READ
argument_list|,
name|REGISTERED_USERS
argument_list|,
name|ref
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|project
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
DECL|method|toBranchInfoList (RestResponse r)
specifier|private
specifier|static
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|toBranchInfoList
parameter_list|(
name|RestResponse
name|r
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|BranchInfo
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
name|BranchInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|result
return|;
block|}
DECL|method|pushTo (String ref)
specifier|private
name|PushOneCommit
operator|.
name|Result
name|pushTo
parameter_list|(
name|String
name|ref
parameter_list|)
throws|throws
name|GitAPIException
throws|,
name|IOException
block|{
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|db
argument_list|,
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|push
operator|.
name|to
argument_list|(
name|git
argument_list|,
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit

