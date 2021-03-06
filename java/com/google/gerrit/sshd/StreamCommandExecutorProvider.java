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
name|Provider
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
DECL|class|StreamCommandExecutorProvider
class|class
name|StreamCommandExecutorProvider
implements|implements
name|Provider
argument_list|<
name|ScheduledThreadPoolExecutor
argument_list|>
block|{
DECL|field|poolSize
specifier|private
specifier|final
name|int
name|poolSize
decl_stmt|;
DECL|field|queues
specifier|private
specifier|final
name|WorkQueue
name|queues
decl_stmt|;
annotation|@
name|Inject
DECL|method|StreamCommandExecutorProvider (@erritServerConfig Config config, WorkQueue wq)
name|StreamCommandExecutorProvider
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|,
name|WorkQueue
name|wq
parameter_list|)
block|{
specifier|final
name|int
name|cores
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|availableProcessors
argument_list|()
decl_stmt|;
name|poolSize
operator|=
name|config
operator|.
name|getInt
argument_list|(
literal|"sshd"
argument_list|,
literal|"streamThreads"
argument_list|,
name|cores
operator|+
literal|1
argument_list|)
expr_stmt|;
name|queues
operator|=
name|wq
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|ScheduledThreadPoolExecutor
name|get
parameter_list|()
block|{
return|return
name|queues
operator|.
name|createQueue
argument_list|(
name|poolSize
argument_list|,
literal|"SSH-Stream-Worker"
argument_list|,
name|Thread
operator|.
name|MIN_PRIORITY
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

