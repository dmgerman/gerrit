begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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

begin_comment
comment|/**  * Message used as result of a validation that run during a git operation (for example {@code git  * push}. Intended to be shown to users.  */
end_comment

begin_class
DECL|class|ValidationMessage
specifier|public
class|class
name|ValidationMessage
block|{
DECL|enum|Type
specifier|public
enum|enum
name|Type
block|{
DECL|enumConstant|ERROR
name|ERROR
argument_list|(
literal|"ERROR: "
argument_list|)
block|,
DECL|enumConstant|WARNING
name|WARNING
argument_list|(
literal|"WARNING: "
argument_list|)
block|,
DECL|enumConstant|HINT
name|HINT
argument_list|(
literal|"hint: "
argument_list|)
block|,
DECL|enumConstant|OTHER
name|OTHER
argument_list|(
literal|""
argument_list|)
block|;
DECL|field|prefix
specifier|private
specifier|final
name|String
name|prefix
decl_stmt|;
DECL|method|Type (String prefix)
name|Type
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|this
operator|.
name|prefix
operator|=
name|prefix
expr_stmt|;
block|}
DECL|method|getPrefix ()
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
block|}
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
comment|/** @see ValidationMessage */
DECL|method|ValidationMessage (String message, Type type)
specifier|public
name|ValidationMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|Type
name|type
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
name|type
operator|=
name|type
expr_stmt|;
block|}
comment|// TODO: Remove and move callers to ValidationMessage(String message, Type type)
DECL|method|ValidationMessage (String message, boolean isError)
specifier|public
name|ValidationMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|boolean
name|isError
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
name|type
operator|=
operator|(
name|isError
condition|?
name|Type
operator|.
name|ERROR
else|:
name|Type
operator|.
name|OTHER
operator|)
expr_stmt|;
block|}
comment|/** Returns the message to be shown to the user. */
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
comment|/**    * Returns the {@link Type}. Used to as prefix for the message in the git CLI and to color    * messages.    */
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**    * Returns {@true} if this message is an error. Used to decide if the operation should be aborted.    */
DECL|method|isError ()
specifier|public
name|boolean
name|isError
parameter_list|()
block|{
return|return
name|type
operator|==
name|Type
operator|.
name|ERROR
return|;
block|}
block|}
end_class

end_unit

