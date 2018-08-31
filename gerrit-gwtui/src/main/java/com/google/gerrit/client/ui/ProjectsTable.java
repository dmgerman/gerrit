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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
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
name|projects
operator|.
name|ProjectInfo
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
name|projects
operator|.
name|ProjectMap
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
name|List
import|;
end_import

begin_class
DECL|class|ProjectsTable
specifier|public
class|class
name|ProjectsTable
extends|extends
name|NavigationTable
argument_list|<
name|ProjectInfo
argument_list|>
block|{
DECL|field|C_STATE
specifier|public
specifier|static
specifier|final
name|int
name|C_STATE
init|=
literal|1
decl_stmt|;
DECL|field|C_NAME
specifier|public
specifier|static
specifier|final
name|int
name|C_NAME
init|=
literal|2
decl_stmt|;
DECL|field|C_DESCRIPTION
specifier|public
specifier|static
specifier|final
name|int
name|C_DESCRIPTION
init|=
literal|3
decl_stmt|;
DECL|field|C_REPO_BROWSER
specifier|public
specifier|static
specifier|final
name|int
name|C_REPO_BROWSER
init|=
literal|4
decl_stmt|;
DECL|method|ProjectsTable ()
specifier|public
name|ProjectsTable
parameter_list|()
block|{
name|super
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectItemHelp
argument_list|()
argument_list|)
expr_stmt|;
name|initColumnHeaders
argument_list|()
expr_stmt|;
block|}
DECL|method|initColumnHeaders ()
specifier|protected
name|void
name|initColumnHeaders
parameter_list|()
block|{
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|C_STATE
argument_list|,
name|Util
operator|.
name|C
operator|.
name|projectStateAbbrev
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|getElement
argument_list|(
literal|0
argument_list|,
name|C_STATE
argument_list|)
operator|.
name|setTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|projectStateHelp
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|C_NAME
argument_list|,
name|Util
operator|.
name|C
operator|.
name|projectName
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
name|C_DESCRIPTION
argument_list|,
name|Util
operator|.
name|C
operator|.
name|projectDescription
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
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|C_STATE
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|iconHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|C_NAME
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
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|C_DESCRIPTION
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
annotation|@
name|Override
DECL|method|getRowItemKey (ProjectInfo item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
name|ProjectInfo
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|name
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
if|if
condition|(
name|row
operator|>
literal|0
condition|)
block|{
name|movePointerTo
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (ProjectMap projects)
specifier|public
name|void
name|display
parameter_list|(
name|ProjectMap
name|projects
parameter_list|)
block|{
name|displaySubset
argument_list|(
name|projects
argument_list|,
literal|0
argument_list|,
name|projects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|displaySubset (ProjectMap projects, int fromIndex, int toIndex)
specifier|public
name|void
name|displaySubset
parameter_list|(
name|ProjectMap
name|projects
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
name|List
argument_list|<
name|ProjectInfo
argument_list|>
name|list
init|=
name|Natives
operator|.
name|asList
argument_list|(
name|projects
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
name|comparing
argument_list|(
name|ProjectInfo
operator|::
name|name
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|ProjectInfo
name|p
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
name|insert
argument_list|(
name|table
operator|.
name|getRowCount
argument_list|()
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
name|finishDisplay
argument_list|()
expr_stmt|;
block|}
DECL|method|insert (int row, ProjectInfo k)
specifier|protected
name|void
name|insert
parameter_list|(
name|int
name|row
parameter_list|,
name|ProjectInfo
name|k
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
name|C_STATE
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|iconCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|C_NAME
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
name|C_NAME
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|projectNameColumn
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|C_DESCRIPTION
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
name|populate
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|populate (int row, ProjectInfo k)
specifier|protected
name|void
name|populate
parameter_list|(
name|int
name|row
parameter_list|,
name|ProjectInfo
name|k
parameter_list|)
block|{
name|populateState
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|C_NAME
argument_list|,
name|k
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|C_DESCRIPTION
argument_list|,
name|k
operator|.
name|description
argument_list|()
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
DECL|method|populateState (int row, ProjectInfo k)
specifier|protected
name|void
name|populateState
parameter_list|(
name|int
name|row
parameter_list|,
name|ProjectInfo
name|k
parameter_list|)
block|{
name|Image
name|state
init|=
operator|new
name|Image
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|k
operator|.
name|state
argument_list|()
condition|)
block|{
case|case
name|HIDDEN
case|:
name|state
operator|.
name|setResource
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|redNot
argument_list|()
argument_list|)
expr_stmt|;
name|state
operator|.
name|setTitle
argument_list|(
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
name|k
operator|.
name|state
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
break|break;
case|case
name|READ_ONLY
case|:
name|state
operator|.
name|setResource
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|readOnly
argument_list|()
argument_list|)
expr_stmt|;
name|state
operator|.
name|setTitle
argument_list|(
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
name|k
operator|.
name|state
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|ProjectsTable
operator|.
name|C_STATE
argument_list|,
name|state
argument_list|)
expr_stmt|;
break|break;
case|case
name|ACTIVE
case|:
default|default:
comment|// Intentionally left blank, do not show an icon when active.
break|break;
block|}
block|}
block|}
end_class

end_unit

