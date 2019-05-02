begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.cache.serialize
package|package
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
name|serialize
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
name|proto
operator|.
name|testing
operator|.
name|Test
operator|.
name|SerializableProto
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
DECL|class|ProtobufSerializerTest
specifier|public
class|class
name|ProtobufSerializerTest
block|{
annotation|@
name|Test
DECL|method|requiredAndOptionalTypes ()
specifier|public
name|void
name|requiredAndOptionalTypes
parameter_list|()
block|{
name|assertRoundTrip
argument_list|(
name|SerializableProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
argument_list|)
expr_stmt|;
name|assertRoundTrip
argument_list|(
name|SerializableProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
operator|.
name|setText
argument_list|(
literal|"foo bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exactByteSequence ()
specifier|public
name|void
name|exactByteSequence
parameter_list|()
block|{
name|ProtobufSerializer
argument_list|<
name|SerializableProto
argument_list|>
name|s
init|=
operator|new
name|ProtobufSerializer
argument_list|<>
argument_list|(
name|SerializableProto
operator|.
name|parser
argument_list|()
argument_list|)
decl_stmt|;
name|SerializableProto
name|proto
init|=
name|SerializableProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
operator|.
name|setText
argument_list|(
literal|"foo bar"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|s
operator|.
name|serialize
argument_list|(
name|proto
argument_list|)
decl_stmt|;
comment|// Hard-code byte sequence to detect library changes
name|assertThat
argument_list|(
name|serialized
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|8
block|,
literal|123
block|,
literal|18
block|,
literal|7
block|,
literal|102
block|,
literal|111
block|,
literal|111
block|,
literal|32
block|,
literal|98
block|,
literal|97
block|,
literal|114
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRoundTrip (SerializableProto.Builder input)
specifier|private
specifier|static
name|void
name|assertRoundTrip
parameter_list|(
name|SerializableProto
operator|.
name|Builder
name|input
parameter_list|)
block|{
name|ProtobufSerializer
argument_list|<
name|SerializableProto
argument_list|>
name|s
init|=
operator|new
name|ProtobufSerializer
argument_list|<>
argument_list|(
name|SerializableProto
operator|.
name|parser
argument_list|()
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|s
operator|.
name|deserialize
argument_list|(
name|s
operator|.
name|serialize
argument_list|(
name|input
operator|.
name|build
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

