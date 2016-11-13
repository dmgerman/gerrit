begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.index.group
package|package
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
name|group
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
name|git
operator|.
name|QueueProvider
operator|.
name|QueueType
operator|.
name|BATCH
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
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|AccountGroup
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
name|account
operator|.
name|GroupCache
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
name|IndexExecutor
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
name|SiteIndexer
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
name|Singleton
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
name|atomic
operator|.
name|AtomicBoolean
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
name|AtomicInteger
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
annotation|@
name|Singleton
DECL|class|AllGroupsIndexer
specifier|public
class|class
name|AllGroupsIndexer
extends|extends
name|SiteIndexer
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|,
name|AccountGroup
argument_list|,
name|GroupIndex
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
name|AllGroupsIndexer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|schemaFactory
specifier|private
specifier|final
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ListeningExecutorService
name|executor
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllGroupsIndexer ( SchemaFactory<ReviewDb> schemaFactory, @IndexExecutor(BATCH) ListeningExecutorService executor, GroupCache groupCache)
name|AllGroupsIndexer
parameter_list|(
name|SchemaFactory
argument_list|<
name|ReviewDb
argument_list|>
name|schemaFactory
parameter_list|,
annotation|@
name|IndexExecutor
argument_list|(
name|BATCH
argument_list|)
name|ListeningExecutorService
name|executor
parameter_list|,
name|GroupCache
name|groupCache
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|indexAll (GroupIndex index)
specifier|public
name|SiteIndexer
operator|.
name|Result
name|indexAll
parameter_list|(
name|GroupIndex
name|index
parameter_list|)
block|{
name|ProgressMonitor
name|progress
init|=
operator|new
name|TextProgressMonitor
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|progressOut
argument_list|)
argument_list|)
decl_stmt|;
name|progress
operator|.
name|start
argument_list|(
literal|2
argument_list|)
expr_stmt|;
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
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uuids
decl_stmt|;
try|try
block|{
name|uuids
operator|=
name|collectGroups
argument_list|(
name|progress
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error collecting groups"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|SiteIndexer
operator|.
name|Result
argument_list|(
name|sw
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
return|return
name|reindexGroups
argument_list|(
name|index
argument_list|,
name|uuids
argument_list|,
name|progress
argument_list|)
return|;
block|}
DECL|method|reindexGroups ( GroupIndex index, List<AccountGroup.UUID> uuids, ProgressMonitor progress)
specifier|private
name|SiteIndexer
operator|.
name|Result
name|reindexGroups
parameter_list|(
name|GroupIndex
name|index
parameter_list|,
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uuids
parameter_list|,
name|ProgressMonitor
name|progress
parameter_list|)
block|{
name|progress
operator|.
name|beginTask
argument_list|(
literal|"Reindexing groups"
argument_list|,
name|uuids
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ListenableFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|futures
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|uuids
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|AtomicBoolean
name|ok
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|done
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
specifier|final
name|AtomicInteger
name|failed
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
name|Stopwatch
name|sw
init|=
name|Stopwatch
operator|.
name|createStarted
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|AccountGroup
operator|.
name|UUID
name|uuid
range|:
name|uuids
control|)
block|{
specifier|final
name|String
name|desc
init|=
literal|"group "
operator|+
name|uuid
decl_stmt|;
name|ListenableFuture
argument_list|<
name|?
argument_list|>
name|future
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|AccountGroup
name|oldGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldGroup
operator|!=
literal|null
condition|)
block|{
name|groupCache
operator|.
name|evict
argument_list|(
name|oldGroup
argument_list|)
expr_stmt|;
block|}
name|index
operator|.
name|replace
argument_list|(
name|groupCache
operator|.
name|get
argument_list|(
name|uuid
argument_list|)
argument_list|)
expr_stmt|;
name|verboseWriter
operator|.
name|println
argument_list|(
literal|"Reindexed "
operator|+
name|desc
argument_list|)
expr_stmt|;
name|done
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|failed
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|addErrorListener
argument_list|(
name|future
argument_list|,
name|desc
argument_list|,
name|progress
argument_list|,
name|ok
argument_list|)
expr_stmt|;
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
name|Futures
operator|.
name|successfulAsList
argument_list|(
name|futures
argument_list|)
operator|.
name|get
argument_list|()
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
name|log
operator|.
name|error
argument_list|(
literal|"Error waiting on group futures"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
operator|new
name|SiteIndexer
operator|.
name|Result
argument_list|(
name|sw
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|0
argument_list|)
return|;
block|}
name|progress
operator|.
name|endTask
argument_list|()
expr_stmt|;
return|return
operator|new
name|SiteIndexer
operator|.
name|Result
argument_list|(
name|sw
argument_list|,
name|ok
operator|.
name|get
argument_list|()
argument_list|,
name|done
operator|.
name|get
argument_list|()
argument_list|,
name|failed
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
DECL|method|collectGroups (ProgressMonitor progress)
specifier|private
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|collectGroups
parameter_list|(
name|ProgressMonitor
name|progress
parameter_list|)
throws|throws
name|OrmException
block|{
name|progress
operator|.
name|beginTask
argument_list|(
literal|"Collecting groups"
argument_list|,
name|ProgressMonitor
operator|.
name|UNKNOWN
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|uuids
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|AccountGroup
name|group
range|:
name|db
operator|.
name|accountGroups
argument_list|()
operator|.
name|all
argument_list|()
control|)
block|{
name|uuids
operator|.
name|add
argument_list|(
name|group
operator|.
name|getGroupUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|progress
operator|.
name|endTask
argument_list|()
expr_stmt|;
return|return
name|uuids
return|;
block|}
block|}
end_class

end_unit

