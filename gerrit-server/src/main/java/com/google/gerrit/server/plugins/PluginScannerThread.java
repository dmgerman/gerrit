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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
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

begin_class
DECL|class|PluginScannerThread
class|class
name|PluginScannerThread
extends|extends
name|Thread
block|{
DECL|field|done
specifier|private
specifier|final
name|CountDownLatch
name|done
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|loader
specifier|private
specifier|final
name|PluginLoader
name|loader
decl_stmt|;
DECL|field|checkFrequencyMillis
specifier|private
specifier|final
name|long
name|checkFrequencyMillis
decl_stmt|;
DECL|method|PluginScannerThread (PluginLoader loader, long checkFrequencyMillis)
name|PluginScannerThread
parameter_list|(
name|PluginLoader
name|loader
parameter_list|,
name|long
name|checkFrequencyMillis
parameter_list|)
block|{
name|this
operator|.
name|loader
operator|=
name|loader
expr_stmt|;
name|this
operator|.
name|checkFrequencyMillis
operator|=
name|checkFrequencyMillis
expr_stmt|;
name|setDaemon
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setName
argument_list|(
literal|"PluginScanner"
argument_list|)
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
for|for
control|(
init|;
condition|;
control|)
block|{
try|try
block|{
if|if
condition|(
name|done
operator|.
name|await
argument_list|(
name|checkFrequencyMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
condition|)
block|{
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{       }
name|loader
operator|.
name|rescan
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|end ()
name|void
name|end
parameter_list|()
block|{
name|done
operator|.
name|countDown
argument_list|()
expr_stmt|;
try|try
block|{
name|join
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{     }
block|}
block|}
end_class

end_unit

