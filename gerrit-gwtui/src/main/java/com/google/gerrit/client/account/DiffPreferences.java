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
name|extensions
operator|.
name|client
operator|.
name|DiffPreferencesInfo
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
name|client
operator|.
name|DiffPreferencesInfo
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
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|Theme
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
DECL|method|create (DiffPreferencesInfo in)
specifier|public
specifier|static
name|DiffPreferences
name|create
parameter_list|(
name|DiffPreferencesInfo
name|in
parameter_list|)
block|{
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
name|in
operator|=
name|DiffPreferencesInfo
operator|.
name|defaults
argument_list|()
expr_stmt|;
block|}
name|DiffPreferences
name|p
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|p
operator|.
name|ignoreWhitespace
argument_list|(
name|in
operator|.
name|ignoreWhitespace
argument_list|)
expr_stmt|;
name|p
operator|.
name|tabSize
argument_list|(
name|in
operator|.
name|tabSize
argument_list|)
expr_stmt|;
name|p
operator|.
name|lineLength
argument_list|(
name|in
operator|.
name|lineLength
argument_list|)
expr_stmt|;
name|p
operator|.
name|cursorBlinkRate
argument_list|(
name|in
operator|.
name|cursorBlinkRate
argument_list|)
expr_stmt|;
name|p
operator|.
name|context
argument_list|(
name|in
operator|.
name|context
argument_list|)
expr_stmt|;
name|p
operator|.
name|intralineDifference
argument_list|(
name|in
operator|.
name|intralineDifference
argument_list|)
expr_stmt|;
name|p
operator|.
name|showLineEndings
argument_list|(
name|in
operator|.
name|showLineEndings
argument_list|)
expr_stmt|;
name|p
operator|.
name|showTabs
argument_list|(
name|in
operator|.
name|showTabs
argument_list|)
expr_stmt|;
name|p
operator|.
name|showWhitespaceErrors
argument_list|(
name|in
operator|.
name|showWhitespaceErrors
argument_list|)
expr_stmt|;
name|p
operator|.
name|syntaxHighlighting
argument_list|(
name|in
operator|.
name|syntaxHighlighting
argument_list|)
expr_stmt|;
name|p
operator|.
name|hideTopMenu
argument_list|(
name|in
operator|.
name|hideTopMenu
argument_list|)
expr_stmt|;
name|p
operator|.
name|autoHideDiffTableHeader
argument_list|(
name|in
operator|.
name|autoHideDiffTableHeader
argument_list|)
expr_stmt|;
name|p
operator|.
name|hideLineNumbers
argument_list|(
name|in
operator|.
name|hideLineNumbers
argument_list|)
expr_stmt|;
name|p
operator|.
name|expandAllComments
argument_list|(
name|in
operator|.
name|expandAllComments
argument_list|)
expr_stmt|;
name|p
operator|.
name|manualReview
argument_list|(
name|in
operator|.
name|manualReview
argument_list|)
expr_stmt|;
name|p
operator|.
name|renderEntireFile
argument_list|(
name|in
operator|.
name|renderEntireFile
argument_list|)
expr_stmt|;
name|p
operator|.
name|theme
argument_list|(
name|in
operator|.
name|theme
argument_list|)
expr_stmt|;
name|p
operator|.
name|hideEmptyPane
argument_list|(
name|in
operator|.
name|hideEmptyPane
argument_list|)
expr_stmt|;
name|p
operator|.
name|retainHeader
argument_list|(
name|in
operator|.
name|retainHeader
argument_list|)
expr_stmt|;
name|p
operator|.
name|skipUnchanged
argument_list|(
name|in
operator|.
name|skipUnchanged
argument_list|)
expr_stmt|;
name|p
operator|.
name|skipUncommented
argument_list|(
name|in
operator|.
name|skipUncommented
argument_list|)
expr_stmt|;
name|p
operator|.
name|skipDeleted
argument_list|(
name|in
operator|.
name|skipDeleted
argument_list|)
expr_stmt|;
name|p
operator|.
name|matchBrackets
argument_list|(
name|in
operator|.
name|matchBrackets
argument_list|)
expr_stmt|;
name|p
operator|.
name|lineWrapping
argument_list|(
name|in
operator|.
name|lineWrapping
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|copyTo (DiffPreferencesInfo p)
specifier|public
specifier|final
name|void
name|copyTo
parameter_list|(
name|DiffPreferencesInfo
name|p
parameter_list|)
block|{
name|p
operator|.
name|context
operator|=
name|context
argument_list|()
expr_stmt|;
name|p
operator|.
name|tabSize
operator|=
name|tabSize
argument_list|()
expr_stmt|;
name|p
operator|.
name|lineLength
operator|=
name|lineLength
argument_list|()
expr_stmt|;
name|p
operator|.
name|cursorBlinkRate
operator|=
name|cursorBlinkRate
argument_list|()
expr_stmt|;
name|p
operator|.
name|expandAllComments
operator|=
name|expandAllComments
argument_list|()
expr_stmt|;
name|p
operator|.
name|intralineDifference
operator|=
name|intralineDifference
argument_list|()
expr_stmt|;
name|p
operator|.
name|manualReview
operator|=
name|manualReview
argument_list|()
expr_stmt|;
name|p
operator|.
name|retainHeader
operator|=
name|retainHeader
argument_list|()
expr_stmt|;
name|p
operator|.
name|showLineEndings
operator|=
name|showLineEndings
argument_list|()
expr_stmt|;
name|p
operator|.
name|showTabs
operator|=
name|showTabs
argument_list|()
expr_stmt|;
name|p
operator|.
name|showWhitespaceErrors
operator|=
name|showWhitespaceErrors
argument_list|()
expr_stmt|;
name|p
operator|.
name|skipDeleted
operator|=
name|skipDeleted
argument_list|()
expr_stmt|;
name|p
operator|.
name|skipUnchanged
operator|=
name|skipUnchanged
argument_list|()
expr_stmt|;
name|p
operator|.
name|skipUncommented
operator|=
name|skipUncommented
argument_list|()
expr_stmt|;
name|p
operator|.
name|syntaxHighlighting
operator|=
name|syntaxHighlighting
argument_list|()
expr_stmt|;
name|p
operator|.
name|hideTopMenu
operator|=
name|hideTopMenu
argument_list|()
expr_stmt|;
name|p
operator|.
name|autoHideDiffTableHeader
operator|=
name|autoHideDiffTableHeader
argument_list|()
expr_stmt|;
name|p
operator|.
name|hideLineNumbers
operator|=
name|hideLineNumbers
argument_list|()
expr_stmt|;
name|p
operator|.
name|renderEntireFile
operator|=
name|renderEntireFile
argument_list|()
expr_stmt|;
name|p
operator|.
name|hideEmptyPane
operator|=
name|hideEmptyPane
argument_list|()
expr_stmt|;
name|p
operator|.
name|matchBrackets
operator|=
name|matchBrackets
argument_list|()
expr_stmt|;
name|p
operator|.
name|lineWrapping
operator|=
name|lineWrapping
argument_list|()
expr_stmt|;
name|p
operator|.
name|theme
operator|=
name|theme
argument_list|()
expr_stmt|;
name|p
operator|.
name|ignoreWhitespace
operator|=
name|ignoreWhitespace
argument_list|()
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
DECL|method|theme (Theme i)
specifier|public
specifier|final
name|void
name|theme
parameter_list|(
name|Theme
name|i
parameter_list|)
block|{
name|setThemeRaw
argument_list|(
name|i
operator|!=
literal|null
condition|?
name|i
operator|.
name|toString
argument_list|()
else|:
name|Theme
operator|.
name|DEFAULT
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|showLineNumbers (boolean s)
specifier|public
specifier|final
name|void
name|showLineNumbers
parameter_list|(
name|boolean
name|s
parameter_list|)
block|{
name|hideLineNumbers
argument_list|(
operator|!
name|s
argument_list|)
expr_stmt|;
block|}
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
DECL|method|theme ()
specifier|public
specifier|final
name|Theme
name|theme
parameter_list|()
block|{
name|String
name|s
init|=
name|themeRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|Theme
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|Theme
operator|.
name|DEFAULT
return|;
block|}
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
DECL|method|lineLength ()
specifier|public
specifier|final
name|int
name|lineLength
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"line_length"
argument_list|,
literal|100
argument_list|)
return|;
block|}
DECL|method|cursorBlinkRate ()
specifier|public
specifier|final
name|int
name|cursorBlinkRate
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"cursor_blink_rate"
argument_list|,
literal|0
argument_list|)
return|;
block|}
DECL|method|showLineNumbers ()
specifier|public
specifier|final
name|boolean
name|showLineNumbers
parameter_list|()
block|{
return|return
operator|!
name|hideLineNumbers
argument_list|()
return|;
block|}
DECL|method|autoReview ()
specifier|public
specifier|final
name|boolean
name|autoReview
parameter_list|()
block|{
return|return
operator|!
name|manualReview
argument_list|()
return|;
block|}
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
DECL|method|lineLength (int c)
specifier|public
specifier|final
specifier|native
name|void
name|lineLength
parameter_list|(
name|int
name|c
parameter_list|)
comment|/*-{ this.line_length = c }-*/
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
DECL|method|cursorBlinkRate (int r)
specifier|public
specifier|final
specifier|native
name|void
name|cursorBlinkRate
parameter_list|(
name|int
name|r
parameter_list|)
comment|/*-{ this.cursor_blink_rate = r }-*/
function_decl|;
DECL|method|intralineDifference (Boolean i)
specifier|public
specifier|final
specifier|native
name|void
name|intralineDifference
parameter_list|(
name|Boolean
name|i
parameter_list|)
comment|/*-{ this.intraline_difference = i }-*/
function_decl|;
DECL|method|showLineEndings (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showLineEndings
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.show_line_endings = s }-*/
function_decl|;
DECL|method|showTabs (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showTabs
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.show_tabs = s }-*/
function_decl|;
DECL|method|showWhitespaceErrors (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showWhitespaceErrors
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.show_whitespace_errors = s }-*/
function_decl|;
DECL|method|syntaxHighlighting (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|syntaxHighlighting
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.syntax_highlighting = s }-*/
function_decl|;
DECL|method|hideTopMenu (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|hideTopMenu
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.hide_top_menu = s }-*/
function_decl|;
DECL|method|autoHideDiffTableHeader (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|autoHideDiffTableHeader
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.auto_hide_diff_table_header = s }-*/
function_decl|;
DECL|method|hideLineNumbers (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|hideLineNumbers
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.hide_line_numbers = s }-*/
function_decl|;
DECL|method|expandAllComments (Boolean e)
specifier|public
specifier|final
specifier|native
name|void
name|expandAllComments
parameter_list|(
name|Boolean
name|e
parameter_list|)
comment|/*-{ this.expand_all_comments = e }-*/
function_decl|;
DECL|method|manualReview (Boolean r)
specifier|public
specifier|final
specifier|native
name|void
name|manualReview
parameter_list|(
name|Boolean
name|r
parameter_list|)
comment|/*-{ this.manual_review = r }-*/
function_decl|;
DECL|method|renderEntireFile (Boolean r)
specifier|public
specifier|final
specifier|native
name|void
name|renderEntireFile
parameter_list|(
name|Boolean
name|r
parameter_list|)
comment|/*-{ this.render_entire_file = r }-*/
function_decl|;
DECL|method|retainHeader (Boolean r)
specifier|public
specifier|final
specifier|native
name|void
name|retainHeader
parameter_list|(
name|Boolean
name|r
parameter_list|)
comment|/*-{ this.retain_header = r }-*/
function_decl|;
DECL|method|hideEmptyPane (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|hideEmptyPane
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.hide_empty_pane = s }-*/
function_decl|;
DECL|method|skipUnchanged (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|skipUnchanged
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.skip_unchanged = s }-*/
function_decl|;
DECL|method|skipUncommented (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|skipUncommented
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.skip_uncommented = s }-*/
function_decl|;
DECL|method|skipDeleted (Boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|skipDeleted
parameter_list|(
name|Boolean
name|s
parameter_list|)
comment|/*-{ this.skip_deleted = s }-*/
function_decl|;
DECL|method|matchBrackets (Boolean m)
specifier|public
specifier|final
specifier|native
name|void
name|matchBrackets
parameter_list|(
name|Boolean
name|m
parameter_list|)
comment|/*-{ this.match_brackets = m }-*/
function_decl|;
DECL|method|lineWrapping (Boolean w)
specifier|public
specifier|final
specifier|native
name|void
name|lineWrapping
parameter_list|(
name|Boolean
name|w
parameter_list|)
comment|/*-{ this.line_wrapping = w }-*/
function_decl|;
DECL|method|intralineDifference ()
specifier|public
specifier|final
specifier|native
name|boolean
name|intralineDifference
parameter_list|()
comment|/*-{ return this.intraline_difference || false }-*/
function_decl|;
DECL|method|showLineEndings ()
specifier|public
specifier|final
specifier|native
name|boolean
name|showLineEndings
parameter_list|()
comment|/*-{ return this.show_line_endings || false }-*/
function_decl|;
DECL|method|showTabs ()
specifier|public
specifier|final
specifier|native
name|boolean
name|showTabs
parameter_list|()
comment|/*-{ return this.show_tabs || false }-*/
function_decl|;
DECL|method|showWhitespaceErrors ()
specifier|public
specifier|final
specifier|native
name|boolean
name|showWhitespaceErrors
parameter_list|()
comment|/*-{ return this.show_whitespace_errors || false }-*/
function_decl|;
DECL|method|syntaxHighlighting ()
specifier|public
specifier|final
specifier|native
name|boolean
name|syntaxHighlighting
parameter_list|()
comment|/*-{ return this.syntax_highlighting || false }-*/
function_decl|;
DECL|method|hideTopMenu ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hideTopMenu
parameter_list|()
comment|/*-{ return this.hide_top_menu || false }-*/
function_decl|;
DECL|method|autoHideDiffTableHeader ()
specifier|public
specifier|final
specifier|native
name|boolean
name|autoHideDiffTableHeader
parameter_list|()
comment|/*-{ return this.auto_hide_diff_table_header || false }-*/
function_decl|;
DECL|method|hideLineNumbers ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hideLineNumbers
parameter_list|()
comment|/*-{ return this.hide_line_numbers || false }-*/
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
DECL|method|manualReview ()
specifier|public
specifier|final
specifier|native
name|boolean
name|manualReview
parameter_list|()
comment|/*-{ return this.manual_review || false }-*/
function_decl|;
DECL|method|renderEntireFile ()
specifier|public
specifier|final
specifier|native
name|boolean
name|renderEntireFile
parameter_list|()
comment|/*-{ return this.render_entire_file || false }-*/
function_decl|;
DECL|method|hideEmptyPane ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hideEmptyPane
parameter_list|()
comment|/*-{ return this.hide_empty_pane || false }-*/
function_decl|;
DECL|method|retainHeader ()
specifier|public
specifier|final
specifier|native
name|boolean
name|retainHeader
parameter_list|()
comment|/*-{ return this.retain_header || false }-*/
function_decl|;
DECL|method|skipUnchanged ()
specifier|public
specifier|final
specifier|native
name|boolean
name|skipUnchanged
parameter_list|()
comment|/*-{ return this.skip_unchanged || false }-*/
function_decl|;
DECL|method|skipUncommented ()
specifier|public
specifier|final
specifier|native
name|boolean
name|skipUncommented
parameter_list|()
comment|/*-{ return this.skip_uncommented || false }-*/
function_decl|;
DECL|method|skipDeleted ()
specifier|public
specifier|final
specifier|native
name|boolean
name|skipDeleted
parameter_list|()
comment|/*-{ return this.skip_deleted || false }-*/
function_decl|;
DECL|method|matchBrackets ()
specifier|public
specifier|final
specifier|native
name|boolean
name|matchBrackets
parameter_list|()
comment|/*-{ return this.match_brackets || false }-*/
function_decl|;
DECL|method|lineWrapping ()
specifier|public
specifier|final
specifier|native
name|boolean
name|lineWrapping
parameter_list|()
comment|/*-{ return this.line_wrapping || false }-*/
function_decl|;
DECL|method|setThemeRaw (String i)
specifier|private
specifier|native
name|void
name|setThemeRaw
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.theme = i }-*/
function_decl|;
DECL|method|setIgnoreWhitespaceRaw (String i)
specifier|private
specifier|native
name|void
name|setIgnoreWhitespaceRaw
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.ignore_whitespace = i }-*/
function_decl|;
DECL|method|ignoreWhitespaceRaw ()
specifier|private
specifier|native
name|String
name|ignoreWhitespaceRaw
parameter_list|()
comment|/*-{ return this.ignore_whitespace }-*/
function_decl|;
DECL|method|themeRaw ()
specifier|private
specifier|native
name|String
name|themeRaw
parameter_list|()
comment|/*-{ return this.theme }-*/
function_decl|;
DECL|method|get (String n, int d)
specifier|private
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
DECL|method|DiffPreferences ()
specifier|protected
name|DiffPreferences
parameter_list|()
block|{   }
block|}
end_class

end_unit

