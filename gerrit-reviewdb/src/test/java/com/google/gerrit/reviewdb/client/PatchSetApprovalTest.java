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
DECL|package|com.google.gerrit.reviewdb.client
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
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
name|testutil
operator|.
name|GerritBaseTests
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
DECL|class|PatchSetApprovalTest
specifier|public
class|class
name|PatchSetApprovalTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|keyEquality ()
specifier|public
name|void
name|keyEquality
parameter_list|()
block|{
name|PatchSetApproval
operator|.
name|Key
name|k1
init|=
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|2
argument_list|)
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|LabelId
argument_list|(
literal|"My-Label"
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSetApproval
operator|.
name|Key
name|k2
init|=
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|2
argument_list|)
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|LabelId
argument_list|(
literal|"My-Label"
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSetApproval
operator|.
name|Key
name|k3
init|=
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|1
argument_list|)
argument_list|,
literal|2
argument_list|)
argument_list|,
operator|new
name|Account
operator|.
name|Id
argument_list|(
literal|3
argument_list|)
argument_list|,
operator|new
name|LabelId
argument_list|(
literal|"Other-Label"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|k2
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|k3
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|k2
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|k1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|k3
operator|.
name|hashCode
argument_list|()
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|k1
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|PatchSetApproval
operator|.
name|Key
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|k1
argument_list|,
literal|"k1"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|k2
argument_list|,
literal|"k2"
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|k3
argument_list|,
literal|"k3"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsKey
argument_list|(
name|k1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsKey
argument_list|(
name|k2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsKey
argument_list|(
name|k3
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsEntry
argument_list|(
name|k1
argument_list|,
literal|"k2"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsEntry
argument_list|(
name|k2
argument_list|,
literal|"k2"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|map
argument_list|)
operator|.
name|containsEntry
argument_list|(
name|k3
argument_list|,
literal|"k3"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

