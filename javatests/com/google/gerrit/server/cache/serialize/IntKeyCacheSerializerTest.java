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
name|gwtorm
operator|.
name|client
operator|.
name|IntKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|Key
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
DECL|class|IntKeyCacheSerializerTest
specifier|public
class|class
name|IntKeyCacheSerializerTest
extends|extends
name|GerritBaseTests
block|{
DECL|class|MyIntKey
specifier|private
specifier|static
class|class
name|MyIntKey
extends|extends
name|IntKey
argument_list|<
name|Key
argument_list|<
name|?
argument_list|>
argument_list|>
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
DECL|field|val
specifier|private
name|int
name|val
decl_stmt|;
DECL|method|MyIntKey (int val)
name|MyIntKey
parameter_list|(
name|int
name|val
parameter_list|)
block|{
name|this
operator|.
name|val
operator|=
name|val
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|int
name|get
parameter_list|()
block|{
return|return
name|val
return|;
block|}
annotation|@
name|Override
DECL|method|set (int newValue)
specifier|protected
name|void
name|set
parameter_list|(
name|int
name|newValue
parameter_list|)
block|{
name|this
operator|.
name|val
operator|=
name|newValue
expr_stmt|;
block|}
block|}
DECL|field|SERIALIZER
specifier|private
specifier|static
specifier|final
name|IntKeyCacheSerializer
argument_list|<
name|MyIntKey
argument_list|>
name|SERIALIZER
init|=
operator|new
name|IntKeyCacheSerializer
argument_list|<>
argument_list|(
name|MyIntKey
operator|::
operator|new
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
name|MyIntKey
name|k
init|=
operator|new
name|MyIntKey
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
name|k
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
name|get
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

