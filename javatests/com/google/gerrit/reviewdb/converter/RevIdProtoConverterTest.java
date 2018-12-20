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
DECL|package|com.google.gerrit.reviewdb.converter
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|converter
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
name|proto
operator|.
name|Entities
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
name|reviewdb
operator|.
name|client
operator|.
name|RevId
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|Parser
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
DECL|class|RevIdProtoConverterTest
specifier|public
class|class
name|RevIdProtoConverterTest
block|{
DECL|field|revIdProtoConverter
specifier|private
specifier|final
name|RevIdProtoConverter
name|revIdProtoConverter
init|=
name|RevIdProtoConverter
operator|.
name|INSTANCE
decl_stmt|;
annotation|@
name|Test
DECL|method|allValuesConvertedToProto ()
specifier|public
name|void
name|allValuesConvertedToProto
parameter_list|()
block|{
name|RevId
name|revId
init|=
operator|new
name|RevId
argument_list|(
literal|"9903402f303249e"
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|RevId
name|proto
init|=
name|revIdProtoConverter
operator|.
name|toProto
argument_list|(
name|revId
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|RevId
name|expectedProto
init|=
name|Entities
operator|.
name|RevId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"9903402f303249e"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|proto
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedProto
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allValuesConvertedToProtoAndBackAgain ()
specifier|public
name|void
name|allValuesConvertedToProtoAndBackAgain
parameter_list|()
block|{
name|RevId
name|revId
init|=
operator|new
name|RevId
argument_list|(
literal|"ff3934a320bb"
argument_list|)
decl_stmt|;
name|RevId
name|convertedRevId
init|=
name|revIdProtoConverter
operator|.
name|fromProto
argument_list|(
name|revIdProtoConverter
operator|.
name|toProto
argument_list|(
name|revId
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|convertedRevId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|revId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|protoCanBeParsedFromBytes ()
specifier|public
name|void
name|protoCanBeParsedFromBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|Entities
operator|.
name|RevId
name|proto
init|=
name|Entities
operator|.
name|RevId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"9903402f303249e"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|proto
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|Parser
argument_list|<
name|Entities
operator|.
name|RevId
argument_list|>
name|parser
init|=
name|revIdProtoConverter
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|RevId
name|parsedProto
init|=
name|parser
operator|.
name|parseFrom
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|parsedProto
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|proto
argument_list|)
expr_stmt|;
block|}
comment|/** See {@link SerializedClassSubject} for background and what to do if this test fails. */
annotation|@
name|Test
DECL|method|fieldsExistAsExpected ()
specifier|public
name|void
name|fieldsExistAsExpected
parameter_list|()
block|{
name|assertThatSerializedClass
argument_list|(
name|RevId
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
literal|"id"
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

