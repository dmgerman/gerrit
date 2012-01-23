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
name|gerrit
operator|.
name|server
operator|.
name|config
operator|.
name|ConfigUtil
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
name|ScheduledExecutorService
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
name|ThreadFactory
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
name|AtomicInteger
import|;
end_import

begin_comment
comment|/** Ticks periodically to force refresh events for {@link ProjectCacheImpl}. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ProjectCacheClock
specifier|public
class|class
name|ProjectCacheClock
block|{
DECL|field|generation
specifier|private
specifier|volatile
name|long
name|generation
decl_stmt|;
annotation|@
name|Inject
DECL|method|ProjectCacheClock (@erritServerConfig Config serverConfig)
specifier|public
name|ProjectCacheClock
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|serverConfig
parameter_list|)
block|{
name|this
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|convert
argument_list|(
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|serverConfig
argument_list|,
literal|"cache"
argument_list|,
literal|"projects"
argument_list|,
literal|"checkFrequency"
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|ProjectCacheClock (long checkFrequencyMillis)
specifier|public
name|ProjectCacheClock
parameter_list|(
name|long
name|checkFrequencyMillis
parameter_list|)
block|{
if|if
condition|(
literal|10
operator|<
name|checkFrequencyMillis
condition|)
block|{
comment|// Start with generation 1 (to avoid magic 0 below).
name|generation
operator|=
literal|1
expr_stmt|;
name|ThreadFactory
name|factory
init|=
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|private
specifier|final
name|AtomicInteger
name|id
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
name|Thread
name|thread
init|=
name|Executors
operator|.
name|defaultThreadFactory
argument_list|()
operator|.
name|newThread
argument_list|(
name|runnable
argument_list|)
decl_stmt|;
name|thread
operator|.
name|setName
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"ProjectCacheClock-%d"
argument_list|,
name|id
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|thread
operator|.
name|setPriority
argument_list|(
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|)
expr_stmt|;
return|return
name|thread
return|;
block|}
block|}
decl_stmt|;
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|,
name|factory
argument_list|)
decl_stmt|;
name|executor
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
comment|// This is not exactly thread-safe, but is OK for our use.
comment|// The only thread that writes the volatile is this task.
name|generation
operator|=
name|generation
operator|+
literal|1
expr_stmt|;
block|}
block|}
argument_list|,
name|checkFrequencyMillis
argument_list|,
name|checkFrequencyMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Magic generation 0 triggers ProjectState to always
comment|// check on each needsRefresh() request we make to it.
name|generation
operator|=
literal|0
expr_stmt|;
block|}
block|}
DECL|method|read ()
name|long
name|read
parameter_list|()
block|{
return|return
name|generation
return|;
block|}
block|}
end_class

end_unit

