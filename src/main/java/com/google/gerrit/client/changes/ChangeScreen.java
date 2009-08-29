begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|FormatUtil
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
name|Link
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
name|data
operator|.
name|AccountInfoCache
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
name|client
operator|.
name|data
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
name|data
operator|.
name|GitwebLink
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
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|ChangeMessage
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
name|ScreenLoadCallback
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
name|ComplexDisclosurePanel
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
name|ExpandAllCommand
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
name|LinkMenuBar
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
name|NeedsSignInKeyCommand
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
name|RefreshListener
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
name|Screen
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
name|i18n
operator|.
name|client
operator|.
name|LocaleInfo
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
name|InlineLabel
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
name|Panel
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
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
DECL|class|ChangeScreen
specifier|public
class|class
name|ChangeScreen
extends|extends
name|Screen
block|{
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|starChange
specifier|private
name|Image
name|starChange
decl_stmt|;
DECL|field|starred
specifier|private
name|boolean
name|starred
decl_stmt|;
DECL|field|currentPatchSet
specifier|private
name|PatchSet
operator|.
name|Id
name|currentPatchSet
decl_stmt|;
DECL|field|descriptionBlock
specifier|private
name|ChangeDescriptionBlock
name|descriptionBlock
decl_stmt|;
DECL|field|dependenciesPanel
specifier|private
name|DisclosurePanel
name|dependenciesPanel
decl_stmt|;
DECL|field|dependencies
specifier|private
name|ChangeTable
name|dependencies
decl_stmt|;
DECL|field|dependsOn
specifier|private
name|ChangeTable
operator|.
name|Section
name|dependsOn
decl_stmt|;
DECL|field|neededBy
specifier|private
name|ChangeTable
operator|.
name|Section
name|neededBy
decl_stmt|;
DECL|field|approvalsPanel
specifier|private
name|DisclosurePanel
name|approvalsPanel
decl_stmt|;
DECL|field|approvals
specifier|private
name|ApprovalTable
name|approvals
decl_stmt|;
DECL|field|patchSetPanels
specifier|private
name|FlowPanel
name|patchSetPanels
decl_stmt|;
DECL|field|messagesPanel
specifier|private
name|DisclosurePanel
name|messagesPanel
decl_stmt|;
DECL|field|messagesContent
specifier|private
name|Panel
name|messagesContent
decl_stmt|;
DECL|field|keysNavigation
specifier|private
name|KeyCommandSet
name|keysNavigation
decl_stmt|;
DECL|field|keysAction
specifier|private
name|KeyCommandSet
name|keysAction
decl_stmt|;
DECL|field|regNavigation
specifier|private
name|HandlerRegistration
name|regNavigation
decl_stmt|;
DECL|field|regAction
specifier|private
name|HandlerRegistration
name|regAction
decl_stmt|;
DECL|method|ChangeScreen (final Change.Id toShow)
specifier|public
name|ChangeScreen
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|toShow
parameter_list|)
block|{
name|changeId
operator|=
name|toShow
expr_stmt|;
block|}
DECL|method|ChangeScreen (final ChangeInfo c)
specifier|public
name|ChangeScreen
parameter_list|(
specifier|final
name|ChangeInfo
name|c
parameter_list|)
block|{
name|this
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSignOut ()
specifier|public
name|void
name|onSignOut
parameter_list|()
block|{
name|super
operator|.
name|onSignOut
argument_list|()
expr_stmt|;
if|if
condition|(
name|starChange
operator|!=
literal|null
condition|)
block|{
name|starChange
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|refresh
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
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
if|if
condition|(
name|regAction
operator|!=
literal|null
condition|)
block|{
name|regAction
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
name|regAction
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
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
name|regAction
operator|=
name|GlobalKey
operator|.
name|add
argument_list|(
name|this
argument_list|,
name|keysAction
argument_list|)
expr_stmt|;
block|}
DECL|method|refresh ()
specifier|public
name|void
name|refresh
parameter_list|()
block|{
name|Util
operator|.
name|DETAIL_SVC
operator|.
name|changeDetail
argument_list|(
name|changeId
argument_list|,
operator|new
name|ScreenLoadCallback
argument_list|<
name|ChangeDetail
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
specifier|final
name|ChangeDetail
name|r
parameter_list|)
block|{
if|if
condition|(
name|starChange
operator|!=
literal|null
condition|)
block|{
name|setStarred
argument_list|(
name|r
operator|.
name|isStarred
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|display
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|setStarred (final boolean s)
specifier|private
name|void
name|setStarred
parameter_list|(
specifier|final
name|boolean
name|s
parameter_list|)
block|{
if|if
condition|(
name|s
condition|)
block|{
name|Gerrit
operator|.
name|ICONS
operator|.
name|starFilled
argument_list|()
operator|.
name|applyTo
argument_list|(
name|starChange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|ICONS
operator|.
name|starOpen
argument_list|()
operator|.
name|applyTo
argument_list|(
name|starChange
argument_list|)
expr_stmt|;
block|}
name|starred
operator|=
name|s
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|addStyleName
argument_list|(
literal|"gerrit-ChangeScreen"
argument_list|)
expr_stmt|;
name|keysNavigation
operator|=
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
expr_stmt|;
name|keysAction
operator|=
operator|new
name|KeyCommandSet
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|sectionActions
argument_list|()
argument_list|)
expr_stmt|;
name|keysNavigation
operator|.
name|add
argument_list|(
operator|new
name|DashboardKeyCommand
argument_list|(
literal|0
argument_list|,
literal|'u'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|upToDashboard
argument_list|()
argument_list|)
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
name|keysAction
operator|.
name|add
argument_list|(
operator|new
name|StarKeyCommand
argument_list|(
literal|0
argument_list|,
literal|'s'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeTableStar
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|keysAction
operator|.
name|add
argument_list|(
operator|new
name|PublishCommentsKeyCommand
argument_list|(
literal|0
argument_list|,
literal|'r'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|keyPublishComments
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|starChange
operator|=
name|Gerrit
operator|.
name|ICONS
operator|.
name|starOpen
argument_list|()
operator|.
name|createImage
argument_list|()
expr_stmt|;
name|starChange
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-ChangeScreen-StarIcon"
argument_list|)
expr_stmt|;
name|starChange
operator|.
name|setVisible
argument_list|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
argument_list|)
expr_stmt|;
name|starChange
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
name|toggleStar
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|insertTitleWidget
argument_list|(
name|starChange
argument_list|)
expr_stmt|;
block|}
name|descriptionBlock
operator|=
operator|new
name|ChangeDescriptionBlock
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|descriptionBlock
argument_list|)
expr_stmt|;
name|dependencies
operator|=
operator|new
name|ChangeTable
argument_list|()
expr_stmt|;
name|dependsOn
operator|=
operator|new
name|ChangeTable
operator|.
name|Section
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeScreenDependsOn
argument_list|()
argument_list|)
expr_stmt|;
name|neededBy
operator|=
operator|new
name|ChangeTable
operator|.
name|Section
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeScreenNeededBy
argument_list|()
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|addSection
argument_list|(
name|dependsOn
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|addSection
argument_list|(
name|neededBy
argument_list|)
expr_stmt|;
name|dependenciesPanel
operator|=
operator|new
name|DisclosurePanel
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeScreenDependencies
argument_list|()
argument_list|)
expr_stmt|;
name|dependenciesPanel
operator|.
name|setContent
argument_list|(
name|dependencies
argument_list|)
expr_stmt|;
name|dependenciesPanel
operator|.
name|setWidth
argument_list|(
literal|"95%"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|dependenciesPanel
argument_list|)
expr_stmt|;
name|approvals
operator|=
operator|new
name|ApprovalTable
argument_list|()
expr_stmt|;
name|approvalsPanel
operator|=
operator|new
name|DisclosurePanel
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeScreenApprovals
argument_list|()
argument_list|)
expr_stmt|;
name|approvalsPanel
operator|.
name|setContent
argument_list|(
name|wrap
argument_list|(
name|approvals
argument_list|)
argument_list|)
expr_stmt|;
name|dependenciesPanel
operator|.
name|setWidth
argument_list|(
literal|"95%"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|approvalsPanel
argument_list|)
expr_stmt|;
name|patchSetPanels
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|patchSetPanels
argument_list|)
expr_stmt|;
name|messagesContent
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|messagesContent
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-ChangeMessages"
argument_list|)
expr_stmt|;
name|messagesPanel
operator|=
operator|new
name|DisclosurePanel
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeScreenMessages
argument_list|()
argument_list|)
expr_stmt|;
name|messagesPanel
operator|.
name|setContent
argument_list|(
name|messagesContent
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|messagesPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|displayTitle (final String subject)
specifier|private
name|void
name|displayTitle
parameter_list|(
specifier|final
name|String
name|subject
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|titleBuf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|LocaleInfo
operator|.
name|getCurrentLocale
argument_list|()
operator|.
name|isRTL
argument_list|()
condition|)
block|{
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|titleBuf
operator|.
name|append
argument_list|(
name|subject
argument_list|)
expr_stmt|;
name|titleBuf
operator|.
name|append
argument_list|(
literal|" :"
argument_list|)
expr_stmt|;
block|}
name|titleBuf
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|changeScreenTitleId
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|titleBuf
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|changeScreenTitleId
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|titleBuf
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|titleBuf
operator|.
name|append
argument_list|(
name|subject
argument_list|)
expr_stmt|;
block|}
block|}
name|setPageTitle
argument_list|(
name|titleBuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final ChangeDetail detail)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|displayTitle
argument_list|(
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|dependencies
operator|.
name|setAccountInfoCache
argument_list|(
name|detail
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|approvals
operator|.
name|setAccountInfoCache
argument_list|(
name|detail
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|descriptionBlock
operator|.
name|display
argument_list|(
name|detail
operator|.
name|getChange
argument_list|()
argument_list|,
name|detail
operator|.
name|getCurrentPatchSetDetail
argument_list|()
operator|.
name|getInfo
argument_list|()
argument_list|,
name|detail
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|dependsOn
operator|.
name|display
argument_list|(
name|detail
operator|.
name|getDependsOn
argument_list|()
argument_list|)
expr_stmt|;
name|neededBy
operator|.
name|display
argument_list|(
name|detail
operator|.
name|getNeededBy
argument_list|()
argument_list|)
expr_stmt|;
name|approvals
operator|.
name|display
argument_list|(
name|detail
operator|.
name|getChange
argument_list|()
argument_list|,
name|detail
operator|.
name|getMissingApprovals
argument_list|()
argument_list|,
name|detail
operator|.
name|getApprovals
argument_list|()
argument_list|)
expr_stmt|;
name|addPatchSets
argument_list|(
name|detail
argument_list|)
expr_stmt|;
name|addMessages
argument_list|(
name|detail
argument_list|)
expr_stmt|;
comment|// If any dependency change is still open, show our dependency list.
comment|//
name|boolean
name|depsOpen
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isClosed
argument_list|()
operator|&&
name|detail
operator|.
name|getDependsOn
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|ChangeInfo
name|ci
range|:
name|detail
operator|.
name|getDependsOn
argument_list|()
control|)
block|{
if|if
condition|(
name|ci
operator|.
name|getStatus
argument_list|()
operator|!=
name|Change
operator|.
name|Status
operator|.
name|MERGED
condition|)
block|{
name|depsOpen
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
name|dependenciesPanel
operator|.
name|setOpen
argument_list|(
name|depsOpen
argument_list|)
expr_stmt|;
name|approvalsPanel
operator|.
name|setOpen
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|addPatchSets (final ChangeDetail detail)
specifier|private
name|void
name|addPatchSets
parameter_list|(
specifier|final
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|patchSetPanels
operator|.
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
specifier|final
name|GitwebLink
name|gw
init|=
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getGitwebLink
argument_list|()
decl_stmt|;
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
specifier|final
name|ComplexDisclosurePanel
name|panel
init|=
operator|new
name|ComplexDisclosurePanel
argument_list|(
name|Util
operator|.
name|M
operator|.
name|patchSetHeader
argument_list|(
name|ps
operator|.
name|getPatchSetId
argument_list|()
argument_list|)
argument_list|,
name|ps
operator|==
name|currps
argument_list|)
decl_stmt|;
specifier|final
name|PatchSetPanel
name|psp
init|=
operator|new
name|PatchSetPanel
argument_list|(
name|detail
argument_list|,
name|ps
argument_list|)
decl_stmt|;
name|panel
operator|.
name|setContent
argument_list|(
name|psp
argument_list|)
expr_stmt|;
specifier|final
name|InlineLabel
name|revtxt
init|=
operator|new
name|InlineLabel
argument_list|(
name|ps
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|" "
argument_list|)
decl_stmt|;
name|revtxt
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-PatchSetRevision"
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getHeader
argument_list|()
operator|.
name|add
argument_list|(
name|revtxt
argument_list|)
expr_stmt|;
if|if
condition|(
name|gw
operator|!=
literal|null
condition|)
block|{
specifier|final
name|Anchor
name|revlink
init|=
operator|new
name|Anchor
argument_list|(
literal|"(gitweb)"
argument_list|,
literal|false
argument_list|,
name|gw
operator|.
name|toRevision
argument_list|(
name|detail
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|ps
argument_list|)
argument_list|)
decl_stmt|;
name|revlink
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-PatchSetLink"
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getHeader
argument_list|()
operator|.
name|add
argument_list|(
name|revlink
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ps
operator|==
name|currps
condition|)
block|{
name|psp
operator|.
name|ensureLoaded
argument_list|(
name|detail
operator|.
name|getCurrentPatchSetDetail
argument_list|()
argument_list|)
expr_stmt|;
name|psp
operator|.
name|addRefreshListener
argument_list|(
operator|new
name|RefreshListener
argument_list|()
block|{
specifier|public
name|void
name|onSuggestRefresh
parameter_list|()
block|{
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|panel
operator|.
name|addOpenHandler
argument_list|(
name|psp
argument_list|)
expr_stmt|;
block|}
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|patchSetPanels
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
block|}
name|currentPatchSet
operator|=
name|currps
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
DECL|method|addMessages (final ChangeDetail detail)
specifier|private
name|void
name|addMessages
parameter_list|(
specifier|final
name|ChangeDetail
name|detail
parameter_list|)
block|{
name|messagesContent
operator|.
name|clear
argument_list|()
expr_stmt|;
specifier|final
name|AccountInfoCache
name|accts
init|=
name|detail
operator|.
name|getAccounts
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|msgList
init|=
name|detail
operator|.
name|getMessages
argument_list|()
decl_stmt|;
if|if
condition|(
name|msgList
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|messagesContent
operator|.
name|add
argument_list|(
name|messagesMenuBar
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|long
name|AGE
init|=
literal|7
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
specifier|final
name|Timestamp
name|aged
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|AGE
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
name|msgList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|ChangeMessage
name|msg
init|=
name|msgList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|MessagePanel
name|mp
init|=
operator|new
name|MessagePanel
argument_list|(
name|msg
argument_list|)
decl_stmt|;
specifier|final
name|String
name|panelHeader
decl_stmt|;
specifier|final
name|ComplexDisclosurePanel
name|panel
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getAuthor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|panelHeader
operator|=
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|accts
operator|.
name|get
argument_list|(
name|msg
operator|.
name|getAuthor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|panelHeader
operator|=
name|Util
operator|.
name|C
operator|.
name|messageNoAuthor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|==
name|msgList
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|mp
operator|.
name|isRecent
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// TODO Instead of opening messages by strict age, do it by "unread"?
name|mp
operator|.
name|isRecent
operator|=
name|msg
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|after
argument_list|(
name|aged
argument_list|)
expr_stmt|;
block|}
name|panel
operator|=
operator|new
name|ComplexDisclosurePanel
argument_list|(
name|panelHeader
argument_list|,
name|mp
operator|.
name|isRecent
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getHeader
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|InlineLabel
argument_list|(
name|Util
operator|.
name|M
operator|.
name|messageWrittenOn
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|msg
operator|.
name|getWrittenOn
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setContent
argument_list|(
name|mp
argument_list|)
expr_stmt|;
name|messagesContent
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|msgList
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|messagesContent
operator|.
name|add
argument_list|(
name|messagesMenuBar
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|messagesPanel
operator|.
name|setOpen
argument_list|(
name|msgList
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|messagesPanel
operator|.
name|setVisible
argument_list|(
name|msgList
operator|.
name|size
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|messagesMenuBar ()
specifier|private
name|LinkMenuBar
name|messagesMenuBar
parameter_list|()
block|{
specifier|final
name|Panel
name|c
init|=
name|messagesContent
decl_stmt|;
specifier|final
name|LinkMenuBar
name|m
init|=
operator|new
name|LinkMenuBar
argument_list|()
decl_stmt|;
name|m
operator|.
name|addItem
argument_list|(
name|Util
operator|.
name|C
operator|.
name|messageExpandRecent
argument_list|()
argument_list|,
operator|new
name|ExpandAllCommand
argument_list|(
name|c
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|expand
parameter_list|(
specifier|final
name|ComplexDisclosurePanel
name|w
parameter_list|)
block|{
specifier|final
name|MessagePanel
name|mp
init|=
operator|(
name|MessagePanel
operator|)
name|w
operator|.
name|getContent
argument_list|()
decl_stmt|;
name|w
operator|.
name|setOpen
argument_list|(
name|mp
operator|.
name|isRecent
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|m
operator|.
name|addItem
argument_list|(
name|Util
operator|.
name|C
operator|.
name|messageExpandAll
argument_list|()
argument_list|,
operator|new
name|ExpandAllCommand
argument_list|(
name|c
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|m
operator|.
name|addItem
argument_list|(
name|Util
operator|.
name|C
operator|.
name|messageCollapseAll
argument_list|()
argument_list|,
operator|new
name|ExpandAllCommand
argument_list|(
name|c
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
DECL|method|toggleStar ()
specifier|private
name|void
name|toggleStar
parameter_list|()
block|{
specifier|final
name|boolean
name|prior
init|=
name|starred
decl_stmt|;
name|setStarred
argument_list|(
operator|!
name|prior
argument_list|)
expr_stmt|;
specifier|final
name|ToggleStarRequest
name|req
init|=
operator|new
name|ToggleStarRequest
argument_list|()
decl_stmt|;
name|req
operator|.
name|toggle
argument_list|(
name|changeId
argument_list|,
name|starred
argument_list|)
expr_stmt|;
name|Util
operator|.
name|LIST_SVC
operator|.
name|toggleStars
argument_list|(
name|req
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|VoidResult
name|result
parameter_list|)
block|{       }
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
name|setStarred
argument_list|(
name|prior
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|wrap (final Widget w)
specifier|private
specifier|static
name|FlowPanel
name|wrap
parameter_list|(
specifier|final
name|Widget
name|w
parameter_list|)
block|{
specifier|final
name|FlowPanel
name|p
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|p
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|class|DashboardKeyCommand
specifier|public
class|class
name|DashboardKeyCommand
extends|extends
name|KeyCommand
block|{
DECL|method|DashboardKeyCommand (int mask, char key, String help)
specifier|public
name|DashboardKeyCommand
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
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|Link
operator|.
name|MINE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|Link
operator|.
name|ALL_OPEN
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|StarKeyCommand
specifier|public
class|class
name|StarKeyCommand
extends|extends
name|NeedsSignInKeyCommand
block|{
DECL|method|StarKeyCommand (int mask, char key, String help)
specifier|public
name|StarKeyCommand
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
name|toggleStar
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|PublishCommentsKeyCommand
specifier|public
class|class
name|PublishCommentsKeyCommand
extends|extends
name|NeedsSignInKeyCommand
block|{
DECL|method|PublishCommentsKeyCommand (int mask, char key, String help)
specifier|public
name|PublishCommentsKeyCommand
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
name|Gerrit
operator|.
name|display
argument_list|(
literal|"change,publish,"
operator|+
name|currentPatchSet
operator|.
name|toString
argument_list|()
argument_list|,
operator|new
name|PublishCommentScreen
argument_list|(
name|currentPatchSet
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

