begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|OnEditEnabler
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
name|SmallHeading
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ValueChangeEvent
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
name|logical
operator|.
name|shared
operator|.
name|ValueChangeHandler
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
name|Event
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
name|CheckBox
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
name|FocusWidget
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
name|HorizontalPanel
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
name|ImageResourceRenderer
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
name|NpTextBox
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

begin_class
DECL|class|StringListPanel
specifier|public
class|class
name|StringListPanel
extends|extends
name|FlowPanel
block|{
DECL|field|t
specifier|private
specifier|final
name|StringListTable
name|t
decl_stmt|;
DECL|field|deleteButton
specifier|private
specifier|final
name|Button
name|deleteButton
decl_stmt|;
DECL|field|titlePanel
specifier|private
specifier|final
name|HorizontalPanel
name|titlePanel
decl_stmt|;
DECL|field|info
specifier|private
name|Image
name|info
decl_stmt|;
DECL|field|widget
specifier|private
name|FocusWidget
name|widget
decl_stmt|;
DECL|method|StringListPanel (String title, List<String> fieldNames, FocusWidget w, boolean autoSort)
specifier|public
name|StringListPanel
parameter_list|(
name|String
name|title
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|fieldNames
parameter_list|,
name|FocusWidget
name|w
parameter_list|,
name|boolean
name|autoSort
parameter_list|)
block|{
name|widget
operator|=
name|w
expr_stmt|;
name|titlePanel
operator|=
operator|new
name|HorizontalPanel
argument_list|()
expr_stmt|;
name|SmallHeading
name|titleLabel
init|=
operator|new
name|SmallHeading
argument_list|(
name|title
argument_list|)
decl_stmt|;
name|titlePanel
operator|.
name|add
argument_list|(
name|titleLabel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|titlePanel
argument_list|)
expr_stmt|;
name|t
operator|=
operator|new
name|StringListTable
argument_list|(
name|fieldNames
argument_list|,
name|autoSort
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|deleteButton
operator|=
operator|new
name|Button
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|stringListPanelDelete
argument_list|()
argument_list|)
expr_stmt|;
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|deleteButton
argument_list|)
expr_stmt|;
name|deleteButton
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
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|t
operator|.
name|deleteChecked
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|display (List<List<String>> values)
specifier|public
name|void
name|display
parameter_list|(
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
parameter_list|)
block|{
name|t
operator|.
name|display
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
DECL|method|setInfo (String msg)
specifier|public
name|void
name|setInfo
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
name|info
operator|=
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|info
argument_list|()
argument_list|)
expr_stmt|;
name|titlePanel
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|setTitle
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
DECL|method|getValues ()
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getValues
parameter_list|()
block|{
return|return
name|t
operator|.
name|getValues
argument_list|()
return|;
block|}
DECL|class|StringListTable
specifier|private
class|class
name|StringListTable
extends|extends
name|NavigationTable
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
block|{
DECL|field|inputs
specifier|private
specifier|final
name|List
argument_list|<
name|NpTextBox
argument_list|>
name|inputs
decl_stmt|;
DECL|field|autoSort
specifier|private
specifier|final
name|boolean
name|autoSort
decl_stmt|;
DECL|method|StringListTable (List<String> names, boolean autoSort)
name|StringListTable
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|,
name|boolean
name|autoSort
parameter_list|)
block|{
name|this
operator|.
name|autoSort
operator|=
name|autoSort
expr_stmt|;
name|Button
name|addButton
init|=
operator|new
name|Button
argument_list|(
operator|new
name|ImageResourceRenderer
argument_list|()
operator|.
name|render
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|listAdd
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|addButton
operator|.
name|setTitle
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|stringListPanelAdd
argument_list|()
argument_list|)
expr_stmt|;
name|OnEditEnabler
name|e
init|=
operator|new
name|OnEditEnabler
argument_list|(
name|addButton
argument_list|)
decl_stmt|;
name|inputs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
literal|0
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
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|leftMostCell
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
name|names
operator|.
name|size
argument_list|()
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
operator|+
literal|1
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
name|setText
argument_list|(
literal|0
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|names
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
name|NpTextBox
name|input
init|=
operator|new
name|NpTextBox
argument_list|()
decl_stmt|;
name|input
operator|.
name|setVisibleLength
argument_list|(
literal|35
argument_list|)
expr_stmt|;
name|input
operator|.
name|addKeyPressHandler
argument_list|(
operator|new
name|KeyPressHandler
argument_list|()
block|{
annotation|@
name|Override
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
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|add
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|inputs
operator|.
name|add
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|1
argument_list|,
name|i
operator|+
literal|1
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
name|setWidget
argument_list|(
literal|1
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
name|addButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|addButton
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
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|add
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|1
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
name|iconHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|1
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
name|leftMostCell
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|addButton
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|autoSort
condition|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|0
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|+
literal|1
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
name|names
operator|.
name|size
argument_list|()
operator|+
literal|2
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
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|+
literal|1
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
literal|1
argument_list|,
name|names
operator|.
name|size
argument_list|()
operator|+
literal|2
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
block|}
block|}
DECL|method|display (List<List<String>> values)
name|void
name|display
parameter_list|(
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
parameter_list|)
block|{
for|for
control|(
name|int
name|row
init|=
literal|2
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|row
operator|--
argument_list|)
expr_stmt|;
block|}
name|int
name|row
init|=
literal|2
decl_stmt|;
for|for
control|(
name|List
argument_list|<
name|String
argument_list|>
name|v
range|:
name|values
control|)
block|{
name|populate
argument_list|(
name|row
argument_list|,
name|v
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
block|}
name|updateNavigationLinks
argument_list|()
expr_stmt|;
block|}
DECL|method|getValues ()
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getValues
parameter_list|()
block|{
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|2
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|values
operator|.
name|add
argument_list|(
name|getRowItem
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|values
return|;
block|}
annotation|@
name|Override
DECL|method|getRowItem (int row)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getRowItem
parameter_list|(
name|int
name|row
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|v
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|inputs
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|v
operator|.
name|add
argument_list|(
name|table
operator|.
name|getText
argument_list|(
name|row
argument_list|,
name|i
operator|+
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|v
return|;
block|}
DECL|method|populate (final int row, List<String> values)
specifier|private
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|values
parameter_list|)
block|{
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
literal|0
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
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|leftMostCell
argument_list|()
argument_list|)
expr_stmt|;
name|CheckBox
name|checkBox
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|checkBox
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|Boolean
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onValueChange
parameter_list|(
name|ValueChangeEvent
argument_list|<
name|Boolean
argument_list|>
name|event
parameter_list|)
block|{
name|enableDelete
argument_list|()
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
literal|0
argument_list|,
name|checkBox
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
name|values
operator|.
name|size
argument_list|()
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
operator|+
literal|1
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
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|i
operator|+
literal|1
argument_list|,
name|values
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|autoSort
condition|)
block|{
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|values
operator|.
name|size
argument_list|()
operator|+
literal|1
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
name|values
operator|.
name|size
argument_list|()
operator|+
literal|2
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
name|Image
name|down
init|=
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|arrowDown
argument_list|()
argument_list|)
decl_stmt|;
name|down
operator|.
name|setTitle
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|stringListPanelDown
argument_list|()
argument_list|)
expr_stmt|;
name|down
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
name|moveDown
argument_list|(
name|row
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
name|values
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|,
name|down
argument_list|)
expr_stmt|;
name|Image
name|up
init|=
operator|new
name|Image
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|arrowUp
argument_list|()
argument_list|)
decl_stmt|;
name|up
operator|.
name|setTitle
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|stringListPanelUp
argument_list|()
argument_list|)
expr_stmt|;
name|up
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
name|moveUp
argument_list|(
name|row
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
name|values
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|,
name|up
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onCellSingleClick (Event event, int row, int column)
specifier|protected
name|void
name|onCellSingleClick
parameter_list|(
name|Event
name|event
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
if|if
condition|(
name|column
operator|==
name|inputs
operator|.
name|size
argument_list|()
operator|+
literal|1
operator|&&
name|row
operator|>=
literal|2
operator|&&
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|2
condition|)
block|{
name|moveDown
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|column
operator|==
name|inputs
operator|.
name|size
argument_list|()
operator|+
literal|2
operator|&&
name|row
operator|>
literal|2
condition|)
block|{
name|moveUp
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|moveDown (int row)
name|void
name|moveDown
parameter_list|(
name|int
name|row
parameter_list|)
block|{
if|if
condition|(
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
condition|)
block|{
name|swap
argument_list|(
name|row
argument_list|,
name|row
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|moveUp (int row)
name|void
name|moveUp
parameter_list|(
name|int
name|row
parameter_list|)
block|{
if|if
condition|(
name|row
operator|>
literal|2
condition|)
block|{
name|swap
argument_list|(
name|row
operator|-
literal|1
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|swap (int row1, int row2)
name|void
name|swap
parameter_list|(
name|int
name|row1
parameter_list|,
name|int
name|row2
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|getRowItem
argument_list|(
name|row1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|nextValue
init|=
name|getRowItem
argument_list|(
name|row2
argument_list|)
decl_stmt|;
name|populate
argument_list|(
name|row1
argument_list|,
name|nextValue
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|row2
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|updateNavigationLinks
argument_list|()
expr_stmt|;
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|updateNavigationLinks ()
specifier|private
name|void
name|updateNavigationLinks
parameter_list|()
block|{
if|if
condition|(
operator|!
name|autoSort
condition|)
block|{
for|for
control|(
name|int
name|row
init|=
literal|2
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|inputs
operator|.
name|size
argument_list|()
operator|+
literal|1
argument_list|)
operator|.
name|setVisible
argument_list|(
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|inputs
operator|.
name|size
argument_list|()
operator|+
literal|2
argument_list|)
operator|.
name|setVisible
argument_list|(
name|row
operator|>
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|add ()
name|void
name|add
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|NpTextBox
name|input
range|:
name|inputs
control|)
block|{
name|String
name|v
init|=
name|input
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|v
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|input
operator|.
name|setValue
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|v
argument_list|)
expr_stmt|;
block|}
block|}
name|insert
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
DECL|method|insert (List<String> v)
name|void
name|insert
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|v
parameter_list|)
block|{
name|int
name|insertPos
init|=
name|table
operator|.
name|getRowCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|autoSort
condition|)
block|{
for|for
control|(
name|int
name|row
init|=
literal|1
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|int
name|compareResult
init|=
name|v
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|compareTo
argument_list|(
name|table
operator|.
name|getText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|compareResult
operator|<
literal|0
condition|)
block|{
name|insertPos
operator|=
name|row
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|compareResult
operator|==
literal|0
condition|)
block|{
return|return;
block|}
block|}
block|}
name|table
operator|.
name|insertRow
argument_list|(
name|insertPos
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|insertPos
argument_list|,
name|v
argument_list|)
expr_stmt|;
name|updateNavigationLinks
argument_list|()
expr_stmt|;
block|}
DECL|method|enableDelete ()
name|void
name|enableDelete
parameter_list|()
block|{
for|for
control|(
name|int
name|row
init|=
literal|2
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|CheckBox
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|0
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteChecked ()
name|void
name|deleteChecked
parameter_list|()
block|{
name|deleteButton
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|2
init|;
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|CheckBox
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|0
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|row
operator|--
argument_list|)
expr_stmt|;
block|}
block|}
name|updateNavigationLinks
argument_list|()
expr_stmt|;
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
block|{     }
annotation|@
name|Override
DECL|method|getRowItemKey (List<String> item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

