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
DECL|package|com.google.gerrit.acceptance.rest.change
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
name|change
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|IndexChangeIT
specifier|public
class|class
name|IndexChangeIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|indexChange ()
specifier|public
name|void
name|indexChange
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/index/"
argument_list|)
operator|.
name|assertNoContent
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|indexChangeOnNonVisibleBranch ()
specifier|public
name|void
name|indexChangeOnNonVisibleBranch
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|changeId
init|=
name|createChange
argument_list|()
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|blockRead
argument_list|(
literal|"refs/heads/master"
argument_list|)
expr_stmt|;
name|userRestSession
operator|.
name|post
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/index/"
argument_list|)
operator|.
name|assertNotFound
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

