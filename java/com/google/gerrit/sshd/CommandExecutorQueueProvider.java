begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
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
name|config
operator|.
name|ThreadSettingsConfig
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
name|QueueProvider
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
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
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

begin_class
annotation|@
name|Singleton
DECL|class|CommandExecutorQueueProvider
specifier|public
class|class
name|CommandExecutorQueueProvider
implements|implements
name|QueueProvider
block|{
DECL|field|poolSize
specifier|private
name|int
name|poolSize
decl_stmt|;
DECL|field|batchThreads
specifier|private
specifier|final
name|int
name|batchThreads
decl_stmt|;
DECL|field|interactiveExecutor
specifier|private
specifier|final
name|ScheduledThreadPoolExecutor
name|interactiveExecutor
decl_stmt|;
DECL|field|batchExecutor
specifier|private
specifier|final
name|ScheduledThreadPoolExecutor
name|batchExecutor
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommandExecutorQueueProvider ( @erritServerConfig Config config, ThreadSettingsConfig threadsSettingsConfig, WorkQueue queues)
specifier|public
name|CommandExecutorQueueProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|ThreadSettingsConfig
name|threadsSettingsConfig
parameter_list|,
name|WorkQueue
name|queues
parameter_list|)
block|{
name|poolSize
operator|=
name|threadsSettingsConfig
operator|.
name|getSshdThreads
argument_list|()
expr_stmt|;
name|batchThreads
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"sshd"
argument_list|,
literal|"batchThreads"
argument_list|,
name|threadsSettingsConfig
operator|.
name|getSshdBatchTreads
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|batchThreads
operator|>
name|poolSize
condition|)
block|{
name|poolSize
operator|+=
name|batchThreads
expr_stmt|;
block|}
name|int
name|interactiveThreads
init|=
name|Math
operator|.
name|max
argument_list|(
literal|1
argument_list|,
name|poolSize
operator|-
name|batchThreads
argument_list|)
decl_stmt|;
name|interactiveExecutor
operator|=
name|queues
operator|.
name|createQueue
argument_list|(
name|interactiveThreads
argument_list|,
literal|"SSH-Interactive-Worker"
argument_list|,
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|batchThreads
operator|!=
literal|0
condition|)
block|{
name|batchExecutor
operator|=
name|queues
operator|.
name|createQueue
argument_list|(
name|batchThreads
argument_list|,
literal|"SSH-Batch-Worker"
argument_list|,
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|batchExecutor
operator|=
name|interactiveExecutor
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getQueue (QueueType type)
specifier|public
name|ScheduledThreadPoolExecutor
name|getQueue
parameter_list|(
name|QueueType
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|INTERACTIVE
case|:
return|return
name|interactiveExecutor
return|;
case|case
name|BATCH
case|:
default|default:
return|return
name|batchExecutor
return|;
block|}
block|}
block|}
end_class

end_unit

