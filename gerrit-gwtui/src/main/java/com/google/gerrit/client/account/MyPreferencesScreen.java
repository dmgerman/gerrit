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
import|import static
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
name|AccountGeneralPreferences
operator|.
name|DEFAULT_PAGESIZE
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|PAGESIZE_CHOICES
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
name|AccountGeneralPreferences
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
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
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
name|i18n
operator|.
name|client
operator|.
name|DateTimeFormat
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
name|i18n
operator|.
name|client
operator|.
name|LocaleInfo
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
name|ListBox
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
name|Date
import|;
end_import

begin_class
DECL|class|MyPreferencesScreen
specifier|public
class|class
name|MyPreferencesScreen
extends|extends
name|SettingsScreen
block|{
DECL|field|showSiteHeader
specifier|private
name|CheckBox
name|showSiteHeader
decl_stmt|;
DECL|field|useFlashClipboard
specifier|private
name|CheckBox
name|useFlashClipboard
decl_stmt|;
DECL|field|copySelfOnEmails
specifier|private
name|CheckBox
name|copySelfOnEmails
decl_stmt|;
DECL|field|reversePatchSetOrder
specifier|private
name|CheckBox
name|reversePatchSetOrder
decl_stmt|;
DECL|field|showUsernameInReviewCategory
specifier|private
name|CheckBox
name|showUsernameInReviewCategory
decl_stmt|;
DECL|field|relativeDateInChangeTable
specifier|private
name|CheckBox
name|relativeDateInChangeTable
decl_stmt|;
DECL|field|maximumPageSize
specifier|private
name|ListBox
name|maximumPageSize
decl_stmt|;
DECL|field|dateFormat
specifier|private
name|ListBox
name|dateFormat
decl_stmt|;
DECL|field|timeFormat
specifier|private
name|ListBox
name|timeFormat
decl_stmt|;
DECL|field|commentVisibilityStrategy
specifier|private
name|ListBox
name|commentVisibilityStrategy
decl_stmt|;
DECL|field|diffView
specifier|private
name|ListBox
name|diffView
decl_stmt|;
DECL|field|save
specifier|private
name|Button
name|save
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
name|showSiteHeader
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|showSiteHeader
argument_list|()
argument_list|)
expr_stmt|;
name|useFlashClipboard
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|useFlashClipboard
argument_list|()
argument_list|)
expr_stmt|;
name|copySelfOnEmails
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|copySelfOnEmails
argument_list|()
argument_list|)
expr_stmt|;
name|reversePatchSetOrder
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|reversePatchSetOrder
argument_list|()
argument_list|)
expr_stmt|;
name|showUsernameInReviewCategory
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|showUsernameInReviewCategory
argument_list|()
argument_list|)
expr_stmt|;
name|maximumPageSize
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|short
name|v
range|:
name|PAGESIZE_CHOICES
control|)
block|{
name|maximumPageSize
operator|.
name|addItem
argument_list|(
name|Util
operator|.
name|M
operator|.
name|rowsPerPage
argument_list|(
name|v
argument_list|)
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|commentVisibilityStrategy
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
name|commentVisibilityStrategy
operator|.
name|addItem
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
name|C
operator|.
name|messageCollapseAll
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
operator|.
name|COLLAPSE_ALL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|commentVisibilityStrategy
operator|.
name|addItem
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
name|C
operator|.
name|messageExpandMostRecent
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
operator|.
name|EXPAND_MOST_RECENT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|commentVisibilityStrategy
operator|.
name|addItem
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
name|C
operator|.
name|messageExpandRecent
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
operator|.
name|EXPAND_RECENT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|commentVisibilityStrategy
operator|.
name|addItem
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
name|C
operator|.
name|messageExpandAll
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
operator|.
name|EXPAND_ALL
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|diffView
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
name|diffView
operator|.
name|addItem
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
name|C
operator|.
name|sideBySide
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DiffView
operator|.
name|SIDE_BY_SIDE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|diffView
operator|.
name|addItem
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
name|C
operator|.
name|unifiedDiff
argument_list|()
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DiffView
operator|.
name|UNIFIED_DIFF
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|Date
name|now
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|dateFormat
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
for|for
control|(
name|AccountGeneralPreferences
operator|.
name|DateFormat
name|fmt
range|:
name|AccountGeneralPreferences
operator|.
name|DateFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt
operator|.
name|getShortFormat
argument_list|()
argument_list|)
operator|.
name|format
argument_list|(
name|now
argument_list|)
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
literal|" ; "
argument_list|)
expr_stmt|;
name|r
operator|.
name|append
argument_list|(
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt
operator|.
name|getLongFormat
argument_list|()
argument_list|)
operator|.
name|format
argument_list|(
name|now
argument_list|)
argument_list|)
expr_stmt|;
name|dateFormat
operator|.
name|addItem
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|,
name|fmt
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|timeFormat
operator|=
operator|new
name|ListBox
argument_list|()
expr_stmt|;
for|for
control|(
name|AccountGeneralPreferences
operator|.
name|TimeFormat
name|fmt
range|:
name|AccountGeneralPreferences
operator|.
name|TimeFormat
operator|.
name|values
argument_list|()
control|)
block|{
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|DateTimeFormat
operator|.
name|getFormat
argument_list|(
name|fmt
operator|.
name|getFormat
argument_list|()
argument_list|)
operator|.
name|format
argument_list|(
name|now
argument_list|)
argument_list|)
expr_stmt|;
name|timeFormat
operator|.
name|addItem
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|,
name|fmt
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|FlowPanel
name|dateTimePanel
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
specifier|final
name|int
name|labelIdx
decl_stmt|,
name|fieldIdx
decl_stmt|;
if|if
condition|(
name|LocaleInfo
operator|.
name|getCurrentLocale
argument_list|()
operator|.
name|isRTL
argument_list|()
condition|)
block|{
name|labelIdx
operator|=
literal|1
expr_stmt|;
name|fieldIdx
operator|=
literal|0
expr_stmt|;
name|dateTimePanel
operator|.
name|add
argument_list|(
name|timeFormat
argument_list|)
expr_stmt|;
name|dateTimePanel
operator|.
name|add
argument_list|(
name|dateFormat
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|labelIdx
operator|=
literal|0
expr_stmt|;
name|fieldIdx
operator|=
literal|1
expr_stmt|;
name|dateTimePanel
operator|.
name|add
argument_list|(
name|dateFormat
argument_list|)
expr_stmt|;
name|dateTimePanel
operator|.
name|add
argument_list|(
name|timeFormat
argument_list|)
expr_stmt|;
block|}
name|relativeDateInChangeTable
operator|=
operator|new
name|CheckBox
argument_list|(
name|Util
operator|.
name|C
operator|.
name|showRelativeDateInChangeTable
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Grid
name|formGrid
init|=
operator|new
name|Grid
argument_list|(
literal|10
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|int
name|row
init|=
literal|0
decl_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|showSiteHeader
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|useFlashClipboard
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|copySelfOnEmails
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|reversePatchSetOrder
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|showUsernameInReviewCategory
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
name|Util
operator|.
name|C
operator|.
name|maximumPageSizeFieldLabel
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|maximumPageSize
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
name|Util
operator|.
name|C
operator|.
name|dateFormatLabel
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|dateTimePanel
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|relativeDateInChangeTable
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
name|Util
operator|.
name|C
operator|.
name|commentVisibilityLabel
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|commentVisibilityStrategy
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
name|row
argument_list|,
name|labelIdx
argument_list|,
name|Util
operator|.
name|C
operator|.
name|diffViewLabel
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|fieldIdx
argument_list|,
name|diffView
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
name|add
argument_list|(
name|formGrid
argument_list|)
expr_stmt|;
name|save
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSaveChanges
argument_list|()
argument_list|)
expr_stmt|;
name|save
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|save
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
name|doSave
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|save
argument_list|)
expr_stmt|;
specifier|final
name|OnEditEnabler
name|e
init|=
operator|new
name|OnEditEnabler
argument_list|(
name|save
argument_list|)
decl_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|showSiteHeader
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|useFlashClipboard
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|copySelfOnEmails
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|reversePatchSetOrder
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|showUsernameInReviewCategory
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|maximumPageSize
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|dateFormat
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|timeFormat
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|relativeDateInChangeTable
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|commentVisibilityStrategy
argument_list|)
expr_stmt|;
name|e
operator|.
name|listenTo
argument_list|(
name|diffView
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
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|myAccount
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|Account
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
name|Account
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|result
operator|.
name|getGeneralPreferences
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|enable (final boolean on)
specifier|private
name|void
name|enable
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|showSiteHeader
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|useFlashClipboard
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|copySelfOnEmails
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|reversePatchSetOrder
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|showUsernameInReviewCategory
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|maximumPageSize
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|dateFormat
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|timeFormat
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|relativeDateInChangeTable
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|commentVisibilityStrategy
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|diffView
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final AccountGeneralPreferences p)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|AccountGeneralPreferences
name|p
parameter_list|)
block|{
name|showSiteHeader
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isShowSiteHeader
argument_list|()
argument_list|)
expr_stmt|;
name|useFlashClipboard
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isUseFlashClipboard
argument_list|()
argument_list|)
expr_stmt|;
name|copySelfOnEmails
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isCopySelfOnEmails
argument_list|()
argument_list|)
expr_stmt|;
name|reversePatchSetOrder
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isReversePatchSetOrder
argument_list|()
argument_list|)
expr_stmt|;
name|showUsernameInReviewCategory
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isShowUsernameInReviewCategory
argument_list|()
argument_list|)
expr_stmt|;
name|setListBox
argument_list|(
name|maximumPageSize
argument_list|,
name|DEFAULT_PAGESIZE
argument_list|,
name|p
operator|.
name|getMaximumPageSize
argument_list|()
argument_list|)
expr_stmt|;
name|setListBox
argument_list|(
name|dateFormat
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DateFormat
operator|.
name|STD
argument_list|,
comment|//
name|p
operator|.
name|getDateFormat
argument_list|()
argument_list|)
expr_stmt|;
name|setListBox
argument_list|(
name|timeFormat
argument_list|,
name|AccountGeneralPreferences
operator|.
name|TimeFormat
operator|.
name|HHMM_12
argument_list|,
comment|//
name|p
operator|.
name|getTimeFormat
argument_list|()
argument_list|)
expr_stmt|;
name|relativeDateInChangeTable
operator|.
name|setValue
argument_list|(
name|p
operator|.
name|isRelativeDateInChangeTable
argument_list|()
argument_list|)
expr_stmt|;
name|setListBox
argument_list|(
name|commentVisibilityStrategy
argument_list|,
name|AccountGeneralPreferences
operator|.
name|CommentVisibilityStrategy
operator|.
name|EXPAND_RECENT
argument_list|,
name|p
operator|.
name|getCommentVisibilityStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|setListBox
argument_list|(
name|diffView
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DiffView
operator|.
name|SIDE_BY_SIDE
argument_list|,
name|p
operator|.
name|getDiffView
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setListBox (final ListBox f, final short defaultValue, final short currentValue)
specifier|private
name|void
name|setListBox
parameter_list|(
specifier|final
name|ListBox
name|f
parameter_list|,
specifier|final
name|short
name|defaultValue
parameter_list|,
specifier|final
name|short
name|currentValue
parameter_list|)
block|{
name|setListBox
argument_list|(
name|f
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|defaultValue
argument_list|)
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|currentValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setListBox (final ListBox f, final T defaultValue, final T currentValue)
specifier|private
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|void
name|setListBox
parameter_list|(
specifier|final
name|ListBox
name|f
parameter_list|,
specifier|final
name|T
name|defaultValue
parameter_list|,
specifier|final
name|T
name|currentValue
parameter_list|)
block|{
name|setListBox
argument_list|(
name|f
argument_list|,
name|defaultValue
operator|.
name|name
argument_list|()
argument_list|,
comment|//
name|currentValue
operator|!=
literal|null
condition|?
name|currentValue
operator|.
name|name
argument_list|()
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|setListBox (final ListBox f, final String defaultValue, final String currentValue)
specifier|private
name|void
name|setListBox
parameter_list|(
specifier|final
name|ListBox
name|f
parameter_list|,
specifier|final
name|String
name|defaultValue
parameter_list|,
specifier|final
name|String
name|currentValue
parameter_list|)
block|{
specifier|final
name|int
name|n
init|=
name|f
operator|.
name|getItemCount
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
name|n
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|f
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
operator|.
name|equals
argument_list|(
name|currentValue
argument_list|)
condition|)
block|{
name|f
operator|.
name|setSelectedIndex
argument_list|(
name|i
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
if|if
condition|(
name|currentValue
operator|!=
name|defaultValue
condition|)
block|{
name|setListBox
argument_list|(
name|f
argument_list|,
name|defaultValue
argument_list|,
name|defaultValue
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getListBox (final ListBox f, final short defaultValue)
specifier|private
name|short
name|getListBox
parameter_list|(
specifier|final
name|ListBox
name|f
parameter_list|,
specifier|final
name|short
name|defaultValue
parameter_list|)
block|{
specifier|final
name|int
name|idx
init|=
name|f
operator|.
name|getSelectedIndex
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|idx
condition|)
block|{
return|return
name|Short
operator|.
name|parseShort
argument_list|(
name|f
operator|.
name|getValue
argument_list|(
name|idx
argument_list|)
argument_list|)
return|;
block|}
return|return
name|defaultValue
return|;
block|}
DECL|method|getListBox (final ListBox f, final T defaultValue, T[] all)
specifier|private
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|?
argument_list|>
parameter_list|>
name|T
name|getListBox
parameter_list|(
specifier|final
name|ListBox
name|f
parameter_list|,
specifier|final
name|T
name|defaultValue
parameter_list|,
name|T
index|[]
name|all
parameter_list|)
block|{
specifier|final
name|int
name|idx
init|=
name|f
operator|.
name|getSelectedIndex
argument_list|()
decl_stmt|;
if|if
condition|(
literal|0
operator|<=
name|idx
condition|)
block|{
name|String
name|v
init|=
name|f
operator|.
name|getValue
argument_list|(
name|idx
argument_list|)
decl_stmt|;
for|for
control|(
name|T
name|t
range|:
name|all
control|)
block|{
if|if
condition|(
name|t
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|v
argument_list|)
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
block|}
return|return
name|defaultValue
return|;
block|}
DECL|method|doSave ()
specifier|private
name|void
name|doSave
parameter_list|()
block|{
specifier|final
name|AccountGeneralPreferences
name|p
init|=
operator|new
name|AccountGeneralPreferences
argument_list|()
decl_stmt|;
name|p
operator|.
name|setShowSiteHeader
argument_list|(
name|showSiteHeader
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setUseFlashClipboard
argument_list|(
name|useFlashClipboard
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setCopySelfOnEmails
argument_list|(
name|copySelfOnEmails
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setReversePatchSetOrder
argument_list|(
name|reversePatchSetOrder
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setShowUsernameInReviewCategory
argument_list|(
name|showUsernameInReviewCategory
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setMaximumPageSize
argument_list|(
name|getListBox
argument_list|(
name|maximumPageSize
argument_list|,
name|DEFAULT_PAGESIZE
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDateFormat
argument_list|(
name|getListBox
argument_list|(
name|dateFormat
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DateFormat
operator|.
name|STD
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DateFormat
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setTimeFormat
argument_list|(
name|getListBox
argument_list|(
name|timeFormat
argument_list|,
name|AccountGeneralPreferences
operator|.
name|TimeFormat
operator|.
name|HHMM_12
argument_list|,
name|AccountGeneralPreferences
operator|.
name|TimeFormat
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setRelativeDateInChangeTable
argument_list|(
name|relativeDateInChangeTable
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|setCommentVisibilityStrategy
argument_list|(
name|getListBox
argument_list|(
name|commentVisibilityStrategy
argument_list|,
name|CommentVisibilityStrategy
operator|.
name|EXPAND_RECENT
argument_list|,
name|CommentVisibilityStrategy
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|p
operator|.
name|setDiffView
argument_list|(
name|getListBox
argument_list|(
name|diffView
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DiffView
operator|.
name|SIDE_BY_SIDE
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DiffView
operator|.
name|values
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|enable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|save
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
name|changePreferences
argument_list|(
name|p
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
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|setGeneralPreferences
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|Gerrit
operator|.
name|applyUserPreferences
argument_list|()
expr_stmt|;
name|enable
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
name|enable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|save
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
block|}
end_class

end_unit

