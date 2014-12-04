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
DECL|package|com.google.gerrit.acceptance.api.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
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
name|ProblemInfo
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
name|Change
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
annotation|@
name|NoHttpd
DECL|class|CheckIT
specifier|public
class|class
name|CheckIT
extends|extends
name|AbstractDaemonTest
block|{
comment|// Most types of tests belong in ConsistencyCheckerTest; these mostly just
comment|// test paths outside of ConsistencyChecker, like API wiring.
annotation|@
name|Test
DECL|method|currentPatchSetMissing ()
specifier|public
name|void
name|currentPatchSetMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|PushOneCommit
operator|.
name|Result
name|r
init|=
name|createChange
argument_list|()
decl_stmt|;
name|Change
name|c
init|=
name|getChange
argument_list|(
name|r
argument_list|)
decl_stmt|;
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|deleteKeys
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ProblemInfo
argument_list|>
name|problems
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|check
argument_list|()
operator|.
name|problems
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|problems
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|message
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"Current patch set 1 not found"
argument_list|)
expr_stmt|;
block|}
DECL|method|getChange (PushOneCommit.Result r)
specifier|private
name|Change
name|getChange
parameter_list|(
name|PushOneCommit
operator|.
name|Result
name|r
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|r
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|_number
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

