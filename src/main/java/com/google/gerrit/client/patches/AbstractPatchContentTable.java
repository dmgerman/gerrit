begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|FormatUtil
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
name|SignedInListener
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
name|PatchLineComment
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
name|ui
operator|.
name|ComplexDisclosurePanel
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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|DOM
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
name|FlexTable
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
name|InlineLabel
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
name|KeyboardListener
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
name|gwtjsonrpc
operator|.
name|client
operator|.
name|VoidResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
DECL|class|AbstractPatchContentTable
specifier|public
specifier|abstract
class|class
name|AbstractPatchContentTable
extends|extends
name|FancyFlexTable
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|AGE
specifier|private
specifier|static
specifier|final
name|long
name|AGE
init|=
literal|7
operator|*
literal|24
operator|*
literal|60
operator|*
literal|60
operator|*
literal|1000L
decl_stmt|;
DECL|field|accountCache
specifier|protected
name|AccountInfoCache
name|accountCache
init|=
name|AccountInfoCache
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|patchKey
specifier|protected
name|Patch
operator|.
name|Key
name|patchKey
decl_stmt|;
DECL|field|versions
specifier|protected
name|List
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|versions
decl_stmt|;
DECL|field|aged
specifier|private
specifier|final
name|Timestamp
name|aged
init|=
operator|new
name|Timestamp
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|AGE
argument_list|)
decl_stmt|;
DECL|field|signedInListener
specifier|private
specifier|final
name|SignedInListener
name|signedInListener
init|=
operator|new
name|SignedInListener
argument_list|()
block|{
specifier|public
name|void
name|onSignIn
parameter_list|()
block|{
if|if
condition|(
name|patchKey
operator|!=
literal|null
condition|)
block|{
name|PatchUtil
operator|.
name|DETAIL_SVC
operator|.
name|myDrafts
argument_list|(
name|patchKey
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|List
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|result
parameter_list|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|bindDrafts
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|onSignOut
parameter_list|()
block|{
comment|// TODO we should probably confirm with the user before sign out starts
comment|// that its OK to sign out if any of our editors are unsaved.
comment|// (bug GERRIT-16)
comment|//
for|for
control|(
name|int
name|row
init|=
literal|0
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
name|int
name|nCells
init|=
name|table
operator|.
name|getCellCount
argument_list|(
name|row
argument_list|)
decl_stmt|;
name|int
name|inc
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|cell
init|=
literal|0
init|;
name|cell
operator|<
name|nCells
condition|;
name|cell
operator|++
control|)
block|{
if|if
condition|(
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|cell
argument_list|)
operator|instanceof
name|CommentEditorPanel
condition|)
block|{
name|destroyEditor
argument_list|(
name|table
argument_list|,
name|row
argument_list|,
name|cell
argument_list|)
expr_stmt|;
name|inc
operator|=
literal|0
expr_stmt|;
block|}
block|}
name|row
operator|+=
name|inc
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
DECL|method|AbstractPatchContentTable ()
specifier|protected
name|AbstractPatchContentTable
parameter_list|()
block|{
name|table
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-PatchContentTable"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|public
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|Gerrit
operator|.
name|addSignedInListener
argument_list|(
name|signedInListener
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|public
name|void
name|onUnload
parameter_list|()
block|{
name|Gerrit
operator|.
name|removeSignedInListener
argument_list|(
name|signedInListener
argument_list|)
expr_stmt|;
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createFlexTable ()
specifier|protected
name|MyFlexTable
name|createFlexTable
parameter_list|()
block|{
return|return
operator|new
name|DoubleClickFlexTable
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final char keyCode, final int modifiers)
specifier|protected
name|boolean
name|onKeyPress
parameter_list|(
specifier|final
name|char
name|keyCode
parameter_list|,
specifier|final
name|int
name|modifiers
parameter_list|)
block|{
if|if
condition|(
name|modifiers
operator|==
literal|0
condition|)
block|{
switch|switch
condition|(
name|keyCode
condition|)
block|{
case|case
name|KeyboardListener
operator|.
name|KEY_UP
case|:
case|case
name|KeyboardListener
operator|.
name|KEY_DOWN
case|:
return|return
literal|false
return|;
block|}
block|}
return|return
name|super
operator|.
name|onKeyPress
argument_list|(
name|keyCode
argument_list|,
name|modifiers
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRowItemKey (final Object item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
specifier|final
name|Object
name|item
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
comment|/** Invoked when the user clicks on a table cell. */
DECL|method|onCellDoubleClick (int row, int column)
specifier|protected
specifier|abstract
name|void
name|onCellDoubleClick
parameter_list|(
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
function_decl|;
DECL|method|bindDrafts (List<PatchLineComment> drafts)
specifier|protected
specifier|abstract
name|void
name|bindDrafts
parameter_list|(
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|drafts
parameter_list|)
function_decl|;
DECL|method|initVersions (int fileCnt)
specifier|protected
name|void
name|initVersions
parameter_list|(
name|int
name|fileCnt
parameter_list|)
block|{
if|if
condition|(
name|versions
operator|==
literal|null
condition|)
block|{
name|versions
operator|=
operator|new
name|ArrayList
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|file
init|=
literal|0
init|;
name|file
operator|<
name|fileCnt
operator|-
literal|1
condition|;
name|file
operator|++
control|)
block|{
name|versions
operator|.
name|add
argument_list|(
name|PatchSet
operator|.
name|BASE
argument_list|)
expr_stmt|;
block|}
name|versions
operator|.
name|add
argument_list|(
name|patchKey
operator|.
name|getParentKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getVersions ()
specifier|protected
name|List
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|>
name|getVersions
parameter_list|()
block|{
return|return
name|versions
return|;
block|}
DECL|method|getVersion (final int fileId)
specifier|protected
name|PatchSet
operator|.
name|Id
name|getVersion
parameter_list|(
specifier|final
name|int
name|fileId
parameter_list|)
block|{
return|return
name|versions
operator|.
name|get
argument_list|(
name|fileId
argument_list|)
return|;
block|}
DECL|method|setVersion (final int fileId, final PatchSet.Id v)
specifier|protected
name|void
name|setVersion
parameter_list|(
specifier|final
name|int
name|fileId
parameter_list|,
specifier|final
name|PatchSet
operator|.
name|Id
name|v
parameter_list|)
block|{
name|versions
operator|.
name|set
argument_list|(
name|fileId
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
DECL|method|fileFor (final PatchLineComment c)
specifier|protected
name|int
name|fileFor
parameter_list|(
specifier|final
name|PatchLineComment
name|c
parameter_list|)
block|{
name|int
name|fileId
decl_stmt|;
for|for
control|(
name|fileId
operator|=
literal|0
init|;
name|fileId
operator|<
name|versions
operator|.
name|size
argument_list|()
condition|;
name|fileId
operator|++
control|)
block|{
specifier|final
name|PatchSet
operator|.
name|Id
name|i
init|=
name|versions
operator|.
name|get
argument_list|(
name|fileId
argument_list|)
decl_stmt|;
if|if
condition|(
name|PatchSet
operator|.
name|BASE
operator|.
name|equals
argument_list|(
name|i
argument_list|)
operator|&&
name|c
operator|.
name|getSide
argument_list|()
operator|==
name|fileId
operator|&&
name|patchKey
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|c
operator|.
name|getSide
argument_list|()
operator|==
name|versions
operator|.
name|size
argument_list|()
operator|-
literal|1
operator|&&
name|i
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
return|return
name|fileId
return|;
block|}
DECL|method|createCommentEditor (final int suggestRow, final int column, final int line, final short file)
specifier|protected
name|void
name|createCommentEditor
parameter_list|(
specifier|final
name|int
name|suggestRow
parameter_list|,
specifier|final
name|int
name|column
parameter_list|,
specifier|final
name|int
name|line
parameter_list|,
specifier|final
name|short
name|file
parameter_list|)
block|{
name|int
name|row
init|=
name|suggestRow
decl_stmt|;
name|int
name|spans
index|[]
init|=
operator|new
name|int
index|[
name|column
operator|+
literal|1
index|]
decl_stmt|;
name|OUTER
label|:
while|while
condition|(
name|row
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
condition|)
block|{
name|int
name|col
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|cell
init|=
literal|0
init|;
name|cell
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|row
argument_list|)
condition|;
name|cell
operator|++
control|)
block|{
while|while
condition|(
name|col
operator|<
name|column
operator|&&
literal|0
operator|<
name|spans
index|[
name|col
index|]
condition|)
block|{
name|spans
index|[
name|col
operator|++
index|]
operator|--
expr_stmt|;
block|}
name|spans
index|[
name|col
index|]
operator|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|getRowSpan
argument_list|(
name|row
argument_list|,
name|cell
argument_list|)
expr_stmt|;
if|if
condition|(
name|col
operator|==
name|column
condition|)
block|{
if|if
condition|(
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|cell
argument_list|)
operator|instanceof
name|ComplexDisclosurePanel
condition|)
block|{
name|row
operator|++
expr_stmt|;
block|}
else|else
block|{
break|break
name|OUTER
break|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|column
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|row
argument_list|)
operator|&&
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
operator|instanceof
name|CommentEditorPanel
condition|)
block|{
comment|// Don't insert two editors on the same position, it doesn't make
comment|// any sense to the user.
comment|//
operator|(
operator|(
name|CommentEditorPanel
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
operator|)
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
condition|)
block|{
name|Gerrit
operator|.
name|doSignIn
argument_list|(
operator|new
name|AsyncCallback
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
name|createCommentEditor
argument_list|(
name|suggestRow
argument_list|,
name|column
argument_list|,
name|line
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|caught
parameter_list|)
block|{         }
block|}
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|Patch
operator|.
name|Key
name|parentKey
decl_stmt|;
specifier|final
name|short
name|side
decl_stmt|;
if|if
condition|(
name|PatchSet
operator|.
name|BASE
operator|.
name|equals
argument_list|(
name|getVersion
argument_list|(
name|file
argument_list|)
argument_list|)
condition|)
block|{
name|parentKey
operator|=
name|patchKey
expr_stmt|;
name|side
operator|=
name|file
expr_stmt|;
block|}
else|else
block|{
name|parentKey
operator|=
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|getVersion
argument_list|(
name|file
argument_list|)
argument_list|,
name|patchKey
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|side
operator|=
operator|(
name|short
operator|)
literal|1
expr_stmt|;
block|}
specifier|final
name|PatchLineComment
name|newComment
init|=
operator|new
name|PatchLineComment
argument_list|(
operator|new
name|PatchLineComment
operator|.
name|Key
argument_list|(
name|parentKey
argument_list|,
literal|null
argument_list|)
argument_list|,
name|line
argument_list|,
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|newComment
operator|.
name|setSide
argument_list|(
name|side
argument_list|)
expr_stmt|;
name|newComment
operator|.
name|setMessage
argument_list|(
literal|""
argument_list|)
expr_stmt|;
specifier|final
name|CommentEditorPanel
name|ed
init|=
operator|new
name|CommentEditorPanel
argument_list|(
name|newComment
argument_list|)
decl_stmt|;
name|boolean
name|needInsert
init|=
literal|true
decl_stmt|;
for|for
control|(
name|int
name|cell
init|=
literal|0
init|;
name|cell
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|row
argument_list|)
condition|;
name|cell
operator|++
control|)
block|{
specifier|final
name|Widget
name|w
init|=
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|cell
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|instanceof
name|CommentEditorPanel
operator|||
name|w
operator|instanceof
name|ComplexDisclosurePanel
condition|)
block|{
name|needInsert
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|needInsert
condition|)
block|{
name|table
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|table
operator|.
name|getCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
literal|0
argument_list|,
name|S_ICON_CELL
argument_list|)
expr_stmt|;
block|}
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|ed
argument_list|)
expr_stmt|;
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
literal|"Comment"
argument_list|)
expr_stmt|;
name|int
name|span
init|=
literal|1
decl_stmt|;
for|for
control|(
name|int
name|r
init|=
name|row
operator|+
literal|1
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
name|boolean
name|hasComment
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|r
argument_list|)
condition|;
name|c
operator|++
control|)
block|{
specifier|final
name|Widget
name|w
init|=
name|table
operator|.
name|getWidget
argument_list|(
name|r
argument_list|,
name|c
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|instanceof
name|ComplexDisclosurePanel
operator|||
name|w
operator|instanceof
name|CommentEditorPanel
condition|)
block|{
name|hasComment
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|hasComment
condition|)
block|{
name|table
operator|.
name|removeCell
argument_list|(
name|r
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|span
operator|++
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
if|if
condition|(
name|span
operator|>
literal|1
condition|)
block|{
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setRowSpan
argument_list|(
name|row
argument_list|,
name|column
argument_list|,
name|span
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|r
init|=
name|row
operator|-
literal|1
init|;
name|r
operator|>
literal|0
condition|;
name|r
operator|--
control|)
block|{
if|if
condition|(
name|getRowItem
argument_list|(
name|r
argument_list|)
operator|instanceof
name|CommentList
condition|)
block|{
continue|continue;
block|}
elseif|else
if|if
condition|(
name|getRowItem
argument_list|(
name|r
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|movePointerTo
argument_list|(
name|r
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|ed
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onOpenItem (final Object item)
specifier|protected
name|void
name|onOpenItem
parameter_list|(
specifier|final
name|Object
name|item
parameter_list|)
block|{
if|if
condition|(
name|item
operator|instanceof
name|CommentList
condition|)
block|{
for|for
control|(
specifier|final
name|ComplexDisclosurePanel
name|p
range|:
operator|(
operator|(
name|CommentList
operator|)
name|item
operator|)
operator|.
name|panels
control|)
block|{
name|p
operator|.
name|setOpen
argument_list|(
operator|!
name|p
operator|.
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setAccountInfoCache (final AccountInfoCache aic)
specifier|public
name|void
name|setAccountInfoCache
parameter_list|(
specifier|final
name|AccountInfoCache
name|aic
parameter_list|)
block|{
assert|assert
name|aic
operator|!=
literal|null
assert|;
name|accountCache
operator|=
name|aic
expr_stmt|;
block|}
DECL|method|setPatchKey (final Patch.Key id)
specifier|public
name|void
name|setPatchKey
parameter_list|(
specifier|final
name|Patch
operator|.
name|Key
name|id
parameter_list|)
block|{
name|patchKey
operator|=
name|id
expr_stmt|;
block|}
DECL|method|destroyEditor (final FlexTable table, final int row, final int col)
specifier|static
name|void
name|destroyEditor
parameter_list|(
specifier|final
name|FlexTable
name|table
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
name|table
operator|.
name|clearCell
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
expr_stmt|;
specifier|final
name|int
name|span
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|getRowSpan
argument_list|(
name|row
argument_list|,
name|col
argument_list|)
decl_stmt|;
name|boolean
name|removeRow
init|=
literal|true
decl_stmt|;
specifier|final
name|int
name|nCells
init|=
name|table
operator|.
name|getCellCount
argument_list|(
name|row
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|cell
init|=
literal|0
init|;
name|cell
operator|<
name|nCells
condition|;
name|cell
operator|++
control|)
block|{
if|if
condition|(
name|table
operator|.
name|getWidget
argument_list|(
name|row
argument_list|,
name|cell
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|removeRow
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|removeRow
condition|)
block|{
for|for
control|(
name|int
name|r
init|=
name|row
operator|-
literal|1
init|;
literal|0
operator|<=
name|r
condition|;
name|r
operator|--
control|)
block|{
name|boolean
name|data
init|=
literal|false
decl_stmt|;
for|for
control|(
name|int
name|c
init|=
literal|0
init|;
name|c
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|r
argument_list|)
condition|;
name|c
operator|++
control|)
block|{
name|data
operator||=
name|table
operator|.
name|getWidget
argument_list|(
name|r
argument_list|,
name|c
argument_list|)
operator|!=
literal|null
expr_stmt|;
specifier|final
name|int
name|s
init|=
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|getRowSpan
argument_list|(
name|r
argument_list|,
name|c
argument_list|)
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|r
operator|+
name|s
operator|==
name|row
condition|)
block|{
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setRowSpan
argument_list|(
name|r
argument_list|,
name|c
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|data
condition|)
block|{
break|break;
block|}
block|}
name|table
operator|.
name|removeRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|span
operator|!=
literal|1
condition|)
block|{
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setRowSpan
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|r
init|=
name|row
operator|+
literal|1
init|;
name|r
operator|<
name|row
operator|+
name|span
condition|;
name|r
operator|++
control|)
block|{
name|table
operator|.
name|insertCell
argument_list|(
name|r
argument_list|,
name|col
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|bindComment (final int row, final int col, final PatchLineComment line, final boolean isLast)
specifier|protected
name|void
name|bindComment
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|int
name|col
parameter_list|,
specifier|final
name|PatchLineComment
name|line
parameter_list|,
specifier|final
name|boolean
name|isLast
parameter_list|)
block|{
if|if
condition|(
name|line
operator|.
name|getStatus
argument_list|()
operator|==
name|PatchLineComment
operator|.
name|Status
operator|.
name|DRAFT
condition|)
block|{
name|boolean
name|takeFocus
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|row
operator|+
literal|1
operator|<
name|table
operator|.
name|getRowCount
argument_list|()
operator|&&
name|col
operator|<
name|table
operator|.
name|getCellCount
argument_list|(
name|row
operator|+
literal|1
argument_list|)
operator|&&
name|table
operator|.
name|getWidget
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|col
argument_list|)
operator|instanceof
name|CommentEditorPanel
operator|&&
operator|(
operator|(
name|CommentEditorPanel
operator|)
name|table
operator|.
name|getWidget
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|col
argument_list|)
operator|)
operator|.
name|isNew
argument_list|()
condition|)
block|{
comment|// Assume the second panel is a new one created while logging in;
comment|// this line is from the myDrafts callback and we discovered we
comment|// already have a draft at this location. We want to destroy the
comment|// dummy editor and keep the real one.
comment|//
name|destroyEditor
argument_list|(
name|table
argument_list|,
name|row
operator|+
literal|1
argument_list|,
name|col
argument_list|)
expr_stmt|;
name|takeFocus
operator|=
literal|true
expr_stmt|;
block|}
specifier|final
name|CommentEditorPanel
name|plc
init|=
operator|new
name|CommentEditorPanel
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|table
operator|.
name|setWidget
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|plc
argument_list|)
expr_stmt|;
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
literal|"Comment"
argument_list|)
expr_stmt|;
if|if
condition|(
name|takeFocus
condition|)
block|{
name|plc
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
specifier|final
name|LineCommentPanel
name|mp
init|=
operator|new
name|LineCommentPanel
argument_list|(
name|line
argument_list|)
decl_stmt|;
name|String
name|panelHeader
decl_stmt|;
specifier|final
name|ComplexDisclosurePanel
name|panel
decl_stmt|;
if|if
condition|(
name|line
operator|.
name|getAuthor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|panelHeader
operator|=
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|accountCache
operator|.
name|get
argument_list|(
name|line
operator|.
name|getAuthor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|panelHeader
operator|=
name|Util
operator|.
name|C
operator|.
name|messageNoAuthor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isLast
condition|)
block|{
name|mp
operator|.
name|isRecent
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// TODO Instead of opening messages by strict age, do it by "unread"?
name|mp
operator|.
name|isRecent
operator|=
name|line
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|after
argument_list|(
name|aged
argument_list|)
expr_stmt|;
block|}
name|panel
operator|=
operator|new
name|ComplexDisclosurePanel
argument_list|(
name|panelHeader
argument_list|,
name|mp
operator|.
name|isRecent
argument_list|)
expr_stmt|;
name|panel
operator|.
name|getHeader
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|InlineLabel
argument_list|(
name|Util
operator|.
name|M
operator|.
name|messageWrittenOn
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|line
operator|.
name|getWrittenOn
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|.
name|setContent
argument_list|(
name|mp
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
name|panel
argument_list|)
expr_stmt|;
name|table
operator|.
name|getFlexCellFormatter
argument_list|()
operator|.
name|setStyleName
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
literal|"Comment"
argument_list|)
expr_stmt|;
name|CommentList
name|l
init|=
operator|(
name|CommentList
operator|)
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|==
literal|null
condition|)
block|{
name|l
operator|=
operator|new
name|CommentList
argument_list|()
expr_stmt|;
name|setRowItem
argument_list|(
name|row
argument_list|,
name|l
argument_list|)
expr_stmt|;
block|}
name|l
operator|.
name|comments
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|l
operator|.
name|panels
operator|.
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
block|}
DECL|class|CommentList
specifier|protected
specifier|static
class|class
name|CommentList
block|{
DECL|field|comments
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|panels
specifier|final
name|List
argument_list|<
name|ComplexDisclosurePanel
argument_list|>
name|panels
init|=
operator|new
name|ArrayList
argument_list|<
name|ComplexDisclosurePanel
argument_list|>
argument_list|()
decl_stmt|;
block|}
DECL|class|DoubleClickFlexTable
specifier|protected
class|class
name|DoubleClickFlexTable
extends|extends
name|MyFlexTable
block|{
DECL|method|DoubleClickFlexTable ()
specifier|public
name|DoubleClickFlexTable
parameter_list|()
block|{
name|sinkEvents
argument_list|(
name|Event
operator|.
name|ONDBLCLICK
operator||
name|Event
operator|.
name|ONCLICK
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onBrowserEvent (final Event event)
specifier|public
name|void
name|onBrowserEvent
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
switch|switch
condition|(
name|DOM
operator|.
name|eventGetType
argument_list|(
name|event
argument_list|)
condition|)
block|{
case|case
name|Event
operator|.
name|ONCLICK
case|:
block|{
comment|// Find out which cell was actually clicked.
specifier|final
name|Element
name|td
init|=
name|getEventTargetCell
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|td
operator|==
literal|null
condition|)
block|{
break|break;
block|}
specifier|final
name|Element
name|tr
init|=
name|DOM
operator|.
name|getParent
argument_list|(
name|td
argument_list|)
decl_stmt|;
specifier|final
name|Element
name|body
init|=
name|DOM
operator|.
name|getParent
argument_list|(
name|tr
argument_list|)
decl_stmt|;
specifier|final
name|int
name|row
init|=
name|DOM
operator|.
name|getChildIndex
argument_list|(
name|body
argument_list|,
name|tr
argument_list|)
decl_stmt|;
if|if
condition|(
name|getRowItem
argument_list|(
name|row
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|movePointerTo
argument_list|(
name|row
argument_list|)
expr_stmt|;
return|return;
block|}
break|break;
block|}
case|case
name|Event
operator|.
name|ONDBLCLICK
case|:
block|{
comment|// Find out which cell was actually clicked.
name|Element
name|td
init|=
name|getEventTargetCell
argument_list|(
name|event
argument_list|)
decl_stmt|;
if|if
condition|(
name|td
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|Element
name|tr
init|=
name|DOM
operator|.
name|getParent
argument_list|(
name|td
argument_list|)
decl_stmt|;
name|Element
name|body
init|=
name|DOM
operator|.
name|getParent
argument_list|(
name|tr
argument_list|)
decl_stmt|;
name|int
name|row
init|=
name|DOM
operator|.
name|getChildIndex
argument_list|(
name|body
argument_list|,
name|tr
argument_list|)
decl_stmt|;
name|int
name|column
init|=
name|DOM
operator|.
name|getChildIndex
argument_list|(
name|tr
argument_list|,
name|td
argument_list|)
decl_stmt|;
name|onCellDoubleClick
argument_list|(
name|row
argument_list|,
name|column
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|super
operator|.
name|onBrowserEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

