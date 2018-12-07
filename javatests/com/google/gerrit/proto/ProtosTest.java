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
DECL|package|com.google.gerrit.proto
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|proto
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
name|assert_
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
name|ChangeNotesKeyProto
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
name|ChangeNotesStateProto
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
name|com
operator|.
name|google
operator|.
name|protobuf
operator|.
name|ByteString
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
DECL|class|ProtosTest
specifier|public
class|class
name|ProtosTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|parseUncheckedWrongProtoType ()
specifier|public
name|void
name|parseUncheckedWrongProtoType
parameter_list|()
block|{
name|ChangeNotesKeyProto
name|proto
init|=
name|ChangeNotesKeyProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setProject
argument_list|(
literal|"project"
argument_list|)
operator|.
name|setChangeId
argument_list|(
literal|1234
argument_list|)
operator|.
name|setId
argument_list|(
name|ByteString
operator|.
name|copyFromUtf8
argument_list|(
literal|"foo"
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
name|Protos
operator|.
name|toByteArray
argument_list|(
name|proto
argument_list|)
decl_stmt|;
try|try
block|{
name|Protos
operator|.
name|parseUnchecked
argument_list|(
name|ChangeNotesStateProto
operator|.
name|parser
argument_list|()
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|parseUncheckedInvalidData ()
specifier|public
name|void
name|parseUncheckedInvalidData
parameter_list|()
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[]
block|{
literal|0x00
block|}
decl_stmt|;
try|try
block|{
name|Protos
operator|.
name|parseUnchecked
argument_list|(
name|ChangeNotesStateProto
operator|.
name|parser
argument_list|()
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
annotation|@
name|Test
DECL|method|parseUnchecked ()
specifier|public
name|void
name|parseUnchecked
parameter_list|()
block|{
name|ChangeNotesKeyProto
name|proto
init|=
name|ChangeNotesKeyProto
operator|.
name|newBuilder
argument_list|()
operator|.
name|setProject
argument_list|(
literal|"project"
argument_list|)
operator|.
name|setChangeId
argument_list|(
literal|1234
argument_list|)
operator|.
name|setId
argument_list|(
name|ByteString
operator|.
name|copyFromUtf8
argument_list|(
literal|"foo"
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
name|Protos
operator|.
name|toByteArray
argument_list|(
name|proto
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|Protos
operator|.
name|parseUnchecked
argument_list|(
name|ChangeNotesKeyProto
operator|.
name|parser
argument_list|()
argument_list|,
name|bytes
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|proto
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

