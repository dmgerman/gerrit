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
name|Plugin
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
name|plugin
operator|.
name|client
operator|.
name|PluginEntryPoint
import|;
end_import

begin_import
import|import
name|$
block|{
package|package
block|}
end_import

begin_expr_stmt
operator|.
name|client
operator|.
name|HelloScreen
expr_stmt|;
end_expr_stmt

begin_comment
comment|/**  * HelloWorld Plugin.  */
end_comment

begin_class
DECL|class|HelloPlugin
specifier|public
class|class
name|HelloPlugin
extends|extends
name|PluginEntryPoint
block|{
annotation|@
name|Override
DECL|method|onPluginLoad ()
specifier|public
name|void
name|onPluginLoad
parameter_list|()
block|{
name|Plugin
operator|.
name|get
argument_list|()
operator|.
name|screen
argument_list|(
literal|""
argument_list|,
operator|new
name|HelloScreen
operator|.
name|Factory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

