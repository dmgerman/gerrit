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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|FormatUtil
operator|.
name|mediumFormat
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
name|AccountLinkPanel
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
name|BranchLink
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
name|ProjectSearchLink
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
name|common
operator|.
name|data
operator|.
name|SubmitTypeRecord
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
name|Branch
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
name|KeyCodes
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
name|dom
operator|.
name|client
operator|.
name|KeyPressHandler
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
name|Grid
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
name|HTMLTable
operator|.
name|CellFormatter
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
name|TextBox
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
name|clippy
operator|.
name|client
operator|.
name|CopyableLabel
import|;
end_import

begin_class
DECL|class|ChangeInfoBlock
specifier|public
class|class
name|ChangeInfoBlock
extends|extends
name|Composite
block|{
DECL|field|R_CHANGE_ID
specifier|private
specifier|static
specifier|final
name|int
name|R_CHANGE_ID
init|=
literal|0
decl_stmt|;
DECL|field|R_OWNER
specifier|private
specifier|static
specifier|final
name|int
name|R_OWNER
init|=
literal|1
decl_stmt|;
DECL|field|R_PROJECT
specifier|private
specifier|static
specifier|final
name|int
name|R_PROJECT
init|=
literal|2
decl_stmt|;
DECL|field|R_BRANCH
specifier|private
specifier|static
specifier|final
name|int
name|R_BRANCH
init|=
literal|3
decl_stmt|;
DECL|field|R_TOPIC
specifier|private
specifier|static
specifier|final
name|int
name|R_TOPIC
init|=
literal|4
decl_stmt|;
DECL|field|R_UPLOADED
specifier|private
specifier|static
specifier|final
name|int
name|R_UPLOADED
init|=
literal|5
decl_stmt|;
DECL|field|R_UPDATED
specifier|private
specifier|static
specifier|final
name|int
name|R_UPDATED
init|=
literal|6
decl_stmt|;
DECL|field|R_SUBMIT_TYPE
specifier|private
specifier|static
specifier|final
name|int
name|R_SUBMIT_TYPE
init|=
literal|7
decl_stmt|;
DECL|field|R_STATUS
specifier|private
specifier|static
specifier|final
name|int
name|R_STATUS
init|=
literal|8
decl_stmt|;
DECL|field|R_MERGE_TEST
specifier|private
specifier|static
specifier|final
name|int
name|R_MERGE_TEST
init|=
literal|9
decl_stmt|;
DECL|field|R_CNT
specifier|private
specifier|static
specifier|final
name|int
name|R_CNT
init|=
literal|10
decl_stmt|;
DECL|field|table
specifier|private
specifier|final
name|Grid
name|table
decl_stmt|;
DECL|method|ChangeInfoBlock ()
specifier|public
name|ChangeInfoBlock
parameter_list|()
block|{
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|testChangeMerge
argument_list|()
condition|)
block|{
name|table
operator|=
operator|new
name|Grid
argument_list|(
name|R_CNT
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|=
operator|new
name|Grid
argument_list|(
name|R_CNT
operator|-
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
name|table
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
name|infoBlock
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|changeInfoBlock
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_CHANGE_ID
argument_list|,
literal|"Change-Id: "
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_OWNER
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockOwner
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_PROJECT
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockProject
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_BRANCH
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockBranch
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_TOPIC
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockTopic
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_UPLOADED
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockUploaded
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_UPDATED
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockUpdated
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_STATUS
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockStatus
argument_list|()
argument_list|)
expr_stmt|;
name|initRow
argument_list|(
name|R_SUBMIT_TYPE
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockSubmitType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|testChangeMerge
argument_list|()
condition|)
block|{
name|initRow
argument_list|(
name|R_MERGE_TEST
argument_list|,
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockCanMerge
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|topmost
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|topmost
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|R_CHANGE_ID
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|changeid
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|R_CNT
operator|-
literal|2
argument_list|,
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|bottomheader
argument_list|()
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
DECL|method|initRow (final int row, final String name)
specifier|private
name|void
name|initRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|header
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final ChangeDetail changeDetail, final AccountInfoCache acc, SubmitTypeRecord submitTypeRecord)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|ChangeDetail
name|changeDetail
parameter_list|,
specifier|final
name|AccountInfoCache
name|acc
parameter_list|,
name|SubmitTypeRecord
name|submitTypeRecord
parameter_list|)
block|{
specifier|final
name|Change
name|chg
init|=
name|changeDetail
operator|.
name|getChange
argument_list|()
decl_stmt|;
specifier|final
name|Branch
operator|.
name|NameKey
name|dst
init|=
name|chg
operator|.
name|getDest
argument_list|()
decl_stmt|;
name|CopyableLabel
name|changeIdLabel
init|=
operator|new
name|CopyableLabel
argument_list|(
literal|"Change-Id: "
operator|+
name|chg
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|changeIdLabel
operator|.
name|setPreviewText
argument_list|(
name|chg
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|R_CHANGE_ID
argument_list|,
literal|1
argument_list|,
name|changeIdLabel
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|R_OWNER
argument_list|,
literal|1
argument_list|,
name|AccountLinkPanel
operator|.
name|link
argument_list|(
name|acc
argument_list|,
name|chg
operator|.
name|getOwner
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
operator|new
name|ProjectSearchLink
argument_list|(
name|chg
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
operator|new
name|InlineHyperlink
argument_list|(
name|chg
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|PageLinks
operator|.
name|toProject
argument_list|(
name|chg
operator|.
name|getProject
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|R_PROJECT
argument_list|,
literal|1
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|R_BRANCH
argument_list|,
literal|1
argument_list|,
operator|new
name|BranchLink
argument_list|(
name|dst
operator|.
name|getShortName
argument_list|()
argument_list|,
name|chg
operator|.
name|getProject
argument_list|()
argument_list|,
name|chg
operator|.
name|getStatus
argument_list|()
argument_list|,
name|dst
operator|.
name|get
argument_list|()
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|R_TOPIC
argument_list|,
literal|1
argument_list|,
name|topic
argument_list|(
name|changeDetail
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|R_UPLOADED
argument_list|,
literal|1
argument_list|,
name|mediumFormat
argument_list|(
name|chg
operator|.
name|getCreatedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|R_UPDATED
argument_list|,
literal|1
argument_list|,
name|mediumFormat
argument_list|(
name|chg
operator|.
name|getLastUpdatedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|R_STATUS
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|toLongString
argument_list|(
name|chg
operator|.
name|getStatus
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|submitType
decl_stmt|;
if|if
condition|(
name|submitTypeRecord
operator|.
name|status
operator|==
name|SubmitTypeRecord
operator|.
name|Status
operator|.
name|OK
condition|)
block|{
name|submitType
operator|=
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
operator|.
name|Util
operator|.
name|toLongString
argument_list|(
name|submitTypeRecord
operator|.
name|type
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitType
operator|=
name|submitTypeRecord
operator|.
name|status
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
name|table
operator|.
name|setText
argument_list|(
name|R_SUBMIT_TYPE
argument_list|,
literal|1
argument_list|,
name|submitType
argument_list|)
expr_stmt|;
specifier|final
name|Change
operator|.
name|Status
name|status
init|=
name|chg
operator|.
name|getStatus
argument_list|()
decl_stmt|;
if|if
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|testChangeMerge
argument_list|()
condition|)
block|{
if|if
condition|(
name|status
operator|.
name|equals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|NEW
argument_list|)
operator|||
name|status
operator|.
name|equals
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|DRAFT
argument_list|)
condition|)
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|R_MERGE_TEST
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|R_MERGE_TEST
argument_list|,
literal|1
argument_list|,
name|chg
operator|.
name|isMergeable
argument_list|()
condition|?
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockCanMergeYes
argument_list|()
else|:
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockCanMergeNo
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|R_MERGE_TEST
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|status
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
name|R_STATUS
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|closedstate
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|R_SUBMIT_TYPE
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|removeStyleName
argument_list|(
name|R_STATUS
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|closedstate
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|R_SUBMIT_TYPE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|topic (final ChangeDetail changeDetail)
specifier|public
name|Widget
name|topic
parameter_list|(
specifier|final
name|ChangeDetail
name|changeDetail
parameter_list|)
block|{
specifier|final
name|Change
name|chg
init|=
name|changeDetail
operator|.
name|getChange
argument_list|()
decl_stmt|;
specifier|final
name|Branch
operator|.
name|NameKey
name|dst
init|=
name|chg
operator|.
name|getDest
argument_list|()
decl_stmt|;
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|fp
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|changeInfoTopicPanel
argument_list|()
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
operator|new
name|BranchLink
argument_list|(
name|chg
operator|.
name|getTopic
argument_list|()
argument_list|,
name|chg
operator|.
name|getProject
argument_list|()
argument_list|,
name|chg
operator|.
name|getStatus
argument_list|()
argument_list|,
name|dst
operator|.
name|get
argument_list|()
argument_list|,
name|chg
operator|.
name|getTopic
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|changeDetail
operator|.
name|canEditTopicName
argument_list|()
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
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|link
argument_list|()
argument_list|)
expr_stmt|;
name|edit
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|changeInfoBlockTopicAlterTopicToolTip
argument_list|()
argument_list|)
expr_stmt|;
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
name|AlterTopicDialog
argument_list|(
name|chg
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|edit
argument_list|)
expr_stmt|;
block|}
return|return
name|fp
return|;
block|}
DECL|class|AlterTopicDialog
specifier|private
class|class
name|AlterTopicDialog
extends|extends
name|CommentedActionDialog
argument_list|<
name|ChangeDetail
argument_list|>
implements|implements
name|KeyPressHandler
block|{
DECL|field|newTopic
name|TextBox
name|newTopic
decl_stmt|;
DECL|field|change
name|Change
name|change
decl_stmt|;
DECL|method|AlterTopicDialog (Change chg)
name|AlterTopicDialog
parameter_list|(
name|Change
name|chg
parameter_list|)
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|alterTopicTitle
argument_list|()
argument_list|,
name|Util
operator|.
name|C
operator|.
name|headingAlterTopicMessage
argument_list|()
argument_list|,
operator|new
name|ChangeDetailCache
operator|.
name|IgnoreErrorCallback
argument_list|()
argument_list|)
expr_stmt|;
name|change
operator|=
name|chg
expr_stmt|;
name|newTopic
operator|=
operator|new
name|TextBox
argument_list|()
expr_stmt|;
name|newTopic
operator|.
name|addKeyPressHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|setFocusOn
argument_list|(
name|newTopic
argument_list|)
expr_stmt|;
name|panel
operator|.
name|insert
argument_list|(
name|newTopic
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|panel
operator|.
name|insert
argument_list|(
operator|new
name|InlineLabel
argument_list|(
name|Util
operator|.
name|C
operator|.
name|alterTopicLabel
argument_list|()
argument_list|)
argument_list|,
literal|0
argument_list|)
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
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|newTopic
operator|.
name|setText
argument_list|(
name|change
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doTopicEdit ()
specifier|private
name|void
name|doTopicEdit
parameter_list|()
block|{
name|String
name|topic
init|=
name|newTopic
operator|.
name|getText
argument_list|()
decl_stmt|;
name|ChangeApi
operator|.
name|topic
argument_list|(
name|change
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|topic
argument_list|,
name|getMessageText
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|String
name|result
parameter_list|)
block|{
name|sent
operator|=
literal|true
expr_stmt|;
name|Gerrit
operator|.
name|display
argument_list|(
name|PageLinks
operator|.
name|toChange
argument_list|(
name|change
operator|.
name|getId
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
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|enableButtons
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSend ()
specifier|public
name|void
name|onSend
parameter_list|()
block|{
name|doTopicEdit
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getSource
argument_list|()
operator|==
name|newTopic
operator|&&
name|event
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|doTopicEdit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

