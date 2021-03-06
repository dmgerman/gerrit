begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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

begin_interface
annotation|@
name|ExtensionPoint
DECL|interface|OAuthTokenEncrypter
specifier|public
interface|interface
name|OAuthTokenEncrypter
block|{
comment|/**    * Encrypts the secret parts of the given OAuth access token.    *    * @param unencrypted a raw OAuth access token.    */
DECL|method|encrypt (OAuthToken unencrypted)
name|OAuthToken
name|encrypt
parameter_list|(
name|OAuthToken
name|unencrypted
parameter_list|)
function_decl|;
comment|/**    * Decrypts the secret parts of the given OAuth access token.    *    * @param encrypted an encryppted OAuth access token.    */
DECL|method|decrypt (OAuthToken encrypted)
name|OAuthToken
name|decrypt
parameter_list|(
name|OAuthToken
name|encrypted
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

