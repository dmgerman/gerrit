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
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
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
name|AssistedInject
import|;
end_import

begin_comment
comment|/** Sender that informs a user by email that the HTTP password of their account was updated. */
end_comment

begin_class
DECL|class|HttpPasswordUpdateSender
specifier|public
class|class
name|HttpPasswordUpdateSender
extends|extends
name|OutgoingEmail
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (IdentifiedUser user, String operation)
name|HttpPasswordUpdateSender
name|create
parameter_list|(
name|IdentifiedUser
name|user
parameter_list|,
name|String
name|operation
parameter_list|)
function_decl|;
block|}
DECL|field|user
specifier|private
specifier|final
name|IdentifiedUser
name|user
decl_stmt|;
DECL|field|operation
specifier|private
specifier|final
name|String
name|operation
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|HttpPasswordUpdateSender ( EmailArguments args, @Assisted IdentifiedUser user, @Assisted String operation)
specifier|public
name|HttpPasswordUpdateSender
parameter_list|(
name|EmailArguments
name|args
parameter_list|,
annotation|@
name|Assisted
name|IdentifiedUser
name|user
parameter_list|,
annotation|@
name|Assisted
name|String
name|operation
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
literal|"HttpPasswordUpdate"
argument_list|)
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|operation
operator|=
name|operation
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
literal|"[Gerrit Code Review] HTTP password was "
operator|+
name|operation
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
name|getEmail
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shouldSendMessage ()
specifier|protected
name|boolean
name|shouldSendMessage
parameter_list|()
block|{
comment|// Always send an email if the HTTP password is updated.
return|return
literal|true
return|;
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
literal|"HttpPasswordUpdate"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|useHtml
argument_list|()
condition|)
block|{
name|appendHtml
argument_list|(
name|soyHtmlTemplate
argument_list|(
literal|"HttpPasswordUpdateHtml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
literal|"email"
argument_list|,
name|getEmail
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
name|soyContextEmailData
operator|.
name|put
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|supportsHtml ()
specifier|protected
name|boolean
name|supportsHtml
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getEmail ()
specifier|private
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|user
operator|.
name|getAccount
argument_list|()
operator|.
name|preferredEmail
argument_list|()
return|;
block|}
block|}
end_class

end_unit

