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
DECL|package|com.google.gerrit.server.index
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
name|INTERACTIVE
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
name|query
operator|.
name|change
operator|.
name|ChangeQueryRewriter
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
name|Provides
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

begin_comment
comment|/**  * Module for non-indexer-specific secondary index setup.  *<p>  * This module should not be used directly except by specific secondary indexer  * implementations (e.g. Lucene).  */
end_comment

begin_class
DECL|class|IndexModule
specifier|public
class|class
name|IndexModule
extends|extends
name|LifecycleModule
block|{
DECL|enum|IndexType
specifier|public
enum|enum
name|IndexType
block|{
DECL|enumConstant|LUCENE
DECL|enumConstant|SOLR
name|LUCENE
block|,
name|SOLR
block|}
comment|/** Type of secondary index. */
DECL|method|getIndexType (Injector injector)
specifier|public
specifier|static
name|IndexType
name|getIndexType
parameter_list|(
name|Injector
name|injector
parameter_list|)
block|{
name|Config
name|cfg
init|=
name|injector
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
return|return
name|cfg
operator|.
name|getEnum
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"type"
argument_list|,
name|IndexType
operator|.
name|LUCENE
argument_list|)
return|;
block|}
DECL|field|threads
specifier|private
specifier|final
name|int
name|threads
decl_stmt|;
DECL|field|interactiveExecutor
specifier|private
specifier|final
name|ListeningExecutorService
name|interactiveExecutor
decl_stmt|;
DECL|field|batchExecutor
specifier|private
specifier|final
name|ListeningExecutorService
name|batchExecutor
decl_stmt|;
DECL|method|IndexModule (int threads)
specifier|public
name|IndexModule
parameter_list|(
name|int
name|threads
parameter_list|)
block|{
name|this
operator|.
name|threads
operator|=
name|threads
expr_stmt|;
name|this
operator|.
name|interactiveExecutor
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|batchExecutor
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|IndexModule (ListeningExecutorService interactiveExecutor, ListeningExecutorService batchExecutor)
specifier|public
name|IndexModule
parameter_list|(
name|ListeningExecutorService
name|interactiveExecutor
parameter_list|,
name|ListeningExecutorService
name|batchExecutor
parameter_list|)
block|{
name|this
operator|.
name|threads
operator|=
operator|-
literal|1
expr_stmt|;
name|this
operator|.
name|interactiveExecutor
operator|=
name|interactiveExecutor
expr_stmt|;
name|this
operator|.
name|batchExecutor
operator|=
name|batchExecutor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|ChangeQueryRewriter
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|IndexRewriteImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|IndexCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|IndexCollection
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
argument_list|(
name|ChangeIndexer
operator|.
name|Factory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
DECL|method|getChangeIndexer ( @ndexExecutorINTERACTIVE) ListeningExecutorService executor, ChangeIndexer.Factory factory, IndexCollection indexes)
name|ChangeIndexer
name|getChangeIndexer
parameter_list|(
annotation|@
name|IndexExecutor
argument_list|(
name|INTERACTIVE
argument_list|)
name|ListeningExecutorService
name|executor
parameter_list|,
name|ChangeIndexer
operator|.
name|Factory
name|factory
parameter_list|,
name|IndexCollection
name|indexes
parameter_list|)
block|{
comment|// Bind default indexer to interactive executor; callers who need a
comment|// different executor can use the factory directly.
return|return
name|factory
operator|.
name|create
argument_list|(
name|executor
argument_list|,
name|indexes
argument_list|)
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|IndexExecutor
argument_list|(
name|INTERACTIVE
argument_list|)
DECL|method|getInteractiveIndexExecutor ( @erritServerConfig Config config, WorkQueue workQueue)
name|ListeningExecutorService
name|getInteractiveIndexExecutor
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|WorkQueue
name|workQueue
parameter_list|)
block|{
if|if
condition|(
name|interactiveExecutor
operator|!=
literal|null
condition|)
block|{
return|return
name|interactiveExecutor
return|;
block|}
name|int
name|threads
init|=
name|this
operator|.
name|threads
decl_stmt|;
if|if
condition|(
name|threads
operator|<=
literal|0
condition|)
block|{
name|threads
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"threads"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threads
operator|<=
literal|0
condition|)
block|{
name|threads
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"changeMerge"
argument_list|,
literal|null
argument_list|,
literal|"interactiveThreadPoolSize"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threads
operator|<=
literal|0
condition|)
block|{
return|return
name|MoreExecutors
operator|.
name|newDirectExecutorService
argument_list|()
return|;
block|}
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
literal|"Index-Interactive"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|IndexExecutor
argument_list|(
name|BATCH
argument_list|)
DECL|method|getBatchIndexExecutor ( @erritServerConfig Config config, WorkQueue workQueue)
name|ListeningExecutorService
name|getBatchIndexExecutor
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|WorkQueue
name|workQueue
parameter_list|)
block|{
if|if
condition|(
name|batchExecutor
operator|!=
literal|null
condition|)
block|{
return|return
name|batchExecutor
return|;
block|}
name|int
name|threads
init|=
name|config
operator|.
name|getInt
argument_list|(
literal|"index"
argument_list|,
literal|null
argument_list|,
literal|"batchThreads"
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|threads
operator|<=
literal|0
condition|)
block|{
name|threads
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"changeMerge"
argument_list|,
literal|null
argument_list|,
literal|"threadPoolSize"
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|threads
operator|<=
literal|0
condition|)
block|{
name|threads
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
expr_stmt|;
block|}
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
literal|"Index-Batch"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

