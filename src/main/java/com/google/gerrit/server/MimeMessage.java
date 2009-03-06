begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Session
import|;
end_import

begin_class
DECL|class|MimeMessage
class|class
name|MimeMessage
extends|extends
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
block|{
DECL|method|MimeMessage (final Session session)
name|MimeMessage
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|)
block|{
name|super
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
DECL|field|messageID
specifier|private
name|String
name|messageID
decl_stmt|;
DECL|method|setMessageID (final String id)
name|void
name|setMessageID
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
name|messageID
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateMessageID ()
specifier|protected
name|void
name|updateMessageID
parameter_list|()
throws|throws
name|MessagingException
block|{
if|if
condition|(
name|messageID
operator|!=
literal|null
condition|)
block|{
name|setHeader
argument_list|(
literal|"Message-ID"
argument_list|,
name|messageID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|updateMessageID
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

