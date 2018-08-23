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
name|byteArray
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
DECL|class|ObjectIdCacheSerializerTest
specifier|public
class|class
name|ObjectIdCacheSerializerTest
block|{
annotation|@
name|Test
DECL|method|serialize ()
specifier|public
name|void
name|serialize
parameter_list|()
block|{
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
literal|"aabbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|ObjectIdCacheSerializer
operator|.
name|INSTANCE
operator|.
name|serialize
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|serialized
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|byteArray
argument_list|(
literal|0xaa
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|,
literal|0xbb
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|ObjectIdCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deserializeInvalid ()
specifier|public
name|void
name|deserializeInvalid
parameter_list|()
block|{
name|assertDeserializeFails
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
name|byteArray
argument_list|()
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
name|byteArray
argument_list|(
literal|0xaa
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeserializeFails
argument_list|(
name|byteArray
argument_list|(
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|,
literal|0xaa
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDeserializeFails (byte[] bytes)
specifier|private
name|void
name|assertDeserializeFails
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
try|try
block|{
name|ObjectIdCacheSerializer
operator|.
name|INSTANCE
operator|.
name|deserialize
argument_list|(
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
block|}
end_class

end_unit

