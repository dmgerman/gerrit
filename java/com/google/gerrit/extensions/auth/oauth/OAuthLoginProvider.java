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
DECL|package|com.google.gerrit.extensions.auth.oauth
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|auth
operator|.
name|oauth
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|OAuthLoginProvider
specifier|public
interface|interface
name|OAuthLoginProvider
block|{
comment|/**    * Performs a login with an OAuth2 provider for Git over HTTP communication.    *    *<p>An implementation of this interface must transmit the given user name and secret, which can    * be either an OAuth2 access token or a password, to the OAuth2 backend for verification.    *    * @param username the user's identifier.    * @param secret the secret to verify, e.g. a previously received access token or a password.    * @return information about the logged in user, at least external id, user name and email    *     address.    * @throws IOException if the login failed.    */
DECL|method|login (String username, String secret)
name|OAuthUserInfo
name|login
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|secret
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

