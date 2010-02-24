begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.prettify.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|prettify
operator|.
name|common
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|diff
operator|.
name|Edit
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
DECL|class|SparseFileContent
specifier|public
class|class
name|SparseFileContent
block|{
DECL|field|path
specifier|protected
name|String
name|path
decl_stmt|;
DECL|field|ranges
specifier|protected
name|List
argument_list|<
name|Range
argument_list|>
name|ranges
decl_stmt|;
DECL|field|size
specifier|protected
name|int
name|size
decl_stmt|;
DECL|field|missingNewlineAtEnd
specifier|protected
name|boolean
name|missingNewlineAtEnd
decl_stmt|;
DECL|field|currentRangeIdx
specifier|private
specifier|transient
name|int
name|currentRangeIdx
decl_stmt|;
DECL|method|SparseFileContent ()
specifier|public
name|SparseFileContent
parameter_list|()
block|{
name|ranges
operator|=
operator|new
name|ArrayList
argument_list|<
name|Range
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|setSize (final int s)
specifier|public
name|void
name|setSize
parameter_list|(
specifier|final
name|int
name|s
parameter_list|)
block|{
name|size
operator|=
name|s
expr_stmt|;
block|}
DECL|method|isMissingNewlineAtEnd ()
specifier|public
name|boolean
name|isMissingNewlineAtEnd
parameter_list|()
block|{
return|return
name|missingNewlineAtEnd
return|;
block|}
DECL|method|setMissingNewlineAtEnd (final boolean missing)
specifier|public
name|void
name|setMissingNewlineAtEnd
parameter_list|(
specifier|final
name|boolean
name|missing
parameter_list|)
block|{
name|missingNewlineAtEnd
operator|=
name|missing
expr_stmt|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPath (String filePath)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|filePath
parameter_list|)
block|{
name|path
operator|=
name|filePath
expr_stmt|;
block|}
DECL|method|isWholeFile ()
specifier|public
name|boolean
name|isWholeFile
parameter_list|()
block|{
if|if
condition|(
name|size
operator|==
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
literal|1
operator|==
name|ranges
operator|.
name|size
argument_list|()
condition|)
block|{
name|Range
name|r
init|=
name|ranges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|r
operator|.
name|base
operator|==
literal|0
operator|&&
name|r
operator|.
name|end
argument_list|()
operator|==
name|size
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|get (final int idx)
specifier|public
name|String
name|get
parameter_list|(
specifier|final
name|int
name|idx
parameter_list|)
block|{
specifier|final
name|String
name|line
init|=
name|getLine
argument_list|(
name|idx
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|idx
argument_list|)
throw|;
block|}
return|return
name|line
return|;
block|}
DECL|method|contains (final int idx)
specifier|public
name|boolean
name|contains
parameter_list|(
specifier|final
name|int
name|idx
parameter_list|)
block|{
return|return
name|getLine
argument_list|(
name|idx
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|first ()
specifier|public
name|int
name|first
parameter_list|()
block|{
return|return
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|?
name|size
argument_list|()
else|:
name|ranges
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|base
return|;
block|}
DECL|method|next (final int idx)
specifier|public
name|int
name|next
parameter_list|(
specifier|final
name|int
name|idx
parameter_list|)
block|{
comment|// Most requests are sequential in nature, fetching the next
comment|// line from the current range, or the immediate next range.
comment|//
name|int
name|high
init|=
name|ranges
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentRangeIdx
operator|<
name|high
condition|)
block|{
name|Range
name|cur
init|=
name|ranges
operator|.
name|get
argument_list|(
name|currentRangeIdx
argument_list|)
decl_stmt|;
if|if
condition|(
name|cur
operator|.
name|contains
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
condition|)
block|{
return|return
name|idx
operator|+
literal|1
return|;
block|}
if|if
condition|(
operator|++
name|currentRangeIdx
operator|<
name|high
condition|)
block|{
comment|// Its not plus one, its the base of the next range.
comment|//
return|return
name|ranges
operator|.
name|get
argument_list|(
name|currentRangeIdx
argument_list|)
operator|.
name|base
return|;
block|}
block|}
comment|// Binary search for the current value, since we know its a sorted list.
comment|//
name|int
name|low
init|=
literal|0
decl_stmt|;
do|do
block|{
specifier|final
name|int
name|mid
init|=
operator|(
name|low
operator|+
name|high
operator|)
operator|/
literal|2
decl_stmt|;
specifier|final
name|Range
name|cur
init|=
name|ranges
operator|.
name|get
argument_list|(
name|mid
argument_list|)
decl_stmt|;
if|if
condition|(
name|cur
operator|.
name|contains
argument_list|(
name|idx
argument_list|)
condition|)
block|{
if|if
condition|(
name|cur
operator|.
name|contains
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
condition|)
block|{
comment|// Trivial plus one case above failed due to wrong currentRangeIdx.
comment|// Reset the cache so we don't miss in the future.
comment|//
name|currentRangeIdx
operator|=
name|mid
expr_stmt|;
return|return
name|idx
operator|+
literal|1
return|;
block|}
if|if
condition|(
name|mid
operator|+
literal|1
operator|<
name|ranges
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// Its the base of the next range.
name|currentRangeIdx
operator|=
name|mid
operator|+
literal|1
expr_stmt|;
return|return
name|ranges
operator|.
name|get
argument_list|(
name|currentRangeIdx
argument_list|)
operator|.
name|base
return|;
block|}
comment|// No more lines in the file.
comment|//
return|return
name|size
argument_list|()
return|;
block|}
if|if
condition|(
name|idx
operator|<
name|cur
operator|.
name|base
condition|)
name|high
operator|=
name|mid
expr_stmt|;
else|else
name|low
operator|=
name|mid
operator|+
literal|1
expr_stmt|;
block|}
do|while
condition|(
name|low
operator|<
name|high
condition|)
do|;
return|return
name|size
argument_list|()
return|;
block|}
DECL|method|mapIndexToLine (int arrayIndex)
specifier|public
name|int
name|mapIndexToLine
parameter_list|(
name|int
name|arrayIndex
parameter_list|)
block|{
specifier|final
name|int
name|origIndex
init|=
name|arrayIndex
decl_stmt|;
for|for
control|(
name|Range
name|r
range|:
name|ranges
control|)
block|{
if|if
condition|(
name|arrayIndex
operator|<
name|r
operator|.
name|lines
operator|.
name|size
argument_list|()
condition|)
block|{
return|return
name|r
operator|.
name|base
operator|+
name|arrayIndex
return|;
block|}
name|arrayIndex
operator|-=
name|r
operator|.
name|lines
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|ArrayIndexOutOfBoundsException
argument_list|(
name|origIndex
argument_list|)
throw|;
block|}
DECL|method|getLine (final int idx)
specifier|private
name|String
name|getLine
parameter_list|(
specifier|final
name|int
name|idx
parameter_list|)
block|{
comment|// Most requests are sequential in nature, fetching the next
comment|// line from the current range, or the next range.
comment|//
name|int
name|high
init|=
name|ranges
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentRangeIdx
operator|<
name|high
condition|)
block|{
name|Range
name|cur
init|=
name|ranges
operator|.
name|get
argument_list|(
name|currentRangeIdx
argument_list|)
decl_stmt|;
if|if
condition|(
name|cur
operator|.
name|contains
argument_list|(
name|idx
argument_list|)
condition|)
block|{
return|return
name|cur
operator|.
name|get
argument_list|(
name|idx
argument_list|)
return|;
block|}
if|if
condition|(
operator|++
name|currentRangeIdx
operator|<
name|high
condition|)
block|{
specifier|final
name|Range
name|next
init|=
name|ranges
operator|.
name|get
argument_list|(
name|currentRangeIdx
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|.
name|contains
argument_list|(
name|idx
argument_list|)
condition|)
block|{
return|return
name|next
operator|.
name|get
argument_list|(
name|idx
argument_list|)
return|;
block|}
block|}
block|}
comment|// Binary search for the range, since we know its a sorted list.
comment|//
if|if
condition|(
name|ranges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|low
init|=
literal|0
decl_stmt|;
do|do
block|{
specifier|final
name|int
name|mid
init|=
operator|(
name|low
operator|+
name|high
operator|)
operator|/
literal|2
decl_stmt|;
specifier|final
name|Range
name|cur
init|=
name|ranges
operator|.
name|get
argument_list|(
name|mid
argument_list|)
decl_stmt|;
if|if
condition|(
name|cur
operator|.
name|contains
argument_list|(
name|idx
argument_list|)
condition|)
block|{
name|currentRangeIdx
operator|=
name|mid
expr_stmt|;
return|return
name|cur
operator|.
name|get
argument_list|(
name|idx
argument_list|)
return|;
block|}
if|if
condition|(
name|idx
operator|<
name|cur
operator|.
name|base
condition|)
name|high
operator|=
name|mid
expr_stmt|;
else|else
name|low
operator|=
name|mid
operator|+
literal|1
expr_stmt|;
block|}
do|while
condition|(
name|low
operator|<
name|high
condition|)
do|;
return|return
literal|null
return|;
block|}
DECL|method|addLine (final int i, final String content)
specifier|public
name|void
name|addLine
parameter_list|(
specifier|final
name|int
name|i
parameter_list|,
specifier|final
name|String
name|content
parameter_list|)
block|{
specifier|final
name|Range
name|r
decl_stmt|;
if|if
condition|(
operator|!
name|ranges
operator|.
name|isEmpty
argument_list|()
operator|&&
name|i
operator|==
name|last
argument_list|()
operator|.
name|end
argument_list|()
condition|)
block|{
name|r
operator|=
name|last
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|=
operator|new
name|Range
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|ranges
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|lines
operator|.
name|add
argument_list|(
name|content
argument_list|)
expr_stmt|;
block|}
DECL|method|last ()
specifier|private
name|Range
name|last
parameter_list|()
block|{
return|return
name|ranges
operator|.
name|get
argument_list|(
name|ranges
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
return|;
block|}
DECL|method|asString ()
specifier|public
name|String
name|asString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Range
name|r
range|:
name|ranges
control|)
block|{
for|for
control|(
name|String
name|l
range|:
name|r
operator|.
name|lines
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
literal|0
operator|<
name|b
operator|.
name|length
argument_list|()
operator|&&
name|isMissingNewlineAtEnd
argument_list|()
condition|)
block|{
name|b
operator|.
name|setLength
argument_list|(
name|b
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|apply (SparseFileContent a, List<Edit> edits)
specifier|public
name|SparseFileContent
name|apply
parameter_list|(
name|SparseFileContent
name|a
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|)
block|{
name|EditList
name|list
init|=
operator|new
name|EditList
argument_list|(
name|edits
argument_list|,
name|size
argument_list|,
name|a
operator|.
name|size
argument_list|()
argument_list|,
name|size
argument_list|)
decl_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|EditList
operator|.
name|Hunk
name|hunk
range|:
name|list
operator|.
name|getHunks
argument_list|()
control|)
block|{
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
if|if
condition|(
name|contains
argument_list|(
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|get
argument_list|(
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lines
operator|.
name|add
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
expr_stmt|;
block|}
name|hunk
operator|.
name|incBoth
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|hunk
operator|.
name|isDeletedA
argument_list|()
condition|)
block|{
name|hunk
operator|.
name|incA
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|hunk
operator|.
name|isInsertedB
argument_list|()
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|get
argument_list|(
name|hunk
operator|.
name|getCurB
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|hunk
operator|.
name|incB
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|Range
name|range
init|=
operator|new
name|Range
argument_list|()
decl_stmt|;
name|range
operator|.
name|lines
operator|=
name|lines
expr_stmt|;
name|SparseFileContent
name|r
init|=
operator|new
name|SparseFileContent
argument_list|()
decl_stmt|;
name|r
operator|.
name|setSize
argument_list|(
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setMissingNewlineAtEnd
argument_list|(
name|isMissingNewlineAtEnd
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setPath
argument_list|(
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|ranges
operator|.
name|add
argument_list|(
name|range
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"SparseFileContent[\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|Range
name|r
range|:
name|ranges
control|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"  "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|r
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|Range
specifier|static
class|class
name|Range
block|{
DECL|field|base
specifier|protected
name|int
name|base
decl_stmt|;
DECL|field|lines
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|lines
decl_stmt|;
DECL|method|Range (final int b)
specifier|private
name|Range
parameter_list|(
specifier|final
name|int
name|b
parameter_list|)
block|{
name|base
operator|=
name|b
expr_stmt|;
name|lines
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|Range ()
specifier|protected
name|Range
parameter_list|()
block|{     }
DECL|method|get (final int i)
specifier|private
name|String
name|get
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|lines
operator|.
name|get
argument_list|(
name|i
operator|-
name|base
argument_list|)
return|;
block|}
DECL|method|end ()
specifier|private
name|int
name|end
parameter_list|()
block|{
return|return
name|base
operator|+
name|lines
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|contains (final int i)
specifier|private
name|boolean
name|contains
parameter_list|(
specifier|final
name|int
name|i
parameter_list|)
block|{
return|return
name|base
operator|<=
name|i
operator|&&
name|i
operator|<
name|end
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Range["
operator|+
name|base
operator|+
literal|","
operator|+
name|end
argument_list|()
operator|+
literal|")"
return|;
block|}
block|}
block|}
end_class

end_unit

