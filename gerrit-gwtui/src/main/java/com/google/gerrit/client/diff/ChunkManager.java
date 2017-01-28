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
name|DisplaySide
operator|.
name|A
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
name|JavaScriptObject
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
name|LineClassWhere
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

begin_import
import|import
name|net
operator|.
name|codemirror
operator|.
name|lib
operator|.
name|TextMarker
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
name|Comparator
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

begin_comment
comment|/** Colors modified regions for {@link SideBySide} and {@link Unified}. */
end_comment

begin_class
DECL|class|ChunkManager
specifier|abstract
class|class
name|ChunkManager
block|{
DECL|method|onClick (Element e, JavaScriptObject f)
specifier|static
specifier|final
specifier|native
name|void
name|onClick
parameter_list|(
name|Element
name|e
parameter_list|,
name|JavaScriptObject
name|f
parameter_list|)
comment|/*-{ e.onclick = f }-*/
function_decl|;
DECL|field|scrollbar
specifier|final
name|Scrollbar
name|scrollbar
decl_stmt|;
DECL|field|lineMapper
specifier|final
name|LineMapper
name|lineMapper
decl_stmt|;
DECL|field|markers
specifier|private
name|List
argument_list|<
name|TextMarker
argument_list|>
name|markers
decl_stmt|;
DECL|field|undo
specifier|private
name|List
argument_list|<
name|Runnable
argument_list|>
name|undo
decl_stmt|;
DECL|method|ChunkManager (Scrollbar scrollbar)
name|ChunkManager
parameter_list|(
name|Scrollbar
name|scrollbar
parameter_list|)
block|{
name|this
operator|.
name|scrollbar
operator|=
name|scrollbar
expr_stmt|;
name|this
operator|.
name|lineMapper
operator|=
operator|new
name|LineMapper
argument_list|()
expr_stmt|;
block|}
DECL|method|getFirst ()
specifier|abstract
name|DiffChunkInfo
name|getFirst
parameter_list|()
function_decl|;
DECL|method|getMarkers ()
name|List
argument_list|<
name|TextMarker
argument_list|>
name|getMarkers
parameter_list|()
block|{
return|return
name|markers
return|;
block|}
DECL|method|reset ()
name|void
name|reset
parameter_list|()
block|{
name|lineMapper
operator|.
name|reset
argument_list|()
expr_stmt|;
for|for
control|(
name|TextMarker
name|m
range|:
name|markers
control|)
block|{
name|m
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|Runnable
name|r
range|:
name|undo
control|)
block|{
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|render (DiffInfo diff)
specifier|abstract
name|void
name|render
parameter_list|(
name|DiffInfo
name|diff
parameter_list|)
function_decl|;
DECL|method|render ()
name|void
name|render
parameter_list|()
block|{
name|markers
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|undo
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|colorLines (CodeMirror cm, String color, int line, int cnt)
name|void
name|colorLines
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|String
name|color
parameter_list|,
name|int
name|line
parameter_list|,
name|int
name|cnt
parameter_list|)
block|{
name|colorLines
argument_list|(
name|cm
argument_list|,
name|LineClassWhere
operator|.
name|WRAP
argument_list|,
name|color
argument_list|,
name|line
argument_list|,
name|line
operator|+
name|cnt
argument_list|)
expr_stmt|;
block|}
DECL|method|colorLines (final CodeMirror cm, final LineClassWhere where, final String className, final int start, final int end)
name|void
name|colorLines
parameter_list|(
specifier|final
name|CodeMirror
name|cm
parameter_list|,
specifier|final
name|LineClassWhere
name|where
parameter_list|,
specifier|final
name|String
name|className
parameter_list|,
specifier|final
name|int
name|start
parameter_list|,
specifier|final
name|int
name|end
parameter_list|)
block|{
if|if
condition|(
name|start
operator|<
name|end
condition|)
block|{
for|for
control|(
name|int
name|line
init|=
name|start
init|;
name|line
operator|<
name|end
condition|;
name|line
operator|++
control|)
block|{
name|cm
operator|.
name|addLineClass
argument_list|(
name|line
argument_list|,
name|where
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
name|undo
operator|.
name|add
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
for|for
control|(
name|int
name|line
init|=
name|start
init|;
name|line
operator|<
name|end
condition|;
name|line
operator|++
control|)
block|{
name|cm
operator|.
name|removeLineClass
argument_list|(
name|line
argument_list|,
name|where
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|diffChunkNav (CodeMirror cm, Direction dir)
specifier|abstract
name|Runnable
name|diffChunkNav
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|Direction
name|dir
parameter_list|)
function_decl|;
DECL|method|diffChunkNavHelper (List<? extends DiffChunkInfo> chunks, DiffScreen host, int res, Direction dir)
name|void
name|diffChunkNavHelper
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|DiffChunkInfo
argument_list|>
name|chunks
parameter_list|,
name|DiffScreen
name|host
parameter_list|,
name|int
name|res
parameter_list|,
name|Direction
name|dir
parameter_list|)
block|{
if|if
condition|(
name|res
operator|<
literal|0
condition|)
block|{
name|res
operator|=
operator|-
name|res
operator|-
operator|(
name|dir
operator|==
name|Direction
operator|.
name|PREV
condition|?
literal|1
else|:
literal|2
operator|)
expr_stmt|;
block|}
name|res
operator|=
name|res
operator|+
operator|(
name|dir
operator|==
name|Direction
operator|.
name|PREV
condition|?
operator|-
literal|1
else|:
literal|1
operator|)
expr_stmt|;
if|if
condition|(
name|res
operator|<
literal|0
operator|||
name|chunks
operator|.
name|size
argument_list|()
operator|<=
name|res
condition|)
block|{
return|return;
block|}
name|DiffChunkInfo
name|lookUp
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
argument_list|)
decl_stmt|;
comment|// If edit, skip the deletion chunk and set focus on the insertion one.
if|if
condition|(
name|lookUp
operator|.
name|isEdit
argument_list|()
operator|&&
name|lookUp
operator|.
name|getSide
argument_list|()
operator|==
name|A
condition|)
block|{
name|res
operator|=
name|res
operator|+
operator|(
name|dir
operator|==
name|Direction
operator|.
name|PREV
condition|?
operator|-
literal|1
else|:
literal|1
operator|)
expr_stmt|;
if|if
condition|(
name|res
operator|<
literal|0
operator|||
name|chunks
operator|.
name|size
argument_list|()
operator|<=
name|res
condition|)
block|{
return|return;
block|}
block|}
name|DiffChunkInfo
name|target
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
argument_list|)
decl_stmt|;
name|CodeMirror
name|targetCm
init|=
name|host
operator|.
name|getCmFromSide
argument_list|(
name|target
operator|.
name|getSide
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|cmLine
init|=
name|getCmLine
argument_list|(
name|target
operator|.
name|getStart
argument_list|()
argument_list|,
name|target
operator|.
name|getSide
argument_list|()
argument_list|)
decl_stmt|;
name|targetCm
operator|.
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|cmLine
argument_list|)
argument_list|)
expr_stmt|;
name|targetCm
operator|.
name|focus
argument_list|()
expr_stmt|;
name|targetCm
operator|.
name|scrollToY
argument_list|(
name|targetCm
operator|.
name|heightAtLine
argument_list|(
name|cmLine
argument_list|,
literal|"local"
argument_list|)
operator|-
literal|0.5
operator|*
name|targetCm
operator|.
name|scrollbarV
argument_list|()
operator|.
name|getClientHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getDiffChunkComparator ()
name|Comparator
argument_list|<
name|DiffChunkInfo
argument_list|>
name|getDiffChunkComparator
parameter_list|()
block|{
comment|// Chunks are ordered by their starting line. If it's a deletion,
comment|// use its corresponding line on the revision side for comparison.
comment|// In the edit case, put the deletion chunk right before the
comment|// insertion chunk. This placement guarantees well-ordering.
return|return
operator|new
name|Comparator
argument_list|<
name|DiffChunkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|DiffChunkInfo
name|a
parameter_list|,
name|DiffChunkInfo
name|b
parameter_list|)
block|{
if|if
condition|(
name|a
operator|.
name|getSide
argument_list|()
operator|==
name|b
operator|.
name|getSide
argument_list|()
condition|)
block|{
return|return
name|a
operator|.
name|getStart
argument_list|()
operator|-
name|b
operator|.
name|getStart
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|a
operator|.
name|getSide
argument_list|()
operator|==
name|A
condition|)
block|{
name|int
name|comp
init|=
name|lineMapper
operator|.
name|lineOnOther
argument_list|(
name|a
operator|.
name|getSide
argument_list|()
argument_list|,
name|a
operator|.
name|getStart
argument_list|()
argument_list|)
operator|.
name|getLine
argument_list|()
operator|-
name|b
operator|.
name|getStart
argument_list|()
decl_stmt|;
return|return
name|comp
operator|==
literal|0
condition|?
operator|-
literal|1
else|:
name|comp
return|;
block|}
else|else
block|{
name|int
name|comp
init|=
name|a
operator|.
name|getStart
argument_list|()
operator|-
name|lineMapper
operator|.
name|lineOnOther
argument_list|(
name|b
operator|.
name|getSide
argument_list|()
argument_list|,
name|b
operator|.
name|getStart
argument_list|()
argument_list|)
operator|.
name|getLine
argument_list|()
decl_stmt|;
return|return
name|comp
operator|==
literal|0
condition|?
literal|1
else|:
name|comp
return|;
block|}
block|}
block|}
return|;
block|}
DECL|method|getCmLine (int line, DisplaySide side)
specifier|abstract
name|int
name|getCmLine
parameter_list|(
name|int
name|line
parameter_list|,
name|DisplaySide
name|side
parameter_list|)
function_decl|;
block|}
end_class

end_unit

