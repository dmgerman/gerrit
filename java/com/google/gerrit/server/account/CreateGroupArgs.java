begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|CreateGroupArgs
specifier|public
class|class
name|CreateGroupArgs
block|{
DECL|field|groupName
specifier|private
name|AccountGroup
operator|.
name|NameKey
name|groupName
decl_stmt|;
DECL|field|groupDescription
specifier|public
name|String
name|groupDescription
decl_stmt|;
DECL|field|visibleToAll
specifier|public
name|boolean
name|visibleToAll
decl_stmt|;
DECL|field|ownerGroupUuid
specifier|public
name|AccountGroup
operator|.
name|UUID
name|ownerGroupUuid
decl_stmt|;
DECL|field|initialMembers
specifier|public
name|Collection
argument_list|<
name|?
extends|extends
name|Account
operator|.
name|Id
argument_list|>
name|initialMembers
decl_stmt|;
DECL|method|getGroup ()
specifier|public
name|AccountGroup
operator|.
name|NameKey
name|getGroup
parameter_list|()
block|{
return|return
name|groupName
return|;
block|}
DECL|method|getGroupName ()
specifier|public
name|String
name|getGroupName
parameter_list|()
block|{
return|return
name|groupName
operator|!=
literal|null
condition|?
name|groupName
operator|.
name|get
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|setGroupName (String n)
specifier|public
name|void
name|setGroupName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|groupName
operator|=
name|n
operator|!=
literal|null
condition|?
name|AccountGroup
operator|.
name|nameKey
argument_list|(
name|n
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
DECL|method|setGroupName (AccountGroup.NameKey n)
specifier|public
name|void
name|setGroupName
parameter_list|(
name|AccountGroup
operator|.
name|NameKey
name|n
parameter_list|)
block|{
name|groupName
operator|=
name|n
expr_stmt|;
block|}
block|}
end_class

end_unit

