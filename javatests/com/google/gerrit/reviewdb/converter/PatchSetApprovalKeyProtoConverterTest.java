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
name|LabelId
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
name|PatchSetApproval
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
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
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
DECL|class|PatchSetApprovalKeyProtoConverterTest
specifier|public
class|class
name|PatchSetApprovalKeyProtoConverterTest
block|{
DECL|field|protoConverter
specifier|private
specifier|final
name|PatchSetApprovalKeyProtoConverter
name|protoConverter
init|=
name|PatchSetApprovalKeyProtoConverter
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
name|PatchSetApproval
operator|.
name|Key
name|key
init|=
name|PatchSetApproval
operator|.
name|key
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|42
argument_list|)
argument_list|,
literal|14
argument_list|)
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|100013
argument_list|)
argument_list|,
name|LabelId
operator|.
name|create
argument_list|(
literal|"label-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|PatchSetApproval_Key
name|proto
init|=
name|protoConverter
operator|.
name|toProto
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|PatchSetApproval_Key
name|expectedProto
init|=
name|Entities
operator|.
name|PatchSetApproval_Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPatchSetId
argument_list|(
name|Entities
operator|.
name|PatchSet_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setChangeId
argument_list|(
name|Entities
operator|.
name|Change_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|42
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|14
argument_list|)
argument_list|)
operator|.
name|setAccountId
argument_list|(
name|Entities
operator|.
name|Account_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|100013
argument_list|)
argument_list|)
operator|.
name|setLabelId
argument_list|(
name|Entities
operator|.
name|LabelId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"label-8"
argument_list|)
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
name|PatchSetApproval
operator|.
name|Key
name|key
init|=
name|PatchSetApproval
operator|.
name|key
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
operator|new
name|Change
operator|.
name|Id
argument_list|(
literal|42
argument_list|)
argument_list|,
literal|14
argument_list|)
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|100013
argument_list|)
argument_list|,
name|LabelId
operator|.
name|create
argument_list|(
literal|"label-8"
argument_list|)
argument_list|)
decl_stmt|;
name|PatchSetApproval
operator|.
name|Key
name|convertedKey
init|=
name|protoConverter
operator|.
name|fromProto
argument_list|(
name|protoConverter
operator|.
name|toProto
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|convertedKey
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|key
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
name|PatchSetApproval_Key
name|proto
init|=
name|Entities
operator|.
name|PatchSetApproval_Key
operator|.
name|newBuilder
argument_list|()
operator|.
name|setPatchSetId
argument_list|(
name|Entities
operator|.
name|PatchSet_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setChangeId
argument_list|(
name|Entities
operator|.
name|Change_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|42
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|14
argument_list|)
argument_list|)
operator|.
name|setAccountId
argument_list|(
name|Entities
operator|.
name|Account_Id
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|100013
argument_list|)
argument_list|)
operator|.
name|setLabelId
argument_list|(
name|Entities
operator|.
name|LabelId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
argument_list|(
literal|"label-8"
argument_list|)
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
name|PatchSetApproval_Key
argument_list|>
name|parser
init|=
name|protoConverter
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|PatchSetApproval_Key
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
name|PatchSetApproval
operator|.
name|Key
operator|.
name|class
argument_list|)
operator|.
name|hasAutoValueMethods
argument_list|(
name|ImmutableMap
operator|.
expr|<
name|String
argument_list|,
name|Type
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
literal|"patchSetId"
argument_list|,
name|PatchSet
operator|.
name|Id
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"accountId"
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"labelId"
argument_list|,
name|LabelId
operator|.
name|class
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

