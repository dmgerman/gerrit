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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|regex
operator|.
name|Pattern
import|;
end_import

begin_class
DECL|class|ParserUtil
specifier|public
class|class
name|ParserUtil
block|{
DECL|field|SIMPLE_EMAIL_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SIMPLE_EMAIL_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+"
operator|+
literal|"(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})"
argument_list|)
decl_stmt|;
comment|/**    * Trims the quotation line that email clients add    * Example: On Sun, Nov 20, 2016 at 10:33 PM,<gerrit@hiesel.it> wrote:    * @param comment Comment parsed from an email.    * @return Trimmed comment.    */
DECL|method|trimQuotationLine (String comment)
specifier|public
specifier|static
name|String
name|trimQuotationLine
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
comment|// Identifying the quotation line is hard, as it can be in any language.
comment|// We identify this line by it's characteristics: It usually contains a
comment|// valid email address, some digits for the date in groups of 1-4 in a row
comment|// as well as some characters.
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|comment
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|)
control|)
block|{
comment|// Count occurrences of digit groups
name|int
name|numConsecutiveDigits
init|=
literal|0
decl_stmt|;
name|int
name|maxConsecutiveDigits
init|=
literal|0
decl_stmt|;
name|int
name|numDigitGroups
init|=
literal|0
decl_stmt|;
for|for
control|(
name|char
name|c
range|:
name|line
operator|.
name|toCharArray
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|>=
literal|'0'
operator|&&
name|c
operator|<=
literal|'9'
condition|)
block|{
name|numConsecutiveDigits
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|numConsecutiveDigits
operator|>
literal|0
condition|)
block|{
name|maxConsecutiveDigits
operator|=
name|Integer
operator|.
name|max
argument_list|(
name|maxConsecutiveDigits
argument_list|,
name|numConsecutiveDigits
argument_list|)
expr_stmt|;
name|numConsecutiveDigits
operator|=
literal|0
expr_stmt|;
name|numDigitGroups
operator|++
expr_stmt|;
block|}
block|}
if|if
condition|(
name|numDigitGroups
argument_list|<
literal|4
operator|||
name|maxConsecutiveDigits
argument_list|>
literal|4
operator|||
operator|!
name|SIMPLE_EMAIL_PATTERN
operator|.
name|matcher
argument_list|(
name|line
argument_list|)
operator|.
name|find
argument_list|()
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
return|;
block|}
comment|/** Check if string is an inline comment url on a patch set or the base */
DECL|method|isCommentUrl (String str, String changeUrl, Comment comment)
specifier|public
specifier|static
name|boolean
name|isCommentUrl
parameter_list|(
name|String
name|str
parameter_list|,
name|String
name|changeUrl
parameter_list|,
name|Comment
name|comment
parameter_list|)
block|{
return|return
name|str
operator|.
name|equals
argument_list|(
name|filePath
argument_list|(
name|changeUrl
argument_list|,
name|comment
argument_list|)
operator|+
literal|"@"
operator|+
name|comment
operator|.
name|lineNbr
argument_list|)
operator|||
name|str
operator|.
name|equals
argument_list|(
name|filePath
argument_list|(
name|changeUrl
argument_list|,
name|comment
argument_list|)
operator|+
literal|"@a"
operator|+
name|comment
operator|.
name|lineNbr
argument_list|)
return|;
block|}
comment|/** Generate the fully qualified filepath */
DECL|method|filePath (String changeUrl, Comment comment)
specifier|public
specifier|static
name|String
name|filePath
parameter_list|(
name|String
name|changeUrl
parameter_list|,
name|Comment
name|comment
parameter_list|)
block|{
return|return
name|changeUrl
operator|+
literal|"/"
operator|+
name|comment
operator|.
name|key
operator|.
name|patchSetId
operator|+
literal|"/"
operator|+
name|comment
operator|.
name|key
operator|.
name|filename
return|;
block|}
block|}
end_class

end_unit

