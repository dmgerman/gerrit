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
DECL|package|com.google.gerrit.client.ui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|ui
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
name|gerrit
operator|.
name|client
operator|.
name|Link
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
name|DOM
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
name|Widget
import|;
end_import

begin_class
DECL|class|Screen
specifier|public
class|class
name|Screen
extends|extends
name|FlowPanel
block|{
DECL|field|requiresSignIn
specifier|private
name|boolean
name|requiresSignIn
decl_stmt|;
DECL|field|headerElem
specifier|private
specifier|final
name|Element
name|headerElem
decl_stmt|;
DECL|field|headerText
specifier|private
name|Element
name|headerText
decl_stmt|;
DECL|method|Screen ()
specifier|protected
name|Screen
parameter_list|()
block|{
name|this
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|Screen (final String headingText)
specifier|protected
name|Screen
parameter_list|(
specifier|final
name|String
name|headingText
parameter_list|)
block|{
name|setStyleName
argument_list|(
literal|"gerrit-Screen"
argument_list|)
expr_stmt|;
name|headerElem
operator|=
name|DOM
operator|.
name|createElement
argument_list|(
literal|"h1"
argument_list|)
expr_stmt|;
name|headerText
operator|=
name|headerElem
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|headerElem
argument_list|)
expr_stmt|;
name|setTitleText
argument_list|(
name|headingText
argument_list|)
expr_stmt|;
block|}
DECL|method|setTitleText (final String text)
specifier|public
name|void
name|setTitleText
parameter_list|(
specifier|final
name|String
name|text
parameter_list|)
block|{
name|DOM
operator|.
name|setInnerText
argument_list|(
name|headerText
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
DECL|method|insertTitleWidget (final Widget w)
specifier|protected
name|void
name|insertTitleWidget
parameter_list|(
specifier|final
name|Widget
name|w
parameter_list|)
block|{
if|if
condition|(
name|headerText
operator|==
name|headerElem
condition|)
block|{
name|headerText
operator|=
name|DOM
operator|.
name|createElement
argument_list|(
literal|"span"
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|setInnerText
argument_list|(
name|headerText
argument_list|,
name|DOM
operator|.
name|getInnerText
argument_list|(
name|headerElem
argument_list|)
argument_list|)
expr_stmt|;
name|DOM
operator|.
name|appendChild
argument_list|(
name|headerElem
argument_list|,
name|headerText
argument_list|)
expr_stmt|;
block|}
name|insert
argument_list|(
name|w
argument_list|,
name|headerElem
argument_list|,
literal|0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/** Set whether or not {@link Gerrit#isSignedIn()} must be true. */
DECL|method|setRequiresSignIn (final boolean b)
specifier|public
name|void
name|setRequiresSignIn
parameter_list|(
specifier|final
name|boolean
name|b
parameter_list|)
block|{
name|requiresSignIn
operator|=
name|b
expr_stmt|;
block|}
comment|/** Does {@link Gerrit#isSignedIn()} have to be true to be on this screen? */
DECL|method|isRequiresSignIn ()
specifier|public
name|boolean
name|isRequiresSignIn
parameter_list|()
block|{
return|return
name|requiresSignIn
return|;
block|}
comment|/** Invoked if this screen is the current screen and the user signs out. */
DECL|method|onSignOut ()
specifier|public
name|void
name|onSignOut
parameter_list|()
block|{
if|if
condition|(
name|isRequiresSignIn
argument_list|()
condition|)
block|{
name|History
operator|.
name|newItem
argument_list|(
name|Link
operator|.
name|ALL_OPEN
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Invoked if this screen is the current screen and the user signs in. */
DECL|method|onSignIn ()
specifier|public
name|void
name|onSignIn
parameter_list|()
block|{   }
comment|/** Get the token to cache this screen's widget; null if it shouldn't cache. */
DECL|method|getScreenCacheToken ()
specifier|public
name|Object
name|getScreenCacheToken
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**    * Reconfigure this screen after being recycled.    *<p>    * This method is invoked on a cached screen instance just before it is    * recycled into the UI. The returned screen instance is what will actually be    * shown to the user.    *     * @param newScreen the screen object created by the Link class (or some other    *        form of screen constructor) and that was just passed into    *        {@link Gerrit#display(Screen)}. Its {@link #getScreenCacheToken()}    *        is equal to<code>this.getScreenCacheToken()</code> but it may have    *        other parameter information worth copying.    * @return typically<code>this</code> to reuse the cached screen;    *<code>newScreen</code> to discard the cached screen instance and    *         use the new one.    */
DECL|method|recycleThis (final Screen newScreen)
specifier|public
name|Screen
name|recycleThis
parameter_list|(
specifier|final
name|Screen
name|newScreen
parameter_list|)
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

