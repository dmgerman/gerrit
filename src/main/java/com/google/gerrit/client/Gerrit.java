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
DECL|package|com.google.gerrit.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|gerrit
operator|.
name|client
operator|.
name|data
operator|.
name|GerritConfig
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
name|data
operator|.
name|SystemInfoService
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
name|reviewdb
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
name|gerrit
operator|.
name|client
operator|.
name|reviewdb
operator|.
name|SystemConfig
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
name|Common
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
name|LinkMenuBar
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
name|LinkMenuItem
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
name|Screen
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
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
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
name|History
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
name|HistoryListener
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
name|rpc
operator|.
name|AsyncCallback
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
name|MenuBar
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
name|MenuItem
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
name|gwtexpui
operator|.
name|user
operator|.
name|client
operator|.
name|UserAgent
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
name|ViewSite
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
name|client
operator|.
name|JsonUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_class
DECL|class|Gerrit
specifier|public
class|class
name|Gerrit
implements|implements
name|EntryPoint
block|{
comment|/**    * Name of the Cookie our authentication data is stored in.    *<p>    * If this cookie has a value we assume we are signed in.    *     * @see #isSignedIn()    */
DECL|field|ACCOUNT_COOKIE
specifier|public
specifier|static
specifier|final
name|String
name|ACCOUNT_COOKIE
init|=
literal|"GerritAccount"
decl_stmt|;
DECL|field|C
specifier|public
specifier|static
specifier|final
name|GerritConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|GerritConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|M
specifier|public
specifier|static
specifier|final
name|GerritMessages
name|M
init|=
name|GWT
operator|.
name|create
argument_list|(
name|GerritMessages
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ICONS
specifier|public
specifier|static
specifier|final
name|GerritIcons
name|ICONS
init|=
name|GWT
operator|.
name|create
argument_list|(
name|GerritIcons
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SYSTEM_SVC
specifier|public
specifier|static
specifier|final
name|SystemInfoService
name|SYSTEM_SVC
decl_stmt|;
DECL|field|myHost
specifier|private
specifier|static
name|String
name|myHost
decl_stmt|;
DECL|field|myVersion
specifier|private
specifier|static
name|String
name|myVersion
decl_stmt|;
DECL|field|myAccount
specifier|private
specifier|static
name|Account
name|myAccount
decl_stmt|;
DECL|field|signedInListeners
specifier|private
specifier|static
specifier|final
name|ArrayList
argument_list|<
name|SignedInListener
argument_list|>
name|signedInListeners
init|=
operator|new
name|ArrayList
argument_list|<
name|SignedInListener
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|menuBar
specifier|private
specifier|static
name|LinkMenuBar
name|menuBar
decl_stmt|;
DECL|field|siteHeader
specifier|private
specifier|static
name|RootPanel
name|siteHeader
decl_stmt|;
DECL|field|siteFooter
specifier|private
specifier|static
name|RootPanel
name|siteFooter
decl_stmt|;
DECL|field|body
specifier|private
specifier|static
name|ViewSite
argument_list|<
name|Screen
argument_list|>
name|body
decl_stmt|;
static|static
block|{
name|SYSTEM_SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|SystemInfoService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|SYSTEM_SVC
argument_list|,
literal|"rpc/SystemInfoService"
argument_list|)
expr_stmt|;
block|}
DECL|method|display (final String historyToken, final boolean go)
specifier|public
specifier|static
name|void
name|display
parameter_list|(
specifier|final
name|String
name|historyToken
parameter_list|,
specifier|final
name|boolean
name|go
parameter_list|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|historyToken
argument_list|,
name|go
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|go
operator|&&
name|historyHooks
operator|!=
literal|null
condition|)
block|{
name|dispatchHistoryHooks
argument_list|(
name|historyToken
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final String historyToken, final Screen view)
specifier|public
specifier|static
name|void
name|display
parameter_list|(
specifier|final
name|String
name|historyToken
parameter_list|,
specifier|final
name|Screen
name|view
parameter_list|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|historyToken
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|display
argument_list|(
name|view
argument_list|)
expr_stmt|;
if|if
condition|(
name|historyHooks
operator|!=
literal|null
condition|)
block|{
name|dispatchHistoryHooks
argument_list|(
name|historyToken
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|display (final Screen view)
specifier|public
specifier|static
name|void
name|display
parameter_list|(
specifier|final
name|Screen
name|view
parameter_list|)
block|{
if|if
condition|(
name|view
operator|.
name|isRequiresSignIn
argument_list|()
operator|&&
operator|!
name|isSignedIn
argument_list|()
condition|)
block|{
name|doSignIn
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|body
operator|.
name|setView
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setWindowTitle (final Screen screen, final String text)
specifier|public
specifier|static
name|void
name|setWindowTitle
parameter_list|(
specifier|final
name|Screen
name|screen
parameter_list|,
specifier|final
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|screen
operator|==
name|body
operator|.
name|getView
argument_list|()
condition|)
block|{
if|if
condition|(
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|Window
operator|.
name|setTitle
argument_list|(
name|M
operator|.
name|windowTitle1
argument_list|(
name|myHost
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Window
operator|.
name|setTitle
argument_list|(
name|M
operator|.
name|windowTitle2
argument_list|(
name|text
argument_list|,
name|myHost
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/** @return the currently signed in user's account data; null if no account */
DECL|method|getUserAccount ()
specifier|public
specifier|static
name|Account
name|getUserAccount
parameter_list|()
block|{
return|return
name|myAccount
return|;
block|}
comment|/** @return true if the user is currently authenticated */
DECL|method|isSignedIn ()
specifier|public
specifier|static
name|boolean
name|isSignedIn
parameter_list|()
block|{
return|return
name|getUserAccount
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**    * Sign the user into the application.    */
DECL|method|doSignIn ()
specifier|public
specifier|static
name|void
name|doSignIn
parameter_list|()
block|{
operator|new
name|SignInDialog
argument_list|()
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
comment|/** Sign the user out of the application (and discard the cookies). */
DECL|method|doSignOut ()
specifier|public
specifier|static
name|void
name|doSignOut
parameter_list|()
block|{
name|myAccount
operator|=
literal|null
expr_stmt|;
name|Cookies
operator|.
name|removeCookie
argument_list|(
name|ACCOUNT_COOKIE
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|SignedInListener
name|l
range|:
name|signedInListeners
control|)
block|{
name|l
operator|.
name|onSignOut
argument_list|()
expr_stmt|;
block|}
name|refreshMenuBar
argument_list|()
expr_stmt|;
specifier|final
name|Screen
name|cs
init|=
name|body
operator|.
name|getView
argument_list|()
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|cs
operator|.
name|onSignOut
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Add a listener to monitor sign-in status. */
DECL|method|addSignedInListener (final SignedInListener l)
specifier|public
specifier|static
name|void
name|addSignedInListener
parameter_list|(
specifier|final
name|SignedInListener
name|l
parameter_list|)
block|{
if|if
condition|(
operator|!
name|signedInListeners
operator|.
name|contains
argument_list|(
name|l
argument_list|)
condition|)
block|{
name|signedInListeners
operator|.
name|add
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Remove a previously added sign in listener. */
DECL|method|removeSignedInListener (final SignedInListener l)
specifier|public
specifier|static
name|void
name|removeSignedInListener
parameter_list|(
specifier|final
name|SignedInListener
name|l
parameter_list|)
block|{
name|signedInListeners
operator|.
name|remove
argument_list|(
name|l
argument_list|)
expr_stmt|;
block|}
DECL|method|onModuleLoad ()
specifier|public
name|void
name|onModuleLoad
parameter_list|()
block|{
name|UserAgent
operator|.
name|assertNotInIFrame
argument_list|()
expr_stmt|;
name|initHostname
argument_list|()
expr_stmt|;
name|Window
operator|.
name|setTitle
argument_list|(
name|M
operator|.
name|windowTitle1
argument_list|(
name|myHost
argument_list|)
argument_list|)
expr_stmt|;
name|loadCSS
argument_list|()
expr_stmt|;
name|initHistoryHooks
argument_list|()
expr_stmt|;
name|populateBottomMenu
argument_list|()
expr_stmt|;
specifier|final
name|RootPanel
name|topMenu
init|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_topmenu"
argument_list|)
decl_stmt|;
name|menuBar
operator|=
operator|new
name|LinkMenuBar
argument_list|()
expr_stmt|;
name|topMenu
operator|.
name|add
argument_list|(
name|menuBar
argument_list|)
expr_stmt|;
name|siteHeader
operator|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_header"
argument_list|)
expr_stmt|;
name|siteFooter
operator|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_footer"
argument_list|)
expr_stmt|;
name|body
operator|=
operator|new
name|ViewSite
argument_list|<
name|Screen
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|protected
name|void
name|onShowView
parameter_list|(
name|Screen
name|view
parameter_list|)
block|{
name|super
operator|.
name|onShowView
argument_list|(
name|view
argument_list|)
expr_stmt|;
name|view
operator|.
name|onShowView
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_body"
argument_list|)
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|addRpcStatusListener
argument_list|(
operator|new
name|RpcStatus
argument_list|(
name|topMenu
argument_list|)
argument_list|)
expr_stmt|;
name|SYSTEM_SVC
operator|.
name|loadGerritConfig
argument_list|(
operator|new
name|GerritCallback
argument_list|<
name|GerritConfig
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|GerritConfig
name|result
parameter_list|)
block|{
name|Common
operator|.
name|setGerritConfig
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|onModuleLoad2
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|initHostname ()
specifier|private
specifier|static
name|void
name|initHostname
parameter_list|()
block|{
name|myHost
operator|=
name|Window
operator|.
name|Location
operator|.
name|getHostName
argument_list|()
expr_stmt|;
specifier|final
name|int
name|d1
init|=
name|myHost
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|d1
operator|<
literal|0
condition|)
block|{
return|return;
block|}
specifier|final
name|int
name|d2
init|=
name|myHost
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|,
name|d1
operator|+
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|d2
operator|>=
literal|0
condition|)
block|{
name|myHost
operator|=
name|myHost
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|d2
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|loadCSS ()
specifier|private
specifier|static
name|void
name|loadCSS
parameter_list|()
block|{
specifier|final
name|GerritCss
name|css
init|=
name|GWT
operator|.
name|create
argument_list|(
name|GerritCss
operator|.
name|class
argument_list|)
decl_stmt|;
name|css
operator|.
name|load
argument_list|()
operator|.
name|inject
argument_list|()
expr_stmt|;
block|}
DECL|field|historyHooks
specifier|private
specifier|static
name|ArrayList
argument_list|<
name|JavaScriptObject
argument_list|>
name|historyHooks
decl_stmt|;
DECL|method|initHistoryHooks ()
specifier|private
specifier|static
specifier|native
name|void
name|initHistoryHooks
parameter_list|()
comment|/*-{ $wnd['gerrit_addHistoryHook'] = function(h) { @com.google.gerrit.client.Gerrit::addHistoryHook(Lcom/google/gwt/core/client/JavaScriptObject;)(h); }; }-*/
function_decl|;
DECL|method|addHistoryHook (final JavaScriptObject hook)
specifier|static
name|void
name|addHistoryHook
parameter_list|(
specifier|final
name|JavaScriptObject
name|hook
parameter_list|)
block|{
if|if
condition|(
name|historyHooks
operator|==
literal|null
condition|)
block|{
name|historyHooks
operator|=
operator|new
name|ArrayList
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
expr_stmt|;
name|History
operator|.
name|addHistoryListener
argument_list|(
operator|new
name|HistoryListener
argument_list|()
block|{
specifier|public
name|void
name|onHistoryChanged
parameter_list|(
specifier|final
name|String
name|historyToken
parameter_list|)
block|{
name|dispatchHistoryHooks
argument_list|(
name|historyToken
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|historyHooks
operator|.
name|add
argument_list|(
name|hook
argument_list|)
expr_stmt|;
block|}
DECL|method|callHistoryHook (JavaScriptObject hook, String url)
specifier|private
specifier|static
specifier|native
name|void
name|callHistoryHook
parameter_list|(
name|JavaScriptObject
name|hook
parameter_list|,
name|String
name|url
parameter_list|)
comment|/*-{ hook(url); }-*/
function_decl|;
DECL|method|dispatchHistoryHooks (final String historyToken)
specifier|private
specifier|static
name|void
name|dispatchHistoryHooks
parameter_list|(
specifier|final
name|String
name|historyToken
parameter_list|)
block|{
specifier|final
name|String
name|url
init|=
name|Window
operator|.
name|Location
operator|.
name|getPath
argument_list|()
operator|+
literal|"#"
operator|+
name|historyToken
decl_stmt|;
for|for
control|(
specifier|final
name|JavaScriptObject
name|hook
range|:
name|historyHooks
control|)
block|{
name|callHistoryHook
argument_list|(
name|hook
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateBottomMenu ()
specifier|private
specifier|static
name|void
name|populateBottomMenu
parameter_list|()
block|{
specifier|final
name|RootPanel
name|btmmenu
init|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_btmmenu"
argument_list|)
decl_stmt|;
specifier|final
name|String
name|vs
init|=
name|getVersion
argument_list|()
decl_stmt|;
specifier|final
name|HTML
name|version
init|=
operator|new
name|HTML
argument_list|(
name|M
operator|.
name|poweredBy
argument_list|(
name|vs
argument_list|)
argument_list|)
decl_stmt|;
name|version
operator|.
name|setStyleName
argument_list|(
literal|"gerrit-version"
argument_list|)
expr_stmt|;
name|btmmenu
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
comment|/** @return version number of the Gerrit application software */
DECL|method|getVersion ()
specifier|public
specifier|static
name|String
name|getVersion
parameter_list|()
block|{
if|if
condition|(
name|myVersion
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|GWT
operator|.
name|isScript
argument_list|()
condition|)
block|{
specifier|final
name|GerritVersion
name|v
init|=
name|GWT
operator|.
name|create
argument_list|(
name|GerritVersion
operator|.
name|class
argument_list|)
decl_stmt|;
name|myVersion
operator|=
name|v
operator|.
name|version
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|myVersion
operator|=
literal|"dev"
expr_stmt|;
block|}
block|}
return|return
name|myVersion
return|;
block|}
DECL|method|onModuleLoad2 ()
specifier|private
name|void
name|onModuleLoad2
parameter_list|()
block|{
if|if
condition|(
name|Cookies
operator|.
name|getCookie
argument_list|(
name|ACCOUNT_COOKIE
argument_list|)
operator|!=
literal|null
operator|||
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
operator|==
name|SystemConfig
operator|.
name|LoginType
operator|.
name|HTTP
condition|)
block|{
comment|// If the user is likely to already be signed into their account,
comment|// load the account data and update the UI with that.
comment|//
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
name|Util
operator|.
name|ACCOUNT_SVC
operator|.
name|myAccount
argument_list|(
operator|new
name|AsyncCallback
argument_list|<
name|Account
argument_list|>
argument_list|()
block|{
specifier|public
name|void
name|onSuccess
parameter_list|(
specifier|final
name|Account
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|postSignIn
argument_list|(
name|result
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Cookies
operator|.
name|removeCookie
argument_list|(
name|ACCOUNT_COOKIE
argument_list|)
expr_stmt|;
name|refreshMenuBar
argument_list|()
expr_stmt|;
block|}
name|showInitialScreen
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
specifier|final
name|Throwable
name|caught
parameter_list|)
block|{
if|if
condition|(
operator|!
name|GWT
operator|.
name|isScript
argument_list|()
operator|&&
operator|!
name|GerritCallback
operator|.
name|isNotSignedIn
argument_list|(
name|caught
argument_list|)
condition|)
block|{
name|GWT
operator|.
name|log
argument_list|(
literal|"Unexpected failure from validating account"
argument_list|,
name|caught
argument_list|)
expr_stmt|;
block|}
name|Cookies
operator|.
name|removeCookie
argument_list|(
name|ACCOUNT_COOKIE
argument_list|)
expr_stmt|;
name|refreshMenuBar
argument_list|()
expr_stmt|;
name|showInitialScreen
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|refreshMenuBar
argument_list|()
expr_stmt|;
name|showInitialScreen
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|showInitialScreen ()
specifier|private
name|void
name|showInitialScreen
parameter_list|()
block|{
specifier|final
name|RootPanel
name|sg
init|=
name|RootPanel
operator|.
name|get
argument_list|(
literal|"gerrit_startinggerrit"
argument_list|)
decl_stmt|;
name|sg
operator|.
name|getElement
argument_list|()
operator|.
name|getParentElement
argument_list|()
operator|.
name|removeChild
argument_list|(
name|sg
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|RootPanel
operator|.
name|detachNow
argument_list|(
name|sg
argument_list|)
expr_stmt|;
name|History
operator|.
name|addHistoryListener
argument_list|(
operator|new
name|Link
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|History
operator|.
name|getToken
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|isSignedIn
argument_list|()
condition|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Link
operator|.
name|MINE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Link
operator|.
name|ALL_OPEN
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|History
operator|.
name|fireCurrentHistoryState
argument_list|()
expr_stmt|;
block|}
block|}
comment|/** Hook from {@link SignInDialog} to let us know to refresh the UI. */
DECL|method|postSignIn (final Account acct, final AsyncCallback<?> ac)
specifier|static
name|void
name|postSignIn
parameter_list|(
specifier|final
name|Account
name|acct
parameter_list|,
specifier|final
name|AsyncCallback
argument_list|<
name|?
argument_list|>
name|ac
parameter_list|)
block|{
name|myAccount
operator|=
name|acct
expr_stmt|;
name|refreshMenuBar
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|SignedInListener
name|l
range|:
name|signedInListeners
control|)
block|{
name|l
operator|.
name|onSignIn
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Screen
name|cs
init|=
name|body
operator|.
name|getView
argument_list|()
decl_stmt|;
if|if
condition|(
name|cs
operator|!=
literal|null
condition|)
block|{
name|cs
operator|.
name|onSignIn
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|ac
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|refreshMenuBar ()
specifier|public
specifier|static
name|void
name|refreshMenuBar
parameter_list|()
block|{
name|menuBar
operator|.
name|clearItems
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|signedIn
init|=
name|isSignedIn
argument_list|()
decl_stmt|;
name|MenuBar
name|m
decl_stmt|;
name|m
operator|=
operator|new
name|MenuBar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuAllOpen
argument_list|()
argument_list|,
name|Link
operator|.
name|ALL_OPEN
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuAllMerged
argument_list|()
argument_list|,
name|Link
operator|.
name|ALL_MERGED
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuAllAbandoned
argument_list|()
argument_list|,
name|Link
operator|.
name|ALL_ABANDONED
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|addItem
argument_list|(
name|C
operator|.
name|menuAll
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
if|if
condition|(
name|signedIn
condition|)
block|{
name|m
operator|=
operator|new
name|MenuBar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuMyChanges
argument_list|()
argument_list|,
name|Link
operator|.
name|MINE
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menyMyDrafts
argument_list|()
argument_list|,
name|Link
operator|.
name|MINE_DRAFTS
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuMyStarredChanges
argument_list|()
argument_list|,
name|Link
operator|.
name|MINE_STARRED
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuSettings
argument_list|()
argument_list|,
name|Link
operator|.
name|SETTINGS
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|addItem
argument_list|(
name|C
operator|.
name|menuMine
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|signedIn
condition|)
block|{
name|m
operator|=
operator|new
name|MenuBar
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuGroups
argument_list|()
argument_list|,
name|Link
operator|.
name|ADMIN_GROUPS
argument_list|)
expr_stmt|;
name|addLink
argument_list|(
name|m
argument_list|,
name|C
operator|.
name|menuProjects
argument_list|()
argument_list|,
name|Link
operator|.
name|ADMIN_PROJECTS
argument_list|)
expr_stmt|;
name|menuBar
operator|.
name|addItem
argument_list|(
name|C
operator|.
name|menuAdmin
argument_list|()
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
name|menuBar
operator|.
name|lastInGroup
argument_list|()
expr_stmt|;
name|menuBar
operator|.
name|addGlue
argument_list|()
expr_stmt|;
if|if
condition|(
name|signedIn
condition|)
block|{
name|whoAmI
argument_list|()
expr_stmt|;
name|menuBar
operator|.
name|addItem
argument_list|(
operator|new
name|LinkMenuItem
argument_list|(
name|C
operator|.
name|menuSettings
argument_list|()
argument_list|,
name|Link
operator|.
name|SETTINGS
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|signout
init|=
literal|false
decl_stmt|;
switch|switch
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|HTTP
case|:
break|break;
case|case
name|OPENID
case|:
default|default:
name|signout
operator|=
literal|true
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|signout
operator|||
operator|(
name|GWT
operator|.
name|isClient
argument_list|()
operator|&&
operator|!
name|GWT
operator|.
name|isScript
argument_list|()
operator|)
condition|)
block|{
name|menuBar
operator|.
name|addItem
argument_list|(
name|C
operator|.
name|menuSignOut
argument_list|()
argument_list|,
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|doSignOut
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
switch|switch
condition|(
name|Common
operator|.
name|getGerritConfig
argument_list|()
operator|.
name|getLoginType
argument_list|()
condition|)
block|{
case|case
name|HTTP
case|:
break|break;
case|case
name|OPENID
case|:
default|default:
name|menuBar
operator|.
name|addItem
argument_list|(
name|C
operator|.
name|menuSignIn
argument_list|()
argument_list|,
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|doSignIn
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|GWT
operator|.
name|isClient
argument_list|()
operator|&&
operator|!
name|GWT
operator|.
name|isScript
argument_list|()
condition|)
block|{
name|menuBar
operator|.
name|addItem
argument_list|(
literal|"Become"
argument_list|,
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|Window
operator|.
name|Location
operator|.
name|assign
argument_list|(
name|GWT
operator|.
name|getHostPageBaseURL
argument_list|()
operator|+
literal|"become"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|menuBar
operator|.
name|lastInGroup
argument_list|()
expr_stmt|;
specifier|final
name|boolean
name|view
init|=
name|myAccount
operator|==
literal|null
operator|||
name|myAccount
operator|.
name|isShowSiteHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|siteHeader
operator|!=
literal|null
condition|)
block|{
name|siteHeader
operator|.
name|setVisible
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|siteFooter
operator|!=
literal|null
condition|)
block|{
name|siteFooter
operator|.
name|setVisible
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|whoAmI ()
specifier|private
specifier|static
name|void
name|whoAmI
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
name|FormatUtil
operator|.
name|nameEmail
argument_list|(
name|getUserAccount
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MenuItem
name|me
init|=
name|menuBar
operator|.
name|addItem
argument_list|(
name|name
argument_list|,
operator|(
name|Command
operator|)
literal|null
argument_list|)
decl_stmt|;
name|me
operator|.
name|removeStyleName
argument_list|(
literal|"gwt-MenuItem"
argument_list|)
expr_stmt|;
name|me
operator|.
name|addStyleName
argument_list|(
literal|"gerrit-MenuBarUserName"
argument_list|)
expr_stmt|;
block|}
DECL|method|addLink (final MenuBar m, final String text, final String historyToken)
specifier|private
specifier|static
name|void
name|addLink
parameter_list|(
specifier|final
name|MenuBar
name|m
parameter_list|,
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|String
name|historyToken
parameter_list|)
block|{
name|m
operator|.
name|addItem
argument_list|(
operator|new
name|LinkMenuItem
argument_list|(
name|text
argument_list|,
name|historyToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

