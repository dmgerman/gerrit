begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
name|schema
operator|.
name|DataSourceProvider
operator|.
name|Context
operator|.
name|MULTI_USER
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
name|Stopwatch
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
name|ArrayListMultimap
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
name|common
operator|.
name|collect
operator|.
name|Multimap
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
name|util
operator|.
name|concurrent
operator|.
name|AsyncFunction
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|util
operator|.
name|concurrent
operator|.
name|ListenableFuture
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
name|util
operator|.
name|concurrent
operator|.
name|ListeningExecutorService
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
name|util
operator|.
name|concurrent
operator|.
name|MoreExecutors
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
name|GitReferenceUpdatedListener
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
name|lifecycle
operator|.
name|LifecycleManager
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
name|pgm
operator|.
name|util
operator|.
name|BatchProgramModule
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
name|pgm
operator|.
name|util
operator|.
name|SiteProgram
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
name|pgm
operator|.
name|util
operator|.
name|ThreadLimiter
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
name|Change
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
name|reviewdb
operator|.
name|client
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|AllUsersName
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
name|git
operator|.
name|MultiProgressMonitor
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
name|MultiProgressMonitor
operator|.
name|Task
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
name|SearchingChangeCacheImpl
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
name|WorkQueue
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
name|index
operator|.
name|DummyIndexModule
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
name|index
operator|.
name|ReindexAfterUpdate
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
name|notedb
operator|.
name|ChangeRebuilder
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
name|notedb
operator|.
name|NoteDbModule
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
name|notedb
operator|.
name|NotesMigration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|SchemaFactory
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
name|AbstractModule
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
name|Injector
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
name|Key
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
name|TypeLiteral
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
name|BatchRefUpdate
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
name|RefDatabase
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
name|transport
operator|.
name|ReceiveCommand
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
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_class
DECL|class|RebuildNotedb
specifier|public
class|class
name|RebuildNotedb
extends|extends
name|SiteProgram
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
name|RebuildNotedb
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--threads"
argument_list|,
name|usage
operator|=
literal|"Number of threads to use for indexing"
argument_list|)
DECL|field|threads
specifier|private
name|int
name|threads
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
DECL|field|dbInjector
specifier|private
name|Injector
name|dbInjector
decl_stmt|;
DECL|field|sysInjector
specifier|private
name|Injector
name|sysInjector
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|mustHaveValidSite
argument_list|()
expr_stmt|;
name|dbInjector
operator|=
name|createDbInjector
argument_list|(
name|MULTI_USER
argument_list|)
expr_stmt|;
name|threads
operator|=
name|ThreadLimiter
operator|.
name|limitThreads
argument_list|(
name|dbInjector
argument_list|,
name|threads
argument_list|)
expr_stmt|;
name|LifecycleManager
name|dbManager
init|=
operator|new
name|LifecycleManager
argument_list|()
decl_stmt|;
name|dbManager
operator|.
name|add
argument_list|(
name|dbInjector
argument_list|)
expr_stmt|;
name|dbManager
operator|.
name|start
argument_list|()
expr_stmt|;
name|sysInjector
operator|=
name|createSysInjector
argument_list|()
expr_stmt|;
name|NotesMigration
name|notesMigration
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|NotesMigration
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|notesMigration
operator|.
name|enabled
argument_list|()
condition|)
block|{
name|die
argument_list|(
literal|"Notedb is not enabled."
argument_list|)
expr_stmt|;
block|}
name|LifecycleManager
name|sysManager
init|=
operator|new
name|LifecycleManager
argument_list|()
decl_stmt|;
name|sysManager
operator|.
name|add
argument_list|(
name|sysInjector
argument_list|)
expr_stmt|;
name|sysManager
operator|.
name|start
argument_list|()
expr_stmt|;
name|ListeningExecutorService
name|executor
init|=
name|newExecutor
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Rebuilding the notedb"
argument_list|)
expr_stmt|;
name|ChangeRebuilder
name|rebuilder
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|ChangeRebuilder
operator|.
name|class
argument_list|)
decl_stmt|;
name|Multimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
argument_list|>
name|changesByProject
init|=
name|getChangesByProject
argument_list|()
decl_stmt|;
specifier|final
name|AtomicBoolean
name|ok
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|Stopwatch
name|sw
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
name|GitRepositoryManager
name|repoManager
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|GitRepositoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Project
operator|.
name|NameKey
name|allUsersName
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|AllUsersName
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|allUsersRepo
init|=
name|repoManager
operator|.
name|openMetadataRepository
argument_list|(
name|allUsersName
argument_list|)
init|)
block|{
name|deleteDraftRefs
argument_list|(
name|allUsersRepo
argument_list|)
expr_stmt|;
for|for
control|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
range|:
name|changesByProject
operator|.
name|keySet
argument_list|()
control|)
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openMetadataRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
specifier|final
name|BatchRefUpdate
name|bru
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
specifier|final
name|BatchRefUpdate
name|bruForDrafts
init|=
name|allUsersRepo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
comment|// Here, we truncate the project name to 50 characters to ensure that
comment|// the whole monitor line for a project fits on one line (<80 chars).
specifier|final
name|MultiProgressMonitor
name|mpm
init|=
operator|new
name|MultiProgressMonitor
argument_list|(
name|System
operator|.
name|out
argument_list|,
name|truncateProjectName
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Task
name|doneTask
init|=
name|mpm
operator|.
name|beginSubTask
argument_list|(
literal|"done"
argument_list|,
name|changesByProject
operator|.
name|get
argument_list|(
name|project
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Task
name|failedTask
init|=
name|mpm
operator|.
name|beginSubTask
argument_list|(
literal|"failed"
argument_list|,
name|MultiProgressMonitor
operator|.
name|UNKNOWN
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|Change
name|c
range|:
name|changesByProject
operator|.
name|get
argument_list|(
name|project
argument_list|)
control|)
block|{
specifier|final
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
init|=
name|rebuilder
operator|.
name|rebuildAsync
argument_list|(
name|c
argument_list|,
name|executor
argument_list|,
name|bru
argument_list|,
name|bruForDrafts
argument_list|,
name|repo
argument_list|,
name|allUsersRepo
argument_list|)
decl_stmt|;
name|futures
operator|.
name|add
argument_list|(
name|future
argument_list|)
expr_stmt|;
name|future
operator|.
name|addListener
argument_list|(
operator|new
name|RebuildListener
argument_list|(
name|c
operator|.
name|getId
argument_list|()
argument_list|,
name|future
argument_list|,
name|ok
argument_list|,
name|doneTask
argument_list|,
name|failedTask
argument_list|)
argument_list|,
name|MoreExecutors
operator|.
name|directExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mpm
operator|.
name|waitFor
argument_list|(
name|Futures
operator|.
name|transformAsync
argument_list|(
name|Futures
operator|.
name|successfulAsList
argument_list|(
name|futures
argument_list|)
argument_list|,
operator|new
name|AsyncFunction
argument_list|<
name|List
argument_list|<
name|?
argument_list|>
argument_list|,
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ListenableFuture
argument_list|<
name|Void
argument_list|>
name|apply
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|bru
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|bruForDrafts
argument_list|,
name|allUsersRepo
argument_list|)
expr_stmt|;
name|mpm
operator|.
name|end
argument_list|()
expr_stmt|;
return|return
name|Futures
operator|.
name|immediateFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
block|}
block|)
block|)
empty_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error rebuilding notedb"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ok
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
end_class

begin_decl_stmt
name|double
name|t
init|=
name|sw
operator|.
name|elapsed
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|/
literal|1000d
decl_stmt|;
end_decl_stmt

begin_expr_stmt
name|System
operator|.
name|out
operator|.
name|format
argument_list|(
literal|"Rebuild %d changes in %.01fs (%.01f/s)\n"
argument_list|,
name|changesByProject
operator|.
name|size
argument_list|()
argument_list|,
name|t
argument_list|,
name|changesByProject
operator|.
name|size
argument_list|()
operator|/
name|t
argument_list|)
expr_stmt|;
end_expr_stmt

begin_return
return|return
name|ok
operator|.
name|get
argument_list|()
condition|?
literal|0
else|:
literal|1
return|;
end_return

begin_function
unit|}    private
DECL|method|truncateProjectName (String projectName)
specifier|static
name|String
name|truncateProjectName
parameter_list|(
name|String
name|projectName
parameter_list|)
block|{
name|int
name|monitorStringMaxLength
init|=
literal|50
decl_stmt|;
name|String
name|monitorString
init|=
operator|(
name|projectName
operator|.
name|length
argument_list|()
operator|>
name|monitorStringMaxLength
operator|)
condition|?
name|projectName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|monitorStringMaxLength
argument_list|)
else|:
name|projectName
decl_stmt|;
if|if
condition|(
name|projectName
operator|.
name|length
argument_list|()
operator|>
name|monitorString
operator|.
name|length
argument_list|()
condition|)
block|{
name|monitorString
operator|=
name|monitorString
operator|+
literal|"..."
expr_stmt|;
block|}
return|return
name|monitorString
return|;
block|}
end_function

begin_function
DECL|method|execute (BatchRefUpdate bru, Repository repo)
specifier|private
specifier|static
name|void
name|execute
parameter_list|(
name|BatchRefUpdate
name|bru
parameter_list|,
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|bru
operator|.
name|execute
argument_list|(
name|rw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|deleteDraftRefs (Repository allUsersRepo)
specifier|private
name|void
name|deleteDraftRefs
parameter_list|(
name|Repository
name|allUsersRepo
parameter_list|)
throws|throws
name|IOException
block|{
name|RefDatabase
name|refDb
init|=
name|allUsersRepo
operator|.
name|getRefDatabase
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|allRefs
init|=
name|refDb
operator|.
name|getRefs
argument_list|(
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
argument_list|)
decl_stmt|;
name|BatchRefUpdate
name|bru
init|=
name|refDb
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Ref
argument_list|>
name|ref
range|:
name|allRefs
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|bru
operator|.
name|addCommand
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|ref
operator|.
name|getValue
argument_list|()
operator|.
name|getObjectId
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
operator|+
name|ref
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|execute
argument_list|(
name|bru
argument_list|,
name|allUsersRepo
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|createSysInjector ()
specifier|private
name|Injector
name|createSysInjector
parameter_list|()
block|{
return|return
name|dbInjector
operator|.
name|createChildInjector
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|install
argument_list|(
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|BatchProgramModule
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|install
argument_list|(
name|SearchingChangeCacheImpl
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|NoteDbModule
argument_list|()
argument_list|)
expr_stmt|;
name|DynamicSet
operator|.
name|bind
argument_list|(
name|binder
argument_list|()
argument_list|,
name|GitReferenceUpdatedListener
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|ReindexAfterUpdate
operator|.
name|class
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DummyIndexModule
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|newExecutor ()
specifier|private
name|ListeningExecutorService
name|newExecutor
parameter_list|()
block|{
if|if
condition|(
name|threads
operator|>
literal|0
condition|)
block|{
return|return
name|MoreExecutors
operator|.
name|listeningDecorator
argument_list|(
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|WorkQueue
operator|.
name|class
argument_list|)
operator|.
name|createQueue
argument_list|(
name|threads
argument_list|,
literal|"RebuildChange"
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
return|;
block|}
block|}
end_function

begin_function
DECL|method|getChangesByProject ()
specifier|private
name|Multimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
argument_list|>
name|getChangesByProject
parameter_list|()
throws|throws
name|OrmException
block|{
comment|// Memorize all changes so we can close the db connection and allow
comment|// rebuilder threads to use the full connection pool.
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
argument_list|)
decl_stmt|;
name|Multimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
argument_list|>
name|changesByProject
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
init|(
name|ReviewDb
name|db
init|=
name|schemaFactory
operator|.
name|open
argument_list|()
init|)
block|{
for|for
control|(
name|Change
name|c
range|:
name|db
operator|.
name|changes
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|changesByProject
operator|.
name|put
argument_list|(
name|c
operator|.
name|getProject
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
return|return
name|changesByProject
return|;
block|}
block|}
end_function

begin_class
DECL|class|RebuildListener
specifier|private
specifier|static
class|class
name|RebuildListener
implements|implements
name|Runnable
block|{
DECL|field|changeId
specifier|private
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|future
specifier|private
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|ok
specifier|private
name|AtomicBoolean
name|ok
decl_stmt|;
DECL|field|doneTask
specifier|private
name|Task
name|doneTask
decl_stmt|;
DECL|field|failedTask
specifier|private
name|Task
name|failedTask
decl_stmt|;
DECL|method|RebuildListener (Change.Id changeId, ListenableFuture<?> future, AtomicBoolean ok, Task doneTask, Task failedTask)
specifier|private
name|RebuildListener
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
parameter_list|,
name|AtomicBoolean
name|ok
parameter_list|,
name|Task
name|doneTask
parameter_list|,
name|Task
name|failedTask
parameter_list|)
block|{
name|this
operator|.
name|changeId
operator|=
name|changeId
expr_stmt|;
name|this
operator|.
name|future
operator|=
name|future
expr_stmt|;
name|this
operator|.
name|ok
operator|=
name|ok
expr_stmt|;
name|this
operator|.
name|doneTask
operator|=
name|doneTask
expr_stmt|;
name|this
operator|.
name|failedTask
operator|=
name|failedTask
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
name|doneTask
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExecutionException
decl||
name|InterruptedException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|failAndThrow
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
comment|// Can't join with RuntimeException because "RuntimeException
comment|// | Error" becomes Throwable, which messes with signatures.
name|failAndThrow
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|fail (Throwable t)
specifier|private
name|void
name|fail
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to rebuild change "
operator|+
name|changeId
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|ok
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|failedTask
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|failAndThrow (RuntimeException e)
specifier|private
name|void
name|failAndThrow
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
DECL|method|failAndThrow (Error e)
specifier|private
name|void
name|failAndThrow
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
end_class

unit|}
end_unit

