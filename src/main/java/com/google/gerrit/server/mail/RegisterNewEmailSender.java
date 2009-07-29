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
name|server
operator|.
name|GerritServer
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
name|config
operator|.
name|AuthConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|server
operator|.
name|XsrfException
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

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|util
operator|.
name|Base64
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
specifier|public
name|RegisterNewEmailSender
name|create
parameter_list|(
name|String
name|address
parameter_list|)
function_decl|;
block|}
DECL|field|req
specifier|private
specifier|final
name|HttpServletRequest
name|req
decl_stmt|;
DECL|field|addr
specifier|private
specifier|final
name|String
name|addr
decl_stmt|;
DECL|field|authConfig
specifier|private
specifier|final
name|AuthConfig
name|authConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|RegisterNewEmailSender (final GerritServer gs, final EmailSender sf, final HttpServletRequest request, final AuthConfig ac, @Assisted final String address)
specifier|public
name|RegisterNewEmailSender
parameter_list|(
specifier|final
name|GerritServer
name|gs
parameter_list|,
specifier|final
name|EmailSender
name|sf
parameter_list|,
specifier|final
name|HttpServletRequest
name|request
parameter_list|,
specifier|final
name|AuthConfig
name|ac
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
name|gs
argument_list|,
name|sf
argument_list|,
literal|null
argument_list|,
literal|"registernewemail"
argument_list|)
expr_stmt|;
name|addr
operator|=
name|address
expr_stmt|;
name|req
operator|=
name|request
expr_stmt|;
name|authConfig
operator|=
name|ac
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
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
DECL|method|shouldSendMessage ()
specifier|protected
name|boolean
name|shouldSendMessage
parameter_list|()
block|{
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
block|{
specifier|final
name|StringBuffer
name|url
init|=
name|req
operator|.
name|getRequestURL
argument_list|()
decl_stmt|;
name|url
operator|.
name|setLength
argument_list|(
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
comment|// cut "AccountSecurity"
name|url
operator|.
name|setLength
argument_list|(
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
comment|// cut "rpc"
name|url
operator|.
name|setLength
argument_list|(
name|url
operator|.
name|lastIndexOf
argument_list|(
literal|"/"
argument_list|)
argument_list|)
expr_stmt|;
comment|// cut "gerrit"
name|url
operator|.
name|append
argument_list|(
literal|"/Gerrit#VE,"
argument_list|)
expr_stmt|;
try|try
block|{
name|url
operator|.
name|append
argument_list|(
name|authConfig
operator|.
name|getEmailRegistrationToken
argument_list|()
operator|.
name|newToken
argument_list|(
name|Base64
operator|.
name|encodeBytes
argument_list|(
name|addr
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XsrfException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|appendText
argument_list|(
literal|"Welcome to Gerrit Code Review at "
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|req
operator|.
name|getServerName
argument_list|()
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|".\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"To add a verified email address to your user account, please\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"click on the following link:\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"If you have received this mail in error,"
operator|+
literal|" you do not need to take any\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"action to cancel the account."
operator|+
literal|" The account will not be activated, and\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"you will not receive any further emails.\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"If clicking the link above does not work,"
operator|+
literal|" copy and paste the URL in a\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"new browser window instead.\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"This is a send-only email address."
operator|+
literal|"  Replies to this message will not\n"
argument_list|)
expr_stmt|;
name|appendText
argument_list|(
literal|"be read or answered.\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

