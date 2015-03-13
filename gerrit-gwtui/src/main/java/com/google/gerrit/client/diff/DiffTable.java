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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
operator|.
name|DiffPreferences
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
name|ChangeInfo
operator|.
name|RevisionInfo
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
operator|.
name|ChangeType
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
name|PatchSet
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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
name|dom
operator|.
name|client
operator|.
name|Style
operator|.
name|Unit
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
name|resources
operator|.
name|client
operator|.
name|CssResource
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
name|ui
operator|.
name|Composite
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
name|HTMLPanel
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
name|UIObject
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
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|CodeMirror
import|;
end_import

begin_comment
comment|/**  * A table with one row and two columns to hold the two CodeMirrors displaying  * the files to be diffed.  */
end_comment

begin_class
DECL|class|DiffTable
class|class
name|DiffTable
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|DiffTable
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
DECL|interface|DiffTableStyle
interface|interface
name|DiffTableStyle
extends|extends
name|CssResource
block|{
DECL|method|fullscreen ()
name|String
name|fullscreen
parameter_list|()
function_decl|;
DECL|method|intralineBg ()
name|String
name|intralineBg
parameter_list|()
function_decl|;
DECL|method|dark ()
name|String
name|dark
parameter_list|()
function_decl|;
DECL|method|diff ()
name|String
name|diff
parameter_list|()
function_decl|;
DECL|method|noIntraline ()
name|String
name|noIntraline
parameter_list|()
function_decl|;
DECL|method|range ()
name|String
name|range
parameter_list|()
function_decl|;
DECL|method|rangeHighlight ()
name|String
name|rangeHighlight
parameter_list|()
function_decl|;
DECL|method|showLineNumbers ()
name|String
name|showLineNumbers
parameter_list|()
function_decl|;
DECL|method|hideA ()
name|String
name|hideA
parameter_list|()
function_decl|;
DECL|method|hideB ()
name|String
name|hideB
parameter_list|()
function_decl|;
DECL|method|padding ()
name|String
name|padding
parameter_list|()
function_decl|;
block|}
DECL|field|cmA
annotation|@
name|UiField
name|Element
name|cmA
decl_stmt|;
DECL|field|cmB
annotation|@
name|UiField
name|Element
name|cmB
decl_stmt|;
DECL|field|scrollbar
name|Scrollbar
name|scrollbar
decl_stmt|;
DECL|field|patchSetNavRow
annotation|@
name|UiField
name|Element
name|patchSetNavRow
decl_stmt|;
DECL|field|patchSetNavCellA
annotation|@
name|UiField
name|Element
name|patchSetNavCellA
decl_stmt|;
DECL|field|patchSetNavCellB
annotation|@
name|UiField
name|Element
name|patchSetNavCellB
decl_stmt|;
DECL|field|diffHeaderRow
annotation|@
name|UiField
name|Element
name|diffHeaderRow
decl_stmt|;
DECL|field|diffHeaderText
annotation|@
name|UiField
name|Element
name|diffHeaderText
decl_stmt|;
DECL|field|widgets
annotation|@
name|UiField
name|FlowPanel
name|widgets
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
specifier|static
name|DiffTableStyle
name|style
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|patchSetSelectBoxA
name|PatchSetSelectBox
name|patchSetSelectBoxA
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|patchSetSelectBoxB
name|PatchSetSelectBox
name|patchSetSelectBoxB
decl_stmt|;
DECL|field|parent
specifier|private
name|SideBySide
name|parent
decl_stmt|;
DECL|field|header
specifier|private
name|boolean
name|header
decl_stmt|;
DECL|field|visibleA
specifier|private
name|boolean
name|visibleA
decl_stmt|;
DECL|field|changeType
specifier|private
name|ChangeType
name|changeType
decl_stmt|;
DECL|method|DiffTable (SideBySide parent, PatchSet.Id base, PatchSet.Id revision, String path)
name|DiffTable
parameter_list|(
name|SideBySide
name|parent
parameter_list|,
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|PatchSet
operator|.
name|Id
name|revision
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|patchSetSelectBoxA
operator|=
operator|new
name|PatchSetSelectBox
argument_list|(
name|parent
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|,
name|revision
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|base
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|patchSetSelectBoxB
operator|=
operator|new
name|PatchSetSelectBox
argument_list|(
name|parent
argument_list|,
name|DisplaySide
operator|.
name|B
argument_list|,
name|revision
operator|.
name|getParentKey
argument_list|()
argument_list|,
name|revision
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|PatchSetSelectBox
operator|.
name|link
argument_list|(
name|patchSetSelectBoxA
argument_list|,
name|patchSetSelectBoxB
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|scrollbar
operator|=
operator|new
name|Scrollbar
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|parent
expr_stmt|;
name|this
operator|.
name|visibleA
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isVisibleA ()
name|boolean
name|isVisibleA
parameter_list|()
block|{
return|return
name|visibleA
return|;
block|}
DECL|method|setVisibleA (boolean show)
name|void
name|setVisibleA
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
name|visibleA
operator|=
name|show
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
name|removeStyleName
argument_list|(
name|style
operator|.
name|hideA
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|.
name|syncScroll
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|)
expr_stmt|;
comment|// match B's viewport
block|}
else|else
block|{
name|addStyleName
argument_list|(
name|style
operator|.
name|hideA
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|toggleA ()
name|Runnable
name|toggleA
parameter_list|()
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
block|{
name|setVisibleA
argument_list|(
operator|!
name|isVisibleA
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|setVisibleB (boolean show)
name|void
name|setVisibleB
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
if|if
condition|(
name|show
condition|)
block|{
name|removeStyleName
argument_list|(
name|style
operator|.
name|hideB
argument_list|()
argument_list|)
expr_stmt|;
name|parent
operator|.
name|syncScroll
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|)
expr_stmt|;
comment|// match A's viewport
block|}
else|else
block|{
name|addStyleName
argument_list|(
name|style
operator|.
name|hideB
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setHeaderVisible (boolean show)
name|void
name|setHeaderVisible
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
if|if
condition|(
name|show
operator|!=
name|UIObject
operator|.
name|isVisible
argument_list|(
name|patchSetNavRow
argument_list|)
condition|)
block|{
name|UIObject
operator|.
name|setVisible
argument_list|(
name|patchSetNavRow
argument_list|,
name|show
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|diffHeaderRow
argument_list|,
name|show
operator|&&
name|header
argument_list|)
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
name|parent
operator|.
name|header
operator|.
name|removeStyleName
argument_list|(
name|style
operator|.
name|fullscreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|parent
operator|.
name|header
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|fullscreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|parent
operator|.
name|resizeCodeMirror
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getHeaderHeight ()
name|int
name|getHeaderHeight
parameter_list|()
block|{
name|int
name|h
init|=
name|patchSetSelectBoxA
operator|.
name|getOffsetHeight
argument_list|()
decl_stmt|;
if|if
condition|(
name|header
condition|)
block|{
name|h
operator|+=
name|diffHeaderRow
operator|.
name|getOffsetHeight
argument_list|()
expr_stmt|;
block|}
return|return
name|h
return|;
block|}
DECL|method|getChangeType ()
name|ChangeType
name|getChangeType
parameter_list|()
block|{
return|return
name|changeType
return|;
block|}
DECL|method|set (DiffPreferences prefs, JsArray<RevisionInfo> list, DiffInfo info, boolean editExists, int currentPatchSet, boolean open, boolean binary)
name|void
name|set
parameter_list|(
name|DiffPreferences
name|prefs
parameter_list|,
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|list
parameter_list|,
name|DiffInfo
name|info
parameter_list|,
name|boolean
name|editExists
parameter_list|,
name|int
name|currentPatchSet
parameter_list|,
name|boolean
name|open
parameter_list|,
name|boolean
name|binary
parameter_list|)
block|{
name|this
operator|.
name|changeType
operator|=
name|info
operator|.
name|change_type
argument_list|()
expr_stmt|;
name|patchSetSelectBoxA
operator|.
name|setUpPatchSetNav
argument_list|(
name|list
argument_list|,
name|info
operator|.
name|meta_a
argument_list|()
argument_list|,
name|editExists
argument_list|,
name|currentPatchSet
argument_list|,
name|open
argument_list|,
name|binary
argument_list|)
expr_stmt|;
name|patchSetSelectBoxB
operator|.
name|setUpPatchSetNav
argument_list|(
name|list
argument_list|,
name|info
operator|.
name|meta_b
argument_list|()
argument_list|,
name|editExists
argument_list|,
name|currentPatchSet
argument_list|,
name|open
argument_list|,
name|binary
argument_list|)
expr_stmt|;
name|JsArrayString
name|hdr
init|=
name|info
operator|.
name|diff_header
argument_list|()
decl_stmt|;
if|if
condition|(
name|hdr
operator|!=
literal|null
condition|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
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
operator|<
name|hdr
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|String
name|s
init|=
name|hdr
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"diff --git "
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"index "
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"+++ "
argument_list|)
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"--- "
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|b
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|String
name|hdrTxt
init|=
name|b
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
name|header
operator|=
operator|!
name|hdrTxt
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|diffHeaderText
operator|.
name|setInnerText
argument_list|(
name|hdrTxt
argument_list|)
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|diffHeaderRow
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|header
operator|=
literal|false
expr_stmt|;
name|UIObject
operator|.
name|setVisible
argument_list|(
name|diffHeaderRow
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
name|setHideEmptyPane
argument_list|(
name|prefs
operator|.
name|hideEmptyPane
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setHideEmptyPane (boolean hide)
name|void
name|setHideEmptyPane
parameter_list|(
name|boolean
name|hide
parameter_list|)
block|{
if|if
condition|(
name|changeType
operator|==
name|ChangeType
operator|.
name|ADDED
condition|)
block|{
name|setVisibleA
argument_list|(
operator|!
name|hide
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|changeType
operator|==
name|ChangeType
operator|.
name|DELETED
condition|)
block|{
name|setVisibleB
argument_list|(
operator|!
name|hide
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|refresh ()
name|void
name|refresh
parameter_list|()
block|{
if|if
condition|(
name|header
condition|)
block|{
name|CodeMirror
name|cm
init|=
name|parent
operator|.
name|getCmFromSide
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|)
decl_stmt|;
name|diffHeaderText
operator|.
name|getStyle
argument_list|()
operator|.
name|setMarginLeft
argument_list|(
name|cm
operator|.
name|getGutterElement
argument_list|()
operator|.
name|getOffsetWidth
argument_list|()
argument_list|,
name|Unit
operator|.
name|PX
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|add (Widget widget)
name|void
name|add
parameter_list|(
name|Widget
name|widget
parameter_list|)
block|{
name|widgets
operator|.
name|add
argument_list|(
name|widget
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

