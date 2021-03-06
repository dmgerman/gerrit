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
DECL|package|com.google.gerrit.acceptance.rest.change
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
name|change
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
name|testsuite
operator|.
name|project
operator|.
name|TestProjectUpdate
operator|.
name|allow
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
name|TestAccount
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
name|testsuite
operator|.
name|group
operator|.
name|GroupOperations
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
name|entities
operator|.
name|AccountGroup
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
name|entities
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
name|extensions
operator|.
name|common
operator|.
name|ChangeInfo
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
name|java
operator|.
name|util
operator|.
name|List
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|InMemoryRepository
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
name|junit
operator|.
name|TestRepository
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
DECL|class|IndexChangeIT
specifier|public
class|class
name|IndexChangeIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|groupOperations
annotation|@
name|Inject
specifier|private
name|GroupOperations
name|groupOperations
decl_stmt|;
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
DECL|method|indexChange ()
specifier|public
name|void
name|indexChange
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/index/"
argument_list|)
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexChangeOnNonVisibleBranch ()
specifier|public
name|void
name|indexChangeOnNonVisibleBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
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
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/index/"
argument_list|)
operator|.
name|assertNotFound
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"index.change.indexMergeable"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|indexChangeAfterOwnerLosesVisibility ()
specifier|public
name|void
name|indexChangeAfterOwnerLosesVisibility
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a test group with 2 users as members
name|TestAccount
name|user2
init|=
name|accountCreator
operator|.
name|user2
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|groupId
init|=
name|groupOperations
operator|.
name|newGroup
argument_list|()
operator|.
name|name
argument_list|(
literal|"test"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|group
init|=
name|groupOperations
operator|.
name|group
argument_list|(
name|groupId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|group
argument_list|)
operator|.
name|addMembers
argument_list|(
literal|"admin"
argument_list|,
literal|"user"
argument_list|,
name|user2
operator|.
name|username
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a project and restrict its visibility to the group
name|Project
operator|.
name|NameKey
name|p
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|projectOperations
operator|.
name|project
argument_list|(
name|p
argument_list|)
operator|.
name|forUpdate
argument_list|()
operator|.
name|add
argument_list|(
name|allow
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
name|groupCache
operator|.
name|get
argument_list|(
name|AccountGroup
operator|.
name|nameKey
argument_list|(
name|group
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
argument_list|)
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
comment|// Clone it and push a change as a regular user
name|TestRepository
argument_list|<
name|InMemoryRepository
argument_list|>
name|repo
init|=
name|cloneProject
argument_list|(
name|p
argument_list|,
name|user
argument_list|)
decl_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|user
operator|.
name|newIdent
argument_list|()
argument_list|,
name|repo
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|result
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|result
operator|.
name|assertOkStatus
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|getChange
argument_list|()
operator|.
name|change
argument_list|()
operator|.
name|getOwner
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|user
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|changeId
init|=
name|result
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
comment|// User can see the change and it is mergeable
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
name|List
argument_list|<
name|ChangeInfo
argument_list|>
name|changes
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|changes
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|changes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|mergeable
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
comment|// Other user can see the change and it is mergeable
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user2
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|changes
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|changes
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|changes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|mergeable
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// Remove the user from the group so they can no longer see the project
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|group
argument_list|)
operator|.
name|removeMembers
argument_list|(
literal|"user"
argument_list|)
expr_stmt|;
comment|// User can no longer see the change
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
name|changes
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|changes
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
comment|// Reindex the change
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|index
argument_list|()
expr_stmt|;
comment|// Other user can still see the change and it is still mergeable
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|user2
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|changes
operator|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|query
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|changes
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|changes
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|mergeable
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

