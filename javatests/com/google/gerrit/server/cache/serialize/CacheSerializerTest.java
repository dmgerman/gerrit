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
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|base
operator|.
name|Converter
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
DECL|class|CacheSerializerTest
specifier|public
class|class
name|CacheSerializerTest
block|{
annotation|@
name|AutoValue
DECL|class|MyAutoValue
specifier|abstract
specifier|static
class|class
name|MyAutoValue
block|{
DECL|method|create (int val)
specifier|static
name|MyAutoValue
name|create
parameter_list|(
name|int
name|val
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_CacheSerializerTest_MyAutoValue
argument_list|(
name|val
argument_list|)
return|;
block|}
DECL|method|val ()
specifier|abstract
name|int
name|val
parameter_list|()
function_decl|;
block|}
DECL|field|SERIALIZER
specifier|private
specifier|static
specifier|final
name|CacheSerializer
argument_list|<
name|MyAutoValue
argument_list|>
name|SERIALIZER
init|=
name|CacheSerializer
operator|.
name|convert
argument_list|(
name|IntegerCacheSerializer
operator|.
name|INSTANCE
argument_list|,
name|Converter
operator|.
name|from
argument_list|(
name|MyAutoValue
operator|::
name|val
argument_list|,
name|MyAutoValue
operator|::
name|create
argument_list|)
argument_list|)
decl_stmt|;
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
name|MyAutoValue
name|v
init|=
name|MyAutoValue
operator|.
name|create
argument_list|(
literal|1234
argument_list|)
decl_stmt|;
name|byte
index|[]
name|serialized
init|=
name|SERIALIZER
operator|.
name|serialize
argument_list|(
name|v
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|serialized
argument_list|)
operator|.
name|isEqualTo
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|-
literal|46
block|,
literal|9
block|}
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|SERIALIZER
operator|.
name|deserialize
argument_list|(
name|serialized
argument_list|)
operator|.
name|val
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1234
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deserializeNullFails ()
specifier|public
name|void
name|deserializeNullFails
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|SERIALIZER
operator|.
name|deserialize
argument_list|(
literal|null
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

