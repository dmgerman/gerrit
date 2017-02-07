begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|accounts
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
name|restapi
operator|.
name|DefaultInput
import|;
end_import

begin_comment
comment|/** This entity contains information for registering a new email address. */
end_comment

begin_class
DECL|class|EmailInput
specifier|public
class|class
name|EmailInput
block|{
comment|/* The email address. If provided, must match the email address from the URL. */
DECL|field|email
annotation|@
name|DefaultInput
specifier|public
name|String
name|email
decl_stmt|;
comment|/* Whether the new email address should become the preferred email address of    * the user. Only supported if {@link #noConfirmation} is set or if the    * authentication type is DEVELOPMENT_BECOME_ANY_ACCOUNT.*/
DECL|field|preferred
specifier|public
name|boolean
name|preferred
decl_stmt|;
comment|/* Whether the email address should be added without confirmation. In this    * case no verification email is sent to the user. Only Gerrit administrators    * are allowed to add email addresses without confirmation. */
DECL|field|noConfirmation
specifier|public
name|boolean
name|noConfirmation
decl_stmt|;
block|}
end_class

end_unit

