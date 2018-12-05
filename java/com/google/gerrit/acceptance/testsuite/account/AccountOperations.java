begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
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

begin_comment
comment|/**  * An aggregation of operations on accounts for test purposes.  *  *<p>To execute the operations, no Gerrit permissions are necessary.  *  *<p><strong>Note:</strong> This interface is not implemented using the REST or extension API.  * Hence, it cannot be used for testing those APIs.  */
end_comment

begin_interface
DECL|interface|AccountOperations
specifier|public
interface|interface
name|AccountOperations
block|{
comment|/**    * Starts the fluent chain for a querying or modifying an account. Please see the methods of    * {@link MoreAccountOperations} for details on possible operations.    *    * @return an aggregation of operations on a specific account    */
DECL|method|account (Account.Id accountId)
name|MoreAccountOperations
name|account
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/**    * Starts the fluent chain to create an account. The returned builder can be used to specify the    * attributes of the new account. To create the account for real, {@link    * TestAccountCreation.Builder#create()} must be called.    *    *<p>Example:    *    *<pre>    * Account.Id createdAccountId = accountOperations    *     .newAccount()    *     .username("janedoe")    *     .preferredEmail("janedoe@example.com")    *     .fullname("Jane Doe")    *     .create();    *</pre>    *    *<p><strong>Note:</strong> If another account with the provided user name or preferred email    * address already exists, the creation of the account will fail.    *    * @return a builder to create the new account    */
DECL|method|newAccount ()
name|TestAccountCreation
operator|.
name|Builder
name|newAccount
parameter_list|()
function_decl|;
comment|/** An aggregation of methods on a specific account. */
DECL|interface|MoreAccountOperations
interface|interface
name|MoreAccountOperations
block|{
comment|/**      * Checks whether the account exists.      *      * @return {@code true} if the account exists      */
DECL|method|exists ()
name|boolean
name|exists
parameter_list|()
function_decl|;
comment|/**      * Retrieves the account.      *      *<p><strong>Note:</strong> This call will fail with an exception if the requested account      * doesn't exist. If you want to check for the existence of an account, use {@link #exists()}      * instead.      *      * @return the corresponding {@code TestAccount}      */
DECL|method|get ()
name|TestAccount
name|get
parameter_list|()
function_decl|;
comment|/**      * Starts the fluent chain to update an account. The returned builder can be used to specify how      * the attributes of the account should be modified. To update the account for real, {@link      * TestAccountUpdate.Builder#update()} must be called.      *      *<p>Example:      *      *<pre>      * TestAccount updatedAccount = accountOperations.forUpdate().status("on vacation").update();      *</pre>      *      *<p><strong>Note:</strong> The update will fail with an exception if the account to update      * doesn't exist. If you want to check for the existence of an account, use {@link #exists()}.      *      * @return a builder to update the account      */
DECL|method|forUpdate ()
name|TestAccountUpdate
operator|.
name|Builder
name|forUpdate
parameter_list|()
function_decl|;
block|}
block|}
end_interface

end_unit

