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
DECL|package|com.google.gerrit.index.query
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|index
operator|.
name|query
operator|.
name|QueryParser
operator|.
name|FIELD_NAME
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
name|index
operator|.
name|query
operator|.
name|QueryParser
operator|.
name|SINGLE_WORD
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
name|index
operator|.
name|query
operator|.
name|QueryParser
operator|.
name|parse
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
name|index
operator|.
name|query
operator|.
name|testing
operator|.
name|TreeSubject
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
name|testing
operator|.
name|GerritBaseTests
import|;
end_import

begin_import
import|import
name|org
operator|.
name|antlr
operator|.
name|runtime
operator|.
name|tree
operator|.
name|Tree
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
DECL|class|QueryParserTest
specifier|public
class|class
name|QueryParserTest
extends|extends
name|GerritBaseTests
block|{
annotation|@
name|Test
DECL|method|fieldNameAndValue ()
specifier|public
name|void
name|fieldNameAndValue
parameter_list|()
throws|throws
name|Exception
block|{
name|Tree
name|r
init|=
name|parse
argument_list|(
literal|"project:tools/gerrit"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasType
argument_list|(
name|FIELD_NAME
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasText
argument_list|(
literal|"project"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|hasChildCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|child
argument_list|(
literal|0
argument_list|)
operator|.
name|hasType
argument_list|(
name|SINGLE_WORD
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|child
argument_list|(
literal|0
argument_list|)
operator|.
name|hasText
argument_list|(
literal|"tools/gerrit"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|child
argument_list|(
literal|0
argument_list|)
operator|.
name|hasNoChildren
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

