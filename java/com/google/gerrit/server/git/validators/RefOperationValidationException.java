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

begin_class
DECL|class|RefOperationValidationException
specifier|public
class|class
name|RefOperationValidationException
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
name|Iterable
argument_list|<
name|ValidationMessage
argument_list|>
name|messages
decl_stmt|;
DECL|method|RefOperationValidationException (String reason, Iterable<ValidationMessage> messages)
specifier|public
name|RefOperationValidationException
parameter_list|(
name|String
name|reason
parameter_list|,
name|Iterable
argument_list|<
name|ValidationMessage
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
name|messages
expr_stmt|;
block|}
DECL|method|getMessages ()
specifier|public
name|Iterable
argument_list|<
name|ValidationMessage
argument_list|>
name|getMessages
parameter_list|()
block|{
return|return
name|messages
return|;
block|}
annotation|@
name|Override
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|(
name|super
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ValidationMessage
name|error
range|:
name|messages
control|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
name|error
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

