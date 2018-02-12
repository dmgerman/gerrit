begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.mail.send
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|mail
operator|.
name|send
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
name|base
operator|.
name|Strings
operator|.
name|isNullOrEmpty
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
name|base
operator|.
name|Splitter
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
name|common
operator|.
name|Nullable
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
name|Collections
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

begin_class
DECL|class|CommentFormatter
specifier|public
class|class
name|CommentFormatter
block|{
DECL|enum|BlockType
specifier|public
enum|enum
name|BlockType
block|{
DECL|enumConstant|LIST
name|LIST
block|,
DECL|enumConstant|PARAGRAPH
name|PARAGRAPH
block|,
DECL|enumConstant|PRE_FORMATTED
name|PRE_FORMATTED
block|,
DECL|enumConstant|QUOTE
name|QUOTE
block|}
DECL|class|Block
specifier|public
specifier|static
class|class
name|Block
block|{
DECL|field|type
specifier|public
name|BlockType
name|type
decl_stmt|;
DECL|field|text
specifier|public
name|String
name|text
decl_stmt|;
DECL|field|items
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|items
decl_stmt|;
comment|// For the items of list blocks.
DECL|field|quotedBlocks
specifier|public
name|List
argument_list|<
name|Block
argument_list|>
name|quotedBlocks
decl_stmt|;
comment|// For the contents of quote blocks.
block|}
comment|/**    * Take a string of comment text that was written using the wiki-Like format and emit a list of    * blocks that can be rendered to block-level HTML. This method does not escape HTML.    *    *<p>Adapted from the {@code wikify} method found in:    * com.google.gwtexpui.safehtml.client.SafeHtml    *    * @param source The raw, unescaped comment in the Gerrit wiki-like format.    * @return List of block objects, each with unescaped comment content.    */
DECL|method|parse (@ullable String source)
specifier|public
specifier|static
name|List
argument_list|<
name|Block
argument_list|>
name|parse
parameter_list|(
annotation|@
name|Nullable
name|String
name|source
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|source
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|Block
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|p
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|"\n\n"
argument_list|)
operator|.
name|split
argument_list|(
name|source
argument_list|)
control|)
block|{
if|if
condition|(
name|isQuote
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|makeQuote
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isPreFormat
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|makePre
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|isList
argument_list|(
name|p
argument_list|)
condition|)
block|{
name|makeList
argument_list|(
name|p
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|p
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|makeParagraph
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**    * Take a block of comment text that contains a list and potentially paragraphs (but does not    * contain blank lines), generate appropriate block elements and append them to the output list.    *    *<p>In simple cases, this will generate a single list block. For example, on the following    * input.    *    *<p>* Item one. * Item two. * item three.    *    *<p>However, if the list is adjacent to a paragraph, it will need to also generate that    * paragraph. Consider the following input.    *    *<p>A bit of text describing the context of the list: * List item one. * List item two. * Et    * cetera.    *    *<p>In this case, {@code makeList} generates a paragraph block object containing the    * non-bullet-prefixed text, followed by a list block.    *    *<p>Adapted from the {@code wikifyList} method found in:    * com.google.gwtexpui.safehtml.client.SafeHtml    *    * @param p The block containing the list (as well as potential paragraphs).    * @param out The list of blocks to append to.    */
DECL|method|makeList (String p, List<Block> out)
specifier|private
specifier|static
name|void
name|makeList
parameter_list|(
name|String
name|p
parameter_list|,
name|List
argument_list|<
name|Block
argument_list|>
name|out
parameter_list|)
block|{
name|Block
name|block
init|=
literal|null
decl_stmt|;
name|StringBuilder
name|textBuilder
init|=
literal|null
decl_stmt|;
name|boolean
name|inList
init|=
literal|false
decl_stmt|;
name|boolean
name|inParagraph
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|Splitter
operator|.
name|on
argument_list|(
literal|'\n'
argument_list|)
operator|.
name|split
argument_list|(
name|p
argument_list|)
control|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"-"
argument_list|)
operator|||
name|line
operator|.
name|startsWith
argument_list|(
literal|"*"
argument_list|)
condition|)
block|{
comment|// The next line looks like a list item. If not building a list already,
comment|// then create one. Remove the list item marker (* or -) from the line.
if|if
condition|(
operator|!
name|inList
condition|)
block|{
if|if
condition|(
name|inParagraph
condition|)
block|{
comment|// Add the finished paragraph block to the result.
name|inParagraph
operator|=
literal|false
expr_stmt|;
name|block
operator|.
name|text
operator|=
name|textBuilder
operator|.
name|toString
argument_list|()
expr_stmt|;
name|out
operator|.
name|add
argument_list|(
name|block
argument_list|)
expr_stmt|;
block|}
name|inList
operator|=
literal|true
expr_stmt|;
name|block
operator|=
operator|new
name|Block
argument_list|()
expr_stmt|;
name|block
operator|.
name|type
operator|=
name|BlockType
operator|.
name|LIST
expr_stmt|;
name|block
operator|.
name|items
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|line
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|inList
condition|)
block|{
comment|// Otherwise, if a list has not yet been started, but the next line does
comment|// not look like a list item, then add the line to a paragraph block. If
comment|// a paragraph block has not yet been started, then create one.
if|if
condition|(
operator|!
name|inParagraph
condition|)
block|{
name|inParagraph
operator|=
literal|true
expr_stmt|;
name|block
operator|=
operator|new
name|Block
argument_list|()
expr_stmt|;
name|block
operator|.
name|type
operator|=
name|BlockType
operator|.
name|PARAGRAPH
expr_stmt|;
name|textBuilder
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|textBuilder
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|textBuilder
operator|.
name|append
argument_list|(
name|line
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|block
operator|.
name|items
operator|.
name|add
argument_list|(
name|line
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|block
operator|!=
literal|null
condition|)
block|{
name|out
operator|.
name|add
argument_list|(
name|block
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|makeQuote (String p)
specifier|private
specifier|static
name|Block
name|makeQuote
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|String
name|quote
init|=
name|p
operator|.
name|replaceAll
argument_list|(
literal|"\n\\s?>\\s?"
argument_list|,
literal|"\n"
argument_list|)
decl_stmt|;
if|if
condition|(
name|quote
operator|.
name|startsWith
argument_list|(
literal|"> "
argument_list|)
condition|)
block|{
name|quote
operator|=
name|quote
operator|.
name|substring
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|quote
operator|.
name|startsWith
argument_list|(
literal|"> "
argument_list|)
condition|)
block|{
name|quote
operator|=
name|quote
operator|.
name|substring
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|Block
name|block
init|=
operator|new
name|Block
argument_list|()
decl_stmt|;
name|block
operator|.
name|type
operator|=
name|BlockType
operator|.
name|QUOTE
expr_stmt|;
name|block
operator|.
name|quotedBlocks
operator|=
name|CommentFormatter
operator|.
name|parse
argument_list|(
name|quote
argument_list|)
expr_stmt|;
return|return
name|block
return|;
block|}
DECL|method|makePre (String p)
specifier|private
specifier|static
name|Block
name|makePre
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|Block
name|block
init|=
operator|new
name|Block
argument_list|()
decl_stmt|;
name|block
operator|.
name|type
operator|=
name|BlockType
operator|.
name|PRE_FORMATTED
expr_stmt|;
name|block
operator|.
name|text
operator|=
name|p
expr_stmt|;
return|return
name|block
return|;
block|}
DECL|method|makeParagraph (String p)
specifier|private
specifier|static
name|Block
name|makeParagraph
parameter_list|(
name|String
name|p
parameter_list|)
block|{
name|Block
name|block
init|=
operator|new
name|Block
argument_list|()
decl_stmt|;
name|block
operator|.
name|type
operator|=
name|BlockType
operator|.
name|PARAGRAPH
expr_stmt|;
name|block
operator|.
name|text
operator|=
name|p
expr_stmt|;
return|return
name|block
return|;
block|}
DECL|method|isQuote (String p)
specifier|private
specifier|static
name|boolean
name|isQuote
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|startsWith
argument_list|(
literal|"> "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"> "
argument_list|)
return|;
block|}
DECL|method|isPreFormat (String p)
specifier|private
specifier|static
name|boolean
name|isPreFormat
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|startsWith
argument_list|(
literal|" "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"\t"
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n "
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n\t"
argument_list|)
return|;
block|}
DECL|method|isList (String p)
specifier|private
specifier|static
name|boolean
name|isList
parameter_list|(
name|String
name|p
parameter_list|)
block|{
return|return
name|p
operator|.
name|startsWith
argument_list|(
literal|"- "
argument_list|)
operator|||
name|p
operator|.
name|startsWith
argument_list|(
literal|"* "
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n- "
argument_list|)
operator|||
name|p
operator|.
name|contains
argument_list|(
literal|"\n* "
argument_list|)
return|;
block|}
block|}
end_class

end_unit

