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
name|collect
operator|.
name|Lists
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
name|server
operator|.
name|config
operator|.
name|GcConfig
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
name|project
operator|.
name|ProjectCache
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
comment|/** Runnable to enable scheduling gc to run periodically */
end_comment

begin_class
DECL|class|GarbageCollectionRunner
specifier|public
class|class
name|GarbageCollectionRunner
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
DECL|field|gcRunner
specifier|private
specifier|final
name|GarbageCollectionRunner
name|gcRunner
decl_stmt|;
DECL|field|gcConfig
specifier|private
specifier|final
name|GcConfig
name|gcConfig
decl_stmt|;
annotation|@
name|Inject
DECL|method|Lifecycle (WorkQueue queue, GarbageCollectionRunner gcRunner, GcConfig config)
name|Lifecycle
parameter_list|(
name|WorkQueue
name|queue
parameter_list|,
name|GarbageCollectionRunner
name|gcRunner
parameter_list|,
name|GcConfig
name|config
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
name|gcRunner
operator|=
name|gcRunner
expr_stmt|;
name|this
operator|.
name|gcConfig
operator|=
name|config
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
name|gcConfig
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
name|gcRunner
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
DECL|field|garbageCollectionFactory
specifier|private
specifier|final
name|GarbageCollection
operator|.
name|Factory
name|garbageCollectionFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|GarbageCollectionRunner ( GarbageCollection.Factory garbageCollectionFactory, ProjectCache projectCache)
name|GarbageCollectionRunner
parameter_list|(
name|GarbageCollection
operator|.
name|Factory
name|garbageCollectionFactory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|garbageCollectionFactory
operator|=
name|garbageCollectionFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
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
literal|"Triggering gc on all repositories"
argument_list|)
expr_stmt|;
name|garbageCollectionFactory
operator|.
name|create
argument_list|()
operator|.
name|run
argument_list|(
name|Lists
operator|.
name|newArrayList
argument_list|(
name|projectCache
operator|.
name|all
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"GC runner"
return|;
block|}
block|}
end_class

end_unit

