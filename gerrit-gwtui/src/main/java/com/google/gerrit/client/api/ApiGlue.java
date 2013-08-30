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
name|Gerrit
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
block|}
DECL|method|init0 ()
specifier|private
specifier|static
specifier|native
name|void
name|init0
parameter_list|()
comment|/*-{     var serverUrl = @com.google.gwt.core.client.GWT::getHostPageBaseURL()();     var Plugin = function (name){this.name = name};     var Gerrit = {       getPluginName: @com.google.gerrit.client.api.ApiGlue::getPluginName(),       install: function (f) {         var p = new Plugin(this.getPluginName());         @com.google.gerrit.client.api.ApiGlue::install(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(f,p);       },        go: @com.google.gerrit.client.api.ApiGlue::go(Ljava/lang/String;),       refresh: @com.google.gerrit.client.api.ApiGlue::refresh(),        change_actions: {},       revision_actions: {},       project_actions: {},       onAction: function (t,n,c){this._onAction(this.getPluginName(),t,n,c)},       _onAction: function (p,t,n,c) {         var i = p+'~'+n;         if ('change' == t) this.change_actions[i]=c;         else if ('revision' == t) this.revision_actions[i]=c;         else if ('project' == t) this.project_actions[i]=c;       },        url: function (d) {         if (d&& d.length> 0)           return serverUrl + (d.charAt(0)=='/' ? d.substring(1) : d);         return serverUrl;       },        _api: function(u) {return @com.google.gerrit.client.rpc.RestApi::new(Ljava/lang/String;)(u)},       get: function(u,b){@com.google.gerrit.client.api.ActionContext::get(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},       post: function(u,i,b){@com.google.gerrit.client.api.ActionContext::post(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       put: function(u,i,b){@com.google.gerrit.client.api.ActionContext::put(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       'delete': function(u,b){@com.google.gerrit.client.api.ActionContext::delete(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},     };      Plugin.prototype = {       getPluginName: function(){return this.name},       go: @com.google.gerrit.client.api.ApiGlue::go(Ljava/lang/String;),       refresh: Gerrit.refresh,       onAction: function(t,n,c) {Gerrit._onAction(this.name,t,n,c)},        url: function (d) {         var u = serverUrl + 'plugins/' + this.name + '/';         if (d&& d.length> 0) u += d.charAt(0)=='/' ? d.substring(1) : d;         return u;       },        _api: function(d) {         var u = 'plugins/' + this.name + '/';         if (d&& d.length> 0) u += d.charAt(0)=='/' ? d.substring(1) : d;         return @com.google.gerrit.client.rpc.RestApi::new(Ljava/lang/String;)(u);       },        get: function(u,b){@com.google.gerrit.client.api.ActionContext::get(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},       post: function(u,i,b){@com.google.gerrit.client.api.ActionContext::post(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       put: function(u,i,b){@com.google.gerrit.client.api.ActionContext::put(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       'delete': function(u,b){@com.google.gerrit.client.api.ActionContext::delete(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},     };      $wnd.Gerrit = Gerrit;   }-*/
function_decl|;
DECL|method|install (JavaScriptObject cb, JavaScriptObject p)
specifier|private
specifier|static
name|void
name|install
parameter_list|(
name|JavaScriptObject
name|cb
parameter_list|,
name|JavaScriptObject
name|p
parameter_list|)
block|{
try|try
block|{
name|pluginName
operator|=
name|PluginName
operator|.
name|get
argument_list|()
expr_stmt|;
name|invoke
argument_list|(
name|cb
argument_list|,
name|p
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|pluginName
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getPluginName ()
specifier|private
specifier|static
specifier|final
name|String
name|getPluginName
parameter_list|()
block|{
return|return
name|pluginName
operator|!=
literal|null
condition|?
name|pluginName
else|:
name|PluginName
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|go (String urlOrToken)
specifier|private
specifier|static
specifier|final
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
specifier|final
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
DECL|method|ApiGlue ()
specifier|private
name|ApiGlue
parameter_list|()
block|{   }
block|}
end_class

end_unit

