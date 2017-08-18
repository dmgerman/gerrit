begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|common
operator|.
name|PageLinks
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Utility class for building GroupDescription objects. */
end_comment

begin_class
DECL|class|GroupDescriptions
specifier|public
class|class
name|GroupDescriptions
block|{
DECL|method|forAccountGroup (AccountGroup group)
specifier|public
specifier|static
name|GroupDescription
operator|.
name|Internal
name|forAccountGroup
parameter_list|(
name|AccountGroup
name|group
parameter_list|)
block|{
return|return
operator|new
name|GroupDescription
operator|.
name|Internal
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|group
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|group
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
literal|"#"
operator|+
name|PageLinks
operator|.
name|toGroup
argument_list|(
name|getGroupUUID
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|group
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|group
operator|.
name|getDescription
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getOwnerGroupUUID
parameter_list|()
block|{
return|return
name|group
operator|.
name|getOwnerGroupUUID
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isVisibleToAll
parameter_list|()
block|{
return|return
name|group
operator|.
name|isVisibleToAll
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Timestamp
name|getCreatedOn
parameter_list|()
block|{
return|return
name|group
operator|.
name|getCreatedOn
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|GroupDescriptions ()
specifier|private
name|GroupDescriptions
parameter_list|()
block|{}
block|}
end_class

end_unit

