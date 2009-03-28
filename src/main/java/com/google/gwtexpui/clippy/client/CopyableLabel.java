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
DECL|package|com.google.gwtexpui.clippy.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|clippy
operator|.
name|client
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
name|http
operator|.
name|client
operator|.
name|URL
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
name|Command
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
name|DeferredCommand
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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|ClickListener
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
name|FlowPanel
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
name|FocusListener
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
name|HasText
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
name|InlineLabel
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
name|KeyboardListenerAdapter
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
name|TextBox
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
name|Widget
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
name|SafeHtml
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
name|gwtexpui
operator|.
name|user
operator|.
name|client
operator|.
name|UserAgent
import|;
end_import

begin_comment
comment|/**  * Label which permits the user to easily copy the complete content.  *<p>  * If the Flash plugin is available a "movie" is embedded that provides  * one-click copying of the content onto the system clipboard. The label (if  * visible) can also be clicked, switching from a label to an input box,  * allowing the user to copy the text with a keyboard shortcut.  *<p>  * Style name:<code>gwtexpui-Clippy</code>  */
end_comment

begin_class
DECL|class|CopyableLabel
specifier|public
class|class
name|CopyableLabel
extends|extends
name|Composite
implements|implements
name|HasText
block|{
DECL|field|SWF_WIDTH
specifier|private
specifier|static
specifier|final
name|int
name|SWF_WIDTH
init|=
literal|110
decl_stmt|;
DECL|field|SWF_HEIGHT
specifier|private
specifier|static
specifier|final
name|int
name|SWF_HEIGHT
init|=
literal|14
decl_stmt|;
DECL|field|swfUrl
specifier|private
specifier|static
name|String
name|swfUrl
decl_stmt|;
DECL|method|swfUrl ()
specifier|private
specifier|static
name|String
name|swfUrl
parameter_list|()
block|{
if|if
condition|(
name|swfUrl
operator|==
literal|null
condition|)
block|{
name|swfUrl
operator|=
name|GWT
operator|.
name|getModuleBaseURL
argument_list|()
operator|+
literal|"gwtexpui_clippy1.cache.swf"
expr_stmt|;
block|}
return|return
name|swfUrl
return|;
block|}
DECL|field|content
specifier|private
specifier|final
name|FlowPanel
name|content
decl_stmt|;
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|textLabel
specifier|private
name|Label
name|textLabel
decl_stmt|;
DECL|field|textBox
specifier|private
name|TextBox
name|textBox
decl_stmt|;
DECL|field|swf
specifier|private
name|Element
name|swf
decl_stmt|;
comment|/**    * Create a new label    *     * @param str initial content    */
DECL|method|CopyableLabel (final String str)
specifier|public
name|CopyableLabel
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
name|this
argument_list|(
name|str
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a new label    *     * @param str initial content    * @param showLabel if true, the content is shown, if false it is hidden from    *        view and only the copy icon is displayed.    */
DECL|method|CopyableLabel (final String str, final boolean showLabel)
specifier|public
name|CopyableLabel
parameter_list|(
specifier|final
name|String
name|str
parameter_list|,
specifier|final
name|boolean
name|showLabel
parameter_list|)
block|{
name|content
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|content
operator|.
name|setStyleName
argument_list|(
literal|"gwtexpui-Clippy"
argument_list|)
expr_stmt|;
name|initWidget
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|text
operator|=
name|str
expr_stmt|;
if|if
condition|(
name|showLabel
condition|)
block|{
name|textLabel
operator|=
operator|new
name|InlineLabel
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|textLabel
operator|.
name|setStyleName
argument_list|(
literal|"gwtexpui-Clippy-Label"
argument_list|)
expr_stmt|;
name|textLabel
operator|.
name|addClickListener
argument_list|(
operator|new
name|ClickListener
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
specifier|final
name|Widget
name|sender
parameter_list|)
block|{
name|showTextBox
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|content
operator|.
name|add
argument_list|(
name|textLabel
argument_list|)
expr_stmt|;
block|}
name|embedMovie
argument_list|()
expr_stmt|;
block|}
DECL|method|embedMovie ()
specifier|private
name|void
name|embedMovie
parameter_list|()
block|{
if|if
condition|(
name|UserAgent
operator|.
name|hasFlash
condition|)
block|{
specifier|final
name|String
name|flashVars
init|=
literal|"text="
operator|+
name|URL
operator|.
name|encodeComponent
argument_list|(
name|getText
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtmlBuilder
name|h
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
decl_stmt|;
name|h
operator|.
name|openElement
argument_list|(
literal|"span"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setStyleName
argument_list|(
literal|"gwtexpui-Clippy-Control"
argument_list|)
expr_stmt|;
name|h
operator|.
name|openElement
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setWidth
argument_list|(
name|SWF_WIDTH
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHeight
argument_list|(
name|SWF_HEIGHT
argument_list|)
expr_stmt|;
name|h
operator|.
name|setAttribute
argument_list|(
literal|"classid"
argument_list|,
literal|"clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"
argument_list|)
expr_stmt|;
name|h
operator|.
name|paramElement
argument_list|(
literal|"movie"
argument_list|,
name|swfUrl
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|paramElement
argument_list|(
literal|"FlashVars"
argument_list|,
name|flashVars
argument_list|)
expr_stmt|;
name|h
operator|.
name|openElement
argument_list|(
literal|"embed"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setWidth
argument_list|(
name|SWF_WIDTH
argument_list|)
expr_stmt|;
name|h
operator|.
name|setHeight
argument_list|(
name|SWF_HEIGHT
argument_list|)
expr_stmt|;
name|h
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
literal|"application/x-shockwave-flash"
argument_list|)
expr_stmt|;
name|h
operator|.
name|setAttribute
argument_list|(
literal|"src"
argument_list|,
name|swfUrl
argument_list|()
argument_list|)
expr_stmt|;
name|h
operator|.
name|setAttribute
argument_list|(
literal|"FlashVars"
argument_list|,
name|flashVars
argument_list|)
expr_stmt|;
name|h
operator|.
name|closeSelf
argument_list|()
expr_stmt|;
name|h
operator|.
name|closeElement
argument_list|(
literal|"object"
argument_list|)
expr_stmt|;
name|h
operator|.
name|closeElement
argument_list|(
literal|"span"
argument_list|)
expr_stmt|;
if|if
condition|(
name|swf
operator|!=
literal|null
condition|)
block|{
name|DOM
operator|.
name|removeChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|swf
argument_list|)
expr_stmt|;
block|}
name|DOM
operator|.
name|appendChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|swf
operator|=
name|SafeHtml
operator|.
name|parse
argument_list|(
name|h
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|setText (final String newText)
specifier|public
name|void
name|setText
parameter_list|(
specifier|final
name|String
name|newText
parameter_list|)
block|{
name|text
operator|=
name|newText
expr_stmt|;
if|if
condition|(
name|textLabel
operator|!=
literal|null
condition|)
block|{
name|textLabel
operator|.
name|setText
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|textBox
operator|!=
literal|null
condition|)
block|{
name|textBox
operator|.
name|setText
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|selectAll
argument_list|()
expr_stmt|;
block|}
name|embedMovie
argument_list|()
expr_stmt|;
block|}
DECL|method|showTextBox ()
specifier|private
name|void
name|showTextBox
parameter_list|()
block|{
if|if
condition|(
name|textBox
operator|==
literal|null
condition|)
block|{
name|textBox
operator|=
operator|new
name|TextBox
argument_list|()
expr_stmt|;
name|textBox
operator|.
name|setText
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setVisibleLength
argument_list|(
name|getText
argument_list|()
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|addKeyboardListener
argument_list|(
operator|new
name|KeyboardListenerAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|Widget
name|sender
parameter_list|,
specifier|final
name|char
name|kc
parameter_list|,
specifier|final
name|int
name|mod
parameter_list|)
block|{
if|if
condition|(
operator|(
name|mod
operator|&
name|MODIFIER_CTRL
operator|)
operator|==
name|MODIFIER_CTRL
operator|||
operator|(
name|mod
operator|&
name|MODIFIER_META
operator|)
operator|==
name|MODIFIER_META
condition|)
block|{
switch|switch
condition|(
name|kc
condition|)
block|{
case|case
literal|'c'
case|:
case|case
literal|'x'
case|:
name|DeferredCommand
operator|.
name|addCommand
argument_list|(
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|hideTextBox
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|addFocusListener
argument_list|(
operator|new
name|FocusListener
argument_list|()
block|{
specifier|public
name|void
name|onFocus
parameter_list|(
name|Widget
name|arg0
parameter_list|)
block|{         }
specifier|public
name|void
name|onLostFocus
parameter_list|(
name|Widget
name|arg0
parameter_list|)
block|{
name|hideTextBox
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|content
operator|.
name|insert
argument_list|(
name|textBox
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
name|textLabel
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|textBox
operator|.
name|selectAll
argument_list|()
expr_stmt|;
name|textBox
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|hideTextBox ()
specifier|private
name|void
name|hideTextBox
parameter_list|()
block|{
if|if
condition|(
name|textBox
operator|!=
literal|null
condition|)
block|{
name|textBox
operator|.
name|removeFromParent
argument_list|()
expr_stmt|;
name|textBox
operator|=
literal|null
expr_stmt|;
block|}
name|textLabel
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

