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
name|rest
operator|.
name|project
operator|.
name|RefAssert
operator|.
name|assertRefs
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
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|block
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
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
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
name|NoHttpd
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
name|TestProjectInput
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
name|testsuite
operator|.
name|project
operator|.
name|ProjectOperations
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
name|testsuite
operator|.
name|request
operator|.
name|RequestScopeOperations
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
name|extensions
operator|.
name|api
operator|.
name|projects
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
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ProjectApi
operator|.
name|ListRefsRequest
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
name|BadRequestException
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
name|ResourceNotFoundException
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
name|RefNames
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
name|Inject
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
annotation|@
name|NoHttpd
DECL|class|ListBranchesIT
specifier|public
class|class
name|ListBranchesIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
DECL|field|requestScopeOperations
annotation|@
name|Inject
specifier|private
name|RequestScopeOperations
name|requestScopeOperations
decl_stmt|;
annotation|@
name|Test
DECL|method|listBranchesOfNonExistingProject_NotFound ()
specifier|public
name|void
name|listBranchesOfNonExistingProject_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThrows
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
literal|"non-existing"
argument_list|)
operator|.
name|branches
argument_list|()
operator|.
name|get
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
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|block
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/*"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|assertThrows
argument_list|(
name|ResourceNotFoundException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branches
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|TestProjectInput
argument_list|(
name|createEmptyCommit
operator|=
literal|false
argument_list|)
DECL|method|listBranchesOfEmptyProject ()
specifier|public
name|void
name|listBranchesOfEmptyProject
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch
argument_list|(
literal|"HEAD"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|,
name|branch
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|get
argument_list|()
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
name|Exception
block|{
name|String
name|master
init|=
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|dev
init|=
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|refsConfig
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|name
argument_list|()
decl_stmt|;
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch
argument_list|(
literal|"HEAD"
argument_list|,
literal|"master"
argument_list|,
literal|false
argument_list|)
argument_list|,
name|branch
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|refsConfig
argument_list|,
literal|false
argument_list|)
argument_list|,
name|branch
argument_list|(
literal|"refs/heads/dev"
argument_list|,
name|dev
argument_list|,
literal|true
argument_list|)
argument_list|,
name|branch
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|master
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|get
argument_list|()
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
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|block
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/dev"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|String
name|master
init|=
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
expr_stmt|;
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
comment|// refs/meta/config is hidden since user is no project owner
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch
argument_list|(
literal|"HEAD"
argument_list|,
literal|"master"
argument_list|,
literal|false
argument_list|)
argument_list|,
name|branch
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|master
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|get
argument_list|()
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
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|block
argument_list|(
name|Permission
operator|.
name|READ
argument_list|)
operator|.
name|ref
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|group
argument_list|(
name|REGISTERED_USERS
argument_list|)
argument_list|)
operator|.
name|update
argument_list|()
expr_stmt|;
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|String
name|dev
init|=
name|pushTo
argument_list|(
literal|"refs/heads/dev"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
comment|// refs/meta/config is hidden since user is no project owner
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch
argument_list|(
literal|"refs/heads/dev"
argument_list|,
name|dev
argument_list|,
literal|false
argument_list|)
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|get
argument_list|()
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
name|BranchInfo
name|head
init|=
name|branch
argument_list|(
literal|"HEAD"
argument_list|,
literal|"master"
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BranchInfo
name|refsConfig
init|=
name|branch
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BranchInfo
name|master
init|=
name|branch
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch1
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch2
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch3
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Using only limit.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|head
argument_list|,
name|refsConfig
argument_list|,
name|master
argument_list|,
name|branch1
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withLimit
argument_list|(
literal|4
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Limit higher than total number of branches.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|head
argument_list|,
name|refsConfig
argument_list|,
name|master
argument_list|,
name|branch1
argument_list|,
name|branch2
argument_list|,
name|branch3
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withLimit
argument_list|(
literal|25
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Using start only.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|master
argument_list|,
name|branch1
argument_list|,
name|branch2
argument_list|,
name|branch3
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withStart
argument_list|(
literal|2
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Skip more branches than the number of available branches.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|list
argument_list|()
operator|.
name|withStart
argument_list|(
literal|7
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Ssing start and limit.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|master
argument_list|,
name|branch1
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withStart
argument_list|(
literal|2
argument_list|)
operator|.
name|withLimit
argument_list|(
literal|2
argument_list|)
operator|.
name|get
argument_list|()
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
name|BranchInfo
name|master
init|=
name|branch
argument_list|(
literal|"refs/heads/master"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch1
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch1"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch2
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch2"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|BranchInfo
name|branch3
init|=
name|branch
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|,
name|pushTo
argument_list|(
literal|"refs/heads/someBranch3"
argument_list|)
operator|.
name|getCommit
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// Using substring.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch1
argument_list|,
name|branch2
argument_list|,
name|branch3
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"some"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch1
argument_list|,
name|branch2
argument_list|,
name|branch3
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"Branch"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|branch1
argument_list|,
name|branch2
argument_list|,
name|branch3
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"somebranch"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Using regex.
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|master
argument_list|)
argument_list|,
name|list
argument_list|()
operator|.
name|withRegex
argument_list|(
literal|".*ast.*r"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertRefs
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|list
argument_list|()
operator|.
name|withRegex
argument_list|(
literal|".*AST.*R"
argument_list|)
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Conflicting options
name|assertBadRequest
argument_list|(
name|list
argument_list|()
operator|.
name|withSubstring
argument_list|(
literal|"somebranch"
argument_list|)
operator|.
name|withRegex
argument_list|(
literal|".*ast.*r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|list ()
specifier|private
name|ListRefsRequest
argument_list|<
name|BranchInfo
argument_list|>
name|list
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branches
argument_list|()
return|;
block|}
DECL|method|branch (String ref, String revision, boolean canDelete)
specifier|private
specifier|static
name|BranchInfo
name|branch
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|revision
parameter_list|,
name|boolean
name|canDelete
parameter_list|)
block|{
name|BranchInfo
name|info
init|=
operator|new
name|BranchInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|info
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|info
operator|.
name|canDelete
operator|=
name|canDelete
condition|?
literal|true
else|:
literal|null
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|assertBadRequest (ListRefsRequest<BranchInfo> req)
specifier|private
name|void
name|assertBadRequest
parameter_list|(
name|ListRefsRequest
argument_list|<
name|BranchInfo
argument_list|>
name|req
parameter_list|)
throws|throws
name|Exception
block|{
name|assertThrows
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|req
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

