begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
name|GerritConfig
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
name|EmailHeader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
DECL|class|MailSenderIT
specifier|public
class|class
name|MailSenderIT
extends|extends
name|AbstractMailIT
block|{
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"sendemail.replyToAddress"
argument_list|,
name|value
operator|=
literal|"custom@gerritcodereview.com"
argument_list|)
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receiveemail.protocol"
argument_list|,
name|value
operator|=
literal|"POP3"
argument_list|)
DECL|method|outgoingMailHasCustomReplyToHeader ()
specifier|public
name|void
name|outgoingMailHasCustomReplyToHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|createChangeWithReview
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// Check that the custom address was added as Reply-To
name|assertThat
argument_list|(
name|sender
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
init|=
name|sender
operator|.
name|getMessages
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|headers
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|headerString
argument_list|(
name|headers
argument_list|,
literal|"Reply-To"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"custom@gerritcodereview.com"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outgoingMailHasUserEmailInReplyToHeader ()
specifier|public
name|void
name|outgoingMailHasUserEmailInReplyToHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|createChangeWithReview
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// Check that the user's email was added as Reply-To
name|assertThat
argument_list|(
name|sender
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
init|=
name|sender
operator|.
name|getMessages
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|headers
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|headerString
argument_list|(
name|headers
argument_list|,
literal|"Reply-To"
argument_list|)
argument_list|)
operator|.
name|contains
argument_list|(
name|user
operator|.
name|email
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outgoingMailHasListHeaders ()
specifier|public
name|void
name|outgoingMailHasListHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChangeWithReview
argument_list|(
name|user
argument_list|)
decl_stmt|;
comment|// Check that the mail has the expected headers
name|assertThat
argument_list|(
name|sender
operator|.
name|getMessages
argument_list|()
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
init|=
name|sender
operator|.
name|getMessages
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|headers
argument_list|()
decl_stmt|;
name|String
name|hostname
init|=
name|URI
operator|.
name|create
argument_list|(
name|canonicalWebUrl
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|getHost
argument_list|()
decl_stmt|;
name|String
name|listId
init|=
name|String
operator|.
name|format
argument_list|(
literal|"<gerrit-%s.%s>"
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|hostname
argument_list|)
decl_stmt|;
name|String
name|unsubscribeLink
init|=
name|String
operator|.
name|format
argument_list|(
literal|"<%ssettings>"
argument_list|,
name|canonicalWebUrl
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|threadId
init|=
name|String
operator|.
name|format
argument_list|(
literal|"<gerrit.%s.%s@%s>"
argument_list|,
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|created
operator|.
name|getTime
argument_list|()
argument_list|,
name|changeId
argument_list|,
name|hostname
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|headerString
argument_list|(
name|headers
argument_list|,
literal|"List-Id"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|listId
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|headerString
argument_list|(
name|headers
argument_list|,
literal|"List-Unsubscribe"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|unsubscribeLink
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|headerString
argument_list|(
name|headers
argument_list|,
literal|"In-Reply-To"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|threadId
argument_list|)
expr_stmt|;
block|}
DECL|method|headerString (Map<String, EmailHeader> headers, String name)
specifier|private
name|String
name|headerString
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|EmailHeader
argument_list|>
name|headers
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|EmailHeader
name|header
init|=
name|headers
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|header
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|EmailHeader
operator|.
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
operator|(
operator|(
name|EmailHeader
operator|.
name|String
operator|)
name|header
operator|)
operator|.
name|getString
argument_list|()
return|;
block|}
block|}
end_class

end_unit
