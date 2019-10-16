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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|RefNames
operator|.
name|REFS_DASHBOARDS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Joiner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Splitter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
import|;
end_import

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
name|Lists
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
name|entities
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|DashboardInfo
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
name|DashboardSectionInfo
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
name|ChildCollection
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
name|Url
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
name|UrlEncoded
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
name|git
operator|.
name|GitRepositoryManager
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
name|RefPermission
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
name|DashboardResource
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
name|java
operator|.
name|util
operator|.
name|List
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
name|errors
operator|.
name|AmbiguousObjectException
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
name|errors
operator|.
name|ConfigInvalidException
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
name|errors
operator|.
name|IncorrectObjectTypeException
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
name|errors
operator|.
name|RepositoryNotFoundException
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
name|BlobBasedConfig
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
name|Config
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
name|ObjectId
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
name|Repository
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DashboardsCollection
specifier|public
class|class
name|DashboardsCollection
implements|implements
name|ChildCollection
argument_list|<
name|ProjectResource
argument_list|,
name|DashboardResource
argument_list|>
block|{
DECL|field|DEFAULT_DASHBOARD_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_DASHBOARD_NAME
init|=
literal|"default"
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
decl_stmt|;
DECL|field|views
specifier|private
specifier|final
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|DashboardResource
argument_list|>
argument_list|>
name|views
decl_stmt|;
DECL|field|list
specifier|private
specifier|final
name|Provider
argument_list|<
name|ListDashboards
argument_list|>
name|list
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
annotation|@
name|Inject
DECL|method|DashboardsCollection ( GitRepositoryManager gitManager, DynamicMap<RestView<DashboardResource>> views, Provider<ListDashboards> list, PermissionBackend permissionBackend)
name|DashboardsCollection
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|,
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|DashboardResource
argument_list|>
argument_list|>
name|views
parameter_list|,
name|Provider
argument_list|<
name|ListDashboards
argument_list|>
name|list
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|)
block|{
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
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
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
block|}
DECL|method|isDefaultDashboard (@ullable String id)
specifier|public
specifier|static
name|boolean
name|isDefaultDashboard
parameter_list|(
annotation|@
name|Nullable
name|String
name|id
parameter_list|)
block|{
return|return
name|DEFAULT_DASHBOARD_NAME
operator|.
name|equals
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|isDefaultDashboard (@ullable IdString id)
specifier|public
specifier|static
name|boolean
name|isDefaultDashboard
parameter_list|(
annotation|@
name|Nullable
name|IdString
name|id
parameter_list|)
block|{
return|return
name|id
operator|!=
literal|null
operator|&&
name|isDefaultDashboard
argument_list|(
name|id
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|RestView
argument_list|<
name|ProjectResource
argument_list|>
name|list
parameter_list|()
throws|throws
name|ResourceNotFoundException
block|{
return|return
name|list
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|parse (ProjectResource parent, IdString id)
specifier|public
name|DashboardResource
name|parse
parameter_list|(
name|ProjectResource
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
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
name|parent
operator|.
name|getProjectState
argument_list|()
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
if|if
condition|(
name|isDefaultDashboard
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|DashboardResource
operator|.
name|projectDefault
argument_list|(
name|parent
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|parent
operator|.
name|getUser
argument_list|()
argument_list|)
return|;
block|}
name|DashboardInfo
name|info
decl_stmt|;
try|try
block|{
name|info
operator|=
name|newDashboardInfo
argument_list|(
name|id
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidDashboardId
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
for|for
control|(
name|ProjectState
name|ps
range|:
name|parent
operator|.
name|getProjectState
argument_list|()
operator|.
name|tree
argument_list|()
control|)
block|{
try|try
block|{
return|return
name|parse
argument_list|(
name|ps
argument_list|,
name|parent
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|parent
operator|.
name|getUser
argument_list|()
argument_list|,
name|info
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|AmbiguousObjectException
decl||
name|ConfigInvalidException
decl||
name|IncorrectObjectTypeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
continue|continue;
block|}
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
DECL|method|normalizeDashboardRef (String ref)
specifier|public
specifier|static
name|String
name|normalizeDashboardRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ref
operator|.
name|startsWith
argument_list|(
name|REFS_DASHBOARDS
argument_list|)
condition|)
block|{
return|return
name|REFS_DASHBOARDS
operator|+
name|ref
return|;
block|}
return|return
name|ref
return|;
block|}
DECL|method|parse ( ProjectState parent, ProjectState current, CurrentUser user, DashboardInfo info)
specifier|private
name|DashboardResource
name|parse
parameter_list|(
name|ProjectState
name|parent
parameter_list|,
name|ProjectState
name|current
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|DashboardInfo
name|info
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|AmbiguousObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
throws|,
name|ResourceConflictException
block|{
name|String
name|ref
init|=
name|normalizeDashboardRef
argument_list|(
name|info
operator|.
name|ref
argument_list|)
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|project
argument_list|(
name|parent
operator|.
name|getNameKey
argument_list|()
argument_list|)
operator|.
name|ref
argument_list|(
name|ref
argument_list|)
operator|.
name|check
argument_list|(
name|RefPermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
comment|// Don't leak the project's existence
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|info
operator|.
name|id
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|ref
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|info
operator|.
name|id
argument_list|)
throw|;
block|}
name|current
operator|.
name|checkStatePermitsRead
argument_list|()
expr_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|parent
operator|.
name|getNameKey
argument_list|()
argument_list|)
init|)
block|{
name|ObjectId
name|objId
init|=
name|git
operator|.
name|resolve
argument_list|(
name|ref
operator|+
literal|":"
operator|+
name|info
operator|.
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|objId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|info
operator|.
name|id
argument_list|)
throw|;
block|}
name|BlobBasedConfig
name|cfg
init|=
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|git
argument_list|,
name|objId
argument_list|)
decl_stmt|;
return|return
operator|new
name|DashboardResource
argument_list|(
name|current
argument_list|,
name|user
argument_list|,
name|ref
argument_list|,
name|info
operator|.
name|path
argument_list|,
name|cfg
argument_list|,
literal|false
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|info
operator|.
name|id
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|views ()
specifier|public
name|DynamicMap
argument_list|<
name|RestView
argument_list|<
name|DashboardResource
argument_list|>
argument_list|>
name|views
parameter_list|()
block|{
return|return
name|views
return|;
block|}
DECL|method|newDashboardInfo (String ref, String path)
specifier|public
specifier|static
name|DashboardInfo
name|newDashboardInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|DashboardInfo
name|info
init|=
operator|new
name|DashboardInfo
argument_list|()
decl_stmt|;
name|info
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|info
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|info
operator|.
name|id
operator|=
name|Joiner
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|join
argument_list|(
name|Url
operator|.
name|encode
argument_list|(
name|ref
argument_list|)
argument_list|,
name|Url
operator|.
name|encode
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|class|InvalidDashboardId
specifier|public
specifier|static
class|class
name|InvalidDashboardId
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|InvalidDashboardId (String id)
specifier|public
name|InvalidDashboardId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|newDashboardInfo (String id)
specifier|static
name|DashboardInfo
name|newDashboardInfo
parameter_list|(
name|String
name|id
parameter_list|)
throws|throws
name|InvalidDashboardId
block|{
name|DashboardInfo
name|info
init|=
operator|new
name|DashboardInfo
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|parts
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|':'
argument_list|)
operator|.
name|limit
argument_list|(
literal|2
argument_list|)
operator|.
name|split
argument_list|(
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|size
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|InvalidDashboardId
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|info
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|info
operator|.
name|ref
operator|=
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|info
operator|.
name|path
operator|=
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|parse ( Project definingProject, String refName, String path, Config config, String project, boolean setDefault)
specifier|static
name|DashboardInfo
name|parse
parameter_list|(
name|Project
name|definingProject
parameter_list|,
name|String
name|refName
parameter_list|,
name|String
name|path
parameter_list|,
name|Config
name|config
parameter_list|,
name|String
name|project
parameter_list|,
name|boolean
name|setDefault
parameter_list|)
block|{
name|DashboardInfo
name|info
init|=
name|newDashboardInfo
argument_list|(
name|refName
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|info
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|info
operator|.
name|definingProject
operator|=
name|definingProject
operator|.
name|getName
argument_list|()
expr_stmt|;
name|String
name|title
init|=
name|config
operator|.
name|getString
argument_list|(
literal|"dashboard"
argument_list|,
literal|null
argument_list|,
literal|"title"
argument_list|)
decl_stmt|;
name|info
operator|.
name|title
operator|=
name|replace
argument_list|(
name|project
argument_list|,
name|title
operator|==
literal|null
condition|?
name|info
operator|.
name|path
else|:
name|title
argument_list|)
expr_stmt|;
name|info
operator|.
name|description
operator|=
name|replace
argument_list|(
name|project
argument_list|,
name|config
operator|.
name|getString
argument_list|(
literal|"dashboard"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|foreach
operator|=
name|config
operator|.
name|getString
argument_list|(
literal|"dashboard"
argument_list|,
literal|null
argument_list|,
literal|"foreach"
argument_list|)
expr_stmt|;
if|if
condition|(
name|setDefault
condition|)
block|{
name|String
name|id
init|=
name|refName
operator|+
literal|":"
operator|+
name|path
decl_stmt|;
name|info
operator|.
name|isDefault
operator|=
name|id
operator|.
name|equals
argument_list|(
name|defaultOf
argument_list|(
name|definingProject
argument_list|)
argument_list|)
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
name|UrlEncoded
name|u
init|=
operator|new
name|UrlEncoded
argument_list|(
literal|"/dashboard/"
argument_list|)
decl_stmt|;
name|u
operator|.
name|put
argument_list|(
literal|"title"
argument_list|,
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|info
operator|.
name|title
argument_list|,
name|info
operator|.
name|path
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|.
name|foreach
operator|!=
literal|null
condition|)
block|{
name|u
operator|.
name|put
argument_list|(
literal|"foreach"
argument_list|,
name|replace
argument_list|(
name|project
argument_list|,
name|info
operator|.
name|foreach
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|name
range|:
name|config
operator|.
name|getSubsections
argument_list|(
literal|"section"
argument_list|)
control|)
block|{
name|DashboardSectionInfo
name|s
init|=
operator|new
name|DashboardSectionInfo
argument_list|()
decl_stmt|;
name|s
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|s
operator|.
name|query
operator|=
name|config
operator|.
name|getString
argument_list|(
literal|"section"
argument_list|,
name|name
argument_list|,
literal|"query"
argument_list|)
expr_stmt|;
name|u
operator|.
name|put
argument_list|(
name|s
operator|.
name|name
argument_list|,
name|replace
argument_list|(
name|project
argument_list|,
name|s
operator|.
name|query
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|sections
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|url
operator|=
name|u
operator|.
name|toString
argument_list|()
operator|.
name|replace
argument_list|(
literal|"%3A"
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|replace (String project, String input)
specifier|private
specifier|static
name|String
name|replace
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|==
literal|null
condition|?
name|input
else|:
name|input
operator|.
name|replace
argument_list|(
literal|"${project}"
argument_list|,
name|project
argument_list|)
return|;
block|}
DECL|method|defaultOf (Project proj)
specifier|private
specifier|static
name|String
name|defaultOf
parameter_list|(
name|Project
name|proj
parameter_list|)
block|{
specifier|final
name|String
name|defaultId
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|proj
operator|.
name|getLocalDefaultDashboard
argument_list|()
argument_list|,
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|proj
operator|.
name|getDefaultDashboard
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultId
operator|.
name|startsWith
argument_list|(
name|REFS_DASHBOARDS
argument_list|)
condition|)
block|{
return|return
name|defaultId
operator|.
name|substring
argument_list|(
name|REFS_DASHBOARDS
operator|.
name|length
argument_list|()
argument_list|)
return|;
block|}
return|return
name|defaultId
return|;
block|}
block|}
end_class

end_unit

