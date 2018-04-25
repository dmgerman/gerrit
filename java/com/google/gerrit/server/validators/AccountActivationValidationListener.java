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
DECL|package|com.google.gerrit.server.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|validators
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
name|extensions
operator|.
name|annotations
operator|.
name|ExtensionPoint
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
comment|/**  * Validator that is invoked when an account activated or deactivated via the Gerrit REST API or the  * Java extension API.  */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|AccountActivationValidationListener
specifier|public
interface|interface
name|AccountActivationValidationListener
block|{
comment|/**    * Called when an account should be activated to allow validation of the account activation.    *    * @param account the account that should be activated    * @throws ValidationException if validation fails    */
DECL|method|validateActivation (AccountState account)
name|void
name|validateActivation
parameter_list|(
name|AccountState
name|account
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**    * Called when an account should be deactivated to allow validation of the account deactivation.    *    * @param account the account that should be deactivated    * @throws ValidationException if validation fails    */
DECL|method|validateDeactivation (AccountState account)
name|void
name|validateDeactivation
parameter_list|(
name|AccountState
name|account
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit

