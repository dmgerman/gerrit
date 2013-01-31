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
name|MemberInfo
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
name|AccountGroupSuggestOracle
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
name|AccountLink
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
name|AddMemberBox
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
name|common
operator|.
name|data
operator|.
name|GroupDetail
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
name|common
operator|.
name|data
operator|.
name|GroupInfoCache
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroupIncludeByUuid
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
name|AccountGroupMember
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
name|Panel
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

begin_class
DECL|class|AccountGroupMembersScreen
specifier|public
class|class
name|AccountGroupMembersScreen
extends|extends
name|AccountGroupScreen
block|{
DECL|field|accounts
specifier|private
name|AccountInfoCache
name|accounts
init|=
name|AccountInfoCache
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|groups
specifier|private
name|GroupInfoCache
name|groups
init|=
name|GroupInfoCache
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|members
specifier|private
name|MemberTable
name|members
decl_stmt|;
DECL|field|includes
specifier|private
name|IncludeTable
name|includes
decl_stmt|;
DECL|field|memberPanel
specifier|private
name|Panel
name|memberPanel
decl_stmt|;
DECL|field|addMemberBox
specifier|private
name|AddMemberBox
name|addMemberBox
decl_stmt|;
DECL|field|delMember
specifier|private
name|Button
name|delMember
decl_stmt|;
DECL|field|includePanel
specifier|private
name|Panel
name|includePanel
decl_stmt|;
DECL|field|addIncludeBox
specifier|private
name|AddMemberBox
name|addIncludeBox
decl_stmt|;
DECL|field|delInclude
specifier|private
name|Button
name|delInclude
decl_stmt|;
DECL|field|noMembersInfo
specifier|private
name|FlowPanel
name|noMembersInfo
decl_stmt|;
DECL|method|AccountGroupMembersScreen (final GroupDetail toShow, final String token)
specifier|public
name|AccountGroupMembersScreen
parameter_list|(
specifier|final
name|GroupDetail
name|toShow
parameter_list|,
specifier|final
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
name|initMemberList
argument_list|()
expr_stmt|;
name|initIncludeList
argument_list|()
expr_stmt|;
name|initNoMembersInfo
argument_list|()
expr_stmt|;
block|}
DECL|method|enableForm (final boolean canModify)
specifier|private
name|void
name|enableForm
parameter_list|(
specifier|final
name|boolean
name|canModify
parameter_list|)
block|{
name|addMemberBox
operator|.
name|setEnabled
argument_list|(
name|canModify
argument_list|)
expr_stmt|;
name|members
operator|.
name|setEnabled
argument_list|(
name|canModify
argument_list|)
expr_stmt|;
name|addIncludeBox
operator|.
name|setEnabled
argument_list|(
name|canModify
argument_list|)
expr_stmt|;
name|includes
operator|.
name|setEnabled
argument_list|(
name|canModify
argument_list|)
expr_stmt|;
block|}
DECL|method|initMemberList ()
specifier|private
name|void
name|initMemberList
parameter_list|()
block|{
name|addMemberBox
operator|=
operator|new
name|AddMemberBox
argument_list|()
expr_stmt|;
name|addMemberBox
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
name|doAddNewMember
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|members
operator|=
operator|new
name|MemberTable
argument_list|()
expr_stmt|;
name|members
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|groupMembersTable
argument_list|()
argument_list|)
expr_stmt|;
name|delMember
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
name|delMember
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
name|members
operator|.
name|deleteChecked
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|memberPanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|memberPanel
operator|.
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingMembers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|memberPanel
operator|.
name|add
argument_list|(
name|addMemberBox
argument_list|)
expr_stmt|;
name|memberPanel
operator|.
name|add
argument_list|(
name|members
argument_list|)
expr_stmt|;
name|memberPanel
operator|.
name|add
argument_list|(
name|delMember
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|memberPanel
argument_list|)
expr_stmt|;
block|}
DECL|method|initIncludeList ()
specifier|private
name|void
name|initIncludeList
parameter_list|()
block|{
specifier|final
name|AccountGroupSuggestOracle
name|oracle
init|=
operator|new
name|AccountGroupSuggestOracle
argument_list|()
decl_stmt|;
name|addIncludeBox
operator|=
operator|new
name|AddMemberBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonAddIncludedGroup
argument_list|()
argument_list|,
name|Util
operator|.
name|C
operator|.
name|defaultAccountGroupName
argument_list|()
argument_list|,
name|oracle
argument_list|)
expr_stmt|;
name|addIncludeBox
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
name|doAddNewInclude
argument_list|(
name|oracle
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|includes
operator|=
operator|new
name|IncludeTable
argument_list|()
expr_stmt|;
name|includes
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|groupIncludesTable
argument_list|()
argument_list|)
expr_stmt|;
name|delInclude
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonDeleteIncludedGroup
argument_list|()
argument_list|)
expr_stmt|;
name|delInclude
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
name|includes
operator|.
name|deleteChecked
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|includePanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|includePanel
operator|.
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|headingIncludedGroups
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|includePanel
operator|.
name|add
argument_list|(
name|addIncludeBox
argument_list|)
expr_stmt|;
name|includePanel
operator|.
name|add
argument_list|(
name|includes
argument_list|)
expr_stmt|;
name|includePanel
operator|.
name|add
argument_list|(
name|delInclude
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|includePanel
argument_list|)
expr_stmt|;
block|}
DECL|method|initNoMembersInfo ()
specifier|private
name|void
name|initNoMembersInfo
parameter_list|()
block|{
name|noMembersInfo
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|noMembersInfo
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|noMembersInfo
operator|.
name|add
argument_list|(
operator|new
name|SmallHeading
argument_list|(
name|Util
operator|.
name|C
operator|.
name|noMembersInfo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|noMembersInfo
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|display (final GroupDetail groupDetail)
specifier|protected
name|void
name|display
parameter_list|(
specifier|final
name|GroupDetail
name|groupDetail
parameter_list|)
block|{
switch|switch
condition|(
name|groupDetail
operator|.
name|group
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|INTERNAL
case|:
name|accounts
operator|=
name|groupDetail
operator|.
name|accounts
expr_stmt|;
name|groups
operator|=
name|groupDetail
operator|.
name|groups
expr_stmt|;
name|members
operator|.
name|display
argument_list|(
name|groupDetail
operator|.
name|members
argument_list|)
expr_stmt|;
name|includes
operator|.
name|display
argument_list|(
name|groupDetail
operator|.
name|includes
argument_list|)
expr_stmt|;
break|break;
default|default:
name|memberPanel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|includePanel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|noMembersInfo
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
break|break;
block|}
name|enableForm
argument_list|(
name|groupDetail
operator|.
name|canModify
argument_list|)
expr_stmt|;
name|delMember
operator|.
name|setVisible
argument_list|(
name|groupDetail
operator|.
name|canModify
argument_list|)
expr_stmt|;
name|delInclude
operator|.
name|setVisible
argument_list|(
name|groupDetail
operator|.
name|canModify
argument_list|)
expr_stmt|;
block|}
DECL|method|doAddNewMember ()
name|void
name|doAddNewMember
parameter_list|()
block|{
specifier|final
name|String
name|nameEmail
init|=
name|addMemberBox
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|nameEmail
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|addMemberBox
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|GroupApi
operator|.
name|addMember
argument_list|(
name|getGroupUUID
argument_list|()
argument_list|,
name|nameEmail
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|MemberInfo
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|MemberInfo
name|memberInfo
parameter_list|)
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|getGroupUUID
argument_list|()
argument_list|,
name|AccountGroupScreen
operator|.
name|MEMBERS
argument_list|)
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
name|addMemberBox
operator|.
name|setEnabled
argument_list|(
literal|true
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
DECL|method|doAddNewInclude (final AccountGroupSuggestOracle oracle)
name|void
name|doAddNewInclude
parameter_list|(
specifier|final
name|AccountGroupSuggestOracle
name|oracle
parameter_list|)
block|{
specifier|final
name|String
name|groupName
init|=
name|addIncludeBox
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|addIncludeBox
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Util
operator|.
name|GROUP_SVC
operator|.
name|addGroupInclude
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|oracle
operator|.
name|getUUID
argument_list|(
name|groupName
argument_list|)
argument_list|,
name|groupName
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|GroupDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|GroupDetail
name|result
parameter_list|)
block|{
name|addIncludeBox
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addIncludeBox
operator|.
name|setText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|groups
operator|!=
literal|null
operator|&&
name|result
operator|.
name|includes
operator|!=
literal|null
condition|)
block|{
name|groups
operator|.
name|merge
argument_list|(
name|result
operator|.
name|groups
argument_list|)
expr_stmt|;
name|includes
operator|.
name|display
argument_list|(
name|result
operator|.
name|includes
argument_list|)
expr_stmt|;
block|}
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
name|addIncludeBox
operator|.
name|setEnabled
argument_list|(
literal|true
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
DECL|class|MemberTable
specifier|private
class|class
name|MemberTable
extends|extends
name|FancyFlexTable
argument_list|<
name|AccountGroupMember
argument_list|>
block|{
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|MemberTable ()
name|MemberTable
parameter_list|()
block|{
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
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnEmailAddress
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
block|}
DECL|method|setEnabled (final boolean enabled)
name|void
name|setEnabled
parameter_list|(
specifier|final
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
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
name|AccountGroupMember
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
condition|)
block|{
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
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|deleteChecked ()
name|void
name|deleteChecked
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|ids
init|=
operator|new
name|HashSet
argument_list|<
name|Account
operator|.
name|Id
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
specifier|final
name|AccountGroupMember
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
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|ids
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|GroupApi
operator|.
name|removeMembers
argument_list|(
name|getGroupUUID
argument_list|()
argument_list|,
name|ids
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|VoidResult
name|result
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
name|AccountGroupMember
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
name|getAccountId
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final List<AccountGroupMember> result)
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountGroupMember
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
name|AccountGroupMember
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
DECL|method|populate (final int row, final AccountGroupMember k)
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|AccountGroupMember
name|k
parameter_list|)
block|{
specifier|final
name|Account
operator|.
name|Id
name|accountId
init|=
name|k
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|CheckBox
name|checkBox
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|checkBox
argument_list|)
expr_stmt|;
name|checkBox
operator|.
name|setEnabled
argument_list|(
name|enabled
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
name|AccountLink
operator|.
name|link
argument_list|(
name|accounts
argument_list|,
name|accountId
argument_list|)
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
name|accounts
operator|.
name|get
argument_list|(
name|accountId
argument_list|)
operator|.
name|getPreferredEmail
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
name|setRowItem
argument_list|(
name|row
argument_list|,
name|k
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|IncludeTable
specifier|private
class|class
name|IncludeTable
extends|extends
name|FancyFlexTable
argument_list|<
name|AccountGroupIncludeByUuid
argument_list|>
block|{
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|IncludeTable ()
name|IncludeTable
parameter_list|()
block|{
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
literal|3
argument_list|,
name|Util
operator|.
name|C
operator|.
name|columnGroupDescription
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
block|}
DECL|method|setEnabled (final boolean enabled)
name|void
name|setEnabled
parameter_list|(
specifier|final
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
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
name|AccountGroupIncludeByUuid
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
condition|)
block|{
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
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|deleteChecked ()
name|void
name|deleteChecked
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|AccountGroupIncludeByUuid
operator|.
name|Key
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<
name|AccountGroupIncludeByUuid
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
specifier|final
name|AccountGroupIncludeByUuid
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
name|keys
operator|.
name|add
argument_list|(
name|k
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|keys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Util
operator|.
name|GROUP_SVC
operator|.
name|deleteGroupIncludes
argument_list|(
name|getGroupId
argument_list|()
argument_list|,
name|keys
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|VoidResult
name|result
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
name|AccountGroupIncludeByUuid
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
name|keys
operator|.
name|contains
argument_list|(
name|k
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
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final List<AccountGroupIncludeByUuid> result)
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|AccountGroupIncludeByUuid
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
name|AccountGroupIncludeByUuid
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
DECL|method|populate (final int row, final AccountGroupIncludeByUuid k)
name|void
name|populate
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|AccountGroupIncludeByUuid
name|k
parameter_list|)
block|{
specifier|final
name|FlexCellFormatter
name|fmt
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
decl_stmt|;
name|AccountGroup
operator|.
name|UUID
name|uuid
init|=
name|k
operator|.
name|getIncludeUUID
argument_list|()
decl_stmt|;
name|GroupInfo
name|info
init|=
name|groups
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
name|CheckBox
name|checkBox
init|=
operator|new
name|CheckBox
argument_list|()
decl_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
literal|1
argument_list|,
name|checkBox
argument_list|)
expr_stmt|;
name|checkBox
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
if|if
condition|(
name|AccountGroup
operator|.
name|isInternalGroup
argument_list|(
name|uuid
argument_list|)
condition|)
block|{
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
name|info
operator|.
name|getName
argument_list|()
argument_list|,
name|Dispatcher
operator|.
name|toGroup
argument_list|(
name|uuid
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
literal|2
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|null
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
name|info
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|info
operator|.
name|getUrl
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
name|info
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|setHref
argument_list|(
name|info
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|a
operator|.
name|setTitle
argument_list|(
literal|"UUID "
operator|+
name|uuid
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
literal|2
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
literal|2
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
literal|2
argument_list|,
name|info
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
name|row
argument_list|,
literal|2
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"UUID "
operator|+
name|uuid
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
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

