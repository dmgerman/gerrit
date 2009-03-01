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
name|data
operator|.
name|PatchLine
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
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|SafeHtmlBuilder
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
DECL|class|UnifiedDiffTable
specifier|public
class|class
name|UnifiedDiffTable
extends|extends
name|AbstractPatchContentTable
block|{
DECL|field|PC
specifier|private
specifier|static
specifier|final
name|int
name|PC
init|=
literal|3
decl_stmt|;
annotation|@
name|Override
DECL|method|onCellDoubleClick (final int row, final int column)
specifier|protected
name|void
name|onCellDoubleClick
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
specifier|final
name|int
name|column
parameter_list|)
block|{
if|if
condition|(
name|getRowItem
argument_list|(
name|row
argument_list|)
operator|instanceof
name|PatchLine
condition|)
block|{
specifier|final
name|PatchLine
name|pl
init|=
operator|(
name|PatchLine
operator|)
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|pl
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|PRE_IMAGE
case|:
case|case
name|CONTEXT
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|PC
argument_list|,
name|pl
operator|.
name|getOldLineNumber
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|POST_IMAGE
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|PC
argument_list|,
name|pl
operator|.
name|getNewLineNumber
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
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
name|PatchLine
condition|)
block|{
specifier|final
name|PatchLine
name|pl
init|=
operator|(
name|PatchLine
operator|)
name|item
decl_stmt|;
specifier|final
name|int
name|row
init|=
name|getCurrentRow
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|pl
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|PRE_IMAGE
case|:
case|case
name|CONTEXT
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|PC
argument_list|,
name|pl
operator|.
name|getOldLineNumber
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
break|break;
case|case
name|POST_IMAGE
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|PC
argument_list|,
name|pl
operator|.
name|getOldLineNumber
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
break|break;
block|}
return|return;
block|}
name|super
operator|.
name|onOpenItem
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindDrafts (final List<PatchLineComment> drafts)
specifier|protected
name|void
name|bindDrafts
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|drafts
parameter_list|)
block|{
name|int
name|row
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|PatchLineComment
name|c
range|:
name|drafts
control|)
block|{
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
if|if
condition|(
name|getRowItem
argument_list|(
name|row
argument_list|)
operator|instanceof
name|PatchLine
condition|)
block|{
specifier|final
name|PatchLine
name|pl
init|=
operator|(
name|PatchLine
operator|)
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
if|if
condition|(
name|pl
operator|.
name|getOldLineNumber
argument_list|()
operator|>=
name|c
operator|.
name|getLine
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
name|row
operator|++
expr_stmt|;
block|}
name|table
operator|.
name|insertRow
argument_list|(
name|row
operator|+
literal|1
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
operator|+
literal|1
argument_list|,
literal|0
argument_list|,
name|S_ICON_CELL
argument_list|)
expr_stmt|;
name|bindComment
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|PC
argument_list|,
name|c
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final List<PatchLine> list)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLine
argument_list|>
name|list
parameter_list|)
block|{
name|initVersions
argument_list|(
literal|2
argument_list|)
expr_stmt|;
specifier|final
name|SafeHtmlBuilder
name|nc
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|PatchLine
name|pLine
range|:
name|list
control|)
block|{
name|appendLine
argument_list|(
name|nc
argument_list|,
name|pLine
argument_list|)
expr_stmt|;
block|}
name|resetHtml
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|int
name|row
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|PatchLine
name|pLine
range|:
name|list
control|)
block|{
name|setRowItem
argument_list|(
name|row
argument_list|,
name|pLine
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|pLine
operator|.
name|getComments
argument_list|()
decl_stmt|;
if|if
condition|(
name|comments
operator|!=
literal|null
condition|)
block|{
for|for
control|(
specifier|final
name|Iterator
argument_list|<
name|PatchLineComment
argument_list|>
name|ci
init|=
name|comments
operator|.
name|iterator
argument_list|()
init|;
name|ci
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
specifier|final
name|PatchLineComment
name|c
init|=
name|ci
operator|.
name|next
argument_list|()
decl_stmt|;
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
name|bindComment
argument_list|(
name|row
argument_list|,
name|PC
argument_list|,
name|c
argument_list|,
operator|!
name|ci
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|appendLine (final SafeHtmlBuilder m, final PatchLine line)
specifier|private
name|void
name|appendLine
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|PatchLine
name|line
parameter_list|)
block|{
name|m
operator|.
name|openTr
argument_list|()
expr_stmt|;
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
name|S_ICON_CELL
argument_list|)
expr_stmt|;
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|line
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|FILE_HEADER
case|:
case|case
name|HUNK_HEADER
case|:
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
literal|"LineNumber"
argument_list|)
expr_stmt|;
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
literal|"LineNumber"
argument_list|)
expr_stmt|;
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
break|break;
default|default:
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
literal|"LineNumber"
argument_list|)
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|getOldLineNumber
argument_list|()
operator|!=
literal|0
operator|&&
operator|(
name|line
operator|.
name|getType
argument_list|()
operator|==
name|PatchLine
operator|.
name|Type
operator|.
name|CONTEXT
operator|||
name|line
operator|.
name|getType
argument_list|()
operator|==
name|PatchLine
operator|.
name|Type
operator|.
name|PRE_IMAGE
operator|)
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
name|line
operator|.
name|getOldLineNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
literal|"LineNumber"
argument_list|)
expr_stmt|;
if|if
condition|(
name|line
operator|.
name|getNewLineNumber
argument_list|()
operator|!=
literal|0
operator|&&
operator|(
name|line
operator|.
name|getType
argument_list|()
operator|==
name|PatchLine
operator|.
name|Type
operator|.
name|CONTEXT
operator|||
name|line
operator|.
name|getType
argument_list|()
operator|==
name|PatchLine
operator|.
name|Type
operator|.
name|POST_IMAGE
operator|)
condition|)
block|{
name|m
operator|.
name|append
argument_list|(
name|line
operator|.
name|getNewLineNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
break|break;
block|}
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|addStyleName
argument_list|(
literal|"DiffText"
argument_list|)
expr_stmt|;
name|m
operator|.
name|addStyleName
argument_list|(
literal|"DiffText-"
operator|+
name|line
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|line
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|boolean
name|showWhitespaceErrors
init|=
literal|false
decl_stmt|;
switch|switch
condition|(
name|line
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|POST_IMAGE
case|:
comment|// Only show whitespace errors if the error was introduced.
comment|//
name|showWhitespaceErrors
operator|=
literal|true
expr_stmt|;
break|break;
block|}
name|m
operator|.
name|append
argument_list|(
name|PatchUtil
operator|.
name|lineToSafeHtml
argument_list|(
name|line
operator|.
name|getText
argument_list|()
argument_list|,
literal|0
argument_list|,
name|showWhitespaceErrors
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
block|}
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|closeTr
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

