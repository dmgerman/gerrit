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
name|CallbackGroup
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
name|rpc
operator|.
name|ScreenLoadCallback
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
name|Screen
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
name|client
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ResizeEvent
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ResizeHandler
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
name|event
operator|.
name|shared
operator|.
name|HandlerRegistration
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
name|Window
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
name|LineCharacter
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
name|ModeInjector
import|;
end_import

begin_class
DECL|class|CodeMirrorDemo
specifier|public
class|class
name|CodeMirrorDemo
extends|extends
name|Screen
block|{
DECL|field|HEADER_FOOTER
specifier|private
specifier|static
specifier|final
name|int
name|HEADER_FOOTER
init|=
literal|60
operator|+
literal|15
operator|*
literal|2
operator|+
literal|38
decl_stmt|;
DECL|field|base
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|base
decl_stmt|;
DECL|field|revision
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|revision
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|diffTable
specifier|private
name|DiffTable
name|diffTable
decl_stmt|;
DECL|field|cmA
specifier|private
name|CodeMirror
name|cmA
decl_stmt|;
DECL|field|cmB
specifier|private
name|CodeMirror
name|cmB
decl_stmt|;
DECL|field|resizeHandler
specifier|private
name|HandlerRegistration
name|resizeHandler
decl_stmt|;
DECL|method|CodeMirrorDemo ( PatchSet.Id base, PatchSet.Id revision, String path)
specifier|public
name|CodeMirrorDemo
parameter_list|(
name|PatchSet
operator|.
name|Id
name|base
parameter_list|,
name|PatchSet
operator|.
name|Id
name|revision
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|base
operator|=
name|base
expr_stmt|;
name|this
operator|.
name|revision
operator|=
name|revision
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|diffTable
operator|=
operator|new
name|DiffTable
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|CallbackGroup
name|group
init|=
operator|new
name|CallbackGroup
argument_list|()
decl_stmt|;
name|CodeMirror
operator|.
name|initLibrary
argument_list|(
name|group
operator|.
name|add
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|Void
name|result
parameter_list|)
block|{       }
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|DiffApi
operator|.
name|diff
argument_list|(
name|revision
argument_list|,
name|path
argument_list|)
operator|.
name|base
argument_list|(
name|base
argument_list|)
operator|.
name|wholeFile
argument_list|()
operator|.
name|intraline
argument_list|()
operator|.
name|ignoreWhitespace
argument_list|(
name|DiffApi
operator|.
name|IgnoreWhitespace
operator|.
name|NONE
argument_list|)
operator|.
name|get
argument_list|(
name|group
operator|.
name|add
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|DiffInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|DiffInfo
name|diff
parameter_list|)
block|{
operator|new
name|ModeInjector
argument_list|()
operator|.
name|add
argument_list|(
name|getContentType
argument_list|(
name|diff
operator|.
name|meta_a
argument_list|()
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|getContentType
argument_list|(
name|diff
operator|.
name|meta_b
argument_list|()
argument_list|)
argument_list|)
operator|.
name|inject
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|Void
argument_list|>
argument_list|(
name|CodeMirrorDemo
operator|.
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
name|Void
name|result
parameter_list|)
block|{
name|display
argument_list|(
name|diff
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onShowView ()
specifier|public
name|void
name|onShowView
parameter_list|()
block|{
name|super
operator|.
name|onShowView
argument_list|()
expr_stmt|;
if|if
condition|(
name|cmA
operator|!=
literal|null
condition|)
block|{
name|cmA
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cmB
operator|!=
literal|null
condition|)
block|{
name|cmB
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
if|if
condition|(
name|resizeHandler
operator|!=
literal|null
condition|)
block|{
name|resizeHandler
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
name|resizeHandler
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|cmA
operator|!=
literal|null
condition|)
block|{
name|cmA
operator|.
name|getWrapperElement
argument_list|()
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|cmA
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|cmB
operator|!=
literal|null
condition|)
block|{
name|cmB
operator|.
name|getWrapperElement
argument_list|()
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|cmB
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|display (DiffInfo diff)
specifier|private
name|void
name|display
parameter_list|(
name|DiffInfo
name|diff
parameter_list|)
block|{
name|cmA
operator|=
name|displaySide
argument_list|(
name|diff
operator|.
name|meta_a
argument_list|()
argument_list|,
name|diff
operator|.
name|text_a
argument_list|()
argument_list|,
name|diffTable
operator|.
name|getCmA
argument_list|()
argument_list|)
expr_stmt|;
name|cmB
operator|=
name|displaySide
argument_list|(
name|diff
operator|.
name|meta_b
argument_list|()
argument_list|,
name|diff
operator|.
name|text_b
argument_list|()
argument_list|,
name|diffTable
operator|.
name|getCmB
argument_list|()
argument_list|)
expr_stmt|;
name|render
argument_list|(
name|diff
argument_list|)
expr_stmt|;
name|resizeHandler
operator|=
name|Window
operator|.
name|addResizeHandler
argument_list|(
operator|new
name|ResizeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResize
parameter_list|(
name|ResizeEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|cmA
operator|!=
literal|null
condition|)
block|{
name|cmA
operator|.
name|setHeight
argument_list|(
name|event
operator|.
name|getHeight
argument_list|()
operator|-
name|HEADER_FOOTER
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cmB
operator|!=
literal|null
condition|)
block|{
name|cmB
operator|.
name|setHeight
argument_list|(
name|event
operator|.
name|getHeight
argument_list|()
operator|-
name|HEADER_FOOTER
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|refresh
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|cmA
operator|.
name|on
argument_list|(
literal|"scroll"
argument_list|,
name|doScroll
argument_list|(
name|cmB
argument_list|)
argument_list|)
expr_stmt|;
name|cmB
operator|.
name|on
argument_list|(
literal|"scroll"
argument_list|,
name|doScroll
argument_list|(
name|cmA
argument_list|)
argument_list|)
expr_stmt|;
name|Window
operator|.
name|enableScrolling
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|displaySide (DiffInfo.FileMeta meta, String contents, Element ele)
specifier|private
name|CodeMirror
name|displaySide
parameter_list|(
name|DiffInfo
operator|.
name|FileMeta
name|meta
parameter_list|,
name|String
name|contents
parameter_list|,
name|Element
name|ele
parameter_list|)
block|{
if|if
condition|(
name|meta
operator|==
literal|null
condition|)
block|{
name|contents
operator|=
literal|""
expr_stmt|;
block|}
name|Configuration
name|cfg
init|=
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"readOnly"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"lineNumbers"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"tabSize"
argument_list|,
literal|2
argument_list|)
operator|.
name|set
argument_list|(
literal|"mode"
argument_list|,
name|getContentType
argument_list|(
name|meta
argument_list|)
argument_list|)
operator|.
name|set
argument_list|(
literal|"styleSelectedText"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"value"
argument_list|,
name|contents
argument_list|)
decl_stmt|;
specifier|final
name|CodeMirror
name|cm
init|=
name|CodeMirror
operator|.
name|create
argument_list|(
name|ele
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setWidth
argument_list|(
literal|"100%"
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setHeight
argument_list|(
name|Window
operator|.
name|getClientHeight
argument_list|()
operator|-
name|HEADER_FOOTER
argument_list|)
expr_stmt|;
return|return
name|cm
return|;
block|}
DECL|method|render (DiffInfo diff)
specifier|private
name|void
name|render
parameter_list|(
name|DiffInfo
name|diff
parameter_list|)
block|{
name|JsArray
argument_list|<
name|Region
argument_list|>
name|regions
init|=
name|diff
operator|.
name|content
argument_list|()
decl_stmt|;
name|int
name|lineA
init|=
literal|0
decl_stmt|,
name|lineB
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|regions
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Region
name|current
init|=
name|regions
operator|.
name|get
argument_list|(
name|i
argument_list|)
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
name|lineA
operator|+=
name|current
operator|.
name|ab
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
name|lineB
operator|+=
name|current
operator|.
name|ab
argument_list|()
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|a
argument_list|()
operator|==
literal|null
operator|&&
name|current
operator|.
name|b
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|int
name|delta
init|=
name|current
operator|.
name|b
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|insertEmptyLines
argument_list|(
name|cmA
argument_list|,
name|lineA
argument_list|,
name|delta
argument_list|)
expr_stmt|;
name|lineB
operator|=
name|colorLines
argument_list|(
name|cmB
argument_list|,
name|lineB
argument_list|,
name|delta
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|current
operator|.
name|a
argument_list|()
operator|!=
literal|null
operator|&&
name|current
operator|.
name|b
argument_list|()
operator|==
literal|null
condition|)
block|{
name|int
name|delta
init|=
name|current
operator|.
name|a
argument_list|()
operator|.
name|length
argument_list|()
decl_stmt|;
name|insertEmptyLines
argument_list|(
name|cmB
argument_list|,
name|lineB
argument_list|,
name|delta
argument_list|)
expr_stmt|;
name|lineA
operator|=
name|colorLines
argument_list|(
name|cmA
argument_list|,
name|lineA
argument_list|,
name|delta
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|JsArrayString
name|currentA
init|=
name|current
operator|.
name|a
argument_list|()
decl_stmt|;
name|JsArrayString
name|currentB
init|=
name|current
operator|.
name|b
argument_list|()
decl_stmt|;
name|int
name|aLength
init|=
name|currentA
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|bLength
init|=
name|currentB
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|origLineA
init|=
name|lineA
decl_stmt|;
name|int
name|origLineB
init|=
name|lineB
decl_stmt|;
name|lineA
operator|=
name|colorLines
argument_list|(
name|cmA
argument_list|,
name|lineA
argument_list|,
name|aLength
argument_list|)
expr_stmt|;
name|lineB
operator|=
name|colorLines
argument_list|(
name|cmB
argument_list|,
name|lineB
argument_list|,
name|bLength
argument_list|)
expr_stmt|;
if|if
condition|(
name|aLength
operator|<
name|bLength
condition|)
block|{
name|insertEmptyLines
argument_list|(
name|cmA
argument_list|,
name|lineA
argument_list|,
name|bLength
operator|-
name|aLength
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aLength
operator|>
name|bLength
condition|)
block|{
name|insertEmptyLines
argument_list|(
name|cmB
argument_list|,
name|lineB
argument_list|,
name|aLength
operator|-
name|bLength
argument_list|)
expr_stmt|;
block|}
name|markEdit
argument_list|(
name|cmA
argument_list|,
name|currentA
argument_list|,
name|current
operator|.
name|edit_a
argument_list|()
argument_list|,
name|origLineA
argument_list|)
expr_stmt|;
name|markEdit
argument_list|(
name|cmB
argument_list|,
name|currentB
argument_list|,
name|current
operator|.
name|edit_b
argument_list|()
argument_list|,
name|origLineB
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|insertEmptyLines (CodeMirror cm, int line, int cnt)
specifier|private
name|void
name|insertEmptyLines
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|int
name|line
parameter_list|,
name|int
name|cnt
parameter_list|)
block|{
name|Element
name|div
init|=
name|DOM
operator|.
name|createDiv
argument_list|()
decl_stmt|;
name|div
operator|.
name|setClassName
argument_list|(
name|diffTable
operator|.
name|style
operator|.
name|padding
argument_list|()
argument_list|)
expr_stmt|;
name|div
operator|.
name|getStyle
argument_list|()
operator|.
name|setHeight
argument_list|(
name|cnt
argument_list|,
name|Unit
operator|.
name|EM
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
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
literal|"above"
argument_list|,
name|line
operator|==
literal|0
argument_list|)
decl_stmt|;
name|cm
operator|.
name|addLineWidget
argument_list|(
name|line
operator|==
literal|0
condition|?
literal|0
else|:
operator|(
name|line
operator|-
literal|1
operator|)
argument_list|,
name|div
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
DECL|method|colorLines (CodeMirror cm, int line, int cnt)
specifier|private
name|int
name|colorLines
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|int
name|line
parameter_list|,
name|int
name|cnt
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|cnt
condition|;
name|i
operator|++
control|)
block|{
name|cm
operator|.
name|addLineClass
argument_list|(
name|line
operator|+
name|i
argument_list|,
name|LineClassWhere
operator|.
name|WRAP
argument_list|,
name|diffTable
operator|.
name|style
operator|.
name|diff
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|line
operator|+
name|cnt
return|;
block|}
DECL|method|markEdit (CodeMirror cm, JsArrayString lines, JsArray<Span> edits, int startLine)
specifier|private
name|void
name|markEdit
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|JsArrayString
name|lines
parameter_list|,
name|JsArray
argument_list|<
name|Span
argument_list|>
name|edits
parameter_list|,
name|int
name|startLine
parameter_list|)
block|{
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
name|diffOpt
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
name|diffTable
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
name|Configuration
name|editOpt
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
name|diffTable
operator|.
name|style
operator|.
name|intraline
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
name|LineCharacter
name|last
init|=
name|LineCharacter
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
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|edits
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Span
name|span
init|=
name|edits
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|LineCharacter
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
name|LineCharacter
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
name|int
name|fromLine
init|=
name|from
operator|.
name|getLine
argument_list|()
decl_stmt|;
if|if
condition|(
name|last
operator|.
name|getLine
argument_list|()
operator|==
name|fromLine
condition|)
block|{
name|cm
operator|.
name|markText
argument_list|(
name|last
argument_list|,
name|from
argument_list|,
name|diffOpt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cm
operator|.
name|markText
argument_list|(
name|LineCharacter
operator|.
name|create
argument_list|(
name|fromLine
argument_list|,
literal|0
argument_list|)
argument_list|,
name|from
argument_list|,
name|diffOpt
argument_list|)
expr_stmt|;
block|}
name|cm
operator|.
name|markText
argument_list|(
name|from
argument_list|,
name|to
argument_list|,
name|editOpt
argument_list|)
expr_stmt|;
name|last
operator|=
name|to
expr_stmt|;
for|for
control|(
name|int
name|line
init|=
name|fromLine
init|;
name|line
operator|<
name|to
operator|.
name|getLine
argument_list|()
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
name|LineClassWhere
operator|.
name|BACKGROUND
argument_list|,
name|diffTable
operator|.
name|style
operator|.
name|intraline
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|doScroll (final CodeMirror cm)
specifier|public
name|Runnable
name|doScroll
parameter_list|(
specifier|final
name|CodeMirror
name|cm
parameter_list|)
block|{
specifier|final
name|CodeMirror
name|other
init|=
name|cm
operator|==
name|cmA
condition|?
name|cmB
else|:
name|cmA
decl_stmt|;
return|return
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|cm
operator|.
name|scrollToY
argument_list|(
name|other
operator|.
name|getScrollInfo
argument_list|()
operator|.
name|getTop
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getContentType (DiffInfo.FileMeta meta)
specifier|private
specifier|static
name|String
name|getContentType
parameter_list|(
name|DiffInfo
operator|.
name|FileMeta
name|meta
parameter_list|)
block|{
return|return
name|meta
operator|!=
literal|null
operator|&&
name|meta
operator|.
name|content_type
argument_list|()
operator|!=
literal|null
condition|?
name|ModeInjector
operator|.
name|getContentType
argument_list|(
name|meta
operator|.
name|content_type
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
DECL|class|EditIterator
specifier|static
class|class
name|EditIterator
block|{
DECL|field|lines
specifier|private
specifier|final
name|JsArrayString
name|lines
decl_stmt|;
DECL|field|startLine
specifier|private
specifier|final
name|int
name|startLine
decl_stmt|;
DECL|field|currLineIndex
specifier|private
name|int
name|currLineIndex
decl_stmt|;
DECL|field|currLineOffset
specifier|private
name|int
name|currLineOffset
decl_stmt|;
DECL|method|EditIterator (JsArrayString lineArray, int start)
name|EditIterator
parameter_list|(
name|JsArrayString
name|lineArray
parameter_list|,
name|int
name|start
parameter_list|)
block|{
name|lines
operator|=
name|lineArray
expr_stmt|;
name|startLine
operator|=
name|start
expr_stmt|;
block|}
DECL|method|advance (int numOfChar)
name|LineCharacter
name|advance
parameter_list|(
name|int
name|numOfChar
parameter_list|)
block|{
while|while
condition|(
name|currLineIndex
operator|<
name|lines
operator|.
name|length
argument_list|()
condition|)
block|{
name|int
name|lengthWithNewline
init|=
name|lines
operator|.
name|get
argument_list|(
name|currLineIndex
argument_list|)
operator|.
name|length
argument_list|()
operator|-
name|currLineOffset
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|numOfChar
operator|<
name|lengthWithNewline
condition|)
block|{
name|LineCharacter
name|at
init|=
name|LineCharacter
operator|.
name|create
argument_list|(
name|startLine
operator|+
name|currLineIndex
argument_list|,
name|numOfChar
operator|+
name|currLineOffset
argument_list|)
decl_stmt|;
name|currLineOffset
operator|+=
name|numOfChar
expr_stmt|;
return|return
name|at
return|;
block|}
name|numOfChar
operator|-=
name|lengthWithNewline
expr_stmt|;
name|advanceLine
argument_list|()
expr_stmt|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"EditIterator index out of bound"
argument_list|)
throw|;
block|}
DECL|method|advanceLine ()
specifier|private
name|void
name|advanceLine
parameter_list|()
block|{
name|currLineIndex
operator|++
expr_stmt|;
name|currLineOffset
operator|=
literal|0
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

