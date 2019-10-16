begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.group
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|ImmutableSet
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
name|common
operator|.
name|data
operator|.
name|GroupDescription
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
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_class
DECL|class|InternalGroupDescription
specifier|public
class|class
name|InternalGroupDescription
implements|implements
name|GroupDescription
operator|.
name|Internal
block|{
DECL|field|internalGroup
specifier|private
specifier|final
name|InternalGroup
name|internalGroup
decl_stmt|;
DECL|method|InternalGroupDescription (InternalGroup internalGroup)
specifier|public
name|InternalGroupDescription
parameter_list|(
name|InternalGroup
name|internalGroup
parameter_list|)
block|{
name|this
operator|.
name|internalGroup
operator|=
name|requireNonNull
argument_list|(
name|internalGroup
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getGroupUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getGroupUUID
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getGroupUUID
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Nullable
annotation|@
name|Override
DECL|method|getEmailAddress ()
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
name|Nullable
annotation|@
name|Override
DECL|method|getUrl ()
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
DECL|method|getId ()
specifier|public
name|AccountGroup
operator|.
name|Id
name|getId
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getId
argument_list|()
return|;
block|}
annotation|@
name|Override
annotation|@
name|Nullable
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getDescription
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getOwnerGroupUUID ()
specifier|public
name|AccountGroup
operator|.
name|UUID
name|getOwnerGroupUUID
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getOwnerGroupUUID
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isVisibleToAll ()
specifier|public
name|boolean
name|isVisibleToAll
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|isVisibleToAll
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCreatedOn ()
specifier|public
name|Timestamp
name|getCreatedOn
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getCreatedOn
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMembers ()
specifier|public
name|ImmutableSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|getMembers
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getMembers
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSubgroups ()
specifier|public
name|ImmutableSet
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getSubgroups
parameter_list|()
block|{
return|return
name|internalGroup
operator|.
name|getSubgroups
argument_list|()
return|;
block|}
block|}
end_class

end_unit

