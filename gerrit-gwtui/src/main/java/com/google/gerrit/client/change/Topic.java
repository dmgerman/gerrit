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
name|BranchLink
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
name|InlineHyperlink
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
name|ClickHandler
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
name|Image
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
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextBox
import|;
end_import

begin_comment
comment|/** Displays (and edits) the change topic string. */
end_comment

begin_class
DECL|class|Topic
class|class
name|Topic
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
name|Topic
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
DECL|field|psId
specifier|private
name|PatchSet
operator|.
name|Id
name|psId
decl_stmt|;
DECL|field|canEdit
specifier|private
name|boolean
name|canEdit
decl_stmt|;
DECL|field|show
annotation|@
name|UiField
name|Element
name|show
decl_stmt|;
DECL|field|text
annotation|@
name|UiField
name|InlineHyperlink
name|text
decl_stmt|;
DECL|field|editIcon
annotation|@
name|UiField
name|Image
name|editIcon
decl_stmt|;
DECL|field|form
annotation|@
name|UiField
name|Element
name|form
decl_stmt|;
DECL|field|input
annotation|@
name|UiField
name|NpTextBox
name|input
decl_stmt|;
DECL|field|save
annotation|@
name|UiField
name|Button
name|save
decl_stmt|;
DECL|field|cancel
annotation|@
name|UiField
name|Button
name|cancel
decl_stmt|;
DECL|method|Topic ()
name|Topic
parameter_list|()
block|{
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
name|editIcon
operator|.
name|addDomHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|onEdit
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|ClickEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|set (ChangeInfo info, String revision)
name|void
name|set
parameter_list|(
name|ChangeInfo
name|info
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|canEdit
operator|=
name|info
operator|.
name|has_actions
argument_list|()
operator|&&
name|info
operator|.
name|actions
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"topic"
argument_list|)
operator|&&
name|info
operator|.
name|actions
argument_list|()
operator|.
name|get
argument_list|(
literal|"topic"
argument_list|)
operator|.
name|enabled
argument_list|()
expr_stmt|;
name|psId
operator|=
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|info
operator|.
name|legacy_id
argument_list|()
argument_list|,
name|info
operator|.
name|revisions
argument_list|()
operator|.
name|get
argument_list|(
name|revision
argument_list|)
operator|.
name|_number
argument_list|()
argument_list|)
expr_stmt|;
name|initTopicLink
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|editIcon
operator|.
name|setVisible
argument_list|(
name|canEdit
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|canEdit
condition|)
block|{
name|show
operator|.
name|setTitle
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initTopicLink (ChangeInfo info)
specifier|private
name|void
name|initTopicLink
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|text
operator|.
name|setText
argument_list|(
name|info
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
name|text
operator|.
name|setTargetHistoryToken
argument_list|(
name|PageLinks
operator|.
name|toChangeQuery
argument_list|(
name|BranchLink
operator|.
name|query
argument_list|(
name|info
operator|.
name|project_name_key
argument_list|()
argument_list|,
name|info
operator|.
name|status
argument_list|()
argument_list|,
name|info
operator|.
name|branch
argument_list|()
argument_list|,
name|info
operator|.
name|topic
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|canEdit ()
name|boolean
name|canEdit
parameter_list|()
block|{
return|return
name|canEdit
return|;
block|}
DECL|method|onEdit ()
name|void
name|onEdit
parameter_list|()
block|{
if|if
condition|(
name|canEdit
condition|)
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|form
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|input
operator|.
name|setText
argument_list|(
name|text
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"cancel"
argument_list|)
DECL|method|onCancel (ClickEvent e)
name|void
name|onCancel
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|input
operator|.
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|form
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"input"
argument_list|)
DECL|method|onKeyDownInput (KeyDownEvent e)
name|void
name|onKeyDownInput
parameter_list|(
name|KeyDownEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getNativeKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ESCAPE
condition|)
block|{
name|onCancel
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|e
operator|.
name|getNativeKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|e
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
name|onSave
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
name|ChangeApi
operator|.
name|topic
argument_list|(
name|psId
operator|.
name|getParentKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|input
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
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
name|onCancel
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

