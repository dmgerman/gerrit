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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|PushOneCommit
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
name|TestAccount
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
operator|.
name|CommentInput
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
name|extensions
operator|.
name|client
operator|.
name|Comment
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
name|extensions
operator|.
name|client
operator|.
name|Side
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
name|java
operator|.
name|util
operator|.
name|HashMap
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

begin_class
annotation|@
name|Ignore
DECL|class|AbstractMailIT
specifier|public
class|class
name|AbstractMailIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|method|messageBuilderWithDefaultFields ()
specifier|protected
name|MailMessage
operator|.
name|Builder
name|messageBuilderWithDefaultFields
parameter_list|()
block|{
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
name|user
operator|.
name|emailAddress
argument_list|)
expr_stmt|;
name|b
operator|.
name|addTo
argument_list|(
name|user
operator|.
name|emailAddress
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
return|return
name|b
return|;
block|}
DECL|method|createChangeWithReview ()
specifier|protected
name|String
name|createChangeWithReview
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|createChangeWithReview
argument_list|(
name|admin
argument_list|)
return|;
block|}
DECL|method|createChangeWithReview (TestAccount reviewer)
specifier|protected
name|String
name|createChangeWithReview
parameter_list|(
name|TestAccount
name|reviewer
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Create change
name|String
name|file
init|=
literal|"gerrit-server/test.txt"
decl_stmt|;
name|String
name|contents
init|=
literal|"contents \nlorem \nipsum \nlorem"
decl_stmt|;
name|PushOneCommit
name|push
init|=
name|pushFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|getIdent
argument_list|()
argument_list|,
name|testRepo
argument_list|,
literal|"first subject"
argument_list|,
name|file
argument_list|,
name|contents
argument_list|)
decl_stmt|;
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|push
operator|.
name|to
argument_list|(
literal|"refs/for/master"
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
comment|// Review it
name|setApiUser
argument_list|(
name|reviewer
argument_list|)
expr_stmt|;
name|ReviewInput
name|input
init|=
operator|new
name|ReviewInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|message
operator|=
literal|"I have two comments"
expr_stmt|;
name|input
operator|.
name|comments
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|CommentInput
name|c1
init|=
name|newComment
argument_list|(
name|file
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|,
literal|0
argument_list|,
literal|"comment on file"
argument_list|)
decl_stmt|;
name|CommentInput
name|c2
init|=
name|newComment
argument_list|(
name|file
argument_list|,
name|Side
operator|.
name|REVISION
argument_list|,
literal|2
argument_list|,
literal|"inline comment"
argument_list|)
decl_stmt|;
name|input
operator|.
name|comments
operator|.
name|put
argument_list|(
name|c1
operator|.
name|path
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|(
name|c1
argument_list|,
name|c2
argument_list|)
argument_list|)
expr_stmt|;
name|revision
argument_list|(
name|r
argument_list|)
operator|.
name|review
argument_list|(
name|input
argument_list|)
expr_stmt|;
return|return
name|changeId
return|;
block|}
DECL|method|newComment (String path, Side side, int line, String message)
specifier|protected
specifier|static
name|CommentInput
name|newComment
parameter_list|(
name|String
name|path
parameter_list|,
name|Side
name|side
parameter_list|,
name|int
name|line
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|CommentInput
name|c
init|=
operator|new
name|CommentInput
argument_list|()
decl_stmt|;
name|c
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|c
operator|.
name|side
operator|=
name|side
expr_stmt|;
name|c
operator|.
name|line
operator|=
name|line
operator|!=
literal|0
condition|?
name|line
else|:
literal|null
expr_stmt|;
name|c
operator|.
name|message
operator|=
name|message
expr_stmt|;
if|if
condition|(
name|line
operator|!=
literal|0
condition|)
block|{
name|Comment
operator|.
name|Range
name|range
init|=
operator|new
name|Comment
operator|.
name|Range
argument_list|()
decl_stmt|;
name|range
operator|.
name|startLine
operator|=
name|line
expr_stmt|;
name|range
operator|.
name|startCharacter
operator|=
literal|1
expr_stmt|;
name|range
operator|.
name|endLine
operator|=
name|line
expr_stmt|;
name|range
operator|.
name|endCharacter
operator|=
literal|5
expr_stmt|;
name|c
operator|.
name|range
operator|=
name|range
expr_stmt|;
block|}
return|return
name|c
return|;
block|}
comment|/**    * Create a plaintext message body with the specified comments.    *    * @param changeMessage    * @param c1 Comment in reply to first inline comment.    * @param f1 Comment on file one.    * @param fc1 Comment in reply to a comment of file 1.    * @return A string with all inline comments and the original quoted email.    */
DECL|method|newPlaintextBody ( String changeURL, String changeMessage, String c1, String f1, String fc1)
specifier|protected
specifier|static
name|String
name|newPlaintextBody
parameter_list|(
name|String
name|changeURL
parameter_list|,
name|String
name|changeMessage
parameter_list|,
name|String
name|c1
parameter_list|,
name|String
name|f1
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
literal|"> Foo Bar has posted comments on this change. (  \n"
operator|+
literal|"> "
operator|+
name|changeURL
operator|+
literal|" )\n"
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
name|changeURL
operator|+
literal|"/gerrit-server/test.txt\n"
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
name|changeURL
operator|+
literal|"/gerrit-server/test.txt\n"
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
name|changeURL
operator|+
literal|"/gerrit-server/test.txt@2\n"
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
literal|"> \n"
return|;
block|}
DECL|method|textFooterForChange (int changeNumber, String timestamp)
specifier|protected
specifier|static
name|String
name|textFooterForChange
parameter_list|(
name|int
name|changeNumber
parameter_list|,
name|String
name|timestamp
parameter_list|)
block|{
return|return
literal|"Gerrit-Change-Number: "
operator|+
name|changeNumber
operator|+
literal|"\n"
operator|+
literal|"Gerrit-PatchSet: 1\n"
operator|+
literal|"Gerrit-MessageType: comment\n"
operator|+
literal|"Gerrit-Comment-Date: "
operator|+
name|timestamp
operator|+
literal|"\n"
return|;
block|}
block|}
end_class

end_unit

