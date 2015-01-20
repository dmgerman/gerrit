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
name|VoidResult
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
name|ChangeEditApi
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
name|ui
operator|.
name|RemoteSuggestBox
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
name|rpc
operator|.
name|AsyncCallback
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
name|NpTextBox
import|;
end_import

begin_class
DECL|class|RenameFileBox
class|class
name|RenameFileBox
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
name|RenameFileBox
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
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|rename
annotation|@
name|UiField
name|Button
name|rename
decl_stmt|;
DECL|field|cancel
annotation|@
name|UiField
name|Button
name|cancel
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|path
name|RemoteSuggestBox
name|path
decl_stmt|;
DECL|field|newPath
annotation|@
name|UiField
name|NpTextBox
name|newPath
decl_stmt|;
DECL|method|RenameFileBox (Change.Id changeId, RevisionInfo revision)
name|RenameFileBox
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|RevisionInfo
name|revision
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|path
operator|=
operator|new
name|RemoteSuggestBox
argument_list|(
operator|new
name|PathSuggestOracle
argument_list|(
name|changeId
argument_list|,
name|revision
argument_list|)
argument_list|)
expr_stmt|;
name|path
operator|.
name|addCloseHandler
argument_list|(
operator|new
name|CloseHandler
argument_list|<
name|RemoteSuggestBox
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
name|RemoteSuggestBox
argument_list|>
name|event
parameter_list|)
block|{
name|hide
argument_list|()
expr_stmt|;
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
block|}
DECL|method|setFocus (boolean focus)
name|void
name|setFocus
parameter_list|(
name|boolean
name|focus
parameter_list|)
block|{
name|path
operator|.
name|setFocus
argument_list|(
name|focus
argument_list|)
expr_stmt|;
block|}
DECL|method|clearPath ()
name|void
name|clearPath
parameter_list|()
block|{
name|path
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"rename"
argument_list|)
DECL|method|onRename (@uppressWarningsR) ClickEvent e)
name|void
name|onRename
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
name|rename
argument_list|(
name|path
operator|.
name|getText
argument_list|()
argument_list|,
name|newPath
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|rename (String path, String newPath)
specifier|private
name|void
name|rename
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|newPath
parameter_list|)
block|{
name|hide
argument_list|()
expr_stmt|;
name|ChangeEditApi
operator|.
name|rename
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|path
argument_list|,
name|newPath
argument_list|,
operator|new
name|AsyncCallback
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
name|display
argument_list|(
name|PageLinks
operator|.
name|toChangeInEditMode
argument_list|(
name|changeId
argument_list|)
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
block|{           }
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"cancel"
argument_list|)
DECL|method|onCancel (@uppressWarningsR) ClickEvent e)
name|void
name|onCancel
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
block|}
end_class

end_unit

