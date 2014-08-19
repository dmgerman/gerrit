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
DECL|package|com.google.gerrit.client.documentation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|documentation
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
name|ui
operator|.
name|NavigationTable
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
name|Window
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
name|Cell
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

begin_class
DECL|class|DocTable
class|class
name|DocTable
extends|extends
name|NavigationTable
argument_list|<
name|DocInfo
argument_list|>
block|{
DECL|field|C_TITLE
specifier|private
specifier|static
specifier|final
name|int
name|C_TITLE
init|=
literal|1
decl_stmt|;
DECL|field|rows
specifier|private
name|int
name|rows
init|=
literal|0
decl_stmt|;
DECL|field|dataBeginRow
specifier|private
name|int
name|dataBeginRow
init|=
literal|0
decl_stmt|;
DECL|method|DocTable ()
specifier|public
name|DocTable
parameter_list|()
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|docItemHelp
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|C_TITLE
argument_list|,
name|Util
operator|.
name|C
operator|.
name|docTableColumnTitle
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
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|C_TITLE
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
name|table
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
name|Cell
name|cell
init|=
name|table
operator|.
name|getCellForEvent
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|cell
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|getRowItem
argument_list|(
name|cell
operator|.
name|getRowIndex
argument_list|()
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|movePointerTo
argument_list|(
name|cell
operator|.
name|getRowIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRowItemKey (DocInfo item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
name|DocInfo
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|url
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onOpenRow (int row)
specifier|protected
name|void
name|onOpenRow
parameter_list|(
name|int
name|row
parameter_list|)
block|{
name|DocInfo
name|d
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|Window
operator|.
name|Location
operator|.
name|assign
argument_list|(
name|d
operator|.
name|getFullUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|insertNoneRow (int row)
specifier|private
name|void
name|insertNoneRow
parameter_list|(
name|int
name|row
parameter_list|)
block|{
name|table
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|docTableNone
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
name|fmt
operator|.
name|setStyleName
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
name|emptySection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|insertDocRow (int row)
specifier|private
name|void
name|insertDocRow
parameter_list|(
name|int
name|row
parameter_list|)
block|{
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
block|}
annotation|@
name|Override
DECL|method|applyDataRowStyle (int row)
specifier|protected
name|void
name|applyDataRowStyle
parameter_list|(
name|int
name|row
parameter_list|)
block|{
name|super
operator|.
name|applyDataRowStyle
argument_list|(
name|row
argument_list|)
expr_stmt|;
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
name|C_TITLE
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
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|C_TITLE
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|cSUBJECT
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|populateDocRow (int row, DocInfo d)
specifier|private
name|void
name|populateDocRow
parameter_list|(
name|int
name|row
parameter_list|,
name|DocInfo
name|d
parameter_list|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|C_TITLE
argument_list|,
operator|new
name|DocLink
argument_list|(
name|d
argument_list|)
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
DECL|method|display (JsArray<DocInfo> docList)
specifier|public
name|void
name|display
parameter_list|(
name|JsArray
argument_list|<
name|DocInfo
argument_list|>
name|docList
parameter_list|)
block|{
name|int
name|sz
init|=
name|docList
operator|!=
literal|null
condition|?
name|docList
operator|.
name|length
argument_list|()
else|:
literal|0
decl_stmt|;
name|boolean
name|hadData
init|=
name|rows
operator|>
literal|0
decl_stmt|;
if|if
condition|(
name|hadData
condition|)
block|{
while|while
condition|(
name|sz
operator|<
name|rows
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|dataBeginRow
argument_list|)
expr_stmt|;
name|rows
operator|--
expr_stmt|;
block|}
block|}
else|else
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|dataBeginRow
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sz
operator|==
literal|0
condition|)
block|{
name|insertNoneRow
argument_list|(
name|dataBeginRow
argument_list|)
expr_stmt|;
return|return;
block|}
while|while
condition|(
name|rows
operator|<
name|sz
condition|)
block|{
name|insertDocRow
argument_list|(
name|dataBeginRow
operator|+
name|rows
argument_list|)
expr_stmt|;
name|rows
operator|++
expr_stmt|;
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
name|sz
condition|;
name|i
operator|++
control|)
block|{
name|populateDocRow
argument_list|(
name|dataBeginRow
operator|+
name|i
argument_list|,
name|docList
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|DocLink
specifier|public
specifier|static
class|class
name|DocLink
extends|extends
name|Anchor
block|{
DECL|method|DocLink (DocInfo d)
specifier|public
name|DocLink
parameter_list|(
name|DocInfo
name|d
parameter_list|)
block|{
name|super
argument_list|(
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
operator|.
name|cropSubject
argument_list|(
name|d
operator|.
name|title
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setHref
argument_list|(
name|d
operator|.
name|getFullUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

