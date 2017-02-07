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
name|gson
operator|.
name|annotations
operator|.
name|SerializedName
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
name|ArrayList
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
class|class
name|DashboardsCollection
implements|implements
name|ChildCollection
argument_list|<
name|ProjectResource
argument_list|,
name|DashboardResource
argument_list|>
implements|,
name|AcceptsCreate
argument_list|<
name|ProjectResource
argument_list|>
block|{
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
DECL|field|createDefault
specifier|private
specifier|final
name|Provider
argument_list|<
name|SetDefaultDashboard
operator|.
name|CreateDefault
argument_list|>
name|createDefault
decl_stmt|;
annotation|@
name|Inject
DECL|method|DashboardsCollection ( GitRepositoryManager gitManager, DynamicMap<RestView<DashboardResource>> views, Provider<ListDashboards> list, Provider<SetDefaultDashboard.CreateDefault> createDefault)
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
name|Provider
argument_list|<
name|SetDefaultDashboard
operator|.
name|CreateDefault
argument_list|>
name|createDefault
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
name|createDefault
operator|=
name|createDefault
expr_stmt|;
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
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|create (ProjectResource parent, IdString id)
specifier|public
name|RestModifyView
argument_list|<
name|ProjectResource
argument_list|,
name|?
argument_list|>
name|create
parameter_list|(
name|ProjectResource
name|parent
parameter_list|,
name|IdString
name|id
parameter_list|)
throws|throws
name|RestApiException
block|{
if|if
condition|(
name|id
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"default"
argument_list|)
condition|)
block|{
return|return
name|createDefault
operator|.
name|get
argument_list|()
return|;
block|}
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
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
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|ProjectControl
name|myCtl
init|=
name|parent
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
literal|"default"
argument_list|)
condition|)
block|{
return|return
name|DashboardResource
operator|.
name|projectDefault
argument_list|(
name|myCtl
argument_list|)
return|;
block|}
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
operator|.
name|get
argument_list|()
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
name|ResourceNotFoundException
argument_list|(
name|id
argument_list|)
throw|;
block|}
name|CurrentUser
name|user
init|=
name|myCtl
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|String
name|ref
init|=
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|parts
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|ProjectState
name|ps
range|:
name|myCtl
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
operator|.
name|controlFor
argument_list|(
name|user
argument_list|)
argument_list|,
name|ref
argument_list|,
name|path
argument_list|,
name|myCtl
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
DECL|method|parse (ProjectControl ctl, String ref, String path, ProjectControl myCtl)
specifier|private
name|DashboardResource
name|parse
parameter_list|(
name|ProjectControl
name|ctl
parameter_list|,
name|String
name|ref
parameter_list|,
name|String
name|path
parameter_list|,
name|ProjectControl
name|myCtl
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
block|{
name|String
name|id
init|=
name|ref
operator|+
literal|":"
operator|+
name|path
decl_stmt|;
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
name|ref
operator|=
name|REFS_DASHBOARDS
operator|+
name|ref
expr_stmt|;
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
operator|||
operator|!
name|ctl
operator|.
name|controlForRef
argument_list|(
name|ref
argument_list|)
operator|.
name|canRead
argument_list|()
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
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
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
name|myCtl
argument_list|,
name|ref
argument_list|,
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
operator|new
name|DashboardInfo
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
name|query
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
name|query
operator|==
literal|null
condition|?
name|info
operator|.
name|path
else|:
name|query
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
name|Section
name|s
init|=
operator|new
name|Section
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
DECL|method|replace (String project, String query)
specifier|private
specifier|static
name|String
name|replace
parameter_list|(
name|String
name|project
parameter_list|,
name|String
name|query
parameter_list|)
block|{
return|return
name|query
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
DECL|class|DashboardInfo
specifier|static
class|class
name|DashboardInfo
block|{
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|project
name|String
name|project
decl_stmt|;
DECL|field|definingProject
name|String
name|definingProject
decl_stmt|;
DECL|field|ref
name|String
name|ref
decl_stmt|;
DECL|field|path
name|String
name|path
decl_stmt|;
DECL|field|description
name|String
name|description
decl_stmt|;
DECL|field|foreach
name|String
name|foreach
decl_stmt|;
DECL|field|url
name|String
name|url
decl_stmt|;
annotation|@
name|SerializedName
argument_list|(
literal|"default"
argument_list|)
DECL|field|isDefault
name|Boolean
name|isDefault
decl_stmt|;
DECL|field|title
name|String
name|title
decl_stmt|;
DECL|field|sections
name|List
argument_list|<
name|Section
argument_list|>
name|sections
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DashboardInfo (String ref, String name)
name|DashboardInfo
parameter_list|(
name|String
name|ref
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|name
expr_stmt|;
name|this
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
block|}
block|}
DECL|class|Section
specifier|static
class|class
name|Section
block|{
DECL|field|name
name|String
name|name
decl_stmt|;
DECL|field|query
name|String
name|query
decl_stmt|;
block|}
block|}
end_class

end_unit

