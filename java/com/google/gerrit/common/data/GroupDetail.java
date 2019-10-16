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
name|entities
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
name|entities
operator|.
name|AccountGroup
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
DECL|class|GroupDetail
specifier|public
class|class
name|GroupDetail
block|{
DECL|field|members
specifier|private
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|members
decl_stmt|;
DECL|field|includes
specifier|private
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|includes
decl_stmt|;
DECL|method|GroupDetail (Set<Account.Id> members, Set<AccountGroup.UUID> includes)
specifier|public
name|GroupDetail
parameter_list|(
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|members
parameter_list|,
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|includes
parameter_list|)
block|{
name|this
operator|.
name|members
operator|=
name|members
expr_stmt|;
name|this
operator|.
name|includes
operator|=
name|includes
expr_stmt|;
block|}
DECL|method|getMembers ()
specifier|public
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getMembers
parameter_list|()
block|{
return|return
name|members
return|;
block|}
DECL|method|getIncludes ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
block|}
end_class

end_unit

