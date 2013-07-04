begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|ChangeUtil
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
name|ChangeIndex
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
name|IndexCollection
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
name|Schema
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
name|IntPredicate
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
name|QueryRewriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|OutOfScopeException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_class
DECL|class|BasicChangeRewrites
specifier|public
specifier|abstract
class|class
name|BasicChangeRewrites
extends|extends
name|QueryRewriter
argument_list|<
name|ChangeData
argument_list|>
block|{
DECL|field|BUILDER
specifier|protected
specifier|static
specifier|final
name|ChangeQueryBuilder
name|BUILDER
init|=
operator|new
name|ChangeQueryBuilder
argument_list|(
operator|new
name|ChangeQueryBuilder
operator|.
name|Arguments
argument_list|(
comment|//
operator|new
name|InvalidProvider
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
argument_list|,
comment|//
operator|new
name|InvalidProvider
argument_list|<
name|ChangeQueryRewriter
argument_list|>
argument_list|()
argument_list|,
comment|//
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
comment|//
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
DECL|method|schema (@ullable IndexCollection indexes)
specifier|static
name|Schema
argument_list|<
name|ChangeData
argument_list|>
name|schema
parameter_list|(
annotation|@
name|Nullable
name|IndexCollection
name|indexes
parameter_list|)
block|{
name|ChangeIndex
name|index
init|=
name|indexes
operator|!=
literal|null
condition|?
name|indexes
operator|.
name|getSearchIndex
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|index
operator|!=
literal|null
condition|?
name|index
operator|.
name|getSchema
argument_list|()
else|:
literal|null
return|;
block|}
DECL|field|dbProvider
specifier|protected
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|IndexCollection
name|indexes
decl_stmt|;
DECL|method|BasicChangeRewrites ( Definition<ChangeData, ? extends QueryRewriter<ChangeData>> def, Provider<ReviewDb> dbProvider, IndexCollection indexes)
specifier|protected
name|BasicChangeRewrites
parameter_list|(
name|Definition
argument_list|<
name|ChangeData
argument_list|,
name|?
extends|extends
name|QueryRewriter
argument_list|<
name|ChangeData
argument_list|>
argument_list|>
name|def
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|IndexCollection
name|indexes
parameter_list|)
block|{
name|super
argument_list|(
name|def
argument_list|)
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
block|}
annotation|@
name|Rewrite
argument_list|(
literal|"-status:open"
argument_list|)
annotation|@
name|NoCostComputation
DECL|method|r00_notOpen ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_notOpen
parameter_list|()
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|closed
argument_list|(
name|dbProvider
argument_list|)
return|;
block|}
annotation|@
name|Rewrite
argument_list|(
literal|"-status:closed"
argument_list|)
annotation|@
name|NoCostComputation
DECL|method|r00_notClosed ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_notClosed
parameter_list|()
block|{
return|return
name|ChangeStatusPredicate
operator|.
name|open
argument_list|(
name|dbProvider
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"-status:merged"
argument_list|)
DECL|method|r00_notMerged ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_notMerged
parameter_list|()
block|{
return|return
name|or
argument_list|(
name|ChangeStatusPredicate
operator|.
name|open
argument_list|(
name|dbProvider
argument_list|)
argument_list|,
operator|new
name|ChangeStatusPredicate
argument_list|(
name|dbProvider
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"-status:abandoned"
argument_list|)
DECL|method|r00_notAbandoned ()
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_notAbandoned
parameter_list|()
block|{
return|return
name|or
argument_list|(
name|ChangeStatusPredicate
operator|.
name|open
argument_list|(
name|dbProvider
argument_list|)
argument_list|,
operator|new
name|ChangeStatusPredicate
argument_list|(
name|dbProvider
argument_list|,
name|Change
operator|.
name|Status
operator|.
name|MERGED
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"sortkey_before:z A=(age:*)"
argument_list|)
DECL|method|r00_ageToSortKey (@amedR) AgePredicate a)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_ageToSortKey
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"A"
argument_list|)
name|AgePredicate
name|a
parameter_list|)
block|{
name|String
name|cut
init|=
name|ChangeUtil
operator|.
name|sortKey
argument_list|(
name|a
operator|.
name|getCut
argument_list|()
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
decl_stmt|;
return|return
name|and
argument_list|(
operator|new
name|SortKeyPredicate
operator|.
name|Before
argument_list|(
name|schema
argument_list|(
name|indexes
argument_list|)
argument_list|,
name|dbProvider
argument_list|,
name|cut
argument_list|)
argument_list|,
name|a
argument_list|)
return|;
block|}
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"A=(limit:*) B=(limit:*)"
argument_list|)
DECL|method|r00_smallestLimit ( @amedR) IntPredicate<ChangeData> a, @Named(R) IntPredicate<ChangeData> b)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_smallestLimit
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"A"
argument_list|)
name|IntPredicate
argument_list|<
name|ChangeData
argument_list|>
name|a
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"B"
argument_list|)
name|IntPredicate
argument_list|<
name|ChangeData
argument_list|>
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|intValue
argument_list|()
operator|<=
name|b
operator|.
name|intValue
argument_list|()
condition|?
name|a
else|:
name|b
return|;
block|}
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"A=(sortkey_before:*) B=(sortkey_before:*)"
argument_list|)
DECL|method|r00_oldestSortKey ( @amedR) SortKeyPredicate.Before a, @Named(R) SortKeyPredicate.Before b)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_oldestSortKey
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"A"
argument_list|)
name|SortKeyPredicate
operator|.
name|Before
name|a
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"B"
argument_list|)
name|SortKeyPredicate
operator|.
name|Before
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getValue
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
operator|<=
literal|0
condition|?
name|a
else|:
name|b
return|;
block|}
annotation|@
name|NoCostComputation
annotation|@
name|Rewrite
argument_list|(
literal|"A=(sortkey_after:*) B=(sortkey_after:*)"
argument_list|)
DECL|method|r00_newestSortKey ( @amedR) SortKeyPredicate.After a, @Named(R) SortKeyPredicate.After b)
specifier|public
name|Predicate
argument_list|<
name|ChangeData
argument_list|>
name|r00_newestSortKey
parameter_list|(
annotation|@
name|Named
argument_list|(
literal|"A"
argument_list|)
name|SortKeyPredicate
operator|.
name|After
name|a
parameter_list|,
annotation|@
name|Named
argument_list|(
literal|"B"
argument_list|)
name|SortKeyPredicate
operator|.
name|After
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|getValue
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|getValue
argument_list|()
argument_list|)
operator|>=
literal|0
condition|?
name|a
else|:
name|b
return|;
block|}
DECL|class|InvalidProvider
specifier|private
specifier|static
specifier|final
class|class
name|InvalidProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|Provider
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|T
name|get
parameter_list|()
block|{
throw|throw
operator|new
name|OutOfScopeException
argument_list|(
literal|"Not available at init"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

