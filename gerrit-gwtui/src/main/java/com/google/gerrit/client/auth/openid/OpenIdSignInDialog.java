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
DECL|package|com.google.gerrit.client.auth.openid
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
name|openid
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
name|openid
operator|.
name|DiscoveryResult
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
name|openid
operator|.
name|OpenIdProviderPattern
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
name|openid
operator|.
name|OpenIdUrls
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
name|FormElement
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
name|resources
operator|.
name|client
operator|.
name|ImageResource
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
name|Cookies
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
name|FormPanel
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
name|FormPanel
operator|.
name|SubmitEvent
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
name|FormSubmitCompleteEvent
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
name|Hidden
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
name|Image
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|OpenIdSignInDialog
specifier|public
class|class
name|OpenIdSignInDialog
extends|extends
name|SignInDialog
implements|implements
name|FormPanel
operator|.
name|SubmitHandler
block|{
static|static
block|{
name|OpenIdResources
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
DECL|field|panelWidget
specifier|private
specifier|final
name|FlowPanel
name|panelWidget
decl_stmt|;
DECL|field|form
specifier|private
specifier|final
name|FormPanel
name|form
decl_stmt|;
DECL|field|formBody
specifier|private
specifier|final
name|FlowPanel
name|formBody
decl_stmt|;
DECL|field|redirectForm
specifier|private
specifier|final
name|FormPanel
name|redirectForm
decl_stmt|;
DECL|field|redirectBody
specifier|private
specifier|final
name|FlowPanel
name|redirectBody
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
DECL|field|providerId
specifier|private
name|NpTextBox
name|providerId
decl_stmt|;
DECL|field|rememberId
specifier|private
name|CheckBox
name|rememberId
decl_stmt|;
DECL|field|discovering
specifier|private
name|boolean
name|discovering
decl_stmt|;
DECL|method|OpenIdSignInDialog (final SignInMode requestedMode, final String token, final String initialErrorMsg)
specifier|public
name|OpenIdSignInDialog
parameter_list|(
specifier|final
name|SignInMode
name|requestedMode
parameter_list|,
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
name|requestedMode
argument_list|,
name|token
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
name|OpenIdResources
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
name|form
operator|=
operator|new
name|FormPanel
argument_list|()
expr_stmt|;
name|form
operator|.
name|setMethod
argument_list|(
name|FormPanel
operator|.
name|METHOD_GET
argument_list|)
expr_stmt|;
name|form
operator|.
name|addSubmitHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|form
operator|.
name|add
argument_list|(
name|formBody
argument_list|)
expr_stmt|;
name|redirectBody
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|redirectBody
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|redirectForm
operator|=
operator|new
name|FormPanel
argument_list|()
expr_stmt|;
name|redirectForm
operator|.
name|add
argument_list|(
name|redirectBody
argument_list|)
expr_stmt|;
name|panelWidget
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|panelWidget
operator|.
name|add
argument_list|(
name|form
argument_list|)
expr_stmt|;
name|panelWidget
operator|.
name|add
argument_list|(
name|redirectForm
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|panelWidget
argument_list|)
expr_stmt|;
name|createHeaderLogo
argument_list|()
expr_stmt|;
name|createHeaderText
argument_list|()
expr_stmt|;
name|createErrorBox
argument_list|()
expr_stmt|;
name|createIdentBox
argument_list|()
expr_stmt|;
name|link
argument_list|(
name|OpenIdUrls
operator|.
name|URL_GOOGLE
argument_list|,
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameGoogle
argument_list|()
argument_list|,
name|OpenIdResources
operator|.
name|I
operator|.
name|iconGoogle
argument_list|()
argument_list|)
expr_stmt|;
name|link
argument_list|(
name|OpenIdUrls
operator|.
name|URL_YAHOO
argument_list|,
name|OpenIdUtil
operator|.
name|C
operator|.
name|nameYahoo
argument_list|()
argument_list|,
name|OpenIdResources
operator|.
name|I
operator|.
name|iconYahoo
argument_list|()
argument_list|)
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
name|formBody
operator|.
name|add
argument_list|(
operator|new
name|HTML
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|whatIsOpenIDHtml
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|providerId
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
name|providerId
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
DECL|method|createHeaderLogo ()
specifier|private
name|void
name|createHeaderLogo
parameter_list|()
block|{
specifier|final
name|FlowPanel
name|headerLogo
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|headerLogo
operator|.
name|setStyleName
argument_list|(
name|OpenIdResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|logo
argument_list|()
argument_list|)
expr_stmt|;
name|headerLogo
operator|.
name|add
argument_list|(
operator|new
name|Image
argument_list|(
name|OpenIdResources
operator|.
name|I
operator|.
name|openidLogo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|formBody
operator|.
name|add
argument_list|(
name|headerLogo
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
name|String
name|me
init|=
name|Window
operator|.
name|Location
operator|.
name|getHostName
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
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|LINK_IDENTIY
case|:
name|headerLabel
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|linkAt
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|REGISTER
case|:
name|headerLabel
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|registerAt
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIGN_IN
case|:
default|default:
name|headerLabel
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|signInAt
argument_list|(
name|me
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
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
name|OpenIdResources
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
DECL|method|createIdentBox ()
specifier|private
name|void
name|createIdentBox
parameter_list|()
block|{
name|boolean
name|remember
init|=
name|mode
operator|==
name|SignInMode
operator|.
name|SIGN_IN
operator|||
name|mode
operator|==
name|SignInMode
operator|.
name|REGISTER
decl_stmt|;
specifier|final
name|FlowPanel
name|group
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|group
operator|.
name|setStyleName
argument_list|(
name|OpenIdResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|loginLine
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|FlowPanel
name|line1
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|group
operator|.
name|add
argument_list|(
name|line1
argument_list|)
expr_stmt|;
name|providerId
operator|=
operator|new
name|NpTextBox
argument_list|()
expr_stmt|;
name|providerId
operator|.
name|setVisibleLength
argument_list|(
literal|60
argument_list|)
expr_stmt|;
name|providerId
operator|.
name|setStyleName
argument_list|(
name|OpenIdResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|identifier
argument_list|()
argument_list|)
expr_stmt|;
name|providerId
operator|.
name|setTabIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|providerId
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
name|form
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|line1
operator|.
name|add
argument_list|(
name|providerId
argument_list|)
expr_stmt|;
name|login
operator|=
operator|new
name|Button
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|LINK_IDENTIY
case|:
name|login
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|buttonLinkId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REGISTER
case|:
name|login
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|buttonRegister
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIGN_IN
case|:
default|default:
name|login
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|buttonSignIn
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
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
name|form
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|login
operator|.
name|setTabIndex
argument_list|(
name|remember
condition|?
literal|2
else|:
literal|1
argument_list|)
expr_stmt|;
name|line1
operator|.
name|add
argument_list|(
name|login
argument_list|)
expr_stmt|;
name|Button
name|close
init|=
operator|new
name|Button
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|signInDialogClose
argument_list|()
argument_list|)
decl_stmt|;
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
name|close
operator|.
name|setTabIndex
argument_list|(
name|remember
condition|?
literal|3
else|:
literal|2
argument_list|)
expr_stmt|;
name|line1
operator|.
name|add
argument_list|(
name|close
argument_list|)
expr_stmt|;
if|if
condition|(
name|remember
condition|)
block|{
name|rememberId
operator|=
operator|new
name|CheckBox
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|rememberMe
argument_list|()
argument_list|)
expr_stmt|;
name|rememberId
operator|.
name|setTabIndex
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|group
operator|.
name|add
argument_list|(
name|rememberId
argument_list|)
expr_stmt|;
name|String
name|last
init|=
name|Cookies
operator|.
name|getCookie
argument_list|(
name|OpenIdUrls
operator|.
name|LASTID_COOKIE
argument_list|)
decl_stmt|;
if|if
condition|(
name|last
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|last
argument_list|)
condition|)
block|{
if|if
condition|(
name|last
operator|.
name|startsWith
argument_list|(
literal|"\""
argument_list|)
operator|&&
name|last
operator|.
name|endsWith
argument_list|(
literal|"\""
argument_list|)
condition|)
block|{
comment|// Dequote the value. We shouldn't have to do this, but
comment|// something is causing some Google Account tokens to get
comment|// wrapped up in double quotes when obtained from the cookie.
comment|//
name|last
operator|=
name|last
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|last
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
name|providerId
operator|.
name|setText
argument_list|(
name|last
argument_list|)
expr_stmt|;
name|rememberId
operator|.
name|setValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|formBody
operator|.
name|add
argument_list|(
name|group
argument_list|)
expr_stmt|;
block|}
DECL|method|link (final String identUrl, final String who, final ImageResource icon)
specifier|private
name|void
name|link
parameter_list|(
specifier|final
name|String
name|identUrl
parameter_list|,
specifier|final
name|String
name|who
parameter_list|,
specifier|final
name|ImageResource
name|icon
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAllowedProvider
argument_list|(
name|identUrl
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|ClickHandler
name|i
init|=
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
name|event
operator|.
name|preventDefault
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|discovering
condition|)
block|{
name|providerId
operator|.
name|setText
argument_list|(
name|identUrl
argument_list|)
expr_stmt|;
name|form
operator|.
name|submit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
specifier|final
name|FlowPanel
name|line
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|line
operator|.
name|addStyleName
argument_list|(
name|OpenIdResources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|directLink
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|img
init|=
operator|new
name|Image
argument_list|(
name|icon
argument_list|)
decl_stmt|;
name|img
operator|.
name|addClickHandler
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|img
argument_list|)
expr_stmt|;
specifier|final
name|Anchor
name|text
init|=
operator|new
name|Anchor
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|mode
condition|)
block|{
case|case
name|LINK_IDENTIY
case|:
name|text
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|linkWith
argument_list|(
name|who
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|REGISTER
case|:
name|text
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|registerWith
argument_list|(
name|who
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|SIGN_IN
case|:
default|default:
name|text
operator|.
name|setText
argument_list|(
name|OpenIdUtil
operator|.
name|M
operator|.
name|signInWith
argument_list|(
name|who
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
name|text
operator|.
name|setHref
argument_list|(
name|identUrl
argument_list|)
expr_stmt|;
name|text
operator|.
name|addClickHandler
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|formBody
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
DECL|method|isAllowedProvider (final String identUrl)
specifier|private
specifier|static
name|boolean
name|isAllowedProvider
parameter_list|(
specifier|final
name|String
name|identUrl
parameter_list|)
block|{
for|for
control|(
name|OpenIdProviderPattern
name|p
range|:
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getAllowedOpenIDs
argument_list|()
control|)
block|{
if|if
condition|(
name|p
operator|.
name|matches
argument_list|(
name|identUrl
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
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
name|providerId
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
DECL|method|onDiscovery (final DiscoveryResult result)
specifier|private
name|void
name|onDiscovery
parameter_list|(
specifier|final
name|DiscoveryResult
name|result
parameter_list|)
block|{
name|discovering
operator|=
literal|false
expr_stmt|;
switch|switch
condition|(
name|result
operator|.
name|status
condition|)
block|{
case|case
name|VALID
case|:
comment|// The provider won't support operation inside an IFRAME,
comment|// so we replace our entire application.
comment|//
name|redirectForm
operator|.
name|setMethod
argument_list|(
name|FormPanel
operator|.
name|METHOD_POST
argument_list|)
expr_stmt|;
name|redirectForm
operator|.
name|setAction
argument_list|(
name|result
operator|.
name|providerUrl
argument_list|)
expr_stmt|;
name|redirectBody
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|e
range|:
name|result
operator|.
name|providerArgs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|redirectBody
operator|.
name|add
argument_list|(
operator|new
name|Hidden
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|FormElement
operator|.
name|as
argument_list|(
name|redirectForm
operator|.
name|getElement
argument_list|()
argument_list|)
operator|.
name|setTarget
argument_list|(
literal|"_top"
argument_list|)
expr_stmt|;
name|redirectForm
operator|.
name|submit
argument_list|()
expr_stmt|;
break|break;
case|case
name|NOT_ALLOWED
case|:
name|showError
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|notAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|enableRetryDiscovery
argument_list|()
expr_stmt|;
break|break;
case|case
name|NO_PROVIDER
case|:
name|showError
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|noProvider
argument_list|()
argument_list|)
expr_stmt|;
name|enableRetryDiscovery
argument_list|()
expr_stmt|;
break|break;
case|case
name|ERROR
case|:
default|default:
name|showError
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|error
argument_list|()
argument_list|)
expr_stmt|;
name|enableRetryDiscovery
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
DECL|method|enableRetryDiscovery ()
specifier|private
name|void
name|enableRetryDiscovery
parameter_list|()
block|{
name|enable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|providerId
operator|.
name|selectAll
argument_list|()
expr_stmt|;
name|providerId
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSubmit (final SubmitEvent event)
specifier|public
name|void
name|onSubmit
parameter_list|(
specifier|final
name|SubmitEvent
name|event
parameter_list|)
block|{
name|event
operator|.
name|cancel
argument_list|()
expr_stmt|;
specifier|final
name|String
name|openidIdentifier
init|=
name|providerId
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|openidIdentifier
operator|==
literal|null
operator|||
name|openidIdentifier
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|enable
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|isAllowedProvider
argument_list|(
name|openidIdentifier
argument_list|)
condition|)
block|{
name|showError
argument_list|(
name|OpenIdUtil
operator|.
name|C
operator|.
name|notAllowed
argument_list|()
argument_list|)
expr_stmt|;
name|enableRetryDiscovery
argument_list|()
expr_stmt|;
return|return;
block|}
name|discovering
operator|=
literal|true
expr_stmt|;
name|enable
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|hideError
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|remember
init|=
name|rememberId
operator|!=
literal|null
operator|&&
name|rememberId
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|OpenIdUtil
operator|.
name|SVC
operator|.
name|discover
argument_list|(
name|openidIdentifier
argument_list|,
name|mode
argument_list|,
name|remember
argument_list|,
name|token
argument_list|,
operator|new
name|GerritCallback
argument_list|<
name|DiscoveryResult
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|DiscoveryResult
name|result
parameter_list|)
block|{
name|onDiscovery
argument_list|(
name|result
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
name|super
operator|.
name|onFailure
argument_list|(
name|caught
argument_list|)
expr_stmt|;
name|enableRetryDiscovery
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|onSubmitComplete (final FormSubmitCompleteEvent event)
specifier|public
name|void
name|onSubmitComplete
parameter_list|(
specifier|final
name|FormSubmitCompleteEvent
name|event
parameter_list|)
block|{   }
block|}
end_class

end_unit

