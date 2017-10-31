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
name|resources
operator|.
name|client
operator|.
name|ClientBundle
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
name|resources
operator|.
name|client
operator|.
name|CssResource
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
name|UIObject
import|;
end_import

begin_comment
comment|/** Displays custom tooltip message below an element. */
end_comment

begin_class
DECL|class|Tooltip
specifier|public
class|class
name|Tooltip
block|{
DECL|interface|Resources
interface|interface
name|Resources
extends|extends
name|ClientBundle
block|{
DECL|field|I
name|Resources
name|I
init|=
name|GWT
operator|.
name|create
argument_list|(
name|Resources
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Source
argument_list|(
literal|"tooltip.css"
argument_list|)
DECL|method|css ()
name|Css
name|css
parameter_list|()
function_decl|;
block|}
DECL|interface|Css
interface|interface
name|Css
extends|extends
name|CssResource
block|{
DECL|method|tooltip ()
name|String
name|tooltip
parameter_list|()
function_decl|;
block|}
static|static
block|{
name|Resources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|ensureInjected
argument_list|()
expr_stmt|;
block|}
comment|/**    * Add required supporting style to enable custom tooltip rendering.    *    * @param o widget whose element should display a tooltip on hover.    */
DECL|method|addStyle (UIObject o)
specifier|public
specifier|static
name|void
name|addStyle
parameter_list|(
name|UIObject
name|o
parameter_list|)
block|{
name|addStyle
argument_list|(
name|o
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Add required supporting style to enable custom tooltip rendering.    *    * @param e element that should display a tooltip on hover.    */
DECL|method|addStyle (Element e)
specifier|public
specifier|static
name|void
name|addStyle
parameter_list|(
name|Element
name|e
parameter_list|)
block|{
name|e
operator|.
name|addClassName
argument_list|(
name|Resources
operator|.
name|I
operator|.
name|css
argument_list|()
operator|.
name|tooltip
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Set the text displayed on hover.    *    * @param o widget whose hover text is being set.    * @param text message to display on hover.    */
DECL|method|setLabel (UIObject o, String text)
specifier|public
specifier|static
name|void
name|setLabel
parameter_list|(
name|UIObject
name|o
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|setLabel
argument_list|(
name|o
operator|.
name|getElement
argument_list|()
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
comment|/**    * Set the text displayed on hover.    *    * @param e element whose hover text is being set.    * @param text message to display on hover.    */
DECL|method|setLabel (Element e, String text)
specifier|public
specifier|static
name|void
name|setLabel
parameter_list|(
name|Element
name|e
parameter_list|,
name|String
name|text
parameter_list|)
block|{
name|e
operator|.
name|setAttribute
argument_list|(
literal|"aria-label"
argument_list|,
name|text
operator|!=
literal|null
condition|?
name|text
else|:
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|Tooltip ()
specifier|private
name|Tooltip
parameter_list|()
block|{}
block|}
end_class

end_unit

