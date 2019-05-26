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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

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
name|Iterators
import|;
end_import

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
name|PeekingIterator
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_comment
comment|/** Provides parsing functionality for plaintext email. */
end_comment

begin_class
DECL|class|TextParser
specifier|public
class|class
name|TextParser
block|{
DECL|method|TextParser ()
specifier|private
name|TextParser
parameter_list|()
block|{}
comment|/**    * Parses comments from plaintext email.    *    * @param email @param email the message as received from the email service    * @param comments list of {@link Comment}s previously persisted on the change that caused the    *     original notification email to be sent out. Ordering must be the same as in the outbound    *     email    * @param changeUrl canonical change url that points to the change on this Gerrit instance.    *     Example: https://go-review.googlesource.com/#/c/91570    * @return list of MailComments parsed from the plaintext part of the email    */
DECL|method|parse ( MailMessage email, Collection<Comment> comments, String changeUrl)
specifier|public
specifier|static
name|List
argument_list|<
name|MailComment
argument_list|>
name|parse
parameter_list|(
name|MailMessage
name|email
parameter_list|,
name|Collection
argument_list|<
name|Comment
argument_list|>
name|comments
parameter_list|,
name|String
name|changeUrl
parameter_list|)
block|{
name|String
name|body
init|=
name|email
operator|.
name|textContent
argument_list|()
decl_stmt|;
comment|// Replace CR-LF by \n
name|body
operator|=
name|body
operator|.
name|replace
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|MailComment
argument_list|>
name|parsedComments
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Some email clients (like GMail) use>> for enquoting text when there are
comment|// inline comments that the users typed. These will then be enquoted by a
comment|// single>. We sanitize this by unifying it into>. Inline comments typed
comment|// by the user will not be enquoted.
comment|//
comment|// Example:
comment|// Some comment
comment|//>> Quoted Text
comment|//>> Quoted Text
comment|//> A comment typed in the email directly
name|String
name|singleQuotePattern
init|=
literal|"\n> "
decl_stmt|;
name|String
name|doubleQuotePattern
init|=
literal|"\n>> "
decl_stmt|;
if|if
condition|(
name|countOccurrences
argument_list|(
name|body
argument_list|,
name|doubleQuotePattern
argument_list|)
operator|>
name|countOccurrences
argument_list|(
name|body
argument_list|,
name|singleQuotePattern
argument_list|)
condition|)
block|{
name|body
operator|=
name|body
operator|.
name|replace
argument_list|(
name|doubleQuotePattern
argument_list|,
name|singleQuotePattern
argument_list|)
expr_stmt|;
block|}
name|PeekingIterator
argument_list|<
name|Comment
argument_list|>
name|iter
init|=
name|Iterators
operator|.
name|peekingIterator
argument_list|(
name|comments
operator|.
name|iterator
argument_list|()
argument_list|)
decl_stmt|;
name|MailComment
name|currentComment
init|=
literal|null
decl_stmt|;
name|String
name|lastEncounteredFileName
init|=
literal|null
decl_stmt|;
name|Comment
name|lastEncounteredComment
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|)
control|)
block|{
if|if
condition|(
name|line
operator|.
name|equals
argument_list|(
literal|">"
argument_list|)
condition|)
block|{
comment|// Skip empty lines
continue|continue;
block|}
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"> "
argument_list|)
condition|)
block|{
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|"> "
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// This is not a comment, try to advance the file/comment pointers and
comment|// add previous comment to list if applicable
if|if
condition|(
name|currentComment
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|currentComment
operator|.
name|type
operator|==
name|MailComment
operator|.
name|CommentType
operator|.
name|CHANGE_MESSAGE
condition|)
block|{
name|currentComment
operator|.
name|message
operator|=
name|ParserUtil
operator|.
name|trimQuotation
argument_list|(
name|currentComment
operator|.
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|currentComment
operator|.
name|message
argument_list|)
condition|)
block|{
name|ParserUtil
operator|.
name|appendOrAddNewComment
argument_list|(
name|currentComment
argument_list|,
name|parsedComments
argument_list|)
expr_stmt|;
block|}
name|currentComment
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Comment
name|perspectiveComment
init|=
name|iter
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|equals
argument_list|(
name|ParserUtil
operator|.
name|filePath
argument_list|(
name|changeUrl
argument_list|,
name|perspectiveComment
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|lastEncounteredFileName
operator|==
literal|null
operator|||
operator|!
name|lastEncounteredFileName
operator|.
name|equals
argument_list|(
name|perspectiveComment
operator|.
name|key
operator|.
name|filename
argument_list|)
condition|)
block|{
comment|// This is the annotation of a file
name|lastEncounteredFileName
operator|=
name|perspectiveComment
operator|.
name|key
operator|.
name|filename
expr_stmt|;
name|lastEncounteredComment
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|perspectiveComment
operator|.
name|lineNbr
operator|==
literal|0
condition|)
block|{
comment|// This was originally a file-level comment
name|lastEncounteredComment
operator|=
name|perspectiveComment
expr_stmt|;
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|ParserUtil
operator|.
name|isCommentUrl
argument_list|(
name|line
argument_list|,
name|changeUrl
argument_list|,
name|perspectiveComment
argument_list|)
condition|)
block|{
name|lastEncounteredComment
operator|=
name|perspectiveComment
expr_stmt|;
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// This is a comment. Try to append to previous comment if applicable or
comment|// create a new comment.
if|if
condition|(
name|currentComment
operator|==
literal|null
condition|)
block|{
comment|// Start new comment
name|currentComment
operator|=
operator|new
name|MailComment
argument_list|()
expr_stmt|;
name|currentComment
operator|.
name|message
operator|=
name|line
expr_stmt|;
if|if
condition|(
name|lastEncounteredComment
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|lastEncounteredFileName
operator|==
literal|null
condition|)
block|{
comment|// Change message
name|currentComment
operator|.
name|type
operator|=
name|MailComment
operator|.
name|CommentType
operator|.
name|CHANGE_MESSAGE
expr_stmt|;
block|}
else|else
block|{
comment|// File comment not sent in reply to another comment
name|currentComment
operator|.
name|type
operator|=
name|MailComment
operator|.
name|CommentType
operator|.
name|FILE_COMMENT
expr_stmt|;
name|currentComment
operator|.
name|fileName
operator|=
name|lastEncounteredFileName
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Comment sent in reply to another comment
name|currentComment
operator|.
name|inReplyTo
operator|=
name|lastEncounteredComment
expr_stmt|;
name|currentComment
operator|.
name|type
operator|=
name|MailComment
operator|.
name|CommentType
operator|.
name|INLINE_COMMENT
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Attach to previous comment
name|currentComment
operator|.
name|message
operator|+=
literal|"\n"
operator|+
name|line
expr_stmt|;
block|}
block|}
block|}
comment|// There is no need to attach the currentComment after this loop as all
comment|// emails have footers and other enquoted text after the last comment
comment|// appeared and the last comment will have already been added to the list
comment|// at this point.
return|return
name|parsedComments
return|;
block|}
comment|/** Counts the occurrences of pattern in s */
DECL|method|countOccurrences (String s, String pattern)
specifier|private
specifier|static
name|int
name|countOccurrences
parameter_list|(
name|String
name|s
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
return|return
operator|(
name|s
operator|.
name|length
argument_list|()
operator|-
name|s
operator|.
name|replace
argument_list|(
name|pattern
argument_list|,
literal|""
argument_list|)
operator|.
name|length
argument_list|()
operator|)
operator|/
name|pattern
operator|.
name|length
argument_list|()
return|;
block|}
block|}
end_class

end_unit

