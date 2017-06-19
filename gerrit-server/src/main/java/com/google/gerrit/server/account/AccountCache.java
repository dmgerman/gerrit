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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/** Caches important (but small) account state to avoid database hits. */
end_comment

begin_interface
DECL|interface|AccountCache
specifier|public
interface|interface
name|AccountCache
block|{
comment|/**    * Returns an {@code AccountState} instance for the given account ID. If not cached yet the    * account is loaded. Returns an empty {@code AccountState} instance to represent a missing    * account.    *    * @param accountId ID of the account that should be retrieved    * @return {@code AccountState} instance for the given account ID, if no account with this ID    *     exists an empty {@code AccountState} instance is returned to represent the missing account    */
DECL|method|get (Account.Id accountId)
name|AccountState
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/**    * Returns an {@code AccountState} instance for the given account ID. If not cached yet the    * account is loaded. Returns {@code null} if the account is missing.    *    * @param accountId ID of the account that should be retrieved    * @return {@code AccountState} instance for the given account ID, if no account with this ID    *     exists {@code null} is returned    */
annotation|@
name|Nullable
DECL|method|getOrNull (Account.Id accountId)
name|AccountState
name|getOrNull
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
DECL|method|getByUsername (String username)
name|AccountState
name|getByUsername
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
comment|/**    * Evicts the account from the cache and triggers a reindex for it.    *    * @param accountId account ID of the account that should be evicted    * @throws IOException thrown if reindexing fails    */
DECL|method|evict (Account.Id accountId)
name|void
name|evict
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|evictByUsername (String username)
name|void
name|evictByUsername
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
comment|/** Evict all accounts from the cache, but doesn't trigger reindex of all accounts. */
DECL|method|evictAllNoReindex ()
name|void
name|evictAllNoReindex
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

