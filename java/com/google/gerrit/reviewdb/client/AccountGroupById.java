begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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

begin_comment
comment|/** Membership of an {@link AccountGroup} in an {@link AccountGroup}. */
end_comment

begin_class
DECL|class|AccountGroupById
specifier|public
specifier|final
class|class
name|AccountGroupById
block|{
DECL|class|Key
specifier|public
specifier|static
class|class
name|Key
extends|extends
name|CompoundKey
argument_list|<
name|AccountGroup
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
literal|2
argument_list|)
DECL|field|includeUUID
specifier|protected
name|AccountGroup
operator|.
name|UUID
name|includeUUID
decl_stmt|;
DECL|method|Key ()
specifier|protected
name|Key
parameter_list|()
block|{
name|groupId
operator|=
operator|new
name|AccountGroup
operator|.
name|Id
argument_list|()
expr_stmt|;
name|includeUUID
operator|=
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|()
expr_stmt|;
block|}
DECL|method|Key (AccountGroup.Id g, AccountGroup.UUID u)
specifier|public
name|Key
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|g
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|u
parameter_list|)
block|{
name|groupId
operator|=
name|g
expr_stmt|;
name|includeUUID
operator|=
name|u
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getParentKey ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getParentKey
parameter_list|()
block|{
return|return
name|groupId
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
DECL|method|getIncludeUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getIncludeUUID
parameter_list|()
block|{
return|return
name|includeUUID
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
name|includeUUID
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
DECL|method|AccountGroupById ()
specifier|protected
name|AccountGroupById
parameter_list|()
block|{}
DECL|method|AccountGroupById (AccountGroupById.Key k)
specifier|public
name|AccountGroupById
parameter_list|(
name|AccountGroupById
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
name|AccountGroupById
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
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
name|key
operator|.
name|groupId
return|;
block|}
DECL|method|getIncludeUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getIncludeUUID
parameter_list|()
block|{
return|return
name|key
operator|.
name|includeUUID
return|;
block|}
block|}
end_class

end_unit
