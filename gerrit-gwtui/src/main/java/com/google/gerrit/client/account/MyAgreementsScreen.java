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
name|info
operator|.
name|AgreementInfo
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
name|FancyFlexTable
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
name|Hyperlink
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
name|ContributorAgreement
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
name|FlexTable
operator|.
name|FlexCellFormatter
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
DECL|class|MyAgreementsScreen
specifier|public
class|class
name|MyAgreementsScreen
extends|extends
name|SettingsScreen
block|{
DECL|field|agreements
specifier|private
name|AgreementTable
name|agreements
decl_stmt|;
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
name|agreements
operator|=
operator|new
name|AgreementTable
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|agreements
argument_list|)
expr_stmt|;
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
name|PageLinks
operator|.
name|SETTINGS_NEW_AGREEMENT
argument_list|)
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
name|AccountApi
operator|.
name|getAgreements
argument_list|(
literal|"self"
argument_list|,
operator|new
name|ScreenLoadCallback
argument_list|<
name|JsArray
argument_list|<
name|AgreementInfo
argument_list|>
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|preDisplay
parameter_list|(
name|JsArray
argument_list|<
name|AgreementInfo
argument_list|>
name|result
parameter_list|)
block|{
name|agreements
operator|.
name|display
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|result
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|class|AgreementTable
specifier|private
specifier|static
class|class
name|AgreementTable
extends|extends
name|FancyFlexTable
argument_list|<
name|ContributorAgreement
argument_list|>
block|{
DECL|method|AgreementTable ()
name|AgreementTable
parameter_list|()
block|{
name|table
operator|.
name|setWidth
argument_list|(
literal|""
argument_list|)
expr_stmt|;
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
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|agreementDescription
argument_list|()
argument_list|)
expr_stmt|;
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
operator|<
literal|3
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (List<AgreementInfo> result)
name|void
name|display
parameter_list|(
name|List
argument_list|<
name|AgreementInfo
argument_list|>
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
block|{
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
block|}
for|for
control|(
name|AgreementInfo
name|info
range|:
name|result
control|)
block|{
name|addOne
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addOne (AgreementInfo info)
name|void
name|addOne
parameter_list|(
name|AgreementInfo
name|info
parameter_list|)
block|{
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
name|String
name|url
init|=
name|info
operator|.
name|url
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
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|(
name|info
operator|.
name|name
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
literal|1
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
literal|1
argument_list|,
name|info
operator|.
name|name
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
literal|2
argument_list|,
name|info
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
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
operator|<
literal|3
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|dataCell
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

