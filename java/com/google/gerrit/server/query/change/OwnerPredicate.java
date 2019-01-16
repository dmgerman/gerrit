begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|exceptions
operator|.
name|StorageException
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
name|index
operator|.
name|change
operator|.
name|ChangeField
import|;
end_import

begin_class
DECL|class|OwnerPredicate
specifier|public
class|class
name|OwnerPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|field|id
specifier|protected
specifier|final
name|Account
operator|.
name|Id
name|id
decl_stmt|;
DECL|method|OwnerPredicate (Account.Id id)
specifier|public
name|OwnerPredicate
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|OWNER
argument_list|,
name|id
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getAccountId ()
specifier|protected
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|id
return|;
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
throws|throws
name|StorageException
block|{
name|Change
name|change
init|=
name|object
operator|.
name|change
argument_list|()
decl_stmt|;
return|return
name|change
operator|!=
literal|null
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|change
operator|.
name|getOwner
argument_list|()
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
literal|1
return|;
block|}
block|}
end_class

end_unit

