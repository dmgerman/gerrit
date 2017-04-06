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
DECL|package|com.google.gerrit.server.mail.receive
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
package|;
end_package

begin_comment
comment|/** Test parser for a generic Html email client response */
end_comment

begin_class
DECL|class|GenericHtmlParserTest
specifier|public
class|class
name|GenericHtmlParserTest
extends|extends
name|HtmlParserTest
block|{
annotation|@
name|Override
DECL|method|newHtmlBody ( String changeMessage, String c1, String c2, String c3, String f1, String f2, String fc1)
specifier|protected
name|String
name|newHtmlBody
parameter_list|(
name|String
name|changeMessage
parameter_list|,
name|String
name|c1
parameter_list|,
name|String
name|c2
parameter_list|,
name|String
name|c3
parameter_list|,
name|String
name|f1
parameter_list|,
name|String
name|f2
parameter_list|,
name|String
name|fc1
parameter_list|)
block|{
name|String
name|email
init|=
literal|""
operator|+
literal|"<div dir=\"ltr\">"
operator|+
operator|(
name|changeMessage
operator|!=
literal|null
condition|?
name|changeMessage
else|:
literal|""
operator|)
operator|+
literal|"<div class=\"extra\"><br><div class=\"quote\">"
operator|+
literal|"On Fri, Nov 18, 2016 at 11:15 AM, foobar (Gerrit) noreply@gerrit.com"
operator|+
literal|"<span dir=\"ltr\">&lt;<a href=\"mailto:noreply@gerrit.com\" "
operator|+
literal|"target=\"_blank\">noreply@gerrit.com</a>&gt;</span> wrote:<br>"
operator|+
literal|"<blockquote class=\"quote\" "
operator|+
literal|"<p>foobar<strong>posted comments</strong> on this change.</p>"
operator|+
literal|"<p><a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1\" "
operator|+
literal|"target=\"_blank\">View Change</a></p><div>Patch Set 2: CR-1\n"
operator|+
literal|"\n"
operator|+
literal|"(3 comments)</div><ul><li>"
operator|+
literal|"<p>"
operator|+
comment|// File #1: test.txt
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt\">"
operator|+
literal|"File gerrit-server/<wbr>test.txt:</a></p>"
operator|+
name|commentBlock
argument_list|(
name|f1
argument_list|)
operator|+
literal|"<li><p>"
operator|+
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt\">"
operator|+
literal|"Patch Set #2:</a></p>"
operator|+
literal|"<blockquote><pre>Some inline comment from Gerrit</pre>"
operator|+
literal|"</blockquote><p>Some comment on file 1</p>"
operator|+
literal|"</li>"
operator|+
name|commentBlock
argument_list|(
name|fc1
argument_list|)
operator|+
literal|"<li><p>"
operator|+
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt@2\">"
operator|+
literal|"Patch Set #2, Line 31:</a></p>"
operator|+
literal|"<blockquote><pre>Some inline comment from Gerrit</pre>"
operator|+
literal|"</blockquote><p>Some text from original comment</p>"
operator|+
literal|"</li>"
operator|+
name|commentBlock
argument_list|(
name|c1
argument_list|)
operator|+
literal|""
operator|+
comment|// Inline comment #2
literal|"<li><p>"
operator|+
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt@3\">"
operator|+
literal|"Patch Set #2, Line 47:</a></p>"
operator|+
literal|"<blockquote><pre>Some comment posted on Gerrit</pre>"
operator|+
literal|"</blockquote><p>Some more comments from Gerrit</p>"
operator|+
literal|"</li>"
operator|+
name|commentBlock
argument_list|(
name|c2
argument_list|)
operator|+
literal|"<li><p>"
operator|+
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt@115\">"
operator|+
literal|"Patch Set #2, Line 115:</a><code>some code</code></p>"
operator|+
literal|"<p>some comment</p></li></ul></li>"
operator|+
literal|""
operator|+
literal|"<li><p>"
operator|+
comment|// File #2: test.txt
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/readme.txt\">"
operator|+
literal|"File gerrit-server/<wbr>readme.txt:</a></p>"
operator|+
name|commentBlock
argument_list|(
name|f2
argument_list|)
operator|+
literal|"<li><p>"
operator|+
literal|"<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/readme.txt@3\">"
operator|+
literal|"Patch Set #2, Line 31:</a></p>"
operator|+
literal|"<blockquote><pre>Some inline comment from Gerrit</pre>"
operator|+
literal|"</blockquote><p>Some text from original comment</p>"
operator|+
literal|"</li>"
operator|+
name|commentBlock
argument_list|(
name|c3
argument_list|)
operator|+
literal|""
operator|+
comment|// Inline comment #2
literal|"</ul></li></ul>"
operator|+
literal|""
operator|+
comment|// Footer
literal|"<p>To view, visit<a href=\""
operator|+
name|CHANGE_URL
operator|+
literal|"/1\">this change</a>. "
operator|+
literal|"To unsubscribe, visit<a href=\"https://someurl\">settings</a>."
operator|+
literal|"</p><p>Gerrit-MessageType: comment<br>"
operator|+
literal|"Footer omitted</p>"
operator|+
literal|"<div><div></div></div>"
operator|+
literal|"<p>Gerrit-HasComments: Yes</p></blockquote></div><br></div></div>"
decl_stmt|;
return|return
name|email
return|;
block|}
DECL|method|commentBlock (String comment)
specifier|private
specifier|static
name|String
name|commentBlock
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
if|if
condition|(
name|comment
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
literal|"</ul></li></ul></blockquote><div>"
operator|+
name|comment
operator|+
literal|"</div><blockquote class=\"quote\"><ul><li><ul>"
return|;
block|}
block|}
end_class

end_unit

