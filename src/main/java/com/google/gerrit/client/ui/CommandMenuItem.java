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
name|event
operator|.
name|dom
operator|.
name|client
operator|.
name|ClickEvent
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
name|dom
operator|.
name|client
operator|.
name|ClickHandler
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
name|Anchor
import|;
end_import

begin_class
DECL|class|CommandMenuItem
specifier|public
class|class
name|CommandMenuItem
extends|extends
name|Anchor
implements|implements
name|ClickHandler
block|{
DECL|field|command
specifier|private
specifier|final
name|Command
name|command
decl_stmt|;
DECL|method|CommandMenuItem (final String text, final Command cmd)
specifier|public
name|CommandMenuItem
parameter_list|(
specifier|final
name|String
name|text
parameter_list|,
specifier|final
name|Command
name|cmd
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|)
expr_stmt|;
name|setStyleName
argument_list|(
literal|"gerrit-MenuItem"
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
name|ROLE_MENUITEM
argument_list|)
expr_stmt|;
name|addClickHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|command
operator|=
name|cmd
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClick (final ClickEvent event)
specifier|public
name|void
name|onClick
parameter_list|(
specifier|final
name|ClickEvent
name|event
parameter_list|)
block|{
name|setFocus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

