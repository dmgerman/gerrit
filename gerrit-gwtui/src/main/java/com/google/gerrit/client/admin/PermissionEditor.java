begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|SuggestUtil
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
name|AccessSection
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
name|GlobalCapability
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
name|GroupReference
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
name|LabelType
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
name|LabelTypes
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
name|Permission
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
name|PermissionRange
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
name|PermissionRule
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
name|ProjectAccess
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
name|RefConfigSection
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
name|Project
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
name|DivElement
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
name|Style
operator|.
name|Display
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
name|editor
operator|.
name|client
operator|.
name|Editor
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
name|editor
operator|.
name|client
operator|.
name|EditorDelegate
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
name|editor
operator|.
name|client
operator|.
name|ValueAwareEditor
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
name|editor
operator|.
name|client
operator|.
name|adapters
operator|.
name|EditorSource
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
name|editor
operator|.
name|client
operator|.
name|adapters
operator|.
name|ListEditor
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
name|MouseOutEvent
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
name|MouseOverEvent
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
name|CloseEvent
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
name|SelectionEvent
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
name|FlowPanel
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
name|ValueLabel
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
name|List
import|;
end_import

begin_class
DECL|class|PermissionEditor
specifier|public
class|class
name|PermissionEditor
extends|extends
name|Composite
implements|implements
name|Editor
argument_list|<
name|Permission
argument_list|>
implements|,
name|ValueAwareEditor
argument_list|<
name|Permission
argument_list|>
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|PermissionEditor
argument_list|>
block|{   }
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
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"name"
argument_list|)
DECL|field|normalName
name|ValueLabel
argument_list|<
name|String
argument_list|>
name|normalName
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"name"
argument_list|)
DECL|field|deletedName
name|ValueLabel
argument_list|<
name|String
argument_list|>
name|deletedName
decl_stmt|;
annotation|@
name|UiField
DECL|field|exclusiveGroup
name|CheckBox
name|exclusiveGroup
decl_stmt|;
annotation|@
name|UiField
DECL|field|ruleContainer
name|FlowPanel
name|ruleContainer
decl_stmt|;
DECL|field|rules
name|ListEditor
argument_list|<
name|PermissionRule
argument_list|,
name|PermissionRuleEditor
argument_list|>
name|rules
decl_stmt|;
annotation|@
name|UiField
DECL|field|addContainer
name|DivElement
name|addContainer
decl_stmt|;
annotation|@
name|UiField
DECL|field|addStage1
name|DivElement
name|addStage1
decl_stmt|;
annotation|@
name|UiField
DECL|field|addStage2
name|DivElement
name|addStage2
decl_stmt|;
annotation|@
name|UiField
DECL|field|beginAddRule
name|Anchor
name|beginAddRule
decl_stmt|;
annotation|@
name|UiField
annotation|@
name|Editor
operator|.
name|Ignore
DECL|field|groupToAdd
name|GroupReferenceBox
name|groupToAdd
decl_stmt|;
annotation|@
name|UiField
DECL|field|addRule
name|Button
name|addRule
decl_stmt|;
annotation|@
name|UiField
DECL|field|deletePermission
name|Anchor
name|deletePermission
decl_stmt|;
annotation|@
name|UiField
DECL|field|normal
name|DivElement
name|normal
decl_stmt|;
annotation|@
name|UiField
DECL|field|deleted
name|DivElement
name|deleted
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|field|readOnly
specifier|private
specifier|final
name|boolean
name|readOnly
decl_stmt|;
DECL|field|section
specifier|private
specifier|final
name|AccessSection
name|section
decl_stmt|;
DECL|field|labelTypes
specifier|private
specifier|final
name|LabelTypes
name|labelTypes
decl_stmt|;
DECL|field|value
specifier|private
name|Permission
name|value
decl_stmt|;
DECL|field|validRange
specifier|private
name|PermissionRange
operator|.
name|WithDefaults
name|validRange
decl_stmt|;
DECL|field|isDeleted
specifier|private
name|boolean
name|isDeleted
decl_stmt|;
DECL|method|PermissionEditor (ProjectAccess projectAccess, boolean readOnly, AccessSection section, LabelTypes labelTypes)
specifier|public
name|PermissionEditor
parameter_list|(
name|ProjectAccess
name|projectAccess
parameter_list|,
name|boolean
name|readOnly
parameter_list|,
name|AccessSection
name|section
parameter_list|,
name|LabelTypes
name|labelTypes
parameter_list|)
block|{
name|this
operator|.
name|readOnly
operator|=
name|readOnly
expr_stmt|;
name|this
operator|.
name|section
operator|=
name|section
expr_stmt|;
name|this
operator|.
name|projectName
operator|=
name|projectAccess
operator|.
name|getProjectName
argument_list|()
expr_stmt|;
name|this
operator|.
name|labelTypes
operator|=
name|labelTypes
expr_stmt|;
name|PermissionNameRenderer
name|nameRenderer
init|=
operator|new
name|PermissionNameRenderer
argument_list|(
name|projectAccess
operator|.
name|getCapabilities
argument_list|()
argument_list|)
decl_stmt|;
name|normalName
operator|=
operator|new
name|ValueLabel
argument_list|<
name|String
argument_list|>
argument_list|(
name|nameRenderer
argument_list|)
expr_stmt|;
name|deletedName
operator|=
operator|new
name|ValueLabel
argument_list|<
name|String
argument_list|>
argument_list|(
name|nameRenderer
argument_list|)
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
name|groupToAdd
operator|.
name|setProject
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|rules
operator|=
name|ListEditor
operator|.
name|of
argument_list|(
operator|new
name|RuleEditorSource
argument_list|()
argument_list|)
expr_stmt|;
name|exclusiveGroup
operator|.
name|setEnabled
argument_list|(
operator|!
name|readOnly
argument_list|)
expr_stmt|;
name|exclusiveGroup
operator|.
name|setVisible
argument_list|(
name|RefConfigSection
operator|.
name|isValid
argument_list|(
name|section
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|readOnly
condition|)
block|{
name|addContainer
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|addContainer
operator|=
literal|null
expr_stmt|;
name|deletePermission
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|deletePermission
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"deletePermission"
argument_list|)
DECL|method|onDeleteHover (MouseOverEvent event)
name|void
name|onDeleteHover
parameter_list|(
name|MouseOverEvent
name|event
parameter_list|)
block|{
name|addStyleName
argument_list|(
name|AdminResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|deleteSectionHover
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"deletePermission"
argument_list|)
DECL|method|onDeleteNonHover (MouseOutEvent event)
name|void
name|onDeleteNonHover
parameter_list|(
name|MouseOutEvent
name|event
parameter_list|)
block|{
name|removeStyleName
argument_list|(
name|AdminResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|deleteSectionHover
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"deletePermission"
argument_list|)
DECL|method|onDeletePermission (ClickEvent event)
name|void
name|onDeletePermission
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|isDeleted
operator|=
literal|true
expr_stmt|;
name|normal
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|deleted
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|BLOCK
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"undoDelete"
argument_list|)
DECL|method|onUndoDelete (ClickEvent event)
name|void
name|onUndoDelete
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|isDeleted
operator|=
literal|false
expr_stmt|;
name|deleted
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|normal
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|BLOCK
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"beginAddRule"
argument_list|)
DECL|method|onBeginAddRule (ClickEvent event)
name|void
name|onBeginAddRule
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|beginAddRule
argument_list|()
expr_stmt|;
block|}
DECL|method|beginAddRule ()
name|void
name|beginAddRule
parameter_list|()
block|{
name|addStage1
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|addStage2
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|BLOCK
argument_list|)
expr_stmt|;
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
name|groupToAdd
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
literal|"addRule"
argument_list|)
DECL|method|onAddGroupByClick (ClickEvent event)
name|void
name|onAddGroupByClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|GroupReference
name|ref
init|=
name|groupToAdd
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|addGroup
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupToAdd
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
literal|"groupToAdd"
argument_list|)
DECL|method|onAddGroupByEnter (SelectionEvent<GroupReference> event)
name|void
name|onAddGroupByEnter
parameter_list|(
name|SelectionEvent
argument_list|<
name|GroupReference
argument_list|>
name|event
parameter_list|)
block|{
name|GroupReference
name|ref
init|=
name|event
operator|.
name|getSelectedItem
argument_list|()
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|addGroup
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"groupToAdd"
argument_list|)
DECL|method|onAbortAddGroup (CloseEvent<GroupReferenceBox> event)
name|void
name|onAbortAddGroup
parameter_list|(
name|CloseEvent
argument_list|<
name|GroupReferenceBox
argument_list|>
name|event
parameter_list|)
block|{
name|hideAddGroup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"hideAddGroup"
argument_list|)
DECL|method|hideAddGroup (ClickEvent event)
name|void
name|hideAddGroup
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|hideAddGroup
argument_list|()
expr_stmt|;
block|}
DECL|method|hideAddGroup ()
specifier|private
name|void
name|hideAddGroup
parameter_list|()
block|{
name|addStage1
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|BLOCK
argument_list|)
expr_stmt|;
name|addStage2
operator|.
name|getStyle
argument_list|()
operator|.
name|setDisplay
argument_list|(
name|Display
operator|.
name|NONE
argument_list|)
expr_stmt|;
block|}
DECL|method|addGroup (GroupReference ref)
specifier|private
name|void
name|addGroup
parameter_list|(
name|GroupReference
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|.
name|getUUID
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|value
operator|.
name|getRule
argument_list|(
name|ref
argument_list|)
operator|==
literal|null
condition|)
block|{
name|PermissionRule
name|newRule
init|=
name|value
operator|.
name|getRule
argument_list|(
name|ref
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|validRange
operator|!=
literal|null
condition|)
block|{
name|int
name|min
init|=
name|validRange
operator|.
name|getDefaultMin
argument_list|()
decl_stmt|;
name|int
name|max
init|=
name|validRange
operator|.
name|getDefaultMax
argument_list|()
decl_stmt|;
name|newRule
operator|.
name|setRange
argument_list|(
name|min
argument_list|,
name|max
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|GlobalCapability
operator|.
name|PRIORITY
operator|.
name|equals
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|newRule
operator|.
name|setAction
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|BATCH
argument_list|)
expr_stmt|;
block|}
name|rules
operator|.
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|newRule
argument_list|)
expr_stmt|;
block|}
name|groupToAdd
operator|.
name|setValue
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|groupToAdd
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// If the oracle didn't get to complete a UUID, resolve it now.
comment|//
name|addRule
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|SuggestUtil
operator|.
name|SVC
operator|.
name|suggestAccountGroupForProject
argument_list|(
name|projectName
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
literal|1
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|List
argument_list|<
name|GroupReference
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|List
argument_list|<
name|GroupReference
argument_list|>
name|result
parameter_list|)
block|{
name|addRule
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|addGroup
argument_list|(
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|groupToAdd
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|addRule
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|isDeleted ()
name|boolean
name|isDeleted
parameter_list|()
block|{
return|return
name|isDeleted
return|;
block|}
annotation|@
name|Override
DECL|method|setValue (Permission value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Permission
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
if|if
condition|(
name|Permission
operator|.
name|hasRange
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|LabelType
name|lt
init|=
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|value
operator|.
name|getLabel
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lt
operator|!=
literal|null
condition|)
block|{
name|validRange
operator|=
operator|new
name|PermissionRange
operator|.
name|WithDefaults
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|,
name|lt
operator|.
name|getMin
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|lt
operator|.
name|getMax
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|lt
operator|.
name|getMin
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|,
name|lt
operator|.
name|getMax
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|GlobalCapability
operator|.
name|isCapability
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validRange
operator|=
name|GlobalCapability
operator|.
name|getRange
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|validRange
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|Permission
operator|.
name|OWNER
operator|.
name|equals
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|exclusiveGroup
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exclusiveGroup
operator|.
name|setEnabled
argument_list|(
operator|!
name|readOnly
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|flush ()
specifier|public
name|void
name|flush
parameter_list|()
block|{
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|src
init|=
name|rules
operator|.
name|getList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|PermissionRule
argument_list|>
name|keep
init|=
operator|new
name|ArrayList
argument_list|<
name|PermissionRule
argument_list|>
argument_list|(
name|src
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
name|src
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|PermissionRuleEditor
name|e
init|=
operator|(
name|PermissionRuleEditor
operator|)
name|ruleContainer
operator|.
name|getWidget
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|e
operator|.
name|isDeleted
argument_list|()
condition|)
block|{
name|keep
operator|.
name|add
argument_list|(
name|src
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|value
operator|.
name|setRules
argument_list|(
name|keep
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onPropertyChange (String... paths)
specifier|public
name|void
name|onPropertyChange
parameter_list|(
name|String
modifier|...
name|paths
parameter_list|)
block|{   }
annotation|@
name|Override
DECL|method|setDelegate (EditorDelegate<Permission> delegate)
specifier|public
name|void
name|setDelegate
parameter_list|(
name|EditorDelegate
argument_list|<
name|Permission
argument_list|>
name|delegate
parameter_list|)
block|{   }
DECL|class|RuleEditorSource
specifier|private
class|class
name|RuleEditorSource
extends|extends
name|EditorSource
argument_list|<
name|PermissionRuleEditor
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (int index)
specifier|public
name|PermissionRuleEditor
name|create
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|PermissionRuleEditor
name|subEditor
init|=
operator|new
name|PermissionRuleEditor
argument_list|(
name|readOnly
argument_list|,
name|section
argument_list|,
name|value
argument_list|,
name|validRange
argument_list|)
decl_stmt|;
name|ruleContainer
operator|.
name|insert
argument_list|(
name|subEditor
argument_list|,
name|index
argument_list|)
expr_stmt|;
return|return
name|subEditor
return|;
block|}
annotation|@
name|Override
DECL|method|dispose (PermissionRuleEditor subEditor)
specifier|public
name|void
name|dispose
parameter_list|(
name|PermissionRuleEditor
name|subEditor
parameter_list|)
block|{
name|subEditor
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setIndex (PermissionRuleEditor subEditor, int index)
specifier|public
name|void
name|setIndex
parameter_list|(
name|PermissionRuleEditor
name|subEditor
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|ruleContainer
operator|.
name|insert
argument_list|(
name|subEditor
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

