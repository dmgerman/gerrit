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
name|MoreObjects
operator|.
name|firstNonNull
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
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|joining
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|ImmutableList
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
name|RuntimeShutdown
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|notedb
operator|.
name|rebuild
operator|.
name|NoteDbMigrator
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
name|kohsuke
operator|.
name|args4j
operator|.
name|spi
operator|.
name|ExplicitBooleanOptionHandler
import|;
end_import

begin_class
DECL|class|MigrateToNoteDb
specifier|public
class|class
name|MigrateToNoteDb
extends|extends
name|SiteProgram
block|{
DECL|field|TRIAL_USAGE
specifier|static
specifier|final
name|String
name|TRIAL_USAGE
init|=
literal|"Trial mode: migrate changes and turn on reading from NoteDb, but leave ReviewDb as the"
operator|+
literal|" source of truth"
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
literal|"Only rebuild these projects, do no other migration; incompatible with --change;"
operator|+
literal|" recommended for debugging only"
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
literal|"Only rebuild these changes, do no other migration; incompatible with --project;"
operator|+
literal|" recommended for debugging only"
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--force"
argument_list|,
name|usage
operator|=
literal|"Force rebuilding changes where ReviewDb is still the source of truth, even if they"
operator|+
literal|" were previously migrated"
argument_list|)
DECL|field|force
specifier|private
name|boolean
name|force
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--trial"
argument_list|,
name|usage
operator|=
name|TRIAL_USAGE
argument_list|)
DECL|field|trial
specifier|private
name|boolean
name|trial
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--sequence-gap"
argument_list|,
name|usage
operator|=
literal|"gap in change sequence numbers between last ReviewDb number and first NoteDb number;"
operator|+
literal|" negative indicates using the value of noteDb.changes.initialSequenceGap (default"
operator|+
literal|" 1000)"
argument_list|)
DECL|field|sequenceGap
specifier|private
name|int
name|sequenceGap
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--reindex"
argument_list|,
name|usage
operator|=
literal|"Reindex all changes after migration; defaults to false in trial mode, true otherwise"
argument_list|,
name|handler
operator|=
name|ExplicitBooleanOptionHandler
operator|.
name|class
argument_list|)
DECL|field|reindex
specifier|private
name|Boolean
name|reindex
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
DECL|field|dbManager
specifier|private
name|LifecycleManager
name|dbManager
decl_stmt|;
DECL|field|sysManager
specifier|private
name|LifecycleManager
name|sysManager
decl_stmt|;
DECL|field|migratorBuilderProvider
annotation|@
name|Inject
specifier|private
name|Provider
argument_list|<
name|NoteDbMigrator
operator|.
name|Builder
argument_list|>
name|migratorBuilderProvider
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
name|RuntimeShutdown
operator|.
name|add
argument_list|(
name|this
operator|::
name|stop
argument_list|)
expr_stmt|;
try|try
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
name|dbManager
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
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
name|sysManager
operator|=
operator|new
name|LifecycleManager
argument_list|()
expr_stmt|;
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
try|try
init|(
name|NoteDbMigrator
name|migrator
init|=
name|migratorBuilderProvider
operator|.
name|get
argument_list|()
operator|.
name|setThreads
argument_list|(
name|threads
argument_list|)
operator|.
name|setProgressOut
argument_list|(
name|System
operator|.
name|err
argument_list|)
operator|.
name|setProjects
argument_list|(
name|projects
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Project
operator|.
name|NameKey
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setChanges
argument_list|(
name|changes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Change
operator|.
name|Id
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|setTrialMode
argument_list|(
name|trial
argument_list|)
operator|.
name|setForceRebuild
argument_list|(
name|force
argument_list|)
operator|.
name|setSequenceGap
argument_list|(
name|sequenceGap
argument_list|)
operator|.
name|build
argument_list|()
init|)
block|{
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|changes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|migrator
operator|.
name|rebuild
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|migrator
operator|.
name|migrate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|stop
argument_list|()
expr_stmt|;
block|}
name|boolean
name|reindex
init|=
name|firstNonNull
argument_list|(
name|this
operator|.
name|reindex
argument_list|,
operator|!
name|trial
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|reindex
condition|)
block|{
return|return
literal|0
return|;
block|}
comment|// Reindex all indices, to save the user from having to run yet another program by hand while
comment|// their server is offline.
name|List
argument_list|<
name|String
argument_list|>
name|reindexArgs
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"--site-path"
argument_list|,
name|getSitePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
literal|"--threads"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|threads
argument_list|)
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Migration complete, reindexing changes with:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"  reindex "
operator|+
name|reindexArgs
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|joining
argument_list|(
literal|" "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|Reindex
name|reindexPgm
init|=
operator|new
name|Reindex
argument_list|()
decl_stmt|;
return|return
name|reindexPgm
operator|.
name|main
argument_list|(
name|reindexArgs
operator|.
name|stream
argument_list|()
operator|.
name|toArray
argument_list|(
name|String
index|[]
operator|::
operator|new
argument_list|)
argument_list|)
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
name|bind
argument_list|(
name|GitReferenceUpdated
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
name|GitReferenceUpdated
operator|.
name|DISABLED
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
DECL|method|stop ()
specifier|private
name|void
name|stop
parameter_list|()
block|{
try|try
block|{
name|LifecycleManager
name|m
init|=
name|sysManager
decl_stmt|;
name|sysManager
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|LifecycleManager
name|m
init|=
name|dbManager
decl_stmt|;
name|dbManager
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|m
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit
