begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
name|HorizontalPanel
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

begin_class
DECL|class|MenuScreen
specifier|public
specifier|abstract
class|class
name|MenuScreen
extends|extends
name|Screen
block|{
DECL|field|menu
specifier|private
specifier|final
name|LinkMenuBar
name|menu
decl_stmt|;
DECL|field|body
specifier|private
specifier|final
name|FlowPanel
name|body
decl_stmt|;
DECL|method|MenuScreen ()
specifier|public
name|MenuScreen
parameter_list|()
block|{
name|menu
operator|=
operator|new
name|LinkMenuBar
argument_list|()
expr_stmt|;
name|menu
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|menuScreenMenuBar
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInitUI ()
specifier|protected
name|void
name|onInitUI
parameter_list|()
block|{
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|HorizontalPanel
name|hp
init|=
operator|new
name|HorizontalPanel
argument_list|()
decl_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|menu
argument_list|)
expr_stmt|;
name|hp
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|super
operator|.
name|add
argument_list|(
name|hp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setToken (String token)
specifier|public
name|void
name|setToken
parameter_list|(
name|String
name|token
parameter_list|)
block|{
name|LinkMenuItem
name|self
init|=
name|menu
operator|.
name|find
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|self
operator|!=
literal|null
condition|)
block|{
name|self
operator|.
name|addStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|activeRow
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (final Widget w)
specifier|protected
name|void
name|add
parameter_list|(
specifier|final
name|Widget
name|w
parameter_list|)
block|{
name|body
operator|.
name|add
argument_list|(
name|w
argument_list|)
expr_stmt|;
block|}
DECL|method|link (String text, String target)
specifier|protected
name|void
name|link
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|target
parameter_list|)
block|{
specifier|final
name|LinkMenuItem
name|item
init|=
operator|new
name|LinkMenuItem
argument_list|(
name|text
argument_list|,
name|target
argument_list|)
decl_stmt|;
name|item
operator|.
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|menuItem
argument_list|()
argument_list|)
expr_stmt|;
name|menu
operator|.
name|add
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

