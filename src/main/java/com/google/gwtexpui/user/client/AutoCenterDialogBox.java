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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ResizeEvent
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
name|event
operator|.
name|logical
operator|.
name|shared
operator|.
name|ResizeHandler
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
name|event
operator|.
name|shared
operator|.
name|HandlerRegistration
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
comment|/** A DialogBox that automatically re-centers itself if the window changes */
end_comment

begin_class
DECL|class|AutoCenterDialogBox
specifier|public
class|class
name|AutoCenterDialogBox
extends|extends
name|PluginSafeDialogBox
block|{
DECL|field|recenter
specifier|private
name|HandlerRegistration
name|recenter
decl_stmt|;
DECL|method|AutoCenterDialogBox ()
specifier|public
name|AutoCenterDialogBox
parameter_list|()
block|{
name|this
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|AutoCenterDialogBox (final boolean autoHide)
specifier|public
name|AutoCenterDialogBox
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
DECL|method|AutoCenterDialogBox (final boolean autoHide, final boolean modal)
specifier|public
name|AutoCenterDialogBox
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
DECL|method|show ()
specifier|public
name|void
name|show
parameter_list|()
block|{
if|if
condition|(
name|recenter
operator|==
literal|null
condition|)
block|{
name|recenter
operator|=
name|Window
operator|.
name|addResizeHandler
argument_list|(
operator|new
name|ResizeHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResize
parameter_list|(
specifier|final
name|ResizeEvent
name|event
parameter_list|)
block|{
specifier|final
name|int
name|w
init|=
name|event
operator|.
name|getWidth
argument_list|()
decl_stmt|;
specifier|final
name|int
name|h
init|=
name|event
operator|.
name|getHeight
argument_list|()
decl_stmt|;
name|AutoCenterDialogBox
operator|.
name|this
operator|.
name|onResize
argument_list|(
name|w
argument_list|,
name|h
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|show
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
name|recenter
operator|!=
literal|null
condition|)
block|{
name|recenter
operator|.
name|removeHandler
argument_list|()
expr_stmt|;
name|recenter
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
comment|/**    * Invoked when the outer browser window resizes.    *<p>    * Subclasses may override (but should ensure they still call super.onResize)    * to implement custom logic when a window resize occurs.    *     * @param width new browser window width    * @param height new browser window height    */
DECL|method|onResize (final int width, final int height)
specifier|protected
name|void
name|onResize
parameter_list|(
specifier|final
name|int
name|width
parameter_list|,
specifier|final
name|int
name|height
parameter_list|)
block|{
if|if
condition|(
name|isAttached
argument_list|()
condition|)
block|{
name|center
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

