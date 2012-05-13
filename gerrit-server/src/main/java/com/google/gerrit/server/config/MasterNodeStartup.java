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
name|PushAllProjectsOp
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
name|TimeUnit
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
name|OnStart
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|class|OnStart
specifier|static
class|class
name|OnStart
implements|implements
name|LifecycleListener
block|{
DECL|field|pushAll
specifier|private
specifier|final
name|PushAllProjectsOp
operator|.
name|Factory
name|pushAll
decl_stmt|;
DECL|field|submit
specifier|private
specifier|final
name|ReloadSubmitQueueOp
operator|.
name|Factory
name|submit
decl_stmt|;
DECL|field|replicateOnStartup
specifier|private
specifier|final
name|boolean
name|replicateOnStartup
decl_stmt|;
annotation|@
name|Inject
DECL|method|OnStart (final PushAllProjectsOp.Factory pushAll, final ReloadSubmitQueueOp.Factory submit, final @GerritServerConfig Config cfg)
name|OnStart
parameter_list|(
specifier|final
name|PushAllProjectsOp
operator|.
name|Factory
name|pushAll
parameter_list|,
specifier|final
name|ReloadSubmitQueueOp
operator|.
name|Factory
name|submit
parameter_list|,
specifier|final
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|pushAll
operator|=
name|pushAll
expr_stmt|;
name|this
operator|.
name|submit
operator|=
name|submit
expr_stmt|;
name|replicateOnStartup
operator|=
name|cfg
operator|.
name|getBoolean
argument_list|(
literal|"gerrit"
argument_list|,
literal|"replicateOnStartup"
argument_list|,
literal|true
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
name|replicateOnStartup
condition|)
block|{
name|pushAll
operator|.
name|create
argument_list|(
literal|null
argument_list|)
operator|.
name|start
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
name|submit
operator|.
name|create
argument_list|()
operator|.
name|start
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{     }
block|}
block|}
end_class

end_unit

