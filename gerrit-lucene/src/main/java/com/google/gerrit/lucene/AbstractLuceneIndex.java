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
DECL|package|com.google.gerrit.lucene
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|lucene
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MILLISECONDS
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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|AbstractFuture
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
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactoryBuilder
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
name|index
operator|.
name|IndexUtils
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
name|gerrit
operator|.
name|server
operator|.
name|index
operator|.
name|FieldDef
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
name|FieldDef
operator|.
name|FillArgs
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
name|FieldType
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
name|Index
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
name|Schema
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
name|Schema
operator|.
name|Values
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
operator|.
name|Store
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|IntField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|LongField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StoredField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StringField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|TextField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|TrackingIndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|ControlledRealTimeReopenThread
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|IndexSearcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|ReferenceManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|ReferenceManager
operator|.
name|RefreshListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|SearcherFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|AlreadyClosedException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|Directory
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
name|sql
operator|.
name|Timestamp
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
name|Callable
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
name|Executor
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
name|Executors
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
name|ScheduledThreadPoolExecutor
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
name|TimeoutException
import|;
end_import

begin_comment
comment|/** Basic Lucene index implementation. */
end_comment

begin_class
DECL|class|AbstractLuceneIndex
specifier|public
specifier|abstract
class|class
name|AbstractLuceneIndex
parameter_list|<
name|K
parameter_list|,
name|V
parameter_list|>
implements|implements
name|Index
argument_list|<
name|K
argument_list|,
name|V
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
name|AbstractLuceneIndex
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|sortFieldName (FieldDef<?, ?> f)
specifier|static
name|String
name|sortFieldName
parameter_list|(
name|FieldDef
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|f
parameter_list|)
block|{
return|return
name|f
operator|.
name|getName
argument_list|()
operator|+
literal|"_SORT"
return|;
block|}
DECL|field|schema
specifier|private
specifier|final
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
decl_stmt|;
DECL|field|sitePaths
specifier|private
specifier|final
name|SitePaths
name|sitePaths
decl_stmt|;
DECL|field|dir
specifier|private
specifier|final
name|Directory
name|dir
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|writerThread
specifier|private
specifier|final
name|ListeningExecutorService
name|writerThread
decl_stmt|;
DECL|field|writer
specifier|private
specifier|final
name|TrackingIndexWriter
name|writer
decl_stmt|;
DECL|field|searcherManager
specifier|private
specifier|final
name|ReferenceManager
argument_list|<
name|IndexSearcher
argument_list|>
name|searcherManager
decl_stmt|;
DECL|field|reopenThread
specifier|private
specifier|final
name|ControlledRealTimeReopenThread
argument_list|<
name|IndexSearcher
argument_list|>
name|reopenThread
decl_stmt|;
DECL|field|notDoneNrtFutures
specifier|private
specifier|final
name|Set
argument_list|<
name|NrtFuture
argument_list|>
name|notDoneNrtFutures
decl_stmt|;
DECL|field|autoCommitExecutor
specifier|private
name|ScheduledThreadPoolExecutor
name|autoCommitExecutor
decl_stmt|;
DECL|method|AbstractLuceneIndex ( Schema<V> schema, SitePaths sitePaths, Directory dir, String name, String subIndex, GerritIndexWriterConfig writerConfig, SearcherFactory searcherFactory)
name|AbstractLuceneIndex
parameter_list|(
name|Schema
argument_list|<
name|V
argument_list|>
name|schema
parameter_list|,
name|SitePaths
name|sitePaths
parameter_list|,
name|Directory
name|dir
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|subIndex
parameter_list|,
name|GerritIndexWriterConfig
name|writerConfig
parameter_list|,
name|SearcherFactory
name|searcherFactory
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|sitePaths
operator|=
name|sitePaths
expr_stmt|;
name|this
operator|.
name|dir
operator|=
name|dir
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
specifier|final
name|String
name|index
init|=
name|Joiner
operator|.
name|on
argument_list|(
literal|'_'
argument_list|)
operator|.
name|skipNulls
argument_list|()
operator|.
name|join
argument_list|(
name|name
argument_list|,
name|subIndex
argument_list|)
decl_stmt|;
name|IndexWriter
name|delegateWriter
decl_stmt|;
name|long
name|commitPeriod
init|=
name|writerConfig
operator|.
name|getCommitWithinMs
argument_list|()
decl_stmt|;
if|if
condition|(
name|commitPeriod
operator|<
literal|0
condition|)
block|{
name|delegateWriter
operator|=
operator|new
name|AutoCommitWriter
argument_list|(
name|dir
argument_list|,
name|writerConfig
operator|.
name|getLuceneConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|commitPeriod
operator|==
literal|0
condition|)
block|{
name|delegateWriter
operator|=
operator|new
name|AutoCommitWriter
argument_list|(
name|dir
argument_list|,
name|writerConfig
operator|.
name|getLuceneConfig
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|AutoCommitWriter
name|autoCommitWriter
init|=
operator|new
name|AutoCommitWriter
argument_list|(
name|dir
argument_list|,
name|writerConfig
operator|.
name|getLuceneConfig
argument_list|()
argument_list|)
decl_stmt|;
name|delegateWriter
operator|=
name|autoCommitWriter
expr_stmt|;
name|autoCommitExecutor
operator|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|1
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
name|index
operator|+
literal|" Commit-%d"
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|autoCommitExecutor
operator|.
name|scheduleAtFixedRate
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|autoCommitWriter
operator|.
name|hasUncommittedChanges
argument_list|()
condition|)
block|{
name|autoCommitWriter
operator|.
name|manualFlush
argument_list|()
expr_stmt|;
name|autoCommitWriter
operator|.
name|commit
argument_list|()
expr_stmt|;
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
name|error
argument_list|(
literal|"Error committing "
operator|+
name|index
operator|+
literal|" Lucene index"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OutOfMemoryError
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error committing "
operator|+
name|index
operator|+
literal|" Lucene index"
argument_list|,
name|e
argument_list|)
expr_stmt|;
try|try
block|{
name|autoCommitWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"SEVERE: Error closing "
operator|+
name|index
operator|+
literal|" Lucene index  after OOM; index may be corrupted."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|,
name|commitPeriod
argument_list|,
name|commitPeriod
argument_list|,
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
name|writer
operator|=
operator|new
name|TrackingIndexWriter
argument_list|(
name|delegateWriter
argument_list|)
expr_stmt|;
name|searcherManager
operator|=
operator|new
name|WrappableSearcherManager
argument_list|(
name|writer
operator|.
name|getIndexWriter
argument_list|()
argument_list|,
literal|true
argument_list|,
name|searcherFactory
argument_list|)
expr_stmt|;
name|notDoneNrtFutures
operator|=
name|Sets
operator|.
name|newConcurrentHashSet
argument_list|()
expr_stmt|;
name|writerThread
operator|=
name|MoreExecutors
operator|.
name|listeningDecorator
argument_list|(
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
name|index
operator|+
literal|" Write-%d"
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|reopenThread
operator|=
operator|new
name|ControlledRealTimeReopenThread
argument_list|<>
argument_list|(
name|writer
argument_list|,
name|searcherManager
argument_list|,
literal|0.500
comment|/* maximum stale age (seconds) */
argument_list|,
literal|0.010
comment|/* minimum stale age (seconds) */
argument_list|)
expr_stmt|;
name|reopenThread
operator|.
name|setName
argument_list|(
name|index
operator|+
literal|" NRT"
argument_list|)
expr_stmt|;
name|reopenThread
operator|.
name|setPriority
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getPriority
argument_list|()
operator|+
literal|2
argument_list|,
name|Thread
operator|.
name|MAX_PRIORITY
argument_list|)
argument_list|)
expr_stmt|;
name|reopenThread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// This must be added after the reopen thread is created. The reopen thread
comment|// adds its own listener which copies its internally last-refreshed
comment|// generation to the searching generation. removeIfDone() depends on the
comment|// searching generation being up to date when calling
comment|// reopenThread.waitForGeneration(gen, 0), therefore the reopen thread's
comment|// internal listener needs to be called first.
comment|// TODO(dborowitz): This may have been fixed by
comment|// http://issues.apache.org/jira/browse/LUCENE-5461
name|searcherManager
operator|.
name|addListener
argument_list|(
operator|new
name|RefreshListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|beforeRefresh
parameter_list|()
throws|throws
name|IOException
block|{       }
annotation|@
name|Override
specifier|public
name|void
name|afterRefresh
parameter_list|(
name|boolean
name|didRefresh
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|NrtFuture
name|f
range|:
name|notDoneNrtFutures
control|)
block|{
name|f
operator|.
name|removeIfDone
argument_list|()
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|reopenThread
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|markReady (boolean ready)
specifier|public
name|void
name|markReady
parameter_list|(
name|boolean
name|ready
parameter_list|)
throws|throws
name|IOException
block|{
name|IndexUtils
operator|.
name|setReady
argument_list|(
name|sitePaths
argument_list|,
name|name
argument_list|,
name|schema
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ready
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|autoCommitExecutor
operator|!=
literal|null
condition|)
block|{
name|autoCommitExecutor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|writerThread
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|writerThread
operator|.
name|awaitTermination
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"shutting down "
operator|+
name|name
operator|+
literal|" index with pending Lucene writes"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"interrupted waiting for pending Lucene writes of "
operator|+
name|name
operator|+
literal|" index"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|reopenThread
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// Closing the reopen thread sets its generation to Long.MAX_VALUE, but we
comment|// still need to refresh the searcher manager to let pending NrtFutures
comment|// know.
comment|//
comment|// Any futures created after this method (which may happen due to undefined
comment|// shutdown ordering behavior) will finish immediately, even though they may
comment|// not have flushed.
try|try
block|{
name|searcherManager
operator|.
name|maybeRefreshBlocking
argument_list|()
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
literal|"error finishing pending Lucene writes"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|writer
operator|.
name|getIndexWriter
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AlreadyClosedException
name|e
parameter_list|)
block|{
comment|// Ignore.
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
literal|"error closing Lucene writer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|dir
operator|.
name|close
argument_list|()
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
literal|"error closing Lucene directory"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|insert (final Document doc)
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|insert
parameter_list|(
specifier|final
name|Document
name|doc
parameter_list|)
block|{
return|return
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|call
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
return|return
name|writer
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|replace (final Term term, final Document doc)
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|replace
parameter_list|(
specifier|final
name|Term
name|term
parameter_list|,
specifier|final
name|Document
name|doc
parameter_list|)
block|{
return|return
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|call
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
return|return
name|writer
operator|.
name|updateDocument
argument_list|(
name|term
argument_list|,
name|doc
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|delete (final Term term)
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|delete
parameter_list|(
specifier|final
name|Term
name|term
parameter_list|)
block|{
return|return
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|call
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
return|return
name|writer
operator|.
name|deleteDocuments
argument_list|(
name|term
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|submit (Callable<Long> task)
specifier|private
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|Long
argument_list|>
name|task
parameter_list|)
block|{
name|ListenableFuture
argument_list|<
name|Long
argument_list|>
name|future
init|=
name|Futures
operator|.
name|nonCancellationPropagating
argument_list|(
name|writerThread
operator|.
name|submit
argument_list|(
name|task
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|Futures
operator|.
name|transformAsync
argument_list|(
name|future
argument_list|,
operator|new
name|AsyncFunction
argument_list|<
name|Long
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
name|Long
name|gen
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// Tell the reopen thread a future is waiting on this
comment|// generation so it uses the min stale time when refreshing.
name|reopenThread
operator|.
name|waitForGeneration
argument_list|(
name|gen
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
operator|new
name|NrtFuture
argument_list|(
name|gen
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|deleteAll ()
specifier|public
name|void
name|deleteAll
parameter_list|()
throws|throws
name|IOException
block|{
name|writer
operator|.
name|deleteAll
argument_list|()
expr_stmt|;
block|}
DECL|method|getWriter ()
specifier|public
name|TrackingIndexWriter
name|getWriter
parameter_list|()
block|{
return|return
name|writer
return|;
block|}
DECL|method|acquire ()
name|IndexSearcher
name|acquire
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|searcherManager
operator|.
name|acquire
argument_list|()
return|;
block|}
DECL|method|release (IndexSearcher searcher)
name|void
name|release
parameter_list|(
name|IndexSearcher
name|searcher
parameter_list|)
throws|throws
name|IOException
block|{
name|searcherManager
operator|.
name|release
argument_list|(
name|searcher
argument_list|)
expr_stmt|;
block|}
DECL|method|toDocument (V obj, FillArgs fillArgs)
name|Document
name|toDocument
parameter_list|(
name|V
name|obj
parameter_list|,
name|FillArgs
name|fillArgs
parameter_list|)
block|{
name|Document
name|result
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
for|for
control|(
name|Values
argument_list|<
name|V
argument_list|>
name|vs
range|:
name|schema
operator|.
name|buildFields
argument_list|(
name|obj
argument_list|,
name|fillArgs
argument_list|)
control|)
block|{
if|if
condition|(
name|vs
operator|.
name|getValues
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|add
argument_list|(
name|result
argument_list|,
name|vs
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|add (Document doc, Values<V> values)
name|void
name|add
parameter_list|(
name|Document
name|doc
parameter_list|,
name|Values
argument_list|<
name|V
argument_list|>
name|values
parameter_list|)
block|{
name|String
name|name
init|=
name|values
operator|.
name|getField
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|FieldType
argument_list|<
name|?
argument_list|>
name|type
init|=
name|values
operator|.
name|getField
argument_list|()
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Store
name|store
init|=
name|store
argument_list|(
name|values
operator|.
name|getField
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|INTEGER
operator|||
name|type
operator|==
name|FieldType
operator|.
name|INTEGER_RANGE
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|IntField
argument_list|(
name|name
argument_list|,
operator|(
name|Integer
operator|)
name|value
argument_list|,
name|store
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|LONG
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|LongField
argument_list|(
name|name
argument_list|,
operator|(
name|Long
operator|)
name|value
argument_list|,
name|store
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|TIMESTAMP
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|LongField
argument_list|(
name|name
argument_list|,
operator|(
operator|(
name|Timestamp
operator|)
name|value
operator|)
operator|.
name|getTime
argument_list|()
argument_list|,
name|store
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|EXACT
operator|||
name|type
operator|==
name|FieldType
operator|.
name|PREFIX
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|StringField
argument_list|(
name|name
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|,
name|store
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|FULL_TEXT
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|TextField
argument_list|(
name|name
argument_list|,
operator|(
name|String
operator|)
name|value
argument_list|,
name|store
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|FieldType
operator|.
name|STORED_ONLY
condition|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|values
operator|.
name|getValues
argument_list|()
control|)
block|{
name|doc
operator|.
name|add
argument_list|(
operator|new
name|StoredField
argument_list|(
name|name
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
name|FieldType
operator|.
name|badFieldType
argument_list|(
name|type
argument_list|)
throw|;
block|}
block|}
DECL|method|store (FieldDef<?, ?> f)
specifier|private
specifier|static
name|Field
operator|.
name|Store
name|store
parameter_list|(
name|FieldDef
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|f
parameter_list|)
block|{
return|return
name|f
operator|.
name|isStored
argument_list|()
condition|?
name|Field
operator|.
name|Store
operator|.
name|YES
else|:
name|Field
operator|.
name|Store
operator|.
name|NO
return|;
block|}
DECL|class|NrtFuture
specifier|private
specifier|final
class|class
name|NrtFuture
extends|extends
name|AbstractFuture
argument_list|<
name|Void
argument_list|>
block|{
DECL|field|gen
specifier|private
specifier|final
name|long
name|gen
decl_stmt|;
DECL|method|NrtFuture (long gen)
name|NrtFuture
parameter_list|(
name|long
name|gen
parameter_list|)
block|{
name|this
operator|.
name|gen
operator|=
name|gen
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|Void
name|get
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
name|reopenThread
operator|.
name|waitForGeneration
argument_list|(
name|gen
argument_list|)
expr_stmt|;
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (long timeout, TimeUnit unit)
specifier|public
name|Void
name|get
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|TimeoutException
throws|,
name|ExecutionException
block|{
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|reopenThread
operator|.
name|waitForGeneration
argument_list|(
name|gen
argument_list|,
operator|(
name|int
operator|)
name|unit
operator|.
name|toMillis
argument_list|(
name|timeout
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|TimeoutException
argument_list|()
throw|;
block|}
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isDone ()
specifier|public
name|boolean
name|isDone
parameter_list|()
block|{
if|if
condition|(
name|super
operator|.
name|isDone
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|isGenAvailableNowForCurrentSearcher
argument_list|()
condition|)
block|{
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|reopenThread
operator|.
name|isAlive
argument_list|()
condition|)
block|{
name|setException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"NRT thread is dead"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|addListener (Runnable listener, Executor executor)
specifier|public
name|void
name|addListener
parameter_list|(
name|Runnable
name|listener
parameter_list|,
name|Executor
name|executor
parameter_list|)
block|{
if|if
condition|(
name|isGenAvailableNowForCurrentSearcher
argument_list|()
operator|&&
operator|!
name|isCancelled
argument_list|()
condition|)
block|{
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isDone
argument_list|()
condition|)
block|{
name|notDoneNrtFutures
operator|.
name|add
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|addListener
argument_list|(
name|listener
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|cancel (boolean mayInterruptIfRunning)
specifier|public
name|boolean
name|cancel
parameter_list|(
name|boolean
name|mayInterruptIfRunning
parameter_list|)
block|{
name|boolean
name|result
init|=
name|super
operator|.
name|cancel
argument_list|(
name|mayInterruptIfRunning
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
condition|)
block|{
name|notDoneNrtFutures
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|removeIfDone ()
name|void
name|removeIfDone
parameter_list|()
block|{
if|if
condition|(
name|isGenAvailableNowForCurrentSearcher
argument_list|()
condition|)
block|{
name|notDoneNrtFutures
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isCancelled
argument_list|()
condition|)
block|{
name|set
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isGenAvailableNowForCurrentSearcher ()
specifier|private
name|boolean
name|isGenAvailableNowForCurrentSearcher
parameter_list|()
block|{
try|try
block|{
return|return
name|reopenThread
operator|.
name|waitForGeneration
argument_list|(
name|gen
argument_list|,
literal|0
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Interrupted waiting for searcher generation"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getSchema ()
specifier|public
name|Schema
argument_list|<
name|V
argument_list|>
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
block|}
end_class

end_unit

