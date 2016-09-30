begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|NotSignedInDialog
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
name|Util
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
name|info
operator|.
name|AccountInfo
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
name|info
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|SelectionHandler
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
name|StatusCodeException
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

begin_comment
comment|/**  * Edit assignee using auto-completion.  */
end_comment

begin_class
DECL|class|Assignee
specifier|public
class|class
name|Assignee
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
name|Assignee
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
DECL|field|assigneeLink
name|InlineHyperlink
name|assigneeLink
decl_stmt|;
annotation|@
name|UiField
DECL|field|editAssigneeIcon
name|Image
name|editAssigneeIcon
decl_stmt|;
annotation|@
name|UiField
DECL|field|form
name|Element
name|form
decl_stmt|;
annotation|@
name|UiField
DECL|field|error
name|Element
name|error
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|suggestBox
name|RemoteSuggestBox
name|suggestBox
decl_stmt|;
DECL|field|assigneeSuggestOracle
specifier|private
name|AssigneeSuggestOracle
name|assigneeSuggestOracle
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|canEdit
specifier|private
name|boolean
name|canEdit
decl_stmt|;
DECL|method|Assignee ()
name|Assignee
parameter_list|()
block|{
name|assigneeSuggestOracle
operator|=
operator|new
name|AssigneeSuggestOracle
argument_list|()
expr_stmt|;
name|suggestBox
operator|=
operator|new
name|RemoteSuggestBox
argument_list|(
name|assigneeSuggestOracle
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setVisibleLength
argument_list|(
literal|55
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setHintText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|approvalTableEditAssigneeHint
argument_list|()
argument_list|)
expr_stmt|;
name|suggestBox
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
name|Assignee
operator|.
name|this
operator|.
name|onCancel
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|addSelectionHandler
argument_list|(
operator|new
name|SelectionHandler
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSelection
parameter_list|(
name|SelectionEvent
argument_list|<
name|String
argument_list|>
name|event
parameter_list|)
block|{
name|editAssignee
argument_list|(
name|event
operator|.
name|getSelectedItem
argument_list|()
argument_list|)
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
name|editAssigneeIcon
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
name|onOpenForm
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
DECL|method|set (ChangeInfo info)
name|void
name|set
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|info
operator|.
name|legacyId
argument_list|()
expr_stmt|;
name|this
operator|.
name|canEdit
operator|=
name|info
operator|.
name|hasActions
argument_list|()
operator|&&
name|info
operator|.
name|actions
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"assignee"
argument_list|)
expr_stmt|;
name|setAssignee
argument_list|(
name|info
operator|.
name|assignee
argument_list|()
argument_list|)
expr_stmt|;
name|assigneeSuggestOracle
operator|.
name|setChange
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|editAssigneeIcon
operator|.
name|setVisible
argument_list|(
name|canEdit
argument_list|)
expr_stmt|;
block|}
DECL|method|onOpenForm ()
name|void
name|onOpenForm
parameter_list|()
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
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|editAssigneeIcon
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|onCloseForm ()
name|void
name|onCloseForm
parameter_list|()
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|form
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|editAssigneeIcon
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"assign"
argument_list|)
DECL|method|onEditAssignee (@uppressWarningsR) ClickEvent e)
name|void
name|onEditAssignee
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
name|canEdit
condition|)
block|{
name|editAssignee
argument_list|(
name|suggestBox
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|onCloseForm
argument_list|()
expr_stmt|;
block|}
DECL|method|editAssignee (final String assignee)
specifier|private
name|void
name|editAssignee
parameter_list|(
specifier|final
name|String
name|assignee
parameter_list|)
block|{
if|if
condition|(
name|assignee
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ChangeApi
operator|.
name|deleteAssignee
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|AccountInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|AccountInfo
name|result
parameter_list|)
block|{
name|onCloseForm
argument_list|()
expr_stmt|;
name|setAssignee
argument_list|(
literal|null
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
name|err
parameter_list|)
block|{
if|if
condition|(
name|isSigninFailure
argument_list|(
name|err
argument_list|)
condition|)
block|{
operator|new
name|NotSignedInDialog
argument_list|()
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|error
operator|.
name|setInnerText
argument_list|(
name|err
operator|instanceof
name|StatusCodeException
condition|?
operator|(
operator|(
name|StatusCodeException
operator|)
name|err
operator|)
operator|.
name|getEncodedResponse
argument_list|()
else|:
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ChangeApi
operator|.
name|setAssignee
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|assignee
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|AccountInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|AccountInfo
name|result
parameter_list|)
block|{
name|onCloseForm
argument_list|()
expr_stmt|;
name|setAssignee
argument_list|(
name|result
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
name|err
parameter_list|)
block|{
if|if
condition|(
name|isSigninFailure
argument_list|(
name|err
argument_list|)
condition|)
block|{
operator|new
name|NotSignedInDialog
argument_list|()
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|error
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|error
operator|.
name|setInnerText
argument_list|(
name|err
operator|instanceof
name|StatusCodeException
condition|?
operator|(
operator|(
name|StatusCodeException
operator|)
name|err
operator|)
operator|.
name|getEncodedResponse
argument_list|()
else|:
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setAssignee (AccountInfo assignee)
specifier|private
name|void
name|setAssignee
parameter_list|(
name|AccountInfo
name|assignee
parameter_list|)
block|{
name|assigneeLink
operator|.
name|setText
argument_list|(
name|assignee
operator|!=
literal|null
condition|?
name|assignee
operator|.
name|name
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|assigneeLink
operator|.
name|setTargetHistoryToken
argument_list|(
name|assignee
operator|!=
literal|null
condition|?
name|PageLinks
operator|.
name|toAssigneeQuery
argument_list|(
name|assignee
operator|.
name|name
argument_list|()
operator|!=
literal|null
condition|?
name|assignee
operator|.
name|name
argument_list|()
else|:
name|assignee
operator|.
name|email
argument_list|()
operator|!=
literal|null
condition|?
name|assignee
operator|.
name|email
argument_list|()
else|:
name|String
operator|.
name|valueOf
argument_list|(
name|assignee
operator|.
name|_accountId
argument_list|()
argument_list|)
argument_list|)
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

