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
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDbUtil
operator|.
name|unwrapDb
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
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|Predicates
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
name|ImmutableListMultimap
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
name|Iterables
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
name|ListMultimap
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
name|MultimapBuilder
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
name|Ordering
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
name|common
operator|.
name|FormatUtil
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
name|config
operator|.
name|FactoryModule
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
name|change
operator|.
name|ChangeResource
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
name|change
operator|.
name|ReindexAfterRefUpdate
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
name|ChangeBundleReader
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
name|NoteDbUpdateManager
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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|rebuild
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
name|rebuild
operator|.
name|ChangeRebuilder
operator|.
name|NoPatchSetsException
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
name|Injector
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
name|IOException
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ProgressMonitor
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
DECL|class|RebuildNoteDb
specifier|public
class|class
name|RebuildNoteDb
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
name|RebuildNoteDb
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
literal|"Number of threads to use for rebuilding NoteDb"
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--project"
argument_list|,
name|usage
operator|=
literal|"Projects to rebuild; recommended for debugging only"
argument_list|)
DECL|field|projects
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|projects
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--change"
argument_list|,
name|usage
operator|=
literal|"Individual change numbers to rebuild; recommended for debugging only"
argument_list|)
DECL|field|changes
specifier|private
name|List
argument_list|<
name|Integer
argument_list|>
name|changes
init|=
operator|new
name|ArrayList
argument_list|<>
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
DECL|field|rebuilder
annotation|@
name|Inject
specifier|private
name|ChangeRebuilder
name|rebuilder
decl_stmt|;
DECL|field|updateManagerFactory
annotation|@
name|Inject
specifier|private
name|NoteDbUpdateManager
operator|.
name|Factory
name|updateManagerFactory
decl_stmt|;
DECL|field|notesMigration
annotation|@
name|Inject
specifier|private
name|NotesMigration
name|notesMigration
decl_stmt|;
DECL|field|schemaFactory
annotation|@
name|Inject
specifier|private
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|workQueue
annotation|@
name|Inject
specifier|private
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|bundleReader
annotation|@
name|Inject
specifier|private
name|ChangeBundleReader
name|bundleReader
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
name|sysInjector
operator|.
name|injectMembers
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|notesMigration
operator|.
name|enabled
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"NoteDb is not enabled."
argument_list|)
throw|;
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
literal|"Rebuilding the NoteDb"
argument_list|)
expr_stmt|;
name|ImmutableListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|changesByProject
init|=
name|getChangesByProject
argument_list|()
decl_stmt|;
name|boolean
name|ok
decl_stmt|;
name|Stopwatch
name|sw
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ListenableFuture
argument_list|<
name|Boolean
argument_list|>
argument_list|>
name|futures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projectNames
init|=
name|Ordering
operator|.
name|usingToString
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|changesByProject
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|project
range|:
name|projectNames
control|)
block|{
name|ListenableFuture
argument_list|<
name|Boolean
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
try|try
init|(
name|ReviewDb
name|db
init|=
name|unwrapDb
argument_list|(
name|schemaFactory
operator|.
name|open
argument_list|()
argument_list|)
init|)
block|{
return|return
name|rebuildProject
argument_list|(
name|db
argument_list|,
name|changesByProject
argument_list|,
name|project
argument_list|)
return|;
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
literal|"Error rebuilding project "
operator|+
name|project
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|futures
operator|.
name|add
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ok
operator|=
name|Iterables
operator|.
name|all
argument_list|(
name|Futures
operator|.
name|allAsList
argument_list|(
name|futures
argument_list|)
operator|.
name|get
argument_list|()
argument_list|,
name|Predicates
operator|.
name|equalTo
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error rebuilding projects"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ok
operator|=
literal|false
expr_stmt|;
block|}
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
return|return
name|ok
condition|?
literal|0
else|:
literal|1
return|;
block|}
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
name|FactoryModule
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
name|ReindexAfterRefUpdate
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
name|factory
argument_list|(
name|ChangeResource
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
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
name|workQueue
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
return|return
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
return|;
block|}
DECL|method|getChangesByProject ()
specifier|private
name|ImmutableListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|getChangesByProject
parameter_list|()
throws|throws
name|OrmException
block|{
comment|// Memorize all changes so we can close the db connection and allow
comment|// rebuilder threads to use the full connection pool.
name|ListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|changesByProject
init|=
name|MultimapBuilder
operator|.
name|hashKeys
argument_list|()
operator|.
name|arrayListValues
argument_list|()
operator|.
name|build
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
if|if
condition|(
name|projects
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Iterable
argument_list|<
name|Change
argument_list|>
name|todo
init|=
name|unwrapDb
argument_list|(
name|db
argument_list|)
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|Iterables
operator|.
name|transform
argument_list|(
name|changes
argument_list|,
name|Change
operator|.
name|Id
operator|::
operator|new
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Change
name|c
range|:
name|todo
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
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|Change
name|c
range|:
name|unwrapDb
argument_list|(
name|db
argument_list|)
operator|.
name|changes
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|boolean
name|include
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|projects
operator|.
name|isEmpty
argument_list|()
operator|&&
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|include
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
operator|&&
name|projects
operator|.
name|contains
argument_list|(
name|c
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|include
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|changes
operator|.
name|isEmpty
argument_list|()
operator|&&
name|changes
operator|.
name|contains
argument_list|(
name|c
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
name|include
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|include
condition|)
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
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|changesByProject
argument_list|)
return|;
block|}
block|}
DECL|method|rebuildProject ( ReviewDb db, ImmutableListMultimap<Project.NameKey, Change.Id> allChanges, Project.NameKey project)
specifier|private
name|boolean
name|rebuildProject
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ImmutableListMultimap
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
name|allChanges
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|checkArgument
argument_list|(
name|allChanges
operator|.
name|containsKey
argument_list|(
name|project
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
name|ProgressMonitor
name|pm
init|=
operator|new
name|TextProgressMonitor
argument_list|(
operator|new
name|PrintWriter
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|System
operator|.
name|out
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|pm
operator|.
name|beginTask
argument_list|(
name|FormatUtil
operator|.
name|elide
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|50
argument_list|)
argument_list|,
name|allChanges
operator|.
name|get
argument_list|(
name|project
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
try|try
init|(
name|NoteDbUpdateManager
name|manager
init|=
name|updateManagerFactory
operator|.
name|create
argument_list|(
name|project
argument_list|)
init|)
block|{
for|for
control|(
name|Change
operator|.
name|Id
name|changeId
range|:
name|allChanges
operator|.
name|get
argument_list|(
name|project
argument_list|)
control|)
block|{
try|try
block|{
name|rebuilder
operator|.
name|buildUpdates
argument_list|(
name|manager
argument_list|,
name|bundleReader
operator|.
name|fromReviewDb
argument_list|(
name|db
argument_list|,
name|changeId
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoPatchSetsException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
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
operator|=
literal|false
expr_stmt|;
block|}
name|pm
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|manager
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|pm
operator|.
name|endTask
argument_list|()
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
block|}
end_class

end_unit

