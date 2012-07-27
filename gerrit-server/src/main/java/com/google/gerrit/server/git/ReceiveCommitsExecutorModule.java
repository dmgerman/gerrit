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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
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

begin_comment
comment|/** Module providing the {@link ReceiveCommitsExecutor}. */
end_comment

begin_class
DECL|class|ReceiveCommitsExecutorModule
specifier|public
class|class
name|ReceiveCommitsExecutorModule
extends|extends
name|AbstractModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
name|void
name|configure
parameter_list|()
block|{   }
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|ReceiveCommitsExecutor
DECL|method|createReceiveCommitsExecutor ( @erritServerConfig Config config, WorkQueue queues)
specifier|public
name|WorkQueue
operator|.
name|Executor
name|createReceiveCommitsExecutor
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|WorkQueue
name|queues
parameter_list|)
block|{
name|int
name|poolSize
init|=
name|config
operator|.
name|getInt
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"threadPoolSize"
argument_list|,
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|queues
operator|.
name|createQueue
argument_list|(
name|poolSize
argument_list|,
literal|"ReceiveCommits"
argument_list|)
return|;
block|}
annotation|@
name|Provides
annotation|@
name|Singleton
annotation|@
name|ChangeUpdateExecutor
DECL|method|createChangeUpdateExecutor (@erritServerConfig Config config)
specifier|public
name|ListeningExecutorService
name|createChangeUpdateExecutor
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|int
name|poolSize
init|=
name|config
operator|.
name|getInt
argument_list|(
literal|"receive"
argument_list|,
literal|null
argument_list|,
literal|"changeUpdateThreads"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|poolSize
operator|<=
literal|1
condition|)
block|{
return|return
name|MoreExecutors
operator|.
name|sameThreadExecutor
argument_list|()
return|;
block|}
return|return
name|MoreExecutors
operator|.
name|listeningDecorator
argument_list|(
name|MoreExecutors
operator|.
name|getExitingExecutorService
argument_list|(
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|1
argument_list|,
name|poolSize
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|MINUTES
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
name|poolSize
argument_list|)
argument_list|,
operator|new
name|ThreadFactoryBuilder
argument_list|()
operator|.
name|setNameFormat
argument_list|(
literal|"ChangeUpdate-%d"
argument_list|)
operator|.
name|setDaemon
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
operator|new
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

