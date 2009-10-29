begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|AccountInfoCache
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
name|client
operator|.
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|AccountGroupMember
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
DECL|class|GroupDetail
specifier|public
class|class
name|GroupDetail
block|{
DECL|field|accounts
specifier|protected
name|AccountInfoCache
name|accounts
decl_stmt|;
DECL|field|group
specifier|protected
name|AccountGroup
name|group
decl_stmt|;
DECL|field|members
specifier|protected
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|members
decl_stmt|;
DECL|field|ownerGroup
specifier|protected
name|AccountGroup
name|ownerGroup
decl_stmt|;
DECL|method|GroupDetail ()
specifier|public
name|GroupDetail
parameter_list|()
block|{   }
DECL|method|setAccounts (AccountInfoCache c)
specifier|public
name|void
name|setAccounts
parameter_list|(
name|AccountInfoCache
name|c
parameter_list|)
block|{
name|accounts
operator|=
name|c
expr_stmt|;
block|}
DECL|method|setGroup (AccountGroup g)
specifier|public
name|void
name|setGroup
parameter_list|(
name|AccountGroup
name|g
parameter_list|)
block|{
name|group
operator|=
name|g
expr_stmt|;
block|}
DECL|method|setMembers (List<AccountGroupMember> m)
specifier|public
name|void
name|setMembers
parameter_list|(
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|m
parameter_list|)
block|{
name|members
operator|=
name|m
expr_stmt|;
block|}
DECL|method|setOwnerGroup (AccountGroup g)
specifier|public
name|void
name|setOwnerGroup
parameter_list|(
name|AccountGroup
name|g
parameter_list|)
block|{
name|ownerGroup
operator|=
name|g
expr_stmt|;
block|}
block|}
end_class

end_unit

