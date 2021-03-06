begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git.meta
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|meta
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
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
name|server
operator|.
name|git
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/** (De)serializer for tab-delimited text files. */
end_comment

begin_class
DECL|class|TabFile
specifier|public
class|class
name|TabFile
block|{
annotation|@
name|FunctionalInterface
DECL|interface|Parser
specifier|public
interface|interface
name|Parser
block|{
DECL|method|parse (String str)
name|String
name|parse
parameter_list|(
name|String
name|str
parameter_list|)
function_decl|;
block|}
DECL|field|TRIM
specifier|public
specifier|static
name|Parser
name|TRIM
init|=
name|String
operator|::
name|trim
decl_stmt|;
DECL|class|Row
specifier|protected
specifier|static
class|class
name|Row
block|{
DECL|field|left
specifier|public
name|String
name|left
decl_stmt|;
DECL|field|right
specifier|public
name|String
name|right
decl_stmt|;
DECL|method|Row (String left, String right)
specifier|public
name|Row
parameter_list|(
name|String
name|left
parameter_list|,
name|String
name|right
parameter_list|)
block|{
name|this
operator|.
name|left
operator|=
name|left
expr_stmt|;
name|this
operator|.
name|right
operator|=
name|right
expr_stmt|;
block|}
block|}
DECL|method|parse ( String text, String filename, Parser left, Parser right, ValidationError.Sink errors)
specifier|protected
specifier|static
name|List
argument_list|<
name|Row
argument_list|>
name|parse
parameter_list|(
name|String
name|text
parameter_list|,
name|String
name|filename
parameter_list|,
name|Parser
name|left
parameter_list|,
name|Parser
name|right
parameter_list|,
name|ValidationError
operator|.
name|Sink
name|errors
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|StringReader
argument_list|(
name|text
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s
decl_stmt|;
for|for
control|(
name|int
name|lineNumber
init|=
literal|1
init|;
operator|(
name|s
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|;
name|lineNumber
operator|++
control|)
block|{
if|if
condition|(
name|s
operator|.
name|isEmpty
argument_list|()
operator|||
name|s
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|int
name|tab
init|=
name|s
operator|.
name|indexOf
argument_list|(
literal|'\t'
argument_list|)
decl_stmt|;
if|if
condition|(
name|tab
operator|<
literal|0
condition|)
block|{
name|errors
operator|.
name|error
argument_list|(
operator|new
name|ValidationError
argument_list|(
name|filename
argument_list|,
name|lineNumber
argument_list|,
literal|"missing tab delimiter"
argument_list|)
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|Row
name|row
init|=
operator|new
name|Row
argument_list|(
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|tab
argument_list|)
argument_list|,
name|s
operator|.
name|substring
argument_list|(
name|tab
operator|+
literal|1
argument_list|)
argument_list|)
decl_stmt|;
name|rows
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
if|if
condition|(
name|left
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|left
operator|=
name|left
operator|.
name|parse
argument_list|(
name|row
operator|.
name|left
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|right
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|right
operator|=
name|right
operator|.
name|parse
argument_list|(
name|row
operator|.
name|right
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|rows
return|;
block|}
DECL|method|toMap (List<Row> rows)
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toMap
parameter_list|(
name|List
argument_list|<
name|Row
argument_list|>
name|rows
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|rows
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|row
operator|.
name|left
argument_list|,
name|row
operator|.
name|right
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
DECL|method|asText (String left, String right, Map<String, String> entries)
specifier|protected
specifier|static
name|String
name|asText
parameter_list|(
name|String
name|left
parameter_list|,
name|String
name|right
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entries
parameter_list|)
block|{
if|if
condition|(
name|entries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Row
argument_list|>
name|rows
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|entries
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|sort
argument_list|(
name|entries
operator|.
name|keySet
argument_list|()
argument_list|)
control|)
block|{
name|rows
operator|.
name|add
argument_list|(
operator|new
name|Row
argument_list|(
name|key
argument_list|,
name|entries
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|asText
argument_list|(
name|left
argument_list|,
name|right
argument_list|,
name|rows
argument_list|)
return|;
block|}
DECL|method|asText (String left, String right, List<Row> rows)
specifier|protected
specifier|static
name|String
name|asText
parameter_list|(
name|String
name|left
parameter_list|,
name|String
name|right
parameter_list|,
name|List
argument_list|<
name|Row
argument_list|>
name|rows
parameter_list|)
block|{
if|if
condition|(
name|rows
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|left
operator|=
literal|"# "
operator|+
name|left
expr_stmt|;
name|int
name|leftLen
init|=
name|left
operator|.
name|length
argument_list|()
decl_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|leftLen
operator|=
name|Math
operator|.
name|max
argument_list|(
name|leftLen
argument_list|,
name|row
operator|.
name|left
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|pad
argument_list|(
name|leftLen
argument_list|,
name|left
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\t'
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|right
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'#'
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
for|for
control|(
name|Row
name|row
range|:
name|rows
control|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|pad
argument_list|(
name|leftLen
argument_list|,
name|row
operator|.
name|left
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\t'
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|row
operator|.
name|right
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|sort (Collection<T> m)
specifier|protected
specifier|static
parameter_list|<
name|T
extends|extends
name|Comparable
argument_list|<
name|?
super|super
name|T
argument_list|>
parameter_list|>
name|ImmutableList
argument_list|<
name|T
argument_list|>
name|sort
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|m
parameter_list|)
block|{
return|return
name|m
operator|.
name|stream
argument_list|()
operator|.
name|sorted
argument_list|()
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|pad (int len, String src)
specifier|protected
specifier|static
name|String
name|pad
parameter_list|(
name|int
name|len
parameter_list|,
name|String
name|src
parameter_list|)
block|{
if|if
condition|(
name|len
operator|<=
name|src
operator|.
name|length
argument_list|()
condition|)
block|{
return|return
name|src
return|;
block|}
name|StringBuilder
name|r
init|=
operator|new
name|StringBuilder
argument_list|(
name|len
argument_list|)
decl_stmt|;
name|r
operator|.
name|append
argument_list|(
name|src
argument_list|)
expr_stmt|;
while|while
condition|(
name|r
operator|.
name|length
argument_list|()
operator|<
name|len
condition|)
block|{
name|r
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
return|return
name|r
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

