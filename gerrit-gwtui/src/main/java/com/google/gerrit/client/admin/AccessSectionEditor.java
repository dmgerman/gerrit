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
name|SpanElement
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
name|ValueListBox
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

begin_class
DECL|class|AccessSectionEditor
specifier|public
class|class
name|AccessSectionEditor
extends|extends
name|Composite
implements|implements
name|Editor
argument_list|<
name|AccessSection
argument_list|>
implements|,
name|ValueAwareEditor
argument_list|<
name|AccessSection
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
name|AccessSectionEditor
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
DECL|field|name
annotation|@
name|UiField
name|ValueEditor
argument_list|<
name|String
argument_list|>
name|name
decl_stmt|;
DECL|field|permissionContainer
annotation|@
name|UiField
name|FlowPanel
name|permissionContainer
decl_stmt|;
DECL|field|permissions
name|ListEditor
argument_list|<
name|Permission
argument_list|,
name|PermissionEditor
argument_list|>
name|permissions
decl_stmt|;
DECL|field|addContainer
annotation|@
name|UiField
name|DivElement
name|addContainer
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
annotation|@
name|Editor
operator|.
name|Ignore
DECL|field|permissionSelector
name|ValueListBox
argument_list|<
name|String
argument_list|>
name|permissionSelector
decl_stmt|;
DECL|field|deletedName
annotation|@
name|UiField
name|SpanElement
name|deletedName
decl_stmt|;
DECL|field|deleteSection
annotation|@
name|UiField
name|Anchor
name|deleteSection
decl_stmt|;
DECL|field|normal
annotation|@
name|UiField
name|DivElement
name|normal
decl_stmt|;
DECL|field|deleted
annotation|@
name|UiField
name|DivElement
name|deleted
decl_stmt|;
DECL|field|sectionType
annotation|@
name|UiField
name|SpanElement
name|sectionType
decl_stmt|;
DECL|field|sectionName
annotation|@
name|UiField
name|SpanElement
name|sectionName
decl_stmt|;
DECL|field|projectAccess
specifier|private
specifier|final
name|ProjectAccess
name|projectAccess
decl_stmt|;
DECL|field|value
specifier|private
name|AccessSection
name|value
decl_stmt|;
DECL|field|editing
specifier|private
name|boolean
name|editing
decl_stmt|;
DECL|field|readOnly
specifier|private
name|boolean
name|readOnly
decl_stmt|;
DECL|field|isDeleted
specifier|private
name|boolean
name|isDeleted
decl_stmt|;
DECL|method|AccessSectionEditor (ProjectAccess access)
specifier|public
name|AccessSectionEditor
parameter_list|(
name|ProjectAccess
name|access
parameter_list|)
block|{
name|projectAccess
operator|=
name|access
expr_stmt|;
name|permissionSelector
operator|=
operator|new
name|ValueListBox
argument_list|<>
argument_list|(
operator|new
name|PermissionNameRenderer
argument_list|(
name|access
operator|.
name|getCapabilities
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|permissionSelector
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|String
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
name|String
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
operator|!
name|Util
operator|.
name|C
operator|.
name|addPermission
argument_list|()
operator|.
name|equals
argument_list|(
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|onAddPermission
argument_list|(
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|permissions
operator|=
name|ListEditor
operator|.
name|of
argument_list|(
operator|new
name|PermissionEditorSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"deleteSection"
argument_list|)
DECL|method|onDeleteHover (@uppressWarningsR) MouseOverEvent event)
name|void
name|onDeleteHover
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|MouseOverEvent
name|event
parameter_list|)
block|{
name|normal
operator|.
name|addClassName
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
literal|"deleteSection"
argument_list|)
DECL|method|onDeleteNonHover (@uppressWarningsR) MouseOutEvent event)
name|void
name|onDeleteNonHover
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|MouseOutEvent
name|event
parameter_list|)
block|{
name|normal
operator|.
name|removeClassName
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
literal|"deleteSection"
argument_list|)
DECL|method|onDeleteSection (@uppressWarningsR) ClickEvent event)
name|void
name|onDeleteSection
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
name|ClickEvent
name|event
parameter_list|)
block|{
name|isDeleted
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|name
operator|.
name|isVisible
argument_list|()
operator|&&
name|RefConfigSection
operator|.
name|isValid
argument_list|(
name|name
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|deletedName
operator|.
name|setInnerText
argument_list|(
name|Util
operator|.
name|M
operator|.
name|deletedReference
argument_list|(
name|name
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|Util
operator|.
name|C
operator|.
name|sectionNames
argument_list|()
operator|.
name|get
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|value
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|deletedName
operator|.
name|setInnerText
argument_list|(
name|Util
operator|.
name|M
operator|.
name|deletedSection
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|onUndoDelete (@uppressWarningsR) ClickEvent event)
name|void
name|onUndoDelete
parameter_list|(
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
DECL|method|onAddPermission (String varName)
name|void
name|onAddPermission
parameter_list|(
name|String
name|varName
parameter_list|)
block|{
name|int
name|idx
init|=
name|permissions
operator|.
name|getList
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
name|Permission
name|p
init|=
name|value
operator|.
name|getPermission
argument_list|(
name|varName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|permissions
operator|.
name|getList
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|PermissionEditor
name|e
init|=
name|permissions
operator|.
name|getEditors
argument_list|()
operator|.
name|get
argument_list|(
name|idx
argument_list|)
decl_stmt|;
name|e
operator|.
name|beginAddRule
argument_list|()
expr_stmt|;
name|rebuildPermissionSelector
argument_list|()
expr_stmt|;
block|}
DECL|method|editRefPattern ()
name|void
name|editRefPattern
parameter_list|()
block|{
name|name
operator|.
name|edit
argument_list|()
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
name|name
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
DECL|method|enableEditing ()
name|void
name|enableEditing
parameter_list|()
block|{
name|readOnly
operator|=
literal|false
expr_stmt|;
name|addContainer
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
name|rebuildPermissionSelector
argument_list|()
expr_stmt|;
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
DECL|method|setValue (AccessSection value)
specifier|public
name|void
name|setValue
parameter_list|(
name|AccessSection
name|value
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|value
operator|.
name|getPermissions
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|readOnly
operator|=
operator|!
name|editing
operator|||
operator|!
operator|(
name|projectAccess
operator|.
name|isOwnerOf
argument_list|(
name|value
argument_list|)
operator|||
name|projectAccess
operator|.
name|canUpload
argument_list|()
operator|)
expr_stmt|;
name|name
operator|.
name|setEnabled
argument_list|(
operator|!
name|readOnly
argument_list|)
expr_stmt|;
name|deleteSection
operator|.
name|setVisible
argument_list|(
operator|!
name|readOnly
argument_list|)
expr_stmt|;
if|if
condition|(
name|RefConfigSection
operator|.
name|isValid
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|name
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|name
operator|.
name|setIgnoreEditorValue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sectionType
operator|.
name|setInnerText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|sectionTypeReference
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|name
operator|.
name|setIgnoreEditorValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|String
name|name
init|=
name|Util
operator|.
name|C
operator|.
name|sectionNames
argument_list|()
operator|.
name|get
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
name|sectionType
operator|.
name|setInnerText
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|sectionName
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
else|else
block|{
name|sectionType
operator|.
name|setInnerText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|sectionTypeSection
argument_list|()
argument_list|)
expr_stmt|;
name|sectionName
operator|.
name|setInnerText
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sectionName
operator|.
name|getStyle
argument_list|()
operator|.
name|clearDisplay
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|readOnly
condition|)
block|{
name|addContainer
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
else|else
block|{
name|enableEditing
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setEditing (final boolean editing)
name|void
name|setEditing
parameter_list|(
specifier|final
name|boolean
name|editing
parameter_list|)
block|{
name|this
operator|.
name|editing
operator|=
name|editing
expr_stmt|;
block|}
DECL|method|rebuildPermissionSelector ()
specifier|private
name|void
name|rebuildPermissionSelector
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|perms
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|AccessSection
operator|.
name|GLOBAL_CAPABILITIES
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
for|for
control|(
name|String
name|varName
range|:
name|projectAccess
operator|.
name|getCapabilities
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|addPermission
argument_list|(
name|varName
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|RefConfigSection
operator|.
name|isValid
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|LabelType
name|t
range|:
name|projectAccess
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
control|)
block|{
name|addPermission
argument_list|(
name|Permission
operator|.
name|forLabel
argument_list|(
name|t
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|LabelType
name|t
range|:
name|projectAccess
operator|.
name|getLabelTypes
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
control|)
block|{
name|addPermission
argument_list|(
name|Permission
operator|.
name|forLabelAs
argument_list|(
name|t
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|varName
range|:
name|Util
operator|.
name|C
operator|.
name|permissionNames
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|addPermission
argument_list|(
name|varName
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|perms
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|addContainer
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
else|else
block|{
name|addContainer
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
name|perms
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|addPermission
argument_list|()
argument_list|)
expr_stmt|;
name|permissionSelector
operator|.
name|setValue
argument_list|(
name|Util
operator|.
name|C
operator|.
name|addPermission
argument_list|()
argument_list|)
expr_stmt|;
name|permissionSelector
operator|.
name|setAcceptableValues
argument_list|(
name|perms
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addPermission (final String permissionName, final List<String> permissionList)
specifier|private
name|void
name|addPermission
parameter_list|(
specifier|final
name|String
name|permissionName
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|permissionList
parameter_list|)
block|{
if|if
condition|(
name|value
operator|.
name|getPermission
argument_list|(
name|permissionName
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|Gerrit
operator|.
name|info
argument_list|()
operator|.
name|gerrit
argument_list|()
operator|.
name|isAllProjects
argument_list|(
name|projectAccess
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|&&
operator|!
name|Permission
operator|.
name|canBeOnAllProjects
argument_list|(
name|value
operator|.
name|getName
argument_list|()
argument_list|,
name|permissionName
argument_list|)
condition|)
block|{
return|return;
block|}
name|permissionList
operator|.
name|add
argument_list|(
name|permissionName
argument_list|)
expr_stmt|;
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
name|Permission
argument_list|>
name|src
init|=
name|permissions
operator|.
name|getList
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Permission
argument_list|>
name|keep
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|PermissionEditor
name|e
init|=
operator|(
name|PermissionEditor
operator|)
name|permissionContainer
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
name|setPermissions
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
block|{}
annotation|@
name|Override
DECL|method|setDelegate (EditorDelegate<AccessSection> delegate)
specifier|public
name|void
name|setDelegate
parameter_list|(
name|EditorDelegate
argument_list|<
name|AccessSection
argument_list|>
name|delegate
parameter_list|)
block|{}
DECL|class|PermissionEditorSource
specifier|private
class|class
name|PermissionEditorSource
extends|extends
name|EditorSource
argument_list|<
name|PermissionEditor
argument_list|>
block|{
annotation|@
name|Override
DECL|method|create (int index)
specifier|public
name|PermissionEditor
name|create
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|PermissionEditor
name|subEditor
init|=
operator|new
name|PermissionEditor
argument_list|(
name|projectAccess
argument_list|,
name|readOnly
argument_list|,
name|value
argument_list|,
name|projectAccess
operator|.
name|getLabelTypes
argument_list|()
argument_list|)
decl_stmt|;
name|permissionContainer
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
DECL|method|dispose (PermissionEditor subEditor)
specifier|public
name|void
name|dispose
parameter_list|(
name|PermissionEditor
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
DECL|method|setIndex (PermissionEditor subEditor, int index)
specifier|public
name|void
name|setIndex
parameter_list|(
name|PermissionEditor
name|subEditor
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|permissionContainer
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

