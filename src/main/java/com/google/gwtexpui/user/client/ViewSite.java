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
name|user
operator|.
name|client
operator|.
name|ui
operator|.
name|Composite
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
name|SimplePanel
import|;
end_import

begin_comment
comment|/**  * Hosts a single {@link View}.  *<p>  * View instances are attached inside of an invisible DOM node, permitting their  *<code>onLoad()</code> method to be invoked and to update the DOM prior to the  * elements being made visible in the UI.  *<p>  * Complaint View instances must invoke {@link View#display()} once the DOM is  * ready for presentation.  */
end_comment

begin_class
DECL|class|ViewSite
specifier|public
class|class
name|ViewSite
parameter_list|<
name|V
extends|extends
name|View
parameter_list|>
extends|extends
name|Composite
block|{
DECL|field|main
specifier|private
specifier|final
name|FlowPanel
name|main
decl_stmt|;
DECL|field|current
specifier|private
name|SimplePanel
name|current
decl_stmt|;
DECL|field|next
specifier|private
name|SimplePanel
name|next
decl_stmt|;
DECL|method|ViewSite ()
specifier|public
name|ViewSite
parameter_list|()
block|{
name|main
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|main
argument_list|)
expr_stmt|;
block|}
comment|/** Get the current view; null if there is no view being displayed. */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getView ()
specifier|public
name|V
name|getView
parameter_list|()
block|{
return|return
name|current
operator|!=
literal|null
condition|?
operator|(
name|V
operator|)
name|current
operator|.
name|getWidget
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**    * Set the next view to display.    *<p>    * The view will be attached to the DOM tree within a hidden container,    * permitting its<code>onLoad()</code> method to execute and update the DOM    * without the user seeing the result.    *     * @param view the next view to display.    */
DECL|method|setView (final V view)
specifier|public
name|void
name|setView
parameter_list|(
specifier|final
name|V
name|view
parameter_list|)
block|{
if|if
condition|(
name|next
operator|!=
literal|null
condition|)
block|{
name|main
operator|.
name|remove
argument_list|(
name|next
argument_list|)
expr_stmt|;
block|}
name|view
operator|.
name|site
operator|=
name|this
expr_stmt|;
name|next
operator|=
operator|new
name|SimplePanel
argument_list|()
expr_stmt|;
name|next
operator|.
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|main
operator|.
name|add
argument_list|(
name|next
argument_list|)
expr_stmt|;
name|next
operator|.
name|add
argument_list|(
name|view
argument_list|)
expr_stmt|;
block|}
comment|/**    * Invoked after the view becomes the current view and has been made visible.    *     * @param view the view being displayed.    */
DECL|method|onShowView (final V view)
specifier|protected
name|void
name|onShowView
parameter_list|(
specifier|final
name|V
name|view
parameter_list|)
block|{   }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|swap (final View v)
specifier|final
name|void
name|swap
parameter_list|(
specifier|final
name|View
name|v
parameter_list|)
block|{
if|if
condition|(
name|next
operator|!=
literal|null
operator|&&
name|next
operator|.
name|getWidget
argument_list|()
operator|==
name|v
condition|)
block|{
if|if
condition|(
name|current
operator|!=
literal|null
condition|)
block|{
name|main
operator|.
name|remove
argument_list|(
name|current
argument_list|)
expr_stmt|;
block|}
name|current
operator|=
name|next
expr_stmt|;
name|next
operator|=
literal|null
expr_stmt|;
name|current
operator|.
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|onShowView
argument_list|(
operator|(
name|V
operator|)
name|v
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

