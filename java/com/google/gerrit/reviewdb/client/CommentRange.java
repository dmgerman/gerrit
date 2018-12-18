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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
package|;
end_package

begin_class
DECL|class|CommentRange
specifier|public
class|class
name|CommentRange
block|{
DECL|field|startLine
specifier|protected
name|int
name|startLine
decl_stmt|;
DECL|field|startCharacter
specifier|protected
name|int
name|startCharacter
decl_stmt|;
DECL|field|endLine
specifier|protected
name|int
name|endLine
decl_stmt|;
DECL|field|endCharacter
specifier|protected
name|int
name|endCharacter
decl_stmt|;
DECL|method|CommentRange ()
specifier|protected
name|CommentRange
parameter_list|()
block|{}
DECL|method|CommentRange (int sl, int sc, int el, int ec)
specifier|public
name|CommentRange
parameter_list|(
name|int
name|sl
parameter_list|,
name|int
name|sc
parameter_list|,
name|int
name|el
parameter_list|,
name|int
name|ec
parameter_list|)
block|{
comment|// Start position is inclusive; end position is exclusive.
name|startLine
operator|=
name|sl
expr_stmt|;
comment|// 1-based
name|startCharacter
operator|=
name|sc
expr_stmt|;
comment|// 0-based
name|endLine
operator|=
name|el
expr_stmt|;
comment|// 1-based
name|endCharacter
operator|=
name|ec
expr_stmt|;
comment|// 0-based
block|}
DECL|method|getStartLine ()
specifier|public
name|int
name|getStartLine
parameter_list|()
block|{
return|return
name|startLine
return|;
block|}
DECL|method|getStartCharacter ()
specifier|public
name|int
name|getStartCharacter
parameter_list|()
block|{
return|return
name|startCharacter
return|;
block|}
DECL|method|getEndLine ()
specifier|public
name|int
name|getEndLine
parameter_list|()
block|{
return|return
name|endLine
return|;
block|}
DECL|method|getEndCharacter ()
specifier|public
name|int
name|getEndCharacter
parameter_list|()
block|{
return|return
name|endCharacter
return|;
block|}
DECL|method|setStartLine (int sl)
specifier|public
name|void
name|setStartLine
parameter_list|(
name|int
name|sl
parameter_list|)
block|{
name|startLine
operator|=
name|sl
expr_stmt|;
block|}
DECL|method|setStartCharacter (int sc)
specifier|public
name|void
name|setStartCharacter
parameter_list|(
name|int
name|sc
parameter_list|)
block|{
name|startCharacter
operator|=
name|sc
expr_stmt|;
block|}
DECL|method|setEndLine (int el)
specifier|public
name|void
name|setEndLine
parameter_list|(
name|int
name|el
parameter_list|)
block|{
name|endLine
operator|=
name|el
expr_stmt|;
block|}
DECL|method|setEndCharacter (int ec)
specifier|public
name|void
name|setEndCharacter
parameter_list|(
name|int
name|ec
parameter_list|)
block|{
name|endCharacter
operator|=
name|ec
expr_stmt|;
block|}
DECL|method|asCommentRange ()
specifier|public
name|Comment
operator|.
name|Range
name|asCommentRange
parameter_list|()
block|{
return|return
operator|new
name|Comment
operator|.
name|Range
argument_list|(
name|startLine
argument_list|,
name|startCharacter
argument_list|,
name|endLine
argument_list|,
name|endCharacter
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|CommentRange
condition|)
block|{
name|CommentRange
name|other
init|=
operator|(
name|CommentRange
operator|)
name|obj
decl_stmt|;
return|return
name|startLine
operator|==
name|other
operator|.
name|startLine
operator|&&
name|startCharacter
operator|==
name|other
operator|.
name|startCharacter
operator|&&
name|endLine
operator|==
name|other
operator|.
name|endLine
operator|&&
name|endCharacter
operator|==
name|other
operator|.
name|endCharacter
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|h
init|=
name|startLine
decl_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
name|startCharacter
expr_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
name|endLine
expr_stmt|;
name|h
operator|=
name|h
operator|*
literal|31
operator|+
name|endCharacter
expr_stmt|;
return|return
name|h
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
literal|"Range[startLine="
operator|+
name|startLine
operator|+
literal|", startCharacter="
operator|+
name|startCharacter
operator|+
literal|", endLine="
operator|+
name|endLine
operator|+
literal|", endCharacter="
operator|+
name|endCharacter
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

