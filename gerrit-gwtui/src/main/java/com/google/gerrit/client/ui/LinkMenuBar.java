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
name|Command
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
name|Accessibility
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
name|Widget
import|;
end_import

begin_class
DECL|class|LinkMenuBar
specifier|public
class|class
name|LinkMenuBar
extends|extends
name|Composite
block|{
DECL|field|body
specifier|private
specifier|final
name|FlowPanel
name|body
decl_stmt|;
DECL|method|LinkMenuBar ()
specifier|public
name|LinkMenuBar
parameter_list|()
block|{
name|body
operator|=
operator|new
name|FlowPanel
argument_list|()
expr_stmt|;
name|initWidget
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
name|Gerrit
operator|.
name|RESOURCES
operator|.
name|css
argument_list|()
operator|.
name|linkMenuBar
argument_list|()
argument_list|)
expr_stmt|;
name|Accessibility
operator|.
name|setRole
argument_list|(
name|getElement
argument_list|()
argument_list|,
name|Accessibility
operator|.
name|ROLE_MENUBAR
argument_list|)
expr_stmt|;
block|}
DECL|method|addItem (final String text, final Command imp)
specifier|public
name|void
name|addItem
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Command
name|imp
parameter_list|)
block|{
name|add
argument_list|(
operator|new
name|CommandMenuItem
argument_list|(
name|text
argument_list|,
name|imp
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addItem (final CommandMenuItem i)
specifier|public
name|void
name|addItem
parameter_list|(
specifier|final
name|CommandMenuItem
name|i
parameter_list|)
block|{
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
DECL|method|addItem (final LinkMenuItem i)
specifier|public
name|void
name|addItem
parameter_list|(
specifier|final
name|LinkMenuItem
name|i
parameter_list|)
block|{
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|body
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|add (final Widget i)
specifier|public
name|void
name|add
parameter_list|(
specifier|final
name|Widget
name|i
parameter_list|)
block|{
if|if
condition|(
name|body
operator|.
name|getWidgetCount
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|Widget
name|p
init|=
name|body
operator|.
name|getWidget
argument_list|(
name|body
operator|.
name|getWidgetCount
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|p
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
name|linkMenuItemNotLast
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|body
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

