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
name|mail
operator|.
name|Address
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
DECL|class|ReviewerByEmailPredicate
class|class
name|ReviewerByEmailPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|method|forState (Address adr, ReviewerStateInternal state)
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|forState
parameter_list|(
name|Address
name|adr
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
name|ReviewerByEmailPredicate
argument_list|(
name|state
argument_list|,
name|adr
argument_list|)
return|;
block|}
DECL|field|state
specifier|private
specifier|final
name|ReviewerStateInternal
name|state
decl_stmt|;
DECL|field|adr
specifier|private
specifier|final
name|Address
name|adr
decl_stmt|;
DECL|method|ReviewerByEmailPredicate (ReviewerStateInternal state, Address adr)
specifier|private
name|ReviewerByEmailPredicate
parameter_list|(
name|ReviewerStateInternal
name|state
parameter_list|,
name|Address
name|adr
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|REVIEWER_BY_EMAIL
argument_list|,
name|ChangeField
operator|.
name|getReviewerByEmailFieldValue
argument_list|(
name|state
argument_list|,
name|adr
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
name|adr
operator|=
name|adr
expr_stmt|;
block|}
DECL|method|getAddress ()
name|Address
name|getAddress
parameter_list|()
block|{
return|return
name|adr
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
block|{
return|return
name|cd
operator|.
name|reviewersByEmail
argument_list|()
operator|.
name|asTable
argument_list|()
operator|.
name|get
argument_list|(
name|state
argument_list|,
name|adr
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

