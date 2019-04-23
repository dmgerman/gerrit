begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
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
name|reviewdb
operator|.
name|client
operator|.
name|Account
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
name|PatchSet
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
name|gerrit
operator|.
name|testing
operator|.
name|GerritBaseTests
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
name|testing
operator|.
name|TestChanges
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
DECL|class|ChangeDataTest
specifier|public
class|class
name|ChangeDataTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|setPatchSetsClearsCurrentPatchSet ()
specifier|public
name|void
name|setPatchSetsClearsCurrentPatchSet
parameter_list|()
throws|throws
name|Exception
block|{
name|Project
operator|.
name|NameKey
name|project
init|=
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project"
argument_list|)
decl_stmt|;
name|ChangeData
name|cd
init|=
name|ChangeData
operator|.
name|createForTest
argument_list|(
name|project
argument_list|,
name|Change
operator|.
name|id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|cd
operator|.
name|setChange
argument_list|(
name|TestChanges
operator|.
name|newChange
argument_list|(
name|project
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|1000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|PatchSet
name|curr1
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
name|int
name|currId
init|=
name|curr1
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|PatchSet
name|ps1
init|=
operator|new
name|PatchSet
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|currId
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSet
name|ps2
init|=
operator|new
name|PatchSet
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|cd
operator|.
name|getId
argument_list|()
argument_list|,
name|currId
operator|+
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|cd
operator|.
name|setPatchSets
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|ps1
argument_list|,
name|ps2
argument_list|)
argument_list|)
expr_stmt|;
name|PatchSet
name|curr2
init|=
name|cd
operator|.
name|currentPatchSet
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|curr2
argument_list|)
operator|.
name|isNotSameInstanceAs
argument_list|(
name|curr1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

