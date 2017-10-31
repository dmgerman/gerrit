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
DECL|package|com.google.gerrit.server.fixes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|fixes
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
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
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
name|Matcher
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

begin_comment
comment|/**  * An identifier of lines in a string. Lines are sequences of characters which are separated by any  * Unicode linebreak sequence as defined by the regular expression {@code \R}. If data for several  * lines is requested, calls which are ordered according to ascending line numbers are the most  * efficient.  */
end_comment

begin_class
DECL|class|LineIdentifier
class|class
name|LineIdentifier
block|{
DECL|field|LINE_SEPARATOR_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|LINE_SEPARATOR_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\R"
argument_list|)
decl_stmt|;
DECL|field|lineSeparatorMatcher
specifier|private
specifier|final
name|Matcher
name|lineSeparatorMatcher
decl_stmt|;
DECL|field|nextLineNumber
specifier|private
name|int
name|nextLineNumber
decl_stmt|;
DECL|field|nextLineStartIndex
specifier|private
name|int
name|nextLineStartIndex
decl_stmt|;
DECL|field|currentLineStartIndex
specifier|private
name|int
name|currentLineStartIndex
decl_stmt|;
DECL|field|currentLineEndIndex
specifier|private
name|int
name|currentLineEndIndex
decl_stmt|;
DECL|method|LineIdentifier (String string)
name|LineIdentifier
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|checkNotNull
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|lineSeparatorMatcher
operator|=
name|LINE_SEPARATOR_PATTERN
operator|.
name|matcher
argument_list|(
name|string
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
block|}
comment|/**    * Returns the start index of the indicated line within the given string. Start indices are    * zero-based while line numbers are one-based.    *    *<p><b>Note:</b> Requesting data for several lines is more efficient if those calls occur with    * increasing line number.    *    * @param lineNumber the line whose start index should be determined    * @return the start index of the line    * @throws StringIndexOutOfBoundsException if the line number is negative, zero or greater than    *     the identified number of lines    */
DECL|method|getStartIndexOfLine (int lineNumber)
specifier|public
name|int
name|getStartIndexOfLine
parameter_list|(
name|int
name|lineNumber
parameter_list|)
block|{
name|findLine
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
return|return
name|currentLineStartIndex
return|;
block|}
comment|/**    * Returns the length of the indicated line in the given string. The character(s) used to separate    * lines aren't included in the count. Line numbers are one-based.    *    *<p><b>Note:</b> Requesting data for several lines is more efficient if those calls occur with    * increasing line number.    *    * @param lineNumber the line whose length should be determined    * @return the length of the line    * @throws StringIndexOutOfBoundsException if the line number is negative, zero or greater than    *     the identified number of lines    */
DECL|method|getLengthOfLine (int lineNumber)
specifier|public
name|int
name|getLengthOfLine
parameter_list|(
name|int
name|lineNumber
parameter_list|)
block|{
name|findLine
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
return|return
name|currentLineEndIndex
operator|-
name|currentLineStartIndex
return|;
block|}
DECL|method|findLine (int targetLineNumber)
specifier|private
name|void
name|findLine
parameter_list|(
name|int
name|targetLineNumber
parameter_list|)
block|{
if|if
condition|(
name|targetLineNumber
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|StringIndexOutOfBoundsException
argument_list|(
literal|"Line number must be positive"
argument_list|)
throw|;
block|}
if|if
condition|(
name|targetLineNumber
operator|<
name|nextLineNumber
condition|)
block|{
name|reset
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
name|nextLineNumber
operator|<
name|targetLineNumber
operator|+
literal|1
operator|&&
name|lineSeparatorMatcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|currentLineStartIndex
operator|=
name|nextLineStartIndex
expr_stmt|;
name|currentLineEndIndex
operator|=
name|lineSeparatorMatcher
operator|.
name|start
argument_list|()
expr_stmt|;
name|nextLineStartIndex
operator|=
name|lineSeparatorMatcher
operator|.
name|end
argument_list|()
expr_stmt|;
name|nextLineNumber
operator|++
expr_stmt|;
block|}
comment|// End of string
if|if
condition|(
name|nextLineNumber
operator|==
name|targetLineNumber
condition|)
block|{
name|currentLineStartIndex
operator|=
name|nextLineStartIndex
expr_stmt|;
name|currentLineEndIndex
operator|=
name|lineSeparatorMatcher
operator|.
name|regionEnd
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|nextLineNumber
operator|<
name|targetLineNumber
condition|)
block|{
throw|throw
operator|new
name|StringIndexOutOfBoundsException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Line %d isn't available"
argument_list|,
name|targetLineNumber
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|reset ()
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|nextLineNumber
operator|=
literal|1
expr_stmt|;
name|nextLineStartIndex
operator|=
literal|0
expr_stmt|;
name|currentLineStartIndex
operator|=
literal|0
expr_stmt|;
name|currentLineEndIndex
operator|=
literal|0
expr_stmt|;
name|lineSeparatorMatcher
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

