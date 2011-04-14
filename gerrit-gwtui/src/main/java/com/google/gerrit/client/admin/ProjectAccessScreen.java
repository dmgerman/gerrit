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
name|GerritConfig
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
name|InheritedRefRight
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
name|ProjectDetail
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
name|AccountGroup
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
name|Project
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
name|RefRight
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
name|VerticalPanel
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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

begin_class
DECL|class|ProjectAccessScreen
specifier|public
class|class
name|ProjectAccessScreen
extends|extends
name|ProjectScreen
block|{
DECL|field|parentPanel
specifier|private
name|Panel
name|parentPanel
decl_stmt|;
DECL|field|parentName
specifier|private
name|Hyperlink
name|parentName
decl_stmt|;
DECL|field|rights
specifier|private
name|RightsTable
name|rights
decl_stmt|;
DECL|field|delRight
specifier|private
name|Button
name|delRight
decl_stmt|;
DECL|field|rightEditor
specifier|private
name|AccessRightEditor
name|rightEditor
decl_stmt|;
DECL|field|showInherited
specifier|private
name|CheckBox
name|showInherited
decl_stmt|;
DECL|method|ProjectAccessScreen (final Project.NameKey toShow)
specifier|public
name|ProjectAccessScreen
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|toShow
parameter_list|)
block|{
name|super
argument_list|(
name|toShow
argument_list|)
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
name|initParent
argument_list|()
expr_stmt|;
name|initRights
argument_list|()
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
name|Util
operator|.
name|PROJECT_SVC
operator|.
name|projectDetail
argument_list|(
name|getProjectKey
argument_list|()
argument_list|,
operator|new
name|ScreenLoadCallback
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
specifier|public
name|void
name|preDisplay
parameter_list|(
specifier|final
name|ProjectDetail
name|result
parameter_list|)
block|{
name|enableForm
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
DECL|method|enableForm (final boolean on)
specifier|private
name|void
name|enableForm
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|delRight
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|rightEditor
operator|.
name|enableForm
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|method|initParent ()
specifier|private
name|void
name|initParent
parameter_list|()
block|{
name|parentName
operator|=
operator|new
name|Hyperlink
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|showInherited
operator|=
operator|new
name|CheckBox
argument_list|()
expr_stmt|;
name|showInherited
operator|.
name|setChecked
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|showInherited
operator|.
name|addClickHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|rights
operator|.
name|showInherited
argument_list|(
name|showInherited
operator|.
name|isChecked
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Grid
name|g
init|=
operator|new
name|Grid
argument_list|(
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|g
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingParentProjectName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|g
operator|.
name|setWidget
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|parentName
argument_list|)
expr_stmt|;
name|g
operator|.
name|setWidget
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|showInherited
argument_list|)
expr_stmt|;
name|g
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|headingShowInherited
argument_list|()
argument_list|)
expr_stmt|;
name|parentPanel
operator|=
operator|new
name|VerticalPanel
argument_list|()
expr_stmt|;
name|parentPanel
operator|.
name|add
argument_list|(
name|g
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|parentPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|initRights ()
specifier|private
name|void
name|initRights
parameter_list|()
block|{
name|rights
operator|=
operator|new
name|RightsTable
argument_list|()
expr_stmt|;
name|delRight
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonDeleteGroupMembers
argument_list|()
argument_list|)
expr_stmt|;
name|delRight
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
specifier|final
name|HashSet
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
name|refRightIds
init|=
name|rights
operator|.
name|getRefRightIdsChecked
argument_list|()
decl_stmt|;
name|doDeleteRefRights
argument_list|(
name|refRightIds
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|rightEditor
operator|=
operator|new
name|AccessRightEditor
argument_list|(
name|getProjectKey
argument_list|()
argument_list|)
expr_stmt|;
name|rightEditor
operator|.
name|addValueChangeHandler
argument_list|(
operator|new
name|ValueChangeHandler
argument_list|<
name|ProjectDetail
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
name|ProjectDetail
argument_list|>
name|event
parameter_list|)
block|{
name|display
argument_list|(
name|event
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingAccessRights
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|rights
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|delRight
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|rightEditor
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final ProjectDetail result)
name|void
name|display
parameter_list|(
specifier|final
name|ProjectDetail
name|result
parameter_list|)
block|{
specifier|final
name|Project
name|project
init|=
name|result
operator|.
name|project
decl_stmt|;
specifier|final
name|Project
operator|.
name|NameKey
name|wildKey
init|=
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getWildProject
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|isWild
init|=
name|wildKey
operator|.
name|equals
argument_list|(
name|project
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
name|Project
operator|.
name|NameKey
name|parent
init|=
name|project
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
name|parent
operator|=
name|wildKey
expr_stmt|;
block|}
name|parentPanel
operator|.
name|setVisible
argument_list|(
operator|!
name|isWild
argument_list|)
expr_stmt|;
name|parentName
operator|.
name|setTargetHistoryToken
argument_list|(
name|Dispatcher
operator|.
name|toProjectAdmin
argument_list|(
name|parent
argument_list|,
name|ACCESS
argument_list|)
argument_list|)
expr_stmt|;
name|parentName
operator|.
name|setText
argument_list|(
name|parent
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|rights
operator|.
name|display
argument_list|(
name|result
operator|.
name|groups
argument_list|,
name|result
operator|.
name|rights
argument_list|)
expr_stmt|;
name|rightEditor
operator|.
name|setVisible
argument_list|(
name|result
operator|.
name|canModifyAccess
argument_list|)
expr_stmt|;
name|delRight
operator|.
name|setVisible
argument_list|(
name|rights
operator|.
name|getCanDelete
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doDeleteRefRights (final HashSet<RefRight.Key> refRightIds)
specifier|private
name|void
name|doDeleteRefRights
parameter_list|(
specifier|final
name|HashSet
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
name|refRightIds
parameter_list|)
block|{
if|if
condition|(
operator|!
name|refRightIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Util
operator|.
name|PROJECT_SVC
operator|.
name|deleteRight
argument_list|(
name|getProjectKey
argument_list|()
argument_list|,
name|refRightIds
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|ProjectDetail
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|ProjectDetail
name|result
parameter_list|)
block|{
comment|//The user could no longer modify access after deleting a ref right.
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
block|}
DECL|class|RightsTable
specifier|private
class|class
name|RightsTable
extends|extends
name|FancyFlexTable
argument_list|<
name|InheritedRefRight
argument_list|>
block|{
DECL|field|canDelete
name|boolean
name|canDelete
decl_stmt|;
DECL|field|groups
name|Map
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|AccountGroup
argument_list|>
name|groups
decl_stmt|;
DECL|method|RightsTable ()
name|RightsTable
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
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnRightOrigin
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
name|columnApprovalCategory
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
literal|5
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnRefName
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|6
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnRightRange
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
literal|2
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
literal|3
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
literal|4
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
literal|5
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
literal|6
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
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|onOpenRow
argument_list|(
name|table
operator|.
name|getCellForEvent
argument_list|(
name|event
argument_list|)
operator|.
name|getRowIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getRefRightIdsChecked ()
name|HashSet
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
name|getRefRightIdsChecked
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
name|refRightIds
init|=
operator|new
name|HashSet
argument_list|<
name|RefRight
operator|.
name|Key
argument_list|>
argument_list|()
decl_stmt|;
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
name|RefRight
name|r
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
operator|.
name|getRight
argument_list|()
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
operator|&&
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|)
operator|instanceof
name|CheckBox
operator|&&
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
literal|1
argument_list|)
operator|)
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|refRightIds
operator|.
name|add
argument_list|(
name|r
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|refRightIds
return|;
block|}
DECL|method|display (final Map<AccountGroup.Id, AccountGroup> grps, final List<InheritedRefRight> refRights)
name|void
name|display
parameter_list|(
specifier|final
name|Map
argument_list|<
name|AccountGroup
operator|.
name|Id
argument_list|,
name|AccountGroup
argument_list|>
name|grps
parameter_list|,
specifier|final
name|List
argument_list|<
name|InheritedRefRight
argument_list|>
name|refRights
parameter_list|)
block|{
name|groups
operator|=
name|grps
expr_stmt|;
name|canDelete
operator|=
literal|false
expr_stmt|;
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
name|InheritedRefRight
name|r
range|:
name|refRights
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
if|if
condition|(
operator|!
name|showInherited
operator|.
name|isChecked
argument_list|()
operator|&&
name|r
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|row
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|applyDataRowStyle
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|populate
argument_list|(
name|row
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|row
operator|>
literal|0
condition|)
block|{
name|RefRight
name|right
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
operator|.
name|getRight
argument_list|()
decl_stmt|;
name|rightEditor
operator|.
name|load
argument_list|(
name|right
argument_list|,
name|groups
operator|.
name|get
argument_list|(
name|right
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populate (final int row, final InheritedRefRight r)
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|InheritedRefRight
name|r
parameter_list|)
block|{
specifier|final
name|GerritConfig
name|config
init|=
name|Gerrit
operator|.
name|getConfig
argument_list|()
decl_stmt|;
specifier|final
name|RefRight
name|right
init|=
name|r
operator|.
name|getRight
argument_list|()
decl_stmt|;
specifier|final
name|ApprovalType
name|ar
init|=
name|config
operator|.
name|getApprovalTypes
argument_list|()
operator|.
name|getApprovalType
argument_list|(
name|right
operator|.
name|getApprovalCategoryId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|AccountGroup
name|group
init|=
name|groups
operator|.
name|get
argument_list|(
name|right
operator|.
name|getAccountGroupId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|.
name|isInherited
argument_list|()
operator|||
operator|!
name|r
operator|.
name|isOwner
argument_list|()
condition|)
block|{
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
else|else
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
name|CheckBox
argument_list|()
argument_list|)
expr_stmt|;
name|canDelete
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|r
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|Project
operator|.
name|NameKey
name|fromProject
init|=
name|right
operator|.
name|getKey
argument_list|()
operator|.
name|getProjectNameKey
argument_list|()
decl_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
operator|new
name|Hyperlink
argument_list|(
name|fromProject
operator|.
name|get
argument_list|()
argument_list|,
name|Dispatcher
operator|.
name|toProjectAdmin
argument_list|(
name|fromProject
argument_list|,
name|ACCESS
argument_list|)
argument_list|)
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
literal|""
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
name|ar
operator|!=
literal|null
condition|?
name|ar
operator|.
name|getCategory
argument_list|()
operator|.
name|getName
argument_list|()
else|:
name|right
operator|.
name|getApprovalCategoryId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|group
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
operator|new
name|Hyperlink
argument_list|(
name|group
operator|.
name|getName
argument_list|()
argument_list|,
name|Dispatcher
operator|.
name|toAccountGroup
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
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
literal|4
argument_list|,
name|Util
operator|.
name|M
operator|.
name|deletedGroup
argument_list|(
name|right
operator|.
name|getAccountGroupId
argument_list|()
operator|.
name|get
argument_list|()
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
literal|5
argument_list|,
name|right
operator|.
name|getRefPatternForDisplay
argument_list|()
argument_list|)
expr_stmt|;
block|{
specifier|final
name|SafeHtmlBuilder
name|m
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
specifier|final
name|ApprovalCategoryValue
name|min
decl_stmt|,
name|max
decl_stmt|;
name|min
operator|=
name|ar
operator|!=
literal|null
condition|?
name|ar
operator|.
name|getValue
argument_list|(
name|right
operator|.
name|getMinValue
argument_list|()
argument_list|)
else|:
literal|null
expr_stmt|;
name|max
operator|=
name|ar
operator|!=
literal|null
condition|?
name|ar
operator|.
name|getValue
argument_list|(
name|right
operator|.
name|getMaxValue
argument_list|()
argument_list|)
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|ar
operator|!=
literal|null
operator|&&
name|ar
operator|.
name|getCategory
argument_list|()
operator|.
name|isRange
argument_list|()
condition|)
block|{
name|formatValue
argument_list|(
name|m
argument_list|,
name|right
operator|.
name|getMinValue
argument_list|()
argument_list|,
name|min
argument_list|)
expr_stmt|;
name|m
operator|.
name|br
argument_list|()
expr_stmt|;
block|}
name|formatValue
argument_list|(
name|m
argument_list|,
name|right
operator|.
name|getMaxValue
argument_list|()
argument_list|,
name|max
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
literal|6
argument_list|,
name|m
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
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
literal|3
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
literal|4
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
literal|5
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
literal|6
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
literal|6
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|projectAdminApprovalCategoryRangeLine
argument_list|()
argument_list|)
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|showInherited (boolean visible)
specifier|public
name|void
name|showInherited
parameter_list|(
name|boolean
name|visible
parameter_list|)
block|{
for|for
control|(
name|int
name|r
init|=
literal|0
init|;
name|r
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|;
name|r
operator|++
control|)
block|{
if|if
condition|(
name|getRowItem
argument_list|(
name|r
argument_list|)
operator|!=
literal|null
operator|&&
name|getRowItem
argument_list|(
name|r
argument_list|)
operator|.
name|isInherited
argument_list|()
condition|)
block|{
name|table
operator|.
name|getRowFormatter
argument_list|()
operator|.
name|setVisible
argument_list|(
name|r
argument_list|,
name|visible
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|formatValue (final SafeHtmlBuilder m, final short v, final ApprovalCategoryValue e)
specifier|private
name|void
name|formatValue
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|short
name|v
parameter_list|,
specifier|final
name|ApprovalCategoryValue
name|e
parameter_list|)
block|{
name|m
operator|.
name|openSpan
argument_list|()
expr_stmt|;
name|m
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
name|projectAdminApprovalCategoryValue
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|v
operator|==
literal|0
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|v
operator|>
literal|0
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|'+'
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|append
argument_list|(
name|v
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeSpan
argument_list|()
expr_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCanDelete ()
specifier|private
name|boolean
name|getCanDelete
parameter_list|()
block|{
return|return
name|canDelete
return|;
block|}
block|}
block|}
end_class

end_unit

