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
DECL|package|com.google.gerrit.pgm.init
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|InitUtil
operator|.
name|isLocal
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|init
operator|.
name|InitUtil
operator|.
name|username
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
name|pgm
operator|.
name|util
operator|.
name|ConsoleUI
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
name|SitePaths
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
name|SmtpEmailSender
operator|.
name|Encryption
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
name|Singleton
import|;
end_import

begin_comment
comment|/** Initialize the {@code sendemail} configuration section. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|InitSendEmail
class|class
name|InitSendEmail
implements|implements
name|InitStep
block|{
DECL|field|ui
specifier|private
specifier|final
name|ConsoleUI
name|ui
decl_stmt|;
DECL|field|sendemail
specifier|private
specifier|final
name|Section
name|sendemail
decl_stmt|;
DECL|field|site
specifier|private
specifier|final
name|SitePaths
name|site
decl_stmt|;
annotation|@
name|Inject
DECL|method|InitSendEmail (final ConsoleUI ui, final SitePaths site, final Section.Factory sections)
name|InitSendEmail
parameter_list|(
specifier|final
name|ConsoleUI
name|ui
parameter_list|,
specifier|final
name|SitePaths
name|site
parameter_list|,
specifier|final
name|Section
operator|.
name|Factory
name|sections
parameter_list|)
block|{
name|this
operator|.
name|ui
operator|=
name|ui
expr_stmt|;
name|this
operator|.
name|sendemail
operator|=
name|sections
operator|.
name|get
argument_list|(
literal|"sendemail"
argument_list|)
expr_stmt|;
name|this
operator|.
name|site
operator|=
name|site
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ui
operator|.
name|header
argument_list|(
literal|"Email Delivery"
argument_list|)
expr_stmt|;
specifier|final
name|String
name|hostname
init|=
name|sendemail
operator|.
name|string
argument_list|(
literal|"SMTP server hostname"
argument_list|,
literal|"smtpServer"
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|sendemail
operator|.
name|string
argument_list|(
literal|"SMTP server port"
argument_list|,
literal|"smtpServerPort"
argument_list|,
literal|"(default)"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|Encryption
name|enc
init|=
name|sendemail
operator|.
name|select
argument_list|(
literal|"SMTP encryption"
argument_list|,
literal|"smtpEncryption"
argument_list|,
name|Encryption
operator|.
name|NONE
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|username
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|site
operator|.
name|gerrit_config
operator|.
name|exists
argument_list|()
condition|)
block|{
name|username
operator|=
name|sendemail
operator|.
name|get
argument_list|(
literal|"smtpUser"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|(
name|enc
operator|!=
literal|null
operator|&&
name|enc
operator|!=
name|Encryption
operator|.
name|NONE
operator|)
operator|||
operator|!
name|isLocal
argument_list|(
name|hostname
argument_list|)
condition|)
block|{
name|username
operator|=
name|username
argument_list|()
expr_stmt|;
block|}
name|sendemail
operator|.
name|string
argument_list|(
literal|"SMTP username"
argument_list|,
literal|"smtpUser"
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|sendemail
operator|.
name|password
argument_list|(
literal|"smtpUser"
argument_list|,
literal|"smtpPass"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

