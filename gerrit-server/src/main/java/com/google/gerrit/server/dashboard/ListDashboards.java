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
DECL|package|com.google.gerrit.server.dashboard
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|dashboard
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
name|Maps
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
name|config
operator|.
name|AllProjectsName
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
name|ProjectControl
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
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
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
name|ObjectLoader
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
name|RevCommit
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
name|RevTree
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
name|eclipse
operator|.
name|jgit
operator|.
name|treewalk
operator|.
name|filter
operator|.
name|PathFilter
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_comment
comment|/** List projects visible to the calling user. */
end_comment

begin_class
DECL|class|ListDashboards
specifier|public
class|class
name|ListDashboards
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
DECL|field|REFS_DASHBOARDS
specifier|private
specifier|static
name|String
name|REFS_DASHBOARDS
init|=
literal|"refs/meta/dashboards/"
decl_stmt|;
DECL|enum|Level
specifier|public
specifier|static
enum|enum
name|Level
block|{
DECL|enumConstant|PROJECT
name|PROJECT
block|}
empty_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|CurrentUser
name|currentUser
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allProjects
specifier|private
name|AllProjectsName
name|allProjects
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|,
name|metaVar
operator|=
literal|"FMT"
argument_list|,
name|usage
operator|=
literal|"Output display format"
argument_list|)
DECL|field|format
specifier|private
name|OutputFormat
name|format
init|=
name|OutputFormat
operator|.
name|JSON
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--default"
argument_list|,
name|usage
operator|=
literal|"only the projects default dashboard is returned"
argument_list|)
DECL|field|defaultDashboard
specifier|private
name|boolean
name|defaultDashboard
decl_stmt|;
DECL|field|level
specifier|private
name|Level
name|level
decl_stmt|;
DECL|field|entityName
specifier|private
name|String
name|entityName
decl_stmt|;
annotation|@
name|Inject
DECL|method|ListDashboards (CurrentUser currentUser, ProjectCache projectCache, GitRepositoryManager repoManager, AllProjectsName allProjects)
specifier|protected
name|ListDashboards
parameter_list|(
name|CurrentUser
name|currentUser
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllProjectsName
name|allProjects
parameter_list|)
block|{
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|OutputFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (OutputFormat fmt)
specifier|public
name|ListDashboards
name|setFormat
parameter_list|(
name|OutputFormat
name|fmt
parameter_list|)
block|{
if|if
condition|(
operator|!
name|format
operator|.
name|isJson
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|format
operator|.
name|name
argument_list|()
operator|+
literal|" not supported"
argument_list|)
throw|;
block|}
name|this
operator|.
name|format
operator|=
name|fmt
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setLevel (Level level)
specifier|public
name|ListDashboards
name|setLevel
parameter_list|(
name|Level
name|level
parameter_list|)
block|{
name|this
operator|.
name|level
operator|=
name|level
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setEntityName (String entityName)
specifier|public
name|ListDashboards
name|setEntityName
parameter_list|(
name|String
name|entityName
parameter_list|)
block|{
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|display (OutputStream out)
specifier|public
name|void
name|display
parameter_list|(
name|OutputStream
name|out
parameter_list|)
block|{
specifier|final
name|PrintWriter
name|stdout
decl_stmt|;
try|try
block|{
name|stdout
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|out
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// Our encoding is required by the specifications for the runtime.
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"JVM lacks UTF-8 encoding"
argument_list|,
name|e
argument_list|)
throw|;
block|}
try|try
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|dashboards
decl_stmt|;
if|if
condition|(
name|level
operator|!=
literal|null
condition|)
block|{
switch|switch
condition|(
name|level
condition|)
block|{
case|case
name|PROJECT
case|:
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
init|=
operator|new
name|Project
operator|.
name|NameKey
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
specifier|final
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
name|DashboardInfo
name|defaultInfo
init|=
name|findProjectDefaultDashboard
argument_list|(
name|projectState
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultDashboard
condition|)
block|{
name|dashboards
operator|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
expr_stmt|;
if|if
condition|(
name|defaultInfo
operator|!=
literal|null
condition|)
block|{
name|dashboards
operator|.
name|put
argument_list|(
name|defaultInfo
operator|.
name|id
argument_list|,
name|defaultInfo
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|dashboards
operator|=
name|allDashboardsFor
argument_list|(
name|projectState
argument_list|,
name|defaultInfo
operator|!=
literal|null
condition|?
name|defaultInfo
operator|.
name|id
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unsupported dashboard level: "
operator|+
name|level
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|dashboards
operator|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
expr_stmt|;
block|}
name|format
operator|.
name|newGson
argument_list|()
operator|.
name|toJson
argument_list|(
name|dashboards
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|,
name|stdout
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stdout
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|allDashboardsFor (ProjectState projectState, final String defaultId)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|allDashboardsFor
parameter_list|(
name|ProjectState
name|projectState
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|)
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|parent
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|dashboards
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
name|seen
operator|.
name|add
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
do|do
block|{
name|dashboards
operator|=
name|addProjectDashboards
argument_list|(
name|projectState
argument_list|,
name|dashboards
argument_list|,
name|defaultId
argument_list|)
expr_stmt|;
name|parent
operator|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
name|projectState
operator|=
name|projectCache
operator|.
name|get
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|projectState
operator|!=
literal|null
operator|&&
name|seen
operator|.
name|add
argument_list|(
name|parent
argument_list|)
condition|)
do|;
return|return
name|dashboards
return|;
block|}
DECL|method|addProjectDashboards ( final ProjectState projectState, Map<String, DashboardInfo> all, final String defaultId)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|addProjectDashboards
parameter_list|(
specifier|final
name|ProjectState
name|projectState
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|all
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|dashboards
init|=
name|projectDashboards
argument_list|(
name|projectState
argument_list|,
name|defaultId
argument_list|)
decl_stmt|;
name|dashboards
operator|.
name|putAll
argument_list|(
name|all
argument_list|)
expr_stmt|;
return|return
name|dashboards
return|;
block|}
DECL|method|projectDashboards ( final ProjectState projectState, final String defaultId)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|projectDashboards
parameter_list|(
specifier|final
name|ProjectState
name|projectState
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|)
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|dashboards
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
specifier|final
name|ProjectControl
name|projectControl
init|=
name|projectState
operator|.
name|controlFor
argument_list|(
name|currentUser
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
operator|||
operator|!
name|projectControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
return|return
name|dashboards
return|;
block|}
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
literal|null
decl_stmt|;
name|RevWalk
name|revWalk
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|refs
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefs
argument_list|(
name|REFS_DASHBOARDS
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Ref
name|ref
range|:
name|refs
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|projectControl
operator|.
name|controlForRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|canRead
argument_list|()
condition|)
block|{
name|dashboards
operator|.
name|putAll
argument_list|(
name|loadDashboards
argument_list|(
name|projectControl
operator|.
name|getProject
argument_list|()
argument_list|,
name|repo
argument_list|,
name|revWalk
argument_list|,
name|ref
argument_list|,
name|defaultId
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to load dashboards of project "
operator|+
name|projectName
operator|.
name|get
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|revWalk
operator|!=
literal|null
condition|)
block|{
name|revWalk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|dashboards
return|;
block|}
DECL|method|loadDashboards (final Project project, final Repository repo, final RevWalk revWalk, final Ref ref, final String defaultId)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|loadDashboards
parameter_list|(
specifier|final
name|Project
name|project
parameter_list|,
specifier|final
name|Repository
name|repo
parameter_list|,
specifier|final
name|RevWalk
name|revWalk
parameter_list|,
specifier|final
name|Ref
name|ref
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|DashboardInfo
argument_list|>
name|dashboards
init|=
name|Maps
operator|.
name|newTreeMap
argument_list|()
decl_stmt|;
name|TreeWalk
name|treeWalk
init|=
operator|new
name|TreeWalk
argument_list|(
name|repo
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|RevCommit
name|commit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|RevTree
name|tree
init|=
name|commit
operator|.
name|getTree
argument_list|()
decl_stmt|;
name|treeWalk
operator|.
name|addTree
argument_list|(
name|tree
argument_list|)
expr_stmt|;
name|treeWalk
operator|.
name|setRecursive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
while|while
condition|(
name|treeWalk
operator|.
name|next
argument_list|()
condition|)
block|{
specifier|final
name|ObjectLoader
name|loader
init|=
name|repo
operator|.
name|open
argument_list|(
name|treeWalk
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|DashboardInfo
name|info
init|=
name|loadDashboard
argument_list|(
name|project
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|treeWalk
operator|.
name|getPathString
argument_list|()
argument_list|,
name|defaultId
argument_list|,
name|loader
argument_list|)
decl_stmt|;
name|dashboards
operator|.
name|put
argument_list|(
name|info
operator|.
name|id
argument_list|,
name|info
argument_list|)
expr_stmt|;
block|}
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
literal|"Failed to load dashboards of project "
operator|+
name|project
operator|.
name|getName
argument_list|()
operator|+
literal|" from ref "
operator|+
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to load dashboards of project "
operator|+
name|project
operator|.
name|getName
argument_list|()
operator|+
literal|" from ref "
operator|+
name|ref
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|treeWalk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
return|return
name|dashboards
return|;
block|}
DECL|method|findProjectDefaultDashboard (ProjectState projectState)
specifier|private
name|DashboardInfo
name|findProjectDefaultDashboard
parameter_list|(
name|ProjectState
name|projectState
parameter_list|)
block|{
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
init|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|parent
decl_stmt|;
name|DashboardInfo
name|info
decl_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
argument_list|()
decl_stmt|;
name|seen
operator|.
name|add
argument_list|(
name|projectName
argument_list|)
expr_stmt|;
name|boolean
name|considerLocal
init|=
literal|true
decl_stmt|;
do|do
block|{
name|info
operator|=
name|loadProjectDefaultDashboard
argument_list|(
name|projectState
argument_list|,
name|considerLocal
argument_list|)
expr_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
return|;
block|}
name|considerLocal
operator|=
literal|false
expr_stmt|;
name|parent
operator|=
name|projectState
operator|.
name|getProject
argument_list|()
operator|.
name|getParent
argument_list|(
name|allProjects
argument_list|)
expr_stmt|;
name|projectState
operator|=
name|projectCache
operator|.
name|get
argument_list|(
name|parent
argument_list|)
expr_stmt|;
block|}
do|while
condition|(
name|projectState
operator|!=
literal|null
operator|&&
name|seen
operator|.
name|add
argument_list|(
name|parent
argument_list|)
condition|)
do|;
return|return
name|info
return|;
block|}
DECL|method|loadProjectDefaultDashboard (final ProjectState projectState, boolean considerLocal)
specifier|private
name|DashboardInfo
name|loadProjectDefaultDashboard
parameter_list|(
specifier|final
name|ProjectState
name|projectState
parameter_list|,
name|boolean
name|considerLocal
parameter_list|)
block|{
specifier|final
name|ProjectControl
name|projectControl
init|=
name|projectState
operator|.
name|controlFor
argument_list|(
name|currentUser
argument_list|)
decl_stmt|;
if|if
condition|(
name|projectState
operator|==
literal|null
operator|||
operator|!
name|projectControl
operator|.
name|isVisible
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|Project
name|project
init|=
name|projectControl
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|String
name|defaultDashboardId
init|=
name|project
operator|.
name|getDefaultDashboard
argument_list|()
decl_stmt|;
if|if
condition|(
name|considerLocal
operator|&&
name|project
operator|.
name|getLocalDefaultDashboard
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|defaultDashboardId
operator|=
name|project
operator|.
name|getLocalDefaultDashboard
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|defaultDashboardId
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|loadDashboard
argument_list|(
name|projectControl
argument_list|,
name|defaultDashboardId
argument_list|,
name|defaultDashboardId
argument_list|)
return|;
block|}
DECL|method|loadDashboard (final ProjectControl projectControl, final String dashboardId, final String defaultId)
specifier|private
name|DashboardInfo
name|loadDashboard
parameter_list|(
specifier|final
name|ProjectControl
name|projectControl
parameter_list|,
specifier|final
name|String
name|dashboardId
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|)
block|{
name|StringTokenizer
name|t
init|=
operator|new
name|StringTokenizer
argument_list|(
name|dashboardId
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|countTokens
argument_list|()
operator|!=
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"failed to load dashboard, invalid dashboard id: "
operator|+
name|dashboardId
argument_list|)
throw|;
block|}
specifier|final
name|String
name|refName
init|=
name|t
operator|.
name|nextToken
argument_list|()
decl_stmt|;
specifier|final
name|String
name|path
init|=
name|t
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|Repository
name|repo
init|=
literal|null
decl_stmt|;
name|RevWalk
name|revWalk
init|=
literal|null
decl_stmt|;
name|TreeWalk
name|treeWalk
init|=
literal|null
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRef
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
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
operator|!
name|projectControl
operator|.
name|controlForRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|canRead
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
specifier|final
name|RevCommit
name|commit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|ref
operator|.
name|getObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|treeWalk
operator|=
operator|new
name|TreeWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|treeWalk
operator|.
name|addTree
argument_list|(
name|commit
operator|.
name|getTree
argument_list|()
argument_list|)
expr_stmt|;
name|treeWalk
operator|.
name|setRecursive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|treeWalk
operator|.
name|setFilter
argument_list|(
name|PathFilter
operator|.
name|create
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|treeWalk
operator|.
name|next
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|ObjectLoader
name|loader
init|=
name|repo
operator|.
name|open
argument_list|(
name|treeWalk
operator|.
name|getObjectId
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|loadDashboard
argument_list|(
name|projectControl
operator|.
name|getProject
argument_list|()
argument_list|,
name|refName
argument_list|,
name|path
argument_list|,
name|defaultId
argument_list|,
name|loader
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to load default dashboard"
argument_list|,
name|e
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
literal|"Failed to load dashboards of project "
operator|+
name|projectControl
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" from ref "
operator|+
name|refName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|treeWalk
operator|!=
literal|null
condition|)
block|{
name|treeWalk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|revWalk
operator|!=
literal|null
condition|)
block|{
name|revWalk
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|loadDashboard (final Project project, final String refName, final String path, final String defaultId, final ObjectLoader loader)
specifier|private
name|DashboardInfo
name|loadDashboard
parameter_list|(
specifier|final
name|Project
name|project
parameter_list|,
specifier|final
name|String
name|refName
parameter_list|,
specifier|final
name|String
name|path
parameter_list|,
specifier|final
name|String
name|defaultId
parameter_list|,
specifier|final
name|ObjectLoader
name|loader
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
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
name|dashboardName
operator|=
name|path
expr_stmt|;
name|info
operator|.
name|refName
operator|=
name|refName
expr_stmt|;
name|info
operator|.
name|projectName
operator|=
name|project
operator|.
name|getName
argument_list|()
expr_stmt|;
name|info
operator|.
name|id
operator|=
name|createId
argument_list|(
name|info
operator|.
name|refName
argument_list|,
name|info
operator|.
name|dashboardName
argument_list|)
expr_stmt|;
name|info
operator|.
name|isDefault
operator|=
name|info
operator|.
name|id
operator|.
name|equals
argument_list|(
name|defaultId
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|loader
operator|.
name|copyTo
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|Config
name|dashboardConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|dashboardConfig
operator|.
name|fromText
argument_list|(
operator|new
name|String
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|info
operator|.
name|description
operator|=
name|dashboardConfig
operator|.
name|getString
argument_list|(
literal|"main"
argument_list|,
literal|null
argument_list|,
literal|"description"
argument_list|)
expr_stmt|;
specifier|final
name|StringBuilder
name|query
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|query
operator|.
name|append
argument_list|(
literal|"title="
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
name|info
operator|.
name|dashboardName
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"+"
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|sections
init|=
name|dashboardConfig
operator|.
name|getSubsections
argument_list|(
literal|"section"
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|section
range|:
name|sections
control|)
block|{
name|query
operator|.
name|append
argument_list|(
literal|"&"
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
name|section
operator|.
name|replaceAll
argument_list|(
literal|" "
argument_list|,
literal|"+"
argument_list|)
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
name|query
operator|.
name|append
argument_list|(
name|dashboardConfig
operator|.
name|getString
argument_list|(
literal|"section"
argument_list|,
name|section
argument_list|,
literal|"query"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|info
operator|.
name|parameters
operator|=
name|query
operator|.
name|toString
argument_list|()
expr_stmt|;
return|return
name|info
return|;
block|}
DECL|method|createId (final String refName, final String dashboardName)
specifier|private
specifier|static
name|String
name|createId
parameter_list|(
specifier|final
name|String
name|refName
parameter_list|,
specifier|final
name|String
name|dashboardName
parameter_list|)
block|{
return|return
name|refName
operator|+
literal|":"
operator|+
name|dashboardName
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|DashboardInfo
specifier|private
specifier|static
class|class
name|DashboardInfo
block|{
DECL|field|kind
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#dashboard"
decl_stmt|;
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|dashboardName
name|String
name|dashboardName
decl_stmt|;
DECL|field|refName
name|String
name|refName
decl_stmt|;
DECL|field|projectName
name|String
name|projectName
decl_stmt|;
DECL|field|description
name|String
name|description
decl_stmt|;
DECL|field|parameters
name|String
name|parameters
decl_stmt|;
DECL|field|isDefault
name|boolean
name|isDefault
decl_stmt|;
block|}
block|}
end_class

end_unit

