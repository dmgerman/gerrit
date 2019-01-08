begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.testsuite.request
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
name|request
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
name|acceptance
operator|.
name|AcceptanceTestRequestScope
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
name|acceptance
operator|.
name|testsuite
operator|.
name|account
operator|.
name|TestAccount
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

begin_comment
comment|/**  * An aggregation of operations on Guice request scopes for test purposes.  *  *<p>To execute the operations, no Gerrit permissions are necessary.  */
end_comment

begin_interface
DECL|interface|RequestScopeOperations
specifier|public
interface|interface
name|RequestScopeOperations
block|{
comment|/**    * Sets the Guice request scope to the given account.    *    *<p>The resulting context has an SSH session attached. In order to use the SSH session returned    * by {@link AcceptanceTestRequestScope.Context#getSession()}, SSH must be enabled in the test and    * the account must have a username set. However, these are not requirements simply to call this    * method.    *    * @param accountId account ID. Must exist; throws an unchecked exception otherwise.    * @return the previous request scope.    */
DECL|method|setApiUser (Account.Id accountId)
name|AcceptanceTestRequestScope
operator|.
name|Context
name|setApiUser
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
comment|/**    * Sets the Guice request scope to the given account.    *    *<p>The resulting context has an SSH session attached. In order to use the SSH session returned    * by {@link AcceptanceTestRequestScope.Context#getSession()}, SSH must be enabled in the test and    * the account must have a username set. However, these are not requirements simply to call this    * method.    *    * @param testAccount test account from {@code AccountOperations}.    * @return the previous request scope.    */
DECL|method|setApiUser (TestAccount testAccount)
name|AcceptanceTestRequestScope
operator|.
name|Context
name|setApiUser
parameter_list|(
name|TestAccount
name|testAccount
parameter_list|)
function_decl|;
comment|/**    * Enforces a new request context for the current API user.    *    *<p>This recreates the {@code IdentifiedUser}, hence everything which is cached in the {@code    * IdentifiedUser} is reloaded (e.g. the email addresses of the user).    *    *<p>The current user must be an identified user.    *    * @return the previous request scope.    */
DECL|method|resetCurrentApiUser ()
name|AcceptanceTestRequestScope
operator|.
name|Context
name|resetCurrentApiUser
parameter_list|()
function_decl|;
comment|/**    * Sets the Guice request scope to the anonymous user.    *    * @return the previous request scope.    */
DECL|method|setApiUserAnonymous ()
name|AcceptanceTestRequestScope
operator|.
name|Context
name|setApiUserAnonymous
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

