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
DECL|package|com.google.gwtexpui.globalkey.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
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
name|dom
operator|.
name|client
operator|.
name|KeyPressEvent
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
operator|.
name|PositionCallback
import|;
end_import

begin_class
DECL|class|ShowHelpCommand
specifier|public
class|class
name|ShowHelpCommand
extends|extends
name|KeyCommand
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|ShowHelpCommand
name|INSTANCE
init|=
operator|new
name|ShowHelpCommand
argument_list|()
decl_stmt|;
DECL|method|ShowHelpCommand ()
specifier|public
name|ShowHelpCommand
parameter_list|()
block|{
name|super
argument_list|(
literal|0
argument_list|,
literal|'?'
argument_list|,
name|Util
operator|.
name|C
operator|.
name|showHelp
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onKeyPress (final KeyPressEvent event)
specifier|public
name|void
name|onKeyPress
parameter_list|(
specifier|final
name|KeyPressEvent
name|event
parameter_list|)
block|{
specifier|final
name|KeyHelpPopup
name|help
init|=
operator|new
name|KeyHelpPopup
argument_list|()
decl_stmt|;
name|help
operator|.
name|setPopupPositionAndShow
argument_list|(
operator|new
name|PositionCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|setPosition
parameter_list|(
specifier|final
name|int
name|pWidth
parameter_list|,
specifier|final
name|int
name|pHeight
parameter_list|)
block|{
specifier|final
name|int
name|left
init|=
operator|(
name|Window
operator|.
name|getClientWidth
argument_list|()
operator|-
name|pWidth
operator|)
operator|>>
literal|1
decl_stmt|;
specifier|final
name|int
name|wLeft
init|=
name|Window
operator|.
name|getScrollLeft
argument_list|()
decl_stmt|;
specifier|final
name|int
name|wTop
init|=
name|Window
operator|.
name|getScrollTop
argument_list|()
decl_stmt|;
name|help
operator|.
name|setPopupPosition
argument_list|(
name|wLeft
operator|+
name|left
argument_list|,
name|wTop
operator|+
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

