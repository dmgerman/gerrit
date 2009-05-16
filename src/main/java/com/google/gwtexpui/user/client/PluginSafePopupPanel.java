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
name|ui
operator|.
name|PopupPanel
import|;
end_import

begin_comment
comment|/**  * A PopupPanel that can appear over Flash movies and Java applets.  *<p>  * Some browsers have issues with placing a&lt;div&gt; (such as that used by  * the PopupPanel implementation) over top of native UI such as that used by the  * Flash plugin. Often the native UI leaks over top of the&lt;div&gt;, which is  * not the desired behavior for a dialog box.  *<p>  * This implementation hides the native resources by setting their display  * property to 'none' when the dialog is shown, and restores them back to their  * prior setting when the dialog is hidden.  * */
end_comment

begin_class
DECL|class|PluginSafePopupPanel
specifier|public
class|class
name|PluginSafePopupPanel
extends|extends
name|PopupPanel
block|{
DECL|field|impl
specifier|private
specifier|final
name|PluginSafeDialogBoxImpl
name|impl
init|=
name|GWT
operator|.
name|create
argument_list|(
name|PluginSafeDialogBoxImpl
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|PluginSafePopupPanel ()
specifier|public
name|PluginSafePopupPanel
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|PluginSafePopupPanel (final boolean autoHide)
specifier|public
name|PluginSafePopupPanel
parameter_list|(
specifier|final
name|boolean
name|autoHide
parameter_list|)
block|{
name|this
argument_list|(
name|autoHide
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|PluginSafePopupPanel (final boolean autoHide, final boolean modal)
specifier|public
name|PluginSafePopupPanel
parameter_list|(
specifier|final
name|boolean
name|autoHide
parameter_list|,
specifier|final
name|boolean
name|modal
parameter_list|)
block|{
name|super
argument_list|(
name|autoHide
argument_list|,
name|modal
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setVisible (final boolean show)
specifier|public
name|void
name|setVisible
parameter_list|(
specifier|final
name|boolean
name|show
parameter_list|)
block|{
name|impl
operator|.
name|visible
argument_list|(
name|show
argument_list|)
expr_stmt|;
name|super
operator|.
name|setVisible
argument_list|(
name|show
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
name|impl
operator|.
name|visible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|show
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hide (final boolean autoClosed)
specifier|public
name|void
name|hide
parameter_list|(
specifier|final
name|boolean
name|autoClosed
parameter_list|)
block|{
name|impl
operator|.
name|visible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|super
operator|.
name|hide
argument_list|(
name|autoClosed
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

