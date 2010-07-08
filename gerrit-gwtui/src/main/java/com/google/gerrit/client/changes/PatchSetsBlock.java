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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|ChangeDetail
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
name|OpenEvent
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
name|OpenHandler
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
name|DisclosurePanel
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
name|globalkey
operator|.
name|client
operator|.
name|GlobalKey
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
name|KeyCommand
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
name|KeyCommandSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_comment
comment|/**  * Composite that displays the patch sets of a change. This composite ensures  * that keyboard navigation to each changed file in all patch sets is possible.  */
end_comment

begin_class
DECL|class|PatchSetsBlock
specifier|public
class|class
name|PatchSetsBlock
extends|extends
name|Composite
block|{
DECL|field|patchSetPanels
specifier|private
specifier|final
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|PatchSetComplexDisclosurePanel
argument_list|>
name|patchSetPanels
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|PatchSetComplexDisclosurePanel
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|parent
specifier|private
specifier|final
name|ChangeScreen
name|parent
decl_stmt|;
DECL|field|body
specifier|private
specifier|final
name|FlowPanel
name|body
decl_stmt|;
DECL|field|regNavigation
specifier|private
name|HandlerRegistration
name|regNavigation
decl_stmt|;
comment|/**    * the patch set id of the patch set for which is the keyboard navigation is    * currently enabled    */
DECL|field|activePatchSetId
specifier|private
name|int
name|activePatchSetId
init|=
operator|-
literal|1
decl_stmt|;
comment|/** the patch set id of the current (latest) patch set */
DECL|field|currentPatchSetId
specifier|private
name|int
name|currentPatchSetId
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|PatchSetsBlock (final ChangeScreen parent)
name|PatchSetsBlock
parameter_list|(
specifier|final
name|ChangeScreen
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|body
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
comment|/** Adds UI elements for each patch set of the given change to this composite. */
DECL|method|display (final ChangeDetail detail)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|clear
argument_list|()
expr_stmt|;
specifier|final
name|PatchSet
name|currps
init|=
name|detail
operator|.
name|getCurrentPatchSet
argument_list|()
decl_stmt|;
name|currentPatchSetId
operator|=
name|currps
operator|.
name|getPatchSetId
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|PatchSet
name|ps
range|:
name|detail
operator|.
name|getPatchSets
argument_list|()
control|)
block|{
if|if
condition|(
name|ps
operator|==
name|currps
condition|)
block|{
name|add
argument_list|(
operator|new
name|PatchSetComplexDisclosurePanel
argument_list|(
name|parent
argument_list|,
name|detail
argument_list|,
name|detail
operator|.
name|getCurrentPatchSetDetail
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|add
argument_list|(
operator|new
name|PatchSetComplexDisclosurePanel
argument_list|(
name|parent
argument_list|,
name|detail
argument_list|,
name|ps
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|clear ()
specifier|private
name|void
name|clear
parameter_list|()
block|{
name|body
operator|.
name|clear
argument_list|()
expr_stmt|;
name|patchSetPanels
operator|.
name|clear
argument_list|()
expr_stmt|;
name|setRegisterKeys
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**    * Adds the given patch set panel to this composite and ensures that handler    * to activate / deactivate keyboard navigation for the patch set panel are    * registered.    */
DECL|method|add (final PatchSetComplexDisclosurePanel patchSetPanel)
specifier|private
name|void
name|add
parameter_list|(
specifier|final
name|PatchSetComplexDisclosurePanel
name|patchSetPanel
parameter_list|)
block|{
name|body
operator|.
name|add
argument_list|(
name|patchSetPanel
argument_list|)
expr_stmt|;
name|int
name|patchSetId
init|=
name|patchSetPanel
operator|.
name|getPatchSet
argument_list|()
operator|.
name|getPatchSetId
argument_list|()
decl_stmt|;
name|ActivationHandler
name|activationHandler
init|=
operator|new
name|ActivationHandler
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
name|patchSetPanel
operator|.
name|addOpenHandler
argument_list|(
name|activationHandler
argument_list|)
expr_stmt|;
name|patchSetPanel
operator|.
name|addClickHandler
argument_list|(
name|activationHandler
argument_list|)
expr_stmt|;
name|patchSetPanels
operator|.
name|put
argument_list|(
name|patchSetId
argument_list|,
name|patchSetPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|setRegisterKeys (final boolean on)
specifier|public
name|void
name|setRegisterKeys
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
name|KeyCommandSet
name|keysNavigation
init|=
operator|new
name|KeyCommandSet
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|sectionNavigation
argument_list|()
argument_list|)
decl_stmt|;
name|keysNavigation
operator|.
name|add
argument_list|(
operator|new
name|PreviousPatchSetKeyCommand
argument_list|(
literal|0
argument_list|,
literal|'p'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|previousPatchSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|keysNavigation
operator|.
name|add
argument_list|(
operator|new
name|NextPatchSetKeyCommand
argument_list|(
literal|0
argument_list|,
literal|'n'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|nextPatchSet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|regNavigation
operator|=
name|GlobalKey
operator|.
name|add
argument_list|(
name|this
argument_list|,
name|keysNavigation
argument_list|)
expr_stmt|;
if|if
condition|(
name|activePatchSetId
operator|!=
operator|-
literal|1
condition|)
block|{
name|activate
argument_list|(
name|activePatchSetId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|activate
argument_list|(
name|currentPatchSetId
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|regNavigation
operator|!=
literal|null
condition|)
block|{
name|regNavigation
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
name|regNavigation
operator|=
literal|null
expr_stmt|;
block|}
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
name|setRegisterKeys
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
block|}
comment|/**    * Activates keyboard navigation for the patch set panel that displays the    * patch set with the given patch set id.    * The keyboard navigation for the previously active patch set panel is    * automatically deactivated.    * This method also ensures that the current row is only highlighted in the    * table of the active patch set panel.    */
DECL|method|activate (final int patchSetId)
specifier|private
name|void
name|activate
parameter_list|(
specifier|final
name|int
name|patchSetId
parameter_list|)
block|{
if|if
condition|(
name|activePatchSetId
operator|!=
name|patchSetId
condition|)
block|{
name|deactivate
argument_list|()
expr_stmt|;
name|PatchSetComplexDisclosurePanel
name|patchSetPanel
init|=
name|patchSetPanels
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
name|patchSetPanel
operator|.
name|setOpen
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|patchSetPanel
operator|.
name|setActive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|activePatchSetId
operator|=
name|patchSetId
expr_stmt|;
block|}
block|}
comment|/** Deactivates the keyboard navigation for the currently active patch set panel. */
DECL|method|deactivate ()
specifier|private
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
name|activePatchSetId
operator|!=
operator|-
literal|1
condition|)
block|{
name|PatchSetComplexDisclosurePanel
name|patchSetPanel
init|=
name|patchSetPanels
operator|.
name|get
argument_list|(
name|activePatchSetId
argument_list|)
decl_stmt|;
name|patchSetPanel
operator|.
name|setActive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|activePatchSetId
operator|=
operator|-
literal|1
expr_stmt|;
block|}
block|}
DECL|method|getCurrentPatchSet ()
specifier|public
name|PatchSet
name|getCurrentPatchSet
parameter_list|()
block|{
name|PatchSetComplexDisclosurePanel
name|patchSetPanel
init|=
name|patchSetPanels
operator|.
name|get
argument_list|(
name|currentPatchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
name|patchSetPanel
operator|!=
literal|null
condition|)
block|{
return|return
name|patchSetPanel
operator|.
name|getPatchSet
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|ActivationHandler
specifier|private
class|class
name|ActivationHandler
implements|implements
name|OpenHandler
argument_list|<
name|DisclosurePanel
argument_list|>
implements|,
name|ClickHandler
block|{
DECL|field|patchSetId
specifier|private
specifier|final
name|int
name|patchSetId
decl_stmt|;
DECL|method|ActivationHandler (int patchSetId)
name|ActivationHandler
parameter_list|(
name|int
name|patchSetId
parameter_list|)
block|{
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onOpen (OpenEvent<DisclosurePanel> event)
specifier|public
name|void
name|onOpen
parameter_list|(
name|OpenEvent
argument_list|<
name|DisclosurePanel
argument_list|>
name|event
parameter_list|)
block|{
comment|// when a patch set panel is opened by the user
comment|// it should automatically become active
name|activate
argument_list|(
name|patchSetId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClick (ClickEvent event)
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
comment|// when a user clicks on a patch table the corresponding
comment|// patch set panel should automatically become active
name|activate
argument_list|(
name|patchSetId
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PreviousPatchSetKeyCommand
specifier|public
class|class
name|PreviousPatchSetKeyCommand
extends|extends
name|KeyCommand
block|{
DECL|method|PreviousPatchSetKeyCommand (int mask, char key, String help)
specifier|public
name|PreviousPatchSetKeyCommand
parameter_list|(
name|int
name|mask
parameter_list|,
name|char
name|key
parameter_list|,
name|String
name|help
parameter_list|)
block|{
name|super
argument_list|(
name|mask
argument_list|,
name|key
argument_list|,
name|help
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|activePatchSetId
operator|>
literal|1
condition|)
block|{
name|activate
argument_list|(
name|activePatchSetId
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|NextPatchSetKeyCommand
specifier|public
class|class
name|NextPatchSetKeyCommand
extends|extends
name|KeyCommand
block|{
DECL|method|NextPatchSetKeyCommand (int mask, char key, String help)
specifier|public
name|NextPatchSetKeyCommand
parameter_list|(
name|int
name|mask
parameter_list|,
name|char
name|key
parameter_list|,
name|String
name|help
parameter_list|)
block|{
name|super
argument_list|(
name|mask
argument_list|,
name|key
argument_list|,
name|help
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|activePatchSetId
operator|>
literal|0
operator|&&
name|activePatchSetId
operator|<
name|body
operator|.
name|getWidgetCount
argument_list|()
condition|)
block|{
name|activate
argument_list|(
name|activePatchSetId
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

