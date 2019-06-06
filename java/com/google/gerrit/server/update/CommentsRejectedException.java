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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|collect
operator|.
name|ImmutableList
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
name|extensions
operator|.
name|validators
operator|.
name|CommentValidationFailure
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/** Thrown when comment validation rejected a comment, preventing it from being published. */
end_comment

begin_class
DECL|class|CommentsRejectedException
specifier|public
class|class
name|CommentsRejectedException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|commentValidationFailures
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|CommentValidationFailure
argument_list|>
name|commentValidationFailures
decl_stmt|;
DECL|method|CommentsRejectedException (Collection<CommentValidationFailure> commentValidationFailures)
specifier|public
name|CommentsRejectedException
parameter_list|(
name|Collection
argument_list|<
name|CommentValidationFailure
argument_list|>
name|commentValidationFailures
parameter_list|)
block|{
name|this
operator|.
name|commentValidationFailures
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|commentValidationFailures
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
literal|"One or more comments were rejected in validation: "
operator|+
name|commentValidationFailures
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|CommentValidationFailure
operator|::
name|getMessage
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|"; "
argument_list|)
argument_list|)
return|;
block|}
comment|/**    * Returns the validation failures that caused this exception. By contract this list is never    * empty.    */
DECL|method|getCommentValidationFailures ()
specifier|public
name|ImmutableList
argument_list|<
name|CommentValidationFailure
argument_list|>
name|getCommentValidationFailures
parameter_list|()
block|{
return|return
name|commentValidationFailures
return|;
block|}
block|}
end_class

end_unit

