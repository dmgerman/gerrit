begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.plugin.client.extension
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|plugin
operator|.
name|client
operator|.
name|extension
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
name|GerritUiExtensionPoint
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
name|ui
operator|.
name|SimplePanel
import|;
end_import

begin_comment
comment|/**  * Panel that extends a Gerrit core screen contributed by this plugin.  *  * Panel should be registered early at module load:  *  *<pre>  *&#064;Override  * public void onModuleLoad() {  *   Plugin.get().panel(GerritUiExtensionPoint.CHANGE_SCREEN_BELOW_CHANGE_INFO_BLOCK,  *       new Panel.EntryPoint() {  *&#064;Override  *         public void onLoad(Panel panel) {  *           panel.setWidget(new Label(&quot;World&quot;));  *         }  *       });  * }  *</pre>  */
end_comment

begin_class
DECL|class|Panel
specifier|public
class|class
name|Panel
extends|extends
name|SimplePanel
block|{
comment|/** Initializes a panel for display. */
DECL|interface|EntryPoint
specifier|public
interface|interface
name|EntryPoint
block|{
comment|/**      * Invoked when the panel has been created.      *<p>      * The implementation should create a single widget to define the content of      * this panel and add it to the passed panel instance.      *<p>      * To use multiple widgets, compose them in panels such as {@code FlowPanel}      * and add only the top level widget to the panel.      *<p>      * The panel is already attached to the browser DOM.      * Any widgets added to the screen will immediately receive {@code onLoad()}.      * GWT will fire {@code onUnload()} when the panel is removed from the UI,      * generally caused by the user navigating to another screen.      *      * @param panel panel that will contain the panel widget.      */
DECL|method|onLoad (Panel panel)
specifier|public
name|void
name|onLoad
parameter_list|(
name|Panel
name|panel
parameter_list|)
function_decl|;
block|}
DECL|class|Context
specifier|static
specifier|final
class|class
name|Context
extends|extends
name|JavaScriptObject
block|{
DECL|method|body ()
specifier|final
specifier|native
name|Element
name|body
parameter_list|()
comment|/*-{ return this.body }-*/
function_decl|;
DECL|method|get (String k)
specifier|final
specifier|native
name|String
name|get
parameter_list|(
name|String
name|k
parameter_list|)
comment|/*-{ return this.p[k]; }-*/
function_decl|;
DECL|method|getInt (String k, int d)
specifier|final
specifier|native
name|int
name|getInt
parameter_list|(
name|String
name|k
parameter_list|,
name|int
name|d
parameter_list|)
comment|/*-{       return this.p.hasOwnProperty(k) ? this.p[k] : d     }-*/
function_decl|;
DECL|method|getBoolean (String k, boolean d)
specifier|final
specifier|native
name|int
name|getBoolean
parameter_list|(
name|String
name|k
parameter_list|,
name|boolean
name|d
parameter_list|)
comment|/*-{       return this.p.hasOwnProperty(k) ? this.p[k] : d     }-*/
function_decl|;
DECL|method|getObject (String k)
specifier|final
specifier|native
name|JavaScriptObject
name|getObject
parameter_list|(
name|String
name|k
parameter_list|)
comment|/*-{ return this.p[k]; }-*/
function_decl|;
DECL|method|detach (Panel p)
specifier|final
specifier|native
name|void
name|detach
parameter_list|(
name|Panel
name|p
parameter_list|)
comment|/*-{       this.onUnload($entry(function(){         p.@com.google.gwt.user.client.ui.Widget::onDetach()();       }));     }-*/
function_decl|;
DECL|method|Context ()
specifier|protected
name|Context
parameter_list|()
block|{     }
block|}
DECL|field|ctx
specifier|private
specifier|final
name|Context
name|ctx
decl_stmt|;
DECL|method|Panel (Context ctx)
name|Panel
parameter_list|(
name|Context
name|ctx
parameter_list|)
block|{
name|super
argument_list|(
name|ctx
operator|.
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|ctx
operator|=
name|ctx
expr_stmt|;
name|onAttach
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|detach
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|get (GerritUiExtensionPoint.Key key)
specifier|public
name|String
name|get
parameter_list|(
name|GerritUiExtensionPoint
operator|.
name|Key
name|key
parameter_list|)
block|{
return|return
name|ctx
operator|.
name|get
argument_list|(
name|key
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getInt (GerritUiExtensionPoint.Key key, int defaultValue)
specifier|public
name|int
name|getInt
parameter_list|(
name|GerritUiExtensionPoint
operator|.
name|Key
name|key
parameter_list|,
name|int
name|defaultValue
parameter_list|)
block|{
return|return
name|ctx
operator|.
name|getInt
argument_list|(
name|key
operator|.
name|name
argument_list|()
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getBoolean (GerritUiExtensionPoint.Key key, boolean defaultValue)
specifier|public
name|int
name|getBoolean
parameter_list|(
name|GerritUiExtensionPoint
operator|.
name|Key
name|key
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
return|return
name|ctx
operator|.
name|getBoolean
argument_list|(
name|key
operator|.
name|name
argument_list|()
argument_list|,
name|defaultValue
argument_list|)
return|;
block|}
DECL|method|getObject (GerritUiExtensionPoint.Key key)
specifier|public
name|JavaScriptObject
name|getObject
parameter_list|(
name|GerritUiExtensionPoint
operator|.
name|Key
name|key
parameter_list|)
block|{
return|return
name|ctx
operator|.
name|getObject
argument_list|(
name|key
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

