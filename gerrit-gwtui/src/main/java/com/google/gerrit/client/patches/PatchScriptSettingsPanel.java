begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|Util
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
name|ListenableAccountDiffPreference
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
name|KeyCodes
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
name|dom
operator|.
name|client
operator|.
name|KeyPressHandler
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
name|HasValueChangeHandlers
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ValueChangeHandler
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
name|shared
operator|.
name|HandlerRegistration
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
name|FocusWidget
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
name|HasWidgets
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
name|Widget
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_class
DECL|class|PatchScriptSettingsPanel
specifier|public
class|class
name|PatchScriptSettingsPanel
extends|extends
name|Composite
implements|implements
name|HasValueChangeHandlers
argument_list|<
name|AccountDiffPreference
argument_list|>
block|{
DECL|field|uiBinder
specifier|private
specifier|static
name|MyUiBinder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|MyUiBinder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|MyUiBinder
interface|interface
name|MyUiBinder
extends|extends
name|UiBinder
argument_list|<
name|Widget
argument_list|,
name|PatchScriptSettingsPanel
argument_list|>
block|{   }
DECL|field|listenablePrefs
specifier|private
name|ListenableAccountDiffPreference
name|listenablePrefs
decl_stmt|;
DECL|field|enableIntralineDifference
specifier|private
name|boolean
name|enableIntralineDifference
init|=
literal|true
decl_stmt|;
DECL|field|enableSmallFileFeatures
specifier|private
name|boolean
name|enableSmallFileFeatures
init|=
literal|true
decl_stmt|;
annotation|@
name|UiField
DECL|field|ignoreWhitespace
name|ListBox
name|ignoreWhitespace
decl_stmt|;
annotation|@
name|UiField
DECL|field|tabWidth
name|NpIntTextBox
name|tabWidth
decl_stmt|;
annotation|@
name|UiField
DECL|field|colWidth
name|NpIntTextBox
name|colWidth
decl_stmt|;
annotation|@
name|UiField
DECL|field|syntaxHighlighting
name|CheckBox
name|syntaxHighlighting
decl_stmt|;
annotation|@
name|UiField
DECL|field|intralineDifference
name|CheckBox
name|intralineDifference
decl_stmt|;
annotation|@
name|UiField
DECL|field|context
name|ListBox
name|context
decl_stmt|;
annotation|@
name|UiField
DECL|field|whitespaceErrors
name|CheckBox
name|whitespaceErrors
decl_stmt|;
annotation|@
name|UiField
DECL|field|showTabs
name|CheckBox
name|showTabs
decl_stmt|;
annotation|@
name|UiField
DECL|field|reviewed
name|CheckBox
name|reviewed
decl_stmt|;
annotation|@
name|UiField
DECL|field|skipDeleted
name|CheckBox
name|skipDeleted
decl_stmt|;
annotation|@
name|UiField
DECL|field|skipUncommented
name|CheckBox
name|skipUncommented
decl_stmt|;
annotation|@
name|UiField
DECL|field|update
name|Button
name|update
decl_stmt|;
comment|/**    * Counts +1 for every setEnabled(true) and -1 for every setEnabled(false)    *    * The purpose is to prevent enabling widgets too early. It might happen that    * setEnabled(false) is called from this class and from an event handler    * of ValueChangeEvent in another class. The first setEnabled(true) would then    * enable widgets too early i.e. before the second setEnabled(true) is called.    *    * With this counter the setEnabled(true) will enable widgets only when    * setEnabledCounter == 0. Until it is less than zero setEnabled(true) will    * not enable the widgets.    */
DECL|field|setEnabledCounter
specifier|private
name|int
name|setEnabledCounter
decl_stmt|;
DECL|method|PatchScriptSettingsPanel (ListenableAccountDiffPreference prefs)
specifier|public
name|PatchScriptSettingsPanel
parameter_list|(
name|ListenableAccountDiffPreference
name|prefs
parameter_list|)
block|{
name|listenablePrefs
operator|=
name|prefs
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
argument_list|(
name|ignoreWhitespace
argument_list|)
expr_stmt|;
name|initContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|reviewed
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|KeyPressHandler
name|onEnter
init|=
operator|new
name|KeyPressHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getCharCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|update
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|tabWidth
operator|.
name|addKeyPressHandler
argument_list|(
name|onEnter
argument_list|)
expr_stmt|;
name|colWidth
operator|.
name|addKeyPressHandler
argument_list|(
name|onEnter
argument_list|)
expr_stmt|;
name|display
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addValueChangeHandler ( ValueChangeHandler<AccountDiffPreference> handler)
specifier|public
name|HandlerRegistration
name|addValueChangeHandler
parameter_list|(
name|ValueChangeHandler
argument_list|<
name|AccountDiffPreference
argument_list|>
name|handler
parameter_list|)
block|{
return|return
name|super
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|,
name|ValueChangeEvent
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
DECL|method|setEnabled (final boolean on)
specifier|public
name|void
name|setEnabled
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
if|if
condition|(
name|on
condition|)
block|{
name|setEnabledCounter
operator|++
expr_stmt|;
block|}
else|else
block|{
name|setEnabledCounter
operator|--
expr_stmt|;
block|}
if|if
condition|(
name|on
operator|&&
name|setEnabledCounter
operator|==
literal|0
operator|||
operator|!
name|on
condition|)
block|{
for|for
control|(
name|Widget
name|w
range|:
operator|(
name|HasWidgets
operator|)
name|getWidget
argument_list|()
control|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|FocusWidget
condition|)
block|{
operator|(
operator|(
name|FocusWidget
operator|)
name|w
operator|)
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
block|}
name|toggleEnabledStatus
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
DECL|method|setEnableSmallFileFeatures (final boolean on)
specifier|public
name|void
name|setEnableSmallFileFeatures
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|enableSmallFileFeatures
operator|=
name|on
expr_stmt|;
if|if
condition|(
name|enableSmallFileFeatures
condition|)
block|{
name|syntaxHighlighting
operator|.
name|setValue
argument_list|(
name|getValue
argument_list|()
operator|.
name|isSyntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|syntaxHighlighting
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|toggleEnabledStatus
argument_list|(
name|update
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setEnableIntralineDifference (final boolean on)
specifier|public
name|void
name|setEnableIntralineDifference
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|enableIntralineDifference
operator|=
name|on
expr_stmt|;
if|if
condition|(
name|enableIntralineDifference
condition|)
block|{
name|intralineDifference
operator|.
name|setValue
argument_list|(
name|getValue
argument_list|()
operator|.
name|isIntralineDifference
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|intralineDifference
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|toggleEnabledStatus
argument_list|(
name|update
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|toggleEnabledStatus (final boolean on)
specifier|private
name|void
name|toggleEnabledStatus
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|intralineDifference
operator|.
name|setEnabled
argument_list|(
name|on
operator|&
name|enableIntralineDifference
argument_list|)
expr_stmt|;
name|syntaxHighlighting
operator|.
name|setEnabled
argument_list|(
name|on
operator|&
name|enableSmallFileFeatures
argument_list|)
expr_stmt|;
specifier|final
name|String
name|title
init|=
name|enableSmallFileFeatures
condition|?
literal|null
else|:
name|PatchUtil
operator|.
name|C
operator|.
name|disabledOnLargeFiles
argument_list|()
decl_stmt|;
name|syntaxHighlighting
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
block|}
DECL|method|getReviewedCheckBox ()
specifier|public
name|CheckBox
name|getReviewedCheckBox
parameter_list|()
block|{
return|return
name|reviewed
return|;
block|}
DECL|method|getValue ()
specifier|public
name|AccountDiffPreference
name|getValue
parameter_list|()
block|{
return|return
name|listenablePrefs
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|setValue (final AccountDiffPreference dp)
specifier|public
name|void
name|setValue
parameter_list|(
specifier|final
name|AccountDiffPreference
name|dp
parameter_list|)
block|{
name|listenablePrefs
operator|.
name|set
argument_list|(
name|dp
argument_list|)
expr_stmt|;
name|display
argument_list|()
expr_stmt|;
block|}
DECL|method|display ()
specifier|protected
name|void
name|display
parameter_list|()
block|{
specifier|final
name|AccountDiffPreference
name|dp
init|=
name|getValue
argument_list|()
decl_stmt|;
name|setIgnoreWhitespace
argument_list|(
name|dp
operator|.
name|getIgnoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|enableSmallFileFeatures
condition|)
block|{
name|syntaxHighlighting
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isSyntaxHighlighting
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|syntaxHighlighting
operator|.
name|setValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|setContext
argument_list|(
name|dp
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|tabWidth
operator|.
name|setIntValue
argument_list|(
name|dp
operator|.
name|getTabSize
argument_list|()
argument_list|)
expr_stmt|;
name|colWidth
operator|.
name|setIntValue
argument_list|(
name|dp
operator|.
name|getLineLength
argument_list|()
argument_list|)
expr_stmt|;
name|intralineDifference
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isIntralineDifference
argument_list|()
argument_list|)
expr_stmt|;
name|whitespaceErrors
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isShowWhitespaceErrors
argument_list|()
argument_list|)
expr_stmt|;
name|showTabs
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isShowTabs
argument_list|()
argument_list|)
expr_stmt|;
name|skipDeleted
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isSkipDeleted
argument_list|()
argument_list|)
expr_stmt|;
name|skipUncommented
operator|.
name|setValue
argument_list|(
name|dp
operator|.
name|isSkipUncommented
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"update"
argument_list|)
DECL|method|onUpdate (ClickEvent event)
name|void
name|onUpdate
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|update
argument_list|()
expr_stmt|;
block|}
DECL|method|update ()
specifier|private
name|void
name|update
parameter_list|()
block|{
name|AccountDiffPreference
name|dp
init|=
operator|new
name|AccountDiffPreference
argument_list|(
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|dp
operator|.
name|setIgnoreWhitespace
argument_list|(
name|getIgnoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setContext
argument_list|(
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setTabSize
argument_list|(
name|tabWidth
operator|.
name|getIntValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setLineLength
argument_list|(
name|colWidth
operator|.
name|getIntValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setSyntaxHighlighting
argument_list|(
name|syntaxHighlighting
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setIntralineDifference
argument_list|(
name|intralineDifference
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setShowWhitespaceErrors
argument_list|(
name|whitespaceErrors
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setShowTabs
argument_list|(
name|showTabs
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setSkipDeleted
argument_list|(
name|skipDeleted
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|dp
operator|.
name|setSkipUncommented
argument_list|(
name|skipUncommented
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|listenablePrefs
operator|.
name|set
argument_list|(
name|dp
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|persistDiffPreferences
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|persistDiffPreferences ()
specifier|private
name|void
name|persistDiffPreferences
parameter_list|()
block|{
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|changeDiffPreferences
argument_list|(
name|getValue
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|VoidResult
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|setAccountDiffPreference
argument_list|(
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|initIgnoreWhitespace (ListBox ws)
specifier|private
name|void
name|initIgnoreWhitespace
parameter_list|(
name|ListBox
name|ws
parameter_list|)
block|{
name|ws
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
comment|//
name|Whitespace
operator|.
name|IGNORE_NONE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ws
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
comment|//
name|Whitespace
operator|.
name|IGNORE_SPACE_AT_EOL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ws
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
comment|//
name|Whitespace
operator|.
name|IGNORE_SPACE_CHANGE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|ws
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
comment|//
name|Whitespace
operator|.
name|IGNORE_ALL_SPACE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initContext (ListBox context)
specifier|private
name|void
name|initContext
parameter_list|(
name|ListBox
name|context
parameter_list|)
block|{
for|for
control|(
specifier|final
name|short
name|v
range|:
name|AccountDiffPreference
operator|.
name|CONTEXT_CHOICES
control|)
block|{
specifier|final
name|String
name|label
decl_stmt|;
if|if
condition|(
name|v
operator|==
name|AccountDiffPreference
operator|.
name|WHOLE_FILE_CONTEXT
condition|)
block|{
name|label
operator|=
name|Util
operator|.
name|C
operator|.
name|contextWholeFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|label
operator|=
name|Util
operator|.
name|M
operator|.
name|lines
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
name|context
operator|.
name|addItem
argument_list|(
name|label
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getIgnoreWhitespace ()
specifier|private
name|Whitespace
name|getIgnoreWhitespace
parameter_list|()
block|{
specifier|final
name|int
name|sel
init|=
name|ignoreWhitespace
operator|.
name|getSelectedIndex
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|sel
condition|)
block|{
return|return
name|Whitespace
operator|.
name|valueOf
argument_list|(
name|ignoreWhitespace
operator|.
name|getValue
argument_list|(
name|sel
argument_list|)
argument_list|)
return|;
block|}
return|return
name|getValue
argument_list|()
operator|.
name|getIgnoreWhitespace
argument_list|()
return|;
block|}
DECL|method|setIgnoreWhitespace (Whitespace s)
specifier|private
name|void
name|setIgnoreWhitespace
parameter_list|(
name|Whitespace
name|s
parameter_list|)
block|{
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
name|s
operator|.
name|name
argument_list|()
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
DECL|method|getContext ()
specifier|private
name|short
name|getContext
parameter_list|()
block|{
specifier|final
name|int
name|sel
init|=
name|context
operator|.
name|getSelectedIndex
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|sel
condition|)
block|{
return|return
name|Short
operator|.
name|parseShort
argument_list|(
name|context
operator|.
name|getValue
argument_list|(
name|sel
argument_list|)
argument_list|)
return|;
block|}
return|return
operator|(
name|short
operator|)
name|getValue
argument_list|()
operator|.
name|getContext
argument_list|()
return|;
block|}
DECL|method|setContext (int ctx)
specifier|private
name|void
name|setContext
parameter_list|(
name|int
name|ctx
parameter_list|)
block|{
name|String
name|v
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|ctx
argument_list|)
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
name|context
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
name|context
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|context
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|context
operator|.
name|setSelectedIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

