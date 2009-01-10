begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2009 Google Inc.
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
DECL|package|com.google.gerrit.client.openid
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|EntryPoint
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
name|AbstractImagePrototype
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
name|FormHandler
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
name|FormSubmitEvent
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
name|RootPanel
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
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|Login
specifier|public
class|class
name|Login
implements|implements
name|EntryPoint
implements|,
name|FormHandler
block|{
DECL|field|URL_YAHOO
specifier|private
specifier|static
specifier|final
name|String
name|URL_YAHOO
init|=
literal|"https://me.yahoo.com"
decl_stmt|;
DECL|field|URL_GOOGLE
specifier|private
specifier|static
specifier|final
name|String
name|URL_GOOGLE
init|=
literal|"https://www.google.com/accounts/o8/id"
decl_stmt|;
DECL|field|allowsIFRAME
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|allowsIFRAME
decl_stmt|;
DECL|field|icons
specifier|private
name|LoginIcons
name|icons
decl_stmt|;
DECL|field|body
specifier|private
name|RootPanel
name|body
decl_stmt|;
DECL|field|form
specifier|private
name|FormPanel
name|form
decl_stmt|;
DECL|field|login
specifier|private
name|Button
name|login
decl_stmt|;
DECL|field|in_callback
specifier|private
name|Hidden
name|in_callback
decl_stmt|;
DECL|field|in_token
specifier|private
name|Hidden
name|in_token
decl_stmt|;
DECL|field|providerUrl
specifier|private
name|TextBox
name|providerUrl
decl_stmt|;
DECL|method|getLast_openid_identifier ()
specifier|private
specifier|static
specifier|native
name|String
name|getLast_openid_identifier
parameter_list|()
comment|/*-{ return $wnd.gerrit_openid_identifier.value; }-*/
function_decl|;
DECL|method|onModuleLoad ()
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{
name|allowsIFRAME
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|allowsIFRAME
operator|.
name|add
argument_list|(
name|URL_GOOGLE
argument_list|)
expr_stmt|;
name|icons
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|LoginIcons
operator|.
name|class
argument_list|)
expr_stmt|;
name|body
operator|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_login"
argument_list|)
expr_stmt|;
name|form
operator|=
name|FormPanel
operator|.
name|wrap
argument_list|(
name|DOM
operator|.
name|getElementById
argument_list|(
literal|"login_form"
argument_list|)
argument_list|)
expr_stmt|;
name|form
operator|.
name|addFormHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|in_callback
operator|=
name|Hidden
operator|.
name|wrap
argument_list|(
name|DOM
operator|.
name|getElementById
argument_list|(
literal|"in_callback"
argument_list|)
argument_list|)
expr_stmt|;
name|in_token
operator|=
name|Hidden
operator|.
name|wrap
argument_list|(
name|DOM
operator|.
name|getElementById
argument_list|(
literal|"in_token"
argument_list|)
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
name|createSignIn
argument_list|(
name|URL_GOOGLE
argument_list|,
name|Util
operator|.
name|C
operator|.
name|directGoogle
argument_list|()
argument_list|,
name|icons
operator|.
name|iconGoogle
argument_list|()
argument_list|)
expr_stmt|;
name|createSignIn
argument_list|(
name|URL_YAHOO
argument_list|,
name|Util
operator|.
name|C
operator|.
name|directYahoo
argument_list|()
argument_list|,
name|icons
operator|.
name|iconYahoo
argument_list|()
argument_list|)
expr_stmt|;
name|providerUrl
operator|.
name|setFocus
argument_list|(
literal|true
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
literal|"gerrit-OpenID-logobox"
argument_list|)
expr_stmt|;
name|headerLogo
operator|.
name|add
argument_list|(
name|icons
operator|.
name|openidLogo
argument_list|()
operator|.
name|createImage
argument_list|()
argument_list|)
expr_stmt|;
name|body
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
name|Label
name|headerLabel
init|=
operator|new
name|Label
argument_list|(
name|Util
operator|.
name|M
operator|.
name|signInAt
argument_list|(
name|Window
operator|.
name|Location
operator|.
name|getHostName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|headerLabel
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-SmallHeading"
argument_list|)
expr_stmt|;
name|headerText
operator|.
name|add
argument_list|(
name|headerLabel
argument_list|)
expr_stmt|;
name|body
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
specifier|final
name|String
name|url
init|=
name|getLast_openid_identifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
specifier|final
name|FlowPanel
name|line
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
specifier|final
name|InlineLabel
name|msg
init|=
operator|new
name|InlineLabel
argument_list|(
name|Util
operator|.
name|M
operator|.
name|notSupported
argument_list|(
name|url
argument_list|)
argument_list|)
decl_stmt|;
name|line
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-OpenID-errorline"
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createIdentBox ()
specifier|private
name|void
name|createIdentBox
parameter_list|()
block|{
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
name|setStyleName
argument_list|(
literal|"gerrit-OpenID-loginline"
argument_list|)
expr_stmt|;
name|providerUrl
operator|=
operator|new
name|TextBox
argument_list|()
expr_stmt|;
name|providerUrl
operator|.
name|setName
argument_list|(
literal|"openid_identifier"
argument_list|)
expr_stmt|;
name|providerUrl
operator|.
name|setVisibleLength
argument_list|(
literal|40
argument_list|)
expr_stmt|;
name|providerUrl
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-OpenID-openid_identifier"
argument_list|)
expr_stmt|;
name|providerUrl
operator|.
name|setTabIndex
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|providerUrl
argument_list|)
expr_stmt|;
name|login
operator|=
operator|new
name|Button
argument_list|(
name|Util
operator|.
name|C
operator|.
name|buttonSignIn
argument_list|()
argument_list|,
operator|new
name|ClickListener
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
name|Widget
name|sender
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
literal|1
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|login
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
DECL|method|createSignIn (final String identUrl, final String prompt, final AbstractImagePrototype icon)
specifier|private
name|void
name|createSignIn
parameter_list|(
specifier|final
name|String
name|identUrl
parameter_list|,
specifier|final
name|String
name|prompt
parameter_list|,
specifier|final
name|AbstractImagePrototype
name|icon
parameter_list|)
block|{
specifier|final
name|ClickListener
name|i
init|=
operator|new
name|ClickListener
argument_list|()
block|{
specifier|public
name|void
name|onClick
parameter_list|(
name|Widget
name|sender
parameter_list|)
block|{
name|providerUrl
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
literal|"gerrit-OpenID-directlink"
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|img
init|=
name|icon
operator|.
name|createImage
argument_list|()
decl_stmt|;
name|img
operator|.
name|addClickListener
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
name|InlineLabel
name|lbl
init|=
operator|new
name|InlineLabel
argument_list|(
name|prompt
argument_list|)
decl_stmt|;
name|lbl
operator|.
name|addClickListener
argument_list|(
name|i
argument_list|)
expr_stmt|;
name|line
operator|.
name|add
argument_list|(
name|lbl
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
DECL|method|onSubmit (final FormSubmitEvent event)
specifier|public
name|void
name|onSubmit
parameter_list|(
specifier|final
name|FormSubmitEvent
name|event
parameter_list|)
block|{
specifier|final
name|String
name|url
init|=
name|providerUrl
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
operator|||
name|url
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|event
operator|.
name|setCancelled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|GWT
operator|.
name|isScript
argument_list|()
operator|||
operator|!
name|allowsIFRAME
operator|.
name|contains
argument_list|(
name|url
argument_list|)
condition|)
block|{
comment|// The hosted mode debugger chokes on our return redirect now,
comment|// and I cannot figure out why. So we use a top level page
comment|// instead, even if the site would have allowed it.
comment|//
comment|// Not all OpenID providers permit their login pages to be
comment|// embedded into an IFRAME. Only those that we know work
comment|// are permitted to stay inside of the IFRAME, everyone else
comment|// has to use this logic to replace the page with that of the
comment|// provider, and eventually redirect back to the same anchor.
comment|//
name|DOM
operator|.
name|setElementAttribute
argument_list|(
name|form
operator|.
name|getElement
argument_list|()
argument_list|,
literal|"target"
argument_list|,
literal|"_top"
argument_list|)
expr_stmt|;
name|form
operator|.
name|setMethod
argument_list|(
literal|"POST"
argument_list|)
expr_stmt|;
name|in_callback
operator|.
name|setValue
argument_list|(
literal|"history:"
operator|+
name|in_token
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|login
operator|.
name|setEnabled
argument_list|(
literal|false
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

