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
DECL|package|com.google.gerrit.server.mail.receive.data
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
name|receive
operator|.
name|data
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
name|mail
operator|.
name|receive
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
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Month
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|/** Tests parsing a Base64 encoded subject. */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|Base64HeaderMessage
specifier|public
class|class
name|Base64HeaderMessage
extends|extends
name|RawMailMessage
block|{
DECL|field|textContent
specifier|private
specifier|static
name|String
name|textContent
init|=
literal|"Some Text"
decl_stmt|;
DECL|field|raw
specifier|private
specifier|static
name|String
name|raw
init|=
literal|""
operator|+
literal|"Date: Tue, 25 Oct 2016 02:11:35 -0700\n"
operator|+
literal|"Message-ID:<001a114da7ae26e2eb053fe0c29c@google.com>\n"
operator|+
literal|"Subject: =?UTF-8?B?8J+YmyB0ZXN0?=\n"
operator|+
literal|"From: \"Jonathan Nieder (Gerrit)\"<noreply-gerritcodereview-"
operator|+
literal|"CtTy0igsBrnvL7dKoWEIEg@google.com>\n"
operator|+
literal|"To: ekempin<ekempin@google.com>\n"
operator|+
literal|"Content-Type: text/plain; charset=UTF-8; format=flowed; delsp=yes\n"
operator|+
literal|"\n"
operator|+
name|textContent
decl_stmt|;
annotation|@
name|Override
DECL|method|raw ()
specifier|public
name|String
name|raw
parameter_list|()
block|{
return|return
name|raw
return|;
block|}
annotation|@
name|Override
DECL|method|rawChars ()
specifier|public
name|int
index|[]
name|rawChars
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|expectedMailMessage ()
specifier|public
name|MailMessage
name|expectedMailMessage
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|expect
init|=
name|MailMessage
operator|.
name|builder
argument_list|()
decl_stmt|;
name|expect
operator|.
name|id
argument_list|(
literal|"<001a114da7ae26e2eb053fe0c29c@google.com>"
argument_list|)
operator|.
name|from
argument_list|(
operator|new
name|Address
argument_list|(
literal|"Jonathan Nieder (Gerrit)"
argument_list|,
literal|"noreply-gerritcodereview-CtTy0igsBrnvL7dKoWEIEg@google.com"
argument_list|)
argument_list|)
operator|.
name|addTo
argument_list|(
operator|new
name|Address
argument_list|(
literal|"ekempin"
argument_list|,
literal|"ekempin@google.com"
argument_list|)
argument_list|)
operator|.
name|textContent
argument_list|(
name|textContent
argument_list|)
operator|.
name|subject
argument_list|(
literal|"\uD83D\uDE1B test"
argument_list|)
operator|.
name|dateReceived
argument_list|(
name|LocalDateTime
operator|.
name|of
argument_list|(
literal|2016
argument_list|,
name|Month
operator|.
name|OCTOBER
argument_list|,
literal|25
argument_list|,
literal|9
argument_list|,
literal|11
argument_list|,
literal|35
argument_list|)
operator|.
name|atOffset
argument_list|(
name|ZoneOffset
operator|.
name|UTC
argument_list|)
operator|.
name|toInstant
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|expect
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

