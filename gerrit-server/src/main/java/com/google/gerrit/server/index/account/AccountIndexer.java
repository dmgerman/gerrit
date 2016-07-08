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
name|account
operator|.
name|AccountCache
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
name|Index
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
name|assistedinject
operator|.
name|Assisted
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
name|assistedinject
operator|.
name|AssistedInject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Collections
import|;
end_import

begin_class
DECL|class|AccountIndexer
specifier|public
class|class
name|AccountIndexer
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (AccountIndexCollection indexes)
name|AccountIndexer
name|create
parameter_list|(
name|AccountIndexCollection
name|indexes
parameter_list|)
function_decl|;
DECL|method|create (@ullable AccountIndex index)
name|AccountIndexer
name|create
parameter_list|(
annotation|@
name|Nullable
name|AccountIndex
name|index
parameter_list|)
function_decl|;
block|}
DECL|field|byIdCache
specifier|private
specifier|final
name|AccountCache
name|byIdCache
decl_stmt|;
DECL|field|indexes
specifier|private
specifier|final
name|AccountIndexCollection
name|indexes
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|AccountIndex
name|index
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|AccountIndexer (AccountCache byIdCache, @Assisted AccountIndexCollection indexes)
name|AccountIndexer
parameter_list|(
name|AccountCache
name|byIdCache
parameter_list|,
annotation|@
name|Assisted
name|AccountIndexCollection
name|indexes
parameter_list|)
block|{
name|this
operator|.
name|byIdCache
operator|=
name|byIdCache
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
name|indexes
expr_stmt|;
name|this
operator|.
name|index
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|AccountIndexer (AccountCache byIdCache, @Assisted AccountIndex index)
name|AccountIndexer
parameter_list|(
name|AccountCache
name|byIdCache
parameter_list|,
annotation|@
name|Assisted
name|AccountIndex
name|index
parameter_list|)
block|{
name|this
operator|.
name|byIdCache
operator|=
name|byIdCache
expr_stmt|;
name|this
operator|.
name|indexes
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
comment|/**    * Synchronously index an account.    *    * @param id account id to index.    */
DECL|method|index (Account.Id id)
specifier|public
name|void
name|index
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Index
argument_list|<
name|?
argument_list|,
name|AccountState
argument_list|>
name|i
range|:
name|getWriteIndexes
argument_list|()
control|)
block|{
name|i
operator|.
name|replace
argument_list|(
name|byIdCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getWriteIndexes ()
specifier|private
name|Collection
argument_list|<
name|AccountIndex
argument_list|>
name|getWriteIndexes
parameter_list|()
block|{
if|if
condition|(
name|indexes
operator|!=
literal|null
condition|)
block|{
return|return
name|indexes
operator|.
name|getWriteIndexes
argument_list|()
return|;
block|}
return|return
name|index
operator|!=
literal|null
condition|?
name|Collections
operator|.
name|singleton
argument_list|(
name|index
argument_list|)
else|:
name|ImmutableSet
operator|.
expr|<
name|AccountIndex
operator|>
name|of
argument_list|()
return|;
block|}
block|}
end_class

end_unit

