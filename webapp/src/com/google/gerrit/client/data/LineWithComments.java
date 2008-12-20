begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|data
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
name|client
operator|.
name|reviewdb
operator|.
name|PatchLineComment
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
DECL|class|LineWithComments
specifier|public
specifier|abstract
class|class
name|LineWithComments
block|{
DECL|field|comments
specifier|protected
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
decl_stmt|;
DECL|method|LineWithComments ()
specifier|protected
name|LineWithComments
parameter_list|()
block|{   }
DECL|method|getComments ()
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|getComments
parameter_list|()
block|{
return|return
name|comments
return|;
block|}
DECL|method|addComment (final PatchLineComment plc)
specifier|public
name|void
name|addComment
parameter_list|(
specifier|final
name|PatchLineComment
name|plc
parameter_list|)
block|{
if|if
condition|(
name|comments
operator|==
literal|null
condition|)
block|{
name|comments
operator|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
name|comments
operator|.
name|add
argument_list|(
name|plc
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

