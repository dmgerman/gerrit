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
DECL|package|com.google.gerrit.server.mail.send
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
name|send
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
name|testing
operator|.
name|GerritBaseTests
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
DECL|class|CommentSenderTest
specifier|public
class|class
name|CommentSenderTest
extends|extends
name|GerritBaseTests
block|{
DECL|class|TestSender
specifier|private
specifier|static
class|class
name|TestSender
extends|extends
name|CommentSender
block|{
DECL|method|TestSender ()
name|TestSender
parameter_list|()
throws|throws
name|OrmException
block|{
name|super
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
argument_list|)
expr_stmt|;
block|}
block|}
comment|// A 100-character long string.
DECL|field|chars100
specifier|private
specifier|static
name|String
name|chars100
init|=
name|String
operator|.
name|join
argument_list|(
literal|""
argument_list|,
name|Collections
operator|.
name|nCopies
argument_list|(
literal|25
argument_list|,
literal|"abcd"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|shortMessageNotShortened ()
specifier|public
name|void
name|shortMessageNotShortened
parameter_list|()
block|{
name|String
name|message
init|=
literal|"foo bar baz"
decl_stmt|;
name|assertThat
argument_list|(
name|TestSender
operator|.
name|getShortenedCommentMessage
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|message
operator|=
literal|"foo bar baz."
expr_stmt|;
name|assertThat
argument_list|(
name|TestSender
operator|.
name|getShortenedCommentMessage
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|longMessageIsShortened ()
specifier|public
name|void
name|longMessageIsShortened
parameter_list|()
block|{
name|String
name|message
init|=
name|chars100
operator|+
literal|"x"
decl_stmt|;
name|String
name|expected
init|=
name|chars100
operator|+
literal|" [â¦]"
decl_stmt|;
name|assertThat
argument_list|(
name|TestSender
operator|.
name|getShortenedCommentMessage
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shortenedToFirstLine ()
specifier|public
name|void
name|shortenedToFirstLine
parameter_list|()
block|{
name|String
name|message
init|=
literal|"abc\n"
operator|+
name|chars100
decl_stmt|;
name|String
name|expected
init|=
literal|"abc [â¦]"
decl_stmt|;
name|assertThat
argument_list|(
name|TestSender
operator|.
name|getShortenedCommentMessage
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shortenedToFirstSentence ()
specifier|public
name|void
name|shortenedToFirstSentence
parameter_list|()
block|{
name|String
name|message
init|=
literal|"foo bar baz. "
operator|+
name|chars100
decl_stmt|;
name|String
name|expected
init|=
literal|"foo bar baz. [â¦]"
decl_stmt|;
name|assertThat
argument_list|(
name|TestSender
operator|.
name|getShortenedCommentMessage
argument_list|(
name|message
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

