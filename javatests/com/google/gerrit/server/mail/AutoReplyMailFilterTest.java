begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
name|mail
operator|.
name|MailMessage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
DECL|class|AutoReplyMailFilterTest
specifier|public
class|class
name|AutoReplyMailFilterTest
block|{
DECL|field|autoReplyMailFilter
specifier|private
name|AutoReplyMailFilter
name|autoReplyMailFilter
init|=
operator|new
name|AutoReplyMailFilter
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|acceptsHumanReply ()
specifier|public
name|void
name|acceptsHumanReply
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|discardsBulk ()
specifier|public
name|void
name|discardsBulk
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
name|b
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Precedence: bulk"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|b
operator|=
name|createChangeAndReplyByEmail
argument_list|()
expr_stmt|;
name|b
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Precedence: list"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|b
operator|=
name|createChangeAndReplyByEmail
argument_list|()
expr_stmt|;
name|b
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Precedence: junk"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|discardsAutoSubmitted ()
specifier|public
name|void
name|discardsAutoSubmitted
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|createChangeAndReplyByEmail
argument_list|()
decl_stmt|;
name|b
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Auto-Submitted: yes"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|b
operator|=
name|createChangeAndReplyByEmail
argument_list|()
expr_stmt|;
name|b
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Auto-Submitted: no"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|autoReplyMailFilter
operator|.
name|shouldProcessMessage
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|createChangeAndReplyByEmail ()
specifier|private
name|MailMessage
operator|.
name|Builder
name|createChangeAndReplyByEmail
parameter_list|()
block|{
comment|// Build Message
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|MailMessage
operator|.
name|builder
argument_list|()
decl_stmt|;
name|b
operator|.
name|id
argument_list|(
literal|"some id"
argument_list|)
expr_stmt|;
name|b
operator|.
name|from
argument_list|(
operator|new
name|Address
argument_list|(
literal|"admim@example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|addTo
argument_list|(
operator|new
name|Address
argument_list|(
literal|"gerrit@my-company.com"
argument_list|)
argument_list|)
expr_stmt|;
comment|// Not evaluated
name|b
operator|.
name|subject
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|b
operator|.
name|dateReceived
argument_list|(
name|Instant
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|textContent
argument_list|(
literal|"I am currently out of office, please leave a code review after the beep."
argument_list|)
expr_stmt|;
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

