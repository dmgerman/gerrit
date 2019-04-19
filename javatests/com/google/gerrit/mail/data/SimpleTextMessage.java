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
DECL|package|com.google.gerrit.mail.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|mail
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
comment|/** Tests parsing a simple text message with different headers. */
end_comment

begin_class
annotation|@
name|Ignore
DECL|class|SimpleTextMessage
specifier|public
class|class
name|SimpleTextMessage
extends|extends
name|RawMailMessage
block|{
DECL|field|textContent
specifier|private
specifier|static
name|String
name|textContent
init|=
literal|""
operator|+
literal|"Jonathan Nieder has posted comments on this change. (  \n"
operator|+
literal|"https://gerrit-review.googlesource.com/90018 )\n"
operator|+
literal|"\n"
operator|+
literal|"Change subject: (Re)enable voting buttons for merged changes\n"
operator|+
literal|"...........................................................\n"
operator|+
literal|"\n"
operator|+
literal|"\n"
operator|+
literal|"Patch Set 2:\n"
operator|+
literal|"\n"
operator|+
literal|"This is producing NPEs server-side and 500s for the client.   \n"
operator|+
literal|"when I try to load this change:\n"
operator|+
literal|"\n"
operator|+
literal|"  Error in GET /changes/90018/detail?O=10004\n"
operator|+
literal|"  com.google.gerrit.exceptions.StorageException: java.lang.NullPointerException\n"
operator|+
literal|"\tat com.google.gerrit.change.ChangeJson.format(ChangeJson.java:303)\n"
operator|+
literal|"\tat com.google.gerrit.change.ChangeJson.format(ChangeJson.java:285)\n"
operator|+
literal|"\tat com.google.gerrit.change.ChangeJson.format(ChangeJson.java:263)\n"
operator|+
literal|"\tat com.google.gerrit.change.GetChange.apply(GetChange.java:50)\n"
operator|+
literal|"\tat com.google.gerrit.change.GetDetail.apply(GetDetail.java:51)\n"
operator|+
literal|"\tat com.google.gerrit.change.GetDetail.apply(GetDetail.java:26)\n"
operator|+
literal|"\tat  \n"
operator|+
literal|"com.google.gerrit.RestApiServlet.service(RestApiServlet.java:367)\n"
operator|+
literal|"\tat javax.servlet.http.HttpServlet.service(HttpServlet.java:717)\n"
operator|+
literal|"[...]\n"
operator|+
literal|"  Caused by: java.lang.NullPointerException\n"
operator|+
literal|"\tat  \n"
operator|+
literal|"com.google.gerrit.ChangeJson.setLabelScores(ChangeJson.java:670)\n"
operator|+
literal|"\tat  \n"
operator|+
literal|"com.google.gerrit.ChangeJson.labelsFor(ChangeJson.java:845)\n"
operator|+
literal|"\tat  \n"
operator|+
literal|"com.google.gerrit.change.ChangeJson.labelsFor(ChangeJson.java:598)\n"
operator|+
literal|"\tat  \n"
operator|+
literal|"com.google.gerrit.change.ChangeJson.toChange(ChangeJson.java:499)\n"
operator|+
literal|"\tat com.google.gerrit.change.ChangeJson.format(ChangeJson.java:294)\n"
operator|+
literal|"\t... 105 more\n"
operator|+
literal|"-- \n"
operator|+
literal|"To view, visit https://gerrit-review.googlesource.com/90018\n"
operator|+
literal|"To unsubscribe, visit https://gerrit-review.googlesource.com\n"
operator|+
literal|"\n"
operator|+
literal|"Gerrit-MessageType: comment\n"
operator|+
literal|"Gerrit-Change-Id: Iba501e00bee77be3bd0ced72f88fd04ba0accaed\n"
operator|+
literal|"Gerrit-PatchSet: 2\n"
operator|+
literal|"Gerrit-Project: gerrit\n"
operator|+
literal|"Gerrit-Branch: master\n"
operator|+
literal|"Gerrit-Owner: ekempin<ekempin@google.com>\n"
operator|+
literal|"Gerrit-Reviewer: Dave Borowitz<dborowitz@google.com>\n"
operator|+
literal|"Gerrit-Reviewer: Edwin Kempin<ekempin@google.com>\n"
operator|+
literal|"Gerrit-Reviewer: GerritForge CI<gerritforge@gmail.com>\n"
operator|+
literal|"Gerrit-Reviewer: Jonathan Nieder<jrn@google.com>\n"
operator|+
literal|"Gerrit-Reviewer: Patrick Hiesel<hiesel@google.com>\n"
operator|+
literal|"Gerrit-Reviewer: ekempin<ekempin@google.com>\n"
operator|+
literal|"Gerrit-HasComments: No"
decl_stmt|;
DECL|field|raw
specifier|private
specifier|static
name|String
name|raw
init|=
literal|""
operator|+
literal|"Authentication-Results: mx.google.com; dkim=pass header.i="
operator|+
literal|"@google.com;\n"
operator|+
literal|"Date: Tue, 25 Oct 2016 02:11:35 -0700\n"
operator|+
literal|"In-Reply-To:<gerrit.1477487889000.Iba501e00bee77be3bd0ced"
operator|+
literal|"72f88fd04ba0accaed@gerrit-review.googlesource.com>\n"
operator|+
literal|"References:<gerrit.1477487889000.Iba501e00bee77be3bd0ced72f8"
operator|+
literal|"8fd04ba0accaed@gerrit-review.googlesource.com>\n"
operator|+
literal|"Message-ID:<001a114da7ae26e2eb053fe0c29c@google.com>\n"
operator|+
literal|"Subject: Change in gerrit[master]: (Re)enable voting buttons for "
operator|+
literal|"merged changes\n"
operator|+
literal|"From: \"Jonathan Nieder (Gerrit)\"<noreply-gerritcodereview-CtTy0"
operator|+
literal|"igsBrnvL7dKoWEIEg@google.com>\n"
operator|+
literal|"To: ekempin<ekempin@google.com>\n"
operator|+
literal|"Cc: Dave Borowitz<dborowitz@google.com>, Jonathan Nieder "
operator|+
literal|"<jrn@google.com>, Patrick Hiesel<hiesel@google.com>\n"
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
name|addCc
argument_list|(
operator|new
name|Address
argument_list|(
literal|"Dave Borowitz"
argument_list|,
literal|"dborowitz@google.com"
argument_list|)
argument_list|)
operator|.
name|addCc
argument_list|(
operator|new
name|Address
argument_list|(
literal|"Jonathan Nieder"
argument_list|,
literal|"jrn@google.com"
argument_list|)
argument_list|)
operator|.
name|addCc
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
name|subject
argument_list|(
literal|"Change in gerrit[master]: (Re)enable voting buttons for merged changes"
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
operator|.
name|addAdditionalHeader
argument_list|(
literal|"Authentication-Results: mx.google.com; dkim=pass header.i=@google.com;"
argument_list|)
operator|.
name|addAdditionalHeader
argument_list|(
literal|"In-Reply-To:<gerrit.1477487889000.Iba501e00bee"
operator|+
literal|"77be3bd0ced72f88fd04ba0accaed@gerrit-review.googlesource.com>"
argument_list|)
operator|.
name|addAdditionalHeader
argument_list|(
literal|"References:<gerrit.1477487889000.Iba501e00bee"
operator|+
literal|"77be3bd0ced72f88fd04ba0accaed@gerrit-review.googlesource.com>"
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

