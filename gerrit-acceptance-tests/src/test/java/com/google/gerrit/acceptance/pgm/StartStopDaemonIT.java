begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|pgm
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|Sandboxed
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ThreadMXBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_class
annotation|@
name|Sandboxed
DECL|class|StartStopDaemonIT
specifier|public
class|class
name|StartStopDaemonIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|suiteDescription
name|Description
name|suiteDescription
init|=
name|Description
operator|.
name|createSuiteDescription
argument_list|(
name|StartStopDaemonIT
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|sandboxedDaemonDoesNotLeakThreads ()
specifier|public
name|void
name|sandboxedDaemonDoesNotLeakThreads
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadMXBean
name|thbean
init|=
name|ManagementFactory
operator|.
name|getThreadMXBean
argument_list|()
decl_stmt|;
name|int
name|startThreads
init|=
name|thbean
operator|.
name|getThreadCount
argument_list|()
decl_stmt|;
name|beforeTest
argument_list|(
name|suiteDescription
argument_list|)
expr_stmt|;
name|afterTest
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|Thread
operator|.
name|activeCount
argument_list|()
argument_list|)
operator|.
name|isLessThan
argument_list|(
name|startThreads
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

