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
name|common
operator|.
name|collect
operator|.
name|Sets
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

begin_comment
comment|/** GroupMembership over an explicit list. */
end_comment

begin_class
DECL|class|ListGroupMembership
specifier|public
class|class
name|ListGroupMembership
implements|implements
name|GroupMembership
block|{
DECL|field|groups
specifier|private
specifier|final
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groups
decl_stmt|;
DECL|method|ListGroupMembership (Iterable<AccountGroup.UUID> groupIds)
specifier|public
name|ListGroupMembership
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
block|{
name|this
operator|.
name|groups
operator|=
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|groupIds
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|contains (AccountGroup.UUID groupId)
specifier|public
name|boolean
name|contains
parameter_list|(
name|AccountGroup
operator|.
name|UUID
name|groupId
parameter_list|)
block|{
return|return
name|groups
operator|.
name|contains
argument_list|(
name|groupId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|containsAnyOf (Iterable<AccountGroup.UUID> groupIds)
specifier|public
name|boolean
name|containsAnyOf
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
block|{
for|for
control|(
name|AccountGroup
operator|.
name|UUID
name|groupId
range|:
name|groupIds
control|)
block|{
if|if
condition|(
name|contains
argument_list|(
name|groupId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|intersection (Iterable<AccountGroup.UUID> groupIds)
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|intersection
parameter_list|(
name|Iterable
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
parameter_list|)
block|{
return|return
name|Sets
operator|.
name|intersection
argument_list|(
name|ImmutableSet
operator|.
name|copyOf
argument_list|(
name|groupIds
argument_list|)
argument_list|,
name|groups
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getKnownGroups ()
specifier|public
name|Set
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|getKnownGroups
parameter_list|()
block|{
return|return
name|Sets
operator|.
name|newHashSet
argument_list|(
name|groups
argument_list|)
return|;
block|}
block|}
end_class

end_unit

