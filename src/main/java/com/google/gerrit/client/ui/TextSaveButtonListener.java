begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
name|Widget
import|;
end_import

begin_comment
comment|/** Enables an action (e.g. a Button) if the text box is modified. */
end_comment

begin_class
DECL|class|TextSaveButtonListener
specifier|public
class|class
name|TextSaveButtonListener
extends|extends
name|KeyboardListenerAdapter
block|{
DECL|field|descAction
specifier|private
specifier|final
name|FocusWidget
name|descAction
decl_stmt|;
DECL|method|TextSaveButtonListener (final FocusWidget action)
specifier|public
name|TextSaveButtonListener
parameter_list|(
specifier|final
name|FocusWidget
name|action
parameter_list|)
block|{
name|descAction
operator|=
name|action
expr_stmt|;
block|}
DECL|method|TextSaveButtonListener (final TextBoxBase text, final FocusWidget action)
specifier|public
name|TextSaveButtonListener
parameter_list|(
specifier|final
name|TextBoxBase
name|text
parameter_list|,
specifier|final
name|FocusWidget
name|action
parameter_list|)
block|{
name|this
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|text
operator|.
name|addKeyboardListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final Widget sender, final char key, final int mod)
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
name|key
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
operator|(
name|MODIFIER_CTRL
operator||
name|MODIFIER_ALT
operator||
name|MODIFIER_META
operator|)
operator|)
operator|==
literal|0
condition|)
block|{
switch|switch
condition|(
name|key
condition|)
block|{
case|case
name|KEY_UP
case|:
case|case
name|KEY_DOWN
case|:
case|case
name|KEY_LEFT
case|:
case|case
name|KEY_RIGHT
case|:
case|case
name|KEY_HOME
case|:
case|case
name|KEY_END
case|:
case|case
name|KEY_PAGEUP
case|:
case|case
name|KEY_PAGEDOWN
case|:
case|case
name|KEY_ALT
case|:
case|case
name|KEY_CTRL
case|:
case|case
name|KEY_SHIFT
case|:
break|break;
default|default:
name|on
argument_list|(
name|sender
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
elseif|else
if|if
condition|(
operator|(
name|mod
operator|&
name|MODIFIER_CTRL
operator|)
operator|!=
literal|0
operator|&&
operator|(
name|key
operator|==
literal|'v'
operator|||
name|key
operator|==
literal|'x'
operator|)
condition|)
block|{
name|on
argument_list|(
name|sender
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|on (final Widget sender)
specifier|private
name|void
name|on
parameter_list|(
specifier|final
name|Widget
name|sender
parameter_list|)
block|{
name|descAction
operator|.
name|setEnabled
argument_list|(
operator|(
operator|(
name|TextBoxBase
operator|)
name|sender
operator|)
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

