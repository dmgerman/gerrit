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
name|Objects
import|;
end_import

begin_comment
comment|/** A comment parsed from inbound email */
end_comment

begin_class
DECL|class|MailComment
specifier|public
class|class
name|MailComment
block|{
DECL|enum|CommentType
specifier|public
enum|enum
name|CommentType
block|{
DECL|enumConstant|CHANGE_MESSAGE
name|CHANGE_MESSAGE
block|,
DECL|enumConstant|FILE_COMMENT
name|FILE_COMMENT
block|,
DECL|enumConstant|INLINE_COMMENT
name|INLINE_COMMENT
block|}
DECL|field|type
name|CommentType
name|type
decl_stmt|;
DECL|field|inReplyTo
name|Comment
name|inReplyTo
decl_stmt|;
DECL|field|fileName
name|String
name|fileName
decl_stmt|;
DECL|field|message
name|String
name|message
decl_stmt|;
DECL|field|isLink
name|boolean
name|isLink
decl_stmt|;
DECL|method|MailComment ()
specifier|public
name|MailComment
parameter_list|()
block|{}
DECL|method|MailComment ( String message, String fileName, Comment inReplyTo, CommentType type, boolean isLink)
specifier|public
name|MailComment
parameter_list|(
name|String
name|message
parameter_list|,
name|String
name|fileName
parameter_list|,
name|Comment
name|inReplyTo
parameter_list|,
name|CommentType
name|type
parameter_list|,
name|boolean
name|isLink
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
name|this
operator|.
name|inReplyTo
operator|=
name|inReplyTo
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|isLink
operator|=
name|isLink
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|CommentType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getInReplyTo ()
specifier|public
name|Comment
name|getInReplyTo
parameter_list|()
block|{
return|return
name|inReplyTo
return|;
block|}
DECL|method|getFileName ()
specifier|public
name|String
name|getFileName
parameter_list|()
block|{
return|return
name|fileName
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
comment|/**    * Checks if the provided comment concerns the same exact spot in the change. This is basically an    * equals method except that the message is not checked.    */
DECL|method|isSameCommentPath (MailComment c)
specifier|public
name|boolean
name|isSameCommentPath
parameter_list|(
name|MailComment
name|c
parameter_list|)
block|{
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|fileName
argument_list|,
name|c
operator|.
name|fileName
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|inReplyTo
argument_list|,
name|c
operator|.
name|inReplyTo
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|type
argument_list|,
name|c
operator|.
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

