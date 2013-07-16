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
DECL|package|com.google.gerrit.extensions.webui
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|webui
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
name|extensions
operator|.
name|restapi
operator|.
name|RestResource
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
name|extensions
operator|.
name|restapi
operator|.
name|RestView
import|;
end_import

begin_interface
DECL|interface|UiAction
specifier|public
interface|interface
name|UiAction
parameter_list|<
name|R
extends|extends
name|RestResource
parameter_list|>
extends|extends
name|RestView
argument_list|<
name|R
argument_list|>
block|{
comment|/**    * Get the description of the action customized for the resource.    *    * @param resource the resource the view would act upon if the action is    *        invoked by the client. Information from the resource can be used to    *        customize the description.    * @return a description of the action. The server will populate the    *         {@code id} and {@code method} properties. If null the action will    *         assumed unavailable and not presented. This is usually the same as    *         {@code setVisible(false)}.    */
DECL|method|getDescription (R resource)
specifier|public
name|Description
name|getDescription
parameter_list|(
name|R
name|resource
parameter_list|)
function_decl|;
comment|/** Describes an action invokable through the web interface. */
DECL|class|Description
specifier|public
specifier|static
class|class
name|Description
block|{
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|visible
specifier|private
name|boolean
name|visible
init|=
literal|true
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/** {@code PrivateInternals_UiActionDescription.setMethod()} */
DECL|method|setMethod (String method)
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/** {@code PrivateInternals_UiActionDescription.setId()} */
DECL|method|setId (String id)
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
name|label
return|;
block|}
comment|/** Set the label to appear on the button to activate this action. */
DECL|method|setLabel (String label)
specifier|public
name|Description
name|setLabel
parameter_list|(
name|String
name|label
parameter_list|)
block|{
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
comment|/** Set the tool-tip text to appear when the mouse hovers on the button. */
DECL|method|setTitle (String title)
specifier|public
name|Description
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|isVisible ()
specifier|public
name|boolean
name|isVisible
parameter_list|()
block|{
return|return
name|visible
return|;
block|}
comment|/**      * Set if the action's button is visible on screen for the current client.      * If not visible the action description may not be sent to the client.      */
DECL|method|setVisible (boolean visible)
specifier|public
name|Description
name|setVisible
parameter_list|(
name|boolean
name|visible
parameter_list|)
block|{
name|this
operator|.
name|visible
operator|=
name|visible
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
operator|&&
name|isVisible
argument_list|()
return|;
block|}
comment|/** Set if the button should be invokable (true), or greyed out (false). */
DECL|method|setEnabled (boolean enabled)
specifier|public
name|Description
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
block|}
end_interface

end_unit

