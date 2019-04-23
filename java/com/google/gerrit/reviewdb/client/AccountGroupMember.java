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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/** Membership of an {@link Account} in an {@link AccountGroup}. */
end_comment

begin_class
DECL|class|AccountGroupMember
specifier|public
specifier|final
class|class
name|AccountGroupMember
block|{
DECL|method|key (Account.Id accountId, AccountGroup.Id groupId)
specifier|public
specifier|static
name|Key
name|key
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_AccountGroupMember_Key
argument_list|(
name|accountId
argument_list|,
name|groupId
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
block|{
DECL|method|accountId ()
specifier|public
specifier|abstract
name|Account
operator|.
name|Id
name|accountId
parameter_list|()
function_decl|;
DECL|method|groupId ()
specifier|public
specifier|abstract
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|()
function_decl|;
block|}
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
DECL|method|AccountGroupMember ()
specifier|protected
name|AccountGroupMember
parameter_list|()
block|{}
DECL|method|AccountGroupMember (AccountGroupMember.Key k)
specifier|public
name|AccountGroupMember
parameter_list|(
name|AccountGroupMember
operator|.
name|Key
name|k
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|AccountGroupMember
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|key
operator|.
name|accountId
argument_list|()
return|;
block|}
DECL|method|getAccountGroupId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getAccountGroupId
parameter_list|()
block|{
return|return
name|key
operator|.
name|groupId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
operator|(
name|o
operator|instanceof
name|AccountGroupMember
operator|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|key
argument_list|,
operator|(
operator|(
name|AccountGroupMember
operator|)
name|o
operator|)
operator|.
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|key
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"{key="
operator|+
name|key
operator|+
literal|"}"
return|;
block|}
block|}
end_class

end_unit

