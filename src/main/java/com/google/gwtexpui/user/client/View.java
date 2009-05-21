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
name|Widget
import|;
end_import

begin_comment
comment|/**  * Widget to display within a {@link ViewSite}.  *<p>  * Implementations must override<code>protected void onLoad()</code> and  * arrange for {@link #display()} to be invoked once the DOM within the view is  * consistent for presentation to the user. Typically this means that the  * subclass can start RPCs within<code>onLoad()</code> and then invoke  *<code>display()</code> from within the AsyncCallback's  *<code>onSuccess(Object)</code> method.  */
end_comment

begin_class
DECL|class|View
specifier|public
specifier|abstract
class|class
name|View
extends|extends
name|Composite
block|{
DECL|field|site
name|ViewSite
argument_list|<
name|?
extends|extends
name|View
argument_list|>
name|site
decl_stmt|;
annotation|@
name|Override
DECL|method|onUnload ()
specifier|protected
name|void
name|onUnload
parameter_list|()
block|{
name|site
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|onUnload
argument_list|()
expr_stmt|;
block|}
comment|/** true if this is the current view of its parent view site */
DECL|method|isCurrentView ()
specifier|public
specifier|final
name|boolean
name|isCurrentView
parameter_list|()
block|{
name|Widget
name|p
init|=
name|getParent
argument_list|()
decl_stmt|;
while|while
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|p
operator|instanceof
name|ViewSite
condition|)
block|{
return|return
operator|(
operator|(
name|ViewSite
argument_list|<
name|?
argument_list|>
operator|)
name|p
operator|)
operator|.
name|getView
argument_list|()
operator|==
name|this
return|;
block|}
name|p
operator|=
name|p
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
comment|/** Replace the current view in the parent ViewSite with this view. */
DECL|method|display ()
specifier|public
specifier|final
name|void
name|display
parameter_list|()
block|{
if|if
condition|(
name|site
operator|!=
literal|null
condition|)
block|{
name|site
operator|.
name|swap
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

