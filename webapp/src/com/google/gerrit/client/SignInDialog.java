begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
name|Command
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
name|DeferredCommand
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
name|rpc
operator|.
name|AsyncCallback
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
name|DialogBox
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
name|Frame
import|;
end_import

begin_comment
comment|/**  * Prompts the user to sign in to their account.  *<p>  * This dialog performs the login within an iframe, allowing normal HTML based  * login pages to be used, including those which aren't served from the same  * server as Gerrit. This is important to permit an OpenID provider or some  * other web based single-sign-on system to be used for authentication.  *<p>  * Post login the iframe content is expected to execute the JavaScript snippet:  *   *<pre>  * parent.gerritPostSignIn(success);  *</pre>  *   * where success is either<code>true</code> (the user is now signed in) or  *<code>false</code> (the sign in was aborted/canceled before it completed).  */
end_comment

begin_class
DECL|class|SignInDialog
specifier|public
class|class
name|SignInDialog
extends|extends
name|DialogBox
block|{
DECL|field|current
specifier|private
specifier|static
name|SignInDialog
name|current
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
argument_list|<
name|?
argument_list|>
name|callback
decl_stmt|;
comment|/**    * Create a new dialog to handle user sign in.    *     * @param callback optional; onSuccess will be called if sign is completed.    *        This can be used to trigger sending an RPC or some other action.    */
DECL|method|SignInDialog (final AsyncCallback<?> callback)
specifier|public
name|SignInDialog
parameter_list|(
specifier|final
name|AsyncCallback
argument_list|<
name|?
argument_list|>
name|callback
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
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
specifier|final
name|Frame
name|f
init|=
operator|new
name|Frame
argument_list|(
name|GWT
operator|.
name|getModuleBaseURL
argument_list|()
operator|+
literal|"login"
argument_list|)
decl_stmt|;
name|f
operator|.
name|setWidth
argument_list|(
literal|"630px"
argument_list|)
expr_stmt|;
name|f
operator|.
name|setHeight
argument_list|(
literal|"420px"
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|f
argument_list|)
expr_stmt|;
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
block|}
annotation|@
name|Override
DECL|method|show ()
specifier|public
name|void
name|show
parameter_list|()
block|{
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|current
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|show
argument_list|()
expr_stmt|;
name|current
operator|=
name|this
expr_stmt|;
name|exportPostSignIn
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
if|if
condition|(
name|current
operator|==
name|this
condition|)
block|{
name|unexportPostSignIn
argument_list|()
expr_stmt|;
name|current
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
block|}
DECL|method|postSignIn (final boolean success)
specifier|static
name|void
name|postSignIn
parameter_list|(
specifier|final
name|boolean
name|success
parameter_list|)
block|{
specifier|final
name|SignInDialog
name|d
init|=
name|current
decl_stmt|;
assert|assert
name|d
operator|!=
literal|null
assert|;
if|if
condition|(
name|success
condition|)
block|{
name|Gerrit
operator|.
name|postSignIn
argument_list|()
expr_stmt|;
name|d
operator|.
name|hide
argument_list|()
expr_stmt|;
specifier|final
name|AsyncCallback
argument_list|<
name|?
argument_list|>
name|ac
init|=
name|d
operator|.
name|callback
decl_stmt|;
if|if
condition|(
name|ac
operator|!=
literal|null
condition|)
block|{
name|DeferredCommand
operator|.
name|addCommand
argument_list|(
operator|new
name|Command
argument_list|()
block|{
specifier|public
name|void
name|execute
parameter_list|()
block|{
name|ac
operator|.
name|onSuccess
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|d
operator|.
name|hide
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unexportPostSignIn ()
specifier|private
specifier|static
specifier|final
specifier|native
name|void
name|unexportPostSignIn
parameter_list|()
comment|/*-{ delete $wnd.gerritPostSignIn; }-*/
function_decl|;
DECL|method|exportPostSignIn ()
specifier|private
specifier|static
specifier|final
specifier|native
name|void
name|exportPostSignIn
parameter_list|()
comment|/*-{ $wnd.gerritPostSignIn = @com.google.gerrit.client.SignInDialog::postSignIn(Z); }-*/
function_decl|;
block|}
end_class

end_unit

