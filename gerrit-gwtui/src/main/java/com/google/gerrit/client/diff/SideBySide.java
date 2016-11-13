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
DECL|package|com.google.gerrit.client.diff
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|diff
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|lang
operator|.
name|Double
operator|.
name|POSITIVE_INFINITY
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
name|DiffObject
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
name|diff
operator|.
name|LineMapper
operator|.
name|LineOnOtherInfo
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
name|patches
operator|.
name|PatchUtil
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
name|ConfigInfoCache
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
name|InlineHyperlink
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
name|extensions
operator|.
name|client
operator|.
name|GeneralPreferencesInfo
operator|.
name|DiffView
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
name|Patch
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
name|GWT
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
name|Scheduler
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
name|Scheduler
operator|.
name|ScheduledCommand
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
name|dom
operator|.
name|client
operator|.
name|Element
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
name|FocusEvent
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
name|FocusHandler
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|rpc
operator|.
name|AsyncCallback
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
name|GlobalKey
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
name|KeyCommand
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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
operator|.
name|LineHandle
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|KeyMap
import|;
end_import

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|Pos
import|;
end_import

begin_class
DECL|class|SideBySide
specifier|public
class|class
name|SideBySide
extends|extends
name|DiffScreen
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|FlowPanel
argument_list|,
name|SideBySide
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LINE_NUMBER_CLASSNAME
specifier|private
specifier|static
specifier|final
name|String
name|LINE_NUMBER_CLASSNAME
init|=
literal|"CodeMirror-linenumber"
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|diffTable
name|SideBySideTable
name|diffTable
decl_stmt|;
DECL|field|cmA
specifier|private
name|CodeMirror
name|cmA
decl_stmt|;
DECL|field|cmB
specifier|private
name|CodeMirror
name|cmB
decl_stmt|;
DECL|field|scrollSynchronizer
specifier|private
name|ScrollSynchronizer
name|scrollSynchronizer
decl_stmt|;
DECL|field|chunkManager
specifier|private
name|SideBySideChunkManager
name|chunkManager
decl_stmt|;
DECL|field|commentManager
specifier|private
name|SideBySideCommentManager
name|commentManager
decl_stmt|;
DECL|method|SideBySide ( DiffObject base, DiffObject revision, String path, DisplaySide startSide, int startLine)
specifier|public
name|SideBySide
parameter_list|(
name|DiffObject
name|base
parameter_list|,
name|DiffObject
name|revision
parameter_list|,
name|String
name|path
parameter_list|,
name|DisplaySide
name|startSide
parameter_list|,
name|int
name|startLine
parameter_list|)
block|{
name|super
argument_list|(
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|,
name|startSide
argument_list|,
name|startLine
argument_list|,
name|DiffView
operator|.
name|SIDE_BY_SIDE
argument_list|)
expr_stmt|;
name|diffTable
operator|=
operator|new
name|SideBySideTable
argument_list|(
name|this
argument_list|,
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|addDomHandler
argument_list|(
name|GlobalKey
operator|.
name|STOP_PROPAGATION
argument_list|,
name|KeyPressEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScreenLoadCallback ( final CommentsCollections comments)
name|ScreenLoadCallback
argument_list|<
name|ConfigInfoCache
operator|.
name|Entry
argument_list|>
name|getScreenLoadCallback
parameter_list|(
specifier|final
name|CommentsCollections
name|comments
parameter_list|)
block|{
return|return
operator|new
name|ScreenLoadCallback
argument_list|<
name|ConfigInfoCache
operator|.
name|Entry
argument_list|>
argument_list|(
name|SideBySide
operator|.
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
name|ConfigInfoCache
operator|.
name|Entry
name|result
parameter_list|)
block|{
name|commentManager
operator|=
operator|new
name|SideBySideCommentManager
argument_list|(
name|SideBySide
operator|.
name|this
argument_list|,
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|,
name|result
operator|.
name|getCommentLinkProcessor
argument_list|()
argument_list|,
name|getChangeStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
name|setTheme
argument_list|(
name|result
operator|.
name|getTheme
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|(
name|comments
argument_list|)
expr_stmt|;
name|header
operator|.
name|setupPrevNextFiles
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|onShowView ()
specifier|public
name|void
name|onShowView
parameter_list|()
block|{
name|super
operator|.
name|onShowView
argument_list|()
expr_stmt|;
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|resizeCodeMirror
argument_list|()
expr_stmt|;
name|chunkManager
operator|.
name|adjustPadding
argument_list|()
expr_stmt|;
name|cmA
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|cmB
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|setLineLength
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|?
literal|72
else|:
name|prefs
operator|.
name|lineLength
argument_list|()
argument_list|)
expr_stmt|;
name|diffTable
operator|.
name|refresh
argument_list|()
expr_stmt|;
if|if
condition|(
name|getStartLine
argument_list|()
operator|==
literal|0
condition|)
block|{
name|DiffChunkInfo
name|d
init|=
name|chunkManager
operator|.
name|getFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|d
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|d
operator|.
name|isEdit
argument_list|()
operator|&&
name|d
operator|.
name|getSide
argument_list|()
operator|==
name|DisplaySide
operator|.
name|A
condition|)
block|{
name|setStartSide
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|)
expr_stmt|;
name|setStartLine
argument_list|(
name|lineOnOther
argument_list|(
name|d
operator|.
name|getSide
argument_list|()
argument_list|,
name|d
operator|.
name|getStart
argument_list|()
argument_list|)
operator|.
name|getLine
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setStartSide
argument_list|(
name|d
operator|.
name|getSide
argument_list|()
argument_list|)
expr_stmt|;
name|setStartLine
argument_list|(
name|d
operator|.
name|getStart
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|getStartSide
argument_list|()
operator|!=
literal|null
operator|&&
name|getStartLine
argument_list|()
operator|>
literal|0
condition|)
block|{
name|CodeMirror
name|cm
init|=
name|getCmFromSide
argument_list|(
name|getStartSide
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|scrollToLine
argument_list|(
name|getStartLine
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cmA
operator|.
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
operator|&&
name|prefs
operator|.
name|autoReview
argument_list|()
condition|)
block|{
name|header
operator|.
name|autoReview
argument_list|()
expr_stmt|;
block|}
name|prefetchNextFile
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerCmEvents (final CodeMirror cm)
name|void
name|registerCmEvents
parameter_list|(
specifier|final
name|CodeMirror
name|cm
parameter_list|)
block|{
name|super
operator|.
name|registerCmEvents
argument_list|(
name|cm
argument_list|)
expr_stmt|;
name|KeyMap
name|keyMap
init|=
name|KeyMap
operator|.
name|create
argument_list|()
operator|.
name|on
argument_list|(
literal|"Shift-A"
argument_list|,
name|diffTable
operator|.
name|toggleA
argument_list|()
argument_list|)
operator|.
name|on
argument_list|(
literal|"Shift-Left"
argument_list|,
name|moveCursorToSide
argument_list|(
name|cm
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|)
argument_list|)
operator|.
name|on
argument_list|(
literal|"Shift-Right"
argument_list|,
name|moveCursorToSide
argument_list|(
name|cm
argument_list|,
name|DisplaySide
operator|.
name|B
argument_list|)
argument_list|)
decl_stmt|;
name|cm
operator|.
name|addKeyMap
argument_list|(
name|keyMap
argument_list|)
expr_stmt|;
name|maybeRegisterRenderEntireFileKeyMap
argument_list|(
name|cm
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
name|getKeysNavigation
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|NoOpKeyCommand
argument_list|(
name|KeyCommand
operator|.
name|M_SHIFT
argument_list|,
name|KeyCodes
operator|.
name|KEY_LEFT
argument_list|,
name|PatchUtil
operator|.
name|C
operator|.
name|focusSideA
argument_list|()
argument_list|)
argument_list|,
operator|new
name|NoOpKeyCommand
argument_list|(
name|KeyCommand
operator|.
name|M_SHIFT
argument_list|,
name|KeyCodes
operator|.
name|KEY_RIGHT
argument_list|,
name|PatchUtil
operator|.
name|C
operator|.
name|focusSideB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getKeysAction
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|KeyCommand
argument_list|(
name|KeyCommand
operator|.
name|M_SHIFT
argument_list|,
literal|'a'
argument_list|,
name|PatchUtil
operator|.
name|C
operator|.
name|toggleSideA
argument_list|()
argument_list|)
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
name|diffTable
operator|.
name|toggleA
argument_list|()
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|registerHandlers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getFocusHandler ()
name|FocusHandler
name|getFocusHandler
parameter_list|()
block|{
return|return
operator|new
name|FocusHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFocus
parameter_list|(
name|FocusEvent
name|event
parameter_list|)
block|{
name|cmB
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|display (final CommentsCollections comments)
specifier|private
name|void
name|display
parameter_list|(
specifier|final
name|CommentsCollections
name|comments
parameter_list|)
block|{
specifier|final
name|DiffInfo
name|diff
init|=
name|getDiff
argument_list|()
decl_stmt|;
name|setThemeStyles
argument_list|(
name|prefs
operator|.
name|theme
argument_list|()
operator|.
name|isDark
argument_list|()
argument_list|)
expr_stmt|;
name|setShowIntraline
argument_list|(
name|prefs
operator|.
name|intralineDifference
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|prefs
operator|.
name|showLineNumbers
argument_list|()
condition|)
block|{
name|diffTable
operator|.
name|addStyleName
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|diffTableStyle
argument_list|()
operator|.
name|showLineNumbers
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cmA
operator|=
name|newCm
argument_list|(
name|diff
operator|.
name|metaA
argument_list|()
argument_list|,
name|diff
operator|.
name|textA
argument_list|()
argument_list|,
name|diffTable
operator|.
name|cmA
argument_list|)
expr_stmt|;
name|cmB
operator|=
name|newCm
argument_list|(
name|diff
operator|.
name|metaB
argument_list|()
argument_list|,
name|diff
operator|.
name|textB
argument_list|()
argument_list|,
name|diffTable
operator|.
name|cmB
argument_list|)
expr_stmt|;
name|getDiffTable
argument_list|()
operator|.
name|setUpBlameIconA
argument_list|(
name|cmA
argument_list|,
name|base
operator|.
name|isBaseOrAutoMerge
argument_list|()
argument_list|,
name|base
operator|.
name|isBaseOrAutoMerge
argument_list|()
condition|?
name|revision
else|:
name|base
operator|.
name|asPatchSetId
argument_list|()
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|getDiffTable
argument_list|()
operator|.
name|setUpBlameIconB
argument_list|(
name|cmB
argument_list|,
name|revision
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|extras
argument_list|()
operator|.
name|side
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|extras
argument_list|()
operator|.
name|side
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|)
expr_stmt|;
name|setShowTabs
argument_list|(
name|prefs
operator|.
name|showTabs
argument_list|()
argument_list|)
expr_stmt|;
name|chunkManager
operator|=
operator|new
name|SideBySideChunkManager
argument_list|(
name|this
argument_list|,
name|cmA
argument_list|,
name|cmB
argument_list|,
name|diffTable
operator|.
name|scrollbar
argument_list|)
expr_stmt|;
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// Estimate initial CodeMirror height, fixed up in onShowView.
name|int
name|height
init|=
name|Window
operator|.
name|getClientHeight
argument_list|()
operator|-
operator|(
name|Gerrit
operator|.
name|getHeaderFooterHeight
argument_list|()
operator|+
literal|18
operator|)
decl_stmt|;
name|cmA
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|render
argument_list|(
name|diff
argument_list|)
expr_stmt|;
name|commentManager
operator|.
name|render
argument_list|(
name|comments
argument_list|,
name|prefs
operator|.
name|expandAllComments
argument_list|()
argument_list|)
expr_stmt|;
name|skipManager
operator|.
name|render
argument_list|(
name|prefs
operator|.
name|context
argument_list|()
argument_list|,
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|registerCmEvents
argument_list|(
name|cmA
argument_list|)
expr_stmt|;
name|registerCmEvents
argument_list|(
name|cmB
argument_list|)
expr_stmt|;
name|scrollSynchronizer
operator|=
operator|new
name|ScrollSynchronizer
argument_list|(
name|diffTable
argument_list|,
name|cmA
argument_list|,
name|cmB
argument_list|,
name|chunkManager
operator|.
name|lineMapper
argument_list|)
expr_stmt|;
name|setPrefsAction
argument_list|(
operator|new
name|PreferencesAction
argument_list|(
name|this
argument_list|,
name|prefs
argument_list|)
argument_list|)
expr_stmt|;
name|header
operator|.
name|init
argument_list|(
name|getPrefsAction
argument_list|()
argument_list|,
name|getUnifiedDiffLink
argument_list|()
argument_list|,
name|diff
operator|.
name|sideBySideWebLinks
argument_list|()
argument_list|)
expr_stmt|;
name|scrollSynchronizer
operator|.
name|setAutoHideDiffTableHeader
argument_list|(
name|prefs
operator|.
name|autoHideDiffTableHeader
argument_list|()
argument_list|)
expr_stmt|;
name|setupSyntaxHighlighting
argument_list|()
expr_stmt|;
block|}
DECL|method|getUnifiedDiffLink ()
specifier|private
name|List
argument_list|<
name|InlineHyperlink
argument_list|>
name|getUnifiedDiffLink
parameter_list|()
block|{
name|InlineHyperlink
name|toUnifiedDiffLink
init|=
operator|new
name|InlineHyperlink
argument_list|()
decl_stmt|;
name|toUnifiedDiffLink
operator|.
name|setHTML
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
name|unifiedDiff
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|toUnifiedDiffLink
operator|.
name|setTargetHistoryToken
argument_list|(
name|Dispatcher
operator|.
name|toUnified
argument_list|(
name|base
argument_list|,
name|revision
argument_list|,
name|path
argument_list|)
argument_list|)
expr_stmt|;
name|toUnifiedDiffLink
operator|.
name|setTitle
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|unifiedDiff
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|toUnifiedDiffLink
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newCm (DiffInfo.FileMeta meta, String contents, Element parent)
name|CodeMirror
name|newCm
parameter_list|(
name|DiffInfo
operator|.
name|FileMeta
name|meta
parameter_list|,
name|String
name|contents
parameter_list|,
name|Element
name|parent
parameter_list|)
block|{
return|return
name|CodeMirror
operator|.
name|create
argument_list|(
name|parent
argument_list|,
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"cursorBlinkRate"
argument_list|,
name|prefs
operator|.
name|cursorBlinkRate
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"cursorHeight"
argument_list|,
literal|0.85
argument_list|)
operator|.
name|set
argument_list|(
literal|"inputStyle"
argument_list|,
literal|"textarea"
argument_list|)
operator|.
name|set
argument_list|(
literal|"keyMap"
argument_list|,
literal|"vim_ro"
argument_list|)
operator|.
name|set
argument_list|(
literal|"lineNumbers"
argument_list|,
name|prefs
operator|.
name|showLineNumbers
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"matchBrackets"
argument_list|,
name|prefs
operator|.
name|matchBrackets
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"lineWrapping"
argument_list|,
name|prefs
operator|.
name|lineWrapping
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"mode"
argument_list|,
name|getFileSize
argument_list|()
operator|==
name|FileSize
operator|.
name|SMALL
condition|?
name|getContentType
argument_list|(
name|meta
argument_list|)
else|:
literal|null
argument_list|)
operator|.
name|set
argument_list|(
literal|"readOnly"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"scrollbarStyle"
argument_list|,
literal|"overlay"
argument_list|)
operator|.
name|set
argument_list|(
literal|"showTrailingSpace"
argument_list|,
name|prefs
operator|.
name|showWhitespaceErrors
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"styleSelectedText"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"tabSize"
argument_list|,
name|prefs
operator|.
name|tabSize
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"theme"
argument_list|,
name|prefs
operator|.
name|theme
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|.
name|set
argument_list|(
literal|"value"
argument_list|,
name|meta
operator|!=
literal|null
condition|?
name|contents
else|:
literal|""
argument_list|)
operator|.
name|set
argument_list|(
literal|"viewportMargin"
argument_list|,
name|renderEntireFile
argument_list|()
condition|?
name|POSITIVE_INFINITY
else|:
literal|10
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setShowLineNumbers (boolean b)
name|void
name|setShowLineNumbers
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|super
operator|.
name|setShowLineNumbers
argument_list|(
name|b
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|setOption
argument_list|(
literal|"lineNumbers"
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setOption
argument_list|(
literal|"lineNumbers"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setSyntaxHighlighting (boolean b)
name|void
name|setSyntaxHighlighting
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
specifier|final
name|DiffInfo
name|diff
init|=
name|getDiff
argument_list|()
decl_stmt|;
if|if
condition|(
name|b
condition|)
block|{
name|injectMode
argument_list|(
name|diff
argument_list|,
operator|new
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|result
parameter_list|)
block|{
if|if
condition|(
name|prefs
operator|.
name|syntaxHighlighting
argument_list|()
condition|)
block|{
name|cmA
operator|.
name|setOption
argument_list|(
literal|"mode"
argument_list|,
name|getContentType
argument_list|(
name|diff
operator|.
name|metaA
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setOption
argument_list|(
literal|"mode"
argument_list|,
name|getContentType
argument_list|(
name|diff
operator|.
name|metaB
argument_list|()
argument_list|)
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
name|Throwable
name|caught
parameter_list|)
block|{
name|prefs
operator|.
name|syntaxHighlighting
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cmA
operator|.
name|setOption
argument_list|(
literal|"mode"
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|setOption
argument_list|(
literal|"mode"
argument_list|,
operator|(
name|String
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setAutoHideDiffHeader (boolean hide)
name|void
name|setAutoHideDiffHeader
parameter_list|(
name|boolean
name|hide
parameter_list|)
block|{
name|scrollSynchronizer
operator|.
name|setAutoHideDiffTableHeader
argument_list|(
name|hide
argument_list|)
expr_stmt|;
block|}
DECL|method|otherCm (CodeMirror me)
name|CodeMirror
name|otherCm
parameter_list|(
name|CodeMirror
name|me
parameter_list|)
block|{
return|return
name|me
operator|==
name|cmA
condition|?
name|cmB
else|:
name|cmA
return|;
block|}
annotation|@
name|Override
DECL|method|getCmFromSide (DisplaySide side)
name|CodeMirror
name|getCmFromSide
parameter_list|(
name|DisplaySide
name|side
parameter_list|)
block|{
return|return
name|side
operator|==
name|DisplaySide
operator|.
name|A
condition|?
name|cmA
else|:
name|cmB
return|;
block|}
annotation|@
name|Override
DECL|method|getCmLine (int line, DisplaySide side)
name|int
name|getCmLine
parameter_list|(
name|int
name|line
parameter_list|,
name|DisplaySide
name|side
parameter_list|)
block|{
return|return
name|line
return|;
block|}
annotation|@
name|Override
DECL|method|updateActiveLine (final CodeMirror cm)
name|Runnable
name|updateActiveLine
parameter_list|(
specifier|final
name|CodeMirror
name|cm
parameter_list|)
block|{
specifier|final
name|CodeMirror
name|other
init|=
name|otherCm
argument_list|(
name|cm
argument_list|)
decl_stmt|;
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// The rendering of active lines has to be deferred. Reflow
comment|// caused by adding and removing styles chokes Firefox when arrow
comment|// key (or j/k) is held down. Performance on Chrome is fine
comment|// without the deferral.
comment|//
name|Scheduler
operator|.
name|get
argument_list|()
operator|.
name|scheduleDeferred
argument_list|(
operator|new
name|ScheduledCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|LineHandle
name|handle
init|=
name|cm
operator|.
name|getLineHandleVisualStart
argument_list|(
name|cm
operator|.
name|getCursor
argument_list|(
literal|"end"
argument_list|)
operator|.
name|line
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|(
name|handle
argument_list|)
condition|)
block|{
return|return;
block|}
name|LineOnOtherInfo
name|info
init|=
name|lineOnOther
argument_list|(
name|cm
operator|.
name|side
argument_list|()
argument_list|,
name|cm
operator|.
name|getLineNumber
argument_list|(
name|handle
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|isAligned
argument_list|()
condition|)
block|{
name|other
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|(
name|other
operator|.
name|getLineHandle
argument_list|(
name|info
operator|.
name|getLine
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|other
operator|.
name|extras
argument_list|()
operator|.
name|clearActiveLine
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|moveCursorToSide (final CodeMirror cmSrc, DisplaySide sideDst)
specifier|private
name|Runnable
name|moveCursorToSide
parameter_list|(
specifier|final
name|CodeMirror
name|cmSrc
parameter_list|,
name|DisplaySide
name|sideDst
parameter_list|)
block|{
specifier|final
name|CodeMirror
name|cmDst
init|=
name|getCmFromSide
argument_list|(
name|sideDst
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmDst
operator|==
name|cmSrc
condition|)
block|{
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{}
block|}
return|;
block|}
specifier|final
name|DisplaySide
name|sideSrc
init|=
name|cmSrc
operator|.
name|side
argument_list|()
decl_stmt|;
return|return
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|cmSrc
operator|.
name|extras
argument_list|()
operator|.
name|hasActiveLine
argument_list|()
condition|)
block|{
name|cmDst
operator|.
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|lineOnOther
argument_list|(
name|sideSrc
argument_list|,
name|cmSrc
operator|.
name|getLineNumber
argument_list|(
name|cmSrc
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|()
argument_list|)
argument_list|)
operator|.
name|getLine
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cmDst
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|syncScroll (DisplaySide masterSide)
name|void
name|syncScroll
parameter_list|(
name|DisplaySide
name|masterSide
parameter_list|)
block|{
if|if
condition|(
name|scrollSynchronizer
operator|!=
literal|null
condition|)
block|{
name|scrollSynchronizer
operator|.
name|syncScroll
argument_list|(
name|masterSide
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|operation (final Runnable apply)
name|void
name|operation
parameter_list|(
specifier|final
name|Runnable
name|apply
parameter_list|)
block|{
name|cmA
operator|.
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|cmB
operator|.
name|operation
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|apply
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCms ()
name|CodeMirror
index|[]
name|getCms
parameter_list|()
block|{
return|return
operator|new
name|CodeMirror
index|[]
block|{
name|cmA
block|,
name|cmB
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getDiffTable ()
name|SideBySideTable
name|getDiffTable
parameter_list|()
block|{
return|return
name|diffTable
return|;
block|}
annotation|@
name|Override
DECL|method|getChunkManager ()
name|SideBySideChunkManager
name|getChunkManager
parameter_list|()
block|{
return|return
name|chunkManager
return|;
block|}
annotation|@
name|Override
DECL|method|getCommentManager ()
name|SideBySideCommentManager
name|getCommentManager
parameter_list|()
block|{
return|return
name|commentManager
return|;
block|}
annotation|@
name|Override
DECL|method|isSideBySide ()
name|boolean
name|isSideBySide
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getLineNumberClassName ()
name|String
name|getLineNumberClassName
parameter_list|()
block|{
return|return
name|LINE_NUMBER_CLASSNAME
return|;
block|}
block|}
end_class

end_unit

