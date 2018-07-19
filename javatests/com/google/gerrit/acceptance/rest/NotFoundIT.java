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
DECL|package|com.google.gerrit.acceptance.rest
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
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_NOT_FOUND
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
DECL|class|NotFoundIT
specifier|public
class|class
name|NotFoundIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|nonExistingRootCollection ()
specifier|public
name|void
name|nonExistingRootCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|response
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/non-existing/"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Not Found"
argument_list|)
expr_stmt|;
name|response
operator|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/non-existing/"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Not Found"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonExistingView ()
specifier|public
name|void
name|nonExistingView
parameter_list|()
throws|throws
name|Exception
block|{
name|RestResponse
name|response
init|=
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/accounts/self/non-existing"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Not found: non-existing"
argument_list|)
expr_stmt|;
name|response
operator|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/accounts/self/non-existing"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|SC_NOT_FOUND
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|response
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Not found: non-existing"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

