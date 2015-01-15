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
DECL|package|net.codemirror.lib
package|package
name|net
operator|.
name|codemirror
operator|.
name|lib
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
name|client
operator|.
name|diff
operator|.
name|DisplaySide
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
name|resources
operator|.
name|client
operator|.
name|CssResource
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
name|rpc
operator|.
name|AsyncCallback
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
operator|.
name|FromTo
import|;
end_import

begin_comment
comment|/**  * Glue to connect CodeMirror to be callable from GWT.  *  * @see<a href="http://codemirror.net/doc/manual.html#api">CodeMirror API</a>  */
end_comment

begin_class
DECL|class|CodeMirror
specifier|public
class|class
name|CodeMirror
extends|extends
name|JavaScriptObject
block|{
DECL|method|preload ()
specifier|public
specifier|static
name|void
name|preload
parameter_list|()
block|{
name|initLibrary
argument_list|(
name|CallbackGroup
operator|.
expr|<
name|Void
operator|>
name|emptyCallback
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|initLibrary (AsyncCallback<Void> cb)
specifier|public
specifier|static
name|void
name|initLibrary
parameter_list|(
name|AsyncCallback
argument_list|<
name|Void
argument_list|>
name|cb
parameter_list|)
block|{
name|Loader
operator|.
name|initLibrary
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|interface|Style
interface|interface
name|Style
extends|extends
name|CssResource
block|{
DECL|method|activeLine ()
name|String
name|activeLine
parameter_list|()
function_decl|;
DECL|method|showTabs ()
name|String
name|showTabs
parameter_list|()
function_decl|;
DECL|method|margin ()
name|String
name|margin
parameter_list|()
function_decl|;
block|}
DECL|method|style ()
specifier|static
name|Style
name|style
parameter_list|()
block|{
return|return
name|Lib
operator|.
name|I
operator|.
name|style
argument_list|()
return|;
block|}
DECL|method|create (Element p, Configuration cfg)
specifier|public
specifier|static
name|CodeMirror
name|create
parameter_list|(
name|Element
name|p
parameter_list|,
name|Configuration
name|cfg
parameter_list|)
block|{
name|CodeMirror
name|cm
init|=
name|newCM
argument_list|(
name|p
argument_list|,
name|cfg
argument_list|)
decl_stmt|;
name|Extras
operator|.
name|attach
argument_list|(
name|cm
argument_list|)
expr_stmt|;
return|return
name|cm
return|;
block|}
DECL|method|newCM (Element p, Configuration cfg)
specifier|private
specifier|static
specifier|native
name|CodeMirror
name|newCM
parameter_list|(
name|Element
name|p
parameter_list|,
name|Configuration
name|cfg
parameter_list|)
comment|/*-{     return $wnd.CodeMirror(p, cfg);   }-*/
function_decl|;
DECL|method|setOption (String option, boolean value)
specifier|public
specifier|final
specifier|native
name|void
name|setOption
parameter_list|(
name|String
name|option
parameter_list|,
name|boolean
name|value
parameter_list|)
comment|/*-{     this.setOption(option, value)   }-*/
function_decl|;
DECL|method|setOption (String option, double value)
specifier|public
specifier|final
specifier|native
name|void
name|setOption
parameter_list|(
name|String
name|option
parameter_list|,
name|double
name|value
parameter_list|)
comment|/*-{     this.setOption(option, value)   }-*/
function_decl|;
DECL|method|setOption (String option, String value)
specifier|public
specifier|final
specifier|native
name|void
name|setOption
parameter_list|(
name|String
name|option
parameter_list|,
name|String
name|value
parameter_list|)
comment|/*-{     this.setOption(option, value)   }-*/
function_decl|;
DECL|method|setOption (String option, JavaScriptObject val)
specifier|public
specifier|final
specifier|native
name|void
name|setOption
parameter_list|(
name|String
name|option
parameter_list|,
name|JavaScriptObject
name|val
parameter_list|)
comment|/*-{     this.setOption(option, val)   }-*/
function_decl|;
DECL|method|getStringOption (String o)
specifier|public
specifier|final
specifier|native
name|String
name|getStringOption
parameter_list|(
name|String
name|o
parameter_list|)
comment|/*-{ return this.getOption(o) }-*/
function_decl|;
DECL|method|getValue ()
specifier|public
specifier|final
specifier|native
name|String
name|getValue
parameter_list|()
comment|/*-{ return this.getValue() }-*/
function_decl|;
DECL|method|setValue (String v)
specifier|public
specifier|final
specifier|native
name|void
name|setValue
parameter_list|(
name|String
name|v
parameter_list|)
comment|/*-{ this.setValue(v) }-*/
function_decl|;
DECL|method|changeGeneration (boolean closeEvent)
specifier|public
specifier|final
specifier|native
name|int
name|changeGeneration
parameter_list|(
name|boolean
name|closeEvent
parameter_list|)
comment|/*-{ return this.changeGeneration(closeEvent) }-*/
function_decl|;
DECL|method|isClean (int generation)
specifier|public
specifier|final
specifier|native
name|boolean
name|isClean
parameter_list|(
name|int
name|generation
parameter_list|)
comment|/*-{ return this.isClean(generation) }-*/
function_decl|;
DECL|method|setWidth (double w)
specifier|public
specifier|final
specifier|native
name|void
name|setWidth
parameter_list|(
name|double
name|w
parameter_list|)
comment|/*-{ this.setSize(w, null) }-*/
function_decl|;
DECL|method|setHeight (double h)
specifier|public
specifier|final
specifier|native
name|void
name|setHeight
parameter_list|(
name|double
name|h
parameter_list|)
comment|/*-{ this.setSize(null, h) }-*/
function_decl|;
DECL|method|getHeight ()
specifier|public
specifier|final
name|int
name|getHeight
parameter_list|()
block|{
return|return
name|getWrapperElement
argument_list|()
operator|.
name|getClientHeight
argument_list|()
return|;
block|}
DECL|method|adjustHeight (int localHeader)
specifier|public
specifier|final
name|void
name|adjustHeight
parameter_list|(
name|int
name|localHeader
parameter_list|)
block|{
name|int
name|rest
init|=
name|Gerrit
operator|.
name|getHeaderFooterHeight
argument_list|()
operator|+
name|localHeader
operator|+
literal|5
decl_stmt|;
comment|// Estimate
name|setHeight
argument_list|(
name|Window
operator|.
name|getClientHeight
argument_list|()
operator|-
name|rest
argument_list|)
expr_stmt|;
block|}
DECL|method|getLine (int n)
specifier|public
specifier|final
specifier|native
name|String
name|getLine
parameter_list|(
name|int
name|n
parameter_list|)
comment|/*-{ return this.getLine(n) }-*/
function_decl|;
DECL|method|barHeight ()
specifier|public
specifier|final
specifier|native
name|double
name|barHeight
parameter_list|()
comment|/*-{ return this.display.barHeight }-*/
function_decl|;
DECL|method|barWidth ()
specifier|public
specifier|final
specifier|native
name|double
name|barWidth
parameter_list|()
comment|/*-{ return this.display.barWidth }-*/
function_decl|;
DECL|method|lastLine ()
specifier|public
specifier|final
specifier|native
name|int
name|lastLine
parameter_list|()
comment|/*-{ return this.lastLine() }-*/
function_decl|;
DECL|method|refresh ()
specifier|public
specifier|final
specifier|native
name|void
name|refresh
parameter_list|()
comment|/*-{ this.refresh() }-*/
function_decl|;
DECL|method|markText (Pos from, Pos to, Configuration options)
specifier|public
specifier|final
specifier|native
name|TextMarker
name|markText
parameter_list|(
name|Pos
name|from
parameter_list|,
name|Pos
name|to
parameter_list|,
name|Configuration
name|options
parameter_list|)
comment|/*-{     return this.markText(from, to, options)   }-*/
function_decl|;
DECL|enum|LineClassWhere
specifier|public
enum|enum
name|LineClassWhere
block|{
DECL|enumConstant|TEXT
name|TEXT
block|{
annotation|@
name|Override
name|String
name|value
parameter_list|()
block|{
return|return
literal|"text"
return|;
block|}
block|}
block|,
DECL|enumConstant|BACKGROUND
name|BACKGROUND
block|{
annotation|@
name|Override
name|String
name|value
parameter_list|()
block|{
return|return
literal|"background"
return|;
block|}
block|}
block|,
DECL|enumConstant|WRAP
name|WRAP
block|{
annotation|@
name|Override
name|String
name|value
parameter_list|()
block|{
return|return
literal|"wrap"
return|;
block|}
block|}
block|;
DECL|method|value ()
specifier|abstract
name|String
name|value
parameter_list|()
function_decl|;
block|}
DECL|method|addLineClass (int line, LineClassWhere where, String className)
specifier|public
specifier|final
name|void
name|addLineClass
parameter_list|(
name|int
name|line
parameter_list|,
name|LineClassWhere
name|where
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|addLineClassNative
argument_list|(
name|line
argument_list|,
name|where
operator|.
name|value
argument_list|()
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
DECL|method|addLineClassNative (int line, String where, String lineClass)
specifier|private
specifier|final
specifier|native
name|void
name|addLineClassNative
parameter_list|(
name|int
name|line
parameter_list|,
name|String
name|where
parameter_list|,
name|String
name|lineClass
parameter_list|)
comment|/*-{     this.addLineClass(line, where, lineClass)   }-*/
function_decl|;
DECL|method|addLineClass (LineHandle line, LineClassWhere where, String className)
specifier|public
specifier|final
name|void
name|addLineClass
parameter_list|(
name|LineHandle
name|line
parameter_list|,
name|LineClassWhere
name|where
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|addLineClassNative
argument_list|(
name|line
argument_list|,
name|where
operator|.
name|value
argument_list|()
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
DECL|method|addLineClassNative (LineHandle line, String where, String lineClass)
specifier|private
specifier|final
specifier|native
name|void
name|addLineClassNative
parameter_list|(
name|LineHandle
name|line
parameter_list|,
name|String
name|where
parameter_list|,
name|String
name|lineClass
parameter_list|)
comment|/*-{     this.addLineClass(line, where, lineClass)   }-*/
function_decl|;
DECL|method|removeLineClass (int line, LineClassWhere where, String className)
specifier|public
specifier|final
name|void
name|removeLineClass
parameter_list|(
name|int
name|line
parameter_list|,
name|LineClassWhere
name|where
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|removeLineClassNative
argument_list|(
name|line
argument_list|,
name|where
operator|.
name|value
argument_list|()
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
DECL|method|removeLineClassNative (int line, String where, String lineClass)
specifier|private
specifier|final
specifier|native
name|void
name|removeLineClassNative
parameter_list|(
name|int
name|line
parameter_list|,
name|String
name|where
parameter_list|,
name|String
name|lineClass
parameter_list|)
comment|/*-{     this.removeLineClass(line, where, lineClass)   }-*/
function_decl|;
DECL|method|removeLineClass (LineHandle line, LineClassWhere where, String className)
specifier|public
specifier|final
name|void
name|removeLineClass
parameter_list|(
name|LineHandle
name|line
parameter_list|,
name|LineClassWhere
name|where
parameter_list|,
name|String
name|className
parameter_list|)
block|{
name|removeLineClassNative
argument_list|(
name|line
argument_list|,
name|where
operator|.
name|value
argument_list|()
argument_list|,
name|className
argument_list|)
expr_stmt|;
block|}
DECL|method|removeLineClassNative (LineHandle line, String where, String lineClass)
specifier|private
specifier|final
specifier|native
name|void
name|removeLineClassNative
parameter_list|(
name|LineHandle
name|line
parameter_list|,
name|String
name|where
parameter_list|,
name|String
name|lineClass
parameter_list|)
comment|/*-{     this.removeLineClass(line, where, lineClass)   }-*/
function_decl|;
DECL|method|addWidget (Pos pos, Element node)
specifier|public
specifier|final
specifier|native
name|void
name|addWidget
parameter_list|(
name|Pos
name|pos
parameter_list|,
name|Element
name|node
parameter_list|)
comment|/*-{     this.addWidget(pos, node, false)   }-*/
function_decl|;
DECL|method|addLineWidget (int line, Element node, Configuration options)
specifier|public
specifier|final
specifier|native
name|LineWidget
name|addLineWidget
parameter_list|(
name|int
name|line
parameter_list|,
name|Element
name|node
parameter_list|,
name|Configuration
name|options
parameter_list|)
comment|/*-{     return this.addLineWidget(line, node, options)   }-*/
function_decl|;
DECL|method|lineAtHeight (double height)
specifier|public
specifier|final
specifier|native
name|int
name|lineAtHeight
parameter_list|(
name|double
name|height
parameter_list|)
comment|/*-{     return this.lineAtHeight(height)   }-*/
function_decl|;
DECL|method|lineAtHeight (double height, String mode)
specifier|public
specifier|final
specifier|native
name|int
name|lineAtHeight
parameter_list|(
name|double
name|height
parameter_list|,
name|String
name|mode
parameter_list|)
comment|/*-{     return this.lineAtHeight(height, mode)   }-*/
function_decl|;
DECL|method|heightAtLine (int line)
specifier|public
specifier|final
specifier|native
name|double
name|heightAtLine
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{     return this.heightAtLine(line)   }-*/
function_decl|;
DECL|method|heightAtLine (int line, String mode)
specifier|public
specifier|final
specifier|native
name|double
name|heightAtLine
parameter_list|(
name|int
name|line
parameter_list|,
name|String
name|mode
parameter_list|)
comment|/*-{     return this.heightAtLine(line, mode)   }-*/
function_decl|;
DECL|method|charCoords (Pos pos, String mode)
specifier|public
specifier|final
specifier|native
name|Rect
name|charCoords
parameter_list|(
name|Pos
name|pos
parameter_list|,
name|String
name|mode
parameter_list|)
comment|/*-{     return this.charCoords(pos, mode)   }-*/
function_decl|;
DECL|method|getDoc ()
specifier|public
specifier|final
specifier|native
name|CodeMirrorDoc
name|getDoc
parameter_list|()
comment|/*-{     return this.getDoc()   }-*/
function_decl|;
DECL|method|scrollTo (double x, double y)
specifier|public
specifier|final
specifier|native
name|void
name|scrollTo
parameter_list|(
name|double
name|x
parameter_list|,
name|double
name|y
parameter_list|)
comment|/*-{     this.scrollTo(x, y)   }-*/
function_decl|;
DECL|method|scrollToY (double y)
specifier|public
specifier|final
specifier|native
name|void
name|scrollToY
parameter_list|(
name|double
name|y
parameter_list|)
comment|/*-{     this.scrollTo(null, y)   }-*/
function_decl|;
DECL|method|scrollToLine (int line)
specifier|public
specifier|final
name|void
name|scrollToLine
parameter_list|(
name|int
name|line
parameter_list|)
block|{
name|int
name|height
init|=
name|getHeight
argument_list|()
decl_stmt|;
if|if
condition|(
name|lineAtHeight
argument_list|(
name|height
operator|-
literal|20
argument_list|)
operator|<
name|line
condition|)
block|{
name|scrollToY
argument_list|(
name|heightAtLine
argument_list|(
name|line
argument_list|,
literal|"local"
argument_list|)
operator|-
literal|0.5
operator|*
name|height
argument_list|)
expr_stmt|;
block|}
name|setCursor
argument_list|(
name|Pos
operator|.
name|create
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getScrollInfo ()
specifier|public
specifier|final
specifier|native
name|ScrollInfo
name|getScrollInfo
parameter_list|()
comment|/*-{     return this.getScrollInfo()   }-*/
function_decl|;
DECL|method|getViewport ()
specifier|public
specifier|final
specifier|native
name|Viewport
name|getViewport
parameter_list|()
comment|/*-{     return this.getViewport()   }-*/
function_decl|;
DECL|method|operation (Runnable thunk)
specifier|public
specifier|final
specifier|native
name|void
name|operation
parameter_list|(
name|Runnable
name|thunk
parameter_list|)
comment|/*-{     this.operation(function() {       thunk.@java.lang.Runnable::run()();     })   }-*/
function_decl|;
DECL|method|off (String event, RegisteredHandler h)
specifier|public
specifier|final
specifier|native
name|void
name|off
parameter_list|(
name|String
name|event
parameter_list|,
name|RegisteredHandler
name|h
parameter_list|)
comment|/*-{     this.off(event, h)   }-*/
function_decl|;
DECL|method|on (String event, Runnable thunk)
specifier|public
specifier|final
specifier|native
name|RegisteredHandler
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|Runnable
name|thunk
parameter_list|)
comment|/*-{     var h = $entry(function() { thunk.@java.lang.Runnable::run()() });     this.on(event, h);     return h;   }-*/
function_decl|;
DECL|method|on (String event, EventHandler handler)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|EventHandler
name|handler
parameter_list|)
comment|/*-{     this.on(event, $entry(function(cm, e) {       handler.@net.codemirror.lib.CodeMirror.EventHandler::handle(         Lnet/codemirror/lib/CodeMirror;         Lcom/google/gwt/dom/client/NativeEvent;)(cm, e);     }))   }-*/
function_decl|;
DECL|method|on (String event, RenderLineHandler handler)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|RenderLineHandler
name|handler
parameter_list|)
comment|/*-{     this.on(event, $entry(function(cm, h, e) {       handler.@net.codemirror.lib.CodeMirror.RenderLineHandler::handle(         Lnet/codemirror/lib/CodeMirror;         Lnet/codemirror/lib/CodeMirror$LineHandle;         Lcom/google/gwt/dom/client/Element;)(cm, h, e);     }))   }-*/
function_decl|;
DECL|method|on (String event, GutterClickHandler handler)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|GutterClickHandler
name|handler
parameter_list|)
comment|/*-{     this.on(event, $entry(function(cm, l, g, e) {       handler.@net.codemirror.lib.CodeMirror.GutterClickHandler::handle(         Lnet/codemirror/lib/CodeMirror;         I         Ljava/lang/String;         Lcom/google/gwt/dom/client/NativeEvent;)(cm, l, g, e);     }))   }-*/
function_decl|;
DECL|method|on (String event, BeforeSelectionChangeHandler handler)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|String
name|event
parameter_list|,
name|BeforeSelectionChangeHandler
name|handler
parameter_list|)
comment|/*-{     this.on(event, $entry(function(cm, o) {       var e = o.ranges[o.ranges.length-1];       handler.@net.codemirror.lib.CodeMirror.BeforeSelectionChangeHandler::handle(         Lnet/codemirror/lib/CodeMirror;         Lnet/codemirror/lib/Pos;         Lnet/codemirror/lib/Pos;)(cm, e.anchor, e.head);     }))   }-*/
function_decl|;
DECL|method|on (ChangesHandler handler)
specifier|public
specifier|final
specifier|native
name|void
name|on
parameter_list|(
name|ChangesHandler
name|handler
parameter_list|)
comment|/*-{     this.on('changes', $entry(function(cm, o) {       handler.@net.codemirror.lib.CodeMirror.ChangesHandler::handle(         Lnet/codemirror/lib/CodeMirror;)(cm);     }))   }-*/
function_decl|;
DECL|method|setCursor (Pos p)
specifier|public
specifier|final
specifier|native
name|void
name|setCursor
parameter_list|(
name|Pos
name|p
parameter_list|)
comment|/*-{ this.setCursor(p) }-*/
function_decl|;
DECL|method|getCursor ()
specifier|public
specifier|final
specifier|native
name|Pos
name|getCursor
parameter_list|()
comment|/*-{ return this.getCursor() }-*/
function_decl|;
DECL|method|getCursor (String start)
specifier|public
specifier|final
specifier|native
name|Pos
name|getCursor
parameter_list|(
name|String
name|start
parameter_list|)
comment|/*-{     return this.getCursor(start)   }-*/
function_decl|;
DECL|method|getSelectedRange ()
specifier|public
specifier|final
name|FromTo
name|getSelectedRange
parameter_list|()
block|{
return|return
name|FromTo
operator|.
name|create
argument_list|(
name|getCursor
argument_list|(
literal|"start"
argument_list|)
argument_list|,
name|getCursor
argument_list|(
literal|"end"
argument_list|)
argument_list|)
return|;
block|}
DECL|method|setSelection (Pos p)
specifier|public
specifier|final
specifier|native
name|void
name|setSelection
parameter_list|(
name|Pos
name|p
parameter_list|)
comment|/*-{ this.setSelection(p) }-*/
function_decl|;
DECL|method|setSelection (Pos anchor, Pos head)
specifier|public
specifier|final
specifier|native
name|void
name|setSelection
parameter_list|(
name|Pos
name|anchor
parameter_list|,
name|Pos
name|head
parameter_list|)
comment|/*-{     this.setSelection(anchor, head)   }-*/
function_decl|;
DECL|method|somethingSelected ()
specifier|public
specifier|final
specifier|native
name|boolean
name|somethingSelected
parameter_list|()
comment|/*-{     return this.somethingSelected()   }-*/
function_decl|;
DECL|method|addKeyMap (KeyMap map)
specifier|public
specifier|final
specifier|native
name|void
name|addKeyMap
parameter_list|(
name|KeyMap
name|map
parameter_list|)
comment|/*-{ this.addKeyMap(map) }-*/
function_decl|;
DECL|method|removeKeyMap (KeyMap map)
specifier|public
specifier|final
specifier|native
name|void
name|removeKeyMap
parameter_list|(
name|KeyMap
name|map
parameter_list|)
comment|/*-{ this.removeKeyMap(map) }-*/
function_decl|;
DECL|method|getLineHandle (int line)
specifier|public
specifier|final
specifier|native
name|LineHandle
name|getLineHandle
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{     return this.getLineHandle(line)   }-*/
function_decl|;
DECL|method|getLineHandleVisualStart (int line)
specifier|public
specifier|final
specifier|native
name|LineHandle
name|getLineHandleVisualStart
parameter_list|(
name|int
name|line
parameter_list|)
comment|/*-{     return this.getLineHandleVisualStart(line)   }-*/
function_decl|;
DECL|method|getLineNumber (LineHandle handle)
specifier|public
specifier|final
specifier|native
name|int
name|getLineNumber
parameter_list|(
name|LineHandle
name|handle
parameter_list|)
comment|/*-{     return this.getLineNumber(handle)   }-*/
function_decl|;
DECL|method|focus ()
specifier|public
specifier|final
specifier|native
name|void
name|focus
parameter_list|()
comment|/*-{     this.focus()   }-*/
function_decl|;
DECL|method|getWrapperElement ()
specifier|public
specifier|final
specifier|native
name|Element
name|getWrapperElement
parameter_list|()
comment|/*-{     return this.getWrapperElement()   }-*/
function_decl|;
DECL|method|getGutterElement ()
specifier|public
specifier|final
specifier|native
name|Element
name|getGutterElement
parameter_list|()
comment|/*-{     return this.getGutterElement()   }-*/
function_decl|;
DECL|method|sizer ()
specifier|public
specifier|final
specifier|native
name|Element
name|sizer
parameter_list|()
comment|/*-{     return this.display.sizer   }-*/
function_decl|;
DECL|method|mover ()
specifier|public
specifier|final
specifier|native
name|Element
name|mover
parameter_list|()
comment|/*-{     return this.display.mover   }-*/
function_decl|;
DECL|method|measure ()
specifier|public
specifier|final
specifier|native
name|Element
name|measure
parameter_list|()
comment|/*-{     return this.display.measure   }-*/
function_decl|;
DECL|method|scrollbarV ()
specifier|public
specifier|final
specifier|native
name|Element
name|scrollbarV
parameter_list|()
comment|/*-{     return this.display.scrollbars.vert.node;   }-*/
function_decl|;
DECL|method|execCommand (String cmd)
specifier|public
specifier|final
specifier|native
name|void
name|execCommand
parameter_list|(
name|String
name|cmd
parameter_list|)
comment|/*-{     this.execCommand(cmd)   }-*/
function_decl|;
DECL|method|getKeyMap (String name)
specifier|public
specifier|static
specifier|final
specifier|native
name|KeyMap
name|getKeyMap
parameter_list|(
name|String
name|name
parameter_list|)
comment|/*-{     return $wnd.CodeMirror.keyMap[name];   }-*/
function_decl|;
DECL|method|addKeyMap (String name, KeyMap km)
specifier|public
specifier|static
specifier|final
specifier|native
name|void
name|addKeyMap
parameter_list|(
name|String
name|name
parameter_list|,
name|KeyMap
name|km
parameter_list|)
comment|/*-{     $wnd.CodeMirror.keyMap[name] = km   }-*/
function_decl|;
DECL|method|vim ()
specifier|public
specifier|final
specifier|native
name|Vim
name|vim
parameter_list|()
comment|/*-{     return this;   }-*/
function_decl|;
DECL|method|side ()
specifier|public
specifier|final
name|DisplaySide
name|side
parameter_list|()
block|{
return|return
name|extras
argument_list|()
operator|.
name|side
argument_list|()
return|;
block|}
DECL|method|extras ()
specifier|public
specifier|final
name|Extras
name|extras
parameter_list|()
block|{
return|return
name|Extras
operator|.
name|get
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|CodeMirror ()
specifier|protected
name|CodeMirror
parameter_list|()
block|{   }
DECL|class|Viewport
specifier|public
specifier|static
class|class
name|Viewport
extends|extends
name|JavaScriptObject
block|{
DECL|method|from ()
specifier|public
specifier|final
specifier|native
name|int
name|from
parameter_list|()
comment|/*-{ return this.from }-*/
function_decl|;
DECL|method|to ()
specifier|public
specifier|final
specifier|native
name|int
name|to
parameter_list|()
comment|/*-{ return this.to }-*/
function_decl|;
DECL|method|contains (int line)
specifier|public
specifier|final
name|boolean
name|contains
parameter_list|(
name|int
name|line
parameter_list|)
block|{
return|return
name|from
argument_list|()
operator|<=
name|line
operator|&&
name|line
operator|<
name|to
argument_list|()
return|;
block|}
DECL|method|Viewport ()
specifier|protected
name|Viewport
parameter_list|()
block|{     }
block|}
DECL|class|LineHandle
specifier|public
specifier|static
class|class
name|LineHandle
extends|extends
name|JavaScriptObject
block|{
DECL|method|LineHandle ()
specifier|protected
name|LineHandle
parameter_list|()
block|{     }
block|}
DECL|class|RegisteredHandler
specifier|public
specifier|static
class|class
name|RegisteredHandler
extends|extends
name|JavaScriptObject
block|{
DECL|method|RegisteredHandler ()
specifier|protected
name|RegisteredHandler
parameter_list|()
block|{     }
block|}
DECL|interface|EventHandler
specifier|public
interface|interface
name|EventHandler
block|{
DECL|method|handle (CodeMirror instance, NativeEvent event)
specifier|public
name|void
name|handle
parameter_list|(
name|CodeMirror
name|instance
parameter_list|,
name|NativeEvent
name|event
parameter_list|)
function_decl|;
block|}
DECL|interface|RenderLineHandler
specifier|public
interface|interface
name|RenderLineHandler
block|{
DECL|method|handle (CodeMirror instance, LineHandle handle, Element element)
specifier|public
name|void
name|handle
parameter_list|(
name|CodeMirror
name|instance
parameter_list|,
name|LineHandle
name|handle
parameter_list|,
name|Element
name|element
parameter_list|)
function_decl|;
block|}
DECL|interface|GutterClickHandler
specifier|public
interface|interface
name|GutterClickHandler
block|{
DECL|method|handle (CodeMirror instance, int line, String gutter, NativeEvent clickEvent)
specifier|public
name|void
name|handle
parameter_list|(
name|CodeMirror
name|instance
parameter_list|,
name|int
name|line
parameter_list|,
name|String
name|gutter
parameter_list|,
name|NativeEvent
name|clickEvent
parameter_list|)
function_decl|;
block|}
DECL|interface|BeforeSelectionChangeHandler
specifier|public
interface|interface
name|BeforeSelectionChangeHandler
block|{
DECL|method|handle (CodeMirror instance, Pos anchor, Pos head)
specifier|public
name|void
name|handle
parameter_list|(
name|CodeMirror
name|instance
parameter_list|,
name|Pos
name|anchor
parameter_list|,
name|Pos
name|head
parameter_list|)
function_decl|;
block|}
DECL|interface|ChangesHandler
specifier|public
interface|interface
name|ChangesHandler
block|{
DECL|method|handle (CodeMirror instance)
specifier|public
name|void
name|handle
parameter_list|(
name|CodeMirror
name|instance
parameter_list|)
function_decl|;
block|}
block|}
end_class

end_unit

