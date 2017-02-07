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
DECL|package|com.google.gerrit.metrics.proc
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|metrics
operator|.
name|proc
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
name|metrics
operator|.
name|MetricMaker
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
comment|/** Guice module to configure metrics on server startup. */
end_comment

begin_class
DECL|class|MetricModule
specifier|public
specifier|abstract
class|class
name|MetricModule
extends|extends
name|LifecycleModule
block|{
comment|/** Configure metrics during server startup. */
DECL|method|configure (MetricMaker metrics)
specifier|protected
specifier|abstract
name|void
name|configure
parameter_list|(
name|MetricMaker
name|metrics
parameter_list|)
function_decl|;
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
name|toInstance
argument_list|(
operator|new
name|LifecycleListener
argument_list|()
block|{
annotation|@
name|Inject
name|MetricMaker
name|metrics
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|()
block|{
name|configure
argument_list|(
name|metrics
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|()
block|{}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

