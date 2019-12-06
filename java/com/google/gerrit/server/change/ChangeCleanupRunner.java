begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|config
operator|.
name|ChangeCleanupConfig
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
name|gerrit
operator|.
name|server
operator|.
name|update
operator|.
name|RetryHelper
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
name|update
operator|.
name|UpdateException
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
name|ManualRequestContext
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
name|OneOffRequestContext
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

begin_comment
comment|/** Runnable to enable scheduling change cleanups to run periodically */
end_comment

begin_class
DECL|class|ChangeCleanupRunner
specifier|public
class|class
name|ChangeCleanupRunner
implements|implements
name|Runnable
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
DECL|class|Module
specifier|public
specifier|static
class|class
name|Module
extends|extends
name|LifecycleModule
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|protected
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
block|}
DECL|class|Lifecycle
specifier|static
class|class
name|Lifecycle
implements|implements
name|LifecycleListener
block|{
DECL|field|queue
specifier|private
specifier|final
name|WorkQueue
name|queue
decl_stmt|;
DECL|field|runner
specifier|private
specifier|final
name|ChangeCleanupRunner
name|runner
decl_stmt|;
DECL|field|cfg
specifier|private
specifier|final
name|ChangeCleanupConfig
name|cfg
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (WorkQueue queue, ChangeCleanupRunner runner, ChangeCleanupConfig cfg)
name|Lifecycle
parameter_list|(
name|WorkQueue
name|queue
parameter_list|,
name|ChangeCleanupRunner
name|runner
parameter_list|,
name|ChangeCleanupConfig
name|cfg
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|runner
operator|=
name|runner
expr_stmt|;
name|this
operator|.
name|cfg
operator|=
name|cfg
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
name|cfg
operator|.
name|getSchedule
argument_list|()
operator|.
name|ifPresent
argument_list|(
name|s
lambda|->
name|queue
operator|.
name|scheduleAtFixedRate
argument_list|(
name|runner
argument_list|,
name|s
argument_list|)
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
block|{
comment|// handled by WorkQueue.stop() already
block|}
block|}
DECL|field|oneOffRequestContext
specifier|private
specifier|final
name|OneOffRequestContext
name|oneOffRequestContext
decl_stmt|;
DECL|field|abandonUtil
specifier|private
specifier|final
name|AbandonUtil
name|abandonUtil
decl_stmt|;
DECL|field|retryHelper
specifier|private
specifier|final
name|RetryHelper
name|retryHelper
decl_stmt|;
annotation|@
name|Inject
DECL|method|ChangeCleanupRunner ( OneOffRequestContext oneOffRequestContext, AbandonUtil abandonUtil, RetryHelper retryHelper)
name|ChangeCleanupRunner
parameter_list|(
name|OneOffRequestContext
name|oneOffRequestContext
parameter_list|,
name|AbandonUtil
name|abandonUtil
parameter_list|,
name|RetryHelper
name|retryHelper
parameter_list|)
block|{
name|this
operator|.
name|oneOffRequestContext
operator|=
name|oneOffRequestContext
expr_stmt|;
name|this
operator|.
name|abandonUtil
operator|=
name|abandonUtil
expr_stmt|;
name|this
operator|.
name|retryHelper
operator|=
name|retryHelper
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
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
literal|"Running change cleanups."
argument_list|)
expr_stmt|;
try|try
init|(
name|ManualRequestContext
name|ctx
init|=
name|oneOffRequestContext
operator|.
name|open
argument_list|()
init|)
block|{
comment|// abandonInactiveOpenChanges skips failures instead of throwing, so retrying will never
comment|// actually happen. For the purposes of this class that is fine: they'll get tried again the
comment|// next time the scheduled task is run.
name|retryHelper
operator|.
name|changeUpdate
argument_list|(
literal|"abandonInactiveOpenChanges"
argument_list|,
name|updateFactory
lambda|->
block|{
name|abandonUtil
operator|.
name|abandonInactiveOpenChanges
argument_list|(
name|updateFactory
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RestApiException
decl||
name|UpdateException
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
literal|"Failed to cleanup changes."
argument_list|)
expr_stmt|;
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
return|return
literal|"change cleanup runner"
return|;
block|}
block|}
end_class

end_unit

