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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_class
DECL|class|StringUtil
specifier|public
class|class
name|StringUtil
block|{
comment|/**    * An array of the string representations that should be used in place    * of the non-printable characters in the beginning of the ASCII table    * when escaping a string. The index of each element in the array    * corresponds to its ASCII value, i.e. the string representation of    * ASCII 0 is found in the first element of this array.    */
DECL|field|NON_PRINTABLE_CHARS
specifier|static
name|String
index|[]
name|NON_PRINTABLE_CHARS
init|=
block|{
literal|"\\x00"
block|,
literal|"\\x01"
block|,
literal|"\\x02"
block|,
literal|"\\x03"
block|,
literal|"\\x04"
block|,
literal|"\\x05"
block|,
literal|"\\x06"
block|,
literal|"\\a"
block|,
literal|"\\b"
block|,
literal|"\\t"
block|,
literal|"\\n"
block|,
literal|"\\v"
block|,
literal|"\\f"
block|,
literal|"\\r"
block|,
literal|"\\x0e"
block|,
literal|"\\x0f"
block|,
literal|"\\x10"
block|,
literal|"\\x11"
block|,
literal|"\\x12"
block|,
literal|"\\x13"
block|,
literal|"\\x14"
block|,
literal|"\\x15"
block|,
literal|"\\x16"
block|,
literal|"\\x17"
block|,
literal|"\\x18"
block|,
literal|"\\x19"
block|,
literal|"\\x1a"
block|,
literal|"\\x1b"
block|,
literal|"\\x1c"
block|,
literal|"\\x1d"
block|,
literal|"\\x1e"
block|,
literal|"\\x1f"
block|}
decl_stmt|;
comment|/**    * Escapes the input string so that all non-printable characters    * (0x00-0x1f) are represented as a hex escape (\x00, \x01, ...)    * or as a C-style escape sequence (\a, \b, \t, \n, \v, \f, or \r).    * Backslashes in the input string are doubled (\\).    */
DECL|method|escapeString (final String str)
specifier|public
specifier|static
name|String
name|escapeString
parameter_list|(
specifier|final
name|String
name|str
parameter_list|)
block|{
comment|// Allocate a buffer big enough to cover the case with a string needed
comment|// very excessive escaping without having to reallocate the buffer.
specifier|final
name|StringBuilder
name|result
init|=
operator|new
name|StringBuilder
argument_list|(
literal|3
operator|*
name|str
operator|.
name|length
argument_list|()
argument_list|)
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
name|str
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|char
name|c
init|=
name|str
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|c
operator|<
name|NON_PRINTABLE_CHARS
operator|.
name|length
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|NON_PRINTABLE_CHARS
index|[
name|c
index|]
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|c
operator|==
literal|'\\'
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
literal|"\\\\"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|append
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

