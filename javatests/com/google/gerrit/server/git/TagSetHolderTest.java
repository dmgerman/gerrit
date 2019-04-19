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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|TagSetHolderProto
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TagSetHolderTest
specifier|public
class|class
name|TagSetHolderTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|serializerWithTagSet ()
specifier|public
name|void
name|serializerWithTagSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TagSetHolder
name|holder
init|=
operator|new
name|TagSetHolder
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project"
argument_list|)
argument_list|)
decl_stmt|;
name|holder
operator|.
name|setTagSet
argument_list|(
operator|new
name|TagSet
argument_list|(
name|holder
operator|.
name|getProjectName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|serialized
init|=
name|TagSetHolder
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|holder
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|TagSetHolderProto
operator|.
name|parseFrom
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|ignoringRepeatedFieldOrder
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|TagSetHolderProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setProjectName
argument_list|(
literal|"project"
argument_list|)
operator|.
name|setTags
argument_list|(
name|holder
operator|.
name|getTagSet
argument_list|()
operator|.
name|toProto
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|TagSetHolder
name|deserialized
init|=
name|TagSetHolder
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|deserialized
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|holder
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
name|TagSetTest
operator|.
name|assertEqual
argument_list|(
name|holder
operator|.
name|getTagSet
argument_list|()
argument_list|,
name|deserialized
operator|.
name|getTagSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|serializerWithoutTagSet ()
specifier|public
name|void
name|serializerWithoutTagSet
parameter_list|()
throws|throws
name|Exception
block|{
name|TagSetHolder
name|holder
init|=
operator|new
name|TagSetHolder
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|TagSetHolder
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|holder
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|TagSetHolderProto
operator|.
name|parseFrom
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|ignoringRepeatedFieldOrder
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|TagSetHolderProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setProjectName
argument_list|(
literal|"project"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|TagSetHolder
name|deserialized
init|=
name|TagSetHolder
operator|.
name|Serializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|deserialized
operator|.
name|getProjectName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|holder
operator|.
name|getProjectName
argument_list|()
argument_list|)
expr_stmt|;
name|TagSetTest
operator|.
name|assertEqual
argument_list|(
name|holder
operator|.
name|getTagSet
argument_list|()
argument_list|,
name|deserialized
operator|.
name|getTagSet
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|fields ()
specifier|public
name|void
name|fields
parameter_list|()
block|{
name|assertThatSerializedClass
argument_list|(
name|TagSetHolder
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
literal|"buildLock"
argument_list|,
name|Object
operator|.
name|class
argument_list|,
literal|"projectName"
argument_list|,
name|Project
operator|.
name|NameKey
operator|.
name|class
argument_list|,
literal|"tags"
argument_list|,
name|TagSet
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

