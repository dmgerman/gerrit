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
name|ImmutableList
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
name|common
operator|.
name|truth
operator|.
name|Truth
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
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|TypeLiteral
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
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
DECL|class|PatchSetProtoConverterTest
specifier|public
class|class
name|PatchSetProtoConverterTest
block|{
DECL|field|patchSetProtoConverter
specifier|private
specifier|final
name|PatchSetProtoConverter
name|patchSetProtoConverter
init|=
name|PatchSetProtoConverter
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
name|PatchSet
name|patchSet
init|=
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
literal|103
argument_list|)
argument_list|,
literal|73
argument_list|)
argument_list|)
operator|.
name|commitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|uploader
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|452
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|930349320L
argument_list|)
argument_list|)
operator|.
name|groups
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"group1"
argument_list|,
literal|" group2"
argument_list|)
argument_list|)
operator|.
name|pushCertificate
argument_list|(
literal|"my push certificate"
argument_list|)
operator|.
name|description
argument_list|(
literal|"This is a patch set description."
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|PatchSet
name|proto
init|=
name|patchSetProtoConverter
operator|.
name|toProto
argument_list|(
name|patchSet
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|PatchSet
name|expectedProto
init|=
name|Entities
operator|.
name|PatchSet
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
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
literal|103
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|73
argument_list|)
argument_list|)
operator|.
name|setCommitId
argument_list|(
name|Entities
operator|.
name|ObjectId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setName
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|setUploaderAccountId
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
literal|452
argument_list|)
argument_list|)
operator|.
name|setCreatedOn
argument_list|(
literal|930349320L
argument_list|)
operator|.
name|setGroups
argument_list|(
literal|"group1, group2"
argument_list|)
operator|.
name|setPushCertificate
argument_list|(
literal|"my push certificate"
argument_list|)
operator|.
name|setDescription
argument_list|(
literal|"This is a patch set description."
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
DECL|method|mandatoryValuesConvertedToProto ()
specifier|public
name|void
name|mandatoryValuesConvertedToProto
parameter_list|()
block|{
name|PatchSet
name|patchSet
init|=
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
literal|103
argument_list|)
argument_list|,
literal|73
argument_list|)
argument_list|)
operator|.
name|commitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|uploader
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|452
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|930349320L
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|PatchSet
name|proto
init|=
name|patchSetProtoConverter
operator|.
name|toProto
argument_list|(
name|patchSet
argument_list|)
decl_stmt|;
name|Entities
operator|.
name|PatchSet
name|expectedProto
init|=
name|Entities
operator|.
name|PatchSet
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
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
literal|103
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|73
argument_list|)
argument_list|)
operator|.
name|setCommitId
argument_list|(
name|Entities
operator|.
name|ObjectId
operator|.
name|newBuilder
argument_list|()
operator|.
name|setName
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|setUploaderAccountId
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
literal|452
argument_list|)
argument_list|)
operator|.
name|setCreatedOn
argument_list|(
literal|930349320L
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
name|PatchSet
name|patchSet
init|=
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
literal|103
argument_list|)
argument_list|,
literal|73
argument_list|)
argument_list|)
operator|.
name|commitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|uploader
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|452
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|930349320L
argument_list|)
argument_list|)
operator|.
name|groups
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
literal|"group1"
argument_list|,
literal|" group2"
argument_list|)
argument_list|)
operator|.
name|pushCertificate
argument_list|(
literal|"my push certificate"
argument_list|)
operator|.
name|description
argument_list|(
literal|"This is a patch set description."
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PatchSet
name|convertedPatchSet
init|=
name|patchSetProtoConverter
operator|.
name|fromProto
argument_list|(
name|patchSetProtoConverter
operator|.
name|toProto
argument_list|(
name|patchSet
argument_list|)
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|convertedPatchSet
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|patchSet
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mandatoryValuesConvertedToProtoAndBackAgain ()
specifier|public
name|void
name|mandatoryValuesConvertedToProtoAndBackAgain
parameter_list|()
block|{
name|PatchSet
name|patchSet
init|=
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
literal|103
argument_list|)
argument_list|,
literal|73
argument_list|)
argument_list|)
operator|.
name|commitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
operator|.
name|uploader
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|452
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|930349320L
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PatchSet
name|convertedPatchSet
init|=
name|patchSetProtoConverter
operator|.
name|fromProto
argument_list|(
name|patchSetProtoConverter
operator|.
name|toProto
argument_list|(
name|patchSet
argument_list|)
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|convertedPatchSet
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|patchSet
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|previouslyOptionalValuesMayBeMissingFromProto ()
specifier|public
name|void
name|previouslyOptionalValuesMayBeMissingFromProto
parameter_list|()
block|{
name|Entities
operator|.
name|PatchSet
name|proto
init|=
name|Entities
operator|.
name|PatchSet
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
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
literal|103
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|73
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|PatchSet
name|convertedPatchSet
init|=
name|patchSetProtoConverter
operator|.
name|fromProto
argument_list|(
name|proto
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|convertedPatchSet
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|PatchSet
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
name|PatchSet
operator|.
name|id
argument_list|(
name|Change
operator|.
name|id
argument_list|(
literal|103
argument_list|)
argument_list|,
literal|73
argument_list|)
argument_list|)
operator|.
name|commitId
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"0000000000000000000000000000000000000000"
argument_list|)
argument_list|)
operator|.
name|uploader
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|createdOn
argument_list|(
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
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
name|PatchSet
name|proto
init|=
name|Entities
operator|.
name|PatchSet
operator|.
name|newBuilder
argument_list|()
operator|.
name|setId
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
literal|103
argument_list|)
argument_list|)
operator|.
name|setId
argument_list|(
literal|73
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
name|PatchSet
argument_list|>
name|parser
init|=
name|patchSetProtoConverter
operator|.
name|getParser
argument_list|()
decl_stmt|;
name|Entities
operator|.
name|PatchSet
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
name|PatchSet
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
literal|"id"
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
literal|"commitId"
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"uploader"
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
literal|"createdOn"
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
literal|"groups"
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableList
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"pushCertificate"
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|Optional
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
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

