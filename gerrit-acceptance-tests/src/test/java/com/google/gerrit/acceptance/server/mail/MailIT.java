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
DECL|package|com.google.gerrit.acceptance.server.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|mail
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|NoHttpd
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
name|receive
operator|.
name|MailReceiver
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
name|testutil
operator|.
name|ConfigSuite
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
name|icegreen
operator|.
name|greenmail
operator|.
name|junit
operator|.
name|GreenMailRule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|icegreen
operator|.
name|greenmail
operator|.
name|user
operator|.
name|GreenMailUser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|icegreen
operator|.
name|greenmail
operator|.
name|util
operator|.
name|GreenMail
import|;
end_import

begin_import
import|import
name|com
operator|.
name|icegreen
operator|.
name|greenmail
operator|.
name|util
operator|.
name|GreenMailUtil
import|;
end_import

begin_import
import|import
name|com
operator|.
name|icegreen
operator|.
name|greenmail
operator|.
name|util
operator|.
name|ServerSetupTest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_class
annotation|@
name|NoHttpd
annotation|@
name|RunWith
argument_list|(
name|ConfigSuite
operator|.
name|class
argument_list|)
DECL|class|MailIT
specifier|public
class|class
name|MailIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|RECEIVEEMAIL
specifier|private
specifier|static
specifier|final
name|String
name|RECEIVEEMAIL
init|=
literal|"receiveemail"
decl_stmt|;
DECL|field|HOST
specifier|private
specifier|static
specifier|final
name|String
name|HOST
init|=
literal|"localhost"
decl_stmt|;
DECL|field|USERNAME
specifier|private
specifier|static
specifier|final
name|String
name|USERNAME
init|=
literal|"user@domain.com"
decl_stmt|;
DECL|field|PASSWORD
specifier|private
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"password"
decl_stmt|;
DECL|field|mailReceiver
annotation|@
name|Inject
specifier|private
name|MailReceiver
name|mailReceiver
decl_stmt|;
DECL|field|greenMail
annotation|@
name|Inject
specifier|private
name|GreenMail
name|greenMail
decl_stmt|;
annotation|@
name|Rule
DECL|field|mockPop3Server
specifier|public
specifier|final
name|GreenMailRule
name|mockPop3Server
init|=
operator|new
name|GreenMailRule
argument_list|(
name|ServerSetupTest
operator|.
name|SMTP_POP3_IMAP
argument_list|)
decl_stmt|;
annotation|@
name|ConfigSuite
operator|.
name|Default
DECL|method|pop3Config ()
specifier|public
specifier|static
name|Config
name|pop3Config
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"host"
argument_list|,
name|HOST
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"port"
argument_list|,
literal|"3110"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"username"
argument_list|,
name|USERNAME
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|,
name|PASSWORD
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"protocol"
argument_list|,
literal|"POP3"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"fetchInterval"
argument_list|,
literal|"99"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|imapConfig ()
specifier|public
specifier|static
name|Config
name|imapConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"host"
argument_list|,
name|HOST
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"port"
argument_list|,
literal|"3143"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"username"
argument_list|,
name|USERNAME
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"password"
argument_list|,
name|PASSWORD
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"protocol"
argument_list|,
literal|"IMAP"
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setString
argument_list|(
name|RECEIVEEMAIL
argument_list|,
literal|null
argument_list|,
literal|"fetchInterval"
argument_list|,
literal|"99"
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Test
DECL|method|delete ()
specifier|public
name|void
name|delete
parameter_list|()
throws|throws
name|Exception
block|{
name|GreenMailUser
name|user
init|=
name|mockPop3Server
operator|.
name|setUser
argument_list|(
name|USERNAME
argument_list|,
name|USERNAME
argument_list|,
name|PASSWORD
argument_list|)
decl_stmt|;
name|user
operator|.
name|deliver
argument_list|(
name|createSimpleMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|mockPop3Server
operator|.
name|getReceivedMessages
argument_list|()
operator|.
name|length
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Let Gerrit handle emails
name|mailReceiver
operator|.
name|handleEmails
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Check that the message is still present
name|assertThat
argument_list|(
name|mockPop3Server
operator|.
name|getReceivedMessages
argument_list|()
operator|.
name|length
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// Mark the message for deletion
name|mailReceiver
operator|.
name|requestDeletion
argument_list|(
name|mockPop3Server
operator|.
name|getReceivedMessages
argument_list|()
index|[
literal|0
index|]
operator|.
name|getMessageID
argument_list|()
argument_list|)
expr_stmt|;
comment|// Let Gerrit handle emails
name|mailReceiver
operator|.
name|handleEmails
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Check that the message was deleted
name|assertThat
argument_list|(
name|mockPop3Server
operator|.
name|getReceivedMessages
argument_list|()
operator|.
name|length
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|createSimpleMessage ()
specifier|private
name|MimeMessage
name|createSimpleMessage
parameter_list|()
block|{
return|return
name|GreenMailUtil
operator|.
name|createTextEmail
argument_list|(
name|USERNAME
argument_list|,
literal|"from@localhost.com"
argument_list|,
literal|"subject"
argument_list|,
literal|"body"
argument_list|,
name|greenMail
operator|.
name|getImap
argument_list|()
operator|.
name|getServerSetup
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

