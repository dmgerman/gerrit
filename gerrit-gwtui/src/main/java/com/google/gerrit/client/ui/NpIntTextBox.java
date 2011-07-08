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
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|NpTextBox
import|;
end_import

begin_comment
comment|/** Text box that accepts only integer values. */
end_comment

begin_class
DECL|class|NpIntTextBox
specifier|public
class|class
name|NpIntTextBox
extends|extends
name|NpTextBox
block|{
DECL|field|intValue
specifier|private
name|int
name|intValue
decl_stmt|;
DECL|method|NpIntTextBox ()
specifier|public
name|NpIntTextBox
parameter_list|()
block|{
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|NpIntTextBox (Element element)
specifier|public
name|NpIntTextBox
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|super
argument_list|(
name|element
argument_list|)
expr_stmt|;
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|init ()
specifier|private
name|void
name|init
parameter_list|()
block|{
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
name|KeyPressEvent
name|event
parameter_list|)
block|{
name|char
name|c
init|=
name|event
operator|.
name|getCharCode
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|<
literal|'0'
operator|||
literal|'9'
operator|<
name|c
condition|)
block|{
specifier|final
name|int
name|nativeCode
init|=
name|event
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|nativeCode
condition|)
block|{
case|case
name|KeyCodes
operator|.
name|KEY_BACKSPACE
case|:
case|case
name|KeyCodes
operator|.
name|KEY_LEFT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_RIGHT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_HOME
case|:
case|case
name|KeyCodes
operator|.
name|KEY_END
case|:
case|case
name|KeyCodes
operator|.
name|KEY_TAB
case|:
break|break;
default|default:
if|if
condition|(
operator|!
name|event
operator|.
name|isAnyModifierKeyDown
argument_list|()
condition|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getIntValue ()
specifier|public
name|int
name|getIntValue
parameter_list|()
block|{
name|String
name|txt
init|=
name|getText
argument_list|()
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|txt
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
try|try
block|{
name|intValue
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{       }
block|}
return|return
name|intValue
return|;
block|}
DECL|method|setIntValue (int v)
specifier|public
name|void
name|setIntValue
parameter_list|(
name|int
name|v
parameter_list|)
block|{
name|intValue
operator|=
name|v
expr_stmt|;
name|setText
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|v
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

