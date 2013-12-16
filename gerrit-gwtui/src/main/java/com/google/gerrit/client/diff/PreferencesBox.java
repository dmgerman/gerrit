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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_import
import|import static
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
name|DEFAULT_CONTEXT
import|;
end_import

begin_import
import|import static
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
name|WHOLE_FILE_CONTEXT
import|;
end_import

begin_import
import|import static
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
operator|.
name|IGNORE_ALL_SPACE
import|;
end_import

begin_import
import|import static
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
operator|.
name|IGNORE_NONE
import|;
end_import

begin_import
import|import static
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
operator|.
name|IGNORE_SPACE_AT_EOL
import|;
end_import

begin_import
import|import static
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
operator|.
name|IGNORE_SPACE_CHANGE
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyCodes
operator|.
name|KEY_ESCAPE
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
name|client
operator|.
name|Gerrit
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
name|client
operator|.
name|account
operator|.
name|AccountApi
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
name|client
operator|.
name|account
operator|.
name|DiffPreferences
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
name|client
operator|.
name|patches
operator|.
name|PatchUtil
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
name|client
operator|.
name|rpc
operator|.
name|GerritCallback
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
name|client
operator|.
name|ui
operator|.
name|NpIntTextBox
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
name|GWT
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ChangeEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownEvent
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyDownHandler
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ValueChangeEvent
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
name|resources
operator|.
name|client
operator|.
name|CssResource
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|uibinder
operator|.
name|client
operator|.
name|UiHandler
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
name|user
operator|.
name|client
operator|.
name|Timer
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Anchor
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Button
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|CheckBox
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|HTMLPanel
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ListBox
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|PopupPanel
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ToggleButton
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
import|;
end_import

begin_comment
comment|/** Displays current diff preferences. */
end_comment

begin_class
DECL|class|PreferencesBox
class|class
name|PreferencesBox
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|PreferencesBox
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Style
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|dialog ()
name|String
name|dialog
parameter_list|()
function_decl|;
block|}
DECL|field|view
specifier|private
specifier|final
name|SideBySide2
name|view
decl_stmt|;
DECL|field|cmA
specifier|private
specifier|final
name|CodeMirror
name|cmA
decl_stmt|;
DECL|field|cmB
specifier|private
specifier|final
name|CodeMirror
name|cmB
decl_stmt|;
DECL|field|prefs
specifier|private
name|DiffPreferences
name|prefs
decl_stmt|;
DECL|field|contextLastValue
specifier|private
name|int
name|contextLastValue
decl_stmt|;
DECL|field|updateContextTimer
specifier|private
name|Timer
name|updateContextTimer
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
name|Style
name|style
decl_stmt|;
DECL|field|close
annotation|@
name|UiField
name|Anchor
name|close
decl_stmt|;
DECL|field|ignoreWhitespace
annotation|@
name|UiField
name|ListBox
name|ignoreWhitespace
decl_stmt|;
DECL|field|tabWidth
annotation|@
name|UiField
name|NpIntTextBox
name|tabWidth
decl_stmt|;
DECL|field|context
annotation|@
name|UiField
name|NpIntTextBox
name|context
decl_stmt|;
DECL|field|contextEntireFile
annotation|@
name|UiField
name|CheckBox
name|contextEntireFile
decl_stmt|;
DECL|field|intralineDifference
annotation|@
name|UiField
name|ToggleButton
name|intralineDifference
decl_stmt|;
DECL|field|syntaxHighlighting
annotation|@
name|UiField
name|ToggleButton
name|syntaxHighlighting
decl_stmt|;
DECL|field|whitespaceErrors
annotation|@
name|UiField
name|ToggleButton
name|whitespaceErrors
decl_stmt|;
DECL|field|showTabs
annotation|@
name|UiField
name|ToggleButton
name|showTabs
decl_stmt|;
DECL|field|lineNumbers
annotation|@
name|UiField
name|ToggleButton
name|lineNumbers
decl_stmt|;
DECL|field|topMenu
annotation|@
name|UiField
name|ToggleButton
name|topMenu
decl_stmt|;
DECL|field|manualReview
annotation|@
name|UiField
name|ToggleButton
name|manualReview
decl_stmt|;
DECL|field|expandAllComments
annotation|@
name|UiField
name|ToggleButton
name|expandAllComments
decl_stmt|;
DECL|field|apply
annotation|@
name|UiField
name|Button
name|apply
decl_stmt|;
DECL|field|save
annotation|@
name|UiField
name|Button
name|save
decl_stmt|;
DECL|method|PreferencesBox (SideBySide2 view)
name|PreferencesBox
parameter_list|(
name|SideBySide2
name|view
parameter_list|)
block|{
name|this
operator|.
name|view
operator|=
name|view
expr_stmt|;
name|this
operator|.
name|cmA
operator|=
name|view
operator|.
name|getCmA
argument_list|()
expr_stmt|;
name|this
operator|.
name|cmB
operator|=
name|view
operator|.
name|getCmB
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|initIgnoreWhitespace
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|public
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|save
operator|.
name|setVisible
argument_list|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
argument_list|)
expr_stmt|;
name|addDomHandler
argument_list|(
operator|new
name|KeyDownHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyDown
parameter_list|(
name|KeyDownEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getNativeKeyCode
argument_list|()
operator|==
name|KEY_ESCAPE
operator|||
name|event
operator|.
name|getNativeKeyCode
argument_list|()
operator|==
literal|','
condition|)
block|{
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|KeyDownEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|updateContextTimer
operator|=
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|prefs
operator|.
name|context
argument_list|()
operator|==
name|WHOLE_FILE_CONTEXT
condition|)
block|{
name|contextEntireFile
operator|.
name|setValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|setContext
argument_list|(
name|prefs
operator|.
name|context
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
DECL|method|set (DiffPreferences prefs)
name|void
name|set
parameter_list|(
name|DiffPreferences
name|prefs
parameter_list|)
block|{
name|this
operator|.
name|prefs
operator|=
name|prefs
expr_stmt|;
name|setIgnoreWhitespace
argument_list|(
name|prefs
operator|.
name|ignoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
name|tabWidth
operator|.
name|setIntValue
argument_list|(
name|prefs
operator|.
name|tabSize
argument_list|()
argument_list|)
expr_stmt|;
name|syntaxHighlighting
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|syntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
name|whitespaceErrors
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|showWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
name|showTabs
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|showTabs
argument_list|()
argument_list|)
expr_stmt|;
name|lineNumbers
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|showLineNumbers
argument_list|()
argument_list|)
expr_stmt|;
name|topMenu
operator|.
name|setValue
argument_list|(
operator|!
name|prefs
operator|.
name|hideTopMenu
argument_list|()
argument_list|)
expr_stmt|;
name|manualReview
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|manualReview
argument_list|()
argument_list|)
expr_stmt|;
name|expandAllComments
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|expandAllComments
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|view
operator|.
name|getIntraLineStatus
argument_list|()
condition|)
block|{
case|case
name|OFF
case|:
case|case
name|OK
case|:
name|intralineDifference
operator|.
name|setValue
argument_list|(
name|prefs
operator|.
name|intralineDifference
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|TIMEOUT
case|:
case|case
name|FAILURE
case|:
name|intralineDifference
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|intralineDifference
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|prefs
operator|.
name|context
argument_list|()
operator|==
name|WHOLE_FILE_CONTEXT
condition|)
block|{
name|contextLastValue
operator|=
name|DEFAULT_CONTEXT
expr_stmt|;
name|context
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|contextEntireFile
operator|.
name|setValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|setIntValue
argument_list|(
name|prefs
operator|.
name|context
argument_list|()
argument_list|)
expr_stmt|;
name|contextEntireFile
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"ignoreWhitespace"
argument_list|)
DECL|method|onIgnoreWhitespace (ChangeEvent e)
name|void
name|onIgnoreWhitespace
parameter_list|(
name|ChangeEvent
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|ignoreWhitespace
argument_list|(
name|Whitespace
operator|.
name|valueOf
argument_list|(
name|ignoreWhitespace
operator|.
name|getValue
argument_list|(
name|ignoreWhitespace
operator|.
name|getSelectedIndex
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|reloadDiffInfo
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"intralineDifference"
argument_list|)
DECL|method|onIntralineDifference (ValueChangeEvent<Boolean> e)
name|void
name|onIntralineDifference
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|intralineDifference
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|setShowIntraline
argument_list|(
name|prefs
operator|.
name|intralineDifference
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"context"
argument_list|)
DECL|method|onContextKey (KeyPressEvent e)
name|void
name|onContextKey
parameter_list|(
name|KeyPressEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|contextEntireFile
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|char
name|c
init|=
name|e
operator|.
name|getCharCode
argument_list|()
decl_stmt|;
if|if
condition|(
literal|'0'
operator|<=
name|c
operator|&&
name|c
operator|<=
literal|'9'
condition|)
block|{
name|contextEntireFile
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"context"
argument_list|)
DECL|method|onContext (ValueChangeEvent<String> e)
name|void
name|onContext
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|String
argument_list|>
name|e
parameter_list|)
block|{
name|String
name|v
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|int
name|c
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
operator|&&
name|v
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|c
operator|=
name|Math
operator|.
name|min
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
argument_list|)
argument_list|,
literal|32767
argument_list|)
expr_stmt|;
name|contextEntireFile
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|v
operator|==
literal|null
operator|||
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|c
operator|=
name|WHOLE_FILE_CONTEXT
expr_stmt|;
block|}
else|else
block|{
return|return;
block|}
name|prefs
operator|.
name|context
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|updateContextTimer
operator|.
name|schedule
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"contextEntireFile"
argument_list|)
DECL|method|onContextEntireFile (ValueChangeEvent<Boolean> e)
name|void
name|onContextEntireFile
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
comment|// If a click arrives too fast after onContext applied an update
comment|// the user committed the context line update by clicking on the
comment|// whole file checkmark. Drop this event, but transfer focus.
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|contextLastValue
operator|=
name|context
operator|.
name|getIntValue
argument_list|()
expr_stmt|;
name|context
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|prefs
operator|.
name|context
argument_list|(
name|WHOLE_FILE_CONTEXT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefs
operator|.
name|context
argument_list|(
name|contextLastValue
operator|>
literal|0
condition|?
name|contextLastValue
else|:
name|DEFAULT_CONTEXT
argument_list|)
expr_stmt|;
name|context
operator|.
name|setIntValue
argument_list|(
name|prefs
operator|.
name|context
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setSelectionRange
argument_list|(
literal|0
argument_list|,
name|context
operator|.
name|getText
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|updateContextTimer
operator|.
name|schedule
argument_list|(
literal|200
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"tabWidth"
argument_list|)
DECL|method|onTabWidth (ValueChangeEvent<String> e)
name|void
name|onTabWidth
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|String
argument_list|>
name|e
parameter_list|)
block|{
name|String
name|v
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|v
operator|!=
literal|null
operator|&&
name|v
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|prefs
operator|.
name|tabSize
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|view
operator|.
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|cmA
operator|.
name|setOption
argument_list|(
literal|"tabSize"
argument_list|,
name|prefs
operator|.
name|tabSize
argument_list|()
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setOption
argument_list|(
literal|"tabSize"
argument_list|,
name|prefs
operator|.
name|tabSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"expandAllComments"
argument_list|)
DECL|method|onExpandAllComments (ValueChangeEvent<Boolean> e)
name|void
name|onExpandAllComments
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|expandAllComments
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|setExpandAllComments
argument_list|(
name|prefs
operator|.
name|expandAllComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"showTabs"
argument_list|)
DECL|method|onShowTabs (ValueChangeEvent<Boolean> e)
name|void
name|onShowTabs
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|showTabs
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|setShowTabs
argument_list|(
name|prefs
operator|.
name|showTabs
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"lineNumbers"
argument_list|)
DECL|method|onLineNumbers (ValueChangeEvent<Boolean> e)
name|void
name|onLineNumbers
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|showLineNumbers
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|setShowLineNumbers
argument_list|(
name|prefs
operator|.
name|showLineNumbers
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"topMenu"
argument_list|)
DECL|method|onTopMenu (ValueChangeEvent<Boolean> e)
name|void
name|onTopMenu
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|hideTopMenu
argument_list|(
operator|!
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|setHeaderVisible
argument_list|(
name|view
operator|.
name|diffTable
operator|.
name|isHeaderVisible
argument_list|()
operator|&&
operator|!
name|prefs
operator|.
name|hideTopMenu
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|resizeCodeMirror
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"manualReview"
argument_list|)
DECL|method|onManualReview (ValueChangeEvent<Boolean> e)
name|void
name|onManualReview
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|manualReview
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"syntaxHighlighting"
argument_list|)
DECL|method|onSyntaxHighlighting (ValueChangeEvent<Boolean> e)
name|void
name|onSyntaxHighlighting
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|syntaxHighlighting
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|setSyntaxHighlighting
argument_list|(
name|prefs
operator|.
name|syntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"whitespaceErrors"
argument_list|)
DECL|method|onWhitespaceErrors (ValueChangeEvent<Boolean> e)
name|void
name|onWhitespaceErrors
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
name|prefs
operator|.
name|showWhitespaceErrors
argument_list|(
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|view
operator|.
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|cmA
operator|.
name|setOption
argument_list|(
literal|"showTrailingSpace"
argument_list|,
name|prefs
operator|.
name|showWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setOption
argument_list|(
literal|"showTrailingSpace"
argument_list|,
name|prefs
operator|.
name|showWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"apply"
argument_list|)
DECL|method|onApply (ClickEvent e)
name|void
name|onApply
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"save"
argument_list|)
DECL|method|onSave (ClickEvent e)
name|void
name|onSave
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|AccountApi
operator|.
name|putDiffPreferences
argument_list|(
name|prefs
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|DiffPreferences
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|DiffPreferences
name|result
parameter_list|)
block|{
name|AccountDiffPreference
name|p
init|=
name|Gerrit
operator|.
name|getAccountDiffPreference
argument_list|()
decl_stmt|;
if|if
condition|(
name|p
operator|==
literal|null
condition|)
block|{
name|p
operator|=
name|AccountDiffPreference
operator|.
name|createDefault
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|copyTo
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|setAccountDiffPreference
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"close"
argument_list|)
DECL|method|onClose (ClickEvent e)
name|void
name|onClose
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|e
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|setFocus (boolean focus)
name|void
name|setFocus
parameter_list|(
name|boolean
name|focus
parameter_list|)
block|{
name|ignoreWhitespace
operator|.
name|setFocus
argument_list|(
name|focus
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
specifier|private
name|void
name|close
parameter_list|()
block|{
operator|(
operator|(
name|PopupPanel
operator|)
name|getParent
argument_list|()
operator|)
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
DECL|method|setIgnoreWhitespace (Whitespace v)
specifier|private
name|void
name|setIgnoreWhitespace
parameter_list|(
name|Whitespace
name|v
parameter_list|)
block|{
name|String
name|name
init|=
name|v
operator|!=
literal|null
condition|?
name|v
operator|.
name|name
argument_list|()
else|:
name|IGNORE_NONE
operator|.
name|name
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|ignoreWhitespace
operator|.
name|getItemCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|ignoreWhitespace
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|ignoreWhitespace
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|ignoreWhitespace
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|initIgnoreWhitespace ()
specifier|private
name|void
name|initIgnoreWhitespace
parameter_list|()
block|{
name|ignoreWhitespace
operator|.
name|addItem
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|whitespaceIGNORE_NONE
argument_list|()
argument_list|,
name|IGNORE_NONE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ignoreWhitespace
operator|.
name|addItem
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|whitespaceIGNORE_SPACE_AT_EOL
argument_list|()
argument_list|,
name|IGNORE_SPACE_AT_EOL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ignoreWhitespace
operator|.
name|addItem
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|whitespaceIGNORE_SPACE_CHANGE
argument_list|()
argument_list|,
name|IGNORE_SPACE_CHANGE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ignoreWhitespace
operator|.
name|addItem
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|whitespaceIGNORE_ALL_SPACE
argument_list|()
argument_list|,
name|IGNORE_ALL_SPACE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

