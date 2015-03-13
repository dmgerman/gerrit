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
name|common
operator|.
name|errors
operator|.
name|ProjectCreationFailedException
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|ProjectInput
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
name|common
operator|.
name|ProjectInfo
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
name|BadRequestException
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
name|ResourceConflictException
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
name|ResourceNotFoundException
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
name|extensions
operator|.
name|restapi
operator|.
name|TopLevelResource
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
name|UnprocessableEntityException
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
name|CreateProject
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
name|ProjectJson
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
name|gerrit
operator|.
name|server
operator|.
name|project
operator|.
name|ProjectsCollection
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
name|Provider
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
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
DECL|method|create (String name)
name|ProjectApiImpl
name|create
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
DECL|field|createProjectFactory
specifier|private
specifier|final
name|Provider
argument_list|<
name|CreateProject
operator|.
name|Factory
argument_list|>
name|createProjectFactory
decl_stmt|;
DECL|field|projectApi
specifier|private
specifier|final
name|ProjectApiImpl
operator|.
name|Factory
name|projectApi
decl_stmt|;
DECL|field|projects
specifier|private
specifier|final
name|ProjectsCollection
name|projects
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
DECL|field|projectJson
specifier|private
specifier|final
name|ProjectJson
name|projectJson
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
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
name|AssistedInject
DECL|method|ProjectApiImpl (Provider<CreateProject.Factory> createProjectFactory, ProjectApiImpl.Factory projectApi, ProjectsCollection projects, ProjectJson projectJson, BranchApiImpl.Factory branchApiFactory, @Assisted ProjectResource project)
name|ProjectApiImpl
parameter_list|(
name|Provider
argument_list|<
name|CreateProject
operator|.
name|Factory
argument_list|>
name|createProjectFactory
parameter_list|,
name|ProjectApiImpl
operator|.
name|Factory
name|projectApi
parameter_list|,
name|ProjectsCollection
name|projects
parameter_list|,
name|ProjectJson
name|projectJson
parameter_list|,
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
argument_list|(
name|createProjectFactory
argument_list|,
name|projectApi
argument_list|,
name|projects
argument_list|,
name|projectJson
argument_list|,
name|branchApiFactory
argument_list|,
name|project
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|ProjectApiImpl (Provider<CreateProject.Factory> createProjectFactory, ProjectApiImpl.Factory projectApi, ProjectsCollection projects, ProjectJson projectJson, BranchApiImpl.Factory branchApiFactory, @Assisted String name)
name|ProjectApiImpl
parameter_list|(
name|Provider
argument_list|<
name|CreateProject
operator|.
name|Factory
argument_list|>
name|createProjectFactory
parameter_list|,
name|ProjectApiImpl
operator|.
name|Factory
name|projectApi
parameter_list|,
name|ProjectsCollection
name|projects
parameter_list|,
name|ProjectJson
name|projectJson
parameter_list|,
name|BranchApiImpl
operator|.
name|Factory
name|branchApiFactory
parameter_list|,
annotation|@
name|Assisted
name|String
name|name
parameter_list|)
block|{
name|this
argument_list|(
name|createProjectFactory
argument_list|,
name|projectApi
argument_list|,
name|projects
argument_list|,
name|projectJson
argument_list|,
name|branchApiFactory
argument_list|,
literal|null
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectApiImpl (Provider<CreateProject.Factory> createProjectFactory, ProjectApiImpl.Factory projectApi, ProjectsCollection projects, ProjectJson projectJson, BranchApiImpl.Factory branchApiFactory, ProjectResource project, String name)
specifier|private
name|ProjectApiImpl
parameter_list|(
name|Provider
argument_list|<
name|CreateProject
operator|.
name|Factory
argument_list|>
name|createProjectFactory
parameter_list|,
name|ProjectApiImpl
operator|.
name|Factory
name|projectApi
parameter_list|,
name|ProjectsCollection
name|projects
parameter_list|,
name|ProjectJson
name|projectJson
parameter_list|,
name|BranchApiImpl
operator|.
name|Factory
name|branchApiFactory
parameter_list|,
name|ProjectResource
name|project
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|createProjectFactory
operator|=
name|createProjectFactory
expr_stmt|;
name|this
operator|.
name|projectApi
operator|=
name|projectApi
expr_stmt|;
name|this
operator|.
name|projects
operator|=
name|projects
expr_stmt|;
name|this
operator|.
name|projectJson
operator|=
name|projectJson
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
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
DECL|method|create ()
specifier|public
name|ProjectApi
name|create
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|create
argument_list|(
operator|new
name|ProjectInput
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|create (ProjectInput in)
specifier|public
name|ProjectApi
name|create
parameter_list|(
name|ProjectInput
name|in
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"Project already exists"
argument_list|)
throw|;
block|}
if|if
condition|(
name|in
operator|.
name|name
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|equals
argument_list|(
name|in
operator|.
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"name must match input.name"
argument_list|)
throw|;
block|}
name|createProjectFactory
operator|.
name|get
argument_list|()
operator|.
name|create
argument_list|(
name|name
argument_list|)
operator|.
name|apply
argument_list|(
name|TopLevelResource
operator|.
name|INSTANCE
argument_list|,
name|in
argument_list|)
expr_stmt|;
return|return
name|projectApi
operator|.
name|create
argument_list|(
name|projects
operator|.
name|parse
argument_list|(
name|name
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|BadRequestException
decl||
name|UnprocessableEntityException
decl||
name|ResourceNotFoundException
decl||
name|ProjectCreationFailedException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"Cannot create project: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ProjectInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|projectJson
operator|.
name|format
argument_list|(
name|project
argument_list|)
return|;
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
throws|throws
name|ResourceNotFoundException
block|{
return|return
name|branchApi
operator|.
name|create
argument_list|(
name|checkExists
argument_list|()
argument_list|,
name|ref
argument_list|)
return|;
block|}
DECL|method|checkExists ()
specifier|private
name|ProjectResource
name|checkExists
parameter_list|()
throws|throws
name|ResourceNotFoundException
block|{
if|if
condition|(
name|project
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|name
argument_list|)
throw|;
block|}
return|return
name|project
return|;
block|}
block|}
end_class

end_unit

