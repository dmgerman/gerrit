begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
name|ChangeFileApi
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
name|TextBoxChangeListener
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
name|NpTextArea
import|;
end_import

begin_class
DECL|class|EditFileBox
class|class
name|EditFileBox
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
name|EditFileBox
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
DECL|field|id
specifier|final
specifier|private
name|PatchSet
operator|.
name|Id
name|id
decl_stmt|;
DECL|field|fileName
specifier|final
specifier|private
name|String
name|fileName
decl_stmt|;
DECL|field|fileContent
specifier|final
specifier|private
name|String
name|fileContent
decl_stmt|;
DECL|field|file
annotation|@
name|UiField
name|FileTextBox
name|file
decl_stmt|;
DECL|field|content
annotation|@
name|UiField
name|NpTextArea
name|content
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
DECL|method|EditFileBox ( PatchSet.Id id, String fileC, String fileName)
name|EditFileBox
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
name|String
name|fileC
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
name|this
operator|.
name|fileContent
operator|=
name|fileC
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
operator|new
name|TextBoxChangeListener
argument_list|(
name|content
argument_list|)
block|{
specifier|public
name|void
name|onTextChanged
parameter_list|(
name|String
name|newText
parameter_list|)
block|{
name|save
operator|.
name|setEnabled
argument_list|(
operator|!
name|file
operator|.
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|newText
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
name|fileContent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|file
operator|.
name|set
argument_list|(
name|id
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|file
operator|.
name|setText
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|file
operator|.
name|setEnabled
argument_list|(
name|fileName
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|.
name|setText
argument_list|(
name|fileContent
argument_list|)
expr_stmt|;
name|save
operator|.
name|setEnabled
argument_list|(
literal|false
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
name|content
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
name|ChangeFileApi
operator|.
name|putContent
argument_list|(
name|id
argument_list|,
name|file
operator|.
name|getText
argument_list|()
argument_list|,
name|content
operator|.
name|getText
argument_list|()
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
name|id
operator|.
name|getParentKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|hide
argument_list|()
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
DECL|method|onCancel (ClickEvent e)
name|void
name|onCancel
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|hide
argument_list|()
expr_stmt|;
block|}
DECL|method|hide ()
specifier|protected
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

