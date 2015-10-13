begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|account
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
name|gerrit
operator|.
name|client
operator|.
name|editor
operator|.
name|EditPreferencesBox
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
DECL|class|MyEditPreferencesScreen
specifier|public
class|class
name|MyEditPreferencesScreen
extends|extends
name|SettingsScreen
block|{
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
name|EditPreferencesBox
name|pb
init|=
operator|new
name|EditPreferencesBox
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|pb
operator|.
name|set
argument_list|(
name|EditPreferences
operator|.
name|create
argument_list|(
name|Gerrit
operator|.
name|getEditPreferences
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|FlowPanel
name|p
init|=
operator|new
name|FlowPanel
argument_list|()
decl_stmt|;
name|p
operator|.
name|setStyleName
argument_list|(
name|pb
operator|.
name|getStyle
argument_list|()
operator|.
name|dialog
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|add
argument_list|(
name|pb
argument_list|)
expr_stmt|;
name|add
argument_list|(
name|p
argument_list|)
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
name|display
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

