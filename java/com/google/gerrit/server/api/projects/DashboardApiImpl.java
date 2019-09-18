begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
import|import static
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
name|ApiUtil
operator|.
name|asRestApiException
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
name|api
operator|.
name|projects
operator|.
name|DashboardApi
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
name|SetDashboardInput
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
name|restapi
operator|.
name|project
operator|.
name|DashboardsCollection
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
name|restapi
operator|.
name|project
operator|.
name|GetDashboard
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
name|restapi
operator|.
name|project
operator|.
name|SetDashboard
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

begin_class
DECL|class|DashboardApiImpl
specifier|public
class|class
name|DashboardApiImpl
implements|implements
name|DashboardApi
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (ProjectResource project, String id)
name|DashboardApiImpl
name|create
parameter_list|(
name|ProjectResource
name|project
parameter_list|,
name|String
name|id
parameter_list|)
function_decl|;
block|}
DECL|field|dashboards
specifier|private
specifier|final
name|DashboardsCollection
name|dashboards
decl_stmt|;
DECL|field|get
specifier|private
specifier|final
name|Provider
argument_list|<
name|GetDashboard
argument_list|>
name|get
decl_stmt|;
DECL|field|set
specifier|private
specifier|final
name|SetDashboard
name|set
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|ProjectResource
name|project
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
annotation|@
name|Inject
DECL|method|DashboardApiImpl ( DashboardsCollection dashboards, Provider<GetDashboard> get, SetDashboard set, @Assisted ProjectResource project, @Assisted @Nullable String id)
name|DashboardApiImpl
parameter_list|(
name|DashboardsCollection
name|dashboards
parameter_list|,
name|Provider
argument_list|<
name|GetDashboard
argument_list|>
name|get
parameter_list|,
name|SetDashboard
name|set
parameter_list|,
annotation|@
name|Assisted
name|ProjectResource
name|project
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|dashboards
operator|=
name|dashboards
expr_stmt|;
name|this
operator|.
name|get
operator|=
name|get
expr_stmt|;
name|this
operator|.
name|set
operator|=
name|set
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|DashboardInfo
name|get
parameter_list|()
throws|throws
name|RestApiException
block|{
return|return
name|get
argument_list|(
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|get (boolean inherited)
specifier|public
name|DashboardInfo
name|get
parameter_list|(
name|boolean
name|inherited
parameter_list|)
throws|throws
name|RestApiException
block|{
try|try
block|{
return|return
name|get
operator|.
name|get
argument_list|()
operator|.
name|setInherited
argument_list|(
name|inherited
argument_list|)
operator|.
name|apply
argument_list|(
name|resource
argument_list|()
argument_list|)
operator|.
name|value
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|asRestApiException
argument_list|(
literal|"Cannot read dashboard"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|setDefault ()
specifier|public
name|void
name|setDefault
parameter_list|()
throws|throws
name|RestApiException
block|{
name|SetDashboardInput
name|input
init|=
operator|new
name|SetDashboardInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|id
operator|=
name|id
expr_stmt|;
try|try
block|{
name|set
operator|.
name|apply
argument_list|(
name|DashboardResource
operator|.
name|projectDefault
argument_list|(
name|project
operator|.
name|getProjectState
argument_list|()
argument_list|,
name|project
operator|.
name|getUser
argument_list|()
argument_list|)
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|String
name|msg
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Cannot %s default dashboard"
argument_list|,
name|id
operator|!=
literal|null
condition|?
literal|"set"
else|:
literal|"remove"
argument_list|)
decl_stmt|;
throw|throw
name|asRestApiException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|resource ()
specifier|private
name|DashboardResource
name|resource
parameter_list|()
throws|throws
name|RestApiException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
throws|,
name|PermissionBackendException
block|{
return|return
name|dashboards
operator|.
name|parse
argument_list|(
name|project
argument_list|,
name|IdString
operator|.
name|fromDecoded
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

