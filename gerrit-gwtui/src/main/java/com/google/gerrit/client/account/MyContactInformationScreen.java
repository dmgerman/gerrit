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

begin_class
DECL|class|MyContactInformationScreen
specifier|public
class|class
name|MyContactInformationScreen
extends|extends
name|SettingsScreen
block|{
DECL|field|panel
specifier|private
name|ContactPanelFull
name|panel
decl_stmt|;
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
name|panel
operator|=
operator|new
name|ContactPanelFull
argument_list|()
block|{
annotation|@
name|Override
name|void
name|display
parameter_list|()
block|{
name|MyContactInformationScreen
operator|.
name|this
operator|.
name|display
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
name|add
argument_list|(
name|panel
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

