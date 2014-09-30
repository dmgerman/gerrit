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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
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
name|util
operator|.
name|TimeUtil
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
name|concurrent
operator|.
name|Future
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
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PluginCleanerTask
class|class
name|PluginCleanerTask
implements|implements
name|Runnable
block|{
DECL|field|workQueue
specifier|private
specifier|final
name|WorkQueue
name|workQueue
decl_stmt|;
DECL|field|loader
specifier|private
specifier|final
name|PluginLoader
name|loader
decl_stmt|;
DECL|field|pending
specifier|private
specifier|volatile
name|int
name|pending
decl_stmt|;
DECL|field|self
specifier|private
name|Future
argument_list|<
name|?
argument_list|>
name|self
decl_stmt|;
DECL|field|attempts
specifier|private
name|int
name|attempts
decl_stmt|;
DECL|field|start
specifier|private
name|long
name|start
decl_stmt|;
annotation|@
name|Inject
DECL|method|PluginCleanerTask (WorkQueue workQueue, PluginLoader loader)
name|PluginCleanerTask
parameter_list|(
name|WorkQueue
name|workQueue
parameter_list|,
name|PluginLoader
name|loader
parameter_list|)
block|{
name|this
operator|.
name|workQueue
operator|=
name|workQueue
expr_stmt|;
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|int
name|t
init|=
literal|0
init|;
name|t
operator|<
literal|2
operator|*
operator|(
name|attempts
operator|+
literal|1
operator|)
condition|;
name|t
operator|++
control|)
block|{
name|System
operator|.
name|gc
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|50
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{     }
name|int
name|left
init|=
name|loader
operator|.
name|processPendingCleanups
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|pending
operator|=
name|left
expr_stmt|;
name|self
operator|=
literal|null
expr_stmt|;
if|if
condition|(
literal|0
operator|<
name|left
condition|)
block|{
name|long
name|waiting
init|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
operator|-
name|start
decl_stmt|;
name|PluginLoader
operator|.
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%d plugins still waiting to be reclaimed after %d minutes"
argument_list|,
name|pending
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMinutes
argument_list|(
name|waiting
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|attempts
operator|=
name|Math
operator|.
name|min
argument_list|(
name|attempts
operator|+
literal|1
argument_list|,
literal|15
argument_list|)
expr_stmt|;
name|ensureScheduled
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|attempts
operator|=
literal|0
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|int
name|p
init|=
name|pending
decl_stmt|;
if|if
condition|(
literal|0
operator|<
name|p
condition|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"Plugin Cleaner (waiting for %d plugins)"
argument_list|,
name|p
argument_list|)
return|;
block|}
return|return
literal|"Plugin Cleaner"
return|;
block|}
DECL|method|clean (int expect)
specifier|synchronized
name|void
name|clean
parameter_list|(
name|int
name|expect
parameter_list|)
block|{
if|if
condition|(
name|self
operator|==
literal|null
operator|&&
name|pending
operator|==
literal|0
condition|)
block|{
name|start
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
expr_stmt|;
block|}
name|pending
operator|=
name|expect
expr_stmt|;
name|ensureScheduled
argument_list|()
expr_stmt|;
block|}
DECL|method|ensureScheduled ()
specifier|private
name|void
name|ensureScheduled
parameter_list|()
block|{
if|if
condition|(
name|self
operator|==
literal|null
operator|&&
literal|0
operator|<
name|pending
condition|)
block|{
if|if
condition|(
name|attempts
operator|==
literal|1
condition|)
block|{
name|self
operator|=
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|self
operator|=
name|workQueue
operator|.
name|getDefaultQueue
argument_list|()
operator|.
name|schedule
argument_list|(
name|this
argument_list|,
name|attempts
operator|+
literal|1
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

