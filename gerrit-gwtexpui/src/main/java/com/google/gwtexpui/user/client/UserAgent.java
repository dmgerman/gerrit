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
comment|/**  * User agent feature tests we don't create permutations for.  *  *<p>Some features aren't worth creating full permutations in GWT for, as each new boolean  * permutation (only two settings) doubles the compile time required. If the setting only affects a  * couple of lines of JavaScript code, the slightly larger cache files for user agents that lack the  * functionality requested is trivial compared to the time developers lose building their  * application.  */
end_comment

begin_class
DECL|class|UserAgent
specifier|public
class|class
name|UserAgent
block|{
DECL|field|jsClip
specifier|private
specifier|static
name|boolean
name|jsClip
init|=
name|guessJavaScriptClipboard
argument_list|()
decl_stmt|;
DECL|method|hasJavaScriptClipboard ()
specifier|public
specifier|static
name|boolean
name|hasJavaScriptClipboard
parameter_list|()
block|{
return|return
name|jsClip
return|;
block|}
DECL|method|disableJavaScriptClipboard ()
specifier|public
specifier|static
name|void
name|disableJavaScriptClipboard
parameter_list|()
block|{
name|jsClip
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|nativeHasCopy ()
specifier|private
specifier|static
specifier|native
name|boolean
name|nativeHasCopy
parameter_list|()
comment|/*-{ return $doc['queryCommandSupported']&& $doc.queryCommandSupported('copy') }-*/
function_decl|;
DECL|method|guessJavaScriptClipboard ()
specifier|private
specifier|static
name|boolean
name|guessJavaScriptClipboard
parameter_list|()
block|{
name|String
name|ua
init|=
name|Window
operator|.
name|Navigator
operator|.
name|getUserAgent
argument_list|()
decl_stmt|;
name|int
name|chrome
init|=
name|major
argument_list|(
name|ua
argument_list|,
literal|"Chrome/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|chrome
operator|>
literal|0
condition|)
block|{
return|return
literal|42
operator|<=
name|chrome
return|;
block|}
name|int
name|ff
init|=
name|major
argument_list|(
name|ua
argument_list|,
literal|"Firefox/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ff
operator|>
literal|0
condition|)
block|{
return|return
literal|41
operator|<=
name|ff
return|;
block|}
name|int
name|opera
init|=
name|major
argument_list|(
name|ua
argument_list|,
literal|"OPR/"
argument_list|)
decl_stmt|;
if|if
condition|(
name|opera
operator|>
literal|0
condition|)
block|{
return|return
literal|29
operator|<=
name|opera
return|;
block|}
name|int
name|msie
init|=
name|major
argument_list|(
name|ua
argument_list|,
literal|"MSIE "
argument_list|)
decl_stmt|;
if|if
condition|(
name|msie
operator|>
literal|0
condition|)
block|{
return|return
literal|9
operator|<=
name|msie
return|;
block|}
if|if
condition|(
name|nativeHasCopy
argument_list|()
condition|)
block|{
comment|// Firefox 39.0 lies and says it supports copy, then fails.
comment|// So we try this after the browser specific test above.
return|return
literal|true
return|;
block|}
comment|// Safari is not planning to support document.execCommand('copy').
comment|// Assume the browser does not have the feature.
return|return
literal|false
return|;
block|}
DECL|method|major (String ua, String product)
specifier|private
specifier|static
name|int
name|major
parameter_list|(
name|String
name|ua
parameter_list|,
name|String
name|product
parameter_list|)
block|{
name|int
name|entry
init|=
name|ua
operator|.
name|indexOf
argument_list|(
name|product
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|>=
literal|0
condition|)
block|{
name|String
name|s
init|=
name|ua
operator|.
name|substring
argument_list|(
name|entry
operator|+
name|product
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|p
init|=
name|s
operator|.
name|split
argument_list|(
literal|"[ /;,.)]"
argument_list|,
literal|2
argument_list|)
index|[
literal|0
index|]
decl_stmt|;
try|try
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|p
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|nan
parameter_list|)
block|{
comment|// Ignored
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
DECL|class|Flash
specifier|public
specifier|static
class|class
name|Flash
block|{
DECL|field|checked
specifier|private
specifier|static
name|boolean
name|checked
decl_stmt|;
DECL|field|installed
specifier|private
specifier|static
name|boolean
name|installed
decl_stmt|;
comment|/**      * Does the browser have ShockwaveFlash plugin installed?      *      *<p>This method may still return true if the user has disabled Flash or set the plugin to      * "click to run".      */
DECL|method|isInstalled ()
specifier|public
specifier|static
name|boolean
name|isInstalled
parameter_list|()
block|{
if|if
condition|(
operator|!
name|checked
condition|)
block|{
name|installed
operator|=
name|hasFlash
argument_list|()
expr_stmt|;
name|checked
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|installed
return|;
block|}
DECL|method|hasFlash ()
specifier|private
specifier|static
specifier|native
name|boolean
name|hasFlash
parameter_list|()
comment|/*-{       if (navigator.plugins&& navigator.plugins.length) {         if (navigator.plugins['Shockwave Flash'])     return true;         if (navigator.plugins['Shockwave Flash 2.0']) return true;        } else if (navigator.mimeTypes&& navigator.mimeTypes.length) {         var mimeType = navigator.mimeTypes['application/x-shockwave-flash'];         if (mimeType&& mimeType.enabledPlugin) return true;        } else {         try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash.7'); return true; } catch (e) {}         try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash.6'); return true; } catch (e) {}         try { new ActiveXObject('ShockwaveFlash.ShockwaveFlash');   return true; } catch (e) {}       }       return false;     }-*/
function_decl|;
block|}
comment|/**    * Test for and disallow running this application in an&lt;iframe&gt;.    *    *<p>If the application is running within an iframe this method requests a browser generated    * redirect to pop the application out of the iframe into the top level window, and then aborts    * execution by throwing an exception. This is call should be placed early within the module's    * onLoad() method, before any real UI can be initialized that an attacking site could try to snip    * out and present in a confusing context.    *    *<p>If the break out works, execution will restart automatically in a proper top level window,    * where the script has full control over the display. If the break out fails, execution will    * abort and stop immediately, preventing UI widgets from being created, leaving the user with an    * empty frame.    */
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
comment|/**    * Test if Gerrit is running on a mobile browser. This check could be incomplete, but should cover    * most cases. Regexes shamelessly borrowed from CodeMirror.    */
DECL|method|isMobile ()
specifier|public
specifier|static
specifier|native
name|boolean
name|isMobile
parameter_list|()
comment|/*-{     var ua = $wnd.navigator.userAgent;     var ios = /AppleWebKit/.test(ua)&& /Mobile\/\w+/.test(ua);     return ios         || /Android|webOS|BlackBerry|Opera Mini|Opera Mobi|IEMobile/i.test(ua);   }-*/
function_decl|;
comment|/** Check if the height of the browser view is greater than its width. */
DECL|method|isPortrait ()
specifier|public
specifier|static
name|boolean
name|isPortrait
parameter_list|()
block|{
return|return
name|Window
operator|.
name|getClientHeight
argument_list|()
operator|>
name|Window
operator|.
name|getClientWidth
argument_list|()
return|;
block|}
DECL|method|UserAgent ()
specifier|private
name|UserAgent
parameter_list|()
block|{}
block|}
end_class

end_unit

