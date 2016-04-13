begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|api
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
name|info
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
name|gerrit
operator|.
name|client
operator|.
name|info
operator|.
name|GeneralPreferences
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
name|info
operator|.
name|ServerInfo
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
name|core
operator|.
name|client
operator|.
name|JsArray
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
name|Window
import|;
end_import

begin_class
DECL|class|ApiGlue
specifier|public
class|class
name|ApiGlue
block|{
DECL|field|pluginName
specifier|private
specifier|static
name|String
name|pluginName
decl_stmt|;
DECL|method|init ()
specifier|public
specifier|static
name|void
name|init
parameter_list|()
block|{
name|init0
argument_list|()
expr_stmt|;
name|ActionContext
operator|.
name|init
argument_list|()
expr_stmt|;
name|HtmlTemplate
operator|.
name|init
argument_list|()
expr_stmt|;
name|Plugin
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|init0 ()
specifier|private
specifier|static
specifier|native
name|void
name|init0
parameter_list|()
comment|/*-{     var serverUrl = @com.google.gwt.core.client.GWT::getHostPageBaseURL()();     var ScreenDefinition = @com.google.gerrit.client.api.ExtensionScreen.Definition::TYPE;     var SettingsScreenDefinition = @com.google.gerrit.client.api.ExtensionSettingsScreen.Definition::TYPE;     var PanelDefinition = @com.google.gerrit.client.api.ExtensionPanel.Definition::TYPE;     $wnd.Gerrit = {       JsonString: @com.google.gerrit.client.rpc.NativeString::TYPE,       events: {},       plugins: {},       screens: {},       settingsScreens: {},       panels: {},       change_actions: {},       edit_actions: {},       revision_actions: {},       project_actions: {},       branch_actions: {},        getPluginName: @com.google.gerrit.client.api.ApiGlue::getPluginName(),       injectCss: @com.google.gwt.dom.client.StyleInjector::inject(Ljava/lang/String;),       install: function (f) {         var p = this._getPluginByUrl(@com.google.gerrit.client.api.PluginName::getCallerUrl()());         @com.google.gerrit.client.api.ApiGlue::install(             Lcom/google/gwt/core/client/JavaScriptObject;             Lcom/google/gerrit/client/api/Plugin;)           (f,p);       },       installGwt: function(u){return this._getPluginByUrl(u)},       _getPluginByUrl: function(u) {         return u.indexOf(serverUrl) == 0           ? this.plugins[u.substring(serverUrl.length)]           : this.plugins[u]       },        go: @com.google.gerrit.client.api.ApiGlue::go(Ljava/lang/String;),       refresh: @com.google.gerrit.client.api.ApiGlue::refresh(),       refreshMenuBar: @com.google.gerrit.client.api.ApiGlue::refreshMenuBar(),       isSignedIn: @com.google.gerrit.client.api.ApiGlue::isSignedIn(),       showError: @com.google.gerrit.client.api.ApiGlue::showError(Ljava/lang/String;),       getServerInfo: @com.google.gerrit.client.api.ApiGlue::getServerInfo(),       getCurrentUser: @com.google.gerrit.client.api.ApiGlue::getCurrentUser(),       getUserPreferences: @com.google.gerrit.client.api.ApiGlue::getUserPreferences(),       refreshUserPreferences: @com.google.gerrit.client.api.ApiGlue::refreshUserPreferences(),        on: function (e,f){(this.events[e] || (this.events[e]=[])).push(f)},       onAction: function (t,n,c){this._onAction(this.getPluginName(),t,n,c)},       _onAction: function (p,t,n,c) {         var i = p+'~'+n;         if ('change' == t) this.change_actions[i]=c;         else if ('edit' == t) this.edit_actions[i]=c;         else if ('revision' == t) this.revision_actions[i]=c;         else if ('project' == t) this.project_actions[i]=c;         else if ('branch' == t) this.branch_actions[i]=c;         else if ('screen' == t) _screen(p,t,c);       },       screen: function(r,c){this._screen(this.getPluginName(),r,c)},       _screen: function(p,r,c){         var s = new ScreenDefinition(r,c);         (this.screens[p] || (this.screens[p]=[])).push(s);       },       settingsScreen: function(p,m,c){this._settingsScreen(this.getPluginName(),p,m,c)},       _settingsScreen: function(n,p,m,c){         var s = new SettingsScreenDefinition(p,m,c);         (this.settingsScreens[n] || (this.settingsScreens[n]=[])).push(s);       },       panel: function(i,c){this._panel(this.getPluginName(),i,c)},       _panel: function(n,i,c){         var p = new PanelDefinition(n,c);         (this.panels[i] || (this.panels[i]=[])).push(p);       },        url: function (d) {         if (d&& d.length> 0)           return serverUrl + (d.charAt(0)=='/' ? d.substring(1) : d);         return serverUrl;       },        _api: function(u) {         return @com.google.gerrit.client.rpc.RestApi::new(Ljava/lang/String;)(u);       },       get: function(u,b) {         @com.google.gerrit.client.api.ActionContext::get(             Lcom/google/gerrit/client/rpc/RestApi;             Lcom/google/gwt/core/client/JavaScriptObject;)           (this._api(u), b);       },       get_raw: function(u,b) {         @com.google.gerrit.client.api.ActionContext::getRaw(             Lcom/google/gerrit/client/rpc/RestApi;             Lcom/google/gwt/core/client/JavaScriptObject;)           (this._api(u), b);       },       post: function(u,i,b) {         if (typeof i == 'string') {           @com.google.gerrit.client.api.ActionContext::post(               Lcom/google/gerrit/client/rpc/RestApi;               Ljava/lang/String;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i, b);         } else {           @com.google.gerrit.client.api.ActionContext::post(               Lcom/google/gerrit/client/rpc/RestApi;               Lcom/google/gwt/core/client/JavaScriptObject;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i, b);         }       },       post_raw: function(u,i,b) {         if (typeof i == 'string') {           @com.google.gerrit.client.api.ActionContext::postRaw(               Lcom/google/gerrit/client/rpc/RestApi;               Ljava/lang/String;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i, b);         } else {           @com.google.gerrit.client.api.ActionContext::postRaw(               Lcom/google/gerrit/client/rpc/RestApi;               Lcom/google/gwt/core/client/JavaScriptObject;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i, b);         }       },       put: function(u,i,b) {         if (b) {           if (typeof i == 'string') {             @com.google.gerrit.client.api.ActionContext::put(                 Lcom/google/gerrit/client/rpc/RestApi;                 Ljava/lang/String;                 Lcom/google/gwt/core/client/JavaScriptObject;)               (this._api(u), i, b);           } else {             @com.google.gerrit.client.api.ActionContext::put(                 Lcom/google/gerrit/client/rpc/RestApi;                 Lcom/google/gwt/core/client/JavaScriptObject;                 Lcom/google/gwt/core/client/JavaScriptObject;)               (this._api(u), i, b);           }         } else {           @com.google.gerrit.client.api.ActionContext::put(               Lcom/google/gerrit/client/rpc/RestApi;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i);         }       },       put_raw: function(u,i,b) {         if (b) {           if (typeof i == 'string') {             @com.google.gerrit.client.api.ActionContext::putRaw(                 Lcom/google/gerrit/client/rpc/RestApi;                 Ljava/lang/String;                 Lcom/google/gwt/core/client/JavaScriptObject;)               (this._api(u), i, b);           } else {             @com.google.gerrit.client.api.ActionContext::putRaw(                 Lcom/google/gerrit/client/rpc/RestApi;                 Lcom/google/gwt/core/client/JavaScriptObject;                 Lcom/google/gwt/core/client/JavaScriptObject;)               (this._api(u), i, b);           }         } else {           @com.google.gerrit.client.api.ActionContext::putRaw(               Lcom/google/gerrit/client/rpc/RestApi;               Lcom/google/gwt/core/client/JavaScriptObject;)             (this._api(u), i);         }       },       'delete': function(u,b) {         @com.google.gerrit.client.api.ActionContext::delete(             Lcom/google/gerrit/client/rpc/RestApi;             Lcom/google/gwt/core/client/JavaScriptObject;)           (this._api(u), b);       },       del: function(u,b) {         @com.google.gerrit.client.api.ActionContext::delete(             Lcom/google/gerrit/client/rpc/RestApi;             Lcom/google/gwt/core/client/JavaScriptObject;)           (this._api(u), b);       },       del_raw: function(u,b) {         @com.google.gerrit.client.api.ActionContext::deleteRaw(             Lcom/google/gerrit/client/rpc/RestApi;             Lcom/google/gwt/core/client/JavaScriptObject;)           (this._api(u), b);       },     };   }-*/
function_decl|;
DECL|method|install (JavaScriptObject cb, Plugin p)
specifier|private
specifier|static
name|void
name|install
parameter_list|(
name|JavaScriptObject
name|cb
parameter_list|,
name|Plugin
name|p
parameter_list|)
throws|throws
name|Exception
block|{
try|try
block|{
name|pluginName
operator|=
name|p
operator|.
name|name
argument_list|()
expr_stmt|;
name|invoke
argument_list|(
name|cb
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|p
operator|.
name|_initialized
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|p
operator|.
name|failure
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|pluginName
operator|=
literal|null
expr_stmt|;
name|PluginLoader
operator|.
name|loaded
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getPluginName ()
specifier|private
specifier|static
name|String
name|getPluginName
parameter_list|()
block|{
if|if
condition|(
name|pluginName
operator|!=
literal|null
condition|)
block|{
return|return
name|pluginName
return|;
block|}
return|return
name|PluginName
operator|.
name|fromUrl
argument_list|(
name|PluginName
operator|.
name|getCallerUrl
argument_list|()
argument_list|)
return|;
block|}
DECL|method|go (String urlOrToken)
specifier|private
specifier|static
name|void
name|go
parameter_list|(
name|String
name|urlOrToken
parameter_list|)
block|{
if|if
condition|(
name|urlOrToken
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|urlOrToken
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
operator|||
name|urlOrToken
operator|.
name|startsWith
argument_list|(
literal|"//"
argument_list|)
condition|)
block|{
name|Window
operator|.
name|Location
operator|.
name|assign
argument_list|(
name|urlOrToken
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|urlOrToken
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|refresh ()
specifier|private
specifier|static
name|void
name|refresh
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|History
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getServerInfo ()
specifier|private
specifier|static
name|ServerInfo
name|getServerInfo
parameter_list|()
block|{
return|return
name|Gerrit
operator|.
name|info
argument_list|()
return|;
block|}
DECL|method|getCurrentUser ()
specifier|private
specifier|static
name|AccountInfo
name|getCurrentUser
parameter_list|()
block|{
return|return
name|Gerrit
operator|.
name|getUserAccount
argument_list|()
return|;
block|}
DECL|method|getUserPreferences ()
specifier|private
specifier|static
name|GeneralPreferences
name|getUserPreferences
parameter_list|()
block|{
return|return
name|Gerrit
operator|.
name|getUserPreferences
argument_list|()
return|;
block|}
DECL|method|refreshUserPreferences ()
specifier|private
specifier|static
name|void
name|refreshUserPreferences
parameter_list|()
block|{
name|Gerrit
operator|.
name|refreshUserPreferences
argument_list|()
expr_stmt|;
block|}
DECL|method|refreshMenuBar ()
specifier|private
specifier|static
name|void
name|refreshMenuBar
parameter_list|()
block|{
name|Gerrit
operator|.
name|refreshMenuBar
argument_list|()
expr_stmt|;
block|}
DECL|method|isSignedIn ()
specifier|private
specifier|static
name|boolean
name|isSignedIn
parameter_list|()
block|{
return|return
name|Gerrit
operator|.
name|isSignedIn
argument_list|()
return|;
block|}
DECL|method|showError (String message)
specifier|private
specifier|static
name|void
name|showError
parameter_list|(
name|String
name|message
parameter_list|)
block|{
operator|new
name|ErrorDialog
argument_list|(
name|message
argument_list|)
operator|.
name|center
argument_list|()
expr_stmt|;
block|}
DECL|method|invoke (JavaScriptObject f)
specifier|static
specifier|final
specifier|native
name|void
name|invoke
parameter_list|(
name|JavaScriptObject
name|f
parameter_list|)
comment|/*-{ f(); }-*/
function_decl|;
DECL|method|invoke (JavaScriptObject f, JavaScriptObject a)
specifier|static
specifier|final
specifier|native
name|void
name|invoke
parameter_list|(
name|JavaScriptObject
name|f
parameter_list|,
name|JavaScriptObject
name|a
parameter_list|)
comment|/*-{ f(a); }-*/
function_decl|;
DECL|method|invoke (JavaScriptObject f, JavaScriptObject a, JavaScriptObject b)
specifier|static
specifier|final
specifier|native
name|void
name|invoke
parameter_list|(
name|JavaScriptObject
name|f
parameter_list|,
name|JavaScriptObject
name|a
parameter_list|,
name|JavaScriptObject
name|b
parameter_list|)
comment|/*-{ f(a,b) }-*/
function_decl|;
DECL|method|invoke (JavaScriptObject f, String a)
specifier|static
specifier|final
specifier|native
name|void
name|invoke
parameter_list|(
name|JavaScriptObject
name|f
parameter_list|,
name|String
name|a
parameter_list|)
comment|/*-{ f(a); }-*/
function_decl|;
DECL|method|fireEvent (String event, String a)
specifier|public
specifier|static
specifier|final
name|void
name|fireEvent
parameter_list|(
name|String
name|event
parameter_list|,
name|String
name|a
parameter_list|)
block|{
name|JsArray
argument_list|<
name|JavaScriptObject
argument_list|>
name|h
init|=
name|getEventHandlers
argument_list|(
name|event
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|h
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|invoke
argument_list|(
name|h
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|a
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fireEvent (String event, Element e)
specifier|public
specifier|static
specifier|final
name|void
name|fireEvent
parameter_list|(
name|String
name|event
parameter_list|,
name|Element
name|e
parameter_list|)
block|{
name|JsArray
argument_list|<
name|JavaScriptObject
argument_list|>
name|h
init|=
name|getEventHandlers
argument_list|(
name|event
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|h
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|invoke
argument_list|(
name|h
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fireEvent (String event, JavaScriptObject a, JavaScriptObject b)
specifier|static
specifier|final
name|void
name|fireEvent
parameter_list|(
name|String
name|event
parameter_list|,
name|JavaScriptObject
name|a
parameter_list|,
name|JavaScriptObject
name|b
parameter_list|)
block|{
name|JsArray
argument_list|<
name|JavaScriptObject
argument_list|>
name|h
init|=
name|getEventHandlers
argument_list|(
name|event
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|h
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|invoke
argument_list|(
name|h
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|a
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getEventHandlers (String e)
specifier|static
specifier|final
specifier|native
name|JsArray
argument_list|<
name|JavaScriptObject
argument_list|>
name|getEventHandlers
parameter_list|(
name|String
name|e
parameter_list|)
comment|/*-{ return $wnd.Gerrit.events[e] || [] }-*/
function_decl|;
DECL|method|ApiGlue ()
specifier|private
name|ApiGlue
parameter_list|()
block|{   }
block|}
end_class

end_unit

