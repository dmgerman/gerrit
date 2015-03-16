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
name|BranchInput
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
name|restapi
operator|.
name|RestApiException
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
name|CreateBranch
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|BranchApiImpl
specifier|public
class|class
name|BranchApiImpl
implements|implements
name|BranchApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ProjectResource project, String ref)
name|BranchApiImpl
name|create
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
block|}
DECL|field|createBranchFactory
specifier|private
specifier|final
name|CreateBranch
operator|.
name|Factory
name|createBranchFactory
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
annotation|@
name|Inject
DECL|method|BranchApiImpl ( CreateBranch.Factory createBranchFactory, @Assisted ProjectResource project, @Assisted String ref)
name|BranchApiImpl
parameter_list|(
name|CreateBranch
operator|.
name|Factory
name|createBranchFactory
parameter_list|,
annotation|@
name|Assisted
name|ProjectResource
name|project
parameter_list|,
annotation|@
name|Assisted
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|createBranchFactory
operator|=
name|createBranchFactory
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|create (BranchInput in)
specifier|public
name|BranchApi
name|create
parameter_list|(
name|BranchInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
name|CreateBranch
operator|.
name|Input
name|input
init|=
operator|new
name|CreateBranch
operator|.
name|Input
argument_list|()
decl_stmt|;
name|input
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|input
operator|.
name|revision
operator|=
name|in
operator|.
name|revision
expr_stmt|;
name|createBranchFactory
operator|.
name|create
argument_list|(
name|ref
argument_list|)
operator|.
name|apply
argument_list|(
name|project
argument_list|,
name|input
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot create branch"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

