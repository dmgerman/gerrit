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
name|gerrit
operator|.
name|client
operator|.
name|ui
operator|.
name|ProjectLink
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
name|AccountProjectWatchInfo
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
name|AccountProjectWatch
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
name|Label
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|common
operator|.
name|VoidResult
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
name|Set
import|;
end_import

begin_class
DECL|class|MyWatchesTable
specifier|public
class|class
name|MyWatchesTable
extends|extends
name|FancyFlexTable
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
block|{
DECL|method|MyWatchesTable ()
specifier|public
name|MyWatchesTable
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
name|insertRow
argument_list|(
literal|1
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
name|watchedProjectName
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
name|watchedProjectColumnEmailNotifications
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
name|setRowSpan
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setRowSpan
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setRowSpan
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|)
operator|.
name|setPropertyString
argument_list|(
literal|"align"
argument_list|,
literal|"center"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setColSpan
argument_list|(
literal|0
argument_list|,
literal|3
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|watchedProjectColumnNewChanges
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|Util
operator|.
name|C
operator|.
name|watchedProjectColumnNewPatchSets
argument_list|()
argument_list|)
expr_stmt|;
name|table
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
name|watchedProjectColumnAllComments
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|watchedProjectColumnSubmittedChanges
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|4
argument_list|,
name|Util
operator|.
name|C
operator|.
name|watchedProjectColumnAbandonedChanges
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
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|1
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
name|dataHeader
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
literal|1
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
literal|1
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
literal|1
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
block|}
DECL|method|deleteChecked ()
specifier|public
name|void
name|deleteChecked
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|AccountProjectWatch
operator|.
name|Key
argument_list|>
name|ids
init|=
name|getCheckedIds
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|deleteProjectWatches
argument_list|(
name|ids
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
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
name|VoidResult
name|result
parameter_list|)
block|{
name|remove
argument_list|(
name|ids
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|remove (Set<AccountProjectWatch.Key> ids)
specifier|protected
name|void
name|remove
parameter_list|(
name|Set
argument_list|<
name|AccountProjectWatch
operator|.
name|Key
argument_list|>
name|ids
parameter_list|)
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
control|)
block|{
specifier|final
name|AccountProjectWatchInfo
name|k
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
operator|&&
name|ids
operator|.
name|contains
argument_list|(
name|k
operator|.
name|getWatch
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|table
operator|.
name|removeRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|row
operator|++
expr_stmt|;
block|}
block|}
block|}
DECL|method|getCheckedIds ()
specifier|protected
name|Set
argument_list|<
name|AccountProjectWatch
operator|.
name|Key
argument_list|>
name|getCheckedIds
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|AccountProjectWatch
operator|.
name|Key
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<>
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
specifier|final
name|AccountProjectWatchInfo
name|k
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|k
operator|!=
literal|null
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
name|ids
operator|.
name|add
argument_list|(
name|k
operator|.
name|getWatch
argument_list|()
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ids
return|;
block|}
DECL|method|insertWatch (final AccountProjectWatchInfo k)
specifier|public
name|void
name|insertWatch
parameter_list|(
specifier|final
name|AccountProjectWatchInfo
name|k
parameter_list|)
block|{
specifier|final
name|String
name|newName
init|=
name|k
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|int
name|row
init|=
literal|1
decl_stmt|;
for|for
control|(
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
specifier|final
name|AccountProjectWatchInfo
name|i
init|=
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|i
operator|!=
literal|null
operator|&&
name|i
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|newName
argument_list|)
operator|>=
literal|0
condition|)
block|{
break|break;
block|}
block|}
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
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final List<AccountProjectWatchInfo> result)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountProjectWatchInfo
argument_list|>
name|result
parameter_list|)
block|{
while|while
condition|(
literal|2
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
name|AccountProjectWatchInfo
name|k
range|:
name|result
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
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populate (final int row, final AccountProjectWatchInfo info)
specifier|protected
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|AccountProjectWatchInfo
name|info
parameter_list|)
block|{
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
operator|new
name|ProjectLink
argument_list|(
name|info
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Label
name|filter
init|=
operator|new
name|Label
argument_list|(
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|getFilter
argument_list|()
argument_list|)
decl_stmt|;
name|filter
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
name|watchedProjectFilter
argument_list|()
argument_list|)
expr_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
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
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|fp
argument_list|)
expr_stmt|;
name|addNotifyButton
argument_list|(
name|AccountProjectWatch
operator|.
name|NotifyType
operator|.
name|NEW_CHANGES
argument_list|,
name|info
argument_list|,
name|row
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|addNotifyButton
argument_list|(
name|AccountProjectWatch
operator|.
name|NotifyType
operator|.
name|NEW_PATCHSETS
argument_list|,
name|info
argument_list|,
name|row
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|addNotifyButton
argument_list|(
name|AccountProjectWatch
operator|.
name|NotifyType
operator|.
name|ALL_COMMENTS
argument_list|,
name|info
argument_list|,
name|row
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|addNotifyButton
argument_list|(
name|AccountProjectWatch
operator|.
name|NotifyType
operator|.
name|SUBMITTED_CHANGES
argument_list|,
name|info
argument_list|,
name|row
argument_list|,
literal|6
argument_list|)
expr_stmt|;
name|addNotifyButton
argument_list|(
name|AccountProjectWatch
operator|.
name|NotifyType
operator|.
name|ABANDONED_CHANGES
argument_list|,
name|info
argument_list|,
name|row
argument_list|,
literal|7
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
literal|7
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
name|setRowItem
argument_list|(
name|row
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
DECL|method|addNotifyButton (final AccountProjectWatch.NotifyType type, final AccountProjectWatchInfo info, final int row, final int col)
specifier|protected
name|void
name|addNotifyButton
parameter_list|(
specifier|final
name|AccountProjectWatch
operator|.
name|NotifyType
name|type
parameter_list|,
specifier|final
name|AccountProjectWatchInfo
name|info
parameter_list|,
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|int
name|col
parameter_list|)
block|{
specifier|final
name|CheckBox
name|cbox
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|cbox
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
name|boolean
name|oldVal
init|=
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|isNotify
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|setNotify
argument_list|(
name|type
argument_list|,
name|cbox
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|cbox
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|updateProjectWatch
argument_list|(
name|info
operator|.
name|getWatch
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
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
name|VoidResult
name|result
parameter_list|)
block|{
name|cbox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
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
name|cbox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|setNotify
argument_list|(
name|type
argument_list|,
name|oldVal
argument_list|)
expr_stmt|;
name|cbox
operator|.
name|setValue
argument_list|(
name|oldVal
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
block|}
argument_list|)
expr_stmt|;
name|cbox
operator|.
name|setValue
argument_list|(
name|info
operator|.
name|getWatch
argument_list|()
operator|.
name|isNotify
argument_list|(
name|type
argument_list|)
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
name|cbox
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

