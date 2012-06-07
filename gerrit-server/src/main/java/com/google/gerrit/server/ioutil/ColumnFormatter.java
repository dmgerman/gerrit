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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|StringUtil
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

begin_comment
comment|/**  * Simple output formatter for column-oriented data, writing its output to  * a {@link java.io.PrintWriter} object. Handles escaping of the column  * data so that the resulting output is unambiguous and reasonably safe and  * machine parsable.  */
end_comment

begin_class
DECL|class|ColumnFormatter
specifier|public
class|class
name|ColumnFormatter
block|{
DECL|field|columnSeparator
specifier|private
name|char
name|columnSeparator
decl_stmt|;
DECL|field|firstColumn
specifier|private
name|boolean
name|firstColumn
decl_stmt|;
DECL|field|out
specifier|private
specifier|final
name|PrintWriter
name|out
decl_stmt|;
comment|/**    * @param out The writer to which output should be sent.    * @param columnSeparator A character that should serve as the separator    *        token between columns of output. As only non-printable characters    *        in the column text are ever escaped, the column separator must be    *        a non-printable character if the output needs to be unambiguously    *        parsed.    */
DECL|method|ColumnFormatter (final PrintWriter out, final char columnSeparator)
specifier|public
name|ColumnFormatter
parameter_list|(
specifier|final
name|PrintWriter
name|out
parameter_list|,
specifier|final
name|char
name|columnSeparator
parameter_list|)
block|{
name|this
operator|.
name|out
operator|=
name|out
expr_stmt|;
name|this
operator|.
name|columnSeparator
operator|=
name|columnSeparator
expr_stmt|;
name|this
operator|.
name|firstColumn
operator|=
literal|true
expr_stmt|;
block|}
comment|/**    * Adds a text string as a new column in the current line of output,    * taking care of escaping as necessary.    *    * @param content the string to add.    */
DECL|method|addColumn (final String content)
specifier|public
name|void
name|addColumn
parameter_list|(
specifier|final
name|String
name|content
parameter_list|)
block|{
if|if
condition|(
operator|!
name|firstColumn
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
name|columnSeparator
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|print
argument_list|(
name|StringUtil
operator|.
name|escapeString
argument_list|(
name|content
argument_list|)
argument_list|)
expr_stmt|;
name|firstColumn
operator|=
literal|false
expr_stmt|;
block|}
comment|/**    * Finishes the output by flushing the current line and takes care of any    * other cleanup action.    */
DECL|method|finish ()
specifier|public
name|void
name|finish
parameter_list|()
block|{
name|nextLine
argument_list|()
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
comment|/**    * Flushes the current line of output and makes the formatter ready to    * start receiving new column data for a new line (or end-of-file).    * If the current line is empty nothing is done, i.e. consecutive calls    * to this method without intervening calls to {@link #addColumn} will    * be squashed.    */
DECL|method|nextLine ()
specifier|public
name|void
name|nextLine
parameter_list|()
block|{
if|if
condition|(
operator|!
name|firstColumn
condition|)
block|{
name|out
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|firstColumn
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

