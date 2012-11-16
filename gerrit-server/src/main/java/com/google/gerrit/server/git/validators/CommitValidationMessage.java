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

begin_class
DECL|class|CommitValidationMessage
specifier|public
class|class
name|CommitValidationMessage
block|{
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|field|isError
specifier|private
specifier|final
name|boolean
name|isError
decl_stmt|;
DECL|method|CommitValidationMessage (final String message, final boolean isError)
specifier|public
name|CommitValidationMessage
parameter_list|(
specifier|final
name|String
name|message
parameter_list|,
specifier|final
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
name|isError
operator|=
name|isError
expr_stmt|;
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
DECL|method|isError ()
specifier|public
name|boolean
name|isError
parameter_list|()
block|{
return|return
name|isError
return|;
block|}
block|}
end_class

end_unit

