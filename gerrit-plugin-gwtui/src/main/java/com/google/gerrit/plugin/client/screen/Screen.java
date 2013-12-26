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
DECL|package|com.google.gerrit.plugin.client.screen
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
name|screen
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

begin_comment
comment|/**  * Screen contributed by this plugin.  *  * Screens should be registered early at module load:  *  *<pre>  *&#064;Override  * public void onModuleLoad() {  *   Plugin.get().screen(&quot;hi&quot;, new Screen.EntryPoint() {  *&#064;Override  *     public void onLoad(Screen screen) {  *       screen.setPageTitle(&quot;Hi&quot;);  *       screen.show(new Label(&quot;World&quot;));  *     }  *   });  * }  *</pre>  */
end_comment

begin_class
DECL|class|Screen
specifier|public
specifier|final
class|class
name|Screen
extends|extends
name|SimplePanel
block|{
comment|/** Initializes a screen for display. */
DECL|interface|EntryPoint
specifier|public
interface|interface
name|EntryPoint
block|{
comment|/**      * Invoked when the screen has been created, but not yet displayed.      *<p>      * The implementation should create a single widget to define the content of      * this screen and added it to the passed screen instance. When the screen      * is ready to be displayed, call {@link Screen#show()}.      *<p>      * To use multiple widgets, compose them in panels such as {@code FlowPanel}      * and add only the top level widget to the screen.      *<p>      * The screen is already attached to the browser DOM in an invisible area.      * Any widgets added to the screen will immediately receive {@code onLoad()}.      * GWT will fire {@code onUnload()} when the screen is removed from the UI,      * generally caused by the user navigating to another screen.      *      * @param screen panel that will contain the screen widget.      */
DECL|method|onLoad (Screen screen)
specifier|public
name|void
name|onLoad
parameter_list|(
name|Screen
name|screen
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
DECL|method|token_match ()
specifier|final
specifier|native
name|JsArrayString
name|token_match
parameter_list|()
comment|/*-{ return this.token_match }-*/
function_decl|;
DECL|method|show ()
specifier|final
specifier|native
name|void
name|show
parameter_list|()
comment|/*-{ this.show() }-*/
function_decl|;
DECL|method|setTitle (String t)
specifier|final
specifier|native
name|void
name|setTitle
parameter_list|(
name|String
name|t
parameter_list|)
comment|/*-{ this.setTitle(t) }-*/
function_decl|;
DECL|method|setWindowTitle (String t)
specifier|final
specifier|native
name|void
name|setWindowTitle
parameter_list|(
name|String
name|t
parameter_list|)
comment|/*-{ this.setWindowTitle(t) }-*/
function_decl|;
DECL|method|detach (Screen s)
specifier|final
specifier|native
name|void
name|detach
parameter_list|(
name|Screen
name|s
parameter_list|)
comment|/*-{       this.onUnload($entry(function(){         s.@com.google.gwt.user.client.ui.Widget::onDetach()();       }));     }-*/
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
DECL|method|Screen (Context ctx)
name|Screen
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
comment|/** @return the token suffix after {@code "/#/x/plugin-name/"}. */
DECL|method|getToken ()
specifier|public
specifier|final
name|String
name|getToken
parameter_list|()
block|{
return|return
name|getToken
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**    * @param group groups range from 1 to {@code getTokenGroups() - 1}. Token    *        group 0 is the entire token, see {@link #getToken()}.    * @return the token from the regex match group.    */
DECL|method|getToken (int group)
specifier|public
specifier|final
name|String
name|getToken
parameter_list|(
name|int
name|group
parameter_list|)
block|{
return|return
name|ctx
operator|.
name|token_match
argument_list|()
operator|.
name|get
argument_list|(
name|group
argument_list|)
return|;
block|}
comment|/** @return total number of token groups. */
DECL|method|getTokenGroups ()
specifier|public
specifier|final
name|int
name|getTokenGroups
parameter_list|()
block|{
return|return
name|ctx
operator|.
name|token_match
argument_list|()
operator|.
name|length
argument_list|()
return|;
block|}
comment|/**    * Set the page title text; appears above the widget.    *    * @param titleText text to display above the widget.    */
DECL|method|setPageTitle (String titleText)
specifier|public
specifier|final
name|void
name|setPageTitle
parameter_list|(
name|String
name|titleText
parameter_list|)
block|{
name|ctx
operator|.
name|setTitle
argument_list|(
name|titleText
argument_list|)
expr_stmt|;
block|}
comment|/**    * Set the window title text; appears in the browser window title bar.    *    * @param titleText text to display in the window title bar.    */
DECL|method|setWindowTitle (String titleText)
specifier|public
specifier|final
name|void
name|setWindowTitle
parameter_list|(
name|String
name|titleText
parameter_list|)
block|{
name|ctx
operator|.
name|setWindowTitle
argument_list|(
name|titleText
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add the widget and immediately show the screen.    *    * @param w child containing the content.    */
DECL|method|show (Widget w)
specifier|public
specifier|final
name|void
name|show
parameter_list|(
name|Widget
name|w
parameter_list|)
block|{
name|setWidget
argument_list|(
name|w
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
comment|/** Show this screen in the web interface. */
DECL|method|show ()
specifier|public
specifier|final
name|void
name|show
parameter_list|()
block|{
name|ctx
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

