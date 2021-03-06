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
DECL|package|com.google.gerrit.server.index.project
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
name|project
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
name|gerrit
operator|.
name|index
operator|.
name|project
operator|.
name|ProjectData
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
name|project
operator|.
name|ProjectIndex
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

begin_comment
comment|/**  * Implementation that can index all projects on a host. Used by Gerrit's initialization and upgrade  * programs as well as by REST API endpoints that offer this functionality.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|AllProjectsIndexer
specifier|public
class|class
name|AllProjectsIndexer
extends|extends
name|SiteIndexer
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|,
name|ProjectData
argument_list|,
name|ProjectIndex
argument_list|>
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
DECL|field|executor
specifier|private
specifier|final
name|ListeningExecutorService
name|executor
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|AllProjectsIndexer ( @ndexExecutorBATCH) ListeningExecutorService executor, ProjectCache projectCache)
name|AllProjectsIndexer
parameter_list|(
annotation|@
name|IndexExecutor
argument_list|(
name|BATCH
argument_list|)
name|ListeningExecutorService
name|executor
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|indexAll (final ProjectIndex index)
specifier|public
name|SiteIndexer
operator|.
name|Result
name|indexAll
parameter_list|(
specifier|final
name|ProjectIndex
name|index
parameter_list|)
block|{
name|ProgressMonitor
name|progress
init|=
operator|new
name|TextProgressMonitor
argument_list|(
name|newPrintWriter
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
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|names
init|=
name|collectProjects
argument_list|(
name|progress
argument_list|)
decl_stmt|;
return|return
name|reindexProjects
argument_list|(
name|index
argument_list|,
name|names
argument_list|,
name|progress
argument_list|)
return|;
block|}
DECL|method|reindexProjects ( ProjectIndex index, List<Project.NameKey> names, ProgressMonitor progress)
specifier|private
name|SiteIndexer
operator|.
name|Result
name|reindexProjects
parameter_list|(
name|ProjectIndex
name|index
parameter_list|,
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|names
parameter_list|,
name|ProgressMonitor
name|progress
parameter_list|)
block|{
name|progress
operator|.
name|beginTask
argument_list|(
literal|"Reindexing projects"
argument_list|,
name|names
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
name|names
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
name|AtomicInteger
name|done
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
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
name|Project
operator|.
name|NameKey
name|name
range|:
name|names
control|)
block|{
name|String
name|desc
init|=
literal|"project "
operator|+
name|name
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
parameter_list|()
lambda|->
block|{
try|try
block|{
name|projectCache
operator|.
name|evict
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|index
operator|.
name|replace
argument_list|(
name|projectCache
operator|.
name|get
argument_list|(
name|name
argument_list|)
operator|.
name|toProjectData
argument_list|()
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
literal|"Error waiting on project futures"
argument_list|)
expr_stmt|;
return|return
name|SiteIndexer
operator|.
name|Result
operator|.
name|create
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
name|SiteIndexer
operator|.
name|Result
operator|.
name|create
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
DECL|method|collectProjects (ProgressMonitor progress)
specifier|private
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|collectProjects
parameter_list|(
name|ProgressMonitor
name|progress
parameter_list|)
block|{
name|progress
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
name|List
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Project
operator|.
name|NameKey
name|nameKey
range|:
name|projectCache
operator|.
name|all
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|nameKey
argument_list|)
expr_stmt|;
block|}
name|progress
operator|.
name|endTask
argument_list|()
expr_stmt|;
return|return
name|names
return|;
block|}
block|}
end_class

end_unit

