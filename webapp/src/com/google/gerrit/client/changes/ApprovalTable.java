begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|client
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
name|client
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
name|client
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
name|client
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
name|client
operator|.
name|reviewdb
operator|.
name|ChangeApproval
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
name|getGerritConfig
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
literal|"gerrit-InfoTable"
argument_list|)
expr_stmt|;
name|displayHeader
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
DECL|method|displayHeader ()
specifier|private
name|void
name|displayHeader
parameter_list|()
block|{
name|int
name|col
init|=
literal|0
decl_stmt|;
name|header
argument_list|(
name|col
operator|++
argument_list|,
name|Util
operator|.
name|C
operator|.
name|approvalTableReviewer
argument_list|()
argument_list|)
expr_stmt|;
name|header
argument_list|(
name|col
operator|++
argument_list|,
literal|""
argument_list|)
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
name|header
argument_list|(
name|col
operator|++
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
block|}
name|applyEdgeStyles
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|header (final int col, final String title)
specifier|private
name|void
name|header
parameter_list|(
specifier|final
name|int
name|col
parameter_list|,
specifier|final
name|String
name|title
parameter_list|)
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|col
argument_list|,
name|title
argument_list|)
expr_stmt|;
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|col
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
block|}
DECL|method|applyEdgeStyles (final int row)
specifier|private
name|void
name|applyEdgeStyles
parameter_list|(
specifier|final
name|int
name|row
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
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
literal|"leftmost"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
literal|"reviewer"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
literal|"approvalrole"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|1
operator|+
name|types
operator|.
name|size
argument_list|()
argument_list|,
literal|"rightmost"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|2
operator|+
name|types
operator|.
name|size
argument_list|()
argument_list|,
literal|"approvalhint"
argument_list|)
expr_stmt|;
block|}
DECL|method|applyScoreStyles (final int row)
specifier|private
name|void
name|applyScoreStyles
parameter_list|(
specifier|final
name|int
name|row
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
for|for
control|(
name|int
name|col
init|=
literal|0
init|;
name|col
operator|<
name|types
operator|.
name|size
argument_list|()
condition|;
name|col
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|2
operator|+
name|col
argument_list|,
literal|"approvalscore"
argument_list|)
expr_stmt|;
block|}
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
DECL|method|display (final List<ApprovalDetail> rows)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|ApprovalDetail
argument_list|>
name|rows
parameter_list|)
block|{
specifier|final
name|int
name|oldcnt
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
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
if|if
condition|(
name|oldcnt
operator|<
literal|1
operator|+
name|rows
operator|.
name|size
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|row
init|=
name|oldcnt
init|;
name|row
operator|<
literal|1
operator|+
name|rows
operator|.
name|size
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|applyEdgeStyles
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|applyScoreStyles
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|displayRow (final int row, final ApprovalDetail ad)
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
name|ChangeApproval
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
name|table
operator|.
name|clearCell
argument_list|(
name|row
argument_list|,
name|col
operator|++
argument_list|)
expr_stmt|;
comment|// TODO populate the account role
for|for
control|(
specifier|final
name|ApprovalType
name|type
range|:
name|types
control|)
block|{
specifier|final
name|ChangeApproval
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
name|Gerrit
operator|.
name|ICONS
operator|.
name|redNot
argument_list|()
operator|.
name|createImage
argument_list|()
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
name|Gerrit
operator|.
name|ICONS
operator|.
name|greenCheck
argument_list|()
operator|.
name|createImage
argument_list|()
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
name|removeStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
literal|"negscore"
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
literal|"posscore"
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
literal|"negscore"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|removeStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
literal|"posscore"
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
operator|++
argument_list|,
name|hint
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

