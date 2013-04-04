begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
import|import static
name|com
operator|.
name|google
operator|.
name|gwtexpui
operator|.
name|safehtml
operator|.
name|client
operator|.
name|LinkFindReplace
operator|.
name|hasValidScheme
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
DECL|class|LinkFindReplaceTest
specifier|public
class|class
name|LinkFindReplaceTest
extends|extends
name|TestCase
block|{
DECL|method|testNoEscaping ()
specifier|public
name|void
name|testNoEscaping
parameter_list|()
block|{
name|String
name|find
init|=
literal|"find"
decl_stmt|;
name|String
name|link
init|=
literal|"link"
decl_stmt|;
name|LinkFindReplace
name|a
init|=
operator|new
name|LinkFindReplace
argument_list|(
name|find
argument_list|,
name|link
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|find
argument_list|,
name|a
operator|.
name|pattern
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<a href=\"link\">find</a>"
argument_list|,
name|a
operator|.
name|replace
argument_list|(
name|find
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"find = "
operator|+
name|find
operator|+
literal|", link = "
operator|+
name|link
argument_list|,
name|a
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBackreference ()
specifier|public
name|void
name|testBackreference
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"<a href=\"/bug?id=123\">issue 123</a>"
argument_list|,
operator|new
name|LinkFindReplace
argument_list|(
literal|"(bug|issue)\\s*([0-9]+)"
argument_list|,
literal|"/bug?id=$2"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"issue 123"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHasValidScheme ()
specifier|public
name|void
name|testHasValidScheme
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"/absolute/path"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"relative/path"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"http://url/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"HTTP://url/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"https://url/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasValidScheme
argument_list|(
literal|"mailto://url/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasValidScheme
argument_list|(
literal|"ftp://url/"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasValidScheme
argument_list|(
literal|"data:evil"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasValidScheme
argument_list|(
literal|"javascript:alert(1)"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvalidSchemeInReplace ()
specifier|public
name|void
name|testInvalidSchemeInReplace
parameter_list|()
block|{
try|try
block|{
operator|new
name|LinkFindReplace
argument_list|(
literal|"find"
argument_list|,
literal|"javascript:alert(1)"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"find"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testInvalidSchemeWithBackreference ()
specifier|public
name|void
name|testInvalidSchemeWithBackreference
parameter_list|()
block|{
try|try
block|{
operator|new
name|LinkFindReplace
argument_list|(
literal|".*(script:[^;]*)"
argument_list|,
literal|"java$1"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"Look at this script: alert(1);"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected IllegalStateException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|expected
parameter_list|)
block|{     }
block|}
DECL|method|testReplaceEscaping ()
specifier|public
name|void
name|testReplaceEscaping
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|"<a href=\"a&quot;&amp;&#39;&lt;&gt;b\">find</a>"
argument_list|,
operator|new
name|LinkFindReplace
argument_list|(
literal|"find"
argument_list|,
literal|"a\"&'<>b"
argument_list|)
operator|.
name|replace
argument_list|(
literal|"find"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testHtmlInFind ()
specifier|public
name|void
name|testHtmlInFind
parameter_list|()
block|{
name|String
name|rawFind
init|=
literal|"<b>&quot;bold&quot;</b>"
decl_stmt|;
name|LinkFindReplace
name|a
init|=
operator|new
name|LinkFindReplace
argument_list|(
name|rawFind
argument_list|,
literal|"/bold"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|rawFind
argument_list|,
name|a
operator|.
name|pattern
argument_list|()
operator|.
name|getSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<a href=\"/bold\">"
operator|+
name|rawFind
operator|+
literal|"</a>"
argument_list|,
name|a
operator|.
name|replace
argument_list|(
name|rawFind
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

