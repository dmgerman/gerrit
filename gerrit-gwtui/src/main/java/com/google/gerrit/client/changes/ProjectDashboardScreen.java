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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|client
operator|.
name|ui
operator|.
name|InlineHyperlink
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
name|common
operator|.
name|PageLinks
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
DECL|class|ProjectDashboardScreen
specifier|public
class|class
name|ProjectDashboardScreen
extends|extends
name|ProjectScreen
implements|implements
name|ChangeListScreen
block|{
DECL|field|table
specifier|private
name|DashboardTable
name|table
decl_stmt|;
DECL|field|params
specifier|private
name|String
name|params
decl_stmt|;
DECL|method|ProjectDashboardScreen (final Project.NameKey toShow, String params)
specifier|public
name|ProjectDashboardScreen
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|toShow
parameter_list|,
name|String
name|params
parameter_list|)
block|{
name|super
argument_list|(
name|toShow
argument_list|)
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|params
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
name|table
operator|=
operator|new
name|DashboardTable
argument_list|(
name|params
argument_list|)
block|{
annotation|@
name|Override
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
name|display
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|String
name|title
init|=
name|table
operator|.
name|getTitle
argument_list|()
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
name|setTitle
argument_list|(
operator|new
name|InlineHyperlink
argument_list|(
name|title
argument_list|,
name|PageLinks
operator|.
name|toCustomDashboard
argument_list|(
name|params
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|onInitUI
argument_list|()
expr_stmt|;
name|add
argument_list|(
name|table
argument_list|)
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
name|table
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

