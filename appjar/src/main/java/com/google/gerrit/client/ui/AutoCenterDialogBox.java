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
name|gwt
operator|.
name|user
operator|.
name|client
operator|.
name|Window
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
name|WindowResizeListener
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

begin_comment
comment|/** A DialogBox that automatically re-centers itself if the window changes */
end_comment

begin_class
DECL|class|AutoCenterDialogBox
specifier|public
class|class
name|AutoCenterDialogBox
extends|extends
name|DialogBox
block|{
DECL|field|recenter
specifier|private
name|WindowResizeListener
name|recenter
decl_stmt|;
DECL|method|AutoCenterDialogBox ()
specifier|public
name|AutoCenterDialogBox
parameter_list|()
block|{   }
DECL|method|AutoCenterDialogBox (final boolean autoHide)
specifier|public
name|AutoCenterDialogBox
parameter_list|(
specifier|final
name|boolean
name|autoHide
parameter_list|)
block|{
name|super
argument_list|(
name|autoHide
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
operator|new
name|WindowResizeListener
argument_list|()
block|{
specifier|public
name|void
name|onWindowResized
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
name|onResize
argument_list|(
name|width
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|Window
operator|.
name|addWindowResizeListener
argument_list|(
name|recenter
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
name|Window
operator|.
name|removeWindowResizeListener
argument_list|(
name|recenter
argument_list|)
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

