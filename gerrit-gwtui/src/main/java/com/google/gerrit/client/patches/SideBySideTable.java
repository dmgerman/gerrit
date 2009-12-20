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
name|patches
operator|.
name|PatchLine
operator|.
name|Type
operator|.
name|CONTEXT
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
name|patches
operator|.
name|PatchLine
operator|.
name|Type
operator|.
name|DELETE
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
name|patches
operator|.
name|PatchLine
operator|.
name|Type
operator|.
name|INSERT
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
name|patches
operator|.
name|PatchLine
operator|.
name|Type
operator|.
name|REPLACE
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
name|common
operator|.
name|data
operator|.
name|CommentDetail
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
name|EditList
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
name|common
operator|.
name|data
operator|.
name|SparseFileContent
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
name|PatchLineComment
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
name|PrettyFormatter
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
name|ArrayList
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
DECL|class|SideBySideTable
specifier|public
class|class
name|SideBySideTable
extends|extends
name|AbstractPatchContentTable
block|{
DECL|field|COL_A
specifier|private
specifier|static
specifier|final
name|int
name|COL_A
init|=
literal|2
decl_stmt|;
DECL|field|COL_B
specifier|private
specifier|static
specifier|final
name|int
name|COL_B
init|=
literal|4
decl_stmt|;
annotation|@
name|Override
DECL|method|onCellDoubleClick (final int row, int column)
specifier|protected
name|void
name|onCellDoubleClick
parameter_list|(
specifier|final
name|int
name|row
parameter_list|,
name|int
name|column
parameter_list|)
block|{
if|if
condition|(
name|column
operator|>
literal|0
operator|&&
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
name|line
init|=
operator|(
name|PatchLine
operator|)
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
specifier|final
name|short
name|file
init|=
call|(
name|short
call|)
argument_list|(
operator|(
name|column
operator|-
literal|1
operator|)
operator|/
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|column
operator|<
operator|(
literal|1
operator|+
name|file
operator|*
literal|2
operator|+
literal|1
operator|)
condition|)
block|{
name|column
operator|++
expr_stmt|;
block|}
switch|switch
condition|(
name|file
condition|)
block|{
case|case
literal|0
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|column
argument_list|,
name|line
operator|.
name|getLineA
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
name|column
argument_list|,
name|line
operator|.
name|getLineB
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onInsertComment (final PatchLine line)
specifier|protected
name|void
name|onInsertComment
parameter_list|(
specifier|final
name|PatchLine
name|line
parameter_list|)
block|{
specifier|final
name|int
name|row
init|=
name|getCurrentRow
argument_list|()
decl_stmt|;
name|createCommentEditor
argument_list|(
name|row
operator|+
literal|1
argument_list|,
literal|4
argument_list|,
name|line
operator|.
name|getLineB
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|render (final PatchScript script)
specifier|protected
name|void
name|render
parameter_list|(
specifier|final
name|PatchScript
name|script
parameter_list|)
block|{
specifier|final
name|SparseFileContent
name|a
init|=
name|script
operator|.
name|getA
argument_list|()
decl_stmt|;
specifier|final
name|SparseFileContent
name|b
init|=
name|script
operator|.
name|getB
argument_list|()
decl_stmt|;
specifier|final
name|PrettyFormatter
name|fmtA
init|=
name|PrettyFormatter
operator|.
name|newFormatter
argument_list|(
name|formatLanguage
argument_list|)
decl_stmt|;
specifier|final
name|PrettyFormatter
name|fmtB
init|=
name|PrettyFormatter
operator|.
name|newFormatter
argument_list|(
name|formatLanguage
argument_list|)
decl_stmt|;
specifier|final
name|ArrayList
argument_list|<
name|PatchLine
argument_list|>
name|lines
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLine
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|SafeHtmlBuilder
name|nc
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|fmtB
operator|.
name|setShowWhiteSpaceErrors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|appendHeader
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|lines
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|int
name|lastB
init|=
literal|0
decl_stmt|;
specifier|final
name|boolean
name|ignoreWS
init|=
name|script
operator|.
name|isIgnoreWhitespace
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|EditList
operator|.
name|Hunk
name|hunk
range|:
name|script
operator|.
name|getHunks
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|hunk
operator|.
name|isStartOfFile
argument_list|()
condition|)
block|{
name|appendSkipLine
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
operator|-
name|lastB
argument_list|)
expr_stmt|;
name|lines
operator|.
name|add
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|hunk
operator|.
name|next
argument_list|()
condition|)
block|{
if|if
condition|(
name|hunk
operator|.
name|isContextLine
argument_list|()
condition|)
block|{
name|openLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
specifier|final
name|SafeHtml
name|ctx
init|=
name|fmtA
operator|.
name|format
argument_list|(
name|a
operator|.
name|get
argument_list|(
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|CONTEXT
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
if|if
condition|(
name|ignoreWS
operator|&&
name|b
operator|.
name|contains
argument_list|(
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
condition|)
block|{
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|,
name|CONTEXT
argument_list|,
name|b
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|,
name|fmtB
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|,
name|CONTEXT
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
block|}
name|closeLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incBoth
argument_list|()
expr_stmt|;
name|lines
operator|.
name|add
argument_list|(
operator|new
name|PatchLine
argument_list|(
name|CONTEXT
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hunk
operator|.
name|isModifiedLine
argument_list|()
condition|)
block|{
specifier|final
name|boolean
name|del
init|=
name|hunk
operator|.
name|isDeletedA
argument_list|()
decl_stmt|;
specifier|final
name|boolean
name|ins
init|=
name|hunk
operator|.
name|isInsertedB
argument_list|()
decl_stmt|;
name|openLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
if|if
condition|(
name|del
condition|)
block|{
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|DELETE
argument_list|,
name|a
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|fmtA
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incA
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|appendLineNone
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ins
condition|)
block|{
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|,
name|INSERT
argument_list|,
name|b
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|,
name|fmtB
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incB
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|appendLineNone
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
name|closeLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
if|if
condition|(
name|del
operator|&&
name|ins
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
operator|new
name|PatchLine
argument_list|(
name|REPLACE
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|del
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
operator|new
name|PatchLine
argument_list|(
name|DELETE
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ins
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
operator|new
name|PatchLine
argument_list|(
name|INSERT
argument_list|,
literal|0
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|lastB
operator|=
name|hunk
operator|.
name|getCurB
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|lastB
operator|!=
name|b
operator|.
name|size
argument_list|()
condition|)
block|{
name|appendSkipLine
argument_list|(
name|nc
argument_list|,
name|b
operator|.
name|size
argument_list|()
operator|-
name|lastB
argument_list|)
expr_stmt|;
block|}
name|resetHtml
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|initScript
argument_list|(
name|script
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|row
init|=
literal|0
init|;
name|row
operator|<
name|lines
operator|.
name|size
argument_list|()
condition|;
name|row
operator|++
control|)
block|{
name|setRowItem
argument_list|(
name|row
argument_list|,
name|lines
operator|.
name|get
argument_list|(
name|row
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|display (final CommentDetail cd)
specifier|public
name|void
name|display
parameter_list|(
specifier|final
name|CommentDetail
name|cd
parameter_list|)
block|{
if|if
condition|(
name|cd
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|setAccountInfoCache
argument_list|(
name|cd
operator|.
name|getAccounts
argument_list|()
argument_list|)
expr_stmt|;
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
name|pLine
init|=
operator|(
name|PatchLine
operator|)
name|getRowItem
argument_list|(
name|row
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|fora
init|=
name|cd
operator|.
name|getForA
argument_list|(
name|pLine
operator|.
name|getLineA
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|forb
init|=
name|cd
operator|.
name|getForB
argument_list|(
name|pLine
operator|.
name|getLineB
argument_list|()
argument_list|)
decl_stmt|;
name|row
operator|++
expr_stmt|;
specifier|final
name|Iterator
argument_list|<
name|PatchLineComment
argument_list|>
name|ai
init|=
name|fora
operator|.
name|iterator
argument_list|()
decl_stmt|;
specifier|final
name|Iterator
argument_list|<
name|PatchLineComment
argument_list|>
name|bi
init|=
name|forb
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|ai
operator|.
name|hasNext
argument_list|()
operator|&&
name|bi
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|PatchLineComment
name|ac
init|=
name|ai
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|PatchLineComment
name|bc
init|=
name|bi
operator|.
name|next
argument_list|()
decl_stmt|;
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|bindComment
argument_list|(
name|row
argument_list|,
name|COL_A
argument_list|,
name|ac
argument_list|,
operator|!
name|ai
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|bindComment
argument_list|(
name|row
argument_list|,
name|COL_B
argument_list|,
name|bc
argument_list|,
operator|!
name|bi
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
block|}
name|row
operator|=
name|finish
argument_list|(
name|ai
argument_list|,
name|row
argument_list|,
name|COL_A
argument_list|)
expr_stmt|;
name|row
operator|=
name|finish
argument_list|(
name|bi
argument_list|,
name|row
argument_list|,
name|COL_B
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
annotation|@
name|Override
DECL|method|insertRow (final int row)
specifier|protected
name|void
name|insertRow
parameter_list|(
specifier|final
name|int
name|row
parameter_list|)
block|{
name|super
operator|.
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
specifier|final
name|CellFormatter
name|fmt
init|=
name|table
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|COL_A
operator|-
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|lineNumber
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|COL_A
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|diffText
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|COL_B
operator|-
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|lineNumber
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|addStyleName
argument_list|(
name|row
argument_list|,
name|COL_B
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|diffText
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|finish (final Iterator<PatchLineComment> i, int row, final int col)
specifier|private
name|int
name|finish
parameter_list|(
specifier|final
name|Iterator
argument_list|<
name|PatchLineComment
argument_list|>
name|i
parameter_list|,
name|int
name|row
parameter_list|,
specifier|final
name|int
name|col
parameter_list|)
block|{
while|while
condition|(
name|i
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|PatchLineComment
name|c
init|=
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|insertRow
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|bindComment
argument_list|(
name|row
argument_list|,
name|col
argument_list|,
name|c
argument_list|,
operator|!
name|i
operator|.
name|hasNext
argument_list|()
argument_list|)
expr_stmt|;
name|row
operator|++
expr_stmt|;
block|}
return|return
name|row
return|;
block|}
DECL|method|appendHeader (final SafeHtmlBuilder m)
specifier|private
name|void
name|appendHeader
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
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
name|addStyleName
argument_list|(
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
name|m
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
name|fileColumnHeader
argument_list|()
argument_list|)
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
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileColumnHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
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
name|lineNumber
argument_list|()
argument_list|)
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileColumnHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setAttribute
argument_list|(
literal|"width"
argument_list|,
literal|"50%"
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|patchHeaderOld
argument_list|()
argument_list|)
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
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileColumnHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
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
name|lineNumber
argument_list|()
argument_list|)
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileColumnHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setAttribute
argument_list|(
literal|"width"
argument_list|,
literal|"50%"
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|PatchUtil
operator|.
name|C
operator|.
name|patchHeaderNew
argument_list|()
argument_list|)
expr_stmt|;
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
DECL|method|appendSkipLine (final SafeHtmlBuilder m, final int skipCnt)
specifier|private
name|void
name|appendSkipLine
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|int
name|skipCnt
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|skipLine
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|setAttribute
argument_list|(
literal|"colspan"
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|patchSkipRegion
argument_list|(
name|skipCnt
argument_list|)
argument_list|)
expr_stmt|;
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
DECL|method|openLine (final SafeHtmlBuilder m)
specifier|private
name|void
name|openLine
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|)
block|{
name|m
operator|.
name|openTr
argument_list|()
expr_stmt|;
name|m
operator|.
name|setAttribute
argument_list|(
literal|"valign"
argument_list|,
literal|"top"
argument_list|)
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
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
block|}
DECL|method|appendLineText (final SafeHtmlBuilder m, final int lineNumberMinusOne, final PatchLine.Type type, final SparseFileContent src, final int i, final PrettyFormatter dst)
specifier|private
name|SafeHtml
name|appendLineText
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|int
name|lineNumberMinusOne
parameter_list|,
specifier|final
name|PatchLine
operator|.
name|Type
name|type
parameter_list|,
specifier|final
name|SparseFileContent
name|src
parameter_list|,
specifier|final
name|int
name|i
parameter_list|,
specifier|final
name|PrettyFormatter
name|dst
parameter_list|)
block|{
specifier|final
name|SafeHtml
name|lineHtml
init|=
name|dst
operator|.
name|format
argument_list|(
name|src
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|appendLineText
argument_list|(
name|m
argument_list|,
name|lineNumberMinusOne
argument_list|,
name|type
argument_list|,
name|lineHtml
argument_list|)
expr_stmt|;
return|return
name|lineHtml
return|;
block|}
DECL|method|appendLineText (final SafeHtmlBuilder m, final int lineNumberMinusOne, final PatchLine.Type type, final SafeHtml lineHtml)
specifier|private
name|void
name|appendLineText
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|int
name|lineNumberMinusOne
parameter_list|,
specifier|final
name|PatchLine
operator|.
name|Type
name|type
parameter_list|,
specifier|final
name|SafeHtml
name|lineHtml
parameter_list|)
block|{
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|lineNumber
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|lineNumberMinusOne
operator|+
literal|1
argument_list|)
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
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileLine
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|CONTEXT
case|:
name|m
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
name|fileLineCONTEXT
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|m
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
name|fileLineDELETE
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|INSERT
case|:
name|m
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
name|fileLineINSERT
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
name|m
operator|.
name|append
argument_list|(
name|lineHtml
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
block|}
DECL|method|appendLineNone (final SafeHtmlBuilder m)
specifier|private
name|void
name|appendLineNone
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|)
block|{
name|m
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|m
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|lineNumber
argument_list|()
argument_list|)
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
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|fileLine
argument_list|()
argument_list|)
expr_stmt|;
name|m
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
name|fileLineNone
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
block|}
DECL|method|closeLine (final SafeHtmlBuilder m)
specifier|private
name|void
name|closeLine
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|)
block|{
name|m
operator|.
name|closeTr
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

