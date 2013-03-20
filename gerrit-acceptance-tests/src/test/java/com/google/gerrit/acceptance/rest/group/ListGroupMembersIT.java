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
DECL|package|com.google.gerrit.acceptance.rest.group
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
name|group
package|;
end_package

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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|base
operator|.
name|Function
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
name|Collections2
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
name|rest
operator|.
name|account
operator|.
name|AccountInfo
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
name|Gson
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_class
DECL|class|ListGroupMembersIT
specifier|public
class|class
name|ListGroupMembersIT
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
DECL|field|session
specifier|private
name|RestSession
name|session
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
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"Administrators"
argument_list|)
decl_stmt|;
name|session
operator|=
operator|new
name|RestSession
argument_list|(
name|admin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listNonExistingGroupMembers_NotFound ()
specifier|public
name|void
name|listNonExistingGroupMembers_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|,
name|session
operator|.
name|get
argument_list|(
literal|"/groups/non-existing/members/"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listEmptyGroupMembers ()
specifier|public
name|void
name|listEmptyGroupMembers
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"empty"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|GET
argument_list|(
literal|"/groups/empty/members/"
argument_list|)
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listNonEmptyGroupMembers ()
specifier|public
name|void
name|listNonEmptyGroupMembers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertMembers
argument_list|(
name|GET
argument_list|(
literal|"/groups/Administrators/members/"
argument_list|)
argument_list|,
literal|"admin"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"admin2"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
name|GET
argument_list|(
literal|"/groups/Administrators/members/"
argument_list|)
argument_list|,
literal|"admin"
argument_list|,
literal|"admin2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listOneGroupMember ()
specifier|public
name|void
name|listOneGroupMember
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|GET_ONE
argument_list|(
literal|"/groups/Administrators/members/admin"
argument_list|)
operator|.
name|name
argument_list|,
literal|"admin"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listGroupMembersRecursively ()
specifier|public
name|void
name|listGroupMembersRecursively
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"gx"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"ux"
argument_list|,
literal|"gx"
argument_list|)
expr_stmt|;
name|group
argument_list|(
literal|"gy"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
literal|"uy"
argument_list|,
literal|"gy"
argument_list|)
expr_stmt|;
name|PUT
argument_list|(
literal|"/groups/Administrators/groups/gx"
argument_list|)
expr_stmt|;
name|PUT
argument_list|(
literal|"/groups/gx/groups/gy"
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
name|GET
argument_list|(
literal|"/groups/Administrators/members/?recursive"
argument_list|)
argument_list|,
literal|"admin"
argument_list|,
literal|"ux"
argument_list|,
literal|"uy"
argument_list|)
expr_stmt|;
block|}
DECL|method|GET (String endpoint)
specifier|private
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|GET
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
operator|new
name|Gson
argument_list|()
operator|)
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
name|AccountInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|GET_ONE (String endpoint)
specifier|private
name|AccountInfo
name|GET_ONE
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
operator|new
name|Gson
argument_list|()
operator|)
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
name|AccountInfo
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|PUT (String endpoint)
specifier|private
name|void
name|PUT
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|session
operator|.
name|put
argument_list|(
name|endpoint
argument_list|)
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|group (String name, String ownerGroup)
specifier|private
name|void
name|group
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|ownerGroup
parameter_list|)
throws|throws
name|IOException
block|{
name|GroupInput
name|in
init|=
operator|new
name|GroupInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|owner_id
operator|=
name|ownerGroup
expr_stmt|;
name|session
operator|.
name|put
argument_list|(
literal|"/groups/"
operator|+
name|name
argument_list|,
name|in
argument_list|)
operator|.
name|consume
argument_list|()
expr_stmt|;
block|}
DECL|method|assertMembers (List<AccountInfo> members, String name, String... names)
specifier|private
name|void
name|assertMembers
parameter_list|(
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|members
parameter_list|,
name|String
name|name
parameter_list|,
name|String
modifier|...
name|names
parameter_list|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|memberNames
init|=
name|Collections2
operator|.
name|transform
argument_list|(
name|members
argument_list|,
operator|new
name|Function
argument_list|<
name|AccountInfo
argument_list|,
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|apply
parameter_list|(
annotation|@
name|Nullable
name|AccountInfo
name|info
parameter_list|)
block|{
return|return
name|info
operator|.
name|name
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|memberNames
operator|.
name|contains
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|n
range|:
name|names
control|)
block|{
name|assertTrue
argument_list|(
name|memberNames
operator|.
name|contains
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|members
operator|.
name|size
argument_list|()
argument_list|,
name|names
operator|.
name|length
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

