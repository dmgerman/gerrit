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
DECL|package|com.google.gerrit.client.auth.userpass
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|auth
operator|.
name|userpass
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
name|SignInDialog
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
name|SmallHeading
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
name|PageLinks
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
name|auth
operator|.
name|SignInMode
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
name|auth
operator|.
name|userpass
operator|.
name|LoginResult
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
name|Window
operator|.
name|Location
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
name|Grid
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
name|PasswordTextBox
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
name|globalkey
operator|.
name|client
operator|.
name|GlobalKey
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

begin_class
DECL|class|UserPassSignInDialog
specifier|public
class|class
name|UserPassSignInDialog
extends|extends
name|SignInDialog
block|{
static|static
block|{
name|UserPassResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
DECL|field|formBody
specifier|private
specifier|final
name|FlowPanel
name|formBody
decl_stmt|;
DECL|field|errorLine
specifier|private
name|FlowPanel
name|errorLine
decl_stmt|;
DECL|field|errorMsg
specifier|private
name|InlineLabel
name|errorMsg
decl_stmt|;
DECL|field|login
specifier|private
name|Button
name|login
decl_stmt|;
DECL|field|close
specifier|private
name|Button
name|close
decl_stmt|;
DECL|field|username
specifier|private
name|TextBox
name|username
decl_stmt|;
DECL|field|password
specifier|private
name|TextBox
name|password
decl_stmt|;
DECL|method|UserPassSignInDialog (final String token, final String initialErrorMsg)
specifier|public
name|UserPassSignInDialog
parameter_list|(
specifier|final
name|String
name|token
parameter_list|,
specifier|final
name|String
name|initialErrorMsg
parameter_list|)
block|{
name|super
argument_list|(
name|SignInMode
operator|.
name|SIGN_IN
argument_list|,
name|token
argument_list|)
expr_stmt|;
name|setAutoHideEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|formBody
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|formBody
operator|.
name|setStyleName
argument_list|(
name|UserPassResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|loginForm
argument_list|()
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|formBody
argument_list|)
expr_stmt|;
name|createHeaderText
argument_list|()
expr_stmt|;
name|createErrorBox
argument_list|()
expr_stmt|;
name|createUsernameBox
argument_list|()
expr_stmt|;
if|if
condition|(
name|initialErrorMsg
operator|!=
literal|null
condition|)
block|{
name|showError
argument_list|(
name|initialErrorMsg
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|show ()
specifier|public
name|void
name|show
parameter_list|()
block|{
name|super
operator|.
name|show
argument_list|()
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
name|username
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createHeaderText ()
specifier|private
name|void
name|createHeaderText
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|headerText
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
specifier|final
name|SmallHeading
name|headerLabel
init|=
operator|new
name|SmallHeading
argument_list|()
decl_stmt|;
name|headerLabel
operator|.
name|setText
argument_list|(
name|Util
operator|.
name|M
operator|.
name|signInAt
argument_list|(
name|Location
operator|.
name|getHostName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|headerText
operator|.
name|add
argument_list|(
name|headerLabel
argument_list|)
expr_stmt|;
name|formBody
operator|.
name|add
argument_list|(
name|headerText
argument_list|)
expr_stmt|;
block|}
DECL|method|createErrorBox ()
specifier|private
name|void
name|createErrorBox
parameter_list|()
block|{
name|errorLine
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|DOM
operator|.
name|setStyleAttribute
argument_list|(
name|errorLine
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"visibility"
argument_list|,
literal|"hidden"
argument_list|)
expr_stmt|;
name|errorLine
operator|.
name|setStyleName
argument_list|(
name|UserPassResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|error
argument_list|()
argument_list|)
expr_stmt|;
name|errorMsg
operator|=
operator|new
name|InlineLabel
argument_list|()
expr_stmt|;
name|errorLine
operator|.
name|add
argument_list|(
name|errorMsg
argument_list|)
expr_stmt|;
name|formBody
operator|.
name|add
argument_list|(
name|errorLine
argument_list|)
expr_stmt|;
block|}
DECL|method|showError (final String msgText)
specifier|private
name|void
name|showError
parameter_list|(
specifier|final
name|String
name|msgText
parameter_list|)
block|{
name|errorMsg
operator|.
name|setText
argument_list|(
name|msgText
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|setStyleAttribute
argument_list|(
name|errorLine
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"visibility"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|hideError ()
specifier|private
name|void
name|hideError
parameter_list|()
block|{
name|DOM
operator|.
name|setStyleAttribute
argument_list|(
name|errorLine
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"visibility"
argument_list|,
literal|"hidden"
argument_list|)
expr_stmt|;
block|}
DECL|method|createUsernameBox ()
specifier|private
name|void
name|createUsernameBox
parameter_list|()
block|{
name|username
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|username
operator|.
name|setVisibleLength
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|username
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
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|password
operator|.
name|selectAll
argument_list|()
expr_stmt|;
name|password
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|password
operator|=
operator|new
name|PasswordTextBox
argument_list|()
expr_stmt|;
name|password
operator|.
name|setVisibleLength
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|password
operator|.
name|addKeyPressHandler
argument_list|(
name|GlobalKey
operator|.
name|STOP_PROPAGATION
argument_list|)
expr_stmt|;
name|password
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
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
name|onLogin
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|buttons
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
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
name|errorDialogButtons
argument_list|()
argument_list|)
expr_stmt|;
name|login
operator|=
operator|new
name|Button
argument_list|()
expr_stmt|;
name|login
operator|.
name|setText
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSignIn
argument_list|()
argument_list|)
expr_stmt|;
name|login
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
name|onLogin
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|login
argument_list|)
expr_stmt|;
name|close
operator|=
operator|new
name|Button
argument_list|()
expr_stmt|;
name|DOM
operator|.
name|setStyleAttribute
argument_list|(
name|close
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"marginLeft"
argument_list|,
literal|"45px"
argument_list|)
expr_stmt|;
name|close
operator|.
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|signInDialogClose
argument_list|()
argument_list|)
expr_stmt|;
name|close
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
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|buttons
operator|.
name|add
argument_list|(
name|close
argument_list|)
expr_stmt|;
specifier|final
name|Grid
name|formGrid
init|=
operator|new
name|Grid
argument_list|(
literal|3
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
literal|0
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|username
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setText
argument_list|(
literal|1
argument_list|,
literal|0
argument_list|,
name|Util
operator|.
name|C
operator|.
name|password
argument_list|()
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|,
name|username
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
name|password
argument_list|)
expr_stmt|;
name|formGrid
operator|.
name|setWidget
argument_list|(
literal|2
argument_list|,
literal|1
argument_list|,
name|buttons
argument_list|)
expr_stmt|;
name|formBody
operator|.
name|add
argument_list|(
name|formGrid
argument_list|)
expr_stmt|;
name|username
operator|.
name|setTabIndex
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|password
operator|.
name|setTabIndex
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|login
operator|.
name|setTabIndex
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|close
operator|.
name|setTabIndex
argument_list|(
literal|4
argument_list|)
expr_stmt|;
block|}
DECL|method|enable (final boolean on)
specifier|private
name|void
name|enable
parameter_list|(
specifier|final
name|boolean
name|on
parameter_list|)
block|{
name|username
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|password
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|login
operator|.
name|setEnabled
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
DECL|method|onLogin ()
specifier|private
name|void
name|onLogin
parameter_list|()
block|{
name|hideError
argument_list|()
expr_stmt|;
specifier|final
name|String
name|user
init|=
name|username
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|user
operator|==
literal|null
operator|||
name|user
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|showError
argument_list|(
name|Util
operator|.
name|C
operator|.
name|usernameRequired
argument_list|()
argument_list|)
expr_stmt|;
name|username
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
specifier|final
name|String
name|pass
init|=
name|password
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|pass
operator|==
literal|null
operator|||
name|pass
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|showError
argument_list|(
name|Util
operator|.
name|C
operator|.
name|passwordRequired
argument_list|()
argument_list|)
expr_stmt|;
name|password
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
name|enable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Util
operator|.
name|SVC
operator|.
name|authenticate
argument_list|(
name|user
argument_list|,
name|pass
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|LoginResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|LoginResult
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|.
name|success
condition|)
block|{
name|String
name|to
init|=
name|token
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|isNew
operator|&&
operator|!
name|to
operator|.
name|startsWith
argument_list|(
name|PageLinks
operator|.
name|REGISTER
operator|+
literal|","
argument_list|)
condition|)
block|{
name|to
operator|=
name|PageLinks
operator|.
name|REGISTER
operator|+
literal|","
operator|+
name|to
expr_stmt|;
block|}
comment|// Unfortunately we no longer support updating the web UI when the
comment|// user signs in. Instead we must force a reload of the page, but
comment|// that isn't easy because we might need to change the anchor. So
comment|// we bounce through a little redirection servlet on the server.
comment|//
name|Location
operator|.
name|replace
argument_list|(
name|Location
operator|.
name|getPath
argument_list|()
operator|+
literal|"login/"
operator|+
name|to
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|showError
argument_list|(
name|Util
operator|.
name|C
operator|.
name|invalidLogin
argument_list|()
argument_list|)
expr_stmt|;
name|enable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|password
operator|.
name|selectAll
argument_list|()
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
name|password
operator|.
name|setFocus
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
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
name|enable
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
end_class

end_unit

