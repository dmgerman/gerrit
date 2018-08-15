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
DECL|package|com.google.gerrit.acceptance.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
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
name|PushOneCommit
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
name|UseLocalDisk
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
name|changes
operator|.
name|ReviewInput
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
name|BranchApi
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
name|ReflogEntryInfo
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
annotation|@
name|UseLocalDisk
DECL|class|ReflogIT
specifier|public
class|class
name|ReflogIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|reflogUpdatedBySubmittingChange ()
specifier|public
name|void
name|reflogUpdatedBySubmittingChange
parameter_list|()
throws|throws
name|Exception
block|{
name|BranchApi
name|branchApi
init|=
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
name|branch
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ReflogEntryInfo
argument_list|>
name|reflog
init|=
name|branchApi
operator|.
name|reflog
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|reflog
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
comment|// Current number of entries in the reflog
name|int
name|refLogLen
init|=
name|reflog
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// Create and submit a change
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|String
name|changeId
init|=
name|r
operator|.
name|getChangeId
argument_list|()
decl_stmt|;
name|String
name|revision
init|=
name|r
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
name|ReviewInput
name|in
init|=
name|ReviewInput
operator|.
name|approve
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
operator|.
name|review
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|revision
argument_list|(
name|revision
argument_list|)
operator|.
name|submit
argument_list|()
expr_stmt|;
comment|// Submitting the change causes a new entry in the reflog
name|reflog
operator|=
name|branchApi
operator|.
name|reflog
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|reflog
argument_list|)
operator|.
name|hasSize
argument_list|(
name|refLogLen
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

