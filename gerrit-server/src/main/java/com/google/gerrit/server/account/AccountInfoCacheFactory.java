begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|gerrit
operator|.
name|common
operator|.
name|data
operator|.
name|AccountInfo
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
name|data
operator|.
name|AccountInfoCache
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
name|inject
operator|.
name|Inject
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
name|HashMap
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
name|Map
import|;
end_import

begin_comment
comment|/** Efficiently builds an {@link AccountInfoCache}. */
end_comment

begin_class
DECL|class|AccountInfoCacheFactory
specifier|public
class|class
name|AccountInfoCacheFactory
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|AccountInfoCacheFactory
name|create
parameter_list|()
function_decl|;
block|}
DECL|field|accountCache
specifier|private
specifier|final
name|AccountCache
name|accountCache
decl_stmt|;
DECL|field|out
specifier|private
specifier|final
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Account
argument_list|>
name|out
decl_stmt|;
annotation|@
name|Inject
DECL|method|AccountInfoCacheFactory (final AccountCache accountCache)
name|AccountInfoCacheFactory
parameter_list|(
specifier|final
name|AccountCache
name|accountCache
parameter_list|)
block|{
name|this
operator|.
name|accountCache
operator|=
name|accountCache
expr_stmt|;
name|this
operator|.
name|out
operator|=
operator|new
name|HashMap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|Account
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|/**    * Indicate an account will be needed later on.    *    * @param id identity that will be needed in the future; may be null.    */
DECL|method|want (final Account.Id id)
specifier|public
name|void
name|want
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
if|if
condition|(
name|id
operator|!=
literal|null
operator|&&
operator|!
name|out
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|out
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|accountCache
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|getAccount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Indicate one or more accounts will be needed later on. */
DECL|method|want (final Iterable<Account.Id> ids)
specifier|public
name|void
name|want
parameter_list|(
specifier|final
name|Iterable
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
parameter_list|)
block|{
for|for
control|(
specifier|final
name|Account
operator|.
name|Id
name|id
range|:
name|ids
control|)
block|{
name|want
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|get (final Account.Id id)
specifier|public
name|Account
name|get
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
name|want
argument_list|(
name|id
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|get
argument_list|(
name|id
argument_list|)
return|;
block|}
comment|/**    * Create an AccountInfoCache with the currently loaded Account entities.    * */
DECL|method|create ()
specifier|public
name|AccountInfoCache
name|create
parameter_list|()
block|{
specifier|final
name|List
argument_list|<
name|AccountInfo
argument_list|>
name|r
init|=
operator|new
name|ArrayList
argument_list|<
name|AccountInfo
argument_list|>
argument_list|(
name|out
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Account
name|a
range|:
name|out
operator|.
name|values
argument_list|()
control|)
block|{
name|r
operator|.
name|add
argument_list|(
operator|new
name|AccountInfo
argument_list|(
name|a
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|AccountInfoCache
argument_list|(
name|r
argument_list|)
return|;
block|}
block|}
end_class

end_unit

