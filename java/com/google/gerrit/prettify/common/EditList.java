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

begin_class
DECL|class|EditList
specifier|public
class|class
name|EditList
block|{
DECL|field|edits
specifier|private
specifier|final
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|int
name|context
decl_stmt|;
DECL|field|aSize
specifier|private
specifier|final
name|int
name|aSize
decl_stmt|;
DECL|field|bSize
specifier|private
specifier|final
name|int
name|bSize
decl_stmt|;
DECL|method|EditList (final List<Edit> edits, int contextLines, int aSize, int bSize)
specifier|public
name|EditList
parameter_list|(
specifier|final
name|List
argument_list|<
name|Edit
argument_list|>
name|edits
parameter_list|,
name|int
name|contextLines
parameter_list|,
name|int
name|aSize
parameter_list|,
name|int
name|bSize
parameter_list|)
block|{
name|this
operator|.
name|edits
operator|=
name|edits
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|contextLines
expr_stmt|;
name|this
operator|.
name|aSize
operator|=
name|aSize
expr_stmt|;
name|this
operator|.
name|bSize
operator|=
name|bSize
expr_stmt|;
block|}
DECL|method|getEdits ()
specifier|public
name|List
argument_list|<
name|Edit
argument_list|>
name|getEdits
parameter_list|()
block|{
return|return
name|edits
return|;
block|}
DECL|method|getHunks ()
specifier|public
name|Iterable
argument_list|<
name|Hunk
argument_list|>
name|getHunks
parameter_list|()
block|{
return|return
parameter_list|()
lambda|->
operator|new
name|Iterator
argument_list|<
name|Hunk
argument_list|>
argument_list|()
block|{
specifier|private
name|int
name|curIdx
return|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|curIdx
operator|<
name|edits
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Hunk
name|next
parameter_list|()
block|{
specifier|final
name|int
name|c
init|=
name|curIdx
decl_stmt|;
specifier|final
name|int
name|e
init|=
name|findCombinedEnd
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|curIdx
operator|=
name|e
operator|+
literal|1
expr_stmt|;
return|return
operator|new
name|Hunk
argument_list|(
name|c
argument_list|,
name|e
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
empty_stmt|;
block|}
end_class

begin_function
DECL|method|findCombinedEnd (int i)
specifier|private
name|int
name|findCombinedEnd
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|int
name|end
init|=
name|i
operator|+
literal|1
decl_stmt|;
while|while
condition|(
name|end
operator|<
name|edits
operator|.
name|size
argument_list|()
operator|&&
operator|(
name|combineA
argument_list|(
name|end
argument_list|)
operator|||
name|combineB
argument_list|(
name|end
argument_list|)
operator|)
condition|)
block|{
name|end
operator|++
expr_stmt|;
block|}
return|return
name|end
operator|-
literal|1
return|;
block|}
end_function

begin_function
DECL|method|combineA (int i)
specifier|private
name|boolean
name|combineA
parameter_list|(
name|int
name|i
parameter_list|)
block|{
specifier|final
name|Edit
name|s
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
specifier|final
name|Edit
name|e
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|// + 1 to prevent '... skipping 1 common line ...' messages.
return|return
name|s
operator|.
name|getBeginA
argument_list|()
operator|-
name|e
operator|.
name|getEndA
argument_list|()
operator|<=
literal|2
operator|*
name|context
operator|+
literal|1
return|;
block|}
end_function

begin_function
DECL|method|combineB (int i)
specifier|private
name|boolean
name|combineB
parameter_list|(
name|int
name|i
parameter_list|)
block|{
specifier|final
name|int
name|s
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getBeginB
argument_list|()
decl_stmt|;
specifier|final
name|int
name|e
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|getEndB
argument_list|()
decl_stmt|;
comment|// + 1 to prevent '... skipping 1 common line ...' messages.
return|return
name|s
operator|-
name|e
operator|<=
literal|2
operator|*
name|context
operator|+
literal|1
return|;
block|}
end_function

begin_class
DECL|class|Hunk
specifier|public
class|class
name|Hunk
block|{
DECL|field|curIdx
specifier|private
name|int
name|curIdx
decl_stmt|;
DECL|field|curEdit
specifier|private
name|Edit
name|curEdit
decl_stmt|;
DECL|field|endIdx
specifier|private
specifier|final
name|int
name|endIdx
decl_stmt|;
DECL|field|endEdit
specifier|private
specifier|final
name|Edit
name|endEdit
decl_stmt|;
DECL|field|aCur
specifier|private
name|int
name|aCur
decl_stmt|;
DECL|field|bCur
specifier|private
name|int
name|bCur
decl_stmt|;
DECL|field|aEnd
specifier|private
specifier|final
name|int
name|aEnd
decl_stmt|;
DECL|field|bEnd
specifier|private
specifier|final
name|int
name|bEnd
decl_stmt|;
DECL|method|Hunk (int ci, int ei)
specifier|private
name|Hunk
parameter_list|(
name|int
name|ci
parameter_list|,
name|int
name|ei
parameter_list|)
block|{
name|curIdx
operator|=
name|ci
expr_stmt|;
name|endIdx
operator|=
name|ei
expr_stmt|;
name|curEdit
operator|=
name|edits
operator|.
name|get
argument_list|(
name|curIdx
argument_list|)
expr_stmt|;
name|endEdit
operator|=
name|edits
operator|.
name|get
argument_list|(
name|endIdx
argument_list|)
expr_stmt|;
name|aCur
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|curEdit
operator|.
name|getBeginA
argument_list|()
operator|-
name|context
argument_list|)
expr_stmt|;
name|bCur
operator|=
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|curEdit
operator|.
name|getBeginB
argument_list|()
operator|-
name|context
argument_list|)
expr_stmt|;
name|aEnd
operator|=
name|Math
operator|.
name|min
argument_list|(
name|aSize
argument_list|,
name|endEdit
operator|.
name|getEndA
argument_list|()
operator|+
name|context
argument_list|)
expr_stmt|;
name|bEnd
operator|=
name|Math
operator|.
name|min
argument_list|(
name|bSize
argument_list|,
name|endEdit
operator|.
name|getEndB
argument_list|()
operator|+
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|getCurA ()
specifier|public
name|int
name|getCurA
parameter_list|()
block|{
return|return
name|aCur
return|;
block|}
DECL|method|getCurB ()
specifier|public
name|int
name|getCurB
parameter_list|()
block|{
return|return
name|bCur
return|;
block|}
DECL|method|getCurEdit ()
specifier|public
name|Edit
name|getCurEdit
parameter_list|()
block|{
return|return
name|curEdit
return|;
block|}
DECL|method|getEndA ()
specifier|public
name|int
name|getEndA
parameter_list|()
block|{
return|return
name|aEnd
return|;
block|}
DECL|method|getEndB ()
specifier|public
name|int
name|getEndB
parameter_list|()
block|{
return|return
name|bEnd
return|;
block|}
DECL|method|incA ()
specifier|public
name|void
name|incA
parameter_list|()
block|{
name|aCur
operator|++
expr_stmt|;
block|}
DECL|method|incB ()
specifier|public
name|void
name|incB
parameter_list|()
block|{
name|bCur
operator|++
expr_stmt|;
block|}
DECL|method|incBoth ()
specifier|public
name|void
name|incBoth
parameter_list|()
block|{
name|incA
argument_list|()
expr_stmt|;
name|incB
argument_list|()
expr_stmt|;
block|}
DECL|method|isStartOfFile ()
specifier|public
name|boolean
name|isStartOfFile
parameter_list|()
block|{
return|return
name|aCur
operator|==
literal|0
operator|&&
name|bCur
operator|==
literal|0
return|;
block|}
DECL|method|isContextLine ()
specifier|public
name|boolean
name|isContextLine
parameter_list|()
block|{
return|return
operator|!
name|isModifiedLine
argument_list|()
return|;
block|}
DECL|method|isDeletedA ()
specifier|public
name|boolean
name|isDeletedA
parameter_list|()
block|{
return|return
name|curEdit
operator|.
name|getBeginA
argument_list|()
operator|<=
name|aCur
operator|&&
name|aCur
operator|<
name|curEdit
operator|.
name|getEndA
argument_list|()
return|;
block|}
DECL|method|isInsertedB ()
specifier|public
name|boolean
name|isInsertedB
parameter_list|()
block|{
return|return
name|curEdit
operator|.
name|getBeginB
argument_list|()
operator|<=
name|bCur
operator|&&
name|bCur
operator|<
name|curEdit
operator|.
name|getEndB
argument_list|()
return|;
block|}
DECL|method|isModifiedLine ()
specifier|public
name|boolean
name|isModifiedLine
parameter_list|()
block|{
return|return
name|isDeletedA
argument_list|()
operator|||
name|isInsertedB
argument_list|()
return|;
block|}
DECL|method|next ()
specifier|public
name|boolean
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|in
argument_list|(
name|curEdit
argument_list|)
condition|)
block|{
if|if
condition|(
name|curIdx
operator|<
name|endIdx
condition|)
block|{
name|curEdit
operator|=
name|edits
operator|.
name|get
argument_list|(
operator|++
name|curIdx
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|aCur
operator|<
name|aEnd
operator|||
name|bCur
operator|<
name|bEnd
return|;
block|}
DECL|method|in (Edit edit)
specifier|private
name|boolean
name|in
parameter_list|(
name|Edit
name|edit
parameter_list|)
block|{
return|return
name|aCur
operator|<
name|edit
operator|.
name|getEndA
argument_list|()
operator|||
name|bCur
operator|<
name|edit
operator|.
name|getEndB
argument_list|()
return|;
block|}
block|}
end_class

unit|}
end_unit

