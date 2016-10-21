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
comment|/**  * RawMailParser parses raw email content received through POP3 or IMAP into  * an internal {@link MailMessage}.  */
end_comment

begin_class
DECL|class|RawMailParser
specifier|public
class|class
name|RawMailParser
block|{
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
comment|// TODO(hiesel) Implement.
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

