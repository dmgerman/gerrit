begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ChangeEvent
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
name|ChangeHandler
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
name|KeyDownEvent
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
name|KeyDownHandler
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
name|KeyPressEvent
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
name|KeyPressHandler
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
name|MouseUpEvent
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
name|MouseUpHandler
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
name|ValueChangeEvent
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
name|ValueChangeHandler
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
name|GwtEvent
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
name|CheckBox
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
name|FocusWidget
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
name|ListBox
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
name|TextBoxBase
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
name|ValueBoxBase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** Enables a FocusWidget (e.g. a Button) if an edit is detected from any  *  registered input widget.  */
end_comment

begin_class
DECL|class|OnEditEnabler
specifier|public
class|class
name|OnEditEnabler
implements|implements
name|KeyPressHandler
implements|,
name|KeyDownHandler
implements|,
name|MouseUpHandler
implements|,
name|ChangeHandler
implements|,
name|ValueChangeHandler
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|widget
specifier|private
specifier|final
name|FocusWidget
name|widget
decl_stmt|;
DECL|field|strings
specifier|private
name|Map
argument_list|<
name|TextBoxBase
argument_list|,
name|String
argument_list|>
name|strings
init|=
operator|new
name|HashMap
argument_list|<
name|TextBoxBase
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// The first parameter to the contructors must be the FocusWidget to enable,
comment|// subsequent parameters are widgets to listenTo.
DECL|method|OnEditEnabler (final FocusWidget w, final TextBoxBase tb)
specifier|public
name|OnEditEnabler
parameter_list|(
specifier|final
name|FocusWidget
name|w
parameter_list|,
specifier|final
name|TextBoxBase
name|tb
parameter_list|)
block|{
name|this
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|listenTo
argument_list|(
name|tb
argument_list|)
expr_stmt|;
block|}
DECL|method|OnEditEnabler (final FocusWidget w, final ListBox lb)
specifier|public
name|OnEditEnabler
parameter_list|(
specifier|final
name|FocusWidget
name|w
parameter_list|,
specifier|final
name|ListBox
name|lb
parameter_list|)
block|{
name|this
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|listenTo
argument_list|(
name|lb
argument_list|)
expr_stmt|;
block|}
DECL|method|OnEditEnabler (final FocusWidget w, final CheckBox cb)
specifier|public
name|OnEditEnabler
parameter_list|(
specifier|final
name|FocusWidget
name|w
parameter_list|,
specifier|final
name|CheckBox
name|cb
parameter_list|)
block|{
name|this
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|listenTo
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
DECL|method|OnEditEnabler (final FocusWidget w)
specifier|public
name|OnEditEnabler
parameter_list|(
specifier|final
name|FocusWidget
name|w
parameter_list|)
block|{
name|widget
operator|=
name|w
expr_stmt|;
block|}
comment|// Register input widgets to be listened to
DECL|method|listenTo (final TextBoxBase tb)
specifier|public
name|void
name|listenTo
parameter_list|(
specifier|final
name|TextBoxBase
name|tb
parameter_list|)
block|{
name|strings
operator|.
name|put
argument_list|(
name|tb
argument_list|,
name|tb
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|tb
operator|.
name|addKeyPressHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Is there another way to capture middle button X11 pastes in browsers
comment|// which do not yet support ONPASTE events (Firefox)?
name|tb
operator|.
name|addMouseUpHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// Resetting the "original text" on focus ensures that we are
comment|// up to date with non-user updates of the text (calls to
comment|// setText()...) and also up to date with user changes which
comment|// occured after enabling "widget".
name|tb
operator|.
name|addFocusHandler
argument_list|(
operator|new
name|FocusHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFocus
parameter_list|(
name|FocusEvent
name|event
parameter_list|)
block|{
name|strings
operator|.
name|put
argument_list|(
name|tb
argument_list|,
name|tb
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// CTRL-V Pastes in Chrome seem only detectable via BrowserEvents or
comment|// KeyDownEvents, the latter is better.
name|tb
operator|.
name|addKeyDownHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|listenTo (final ListBox lb)
specifier|public
name|void
name|listenTo
parameter_list|(
specifier|final
name|ListBox
name|lb
parameter_list|)
block|{
name|lb
operator|.
name|addChangeHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|listenTo (final CheckBox cb)
specifier|public
name|void
name|listenTo
parameter_list|(
specifier|final
name|CheckBox
name|cb
parameter_list|)
block|{
name|cb
operator|.
name|addValueChangeHandler
argument_list|(
operator|(
name|ValueChangeHandler
operator|)
name|this
argument_list|)
expr_stmt|;
block|}
comment|// Handlers
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent e)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|e
parameter_list|)
block|{
name|on
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyDown (final KeyDownEvent e)
specifier|public
name|void
name|onKeyDown
parameter_list|(
specifier|final
name|KeyDownEvent
name|e
parameter_list|)
block|{
name|on
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMouseUp (final MouseUpEvent e)
specifier|public
name|void
name|onMouseUp
parameter_list|(
specifier|final
name|MouseUpEvent
name|e
parameter_list|)
block|{
name|on
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onChange (final ChangeEvent e)
specifier|public
name|void
name|onChange
parameter_list|(
specifier|final
name|ChangeEvent
name|e
parameter_list|)
block|{
name|on
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
DECL|method|onValueChange (final ValueChangeEvent e)
specifier|public
name|void
name|onValueChange
parameter_list|(
specifier|final
name|ValueChangeEvent
name|e
parameter_list|)
block|{
name|on
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|on (final GwtEvent<?> e)
specifier|private
name|void
name|on
parameter_list|(
specifier|final
name|GwtEvent
argument_list|<
name|?
argument_list|>
name|e
parameter_list|)
block|{
if|if
condition|(
name|widget
operator|.
name|isEnabled
argument_list|()
operator|||
operator|!
operator|(
name|e
operator|.
name|getSource
argument_list|()
operator|instanceof
name|FocusWidget
operator|)
operator|||
operator|!
operator|(
operator|(
name|FocusWidget
operator|)
name|e
operator|.
name|getSource
argument_list|()
operator|)
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|instanceof
name|ValueBoxBase
condition|)
block|{
specifier|final
name|TextBoxBase
name|box
init|=
operator|(
operator|(
name|TextBoxBase
operator|)
name|e
operator|.
name|getSource
argument_list|()
operator|)
decl_stmt|;
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
if|if
condition|(
name|box
operator|.
name|getValue
argument_list|()
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|widget
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
if|if
condition|(
name|e
operator|.
name|getSource
argument_list|()
operator|instanceof
name|TextBoxBase
condition|)
block|{
name|onTextBoxBase
argument_list|(
operator|(
name|TextBoxBase
operator|)
name|e
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// For many widgets, we can assume that a change is an edit. If
comment|// a widget does not work that way, it should be special cased
comment|// above.
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onTextBoxBase (final TextBoxBase tb)
specifier|private
name|void
name|onTextBoxBase
parameter_list|(
specifier|final
name|TextBoxBase
name|tb
parameter_list|)
block|{
comment|// The text appears to not get updated until the handlers complete.
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
name|String
name|orig
init|=
name|strings
operator|.
name|get
argument_list|(
name|tb
argument_list|)
decl_stmt|;
if|if
condition|(
name|orig
operator|==
literal|null
condition|)
block|{
name|orig
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|orig
operator|.
name|equals
argument_list|(
name|tb
operator|.
name|getText
argument_list|()
argument_list|)
condition|)
block|{
name|widget
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

