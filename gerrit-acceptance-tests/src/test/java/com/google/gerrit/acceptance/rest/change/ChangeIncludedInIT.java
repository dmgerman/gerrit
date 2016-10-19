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
name|NoHttpd
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
operator|.
name|Result
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
name|TagInput
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
name|reviewdb
operator|.
name|client
operator|.
name|Branch
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
name|NoHttpd
DECL|class|ChangeIncludedInIT
specifier|public
class|class
name|ChangeIncludedInIT
extends|extends
name|AbstractDaemonTest
block|{
annotation|@
name|Test
DECL|method|includedInOpenChange ()
specifier|public
name|void
name|includedInOpenChange
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|branches
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|tags
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|includedInMergedChange ()
specifier|public
name|void
name|includedInMergedChange
parameter_list|()
throws|throws
name|Exception
block|{
name|Result
name|result
init|=
name|createChange
argument_list|()
decl_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|revision
argument_list|(
name|result
operator|.
name|getCommit
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|submit
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|branches
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|tags
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|grantTagPermissions
argument_list|()
expr_stmt|;
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
name|tag
argument_list|(
literal|"test-tag"
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|TagInput
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|tags
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"test-tag"
argument_list|)
expr_stmt|;
name|createBranch
argument_list|(
operator|new
name|Branch
operator|.
name|NameKey
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"test-branch"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|result
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|includedIn
argument_list|()
operator|.
name|branches
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
literal|"test-branch"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

