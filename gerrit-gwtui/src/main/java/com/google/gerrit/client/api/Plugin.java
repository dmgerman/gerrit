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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_class
DECL|class|Plugin
specifier|final
class|class
name|Plugin
extends|extends
name|JavaScriptObject
block|{
DECL|field|TYPE
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|TYPE
init|=
name|createType
argument_list|()
decl_stmt|;
DECL|method|create (String url)
specifier|static
name|Plugin
name|create
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|int
name|s
init|=
literal|"plugins/"
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|e
init|=
name|url
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|url
operator|.
name|substring
argument_list|(
name|s
argument_list|,
name|e
argument_list|)
decl_stmt|;
return|return
name|create
argument_list|(
name|TYPE
argument_list|,
name|url
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|url ()
specifier|final
specifier|native
name|String
name|url
parameter_list|()
comment|/*-{ return this._scriptUrl }-*/
function_decl|;
DECL|method|name ()
specifier|final
specifier|native
name|String
name|name
parameter_list|()
comment|/*-{ return this.name }-*/
function_decl|;
DECL|method|loaded ()
specifier|final
specifier|native
name|boolean
name|loaded
parameter_list|()
comment|/*-{ return this._success || this._failure != null }-*/
function_decl|;
DECL|method|failure ()
specifier|final
specifier|native
name|Exception
name|failure
parameter_list|()
comment|/*-{ return this._failure }-*/
function_decl|;
DECL|method|failure (Exception e)
specifier|final
specifier|native
name|void
name|failure
parameter_list|(
name|Exception
name|e
parameter_list|)
comment|/*-{ this._failure = e }-*/
function_decl|;
DECL|method|success ()
specifier|final
specifier|native
name|boolean
name|success
parameter_list|()
comment|/*-{ return this._success || false }-*/
function_decl|;
DECL|method|_initialized ()
specifier|final
specifier|native
name|void
name|_initialized
parameter_list|()
comment|/*-{ this._success = true }-*/
function_decl|;
DECL|method|create (JavaScriptObject T, String u, String n)
specifier|private
specifier|static
specifier|native
name|Plugin
name|create
parameter_list|(
name|JavaScriptObject
name|T
parameter_list|,
name|String
name|u
parameter_list|,
name|String
name|n
parameter_list|)
comment|/*-{ return new T(u,n) }-*/
function_decl|;
DECL|method|createType ()
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|createType
parameter_list|()
comment|/*-{     function Plugin(u, n) {       this._scriptUrl = u;       this.name = n;     }     return Plugin;   }-*/
function_decl|;
DECL|method|init ()
specifier|static
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{     var G = $wnd.Gerrit;     @com.google.gerrit.client.api.Plugin::TYPE.prototype = {       getPluginName: function(){return this.name},       go: @com.google.gerrit.client.api.ApiGlue::go(Ljava/lang/String;),       refresh: @com.google.gerrit.client.api.ApiGlue::refresh(),       on: G.on,       onAction: function(t,n,c){G._onAction(this.name,t,n,c)},        url: function (u){return G.url(this._url(u))},       get: function(u,b){@com.google.gerrit.client.api.ActionContext::get(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},       post: function(u,i,b){@com.google.gerrit.client.api.ActionContext::post(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       put: function(u,i,b){@com.google.gerrit.client.api.ActionContext::put(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),i,b)},       'delete': function(u,b){@com.google.gerrit.client.api.ActionContext::delete(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._api(u),b)},        _loadedGwt: function(){@com.google.gerrit.client.api.PluginLoader::loaded()()},       _api: function(u){return @com.google.gerrit.client.rpc.RestApi::new(Ljava/lang/String;)(this._url(u))},       _url: function (d) {         var u = 'plugins/' + this.name + '/';         if (d&& d.length> 0)           return u + (d.charAt(0)=='/' ? d.substring(1) : d);         return u;       },     };   }-*/
function_decl|;
DECL|method|Plugin ()
specifier|protected
name|Plugin
parameter_list|()
block|{   }
block|}
end_class

end_unit

