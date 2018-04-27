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
name|metrics
operator|.
name|Description
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
name|Description
operator|.
name|Units
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|storage
operator|.
name|file
operator|.
name|WindowCacheStats
import|;
end_import

begin_class
DECL|class|JGitMetricModule
specifier|public
class|class
name|JGitMetricModule
extends|extends
name|MetricModule
block|{
annotation|@
name|Override
DECL|method|configure (MetricMaker metrics)
specifier|protected
name|void
name|configure
parameter_list|(
name|MetricMaker
name|metrics
parameter_list|)
block|{
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"jgit/block_cache/cache_used"
argument_list|,
name|Long
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"Bytes of memory retained in JGit block cache."
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
name|Units
operator|.
name|BYTES
argument_list|)
argument_list|,
name|WindowCacheStats
operator|::
name|getOpenBytes
argument_list|)
expr_stmt|;
name|metrics
operator|.
name|newCallbackMetric
argument_list|(
literal|"jgit/block_cache/open_files"
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
operator|new
name|Description
argument_list|(
literal|"File handles held open by JGit block cache."
argument_list|)
operator|.
name|setGauge
argument_list|()
operator|.
name|setUnit
argument_list|(
literal|"fds"
argument_list|)
argument_list|,
name|WindowCacheStats
operator|::
name|getOpenFiles
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

