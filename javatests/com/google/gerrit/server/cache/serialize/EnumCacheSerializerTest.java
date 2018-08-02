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
name|Truth
operator|.
name|assert_
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
DECL|class|EnumCacheSerializerTest
specifier|public
class|class
name|EnumCacheSerializerTest
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
name|assertRoundTrip
argument_list|(
name|MyEnum
operator|.
name|FOO
argument_list|)
expr_stmt|;
name|assertRoundTrip
argument_list|(
name|MyEnum
operator|.
name|BAR
argument_list|)
expr_stmt|;
name|assertRoundTrip
argument_list|(
name|MyEnum
operator|.
name|BAZ
argument_list|)
expr_stmt|;
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
literal|""
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
literal|"QUUX"
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|enum|MyEnum
specifier|private
enum|enum
name|MyEnum
block|{
DECL|enumConstant|FOO
name|FOO
block|,
DECL|enumConstant|BAR
name|BAR
block|,
DECL|enumConstant|BAZ
name|BAZ
block|;   }
DECL|method|assertRoundTrip (MyEnum e)
specifier|private
specifier|static
name|void
name|assertRoundTrip
parameter_list|(
name|MyEnum
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|CacheSerializer
argument_list|<
name|MyEnum
argument_list|>
name|s
init|=
operator|new
name|EnumCacheSerializer
argument_list|<>
argument_list|(
name|MyEnum
operator|.
name|class
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
name|e
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|e
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
name|CacheSerializer
argument_list|<
name|MyEnum
argument_list|>
name|s
init|=
operator|new
name|EnumCacheSerializer
argument_list|<>
argument_list|(
name|MyEnum
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
name|s
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

