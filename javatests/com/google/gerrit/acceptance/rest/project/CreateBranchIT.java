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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_HEADS
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
name|ANONYMOUS_USERS
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BranchApi
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
name|BranchInput
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
name|AuthException
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
name|ResourceConflictException
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
name|RestApiException
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
name|Account
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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
name|junit
operator|.
name|Before
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
DECL|class|CreateBranchIT
specifier|public
class|class
name|CreateBranchIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|testBranch
specifier|private
name|Branch
operator|.
name|NameKey
name|testBranch
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|testBranch
operator|=
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranchRestApi ()
specifier|public
name|void
name|createBranchRestApi
parameter_list|()
throws|throws
name|Exception
block|{
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
literal|"foo"
expr_stmt|;
name|assertThat
argument_list|(
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
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|ref
argument_list|)
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|REFS_HEADS
operator|+
name|input
operator|.
name|ref
argument_list|)
expr_stmt|;
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
literal|"/branches/"
operator|+
name|input
operator|.
name|ref
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertCreated
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
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
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|ref
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|REFS_HEADS
operator|+
name|input
operator|.
name|ref
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranch_Forbidden ()
specifier|public
name|void
name|createBranch_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
name|AuthException
operator|.
name|class
argument_list|,
literal|"not permitted: create on refs/heads/test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranchByAdmin ()
specifier|public
name|void
name|createBranchByAdmin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateSucceeds
argument_list|(
name|testBranch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|branchAlreadyExists_Conflict ()
specifier|public
name|void
name|branchAlreadyExists_Conflict
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateSucceeds
argument_list|(
name|testBranch
argument_list|)
expr_stmt|;
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
name|ResourceConflictException
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranchByProjectOwner ()
specifier|public
name|void
name|createBranchByProjectOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertCreateSucceeds
argument_list|(
name|testBranch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranchByAdminCreateReferenceBlocked_Forbidden ()
specifier|public
name|void
name|createBranchByAdminCreateReferenceBlocked_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|blockCreateReference
argument_list|()
expr_stmt|;
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
name|AuthException
operator|.
name|class
argument_list|,
literal|"not permitted: create on refs/heads/test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBranchByProjectOwnerCreateReferenceBlocked_Forbidden ()
specifier|public
name|void
name|createBranchByProjectOwnerCreateReferenceBlocked_Forbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|blockCreateReference
argument_list|()
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
name|AuthException
operator|.
name|class
argument_list|,
literal|"not permitted: create on refs/heads/test"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createMetaBranch ()
specifier|public
name|void
name|createMetaBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|metaRef
init|=
name|RefNames
operator|.
name|REFS_META
operator|+
literal|"foo"
decl_stmt|;
name|allow
argument_list|(
name|metaRef
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|metaRef
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|assertCreateSucceeds
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
argument_list|,
name|metaRef
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createUserBranch_Conflict ()
specifier|public
name|void
name|createUserBranch_Conflict
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|REFS_USERS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|REFS_USERS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|assertCreateFails
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|refsUsers
argument_list|(
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|RefNames
operator|.
name|refsUsers
argument_list|(
name|admin
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|ResourceConflictException
operator|.
name|class
argument_list|,
literal|"Not allowed to create user branch."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"noteDb.groups.write"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|createGroupBranch_Conflict ()
specifier|public
name|void
name|createGroupBranch_Conflict
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|REFS_GROUPS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|allow
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|REFS_GROUPS
operator|+
literal|"*"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
name|assertCreateFails
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|allUsers
argument_list|,
name|RefNames
operator|.
name|refsGroups
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|RefNames
operator|.
name|refsGroups
argument_list|(
name|adminGroupUuid
argument_list|()
argument_list|)
argument_list|,
name|ResourceConflictException
operator|.
name|class
argument_list|,
literal|"Not allowed to create group branch."
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createWithRevision ()
specifier|public
name|void
name|createWithRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|revision
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
comment|// update master so that points to a different revision than the revision on which we create the
comment|// new branch
name|pushTo
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|revision
argument_list|)
expr_stmt|;
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
name|revision
operator|.
name|name
argument_list|()
expr_stmt|;
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|testBranch
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|revision
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|testBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|revision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createWithoutSpecifyingRevision ()
specifier|public
name|void
name|createWithoutSpecifyingRevision
parameter_list|()
throws|throws
name|Exception
block|{
comment|// If revision is not specified, the branch is created based on HEAD, which points to master.
name|RevCommit
name|expectedRevision
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
literal|null
expr_stmt|;
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|testBranch
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|testBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createWithEmptyRevision ()
specifier|public
name|void
name|createWithEmptyRevision
parameter_list|()
throws|throws
name|Exception
block|{
comment|// If revision is not specified, the branch is created based on HEAD, which points to master.
name|RevCommit
name|expectedRevision
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
literal|""
expr_stmt|;
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|testBranch
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|testBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createWithBranchNameAsRevision ()
specifier|public
name|void
name|createWithBranchNameAsRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|expectedRevision
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
literal|"master"
expr_stmt|;
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|testBranch
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|testBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createWithFullBranchNameAsRevision ()
specifier|public
name|void
name|createWithFullBranchNameAsRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|expectedRevision
init|=
name|getRemoteHead
argument_list|(
name|project
argument_list|,
literal|"master"
argument_list|)
decl_stmt|;
name|BranchInput
name|input
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|revision
operator|=
literal|"refs/heads/master"
expr_stmt|;
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|testBranch
argument_list|)
operator|.
name|create
argument_list|(
name|input
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|testBranch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|revision
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRemoteHead
argument_list|(
name|project
argument_list|,
name|testBranch
operator|.
name|getShortName
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedRevision
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotCreateWithNonExistingBranchNameAsRevision ()
specifier|public
name|void
name|cannotCreateWithNonExistingBranchNameAsRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
literal|"refs/heads/non-existing"
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"invalid revision \"refs/heads/non-existing\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotCreateWithNonExistingRevision ()
specifier|public
name|void
name|cannotCreateWithNonExistingRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"invalid revision \"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef\""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cannotCreateWithInvalidRevision ()
specifier|public
name|void
name|cannotCreateWithInvalidRevision
parameter_list|()
throws|throws
name|Exception
block|{
name|assertCreateFails
argument_list|(
name|testBranch
argument_list|,
literal|"invalid\trevision"
argument_list|,
name|BadRequestException
operator|.
name|class
argument_list|,
literal|"invalid revision \"invalid\trevision\""
argument_list|)
expr_stmt|;
block|}
DECL|method|blockCreateReference ()
specifier|private
name|void
name|blockCreateReference
parameter_list|()
throws|throws
name|Exception
block|{
name|block
argument_list|(
literal|"refs/*"
argument_list|,
name|Permission
operator|.
name|CREATE
argument_list|,
name|ANONYMOUS_USERS
argument_list|)
expr_stmt|;
block|}
DECL|method|grantOwner ()
specifier|private
name|void
name|grantOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|allow
argument_list|(
literal|"refs/*"
argument_list|,
name|Permission
operator|.
name|OWNER
argument_list|,
name|REGISTERED_USERS
argument_list|)
expr_stmt|;
block|}
DECL|method|branch (Branch.NameKey branch)
specifier|private
name|BranchApi
name|branch
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
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
name|branch
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|assertCreateSucceeds (Branch.NameKey branch)
specifier|private
name|void
name|assertCreateSucceeds
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|)
throws|throws
name|Exception
block|{
name|BranchInfo
name|created
init|=
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|created
operator|.
name|ref
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCreateFails ( Branch.NameKey branch, Class<? extends RestApiException> errType, String errMsg)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RestApiException
argument_list|>
name|errType
parameter_list|,
name|String
name|errMsg
parameter_list|)
throws|throws
name|Exception
block|{
name|assertCreateFails
argument_list|(
name|branch
argument_list|,
literal|null
argument_list|,
name|errType
argument_list|,
name|errMsg
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCreateFails ( Branch.NameKey branch, String revision, Class<? extends RestApiException> errType, String errMsg)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|String
name|revision
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RestApiException
argument_list|>
name|errType
parameter_list|,
name|String
name|errMsg
parameter_list|)
throws|throws
name|Exception
block|{
name|BranchInput
name|in
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
if|if
condition|(
name|errMsg
operator|!=
literal|null
condition|)
block|{
name|exception
operator|.
name|expectMessage
argument_list|(
name|errMsg
argument_list|)
expr_stmt|;
block|}
name|exception
operator|.
name|expect
argument_list|(
name|errType
argument_list|)
expr_stmt|;
name|branch
argument_list|(
name|branch
argument_list|)
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCreateFails (Branch.NameKey branch, Class<? extends RestApiException> errType)
specifier|private
name|void
name|assertCreateFails
parameter_list|(
name|Branch
operator|.
name|NameKey
name|branch
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|RestApiException
argument_list|>
name|errType
parameter_list|)
throws|throws
name|Exception
block|{
name|assertCreateFails
argument_list|(
name|branch
argument_list|,
name|errType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

