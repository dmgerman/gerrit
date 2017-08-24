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
DECL|package|com.google.gerrit.acceptance.rest.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|config
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
name|RestResponse
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
name|ListTasks
operator|.
name|TaskInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|reflect
operator|.
name|TypeToken
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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

begin_class
DECL|class|GetTaskIT
specifier|public
class|class
name|GetTaskIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|getTask ()
specifier|public
name|void
name|getTask
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/tasks/"
operator|+
name|getLogFileCompressorTaskId
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|TaskInfo
name|info
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|TaskInfo
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|id
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|Long
operator|.
name|parseLong
argument_list|(
name|info
operator|.
name|id
argument_list|,
literal|16
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|command
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Log File Compressor"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|startTime
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTask_NotFound ()
specifier|public
name|void
name|getTask_NotFound
parameter_list|()
throws|throws
name|Exception
block|{
name|userRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/tasks/"
operator|+
name|getLogFileCompressorTaskId
argument_list|()
argument_list|)
operator|.
name|assertNotFound
argument_list|()
expr_stmt|;
block|}
DECL|method|getLogFileCompressorTaskId ()
specifier|private
name|String
name|getLogFileCompressorTaskId
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/tasks/"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|TaskInfo
argument_list|>
name|result
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
operator|new
name|TypeToken
argument_list|<
name|List
argument_list|<
name|TaskInfo
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
for|for
control|(
name|TaskInfo
name|info
range|:
name|result
control|)
block|{
if|if
condition|(
literal|"Log File Compressor"
operator|.
name|equals
argument_list|(
name|info
operator|.
name|command
argument_list|)
condition|)
block|{
return|return
name|info
operator|.
name|id
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

