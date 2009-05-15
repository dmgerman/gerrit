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
DECL|package|com.google.gwtexpui.user.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
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
name|Window
import|;
end_import

begin_comment
comment|/**  * User agent feature tests we don't create permutations for.  *<p>  * Some features aren't worth creating full permutations in GWT for, as each new  * boolean permutation (only two settings) doubles the compile time required. If  * the setting only affects a couple of lines of JavaScript code, the slightly  * larger cache files for user agents that lack the functionality requested is  * trivial compared to the time developers lose building their application.  */
end_comment

begin_class
DECL|class|UserAgent
specifier|public
class|class
name|UserAgent
block|{
comment|/** Does the browser have ShockwaveFlash plugin enabled? */
DECL|field|hasFlash
specifier|public
specifier|static
specifier|final
name|boolean
name|hasFlash
init|=
name|hasFlash
argument_list|()
decl_stmt|;
DECL|method|hasFlash ()
specifier|private
specifier|static
specifier|native
name|boolean
name|hasFlash
parameter_list|()
comment|/*-{     if (navigator.plugins&& navigator.plugins.length) {       if (navigator.plugins['Shockwave Flash'])     return true;       if (navigator.plugins['Shockwave Flash 2.0']) return true;      } else if (navigator.mimeTypes&& navigator.mimeTypes.length) {       var mimeType = navigator.mimeTypes['application/x-shockwave-flash'];       if (mimeType&& mimeType.enabledPlugin) return true;      } else {       try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash.7'); return true; } catch (e) {}       try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash.6'); return true; } catch (e) {}       try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash');   return true; } catch (e) {}     }     return false;   }-*/
function_decl|;
comment|/**    * Test for and disallow running this application in an&lt;iframe&gt;.    *<p>    * If the application is running within an iframe this method requests a    * browser generated redirect to pop the application out of the iframe into    * the top level window, and then aborts execution by throwing an exception.    * This is call should be placed early within the module's onLoad() method,    * before any real UI can be initialized that an attacking site could try to    * snip out and present in a confusing context.    *<p>    * If the break out works, execution will restart automatically in a proper    * top level window, where the script has full control over the display. If    * the break out fails, execution will abort and stop immediately, preventing    * UI widgets from being created, leaving the user with an empty frame.    */
DECL|method|assertNotInIFrame ()
specifier|public
specifier|static
name|void
name|assertNotInIFrame
parameter_list|()
block|{
if|if
condition|(
name|GWT
operator|.
name|isScript
argument_list|()
operator|&&
name|amInsideIFrame
argument_list|()
condition|)
block|{
name|bustOutOfIFrame
argument_list|(
name|Window
operator|.
name|Location
operator|.
name|getHref
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
block|}
DECL|method|amInsideIFrame ()
specifier|private
specifier|static
specifier|native
name|boolean
name|amInsideIFrame
parameter_list|()
comment|/*-{ return top.location != $wnd.location; }-*/
function_decl|;
DECL|method|bustOutOfIFrame (String newloc)
specifier|private
specifier|static
specifier|native
name|void
name|bustOutOfIFrame
parameter_list|(
name|String
name|newloc
parameter_list|)
comment|/*-{ top.location.href = newloc }-*/
function_decl|;
DECL|method|UserAgent ()
specifier|private
name|UserAgent
parameter_list|()
block|{   }
block|}
end_class

end_unit

