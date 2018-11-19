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
DECL|method|projectBare ()
specifier|public
name|void
name|projectBare
parameter_list|()
throws|throws
name|QueryParseException
block|{
name|Tree
name|r
decl_stmt|;
name|r
operator|=
name|parse
argument_list|(
literal|"project:tools/gerrit"
argument_list|)
expr_stmt|;
name|assertSingleWord
argument_list|(
literal|"project"
argument_list|,
literal|"tools/gerrit"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|r
operator|=
name|parse
argument_list|(
literal|"project:tools/*"
argument_list|)
expr_stmt|;
name|assertSingleWord
argument_list|(
literal|"project"
argument_list|,
literal|"tools/*"
argument_list|,
name|r
argument_list|)
expr_stmt|;
block|}
DECL|method|assertSingleWord (String name, String value, Tree r)
specifier|private
specifier|static
name|void
name|assertSingleWord
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|,
name|Tree
name|r
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|QueryParser
operator|.
name|FIELD_NAME
argument_list|,
name|r
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|r
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|r
operator|.
name|getChildCount
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Tree
name|c
init|=
name|r
operator|.
name|getChild
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|QueryParser
operator|.
name|SINGLE_WORD
argument_list|,
name|c
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|value
argument_list|,
name|c
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|c
operator|.
name|getChildCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|parse (String str)
specifier|private
specifier|static
name|Tree
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
throws|throws
name|QueryParseException
block|{
return|return
name|QueryParser
operator|.
name|parse
argument_list|(
name|str
argument_list|)
return|;
block|}
block|}
end_class

end_unit

