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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|reviewdb
operator|.
name|AccountAgreement
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
name|ContributorAgreement
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
name|FancyFlexTable
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
name|Hyperlink
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
name|FlexTable
operator|.
name|FlexCellFormatter
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

begin_class
DECL|class|AgreementPanel
class|class
name|AgreementPanel
extends|extends
name|Composite
block|{
DECL|field|agreements
specifier|private
name|AgreementTable
name|agreements
decl_stmt|;
DECL|method|AgreementPanel ()
name|AgreementPanel
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|agreements
operator|=
operator|new
name|AgreementTable
argument_list|()
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|agreements
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
operator|new
name|Hyperlink
argument_list|(
name|Util
operator|.
name|C
operator|.
name|newAgreement
argument_list|()
argument_list|,
name|Link
operator|.
name|SETTINGS_NEW_AGREEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|public
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|myAgreements
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|AgreementInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|AgreementInfo
name|result
parameter_list|)
block|{
name|agreements
operator|.
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
DECL|class|AgreementTable
specifier|private
class|class
name|AgreementTable
extends|extends
name|FancyFlexTable
argument_list|<
name|AccountAgreement
argument_list|>
block|{
DECL|method|AgreementTable ()
name|AgreementTable
parameter_list|()
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|C
operator|.
name|agreementStatus
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|agreementName
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|agreementDescription
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|4
argument_list|,
name|Util
operator|.
name|C
operator|.
name|agreementAccepted
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
literal|4
condition|;
name|c
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|c
argument_list|,
name|S_DATA_HEADER
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final AgreementInfo result)
name|void
name|display
parameter_list|(
specifier|final
name|AgreementInfo
name|result
parameter_list|)
block|{
while|while
condition|(
literal|1
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
name|table
operator|.
name|removeRow
argument_list|(
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|AccountAgreement
name|k
range|:
name|result
operator|.
name|accepted
control|)
block|{
name|addOne
argument_list|(
name|result
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addOne (final AgreementInfo info, final AccountAgreement k)
name|void
name|addOne
parameter_list|(
specifier|final
name|AgreementInfo
name|info
parameter_list|,
specifier|final
name|AccountAgreement
name|k
parameter_list|)
block|{
specifier|final
name|int
name|row
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
name|table
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|applyDataRowStyle
argument_list|(
name|row
argument_list|)
expr_stmt|;
specifier|final
name|ContributorAgreement
name|cla
init|=
name|info
operator|.
name|agreements
operator|.
name|get
argument_list|(
name|k
operator|.
name|getAgreementId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|statusName
decl_stmt|;
if|if
condition|(
name|cla
operator|==
literal|null
operator|||
operator|!
name|cla
operator|.
name|isActive
argument_list|()
condition|)
block|{
name|statusName
operator|=
name|Util
operator|.
name|C
operator|.
name|agreementStatus_EXPIRED
argument_list|()
expr_stmt|;
block|}
else|else
block|{
switch|switch
condition|(
name|k
operator|.
name|getStatus
argument_list|()
condition|)
block|{
case|case
name|NEW
case|:
name|statusName
operator|=
name|Util
operator|.
name|C
operator|.
name|agreementStatus_NEW
argument_list|()
expr_stmt|;
break|break;
case|case
name|REJECTED
case|:
name|statusName
operator|=
name|Util
operator|.
name|C
operator|.
name|agreementStatus_REJECTED
argument_list|()
expr_stmt|;
break|break;
case|case
name|VERIFIED
case|:
name|statusName
operator|=
name|Util
operator|.
name|C
operator|.
name|agreementStatus_VERIFIED
argument_list|()
expr_stmt|;
break|break;
default|default:
name|statusName
operator|=
name|k
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|statusName
argument_list|)
expr_stmt|;
if|if
condition|(
name|cla
operator|==
literal|null
condition|)
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|String
name|url
init|=
name|cla
operator|.
name|getAgreementUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
name|url
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|(
name|cla
operator|.
name|getShortName
argument_list|()
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|a
operator|.
name|setTarget
argument_list|(
literal|"_blank"
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|cla
operator|.
name|getShortName
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
literal|3
argument_list|,
name|cla
operator|.
name|getShortDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|SafeHtmlBuilder
name|b
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|k
operator|.
name|getAcceptedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|br
argument_list|()
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|k
operator|.
name|getReviewedOn
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|table
argument_list|,
name|row
argument_list|,
literal|4
argument_list|,
name|b
argument_list|)
expr_stmt|;
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|1
init|;
name|c
operator|<=
literal|4
condition|;
name|c
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|c
argument_list|,
name|S_DATA_CELL
argument_list|)
expr_stmt|;
block|}
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
literal|"C_LAST_UPDATE"
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

