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
name|B
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
name|dom
operator|.
name|client
operator|.
name|Style
operator|.
name|Unit
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
name|LineWidget
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
comment|/** Colors modified regions for {@link SideBySide}. */
end_comment

begin_class
DECL|class|ChunkManager
class|class
name|ChunkManager
block|{
DECL|field|DATA_LINES
specifier|private
specifier|static
specifier|final
name|String
name|DATA_LINES
init|=
literal|"_cs2h"
decl_stmt|;
DECL|field|guessedLineHeightPx
specifier|private
specifier|static
name|double
name|guessedLineHeightPx
init|=
literal|15
decl_stmt|;
DECL|field|focusA
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|focusA
init|=
name|initOnClick
argument_list|(
name|A
argument_list|)
decl_stmt|;
DECL|field|focusB
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|focusB
init|=
name|initOnClick
argument_list|(
name|B
argument_list|)
decl_stmt|;
DECL|method|initOnClick (DisplaySide s)
specifier|private
specifier|static
specifier|final
specifier|native
name|JavaScriptObject
name|initOnClick
parameter_list|(
name|DisplaySide
name|s
parameter_list|)
comment|/*-{     return $entry(function(e){       @com.google.gerrit.client.diff.ChunkManager::focus(         Lcom/google/gwt/dom/client/NativeEvent;         Lcom/google/gerrit/client/diff/DisplaySide;)(e,s)     });   }-*/
function_decl|;
DECL|method|focus (NativeEvent event, DisplaySide side)
specifier|private
specifier|static
name|void
name|focus
parameter_list|(
name|NativeEvent
name|event
parameter_list|,
name|DisplaySide
name|side
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
name|SideBySide
condition|)
block|{
operator|(
operator|(
name|SideBySide
operator|)
name|l
operator|)
operator|.
name|getCmFromSide
argument_list|(
name|side
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
DECL|method|focusOnClick (Element e, DisplaySide side)
specifier|static
name|void
name|focusOnClick
parameter_list|(
name|Element
name|e
parameter_list|,
name|DisplaySide
name|side
parameter_list|)
block|{
name|onClick
argument_list|(
name|e
argument_list|,
name|side
operator|==
name|A
condition|?
name|focusA
else|:
name|focusB
argument_list|)
expr_stmt|;
block|}
DECL|method|onClick (Element e, JavaScriptObject f)
specifier|private
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
DECL|field|host
specifier|private
specifier|final
name|SideBySide
name|host
decl_stmt|;
DECL|field|cmA
specifier|private
specifier|final
name|CodeMirror
name|cmA
decl_stmt|;
DECL|field|cmB
specifier|private
specifier|final
name|CodeMirror
name|cmB
decl_stmt|;
DECL|field|scrollbar
specifier|private
specifier|final
name|Scrollbar
name|scrollbar
decl_stmt|;
DECL|field|mapper
specifier|private
specifier|final
name|LineMapper
name|mapper
decl_stmt|;
DECL|field|chunks
specifier|private
name|List
argument_list|<
name|DiffChunkInfo
argument_list|>
name|chunks
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
DECL|field|padding
specifier|private
name|List
argument_list|<
name|LineWidget
argument_list|>
name|padding
decl_stmt|;
DECL|field|paddingDivs
specifier|private
name|List
argument_list|<
name|Element
argument_list|>
name|paddingDivs
decl_stmt|;
DECL|method|ChunkManager (SideBySide host, CodeMirror cmA, CodeMirror cmB, Scrollbar scrollbar)
name|ChunkManager
parameter_list|(
name|SideBySide
name|host
parameter_list|,
name|CodeMirror
name|cmA
parameter_list|,
name|CodeMirror
name|cmB
parameter_list|,
name|Scrollbar
name|scrollbar
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|cmA
operator|=
name|cmA
expr_stmt|;
name|this
operator|.
name|cmB
operator|=
name|cmB
expr_stmt|;
name|this
operator|.
name|scrollbar
operator|=
name|scrollbar
expr_stmt|;
name|this
operator|.
name|mapper
operator|=
operator|new
name|LineMapper
argument_list|()
expr_stmt|;
block|}
DECL|method|getLineMapper ()
name|LineMapper
name|getLineMapper
parameter_list|()
block|{
return|return
name|mapper
return|;
block|}
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
DECL|method|reset ()
name|void
name|reset
parameter_list|()
block|{
name|mapper
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
for|for
control|(
name|LineWidget
name|w
range|:
name|padding
control|)
block|{
name|w
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|render (DiffInfo diff)
name|void
name|render
parameter_list|(
name|DiffInfo
name|diff
parameter_list|)
block|{
name|chunks
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
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
name|padding
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|paddingDivs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|String
name|diffColor
init|=
name|diff
operator|.
name|meta_a
argument_list|()
operator|==
literal|null
operator|||
name|diff
operator|.
name|meta_b
argument_list|()
operator|==
literal|null
condition|?
name|DiffTable
operator|.
name|style
operator|.
name|intralineBg
argument_list|()
else|:
name|DiffTable
operator|.
name|style
operator|.
name|diff
argument_list|()
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
name|mapper
operator|.
name|appendCommon
argument_list|(
name|current
operator|.
name|ab
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
block|}
else|else
block|{
name|render
argument_list|(
name|current
argument_list|,
name|diffColor
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|paddingDivs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|paddingDivs
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|adjustPadding ()
name|void
name|adjustPadding
parameter_list|()
block|{
if|if
condition|(
name|paddingDivs
operator|!=
literal|null
condition|)
block|{
name|double
name|h
init|=
name|cmB
operator|.
name|extras
argument_list|()
operator|.
name|lineHeightPx
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|div
range|:
name|paddingDivs
control|)
block|{
name|int
name|lines
init|=
name|div
operator|.
name|getPropertyInt
argument_list|(
name|DATA_LINES
argument_list|)
decl_stmt|;
name|div
operator|.
name|getStyle
argument_list|()
operator|.
name|setHeight
argument_list|(
name|lines
operator|*
name|h
argument_list|,
name|Unit
operator|.
name|PX
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|LineWidget
name|w
range|:
name|padding
control|)
block|{
name|w
operator|.
name|changed
argument_list|()
expr_stmt|;
block|}
name|paddingDivs
operator|=
literal|null
expr_stmt|;
name|guessedLineHeightPx
operator|=
name|h
expr_stmt|;
block|}
block|}
DECL|method|render (Region region, String diffColor)
specifier|private
name|void
name|render
parameter_list|(
name|Region
name|region
parameter_list|,
name|String
name|diffColor
parameter_list|)
block|{
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
name|String
name|color
init|=
name|a
operator|==
literal|null
operator|||
name|b
operator|==
literal|null
condition|?
name|diffColor
else|:
name|DiffTable
operator|.
name|style
operator|.
name|intralineBg
argument_list|()
decl_stmt|;
name|colorLines
argument_list|(
name|cmA
argument_list|,
name|color
argument_list|,
name|startA
argument_list|,
name|aLen
argument_list|)
expr_stmt|;
name|colorLines
argument_list|(
name|cmB
argument_list|,
name|color
argument_list|,
name|startB
argument_list|,
name|bLen
argument_list|)
expr_stmt|;
name|markEdit
argument_list|(
name|cmA
argument_list|,
name|startA
argument_list|,
name|a
argument_list|,
name|region
operator|.
name|edit_a
argument_list|()
argument_list|)
expr_stmt|;
name|markEdit
argument_list|(
name|cmB
argument_list|,
name|startB
argument_list|,
name|b
argument_list|,
name|region
operator|.
name|edit_b
argument_list|()
argument_list|)
expr_stmt|;
name|addPadding
argument_list|(
name|cmA
argument_list|,
name|startA
operator|+
name|aLen
operator|-
literal|1
argument_list|,
name|bLen
operator|-
name|aLen
argument_list|)
expr_stmt|;
name|addPadding
argument_list|(
name|cmB
argument_list|,
name|startB
operator|+
name|bLen
operator|-
literal|1
argument_list|,
name|aLen
operator|-
name|bLen
argument_list|)
expr_stmt|;
name|addGutterTag
argument_list|(
name|region
argument_list|,
name|startA
argument_list|,
name|startB
argument_list|)
expr_stmt|;
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
name|cmB
argument_list|,
name|endA
argument_list|,
name|aLen
argument_list|,
name|bLen
operator|>
literal|0
argument_list|)
expr_stmt|;
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
name|cmA
argument_list|,
name|endB
argument_list|,
name|bLen
argument_list|,
name|aLen
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addGutterTag (Region region, int startA, int startB)
specifier|private
name|void
name|addGutterTag
parameter_list|(
name|Region
name|region
parameter_list|,
name|int
name|startA
parameter_list|,
name|int
name|startB
parameter_list|)
block|{
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
name|cmB
argument_list|,
name|startB
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
name|cmA
argument_list|,
name|cmB
argument_list|,
name|startA
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
name|cmB
argument_list|,
name|startB
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
DECL|method|markEdit (CodeMirror cm, int startLine, JsArrayString lines, JsArray<Span> edits)
specifier|private
name|void
name|markEdit
parameter_list|(
name|CodeMirror
name|cm
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
name|DiffTable
operator|.
name|style
operator|.
name|intralineBg
argument_list|()
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
name|DiffTable
operator|.
name|style
operator|.
name|diff
argument_list|()
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
name|markers
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
name|markers
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
name|markers
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
name|DiffTable
operator|.
name|style
operator|.
name|diff
argument_list|()
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
DECL|method|colorLines (CodeMirror cm, String color, int line, int cnt)
specifier|private
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
specifier|private
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
comment|/**    * Insert a new padding div below the given line.    *    * @param cm parent CodeMirror to add extra space into.    * @param line line to put the padding below.    * @param len number of lines to pad. Padding is inserted only if    *        {@code len>= 1}.    */
DECL|method|addPadding (CodeMirror cm, int line, final int len)
specifier|private
name|void
name|addPadding
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|int
name|line
parameter_list|,
specifier|final
name|int
name|len
parameter_list|)
block|{
if|if
condition|(
literal|0
operator|<
name|len
condition|)
block|{
name|Element
name|pad
init|=
name|DOM
operator|.
name|createDiv
argument_list|()
decl_stmt|;
name|pad
operator|.
name|setClassName
argument_list|(
name|DiffTable
operator|.
name|style
operator|.
name|padding
argument_list|()
argument_list|)
expr_stmt|;
name|pad
operator|.
name|setPropertyInt
argument_list|(
name|DATA_LINES
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|pad
operator|.
name|getStyle
argument_list|()
operator|.
name|setHeight
argument_list|(
name|guessedLineHeightPx
operator|*
name|len
argument_list|,
name|Unit
operator|.
name|PX
argument_list|)
expr_stmt|;
name|focusOnClick
argument_list|(
name|pad
argument_list|,
name|cm
operator|.
name|side
argument_list|()
argument_list|)
expr_stmt|;
name|paddingDivs
operator|.
name|add
argument_list|(
name|pad
argument_list|)
expr_stmt|;
name|padding
operator|.
name|add
argument_list|(
name|cm
operator|.
name|addLineWidget
argument_list|(
name|line
operator|==
operator|-
literal|1
condition|?
literal|0
else|:
name|line
argument_list|,
name|pad
argument_list|,
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"coverGutter"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"noHScroll"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"above"
argument_list|,
name|line
operator|==
operator|-
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addDiffChunk (CodeMirror cmToPad, int lineOnOther, int chunkSize, boolean edit)
specifier|private
name|void
name|addDiffChunk
parameter_list|(
name|CodeMirror
name|cmToPad
parameter_list|,
name|int
name|lineOnOther
parameter_list|,
name|int
name|chunkSize
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
name|DiffChunkInfo
argument_list|(
name|host
operator|.
name|otherCm
argument_list|(
name|cmToPad
argument_list|)
operator|.
name|side
argument_list|()
argument_list|,
name|lineOnOther
operator|-
name|chunkSize
operator|+
literal|1
argument_list|,
name|lineOnOther
argument_list|,
name|edit
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|DiffChunkInfo
argument_list|(
name|cm
operator|.
name|side
argument_list|()
argument_list|,
name|line
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
name|getDiffChunkComparator
argument_list|()
argument_list|)
decl_stmt|;
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
name|targetCm
operator|.
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|target
operator|.
name|getStart
argument_list|()
argument_list|,
literal|0
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
name|target
operator|.
name|getStart
argument_list|()
argument_list|,
literal|"local"
argument_list|)
operator|-
literal|0.5
operator|*
name|cmB
operator|.
name|scrollbarV
argument_list|()
operator|.
name|getClientHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getDiffChunkComparator ()
specifier|private
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
name|mapper
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
name|mapper
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
DECL|method|getDiffChunk (DisplaySide side, int line)
name|DiffChunkInfo
name|getDiffChunk
parameter_list|(
name|DisplaySide
name|side
parameter_list|,
name|int
name|line
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
name|DiffChunkInfo
argument_list|(
name|side
argument_list|,
name|line
argument_list|,
literal|0
argument_list|,
literal|false
argument_list|)
argument_list|,
comment|// Dummy DiffChunkInfo
name|getDiffChunkComparator
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
return|return
name|chunks
operator|.
name|get
argument_list|(
name|res
argument_list|)
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
name|DiffChunkInfo
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
name|info
operator|.
name|getSide
argument_list|()
operator|==
name|side
operator|&&
name|info
operator|.
name|getStart
argument_list|()
operator|<=
name|line
operator|&&
name|line
operator|<=
name|info
operator|.
name|getEnd
argument_list|()
condition|)
block|{
return|return
name|info
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

