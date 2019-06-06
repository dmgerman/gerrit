begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|validators
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_comment
comment|/**  * Holds a comment's text and {@link CommentType} in order to pass it to a validation plugin.  *  * @see CommentValidator  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|CommentForValidation
specifier|public
specifier|abstract
class|class
name|CommentForValidation
block|{
comment|/** The type of comment. */
DECL|enum|CommentType
specifier|public
enum|enum
name|CommentType
block|{
comment|/** A regular (inline) comment. */
DECL|enumConstant|INLINE_COMMENT
name|INLINE_COMMENT
block|,
comment|/** A file comment. */
DECL|enumConstant|FILE_COMMENT
name|FILE_COMMENT
block|,
comment|/** A change message. */
DECL|enumConstant|CHANGE_MESSAGE
name|CHANGE_MESSAGE
block|}
DECL|method|create (CommentType type, String text)
specifier|public
specifier|static
name|CommentForValidation
name|create
parameter_list|(
name|CommentType
name|type
parameter_list|,
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_CommentForValidation
argument_list|(
name|type
argument_list|,
name|text
argument_list|)
return|;
block|}
DECL|method|getType ()
specifier|public
specifier|abstract
name|CommentType
name|getType
parameter_list|()
function_decl|;
DECL|method|getText ()
specifier|public
specifier|abstract
name|String
name|getText
parameter_list|()
function_decl|;
DECL|method|failValidation (String message)
specifier|public
name|CommentValidationFailure
name|failValidation
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
name|CommentValidationFailure
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

