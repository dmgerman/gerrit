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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|changes
operator|.
name|ChangeApi
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|ApprovalInfo
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|LabelInfo
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
name|changes
operator|.
name|ReviewInput
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
name|rpc
operator|.
name|NativeMap
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
name|common
operator|.
name|PageLinks
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
name|common
operator|.
name|data
operator|.
name|LabelValue
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
name|PatchSet
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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
name|Scheduler
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
name|Scheduler
operator|.
name|ScheduledCommand
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
name|dom
operator|.
name|client
operator|.
name|Element
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
name|Grid
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
name|RadioButton
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
name|UIObject
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
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextArea
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
import|;
end_import

begin_class
DECL|class|ReplyBox
class|class
name|ReplyBox
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
name|ReplyBox
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
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
DECL|interface|Styles
interface|interface
name|Styles
extends|extends
name|CssResource
block|{
DECL|method|label_name ()
name|String
name|label_name
parameter_list|()
function_decl|;
DECL|method|label_value ()
name|String
name|label_value
parameter_list|()
function_decl|;
block|}
DECL|field|psId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|String
name|revision
decl_stmt|;
DECL|field|in
specifier|private
name|ReviewInput
name|in
init|=
name|ReviewInput
operator|.
name|create
argument_list|()
decl_stmt|;
DECL|field|lgtm
specifier|private
name|List
argument_list|<
name|Runnable
argument_list|>
name|lgtm
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
name|Styles
name|style
decl_stmt|;
DECL|field|message
annotation|@
name|UiField
name|NpTextArea
name|message
decl_stmt|;
DECL|field|labelsParent
annotation|@
name|UiField
name|Element
name|labelsParent
decl_stmt|;
DECL|field|labelsTable
annotation|@
name|UiField
name|Grid
name|labelsTable
decl_stmt|;
DECL|field|send
annotation|@
name|UiField
name|Button
name|send
decl_stmt|;
DECL|field|email
annotation|@
name|UiField
name|CheckBox
name|email
decl_stmt|;
DECL|method|ReplyBox ( PatchSet.Id psId, String revision, NativeMap<LabelInfo> all, NativeMap<JsArrayString> permitted)
name|ReplyBox
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|String
name|revision
parameter_list|,
name|NativeMap
argument_list|<
name|LabelInfo
argument_list|>
name|all
parameter_list|,
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
name|permitted
parameter_list|)
block|{
name|this
operator|.
name|psId
operator|=
name|psId
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
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
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|permitted
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|names
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|labelsParent
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|names
argument_list|)
expr_stmt|;
name|lgtm
operator|=
operator|new
name|ArrayList
argument_list|<
name|Runnable
argument_list|>
argument_list|(
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|renderLabels
argument_list|(
name|names
argument_list|,
name|all
argument_list|,
name|permitted
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|Scheduler
operator|.
name|get
argument_list|()
operator|.
name|scheduleDeferred
argument_list|(
operator|new
name|ScheduledCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|message
operator|.
name|setFocus
argument_list|(
literal|true
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
literal|"message"
argument_list|)
DECL|method|onMessageKey (KeyPressEvent event)
name|void
name|onMessageKey
parameter_list|(
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
operator|(
name|event
operator|.
name|getCharCode
argument_list|()
operator|==
literal|'\n'
operator|||
name|event
operator|.
name|getCharCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
operator|)
operator|&&
name|event
operator|.
name|isControlKeyDown
argument_list|()
condition|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|event
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
name|onSend
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lgtm
operator|!=
literal|null
operator|&&
name|event
operator|.
name|getCharCode
argument_list|()
operator|==
literal|'M'
operator|&&
name|message
operator|.
name|getValue
argument_list|()
operator|.
name|equals
argument_list|(
literal|"LGT"
argument_list|)
condition|)
block|{
name|Scheduler
operator|.
name|get
argument_list|()
operator|.
name|scheduleDeferred
argument_list|(
operator|new
name|ScheduledCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
if|if
condition|(
name|message
operator|.
name|getValue
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"LGTM"
argument_list|)
condition|)
block|{
for|for
control|(
name|Runnable
name|r
range|:
name|lgtm
control|)
block|{
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"email"
argument_list|)
DECL|method|onEmail (ValueChangeEvent<Boolean> e)
name|void
name|onEmail
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|in
operator|.
name|notify
argument_list|(
name|ReviewInput
operator|.
name|NotifyHandling
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|in
operator|.
name|notify
argument_list|(
name|ReviewInput
operator|.
name|NotifyHandling
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"send"
argument_list|)
DECL|method|onSend (ClickEvent e)
name|void
name|onSend
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|in
operator|.
name|message
argument_list|(
name|message
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeApi
operator|.
name|revision
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|revision
argument_list|)
operator|.
name|view
argument_list|(
literal|"review"
argument_list|)
operator|.
name|post
argument_list|(
name|in
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ReviewInput
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ReviewInput
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange2
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|psId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|hide
argument_list|()
expr_stmt|;
block|}
DECL|method|hide ()
specifier|private
name|void
name|hide
parameter_list|()
block|{
for|for
control|(
name|Widget
name|w
init|=
name|getParent
argument_list|()
init|;
name|w
operator|!=
literal|null
condition|;
name|w
operator|=
name|w
operator|.
name|getParent
argument_list|()
control|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|PopupPanel
condition|)
block|{
operator|(
operator|(
name|PopupPanel
operator|)
name|w
operator|)
operator|.
name|hide
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|method|renderLabels ( List<String> names, NativeMap<LabelInfo> all, NativeMap<JsArrayString> permitted)
specifier|private
name|void
name|renderLabels
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|,
name|NativeMap
argument_list|<
name|LabelInfo
argument_list|>
name|all
parameter_list|,
name|NativeMap
argument_list|<
name|JsArrayString
argument_list|>
name|permitted
parameter_list|)
block|{
name|TreeSet
argument_list|<
name|Short
argument_list|>
name|values
init|=
operator|new
name|TreeSet
argument_list|<
name|Short
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|names
control|)
block|{
name|JsArrayString
name|p
init|=
name|permitted
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
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
name|p
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|LabelInfo
operator|.
name|parseValue
argument_list|(
name|p
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|List
argument_list|<
name|Short
argument_list|>
name|columns
init|=
operator|new
name|ArrayList
argument_list|<
name|Short
argument_list|>
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|labelsTable
operator|.
name|resize
argument_list|(
literal|1
operator|+
name|permitted
operator|.
name|size
argument_list|()
argument_list|,
literal|1
operator|+
name|values
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|columns
operator|.
name|size
argument_list|()
condition|;
name|c
operator|++
control|)
block|{
name|labelsTable
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|1
operator|+
name|c
argument_list|,
name|LabelValue
operator|.
name|formatValue
argument_list|(
name|columns
operator|.
name|get
argument_list|(
name|c
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|labelsTable
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
literal|1
operator|+
name|c
argument_list|,
name|style
operator|.
name|label_value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|checkboxes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|permitted
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|row
init|=
literal|1
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|names
control|)
block|{
name|Set
argument_list|<
name|Short
argument_list|>
name|vals
init|=
name|all
operator|.
name|get
argument_list|(
name|id
argument_list|)
operator|.
name|value_set
argument_list|()
decl_stmt|;
if|if
condition|(
name|isCheckBox
argument_list|(
name|vals
argument_list|)
condition|)
block|{
name|checkboxes
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|renderRadio
argument_list|(
name|row
operator|++
argument_list|,
name|id
argument_list|,
name|columns
argument_list|,
name|vals
argument_list|,
name|all
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|id
range|:
name|checkboxes
control|)
block|{
name|renderCheckBox
argument_list|(
name|row
operator|++
argument_list|,
name|id
argument_list|,
name|all
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|renderRadio (int row, final String id, List<Short> columns, Set<Short> values, LabelInfo info)
specifier|private
name|void
name|renderRadio
parameter_list|(
name|int
name|row
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
name|List
argument_list|<
name|Short
argument_list|>
name|columns
parameter_list|,
name|Set
argument_list|<
name|Short
argument_list|>
name|values
parameter_list|,
name|LabelInfo
name|info
parameter_list|)
block|{
name|labelsTable
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|labelsTable
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|style
operator|.
name|label_name
argument_list|()
argument_list|)
expr_stmt|;
name|ApprovalInfo
name|self
init|=
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|?
name|info
operator|.
name|for_user
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|List
argument_list|<
name|RadioButton
argument_list|>
name|group
init|=
operator|new
name|ArrayList
argument_list|<
name|RadioButton
argument_list|>
argument_list|(
name|values
operator|.
name|size
argument_list|()
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
name|columns
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Short
name|v
init|=
name|columns
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|contains
argument_list|(
name|v
argument_list|)
condition|)
block|{
name|RadioButton
name|b
init|=
operator|new
name|RadioButton
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|b
operator|.
name|setTitle
argument_list|(
name|info
operator|.
name|value_text
argument_list|(
name|LabelValue
operator|.
name|formatValue
argument_list|(
name|v
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|self
operator|!=
literal|null
operator|&&
name|v
operator|==
name|self
operator|.
name|value
argument_list|()
operator|)
operator|||
operator|(
name|self
operator|==
literal|null
operator|&&
name|v
operator|==
literal|0
operator|)
condition|)
block|{
name|b
operator|.
name|setValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onValueChange
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|in
operator|.
name|label
argument_list|(
name|id
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|group
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|labelsTable
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
operator|+
name|i
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|group
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|lgtm
operator|.
name|add
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|group
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|;
name|i
operator|++
control|)
block|{
name|group
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|setValue
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|group
operator|.
name|get
argument_list|(
name|group
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|setValue
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|renderCheckBox (int row, final String id, LabelInfo info)
specifier|private
name|void
name|renderCheckBox
parameter_list|(
name|int
name|row
parameter_list|,
specifier|final
name|String
name|id
parameter_list|,
name|LabelInfo
name|info
parameter_list|)
block|{
name|ApprovalInfo
name|self
init|=
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|?
name|info
operator|.
name|for_user
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|CheckBox
name|b
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|b
operator|.
name|setText
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|b
operator|.
name|setTitle
argument_list|(
name|info
operator|.
name|value_text
argument_list|(
literal|"+1"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|self
operator|!=
literal|null
operator|&&
name|self
operator|.
name|value
argument_list|()
operator|==
literal|1
condition|)
block|{
name|b
operator|.
name|setValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onValueChange
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|event
parameter_list|)
block|{
name|in
operator|.
name|label
argument_list|(
name|id
argument_list|,
name|event
operator|.
name|getValue
argument_list|()
condition|?
operator|(
name|short
operator|)
literal|1
else|:
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|b
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|label_name
argument_list|()
argument_list|)
expr_stmt|;
name|labelsTable
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|lgtm
operator|.
name|add
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
name|b
operator|.
name|setValue
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|isCheckBox (Set<Short> values)
specifier|private
specifier|static
name|boolean
name|isCheckBox
parameter_list|(
name|Set
argument_list|<
name|Short
argument_list|>
name|values
parameter_list|)
block|{
return|return
name|values
operator|.
name|size
argument_list|()
operator|==
literal|2
operator|&&
name|values
operator|.
name|contains
argument_list|(
operator|(
name|short
operator|)
literal|0
argument_list|)
operator|&&
name|values
operator|.
name|contains
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

