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
name|EditPreferencesInfo
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
name|KeyMapType
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
DECL|class|EditPreferences
specifier|public
class|class
name|EditPreferences
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (EditPreferencesInfo in)
specifier|public
specifier|static
name|EditPreferences
name|create
parameter_list|(
name|EditPreferencesInfo
name|in
parameter_list|)
block|{
name|EditPreferences
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
name|indentUnit
argument_list|(
name|in
operator|.
name|indentUnit
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
name|hideTopMenu
argument_list|(
name|in
operator|.
name|hideTopMenu
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
name|hideLineNumbers
argument_list|(
name|in
operator|.
name|hideLineNumbers
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
name|p
operator|.
name|indentWithTabs
argument_list|(
name|in
operator|.
name|indentWithTabs
argument_list|)
expr_stmt|;
name|p
operator|.
name|autoCloseBrackets
argument_list|(
name|in
operator|.
name|autoCloseBrackets
argument_list|)
expr_stmt|;
name|p
operator|.
name|showBase
argument_list|(
name|in
operator|.
name|showBase
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
name|keyMapType
argument_list|(
name|in
operator|.
name|keyMapType
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|copyTo (EditPreferencesInfo p)
specifier|public
specifier|final
name|EditPreferencesInfo
name|copyTo
parameter_list|(
name|EditPreferencesInfo
name|p
parameter_list|)
block|{
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
name|indentUnit
operator|=
name|indentUnit
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
name|hideTopMenu
operator|=
name|hideTopMenu
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
name|syntaxHighlighting
operator|=
name|syntaxHighlighting
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
name|indentWithTabs
operator|=
name|indentWithTabs
argument_list|()
expr_stmt|;
name|p
operator|.
name|autoCloseBrackets
operator|=
name|autoCloseBrackets
argument_list|()
expr_stmt|;
name|p
operator|.
name|showBase
operator|=
name|showBase
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
name|keyMapType
operator|=
name|keyMapType
argument_list|()
expr_stmt|;
return|return
name|p
return|;
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
DECL|method|keyMapType (KeyMapType i)
specifier|public
specifier|final
name|void
name|keyMapType
parameter_list|(
name|KeyMapType
name|i
parameter_list|)
block|{
name|setkeyMapTypeRaw
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
name|KeyMapType
operator|.
name|DEFAULT
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setkeyMapTypeRaw (String i)
specifier|private
specifier|native
name|void
name|setkeyMapTypeRaw
parameter_list|(
name|String
name|i
parameter_list|)
comment|/*-{ this.key_map_type = i }-*/
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
DECL|method|indentUnit (int c)
specifier|public
specifier|final
specifier|native
name|void
name|indentUnit
parameter_list|(
name|int
name|c
parameter_list|)
comment|/*-{ this.indent_unit = c }-*/
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
DECL|method|showWhitespaceErrors ( boolean s)
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
DECL|method|hideLineNumbers (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|hideLineNumbers
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.hide_line_numbers = s }-*/
function_decl|;
DECL|method|matchBrackets (boolean m)
specifier|public
specifier|final
specifier|native
name|void
name|matchBrackets
parameter_list|(
name|boolean
name|m
parameter_list|)
comment|/*-{ this.match_brackets = m }-*/
function_decl|;
DECL|method|lineWrapping (boolean w)
specifier|public
specifier|final
specifier|native
name|void
name|lineWrapping
parameter_list|(
name|boolean
name|w
parameter_list|)
comment|/*-{ this.line_wrapping = w }-*/
function_decl|;
DECL|method|indentWithTabs (boolean w)
specifier|public
specifier|final
specifier|native
name|void
name|indentWithTabs
parameter_list|(
name|boolean
name|w
parameter_list|)
comment|/*-{ this.indent_with_tabs = w }-*/
function_decl|;
DECL|method|autoCloseBrackets (boolean c)
specifier|public
specifier|final
specifier|native
name|void
name|autoCloseBrackets
parameter_list|(
name|boolean
name|c
parameter_list|)
comment|/*-{ this.auto_close_brackets = c }-*/
function_decl|;
DECL|method|showBase (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showBase
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.show_base = s }-*/
function_decl|;
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
DECL|method|themeRaw ()
specifier|private
specifier|native
name|String
name|themeRaw
parameter_list|()
comment|/*-{ return this.theme }-*/
function_decl|;
DECL|method|keyMapType ()
specifier|public
specifier|final
name|KeyMapType
name|keyMapType
parameter_list|()
block|{
name|String
name|s
init|=
name|keyMapTypeRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|KeyMapType
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|KeyMapType
operator|.
name|DEFAULT
return|;
block|}
DECL|method|keyMapTypeRaw ()
specifier|private
specifier|native
name|String
name|keyMapTypeRaw
parameter_list|()
comment|/*-{ return this.key_map_type }-*/
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
DECL|method|indentUnit ()
specifier|public
specifier|final
name|int
name|indentUnit
parameter_list|()
block|{
return|return
name|get
argument_list|(
literal|"indent_unit"
argument_list|,
literal|2
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
DECL|method|hideTopMenu ()
specifier|public
specifier|final
specifier|native
name|boolean
name|hideTopMenu
parameter_list|()
comment|/*-{ return this.hide_top_menu || false }-*/
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
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|showWhitespaceErrors ()
name|showWhitespaceErrors
parameter_list|()
comment|/*-{ return this.show_whitespace_errors || false }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|syntaxHighlighting ()
name|syntaxHighlighting
parameter_list|()
comment|/*-{ return this.syntax_highlighting || false }-*/
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
DECL|method|indentWithTabs ()
specifier|public
specifier|final
specifier|native
name|boolean
name|indentWithTabs
parameter_list|()
comment|/*-{ return this.indent_with_tabs || false }-*/
function_decl|;
specifier|public
specifier|final
specifier|native
name|boolean
DECL|method|autoCloseBrackets ()
name|autoCloseBrackets
parameter_list|()
comment|/*-{ return this.auto_close_brackets || false }-*/
function_decl|;
DECL|method|showBase ()
specifier|public
specifier|final
specifier|native
name|boolean
name|showBase
parameter_list|()
comment|/*-{ return this.show_base || false }-*/
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
DECL|method|EditPreferences ()
specifier|protected
name|EditPreferences
parameter_list|()
block|{}
block|}
end_class

end_unit

