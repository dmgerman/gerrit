begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
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
name|project
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
name|Truth8
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
operator|.
name|RefNames
operator|.
name|REFS_HEADS
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BranchInput
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
name|common
operator|.
name|ChangeInput
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
DECL|class|CreateChangeIT
specifier|public
class|class
name|CreateChangeIT
extends|extends
name|AbstractDaemonTest
block|{
comment|// Just a basic test. The real functionality is tested under the restapi.change acceptance tests.
annotation|@
name|Test
DECL|method|basic ()
specifier|public
name|void
name|basic
parameter_list|()
throws|throws
name|Exception
block|{
name|BranchInput
name|branchInput
init|=
operator|new
name|BranchInput
argument_list|()
decl_stmt|;
name|branchInput
operator|.
name|ref
operator|=
literal|"foo"
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branches
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|i
lambda|->
name|i
operator|.
name|ref
argument_list|)
argument_list|)
operator|.
name|doesNotContain
argument_list|(
name|REFS_HEADS
operator|+
name|branchInput
operator|.
name|ref
argument_list|)
expr_stmt|;
name|RestResponse
name|r
init|=
name|adminRestSession
operator|.
name|put
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches/"
operator|+
name|branchInput
operator|.
name|ref
argument_list|,
name|branchInput
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertCreated
argument_list|()
expr_stmt|;
name|ChangeInput
name|input
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|input
operator|.
name|branch
operator|=
literal|"foo"
expr_stmt|;
name|input
operator|.
name|subject
operator|=
literal|"subject"
expr_stmt|;
name|RestResponse
name|cr
init|=
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/create.change"
argument_list|,
name|input
argument_list|)
decl_stmt|;
name|cr
operator|.
name|assertCreated
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

