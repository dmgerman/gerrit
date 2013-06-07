begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|base
operator|.
name|Predicate
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
name|Iterables
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
name|Lists
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

begin_class
DECL|class|BranchAssert
specifier|public
class|class
name|BranchAssert
block|{
DECL|method|assertBranches (List<BranchInfo> expectedBranches, List<BranchInfo> actualBranches)
specifier|public
specifier|static
name|void
name|assertBranches
parameter_list|(
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|expectedBranches
parameter_list|,
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|actualBranches
parameter_list|)
block|{
name|List
argument_list|<
name|BranchInfo
argument_list|>
name|missingBranches
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|actualBranches
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|BranchInfo
name|b
range|:
name|expectedBranches
control|)
block|{
name|BranchInfo
name|info
init|=
name|Iterables
operator|.
name|find
argument_list|(
name|actualBranches
argument_list|,
operator|new
name|Predicate
argument_list|<
name|BranchInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|BranchInfo
name|info
parameter_list|)
block|{
return|return
name|info
operator|.
name|ref
operator|.
name|equals
argument_list|(
name|b
operator|.
name|ref
argument_list|)
return|;
block|}
block|}
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"missing branch: "
operator|+
name|b
operator|.
name|ref
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|assertBranchInfo
argument_list|(
name|b
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|missingBranches
operator|.
name|remove
argument_list|(
name|info
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"unexpected branches: "
operator|+
name|missingBranches
argument_list|,
name|missingBranches
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertBranchInfo (BranchInfo expected, BranchInfo actual)
specifier|public
specifier|static
name|void
name|assertBranchInfo
parameter_list|(
name|BranchInfo
name|expected
parameter_list|,
name|BranchInfo
name|actual
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|ref
argument_list|,
name|actual
operator|.
name|ref
argument_list|)
expr_stmt|;
if|if
condition|(
name|expected
operator|.
name|revision
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|expected
operator|.
name|revision
argument_list|,
name|actual
operator|.
name|revision
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expected
operator|.
name|can_delete
argument_list|,
name|toBoolean
argument_list|(
name|actual
operator|.
name|can_delete
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|toBoolean (Boolean b)
specifier|private
specifier|static
name|boolean
name|toBoolean
parameter_list|(
name|Boolean
name|b
parameter_list|)
block|{
if|if
condition|(
name|b
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|b
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

