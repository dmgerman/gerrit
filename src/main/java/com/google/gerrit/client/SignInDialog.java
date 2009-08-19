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
name|openid
operator|.
name|OpenIdLoginPanel
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
name|FlowPanel
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
name|Label
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
name|AutoCenterDialogBox
import|;
end_import

begin_comment
comment|/**  * Prompts the user to sign in to their account.  *<p>  * This dialog performs the login within an iframe, allowing normal HTML based  * login pages to be used, including those which aren't served from the same  * server as Gerrit. This is important to permit an OpenID provider or some  * other web based single-sign-on system to be used for authentication.  *<p>  * Post login the iframe content is expected to execute the JavaScript snippet:  *  *<pre>  * $callback(account);  *</pre>  *  * where<code>$callback</code> is the parameter in the initial request and  *<code>account</code> is either<code>!= null</code> (the user is now signed  * in) or<code>null</code> (the sign in was aborted/canceled before it  * completed).  */
end_comment

begin_class
DECL|class|SignInDialog
specifier|public
class|class
name|SignInDialog
extends|extends
name|AutoCenterDialogBox
block|{
DECL|enum|Mode
specifier|public
specifier|static
enum|enum
name|Mode
block|{
DECL|enumConstant|SIGN_IN
DECL|enumConstant|LINK_IDENTIY
DECL|enumConstant|REGISTER
name|SIGN_IN
block|,
name|LINK_IDENTIY
block|,
name|REGISTER
block|;   }
DECL|field|panel
specifier|private
name|Widget
name|panel
decl_stmt|;
comment|/**    * Create a new dialog to handle user sign in.    */
DECL|method|SignInDialog ()
specifier|public
name|SignInDialog
parameter_list|()
block|{
name|this
argument_list|(
name|Mode
operator|.
name|SIGN_IN
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a new dialog to handle user sign in.    *    * @param signInMode type of mode the login will perform.    */
DECL|method|SignInDialog (final Mode signInMode)
specifier|public
name|SignInDialog
parameter_list|(
specifier|final
name|Mode
name|signInMode
parameter_list|)
block|{
name|this
argument_list|(
name|signInMode
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a new dialog to handle user sign in.    *    * @param signInMode type of mode the login will perform.    * @param errorMsg error message to display, if non-null.    */
DECL|method|SignInDialog (final Mode signInMode, final String errorMsg)
specifier|public
name|SignInDialog
parameter_list|(
specifier|final
name|Mode
name|signInMode
parameter_list|,
specifier|final
name|String
name|errorMsg
parameter_list|)
block|{
name|super
argument_list|(
comment|/* auto hide */
literal|true
argument_list|,
comment|/* modal */
literal|true
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|Gerrit
operator|.
name|getConfig
argument_list|()
operator|.
name|getAuthType
argument_list|()
condition|)
block|{
case|case
name|OPENID
case|:
name|panel
operator|=
operator|new
name|OpenIdLoginPanel
argument_list|(
name|signInMode
argument_list|,
name|errorMsg
argument_list|)
expr_stmt|;
break|break;
default|default:
block|{
specifier|final
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|fp
operator|.
name|add
argument_list|(
operator|new
name|Label
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|loginTypeUnsupported
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|panel
operator|=
name|fp
expr_stmt|;
break|break;
block|}
block|}
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|signInMode
condition|)
block|{
case|case
name|LINK_IDENTIY
case|:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|linkIdentityDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REGISTER
case|:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|registerDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|setText
argument_list|(
name|Gerrit
operator|.
name|C
operator|.
name|signInDialogTitle
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
annotation|@
name|Override
DECL|method|show ()
specifier|public
name|void
name|show
parameter_list|()
block|{
name|super
operator|.
name|show
argument_list|()
expr_stmt|;
if|if
condition|(
name|panel
operator|instanceof
name|OpenIdLoginPanel
condition|)
block|{
operator|(
operator|(
name|OpenIdLoginPanel
operator|)
name|panel
operator|)
operator|.
name|setFocus
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

