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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_class
DECL|class|ColumnFormatterTest
specifier|public
class|class
name|ColumnFormatterTest
extends|extends
name|TestCase
block|{
comment|/**    * Holds an in-memory {@link java.io.PrintWriter} object and allows    * comparisons of its contents to a supplied string via an assert statement.    */
DECL|class|PrintWriterComparator
class|class
name|PrintWriterComparator
block|{
DECL|field|printWriter
specifier|private
name|PrintWriter
name|printWriter
decl_stmt|;
DECL|field|stringWriter
specifier|private
name|StringWriter
name|stringWriter
decl_stmt|;
DECL|method|PrintWriterComparator ()
specifier|public
name|PrintWriterComparator
parameter_list|()
block|{
name|stringWriter
operator|=
operator|new
name|StringWriter
argument_list|()
expr_stmt|;
name|printWriter
operator|=
operator|new
name|PrintWriter
argument_list|(
name|stringWriter
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEquals (String str)
specifier|public
name|void
name|assertEquals
parameter_list|(
name|String
name|str
parameter_list|)
block|{
name|printWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|TestCase
operator|.
name|assertEquals
argument_list|(
name|stringWriter
operator|.
name|toString
argument_list|()
argument_list|,
name|str
argument_list|)
expr_stmt|;
block|}
DECL|method|getPrintWriter ()
specifier|public
name|PrintWriter
name|getPrintWriter
parameter_list|()
block|{
return|return
name|printWriter
return|;
block|}
block|}
comment|/**    * Test that only lines with at least one column of text emit output.    */
DECL|method|testEmptyLine ()
specifier|public
name|void
name|testEmptyLine
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|"foo\tbar\nfoo\tbar\n"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test that there is no output if no columns are ever added.    */
DECL|method|testEmptyOutput ()
specifier|public
name|void
name|testEmptyOutput
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test that there is no output (nor any exceptions) if we finalize    * the output immediately after the creation of the {@link ColumnFormatter}.    */
DECL|method|testNoNextLine ()
specifier|public
name|void
name|testNoNextLine
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test that the text in added columns is escaped while the column separator    * (which of course shouldn't be escaped) is left alone.    */
DECL|method|testEscapingTakesPlace ()
specifier|public
name|void
name|testEscapingTakesPlace
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"\tan indented multi-line\ntext"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|"foo\t\\tan indented multi-line\\ntext\n"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test that we get the correct output with multi-line input where the number    * of columns in each line varies.    */
DECL|method|testMultiLineDifferentColumnCount ()
specifier|public
name|void
name|testMultiLineDifferentColumnCount
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|"foo\tbar\tbaz\nfoo\tbar\n"
argument_list|)
expr_stmt|;
block|}
comment|/**    * Test that we get the correct output with a single column of input.    */
DECL|method|testOneColumn ()
specifier|public
name|void
name|testOneColumn
parameter_list|()
block|{
specifier|final
name|PrintWriterComparator
name|comparator
init|=
operator|new
name|PrintWriterComparator
argument_list|()
decl_stmt|;
specifier|final
name|ColumnFormatter
name|formatter
init|=
operator|new
name|ColumnFormatter
argument_list|(
name|comparator
operator|.
name|getPrintWriter
argument_list|()
argument_list|,
literal|'\t'
argument_list|)
decl_stmt|;
name|formatter
operator|.
name|addColumn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|nextLine
argument_list|()
expr_stmt|;
name|formatter
operator|.
name|finish
argument_list|()
expr_stmt|;
name|comparator
operator|.
name|assertEquals
argument_list|(
literal|"foo\n"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

