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
name|assertWithMessage
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
name|Truth
operator|.
name|assert_
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
name|primitives
operator|.
name|Bytes
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
name|TextFormat
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
DECL|class|IntegerCacheSerializerTest
specifier|public
class|class
name|IntegerCacheSerializerTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|serialize ()
specifier|public
name|void
name|serialize
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|int
name|i
range|:
name|ImmutableList
operator|.
name|of
argument_list|(
name|Integer
operator|.
name|MIN_VALUE
argument_list|,
name|Integer
operator|.
name|MIN_VALUE
operator|+
literal|20
argument_list|,
operator|-
literal|1
argument_list|,
literal|0
argument_list|,
literal|1
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
operator|-
literal|20
argument_list|,
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
control|)
block|{
name|assertRoundTrip
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|deserializeInvalidValues ()
specifier|public
name|void
name|deserializeInvalidValues
parameter_list|()
throws|throws
name|Exception
block|{
name|assertDeserializeFails
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
name|Bytes
operator|.
name|concat
argument_list|(
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
literal|1
argument_list|)
argument_list|,
operator|new
name|byte
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRoundTrip (int i)
specifier|private
specifier|static
name|void
name|assertRoundTrip
parameter_list|(
name|int
name|i
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|serialized
init|=
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|int
name|result
init|=
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
decl_stmt|;
name|assertWithMessage
argument_list|(
literal|"round-trip of %s via \"%s\""
argument_list|,
name|i
argument_list|,
name|TextFormat
operator|.
name|escapeBytes
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|that
argument_list|(
name|result
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDeserializeFails (byte[] in)
specifier|private
specifier|static
name|void
name|assertDeserializeFails
parameter_list|(
name|byte
index|[]
name|in
parameter_list|)
block|{
try|try
block|{
name|IntegerCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"expected RuntimeException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Expected.
block|}
block|}
block|}
end_class

end_unit

