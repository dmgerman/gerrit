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
name|entities
operator|.
name|Account
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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

begin_comment
comment|/** Caches important (but small) account state to avoid database hits. */
end_comment

begin_interface
DECL|interface|AccountCache
specifier|public
interface|interface
name|AccountCache
block|{
comment|/**    * Returns an {@code AccountState} instance for the given account ID. If not cached yet the    * account is loaded. Returns {@link Optional#empty()} if the account is missing.    *    * @param accountId ID of the account that should be retrieved    * @return {@code AccountState} instance for the given account ID, if no account with this ID    *     exists {@link Optional#empty()} is returned    */
DECL|method|get (Account.Id accountId)
name|Optional
argument_list|<
name|AccountState
argument_list|>
name|get
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/**    * Returns a {@code Map} of {@code Account.Id} to {@code AccountState} for the given account IDs.    * If not cached yet the accounts are loaded. If an account can't be loaded (e.g. because it is    * missing), the entry will be missing from the result.    *    *<p>Loads accounts in parallel if applicable.    *    * @param accountIds IDs of the account that should be retrieved    * @return {@code Map} of {@code Account.Id} to {@code AccountState} instances for the given    *     account IDs, if an account can't be loaded (e.g. because it is missing), the entry will be    *     missing from the result    */
DECL|method|get (Set<Account.Id> accountIds)
name|Map
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|AccountState
argument_list|>
name|get
parameter_list|(
name|Set
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|accountIds
parameter_list|)
function_decl|;
comment|/**    * Returns an {@code AccountState} instance for the given account ID. If not cached yet the    * account is loaded. Returns an empty {@code AccountState} instance to represent a missing    * account.    *    *<p>This method should only be used in exceptional cases where it is required to get an account    * state even if the account is missing. Callers should leave a comment with the method invocation    * explaining why this method is used. Most callers of {@link AccountCache} should use {@link    * #get(Account.Id)} instead and handle the missing account case explicitly.    *    * @param accountId ID of the account that should be retrieved    * @return {@code AccountState} instance for the given account ID, if no account with this ID    *     exists an empty {@code AccountState} instance is returned to represent the missing account    */
DECL|method|getEvenIfMissing (Account.Id accountId)
name|AccountState
name|getEvenIfMissing
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/**    * Returns an {@code AccountState} instance for the given username.    *    *<p>This method first loads the external ID for the username and then uses the account ID of the    * external ID to lookup the account from the cache.    *    * @param username username of the account that should be retrieved    * @return {@code AccountState} instance for the given username, if no account with this username    *     exists or if loading the external ID fails {@link Optional#empty()} is returned    */
DECL|method|getByUsername (String username)
name|Optional
argument_list|<
name|AccountState
argument_list|>
name|getByUsername
parameter_list|(
name|String
name|username
parameter_list|)
function_decl|;
comment|/**    * Evicts the account from the cache.    *    * @param accountId account ID of the account that should be evicted    */
DECL|method|evict (@ullable Account.Id accountId)
name|void
name|evict
parameter_list|(
annotation|@
name|Nullable
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/** Evict all accounts from the cache. */
DECL|method|evictAll ()
name|void
name|evictAll
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

