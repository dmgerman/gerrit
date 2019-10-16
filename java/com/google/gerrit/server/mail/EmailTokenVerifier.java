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
DECL|package|com.google.gerrit.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
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
name|entities
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
name|account
operator|.
name|AuthRequest
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
name|send
operator|.
name|RegisterNewEmailSender
import|;
end_import

begin_comment
comment|/** Verifies the token sent by {@link RegisterNewEmailSender}. */
end_comment

begin_interface
DECL|interface|EmailTokenVerifier
specifier|public
interface|interface
name|EmailTokenVerifier
block|{
comment|/**    * Construct a token to verify an email address for a user.    *    * @param accountId the caller that wants to add an email to their account.    * @param emailAddress the address to add.    * @return an unforgeable string to email to {@code emailAddress}. Presenting the string provides    *     proof the user has the ability to read messages sent to that address. Must not be null.    */
DECL|method|encode (Account.Id accountId, String emailAddress)
name|String
name|encode
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|emailAddress
parameter_list|)
function_decl|;
comment|/**    * Decode a token previously created.    *    * @param tokenString the string created by encode. Never null.    * @return a pair of account id and email address.    * @throws InvalidTokenException the token is invalid, expired, malformed, etc.    */
DECL|method|decode (String tokenString)
name|ParsedToken
name|decode
parameter_list|(
name|String
name|tokenString
parameter_list|)
throws|throws
name|InvalidTokenException
function_decl|;
comment|/** Exception thrown when a token does not parse correctly. */
DECL|class|InvalidTokenException
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
comment|/** Pair returned from decode to provide the data used during encode. */
DECL|class|ParsedToken
class|class
name|ParsedToken
block|{
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|emailAddress
specifier|private
specifier|final
name|String
name|emailAddress
decl_stmt|;
DECL|method|ParsedToken (Account.Id accountId, String emailAddress)
specifier|public
name|ParsedToken
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|emailAddress
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|emailAddress
operator|=
name|emailAddress
expr_stmt|;
block|}
DECL|method|getAccountId ()
specifier|public
name|Account
operator|.
name|Id
name|getAccountId
parameter_list|()
block|{
return|return
name|accountId
return|;
block|}
DECL|method|getEmailAddress ()
specifier|public
name|String
name|getEmailAddress
parameter_list|()
block|{
return|return
name|emailAddress
return|;
block|}
DECL|method|toAuthRequest ()
specifier|public
name|AuthRequest
name|toAuthRequest
parameter_list|()
block|{
return|return
name|AuthRequest
operator|.
name|forEmail
argument_list|(
name|getEmailAddress
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|accountId
operator|+
literal|" adds "
operator|+
name|emailAddress
return|;
block|}
block|}
block|}
end_interface

end_unit

