begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|extensions
operator|.
name|api
operator|.
name|accounts
operator|.
name|AccountInput
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
name|extensions
operator|.
name|common
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
name|extensions
operator|.
name|restapi
operator|.
name|ResourceConflictException
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
name|extensions
operator|.
name|restapi
operator|.
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|AccountResource
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
name|Singleton
import|;
end_import

begin_comment
comment|/**  * REST endpoint for updating an existing account.  *  *<p>This REST endpoint handles {@code PUT /accounts/<account-identifier>} requests if the  * specified account already exists. If it doesn't exist yet, the request is handled by {@link  * CreateAccount}.  *  *<p>We do not support account updates via this path, hence this REST endpoint always throws a  * {@link ResourceConflictException} which results in a {@code 409 Conflict} response. Account  * properties can only be updated via the dedicated REST endpoints that serve {@code PUT} requests  * on {@code /accounts/<account-identifier>/<account-view>}.  *  *<p>This REST endpoint solely exists to avoid user confusion if they create a new account with  * {@code PUT /accounts/<account-identifier>} and then repeat the same request. Without this REST  * endpoint the second request would fail with {@code 404 Not Found}, which would be surprising to  * the user.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PutAccount
specifier|public
class|class
name|PutAccount
implements|implements
name|RestModifyView
argument_list|<
name|AccountResource
argument_list|,
name|AccountInput
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (AccountResource resource, AccountInput input)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|resource
parameter_list|,
name|AccountInput
name|input
parameter_list|)
throws|throws
name|ResourceConflictException
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"account exists"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

