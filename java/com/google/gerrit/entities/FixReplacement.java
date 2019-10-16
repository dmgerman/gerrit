begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

begin_class
DECL|class|FixReplacement
specifier|public
class|class
name|FixReplacement
block|{
DECL|field|path
specifier|public
name|String
name|path
decl_stmt|;
DECL|field|range
specifier|public
name|Comment
operator|.
name|Range
name|range
decl_stmt|;
DECL|field|replacement
specifier|public
name|String
name|replacement
decl_stmt|;
DECL|method|FixReplacement (String path, Comment.Range range, String replacement)
specifier|public
name|FixReplacement
parameter_list|(
name|String
name|path
parameter_list|,
name|Comment
operator|.
name|Range
name|range
parameter_list|,
name|String
name|replacement
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|range
operator|=
name|range
expr_stmt|;
name|this
operator|.
name|replacement
operator|=
name|replacement
expr_stmt|;
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
literal|"FixReplacement{"
operator|+
literal|"path='"
operator|+
name|path
operator|+
literal|'\''
operator|+
literal|", range="
operator|+
name|range
operator|+
literal|", replacement='"
operator|+
name|replacement
operator|+
literal|'\''
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

