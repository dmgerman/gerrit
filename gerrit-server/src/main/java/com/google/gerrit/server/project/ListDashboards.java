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
name|FileMode
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
name|Ref
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

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|RevWalk
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
name|treewalk
operator|.
name|TreeWalk
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ListDashboards
class|class
name|ListDashboards
implements|implements
name|RestReadView
argument_list|<
name|ProjectResource
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ListDashboards
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|gitManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitManager
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
DECL|method|ListDashboards (GitRepositoryManager gitManager)
name|ListDashboards
parameter_list|(
name|GitRepositoryManager
name|gitManager
parameter_list|)
block|{
name|this
operator|.
name|gitManager
operator|=
name|gitManager
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ProjectResource resource)
specifier|public
name|List
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|ProjectResource
name|resource
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
name|ProjectControl
name|ctl
init|=
name|resource
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|String
name|project
init|=
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|inherited
condition|)
block|{
return|return
name|scan
argument_list|(
name|resource
operator|.
name|getControl
argument_list|()
argument_list|,
name|project
argument_list|,
literal|true
argument_list|)
return|;
block|}
name|List
argument_list|<
name|List
argument_list|<
name|DashboardInfo
argument_list|>
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|boolean
name|setDefault
init|=
literal|true
decl_stmt|;
for|for
control|(
name|ProjectState
name|ps
range|:
name|ctl
operator|.
name|getProjectState
argument_list|()
operator|.
name|tree
argument_list|()
control|)
block|{
name|ctl
operator|=
name|ps
operator|.
name|controlFor
argument_list|(
name|ctl
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ctl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|list
init|=
name|scan
argument_list|(
name|ctl
argument_list|,
name|project
argument_list|,
name|setDefault
argument_list|)
decl_stmt|;
for|for
control|(
name|DashboardInfo
name|d
range|:
name|list
control|)
block|{
if|if
condition|(
name|d
operator|.
name|isDefault
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|d
operator|.
name|isDefault
argument_list|)
condition|)
block|{
name|setDefault
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|all
operator|.
name|add
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|all
return|;
block|}
DECL|method|scan (ProjectControl ctl, String project, boolean setDefault)
specifier|private
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|scan
parameter_list|(
name|ProjectControl
name|ctl
parameter_list|,
name|String
name|project
parameter_list|,
name|boolean
name|setDefault
parameter_list|)
throws|throws
name|ResourceNotFoundException
throws|,
name|IOException
block|{
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|ctl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
try|try
init|(
name|Repository
name|git
init|=
name|gitManager
operator|.
name|openRepository
argument_list|(
name|projectName
argument_list|)
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|git
argument_list|)
init|)
block|{
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|git
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|REFS_DASHBOARDS
argument_list|)
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|ctl
operator|.
name|controlForRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|isVisible
argument_list|()
condition|)
block|{
name|all
operator|.
name|addAll
argument_list|(
name|scanDashboards
argument_list|(
name|ctl
operator|.
name|getProject
argument_list|()
argument_list|,
name|git
argument_list|,
name|rw
argument_list|,
name|ref
argument_list|,
name|project
argument_list|,
name|setDefault
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|all
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
argument_list|()
throw|;
block|}
block|}
DECL|method|scanDashboards ( Project definingProject, Repository git, RevWalk rw, Ref ref, String project, boolean setDefault)
specifier|private
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|scanDashboards
parameter_list|(
name|Project
name|definingProject
parameter_list|,
name|Repository
name|git
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|Ref
name|ref
parameter_list|,
name|String
name|project
parameter_list|,
name|boolean
name|setDefault
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|DashboardInfo
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
init|(
name|TreeWalk
name|tw
init|=
operator|new
name|TreeWalk
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|)
init|)
block|{
name|tw
operator|.
name|addTree
argument_list|(
name|rw
operator|.
name|parseTree
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|tw
operator|.
name|setRecursive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
while|while
condition|(
name|tw
operator|.
name|next
argument_list|()
condition|)
block|{
if|if
condition|(
name|tw
operator|.
name|getFileMode
argument_list|(
literal|0
argument_list|)
operator|==
name|FileMode
operator|.
name|REGULAR_FILE
condition|)
block|{
try|try
block|{
name|list
operator|.
name|add
argument_list|(
name|DashboardsCollection
operator|.
name|parse
argument_list|(
name|definingProject
argument_list|,
name|ref
operator|.
name|getName
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
name|tw
operator|.
name|getPathString
argument_list|()
argument_list|,
operator|new
name|BlobBasedConfig
argument_list|(
literal|null
argument_list|,
name|git
argument_list|,
name|tw
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
argument_list|,
name|project
argument_list|,
name|setDefault
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot parse dashboard %s:%s:%s: %s"
argument_list|,
name|definingProject
operator|.
name|getName
argument_list|()
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|tw
operator|.
name|getPathString
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

