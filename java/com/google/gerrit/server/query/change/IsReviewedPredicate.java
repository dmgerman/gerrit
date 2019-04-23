begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|change
operator|.
name|ChangeField
operator|.
name|REVIEWEDBY
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_class
DECL|class|IsReviewedPredicate
specifier|public
class|class
name|IsReviewedPredicate
extends|extends
name|ChangeIndexPredicate
block|{
DECL|field|NOT_REVIEWED
specifier|protected
specifier|static
specifier|final
name|Account
operator|.
name|Id
name|NOT_REVIEWED
init|=
name|Account
operator|.
name|id
argument_list|(
name|ChangeField
operator|.
name|NOT_REVIEWED
argument_list|)
decl_stmt|;
DECL|method|create ()
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|create
parameter_list|()
block|{
return|return
name|Predicate
operator|.
name|not
argument_list|(
operator|new
name|IsReviewedPredicate
argument_list|(
name|NOT_REVIEWED
argument_list|)
argument_list|)
return|;
block|}
DECL|method|create (Collection<Account.Id> ids)
specifier|public
specifier|static
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|create
parameter_list|(
name|Collection
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|)
block|{
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|predicates
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Account
operator|.
name|Id
name|id
range|:
name|ids
control|)
block|{
name|predicates
operator|.
name|add
argument_list|(
operator|new
name|IsReviewedPredicate
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|Predicate
operator|.
name|or
argument_list|(
name|predicates
argument_list|)
return|;
block|}
DECL|field|id
specifier|protected
specifier|final
name|Account
operator|.
name|Id
name|id
decl_stmt|;
DECL|method|IsReviewedPredicate (Account.Id id)
specifier|private
name|IsReviewedPredicate
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|REVIEWEDBY
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
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
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|reviewedBy
init|=
name|cd
operator|.
name|reviewedBy
argument_list|()
decl_stmt|;
return|return
operator|!
name|reviewedBy
operator|.
name|isEmpty
argument_list|()
condition|?
name|reviewedBy
operator|.
name|contains
argument_list|(
name|id
argument_list|)
else|:
name|id
operator|.
name|equals
argument_list|(
name|NOT_REVIEWED
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

