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
DECL|package|com.google.gerrit.client.patches
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|patches
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
name|reviewdb
operator|.
name|AccountGeneralPreferences
operator|.
name|DEFAULT_CONTEXT
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
name|reviewdb
operator|.
name|AccountGeneralPreferences
operator|.
name|WHOLE_FILE_CONTEXT
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
name|data
operator|.
name|PatchScriptSettings
operator|.
name|Whitespace
operator|.
name|IGNORE_NONE
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
name|data
operator|.
name|PatchScriptSettings
operator|.
name|Whitespace
operator|.
name|IGNORE_SPACE_CHANGE
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
name|Link
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
name|changes
operator|.
name|PatchTable
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
name|changes
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
name|client
operator|.
name|data
operator|.
name|PatchScript
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
name|data
operator|.
name|PatchScriptSettings
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
name|reviewdb
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
name|client
operator|.
name|reviewdb
operator|.
name|Change
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
name|reviewdb
operator|.
name|Patch
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
name|reviewdb
operator|.
name|PatchSet
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
name|NoDifferencesException
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
name|ChangeLink
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
name|Screen
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
name|DisclosurePanel
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
name|HasHorizontalAlignment
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Widget
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
name|gwtjsonrpc
operator|.
name|client
operator|.
name|RemoteJsonException
import|;
end_import

begin_class
DECL|class|PatchScreen
specifier|public
specifier|abstract
class|class
name|PatchScreen
extends|extends
name|Screen
block|{
DECL|class|SideBySide
specifier|public
specifier|static
class|class
name|SideBySide
extends|extends
name|PatchScreen
block|{
DECL|method|SideBySide (final Patch.Key id, final int patchIndex, final PatchTable patchTable)
specifier|public
name|SideBySide
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|PatchTable
name|patchTable
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|patchIndex
argument_list|,
name|patchTable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createContentTable ()
specifier|protected
name|SideBySideTable
name|createContentTable
parameter_list|()
block|{
return|return
operator|new
name|SideBySideTable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getPatchScreenType ()
specifier|protected
name|PatchScreen
operator|.
name|Type
name|getPatchScreenType
parameter_list|()
block|{
return|return
name|PatchScreen
operator|.
name|Type
operator|.
name|SIDE_BY_SIDE
return|;
block|}
block|}
DECL|class|Unified
specifier|public
specifier|static
class|class
name|Unified
extends|extends
name|PatchScreen
block|{
DECL|method|Unified (final Patch.Key id, final int patchIndex, final PatchTable patchTable)
specifier|public
name|Unified
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|PatchTable
name|patchTable
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|patchIndex
argument_list|,
name|patchTable
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createContentTable ()
specifier|protected
name|UnifiedDiffTable
name|createContentTable
parameter_list|()
block|{
return|return
operator|new
name|UnifiedDiffTable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getPatchScreenType ()
specifier|protected
name|PatchScreen
operator|.
name|Type
name|getPatchScreenType
parameter_list|()
block|{
return|return
name|PatchScreen
operator|.
name|Type
operator|.
name|UNIFIED
return|;
block|}
block|}
DECL|field|patchKey
specifier|protected
specifier|final
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|fileList
specifier|protected
name|PatchTable
name|fileList
decl_stmt|;
DECL|field|idSideA
specifier|protected
name|PatchSet
operator|.
name|Id
name|idSideA
decl_stmt|;
DECL|field|idSideB
specifier|protected
name|PatchSet
operator|.
name|Id
name|idSideB
decl_stmt|;
DECL|field|scriptSettings
specifier|protected
specifier|final
name|PatchScriptSettings
name|scriptSettings
decl_stmt|;
DECL|field|historyPanel
specifier|private
name|DisclosurePanel
name|historyPanel
decl_stmt|;
DECL|field|historyTable
specifier|private
name|HistoryTable
name|historyTable
decl_stmt|;
DECL|field|contentPanel
specifier|private
name|FlowPanel
name|contentPanel
decl_stmt|;
DECL|field|noDifference
specifier|private
name|Label
name|noDifference
decl_stmt|;
DECL|field|contentTable
specifier|private
name|AbstractPatchContentTable
name|contentTable
decl_stmt|;
DECL|field|rpcSequence
specifier|private
name|int
name|rpcSequence
decl_stmt|;
DECL|field|script
specifier|private
name|PatchScript
name|script
decl_stmt|;
DECL|field|comments
specifier|private
name|CommentDetail
name|comments
decl_stmt|;
comment|/** The index of the file we are currently looking at among the fileList */
DECL|field|patchIndex
specifier|private
name|int
name|patchIndex
decl_stmt|;
comment|/**    * How this patch should be displayed in the patch screen.    */
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
DECL|enumConstant|UNIFIED
DECL|enumConstant|SIDE_BY_SIDE
name|UNIFIED
block|,
name|SIDE_BY_SIDE
block|}
DECL|method|PatchScreen (final Patch.Key id, final int patchIndex, final PatchTable patchTable)
specifier|protected
name|PatchScreen
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|,
specifier|final
name|int
name|patchIndex
parameter_list|,
specifier|final
name|PatchTable
name|patchTable
parameter_list|)
block|{
name|patchKey
operator|=
name|id
expr_stmt|;
name|fileList
operator|=
name|patchTable
expr_stmt|;
name|idSideA
operator|=
literal|null
expr_stmt|;
name|idSideB
operator|=
name|id
operator|.
name|getParentKey
argument_list|()
expr_stmt|;
name|this
operator|.
name|patchIndex
operator|=
name|patchIndex
expr_stmt|;
name|scriptSettings
operator|=
operator|new
name|PatchScriptSettings
argument_list|()
expr_stmt|;
name|initContextLines
argument_list|()
expr_stmt|;
block|}
comment|/**    * Initialize the context lines to the user's preference, or to the default    * number if the user is not logged in.    */
DECL|method|initContextLines ()
specifier|private
name|void
name|initContextLines
parameter_list|()
block|{
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
specifier|final
name|AccountGeneralPreferences
name|p
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getGeneralPreferences
argument_list|()
decl_stmt|;
name|scriptSettings
operator|.
name|setContext
argument_list|(
name|p
operator|.
name|getDefaultContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scriptSettings
operator|.
name|setContext
argument_list|(
name|DEFAULT_CONTEXT
argument_list|)
expr_stmt|;
block|}
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
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchKey
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|patchKey
operator|.
name|get
argument_list|()
decl_stmt|;
name|String
name|fileName
init|=
name|path
decl_stmt|;
specifier|final
name|int
name|last
init|=
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|>=
literal|0
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|last
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|setWindowTitle
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|patchWindowTitle
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|fileName
argument_list|)
argument_list|)
expr_stmt|;
name|setPageTitle
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|patchPageTitle
argument_list|(
name|changeId
operator|.
name|get
argument_list|()
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|historyTable
operator|=
operator|new
name|HistoryTable
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|historyPanel
operator|=
operator|new
name|DisclosurePanel
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|patchHistoryTitle
argument_list|()
argument_list|)
expr_stmt|;
name|historyPanel
operator|.
name|setContent
argument_list|(
name|historyTable
argument_list|)
expr_stmt|;
name|historyPanel
operator|.
name|setOpen
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|historyPanel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|historyPanel
argument_list|)
expr_stmt|;
name|initDisplayControls
argument_list|()
expr_stmt|;
name|noDifference
operator|=
operator|new
name|Label
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|noDifference
argument_list|()
argument_list|)
expr_stmt|;
name|noDifference
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-PatchNoDifference"
argument_list|)
expr_stmt|;
name|noDifference
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|contentTable
operator|=
name|createContentTable
argument_list|()
expr_stmt|;
name|contentTable
operator|.
name|fileList
operator|=
name|fileList
expr_stmt|;
name|add
argument_list|(
name|createNextPrevLinks
argument_list|()
argument_list|)
expr_stmt|;
name|contentPanel
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|contentPanel
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-SideBySideScreen-SideBySideTable"
argument_list|)
expr_stmt|;
name|contentPanel
operator|.
name|add
argument_list|(
name|noDifference
argument_list|)
expr_stmt|;
name|contentPanel
operator|.
name|add
argument_list|(
name|contentTable
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|contentPanel
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|createNextPrevLinks
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initDisplayControls ()
specifier|private
name|void
name|initDisplayControls
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|displayControls
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|displayControls
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-PatchScreen-DisplayControls"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|displayControls
argument_list|)
expr_stmt|;
name|displayControls
operator|.
name|add
argument_list|(
name|createShowFullFiles
argument_list|()
argument_list|)
expr_stmt|;
name|displayControls
operator|.
name|add
argument_list|(
name|createIgnoreWhitespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createShowFullFiles ()
specifier|private
name|Widget
name|createShowFullFiles
parameter_list|()
block|{
specifier|final
name|CheckBox
name|cb
init|=
operator|new
name|CheckBox
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|showFullFiles
argument_list|()
argument_list|)
decl_stmt|;
name|cb
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
if|if
condition|(
name|event
operator|.
name|getValue
argument_list|()
condition|)
block|{
comment|// Show a diff of the full files
name|scriptSettings
operator|.
name|setContext
argument_list|(
name|WHOLE_FILE_CONTEXT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Restore the context lines to the user's preference
name|initContextLines
argument_list|()
expr_stmt|;
block|}
name|refresh
argument_list|(
literal|false
comment|/* not the first time */
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|cb
return|;
block|}
DECL|method|createIgnoreWhitespace ()
specifier|private
name|Widget
name|createIgnoreWhitespace
parameter_list|()
block|{
specifier|final
name|CheckBox
name|cb
init|=
operator|new
name|CheckBox
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|ignoreWhitespace
argument_list|()
argument_list|)
decl_stmt|;
name|cb
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
if|if
condition|(
name|event
operator|.
name|getValue
argument_list|()
condition|)
block|{
name|scriptSettings
operator|.
name|setWhitespace
argument_list|(
name|IGNORE_SPACE_CHANGE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scriptSettings
operator|.
name|setWhitespace
argument_list|(
name|IGNORE_NONE
argument_list|)
expr_stmt|;
block|}
name|refresh
argument_list|(
literal|false
comment|/* not the first time */
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|cb
return|;
block|}
DECL|method|createNextPrevLinks ()
specifier|private
name|Widget
name|createNextPrevLinks
parameter_list|()
block|{
specifier|final
name|Grid
name|table
init|=
operator|new
name|Grid
argument_list|(
literal|1
argument_list|,
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|table
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-SideBySideScreen-LinkTable"
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_LEFT
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_CENTER
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_RIGHT
argument_list|)
expr_stmt|;
if|if
condition|(
name|fileList
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|fileList
operator|.
name|getPreviousPatchLink
argument_list|(
name|patchIndex
argument_list|,
name|getPatchScreenType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|fileList
operator|.
name|getNextPatchLink
argument_list|(
name|patchIndex
argument_list|,
name|getPatchScreenType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ChangeLink
name|up
init|=
operator|new
name|ChangeLink
argument_list|(
literal|""
argument_list|,
name|patchKey
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
decl_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|up
argument_list|,
name|SafeHtml
operator|.
name|asis
argument_list|(
name|Util
operator|.
name|C
operator|.
name|upToChangeIconLink
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|up
argument_list|)
expr_stmt|;
return|return
name|table
return|;
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
name|refresh
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
expr_stmt|;
name|contentTable
operator|.
name|setRegisterKeys
argument_list|(
name|contentTable
operator|.
name|isVisible
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createContentTable ()
specifier|protected
specifier|abstract
name|AbstractPatchContentTable
name|createContentTable
parameter_list|()
function_decl|;
DECL|method|getPatchScreenType ()
specifier|protected
specifier|abstract
name|PatchScreen
operator|.
name|Type
name|getPatchScreenType
parameter_list|()
function_decl|;
DECL|method|refresh (final boolean isFirst)
specifier|protected
name|void
name|refresh
parameter_list|(
specifier|final
name|boolean
name|isFirst
parameter_list|)
block|{
specifier|final
name|int
name|rpcseq
init|=
operator|++
name|rpcSequence
decl_stmt|;
name|script
operator|=
literal|null
expr_stmt|;
name|comments
operator|=
literal|null
expr_stmt|;
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|patchScript
argument_list|(
name|patchKey
argument_list|,
name|idSideA
argument_list|,
name|idSideB
argument_list|,
name|scriptSettings
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|PatchScript
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|PatchScript
name|result
parameter_list|)
block|{
if|if
condition|(
name|rpcSequence
operator|==
name|rpcseq
condition|)
block|{
name|script
operator|=
name|result
expr_stmt|;
name|onResult
argument_list|()
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
if|if
condition|(
name|rpcSequence
operator|==
name|rpcseq
condition|)
block|{
if|if
condition|(
name|isNoDifferences
argument_list|(
name|caught
argument_list|)
operator|&&
operator|!
name|isFirst
condition|)
block|{
name|historyTable
operator|.
name|enableAll
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|showPatch
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|isNoDifferences
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
name|caught
operator|instanceof
name|NoDifferencesException
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|caught
operator|instanceof
name|RemoteJsonException
operator|&&
name|caught
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
name|NoDifferencesException
operator|.
name|MESSAGE
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|patchComments
argument_list|(
name|patchKey
argument_list|,
name|idSideA
argument_list|,
name|idSideB
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|CommentDetail
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|CommentDetail
name|result
parameter_list|)
block|{
if|if
condition|(
name|rpcSequence
operator|==
name|rpcseq
condition|)
block|{
name|comments
operator|=
name|result
expr_stmt|;
name|onResult
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{
comment|// Ignore no such entity, the patch script RPC above would
comment|// also notice the problem and report it.
comment|//
if|if
condition|(
operator|!
name|isNoSuchEntity
argument_list|(
name|caught
argument_list|)
operator|&&
name|rpcSequence
operator|==
name|rpcseq
condition|)
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|onResult ()
specifier|private
name|void
name|onResult
parameter_list|()
block|{
if|if
condition|(
name|script
operator|!=
literal|null
operator|&&
name|comments
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|comments
operator|.
name|getHistory
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|historyTable
operator|.
name|display
argument_list|(
name|comments
operator|.
name|getHistory
argument_list|()
argument_list|)
expr_stmt|;
name|historyPanel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|historyPanel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|contentTable
operator|instanceof
name|SideBySideTable
operator|&&
name|script
operator|.
name|getEdits
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|script
operator|.
name|getPatchHeader
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// User asked for SideBySide (or a link guessed, wrong) and we can't
comment|// show a binary or pure-rename change there accurately. Switch to
comment|// the unified view instead.
comment|//
name|contentTable
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|contentTable
operator|=
operator|new
name|UnifiedDiffTable
argument_list|()
expr_stmt|;
name|contentTable
operator|.
name|fileList
operator|=
name|fileList
expr_stmt|;
name|contentPanel
operator|.
name|add
argument_list|(
name|contentTable
argument_list|)
expr_stmt|;
name|History
operator|.
name|newItem
argument_list|(
name|Link
operator|.
name|toPatchUnified
argument_list|(
name|patchKey
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|contentTable
operator|.
name|display
argument_list|(
name|patchKey
argument_list|,
name|idSideA
argument_list|,
name|idSideB
argument_list|,
name|script
argument_list|)
expr_stmt|;
name|contentTable
operator|.
name|display
argument_list|(
name|comments
argument_list|)
expr_stmt|;
name|contentTable
operator|.
name|finishDisplay
argument_list|()
expr_stmt|;
name|showPatch
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|script
operator|=
literal|null
expr_stmt|;
name|comments
operator|=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|isCurrentView
argument_list|()
condition|)
block|{
name|display
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|showPatch (final boolean showPatch)
specifier|private
name|void
name|showPatch
parameter_list|(
specifier|final
name|boolean
name|showPatch
parameter_list|)
block|{
name|noDifference
operator|.
name|setVisible
argument_list|(
operator|!
name|showPatch
argument_list|)
expr_stmt|;
name|contentTable
operator|.
name|setVisible
argument_list|(
name|showPatch
argument_list|)
expr_stmt|;
name|contentTable
operator|.
name|setRegisterKeys
argument_list|(
name|isCurrentView
argument_list|()
operator|&&
name|showPatch
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

