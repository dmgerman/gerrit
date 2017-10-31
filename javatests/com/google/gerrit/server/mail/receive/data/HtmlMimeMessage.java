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
comment|/** Tests a message containing mime/alternative (text + html) content. */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|HtmlMimeMessage
specifier|public
class|class
name|HtmlMimeMessage
extends|extends
name|RawMailMessage
block|{
DECL|field|textContent
specifier|private
specifier|static
name|String
name|textContent
init|=
literal|"Simple test"
decl_stmt|;
comment|// htmlContent is encoded in quoted-printable
DECL|field|htmlContent
specifier|private
specifier|static
name|String
name|htmlContent
init|=
literal|"<div dir=3D\"ltr\">Test<span style"
operator|+
literal|"=3D\"background-color:rgb(255,255,0)\">Messa=\n"
operator|+
literal|"ge</span> in<u>HTML=C2=A0</u><a href=3D\"https://en.wikipedia.org/"
operator|+
literal|"wiki/%C3%=\n9Cmlaut_(band)\" class=3D\"gmail-mw-redirect\" title=3D\""
operator|+
literal|"=C3=9Cmlaut (band)\" st=\nyle=3D\"text-decoration:none;color:rgb(11,"
operator|+
literal|"0,128);background-image:none;backg=\nround-position:initial;background"
operator|+
literal|"-size:initial;background-repeat:initial;ba=\nckground-origin:initial;"
operator|+
literal|"background-clip:initial;font-family:sans-serif;font=\n"
operator|+
literal|"-size:14px\">=C3=9C</a></div>"
decl_stmt|;
DECL|field|unencodedHtmlContent
specifier|private
specifier|static
name|String
name|unencodedHtmlContent
init|=
literal|""
operator|+
literal|"<div dir=\"ltr\">Test<span style=\"background-color:rgb(255,255,0)\">"
operator|+
literal|"Message</span> in<u>HTMLÂ </u><a href=\"https://en.wikipedia.org/wiki/"
operator|+
literal|"%C3%9Cmlaut_(band)\" class=\"gmail-mw-redirect\" title=\"Ãmlaut "
operator|+
literal|"(band)\" style=\"text-decoration:none;color:rgb(11,0,128);"
operator|+
literal|"background-image:none;background-position:initial;background-size:"
operator|+
literal|"initial;background-repeat:initial;background-origin:initial;background"
operator|+
literal|"-clip:initial;font-family:sans-serif;font-size:14px\">Ã</a></div>"
decl_stmt|;
DECL|field|raw
specifier|private
specifier|static
name|String
name|raw
init|=
literal|""
operator|+
literal|"MIME-Version: 1.0\n"
operator|+
literal|"Date: Tue, 25 Oct 2016 02:11:35 -0700\n"
operator|+
literal|"Message-ID:<001a114cd8be55b4ab053face5cd@google.com>\n"
operator|+
literal|"Subject: Change in gerrit[master]: Implement receiver class structure "
operator|+
literal|"and bindings\n"
operator|+
literal|"From: \"ekempin (Gerrit)\"<noreply-gerritcodereview-qUgXfQecoDLHwp0Ml"
operator|+
literal|"dAzig@google.com>\n"
operator|+
literal|"To: Patrick Hiesel<hiesel@google.com>\n"
operator|+
literal|"Cc: ekempin<ekempin@google.com>\n"
operator|+
literal|"Content-Type: multipart/alternative; boundary=001a114cd8b"
operator|+
literal|"e55b486053face5ca\n"
operator|+
literal|"\n"
operator|+
literal|"--001a114cd8be55b486053face5ca\n"
operator|+
literal|"Content-Type: text/plain; charset=UTF-8; format=flowed; delsp=yes\n"
operator|+
literal|"\n"
operator|+
name|textContent
operator|+
literal|"\n"
operator|+
literal|"--001a114cd8be55b486053face5ca\n"
operator|+
literal|"Content-Type: text/html; charset=UTF-8\n"
operator|+
literal|"Content-Transfer-Encoding: quoted-printable\n"
operator|+
literal|"\n"
operator|+
name|htmlContent
operator|+
literal|"\n"
operator|+
literal|"--001a114cd8be55b486053face5ca--"
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
literal|"<001a114cd8be55b4ab053face5cd@google.com>"
argument_list|)
operator|.
name|from
argument_list|(
operator|new
name|Address
argument_list|(
literal|"ekempin (Gerrit)"
argument_list|,
literal|"noreply-gerritcodereview-qUgXfQecoDLHwp0MldAzig@google.com"
argument_list|)
argument_list|)
operator|.
name|addCc
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
name|textContent
argument_list|)
operator|.
name|htmlContent
argument_list|(
name|unencodedHtmlContent
argument_list|)
operator|.
name|subject
argument_list|(
literal|"Change in gerrit[master]: Implement receiver class structure and bindings"
argument_list|)
operator|.
name|addAdditionalHeader
argument_list|(
literal|"MIME-Version: 1.0"
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

