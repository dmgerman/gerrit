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
name|GitUtil
operator|.
name|initSsh
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
name|AccountCreator
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
name|acceptance
operator|.
name|RestSession
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
name|SshSession
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
name|common
operator|.
name|data
operator|.
name|AccessSection
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
name|common
operator|.
name|data
operator|.
name|PermissionRule
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
name|config
operator|.
name|AllProjectsNameProvider
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
name|MetaDataUpdate
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
name|group
operator|.
name|SystemGroupBackend
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
name|ProjectCache
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
name|errors
operator|.
name|ConfigInvalidException
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|DeleteBranchIT
specifier|public
class|class
name|DeleteBranchIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Inject
DECL|field|accounts
specifier|private
name|AccountCreator
name|accounts
decl_stmt|;
annotation|@
name|Inject
DECL|field|metaDataUpdateFactory
specifier|private
name|MetaDataUpdate
operator|.
name|Server
name|metaDataUpdateFactory
decl_stmt|;
annotation|@
name|Inject
DECL|field|projectCache
specifier|private
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|field|allProjects
specifier|private
name|AllProjectsNameProvider
name|allProjects
decl_stmt|;
DECL|field|adminSession
specifier|private
name|RestSession
name|adminSession
decl_stmt|;
DECL|field|userSession
specifier|private
name|RestSession
name|userSession
decl_stmt|;
DECL|field|project
specifier|private
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|branch
specifier|private
name|Branch
operator|.
name|NameKey
name|branch
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
name|TestAccount
name|admin
init|=
name|accounts
operator|.
name|admin
argument_list|()
decl_stmt|;
name|adminSession
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|TestAccount
name|user
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"User"
argument_list|)
decl_stmt|;
name|userSession
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|project
operator|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
literal|"p"
argument_list|)
expr_stmt|;
name|branch
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
name|initSsh
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|SshSession
name|sshSession
init|=
operator|new
name|SshSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
decl_stmt|;
try|try
block|{
name|createProject
argument_list|(
name|sshSession
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|sshSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|adminSession
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteBranch_Forbidden ()
specifier|public
name|void
name|deleteBranch_Forbidden
parameter_list|()
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|delete
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_FORBIDDEN
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteBranchByAdmin ()
specifier|public
name|void
name|deleteBranchByAdmin
parameter_list|()
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|delete
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NO_CONTENT
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
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
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches/"
operator|+
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteBranchByProjectOwner ()
specifier|public
name|void
name|deleteBranchByProjectOwner
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|delete
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NO_CONTENT
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|r
operator|=
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
literal|"/branches/"
operator|+
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteBranchByAdminForcePushBlocked ()
specifier|public
name|void
name|deleteBranchByAdminForcePushBlocked
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|blockForcePush
argument_list|()
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|delete
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NO_CONTENT
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
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
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches/"
operator|+
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteBranchByProjectOwnerForcePushBlocked_Forbidden ()
specifier|public
name|void
name|deleteBranchByProjectOwnerForcePushBlocked_Forbidden
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|grantOwner
argument_list|()
expr_stmt|;
name|blockForcePush
argument_list|()
expr_stmt|;
name|RestResponse
name|r
init|=
name|userSession
operator|.
name|delete
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
name|branch
operator|.
name|getShortName
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_FORBIDDEN
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|blockForcePush ()
specifier|private
name|void
name|blockForcePush
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|allProjects
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Block force %s"
argument_list|,
name|Permission
operator|.
name|PUSH
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|AccessSection
name|s
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/heads/*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Permission
name|p
init|=
name|s
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|PUSH
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PermissionRule
name|rule
init|=
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|SystemGroupBackend
operator|.
name|getGroup
argument_list|(
name|SystemGroupBackend
operator|.
name|ANONYMOUS_USERS
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setForce
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|rule
operator|.
name|setBlock
argument_list|()
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|grantOwner ()
specifier|private
name|void
name|grantOwner
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|MetaDataUpdate
name|md
init|=
name|metaDataUpdateFactory
operator|.
name|create
argument_list|(
name|project
argument_list|)
decl_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Grant %s"
argument_list|,
name|Permission
operator|.
name|OWNER
argument_list|)
argument_list|)
expr_stmt|;
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|AccessSection
name|s
init|=
name|config
operator|.
name|getAccessSection
argument_list|(
literal|"refs/*"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Permission
name|p
init|=
name|s
operator|.
name|getPermission
argument_list|(
name|Permission
operator|.
name|OWNER
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|PermissionRule
name|rule
init|=
operator|new
name|PermissionRule
argument_list|(
name|config
operator|.
name|resolve
argument_list|(
name|SystemGroupBackend
operator|.
name|getGroup
argument_list|(
name|SystemGroupBackend
operator|.
name|REGISTERED_USERS
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|p
operator|.
name|add
argument_list|(
name|rule
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|evict
argument_list|(
name|config
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

