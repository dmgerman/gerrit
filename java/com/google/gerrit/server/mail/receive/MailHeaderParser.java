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
name|flogger
operator|.
name|FluentLogger
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
name|primitives
operator|.
name|Ints
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
name|MailHeader
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
name|MailUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|time
operator|.
name|format
operator|.
name|DateTimeParseException
import|;
end_import

begin_comment
comment|/** Parse metadata from inbound email */
end_comment

begin_class
DECL|class|MailHeaderParser
specifier|public
class|class
name|MailHeaderParser
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|method|parse (MailMessage m)
specifier|public
specifier|static
name|MailMetadata
name|parse
parameter_list|(
name|MailMessage
name|m
parameter_list|)
block|{
name|MailMetadata
name|metadata
init|=
operator|new
name|MailMetadata
argument_list|()
decl_stmt|;
comment|// Find author
name|metadata
operator|.
name|author
operator|=
name|m
operator|.
name|from
argument_list|()
operator|.
name|getEmail
argument_list|()
expr_stmt|;
comment|// Check email headers for X-Gerrit-<Name>
for|for
control|(
name|String
name|header
range|:
name|m
operator|.
name|additionalHeaders
argument_list|()
control|)
block|{
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|MailHeader
operator|.
name|CHANGE_NUMBER
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|num
init|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|CHANGE_NUMBER
operator|.
name|fieldWithDelimiter
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|changeNumber
operator|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|num
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|MailHeader
operator|.
name|PATCH_SET
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|ps
init|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|PATCH_SET
operator|.
name|fieldWithDelimiter
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|metadata
operator|.
name|patchSet
operator|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|MailHeader
operator|.
name|COMMENT_DATE
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|ts
init|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|COMMENT_DATE
operator|.
name|fieldWithDelimiter
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
try|try
block|{
name|metadata
operator|.
name|timestamp
operator|=
name|Timestamp
operator|.
name|from
argument_list|(
name|MailUtil
operator|.
name|rfcDateformatter
operator|.
name|parse
argument_list|(
name|ts
argument_list|,
name|Instant
operator|::
name|from
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Mail: Error while parsing timestamp from header of message %s"
argument_list|,
name|m
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|header
operator|.
name|startsWith
argument_list|(
name|MailHeader
operator|.
name|MESSAGE_TYPE
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|messageType
operator|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|MESSAGE_TYPE
operator|.
name|fieldWithDelimiter
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|metadata
operator|.
name|hasRequiredFields
argument_list|()
condition|)
block|{
return|return
name|metadata
return|;
block|}
comment|// If the required fields were not yet found, continue to parse the text
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|m
operator|.
name|textContent
argument_list|()
argument_list|)
condition|)
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|split
argument_list|(
name|m
operator|.
name|textContent
argument_list|()
operator|.
name|replace
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
argument_list|)
decl_stmt|;
name|extractFooters
argument_list|(
name|lines
argument_list|,
name|metadata
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|hasRequiredFields
argument_list|()
condition|)
block|{
return|return
name|metadata
return|;
block|}
block|}
comment|// If the required fields were not yet found, continue to parse the HTML
comment|// HTML footer are contained inside a<div> tag
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|m
operator|.
name|htmlContent
argument_list|()
argument_list|)
condition|)
block|{
name|Iterable
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|"</div>"
argument_list|)
operator|.
name|split
argument_list|(
name|m
operator|.
name|htmlContent
argument_list|()
operator|.
name|replace
argument_list|(
literal|"\r\n"
argument_list|,
literal|"\n"
argument_list|)
argument_list|)
decl_stmt|;
name|extractFooters
argument_list|(
name|lines
argument_list|,
name|metadata
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|.
name|hasRequiredFields
argument_list|()
condition|)
block|{
return|return
name|metadata
return|;
block|}
block|}
return|return
name|metadata
return|;
block|}
DECL|method|extractFooters (Iterable<String> lines, MailMetadata metadata, MailMessage m)
specifier|private
specifier|static
name|void
name|extractFooters
parameter_list|(
name|Iterable
argument_list|<
name|String
argument_list|>
name|lines
parameter_list|,
name|MailMetadata
name|metadata
parameter_list|,
name|MailMessage
name|m
parameter_list|)
block|{
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
if|if
condition|(
name|metadata
operator|.
name|changeNumber
operator|==
literal|null
operator|&&
name|line
operator|.
name|contains
argument_list|(
name|MailHeader
operator|.
name|CHANGE_NUMBER
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|changeNumber
operator|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|extractFooter
argument_list|(
name|MailHeader
operator|.
name|CHANGE_NUMBER
operator|.
name|withDelimiter
argument_list|()
argument_list|,
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metadata
operator|.
name|patchSet
operator|==
literal|null
operator|&&
name|line
operator|.
name|contains
argument_list|(
name|MailHeader
operator|.
name|PATCH_SET
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|patchSet
operator|=
name|Ints
operator|.
name|tryParse
argument_list|(
name|extractFooter
argument_list|(
name|MailHeader
operator|.
name|PATCH_SET
operator|.
name|withDelimiter
argument_list|()
argument_list|,
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|metadata
operator|.
name|timestamp
operator|==
literal|null
operator|&&
name|line
operator|.
name|contains
argument_list|(
name|MailHeader
operator|.
name|COMMENT_DATE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|ts
init|=
name|extractFooter
argument_list|(
name|MailHeader
operator|.
name|COMMENT_DATE
operator|.
name|withDelimiter
argument_list|()
argument_list|,
name|line
argument_list|)
decl_stmt|;
try|try
block|{
name|metadata
operator|.
name|timestamp
operator|=
name|Timestamp
operator|.
name|from
argument_list|(
name|MailUtil
operator|.
name|rfcDateformatter
operator|.
name|parse
argument_list|(
name|ts
argument_list|,
name|Instant
operator|::
name|from
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DateTimeParseException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Mail: Error while parsing timestamp from footer of message %s"
argument_list|,
name|m
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|metadata
operator|.
name|messageType
operator|==
literal|null
operator|&&
name|line
operator|.
name|contains
argument_list|(
name|MailHeader
operator|.
name|MESSAGE_TYPE
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|metadata
operator|.
name|messageType
operator|=
name|extractFooter
argument_list|(
name|MailHeader
operator|.
name|MESSAGE_TYPE
operator|.
name|withDelimiter
argument_list|()
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|extractFooter (String key, String line)
specifier|private
specifier|static
name|String
name|extractFooter
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|line
parameter_list|)
block|{
return|return
name|line
operator|.
name|substring
argument_list|(
name|line
operator|.
name|indexOf
argument_list|(
name|key
argument_list|)
operator|+
name|key
operator|.
name|length
argument_list|()
argument_list|,
name|line
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
return|;
block|}
block|}
end_class

end_unit

