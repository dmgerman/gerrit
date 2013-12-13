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
name|CloseHandler
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
name|PopupPanel
operator|.
name|PositionCallback
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

begin_class
DECL|class|PreferencesAction
class|class
name|PreferencesAction
block|{
DECL|field|view
specifier|private
specifier|final
name|SideBySide2
name|view
decl_stmt|;
DECL|field|prefs
specifier|private
specifier|final
name|DiffPreferences
name|prefs
decl_stmt|;
DECL|field|popup
specifier|private
name|PopupPanel
name|popup
decl_stmt|;
DECL|field|current
specifier|private
name|PreferencesBox
name|current
decl_stmt|;
DECL|field|partner
specifier|private
name|Widget
name|partner
decl_stmt|;
DECL|method|PreferencesAction (SideBySide2 view, DiffPreferences prefs)
name|PreferencesAction
parameter_list|(
name|SideBySide2
name|view
parameter_list|,
name|DiffPreferences
name|prefs
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
name|prefs
operator|=
name|prefs
expr_stmt|;
block|}
DECL|method|update ()
name|void
name|update
parameter_list|()
block|{
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|current
operator|.
name|set
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|show ()
name|void
name|show
parameter_list|()
block|{
if|if
condition|(
name|popup
operator|!=
literal|null
condition|)
block|{
comment|// Already open? Close the dialog.
name|hide
argument_list|()
expr_stmt|;
return|return;
block|}
name|current
operator|=
operator|new
name|PreferencesBox
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|current
operator|.
name|set
argument_list|(
name|prefs
argument_list|)
expr_stmt|;
name|popup
operator|=
operator|new
name|PopupPanel
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|popup
operator|.
name|setStyleName
argument_list|(
name|current
operator|.
name|style
operator|.
name|dialog
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|add
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|popup
operator|.
name|addAutoHidePartner
argument_list|(
name|partner
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|addCloseHandler
argument_list|(
operator|new
name|CloseHandler
argument_list|<
name|PopupPanel
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|CloseEvent
argument_list|<
name|PopupPanel
argument_list|>
name|event
parameter_list|)
block|{
name|view
operator|.
name|getCmB
argument_list|()
operator|.
name|focus
argument_list|()
expr_stmt|;
name|popup
operator|=
literal|null
expr_stmt|;
name|current
operator|=
literal|null
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|popup
operator|.
name|setPopupPositionAndShow
argument_list|(
operator|new
name|PositionCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|setPosition
parameter_list|(
name|int
name|offsetWidth
parameter_list|,
name|int
name|offsetHeight
parameter_list|)
block|{
name|popup
operator|.
name|setPopupPosition
argument_list|(
literal|300
argument_list|,
literal|120
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|current
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|hide ()
name|void
name|hide
parameter_list|()
block|{
if|if
condition|(
name|popup
operator|!=
literal|null
condition|)
block|{
name|popup
operator|.
name|hide
argument_list|()
expr_stmt|;
name|popup
operator|=
literal|null
expr_stmt|;
name|current
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|setPartner (Widget w)
name|void
name|setPartner
parameter_list|(
name|Widget
name|w
parameter_list|)
block|{
name|partner
operator|=
name|w
expr_stmt|;
block|}
block|}
end_class

end_unit

