begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|lucene
operator|.
name|LuceneIndexModule
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
name|cache
operator|.
name|CacheRemovalListener
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
name|cache
operator|.
name|h2
operator|.
name|DefaultCacheFactory
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
name|index
operator|.
name|ChangeBatchIndexer
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
name|ChangeIndex
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
name|ChangeSchemas
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
name|IndexCollection
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
name|IndexModule
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
name|patch
operator|.
name|PatchListCacheImpl
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
name|schema
operator|.
name|DataSourceProvider
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
name|schema
operator|.
name|DataSourceType
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
name|solr
operator|.
name|SolrIndexModule
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
name|Module
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
name|ProvisionException
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|io
operator|.
name|NullOutputStream
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
name|List
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
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_class
DECL|class|Reindex
specifier|public
class|class
name|Reindex
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
name|Reindex
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
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--schema-version"
argument_list|,
name|usage
operator|=
literal|"Schema version to reindex; default is most recent version"
argument_list|)
DECL|field|version
specifier|private
name|Integer
name|version
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--output"
argument_list|,
name|usage
operator|=
literal|"Prefix for output; path for local disk index, or prefix for remote index"
argument_list|)
DECL|field|outputBase
specifier|private
name|String
name|outputBase
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--verbose"
argument_list|,
name|usage
operator|=
literal|"Output debug information for each change"
argument_list|)
DECL|field|verbose
specifier|private
name|boolean
name|verbose
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--dry-run"
argument_list|,
name|usage
operator|=
literal|"Dry run: don't write anything to index"
argument_list|)
DECL|field|dryRun
specifier|private
name|boolean
name|dryRun
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
DECL|field|index
specifier|private
name|ChangeIndex
name|index
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
name|limitThreads
argument_list|()
expr_stmt|;
if|if
condition|(
name|version
operator|==
literal|null
condition|)
block|{
name|version
operator|=
name|ChangeSchemas
operator|.
name|getLatest
argument_list|()
operator|.
name|getVersion
argument_list|()
expr_stmt|;
block|}
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
name|index
operator|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|IndexCollection
operator|.
name|class
argument_list|)
operator|.
name|getSearchIndex
argument_list|()
expr_stmt|;
name|index
operator|.
name|markReady
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|index
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
name|int
name|result
init|=
name|indexAll
argument_list|()
decl_stmt|;
name|index
operator|.
name|markReady
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sysManager
operator|.
name|stop
argument_list|()
expr_stmt|;
name|dbManager
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
DECL|method|limitThreads ()
specifier|private
name|void
name|limitThreads
parameter_list|()
block|{
name|Config
name|cfg
init|=
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|Key
operator|.
name|get
argument_list|(
name|Config
operator|.
name|class
argument_list|,
name|GerritServerConfig
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|usePool
init|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"database"
argument_list|,
literal|"connectionpool"
argument_list|,
name|dbInjector
operator|.
name|getInstance
argument_list|(
name|DataSourceType
operator|.
name|class
argument_list|)
operator|.
name|usePool
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|poolLimit
init|=
name|cfg
operator|.
name|getInt
argument_list|(
literal|"database"
argument_list|,
literal|"poollimit"
argument_list|,
name|DataSourceProvider
operator|.
name|DEFAULT_POOL_LIMIT
argument_list|)
decl_stmt|;
if|if
condition|(
name|usePool
operator|&&
name|threads
operator|>
name|poolLimit
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Limiting reindexing to "
operator|+
name|poolLimit
operator|+
literal|" threads due to database.poolLimit"
argument_list|)
expr_stmt|;
name|threads
operator|=
name|poolLimit
expr_stmt|;
block|}
block|}
DECL|method|createSysInjector ()
specifier|private
name|Injector
name|createSysInjector
parameter_list|()
block|{
name|List
argument_list|<
name|Module
argument_list|>
name|modules
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|modules
operator|.
name|add
argument_list|(
name|PatchListCacheImpl
operator|.
name|module
argument_list|()
argument_list|)
expr_stmt|;
name|AbstractModule
name|changeIndexModule
decl_stmt|;
switch|switch
condition|(
name|IndexModule
operator|.
name|getIndexType
argument_list|(
name|dbInjector
argument_list|)
condition|)
block|{
case|case
name|LUCENE
case|:
name|changeIndexModule
operator|=
operator|new
name|LuceneIndexModule
argument_list|(
name|version
argument_list|,
name|threads
argument_list|,
name|outputBase
argument_list|)
expr_stmt|;
break|break;
case|case
name|SOLR
case|:
name|changeIndexModule
operator|=
operator|new
name|SolrIndexModule
argument_list|(
literal|false
argument_list|,
name|threads
argument_list|,
name|outputBase
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unsupported index.type"
argument_list|)
throw|;
block|}
name|modules
operator|.
name|add
argument_list|(
name|changeIndexModule
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|ReviewDbModule
argument_list|()
argument_list|)
expr_stmt|;
name|modules
operator|.
name|add
argument_list|(
operator|new
name|AbstractModule
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
comment|// Plugins are not loaded and we're just running through each change
comment|// once, so don't worry about cache removal.
name|bind
argument_list|(
operator|new
name|TypeLiteral
argument_list|<
name|DynamicSet
argument_list|<
name|CacheRemovalListener
argument_list|>
argument_list|>
argument_list|()
block|{}
argument_list|)
operator|.
name|toInstance
argument_list|(
name|DynamicSet
operator|.
expr|<
name|CacheRemovalListener
operator|>
name|emptySet
argument_list|()
argument_list|)
expr_stmt|;
name|install
argument_list|(
operator|new
name|DefaultCacheFactory
operator|.
name|Module
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|dbInjector
operator|.
name|createChildInjector
argument_list|(
name|modules
argument_list|)
return|;
block|}
DECL|class|ReviewDbModule
specifier|private
class|class
name|ReviewDbModule
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
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schema
init|=
name|dbInjector
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
specifier|final
name|List
argument_list|<
name|ReviewDb
argument_list|>
name|dbs
init|=
name|Collections
operator|.
name|synchronizedList
argument_list|(
name|Lists
operator|.
expr|<
name|ReviewDb
operator|>
name|newArrayListWithCapacity
argument_list|(
name|threads
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|ThreadLocal
argument_list|<
name|ReviewDb
argument_list|>
name|localDb
init|=
operator|new
name|ThreadLocal
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
decl_stmt|;
name|bind
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
operator|.
name|toProvider
argument_list|(
operator|new
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ReviewDb
name|get
parameter_list|()
block|{
name|ReviewDb
name|db
init|=
name|localDb
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|db
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|db
operator|=
name|schema
operator|.
name|open
argument_list|()
expr_stmt|;
name|dbs
operator|.
name|add
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|localDb
operator|.
name|set
argument_list|(
name|db
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ProvisionException
argument_list|(
literal|"unable to open ReviewDb"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|db
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|toInstance
argument_list|(
operator|new
name|LifecycleListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{
for|for
control|(
name|ReviewDb
name|db
range|:
name|dbs
control|)
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|indexAll ()
specifier|private
name|int
name|indexAll
parameter_list|()
throws|throws
name|Exception
block|{
name|ReviewDb
name|db
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProgressMonitor
name|pm
init|=
operator|new
name|TextProgressMonitor
argument_list|()
decl_stmt|;
name|pm
operator|.
name|start
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|pm
operator|.
name|beginTask
argument_list|(
literal|"Collecting projects"
argument_list|,
name|ProgressMonitor
operator|.
name|UNKNOWN
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|projects
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|()
decl_stmt|;
name|int
name|changeCount
init|=
literal|0
decl_stmt|;
try|try
block|{
for|for
control|(
name|Change
name|change
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
name|changeCount
operator|++
expr_stmt|;
if|if
condition|(
name|projects
operator|.
name|add
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|)
condition|)
block|{
name|pm
operator|.
name|update
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|db
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|pm
operator|.
name|endTask
argument_list|()
expr_stmt|;
name|ChangeBatchIndexer
name|batchIndexer
init|=
name|sysInjector
operator|.
name|getInstance
argument_list|(
name|ChangeBatchIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
name|ChangeBatchIndexer
operator|.
name|Result
name|result
init|=
name|batchIndexer
operator|.
name|indexAll
argument_list|(
name|index
argument_list|,
name|projects
argument_list|,
name|projects
operator|.
name|size
argument_list|()
argument_list|,
name|changeCount
argument_list|,
name|System
operator|.
name|err
argument_list|,
name|verbose
condition|?
name|System
operator|.
name|out
else|:
name|NullOutputStream
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|int
name|n
init|=
name|result
operator|.
name|doneCount
argument_list|()
operator|+
name|result
operator|.
name|failedCount
argument_list|()
decl_stmt|;
name|double
name|t
init|=
name|result
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
literal|"Reindexed %d changes in %.01fs (%.01f/s)\n"
argument_list|,
name|n
argument_list|,
name|t
argument_list|,
name|n
operator|/
name|t
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|success
argument_list|()
condition|?
literal|0
else|:
literal|1
return|;
block|}
block|}
end_class

end_unit

