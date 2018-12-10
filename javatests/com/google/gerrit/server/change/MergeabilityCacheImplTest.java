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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|proto
operator|.
name|testing
operator|.
name|SerializedClassSubject
operator|.
name|assertThatSerializedClass
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
name|byteString
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
name|proto
operator|.
name|testing
operator|.
name|SerializedClassSubject
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
name|MergeabilityKeyProto
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
DECL|class|MergeabilityCacheImplTest
specifier|public
class|class
name|MergeabilityCacheImplTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|keySerializer ()
specifier|public
name|void
name|keySerializer
parameter_list|()
throws|throws
name|Exception
block|{
name|MergeabilityCacheImpl
operator|.
name|EntryKey
name|key
init|=
operator|new
name|MergeabilityCacheImpl
operator|.
name|EntryKey
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
literal|"aStrategy"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|MergeabilityCacheImpl
operator|.
name|EntryKey
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
name|MergeabilityKeyProto
operator|.
name|parseFrom
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|MergeabilityKeyProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setCommit
argument_list|(
name|byteString
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
name|setInto
argument_list|(
name|byteString
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
name|setMergeStrategy
argument_list|(
literal|"aStrategy"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|MergeabilityCacheImpl
operator|.
name|EntryKey
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
comment|/** See {@link SerializedClassSubject} for background and what to do if this test fails. */
annotation|@
name|Test
DECL|method|keyFields ()
specifier|public
name|void
name|keyFields
parameter_list|()
throws|throws
name|Exception
block|{
name|assertThatSerializedClass
argument_list|(
name|MergeabilityCacheImpl
operator|.
name|EntryKey
operator|.
name|class
argument_list|)
operator|.
name|hasFields
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
literal|"into"
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
literal|"mergeStrategy"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

