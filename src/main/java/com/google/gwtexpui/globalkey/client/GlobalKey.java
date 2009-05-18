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
DECL|package|com.google.gwtexpui.globalkey.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|KeyCodes
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
name|logical
operator|.
name|shared
operator|.
name|CloseEvent
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
name|CloseHandler
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
name|Timer
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
name|PopupPanel
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

begin_class
DECL|class|GlobalKey
specifier|public
class|class
name|GlobalKey
block|{
DECL|field|STOP_PROPAGATION
specifier|public
specifier|static
specifier|final
name|KeyPressHandler
name|STOP_PROPAGATION
init|=
operator|new
name|KeyPressHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|field|global
specifier|private
specifier|static
name|State
name|global
decl_stmt|;
DECL|field|active
specifier|static
name|State
name|active
decl_stmt|;
DECL|field|restoreGlobal
specifier|private
specifier|static
name|CloseHandler
argument_list|<
name|PopupPanel
argument_list|>
name|restoreGlobal
decl_stmt|;
DECL|field|restoreTimer
specifier|private
specifier|static
name|Timer
name|restoreTimer
decl_stmt|;
DECL|method|initEvents ()
specifier|private
specifier|static
name|void
name|initEvents
parameter_list|()
block|{
if|if
condition|(
name|active
operator|==
literal|null
condition|)
block|{
name|DocWidget
operator|.
name|get
argument_list|()
operator|.
name|addKeyPressHandler
argument_list|(
operator|new
name|KeyPressHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
specifier|final
name|KeyCommandSet
name|s
init|=
name|active
operator|.
name|live
decl_stmt|;
if|if
condition|(
name|s
operator|!=
name|active
operator|.
name|all
condition|)
block|{
name|active
operator|.
name|live
operator|=
name|active
operator|.
name|all
expr_stmt|;
name|restoreTimer
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
name|s
operator|.
name|onKeyPress
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|restoreTimer
operator|=
operator|new
name|Timer
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|active
operator|.
name|live
operator|=
name|active
operator|.
name|all
expr_stmt|;
block|}
block|}
expr_stmt|;
name|global
operator|=
operator|new
name|State
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|active
operator|=
name|global
expr_stmt|;
block|}
block|}
DECL|method|initDialog ()
specifier|private
specifier|static
name|void
name|initDialog
parameter_list|()
block|{
if|if
condition|(
name|restoreGlobal
operator|==
literal|null
condition|)
block|{
name|restoreGlobal
operator|=
operator|new
name|CloseHandler
argument_list|<
name|PopupPanel
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
specifier|final
name|CloseEvent
argument_list|<
name|PopupPanel
argument_list|>
name|event
parameter_list|)
block|{
name|active
operator|=
name|global
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
block|}
DECL|method|temporaryWithTimeout (final KeyCommandSet s)
specifier|static
name|void
name|temporaryWithTimeout
parameter_list|(
specifier|final
name|KeyCommandSet
name|s
parameter_list|)
block|{
name|active
operator|.
name|live
operator|=
name|s
expr_stmt|;
name|restoreTimer
operator|.
name|schedule
argument_list|(
literal|250
argument_list|)
expr_stmt|;
block|}
DECL|method|dialog (final PopupPanel panel)
specifier|public
specifier|static
name|void
name|dialog
parameter_list|(
specifier|final
name|PopupPanel
name|panel
parameter_list|)
block|{
name|initEvents
argument_list|()
expr_stmt|;
name|initDialog
argument_list|()
expr_stmt|;
assert|assert
name|panel
operator|.
name|isVisible
argument_list|()
assert|;
assert|assert
name|active
operator|==
name|global
assert|;
name|active
operator|=
operator|new
name|State
argument_list|(
name|panel
argument_list|)
expr_stmt|;
name|active
operator|.
name|add
argument_list|(
operator|new
name|KeyCommand
argument_list|(
literal|0
argument_list|,
name|KeyCodes
operator|.
name|KEY_ESCAPE
argument_list|,
name|Util
operator|.
name|C
operator|.
name|closeCurrentDialog
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|panel
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|panel
operator|.
name|addCloseHandler
argument_list|(
name|restoreGlobal
argument_list|)
expr_stmt|;
block|}
DECL|method|addApplication (final Widget widget, final KeyCommand appKey)
specifier|public
specifier|static
name|HandlerRegistration
name|addApplication
parameter_list|(
specifier|final
name|Widget
name|widget
parameter_list|,
specifier|final
name|KeyCommand
name|appKey
parameter_list|)
block|{
name|initEvents
argument_list|()
expr_stmt|;
specifier|final
name|State
name|state
init|=
name|stateFor
argument_list|(
name|widget
argument_list|)
decl_stmt|;
name|state
operator|.
name|add
argument_list|(
name|appKey
argument_list|)
expr_stmt|;
return|return
operator|new
name|HandlerRegistration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|removeHandler
parameter_list|()
block|{
name|state
operator|.
name|remove
argument_list|(
name|appKey
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|add (final Widget widget, final KeyCommandSet cmdSet)
specifier|public
specifier|static
name|HandlerRegistration
name|add
parameter_list|(
specifier|final
name|Widget
name|widget
parameter_list|,
specifier|final
name|KeyCommandSet
name|cmdSet
parameter_list|)
block|{
name|initEvents
argument_list|()
expr_stmt|;
specifier|final
name|State
name|state
init|=
name|stateFor
argument_list|(
name|widget
argument_list|)
decl_stmt|;
name|state
operator|.
name|add
argument_list|(
name|cmdSet
argument_list|)
expr_stmt|;
return|return
operator|new
name|HandlerRegistration
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|removeHandler
parameter_list|()
block|{
name|state
operator|.
name|remove
argument_list|(
name|cmdSet
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|stateFor (Widget w)
specifier|private
specifier|static
name|State
name|stateFor
parameter_list|(
name|Widget
name|w
parameter_list|)
block|{
while|while
condition|(
name|w
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|w
operator|==
name|active
operator|.
name|root
condition|)
block|{
return|return
name|active
return|;
block|}
name|w
operator|=
name|w
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
name|global
return|;
block|}
DECL|method|filter (final KeyCommandFilter filter)
specifier|public
specifier|static
name|void
name|filter
parameter_list|(
specifier|final
name|KeyCommandFilter
name|filter
parameter_list|)
block|{
name|active
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
if|if
condition|(
name|active
operator|!=
name|global
condition|)
block|{
name|global
operator|.
name|filter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|GlobalKey ()
specifier|private
name|GlobalKey
parameter_list|()
block|{   }
DECL|class|State
specifier|static
class|class
name|State
block|{
DECL|field|root
specifier|final
name|Widget
name|root
decl_stmt|;
DECL|field|app
specifier|final
name|KeyCommandSet
name|app
decl_stmt|;
DECL|field|all
specifier|final
name|KeyCommandSet
name|all
decl_stmt|;
DECL|field|live
name|KeyCommandSet
name|live
decl_stmt|;
DECL|method|State (final Widget r)
name|State
parameter_list|(
specifier|final
name|Widget
name|r
parameter_list|)
block|{
name|root
operator|=
name|r
expr_stmt|;
name|app
operator|=
operator|new
name|KeyCommandSet
argument_list|(
name|Util
operator|.
name|C
operator|.
name|applicationSection
argument_list|()
argument_list|)
expr_stmt|;
name|app
operator|.
name|add
argument_list|(
name|ShowHelpCommand
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|all
operator|=
operator|new
name|KeyCommandSet
argument_list|()
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|app
argument_list|)
expr_stmt|;
name|live
operator|=
name|all
expr_stmt|;
block|}
DECL|method|add (final KeyCommand k)
name|void
name|add
parameter_list|(
specifier|final
name|KeyCommand
name|k
parameter_list|)
block|{
name|app
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
name|all
operator|.
name|add
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (final KeyCommand k)
name|void
name|remove
parameter_list|(
specifier|final
name|KeyCommand
name|k
parameter_list|)
block|{
name|app
operator|.
name|remove
argument_list|(
name|k
argument_list|)
expr_stmt|;
name|all
operator|.
name|remove
argument_list|(
name|k
argument_list|)
expr_stmt|;
block|}
DECL|method|add (final KeyCommandSet s)
name|void
name|add
parameter_list|(
specifier|final
name|KeyCommandSet
name|s
parameter_list|)
block|{
name|all
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|remove (final KeyCommandSet s)
name|void
name|remove
parameter_list|(
specifier|final
name|KeyCommandSet
name|s
parameter_list|)
block|{
name|all
operator|.
name|remove
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|filter (final KeyCommandFilter f)
name|void
name|filter
parameter_list|(
specifier|final
name|KeyCommandFilter
name|f
parameter_list|)
block|{
name|all
operator|.
name|filter
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

