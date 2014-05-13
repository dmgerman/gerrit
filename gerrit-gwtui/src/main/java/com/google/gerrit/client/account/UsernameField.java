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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|ConfirmationCallback
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
name|ConfirmationDialog
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
name|ErrorDialog
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
name|ui
operator|.
name|OnEditEnabler
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
name|common
operator|.
name|errors
operator|.
name|InvalidUserNameException
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
name|Account
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
name|TextBox
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
name|clippy
operator|.
name|client
operator|.
name|CopyableLabel
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
name|gwtjsonrpc
operator|.
name|common
operator|.
name|VoidResult
import|;
end_import

begin_class
DECL|class|UsernameField
class|class
name|UsernameField
extends|extends
name|Composite
block|{
DECL|field|userNameLbl
specifier|private
name|CopyableLabel
name|userNameLbl
decl_stmt|;
DECL|field|userNameTxt
specifier|private
name|NpTextBox
name|userNameTxt
decl_stmt|;
DECL|field|setUserName
specifier|private
name|Button
name|setUserName
decl_stmt|;
DECL|method|UsernameField ()
name|UsernameField
parameter_list|()
block|{
name|String
name|user
init|=
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|getUserName
argument_list|()
decl_stmt|;
name|userNameLbl
operator|=
operator|new
name|CopyableLabel
argument_list|(
name|user
operator|!=
literal|null
condition|?
name|user
else|:
literal|""
argument_list|)
expr_stmt|;
name|userNameLbl
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
name|accountUsername
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|user
operator|!=
literal|null
operator|||
operator|!
name|canEditUserName
argument_list|()
condition|)
block|{
name|initWidget
argument_list|(
name|userNameLbl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|usernameField
argument_list|()
argument_list|)
expr_stmt|;
name|userNameTxt
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|userNameTxt
operator|.
name|addKeyPressHandler
argument_list|(
operator|new
name|UserNameValidator
argument_list|()
argument_list|)
expr_stmt|;
name|userNameTxt
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|accountUsername
argument_list|()
argument_list|)
expr_stmt|;
name|userNameTxt
operator|.
name|setVisibleLength
argument_list|(
literal|16
argument_list|)
expr_stmt|;
name|userNameTxt
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
name|KeyPressEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getNativeEvent
argument_list|()
operator|.
name|getKeyCode
argument_list|()
operator|==
name|KeyCodes
operator|.
name|KEY_ENTER
condition|)
block|{
name|confirmSetUserName
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|setUserName
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSetUserName
argument_list|()
argument_list|)
expr_stmt|;
name|setUserName
operator|.
name|setVisible
argument_list|(
name|canEditUserName
argument_list|()
argument_list|)
expr_stmt|;
name|setUserName
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setUserName
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
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|confirmSetUserName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
operator|new
name|OnEditEnabler
argument_list|(
name|setUserName
argument_list|,
name|userNameTxt
argument_list|)
expr_stmt|;
name|userNameLbl
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
name|userNameLbl
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|userNameTxt
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|setUserName
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|canEditUserName ()
specifier|private
name|boolean
name|canEditUserName
parameter_list|()
block|{
return|return
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|canEdit
argument_list|(
name|Account
operator|.
name|FieldName
operator|.
name|USER_NAME
argument_list|)
return|;
block|}
DECL|method|confirmSetUserName ()
specifier|private
name|void
name|confirmSetUserName
parameter_list|()
block|{
operator|new
name|ConfirmationDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|confirmSetUserNameTitle
argument_list|()
argument_list|,
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|Util
operator|.
name|C
operator|.
name|confirmSetUserName
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ConfirmationCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onOk
parameter_list|()
block|{
name|doSetUserName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
DECL|method|doSetUserName ()
specifier|private
name|void
name|doSetUserName
parameter_list|()
block|{
if|if
condition|(
operator|!
name|canEditUserName
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|newName
init|=
name|userNameTxt
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|newName
argument_list|)
condition|)
block|{
name|newName
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|newName
operator|!=
literal|null
operator|&&
operator|!
name|newName
operator|.
name|matches
argument_list|(
name|Account
operator|.
name|USER_NAME_PATTERN
argument_list|)
condition|)
block|{
name|invalidUserName
argument_list|()
expr_stmt|;
return|return;
block|}
name|enableUI
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|String
name|newUserName
init|=
name|newName
decl_stmt|;
name|Util
operator|.
name|ACCOUNT_SEC
operator|.
name|changeUserName
argument_list|(
name|newUserName
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|VoidResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|VoidResult
name|result
parameter_list|)
block|{
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
operator|.
name|setUserName
argument_list|(
name|newUserName
argument_list|)
expr_stmt|;
name|userNameLbl
operator|.
name|setText
argument_list|(
name|newUserName
argument_list|)
expr_stmt|;
name|userNameLbl
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|userNameTxt
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setUserName
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
name|enableUI
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|InvalidUserNameException
operator|.
name|MESSAGE
operator|.
name|equals
argument_list|(
name|caught
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
name|invalidUserName
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|invalidUserName ()
specifier|private
name|void
name|invalidUserName
parameter_list|()
block|{
operator|new
name|ErrorDialog
argument_list|(
name|Util
operator|.
name|C
operator|.
name|invalidUserName
argument_list|()
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
DECL|method|enableUI (final boolean on)
specifier|private
name|void
name|enableUI
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|userNameTxt
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|setUserName
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|class|UserNameValidator
specifier|private
specifier|final
class|class
name|UserNameValidator
implements|implements
name|KeyPressHandler
block|{
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
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
name|char
name|code
init|=
name|event
operator|.
name|getCharCode
argument_list|()
decl_stmt|;
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
name|KEY_ALT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_BACKSPACE
case|:
case|case
name|KeyCodes
operator|.
name|KEY_CTRL
case|:
case|case
name|KeyCodes
operator|.
name|KEY_DELETE
case|:
case|case
name|KeyCodes
operator|.
name|KEY_DOWN
case|:
case|case
name|KeyCodes
operator|.
name|KEY_END
case|:
case|case
name|KeyCodes
operator|.
name|KEY_ENTER
case|:
case|case
name|KeyCodes
operator|.
name|KEY_ESCAPE
case|:
case|case
name|KeyCodes
operator|.
name|KEY_HOME
case|:
case|case
name|KeyCodes
operator|.
name|KEY_LEFT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_PAGEDOWN
case|:
case|case
name|KeyCodes
operator|.
name|KEY_PAGEUP
case|:
case|case
name|KeyCodes
operator|.
name|KEY_RIGHT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_SHIFT
case|:
case|case
name|KeyCodes
operator|.
name|KEY_TAB
case|:
case|case
name|KeyCodes
operator|.
name|KEY_UP
case|:
comment|// Allow these, even if one of their assigned codes is
comment|// identical to an ASCII character we do not want to
comment|// allow in the box.
comment|//
comment|// We still want to let the user move around the input box
comment|// with their arrow keys, or to move between fields using tab.
comment|// Invalid characters introduced will be caught through the
comment|// server's own validation of the input data.
comment|//
break|break;
default|default:
specifier|final
name|TextBox
name|box
init|=
operator|(
name|TextBox
operator|)
name|event
operator|.
name|getSource
argument_list|()
decl_stmt|;
specifier|final
name|String
name|re
decl_stmt|;
if|if
condition|(
name|box
operator|.
name|getCursorPos
argument_list|()
operator|==
literal|0
condition|)
name|re
operator|=
name|Account
operator|.
name|USER_NAME_PATTERN_FIRST
expr_stmt|;
else|else
name|re
operator|=
name|Account
operator|.
name|USER_NAME_PATTERN_REST
expr_stmt|;
if|if
condition|(
operator|!
name|String
operator|.
name|valueOf
argument_list|(
name|code
argument_list|)
operator|.
name|matches
argument_list|(
literal|"^"
operator|+
name|re
operator|+
literal|"$"
argument_list|)
condition|)
block|{
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|event
operator|.
name|stopPropagation
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

