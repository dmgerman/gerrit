begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|MINUTES
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
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
name|git
operator|.
name|ReloadSubmitQueueOp
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
name|ScheduledFuture
import|;
end_import

begin_comment
comment|/** Configuration for a master node in a cluster of servers. */
end_comment

begin_class
DECL|class|MasterNodeStartup
specifier|public
class|class
name|MasterNodeStartup
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|listener
argument_list|()
operator|.
name|to
argument_list|(
name|Lifecycle
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Singleton
DECL|class|Lifecycle
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|INITIAL_DELAY_S
specifier|private
specifier|static
specifier|final
name|int
name|INITIAL_DELAY_S
init|=
literal|15
decl_stmt|;
DECL|field|submit
specifier|private
specifier|final
name|ReloadSubmitQueueOp
operator|.
name|Factory
name|submit
decl_stmt|;
DECL|field|delay
specifier|private
specifier|final
name|long
name|delay
decl_stmt|;
DECL|field|handle
specifier|private
specifier|volatile
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|handle
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (ReloadSubmitQueueOp.Factory submit, @GerritServerConfig Config config)
name|Lifecycle
parameter_list|(
name|ReloadSubmitQueueOp
operator|.
name|Factory
name|submit
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|submit
operator|=
name|submit
expr_stmt|;
name|this
operator|.
name|delay
operator|=
name|ConfigUtil
operator|.
name|getTimeUnit
argument_list|(
name|config
argument_list|,
literal|"changeMerge"
argument_list|,
literal|null
argument_list|,
literal|"checkFrequency"
argument_list|,
name|SECONDS
operator|.
name|convert
argument_list|(
literal|5
argument_list|,
name|MINUTES
argument_list|)
argument_list|,
name|SECONDS
argument_list|)
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
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
name|handle
operator|=
name|submit
operator|.
name|create
argument_list|()
operator|.
name|startWithFixedDelay
argument_list|(
name|INITIAL_DELAY_S
argument_list|,
name|delay
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|handle
operator|=
name|submit
operator|.
name|create
argument_list|()
operator|.
name|start
argument_list|(
name|INITIAL_DELAY_S
argument_list|,
name|SECONDS
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
block|{
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|f
init|=
name|handle
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
name|handle
operator|=
literal|null
expr_stmt|;
name|f
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

