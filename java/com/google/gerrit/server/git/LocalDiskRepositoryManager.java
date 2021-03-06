begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|flogger
operator|.
name|FluentLogger
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
name|entities
operator|.
name|RefNames
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
name|events
operator|.
name|LifecycleListener
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
name|lifecycle
operator|.
name|LifecycleModule
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
name|GerritServerConfig
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
name|SitePaths
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
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|nio
operator|.
name|file
operator|.
name|FileVisitOption
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|FileVisitResult
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|SimpleFileVisitor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|BasicFileAttributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EnumSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeSet
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
name|ConfigConstants
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
name|lib
operator|.
name|RepositoryCache
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
name|RepositoryCache
operator|.
name|FileKey
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
name|RepositoryCacheConfig
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
name|StoredConfig
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
name|storage
operator|.
name|file
operator|.
name|WindowCacheConfig
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
name|util
operator|.
name|FS
import|;
end_import

begin_comment
comment|/** Manages Git repositories stored on the local filesystem. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|LocalDiskRepositoryManager
specifier|public
class|class
name|LocalDiskRepositoryManager
implements|implements
name|GitRepositoryManager
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|LocalDiskRepositoryManager
operator|.
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|Lifecycle
specifier|public
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|serverConfig
specifier|private
specifier|final
name|Config
name|serverConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (@erritServerConfig Config cfg)
name|Lifecycle
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|serverConfig
operator|=
name|cfg
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{
name|RepositoryCacheConfig
name|repoCacheCfg
init|=
operator|new
name|RepositoryCacheConfig
argument_list|()
decl_stmt|;
name|repoCacheCfg
operator|.
name|fromConfig
argument_list|(
name|serverConfig
argument_list|)
expr_stmt|;
name|repoCacheCfg
operator|.
name|install
argument_list|()
expr_stmt|;
name|WindowCacheConfig
name|cfg
init|=
operator|new
name|WindowCacheConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|fromConfig
argument_list|(
name|serverConfig
argument_list|)
expr_stmt|;
if|if
condition|(
name|serverConfig
operator|.
name|getString
argument_list|(
literal|"core"
argument_list|,
literal|null
argument_list|,
literal|"streamFileThreshold"
argument_list|)
operator|==
literal|null
condition|)
block|{
name|long
name|mx
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|maxMemory
argument_list|()
decl_stmt|;
name|int
name|limit
init|=
operator|(
name|int
operator|)
name|Math
operator|.
name|min
argument_list|(
name|mx
operator|/
literal|4
argument_list|,
comment|// don't use more than 1/4 of the heap.
literal|2047
operator|<<
literal|20
argument_list|)
decl_stmt|;
comment|// cannot exceed array length
if|if
condition|(
operator|(
literal|5
operator|<<
literal|20
operator|)
operator|<
name|limit
operator|&&
name|limit
operator|%
operator|(
literal|1
operator|<<
literal|20
operator|)
operator|!=
literal|0
condition|)
block|{
comment|// If the limit is at least 5 MiB but is not a whole multiple
comment|// of MiB round up to the next one full megabyte. This is a very
comment|// tiny memory increase in exchange for nice round units.
name|limit
operator|=
operator|(
operator|(
name|limit
operator|/
operator|(
literal|1
operator|<<
literal|20
operator|)
operator|)
operator|+
literal|1
operator|)
operator|<<
literal|20
expr_stmt|;
block|}
name|String
name|desc
decl_stmt|;
if|if
condition|(
name|limit
operator|%
operator|(
literal|1
operator|<<
literal|20
operator|)
operator|==
literal|0
condition|)
block|{
name|desc
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%dm"
argument_list|,
name|limit
operator|/
operator|(
literal|1
operator|<<
literal|20
operator|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|limit
operator|%
operator|(
literal|1
operator|<<
literal|10
operator|)
operator|==
literal|0
condition|)
block|{
name|desc
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%dk"
argument_list|,
name|limit
operator|/
operator|(
literal|1
operator|<<
literal|10
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|desc
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"%d"
argument_list|,
name|limit
argument_list|)
expr_stmt|;
block|}
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Defaulting core.streamFileThreshold to %s"
argument_list|,
name|desc
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setStreamFileThreshold
argument_list|(
name|limit
argument_list|)
expr_stmt|;
block|}
name|cfg
operator|.
name|install
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{}
block|}
DECL|field|basePath
specifier|private
specifier|final
name|Path
name|basePath
decl_stmt|;
annotation|@
name|Inject
DECL|method|LocalDiskRepositoryManager (SitePaths site, @GerritServerConfig Config cfg)
name|LocalDiskRepositoryManager
parameter_list|(
name|SitePaths
name|site
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|basePath
operator|=
name|site
operator|.
name|resolve
argument_list|(
name|cfg
operator|.
name|getString
argument_list|(
literal|"gerrit"
argument_list|,
literal|null
argument_list|,
literal|"basePath"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|basePath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"gerrit.basePath must be configured"
argument_list|)
throw|;
block|}
block|}
comment|/**    * Return the basePath under which the specified project is stored.    *    * @param name the name of the project    * @return base directory    */
DECL|method|getBasePath (Project.NameKey name)
specifier|public
name|Path
name|getBasePath
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
return|return
name|basePath
return|;
block|}
annotation|@
name|Override
DECL|method|openRepository (Project.NameKey name)
specifier|public
name|Repository
name|openRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
block|{
return|return
name|openRepository
argument_list|(
name|getBasePath
argument_list|(
name|name
argument_list|)
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|openRepository (Path path, Project.NameKey name)
specifier|private
name|Repository
name|openRepository
parameter_list|(
name|Path
name|path
parameter_list|,
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
block|{
if|if
condition|(
name|isUnreasonableName
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Invalid name: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|FileKey
name|loc
init|=
name|FileKey
operator|.
name|lenient
argument_list|(
name|path
operator|.
name|resolve
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|RepositoryCache
operator|.
name|open
argument_list|(
name|loc
argument_list|)
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
name|RepositoryNotFoundException
argument_list|(
literal|"Cannot open repository "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRepository (Project.NameKey name)
specifier|public
name|Repository
name|createRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|RepositoryCaseMismatchException
throws|,
name|IOException
block|{
name|Path
name|path
init|=
name|getBasePath
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|isUnreasonableName
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
literal|"Invalid name: "
operator|+
name|name
argument_list|)
throw|;
block|}
name|File
name|dir
init|=
name|FileKey
operator|.
name|resolve
argument_list|(
name|path
operator|.
name|resolve
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|!=
literal|null
condition|)
block|{
comment|// Already exists on disk, use the repository we found.
comment|//
name|Project
operator|.
name|NameKey
name|onDiskName
init|=
name|getProjectName
argument_list|(
name|path
argument_list|,
name|dir
operator|.
name|getCanonicalFile
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|onDiskName
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryCaseMismatchException
argument_list|(
name|name
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Repository already exists: "
operator|+
name|name
argument_list|)
throw|;
block|}
comment|// It doesn't exist under any of the standard permutations
comment|// of the repository name, so prefer the standard bare name.
comment|//
name|String
name|n
init|=
name|name
operator|.
name|get
argument_list|()
operator|+
name|Constants
operator|.
name|DOT_GIT_EXT
decl_stmt|;
name|FileKey
name|loc
init|=
name|FileKey
operator|.
name|exact
argument_list|(
name|path
operator|.
name|resolve
argument_list|(
name|n
argument_list|)
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
decl_stmt|;
try|try
block|{
name|Repository
name|db
init|=
name|RepositoryCache
operator|.
name|open
argument_list|(
name|loc
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|db
operator|.
name|create
argument_list|(
literal|true
comment|/* bare */
argument_list|)
expr_stmt|;
name|StoredConfig
name|config
init|=
name|db
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setBoolean
argument_list|(
name|ConfigConstants
operator|.
name|CONFIG_CORE_SECTION
argument_list|,
literal|null
argument_list|,
name|ConfigConstants
operator|.
name|CONFIG_KEY_LOGALLREFUPDATES
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|save
argument_list|()
expr_stmt|;
comment|// JGit only writes to the reflog for refs/meta/config if the log file
comment|// already exists.
comment|//
name|File
name|metaConfigLog
init|=
operator|new
name|File
argument_list|(
name|db
operator|.
name|getDirectory
argument_list|()
argument_list|,
literal|"logs/"
operator|+
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|metaConfigLog
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
operator|||
operator|!
name|metaConfigLog
operator|.
name|createNewFile
argument_list|()
condition|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|log
argument_list|(
literal|"Failed to create ref log for %s in repository %s"
argument_list|,
name|RefNames
operator|.
name|REFS_CONFIG
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|db
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
name|RepositoryNotFoundException
argument_list|(
literal|"Cannot create repository "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isUnreasonableName (Project.NameKey nameKey)
specifier|private
name|boolean
name|isUnreasonableName
parameter_list|(
name|Project
operator|.
name|NameKey
name|nameKey
parameter_list|)
block|{
specifier|final
name|String
name|name
init|=
name|nameKey
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|name
operator|.
name|length
argument_list|()
operator|==
literal|0
comment|// no empty paths
operator|||
name|name
operator|.
name|charAt
argument_list|(
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
operator|==
literal|'/'
comment|// no suffix
operator|||
name|name
operator|.
name|indexOf
argument_list|(
literal|'\\'
argument_list|)
operator|>=
literal|0
comment|// no windows/dos style paths
operator|||
name|name
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
operator|==
literal|'/'
comment|// no absolute paths
operator|||
operator|new
name|File
argument_list|(
name|name
argument_list|)
operator|.
name|isAbsolute
argument_list|()
comment|// no absolute paths
operator|||
name|name
operator|.
name|startsWith
argument_list|(
literal|"../"
argument_list|)
comment|// no "l../etc/passwd"
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"/../"
argument_list|)
comment|// no "foo/../etc/passwd"
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"/./"
argument_list|)
comment|// "foo/./foo" is insane to ask
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"//"
argument_list|)
comment|// windows UNC path can be "//..."
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|".git/"
argument_list|)
comment|// no path segments that end with '.git' as "foo.git/bar"
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
comment|// common unix wildcard
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"%"
argument_list|)
comment|// wildcard or string parameter
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"*"
argument_list|)
comment|// wildcard
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
comment|// Could be used for absolute paths in windows?
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"<"
argument_list|)
comment|// redirect input
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|">"
argument_list|)
comment|// redirect output
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"|"
argument_list|)
comment|// pipe
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"$"
argument_list|)
comment|// dollar sign
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"\r"
argument_list|)
comment|// carriage return
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"/+"
argument_list|)
comment|// delimiter in /changes/
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"~"
argument_list|)
return|;
comment|// delimiter in /changes/
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|list
parameter_list|()
block|{
name|ProjectVisitor
name|visitor
init|=
operator|new
name|ProjectVisitor
argument_list|(
name|basePath
argument_list|)
decl_stmt|;
name|scanProjects
argument_list|(
name|visitor
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSortedSet
argument_list|(
name|visitor
operator|.
name|found
argument_list|)
return|;
block|}
DECL|method|scanProjects (ProjectVisitor visitor)
specifier|protected
name|void
name|scanProjects
parameter_list|(
name|ProjectVisitor
name|visitor
parameter_list|)
block|{
try|try
block|{
name|Files
operator|.
name|walkFileTree
argument_list|(
name|visitor
operator|.
name|startFolder
argument_list|,
name|EnumSet
operator|.
name|of
argument_list|(
name|FileVisitOption
operator|.
name|FOLLOW_LINKS
argument_list|)
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|,
name|visitor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atSevere
argument_list|()
operator|.
name|withCause
argument_list|(
name|e
argument_list|)
operator|.
name|log
argument_list|(
literal|"Error walking repository tree %s"
argument_list|,
name|visitor
operator|.
name|startFolder
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getProjectName (Path startFolder, Path p)
specifier|private
specifier|static
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|(
name|Path
name|startFolder
parameter_list|,
name|Path
name|p
parameter_list|)
block|{
name|String
name|projectName
init|=
name|startFolder
operator|.
name|relativize
argument_list|(
name|p
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|File
operator|.
name|separatorChar
operator|!=
literal|'/'
condition|)
block|{
name|projectName
operator|=
name|projectName
operator|.
name|replace
argument_list|(
name|File
operator|.
name|separatorChar
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|projectName
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
condition|)
block|{
name|int
name|newLen
init|=
name|projectName
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
decl_stmt|;
name|projectName
operator|=
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|newLen
argument_list|)
expr_stmt|;
block|}
return|return
name|Project
operator|.
name|nameKey
argument_list|(
name|projectName
argument_list|)
return|;
block|}
DECL|class|ProjectVisitor
specifier|protected
class|class
name|ProjectVisitor
extends|extends
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
block|{
DECL|field|found
specifier|private
specifier|final
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|found
init|=
operator|new
name|TreeSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|startFolder
specifier|private
name|Path
name|startFolder
decl_stmt|;
DECL|method|ProjectVisitor (Path startFolder)
specifier|public
name|ProjectVisitor
parameter_list|(
name|Path
name|startFolder
parameter_list|)
block|{
name|setStartFolder
argument_list|(
name|startFolder
argument_list|)
expr_stmt|;
block|}
DECL|method|setStartFolder (Path startFolder)
specifier|public
name|void
name|setStartFolder
parameter_list|(
name|Path
name|startFolder
parameter_list|)
block|{
name|this
operator|.
name|startFolder
operator|=
name|startFolder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|preVisitDirectory (Path dir, BasicFileAttributes attrs)
specifier|public
name|FileVisitResult
name|preVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|dir
operator|.
name|equals
argument_list|(
name|startFolder
argument_list|)
operator|&&
name|isRepo
argument_list|(
name|dir
argument_list|)
condition|)
block|{
name|addProject
argument_list|(
name|dir
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|SKIP_SUBTREE
return|;
block|}
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
annotation|@
name|Override
DECL|method|visitFileFailed (Path file, IOException e)
specifier|public
name|FileVisitResult
name|visitFileFailed
parameter_list|(
name|Path
name|file
parameter_list|,
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
DECL|method|isRepo (Path p)
specifier|private
name|boolean
name|isRepo
parameter_list|(
name|Path
name|p
parameter_list|)
block|{
name|String
name|name
init|=
name|p
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|!
name|name
operator|.
name|equals
argument_list|(
name|Constants
operator|.
name|DOT_GIT
argument_list|)
operator|&&
operator|(
name|name
operator|.
name|endsWith
argument_list|(
name|Constants
operator|.
name|DOT_GIT_EXT
argument_list|)
operator|||
name|FileKey
operator|.
name|isGitRepository
argument_list|(
name|p
operator|.
name|toFile
argument_list|()
argument_list|,
name|FS
operator|.
name|DETECTED
argument_list|)
operator|)
return|;
block|}
DECL|method|addProject (Path p)
specifier|private
name|void
name|addProject
parameter_list|(
name|Path
name|p
parameter_list|)
block|{
name|Project
operator|.
name|NameKey
name|nameKey
init|=
name|getProjectName
argument_list|(
name|startFolder
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|getBasePath
argument_list|(
name|nameKey
argument_list|)
operator|.
name|equals
argument_list|(
name|startFolder
argument_list|)
condition|)
block|{
if|if
condition|(
name|isUnreasonableName
argument_list|(
name|nameKey
argument_list|)
condition|)
block|{
name|logger
operator|.
name|atWarning
argument_list|()
operator|.
name|log
argument_list|(
literal|"Ignoring unreasonably named repository %s"
argument_list|,
name|p
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|found
operator|.
name|add
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

