begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//Copyright (C) 2013 The Android Open Source Project
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
name|patches
operator|.
name|PatchUtil
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
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|dom
operator|.
name|client
operator|.
name|ClickHandler
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
name|uibinder
operator|.
name|client
operator|.
name|UiBinder
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
name|uibinder
operator|.
name|client
operator|.
name|UiField
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
name|uibinder
operator|.
name|client
operator|.
name|UiHandler
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
name|Anchor
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
name|Composite
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
name|HTMLPanel
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
name|TextMarker
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
comment|/** The Widget that handles expanding of skipped lines */
end_comment

begin_class
DECL|class|SkipBar
class|class
name|SkipBar
extends|extends
name|Composite
block|{
DECL|interface|Binder
interface|interface
name|Binder
extends|extends
name|UiBinder
argument_list|<
name|HTMLPanel
argument_list|,
name|SkipBar
argument_list|>
block|{}
DECL|field|uiBinder
specifier|private
specifier|static
specifier|final
name|Binder
name|uiBinder
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Binder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NUM_ROWS_TO_EXPAND
specifier|private
specifier|static
specifier|final
name|int
name|NUM_ROWS_TO_EXPAND
init|=
literal|10
decl_stmt|;
DECL|field|UP_DOWN_THRESHOLD
specifier|private
specifier|static
specifier|final
name|int
name|UP_DOWN_THRESHOLD
init|=
literal|30
decl_stmt|;
DECL|interface|SkipBarStyle
interface|interface
name|SkipBarStyle
extends|extends
name|CssResource
block|{
DECL|method|noExpand ()
name|String
name|noExpand
parameter_list|()
function_decl|;
block|}
DECL|field|skipNum
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
name|Anchor
name|skipNum
decl_stmt|;
DECL|field|upArrow
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
name|Anchor
name|upArrow
decl_stmt|;
DECL|field|downArrow
annotation|@
name|UiField
argument_list|(
name|provided
operator|=
literal|true
argument_list|)
name|Anchor
name|downArrow
decl_stmt|;
DECL|field|style
annotation|@
name|UiField
name|SkipBarStyle
name|style
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|SkipManager
name|manager
decl_stmt|;
DECL|field|cm
specifier|private
specifier|final
name|CodeMirror
name|cm
decl_stmt|;
DECL|field|lineWidget
specifier|private
name|LineWidget
name|lineWidget
decl_stmt|;
DECL|field|textMarker
specifier|private
name|TextMarker
name|textMarker
decl_stmt|;
DECL|field|otherBar
specifier|private
name|SkipBar
name|otherBar
decl_stmt|;
DECL|method|SkipBar (SkipManager manager, final CodeMirror cm)
name|SkipBar
parameter_list|(
name|SkipManager
name|manager
parameter_list|,
specifier|final
name|CodeMirror
name|cm
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|cm
operator|=
name|cm
expr_stmt|;
name|skipNum
operator|=
operator|new
name|Anchor
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|upArrow
operator|=
operator|new
name|Anchor
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|downArrow
operator|=
operator|new
name|Anchor
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|uiBinder
operator|.
name|createAndBindUi
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|addDomHandler
argument_list|(
operator|new
name|ClickHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClick
parameter_list|(
name|ClickEvent
name|event
parameter_list|)
block|{
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|,
name|ClickEvent
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|collapse (int start, int end, boolean attach)
name|void
name|collapse
parameter_list|(
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|,
name|boolean
name|attach
parameter_list|)
block|{
if|if
condition|(
name|attach
condition|)
block|{
name|boolean
name|isNew
init|=
name|lineWidget
operator|==
literal|null
decl_stmt|;
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
decl_stmt|;
if|if
condition|(
name|start
operator|==
literal|0
condition|)
block|{
comment|// First line workaround
name|lineWidget
operator|=
name|cm
operator|.
name|addLineWidget
argument_list|(
name|end
operator|+
literal|1
argument_list|,
name|getElement
argument_list|()
argument_list|,
name|cfg
operator|.
name|set
argument_list|(
literal|"above"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|lineWidget
operator|=
name|cm
operator|.
name|addLineWidget
argument_list|(
name|start
operator|-
literal|1
argument_list|,
name|getElement
argument_list|()
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isNew
condition|)
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|lineWidget
operator|.
name|onFirstRedraw
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
name|int
name|w
init|=
name|cm
operator|.
name|getGutterElement
argument_list|()
operator|.
name|getOffsetWidth
argument_list|()
decl_stmt|;
name|getElement
argument_list|()
operator|.
name|getStyle
argument_list|()
operator|.
name|setPaddingLeft
argument_list|(
name|w
argument_list|,
name|Unit
operator|.
name|PX
argument_list|)
expr_stmt|;
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|textMarker
operator|=
name|cm
operator|.
name|markText
argument_list|(
name|CodeMirror
operator|.
name|pos
argument_list|(
name|start
argument_list|,
literal|0
argument_list|)
argument_list|,
name|CodeMirror
operator|.
name|pos
argument_list|(
name|end
argument_list|)
argument_list|,
name|Configuration
operator|.
name|create
argument_list|()
operator|.
name|set
argument_list|(
literal|"collapsed"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"inclusiveLeft"
argument_list|,
literal|true
argument_list|)
operator|.
name|set
argument_list|(
literal|"inclusiveRight"
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|skipped
init|=
name|end
operator|-
name|start
operator|+
literal|1
decl_stmt|;
if|if
condition|(
name|skipped
operator|<=
name|UP_DOWN_THRESHOLD
condition|)
block|{
name|addStyleName
argument_list|(
name|style
operator|.
name|noExpand
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|upArrow
operator|.
name|setHTML
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|expandBefore
argument_list|(
name|NUM_ROWS_TO_EXPAND
argument_list|)
argument_list|)
expr_stmt|;
name|downArrow
operator|.
name|setHTML
argument_list|(
name|PatchUtil
operator|.
name|M
operator|.
name|expandAfter
argument_list|(
name|NUM_ROWS_TO_EXPAND
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|skipNum
operator|.
name|setText
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|skipped
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|link (SkipBar barA, SkipBar barB)
specifier|static
name|void
name|link
parameter_list|(
name|SkipBar
name|barA
parameter_list|,
name|SkipBar
name|barB
parameter_list|)
block|{
name|barA
operator|.
name|otherBar
operator|=
name|barB
expr_stmt|;
name|barB
operator|.
name|otherBar
operator|=
name|barA
expr_stmt|;
block|}
DECL|method|clearMarkerAndWidget ()
specifier|private
name|void
name|clearMarkerAndWidget
parameter_list|()
block|{
name|textMarker
operator|.
name|clear
argument_list|()
expr_stmt|;
name|lineWidget
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|expandAll ()
name|void
name|expandAll
parameter_list|()
block|{
name|clearMarkerAndWidget
argument_list|()
expr_stmt|;
name|removeFromParent
argument_list|()
expr_stmt|;
name|updateSelection
argument_list|()
expr_stmt|;
block|}
DECL|method|expandBefore ()
specifier|private
name|void
name|expandBefore
parameter_list|()
block|{
name|FromTo
name|range
init|=
name|textMarker
operator|.
name|find
argument_list|()
decl_stmt|;
name|int
name|oldStart
init|=
name|range
operator|.
name|getFrom
argument_list|()
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|int
name|newStart
init|=
name|oldStart
operator|+
name|NUM_ROWS_TO_EXPAND
decl_stmt|;
name|int
name|end
init|=
name|range
operator|.
name|getTo
argument_list|()
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|clearMarkerAndWidget
argument_list|()
expr_stmt|;
name|collapse
argument_list|(
name|newStart
argument_list|,
name|end
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|updateSelection
argument_list|()
expr_stmt|;
block|}
DECL|method|expandAfter ()
specifier|private
name|void
name|expandAfter
parameter_list|()
block|{
name|FromTo
name|range
init|=
name|textMarker
operator|.
name|find
argument_list|()
decl_stmt|;
name|int
name|start
init|=
name|range
operator|.
name|getFrom
argument_list|()
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|int
name|oldEnd
init|=
name|range
operator|.
name|getTo
argument_list|()
operator|.
name|getLine
argument_list|()
decl_stmt|;
name|int
name|newEnd
init|=
name|oldEnd
operator|-
name|NUM_ROWS_TO_EXPAND
decl_stmt|;
name|textMarker
operator|.
name|clear
argument_list|()
expr_stmt|;
name|collapse
argument_list|(
name|start
argument_list|,
name|newEnd
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|updateSelection
argument_list|()
expr_stmt|;
block|}
DECL|method|updateSelection ()
specifier|private
name|void
name|updateSelection
parameter_list|()
block|{
if|if
condition|(
name|cm
operator|.
name|somethingSelected
argument_list|()
condition|)
block|{
name|FromTo
name|sel
init|=
name|cm
operator|.
name|getSelectedRange
argument_list|()
decl_stmt|;
name|cm
operator|.
name|setSelection
argument_list|(
name|sel
operator|.
name|getFrom
argument_list|()
argument_list|,
name|sel
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"skipNum"
argument_list|)
DECL|method|onExpandAll (ClickEvent e)
name|void
name|onExpandAll
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|manager
operator|.
name|remove
argument_list|(
name|this
argument_list|,
name|otherBar
argument_list|)
expr_stmt|;
name|otherBar
operator|.
name|expandAll
argument_list|()
expr_stmt|;
name|expandAll
argument_list|()
expr_stmt|;
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"upArrow"
argument_list|)
DECL|method|onExpandBefore (ClickEvent e)
name|void
name|onExpandBefore
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|otherBar
operator|.
name|expandBefore
argument_list|()
expr_stmt|;
name|expandBefore
argument_list|()
expr_stmt|;
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|UiHandler
argument_list|(
literal|"downArrow"
argument_list|)
DECL|method|onExpandAfter (ClickEvent e)
name|void
name|onExpandAfter
parameter_list|(
name|ClickEvent
name|e
parameter_list|)
block|{
name|otherBar
operator|.
name|expandAfter
argument_list|()
expr_stmt|;
name|expandAfter
argument_list|()
expr_stmt|;
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

