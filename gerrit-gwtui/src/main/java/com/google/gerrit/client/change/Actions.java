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
name|actions
operator|.
name|ActionButton
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
name|actions
operator|.
name|ActionInfo
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|CommitInfo
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
name|RevisionInfo
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|FlowPanel
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
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
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
DECL|class|Actions
class|class
name|Actions
extends|extends
name|Composite
block|{
DECL|field|CORE
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|CORE
init|=
block|{
literal|"abandon"
block|,
literal|"restore"
block|,
literal|"revert"
block|,
literal|"topic"
block|,
literal|"cherrypick"
block|,
literal|"submit"
block|,
literal|"rebase"
block|,
literal|"message"
block|,
literal|"publish"
block|,
literal|"followup"
block|,
literal|"/"
block|}
decl_stmt|;
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|FlowPanel
argument_list|,
name|Actions
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
DECL|field|cherrypick
annotation|@
name|UiField
name|Button
name|cherrypick
decl_stmt|;
DECL|field|rebase
annotation|@
name|UiField
name|Button
name|rebase
decl_stmt|;
DECL|field|revert
annotation|@
name|UiField
name|Button
name|revert
decl_stmt|;
DECL|field|submit
annotation|@
name|UiField
name|Button
name|submit
decl_stmt|;
DECL|field|abandon
annotation|@
name|UiField
name|Button
name|abandon
decl_stmt|;
DECL|field|abandonAction
specifier|private
name|AbandonAction
name|abandonAction
decl_stmt|;
DECL|field|restore
annotation|@
name|UiField
name|Button
name|restore
decl_stmt|;
DECL|field|restoreAction
specifier|private
name|RestoreAction
name|restoreAction
decl_stmt|;
DECL|field|followUp
annotation|@
name|UiField
name|Button
name|followUp
decl_stmt|;
DECL|field|followUpAction
specifier|private
name|FollowUpAction
name|followUpAction
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|changeInfo
specifier|private
name|ChangeInfo
name|changeInfo
decl_stmt|;
DECL|field|revision
specifier|private
name|String
name|revision
decl_stmt|;
DECL|field|project
specifier|private
name|String
name|project
decl_stmt|;
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|field|message
specifier|private
name|String
name|message
decl_stmt|;
DECL|field|branch
specifier|private
name|String
name|branch
decl_stmt|;
DECL|field|key
specifier|private
name|String
name|key
decl_stmt|;
DECL|method|Actions ()
name|Actions
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
name|getElement
argument_list|()
operator|.
name|setId
argument_list|(
literal|"change_actions"
argument_list|)
expr_stmt|;
block|}
DECL|method|display (ChangeInfo info, String revision)
name|void
name|display
parameter_list|(
name|ChangeInfo
name|info
parameter_list|,
name|String
name|revision
parameter_list|)
block|{
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|boolean
name|hasUser
init|=
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
decl_stmt|;
name|RevisionInfo
name|revInfo
init|=
name|info
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
decl_stmt|;
name|CommitInfo
name|commit
init|=
name|revInfo
operator|.
name|commit
argument_list|()
decl_stmt|;
name|changeId
operator|=
name|info
operator|.
name|legacy_id
argument_list|()
expr_stmt|;
name|project
operator|=
name|info
operator|.
name|project
argument_list|()
expr_stmt|;
name|subject
operator|=
name|commit
operator|.
name|subject
argument_list|()
expr_stmt|;
name|message
operator|=
name|commit
operator|.
name|message
argument_list|()
expr_stmt|;
name|branch
operator|=
name|info
operator|.
name|branch
argument_list|()
expr_stmt|;
name|key
operator|=
name|info
operator|.
name|change_id
argument_list|()
expr_stmt|;
name|changeInfo
operator|=
name|info
expr_stmt|;
name|initChangeActions
argument_list|(
name|info
argument_list|,
name|hasUser
argument_list|)
expr_stmt|;
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actionMap
init|=
name|revInfo
operator|.
name|has_actions
argument_list|()
condition|?
name|revInfo
operator|.
name|actions
argument_list|()
else|:
name|NativeMap
operator|.
expr|<
name|ActionInfo
operator|>
name|create
argument_list|()
decl_stmt|;
name|actionMap
operator|.
name|copyKeysIntoChildren
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
name|reloadRevisionActions
argument_list|(
name|actionMap
argument_list|)
expr_stmt|;
block|}
DECL|method|initChangeActions (ChangeInfo info, boolean hasUser)
specifier|private
name|void
name|initChangeActions
parameter_list|(
name|ChangeInfo
name|info
parameter_list|,
name|boolean
name|hasUser
parameter_list|)
block|{
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
init|=
name|info
operator|.
name|has_actions
argument_list|()
condition|?
name|info
operator|.
name|actions
argument_list|()
else|:
name|NativeMap
operator|.
expr|<
name|ActionInfo
operator|>
name|create
argument_list|()
decl_stmt|;
name|actions
operator|.
name|copyKeysIntoChildren
argument_list|(
literal|"id"
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasUser
condition|)
block|{
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"abandon"
argument_list|,
name|abandon
argument_list|)
expr_stmt|;
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"restore"
argument_list|,
name|restore
argument_list|)
expr_stmt|;
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"revert"
argument_list|,
name|revert
argument_list|)
expr_stmt|;
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"followup"
argument_list|,
name|followUp
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|id
range|:
name|filterNonCore
argument_list|(
name|actions
argument_list|)
control|)
block|{
name|add
argument_list|(
operator|new
name|ActionButton
argument_list|(
name|info
argument_list|,
name|actions
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|reloadRevisionActions (NativeMap<ActionInfo> actions)
name|void
name|reloadRevisionActions
parameter_list|(
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
return|return;
block|}
name|boolean
name|canSubmit
init|=
name|actions
operator|.
name|containsKey
argument_list|(
literal|"submit"
argument_list|)
decl_stmt|;
if|if
condition|(
name|canSubmit
condition|)
block|{
name|ActionInfo
name|action
init|=
name|actions
operator|.
name|get
argument_list|(
literal|"submit"
argument_list|)
decl_stmt|;
name|submit
operator|.
name|setTitle
argument_list|(
name|action
operator|.
name|title
argument_list|()
argument_list|)
expr_stmt|;
name|submit
operator|.
name|setEnabled
argument_list|(
name|action
operator|.
name|enabled
argument_list|()
argument_list|)
expr_stmt|;
name|submit
operator|.
name|setHTML
argument_list|(
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|openDiv
argument_list|()
operator|.
name|append
argument_list|(
name|action
operator|.
name|label
argument_list|()
argument_list|)
operator|.
name|closeDiv
argument_list|()
argument_list|)
expr_stmt|;
name|submit
operator|.
name|setEnabled
argument_list|(
name|action
operator|.
name|enabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|submit
operator|.
name|setVisible
argument_list|(
name|canSubmit
argument_list|)
expr_stmt|;
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"cherrypick"
argument_list|,
name|cherrypick
argument_list|)
expr_stmt|;
name|a2b
argument_list|(
name|actions
argument_list|,
literal|"rebase"
argument_list|,
name|rebase
argument_list|)
expr_stmt|;
if|if
condition|(
name|rebase
operator|.
name|isVisible
argument_list|()
condition|)
block|{
comment|// it is the rebase button in RebaseDialog that the server wants to disable
name|rebase
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|RevisionInfo
name|revInfo
init|=
name|changeInfo
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|filterNonCore
argument_list|(
name|actions
argument_list|)
control|)
block|{
name|add
argument_list|(
operator|new
name|ActionButton
argument_list|(
name|changeInfo
argument_list|,
name|revInfo
argument_list|,
name|actions
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (ActionButton b)
specifier|private
name|void
name|add
parameter_list|(
name|ActionButton
name|b
parameter_list|)
block|{
operator|(
operator|(
name|FlowPanel
operator|)
name|getWidget
argument_list|()
operator|)
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|filterNonCore (NativeMap<ActionInfo> m)
specifier|private
specifier|static
name|TreeSet
argument_list|<
name|String
argument_list|>
name|filterNonCore
parameter_list|(
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|m
parameter_list|)
block|{
name|TreeSet
argument_list|<
name|String
argument_list|>
name|ids
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|(
name|m
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|id
range|:
name|CORE
control|)
block|{
name|ids
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
return|return
name|ids
return|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"followUp"
argument_list|)
DECL|method|onFollowUp (@uppressWarningsR) ClickEvent e)
name|void
name|onFollowUp
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|followUpAction
operator|==
literal|null
condition|)
block|{
name|followUpAction
operator|=
operator|new
name|FollowUpAction
argument_list|(
name|followUp
argument_list|,
name|project
argument_list|,
name|branch
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
name|followUpAction
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"abandon"
argument_list|)
DECL|method|onAbandon (@uppressWarningsR) ClickEvent e)
name|void
name|onAbandon
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|abandonAction
operator|==
literal|null
condition|)
block|{
name|abandonAction
operator|=
operator|new
name|AbandonAction
argument_list|(
name|abandon
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
block|}
name|abandonAction
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"restore"
argument_list|)
DECL|method|onRestore (@uppressWarningsR) ClickEvent e)
name|void
name|onRestore
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
if|if
condition|(
name|restoreAction
operator|==
literal|null
condition|)
block|{
name|restoreAction
operator|=
operator|new
name|RestoreAction
argument_list|(
name|restore
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
block|}
name|restoreAction
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"rebase"
argument_list|)
DECL|method|onRebase (@uppressWarningsR) ClickEvent e)
name|void
name|onRebase
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
name|RevisionInfo
name|revInfo
init|=
name|changeInfo
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
decl_stmt|;
if|if
condition|(
name|revInfo
operator|.
name|has_actions
argument_list|()
condition|)
block|{
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
init|=
name|revInfo
operator|.
name|actions
argument_list|()
decl_stmt|;
if|if
condition|(
name|actions
operator|.
name|containsKey
argument_list|(
literal|"rebase"
argument_list|)
condition|)
block|{
name|enabled
operator|=
name|actions
operator|.
name|get
argument_list|(
literal|"rebase"
argument_list|)
operator|.
name|enabled
argument_list|()
expr_stmt|;
block|}
block|}
name|RebaseAction
operator|.
name|call
argument_list|(
name|rebase
argument_list|,
name|project
argument_list|,
name|changeInfo
operator|.
name|branch
argument_list|()
argument_list|,
name|changeId
argument_list|,
name|revision
argument_list|,
name|enabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"submit"
argument_list|)
DECL|method|onSubmit (@uppressWarningsR) ClickEvent e)
name|void
name|onSubmit
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|SubmitAction
operator|.
name|call
argument_list|(
name|changeInfo
argument_list|,
name|changeInfo
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"cherrypick"
argument_list|)
DECL|method|onCherryPick (@uppressWarningsR) ClickEvent e)
name|void
name|onCherryPick
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|CherryPickAction
operator|.
name|call
argument_list|(
name|cherrypick
argument_list|,
name|changeInfo
argument_list|,
name|revision
argument_list|,
name|project
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"revert"
argument_list|)
DECL|method|onRevert (@uppressWarningsR) ClickEvent e)
name|void
name|onRevert
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|e
parameter_list|)
block|{
name|RevertAction
operator|.
name|call
argument_list|(
name|revert
argument_list|,
name|changeId
argument_list|,
name|revision
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
DECL|method|a2b (NativeMap<ActionInfo> actions, String a, Button b)
specifier|private
specifier|static
name|void
name|a2b
parameter_list|(
name|NativeMap
argument_list|<
name|ActionInfo
argument_list|>
name|actions
parameter_list|,
name|String
name|a
parameter_list|,
name|Button
name|b
parameter_list|)
block|{
if|if
condition|(
name|actions
operator|.
name|containsKey
argument_list|(
name|a
argument_list|)
condition|)
block|{
name|b
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ActionInfo
name|actionInfo
init|=
name|actions
operator|.
name|get
argument_list|(
name|a
argument_list|)
decl_stmt|;
name|b
operator|.
name|setTitle
argument_list|(
name|actionInfo
operator|.
name|title
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|setEnabled
argument_list|(
name|actionInfo
operator|.
name|enabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

