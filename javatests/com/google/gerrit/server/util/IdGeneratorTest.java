begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|util
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|util
operator|.
name|HashSet
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
DECL|class|IdGeneratorTest
specifier|public
class|class
name|IdGeneratorTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|test1234 ()
specifier|public
name|void
name|test1234
parameter_list|()
block|{
specifier|final
name|HashSet
argument_list|<
name|Integer
argument_list|>
name|seen
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1
operator|<<
literal|16
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|e
init|=
name|IdGenerator
operator|.
name|mix
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"no duplicates"
argument_list|,
name|seen
operator|.
name|add
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mirror image"
argument_list|,
name|i
argument_list|,
name|IdGenerator
operator|.
name|unmix
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0x801234ab
argument_list|,
name|IdGenerator
operator|.
name|unmix
argument_list|(
name|IdGenerator
operator|.
name|mix
argument_list|(
literal|0x801234ab
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xc0ffee12
argument_list|,
name|IdGenerator
operator|.
name|unmix
argument_list|(
name|IdGenerator
operator|.
name|mix
argument_list|(
literal|0xc0ffee12
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0xdeadbeef
argument_list|,
name|IdGenerator
operator|.
name|unmix
argument_list|(
name|IdGenerator
operator|.
name|mix
argument_list|(
literal|0xdeadbeef
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0x0b966b11
argument_list|,
name|IdGenerator
operator|.
name|unmix
argument_list|(
name|IdGenerator
operator|.
name|mix
argument_list|(
literal|0x0b966b11
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

