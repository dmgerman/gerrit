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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|admin
operator|.
name|Util
operator|.
name|C
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
name|Dispatcher
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
name|groups
operator|.
name|GroupList
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
name|groups
operator|.
name|GroupMap
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
name|GroupInfo
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
name|HighlightingInlineHyperlink
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
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|History
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
name|Image
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
DECL|class|GroupTable
specifier|public
class|class
name|GroupTable
extends|extends
name|NavigationTable
argument_list|<
name|GroupInfo
argument_list|>
block|{
DECL|field|NUM_COLS
specifier|private
specifier|static
specifier|final
name|int
name|NUM_COLS
init|=
literal|3
decl_stmt|;
DECL|method|GroupTable ()
specifier|public
name|GroupTable
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|GroupTable (final String pointerId)
specifier|public
name|GroupTable
parameter_list|(
specifier|final
name|String
name|pointerId
parameter_list|)
block|{
name|super
argument_list|(
name|C
operator|.
name|groupItemHelp
argument_list|()
argument_list|)
expr_stmt|;
name|setSavePointerId
argument_list|(
name|pointerId
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
name|C
operator|.
name|columnGroupName
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
name|C
operator|.
name|columnGroupDescription
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
name|C
operator|.
name|columnGroupVisibleToAll
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
specifier|final
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
operator|!=
literal|null
operator|&&
name|cell
operator|.
name|getCellIndex
argument_list|()
operator|!=
literal|1
operator|&&
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
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|NUM_COLS
condition|;
name|i
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|i
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
annotation|@
name|Override
DECL|method|getRowItemKey (final GroupInfo item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
specifier|final
name|GroupInfo
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|getGroupId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onOpenRow (final int row)
specifier|protected
name|void
name|onOpenRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|)
block|{
name|GroupInfo
name|groupInfo
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|isInteralGroup
argument_list|(
name|groupInfo
argument_list|)
condition|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|groupInfo
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|groupInfo
operator|.
name|url
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Window
operator|.
name|open
argument_list|(
name|groupInfo
operator|.
name|url
argument_list|()
argument_list|,
literal|"_self"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (GroupMap groups, String toHighlight)
specifier|public
name|void
name|display
parameter_list|(
name|GroupMap
name|groups
parameter_list|,
name|String
name|toHighlight
parameter_list|)
block|{
name|display
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|groups
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|toHighlight
argument_list|)
expr_stmt|;
block|}
DECL|method|display (GroupList groups)
specifier|public
name|void
name|display
parameter_list|(
name|GroupList
name|groups
parameter_list|)
block|{
name|display
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|groups
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|display (List<GroupInfo> list, String toHighlight)
specifier|public
name|void
name|display
parameter_list|(
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|list
parameter_list|,
name|String
name|toHighlight
parameter_list|)
block|{
name|displaySubset
argument_list|(
name|list
argument_list|,
name|toHighlight
argument_list|,
literal|0
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|displaySubset (GroupMap groups, int fromIndex, int toIndex, String toHighlight)
specifier|public
name|void
name|displaySubset
parameter_list|(
name|GroupMap
name|groups
parameter_list|,
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
parameter_list|,
name|String
name|toHighlight
parameter_list|)
block|{
name|displaySubset
argument_list|(
name|Natives
operator|.
name|asList
argument_list|(
name|groups
operator|.
name|values
argument_list|()
argument_list|)
argument_list|,
name|toHighlight
argument_list|,
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
expr_stmt|;
block|}
DECL|method|displaySubset (List<GroupInfo> list, String toHighlight, int fromIndex, int toIndex)
specifier|public
name|void
name|displaySubset
parameter_list|(
name|List
argument_list|<
name|GroupInfo
argument_list|>
name|list
parameter_list|,
name|String
name|toHighlight
parameter_list|,
name|int
name|fromIndex
parameter_list|,
name|int
name|toIndex
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
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
operator|new
name|Comparator
argument_list|<
name|GroupInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|GroupInfo
name|a
parameter_list|,
name|GroupInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|name
argument_list|()
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
for|for
control|(
name|GroupInfo
name|group
range|:
name|list
operator|.
name|subList
argument_list|(
name|fromIndex
argument_list|,
name|toIndex
argument_list|)
control|)
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
name|populate
argument_list|(
name|row
argument_list|,
name|group
argument_list|,
name|toHighlight
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populate (final int row, final GroupInfo k, final String toHighlight)
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|GroupInfo
name|k
parameter_list|,
specifier|final
name|String
name|toHighlight
parameter_list|)
block|{
if|if
condition|(
name|k
operator|.
name|url
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isInteralGroup
argument_list|(
name|k
argument_list|)
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
operator|new
name|HighlightingInlineHyperlink
argument_list|(
name|k
operator|.
name|name
argument_list|()
argument_list|,
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|k
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|,
name|toHighlight
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Anchor
name|link
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|link
operator|.
name|setHTML
argument_list|(
name|Util
operator|.
name|highlight
argument_list|(
name|k
operator|.
name|name
argument_list|()
argument_list|,
name|toHighlight
argument_list|)
argument_list|)
expr_stmt|;
name|link
operator|.
name|setHref
argument_list|(
name|k
operator|.
name|url
argument_list|()
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
name|link
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|table
operator|.
name|setHTML
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|highlight
argument_list|(
name|k
operator|.
name|name
argument_list|()
argument_list|,
name|toHighlight
argument_list|)
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
name|k
operator|.
name|description
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|k
operator|.
name|options
argument_list|()
operator|.
name|isVisibleToAll
argument_list|()
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|3
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
specifier|final
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
name|row
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
name|groupName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|NUM_COLS
condition|;
name|i
operator|++
control|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|i
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
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|isInteralGroup (final GroupInfo groupInfo)
specifier|private
name|boolean
name|isInteralGroup
parameter_list|(
specifier|final
name|GroupInfo
name|groupInfo
parameter_list|)
block|{
return|return
name|groupInfo
operator|!=
literal|null
operator|&&
name|groupInfo
operator|.
name|url
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"#"
operator|+
name|PageLinks
operator|.
name|ADMIN_GROUPS
argument_list|)
return|;
block|}
block|}
end_class

end_unit

