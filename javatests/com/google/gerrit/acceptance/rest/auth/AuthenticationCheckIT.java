begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.auth
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
name|auth
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
name|acceptance
operator|.
name|RestSession
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
DECL|class|AuthenticationCheckIT
specifier|public
class|class
name|AuthenticationCheckIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|authCheck_loggedInUser_returnsOk ()
specifier|public
name|void
name|authCheck_loggedInUser_returnsOk
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
literal|"/auth-check"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|authCheck_anonymousUser_returnsForbidden ()
specifier|public
name|void
name|authCheck_anonymousUser_returnsForbidden
parameter_list|()
throws|throws
name|Exception
block|{
name|RestSession
name|anonymous
init|=
operator|new
name|RestSession
argument_list|(
name|server
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|RestResponse
name|r
init|=
name|anonymous
operator|.
name|get
argument_list|(
literal|"/auth-check"
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertForbidden
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

