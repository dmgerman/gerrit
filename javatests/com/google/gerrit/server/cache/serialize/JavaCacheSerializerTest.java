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
name|gerrit
operator|.
name|testing
operator|.
name|GerritBaseTests
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
DECL|class|JavaCacheSerializerTest
specifier|public
class|class
name|JavaCacheSerializerTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|builtInTypes ()
specifier|public
name|void
name|builtInTypes
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertRoundTrip
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|1234
argument_list|)
argument_list|)
expr_stmt|;
name|assertRoundTrip
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|customType ()
specifier|public
name|void
name|customType
parameter_list|()
throws|throws
name|Exception
block|{
name|assertRoundTrip
argument_list|(
operator|new
name|AutoValue_JavaCacheSerializerTest_MyType
argument_list|(
literal|123
argument_list|,
literal|"four five six"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AutoValue
DECL|class|MyType
specifier|abstract
specifier|static
class|class
name|MyType
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|anInt ()
specifier|abstract
name|Integer
name|anInt
parameter_list|()
function_decl|;
DECL|method|aString ()
specifier|abstract
name|String
name|aString
parameter_list|()
function_decl|;
block|}
DECL|method|assertRoundTrip (T input)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Serializable
parameter_list|>
name|void
name|assertRoundTrip
parameter_list|(
name|T
name|input
parameter_list|)
throws|throws
name|Exception
block|{
name|JavaCacheSerializer
argument_list|<
name|T
argument_list|>
name|s
init|=
operator|new
name|JavaCacheSerializer
argument_list|<>
argument_list|()
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
name|input
argument_list|)
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

