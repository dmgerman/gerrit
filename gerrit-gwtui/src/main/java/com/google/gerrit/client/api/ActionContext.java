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
name|change
operator|.
name|ActionButton
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
name|changes
operator|.
name|ChangeInfo
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|ActionInfo
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
name|changes
operator|.
name|ChangeInfo
operator|.
name|RevisionInfo
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
name|rpc
operator|.
name|NativeString
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
name|RestApi
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

begin_class
DECL|class|ActionContext
specifier|public
class|class
name|ActionContext
extends|extends
name|JavaScriptObject
block|{
DECL|method|init ()
specifier|static
specifier|final
specifier|native
name|void
name|init
parameter_list|()
comment|/*-{     var Gerrit = $wnd.Gerrit;     var doc = $wnd.document;     var stopPropagation = function (e) {       if (e&& e.stopPropagation) e.stopPropagation();       else $wnd.event.cancelBubble = true;     };      Gerrit.ActionContext = function(u){this._u=u};     Gerrit.ActionContext.prototype = {       go: Gerrit.go,       refresh: Gerrit.refresh,        br: function(){return doc.createElement('br')},       hr: function(){return doc.createElement('hr')},       button: function(label, o) {         var e = doc.createElement('button');         e.appendChild(this.div(doc.createTextNode(label)));         if (o&& o.onclick) e.onclick = o.onclick;         return e;       },       checkbox: function() {         var e = doc.createElement('input');         e.type = 'checkbox';         return e;       },       div: function() {         var e = doc.createElement('div');         for (var i = 0; i< arguments.length; i++)           e.appendChild(arguments[i]);         return e;       },       label: function(c,label) {         var e = doc.createElement('label');         e.appendChild(c);         e.appendChild(doc.createTextNode(label));         return e;       },       prependLabel: function(label,c) {         var e = doc.createElement('label');         e.appendChild(doc.createTextNode(label));         e.appendChild(c);         return e;       },       span: function() {         var e = doc.createElement('span');         for (var i = 0; i< arguments.length; i++)           e.appendChild(arguments[i]);         return e;       },       msg: function(label) {         var e = doc.createElement('span');         e.appendChild(doc.createTextNode(label));         return e;       },       textarea: function(o) {         var e = doc.createElement('textarea');         e.onkeypress = stopPropagation;         if (o&& o.rows) e.rows = o.rows;         if (o&& o.cols) e.cols = o.cols;         return e;       },       textfield: function() {         var e = doc.createElement('input');         e.type = 'text';         e.onkeypress = stopPropagation;         return e;       },        popup: function(e){this._p=@com.google.gerrit.client.api.PopupHelper::popup(Lcom/google/gerrit/client/api/ActionContext;Lcom/google/gwt/dom/client/Element;)(this,e)},       hide: function() {         this._p.@com.google.gerrit.client.api.PopupHelper::hide()();         delete this['_p'];       },        call: function(i,b) {         var m = this.action.method.toLowerCase();         if (m == 'get' || m == 'delete' || i==null) this[m](b);         else this[m](i,b);       },       get: function(b){@com.google.gerrit.client.api.ActionContext::get(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._u,b)},       post: function(i,b){@com.google.gerrit.client.api.ActionContext::post(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._u,i,b)},       put: function(i,b){@com.google.gerrit.client.api.ActionContext::put(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/gwt/core/client/JavaScriptObject;)(this._u,i,b)},       'delete': function(b){@com.google.gerrit.client.api.ActionContext::delete(Lcom/google/gerrit/client/rpc/RestApi;Lcom/google/gwt/core/client/JavaScriptObject;)(this._u,b)},     };   }-*/
function_decl|;
DECL|method|create (RestApi f)
specifier|static
specifier|final
specifier|native
name|ActionContext
name|create
parameter_list|(
name|RestApi
name|f
parameter_list|)
comment|/*-{     return new $wnd.Gerrit.ActionContext(f);   }-*/
function_decl|;
DECL|method|set (ActionInfo a)
specifier|final
specifier|native
name|void
name|set
parameter_list|(
name|ActionInfo
name|a
parameter_list|)
comment|/*-{ this.action=a; }-*/
function_decl|;
DECL|method|set (ChangeInfo c)
specifier|final
specifier|native
name|void
name|set
parameter_list|(
name|ChangeInfo
name|c
parameter_list|)
comment|/*-{ this.change=c; }-*/
function_decl|;
DECL|method|set (RevisionInfo r)
specifier|final
specifier|native
name|void
name|set
parameter_list|(
name|RevisionInfo
name|r
parameter_list|)
comment|/*-{ this.revision=r; }-*/
function_decl|;
DECL|method|button (ActionButton b)
specifier|final
specifier|native
name|void
name|button
parameter_list|(
name|ActionButton
name|b
parameter_list|)
comment|/*-{ this._b=b; }-*/
function_decl|;
DECL|method|button ()
specifier|final
specifier|native
name|ActionButton
name|button
parameter_list|()
comment|/*-{ return this._b; }-*/
function_decl|;
DECL|method|has_popup ()
specifier|public
specifier|final
specifier|native
name|boolean
name|has_popup
parameter_list|()
comment|/*-{ return this.hasOwnProperty('_p') }-*/
function_decl|;
DECL|method|hide ()
specifier|public
specifier|final
specifier|native
name|void
name|hide
parameter_list|()
comment|/*-{ this.hide(); }-*/
function_decl|;
DECL|method|ActionContext ()
specifier|protected
name|ActionContext
parameter_list|()
block|{   }
DECL|method|get (RestApi api, JavaScriptObject cb)
specifier|static
specifier|final
name|void
name|get
parameter_list|(
name|RestApi
name|api
parameter_list|,
name|JavaScriptObject
name|cb
parameter_list|)
block|{
name|api
operator|.
name|get
argument_list|(
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|post (RestApi api, JavaScriptObject in, JavaScriptObject cb)
specifier|static
specifier|final
name|void
name|post
parameter_list|(
name|RestApi
name|api
parameter_list|,
name|JavaScriptObject
name|in
parameter_list|,
name|JavaScriptObject
name|cb
parameter_list|)
block|{
name|api
operator|.
name|post
argument_list|(
name|in
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|put (RestApi api, JavaScriptObject in, JavaScriptObject cb)
specifier|static
specifier|final
name|void
name|put
parameter_list|(
name|RestApi
name|api
parameter_list|,
name|JavaScriptObject
name|in
parameter_list|,
name|JavaScriptObject
name|cb
parameter_list|)
block|{
name|api
operator|.
name|put
argument_list|(
name|in
argument_list|,
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (RestApi api, JavaScriptObject cb)
specifier|static
specifier|final
name|void
name|delete
parameter_list|(
name|RestApi
name|api
parameter_list|,
name|JavaScriptObject
name|cb
parameter_list|)
block|{
name|api
operator|.
name|delete
argument_list|(
name|wrap
argument_list|(
name|cb
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|wrap (final JavaScriptObject cb)
specifier|private
specifier|static
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
name|wrap
parameter_list|(
specifier|final
name|JavaScriptObject
name|cb
parameter_list|)
block|{
return|return
operator|new
name|GerritCallback
argument_list|<
name|JavaScriptObject
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onSuccess
parameter_list|(
name|JavaScriptObject
name|result
parameter_list|)
block|{
if|if
condition|(
name|NativeString
operator|.
name|is
argument_list|(
name|result
argument_list|)
condition|)
block|{
name|NativeString
name|s
init|=
name|result
operator|.
name|cast
argument_list|()
decl_stmt|;
name|ApiGlue
operator|.
name|invoke
argument_list|(
name|cb
argument_list|,
name|s
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ApiGlue
operator|.
name|invoke
argument_list|(
name|cb
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

