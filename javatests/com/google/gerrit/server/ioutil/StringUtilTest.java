begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.ioutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|ioutil
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|StringUtilTest
specifier|public
class|class
name|StringUtilTest
block|{
comment|/** Test the boundary condition that the first character of a string should be escaped. */
annotation|@
name|Test
DECL|method|escapeFirstChar ()
specifier|public
name|void
name|escapeFirstChar
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|StringUtil
operator|.
name|escapeString
argument_list|(
literal|"\tLeading tab"
argument_list|)
argument_list|,
literal|"\\tLeading tab"
argument_list|)
expr_stmt|;
block|}
comment|/** Test the boundary condition that the last character of a string should be escaped. */
annotation|@
name|Test
DECL|method|escapeLastChar ()
specifier|public
name|void
name|escapeLastChar
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|StringUtil
operator|.
name|escapeString
argument_list|(
literal|"Trailing tab\t"
argument_list|)
argument_list|,
literal|"Trailing tab\\t"
argument_list|)
expr_stmt|;
block|}
comment|/** Test that various forms of input strings are escaped (or left as-is) in the expected way. */
annotation|@
name|Test
DECL|method|escapeString ()
specifier|public
name|void
name|escapeString
parameter_list|()
block|{
specifier|final
name|String
index|[]
name|testPairs
init|=
block|{
literal|""
block|,
literal|""
block|,
literal|"plain string"
block|,
literal|"plain string"
block|,
literal|"string with \"quotes\""
block|,
literal|"string with \"quotes\""
block|,
literal|"string with 'quotes'"
block|,
literal|"string with 'quotes'"
block|,
literal|"string with 'quotes'"
block|,
literal|"string with 'quotes'"
block|,
literal|"C:\\Program Files\\MyProgram"
block|,
literal|"C:\\\\Program Files\\\\MyProgram"
block|,
literal|"string\nwith\nnewlines"
block|,
literal|"string\\nwith\\nnewlines"
block|,
literal|"string\twith\ttabs"
block|,
literal|"string\\twith\\ttabs"
block|,     }
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
name|testPairs
operator|.
name|length
condition|;
name|i
operator|+=
literal|2
control|)
block|{
name|assertEquals
argument_list|(
name|StringUtil
operator|.
name|escapeString
argument_list|(
name|testPairs
index|[
name|i
index|]
argument_list|)
argument_list|,
name|testPairs
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

