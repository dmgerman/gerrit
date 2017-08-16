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

begin_comment
comment|/* Contract that OAuth provider must implement */
end_comment

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|OAuthServiceProvider
specifier|public
interface|interface
name|OAuthServiceProvider
block|{
comment|/**    * Returns the URL where you should redirect your users to authenticate your application.    *    * @return the OAuth service URL to redirect your users for authentication    */
DECL|method|getAuthorizationUrl ()
name|String
name|getAuthorizationUrl
parameter_list|()
function_decl|;
comment|/**    * Retrieve the access token    *    * @param verifier verifier code    * @return access token    */
DECL|method|getAccessToken (OAuthVerifier verifier)
name|OAuthToken
name|getAccessToken
parameter_list|(
name|OAuthVerifier
name|verifier
parameter_list|)
function_decl|;
comment|/**    * After establishing of secure communication channel, this method supossed to access the    * protected resoure and retrieve the username.    *    * @param token    * @return OAuth user information    * @throws IOException    */
DECL|method|getUserInfo (OAuthToken token)
name|OAuthUserInfo
name|getUserInfo
parameter_list|(
name|OAuthToken
name|token
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**    * Returns the OAuth version of the service.    *    * @return oauth version as string    */
DECL|method|getVersion ()
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**    * Returns the name of this service. This name is resented the user to choose between multiple    * service providers    *    * @return name of the service    */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

