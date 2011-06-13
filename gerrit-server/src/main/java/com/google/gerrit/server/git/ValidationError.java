begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
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
package|;
end_package

begin_comment
comment|/** Indicates a problem with Git based data. */
end_comment

begin_class
DECL|class|ValidationError
specifier|public
class|class
name|ValidationError
block|{
DECL|field|message
specifier|private
specifier|final
name|String
name|message
decl_stmt|;
DECL|method|ValidationError (String file, String message)
specifier|public
name|ValidationError
parameter_list|(
name|String
name|file
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|file
operator|+
literal|": "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|ValidationError (String file, int line, String message)
specifier|public
name|ValidationError
parameter_list|(
name|String
name|file
parameter_list|,
name|int
name|line
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
argument_list|(
name|file
operator|+
literal|":"
operator|+
name|line
operator|+
literal|": "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|ValidationError (String message)
specifier|public
name|ValidationError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ValidationError["
operator|+
name|message
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

