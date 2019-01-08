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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
comment|/**    * Sets the Guice request scope to the given account.    *    *<p>The resulting scope has no SSH session attached.    *    * @param accountId account ID. Must exist; throws an unchecked exception otherwise.    * @return the previous request scope.    */
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
comment|/**    * Sets the Guice request scope to the given account.    *    *<p>The resulting scope has no SSH session attached.    *    * @param testAccount test account from {@code AccountOperations}.    * @return the previous request scope.    */
DECL|method|setApiUser (TestAccount testAccount)
specifier|default
name|AcceptanceTestRequestScope
operator|.
name|Context
name|setApiUser
parameter_list|(
name|TestAccount
name|testAccount
parameter_list|)
block|{
return|return
name|setApiUser
argument_list|(
name|requireNonNull
argument_list|(
name|testAccount
argument_list|)
operator|.
name|accountId
argument_list|()
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

