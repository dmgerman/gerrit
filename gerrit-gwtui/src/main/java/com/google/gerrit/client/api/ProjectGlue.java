begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.client.api
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|api
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
name|actions
operator|.
name|ActionButton
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
name|actions
operator|.
name|ActionInfo
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
name|projects
operator|.
name|ProjectApi
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
name|RestApi
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
name|JavaScriptObject
import|;
end_import

begin_class
DECL|class|ProjectGlue
specifier|public
class|class
name|ProjectGlue
block|{
DECL|method|onAction ( Project.NameKey project, ActionInfo action, ActionButton button)
specifier|public
specifier|static
name|void
name|onAction
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|ActionInfo
name|action
parameter_list|,
name|ActionButton
name|button
parameter_list|)
block|{
name|RestApi
name|api
init|=
name|ProjectApi
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|view
argument_list|(
name|action
operator|.
name|id
argument_list|()
argument_list|)
decl_stmt|;
name|JavaScriptObject
name|f
init|=
name|get
argument_list|(
name|action
operator|.
name|id
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|ActionContext
name|c
init|=
name|ActionContext
operator|.
name|create
argument_list|(
name|api
argument_list|)
decl_stmt|;
name|c
operator|.
name|set
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|c
operator|.
name|set
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|c
operator|.
name|button
argument_list|(
name|button
argument_list|)
expr_stmt|;
name|ApiGlue
operator|.
name|invoke
argument_list|(
name|f
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|DefaultActions
operator|.
name|invokeProjectAction
argument_list|(
name|action
argument_list|,
name|api
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|get (String id)
specifier|private
specifier|static
specifier|final
specifier|native
name|JavaScriptObject
name|get
parameter_list|(
name|String
name|id
parameter_list|)
comment|/*-{     return $wnd.Gerrit.project_actions[id];   }-*/
function_decl|;
DECL|method|ProjectGlue ()
specifier|private
name|ProjectGlue
parameter_list|()
block|{   }
block|}
end_class

end_unit

