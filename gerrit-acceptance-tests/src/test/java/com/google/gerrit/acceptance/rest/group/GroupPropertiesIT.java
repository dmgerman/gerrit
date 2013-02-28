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
name|group
operator|.
name|GroupAssert
operator|.
name|toBoolean
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
name|assertNull
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

begin_class
DECL|class|GroupPropertiesIT
specifier|public
class|class
name|GroupPropertiesIT
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
DECL|field|groupCache
specifier|private
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|admin
specifier|private
name|TestAccount
name|admin
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
name|admin
operator|=
name|accounts
operator|.
name|create
argument_list|(
literal|"admin"
argument_list|,
literal|"admin@example.com"
argument_list|,
literal|"Administrator"
argument_list|,
literal|"Administrators"
argument_list|)
expr_stmt|;
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
DECL|method|testGroupName ()
specifier|public
name|void
name|testGroupName
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountGroup
operator|.
name|NameKey
name|adminGroupName
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
decl_stmt|;
name|String
name|url
init|=
literal|"/groups/"
operator|+
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/name"
decl_stmt|;
comment|// get name
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|String
name|name
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
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertEquals
argument_list|(
literal|"Administrators"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set name with name conflict
name|GroupNameInput
name|in
init|=
operator|new
name|GroupNameInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|name
operator|=
literal|"Registered Users"
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_CONFLICT
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
comment|// set name to same name
name|in
operator|=
operator|new
name|GroupNameInput
argument_list|()
expr_stmt|;
name|in
operator|.
name|name
operator|=
literal|"Administrators"
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
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
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// rename
name|in
operator|=
operator|new
name|GroupNameInput
argument_list|()
expr_stmt|;
name|in
operator|.
name|name
operator|=
literal|"Admins"
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|String
name|newName
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
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertNotNull
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
name|in
operator|.
name|name
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|name
argument_list|,
name|newName
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
DECL|method|testGroupDescription ()
specifier|public
name|void
name|testGroupDescription
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountGroup
operator|.
name|NameKey
name|adminGroupName
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
decl_stmt|;
name|AccountGroup
name|adminGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
decl_stmt|;
name|String
name|url
init|=
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/description"
decl_stmt|;
comment|// get description
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|String
name|description
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
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertEquals
argument_list|(
name|adminGroup
operator|.
name|getDescription
argument_list|()
argument_list|,
name|description
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set description
name|GroupDescriptionInput
name|in
init|=
operator|new
name|GroupDescriptionInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|description
operator|=
literal|"All users that can administrate the Gerrit Server."
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|String
name|newDescription
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
name|String
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertEquals
argument_list|(
name|in
operator|.
name|description
argument_list|,
name|newDescription
argument_list|)
expr_stmt|;
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|description
argument_list|,
name|adminGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// delete description
name|r
operator|=
name|session
operator|.
name|delete
argument_list|(
name|url
argument_list|)
expr_stmt|;
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
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|adminGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
comment|// set description to empty string
name|in
operator|=
operator|new
name|GroupDescriptionInput
argument_list|()
expr_stmt|;
name|in
operator|.
name|description
operator|=
literal|""
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
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
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|adminGroup
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGroupOptions ()
specifier|public
name|void
name|testGroupOptions
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountGroup
operator|.
name|NameKey
name|adminGroupName
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
decl_stmt|;
name|AccountGroup
name|adminGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
decl_stmt|;
name|String
name|url
init|=
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/options"
decl_stmt|;
comment|// get options
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|GroupOptionsInfo
name|options
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
name|GroupOptionsInfo
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertEquals
argument_list|(
name|adminGroup
operator|.
name|isVisibleToAll
argument_list|()
argument_list|,
name|toBoolean
argument_list|(
name|options
operator|.
name|visible_to_all
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set options
name|GroupOptionsInput
name|in
init|=
operator|new
name|GroupOptionsInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|visible_to_all
operator|=
operator|!
name|adminGroup
operator|.
name|isVisibleToAll
argument_list|()
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|GroupOptionsInfo
name|newOptions
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
name|GroupOptionsInfo
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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
name|assertEquals
argument_list|(
name|in
operator|.
name|visible_to_all
argument_list|,
name|toBoolean
argument_list|(
name|newOptions
operator|.
name|visible_to_all
argument_list|)
argument_list|)
expr_stmt|;
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|visible_to_all
argument_list|,
name|adminGroup
operator|.
name|isVisibleToAll
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
DECL|method|testGroupOwner ()
specifier|public
name|void
name|testGroupOwner
parameter_list|()
throws|throws
name|IOException
block|{
name|AccountGroup
operator|.
name|NameKey
name|adminGroupName
init|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
literal|"Administrators"
argument_list|)
decl_stmt|;
name|AccountGroup
name|adminGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
decl_stmt|;
name|String
name|url
init|=
literal|"/groups/"
operator|+
name|adminGroup
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|"/owner"
decl_stmt|;
comment|// get owner
name|RestResponse
name|r
init|=
name|session
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|GroupInfo
name|options
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
name|assertGroupInfo
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set owner by name
name|GroupOwnerInput
name|in
init|=
operator|new
name|GroupOwnerInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|owner
operator|=
literal|"Registered Users"
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"serial"
argument_list|)
name|GroupInfo
name|newOwner
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
name|assertEquals
argument_list|(
name|in
operator|.
name|owner
argument_list|,
name|newOwner
operator|.
name|name
argument_list|)
expr_stmt|;
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertGroupInfo
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
argument_list|,
name|newOwner
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set owner by UUID
name|in
operator|=
operator|new
name|GroupOwnerInput
argument_list|()
expr_stmt|;
name|in
operator|.
name|owner
operator|=
name|adminGroup
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
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
name|adminGroup
operator|=
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroupName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|in
operator|.
name|owner
argument_list|,
name|groupCache
operator|.
name|get
argument_list|(
name|adminGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
argument_list|)
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
comment|// set non existing owner
name|in
operator|=
operator|new
name|GroupOwnerInput
argument_list|()
expr_stmt|;
name|in
operator|.
name|owner
operator|=
literal|"Non-Existing Group"
expr_stmt|;
name|r
operator|=
name|session
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|SC_BAD_REQUEST
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
DECL|class|GroupNameInput
specifier|private
specifier|static
class|class
name|GroupNameInput
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
block|}
DECL|class|GroupDescriptionInput
specifier|private
specifier|static
class|class
name|GroupDescriptionInput
block|{
DECL|field|description
name|String
name|description
decl_stmt|;
block|}
DECL|class|GroupOptionsInput
specifier|private
specifier|static
class|class
name|GroupOptionsInput
block|{
DECL|field|visible_to_all
name|Boolean
name|visible_to_all
decl_stmt|;
block|}
DECL|class|GroupOwnerInput
specifier|private
specifier|static
class|class
name|GroupOwnerInput
block|{
DECL|field|owner
name|String
name|owner
decl_stmt|;
block|}
block|}
end_class

end_unit

