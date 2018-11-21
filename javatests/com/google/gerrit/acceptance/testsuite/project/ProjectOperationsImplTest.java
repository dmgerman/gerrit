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
DECL|package|com.google.gerrit.acceptance.testsuite.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|testsuite
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
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BranchInfo
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
name|Project
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
DECL|class|ProjectOperationsImplTest
specifier|public
class|class
name|ProjectOperationsImplTest
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|projectOperations
annotation|@
name|Inject
specifier|private
name|ProjectOperations
name|projectOperations
decl_stmt|;
annotation|@
name|Test
DECL|method|defaultName ()
specifier|public
name|void
name|defaultName
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|name
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// check that the project was created (throws exception if not found.)
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Project
operator|.
name|NameKey
name|name2
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|name2
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|specifiedName ()
specifier|public
name|void
name|specifiedName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|name
init|=
literal|"somename"
decl_stmt|;
name|Project
operator|.
name|NameKey
name|key
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|key
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyCommit ()
specifier|public
name|void
name|emptyCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|key
init|=
name|projectOperations
operator|.
name|newProject
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|branches
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|key
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
decl_stmt|;
name|assertThat
argument_list|(
name|branches
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|branches
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|x
lambda|->
name|x
operator|.
name|ref
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"HEAD"
argument_list|,
literal|"refs/meta/config"
argument_list|,
literal|"refs/heads/master"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

