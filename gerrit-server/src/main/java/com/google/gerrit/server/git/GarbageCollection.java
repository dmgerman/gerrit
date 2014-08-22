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
name|collect
operator|.
name|Sets
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
name|data
operator|.
name|GarbageCollectionResult
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
name|GarbageCollectorListener
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
name|GarbageCollectorListener
operator|.
name|Event
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
name|DynamicSet
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
name|config
operator|.
name|GcConfig
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
name|api
operator|.
name|GarbageCollectCommand
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
name|api
operator|.
name|Git
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
name|NullProgressMonitor
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
name|TextProgressMonitor
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
name|pack
operator|.
name|PackConfig
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
name|PrintWriter
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
name|java
operator|.
name|util
operator|.
name|Properties
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

begin_class
DECL|class|GarbageCollection
specifier|public
class|class
name|GarbageCollection
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
name|GarbageCollection
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOG_NAME
specifier|public
specifier|static
specifier|final
name|String
name|LOG_NAME
init|=
literal|"gc_log"
decl_stmt|;
DECL|field|gcLog
specifier|private
specifier|static
specifier|final
name|Logger
name|gcLog
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|LOG_NAME
argument_list|)
decl_stmt|;
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|gcQueue
specifier|private
specifier|final
name|GarbageCollectionQueue
name|gcQueue
decl_stmt|;
DECL|field|gcConfig
specifier|private
specifier|final
name|GcConfig
name|gcConfig
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|GarbageCollectorListener
argument_list|>
name|listeners
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ()
name|GarbageCollection
name|create
parameter_list|()
function_decl|;
block|}
annotation|@
name|Inject
DECL|method|GarbageCollection (GitRepositoryManager repoManager, GarbageCollectionQueue gcQueue, GcConfig config, DynamicSet<GarbageCollectorListener> listeners)
name|GarbageCollection
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|GarbageCollectionQueue
name|gcQueue
parameter_list|,
name|GcConfig
name|config
parameter_list|,
name|DynamicSet
argument_list|<
name|GarbageCollectorListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|gcQueue
operator|=
name|gcQueue
expr_stmt|;
name|this
operator|.
name|gcConfig
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
DECL|method|run (List<Project.NameKey> projectNames)
specifier|public
name|GarbageCollectionResult
name|run
parameter_list|(
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectNames
parameter_list|)
block|{
return|return
name|run
argument_list|(
name|projectNames
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|run (List<Project.NameKey> projectNames, PrintWriter writer)
specifier|public
name|GarbageCollectionResult
name|run
parameter_list|(
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectNames
parameter_list|,
name|PrintWriter
name|writer
parameter_list|)
block|{
return|return
name|run
argument_list|(
name|projectNames
argument_list|,
name|gcConfig
operator|.
name|isAggressive
argument_list|()
argument_list|,
name|writer
argument_list|)
return|;
block|}
DECL|method|run (List<Project.NameKey> projectNames, boolean aggressive, PrintWriter writer)
specifier|public
name|GarbageCollectionResult
name|run
parameter_list|(
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectNames
parameter_list|,
name|boolean
name|aggressive
parameter_list|,
name|PrintWriter
name|writer
parameter_list|)
block|{
name|GarbageCollectionResult
name|result
init|=
operator|new
name|GarbageCollectionResult
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectsToGc
init|=
name|gcQueue
operator|.
name|addAll
argument_list|(
name|projectNames
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|projectName
range|:
name|Sets
operator|.
name|difference
argument_list|(
name|Sets
operator|.
name|newHashSet
argument_list|(
name|projectNames
argument_list|)
argument_list|,
name|projectsToGc
argument_list|)
control|)
block|{
name|result
operator|.
name|addError
argument_list|(
operator|new
name|GarbageCollectionResult
operator|.
name|Error
argument_list|(
name|GarbageCollectionResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|GC_ALREADY_SCHEDULED
argument_list|,
name|projectName
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projectsToGc
control|)
block|{
name|Repository
name|repo
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
name|p
argument_list|)
expr_stmt|;
name|logGcConfiguration
argument_list|(
name|p
argument_list|,
name|repo
argument_list|,
name|aggressive
argument_list|)
expr_stmt|;
name|print
argument_list|(
name|writer
argument_list|,
literal|"collecting garbage for \""
operator|+
name|p
operator|+
literal|"\":\n"
argument_list|)
expr_stmt|;
name|GarbageCollectCommand
name|gc
init|=
name|Git
operator|.
name|wrap
argument_list|(
name|repo
argument_list|)
operator|.
name|gc
argument_list|()
decl_stmt|;
name|gc
operator|.
name|setAggressive
argument_list|(
name|aggressive
argument_list|)
expr_stmt|;
name|logGcInfo
argument_list|(
name|p
argument_list|,
literal|"before:"
argument_list|,
name|gc
operator|.
name|getStatistics
argument_list|()
argument_list|)
expr_stmt|;
name|gc
operator|.
name|setProgressMonitor
argument_list|(
name|writer
operator|!=
literal|null
condition|?
operator|new
name|TextProgressMonitor
argument_list|(
name|writer
argument_list|)
else|:
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|Properties
name|statistics
init|=
name|gc
operator|.
name|call
argument_list|()
decl_stmt|;
name|logGcInfo
argument_list|(
name|p
argument_list|,
literal|"after: "
argument_list|,
name|statistics
argument_list|)
expr_stmt|;
name|print
argument_list|(
name|writer
argument_list|,
literal|"done.\n\n"
argument_list|)
expr_stmt|;
name|fire
argument_list|(
name|p
argument_list|,
name|statistics
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|logGcError
argument_list|(
name|writer
argument_list|,
name|p
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|GarbageCollectionResult
operator|.
name|Error
argument_list|(
name|GarbageCollectionResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|REPOSITORY_NOT_FOUND
argument_list|,
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logGcError
argument_list|(
name|writer
argument_list|,
name|p
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|result
operator|.
name|addError
argument_list|(
operator|new
name|GarbageCollectionResult
operator|.
name|Error
argument_list|(
name|GarbageCollectionResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|GC_FAILED
argument_list|,
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
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
name|gcQueue
operator|.
name|gcFinished
argument_list|(
name|p
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|fire (final Project.NameKey p, final Properties statistics)
specifier|private
name|void
name|fire
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|p
parameter_list|,
specifier|final
name|Properties
name|statistics
parameter_list|)
block|{
name|Event
name|event
init|=
operator|new
name|GarbageCollectorListener
operator|.
name|Event
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getProjectName
parameter_list|()
block|{
return|return
name|p
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Properties
name|getStatistics
parameter_list|()
block|{
return|return
name|statistics
return|;
block|}
block|}
decl_stmt|;
for|for
control|(
name|GarbageCollectorListener
name|l
range|:
name|listeners
control|)
block|{
try|try
block|{
name|l
operator|.
name|onGarbageCollected
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failure in GarbageCollectorListener"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|logGcInfo (Project.NameKey projectName, String msg)
specifier|private
specifier|static
name|void
name|logGcInfo
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|logGcInfo
argument_list|(
name|projectName
argument_list|,
name|msg
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|logGcInfo (Project.NameKey projectName, String msg, Properties statistics)
specifier|private
specifier|static
name|void
name|logGcInfo
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|String
name|msg
parameter_list|,
name|Properties
name|statistics
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"] "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|msg
argument_list|)
expr_stmt|;
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|statistics
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|s
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|gcLog
operator|.
name|info
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|logGcConfiguration (Project.NameKey projectName, Repository repo, boolean aggressive)
specifier|private
specifier|static
name|void
name|logGcConfiguration
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|boolean
name|aggressive
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Config
name|cfg
init|=
name|repo
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"gc.aggressive="
argument_list|)
operator|.
name|append
argument_list|(
name|aggressive
argument_list|)
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|formatConfigValues
argument_list|(
name|cfg
argument_list|,
name|ConfigConstants
operator|.
name|CONFIG_GC_SECTION
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|subsection
range|:
name|cfg
operator|.
name|getSubsections
argument_list|(
name|ConfigConstants
operator|.
name|CONFIG_GC_SECTION
argument_list|)
control|)
block|{
name|b
operator|.
name|append
argument_list|(
name|formatConfigValues
argument_list|(
name|cfg
argument_list|,
name|ConfigConstants
operator|.
name|CONFIG_GC_SECTION
argument_list|,
name|subsection
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|b
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"no set"
argument_list|)
expr_stmt|;
block|}
name|logGcInfo
argument_list|(
name|projectName
argument_list|,
literal|"gc config: "
operator|+
name|b
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|logGcInfo
argument_list|(
name|projectName
argument_list|,
literal|"pack config: "
operator|+
operator|(
operator|new
name|PackConfig
argument_list|(
name|repo
argument_list|)
operator|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|formatConfigValues (Config config, String section, String subsection)
specifier|private
specifier|static
name|String
name|formatConfigValues
parameter_list|(
name|Config
name|config
parameter_list|,
name|String
name|section
parameter_list|,
name|String
name|subsection
parameter_list|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|names
init|=
name|config
operator|.
name|getNames
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|names
control|)
block|{
name|String
name|value
init|=
name|config
operator|.
name|getString
argument_list|(
name|section
argument_list|,
name|subsection
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
name|section
argument_list|)
expr_stmt|;
if|if
condition|(
name|subsection
operator|!=
literal|null
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|subsection
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|logGcError (PrintWriter writer, Project.NameKey projectName, Exception e)
specifier|private
specifier|static
name|void
name|logGcError
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|print
argument_list|(
name|writer
argument_list|,
literal|"failed.\n\n"
argument_list|)
expr_stmt|;
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
operator|.
name|append
argument_list|(
name|projectName
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
name|gcLog
operator|.
name|error
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|log
operator|.
name|error
argument_list|(
name|b
operator|.
name|toString
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|print (PrintWriter writer, String message)
specifier|private
specifier|static
name|void
name|print
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|print
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

