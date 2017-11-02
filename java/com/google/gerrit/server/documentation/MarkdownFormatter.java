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
DECL|package|com.google.gerrit.server.documentation
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|documentation
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|pegdown
operator|.
name|Extensions
operator|.
name|ALL
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|pegdown
operator|.
name|Extensions
operator|.
name|HARDWRAPS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|pegdown
operator|.
name|Extensions
operator|.
name|SUPPRESS_ALL_HTML
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
name|Strings
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringEscapeUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|RawParseUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|TemporaryBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|LinkRenderer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|PegDownProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|ToHtmlSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|ast
operator|.
name|HeaderNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|ast
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|ast
operator|.
name|RootNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|pegdown
operator|.
name|ast
operator|.
name|TextNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MarkdownFormatter
specifier|public
class|class
name|MarkdownFormatter
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MarkdownFormatter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|defaultCss
specifier|private
specifier|static
specifier|final
name|String
name|defaultCss
decl_stmt|;
static|static
block|{
name|AtomicBoolean
name|file
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
name|String
name|src
decl_stmt|;
try|try
block|{
name|src
operator|=
name|readPegdownCss
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load pegdown.css"
argument_list|,
name|err
argument_list|)
expr_stmt|;
name|src
operator|=
literal|""
expr_stmt|;
block|}
name|defaultCss
operator|=
name|file
operator|.
name|get
argument_list|()
condition|?
literal|null
else|:
name|src
expr_stmt|;
block|}
DECL|method|readCSS ()
specifier|private
specifier|static
name|String
name|readCSS
parameter_list|()
block|{
if|if
condition|(
name|defaultCss
operator|!=
literal|null
condition|)
block|{
return|return
name|defaultCss
return|;
block|}
try|try
block|{
return|return
name|readPegdownCss
argument_list|(
operator|new
name|AtomicBoolean
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot load pegdown.css"
argument_list|,
name|err
argument_list|)
expr_stmt|;
return|return
literal|""
return|;
block|}
block|}
DECL|field|suppressHtml
specifier|private
name|boolean
name|suppressHtml
decl_stmt|;
DECL|field|css
specifier|private
name|String
name|css
decl_stmt|;
DECL|method|suppressHtml ()
specifier|public
name|MarkdownFormatter
name|suppressHtml
parameter_list|()
block|{
name|suppressHtml
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|setCss (String css)
specifier|public
name|MarkdownFormatter
name|setCss
parameter_list|(
name|String
name|css
parameter_list|)
block|{
name|this
operator|.
name|css
operator|=
name|StringEscapeUtils
operator|.
name|escapeHtml
argument_list|(
name|css
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|markdownToDocHtml (String md, String charEnc)
specifier|public
name|byte
index|[]
name|markdownToDocHtml
parameter_list|(
name|String
name|md
parameter_list|,
name|String
name|charEnc
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|RootNode
name|root
init|=
name|parseMarkdown
argument_list|(
name|md
argument_list|)
decl_stmt|;
name|String
name|title
init|=
name|findTitle
argument_list|(
name|root
argument_list|)
decl_stmt|;
name|StringBuilder
name|html
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|html
operator|.
name|append
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
literal|"<head>"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|title
argument_list|)
condition|)
block|{
name|html
operator|.
name|append
argument_list|(
literal|"<title>"
argument_list|)
operator|.
name|append
argument_list|(
name|title
argument_list|)
operator|.
name|append
argument_list|(
literal|"</title>"
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|append
argument_list|(
literal|"<style type=\"text/css\">\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|css
operator|!=
literal|null
condition|)
block|{
name|html
operator|.
name|append
argument_list|(
name|css
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|html
operator|.
name|append
argument_list|(
name|readCSS
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|html
operator|.
name|append
argument_list|(
literal|"\n</style>"
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
literal|"</head>"
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
literal|"<body>\n"
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
operator|new
name|ToHtmlSerializer
argument_list|(
operator|new
name|LinkRenderer
argument_list|()
argument_list|)
operator|.
name|toHtml
argument_list|(
name|root
argument_list|)
argument_list|)
expr_stmt|;
name|html
operator|.
name|append
argument_list|(
literal|"\n</body></html>"
argument_list|)
expr_stmt|;
return|return
name|html
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|(
name|charEnc
argument_list|)
return|;
block|}
DECL|method|extractTitleFromMarkdown (byte[] data, String charEnc)
specifier|public
name|String
name|extractTitleFromMarkdown
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|String
name|charEnc
parameter_list|)
block|{
name|String
name|md
init|=
name|RawParseUtils
operator|.
name|decode
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charEnc
argument_list|)
argument_list|,
name|data
argument_list|)
decl_stmt|;
return|return
name|findTitle
argument_list|(
name|parseMarkdown
argument_list|(
name|md
argument_list|)
argument_list|)
return|;
block|}
DECL|method|findTitle (Node root)
specifier|private
name|String
name|findTitle
parameter_list|(
name|Node
name|root
parameter_list|)
block|{
if|if
condition|(
name|root
operator|instanceof
name|HeaderNode
condition|)
block|{
name|HeaderNode
name|h
init|=
operator|(
name|HeaderNode
operator|)
name|root
decl_stmt|;
if|if
condition|(
name|h
operator|.
name|getLevel
argument_list|()
operator|==
literal|1
operator|&&
name|h
operator|.
name|getChildren
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|h
operator|.
name|getChildren
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|Node
name|n
range|:
name|root
operator|.
name|getChildren
argument_list|()
control|)
block|{
if|if
condition|(
name|n
operator|instanceof
name|TextNode
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
operator|(
operator|(
name|TextNode
operator|)
name|n
operator|)
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|b
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
for|for
control|(
name|Node
name|n
range|:
name|root
operator|.
name|getChildren
argument_list|()
control|)
block|{
name|String
name|title
init|=
name|findTitle
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
condition|)
block|{
return|return
name|title
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|parseMarkdown (String md)
specifier|private
name|RootNode
name|parseMarkdown
parameter_list|(
name|String
name|md
parameter_list|)
block|{
name|int
name|options
init|=
name|ALL
operator|&
operator|~
operator|(
name|HARDWRAPS
operator|)
decl_stmt|;
if|if
condition|(
name|suppressHtml
condition|)
block|{
name|options
operator||=
name|SUPPRESS_ALL_HTML
expr_stmt|;
block|}
return|return
operator|new
name|PegDownProcessor
argument_list|(
name|options
argument_list|)
operator|.
name|parseMarkdown
argument_list|(
name|md
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
DECL|method|readPegdownCss (AtomicBoolean file)
specifier|private
specifier|static
name|String
name|readPegdownCss
parameter_list|(
name|AtomicBoolean
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|name
init|=
literal|"pegdown.css"
decl_stmt|;
name|URL
name|url
init|=
name|MarkdownFormatter
operator|.
name|class
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Resource "
operator|+
name|name
argument_list|)
throw|;
block|}
name|file
operator|.
name|set
argument_list|(
literal|"file"
operator|.
name|equals
argument_list|(
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
init|;
name|TemporaryBuffer
operator|.
name|Heap
name|tmp
operator|=
operator|new
name|TemporaryBuffer
operator|.
name|Heap
argument_list|(
literal|128
operator|*
literal|1024
argument_list|)
init|)
block|{
name|tmp
operator|.
name|copy
argument_list|(
name|in
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|tmp
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
