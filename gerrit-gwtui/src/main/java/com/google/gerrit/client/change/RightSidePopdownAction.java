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
DECL|package|com.google.gerrit.client.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|change
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
name|dom
operator|.
name|client
operator|.
name|Document
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
name|Style
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
name|CloseEvent
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
name|CloseHandler
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|globalkey
operator|.
name|client
operator|.
name|GlobalKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|user
operator|.
name|client
operator|.
name|PluginSafePopupPanel
import|;
end_import

begin_class
DECL|class|RightSidePopdownAction
specifier|abstract
class|class
name|RightSidePopdownAction
block|{
DECL|field|style
specifier|private
specifier|final
name|ChangeScreen2
operator|.
name|Style
name|style
decl_stmt|;
DECL|field|button
specifier|private
specifier|final
name|Widget
name|button
decl_stmt|;
DECL|field|relativeTo
specifier|private
specifier|final
name|UIObject
name|relativeTo
decl_stmt|;
DECL|field|popup
specifier|private
name|PopupPanel
name|popup
decl_stmt|;
DECL|method|RightSidePopdownAction ( ChangeScreen2.Style style, UIObject relativeTo, Widget button)
name|RightSidePopdownAction
parameter_list|(
name|ChangeScreen2
operator|.
name|Style
name|style
parameter_list|,
name|UIObject
name|relativeTo
parameter_list|,
name|Widget
name|button
parameter_list|)
block|{
name|this
operator|.
name|style
operator|=
name|style
expr_stmt|;
name|this
operator|.
name|relativeTo
operator|=
name|relativeTo
expr_stmt|;
name|this
operator|.
name|button
operator|=
name|button
expr_stmt|;
block|}
DECL|method|getWidget ()
specifier|abstract
name|Widget
name|getWidget
parameter_list|()
function_decl|;
DECL|method|show ()
name|void
name|show
parameter_list|()
block|{
if|if
condition|(
name|popup
operator|!=
literal|null
condition|)
block|{
name|button
operator|.
name|removeStyleName
argument_list|(
name|style
operator|.
name|selected
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|.
name|hide
argument_list|()
expr_stmt|;
return|return;
block|}
specifier|final
name|PluginSafePopupPanel
name|p
init|=
operator|new
name|PluginSafePopupPanel
argument_list|(
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|setPopupPosition
parameter_list|(
name|int
name|left
parameter_list|,
name|int
name|top
parameter_list|)
block|{
name|top
operator|-=
name|Document
operator|.
name|get
argument_list|()
operator|.
name|getBodyOffsetTop
argument_list|()
expr_stmt|;
name|int
name|w
init|=
name|Window
operator|.
name|getScrollLeft
argument_list|()
operator|+
name|Window
operator|.
name|getClientWidth
argument_list|()
decl_stmt|;
name|int
name|r
init|=
name|relativeTo
operator|.
name|getAbsoluteLeft
argument_list|()
operator|+
name|relativeTo
operator|.
name|getOffsetWidth
argument_list|()
decl_stmt|;
name|int
name|right
init|=
name|w
operator|-
name|r
decl_stmt|;
name|Style
name|style
init|=
name|getElement
argument_list|()
operator|.
name|getStyle
argument_list|()
decl_stmt|;
name|style
operator|.
name|clearProperty
argument_list|(
literal|"left"
argument_list|)
expr_stmt|;
name|style
operator|.
name|setPropertyPx
argument_list|(
literal|"right"
argument_list|,
name|right
argument_list|)
expr_stmt|;
name|style
operator|.
name|setPropertyPx
argument_list|(
literal|"top"
argument_list|,
name|top
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|p
operator|.
name|setStyleName
argument_list|(
name|style
operator|.
name|replyBox
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|addAutoHidePartner
argument_list|(
name|button
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|addCloseHandler
argument_list|(
operator|new
name|CloseHandler
argument_list|<
name|PopupPanel
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|CloseEvent
argument_list|<
name|PopupPanel
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|popup
operator|==
name|p
condition|)
block|{
name|button
operator|.
name|removeStyleName
argument_list|(
name|style
operator|.
name|selected
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|getWidget
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|showRelativeTo
argument_list|(
name|relativeTo
argument_list|)
expr_stmt|;
name|GlobalKey
operator|.
name|dialog
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|button
operator|.
name|addStyleName
argument_list|(
name|style
operator|.
name|selected
argument_list|()
argument_list|)
expr_stmt|;
name|popup
operator|=
name|p
expr_stmt|;
block|}
block|}
end_class

end_unit

