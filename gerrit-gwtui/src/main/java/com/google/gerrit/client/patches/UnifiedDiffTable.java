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
name|common
operator|.
name|data
operator|.
name|EditList
operator|.
name|Hunk
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
operator|.
name|DisplayMethod
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
name|SafeHtmlBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|KeyUtil
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
DECL|field|BY_DATE
specifier|private
specifier|static
specifier|final
name|Comparator
argument_list|<
name|PatchLineComment
argument_list|>
name|BY_DATE
init|=
operator|new
name|Comparator
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
specifier|final
name|PatchLineComment
name|o1
parameter_list|,
specifier|final
name|PatchLineComment
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getWrittenOn
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getWrittenOn
argument_list|()
argument_list|)
return|;
block|}
block|}
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
name|DELETE
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
name|getLineA
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
name|INSERT
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
name|getLineB
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
DECL|method|onInsertComment (final PatchLine pl)
specifier|protected
name|void
name|onInsertComment
parameter_list|(
specifier|final
name|PatchLine
name|pl
parameter_list|)
block|{
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
name|DELETE
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
name|getLineA
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
name|INSERT
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
name|getLineB
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
DECL|method|appendImgTag (SafeHtmlBuilder nc, String url)
specifier|private
name|void
name|appendImgTag
parameter_list|(
name|SafeHtmlBuilder
name|nc
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|nc
operator|.
name|openElement
argument_list|(
literal|"img"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|setAttribute
argument_list|(
literal|"src"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|nc
operator|.
name|closeElement
argument_list|(
literal|"img"
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
name|SafeHtmlBuilder
name|nc
init|=
operator|new
name|SafeHtmlBuilder
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
name|fmtB
operator|.
name|setShowWhiteSpaceErrors
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Display the patch header
for|for
control|(
specifier|final
name|String
name|line
range|:
name|script
operator|.
name|getPatchHeader
argument_list|()
control|)
block|{
name|appendFileHeader
argument_list|(
name|nc
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|script
operator|.
name|getDisplayMethodA
argument_list|()
operator|==
name|DisplayMethod
operator|.
name|IMG
operator|||
name|script
operator|.
name|getDisplayMethodB
argument_list|()
operator|==
name|DisplayMethod
operator|.
name|IMG
condition|)
block|{
specifier|final
name|String
name|rawBase
init|=
name|GWT
operator|.
name|getHostPageBaseURL
argument_list|()
operator|+
literal|"cat/"
decl_stmt|;
name|nc
operator|.
name|openTr
argument_list|()
expr_stmt|;
name|nc
operator|.
name|setAttribute
argument_list|(
literal|"valign"
argument_list|,
literal|"center"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|setAttribute
argument_list|(
literal|"align"
argument_list|,
literal|"center"
argument_list|)
expr_stmt|;
name|nc
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|nc
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|nc
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|openTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|nc
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|openTd
argument_list|()
expr_stmt|;
if|if
condition|(
name|script
operator|.
name|getDisplayMethodA
argument_list|()
operator|==
name|DisplayMethod
operator|.
name|IMG
condition|)
block|{
if|if
condition|(
name|idSideA
operator|==
literal|null
condition|)
block|{
name|appendImgTag
argument_list|(
name|nc
argument_list|,
name|rawBase
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|patchKey
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"^1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Patch
operator|.
name|Key
name|k
init|=
operator|new
name|Patch
operator|.
name|Key
argument_list|(
name|idSideA
argument_list|,
name|patchKey
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|appendImgTag
argument_list|(
name|nc
argument_list|,
name|rawBase
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|k
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"^0"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|script
operator|.
name|getDisplayMethodB
argument_list|()
operator|==
name|DisplayMethod
operator|.
name|IMG
condition|)
block|{
name|appendImgTag
argument_list|(
name|nc
argument_list|,
name|rawBase
operator|+
name|KeyUtil
operator|.
name|encode
argument_list|(
name|patchKey
operator|.
name|toString
argument_list|()
argument_list|)
operator|+
literal|"^0"
argument_list|)
expr_stmt|;
block|}
name|nc
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|nc
operator|.
name|closeTr
argument_list|()
expr_stmt|;
block|}
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
name|appendHunkHeader
argument_list|(
name|nc
argument_list|,
name|hunk
argument_list|)
expr_stmt|;
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
name|appendLineNumber
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
expr_stmt|;
name|appendLineNumber
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
expr_stmt|;
name|appendLineText
argument_list|(
name|nc
argument_list|,
name|CONTEXT
argument_list|,
name|a
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|,
name|fmtA
argument_list|,
name|fmtB
argument_list|)
expr_stmt|;
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
name|isDeletedA
argument_list|()
condition|)
block|{
name|openLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|appendLineNumber
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|appendLineText
argument_list|(
name|nc
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
argument_list|,
name|fmtB
argument_list|)
expr_stmt|;
name|closeLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incA
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|a
operator|.
name|size
argument_list|()
operator|==
name|hunk
operator|.
name|getCurA
argument_list|()
operator|&&
name|a
operator|.
name|isMissingNewlineAtEnd
argument_list|()
condition|)
name|appendNoLF
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hunk
operator|.
name|isInsertedB
argument_list|()
condition|)
block|{
name|openLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|appendLineNumber
argument_list|(
name|nc
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
expr_stmt|;
name|appendLineText
argument_list|(
name|nc
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
name|fmtA
argument_list|,
name|fmtB
argument_list|)
expr_stmt|;
name|closeLine
argument_list|(
name|nc
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incB
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|b
operator|.
name|size
argument_list|()
operator|==
name|hunk
operator|.
name|getCurB
argument_list|()
operator|&&
name|b
operator|.
name|isMissingNewlineAtEnd
argument_list|()
condition|)
name|appendNoLF
argument_list|(
name|nc
argument_list|)
expr_stmt|;
block|}
block|}
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
name|int
name|row
init|=
name|script
operator|.
name|getPatchHeader
argument_list|()
operator|.
name|size
argument_list|()
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
specifier|final
name|Iterator
argument_list|<
name|PatchLine
argument_list|>
name|iLine
init|=
name|lines
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iLine
operator|.
name|hasNext
argument_list|()
condition|)
block|{
specifier|final
name|PatchLine
name|l
init|=
name|iLine
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|final
name|String
name|n
decl_stmt|;
switch|switch
condition|(
name|l
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|CONTEXT
case|:
name|n
operator|=
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|diffTextCONTEXT
argument_list|()
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|n
operator|=
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|diffTextDELETE
argument_list|()
expr_stmt|;
break|break;
case|case
name|INSERT
case|:
name|n
operator|=
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|diffTextINSERT
argument_list|()
expr_stmt|;
break|break;
default|default:
continue|continue;
block|}
while|while
condition|(
operator|!
name|fmt
operator|.
name|getStyleName
argument_list|(
name|row
argument_list|,
name|PC
argument_list|)
operator|.
name|contains
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|row
operator|++
expr_stmt|;
block|}
name|setRowItem
argument_list|(
name|row
operator|++
argument_list|,
name|l
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
specifier|final
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
decl_stmt|;
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
if|if
condition|(
operator|!
name|fora
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|forb
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|all
operator|.
name|clear
argument_list|()
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|fora
argument_list|)
expr_stmt|;
name|all
operator|.
name|addAll
argument_list|(
name|forb
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|all
argument_list|,
name|BY_DATE
argument_list|)
expr_stmt|;
name|row
operator|=
name|insert
argument_list|(
name|all
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|fora
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|row
operator|=
name|insert
argument_list|(
name|fora
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|forb
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|row
operator|=
name|insert
argument_list|(
name|forb
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
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
name|PC
operator|-
literal|2
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
name|PC
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
name|PC
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
DECL|method|insert (final List<PatchLineComment> in, int row)
specifier|private
name|int
name|insert
parameter_list|(
specifier|final
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|in
parameter_list|,
name|int
name|row
parameter_list|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|PatchLineComment
argument_list|>
name|ci
init|=
name|in
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
name|insertRow
argument_list|(
name|row
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
return|return
name|row
return|;
block|}
DECL|method|appendFileHeader (final SafeHtmlBuilder m, final String line)
specifier|private
name|void
name|appendFileHeader
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|String
name|line
parameter_list|)
block|{
name|openLine
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
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
name|diffText
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
name|diffTextFileHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|closeLine
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
DECL|method|appendHunkHeader (final SafeHtmlBuilder m, final Hunk hunk)
specifier|private
name|void
name|appendHunkHeader
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|Hunk
name|hunk
parameter_list|)
block|{
name|openLine
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
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
name|diffText
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
name|diffTextHunkHeader
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|"@@ -"
argument_list|)
expr_stmt|;
name|appendRange
argument_list|(
name|m
argument_list|,
name|hunk
operator|.
name|getCurA
argument_list|()
operator|+
literal|1
argument_list|,
name|hunk
operator|.
name|getEndA
argument_list|()
operator|-
name|hunk
operator|.
name|getCurA
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|" +"
argument_list|)
expr_stmt|;
name|appendRange
argument_list|(
name|m
argument_list|,
name|hunk
operator|.
name|getCurB
argument_list|()
operator|+
literal|1
argument_list|,
name|hunk
operator|.
name|getEndB
argument_list|()
operator|-
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|" @@"
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|closeLine
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
DECL|method|appendRange (final SafeHtmlBuilder m, final int begin, final int cnt)
specifier|private
name|void
name|appendRange
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|int
name|begin
parameter_list|,
specifier|final
name|int
name|cnt
parameter_list|)
block|{
switch|switch
condition|(
name|cnt
condition|)
block|{
case|case
literal|0
case|:
name|m
operator|.
name|append
argument_list|(
name|begin
operator|-
literal|1
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|",0"
argument_list|)
expr_stmt|;
break|break;
case|case
literal|1
case|:
name|m
operator|.
name|append
argument_list|(
name|begin
argument_list|)
expr_stmt|;
break|break;
default|default:
name|m
operator|.
name|append
argument_list|(
name|begin
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|cnt
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
DECL|method|appendLineText (final SafeHtmlBuilder m, final PatchLine.Type type, final SparseFileContent src, final int i, final PrettyFormatter fmtA, final PrettyFormatter fmtB)
specifier|private
name|void
name|appendLineText
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
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
name|fmtA
parameter_list|,
specifier|final
name|PrettyFormatter
name|fmtB
parameter_list|)
block|{
specifier|final
name|String
name|text
init|=
name|src
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
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
name|diffText
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
name|diffTextCONTEXT
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|nbsp
argument_list|()
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|fmtA
operator|.
name|format
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
name|fmtB
operator|.
name|update
argument_list|(
name|text
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
name|diffTextDELETE
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|"-"
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|fmtA
operator|.
name|format
argument_list|(
name|text
argument_list|)
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
name|diffTextINSERT
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|"+"
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
name|fmtB
operator|.
name|format
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
block|}
DECL|method|appendNoLF (final SafeHtmlBuilder m)
specifier|private
name|void
name|appendNoLF
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|)
block|{
name|openLine
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
expr_stmt|;
name|padLineNumber
argument_list|(
name|m
argument_list|)
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
name|diffText
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
name|diffTextNoLF
argument_list|()
argument_list|)
expr_stmt|;
name|m
operator|.
name|append
argument_list|(
literal|"\\ No newline at end of file"
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
name|closeLine
argument_list|(
name|m
argument_list|)
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
DECL|method|padLineNumber (final SafeHtmlBuilder m)
specifier|private
name|void
name|padLineNumber
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
block|}
DECL|method|appendLineNumber (final SafeHtmlBuilder m, final int idx)
specifier|private
name|void
name|appendLineNumber
parameter_list|(
specifier|final
name|SafeHtmlBuilder
name|m
parameter_list|,
specifier|final
name|int
name|idx
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
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|m
operator|.
name|closeTd
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

