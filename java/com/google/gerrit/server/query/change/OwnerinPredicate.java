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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
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
name|index
operator|.
name|query
operator|.
name|PostFilterPredicate
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
name|Change
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
name|server
operator|.
name|IdentifiedUser
import|;
end_import

begin_class
DECL|class|OwnerinPredicate
specifier|public
class|class
name|OwnerinPredicate
extends|extends
name|PostFilterPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|userFactory
specifier|protected
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|uuid
specifier|protected
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
decl_stmt|;
DECL|method|OwnerinPredicate (IdentifiedUser.GenericFactory userFactory, AccountGroup.UUID uuid)
specifier|public
name|OwnerinPredicate
parameter_list|(
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|AccountGroup
operator|.
name|UUID
name|uuid
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeQueryBuilder
operator|.
name|FIELD_OWNERIN
argument_list|,
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|uuid
operator|=
name|uuid
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
specifier|final
name|Change
name|change
init|=
name|object
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
specifier|final
name|IdentifiedUser
name|owner
init|=
name|userFactory
operator|.
name|create
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|owner
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|contains
argument_list|(
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|2
return|;
block|}
block|}
end_class

end_unit

