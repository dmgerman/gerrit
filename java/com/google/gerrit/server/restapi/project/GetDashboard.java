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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_DASHBOARDS
import|;
end_import

begin_import
import|import static
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
operator|.
name|DashboardsCollection
operator|.
name|isDefaultDashboard
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
name|RestReadView
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
name|ConfigInvalidException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|GetDashboard
specifier|public
class|class
name|GetDashboard
implements|implements
name|RestReadView
argument_list|<
name|DashboardResource
argument_list|>
block|{
DECL|field|dashboards
specifier|private
specifier|final
name|DashboardsCollection
name|dashboards
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--inherited"
argument_list|,
name|usage
operator|=
literal|"include inherited dashboards"
argument_list|)
DECL|field|inherited
specifier|private
name|boolean
name|inherited
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetDashboard (DashboardsCollection dashboards)
name|GetDashboard
parameter_list|(
name|DashboardsCollection
name|dashboards
parameter_list|)
block|{
name|this
operator|.
name|dashboards
operator|=
name|dashboards
expr_stmt|;
block|}
DECL|method|setInherited (boolean inherited)
specifier|public
name|GetDashboard
name|setInherited
parameter_list|(
name|boolean
name|inherited
parameter_list|)
block|{
name|this
operator|.
name|inherited
operator|=
name|inherited
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|apply (DashboardResource rsrc)
specifier|public
name|DashboardInfo
name|apply
parameter_list|(
name|DashboardResource
name|rsrc
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|PermissionBackendException
block|{
if|if
condition|(
name|inherited
operator|&&
operator|!
name|rsrc
operator|.
name|isProjectDefault
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"inherited flag can only be used with default"
argument_list|)
throw|;
block|}
if|if
condition|(
name|rsrc
operator|.
name|isProjectDefault
argument_list|()
condition|)
block|{
comment|// The default is not resolved to a definition yet.
try|try
block|{
name|rsrc
operator|=
name|defaultOf
argument_list|(
name|rsrc
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|DashboardsCollection
operator|.
name|parse
argument_list|(
name|rsrc
operator|.
name|getProjectState
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getRefName
argument_list|()
operator|.
name|substring
argument_list|(
name|REFS_DASHBOARDS
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
name|rsrc
operator|.
name|getPathName
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getConfig
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getProjectState
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|defaultOf (ProjectState projectState, CurrentUser user)
specifier|private
name|DashboardResource
name|defaultOf
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
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
name|String
name|id
init|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getLocalDefaultDashboard
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|id
operator|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getDefaultDashboard
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isDefaultDashboard
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|parse
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|,
name|id
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|inherited
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
for|for
control|(
name|ProjectState
name|ps
range|:
name|projectState
operator|.
name|tree
argument_list|()
control|)
block|{
name|id
operator|=
name|ps
operator|.
name|getProject
argument_list|()
operator|.
name|getDefaultDashboard
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
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|parse
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|,
name|id
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
DECL|method|parse (ProjectState projectState, CurrentUser user, String id)
specifier|private
name|DashboardResource
name|parse
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|String
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
name|List
argument_list|<
name|String
argument_list|>
name|p
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
name|String
name|ref
init|=
name|Url
operator|.
name|encode
argument_list|(
name|p
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|Url
operator|.
name|encode
argument_list|(
name|p
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|dashboards
operator|.
name|parse
argument_list|(
operator|new
name|ProjectResource
argument_list|(
name|projectState
argument_list|,
name|user
argument_list|)
argument_list|,
name|IdString
operator|.
name|fromUrl
argument_list|(
name|ref
operator|+
literal|':'
operator|+
name|path
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

