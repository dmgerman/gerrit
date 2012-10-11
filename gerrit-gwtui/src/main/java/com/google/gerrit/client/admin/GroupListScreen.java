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
name|client
operator|.
name|ui
operator|.
name|AccountScreen
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
name|common
operator|.
name|data
operator|.
name|GroupList
import|;
end_import

begin_class
DECL|class|GroupListScreen
specifier|public
class|class
name|GroupListScreen
extends|extends
name|AccountScreen
block|{
DECL|field|groups
specifier|private
name|GroupTable
name|groups
decl_stmt|;
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
name|Util
operator|.
name|GROUP_SVC
operator|.
name|visibleGroups
argument_list|(
operator|new
name|ScreenLoadCallback
argument_list|<
name|GroupList
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
name|GroupList
name|result
parameter_list|)
block|{
name|groups
operator|.
name|display
argument_list|(
name|result
operator|.
name|getGroups
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|.
name|finishDisplay
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
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
name|setPageTitle
argument_list|(
name|Util
operator|.
name|C
operator|.
name|groupListTitle
argument_list|()
argument_list|)
expr_stmt|;
name|groups
operator|=
operator|new
name|GroupTable
argument_list|(
literal|true
comment|/* hyperlink to admin */
argument_list|,
name|PageLinks
operator|.
name|ADMIN_GROUPS
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|groups
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
name|groups
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

