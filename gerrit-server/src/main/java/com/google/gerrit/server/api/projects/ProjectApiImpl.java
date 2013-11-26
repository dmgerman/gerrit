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
DECL|package|com.google.gerrit.server.api.projects
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|api
operator|.
name|projects
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BranchApi
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
name|extensions
operator|.
name|api
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
name|server
operator|.
name|project
operator|.
name|ProjectResource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_class
DECL|class|ProjectApiImpl
specifier|public
class|class
name|ProjectApiImpl
implements|implements
name|ProjectApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ProjectResource project)
name|ProjectApiImpl
name|create
parameter_list|(
name|ProjectResource
name|project
parameter_list|)
function_decl|;
block|}
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
DECL|field|branchApi
specifier|private
specifier|final
name|BranchApiImpl
operator|.
name|Factory
name|branchApi
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectApiImpl ( BranchApiImpl.Factory branchApiFactory, @Assisted ProjectResource project)
name|ProjectApiImpl
parameter_list|(
name|BranchApiImpl
operator|.
name|Factory
name|branchApiFactory
parameter_list|,
annotation|@
name|Assisted
name|ProjectResource
name|project
parameter_list|)
block|{
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|branchApi
operator|=
name|branchApiFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|branch (String ref)
specifier|public
name|BranchApi
name|branch
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
name|branchApi
operator|.
name|create
argument_list|(
name|project
argument_list|,
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit

