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
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTimeZone
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
comment|/**  * Provides a raw message payload and a parsed {@code MailMessage} to check that mime parts that are  * neither text/plain, nor * text/html are dropped.  */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|AttachmentMessage
specifier|public
class|class
name|AttachmentMessage
extends|extends
name|RawMailMessage
block|{
DECL|field|raw
specifier|private
specifier|static
name|String
name|raw
init|=
literal|"MIME-Version: 1.0\n"
operator|+
literal|"Date: Tue, 25 Oct 2016 02:11:35 -0700\n"
operator|+
literal|"Message-ID:<CAM7sg=3meaAVUxW3KXeJEVs8sv_ADw1BnvpcHHiYVR2TQQi__w"
operator|+
literal|"@mail.gmail.com>\n"
operator|+
literal|"Subject: Test Subject\n"
operator|+
literal|"From: Patrick Hiesel<hiesel@google.com>\n"
operator|+
literal|"To: Patrick Hiesel<hiesel@google.com>\n"
operator|+
literal|"Content-Type: multipart/mixed; boundary=001a114e019a56962d054062708f\n"
operator|+
literal|"\n"
operator|+
literal|"--001a114e019a56962d054062708f\n"
operator|+
literal|"Content-Type: multipart/alternative; boundary=001a114e019a5696250540"
operator|+
literal|"62708d\n"
operator|+
literal|"\n"
operator|+
literal|"--001a114e019a569625054062708d\n"
operator|+
literal|"Content-Type: text/plain; charset=UTF-8\n"
operator|+
literal|"\n"
operator|+
literal|"Contains unwanted attachment"
operator|+
literal|"\n"
operator|+
literal|"--001a114e019a569625054062708d\n"
operator|+
literal|"Content-Type: text/html; charset=UTF-8\n"
operator|+
literal|"\n"
operator|+
literal|"<div dir=\"ltr\">Contains unwanted attachment</div>"
operator|+
literal|"\n"
operator|+
literal|"--001a114e019a569625054062708d--\n"
operator|+
literal|"--001a114e019a56962d054062708f\n"
operator|+
literal|"Content-Type: text/plain; charset=US-ASCII; name=\"test.txt\"\n"
operator|+
literal|"Content-Disposition: attachment; filename=\"test.txt\"\n"
operator|+
literal|"Content-Transfer-Encoding: base64\n"
operator|+
literal|"X-Attachment-Id: f_iv264bt50\n"
operator|+
literal|"\n"
operator|+
literal|"VEVTVAo=\n"
operator|+
literal|"--001a114e019a56962d054062708f--"
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\uD83D\uDE1B test"
argument_list|)
expr_stmt|;
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
literal|"<CAM7sg=3meaAVUxW3KXeJEVs8sv_ADw1BnvpcHHiYVR2TQQi__w@mail.gmail.com>"
argument_list|)
operator|.
name|from
argument_list|(
operator|new
name|Address
argument_list|(
literal|"Patrick Hiesel"
argument_list|,
literal|"hiesel@google.com"
argument_list|)
argument_list|)
operator|.
name|addTo
argument_list|(
operator|new
name|Address
argument_list|(
literal|"Patrick Hiesel"
argument_list|,
literal|"hiesel@google.com"
argument_list|)
argument_list|)
operator|.
name|textContent
argument_list|(
literal|"Contains unwanted attachment"
argument_list|)
operator|.
name|htmlContent
argument_list|(
literal|"<div dir=\"ltr\">Contains unwanted attachment</div>"
argument_list|)
operator|.
name|subject
argument_list|(
literal|"Test Subject"
argument_list|)
operator|.
name|addAdditionalHeader
argument_list|(
literal|"MIME-Version: 1.0"
argument_list|)
operator|.
name|dateReceived
argument_list|(
operator|new
name|DateTime
argument_list|(
literal|2016
argument_list|,
literal|10
argument_list|,
literal|25
argument_list|,
literal|9
argument_list|,
literal|11
argument_list|,
literal|35
argument_list|,
literal|0
argument_list|,
name|DateTimeZone
operator|.
name|UTC
argument_list|)
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

