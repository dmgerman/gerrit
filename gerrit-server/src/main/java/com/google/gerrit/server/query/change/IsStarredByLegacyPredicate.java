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
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|query
operator|.
name|OrPredicate
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
name|query
operator|.
name|Predicate
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
annotation|@
name|Deprecated
DECL|class|IsStarredByLegacyPredicate
class|class
name|IsStarredByLegacyPredicate
extends|extends
name|OrPredicate
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|method|predicates (Set<Change.Id> ids)
specifier|private
specifier|static
name|List
argument_list|<
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|predicates
parameter_list|(
name|Set
argument_list|<
name|Change
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
name|r
init|=
name|Lists
operator|.
name|newArrayListWithCapacity
argument_list|(
name|ids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
operator|.
name|Id
name|id
range|:
name|ids
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|LegacyChangeIdPredicate
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|starredChanges
specifier|private
specifier|final
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starredChanges
decl_stmt|;
DECL|method|IsStarredByLegacyPredicate (Account.Id accountId, Set<Change.Id> starredChanges)
name|IsStarredByLegacyPredicate
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Set
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|starredChanges
parameter_list|)
block|{
name|super
argument_list|(
name|predicates
argument_list|(
name|starredChanges
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|starredChanges
operator|=
name|starredChanges
expr_stmt|;
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
block|{
return|return
name|starredChanges
operator|.
name|contains
argument_list|(
name|object
operator|.
name|getId
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
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ChangeQueryBuilder
operator|.
name|FIELD_STARREDBY
operator|+
literal|":"
operator|+
name|accountId
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

