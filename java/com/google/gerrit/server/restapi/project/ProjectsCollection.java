begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.restapi.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|common
operator|.
name|Nullable
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
name|registration
operator|.
name|DynamicMap
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
name|AcceptsCreate
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
name|AuthException
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
name|IdString
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
name|NeedsParams
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
name|RestCollection
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
name|RestView
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
name|gerrit
operator|.
name|server
operator|.
name|CurrentUser
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
name|OutputFormat
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
name|permissions
operator|.
name|PermissionBackend
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
name|permissions
operator|.
name|PermissionBackendException
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
name|permissions
operator|.
name|ProjectPermission
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
name|ProjectCache
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
name|ProjectState
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
name|Singleton
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|ProjectsCollection
specifier|public
class|class
name|ProjectsCollection
implements|implements
name|RestCollection
argument_list|<
name|TopLevelResource
argument_list|,
name|ProjectResource
argument_list|>
implements|,
name|AcceptsCreate
argument_list|<
name|TopLevelResource
argument_list|>
implements|,
name|NeedsParams
block|{
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListProjects
argument_list|>
name|list
decl_stmt|;
DECL|field|queryProjects
specifier|private
specifier|final
name|Provider
argument_list|<
name|QueryProjects
argument_list|>
name|queryProjects
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
decl_stmt|;
DECL|field|createProjectFactory
specifier|private
specifier|final
name|CreateProject
operator|.
name|Factory
name|createProjectFactory
decl_stmt|;
DECL|field|hasQuery
specifier|private
name|boolean
name|hasQuery
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectsCollection ( DynamicMap<RestView<ProjectResource>> views, Provider<ListProjects> list, Provider<QueryProjects> queryProjects, ProjectCache projectCache, PermissionBackend permissionBackend, CreateProject.Factory factory, Provider<CurrentUser> user)
specifier|public
name|ProjectsCollection
parameter_list|(
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListProjects
argument_list|>
name|list
parameter_list|,
name|Provider
argument_list|<
name|QueryProjects
argument_list|>
name|queryProjects
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|CreateProject
operator|.
name|Factory
name|factory
parameter_list|,
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|user
parameter_list|)
block|{
name|this
operator|.
name|views
operator|=
name|views
expr_stmt|;
name|this
operator|.
name|list
operator|=
name|list
expr_stmt|;
name|this
operator|.
name|queryProjects
operator|=
name|queryProjects
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|createProjectFactory
operator|=
name|factory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setParams (ListMultimap<String, String> params)
specifier|public
name|void
name|setParams
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
throws|throws
name|BadRequestException
block|{
comment|// The --query option is defined in QueryProjects
name|this
operator|.
name|hasQuery
operator|=
name|params
operator|.
name|containsKey
argument_list|(
literal|"query"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|TopLevelResource
argument_list|>
name|list
parameter_list|()
block|{
if|if
condition|(
name|hasQuery
condition|)
block|{
return|return
name|queryProjects
operator|.
name|get
argument_list|()
return|;
block|}
return|return
name|list
operator|.
name|get
argument_list|()
operator|.
name|setFormat
argument_list|(
name|OutputFormat
operator|.
name|JSON
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parse (TopLevelResource parent, IdString id)
specifier|public
name|ProjectResource
name|parse
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
name|ProjectResource
name|rsrc
init|=
name|_parse
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsrc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
return|return
name|rsrc
return|;
block|}
comment|/**    * Parses a project ID from a request body and returns the project.    *    * @param id ID of the project, can be a project name    * @return the project    * @throws RestApiException thrown if the project ID cannot be resolved or if the project is not    *     visible to the calling user    * @throws IOException thrown when there is an error.    * @throws PermissionBackendException    */
DECL|method|parse (String id)
specifier|public
name|ProjectResource
name|parse
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
return|return
name|parse
argument_list|(
name|id
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**    * Parses a project ID from a request body and returns the project.    *    * @param id ID of the project, can be a project name    * @param checkAccess if true, check the project is accessible by the current user    * @return the project    * @throws RestApiException thrown if the project ID cannot be resolved or if the project is not    *     visible to the calling user and checkVisibility is true.    * @throws IOException thrown when there is an error.    * @throws PermissionBackendException    */
DECL|method|parse (String id, boolean checkAccess)
specifier|public
name|ProjectResource
name|parse
parameter_list|(
name|String
name|id
parameter_list|,
name|boolean
name|checkAccess
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
name|ProjectResource
name|rsrc
init|=
name|_parse
argument_list|(
name|id
argument_list|,
name|checkAccess
argument_list|)
decl_stmt|;
if|if
condition|(
name|rsrc
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Project Not Found: %s"
argument_list|,
name|id
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|rsrc
return|;
block|}
annotation|@
name|Nullable
DECL|method|_parse (String id, boolean checkAccess)
specifier|private
name|ProjectResource
name|_parse
parameter_list|(
name|String
name|id
parameter_list|,
name|boolean
name|checkAccess
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|ResourceConflictException
block|{
if|if
condition|(
name|id
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|id
operator|.
name|length
argument_list|()
operator|-
name|Constants
operator|.
name|DOT_GIT_EXT
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Project
operator|.
name|NameKey
name|nameKey
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|ProjectState
name|state
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|nameKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|checkAccess
condition|)
block|{
comment|// Hidden projects(permitsRead = false) should only be accessible by the project owners.
comment|// READ_CONFIG is checked here because it's only allowed to project owners(ACCESS may also
comment|// be allowed for other users). Allowing project owners to access here will help them to view
comment|// and update the config of hidden projects easily.
name|ProjectPermission
name|permissionToCheck
init|=
name|state
operator|.
name|statePermitsRead
argument_list|()
condition|?
name|ProjectPermission
operator|.
name|ACCESS
else|:
name|ProjectPermission
operator|.
name|READ_CONFIG
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|nameKey
argument_list|)
operator|.
name|check
argument_list|(
name|permissionToCheck
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
comment|// Pretend like not found on access denied.
block|}
comment|// If the project's state does not permit reading, we want to hide it from all callers. The
comment|// only exception to that are users who are allowed to mutate the project's configuration.
comment|// This enables these users to still mutate the project's state (e.g. set a HIDDEN project to
comment|// ACTIVE). Individual views should still check for checkStatePermitsRead() and this should
comment|// just serve as a safety net in case the individual check is forgotten.
try|try
block|{
name|permissionBackend
operator|.
name|currentUser
argument_list|()
operator|.
name|project
argument_list|(
name|nameKey
argument_list|)
operator|.
name|check
argument_list|(
name|ProjectPermission
operator|.
name|WRITE_CONFIG
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|state
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ProjectResource
argument_list|(
name|state
argument_list|,
name|user
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
annotation|@
name|Override
DECL|method|create (TopLevelResource parent, IdString name)
specifier|public
name|CreateProject
name|create
parameter_list|(
name|TopLevelResource
name|parent
parameter_list|,
name|IdString
name|name
parameter_list|)
block|{
return|return
name|createProjectFactory
operator|.
name|create
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

