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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|client
operator|.
name|SubmitType
operator|.
name|FAST_FORWARD_ONLY
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
name|extensions
operator|.
name|client
operator|.
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|ObjectId
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
DECL|class|ConflictKeyTest
specifier|public
class|class
name|ConflictKeyTest
block|{
annotation|@
name|Test
DECL|method|ffOnlyPreservesInputOrder ()
specifier|public
name|void
name|ffOnlyPreservesInputOrder
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"badc0feebadc0feebadc0feebadc0feebadc0fee"
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
decl_stmt|;
name|ConflictKey
name|id1First
init|=
name|ConflictKey
operator|.
name|create
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|,
name|FAST_FORWARD_ONLY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ConflictKey
name|id2First
init|=
name|ConflictKey
operator|.
name|create
argument_list|(
name|id2
argument_list|,
name|id1
argument_list|,
name|FAST_FORWARD_ONLY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|id1First
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|ConflictKey
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|,
name|FAST_FORWARD_ONLY
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|id2First
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|ConflictKey
argument_list|(
name|id2
argument_list|,
name|id1
argument_list|,
name|FAST_FORWARD_ONLY
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|id1First
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|id2First
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|nonFfOnlyNormalizesInputOrder ()
specifier|public
name|void
name|nonFfOnlyNormalizesInputOrder
parameter_list|()
block|{
name|ObjectId
name|id1
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"badc0feebadc0feebadc0feebadc0feebadc0fee"
argument_list|)
decl_stmt|;
name|ObjectId
name|id2
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
decl_stmt|;
name|ConflictKey
name|id1First
init|=
name|ConflictKey
operator|.
name|create
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|,
name|MERGE_IF_NECESSARY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ConflictKey
name|id2First
init|=
name|ConflictKey
operator|.
name|create
argument_list|(
name|id2
argument_list|,
name|id1
argument_list|,
name|MERGE_IF_NECESSARY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ConflictKey
name|expected
init|=
operator|new
name|ConflictKey
argument_list|(
name|id1
argument_list|,
name|id2
argument_list|,
name|MERGE_IF_NECESSARY
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|id1First
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|id2First
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

