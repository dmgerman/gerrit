begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail
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
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|receive
operator|.
name|MailMessage
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_comment
comment|/** Filters out auto-reply messages according to RFC 3834. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AutoReplyMailFilter
specifier|public
class|class
name|AutoReplyMailFilter
implements|implements
name|MailFilter
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
annotation|@
name|Override
DECL|method|shouldProcessMessage (MailMessage message)
specifier|public
name|boolean
name|shouldProcessMessage
parameter_list|(
name|MailMessage
name|message
parameter_list|)
block|{
for|for
control|(
name|String
name|header
range|:
name|message
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
name|PRECEDENCE
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|prec
init|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|PRECEDENCE
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
if|if
condition|(
name|prec
operator|.
name|equals
argument_list|(
literal|"list"
argument_list|)
operator|||
name|prec
operator|.
name|equals
argument_list|(
literal|"junk"
argument_list|)
operator|||
name|prec
operator|.
name|equals
argument_list|(
literal|"bulk"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Message %s has a Precedence header. Will ignore and delete message."
argument_list|,
name|message
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
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
name|AUTO_SUBMITTED
operator|.
name|fieldWithDelimiter
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|autoSubmitted
init|=
name|header
operator|.
name|substring
argument_list|(
name|MailHeader
operator|.
name|AUTO_SUBMITTED
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
if|if
condition|(
operator|!
name|autoSubmitted
operator|.
name|equals
argument_list|(
literal|"no"
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Message %s has an Auto-Submitted header. Will ignore and delete message."
argument_list|,
name|message
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

