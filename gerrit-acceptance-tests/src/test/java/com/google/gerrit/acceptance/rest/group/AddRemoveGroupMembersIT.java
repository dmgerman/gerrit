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
name|AccountAssert
operator|.
name|assertAccountInfo
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
name|group
operator|.
name|GroupAssert
operator|.
name|assertGroupInfo
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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
name|common
operator|.
name|collect
operator|.
name|Maps
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
name|Sets
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
name|AccountGroupIncludeByUuid
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
name|AccountGroupMember
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
name|server
operator|.
name|ReviewDb
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
name|account
operator|.
name|GroupCache
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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|After
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
name|Iterator
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|AddRemoveGroupMembersIT
specifier|public
class|class
name|AddRemoveGroupMembersIT
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
DECL|field|reviewDbProvider
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|reviewDbProvider
decl_stmt|;
annotation|@
name|Inject
DECL|field|groupCache
specifier|private
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|session
specifier|private
name|RestSession
name|session
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
decl_stmt|;
DECL|field|db
specifier|private
name|ReviewDb
name|db
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
name|admin
operator|=
name|accounts
operator|.
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
name|session
operator|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
name|admin
argument_list|)
expr_stmt|;
name|db
operator|=
name|reviewDbProvider
operator|.
name|open
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addToNonExistingGroup_NotFound ()
specifier|public
name|void
name|addToNonExistingGroup_NotFound
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
name|PUT
argument_list|(
literal|"/groups/non-existing/members/admin"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeFromNonExistingGroup_NotFound ()
specifier|public
name|void
name|removeFromNonExistingGroup_NotFound
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
name|DELETE
argument_list|(
literal|"/groups/non-existing/members/admin"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addRemoveMember ()
specifier|public
name|void
name|addRemoveMember
parameter_list|()
throws|throws
name|Exception
block|{
name|TestAccount
name|u
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"user"
argument_list|,
literal|"user@example.com"
argument_list|,
literal|"Full Name"
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|PUT
argument_list|(
literal|"/groups/Administrators/members/user"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_CREATED
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|AccountInfo
name|ai
init|=
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
decl_stmt|;
name|assertAccountInfo
argument_list|(
name|u
argument_list|,
name|ai
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
literal|"Administrators"
argument_list|,
name|admin
argument_list|,
name|u
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NO_CONTENT
argument_list|,
name|DELETE
argument_list|(
literal|"/groups/Administrators/members/user"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
literal|"Administrators"
argument_list|,
name|admin
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addExistingMember_OK ()
specifier|public
name|void
name|addExistingMember_OK
parameter_list|()
throws|throws
name|IOException
block|{
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|PUT
argument_list|(
literal|"/groups/Administrators/members/admin"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addMultipleMembers ()
specifier|public
name|void
name|addMultipleMembers
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"users"
argument_list|)
expr_stmt|;
name|TestAccount
name|u1
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"u1"
argument_list|,
literal|"u1@example.com"
argument_list|,
literal|"Full Name 1"
argument_list|)
decl_stmt|;
name|TestAccount
name|u2
init|=
name|accounts
operator|.
name|create
argument_list|(
literal|"u2"
argument_list|,
literal|"u2@example.com"
argument_list|,
literal|"Full Name 2"
argument_list|)
decl_stmt|;
name|MembersInput
name|input
init|=
operator|new
name|MembersInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|members
operator|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
expr_stmt|;
name|input
operator|.
name|members
operator|.
name|add
argument_list|(
name|u1
operator|.
name|username
argument_list|)
expr_stmt|;
name|input
operator|.
name|members
operator|.
name|add
argument_list|(
name|u2
operator|.
name|username
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|POST
argument_list|(
literal|"/groups/users/members"
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|ai
init|=
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
decl_stmt|;
name|assertMembers
argument_list|(
name|ai
argument_list|,
name|u1
argument_list|,
name|u2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|includeRemoveGroup ()
specifier|public
name|void
name|includeRemoveGroup
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"newGroup"
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|PUT
argument_list|(
literal|"/groups/Administrators/groups/newGroup"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_CREATED
argument_list|,
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|GroupInfo
name|i
init|=
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
name|GroupInfo
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
name|assertGroupInfo
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"newGroup"
argument_list|)
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|assertIncludes
argument_list|(
literal|"Administrators"
argument_list|,
literal|"newGroup"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_NO_CONTENT
argument_list|,
name|DELETE
argument_list|(
literal|"/groups/Administrators/groups/newGroup"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNoIncludes
argument_list|(
literal|"Administrators"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|includeExistingGroup_OK ()
specifier|public
name|void
name|includeExistingGroup_OK
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"newGroup"
argument_list|)
expr_stmt|;
name|PUT
argument_list|(
literal|"/groups/Administrators/groups/newGroup"
argument_list|)
operator|.
name|consume
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|,
name|PUT
argument_list|(
literal|"/groups/Administrators/groups/newGroup"
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addMultipleIncludes ()
specifier|public
name|void
name|addMultipleIncludes
parameter_list|()
throws|throws
name|Exception
block|{
name|group
argument_list|(
literal|"newGroup1"
argument_list|)
expr_stmt|;
name|group
argument_list|(
literal|"newGroup2"
argument_list|)
expr_stmt|;
name|GroupsInput
name|input
init|=
operator|new
name|GroupsInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|groups
operator|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
expr_stmt|;
name|input
operator|.
name|groups
operator|.
name|add
argument_list|(
literal|"newGroup1"
argument_list|)
expr_stmt|;
name|input
operator|.
name|groups
operator|.
name|add
argument_list|(
literal|"newGroup2"
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|POST
argument_list|(
literal|"/groups/Administrators/groups"
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|gi
init|=
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
name|GroupInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|assertIncludes
argument_list|(
name|gi
argument_list|,
literal|"newGroup1"
argument_list|,
literal|"newGroup2"
argument_list|)
expr_stmt|;
block|}
DECL|method|PUT (String endpoint)
specifier|private
name|RestResponse
name|PUT
parameter_list|(
name|String
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|session
operator|.
name|put
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
DECL|method|DELETE (String endpoint)
specifier|private
name|int
name|DELETE
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
name|delete
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
return|return
name|r
operator|.
name|getStatusCode
argument_list|()
return|;
block|}
DECL|method|POST (String endPoint, MembersInput mi)
specifier|private
name|RestResponse
name|POST
parameter_list|(
name|String
name|endPoint
parameter_list|,
name|MembersInput
name|mi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|session
operator|.
name|post
argument_list|(
name|endPoint
argument_list|,
name|mi
argument_list|)
return|;
block|}
DECL|method|POST (String endPoint, GroupsInput gi)
specifier|private
name|RestResponse
name|POST
parameter_list|(
name|String
name|endPoint
parameter_list|,
name|GroupsInput
name|gi
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|session
operator|.
name|post
argument_list|(
name|endPoint
argument_list|,
name|gi
argument_list|)
return|;
block|}
DECL|method|group (String name)
specifier|private
name|void
name|group
parameter_list|(
name|String
name|name
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
DECL|method|assertMembers (String group, TestAccount... members)
specifier|private
name|void
name|assertMembers
parameter_list|(
name|String
name|group
parameter_list|,
name|TestAccount
modifier|...
name|members
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|group
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|ResultSet
argument_list|<
name|AccountGroupMember
argument_list|>
name|all
init|=
name|db
operator|.
name|accountGroupMembers
argument_list|()
operator|.
name|byGroup
argument_list|(
name|g
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroupMember
name|m
range|:
name|all
control|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|m
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|size
argument_list|()
operator|==
name|members
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|TestAccount
name|a
range|:
name|members
control|)
block|{
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
name|a
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertMembers (List<AccountInfo> ai, TestAccount... members)
specifier|private
name|void
name|assertMembers
parameter_list|(
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|ai
parameter_list|,
name|TestAccount
modifier|...
name|members
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|AccountInfo
argument_list|>
name|infoById
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|AccountInfo
name|i
range|:
name|ai
control|)
block|{
name|infoById
operator|.
name|put
argument_list|(
name|i
operator|.
name|_account_id
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|TestAccount
name|a
range|:
name|members
control|)
block|{
name|AccountInfo
name|i
init|=
name|infoById
operator|.
name|get
argument_list|(
name|a
operator|.
name|id
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertAccountInfo
argument_list|(
name|a
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|ai
operator|.
name|size
argument_list|()
argument_list|,
name|members
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|assertIncludes (String group, String... includes)
specifier|private
name|void
name|assertIncludes
parameter_list|(
name|String
name|group
parameter_list|,
name|String
modifier|...
name|includes
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|group
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|ids
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
name|ResultSet
argument_list|<
name|AccountGroupIncludeByUuid
argument_list|>
name|all
init|=
name|db
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|byGroup
argument_list|(
name|g
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|AccountGroupIncludeByUuid
name|m
range|:
name|all
control|)
block|{
name|ids
operator|.
name|add
argument_list|(
name|m
operator|.
name|getIncludeUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|ids
operator|.
name|size
argument_list|()
operator|==
name|includes
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|i
range|:
name|includes
control|)
block|{
name|AccountGroup
operator|.
name|UUID
name|id
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|i
argument_list|)
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|ids
operator|.
name|contains
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertIncludes (List<GroupInfo> gi, String... includes)
specifier|private
name|void
name|assertIncludes
parameter_list|(
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|gi
parameter_list|,
name|String
modifier|...
name|includes
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|GroupInfo
argument_list|>
name|groupsByName
init|=
name|Maps
operator|.
name|newHashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|GroupInfo
name|i
range|:
name|gi
control|)
block|{
name|groupsByName
operator|.
name|put
argument_list|(
name|i
operator|.
name|name
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|includes
control|)
block|{
name|GroupInfo
name|i
init|=
name|groupsByName
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|assertGroupInfo
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|gi
operator|.
name|size
argument_list|()
argument_list|,
name|includes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNoIncludes (String group)
specifier|private
name|void
name|assertNoIncludes
parameter_list|(
name|String
name|group
parameter_list|)
throws|throws
name|OrmException
block|{
name|AccountGroup
name|g
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|group
argument_list|)
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|AccountGroupIncludeByUuid
argument_list|>
name|it
init|=
name|db
operator|.
name|accountGroupIncludesByUuid
argument_list|()
operator|.
name|byGroup
argument_list|(
name|g
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|it
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

