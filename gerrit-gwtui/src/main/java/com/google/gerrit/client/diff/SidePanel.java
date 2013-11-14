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
name|core
operator|.
name|client
operator|.
name|Scheduler
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
name|Scheduler
operator|.
name|ScheduledCommand
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
name|Label
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
name|LineCharacter
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

begin_comment
comment|/** The Widget that handles the scrollbar gutters */
end_comment

begin_class
DECL|class|SidePanel
class|class
name|SidePanel
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
name|SidePanel
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
DECL|interface|SidePanelStyle
interface|interface
name|SidePanelStyle
extends|extends
name|CssResource
block|{
DECL|method|gutter ()
name|String
name|gutter
parameter_list|()
function_decl|;
DECL|method|halfGutter ()
name|String
name|halfGutter
parameter_list|()
function_decl|;
DECL|method|comment ()
name|String
name|comment
parameter_list|()
function_decl|;
DECL|method|draft ()
name|String
name|draft
parameter_list|()
function_decl|;
DECL|method|insert ()
name|String
name|insert
parameter_list|()
function_decl|;
DECL|method|delete ()
name|String
name|delete
parameter_list|()
function_decl|;
block|}
DECL|enum|GutterType
enum|enum
name|GutterType
block|{
DECL|enumConstant|COMMENT
DECL|enumConstant|DRAFT
DECL|enumConstant|INSERT
DECL|enumConstant|DELETE
DECL|enumConstant|EDIT
name|COMMENT
block|,
name|DRAFT
block|,
name|INSERT
block|,
name|DELETE
block|,
name|EDIT
block|}
annotation|@
name|UiField
DECL|field|style
name|SidePanelStyle
name|style
decl_stmt|;
DECL|field|gutters
specifier|private
name|List
argument_list|<
name|GutterWrapper
argument_list|>
name|gutters
decl_stmt|;
DECL|field|cmB
specifier|private
name|CodeMirror
name|cmB
decl_stmt|;
DECL|method|SidePanel ()
name|SidePanel
parameter_list|()
block|{
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
name|this
operator|.
name|gutters
operator|=
operator|new
name|ArrayList
argument_list|<
name|GutterWrapper
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|addGutter (CodeMirror cm, int line, GutterType type)
name|GutterWrapper
name|addGutter
parameter_list|(
name|CodeMirror
name|cm
parameter_list|,
name|int
name|line
parameter_list|,
name|GutterType
name|type
parameter_list|)
block|{
name|Label
name|gutter
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|GutterWrapper
name|info
init|=
operator|new
name|GutterWrapper
argument_list|(
name|this
argument_list|,
name|gutter
argument_list|,
name|cm
argument_list|,
name|line
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|adjustGutter
argument_list|(
name|info
argument_list|)
expr_stmt|;
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|gutter
argument_list|()
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|COMMENT
case|:
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|comment
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DRAFT
case|:
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|draft
argument_list|()
argument_list|)
expr_stmt|;
name|gutter
operator|.
name|setText
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
break|break;
case|case
name|INSERT
case|:
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|insert
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETE
case|:
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|delete
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|EDIT
case|:
name|gutter
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|insert
argument_list|()
argument_list|)
expr_stmt|;
name|Label
name|labelLeft
init|=
operator|new
name|Label
argument_list|()
decl_stmt|;
name|labelLeft
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|halfGutter
argument_list|()
argument_list|)
expr_stmt|;
name|gutter
operator|.
name|getElement
argument_list|()
operator|.
name|appendChild
argument_list|(
name|labelLeft
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|HTMLPanel
operator|)
name|getWidget
argument_list|()
operator|)
operator|.
name|add
argument_list|(
name|gutter
argument_list|)
expr_stmt|;
name|gutters
operator|.
name|add
argument_list|(
name|info
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|adjustGutters (CodeMirror cmB)
name|void
name|adjustGutters
parameter_list|(
name|CodeMirror
name|cmB
parameter_list|)
block|{
name|this
operator|.
name|cmB
operator|=
name|cmB
expr_stmt|;
name|Scheduler
operator|.
name|get
argument_list|()
operator|.
name|scheduleDeferred
argument_list|(
operator|new
name|ScheduledCommand
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|()
block|{
for|for
control|(
name|GutterWrapper
name|info
range|:
name|gutters
control|)
block|{
name|adjustGutter
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|adjustGutter (GutterWrapper wrapper)
specifier|private
name|void
name|adjustGutter
parameter_list|(
name|GutterWrapper
name|wrapper
parameter_list|)
block|{
if|if
condition|(
name|cmB
operator|==
literal|null
condition|)
block|{
return|return;
block|}
specifier|final
name|CodeMirror
name|cm
init|=
name|wrapper
operator|.
name|cm
decl_stmt|;
specifier|final
name|int
name|line
init|=
name|wrapper
operator|.
name|line
decl_stmt|;
name|Label
name|gutter
init|=
name|wrapper
operator|.
name|gutter
decl_stmt|;
specifier|final
name|double
name|height
init|=
name|cm
operator|.
name|heightAtLine
argument_list|(
name|line
argument_list|,
literal|"local"
argument_list|)
decl_stmt|;
specifier|final
name|double
name|scrollbarHeight
init|=
name|cmB
operator|.
name|getScrollbarV
argument_list|()
operator|.
name|getClientHeight
argument_list|()
decl_stmt|;
name|double
name|top
init|=
name|height
operator|/
name|cmB
operator|.
name|getSizer
argument_list|()
operator|.
name|getClientHeight
argument_list|()
operator|*
name|scrollbarHeight
operator|+
name|cmB
operator|.
name|getScrollbarV
argument_list|()
operator|.
name|getAbsoluteTop
argument_list|()
decl_stmt|;
if|if
condition|(
name|top
operator|==
literal|0
condition|)
block|{
name|top
operator|=
operator|-
literal|10
expr_stmt|;
block|}
name|gutter
operator|.
name|getElement
argument_list|()
operator|.
name|getStyle
argument_list|()
operator|.
name|setTop
argument_list|(
name|top
argument_list|,
name|Unit
operator|.
name|PX
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|replaceClickHandler
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
name|setCursor
argument_list|(
name|LineCharacter
operator|.
name|create
argument_list|(
name|line
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|scrollToY
argument_list|(
name|Math
operator|.
name|max
argument_list|(
literal|0
argument_list|,
name|height
operator|-
literal|0.5
operator|*
name|scrollbarHeight
argument_list|)
argument_list|)
expr_stmt|;
name|cm
operator|.
name|focus
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|removeGutter (GutterWrapper wrapper)
name|void
name|removeGutter
parameter_list|(
name|GutterWrapper
name|wrapper
parameter_list|)
block|{
name|gutters
operator|.
name|remove
argument_list|(
name|wrapper
argument_list|)
expr_stmt|;
block|}
DECL|class|GutterWrapper
specifier|static
class|class
name|GutterWrapper
block|{
DECL|field|host
specifier|private
name|SidePanel
name|host
decl_stmt|;
DECL|field|gutter
specifier|private
name|Label
name|gutter
decl_stmt|;
DECL|field|cm
specifier|private
name|CodeMirror
name|cm
decl_stmt|;
DECL|field|line
specifier|private
name|int
name|line
decl_stmt|;
DECL|field|regClick
specifier|private
name|HandlerRegistration
name|regClick
decl_stmt|;
DECL|method|GutterWrapper (SidePanel host, Label anchor, CodeMirror cm, int line, GutterType type)
name|GutterWrapper
parameter_list|(
name|SidePanel
name|host
parameter_list|,
name|Label
name|anchor
parameter_list|,
name|CodeMirror
name|cm
parameter_list|,
name|int
name|line
parameter_list|,
name|GutterType
name|type
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
name|gutter
operator|=
name|anchor
expr_stmt|;
name|this
operator|.
name|cm
operator|=
name|cm
expr_stmt|;
name|this
operator|.
name|line
operator|=
name|line
expr_stmt|;
block|}
DECL|method|replaceClickHandler (ClickHandler newHandler)
specifier|private
name|void
name|replaceClickHandler
parameter_list|(
name|ClickHandler
name|newHandler
parameter_list|)
block|{
if|if
condition|(
name|regClick
operator|!=
literal|null
condition|)
block|{
name|regClick
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
block|}
name|regClick
operator|=
name|gutter
operator|.
name|addClickHandler
argument_list|(
name|newHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|remove ()
name|void
name|remove
parameter_list|()
block|{
name|gutter
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|host
operator|.
name|removeGutter
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

