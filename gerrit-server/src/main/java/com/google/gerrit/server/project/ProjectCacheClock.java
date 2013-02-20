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
name|checkFrequency
argument_list|(
name|serverConfig
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
name|checkFrequencyMillis
operator|==
name|Long
operator|.
name|MAX_VALUE
condition|)
block|{
comment|// Start with generation 1 (to avoid magic 0 below).
comment|// Do not begin background thread, disabling the clock.
name|generation
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
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
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
literal|"ProjectCacheClock-%d"
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|setPriority
argument_list|(
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|)
operator|.
name|build
argument_list|()
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
DECL|method|checkFrequency (Config serverConfig)
specifier|private
specifier|static
name|long
name|checkFrequency
parameter_list|(
name|Config
name|serverConfig
parameter_list|)
block|{
name|String
name|freq
init|=
name|serverConfig
operator|.
name|getString
argument_list|(
literal|"cache"
argument_list|,
literal|"projects"
argument_list|,
literal|"checkFrequency"
argument_list|)
decl_stmt|;
if|if
condition|(
name|freq
operator|!=
literal|null
operator|&&
operator|(
literal|"disabled"
operator|.
name|equalsIgnoreCase
argument_list|(
name|freq
argument_list|)
operator|||
literal|"off"
operator|.
name|equalsIgnoreCase
argument_list|(
name|freq
argument_list|)
operator|)
condition|)
block|{
return|return
name|Long
operator|.
name|MAX_VALUE
return|;
block|}
return|return
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
return|;
block|}
block|}
end_class

end_unit

