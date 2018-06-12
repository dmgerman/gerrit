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
name|common
operator|.
name|truth
operator|.
name|extensions
operator|.
name|proto
operator|.
name|ProtoTruth
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
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|cache
operator|.
name|testing
operator|.
name|CacheSerializerTestUtil
operator|.
name|bytes
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
name|server
operator|.
name|cache
operator|.
name|testing
operator|.
name|SerializedClassSubject
operator|.
name|assertThatSerializedClass
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
name|ImmutableMap
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
name|client
operator|.
name|SubmitType
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
name|server
operator|.
name|cache
operator|.
name|proto
operator|.
name|Cache
operator|.
name|ConflictKeyProto
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
name|ConflictKey
operator|.
name|createWithoutNormalization
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
name|ConflictKey
operator|.
name|createWithoutNormalization
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
name|ConflictKey
operator|.
name|createWithoutNormalization
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
annotation|@
name|Test
DECL|method|serializer ()
specifier|public
name|void
name|serializer
parameter_list|()
throws|throws
name|Exception
block|{
name|ConflictKey
name|key
init|=
name|ConflictKey
operator|.
name|create
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"badc0feebadc0feebadc0feebadc0feebadc0fee"
argument_list|)
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|,
name|SubmitType
operator|.
name|MERGE_IF_NECESSARY
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|ConflictKey
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ConflictKeyProto
operator|.
name|parseFrom
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ConflictKeyProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setCommit
argument_list|(
name|bytes
argument_list|(
literal|0xba
argument_list|,
literal|0xdc
argument_list|,
literal|0x0f
argument_list|,
literal|0xee
argument_list|,
literal|0xba
argument_list|,
literal|0xdc
argument_list|,
literal|0x0f
argument_list|,
literal|0xee
argument_list|,
literal|0xba
argument_list|,
literal|0xdc
argument_list|,
literal|0x0f
argument_list|,
literal|0xee
argument_list|,
literal|0xba
argument_list|,
literal|0xdc
argument_list|,
literal|0x0f
argument_list|,
literal|0xee
argument_list|,
literal|0xba
argument_list|,
literal|0xdc
argument_list|,
literal|0x0f
argument_list|,
literal|0xee
argument_list|)
argument_list|)
operator|.
name|setOtherCommit
argument_list|(
name|bytes
argument_list|(
literal|0xde
argument_list|,
literal|0xad
argument_list|,
literal|0xbe
argument_list|,
literal|0xef
argument_list|,
literal|0xde
argument_list|,
literal|0xad
argument_list|,
literal|0xbe
argument_list|,
literal|0xef
argument_list|,
literal|0xde
argument_list|,
literal|0xad
argument_list|,
literal|0xbe
argument_list|,
literal|0xef
argument_list|,
literal|0xde
argument_list|,
literal|0xad
argument_list|,
literal|0xbe
argument_list|,
literal|0xef
argument_list|,
literal|0xde
argument_list|,
literal|0xad
argument_list|,
literal|0xbe
argument_list|,
literal|0xef
argument_list|)
argument_list|)
operator|.
name|setSubmitType
argument_list|(
literal|"MERGE_IF_NECESSARY"
argument_list|)
operator|.
name|setContentMerge
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ConflictKey
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**    * See {@link com.google.gerrit.server.cache.testing.SerializedClassSubject} for background and    * what to do if this test fails.    */
annotation|@
name|Test
DECL|method|methods ()
specifier|public
name|void
name|methods
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThatSerializedClass
argument_list|(
name|ConflictKey
operator|.
name|class
argument_list|)
operator|.
name|hasAutoValueMethods
argument_list|(
name|ImmutableMap
operator|.
name|of
argument_list|(
literal|"commit"
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|,
literal|"otherCommit"
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|,
literal|"submitType"
argument_list|,
name|SubmitType
operator|.
name|class
argument_list|,
literal|"contentMerge"
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
