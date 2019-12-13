begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
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
operator|.
name|send
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
name|exceptions
operator|.
name|EmailException
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
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|mail
operator|.
name|Address
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
name|IdentifiedUser
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
name|EmailTokenVerifier
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
name|Inject
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_class
DECL|class|RegisterNewEmailSender
specifier|public
class|class
name|RegisterNewEmailSender
extends|extends
name|OutgoingEmail
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String address)
name|RegisterNewEmailSender
name|create
parameter_list|(
name|String
name|address
parameter_list|)
function_decl|;
block|}
DECL|field|tokenVerifier
specifier|private
specifier|final
name|EmailTokenVerifier
name|tokenVerifier
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|addr
specifier|private
specifier|final
name|String
name|addr
decl_stmt|;
DECL|field|emailToken
specifier|private
name|String
name|emailToken
decl_stmt|;
annotation|@
name|Inject
DECL|method|RegisterNewEmailSender ( EmailArguments args, EmailTokenVerifier tokenVerifier, IdentifiedUser callingUser, @Assisted final String address)
specifier|public
name|RegisterNewEmailSender
parameter_list|(
name|EmailArguments
name|args
parameter_list|,
name|EmailTokenVerifier
name|tokenVerifier
parameter_list|,
name|IdentifiedUser
name|callingUser
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|String
name|address
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
literal|"registernewemail"
argument_list|)
expr_stmt|;
name|this
operator|.
name|tokenVerifier
operator|=
name|tokenVerifier
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|callingUser
expr_stmt|;
name|this
operator|.
name|addr
operator|=
name|address
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
throws|throws
name|EmailException
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
name|setHeader
argument_list|(
literal|"Subject"
argument_list|,
literal|"[Gerrit Code Review] Email Verification"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|,
operator|new
name|Address
argument_list|(
name|addr
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|format ()
specifier|protected
name|void
name|format
parameter_list|()
throws|throws
name|EmailException
block|{
name|appendText
argument_list|(
name|textTemplate
argument_list|(
literal|"RegisterNewEmail"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|isAllowed ()
specifier|public
name|boolean
name|isAllowed
parameter_list|()
block|{
return|return
name|args
operator|.
name|emailSender
operator|.
name|canEmail
argument_list|(
name|addr
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setupSoyContext ()
specifier|protected
name|void
name|setupSoyContext
parameter_list|()
block|{
name|super
operator|.
name|setupSoyContext
argument_list|()
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"emailRegistrationToken"
argument_list|,
name|getEmailRegistrationToken
argument_list|()
argument_list|)
expr_stmt|;
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"userNameEmail"
argument_list|,
name|getUserNameEmailFor
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getEmailRegistrationToken ()
specifier|private
name|String
name|getEmailRegistrationToken
parameter_list|()
block|{
if|if
condition|(
name|emailToken
operator|==
literal|null
condition|)
block|{
name|emailToken
operator|=
name|requireNonNull
argument_list|(
name|tokenVerifier
operator|.
name|encode
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|addr
argument_list|)
argument_list|,
literal|"token"
argument_list|)
expr_stmt|;
block|}
return|return
name|emailToken
return|;
block|}
block|}
end_class

end_unit

