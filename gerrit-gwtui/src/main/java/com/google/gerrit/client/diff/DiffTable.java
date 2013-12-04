begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
DECL|method|activeLine ()
name|String
name|activeLine
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
DECL|method|showTabs ()
name|String
name|showTabs
parameter_list|()
function_decl|;
block|}
annotation|@
name|UiField
DECL|field|cmA
name|Element
name|cmA
decl_stmt|;
annotation|@
name|UiField
DECL|field|cmB
name|Element
name|cmB
decl_stmt|;
annotation|@
name|UiField
DECL|field|sidePanel
name|SidePanel
name|sidePanel
decl_stmt|;
annotation|@
name|UiField
DECL|field|patchSetNavRow
name|Element
name|patchSetNavRow
decl_stmt|;
annotation|@
name|UiField
DECL|field|patchSetNavCellA
name|Element
name|patchSetNavCellA
decl_stmt|;
annotation|@
name|UiField
DECL|field|patchSetNavCellB
name|Element
name|patchSetNavCellB
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|patchSetSelectBoxA
name|PatchSetSelectBox2
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
name|PatchSetSelectBox2
name|patchSetSelectBoxB
decl_stmt|;
annotation|@
name|UiField
DECL|field|fileCommentRow
name|Element
name|fileCommentRow
decl_stmt|;
annotation|@
name|UiField
DECL|field|fileCommentCellA
name|Element
name|fileCommentCellA
decl_stmt|;
annotation|@
name|UiField
DECL|field|fileCommentCellB
name|Element
name|fileCommentCellB
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|fileCommentPanelA
name|FileCommentPanel
name|fileCommentPanelA
decl_stmt|;
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
DECL|field|fileCommentPanelB
name|FileCommentPanel
name|fileCommentPanelB
decl_stmt|;
annotation|@
name|UiField
DECL|field|style
specifier|static
name|DiffTableStyle
name|style
decl_stmt|;
DECL|field|host
specifier|private
name|SideBySide2
name|host
decl_stmt|;
DECL|method|DiffTable (SideBySide2 host, PatchSet.Id base, PatchSet.Id revision, String path)
name|DiffTable
parameter_list|(
name|SideBySide2
name|host
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
name|PatchSetSelectBox2
argument_list|(
name|this
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
name|PatchSetSelectBox2
argument_list|(
name|this
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
name|PatchSetSelectBox2
operator|.
name|link
argument_list|(
name|patchSetSelectBoxA
argument_list|,
name|patchSetSelectBoxB
argument_list|)
expr_stmt|;
name|fileCommentPanelA
operator|=
operator|new
name|FileCommentPanel
argument_list|(
name|host
argument_list|,
name|this
argument_list|,
name|path
argument_list|,
name|DisplaySide
operator|.
name|A
argument_list|)
expr_stmt|;
name|fileCommentPanelB
operator|=
operator|new
name|FileCommentPanel
argument_list|(
name|host
argument_list|,
name|this
argument_list|,
name|path
argument_list|,
name|DisplaySide
operator|.
name|B
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
name|host
operator|=
name|host
expr_stmt|;
block|}
DECL|method|setHeaderVisible (boolean show)
name|void
name|setHeaderVisible
parameter_list|(
name|boolean
name|show
parameter_list|)
block|{
name|Gerrit
operator|.
name|setHeaderVisible
argument_list|(
name|show
argument_list|)
expr_stmt|;
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
name|fileCommentRow
argument_list|,
name|show
operator|&&
operator|(
name|fileCommentPanelA
operator|.
name|getBoxCount
argument_list|()
operator|>
literal|0
operator|||
name|fileCommentPanelB
operator|.
name|getBoxCount
argument_list|()
operator|>
literal|0
operator|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|show
condition|)
block|{
name|host
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
name|host
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
name|host
operator|.
name|resizeCodeMirror
argument_list|()
expr_stmt|;
block|}
DECL|method|getPanelFromSide (DisplaySide side)
specifier|private
name|FileCommentPanel
name|getPanelFromSide
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
name|fileCommentPanelA
else|:
name|fileCommentPanelB
return|;
block|}
DECL|method|createOrEditFileComment (DisplaySide side)
name|void
name|createOrEditFileComment
parameter_list|(
name|DisplaySide
name|side
parameter_list|)
block|{
name|getPanelFromSide
argument_list|(
name|side
argument_list|)
operator|.
name|createOrEditFileComment
argument_list|()
expr_stmt|;
name|setHeaderVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|addFileCommentBox (CommentBox box)
name|void
name|addFileCommentBox
parameter_list|(
name|CommentBox
name|box
parameter_list|)
block|{
name|getPanelFromSide
argument_list|(
name|box
operator|.
name|getSide
argument_list|()
argument_list|)
operator|.
name|addFileComment
argument_list|(
name|box
argument_list|)
expr_stmt|;
block|}
DECL|method|onRemoveDraftBox (DraftBox box)
name|void
name|onRemoveDraftBox
parameter_list|(
name|DraftBox
name|box
parameter_list|)
block|{
name|getPanelFromSide
argument_list|(
name|box
operator|.
name|getSide
argument_list|()
argument_list|)
operator|.
name|onRemoveDraftBox
argument_list|(
name|box
argument_list|)
expr_stmt|;
block|}
DECL|method|getHeaderHeight ()
name|int
name|getHeaderHeight
parameter_list|()
block|{
return|return
name|fileCommentRow
operator|.
name|getOffsetHeight
argument_list|()
operator|+
name|patchSetSelectBoxA
operator|.
name|getOffsetHeight
argument_list|()
return|;
block|}
DECL|method|setUpPatchSetNav (JsArray<RevisionInfo> list, DiffInfo info)
name|void
name|setUpPatchSetNav
parameter_list|(
name|JsArray
argument_list|<
name|RevisionInfo
argument_list|>
name|list
parameter_list|,
name|DiffInfo
name|info
parameter_list|)
block|{
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
argument_list|)
expr_stmt|;
block|}
DECL|method|add (Widget widget)
name|void
name|add
parameter_list|(
name|Widget
name|widget
parameter_list|)
block|{
operator|(
operator|(
name|HTMLPanel
operator|)
name|getWidget
argument_list|()
operator|)
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

