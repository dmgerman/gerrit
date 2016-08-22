begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|FormatUtil
operator|.
name|mediumFormat
import|;
end_import

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
name|FormatUtil
operator|.
name|name
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
name|GroupApi
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
name|GroupAuditEventInfo
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
name|AccountInfo
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
name|reviewdb
operator|.
name|client
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
DECL|class|AccountGroupAuditLogScreen
specifier|public
class|class
name|AccountGroupAuditLogScreen
extends|extends
name|AccountGroupScreen
block|{
DECL|field|auditEventTable
specifier|private
name|AuditEventTable
name|auditEventTable
decl_stmt|;
DECL|method|AccountGroupAuditLogScreen (GroupInfo toShow, String token)
specifier|public
name|AccountGroupAuditLogScreen
parameter_list|(
name|GroupInfo
name|toShow
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|toShow
argument_list|,
name|token
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
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingAuditLog
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|auditEventTable
operator|=
operator|new
name|AuditEventTable
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|auditEventTable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|display (GroupInfo group, boolean canModify)
specifier|protected
name|void
name|display
parameter_list|(
name|GroupInfo
name|group
parameter_list|,
name|boolean
name|canModify
parameter_list|)
block|{
name|GroupApi
operator|.
name|getAuditLog
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|JsArray
argument_list|<
name|GroupAuditEventInfo
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JsArray
argument_list|<
name|GroupAuditEventInfo
argument_list|>
name|result
parameter_list|)
block|{
name|auditEventTable
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
DECL|class|AuditEventTable
specifier|private
class|class
name|AuditEventTable
extends|extends
name|FancyFlexTable
argument_list|<
name|GroupAuditEventInfo
argument_list|>
block|{
DECL|method|AuditEventTable ()
name|AuditEventTable
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
name|columnDate
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
name|columnType
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
name|columnMember
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
name|columnByUser
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
block|}
DECL|method|display (List<GroupAuditEventInfo> auditEvents)
name|void
name|display
parameter_list|(
name|List
argument_list|<
name|GroupAuditEventInfo
argument_list|>
name|auditEvents
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
name|GroupAuditEventInfo
name|auditEvent
range|:
name|auditEvents
control|)
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
name|populate
argument_list|(
name|row
argument_list|,
name|auditEvent
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populate (int row, GroupAuditEventInfo auditEvent)
name|void
name|populate
parameter_list|(
name|int
name|row
parameter_list|,
name|GroupAuditEventInfo
name|auditEvent
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
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|mediumFormat
argument_list|(
name|auditEvent
operator|.
name|date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|auditEvent
operator|.
name|type
argument_list|()
condition|)
block|{
case|case
name|ADD_USER
case|:
case|case
name|ADD_GROUP
case|:
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|typeAdded
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE_USER
case|:
case|case
name|REMOVE_GROUP
case|:
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|2
argument_list|,
name|Util
operator|.
name|C
operator|.
name|typeRemoved
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
switch|switch
condition|(
name|auditEvent
operator|.
name|type
argument_list|()
condition|)
block|{
case|case
name|ADD_USER
case|:
case|case
name|REMOVE_USER
case|:
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
name|formatAccount
argument_list|(
name|auditEvent
operator|.
name|memberAsUser
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|ADD_GROUP
case|:
case|case
name|REMOVE_GROUP
case|:
name|GroupInfo
name|member
init|=
name|auditEvent
operator|.
name|memberAsGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|AccountGroup
operator|.
name|isInternalGroup
argument_list|(
name|member
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
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
name|Hyperlink
argument_list|(
name|member
operator|.
name|name
argument_list|()
argument_list|,
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|member
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
name|row
argument_list|,
literal|3
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|member
operator|.
name|url
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Anchor
name|a
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
name|a
operator|.
name|setText
argument_list|(
name|member
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|member
operator|.
name|url
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|setTitle
argument_list|(
literal|"UUID "
operator|+
name|member
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|3
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
name|row
argument_list|,
literal|3
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|null
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
literal|3
argument_list|,
name|member
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
name|row
argument_list|,
literal|3
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"UUID "
operator|+
name|member
operator|.
name|getGroupUUID
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|table
operator|.
name|setText
argument_list|(
name|row
argument_list|,
literal|4
argument_list|,
name|formatAccount
argument_list|(
name|auditEvent
operator|.
name|user
argument_list|()
argument_list|)
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
name|setRowItem
argument_list|(
name|row
argument_list|,
name|auditEvent
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|formatAccount (AccountInfo account)
specifier|private
specifier|static
name|String
name|formatAccount
parameter_list|(
name|AccountInfo
name|account
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|name
argument_list|(
name|account
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|account
operator|.
name|_accountId
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

