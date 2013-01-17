begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|Lists
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
name|restapi
operator|.
name|BadRequestException
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

begin_class
DECL|class|BadRequestHandler
specifier|public
class|class
name|BadRequestHandler
block|{
DECL|field|errors
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|errors
init|=
name|Lists
operator|.
name|newLinkedList
argument_list|()
decl_stmt|;
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
DECL|method|BadRequestHandler (final String action)
specifier|public
name|BadRequestHandler
parameter_list|(
specifier|final
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|addError (final String message)
specifier|public
name|void
name|addError
parameter_list|(
specifier|final
name|String
name|message
parameter_list|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|addError (final Throwable t)
specifier|public
name|void
name|addError
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
name|errors
operator|.
name|add
argument_list|(
name|t
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|failOnError ()
specifier|public
name|void
name|failOnError
parameter_list|()
throws|throws
name|BadRequestException
block|{
if|if
condition|(
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|errors
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|action
operator|+
literal|" failed: "
operator|+
name|errors
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
throw|;
block|}
specifier|final
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"Multiple errors on "
operator|+
name|action
operator|+
literal|":"
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|String
name|error
range|:
name|errors
control|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

