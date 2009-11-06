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
DECL|package|com.google.gwtexpui.safehtml.client
package|package
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|SafeHtml_ReplaceTest
specifier|public
class|class
name|SafeHtml_ReplaceTest
extends|extends
name|TestCase
block|{
DECL|method|testReplaceTwoLinks ()
specifier|public
name|void
name|testReplaceTwoLinks
parameter_list|()
block|{
specifier|final
name|RegexFindReplace
index|[]
name|repl
init|=
block|{
comment|//
operator|new
name|RegexFindReplace
argument_list|(
literal|"(issue\\s(\\d+))"
argument_list|,
literal|"<a href=\"?$2\">$1</a>"
argument_list|)
comment|//
block|}
decl_stmt|;
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\nissue 42\nissue 9918\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|replaceAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repl
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A\n"
comment|//
operator|+
literal|"<a href=\"?42\">issue 42</a>\n"
comment|//
operator|+
literal|"<a href=\"?9918\">issue 9918</a>\n"
comment|//
operator|+
literal|"B"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testReplaceInOrder1 ()
specifier|public
name|void
name|testReplaceInOrder1
parameter_list|()
block|{
specifier|final
name|RegexFindReplace
index|[]
name|repl
init|=
block|{
comment|//
operator|new
name|RegexFindReplace
argument_list|(
literal|"(GWTEXPUI-(\\d+))"
argument_list|,
literal|"<a href=\"gwtexpui-bug?$2\">$1</a>"
argument_list|)
block|,
comment|//
operator|new
name|RegexFindReplace
argument_list|(
literal|"(issue\\s+(\\d+))"
argument_list|,
literal|"<a href=\"generic-bug?$2\">$1</a>"
argument_list|)
block|,
comment|//
block|}
decl_stmt|;
specifier|final
name|SafeHtml
name|o
init|=
name|html
argument_list|(
literal|"A\nissue 42\nReally GWTEXPUI-9918 is better\nB"
argument_list|)
decl_stmt|;
specifier|final
name|SafeHtml
name|n
init|=
name|o
operator|.
name|replaceAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|repl
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A\n"
comment|//
operator|+
literal|"<a href=\"generic-bug?42\">issue 42</a>\n"
comment|//
operator|+
literal|"Really<a href=\"gwtexpui-bug?9918\">GWTEXPUI-9918</a> is better\n"
operator|+
literal|"B"
comment|//
argument_list|,
name|n
operator|.
name|asString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|html (String text)
specifier|private
specifier|static
name|SafeHtml
name|html
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
operator|new
name|SafeHtmlBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|toSafeHtml
argument_list|()
return|;
block|}
block|}
end_class

end_unit

