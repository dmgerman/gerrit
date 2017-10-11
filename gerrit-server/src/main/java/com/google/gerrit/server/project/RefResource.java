begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|server
operator|.
name|CurrentUser
import|;
end_import

begin_class
DECL|class|RefResource
specifier|public
specifier|abstract
class|class
name|RefResource
extends|extends
name|ProjectResource
block|{
DECL|method|RefResource (ProjectState projectState, CurrentUser user)
specifier|public
name|RefResource
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|super
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
comment|/** @return the ref's name */
DECL|method|getRef ()
specifier|public
specifier|abstract
name|String
name|getRef
parameter_list|()
function_decl|;
comment|/** @return the ref's revision */
DECL|method|getRevision ()
specifier|public
specifier|abstract
name|String
name|getRevision
parameter_list|()
function_decl|;
block|}
end_class

end_unit

