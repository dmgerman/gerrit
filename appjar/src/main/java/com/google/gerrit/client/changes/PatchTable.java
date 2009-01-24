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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|ui
operator|.
name|DomUtil
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
name|ProgressMeter
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
name|DeferredCommand
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
name|IncrementalCommand
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
name|SourcesTableEvents
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
name|TableListener
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
DECL|class|PatchTable
specifier|public
class|class
name|PatchTable
extends|extends
name|FancyFlexTable
argument_list|<
name|Patch
argument_list|>
block|{
DECL|field|psid
specifier|private
name|PatchSet
operator|.
name|Id
name|psid
decl_stmt|;
DECL|method|PatchTable ()
specifier|public
name|PatchTable
parameter_list|()
block|{
name|table
operator|.
name|addTableListener
argument_list|(
operator|new
name|TableListener
argument_list|()
block|{
specifier|public
name|void
name|onCellClicked
parameter_list|(
name|SourcesTableEvents
name|sender
parameter_list|,
name|int
name|row
parameter_list|,
name|int
name|cell
parameter_list|)
block|{
if|if
condition|(
name|row
operator|>
literal|0
condition|)
block|{
name|movePointerTo
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final PatchSet.Id id, final List<Patch> list)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|PatchSet
operator|.
name|Id
name|id
parameter_list|,
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|list
parameter_list|)
block|{
name|psid
operator|=
name|id
expr_stmt|;
specifier|final
name|DisplayCommand
name|cmd
init|=
operator|new
name|DisplayCommand
argument_list|(
name|list
argument_list|)
decl_stmt|;
if|if
condition|(
name|cmd
operator|.
name|execute
argument_list|()
condition|)
block|{
name|cmd
operator|.
name|initMeter
argument_list|()
expr_stmt|;
name|DeferredCommand
operator|.
name|addCommand
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|appendHeader (final StringBuilder nc)
specifier|private
name|void
name|appendHeader
parameter_list|(
specifier|final
name|StringBuilder
name|nc
parameter_list|)
block|{
name|nc
operator|.
name|append
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_ICON_HEADER
operator|+
literal|" LeftMostCell\">&nbsp;</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_ICON_HEADER
operator|+
literal|"\">&nbsp;</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_HEADER
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|patchTableColumnName
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_HEADER
operator|+
literal|"\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|patchTableColumnComments
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_HEADER
operator|+
literal|"\" colspan=\"2\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|patchTableColumnDiff
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
DECL|method|appendRow (final StringBuilder nc, final Patch p)
specifier|private
name|void
name|appendRow
parameter_list|(
specifier|final
name|StringBuilder
name|nc
parameter_list|,
specifier|final
name|Patch
name|p
parameter_list|)
block|{
name|nc
operator|.
name|append
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_ICON_CELL
operator|+
literal|" LeftMostCell\">&nbsp;</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\"ChangeTypeCell\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|p
operator|.
name|getChangeType
argument_list|()
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_CELL
operator|+
literal|" FilePathCell\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<a href=\"#"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|getPatchType
argument_list|()
operator|==
name|Patch
operator|.
name|PatchType
operator|.
name|UNIFIED
condition|)
block|{
name|nc
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toPatchSideBySide
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|nc
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toPatchUnified
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|DomUtil
operator|.
name|escape
argument_list|(
name|p
operator|.
name|getFileName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</a>"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|getSourceFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|secondLine
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|getChangeType
argument_list|()
operator|==
name|Patch
operator|.
name|ChangeType
operator|.
name|RENAMED
condition|)
block|{
name|secondLine
operator|=
name|Util
operator|.
name|M
operator|.
name|renamedFrom
argument_list|(
name|p
operator|.
name|getSourceFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|p
operator|.
name|getChangeType
argument_list|()
operator|==
name|Patch
operator|.
name|ChangeType
operator|.
name|COPIED
condition|)
block|{
name|secondLine
operator|=
name|Util
operator|.
name|M
operator|.
name|copiedFrom
argument_list|(
name|p
operator|.
name|getSourceFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|secondLine
operator|=
name|Util
operator|.
name|M
operator|.
name|otherFrom
argument_list|(
name|p
operator|.
name|getSourceFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"<br>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<span class=\"SourceFilePath\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|DomUtil
operator|.
name|escape
argument_list|(
name|secondLine
argument_list|)
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</span>"
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_CELL
operator|+
literal|" CommentCell\">"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|getCommentCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|patchTableComments
argument_list|(
name|p
operator|.
name|getCommentCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|getDraftCount
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|p
operator|.
name|getCommentCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|nc
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"<span class=\"Drafts\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|M
operator|.
name|patchTableDrafts
argument_list|(
name|p
operator|.
name|getDraftCount
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</span>"
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_CELL
operator|+
literal|" DiffLinkCell\">"
argument_list|)
expr_stmt|;
if|if
condition|(
name|p
operator|.
name|getPatchType
argument_list|()
operator|==
name|Patch
operator|.
name|PatchType
operator|.
name|UNIFIED
condition|)
block|{
name|nc
operator|.
name|append
argument_list|(
literal|"<a href=\"#"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toPatchSideBySide
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|patchTableDiffSideBySide
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</a>"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|nc
operator|.
name|append
argument_list|(
literal|"&nbsp;"
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<td class=\""
operator|+
name|S_DATA_CELL
operator|+
literal|" DiffLinkCell\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"<a href=\"#"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Link
operator|.
name|toPatchUnified
argument_list|(
name|p
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"\">"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|patchTableDiffUnified
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</a>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</td>"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|append
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRowItemKey (final Patch item)
specifier|protected
name|Object
name|getRowItemKey
parameter_list|(
specifier|final
name|Patch
name|item
parameter_list|)
block|{
return|return
name|item
operator|.
name|getKey
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onOpenItem (final Patch item)
specifier|protected
name|void
name|onOpenItem
parameter_list|(
specifier|final
name|Patch
name|item
parameter_list|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Link
operator|.
name|toPatchSideBySide
argument_list|(
name|item
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|DisplayCommand
specifier|private
specifier|final
class|class
name|DisplayCommand
implements|implements
name|IncrementalCommand
block|{
DECL|field|list
specifier|private
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|list
decl_stmt|;
DECL|field|attached
specifier|private
name|boolean
name|attached
decl_stmt|;
DECL|field|nc
specifier|private
name|StringBuilder
name|nc
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|field|stage
specifier|private
name|int
name|stage
decl_stmt|;
DECL|field|row
specifier|private
name|int
name|row
decl_stmt|;
DECL|field|start
specifier|private
name|double
name|start
decl_stmt|;
DECL|field|meter
specifier|private
name|ProgressMeter
name|meter
decl_stmt|;
DECL|method|DisplayCommand (final List<Patch> list)
specifier|private
name|DisplayCommand
parameter_list|(
specifier|final
name|List
argument_list|<
name|Patch
argument_list|>
name|list
parameter_list|)
block|{
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"fallthrough"
argument_list|)
DECL|method|execute ()
specifier|public
name|boolean
name|execute
parameter_list|()
block|{
specifier|final
name|boolean
name|attachedNow
init|=
name|isAttached
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|attached
operator|&&
name|attachedNow
condition|)
block|{
comment|// Remember that we have been attached at least once. If
comment|// later we find we aren't attached we should stop running.
comment|//
name|attached
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|attached
operator|&&
operator|!
name|attachedNow
condition|)
block|{
comment|// If the user navigated away, we aren't in the DOM anymore.
comment|// Don't continue to render.
comment|//
return|return
literal|false
return|;
block|}
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|stage
condition|)
block|{
case|case
literal|0
case|:
if|if
condition|(
name|row
operator|==
literal|0
condition|)
block|{
name|appendHeader
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|row
operator|<
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
name|appendRow
argument_list|(
name|nc
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|++
name|row
operator|%
literal|10
operator|)
operator|==
literal|0
operator|&&
name|longRunning
argument_list|()
condition|)
block|{
name|updateMeter
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
name|resetHtml
argument_list|(
name|nc
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|nc
operator|=
literal|null
expr_stmt|;
name|meter
operator|=
literal|null
expr_stmt|;
name|stage
operator|=
literal|1
expr_stmt|;
name|row
operator|=
literal|0
expr_stmt|;
case|case
literal|1
case|:
while|while
condition|(
name|row
operator|<
name|list
operator|.
name|size
argument_list|()
condition|)
block|{
name|setRowItem
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|list
operator|.
name|get
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
operator|++
name|row
operator|%
literal|10
operator|)
operator|==
literal|0
operator|&&
name|longRunning
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
name|finishDisplay
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|initMeter ()
name|void
name|initMeter
parameter_list|()
block|{
if|if
condition|(
name|meter
operator|==
literal|null
condition|)
block|{
name|resetHtml
argument_list|(
literal|"<tr><td></td></tr>"
argument_list|)
expr_stmt|;
name|meter
operator|=
operator|new
name|ProgressMeter
argument_list|(
name|Util
operator|.
name|M
operator|.
name|loadingPatchSet
argument_list|(
name|psid
operator|.
name|get
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
literal|0
argument_list|,
name|meter
argument_list|)
expr_stmt|;
block|}
name|updateMeter
argument_list|()
expr_stmt|;
block|}
DECL|method|updateMeter ()
name|void
name|updateMeter
parameter_list|()
block|{
if|if
condition|(
name|meter
operator|!=
literal|null
condition|)
block|{
name|meter
operator|.
name|setValue
argument_list|(
operator|(
literal|100
operator|*
name|row
operator|/
name|list
operator|.
name|size
argument_list|()
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|longRunning ()
specifier|private
name|boolean
name|longRunning
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
operator|>
literal|200
return|;
block|}
block|}
block|}
end_class

end_unit

