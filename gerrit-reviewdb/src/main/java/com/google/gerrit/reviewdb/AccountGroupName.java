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
DECL|package|com.google.gerrit.reviewdb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
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

begin_comment
comment|/** Unique name of an {@link AccountGroup}. */
end_comment

begin_class
DECL|class|AccountGroupName
specifier|public
class|class
name|AccountGroupName
block|{
annotation|@
name|Column
argument_list|(
name|id
operator|=
literal|1
argument_list|)
DECL|field|name
specifier|protected
name|AccountGroup
operator|.
name|NameKey
name|name
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
DECL|method|AccountGroupName ()
specifier|protected
name|AccountGroupName
parameter_list|()
block|{   }
DECL|method|AccountGroupName (AccountGroup.NameKey name, AccountGroup.Id groupId)
specifier|public
name|AccountGroupName
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|name
parameter_list|,
name|AccountGroup
operator|.
name|Id
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
block|}
DECL|method|AccountGroupName (AccountGroup group)
specifier|public
name|AccountGroupName
parameter_list|(
name|AccountGroup
name|group
parameter_list|)
block|{
name|this
argument_list|(
name|group
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getNameKey ()
specifier|public
name|AccountGroup
operator|.
name|NameKey
name|getNameKey
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|groupId
return|;
block|}
DECL|method|setId (AccountGroup.Id id)
specifier|public
name|void
name|setId
parameter_list|(
name|AccountGroup
operator|.
name|Id
name|id
parameter_list|)
block|{
name|groupId
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

