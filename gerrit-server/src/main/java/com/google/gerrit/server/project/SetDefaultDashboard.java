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
name|common
operator|.
name|base
operator|.
name|Objects
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
name|Response
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
name|RestModifyView
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
name|IdentifiedUser
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
name|MetaDataUpdate
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
name|ProjectConfig
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
name|DashboardsCollection
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
name|server
operator|.
name|project
operator|.
name|SetDashboard
operator|.
name|Input
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
name|RepositoryNotFoundException
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
DECL|class|SetDefaultDashboard
class|class
name|SetDefaultDashboard
implements|implements
name|RestModifyView
argument_list|<
name|DashboardResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|field|cache
specifier|private
specifier|final
name|ProjectCache
name|cache
decl_stmt|;
DECL|field|updateFactory
specifier|private
specifier|final
name|MetaDataUpdate
operator|.
name|Server
name|updateFactory
decl_stmt|;
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--inherited"
argument_list|,
name|usage
operator|=
literal|"set dashboard inherited by children"
argument_list|)
DECL|field|inherited
specifier|private
name|boolean
name|inherited
decl_stmt|;
annotation|@
name|Inject
DECL|method|SetDefaultDashboard (ProjectCache cache, MetaDataUpdate.Server updateFactory, DashboardsCollection dashboards, Provider<GetDashboard> get)
name|SetDefaultDashboard
parameter_list|(
name|ProjectCache
name|cache
parameter_list|,
name|MetaDataUpdate
operator|.
name|Server
name|updateFactory
parameter_list|,
name|DashboardsCollection
name|dashboards
parameter_list|,
name|Provider
argument_list|<
name|GetDashboard
argument_list|>
name|get
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
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
block|}
annotation|@
name|Override
DECL|method|apply (DashboardResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|DashboardInfo
argument_list|>
name|apply
parameter_list|(
name|DashboardResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|Input
argument_list|()
expr_stmt|;
comment|// Delete would set input to null.
block|}
name|input
operator|.
name|id
operator|=
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|id
argument_list|)
expr_stmt|;
name|ProjectControl
name|ctl
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|ctl
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not project owner"
argument_list|)
throw|;
block|}
name|DashboardResource
name|target
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|id
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|target
operator|=
name|dashboards
operator|.
name|parse
argument_list|(
operator|new
name|ProjectResource
argument_list|(
name|ctl
argument_list|)
argument_list|,
name|IdString
operator|.
name|fromUrl
argument_list|(
name|input
operator|.
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ResourceNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"dashboard "
operator|+
name|input
operator|.
name|id
operator|+
literal|" not found"
argument_list|)
throw|;
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
try|try
block|{
name|MetaDataUpdate
name|md
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|ProjectConfig
name|config
init|=
name|ProjectConfig
operator|.
name|read
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|Project
name|project
init|=
name|config
operator|.
name|getProject
argument_list|()
decl_stmt|;
if|if
condition|(
name|inherited
condition|)
block|{
name|project
operator|.
name|setDefaultDashboard
argument_list|(
name|input
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|project
operator|.
name|setLocalDefaultDashboard
argument_list|(
name|input
operator|.
name|id
argument_list|)
expr_stmt|;
block|}
name|String
name|msg
init|=
name|Objects
operator|.
name|firstNonNull
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|commitMessage
argument_list|)
argument_list|,
name|input
operator|.
name|id
operator|==
literal|null
condition|?
literal|"Removed default dashboard.\n"
else|:
name|String
operator|.
name|format
argument_list|(
literal|"Changed default dashboard to %s.\n"
argument_list|,
name|input
operator|.
name|id
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|msg
operator|.
name|endsWith
argument_list|(
literal|"\n"
argument_list|)
condition|)
block|{
name|msg
operator|+=
literal|"\n"
expr_stmt|;
block|}
name|md
operator|.
name|setAuthor
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|md
operator|.
name|setMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|config
operator|.
name|commit
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|cache
operator|.
name|evict
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|DashboardInfo
name|info
init|=
name|get
operator|.
name|get
argument_list|()
operator|.
name|apply
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|info
operator|.
name|isDefault
operator|=
literal|true
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|info
argument_list|)
return|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
finally|finally
block|{
name|md
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|notFound
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
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
name|String
operator|.
name|format
argument_list|(
literal|"invalid project.config: %s"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|class|CreateDefault
specifier|static
class|class
name|CreateDefault
implements|implements
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|SetDashboard
operator|.
name|Input
argument_list|>
block|{
DECL|field|setDefault
specifier|private
specifier|final
name|Provider
argument_list|<
name|SetDefaultDashboard
argument_list|>
name|setDefault
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
literal|"set dashboard inherited by children"
argument_list|)
DECL|field|inherited
specifier|private
name|boolean
name|inherited
decl_stmt|;
annotation|@
name|Inject
DECL|method|CreateDefault (Provider<SetDefaultDashboard> setDefault)
name|CreateDefault
parameter_list|(
name|Provider
argument_list|<
name|SetDefaultDashboard
argument_list|>
name|setDefault
parameter_list|)
block|{
name|this
operator|.
name|setDefault
operator|=
name|setDefault
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource resource, Input input)
specifier|public
name|Response
argument_list|<
name|DashboardInfo
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|resource
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
block|{
name|SetDefaultDashboard
name|set
init|=
name|setDefault
operator|.
name|get
argument_list|()
decl_stmt|;
name|set
operator|.
name|inherited
operator|=
name|inherited
expr_stmt|;
return|return
name|set
operator|.
name|apply
argument_list|(
name|DashboardResource
operator|.
name|projectDefault
argument_list|(
name|resource
operator|.
name|getControl
argument_list|()
argument_list|)
argument_list|,
name|input
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

