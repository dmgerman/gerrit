begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ProjectList
specifier|public
class|class
name|ProjectList
block|{
DECL|field|projects
specifier|protected
name|List
argument_list|<
name|Project
argument_list|>
name|projects
decl_stmt|;
DECL|field|canCreateProject
specifier|protected
name|boolean
name|canCreateProject
decl_stmt|;
DECL|method|ProjectList ()
specifier|public
name|ProjectList
parameter_list|()
block|{   }
DECL|method|getProjects ()
specifier|public
name|List
argument_list|<
name|Project
argument_list|>
name|getProjects
parameter_list|()
block|{
return|return
name|projects
return|;
block|}
DECL|method|setProjects (List<Project> projects)
specifier|public
name|void
name|setProjects
parameter_list|(
name|List
argument_list|<
name|Project
argument_list|>
name|projects
parameter_list|)
block|{
name|this
operator|.
name|projects
operator|=
name|projects
expr_stmt|;
block|}
DECL|method|canCreateProject ()
specifier|public
name|boolean
name|canCreateProject
parameter_list|()
block|{
return|return
name|canCreateProject
return|;
block|}
DECL|method|setCanCreateProject (boolean canCreateProject)
specifier|public
name|void
name|setCanCreateProject
parameter_list|(
name|boolean
name|canCreateProject
parameter_list|)
block|{
name|this
operator|.
name|canCreateProject
operator|=
name|canCreateProject
expr_stmt|;
block|}
block|}
end_class

end_unit

