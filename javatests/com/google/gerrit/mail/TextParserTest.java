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
DECL|package|com.google.gerrit.mail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|entities
operator|.
name|Comment
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
DECL|class|TextParserTest
specifier|public
class|class
name|TextParserTest
extends|extends
name|AbstractParserTest
block|{
DECL|field|quotedFooter
specifier|private
specifier|static
specifier|final
name|String
name|quotedFooter
init|=
literal|""
operator|+
literal|"> To view, visit https://gerrit-review.googlesource.com/c/project/+/123\n"
operator|+
literal|"> To unsubscribe, visit https://gerrit-review.googlesource.com\n"
operator|+
literal|"> \n"
operator|+
literal|"> Gerrit-MessageType: comment\n"
operator|+
literal|"> Gerrit-Change-Id: Ie1234021bf1e8d1425641af58fd648fc011db153\n"
operator|+
literal|"> Gerrit-PatchSet: 1\n"
operator|+
literal|"> Gerrit-Project: gerrit\n"
operator|+
literal|"> Gerrit-Branch: master\n"
operator|+
literal|"> Gerrit-Owner: Foo Bar<foo@bar.com>\n"
operator|+
literal|"> Gerrit-HasComments: Yes"
decl_stmt|;
annotation|@
name|Test
DECL|method|simpleChangeMessage ()
specifier|public
name|void
name|simpleChangeMessage
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
literal|"Looks good to me\n"
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Looks good to me"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|simpleInlineComments ()
specifier|public
name|void
name|simpleInlineComments
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|newPlaintextBody
argument_list|(
literal|"Looks good to me"
argument_list|,
literal|"I have a comment on this."
argument_list|,
literal|null
argument_list|,
literal|"Also have a comment here."
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Looks good to me"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"I have a comment on this."
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"Also have a comment here."
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|simpleFileComment ()
specifier|public
name|void
name|simpleFileComment
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|newPlaintextBody
argument_list|(
literal|"Looks good to me"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"Also have a comment here."
argument_list|,
literal|"This is a nice file"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Looks good to me"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFileComment
argument_list|(
literal|"This is a nice file"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|key
operator|.
name|filename
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"Also have a comment here."
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noComments ()
specifier|public
name|void
name|noComments
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|newPlaintextBody
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noChangeMessage ()
specifier|public
name|void
name|noChangeMessage
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|newPlaintextBody
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"Also have a comment here."
argument_list|,
literal|"This is a nice file"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertFileComment
argument_list|(
literal|"This is a nice file"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|key
operator|.
name|filename
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"Also have a comment here."
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allCommentsGmail ()
specifier|public
name|void
name|allCommentsGmail
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
operator|(
name|newPlaintextBody
argument_list|(
literal|"Looks good to me"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"Also have a comment here."
argument_list|,
literal|"This is a nice file"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
operator|+
name|quotedFooter
operator|)
operator|.
name|replace
argument_list|(
literal|"> "
argument_list|,
literal|">> "
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Looks good to me"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertFileComment
argument_list|(
literal|"This is a nice file"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|key
operator|.
name|filename
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"Also have a comment here."
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|replyToFileComment ()
specifier|public
name|void
name|replyToFileComment
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
name|newPlaintextBody
argument_list|(
literal|"Looks good to me"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"Comment in reply to file comment"
argument_list|)
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Comment
argument_list|>
name|comments
init|=
name|defaultComments
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|comments
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Looks good to me"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertInlineComment
argument_list|(
literal|"Comment in reply to file comment"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|comments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|squashComments ()
specifier|public
name|void
name|squashComments
parameter_list|()
block|{
name|MailMessage
operator|.
name|Builder
name|b
init|=
name|newMailMessageBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|textContent
argument_list|(
literal|"Nice change\n> Some quoted content\nMy other comment on the same entity\n"
operator|+
name|quotedFooter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
name|TextParser
operator|.
name|parse
argument_list|(
name|b
operator|.
name|build
argument_list|()
argument_list|,
name|defaultComments
argument_list|()
argument_list|,
name|CHANGE_URL
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedComments
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertChangeMessage
argument_list|(
literal|"Nice change\n\nMy other comment on the same entity"
argument_list|,
name|parsedComments
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a plaintext message body with the specified comments.    *    * @param changeMessage    * @param c1 Comment in reply to first inline comment.    * @param c2 Comment in reply to second inline comment.    * @param c3 Comment in reply to third inline comment.    * @param f1 Comment on file one.    * @param f2 Comment on file two.    * @param fc1 Comment in reply to a comment of file 1.    * @return A string with all inline comments and the original quoted email.    */
DECL|method|newPlaintextBody ( String changeMessage, String c1, String c2, String c3, String f1, String f2, String fc1)
specifier|private
specifier|static
name|String
name|newPlaintextBody
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
return|return
operator|(
name|changeMessage
operator|==
literal|null
condition|?
literal|""
else|:
name|changeMessage
operator|+
literal|"\n"
operator|)
operator|+
literal|"On Thu, Feb 9, 2017 at 8:21 AM, ekempin (Gerrit)\n"
operator|+
literal|"<noreply-gerritcodereview-qUgXfQecoDLHwp0MldAzig@google.com> wrote: \n"
operator|+
literal|"> Foo Bar has posted comments on this change. (  \n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1 )\n"
operator|+
literal|"> \n"
operator|+
literal|"> Change subject: Test change\n"
operator|+
literal|"> ...............................................................\n"
operator|+
literal|"> \n"
operator|+
literal|"> \n"
operator|+
literal|"> Patch Set 1: Code-Review+1\n"
operator|+
literal|"> \n"
operator|+
literal|"> (3 comments)\n"
operator|+
literal|"> \n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt\n"
operator|+
literal|"> File  \n"
operator|+
literal|"> gerrit-server/test.txt:\n"
operator|+
operator|(
name|f1
operator|==
literal|null
condition|?
literal|""
else|:
name|f1
operator|+
literal|"\n"
operator|)
operator|+
literal|"> \n"
operator|+
literal|"> Patch Set #4:\n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt\n"
operator|+
literal|"> \n"
operator|+
literal|"> Some comment"
operator|+
literal|"> \n"
operator|+
operator|(
name|fc1
operator|==
literal|null
condition|?
literal|""
else|:
name|fc1
operator|+
literal|"\n"
operator|)
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt@2\n"
operator|+
literal|"> PS1, Line 2: throw new Exception(\"Object has unsupported: \" +\n"
operator|+
literal|">               :             entry.getValue() +\n"
operator|+
literal|">               :             \" must be java.util.Date\");\n"
operator|+
literal|"> Should entry.getKey() be included in this message?\n"
operator|+
literal|"> \n"
operator|+
operator|(
name|c1
operator|==
literal|null
condition|?
literal|""
else|:
name|c1
operator|+
literal|"\n"
operator|)
operator|+
literal|">\n"
operator|+
literal|"> \n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/test.txt@3\n"
operator|+
literal|"> PS1, Line 3: throw new Exception(\"Object has: \" +\n"
operator|+
literal|">               :             entry.getValue().getClass() +\n"
operator|+
literal|">              :             \" must be java.util.Date\");\n"
operator|+
literal|"> same here\n"
operator|+
literal|"> \n"
operator|+
operator|(
name|c2
operator|==
literal|null
condition|?
literal|""
else|:
name|c2
operator|+
literal|"\n"
operator|)
operator|+
literal|"> \n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/readme.txt\n"
operator|+
literal|"> File  \n"
operator|+
literal|"> gerrit-server/readme.txt:\n"
operator|+
operator|(
name|f2
operator|==
literal|null
condition|?
literal|""
else|:
name|f2
operator|+
literal|"\n"
operator|)
operator|+
literal|"> \n"
operator|+
literal|"> "
operator|+
name|CHANGE_URL
operator|+
literal|"/1/gerrit-server/readme.txt@3\n"
operator|+
literal|"> PS1, Line 3: E\n"
operator|+
literal|"> Should this be EEE like in other places?\n"
operator|+
operator|(
name|c3
operator|==
literal|null
condition|?
literal|""
else|:
name|c3
operator|+
literal|"\n"
operator|)
return|;
block|}
block|}
end_class

end_unit

