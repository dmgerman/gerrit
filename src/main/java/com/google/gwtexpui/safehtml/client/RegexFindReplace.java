begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_comment
comment|/** A Find/Replace pair used against the SafeHtml block of text */
end_comment

begin_class
DECL|class|RegexFindReplace
specifier|public
class|class
name|RegexFindReplace
block|{
DECL|field|find
specifier|private
name|String
name|find
decl_stmt|;
DECL|field|replace
specifier|private
name|String
name|replace
decl_stmt|;
DECL|method|RegexFindReplace ()
specifier|protected
name|RegexFindReplace
parameter_list|()
block|{   }
DECL|method|RegexFindReplace (final String find, final String replace)
specifier|public
name|RegexFindReplace
parameter_list|(
specifier|final
name|String
name|find
parameter_list|,
specifier|final
name|String
name|replace
parameter_list|)
block|{
name|this
operator|.
name|find
operator|=
name|find
expr_stmt|;
name|this
operator|.
name|replace
operator|=
name|replace
expr_stmt|;
block|}
DECL|method|find ()
specifier|public
name|String
name|find
parameter_list|()
block|{
return|return
name|find
return|;
block|}
DECL|method|replace ()
specifier|public
name|String
name|replace
parameter_list|()
block|{
return|return
name|replace
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
literal|"find = "
operator|+
name|find
operator|+
literal|", replace = "
operator|+
name|replace
return|;
block|}
block|}
end_class

end_unit

