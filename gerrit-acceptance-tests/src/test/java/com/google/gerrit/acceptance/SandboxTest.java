begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
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
name|server
operator|.
name|account
operator|.
name|PutUsername
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
annotation|@
name|Sandboxed
DECL|class|SandboxTest
specifier|public
class|class
name|SandboxTest
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|After
DECL|method|addUser ()
specifier|public
name|void
name|addUser
parameter_list|()
throws|throws
name|Exception
block|{
name|PutUsername
operator|.
name|Input
name|in
init|=
operator|new
name|PutUsername
operator|.
name|Input
argument_list|()
decl_stmt|;
name|in
operator|.
name|username
operator|=
literal|"sandboxuser"
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|put
argument_list|(
literal|"/accounts/sandboxuser"
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpStatus
operator|.
name|SC_CREATED
argument_list|)
expr_stmt|;
block|}
DECL|method|testUserNotPresent ()
specifier|private
name|void
name|testUserNotPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|r
init|=
name|adminSession
operator|.
name|get
argument_list|(
literal|"/accounts/sandboxuser"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|HttpStatus
operator|.
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUserNotPresent1 ()
specifier|public
name|void
name|testUserNotPresent1
parameter_list|()
throws|throws
name|Exception
block|{
name|testUserNotPresent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUserNotPresent2 ()
specifier|public
name|void
name|testUserNotPresent2
parameter_list|()
throws|throws
name|Exception
block|{
name|testUserNotPresent
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

