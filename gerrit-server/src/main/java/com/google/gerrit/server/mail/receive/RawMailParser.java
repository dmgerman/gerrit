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
name|ImmutableSet
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
name|io
operator|.
name|CharStreams
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
name|Address
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|MimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|MessageBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|Multipart
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|TextBody
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|dom
operator|.
name|address
operator|.
name|Mailbox
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|james
operator|.
name|mime4j
operator|.
name|message
operator|.
name|DefaultMessageBuilder
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
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_comment
comment|/**  * RawMailParser parses raw email content received through POP3 or IMAP into  * an internal {@link MailMessage}.  */
end_comment

begin_class
DECL|class|RawMailParser
specifier|public
class|class
name|RawMailParser
block|{
DECL|field|MAIN_HEADERS
specifier|private
specifier|static
specifier|final
name|ImmutableSet
argument_list|<
name|String
argument_list|>
name|MAIN_HEADERS
init|=
name|ImmutableSet
operator|.
name|of
argument_list|(
literal|"to"
argument_list|,
literal|"from"
argument_list|,
literal|"cc"
argument_list|,
literal|"date"
argument_list|,
literal|"message-id"
argument_list|,
literal|"subject"
argument_list|,
literal|"content-type"
argument_list|)
decl_stmt|;
comment|/**    * Parses a MailMessage from a string.    * @param raw String as received over the wire    * @return Parsed MailMessage    * @throws MailParsingException    */
DECL|method|parse (String raw)
specifier|public
specifier|static
name|MailMessage
name|parse
parameter_list|(
name|String
name|raw
parameter_list|)
throws|throws
name|MailParsingException
block|{
name|MailMessage
operator|.
name|Builder
name|messageBuilder
init|=
name|MailMessage
operator|.
name|builder
argument_list|()
decl_stmt|;
name|Message
name|mimeMessage
decl_stmt|;
try|try
block|{
name|MessageBuilder
name|builder
init|=
operator|new
name|DefaultMessageBuilder
argument_list|()
decl_stmt|;
name|mimeMessage
operator|=
name|builder
operator|.
name|parseMessage
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|raw
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|MimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MailParsingException
argument_list|(
literal|"Can't parse email"
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Add general headers
name|messageBuilder
operator|.
name|id
argument_list|(
name|mimeMessage
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|messageBuilder
operator|.
name|subject
argument_list|(
name|mimeMessage
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|messageBuilder
operator|.
name|dateReceived
argument_list|(
operator|new
name|DateTime
argument_list|(
name|mimeMessage
operator|.
name|getDate
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add From, To and Cc
if|if
condition|(
name|mimeMessage
operator|.
name|getFrom
argument_list|()
operator|!=
literal|null
operator|&&
name|mimeMessage
operator|.
name|getFrom
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Mailbox
name|from
init|=
name|mimeMessage
operator|.
name|getFrom
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|messageBuilder
operator|.
name|from
argument_list|(
operator|new
name|Address
argument_list|(
name|from
operator|.
name|getName
argument_list|()
argument_list|,
name|from
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|mimeMessage
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Mailbox
name|m
range|:
name|mimeMessage
operator|.
name|getTo
argument_list|()
operator|.
name|flatten
argument_list|()
control|)
block|{
name|messageBuilder
operator|.
name|addTo
argument_list|(
operator|new
name|Address
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|mimeMessage
operator|.
name|getCc
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Mailbox
name|m
range|:
name|mimeMessage
operator|.
name|getCc
argument_list|()
operator|.
name|flatten
argument_list|()
control|)
block|{
name|messageBuilder
operator|.
name|addCc
argument_list|(
operator|new
name|Address
argument_list|(
name|m
operator|.
name|getName
argument_list|()
argument_list|,
name|m
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Add additional headers
name|mimeMessage
operator|.
name|getHeader
argument_list|()
operator|.
name|getFields
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|f
lambda|->
operator|!
name|MAIN_HEADERS
operator|.
name|contains
argument_list|(
name|f
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|f
lambda|->
name|messageBuilder
operator|.
name|addAdditionalHeader
argument_list|(
name|f
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|f
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// Add text and html body parts
name|StringBuilder
name|textBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|StringBuilder
name|htmlBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|handleMimePart
argument_list|(
name|mimeMessage
argument_list|,
name|textBuilder
argument_list|,
name|htmlBuilder
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MailParsingException
argument_list|(
literal|"Can't parse email"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|messageBuilder
operator|.
name|textContent
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|textBuilder
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|messageBuilder
operator|.
name|htmlContent
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|htmlBuilder
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// build() will only succeed if all required attributes were set. We wrap
comment|// the IllegalStateException in a MailParsingException indicating that
comment|// required attributes are missing, so that the caller doesn't fall over.
return|return
name|messageBuilder
operator|.
name|build
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MailParsingException
argument_list|(
literal|"Missing required attributes after email was parsed"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**    * Parses a MailMessage from an array of characters. Note that the character    * array is int-typed. This method is only used by POP3, which specifies that    * all transferred characters are US-ASCII (RFC 6856). When reading the input    * in Java, io.Reader yields ints. These can be safely converted to chars    * as all US-ASCII characters fit in a char. If emails contain non-ASCII    * characters, such as UTF runes, these will be encoded in ASCII using either    * Base64 or quoted-printable encoding.    * @param chars Array as received over the wire    * @return Parsed MailMessage    * @throws MailParsingException    */
DECL|method|parse (int[] chars)
specifier|public
specifier|static
name|MailMessage
name|parse
parameter_list|(
name|int
index|[]
name|chars
parameter_list|)
throws|throws
name|MailParsingException
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|(
name|chars
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|c
range|:
name|chars
control|)
block|{
name|b
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|parse
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Traverses a mime tree and parses out text and html parts. All other parts    * will be dropped.    * @param part MimePart to parse    * @param textBuilder StringBuilder to append all plaintext parts    * @param htmlBuilder StringBuilder to append all html parts    * @throws IOException    */
DECL|method|handleMimePart (Entity part, StringBuilder textBuilder, StringBuilder htmlBuilder)
specifier|private
specifier|static
name|void
name|handleMimePart
parameter_list|(
name|Entity
name|part
parameter_list|,
name|StringBuilder
name|textBuilder
parameter_list|,
name|StringBuilder
name|htmlBuilder
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isPlainOrHtml
argument_list|(
name|part
operator|.
name|getMimeType
argument_list|()
argument_list|)
operator|&&
operator|!
name|isAttachment
argument_list|(
name|part
operator|.
name|getDispositionType
argument_list|()
argument_list|)
condition|)
block|{
name|TextBody
name|tb
init|=
operator|(
name|TextBody
operator|)
name|part
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|CharStreams
operator|.
name|toString
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|tb
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|tb
operator|.
name|getMimeCharset
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|part
operator|.
name|getMimeType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"text/plain"
argument_list|)
condition|)
block|{
name|textBuilder
operator|.
name|append
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|part
operator|.
name|getMimeType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"text/html"
argument_list|)
condition|)
block|{
name|htmlBuilder
operator|.
name|append
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|isMixedOrAlternative
argument_list|(
name|part
operator|.
name|getMimeType
argument_list|()
argument_list|)
condition|)
block|{
name|Multipart
name|multipart
init|=
operator|(
name|Multipart
operator|)
name|part
operator|.
name|getBody
argument_list|()
decl_stmt|;
for|for
control|(
name|Entity
name|e
range|:
name|multipart
operator|.
name|getBodyParts
argument_list|()
control|)
block|{
name|handleMimePart
argument_list|(
name|e
argument_list|,
name|textBuilder
argument_list|,
name|htmlBuilder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isPlainOrHtml (String mimeType)
specifier|private
specifier|static
name|boolean
name|isPlainOrHtml
parameter_list|(
name|String
name|mimeType
parameter_list|)
block|{
return|return
operator|(
name|mimeType
operator|.
name|equals
argument_list|(
literal|"text/plain"
argument_list|)
operator|||
name|mimeType
operator|.
name|equals
argument_list|(
literal|"text/html"
argument_list|)
operator|)
return|;
block|}
DECL|method|isMixedOrAlternative (String mimeType)
specifier|private
specifier|static
name|boolean
name|isMixedOrAlternative
parameter_list|(
name|String
name|mimeType
parameter_list|)
block|{
return|return
name|mimeType
operator|.
name|equals
argument_list|(
literal|"multipart/alternative"
argument_list|)
operator|||
name|mimeType
operator|.
name|equals
argument_list|(
literal|"multipart/mixed"
argument_list|)
return|;
block|}
DECL|method|isAttachment (String dispositionType)
specifier|private
specifier|static
name|boolean
name|isAttachment
parameter_list|(
name|String
name|dispositionType
parameter_list|)
block|{
return|return
name|dispositionType
operator|!=
literal|null
operator|&&
name|dispositionType
operator|.
name|equals
argument_list|(
literal|"attachment"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

