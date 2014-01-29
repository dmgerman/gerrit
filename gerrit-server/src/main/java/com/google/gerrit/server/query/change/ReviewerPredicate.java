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
name|ChangeField
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
name|IndexPredicate
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
name|server
operator|.
name|OrmException
import|;
end_import

begin_class
DECL|class|ReviewerPredicate
class|class
name|ReviewerPredicate
extends|extends
name|IndexPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|id
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|allowDrafts
specifier|private
name|boolean
name|allowDrafts
decl_stmt|;
DECL|method|ReviewerPredicate (Account.Id id, boolean allowDrafts)
name|ReviewerPredicate
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|boolean
name|allowDrafts
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|REVIEWER
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
name|this
operator|.
name|allowDrafts
operator|=
name|allowDrafts
expr_stmt|;
block|}
DECL|method|getAccountId ()
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
DECL|method|match (final ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
specifier|final
name|ChangeData
name|object
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|allowDrafts
operator|&&
name|object
operator|.
name|change
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|Change
operator|.
name|Status
operator|.
name|DRAFT
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|Account
operator|.
name|Id
name|accountId
range|:
name|object
operator|.
name|reviewers
argument_list|()
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|accountId
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

