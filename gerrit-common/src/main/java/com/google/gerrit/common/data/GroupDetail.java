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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupById
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
DECL|field|members
specifier|public
name|List
argument_list|<
name|AccountGroupMember
argument_list|>
name|members
decl_stmt|;
DECL|field|includes
specifier|public
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|includes
decl_stmt|;
DECL|method|GroupDetail ()
specifier|public
name|GroupDetail
parameter_list|()
block|{}
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
DECL|method|setIncludes (List<AccountGroupById> i)
specifier|public
name|void
name|setIncludes
parameter_list|(
name|List
argument_list|<
name|AccountGroupById
argument_list|>
name|i
parameter_list|)
block|{
name|includes
operator|=
name|i
expr_stmt|;
block|}
block|}
end_class

end_unit

