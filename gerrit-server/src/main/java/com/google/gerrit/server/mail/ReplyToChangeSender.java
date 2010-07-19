begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|Change
import|;
end_import

begin_comment
comment|/** Alert a user to a reply to a change, usually commentary made during review. */
end_comment

begin_class
DECL|class|ReplyToChangeSender
specifier|public
specifier|abstract
class|class
name|ReplyToChangeSender
extends|extends
name|OutgoingEmail
block|{
DECL|method|ReplyToChangeSender (EmailArguments ea, Change c, String mc)
specifier|protected
name|ReplyToChangeSender
parameter_list|(
name|EmailArguments
name|ea
parameter_list|,
name|Change
name|c
parameter_list|,
name|String
name|mc
parameter_list|)
block|{
name|super
argument_list|(
name|ea
argument_list|,
name|c
argument_list|,
name|mc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|init ()
specifier|protected
name|void
name|init
parameter_list|()
block|{
name|super
operator|.
name|init
argument_list|()
expr_stmt|;
specifier|final
name|String
name|threadId
init|=
name|getChangeMessageThreadId
argument_list|()
decl_stmt|;
name|setHeader
argument_list|(
literal|"In-Reply-To"
argument_list|,
name|threadId
argument_list|)
expr_stmt|;
name|setHeader
argument_list|(
literal|"References"
argument_list|,
name|threadId
argument_list|)
expr_stmt|;
name|rcptToAuthors
argument_list|(
name|RecipientType
operator|.
name|TO
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

