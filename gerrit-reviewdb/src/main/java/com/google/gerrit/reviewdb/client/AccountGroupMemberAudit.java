begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|gwtorm
operator|.
name|client
operator|.
name|Column
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
name|client
operator|.
name|CompoundKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Membership of an {@link Account} in an {@link AccountGroup}. */
end_comment

begin_class
DECL|class|AccountGroupMemberAudit
specifier|public
specifier|final
class|class
name|AccountGroupMemberAudit
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|accountId
specifier|protected
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|groupId
specifier|protected
name|AccountGroup
operator|.
name|Id
name|groupId
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|)
DECL|field|addedOn
specifier|protected
name|Timestamp
name|addedOn
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|accountId
operator|=
operator|new
name|Account
operator|.
name|Id
argument_list|()
expr_stmt|;
name|groupId
operator|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (final Account.Id a, final AccountGroup.Id g, final Timestamp t)
specifier|public
name|Key
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|a
parameter_list|,
specifier|final
name|AccountGroup
operator|.
name|Id
name|g
parameter_list|,
specifier|final
name|Timestamp
name|t
parameter_list|)
block|{
name|accountId
operator|=
name|a
expr_stmt|;
name|groupId
operator|=
name|g
expr_stmt|;
name|addedOn
operator|=
name|t
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|Account
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|getGroupId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getGroupId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
DECL|method|getAddedOn ()
specifier|public
name|Timestamp
name|getAddedOn
parameter_list|()
block|{
return|return
name|addedOn
return|;
block|}
annotation|@
name|Override
DECL|method|members ()
specifier|public
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
name|members
parameter_list|()
block|{
return|return
operator|new
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|groupId
block|}
empty_stmt|;
block|}
block|}
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|,
name|name
operator|=
name|Column
operator|.
name|NONE
argument_list|)
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|2
argument_list|)
DECL|field|addedBy
specifier|protected
name|Account
operator|.
name|Id
name|addedBy
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|3
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|removedBy
specifier|protected
name|Account
operator|.
name|Id
name|removedBy
decl_stmt|;
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|4
argument_list|,
name|notNull
operator|=
literal|false
argument_list|)
DECL|field|removedOn
specifier|protected
name|Timestamp
name|removedOn
decl_stmt|;
DECL|method|AccountGroupMemberAudit ()
specifier|protected
name|AccountGroupMemberAudit
parameter_list|()
block|{}
DECL|method|AccountGroupMemberAudit ( final AccountGroupMember m, final Account.Id adder, Timestamp addedOn)
specifier|public
name|AccountGroupMemberAudit
parameter_list|(
specifier|final
name|AccountGroupMember
name|m
parameter_list|,
specifier|final
name|Account
operator|.
name|Id
name|adder
parameter_list|,
name|Timestamp
name|addedOn
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|who
init|=
name|m
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
specifier|final
name|AccountGroup
operator|.
name|Id
name|group
init|=
name|m
operator|.
name|getAccountGroupId
argument_list|()
decl_stmt|;
name|key
operator|=
operator|new
name|AccountGroupMemberAudit
operator|.
name|Key
argument_list|(
name|who
argument_list|,
name|group
argument_list|,
name|addedOn
argument_list|)
expr_stmt|;
name|addedBy
operator|=
name|adder
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|AccountGroupMemberAudit
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|isActive ()
specifier|public
name|boolean
name|isActive
parameter_list|()
block|{
return|return
name|removedOn
operator|==
literal|null
return|;
block|}
DECL|method|removed (final Account.Id deleter, final Timestamp when)
specifier|public
name|void
name|removed
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|deleter
parameter_list|,
specifier|final
name|Timestamp
name|when
parameter_list|)
block|{
name|removedBy
operator|=
name|deleter
expr_stmt|;
name|removedOn
operator|=
name|when
expr_stmt|;
block|}
DECL|method|removedLegacy ()
specifier|public
name|void
name|removedLegacy
parameter_list|()
block|{
name|removedBy
operator|=
name|addedBy
expr_stmt|;
name|removedOn
operator|=
name|key
operator|.
name|addedOn
expr_stmt|;
block|}
DECL|method|getAddedBy ()
specifier|public
name|Account
operator|.
name|Id
name|getAddedBy
parameter_list|()
block|{
return|return
name|addedBy
return|;
block|}
DECL|method|getRemovedBy ()
specifier|public
name|Account
operator|.
name|Id
name|getRemovedBy
parameter_list|()
block|{
return|return
name|removedBy
return|;
block|}
DECL|method|getRemovedOn ()
specifier|public
name|Timestamp
name|getRemovedOn
parameter_list|()
block|{
return|return
name|removedOn
return|;
block|}
block|}
end_class

end_unit

