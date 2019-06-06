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
comment|/** A comment or review message was rejected by a {@link CommentValidator}. */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|CommentValidationFailure
specifier|public
specifier|abstract
class|class
name|CommentValidationFailure
block|{
DECL|method|create ( CommentForValidation commentForValidation, String message)
specifier|static
name|CommentValidationFailure
name|create
parameter_list|(
name|CommentForValidation
name|commentForValidation
parameter_list|,
name|String
name|message
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_CommentValidationFailure
argument_list|(
name|commentForValidation
argument_list|,
name|message
argument_list|)
return|;
block|}
comment|/** Returns the offending comment. */
DECL|method|getComment ()
specifier|public
specifier|abstract
name|CommentForValidation
name|getComment
parameter_list|()
function_decl|;
comment|/** A friendly message set by the {@link CommentValidator}. */
DECL|method|getMessage ()
specifier|public
specifier|abstract
name|String
name|getMessage
parameter_list|()
function_decl|;
block|}
end_class

end_unit

