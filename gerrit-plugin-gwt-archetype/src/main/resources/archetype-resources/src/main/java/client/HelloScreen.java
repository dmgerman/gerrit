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
package|package
name|$
block|{
package|package
block|}
end_package

begin_expr_stmt
operator|.
name|client
expr_stmt|;
end_expr_stmt

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|plugin
operator|.
name|client
operator|.
name|screen
operator|.
name|Screen
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
name|Label
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
name|VerticalPanel
import|;
end_import

begin_class
DECL|class|HelloScreen
specifier|public
class|class
name|HelloScreen
extends|extends
name|VerticalPanel
block|{
DECL|class|Factory
specifier|static
class|class
name|Factory
implements|implements
name|Screen
operator|.
name|EntryPoint
block|{
annotation|@
name|Override
DECL|method|onLoad (Screen screen)
specifier|public
name|void
name|onLoad
parameter_list|(
name|Screen
name|screen
parameter_list|)
block|{
name|screen
operator|.
name|setPageTitle
argument_list|(
literal|"Hello"
argument_list|)
expr_stmt|;
name|screen
operator|.
name|show
argument_list|(
operator|new
name|HelloScreen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|HelloScreen ()
name|HelloScreen
parameter_list|()
block|{
name|setStyleName
argument_list|(
literal|"hello-panel"
argument_list|)
expr_stmt|;
name|add
argument_list|(
operator|new
name|Label
argument_list|(
literal|"Hello World Screen"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

