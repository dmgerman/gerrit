begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|BaseParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|Parboiled
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|annotations
operator|.
name|BuildParseTree
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|parserunners
operator|.
name|ReportingParseRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|support
operator|.
name|ParseTreeUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|parboiled
operator|.
name|support
operator|.
name|ParsingResult
import|;
end_import

begin_class
DECL|class|ParboiledTest
specifier|public
class|class
name|ParboiledTest
block|{
DECL|field|EXPECTED
specifier|private
specifier|static
specifier|final
name|String
name|EXPECTED
init|=
literal|"[Expression] '42'\n"
operator|+
literal|"  [Term] '42'\n"
operator|+
literal|"    [Factor] '42'\n"
operator|+
literal|"      [Number] '42'\n"
operator|+
literal|"        [0..9] '4'\n"
operator|+
literal|"        [0..9] '2'\n"
operator|+
literal|"    [zeroOrMore]\n"
operator|+
literal|"  [zeroOrMore]\n"
decl_stmt|;
DECL|field|parser
specifier|private
name|CalculatorParser
name|parser
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|parser
operator|=
name|Parboiled
operator|.
name|createParser
argument_list|(
name|CalculatorParser
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
block|{
name|ParsingResult
argument_list|<
name|String
argument_list|>
name|result
init|=
operator|new
name|ReportingParseRunner
argument_list|<
name|String
argument_list|>
argument_list|(
name|parser
operator|.
name|Expression
argument_list|()
argument_list|)
operator|.
name|run
argument_list|(
literal|"42"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|result
operator|.
name|isSuccess
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// next test is optional; we could stop here.
name|assertThat
argument_list|(
name|ParseTreeUtils
operator|.
name|printNodeTree
argument_list|(
name|result
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|EXPECTED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BuildParseTree
DECL|class|CalculatorParser
specifier|static
class|class
name|CalculatorParser
extends|extends
name|BaseParser
argument_list|<
name|Object
argument_list|>
block|{
DECL|method|Expression ()
name|Rule
name|Expression
parameter_list|()
block|{
return|return
name|sequence
argument_list|(
name|Term
argument_list|()
argument_list|,
name|zeroOrMore
argument_list|(
name|anyOf
argument_list|(
literal|"+-"
argument_list|)
argument_list|,
name|Term
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|Term ()
name|Rule
name|Term
parameter_list|()
block|{
return|return
name|sequence
argument_list|(
name|Factor
argument_list|()
argument_list|,
name|zeroOrMore
argument_list|(
name|anyOf
argument_list|(
literal|"*/"
argument_list|)
argument_list|,
name|Factor
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|Factor ()
name|Rule
name|Factor
parameter_list|()
block|{
return|return
name|firstOf
argument_list|(
name|Number
argument_list|()
argument_list|,
name|sequence
argument_list|(
literal|'('
argument_list|,
name|Expression
argument_list|()
argument_list|,
literal|')'
argument_list|)
argument_list|)
return|;
block|}
DECL|method|Number ()
name|Rule
name|Number
parameter_list|()
block|{
return|return
name|oneOrMore
argument_list|(
name|charRange
argument_list|(
literal|'0'
argument_list|,
literal|'9'
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

