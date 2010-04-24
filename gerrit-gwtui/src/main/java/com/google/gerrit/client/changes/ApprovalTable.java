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
name|patches
operator|.
name|PatchUtil
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
name|AccountDashboardLink
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
name|AddMemberBox
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
name|ApprovalDetail
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
name|ApprovalType
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
name|ReviewerResult
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
name|Account
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
name|ApprovalCategory
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
name|ApprovalCategoryValue
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
name|PatchSetApproval
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
name|user
operator|.
name|client
operator|.
name|DOM
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
name|ArrayList
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
comment|/** Displays a table of {@link ApprovalDetail} objects for a change record. */
end_comment

begin_class
DECL|class|ApprovalTable
specifier|public
class|class
name|ApprovalTable
extends|extends
name|Composite
block|{
DECL|field|types
specifier|private
specifier|final
name|List
argument_list|<
name|ApprovalType
argument_list|>
name|types
decl_stmt|;
DECL|field|table
specifier|private
specifier|final
name|Grid
name|table
decl_stmt|;
DECL|field|missing
specifier|private
specifier|final
name|Widget
name|missing
decl_stmt|;
DECL|field|addReviewer
specifier|private
specifier|final
name|Panel
name|addReviewer
decl_stmt|;
DECL|field|addMemberBox
specifier|private
specifier|final
name|AddMemberBox
name|addMemberBox
decl_stmt|;
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|accountCache
specifier|private
name|AccountInfoCache
name|accountCache
init|=
name|AccountInfoCache
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|method|ApprovalTable ()
specifier|public
name|ApprovalTable
parameter_list|()
block|{
name|types
operator|=
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getApprovalTypes
argument_list|()
operator|.
name|getApprovalTypes
argument_list|()
expr_stmt|;
name|table
operator|=
operator|new
name|Grid
argument_list|(
literal|1
argument_list|,
literal|3
operator|+
name|types
operator|.
name|size
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
name|infoTable
argument_list|()
argument_list|)
expr_stmt|;
name|displayHeader
argument_list|()
expr_stmt|;
name|missing
operator|=
operator|new
name|Widget
argument_list|()
block|{
block|{
name|setElement
argument_list|(
name|DOM
operator|.
name|createElement
argument_list|(
literal|"ul"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|missing
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
name|missingApprovalList
argument_list|()
argument_list|)
expr_stmt|;
name|addReviewer
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|addReviewer
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
name|addReviewer
argument_list|()
argument_list|)
expr_stmt|;
name|addMemberBox
operator|=
operator|new
name|AddMemberBox
argument_list|()
expr_stmt|;
name|addMemberBox
operator|.
name|setAddButtonText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|approvalTableAddReviewer
argument_list|()
argument_list|)
expr_stmt|;
name|addMemberBox
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
name|doAddReviewer
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|addReviewer
operator|.
name|add
argument_list|(
name|addMemberBox
argument_list|)
expr_stmt|;
name|addReviewer
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|missing
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|addReviewer
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|fp
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|approvalTable
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|displayHeader ()
specifier|private
name|void
name|displayHeader
parameter_list|()
block|{
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|int
name|col
init|=
literal|0
decl_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|col
argument_list|,
name|Util
operator|.
name|C
operator|.
name|approvalTableReviewer
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
name|col
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
name|col
operator|++
expr_stmt|;
name|table
operator|.
name|clearCell
argument_list|(
literal|0
argument_list|,
name|col
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
name|col
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
name|col
operator|++
expr_stmt|;
for|for
control|(
specifier|final
name|ApprovalType
name|t
range|:
name|types
control|)
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|col
argument_list|,
name|t
operator|.
name|getCategory
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
name|col
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
name|col
operator|++
expr_stmt|;
block|}
name|table
operator|.
name|clearCell
argument_list|(
literal|0
argument_list|,
name|col
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
name|col
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
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|rightmost
argument_list|()
argument_list|)
expr_stmt|;
name|col
operator|++
expr_stmt|;
block|}
DECL|method|setAccountInfoCache (final AccountInfoCache aic)
specifier|public
name|void
name|setAccountInfoCache
parameter_list|(
specifier|final
name|AccountInfoCache
name|aic
parameter_list|)
block|{
assert|assert
name|aic
operator|!=
literal|null
assert|;
name|accountCache
operator|=
name|aic
expr_stmt|;
block|}
DECL|method|link (final Account.Id id)
specifier|private
name|AccountDashboardLink
name|link
parameter_list|(
specifier|final
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|AccountDashboardLink
operator|.
name|link
argument_list|(
name|accountCache
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|display (final Change change, final Set<ApprovalCategory.Id> need, final List<ApprovalDetail> rows)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|Change
name|change
parameter_list|,
specifier|final
name|Set
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|>
name|need
parameter_list|,
specifier|final
name|List
argument_list|<
name|ApprovalDetail
argument_list|>
name|rows
parameter_list|)
block|{
name|changeId
operator|=
name|change
operator|.
name|getId
argument_list|()
expr_stmt|;
if|if
condition|(
name|rows
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|table
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|resizeRows
argument_list|(
literal|1
operator|+
name|rows
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|rows
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|displayRow
argument_list|(
name|i
operator|+
literal|1
argument_list|,
name|rows
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|change
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Element
name|missingList
init|=
name|missing
operator|.
name|getElement
argument_list|()
decl_stmt|;
while|while
condition|(
name|DOM
operator|.
name|getChildCount
argument_list|(
name|missingList
argument_list|)
operator|>
literal|0
condition|)
block|{
name|DOM
operator|.
name|removeChild
argument_list|(
name|missingList
argument_list|,
name|DOM
operator|.
name|getChild
argument_list|(
name|missingList
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|missing
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|need
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|ApprovalType
name|at
range|:
name|types
control|)
block|{
if|if
condition|(
name|need
operator|.
name|contains
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
specifier|final
name|Element
name|li
init|=
name|DOM
operator|.
name|createElement
argument_list|(
literal|"li"
argument_list|)
decl_stmt|;
name|li
operator|.
name|setClassName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|missingApproval
argument_list|()
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|setInnerText
argument_list|(
name|li
argument_list|,
name|Util
operator|.
name|M
operator|.
name|needApproval
argument_list|(
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|at
operator|.
name|getMax
argument_list|()
operator|.
name|formatValue
argument_list|()
argument_list|,
name|at
operator|.
name|getMax
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|missingList
argument_list|,
name|li
argument_list|)
expr_stmt|;
name|missing
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|addReviewer
operator|.
name|setVisible
argument_list|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doAddReviewer ()
specifier|private
name|void
name|doAddReviewer
parameter_list|()
block|{
specifier|final
name|String
name|nameEmail
init|=
name|addMemberBox
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|nameEmail
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|addMemberBox
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|reviewers
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|reviewers
operator|.
name|add
argument_list|(
name|nameEmail
argument_list|)
expr_stmt|;
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|addReviewers
argument_list|(
name|changeId
argument_list|,
name|reviewers
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ReviewerResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|ReviewerResult
name|result
parameter_list|)
block|{
name|addMemberBox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addMemberBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|SafeHtmlBuilder
name|r
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|ReviewerResult
operator|.
name|Error
name|e
range|:
name|result
operator|.
name|getErrors
argument_list|()
control|)
block|{
switch|switch
condition|(
name|e
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|ACCOUNT_NOT_FOUND
case|:
name|r
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|accountNotFound
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|CHANGE_NOT_VISIBLE
case|:
name|r
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|changeNotVisibleTo
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
name|r
operator|.
name|append
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|" - "
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|e
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|br
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
operator|new
name|ErrorDialog
argument_list|(
name|r
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
specifier|final
name|ChangeDetail
name|r
init|=
name|result
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
name|setAccountInfoCache
argument_list|(
name|r
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
argument_list|,
name|r
operator|.
name|getMissingApprovals
argument_list|()
argument_list|,
name|r
operator|.
name|getApprovals
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|addMemberBox
operator|.
name|setEnabled
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
DECL|method|displayRow (final int row, final ApprovalDetail ad, final Change change)
specifier|private
name|void
name|displayRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|ApprovalDetail
name|ad
parameter_list|,
specifier|final
name|Change
name|change
parameter_list|)
block|{
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|ApprovalCategory
operator|.
name|Id
argument_list|,
name|PatchSetApproval
argument_list|>
name|am
init|=
name|ad
operator|.
name|getApprovalMap
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|hint
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|col
init|=
literal|0
decl_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|col
operator|++
argument_list|,
name|link
argument_list|(
name|ad
operator|.
name|getAccount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|ad
operator|.
name|canRemove
argument_list|()
condition|)
block|{
name|Button
name|remove
init|=
operator|new
name|Button
argument_list|(
literal|"X"
argument_list|)
decl_stmt|;
name|remove
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|M
operator|.
name|removeReviewer
argument_list|(
comment|//
name|FormatUtil
operator|.
name|name
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|ad
operator|.
name|getAccount
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|remove
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
name|removeReviewer
argument_list|()
argument_list|)
expr_stmt|;
name|remove
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
name|ClickEvent
name|event
parameter_list|)
block|{
name|doRemove
argument_list|(
name|ad
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|remove
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|clearCell
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
block|}
name|fmt
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|col
operator|++
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|removeReviewerCell
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|ApprovalType
name|type
range|:
name|types
control|)
block|{
name|fmt
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|approvalscore
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|PatchSetApproval
name|ca
init|=
name|am
operator|.
name|get
argument_list|(
name|type
operator|.
name|getCategory
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ca
operator|==
literal|null
operator|||
name|ca
operator|.
name|getValue
argument_list|()
operator|==
literal|0
condition|)
block|{
name|table
operator|.
name|clearCell
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
name|col
operator|++
expr_stmt|;
continue|continue;
block|}
specifier|final
name|ApprovalCategoryValue
name|acv
init|=
name|type
operator|.
name|getValue
argument_list|(
name|ca
argument_list|)
decl_stmt|;
if|if
condition|(
name|acv
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|hint
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|hint
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|hint
operator|.
name|append
argument_list|(
name|acv
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|.
name|isMaxNegative
argument_list|(
name|ca
argument_list|)
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|redNot
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|.
name|isMaxPositive
argument_list|(
name|ca
argument_list|)
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|greenCheck
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|vstr
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|ca
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ca
operator|.
name|getValue
argument_list|()
operator|>
literal|0
condition|)
block|{
name|vstr
operator|=
literal|"+"
operator|+
name|vstr
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|posscore
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|negscore
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|vstr
argument_list|)
expr_stmt|;
block|}
name|col
operator|++
expr_stmt|;
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|hint
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|rightmost
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|approvalhint
argument_list|()
argument_list|)
expr_stmt|;
name|col
operator|++
expr_stmt|;
block|}
DECL|method|doRemove (final ApprovalDetail ad)
specifier|private
name|void
name|doRemove
parameter_list|(
specifier|final
name|ApprovalDetail
name|ad
parameter_list|)
block|{
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|removeReviewer
argument_list|(
name|changeId
argument_list|,
name|ad
operator|.
name|getAccount
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ReviewerResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|ReviewerResult
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|ChangeDetail
name|r
init|=
name|result
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|display
argument_list|(
name|r
operator|.
name|getChange
argument_list|()
argument_list|,
name|r
operator|.
name|getMissingApprovals
argument_list|()
argument_list|,
name|r
operator|.
name|getApprovals
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
operator|new
name|ErrorDialog
argument_list|(
name|result
operator|.
name|getErrors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

