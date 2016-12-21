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
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
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
name|GerritServerConfig
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
name|concurrent
operator|.
name|ExecutorService
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
name|ThreadPoolExecutor
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
annotation|@
name|Singleton
DECL|class|ProjectCacheWarmer
specifier|public
class|class
name|ProjectCacheWarmer
implements|implements
name|LifecycleListener
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
name|ProjectCacheWarmer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|Config
name|config
decl_stmt|;
DECL|field|cache
specifier|private
specifier|final
name|ProjectCache
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectCacheWarmer (@erritServerConfig Config config, ProjectCache cache)
name|ProjectCacheWarmer
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|ProjectCache
name|cache
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
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
name|int
name|cpus
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getBoolean
argument_list|(
literal|"cache"
argument_list|,
literal|"projects"
argument_list|,
literal|"loadOnStartup"
argument_list|,
literal|false
argument_list|)
condition|)
block|{
specifier|final
name|ThreadPoolExecutor
name|pool
init|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
name|config
operator|.
name|getInt
argument_list|(
literal|"cache"
argument_list|,
literal|"projects"
argument_list|,
literal|"loadThreads"
argument_list|,
name|cpus
argument_list|)
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
literal|"ProjectCacheLoader-%d"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
name|ExecutorService
name|scheduler
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Loading project cache"
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|execute
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
for|for
control|(
specifier|final
name|Project
operator|.
name|NameKey
name|name
range|:
name|cache
operator|.
name|all
argument_list|()
control|)
block|{
name|pool
operator|.
name|execute
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
name|cache
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|pool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|pool
operator|.
name|awaitTermination
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Finished loading project cache"
argument_list|)
expr_stmt|;
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
literal|"Interrupted while waiting for project cache to load"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{   }
block|}
end_class

end_unit

