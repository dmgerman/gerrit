begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2015 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|//you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|//You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|//distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|//See the License for the specific language governing permissions and
end_comment

begin_comment
comment|//limitations under the License.
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
name|user
operator|.
name|client
operator|.
name|PluginSafePopupPanel
import|;
end_import

begin_class
DECL|class|RenameFileAction
class|class
name|RenameFileAction
block|{
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|RevisionInfo
name|revision
decl_stmt|;
DECL|field|style
specifier|private
specifier|final
name|ChangeScreen
operator|.
name|Style
name|style
decl_stmt|;
DECL|field|renameButton
specifier|private
specifier|final
name|Widget
name|renameButton
decl_stmt|;
DECL|field|renameBox
specifier|private
name|RenameFileBox
name|renameBox
decl_stmt|;
DECL|field|popup
specifier|private
name|PopupPanel
name|popup
decl_stmt|;
DECL|method|RenameFileAction (Change.Id changeId, RevisionInfo revision, ChangeScreen.Style style, Widget renameButton)
name|RenameFileAction
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|RevisionInfo
name|revision
parameter_list|,
name|ChangeScreen
operator|.
name|Style
name|style
parameter_list|,
name|Widget
name|renameButton
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
name|this
operator|.
name|renameButton
operator|=
name|renameButton
expr_stmt|;
block|}
DECL|method|onRename ()
name|void
name|onRename
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
return|return;
block|}
if|if
condition|(
name|renameBox
operator|==
literal|null
condition|)
block|{
name|renameBox
operator|=
operator|new
name|RenameFileBox
argument_list|(
name|changeId
argument_list|,
name|revision
argument_list|)
expr_stmt|;
block|}
name|renameBox
operator|.
name|clearPath
argument_list|()
expr_stmt|;
specifier|final
name|PluginSafePopupPanel
name|p
init|=
operator|new
name|PluginSafePopupPanel
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|p
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|replyBox
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|addAutoHidePartner
argument_list|(
name|renameButton
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|p
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
if|if
condition|(
name|popup
operator|==
name|p
condition|)
block|{
name|popup
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|renameBox
argument_list|)
expr_stmt|;
name|p
operator|.
name|showRelativeTo
argument_list|(
name|renameButton
argument_list|)
expr_stmt|;
name|GlobalKey
operator|.
name|dialog
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|renameBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|popup
operator|=
name|p
expr_stmt|;
block|}
block|}
end_class

end_unit

