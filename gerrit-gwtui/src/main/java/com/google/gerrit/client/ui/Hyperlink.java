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
name|Event
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
name|Anchor
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
name|impl
operator|.
name|HyperlinkImpl
import|;
end_import

begin_comment
comment|/** Standard GWT hyperlink with late updating of the token. */
end_comment

begin_class
DECL|class|Hyperlink
specifier|public
class|class
name|Hyperlink
extends|extends
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
name|Hyperlink
block|{
DECL|field|impl
specifier|static
specifier|final
name|HyperlinkImpl
name|impl
init|=
name|GWT
operator|.
name|create
argument_list|(
name|HyperlinkImpl
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/** Initialize a default hyperlink with no target and no text. */
DECL|method|Hyperlink ()
specifier|public
name|Hyperlink
parameter_list|()
block|{   }
comment|/**    * Creates a hyperlink with its text and target history token specified.    *    * @param text the hyperlink's text    * @param token the history token to which it will link, which may not be null    *        (use {@link Anchor} instead if you don't need history processing)    */
DECL|method|Hyperlink (final String text, final String token)
specifier|public
name|Hyperlink
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
comment|/**    * Creates a hyperlink with its text and target history token specified.    *    * @param text the hyperlink's text    * @param asHTML<code>true</code> to treat the specified text as html    * @param token the history token to which it will link    * @see #setTargetHistoryToken    */
DECL|method|Hyperlink (String text, boolean asHTML, String token)
specifier|public
name|Hyperlink
parameter_list|(
name|String
name|text
parameter_list|,
name|boolean
name|asHTML
parameter_list|,
name|String
name|token
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
name|asHTML
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onBrowserEvent (final Event event)
specifier|public
name|void
name|onBrowserEvent
parameter_list|(
specifier|final
name|Event
name|event
parameter_list|)
block|{
if|if
condition|(
name|DOM
operator|.
name|eventGetType
argument_list|(
name|event
argument_list|)
operator|==
name|Event
operator|.
name|ONCLICK
operator|&&
name|impl
operator|.
name|handleAsClick
argument_list|(
name|event
argument_list|)
condition|)
block|{
name|DOM
operator|.
name|eventPreventDefault
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|go
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|onBrowserEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Create the screen and start rendering, updating the browser history. */
DECL|method|go ()
specifier|public
name|void
name|go
parameter_list|()
block|{
name|Gerrit
operator|.
name|display
argument_list|(
name|getTargetHistoryToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

