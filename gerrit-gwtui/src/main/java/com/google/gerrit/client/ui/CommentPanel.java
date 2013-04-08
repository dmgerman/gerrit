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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|FormatUtil
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
name|account
operator|.
name|AccountInfo
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
name|BlurEvent
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
name|BlurHandler
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
name|dom
operator|.
name|client
operator|.
name|DoubleClickEvent
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
name|DoubleClickHandler
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
name|FocusEvent
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
name|FocusHandler
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
name|HasBlurHandlers
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
name|HasDoubleClickHandlers
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
name|HasFocusHandlers
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
name|HandlerManager
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
name|ui
operator|.
name|Button
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
name|FlexTable
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
name|HTML
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
name|HTMLTable
operator|.
name|CellFormatter
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
name|HasHorizontalAlignment
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
name|Panel
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
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|CommentPanel
specifier|public
class|class
name|CommentPanel
extends|extends
name|Composite
implements|implements
name|HasDoubleClickHandlers
implements|,
name|HasFocusHandlers
implements|,
name|FocusHandler
implements|,
name|HasBlurHandlers
implements|,
name|BlurHandler
block|{
DECL|field|SUMMARY_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|SUMMARY_LENGTH
init|=
literal|75
decl_stmt|;
DECL|field|handlerManager
specifier|private
specifier|final
name|HandlerManager
name|handlerManager
init|=
operator|new
name|HandlerManager
argument_list|(
name|this
argument_list|)
decl_stmt|;
DECL|field|header
specifier|private
specifier|final
name|FlexTable
name|header
decl_stmt|;
DECL|field|messageSummary
specifier|private
specifier|final
name|InlineLabel
name|messageSummary
decl_stmt|;
DECL|field|content
specifier|private
specifier|final
name|FlowPanel
name|content
decl_stmt|;
DECL|field|messageText
specifier|private
specifier|final
name|DoubleClickHTML
name|messageText
decl_stmt|;
DECL|field|commentLinkProcessor
specifier|private
name|CommentLinkProcessor
name|commentLinkProcessor
decl_stmt|;
DECL|field|buttons
specifier|private
name|FlowPanel
name|buttons
decl_stmt|;
DECL|field|recent
specifier|private
name|boolean
name|recent
decl_stmt|;
DECL|method|CommentPanel (final AccountInfo author, final Date when, String message, CommentLinkProcessor commentLinkProcessor)
specifier|public
name|CommentPanel
parameter_list|(
specifier|final
name|AccountInfo
name|author
parameter_list|,
specifier|final
name|Date
name|when
parameter_list|,
name|String
name|message
parameter_list|,
name|CommentLinkProcessor
name|commentLinkProcessor
parameter_list|)
block|{
name|this
argument_list|(
name|commentLinkProcessor
argument_list|)
expr_stmt|;
name|setMessageText
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|setAuthorNameText
argument_list|(
name|FormatUtil
operator|.
name|name
argument_list|(
name|author
argument_list|)
argument_list|)
expr_stmt|;
name|setDateText
argument_list|(
name|FormatUtil
operator|.
name|shortFormatDayTime
argument_list|(
name|when
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|CellFormatter
name|fmt
init|=
name|header
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|)
operator|.
name|setTitle
argument_list|(
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|author
argument_list|)
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|getElement
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
operator|.
name|setTitle
argument_list|(
name|FormatUtil
operator|.
name|mediumFormat
argument_list|(
name|when
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|CommentPanel (CommentLinkProcessor commentLinkProcessor)
specifier|protected
name|CommentPanel
parameter_list|(
name|CommentLinkProcessor
name|commentLinkProcessor
parameter_list|)
block|{
name|this
operator|.
name|commentLinkProcessor
operator|=
name|commentLinkProcessor
expr_stmt|;
specifier|final
name|FlowPanel
name|body
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanel
argument_list|()
argument_list|)
expr_stmt|;
name|messageSummary
operator|=
operator|new
name|InlineLabel
argument_list|()
expr_stmt|;
name|messageSummary
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelSummary
argument_list|()
argument_list|)
expr_stmt|;
name|header
operator|=
operator|new
name|FlexTable
argument_list|()
expr_stmt|;
name|header
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelHeader
argument_list|()
argument_list|)
expr_stmt|;
name|header
operator|.
name|addClickHandler
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
name|setOpen
argument_list|(
operator|!
name|isOpen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|header
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|header
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|messageSummary
argument_list|)
expr_stmt|;
name|header
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|""
argument_list|)
expr_stmt|;
specifier|final
name|CellFormatter
name|fmt
init|=
name|header
operator|.
name|getCellFormatter
argument_list|()
decl_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelAuthorCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelSummaryCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setStyleName
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelDateCell
argument_list|()
argument_list|)
expr_stmt|;
name|fmt
operator|.
name|setHorizontalAlignment
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|HasHorizontalAlignment
operator|.
name|ALIGN_RIGHT
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|header
argument_list|)
expr_stmt|;
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
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelContent
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|content
argument_list|)
expr_stmt|;
name|messageText
operator|=
operator|new
name|DoubleClickHTML
argument_list|()
expr_stmt|;
name|messageText
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelMessage
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|.
name|add
argument_list|(
name|messageText
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addDoubleClickHandler (DoubleClickHandler handler)
specifier|public
name|HandlerRegistration
name|addDoubleClickHandler
parameter_list|(
name|DoubleClickHandler
name|handler
parameter_list|)
block|{
return|return
name|messageText
operator|.
name|addDoubleClickHandler
argument_list|(
name|handler
argument_list|)
return|;
block|}
DECL|method|setMessageText (String message)
specifier|protected
name|void
name|setMessageText
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
name|message
operator|=
literal|""
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|message
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|messageSummary
operator|.
name|setText
argument_list|(
name|summarize
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|SafeHtml
name|buf
init|=
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|message
argument_list|)
operator|.
name|wikify
argument_list|()
decl_stmt|;
name|buf
operator|=
name|commentLinkProcessor
operator|.
name|apply
argument_list|(
name|buf
argument_list|)
expr_stmt|;
name|SafeHtml
operator|.
name|set
argument_list|(
name|messageText
argument_list|,
name|buf
argument_list|)
expr_stmt|;
block|}
DECL|method|setAuthorNameText (final String nameText)
specifier|public
name|void
name|setAuthorNameText
parameter_list|(
specifier|final
name|String
name|nameText
parameter_list|)
block|{
name|header
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|nameText
argument_list|)
expr_stmt|;
block|}
DECL|method|setDateText (final String dateText)
specifier|protected
name|void
name|setDateText
parameter_list|(
specifier|final
name|String
name|dateText
parameter_list|)
block|{
name|header
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
name|dateText
argument_list|)
expr_stmt|;
block|}
DECL|method|setMessageTextVisible (final boolean show)
specifier|protected
name|void
name|setMessageTextVisible
parameter_list|(
specifier|final
name|boolean
name|show
parameter_list|)
block|{
name|messageText
operator|.
name|setVisible
argument_list|(
name|show
argument_list|)
expr_stmt|;
block|}
DECL|method|addContent (final Widget w)
specifier|protected
name|void
name|addContent
parameter_list|(
specifier|final
name|Widget
name|w
parameter_list|)
block|{
if|if
condition|(
name|buttons
operator|!=
literal|null
condition|)
block|{
name|content
operator|.
name|insert
argument_list|(
name|w
argument_list|,
name|content
operator|.
name|getWidgetIndex
argument_list|(
name|buttons
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|content
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**    * Registers a {@link FocusHandler} for this comment panel.    * The comment panel is considered as being focused whenever any button in the    * comment panel gets focused.    *    * @param handler the focus handler to be registered    */
annotation|@
name|Override
DECL|method|addFocusHandler (final FocusHandler handler)
specifier|public
name|HandlerRegistration
name|addFocusHandler
parameter_list|(
specifier|final
name|FocusHandler
name|handler
parameter_list|)
block|{
return|return
name|handlerManager
operator|.
name|addHandler
argument_list|(
name|FocusEvent
operator|.
name|getType
argument_list|()
argument_list|,
name|handler
argument_list|)
return|;
block|}
comment|/**    * Registers a {@link BlurHandler} for this comment panel.    * The comment panel is considered as being blurred whenever any button in the    * comment panel gets blurred.    *    * @param handler the blur handler to be registered    */
annotation|@
name|Override
DECL|method|addBlurHandler (final BlurHandler handler)
specifier|public
name|HandlerRegistration
name|addBlurHandler
parameter_list|(
specifier|final
name|BlurHandler
name|handler
parameter_list|)
block|{
return|return
name|handlerManager
operator|.
name|addHandler
argument_list|(
name|BlurEvent
operator|.
name|getType
argument_list|()
argument_list|,
name|handler
argument_list|)
return|;
block|}
DECL|method|addButton (final Button button)
specifier|protected
name|void
name|addButton
parameter_list|(
specifier|final
name|Button
name|button
parameter_list|)
block|{
comment|// register focus and blur handler for each button, so that we can fire
comment|// focus and blur events for the comment panel
name|button
operator|.
name|addFocusHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|button
operator|.
name|addBlurHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|getButtonPanel
argument_list|()
operator|.
name|add
argument_list|(
name|button
argument_list|)
expr_stmt|;
block|}
DECL|method|getButtonPanel ()
specifier|private
name|Panel
name|getButtonPanel
parameter_list|()
block|{
if|if
condition|(
name|buttons
operator|==
literal|null
condition|)
block|{
name|buttons
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|buttons
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|commentPanelButtons
argument_list|()
argument_list|)
expr_stmt|;
name|content
operator|.
name|add
argument_list|(
name|buttons
argument_list|)
expr_stmt|;
block|}
return|return
name|buttons
return|;
block|}
annotation|@
name|Override
DECL|method|onFocus (final FocusEvent event)
specifier|public
name|void
name|onFocus
parameter_list|(
specifier|final
name|FocusEvent
name|event
parameter_list|)
block|{
comment|// a button was focused -> fire focus event for the comment panel
name|handlerManager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onBlur (final BlurEvent event)
specifier|public
name|void
name|onBlur
parameter_list|(
specifier|final
name|BlurEvent
name|event
parameter_list|)
block|{
comment|// a button was blurred -> fire blur event for the comment panel
name|handlerManager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|enableButtons (final boolean on)
specifier|public
name|void
name|enableButtons
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
for|for
control|(
name|Widget
name|w
range|:
name|getButtonPanel
argument_list|()
control|)
block|{
if|if
condition|(
name|w
operator|instanceof
name|Button
condition|)
block|{
operator|(
operator|(
name|Button
operator|)
name|w
operator|)
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|summarize (final String message)
specifier|private
specifier|static
name|String
name|summarize
parameter_list|(
specifier|final
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|length
argument_list|()
operator|<
name|SUMMARY_LENGTH
condition|)
block|{
return|return
name|message
return|;
block|}
name|int
name|p
init|=
literal|0
decl_stmt|;
specifier|final
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
while|while
condition|(
name|r
operator|.
name|length
argument_list|()
operator|<
name|SUMMARY_LENGTH
condition|)
block|{
specifier|final
name|int
name|e
init|=
name|message
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|<
literal|0
condition|)
block|{
break|break;
block|}
specifier|final
name|String
name|word
init|=
name|message
operator|.
name|substring
argument_list|(
name|p
argument_list|,
name|e
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|SUMMARY_LENGTH
operator|<=
name|r
operator|.
name|length
argument_list|()
operator|+
name|word
operator|.
name|length
argument_list|()
operator|+
literal|1
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|r
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
name|word
argument_list|)
expr_stmt|;
name|p
operator|=
name|e
operator|+
literal|1
expr_stmt|;
block|}
name|r
operator|.
name|append
argument_list|(
literal|" \u2026"
argument_list|)
expr_stmt|;
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|isOpen ()
specifier|public
name|boolean
name|isOpen
parameter_list|()
block|{
return|return
name|content
operator|.
name|isVisible
argument_list|()
return|;
block|}
DECL|method|setOpen (final boolean open)
specifier|public
name|void
name|setOpen
parameter_list|(
specifier|final
name|boolean
name|open
parameter_list|)
block|{
name|messageSummary
operator|.
name|setVisible
argument_list|(
operator|!
name|open
argument_list|)
expr_stmt|;
name|content
operator|.
name|setVisible
argument_list|(
name|open
argument_list|)
expr_stmt|;
block|}
DECL|method|isRecent ()
specifier|public
name|boolean
name|isRecent
parameter_list|()
block|{
return|return
name|recent
return|;
block|}
DECL|method|setRecent (final boolean r)
specifier|public
name|void
name|setRecent
parameter_list|(
specifier|final
name|boolean
name|r
parameter_list|)
block|{
name|recent
operator|=
name|r
expr_stmt|;
block|}
DECL|class|DoubleClickHTML
specifier|private
specifier|static
class|class
name|DoubleClickHTML
extends|extends
name|HTML
implements|implements
name|HasDoubleClickHandlers
block|{
DECL|method|addDoubleClickHandler (DoubleClickHandler handler)
specifier|public
name|HandlerRegistration
name|addDoubleClickHandler
parameter_list|(
name|DoubleClickHandler
name|handler
parameter_list|)
block|{
return|return
name|addDomHandler
argument_list|(
name|handler
argument_list|,
name|DoubleClickEvent
operator|.
name|getType
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

