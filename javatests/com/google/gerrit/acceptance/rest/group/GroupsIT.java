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
comment|// limitations under the License
end_comment

begin_package
DECL|package|com.google.gerrit.acceptance.rest.group
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
name|group
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|GroupsIT
specifier|public
class|class
name|GroupsIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|invalidQueryOptions ()
specifier|public
name|void
name|invalidQueryOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/groups/?query=foo&query2=bar"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"\"query\" and \"query2\" options are mutually exclusive"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

