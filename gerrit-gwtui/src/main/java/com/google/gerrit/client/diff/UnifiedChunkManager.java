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
name|diff
operator|.
name|DiffInfo
operator|.
name|Region
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
name|diff
operator|.
name|DiffInfo
operator|.
name|Span
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
name|Natives
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
name|NativeEvent
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
name|EventListener
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
name|Configuration
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
name|List
import|;
end_import

begin_comment
comment|/** Colors modified regions for {@link Unified}. */
end_comment

begin_class
DECL|class|UnifiedChunkManager
class|class
name|UnifiedChunkManager
extends|extends
name|ChunkManager
block|{
DECL|field|focus
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|focus
init|=
name|initOnClick
argument_list|()
decl_stmt|;
DECL|method|initOnClick ()
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|initOnClick
parameter_list|()
comment|/*-{     return $entry(function(e){       @com.google.gerrit.client.diff.UnifiedChunkManager::focus(         Lcom/google/gwt/dom/client/NativeEvent;)(e)     });   }-*/
function_decl|;
DECL|field|chunks
specifier|private
name|List
argument_list|<
name|UnifiedDiffChunkInfo
argument_list|>
name|chunks
decl_stmt|;
annotation|@
name|Override
DECL|method|getFirst ()
name|DiffChunkInfo
name|getFirst
parameter_list|()
block|{
return|return
operator|!
name|chunks
operator|.
name|isEmpty
argument_list|()
condition|?
name|chunks
operator|.
name|get
argument_list|(
literal|0
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|focus (NativeEvent event)
specifier|private
specifier|static
name|void
name|focus
parameter_list|(
name|NativeEvent
name|event
parameter_list|)
block|{
name|Element
name|e
init|=
name|Element
operator|.
name|as
argument_list|(
name|event
operator|.
name|getEventTarget
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|e
operator|=
name|DOM
operator|.
name|getParent
argument_list|(
name|e
argument_list|)
init|;
name|e
operator|!=
literal|null
condition|;
name|e
operator|=
name|DOM
operator|.
name|getParent
argument_list|(
name|e
argument_list|)
control|)
block|{
name|EventListener
name|l
init|=
name|DOM
operator|.
name|getEventListener
argument_list|(
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|l
operator|instanceof
name|Unified
condition|)
block|{
operator|(
operator|(
name|Unified
operator|)
name|l
operator|)
operator|.
name|getCmFromSide
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|)
operator|.
name|focus
argument_list|()
expr_stmt|;
name|event
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|focusOnClick (Element e)
specifier|static
name|void
name|focusOnClick
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|onClick
argument_list|(
name|e
argument_list|,
name|focus
argument_list|)
expr_stmt|;
block|}
DECL|field|host
specifier|private
specifier|final
name|Unified
name|host
decl_stmt|;
DECL|field|cm
specifier|private
specifier|final
name|CodeMirror
name|cm
decl_stmt|;
DECL|method|UnifiedChunkManager (Unified host, CodeMirror cm, Scrollbar scrollbar)
name|UnifiedChunkManager
parameter_list|(
name|Unified
name|host
parameter_list|,
name|CodeMirror
name|cm
parameter_list|,
name|Scrollbar
name|scrollbar
parameter_list|)
block|{
name|super
argument_list|(
name|scrollbar
argument_list|)
expr_stmt|;
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|cm
operator|=
name|cm
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|render (DiffInfo diff)
name|void
name|render
parameter_list|(
name|DiffInfo
name|diff
parameter_list|)
block|{
name|super
operator|.
name|render
argument_list|()
expr_stmt|;
name|LineMapper
name|mapper
init|=
name|getLineMapper
argument_list|()
decl_stmt|;
name|chunks
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|int
name|cmLine
init|=
literal|0
decl_stmt|;
name|boolean
name|useIntralineBg
init|=
name|diff
operator|.
name|metaA
argument_list|()
operator|==
literal|null
operator|||
name|diff
operator|.
name|metaB
argument_list|()
operator|==
literal|null
decl_stmt|;
for|for
control|(
name|Region
name|current
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|diff
operator|.
name|content
argument_list|()
argument_list|)
control|)
block|{
name|int
name|origLineA
init|=
name|mapper
operator|.
name|getLineA
argument_list|()
decl_stmt|;
name|int
name|origLineB
init|=
name|mapper
operator|.
name|getLineB
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|ab
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|length
init|=
name|current
operator|.
name|ab
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|appendCommon
argument_list|(
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|length
condition|;
name|i
operator|++
control|)
block|{
name|host
operator|.
name|setLineNumber
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|cmLine
operator|+
name|i
argument_list|,
name|origLineA
operator|+
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
name|host
operator|.
name|setLineNumber
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|cmLine
operator|+
name|i
argument_list|,
name|origLineB
operator|+
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|cmLine
operator|+=
name|length
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|skip
argument_list|()
operator|>
literal|0
condition|)
block|{
name|mapper
operator|.
name|appendCommon
argument_list|(
name|current
operator|.
name|skip
argument_list|()
argument_list|)
expr_stmt|;
name|cmLine
operator|+=
name|current
operator|.
name|skip
argument_list|()
expr_stmt|;
comment|// Maybe current.ab().length();
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|common
argument_list|()
condition|)
block|{
name|mapper
operator|.
name|appendCommon
argument_list|(
name|current
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|cmLine
operator|+=
name|current
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|cmLine
operator|+=
name|render
argument_list|(
name|current
argument_list|,
name|cmLine
argument_list|,
name|useIntralineBg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|render (Region region, int cmLine, boolean useIntralineBg)
specifier|private
name|int
name|render
parameter_list|(
name|Region
name|region
parameter_list|,
name|int
name|cmLine
parameter_list|,
name|boolean
name|useIntralineBg
parameter_list|)
block|{
name|LineMapper
name|mapper
init|=
name|getLineMapper
argument_list|()
decl_stmt|;
name|int
name|startA
init|=
name|mapper
operator|.
name|getLineA
argument_list|()
decl_stmt|;
name|int
name|startB
init|=
name|mapper
operator|.
name|getLineB
argument_list|()
decl_stmt|;
name|JsArrayString
name|a
init|=
name|region
operator|.
name|a
argument_list|()
decl_stmt|;
name|JsArrayString
name|b
init|=
name|region
operator|.
name|b
argument_list|()
decl_stmt|;
name|int
name|aLen
init|=
name|a
operator|!=
literal|null
condition|?
name|a
operator|.
name|length
argument_list|()
else|:
literal|0
decl_stmt|;
name|int
name|bLen
init|=
name|b
operator|!=
literal|null
condition|?
name|b
operator|.
name|length
argument_list|()
else|:
literal|0
decl_stmt|;
name|boolean
name|insertOrDelete
init|=
name|a
operator|==
literal|null
operator|||
name|b
operator|==
literal|null
decl_stmt|;
name|colorLines
argument_list|(
name|cm
argument_list|,
name|insertOrDelete
operator|&&
operator|!
name|useIntralineBg
condition|?
name|UnifiedTable
operator|.
name|style
operator|.
name|diffDelete
argument_list|()
else|:
name|UnifiedTable
operator|.
name|style
operator|.
name|intralineDelete
argument_list|()
argument_list|,
name|cmLine
argument_list|,
name|aLen
argument_list|)
expr_stmt|;
name|colorLines
argument_list|(
name|cm
argument_list|,
name|insertOrDelete
operator|&&
operator|!
name|useIntralineBg
condition|?
name|UnifiedTable
operator|.
name|style
operator|.
name|diffInsert
argument_list|()
else|:
name|UnifiedTable
operator|.
name|style
operator|.
name|intralineInsert
argument_list|()
argument_list|,
name|cmLine
operator|+
name|aLen
argument_list|,
name|bLen
argument_list|)
expr_stmt|;
name|markEdit
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|cmLine
argument_list|,
name|a
argument_list|,
name|region
operator|.
name|editA
argument_list|()
argument_list|)
expr_stmt|;
name|markEdit
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|cmLine
operator|+
name|aLen
argument_list|,
name|b
argument_list|,
name|region
operator|.
name|editB
argument_list|()
argument_list|)
expr_stmt|;
name|addGutterTag
argument_list|(
name|region
argument_list|,
name|cmLine
argument_list|)
expr_stmt|;
comment|// TODO: verify addGutterTag
name|mapper
operator|.
name|appendReplace
argument_list|(
name|aLen
argument_list|,
name|bLen
argument_list|)
expr_stmt|;
name|int
name|endA
init|=
name|mapper
operator|.
name|getLineA
argument_list|()
operator|-
literal|1
decl_stmt|;
name|int
name|endB
init|=
name|mapper
operator|.
name|getLineB
argument_list|()
operator|-
literal|1
decl_stmt|;
if|if
condition|(
name|aLen
operator|>
literal|0
condition|)
block|{
name|addDiffChunk
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|endA
argument_list|,
name|endB
argument_list|,
name|aLen
argument_list|,
name|cmLine
argument_list|,
name|bLen
operator|>
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|aLen
condition|;
name|j
operator|++
control|)
block|{
name|host
operator|.
name|setLineNumber
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|cmLine
operator|+
name|j
argument_list|,
name|startA
operator|+
name|j
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bLen
operator|>
literal|0
condition|)
block|{
name|addDiffChunk
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|endB
argument_list|,
name|endA
argument_list|,
name|bLen
argument_list|,
name|cmLine
operator|+
name|aLen
argument_list|,
name|aLen
operator|>
literal|0
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|bLen
condition|;
name|j
operator|++
control|)
block|{
name|host
operator|.
name|setLineNumber
argument_list|(
name|DisplaySide
operator|.
name|B
argument_list|,
name|cmLine
operator|+
name|aLen
operator|+
name|j
argument_list|,
name|startB
operator|+
name|j
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|aLen
operator|+
name|bLen
return|;
block|}
DECL|method|addGutterTag (Region region, int cmLine)
specifier|private
name|void
name|addGutterTag
parameter_list|(
name|Region
name|region
parameter_list|,
name|int
name|cmLine
parameter_list|)
block|{
name|Scrollbar
name|scrollbar
init|=
name|getScrollbar
argument_list|()
decl_stmt|;
if|if
condition|(
name|region
operator|.
name|a
argument_list|()
operator|==
literal|null
condition|)
block|{
name|scrollbar
operator|.
name|insert
argument_list|(
name|cm
argument_list|,
name|cmLine
argument_list|,
name|region
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|region
operator|.
name|b
argument_list|()
operator|==
literal|null
condition|)
block|{
name|scrollbar
operator|.
name|delete
argument_list|(
name|cm
argument_list|,
name|cm
argument_list|,
name|cmLine
argument_list|,
name|region
operator|.
name|a
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scrollbar
operator|.
name|edit
argument_list|(
name|cm
argument_list|,
name|cmLine
argument_list|,
name|region
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|markEdit (DisplaySide side, int startLine, JsArrayString lines, JsArray<Span> edits)
specifier|private
name|void
name|markEdit
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|startLine
parameter_list|,
name|JsArrayString
name|lines
parameter_list|,
name|JsArray
argument_list|<
name|Span
argument_list|>
name|edits
parameter_list|)
block|{
if|if
condition|(
name|lines
operator|==
literal|null
operator|||
name|edits
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|EditIterator
name|iter
init|=
operator|new
name|EditIterator
argument_list|(
name|lines
argument_list|,
name|startLine
argument_list|)
decl_stmt|;
name|Configuration
name|bg
init|=
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"className"
argument_list|,
name|getIntralineBgFromSide
argument_list|(
name|side
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|"readOnly"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Configuration
name|diff
init|=
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"className"
argument_list|,
name|getDiffColorFromSide
argument_list|(
name|side
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|"readOnly"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Pos
name|last
init|=
name|Pos
operator|.
name|create
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|Span
name|span
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|edits
argument_list|)
control|)
block|{
name|Pos
name|from
init|=
name|iter
operator|.
name|advance
argument_list|(
name|span
operator|.
name|skip
argument_list|()
argument_list|)
decl_stmt|;
name|Pos
name|to
init|=
name|iter
operator|.
name|advance
argument_list|(
name|span
operator|.
name|mark
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|from
operator|.
name|line
argument_list|()
operator|==
name|last
operator|.
name|line
argument_list|()
condition|)
block|{
name|getMarkers
argument_list|()
operator|.
name|add
argument_list|(
name|cm
operator|.
name|markText
argument_list|(
name|last
argument_list|,
name|from
argument_list|,
name|bg
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getMarkers
argument_list|()
operator|.
name|add
argument_list|(
name|cm
operator|.
name|markText
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|from
operator|.
name|line
argument_list|()
argument_list|,
literal|0
argument_list|)
argument_list|,
name|from
argument_list|,
name|bg
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getMarkers
argument_list|()
operator|.
name|add
argument_list|(
name|cm
operator|.
name|markText
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|diff
argument_list|)
argument_list|)
expr_stmt|;
name|last
operator|=
name|to
expr_stmt|;
name|colorLines
argument_list|(
name|cm
argument_list|,
name|LineClassWhere
operator|.
name|BACKGROUND
argument_list|,
name|getDiffColorFromSide
argument_list|(
name|side
argument_list|)
argument_list|,
name|from
operator|.
name|line
argument_list|()
argument_list|,
name|to
operator|.
name|line
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getIntralineBgFromSide (DisplaySide side)
specifier|private
name|String
name|getIntralineBgFromSide
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
name|UnifiedTable
operator|.
name|style
operator|.
name|intralineDelete
argument_list|()
else|:
name|UnifiedTable
operator|.
name|style
operator|.
name|intralineInsert
argument_list|()
return|;
block|}
DECL|method|getDiffColorFromSide (DisplaySide side)
specifier|private
name|String
name|getDiffColorFromSide
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
name|UnifiedTable
operator|.
name|style
operator|.
name|diffDelete
argument_list|()
else|:
name|UnifiedTable
operator|.
name|style
operator|.
name|diffInsert
argument_list|()
return|;
block|}
DECL|method|addDiffChunk (DisplaySide side, int chunkEnd, int otherChunkEnd, int chunkSize, int cmLine, boolean edit)
specifier|private
name|void
name|addDiffChunk
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|chunkEnd
parameter_list|,
name|int
name|otherChunkEnd
parameter_list|,
name|int
name|chunkSize
parameter_list|,
name|int
name|cmLine
parameter_list|,
name|boolean
name|edit
parameter_list|)
block|{
name|chunks
operator|.
name|add
argument_list|(
operator|new
name|UnifiedDiffChunkInfo
argument_list|(
name|side
argument_list|,
name|chunkEnd
operator|-
name|chunkSize
operator|+
literal|1
argument_list|,
name|otherChunkEnd
operator|-
name|chunkSize
operator|+
literal|1
argument_list|,
name|chunkEnd
argument_list|,
name|cmLine
argument_list|,
name|edit
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|diffChunkNav (final CodeMirror cm, final Direction dir)
name|Runnable
name|diffChunkNav
parameter_list|(
specifier|final
name|CodeMirror
name|cm
parameter_list|,
specifier|final
name|Direction
name|dir
parameter_list|)
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
name|int
name|line
init|=
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|hasActiveLine
argument_list|()
condition|?
name|cm
operator|.
name|getLineNumber
argument_list|(
name|cm
operator|.
name|extras
argument_list|()
operator|.
name|activeLine
argument_list|()
argument_list|)
else|:
literal|0
decl_stmt|;
name|int
name|res
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|chunks
argument_list|,
operator|new
name|UnifiedDiffChunkInfo
argument_list|(
name|cm
operator|.
name|side
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|line
argument_list|,
literal|false
argument_list|)
argument_list|,
name|getDiffChunkComparatorCmLine
argument_list|()
argument_list|)
decl_stmt|;
name|diffChunkNavHelper
argument_list|(
name|chunks
argument_list|,
name|host
argument_list|,
name|res
argument_list|,
name|dir
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/** Diff chunks are ordered by their starting lines in CodeMirror */
DECL|method|getDiffChunkComparatorCmLine ()
specifier|private
name|Comparator
argument_list|<
name|UnifiedDiffChunkInfo
argument_list|>
name|getDiffChunkComparatorCmLine
parameter_list|()
block|{
return|return
operator|new
name|Comparator
argument_list|<
name|UnifiedDiffChunkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|UnifiedDiffChunkInfo
name|o1
parameter_list|,
name|UnifiedDiffChunkInfo
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|cmLine
operator|-
name|o2
operator|.
name|cmLine
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|getCmLine (int line, DisplaySide side)
name|int
name|getCmLine
parameter_list|(
name|int
name|line
parameter_list|,
name|DisplaySide
name|side
parameter_list|)
block|{
name|int
name|res
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|chunks
argument_list|,
operator|new
name|UnifiedDiffChunkInfo
argument_list|(
name|side
argument_list|,
name|line
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|)
decl_stmt|;
comment|// Dummy DiffChunkInfo
if|if
condition|(
name|res
operator|>=
literal|0
condition|)
block|{
return|return
name|chunks
operator|.
name|get
argument_list|(
name|res
argument_list|)
operator|.
name|cmLine
return|;
block|}
else|else
block|{
comment|// The line might be within a DiffChunk
name|res
operator|=
operator|-
name|res
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|res
operator|>
literal|0
condition|)
block|{
name|UnifiedDiffChunkInfo
name|info
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|side
operator|==
name|DisplaySide
operator|.
name|A
operator|&&
name|info
operator|.
name|edit
operator|&&
name|info
operator|.
name|side
operator|==
name|DisplaySide
operator|.
name|B
condition|)
block|{
comment|// Need to use the start and cmLine of the deletion chunk
name|UnifiedDiffChunkInfo
name|delete
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
operator|-
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|line
operator|<=
name|delete
operator|.
name|end
condition|)
block|{
return|return
name|delete
operator|.
name|cmLine
operator|+
name|line
operator|-
name|delete
operator|.
name|start
return|;
block|}
else|else
block|{
comment|// Need to add the length of the insertion chunk
return|return
name|delete
operator|.
name|cmLine
operator|+
name|line
operator|-
name|delete
operator|.
name|start
operator|+
name|info
operator|.
name|end
operator|-
name|info
operator|.
name|start
operator|+
literal|1
return|;
block|}
block|}
elseif|else
if|if
condition|(
name|side
operator|==
name|info
operator|.
name|side
condition|)
block|{
return|return
name|info
operator|.
name|cmLine
operator|+
name|line
operator|-
name|info
operator|.
name|start
return|;
block|}
else|else
block|{
return|return
name|info
operator|.
name|cmLine
operator|+
name|getLineMapper
argument_list|()
operator|.
name|lineOnOther
argument_list|(
name|side
argument_list|,
name|line
argument_list|)
operator|.
name|getLine
argument_list|()
operator|-
name|info
operator|.
name|start
return|;
block|}
block|}
else|else
block|{
return|return
name|line
return|;
block|}
block|}
block|}
DECL|method|getLineRegionInfoFromCmLine (int cmLine)
name|LineRegionInfo
name|getLineRegionInfoFromCmLine
parameter_list|(
name|int
name|cmLine
parameter_list|)
block|{
name|int
name|res
init|=
name|Collections
operator|.
name|binarySearch
argument_list|(
name|chunks
argument_list|,
operator|new
name|UnifiedDiffChunkInfo
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|,
name|cmLine
argument_list|,
literal|false
argument_list|)
argument_list|,
comment|// Dummy DiffChunkInfo
name|getDiffChunkComparatorCmLine
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|res
operator|>=
literal|0
condition|)
block|{
comment|// The line is right at the start of a diff chunk.
name|UnifiedDiffChunkInfo
name|info
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
argument_list|)
decl_stmt|;
return|return
operator|new
name|LineRegionInfo
argument_list|(
name|info
operator|.
name|start
argument_list|,
name|displaySideToRegionType
argument_list|(
name|info
operator|.
name|side
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// The line might be within or after a diff chunk.
name|res
operator|=
operator|-
name|res
operator|-
literal|1
expr_stmt|;
if|if
condition|(
name|res
operator|>
literal|0
condition|)
block|{
name|UnifiedDiffChunkInfo
name|info
init|=
name|chunks
operator|.
name|get
argument_list|(
name|res
operator|-
literal|1
argument_list|)
decl_stmt|;
name|int
name|lineOnInfoSide
init|=
name|info
operator|.
name|start
operator|+
name|cmLine
operator|-
name|info
operator|.
name|cmLine
decl_stmt|;
if|if
condition|(
name|lineOnInfoSide
operator|>
name|info
operator|.
name|end
condition|)
block|{
comment|// After a diff chunk
if|if
condition|(
name|info
operator|.
name|side
operator|==
name|DisplaySide
operator|.
name|A
condition|)
block|{
comment|// For the common region after a deletion chunk, associate the line
comment|// on side B with a common region.
return|return
operator|new
name|LineRegionInfo
argument_list|(
name|getLineMapper
argument_list|()
operator|.
name|lineOnOther
argument_list|(
name|DisplaySide
operator|.
name|A
argument_list|,
name|lineOnInfoSide
argument_list|)
operator|.
name|getLine
argument_list|()
argument_list|,
name|RegionType
operator|.
name|COMMON
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|LineRegionInfo
argument_list|(
name|lineOnInfoSide
argument_list|,
name|RegionType
operator|.
name|COMMON
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// Within a diff chunk
return|return
operator|new
name|LineRegionInfo
argument_list|(
name|lineOnInfoSide
argument_list|,
name|displaySideToRegionType
argument_list|(
name|info
operator|.
name|side
argument_list|)
argument_list|)
return|;
block|}
block|}
else|else
block|{
comment|// The line is before any diff chunk, so it always equals cmLine and
comment|// belongs to a common region.
return|return
operator|new
name|LineRegionInfo
argument_list|(
name|cmLine
argument_list|,
name|RegionType
operator|.
name|COMMON
argument_list|)
return|;
block|}
block|}
block|}
DECL|enum|RegionType
enum|enum
name|RegionType
block|{
DECL|enumConstant|INSERT
DECL|enumConstant|DELETE
DECL|enumConstant|COMMON
name|INSERT
block|,
name|DELETE
block|,
name|COMMON
block|,   }
DECL|method|displaySideToRegionType (DisplaySide side)
specifier|private
specifier|static
name|RegionType
name|displaySideToRegionType
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
name|RegionType
operator|.
name|DELETE
else|:
name|RegionType
operator|.
name|INSERT
return|;
block|}
comment|/**    * Helper class to associate a line in the original file with the type of the    * region it belongs to.    *    * @field line The 0-based line number in the original file. Note that this    *     might be different from the line number shown in CodeMirror.    * @field type The type of the region the line belongs to. Can be INSERT,    *     DELETE or COMMON.    */
DECL|class|LineRegionInfo
specifier|static
class|class
name|LineRegionInfo
block|{
DECL|field|line
specifier|final
name|int
name|line
decl_stmt|;
DECL|field|type
specifier|final
name|RegionType
name|type
decl_stmt|;
DECL|method|LineRegionInfo (int line, RegionType type)
name|LineRegionInfo
parameter_list|(
name|int
name|line
parameter_list|,
name|RegionType
name|type
parameter_list|)
block|{
name|this
operator|.
name|line
operator|=
name|line
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getSide ()
name|DisplaySide
name|getSide
parameter_list|()
block|{
comment|// Always return DisplaySide.B for INSERT or COMMON
return|return
name|type
operator|==
name|RegionType
operator|.
name|DELETE
condition|?
name|DisplaySide
operator|.
name|A
else|:
name|DisplaySide
operator|.
name|B
return|;
block|}
block|}
block|}
end_class

end_unit

