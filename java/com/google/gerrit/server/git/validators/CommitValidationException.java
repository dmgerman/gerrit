begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.validators
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|server
operator|.
name|validators
operator|.
name|ValidationException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Exception thrown when a Git commit fails validations. Gerrit supports a wide range of validations  * (for example it validates any commits pushed to NoteDb refs for format compliance or allows to  * enforce commit message lengths to not exceed a certain length).  */
end_comment

begin_class
DECL|class|CommitValidationException
specifier|public
class|class
name|CommitValidationException
extends|extends
name|ValidationException
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
DECL|field|messages
specifier|private
specifier|final
name|ImmutableList
argument_list|<
name|CommitValidationMessage
argument_list|>
name|messages
decl_stmt|;
DECL|method|CommitValidationException (String reason, CommitValidationMessage message)
specifier|public
name|CommitValidationException
parameter_list|(
name|String
name|reason
parameter_list|,
name|CommitValidationMessage
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|reason
argument_list|)
expr_stmt|;
name|this
operator|.
name|messages
operator|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|CommitValidationException (String reason, List<CommitValidationMessage> messages)
specifier|public
name|CommitValidationException
parameter_list|(
name|String
name|reason
parameter_list|,
name|List
argument_list|<
name|CommitValidationMessage
argument_list|>
name|messages
parameter_list|)
block|{
name|super
argument_list|(
name|reason
argument_list|)
expr_stmt|;
name|this
operator|.
name|messages
operator|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|messages
argument_list|)
expr_stmt|;
block|}
DECL|method|CommitValidationException (String reason)
specifier|public
name|CommitValidationException
parameter_list|(
name|String
name|reason
parameter_list|)
block|{
name|super
argument_list|(
name|reason
argument_list|)
expr_stmt|;
name|this
operator|.
name|messages
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
DECL|method|CommitValidationException (String reason, Throwable why)
specifier|public
name|CommitValidationException
parameter_list|(
name|String
name|reason
parameter_list|,
name|Throwable
name|why
parameter_list|)
block|{
name|super
argument_list|(
name|reason
argument_list|,
name|why
argument_list|)
expr_stmt|;
name|this
operator|.
name|messages
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
comment|/** Returns all validation messages individually. */
DECL|method|getMessages ()
specifier|public
name|ImmutableList
argument_list|<
name|CommitValidationMessage
argument_list|>
name|getMessages
parameter_list|()
block|{
return|return
name|messages
return|;
block|}
comment|/** Returns all validation as a single, formatted string. */
DECL|method|getFullMessage ()
specifier|public
name|String
name|getFullMessage
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|messages
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
for|for
control|(
name|CommitValidationMessage
name|msg
range|:
name|messages
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n  "
argument_list|)
operator|.
name|append
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

