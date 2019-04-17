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
name|LabelId
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
DECL|class|LabelIdProtoConverterTest
specifier|public
class|class
name|LabelIdProtoConverterTest
block|{
DECL|field|labelIdProtoConverter
specifier|private
specifier|final
name|LabelIdProtoConverter
name|labelIdProtoConverter
init|=
name|LabelIdProtoConverter
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
name|LabelId
name|labelId
init|=
name|LabelId
operator|.
name|create
argument_list|(
literal|"Label ID 42"
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|LabelId
name|proto
init|=
name|labelIdProtoConverter
operator|.
name|toProto
argument_list|(
name|labelId
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|LabelId
name|expectedProto
init|=
name|Entities
operator|.
name|LabelId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"Label ID 42"
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
name|LabelId
name|labelId
init|=
name|LabelId
operator|.
name|create
argument_list|(
literal|"label-5"
argument_list|)
decl_stmt|;
name|LabelId
name|convertedLabelId
init|=
name|labelIdProtoConverter
operator|.
name|fromProto
argument_list|(
name|labelIdProtoConverter
operator|.
name|toProto
argument_list|(
name|labelId
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|convertedLabelId
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|labelId
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
name|LabelId
name|proto
init|=
name|Entities
operator|.
name|LabelId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"label-23"
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
name|LabelId
argument_list|>
name|parser
init|=
name|labelIdProtoConverter
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|LabelId
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
DECL|method|methodsExistAsExpected ()
specifier|public
name|void
name|methodsExistAsExpected
parameter_list|()
block|{
name|assertThatSerializedClass
argument_list|(
name|LabelId
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

