begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|Dispatcher
import|;
end_import

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
name|admin
operator|.
name|ProjectScreen
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_class
DECL|class|ProjectLinkMenuItem
specifier|public
class|class
name|ProjectLinkMenuItem
extends|extends
name|LinkMenuItem
block|{
DECL|field|panel
specifier|protected
specifier|final
name|String
name|panel
decl_stmt|;
DECL|method|ProjectLinkMenuItem (String text, String panel)
specifier|public
name|ProjectLinkMenuItem
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|panel
parameter_list|)
block|{
name|super
argument_list|(
name|text
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|this
operator|.
name|panel
operator|=
name|panel
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onScreenLoad (ScreenLoadEvent event)
specifier|public
name|void
name|onScreenLoad
parameter_list|(
name|ScreenLoadEvent
name|event
parameter_list|)
block|{
name|Screen
name|screen
init|=
name|event
operator|.
name|getScreen
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|projectKey
decl_stmt|;
if|if
condition|(
name|screen
operator|instanceof
name|ProjectScreen
condition|)
block|{
name|projectKey
operator|=
operator|(
operator|(
name|ProjectScreen
operator|)
name|screen
operator|)
operator|.
name|getProjectKey
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|projectKey
operator|=
name|ProjectScreen
operator|.
name|getSavedKey
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|projectKey
operator|!=
literal|null
condition|)
block|{
name|setVisible
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|onScreenLoad
argument_list|(
name|projectKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setVisible
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|onScreenLoad
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
DECL|method|onScreenLoad (Project.NameKey project)
specifier|protected
name|void
name|onScreenLoad
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|setTargetHistoryToken
argument_list|(
name|Dispatcher
operator|.
name|toProjectAdmin
argument_list|(
name|project
argument_list|,
name|panel
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

