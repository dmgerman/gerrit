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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountDiffPreference
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountDiffPreference
operator|.
name|Whitespace
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_class
DECL|class|DiffPreferences
specifier|public
class|class
name|DiffPreferences
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (AccountDiffPreference in)
specifier|public
specifier|static
name|DiffPreferences
name|create
parameter_list|(
name|AccountDiffPreference
name|in
parameter_list|)
block|{
name|DiffPreferences
name|p
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
name|AccountDiffPreference
operator|.
name|createDefault
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|p
operator|.
name|ignoreWhitespace
argument_list|(
name|in
operator|.
name|getIgnoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|tabSize
argument_list|(
name|in
operator|.
name|getTabSize
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|context
argument_list|(
name|in
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|intralineDifference
argument_list|(
name|in
operator|.
name|isIntralineDifference
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|showLineEndings
argument_list|(
name|in
operator|.
name|isShowLineEndings
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|showTabs
argument_list|(
name|in
operator|.
name|isShowTabs
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|showWhitespaceErrors
argument_list|(
name|in
operator|.
name|isShowWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|syntaxHighlighting
argument_list|(
name|in
operator|.
name|isSyntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|hideTopMenu
argument_list|(
name|in
operator|.
name|isHideTopMenu
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|expandAllComments
argument_list|(
name|in
operator|.
name|isExpandAllComments
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|copyTo (AccountDiffPreference p)
specifier|public
specifier|final
name|void
name|copyTo
parameter_list|(
name|AccountDiffPreference
name|p
parameter_list|)
block|{
name|p
operator|.
name|setIgnoreWhitespace
argument_list|(
name|ignoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setTabSize
argument_list|(
name|tabSize
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setContext
argument_list|(
operator|(
name|short
operator|)
name|context
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setIntralineDifference
argument_list|(
name|intralineDifference
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowLineEndings
argument_list|(
name|showLineEndings
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowTabs
argument_list|(
name|showTabs
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowWhitespaceErrors
argument_list|(
name|showWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setSyntaxHighlighting
argument_list|(
name|syntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setHideTopMenu
argument_list|(
name|hideTopMenu
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setExpandAllComments
argument_list|(
name|expandAllComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ignoreWhitespace (Whitespace i)
specifier|public
specifier|final
name|void
name|ignoreWhitespace
parameter_list|(
name|Whitespace
name|i
parameter_list|)
block|{
name|setIgnoreWhitespaceRaw
argument_list|(
name|i
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setIgnoreWhitespaceRaw (String i)
specifier|private
specifier|final
specifier|native
name|void
name|setIgnoreWhitespaceRaw
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.ignore_whitespace = i }-*/
function_decl|;
DECL|method|tabSize (int t)
specifier|public
specifier|final
specifier|native
name|void
name|tabSize
parameter_list|(
name|int
name|t
parameter_list|)
comment|/*-{ this.tab_size = t }-*/
function_decl|;
DECL|method|context (int c)
specifier|public
specifier|final
specifier|native
name|void
name|context
parameter_list|(
name|int
name|c
parameter_list|)
comment|/*-{ this.context = c }-*/
function_decl|;
DECL|method|intralineDifference (boolean i)
specifier|public
specifier|final
specifier|native
name|void
name|intralineDifference
parameter_list|(
name|boolean
name|i
parameter_list|)
comment|/*-{ this.intraline_difference = i }-*/
function_decl|;
DECL|method|showLineEndings (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showLineEndings
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.show_line_endings = s }-*/
function_decl|;
DECL|method|showTabs (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showTabs
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.show_tabs = s }-*/
function_decl|;
DECL|method|showWhitespaceErrors (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showWhitespaceErrors
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.show_whitespace_errors = s }-*/
function_decl|;
DECL|method|syntaxHighlighting (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|syntaxHighlighting
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.syntax_highlighting = s }-*/
function_decl|;
DECL|method|hideTopMenu (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|hideTopMenu
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.hide_top_menu = s }-*/
function_decl|;
DECL|method|expandAllComments (boolean e)
specifier|public
specifier|final
specifier|native
name|void
name|expandAllComments
parameter_list|(
name|boolean
name|e
parameter_list|)
comment|/*-{ this.expand_all_comments = e }-*/
function_decl|;
DECL|method|ignoreWhitespace ()
specifier|public
specifier|final
name|Whitespace
name|ignoreWhitespace
parameter_list|()
block|{
name|String
name|s
init|=
name|ignoreWhitespaceRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|Whitespace
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|Whitespace
operator|.
name|IGNORE_NONE
return|;
block|}
DECL|method|ignoreWhitespaceRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|ignoreWhitespaceRaw
parameter_list|()
comment|/*-{ return this.ignore_whitespace }-*/
function_decl|;
DECL|method|tabSize ()
specifier|public
specifier|final
name|int
name|tabSize
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"tab_size"
argument_list|,
literal|8
argument_list|)
return|;
block|}
DECL|method|context ()
specifier|public
specifier|final
name|int
name|context
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"context"
argument_list|,
literal|10
argument_list|)
return|;
block|}
DECL|method|intralineDifference ()
specifier|public
specifier|final
name|boolean
name|intralineDifference
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"intraline_difference"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|showLineEndings ()
specifier|public
specifier|final
name|boolean
name|showLineEndings
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"show_line_endings"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|showTabs ()
specifier|public
specifier|final
name|boolean
name|showTabs
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"show_tabs"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|showWhitespaceErrors ()
specifier|public
specifier|final
name|boolean
name|showWhitespaceErrors
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"show_whitespace_errors"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|syntaxHighlighting ()
specifier|public
specifier|final
name|boolean
name|syntaxHighlighting
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"syntax_highlighting"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|hideTopMenu ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hideTopMenu
parameter_list|()
comment|/*-{ return this.hide_top_menu || false }-*/
function_decl|;
DECL|method|expandAllComments ()
specifier|public
specifier|final
specifier|native
name|boolean
name|expandAllComments
parameter_list|()
comment|/*-{ return this.expand_all_comments || false }-*/
function_decl|;
DECL|method|get (String n, int d)
specifier|private
specifier|final
specifier|native
name|int
name|get
parameter_list|(
name|String
name|n
parameter_list|,
name|int
name|d
parameter_list|)
comment|/*-{ return this.hasOwnProperty(n) ? this[n] : d }-*/
function_decl|;
DECL|method|get (String n, boolean d)
specifier|private
specifier|final
specifier|native
name|boolean
name|get
parameter_list|(
name|String
name|n
parameter_list|,
name|boolean
name|d
parameter_list|)
comment|/*-{ return this.hasOwnProperty(n) ? this[n] : d }-*/
function_decl|;
DECL|method|DiffPreferences ()
specifier|protected
name|DiffPreferences
parameter_list|()
block|{   }
block|}
end_class

end_unit

