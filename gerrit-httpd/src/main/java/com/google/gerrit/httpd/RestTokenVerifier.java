begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|mail
operator|.
name|RegisterNewEmailSender
import|;
end_import

begin_comment
comment|/** Verifies the token sent by {@link RegisterNewEmailSender}. */
end_comment

begin_interface
DECL|interface|RestTokenVerifier
specifier|public
interface|interface
name|RestTokenVerifier
block|{
comment|/**    * Construct a token to verify a REST PUT request.    *    * @param user the caller that wants to make a PUT request    * @param url the URL being requested    * @return an unforgeable string to send to the user as the body of a GET    *         request. Presenting the string in a follow-up POST request provides    *         proof the user has the ability to read messages sent to thier    *         browser and they likely aren't making the request via XSRF.    */
DECL|method|sign (Account.Id user, String url)
specifier|public
name|String
name|sign
parameter_list|(
name|Account
operator|.
name|Id
name|user
parameter_list|,
name|String
name|url
parameter_list|)
function_decl|;
comment|/**    * Decode a token previously created.    *    * @param user the user making the verify request.    * @param url the url user is attempting to access.    * @param token the string created by sign.    * @throws InvalidTokenException the token is invalid, expired, malformed,    *         etc.    */
DECL|method|verify (Account.Id user, String url, String token)
specifier|public
name|void
name|verify
parameter_list|(
name|Account
operator|.
name|Id
name|user
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|token
parameter_list|)
throws|throws
name|InvalidTokenException
function_decl|;
comment|/** Exception thrown when a token does not parse correctly. */
DECL|class|InvalidTokenException
specifier|public
specifier|static
class|class
name|InvalidTokenException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|InvalidTokenException ()
specifier|public
name|InvalidTokenException
parameter_list|()
block|{
name|super
argument_list|(
literal|"Invalid token"
argument_list|)
expr_stmt|;
block|}
DECL|method|InvalidTokenException (Throwable cause)
specifier|public
name|InvalidTokenException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
literal|"Invalid token"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_interface

end_unit

