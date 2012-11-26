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
name|ErrorDialog
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
name|ui
operator|.
name|ChangeLink
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
name|CommentLinkProcessor
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
name|CommentedActionDialog
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
name|PreElement
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
name|SimplePanel
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
name|clippy
operator|.
name|client
operator|.
name|CopyableLabel
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
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|AsyncCallback
import|;
end_import

begin_class
DECL|class|CommitMessageBlock
specifier|public
class|class
name|CommitMessageBlock
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
name|CommitMessageBlock
argument_list|>
block|{   }
DECL|field|uiBinder
specifier|private
specifier|static
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
DECL|field|keysAction
specifier|private
name|KeyCommandSet
name|keysAction
decl_stmt|;
annotation|@
name|UiField
DECL|field|starPanel
name|SimplePanel
name|starPanel
decl_stmt|;
annotation|@
name|UiField
DECL|field|permalinkPanel
name|FlowPanel
name|permalinkPanel
decl_stmt|;
annotation|@
name|UiField
DECL|field|commitSummaryPre
name|PreElement
name|commitSummaryPre
decl_stmt|;
annotation|@
name|UiField
DECL|field|commitBodyPre
name|PreElement
name|commitBodyPre
decl_stmt|;
DECL|method|CommitMessageBlock ()
specifier|public
name|CommitMessageBlock
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
block|}
DECL|method|CommitMessageBlock (KeyCommandSet keysAction)
specifier|public
name|CommitMessageBlock
parameter_list|(
name|KeyCommandSet
name|keysAction
parameter_list|)
block|{
name|this
operator|.
name|keysAction
operator|=
name|keysAction
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
DECL|method|display (final String commitMessage)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|String
name|commitMessage
parameter_list|)
block|{
name|display
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
name|commitMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final PatchSet.Id patchSetId, Boolean starred, Boolean canEditCommitMessage, final String commitMessage)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|Boolean
name|starred
parameter_list|,
name|Boolean
name|canEditCommitMessage
parameter_list|,
specifier|final
name|String
name|commitMessage
parameter_list|)
block|{
name|starPanel
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|patchSetId
operator|!=
literal|null
operator|&&
name|starred
operator|!=
literal|null
operator|&&
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|StarredChanges
operator|.
name|Icon
name|star
init|=
name|StarredChanges
operator|.
name|createIcon
argument_list|(
name|changeId
argument_list|,
name|starred
argument_list|)
decl_stmt|;
name|star
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|changeScreenStarIcon
argument_list|()
argument_list|)
expr_stmt|;
name|starPanel
operator|.
name|add
argument_list|(
name|star
argument_list|)
expr_stmt|;
if|if
condition|(
name|keysAction
operator|!=
literal|null
condition|)
block|{
name|keysAction
operator|.
name|add
argument_list|(
name|StarredChanges
operator|.
name|newKeyCommand
argument_list|(
name|star
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|permalinkPanel
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|patchSetId
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|permalinkPanel
operator|.
name|add
argument_list|(
operator|new
name|ChangeLink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changePermalink
argument_list|()
argument_list|,
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
name|permalinkPanel
operator|.
name|add
argument_list|(
operator|new
name|CopyableLabel
argument_list|(
name|ChangeLink
operator|.
name|permalink
argument_list|(
name|changeId
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|canEditCommitMessage
condition|)
block|{
specifier|final
name|Image
name|edit
init|=
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|edit
argument_list|()
argument_list|)
decl_stmt|;
name|edit
operator|.
name|addClickHandler
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
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
operator|new
name|CommentedActionDialog
argument_list|<
name|ChangeDetail
argument_list|>
argument_list|(
name|Util
operator|.
name|C
operator|.
name|titleEditCommitMessage
argument_list|()
argument_list|,
name|Util
operator|.
name|C
operator|.
name|headingEditCommitMessage
argument_list|()
argument_list|,
operator|new
name|ChangeDetailCache
operator|.
name|IgnoreErrorCallback
argument_list|()
block|{}
argument_list|)
block|{
block|{
name|message
operator|.
name|setCharacterWidth
argument_list|(
literal|80
argument_list|)
expr_stmt|;
name|message
operator|.
name|setVisibleLines
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|message
operator|.
name|setText
parameter_list|(
name|commitMessage
parameter_list|)
constructor_decl|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onSend
parameter_list|()
block|{
name|Util
operator|.
name|MANAGE_SVC
operator|.
name|createNewPatchSet
argument_list|(
name|patchSetId
argument_list|,
name|getMessageText
argument_list|()
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|ChangeDetail
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ChangeDetail
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|changeId
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
block|{
name|enableButtons
argument_list|(
literal|true
argument_list|)
expr_stmt|;
operator|new
name|ErrorDialog
argument_list|(
name|caught
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|permalinkPanel
operator|.
name|add
argument_list|(
name|edit
argument_list|)
expr_stmt|;
block|}
block|}
name|String
index|[]
name|splitCommitMessage
init|=
name|commitMessage
operator|.
name|split
argument_list|(
literal|"\n"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|String
name|commitSummary
init|=
name|splitCommitMessage
index|[
literal|0
index|]
decl_stmt|;
name|String
name|commitBody
init|=
literal|""
decl_stmt|;
if|if
condition|(
name|splitCommitMessage
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|commitBody
operator|=
name|splitCommitMessage
index|[
literal|1
index|]
expr_stmt|;
block|}
comment|// Linkify commit summary
name|SafeHtml
name|commitSummaryLinkified
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|commitSummary
argument_list|)
decl_stmt|;
name|commitSummaryLinkified
operator|=
name|commitSummaryLinkified
operator|.
name|linkify
argument_list|()
expr_stmt|;
name|commitSummaryLinkified
operator|=
name|CommentLinkProcessor
operator|.
name|apply
argument_list|(
name|commitSummaryLinkified
argument_list|)
expr_stmt|;
name|commitSummaryPre
operator|.
name|setInnerHTML
argument_list|(
name|commitSummaryLinkified
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Hide commit body if there is no body
if|if
condition|(
name|commitBody
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|commitBodyPre
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
comment|// Linkify commit body
name|SafeHtml
name|commitBodyLinkified
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|commitBody
argument_list|)
decl_stmt|;
name|commitBodyLinkified
operator|=
name|commitBodyLinkified
operator|.
name|linkify
argument_list|()
expr_stmt|;
name|commitBodyLinkified
operator|=
name|CommentLinkProcessor
operator|.
name|apply
argument_list|(
name|commitBodyLinkified
argument_list|)
expr_stmt|;
name|commitBodyLinkified
operator|=
name|commitBodyLinkified
operator|.
name|replaceAll
argument_list|(
literal|"\n\n"
argument_list|,
literal|"<p></p>"
argument_list|)
expr_stmt|;
name|commitBodyLinkified
operator|=
name|commitBodyLinkified
operator|.
name|replaceAll
argument_list|(
literal|"\n"
argument_list|,
literal|"<br />"
argument_list|)
expr_stmt|;
name|commitBodyPre
operator|.
name|setInnerHTML
argument_list|(
name|commitBodyLinkified
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

