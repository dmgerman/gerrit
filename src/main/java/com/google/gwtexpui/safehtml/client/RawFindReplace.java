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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|regexp
operator|.
name|shared
operator|.
name|RegExp
import|;
end_import

begin_comment
comment|/**  * A Find/Replace pair whose replacement string is arbitrary HTML.  *<p>  *<b>WARNING:</b> This class is not safe used with user-provided patterns.  */
end_comment

begin_class
DECL|class|RawFindReplace
specifier|public
class|class
name|RawFindReplace
implements|implements
name|FindReplace
block|{
DECL|field|pat
specifier|private
name|RegExp
name|pat
decl_stmt|;
DECL|field|replace
specifier|private
name|String
name|replace
decl_stmt|;
DECL|method|RawFindReplace ()
specifier|protected
name|RawFindReplace
parameter_list|()
block|{   }
comment|/**    * @param regex regular expression pattern to match substrings with.    * @param repl replacement expression. Capture groups within    *<code>regex</code> can be referenced with<code>$<i>n</i></code>.    */
DECL|method|RawFindReplace (String find, String replace)
specifier|public
name|RawFindReplace
parameter_list|(
name|String
name|find
parameter_list|,
name|String
name|replace
parameter_list|)
block|{
name|this
operator|.
name|pat
operator|=
name|RegExp
operator|.
name|compile
argument_list|(
name|find
argument_list|)
expr_stmt|;
name|this
operator|.
name|replace
operator|=
name|replace
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pattern ()
specifier|public
name|RegExp
name|pattern
parameter_list|()
block|{
return|return
name|pat
return|;
block|}
annotation|@
name|Override
DECL|method|replace (String input)
specifier|public
name|String
name|replace
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|pat
operator|.
name|replace
argument_list|(
name|input
argument_list|,
name|replace
argument_list|)
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
name|pat
operator|.
name|getSource
argument_list|()
operator|+
literal|", replace = "
operator|+
name|replace
return|;
block|}
block|}
end_class

end_unit

