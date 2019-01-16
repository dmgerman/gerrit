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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|index
operator|.
name|query
operator|.
name|Predicate
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
name|server
operator|.
name|index
operator|.
name|change
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
name|notedb
operator|.
name|ReviewerStateInternal
import|;
end_import

begin_class
DECL|class|ReviewerPredicate
specifier|public
class|class
name|ReviewerPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|forState (Account.Id id, ReviewerStateInternal state)
specifier|protected
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|forState
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|,
name|ReviewerStateInternal
name|state
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|state
operator|!=
name|ReviewerStateInternal
operator|.
name|REMOVED
argument_list|,
literal|"can't query by removed reviewer"
argument_list|)
expr_stmt|;
return|return
operator|new
name|ReviewerPredicate
argument_list|(
name|state
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|reviewer (Account.Id id)
specifier|protected
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|reviewer
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|ReviewerPredicate
argument_list|(
name|ReviewerStateInternal
operator|.
name|REVIEWER
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|cc (Account.Id id)
specifier|protected
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|cc
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
operator|new
name|ReviewerPredicate
argument_list|(
name|ReviewerStateInternal
operator|.
name|CC
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|field|state
specifier|protected
specifier|final
name|ReviewerStateInternal
name|state
decl_stmt|;
DECL|field|id
specifier|protected
specifier|final
name|Account
operator|.
name|Id
name|id
decl_stmt|;
DECL|method|ReviewerPredicate (ReviewerStateInternal state, Account.Id id)
specifier|private
name|ReviewerPredicate
parameter_list|(
name|ReviewerStateInternal
name|state
parameter_list|,
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
name|REVIEWER
argument_list|,
name|ChangeField
operator|.
name|getReviewerFieldValue
argument_list|(
name|state
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|state
operator|=
name|state
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
DECL|method|match (ChangeData cd)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
throws|throws
name|StorageException
block|{
return|return
name|cd
operator|.
name|reviewers
argument_list|()
operator|.
name|asTable
argument_list|()
operator|.
name|get
argument_list|(
name|state
argument_list|,
name|id
argument_list|)
operator|!=
literal|null
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

