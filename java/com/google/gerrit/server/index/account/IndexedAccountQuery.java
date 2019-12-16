begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.account
package|package
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
name|account
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
name|checkState
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
name|index
operator|.
name|Index
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
name|QueryOptions
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
name|DataSource
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
name|IndexedQuery
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
name|Matchable
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
name|index
operator|.
name|query
operator|.
name|QueryParseException
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
name|account
operator|.
name|AccountState
import|;
end_import

begin_comment
comment|/**  * Wrapper around {@link Predicate}s that is returned by the {@link  * com.google.gerrit.index.IndexRewriter}. See {@link IndexedQuery}.  */
end_comment

begin_class
DECL|class|IndexedAccountQuery
specifier|public
class|class
name|IndexedAccountQuery
extends|extends
name|IndexedQuery
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
implements|implements
name|DataSource
argument_list|<
name|AccountState
argument_list|>
implements|,
name|Matchable
argument_list|<
name|AccountState
argument_list|>
block|{
DECL|method|IndexedAccountQuery ( Index<Account.Id, AccountState> index, Predicate<AccountState> pred, QueryOptions opts)
specifier|public
name|IndexedAccountQuery
parameter_list|(
name|Index
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|index
parameter_list|,
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|pred
parameter_list|,
name|QueryOptions
name|opts
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|super
argument_list|(
name|index
argument_list|,
name|pred
argument_list|,
name|opts
operator|.
name|convertForBackend
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (AccountState accountState)
specifier|public
name|boolean
name|match
parameter_list|(
name|AccountState
name|accountState
parameter_list|)
block|{
name|Predicate
argument_list|<
name|AccountState
argument_list|>
name|pred
init|=
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|checkState
argument_list|(
name|pred
operator|.
name|isMatchable
argument_list|()
argument_list|,
literal|"match invoked, but child predicate %s doesn't implement %s"
argument_list|,
name|pred
argument_list|,
name|Matchable
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|pred
operator|.
name|asMatchable
argument_list|()
operator|.
name|match
argument_list|(
name|accountState
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

