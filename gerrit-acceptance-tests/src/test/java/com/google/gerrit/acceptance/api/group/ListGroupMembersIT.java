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
DECL|package|com.google.gerrit.acceptance.api.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
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
name|common
operator|.
name|Nullable
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
name|groups
operator|.
name|GroupInput
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceNotFoundException
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
name|util
operator|.
name|Arrays
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
annotation|@
name|NoHttpd
DECL|class|ListGroupMembersIT
specifier|public
class|class
name|ListGroupMembersIT
extends|extends
name|AbstractDaemonTest
block|{
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
try|try
block|{
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
literal|"non-existing"
argument_list|)
operator|.
name|members
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|expected
parameter_list|)
block|{
comment|// Expected.
block|}
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
name|String
name|group
init|=
name|createGroup
argument_list|(
literal|"empty"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
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
name|members
argument_list|()
argument_list|)
operator|.
name|isEmpty
argument_list|()
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
name|String
name|group
init|=
name|createGroup
argument_list|(
literal|"group"
argument_list|)
decl_stmt|;
name|String
name|user1
init|=
name|createAccount
argument_list|(
literal|"user1"
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|String
name|user2
init|=
name|createAccount
argument_list|(
literal|"user2"
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|assertMembers
argument_list|(
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
name|members
argument_list|()
argument_list|,
name|user1
argument_list|,
name|user2
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
name|Exception
block|{
name|String
name|group
init|=
name|createGroup
argument_list|(
literal|"group"
argument_list|)
decl_stmt|;
name|String
name|user
init|=
name|createAccount
argument_list|(
literal|"user1"
argument_list|,
name|group
argument_list|)
decl_stmt|;
name|assertMembers
argument_list|(
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
name|members
argument_list|()
argument_list|,
name|user
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
name|String
name|gx
init|=
name|createGroup
argument_list|(
literal|"gx"
argument_list|)
decl_stmt|;
name|String
name|ux
init|=
name|createAccount
argument_list|(
literal|"ux"
argument_list|,
name|gx
argument_list|)
decl_stmt|;
name|String
name|gy
init|=
name|createGroup
argument_list|(
literal|"gy"
argument_list|)
decl_stmt|;
name|String
name|uy
init|=
name|createAccount
argument_list|(
literal|"uy"
argument_list|,
name|gy
argument_list|)
decl_stmt|;
name|String
name|gz
init|=
name|createGroup
argument_list|(
literal|"gz"
argument_list|)
decl_stmt|;
name|String
name|uz
init|=
name|createAccount
argument_list|(
literal|"uz"
argument_list|,
name|gz
argument_list|)
decl_stmt|;
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|gx
argument_list|)
operator|.
name|addGroups
argument_list|(
name|gy
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|gy
argument_list|)
operator|.
name|addGroups
argument_list|(
name|gz
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|gx
argument_list|)
operator|.
name|members
argument_list|()
argument_list|,
name|ux
argument_list|)
expr_stmt|;
name|assertMembers
argument_list|(
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|gx
argument_list|)
operator|.
name|members
argument_list|(
literal|true
argument_list|)
argument_list|,
name|ux
argument_list|,
name|uy
argument_list|,
name|uz
argument_list|)
expr_stmt|;
block|}
DECL|method|createGroup (String name)
specifier|private
name|String
name|createGroup
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|name
operator|=
name|name
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|GroupInput
name|in
init|=
operator|new
name|GroupInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|in
operator|.
name|ownerId
operator|=
literal|"Administrators"
expr_stmt|;
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|create
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
DECL|method|createAccount (String name, String group)
specifier|private
name|String
name|createAccount
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|group
parameter_list|)
throws|throws
name|Exception
block|{
name|name
operator|=
name|name
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|accounts
operator|.
name|create
argument_list|(
name|name
argument_list|,
name|group
argument_list|)
expr_stmt|;
return|return
name|name
return|;
block|}
DECL|method|assertMembers (List<AccountInfo> members, String... names)
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
modifier|...
name|names
parameter_list|)
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|memberNames
init|=
name|Iterables
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
name|assertThat
argument_list|(
name|memberNames
argument_list|)
operator|.
name|containsExactlyElementsIn
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

