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
name|rpc
operator|.
name|Natives
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
name|core
operator|.
name|client
operator|.
name|JsArrayString
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

begin_comment
comment|/** Screen contributed by a plugin. */
end_comment

begin_class
DECL|class|ExtensionScreen
specifier|public
class|class
name|ExtensionScreen
extends|extends
name|Screen
block|{
DECL|field|ctx
specifier|private
name|Context
name|ctx
decl_stmt|;
DECL|method|ExtensionScreen (String token)
specifier|public
name|ExtensionScreen
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|String
name|name
decl_stmt|;
name|String
name|rest
decl_stmt|;
name|int
name|s
init|=
name|token
operator|.
name|indexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|s
condition|)
block|{
name|name
operator|=
name|token
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
expr_stmt|;
name|rest
operator|=
name|token
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|token
expr_stmt|;
name|rest
operator|=
literal|""
expr_stmt|;
block|}
name|ctx
operator|=
name|create
argument_list|(
name|name
argument_list|,
name|rest
argument_list|)
expr_stmt|;
block|}
DECL|method|create (String name, String rest)
specifier|private
name|Context
name|create
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|rest
parameter_list|)
block|{
for|for
control|(
name|Definition
name|def
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|Definition
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
control|)
block|{
name|JsArrayString
name|m
init|=
name|def
operator|.
name|match
argument_list|(
name|rest
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
name|Context
operator|.
name|create
argument_list|(
name|def
argument_list|,
name|this
argument_list|,
name|m
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|isFound ()
specifier|public
name|boolean
name|isFound
parameter_list|()
block|{
return|return
name|ctx
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|setHeaderVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|onLoad
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
for|for
control|(
name|JavaScriptObject
name|u
range|:
name|Natives
operator|.
name|asList
argument_list|(
name|ctx
operator|.
name|unload
argument_list|()
argument_list|)
control|)
block|{
name|ApiGlue
operator|.
name|invoke
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Definition
specifier|static
class|class
name|Definition
extends|extends
name|JavaScriptObject
block|{
DECL|field|TYPE
specifier|static
specifier|final
name|JavaScriptObject
name|TYPE
init|=
name|init
argument_list|()
decl_stmt|;
DECL|method|init ()
specifier|private
specifier|static
specifier|native
name|JavaScriptObject
name|init
parameter_list|()
comment|/*-{       function ScreenDefinition(r, c) {         this.pattern = r;         this.onLoad = c;       };       return ScreenDefinition;     }-*/
function_decl|;
DECL|method|get (String n)
specifier|static
specifier|native
name|JsArray
argument_list|<
name|Definition
argument_list|>
name|get
parameter_list|(
name|String
name|n
parameter_list|)
comment|/*-{ return $wnd.Gerrit.screens[n] || [] }-*/
function_decl|;
DECL|method|match (String t)
specifier|final
specifier|native
name|JsArrayString
name|match
parameter_list|(
name|String
name|t
parameter_list|)
comment|/*-{       var p = this.pattern;       if (p instanceof $wnd.RegExp) {         var m = p.exec(t);         return m&& m[0] == t ? m : null;       }       return p == t ? [t] : null;     }-*/
function_decl|;
DECL|method|Definition ()
specifier|protected
name|Definition
parameter_list|()
block|{     }
block|}
DECL|class|Context
specifier|static
class|class
name|Context
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ( Definition def, ExtensionScreen view, JsArrayString match)
specifier|static
specifier|final
name|Context
name|create
parameter_list|(
name|Definition
name|def
parameter_list|,
name|ExtensionScreen
name|view
parameter_list|,
name|JsArrayString
name|match
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|TYPE
argument_list|,
name|def
argument_list|,
name|view
argument_list|,
name|view
operator|.
name|getBody
argument_list|()
operator|.
name|getElement
argument_list|()
argument_list|,
name|match
argument_list|)
return|;
block|}
DECL|method|onLoad ()
specifier|final
specifier|native
name|void
name|onLoad
parameter_list|()
comment|/*-{ this._d.onLoad(this) }-*/
function_decl|;
DECL|method|unload ()
specifier|final
specifier|native
name|JsArray
argument_list|<
name|JavaScriptObject
argument_list|>
name|unload
parameter_list|()
comment|/*-{ return this._u }-*/
function_decl|;
DECL|method|create ( JavaScriptObject T, Definition d, ExtensionScreen s, Element e, JsArrayString m)
specifier|private
specifier|static
specifier|final
specifier|native
name|Context
name|create
parameter_list|(
name|JavaScriptObject
name|T
parameter_list|,
name|Definition
name|d
parameter_list|,
name|ExtensionScreen
name|s
parameter_list|,
name|Element
name|e
parameter_list|,
name|JsArrayString
name|m
parameter_list|)
comment|/*-{ return new T(d,s,e,m) }-*/
function_decl|;
DECL|field|TYPE
specifier|private
specifier|static
specifier|final
name|JavaScriptObject
name|TYPE
init|=
name|init
argument_list|()
decl_stmt|;
DECL|method|init ()
specifier|private
specifier|static
specifier|final
specifier|native
name|JavaScriptObject
name|init
parameter_list|()
comment|/*-{       var T = function(d,s,e,m) {         this._d = d;         this._s = s;         this._u = [];         this.body = e;         this.token = m[0];         this.token_match = m;       };       T.prototype = {         setTitle: function(t){this._s.@com.google.gerrit.client.ui.Screen::setPageTitle(Ljava/lang/String;)(t)},         setWindowTitle: function(t){this._s.@com.google.gerrit.client.ui.Screen::setWindowTitle(Ljava/lang/String;)(t)},         show: function(){$entry(this._s.@com.google.gwtexpui.user.client.View::display()())},         onUnload: function(f){this._u.push(f)},       };       return T;     }-*/
function_decl|;
DECL|method|Context ()
specifier|protected
name|Context
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

