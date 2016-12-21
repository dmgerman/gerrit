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
name|AccountExternalId
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

begin_comment
comment|/** Caches external ids of all accounts */
end_comment

begin_interface
DECL|interface|ExternalIdCache
specifier|public
interface|interface
name|ExternalIdCache
block|{
DECL|method|onCreate (Iterable<AccountExternalId> extId)
name|void
name|onCreate
parameter_list|(
name|Iterable
argument_list|<
name|AccountExternalId
argument_list|>
name|extId
parameter_list|)
function_decl|;
DECL|method|onRemove (Iterable<AccountExternalId> extId)
name|void
name|onRemove
parameter_list|(
name|Iterable
argument_list|<
name|AccountExternalId
argument_list|>
name|extId
parameter_list|)
function_decl|;
DECL|method|onRemove (Account.Id accountId, Iterable<AccountExternalId.Key> extIdKeys)
name|void
name|onRemove
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|Iterable
argument_list|<
name|AccountExternalId
operator|.
name|Key
argument_list|>
name|extIdKeys
parameter_list|)
function_decl|;
DECL|method|onUpdate (AccountExternalId extId)
name|void
name|onUpdate
parameter_list|(
name|AccountExternalId
name|extId
parameter_list|)
function_decl|;
DECL|method|byAccount (Account.Id accountId)
name|Collection
argument_list|<
name|AccountExternalId
argument_list|>
name|byAccount
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
DECL|method|onCreate (AccountExternalId extId)
specifier|default
name|void
name|onCreate
parameter_list|(
name|AccountExternalId
name|extId
parameter_list|)
block|{
name|onCreate
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extId
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|onRemove (AccountExternalId extId)
specifier|default
name|void
name|onRemove
parameter_list|(
name|AccountExternalId
name|extId
parameter_list|)
block|{
name|onRemove
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|extId
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|onRemove (Account.Id accountId, AccountExternalId.Key extIdKey)
specifier|default
name|void
name|onRemove
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|AccountExternalId
operator|.
name|Key
name|extIdKey
parameter_list|)
block|{
name|onRemove
argument_list|(
name|accountId
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|extIdKey
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_interface

end_unit

