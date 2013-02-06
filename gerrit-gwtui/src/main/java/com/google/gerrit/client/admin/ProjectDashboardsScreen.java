begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.admin
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|admin
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
name|dashboards
operator|.
name|DashboardList
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
name|dashboards
operator|.
name|DashboardsTable
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
name|rpc
operator|.
name|ScreenLoadCallback
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
name|JsArray
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

begin_class
DECL|class|ProjectDashboardsScreen
specifier|public
class|class
name|ProjectDashboardsScreen
extends|extends
name|ProjectScreen
block|{
DECL|field|dashes
specifier|private
name|DashboardsTable
name|dashes
decl_stmt|;
DECL|field|project
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|method|ProjectDashboardsScreen (final Project.NameKey project)
specifier|public
name|ProjectDashboardsScreen
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|super
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
block|{
name|super
operator|.
name|onLoad
argument_list|()
expr_stmt|;
name|DashboardList
operator|.
name|all
argument_list|(
name|getProjectKey
argument_list|()
argument_list|,
operator|new
name|ScreenLoadCallback
argument_list|<
name|JsArray
argument_list|<
name|DashboardList
argument_list|>
argument_list|>
argument_list|(
name|this
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|void
name|preDisplay
parameter_list|(
name|JsArray
argument_list|<
name|DashboardList
argument_list|>
name|result
parameter_list|)
block|{
name|dashes
operator|.
name|display
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|savedPanel
operator|=
name|DASHBOARDS
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
name|dashes
operator|=
operator|new
name|DashboardsTable
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|FlowPanel
name|fp
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|fp
operator|.
name|add
argument_list|(
name|dashes
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|fp
argument_list|)
expr_stmt|;
name|dashes
operator|.
name|setSavePointerId
argument_list|(
literal|"dashboards/project/"
operator|+
name|getProjectKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|display
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|registerKeys ()
specifier|public
name|void
name|registerKeys
parameter_list|()
block|{
name|super
operator|.
name|registerKeys
argument_list|()
expr_stmt|;
name|dashes
operator|.
name|setRegisterKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

