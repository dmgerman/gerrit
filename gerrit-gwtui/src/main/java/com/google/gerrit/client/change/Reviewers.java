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
name|ConfirmationCallback
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
name|ConfirmationDialog
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
name|info
operator|.
name|ChangeInfo
operator|.
name|ApprovalInfo
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
operator|.
name|LabelInfo
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
name|client
operator|.
name|rpc
operator|.
name|NativeString
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
name|Natives
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|JsArray
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
name|safehtml
operator|.
name|client
operator|.
name|SafeHtml
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/** Add reviewers. */
end_comment

begin_class
DECL|class|Reviewers
specifier|public
class|class
name|Reviewers
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
name|Reviewers
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
DECL|field|reviewersText
annotation|@
name|UiField
name|Element
name|reviewersText
decl_stmt|;
DECL|field|openForm
annotation|@
name|UiField
name|Button
name|openForm
decl_stmt|;
DECL|field|addMe
annotation|@
name|UiField
name|Button
name|addMe
decl_stmt|;
DECL|field|form
annotation|@
name|UiField
name|Element
name|form
decl_stmt|;
DECL|field|error
annotation|@
name|UiField
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
DECL|field|style
specifier|private
name|ChangeScreen
operator|.
name|Style
name|style
decl_stmt|;
DECL|field|ccText
specifier|private
name|Element
name|ccText
decl_stmt|;
DECL|field|reviewerSuggestOracle
specifier|private
name|ReviewerSuggestOracle
name|reviewerSuggestOracle
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|method|Reviewers ()
name|Reviewers
parameter_list|()
block|{
name|reviewerSuggestOracle
operator|=
operator|new
name|ReviewerSuggestOracle
argument_list|()
expr_stmt|;
name|suggestBox
operator|=
operator|new
name|RemoteSuggestBox
argument_list|(
name|reviewerSuggestOracle
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
name|approvalTableAddReviewerHint
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
name|Reviewers
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
name|addReviewer
argument_list|(
name|event
operator|.
name|getSelectedItem
argument_list|()
argument_list|,
literal|false
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
block|}
DECL|method|init (ChangeScreen.Style style, Element ccText)
name|void
name|init
parameter_list|(
name|ChangeScreen
operator|.
name|Style
name|style
parameter_list|,
name|Element
name|ccText
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
name|this
operator|.
name|ccText
operator|=
name|ccText
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
name|display
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|reviewerSuggestOracle
operator|.
name|setChange
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|openForm
operator|.
name|setVisible
argument_list|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"openForm"
argument_list|)
DECL|method|onOpenForm (@uppressWarningsR) ClickEvent e)
name|void
name|onOpenForm
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
name|onOpenForm
argument_list|()
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
name|openForm
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
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"add"
argument_list|)
DECL|method|onAdd (@uppressWarningsR) ClickEvent e)
name|void
name|onAdd
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
name|addReviewer
argument_list|(
name|suggestBox
operator|.
name|getText
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"addMe"
argument_list|)
DECL|method|onAddMe (@uppressWarningsR) ClickEvent e)
name|void
name|onAddMe
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
name|String
name|accountId
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|_accountId
argument_list|()
argument_list|)
decl_stmt|;
name|addReviewer
argument_list|(
name|accountId
argument_list|,
literal|false
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
name|openForm
operator|.
name|setVisible
argument_list|(
literal|true
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
name|suggestBox
operator|.
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|addReviewer (final String reviewer, boolean confirmed)
specifier|private
name|void
name|addReviewer
parameter_list|(
specifier|final
name|String
name|reviewer
parameter_list|,
name|boolean
name|confirmed
parameter_list|)
block|{
if|if
condition|(
name|reviewer
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|ChangeApi
operator|.
name|reviewers
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|post
argument_list|(
name|PostInput
operator|.
name|create
argument_list|(
name|reviewer
argument_list|,
name|confirmed
argument_list|)
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|PostResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|PostResult
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|confirm
argument_list|()
condition|)
block|{
name|askForConfirmation
argument_list|(
name|result
operator|.
name|error
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|.
name|error
argument_list|()
operator|!=
literal|null
condition|)
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
name|result
operator|.
name|error
argument_list|()
argument_list|)
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
literal|false
argument_list|)
expr_stmt|;
name|error
operator|.
name|setInnerText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|suggestBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|reviewers
argument_list|()
operator|!=
literal|null
operator|&&
name|result
operator|.
name|reviewers
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|updateReviewerList
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|askForConfirmation
parameter_list|(
name|String
name|text
parameter_list|)
block|{
operator|new
name|ConfirmationDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|approvalTableAddManyReviewersConfirmationDialogTitle
argument_list|()
argument_list|,
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|text
argument_list|)
argument_list|,
operator|new
name|ConfirmationCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onOk
parameter_list|()
block|{
name|addReviewer
argument_list|(
name|reviewer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|center
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
DECL|method|updateReviewerList ()
specifier|private
name|void
name|updateReviewerList
parameter_list|()
block|{
name|ChangeApi
operator|.
name|detail
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ChangeInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeInfo
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|display (ChangeInfo info)
specifier|private
name|void
name|display
parameter_list|(
name|ChangeInfo
name|info
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|AccountInfo
argument_list|>
name|r
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Integer
argument_list|,
name|AccountInfo
argument_list|>
name|cc
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|LabelInfo
name|label
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|info
operator|.
name|allLabels
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|label
operator|.
name|all
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ApprovalInfo
name|ai
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|label
operator|.
name|all
argument_list|()
argument_list|)
control|)
block|{
operator|(
name|ai
operator|.
name|value
argument_list|()
operator|!=
literal|0
condition|?
name|r
else|:
name|cc
operator|)
operator|.
name|put
argument_list|(
name|ai
operator|.
name|_accountId
argument_list|()
argument_list|,
name|ai
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Integer
name|i
range|:
name|r
operator|.
name|keySet
argument_list|()
control|)
block|{
name|cc
operator|.
name|remove
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|cc
operator|.
name|remove
argument_list|(
name|info
operator|.
name|owner
argument_list|()
operator|.
name|_accountId
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Integer
argument_list|>
name|removable
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|removableReviewers
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AccountInfo
name|a
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|info
operator|.
name|removableReviewers
argument_list|()
argument_list|)
control|)
block|{
name|removable
operator|.
name|add
argument_list|(
name|a
operator|.
name|_accountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|Integer
argument_list|,
name|VotableInfo
argument_list|>
name|votable
init|=
name|votable
argument_list|(
name|info
argument_list|)
decl_stmt|;
name|SafeHtml
name|rHtml
init|=
name|Labels
operator|.
name|formatUserList
argument_list|(
name|style
argument_list|,
name|r
operator|.
name|values
argument_list|()
argument_list|,
name|removable
argument_list|,
name|votable
argument_list|)
decl_stmt|;
name|SafeHtml
name|ccHtml
init|=
name|Labels
operator|.
name|formatUserList
argument_list|(
name|style
argument_list|,
name|cc
operator|.
name|values
argument_list|()
argument_list|,
name|removable
argument_list|,
name|votable
argument_list|)
decl_stmt|;
name|reviewersText
operator|.
name|setInnerSafeHtml
argument_list|(
name|rHtml
argument_list|)
expr_stmt|;
name|ccText
operator|.
name|setInnerSafeHtml
argument_list|(
name|ccHtml
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|int
name|currentUser
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|_accountId
argument_list|()
decl_stmt|;
name|boolean
name|showAddMeButton
init|=
name|info
operator|.
name|owner
argument_list|()
operator|.
name|_accountId
argument_list|()
operator|!=
name|currentUser
operator|&&
operator|!
name|cc
operator|.
name|containsKey
argument_list|(
name|currentUser
argument_list|)
operator|&&
operator|!
name|r
operator|.
name|containsKey
argument_list|(
name|currentUser
argument_list|)
decl_stmt|;
name|addMe
operator|.
name|setVisible
argument_list|(
name|showAddMeButton
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|votable (ChangeInfo change)
specifier|private
specifier|static
name|Map
argument_list|<
name|Integer
argument_list|,
name|VotableInfo
argument_list|>
name|votable
parameter_list|(
name|ChangeInfo
name|change
parameter_list|)
block|{
name|Map
argument_list|<
name|Integer
argument_list|,
name|VotableInfo
argument_list|>
name|d
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|change
operator|.
name|labels
argument_list|()
control|)
block|{
name|LabelInfo
name|label
init|=
name|change
operator|.
name|label
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|label
operator|.
name|all
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ApprovalInfo
name|ai
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|label
operator|.
name|all
argument_list|()
argument_list|)
control|)
block|{
name|int
name|id
init|=
name|ai
operator|.
name|_accountId
argument_list|()
decl_stmt|;
name|VotableInfo
name|ad
init|=
name|d
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|ad
operator|==
literal|null
condition|)
block|{
name|ad
operator|=
operator|new
name|VotableInfo
argument_list|()
expr_stmt|;
name|d
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|ad
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ai
operator|.
name|hasValue
argument_list|()
condition|)
block|{
name|ad
operator|.
name|votable
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|d
return|;
block|}
DECL|class|PostInput
specifier|public
specifier|static
class|class
name|PostInput
extends|extends
name|JavaScriptObject
block|{
DECL|method|create (String reviewer, boolean confirmed)
specifier|public
specifier|static
name|PostInput
name|create
parameter_list|(
name|String
name|reviewer
parameter_list|,
name|boolean
name|confirmed
parameter_list|)
block|{
name|PostInput
name|input
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|input
operator|.
name|init
argument_list|(
name|reviewer
argument_list|,
name|confirmed
argument_list|)
expr_stmt|;
return|return
name|input
return|;
block|}
DECL|method|init (String reviewer, boolean confirmed)
specifier|private
specifier|native
name|void
name|init
parameter_list|(
name|String
name|reviewer
parameter_list|,
name|boolean
name|confirmed
parameter_list|)
comment|/*-{       this.reviewer = reviewer;       if (confirmed) {         this.confirmed = true;       }     }-*/
function_decl|;
DECL|method|PostInput ()
specifier|protected
name|PostInput
parameter_list|()
block|{     }
block|}
DECL|class|ReviewerInfo
specifier|public
specifier|static
class|class
name|ReviewerInfo
extends|extends
name|AccountInfo
block|{
DECL|method|approvals ()
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|approvals
parameter_list|()
block|{
return|return
name|Natives
operator|.
name|keys
argument_list|(
name|_approvals
argument_list|()
argument_list|)
return|;
block|}
DECL|method|approval (String l)
specifier|final
specifier|native
name|String
name|approval
parameter_list|(
name|String
name|l
parameter_list|)
comment|/*-{ return this.approvals[l]; }-*/
function_decl|;
DECL|method|_approvals ()
specifier|private
specifier|final
specifier|native
name|NativeMap
argument_list|<
name|NativeString
argument_list|>
name|_approvals
parameter_list|()
comment|/*-{ return this.approvals; }-*/
function_decl|;
DECL|method|ReviewerInfo ()
specifier|protected
name|ReviewerInfo
parameter_list|()
block|{     }
block|}
DECL|class|PostResult
specifier|public
specifier|static
class|class
name|PostResult
extends|extends
name|JavaScriptObject
block|{
DECL|method|reviewers ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|ReviewerInfo
argument_list|>
name|reviewers
parameter_list|()
comment|/*-{ return this.reviewers; }-*/
function_decl|;
DECL|method|confirm ()
specifier|public
specifier|final
specifier|native
name|boolean
name|confirm
parameter_list|()
comment|/*-{ return this.confirm || false; }-*/
function_decl|;
DECL|method|error ()
specifier|public
specifier|final
specifier|native
name|String
name|error
parameter_list|()
comment|/*-{ return this.error; }-*/
function_decl|;
DECL|method|PostResult ()
specifier|protected
name|PostResult
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

