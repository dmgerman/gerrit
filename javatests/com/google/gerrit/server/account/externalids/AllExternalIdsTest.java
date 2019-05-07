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
DECL|package|com.google.gerrit.server.account.externalids
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|externalids
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
name|common
operator|.
name|collect
operator|.
name|ImmutableSetMultimap
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
name|server
operator|.
name|account
operator|.
name|externalids
operator|.
name|AllExternalIds
operator|.
name|Serializer
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
name|AllExternalIdsProto
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
name|AllExternalIdsProto
operator|.
name|ExternalIdProto
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
name|java
operator|.
name|util
operator|.
name|Arrays
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
DECL|class|AllExternalIdsTest
specifier|public
class|class
name|AllExternalIdsTest
block|{
annotation|@
name|Test
DECL|method|serializeEmptyExternalIds ()
specifier|public
name|void
name|serializeEmptyExternalIds
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
name|allExternalIds
argument_list|()
argument_list|,
name|AllExternalIdsProto
operator|.
name|getDefaultInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|serializeMultipleExternalIds ()
specifier|public
name|void
name|serializeMultipleExternalIds
parameter_list|()
throws|throws
name|Exception
block|{
name|Account
operator|.
name|Id
name|accountId1
init|=
name|Account
operator|.
name|id
argument_list|(
literal|1001
argument_list|)
decl_stmt|;
name|Account
operator|.
name|Id
name|accountId2
init|=
name|Account
operator|.
name|id
argument_list|(
literal|1002
argument_list|)
decl_stmt|;
name|assertRoundTrip
argument_list|(
name|allExternalIds
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme1"
argument_list|,
literal|"id1"
argument_list|,
name|accountId1
argument_list|)
argument_list|,
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme2"
argument_list|,
literal|"id2"
argument_list|,
name|accountId1
argument_list|)
argument_list|,
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme2"
argument_list|,
literal|"id3"
argument_list|,
name|accountId2
argument_list|)
argument_list|,
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme3"
argument_list|,
literal|"id4"
argument_list|,
name|accountId2
argument_list|)
argument_list|)
argument_list|,
name|AllExternalIdsProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme1:id1"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme2:id2"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1001
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme2:id3"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1002
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme3:id4"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1002
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|serializeExternalIdWithEmail ()
specifier|public
name|void
name|serializeExternalIdWithEmail
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
name|allExternalIds
argument_list|(
name|ExternalId
operator|.
name|createEmail
argument_list|(
name|Account
operator|.
name|id
argument_list|(
literal|1001
argument_list|)
argument_list|,
literal|"foo@example.com"
argument_list|)
argument_list|)
argument_list|,
name|AllExternalIdsProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"mailto:foo@example.com"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1001
argument_list|)
operator|.
name|setEmail
argument_list|(
literal|"foo@example.com"
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
DECL|method|serializeExternalIdWithPassword ()
specifier|public
name|void
name|serializeExternalIdWithPassword
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
name|allExternalIds
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme"
argument_list|,
literal|"id"
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|1001
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|"hashed password"
argument_list|)
argument_list|)
argument_list|,
name|AllExternalIdsProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme:id"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1001
argument_list|)
operator|.
name|setPassword
argument_list|(
literal|"hashed password"
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
DECL|method|serializeExternalIdWithBlobId ()
specifier|public
name|void
name|serializeExternalIdWithBlobId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
name|allExternalIds
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
name|ExternalId
operator|.
name|create
argument_list|(
literal|"scheme"
argument_list|,
literal|"id"
argument_list|,
name|Account
operator|.
name|id
argument_list|(
literal|1001
argument_list|)
argument_list|)
argument_list|,
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"deadbeefdeadbeefdeadbeefdeadbeefdeadbeef"
argument_list|)
argument_list|)
argument_list|)
argument_list|,
name|AllExternalIdsProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|addExternalId
argument_list|(
name|ExternalIdProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setKey
argument_list|(
literal|"scheme:id"
argument_list|)
operator|.
name|setAccountId
argument_list|(
literal|1001
argument_list|)
operator|.
name|setBlobId
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
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|allExternalIdsMethods ()
specifier|public
name|void
name|allExternalIdsMethods
parameter_list|()
block|{
name|assertThatSerializedClass
argument_list|(
name|AllExternalIds
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
literal|"byAccount"
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableSetMultimap
argument_list|<
name|Account
operator|.
name|Id
argument_list|,
name|ExternalId
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|,
literal|"byEmail"
argument_list|,
operator|new
name|TypeLiteral
argument_list|<
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|ExternalId
argument_list|>
argument_list|>
argument_list|()
block|{}
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|externalIdMethods ()
specifier|public
name|void
name|externalIdMethods
parameter_list|()
block|{
name|assertThatSerializedClass
argument_list|(
name|ExternalId
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
literal|"key"
argument_list|,
name|ExternalId
operator|.
name|Key
operator|.
name|class
argument_list|,
literal|"accountId"
argument_list|,
name|Account
operator|.
name|Id
operator|.
name|class
argument_list|,
literal|"email"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"password"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"blobId"
argument_list|,
name|ObjectId
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|allExternalIds (ExternalId... externalIds)
specifier|private
specifier|static
name|AllExternalIds
name|allExternalIds
parameter_list|(
name|ExternalId
modifier|...
name|externalIds
parameter_list|)
block|{
return|return
name|AllExternalIds
operator|.
name|create
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|externalIds
argument_list|)
argument_list|)
return|;
block|}
DECL|method|assertRoundTrip ( AllExternalIds allExternalIds, AllExternalIdsProto expectedProto)
specifier|private
specifier|static
name|void
name|assertRoundTrip
parameter_list|(
name|AllExternalIds
name|allExternalIds
parameter_list|,
name|AllExternalIdsProto
name|expectedProto
parameter_list|)
throws|throws
name|Exception
block|{
name|AllExternalIdsProto
name|actualProto
init|=
name|AllExternalIdsProto
operator|.
name|parseFrom
argument_list|(
name|Serializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|allExternalIds
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actualProto
argument_list|)
operator|.
name|ignoringRepeatedFieldOrder
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|expectedProto
argument_list|)
expr_stmt|;
name|AllExternalIds
name|actual
init|=
name|Serializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|Serializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|allExternalIds
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|actual
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|allExternalIds
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

