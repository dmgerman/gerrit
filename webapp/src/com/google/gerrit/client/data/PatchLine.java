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

begin_comment
comment|/** A single line of a 2-way patch file. */
end_comment

begin_class
DECL|class|PatchLine
specifier|public
class|class
name|PatchLine
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
DECL|enumConstant|FILE_HEADER
name|FILE_HEADER
block|,
DECL|enumConstant|HUNK_HEADER
name|HUNK_HEADER
block|,
DECL|enumConstant|PRE_IMAGE
name|PRE_IMAGE
block|,
DECL|enumConstant|CONTEXT
name|CONTEXT
block|,
DECL|enumConstant|POST_IMAGE
name|POST_IMAGE
block|;   }
DECL|field|oldLineNumber
specifier|protected
name|int
name|oldLineNumber
decl_stmt|;
DECL|field|newLineNumber
specifier|protected
name|int
name|newLineNumber
decl_stmt|;
DECL|field|type
specifier|protected
name|PatchLine
operator|.
name|Type
name|type
decl_stmt|;
DECL|field|text
specifier|protected
name|String
name|text
decl_stmt|;
DECL|method|PatchLine ()
specifier|protected
name|PatchLine
parameter_list|()
block|{   }
DECL|method|PatchLine (final int oLine, final int nLine, final PatchLine.Type t, final String s)
specifier|public
name|PatchLine
parameter_list|(
specifier|final
name|int
name|oLine
parameter_list|,
specifier|final
name|int
name|nLine
parameter_list|,
specifier|final
name|PatchLine
operator|.
name|Type
name|t
parameter_list|,
specifier|final
name|String
name|s
parameter_list|)
block|{
name|oldLineNumber
operator|=
name|oLine
expr_stmt|;
name|newLineNumber
operator|=
name|nLine
expr_stmt|;
name|type
operator|=
name|t
expr_stmt|;
name|text
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getOldLineNumber ()
specifier|public
name|int
name|getOldLineNumber
parameter_list|()
block|{
return|return
name|oldLineNumber
return|;
block|}
DECL|method|getNewLineNumber ()
specifier|public
name|int
name|getNewLineNumber
parameter_list|()
block|{
return|return
name|newLineNumber
return|;
block|}
DECL|method|getType ()
specifier|public
name|PatchLine
operator|.
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
block|}
end_class

end_unit

