begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
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
name|InputStreamReader
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
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathFactory
import|;
end_import

begin_comment
comment|/** Utility functions to deal with HTML using W3C DOM operations. */
end_comment

begin_class
DECL|class|HtmlDomUtil
specifier|public
class|class
name|HtmlDomUtil
block|{
comment|/** Standard character encoding we prefer (UTF-8). */
DECL|field|ENC
specifier|public
specifier|static
specifier|final
name|String
name|ENC
init|=
literal|"UTF-8"
decl_stmt|;
comment|/** DOCTYPE for a standards mode HTML document. */
DECL|field|HTML_STRICT
specifier|public
specifier|static
specifier|final
name|String
name|HTML_STRICT
init|=
literal|"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/REC-html40/strict.dtd"
decl_stmt|;
comment|/** Convert a document to a UTF-8 byte sequence. */
DECL|method|toUTF8 (final Document hostDoc)
specifier|public
specifier|static
name|byte
index|[]
name|toUTF8
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toString
argument_list|(
name|hostDoc
argument_list|)
operator|.
name|getBytes
argument_list|(
name|ENC
argument_list|)
return|;
block|}
comment|/** Compress the document. */
DECL|method|compress (final byte[] raw)
specifier|public
specifier|static
name|byte
index|[]
name|compress
parameter_list|(
specifier|final
name|byte
index|[]
name|raw
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
specifier|final
name|GZIPOutputStream
name|gz
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|gz
operator|.
name|write
argument_list|(
name|raw
argument_list|)
expr_stmt|;
name|gz
operator|.
name|finish
argument_list|()
expr_stmt|;
name|gz
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
comment|/** Convert a document to a String, assuming later encoding to UTF-8. */
DECL|method|toString (final Document hostDoc)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
specifier|final
name|Document
name|hostDoc
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|StringWriter
name|out
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
specifier|final
name|DOMSource
name|domSource
init|=
operator|new
name|DOMSource
argument_list|(
name|hostDoc
argument_list|)
decl_stmt|;
specifier|final
name|StreamResult
name|streamResult
init|=
operator|new
name|StreamResult
argument_list|(
name|out
argument_list|)
decl_stmt|;
specifier|final
name|TransformerFactory
name|tf
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|final
name|Transformer
name|serializer
init|=
name|tf
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
name|ENC
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|METHOD
argument_list|,
literal|"html"
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|DOCTYPE_PUBLIC
argument_list|,
name|HtmlDomUtil
operator|.
name|HTML_STRICT
argument_list|)
expr_stmt|;
name|serializer
operator|.
name|transform
argument_list|(
name|domSource
argument_list|,
name|streamResult
argument_list|)
expr_stmt|;
return|return
name|out
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|e
parameter_list|)
block|{
specifier|final
name|IOException
name|r
init|=
operator|new
name|IOException
argument_list|(
literal|"Error transforming page"
argument_list|)
decl_stmt|;
name|r
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|r
throw|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
specifier|final
name|IOException
name|r
init|=
operator|new
name|IOException
argument_list|(
literal|"Error transforming page"
argument_list|)
decl_stmt|;
name|r
operator|.
name|initCause
argument_list|(
name|e
argument_list|)
expr_stmt|;
throw|throw
name|r
throw|;
block|}
block|}
comment|/** Find an element by its "id" attribute; null if no element is found. */
DECL|method|find (final Node parent, final String name)
specifier|public
specifier|static
name|Element
name|find
parameter_list|(
specifier|final
name|Node
name|parent
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
block|{
specifier|final
name|NodeList
name|list
init|=
name|parent
operator|.
name|getChildNodes
argument_list|()
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
name|list
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|Node
name|n
init|=
name|list
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|n
operator|instanceof
name|Element
condition|)
block|{
specifier|final
name|Element
name|e
init|=
operator|(
name|Element
operator|)
name|n
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|e
return|;
block|}
block|}
specifier|final
name|Element
name|r
init|=
name|find
argument_list|(
name|n
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|!=
literal|null
condition|)
block|{
return|return
name|r
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** Append an HTML&lt;input type="hidden"&gt; to the form. */
DECL|method|addHidden (final Element form, final String name, final String value)
specifier|public
specifier|static
name|void
name|addHidden
parameter_list|(
specifier|final
name|Element
name|form
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|String
name|value
parameter_list|)
block|{
specifier|final
name|Element
name|in
init|=
name|form
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElement
argument_list|(
literal|"input"
argument_list|)
decl_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"type"
argument_list|,
literal|"hidden"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|in
operator|.
name|setAttribute
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|form
operator|.
name|appendChild
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
comment|/** Clone a document so it can be safely modified on a per-request basis. */
DECL|method|clone (final Document doc)
specifier|public
specifier|static
name|Document
name|clone
parameter_list|(
specifier|final
name|Document
name|doc
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|Document
name|d
decl_stmt|;
try|try
block|{
name|d
operator|=
name|newBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot clone document"
argument_list|)
throw|;
block|}
specifier|final
name|Node
name|n
init|=
name|d
operator|.
name|importNode
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|d
operator|.
name|appendChild
argument_list|(
name|n
argument_list|)
expr_stmt|;
return|return
name|d
return|;
block|}
comment|/** Parse an XHTML file from our CLASSPATH and return the instance. */
DECL|method|parseFile (final Class<?> context, final String name)
specifier|public
specifier|static
name|Document
name|parseFile
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|context
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|InputStream
name|in
decl_stmt|;
name|in
operator|=
name|context
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
try|try
block|{
try|try
block|{
specifier|final
name|Document
name|doc
init|=
name|newBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|compact
argument_list|(
name|doc
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|compact (final Document doc)
specifier|private
specifier|static
name|void
name|compact
parameter_list|(
specifier|final
name|Document
name|doc
parameter_list|)
block|{
try|try
block|{
specifier|final
name|String
name|expr
init|=
literal|"//text()[normalize-space(.) = '']"
decl_stmt|;
specifier|final
name|XPathFactory
name|xp
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|final
name|XPathExpression
name|e
init|=
name|xp
operator|.
name|newXPath
argument_list|()
operator|.
name|compile
argument_list|(
name|expr
argument_list|)
decl_stmt|;
name|NodeList
name|empty
init|=
operator|(
name|NodeList
operator|)
name|e
operator|.
name|evaluate
argument_list|(
name|doc
argument_list|,
name|XPathConstants
operator|.
name|NODESET
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
name|empty
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|node
init|=
name|empty
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|node
operator|.
name|getParentNode
argument_list|()
operator|.
name|removeChild
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XPathExpressionException
name|e
parameter_list|)
block|{
comment|// Don't do the whitespace removal.
block|}
block|}
comment|/** Read a Read a UTF-8 text file from our CLASSPATH and return it. */
DECL|method|readFile (final Class<?> context, final String name)
specifier|public
specifier|static
name|String
name|readFile
parameter_list|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|context
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|InputStream
name|in
init|=
name|context
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|asString
argument_list|(
name|in
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** Parse an XHTML file from the local drive and return the instance. */
DECL|method|parseFile (final File path)
specifier|public
specifier|static
name|Document
name|parseFile
parameter_list|(
specifier|final
name|File
name|path
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
specifier|final
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
try|try
block|{
specifier|final
name|Document
name|doc
init|=
name|newBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|compact
argument_list|(
name|doc
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/** Read a UTF-8 text file from the local drive. */
DECL|method|readFile (final File parentDir, final String name)
specifier|public
specifier|static
name|String
name|readFile
parameter_list|(
specifier|final
name|File
name|parentDir
parameter_list|,
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|parentDir
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
specifier|final
name|File
name|path
init|=
operator|new
name|File
argument_list|(
name|parentDir
argument_list|,
name|name
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|asString
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading "
operator|+
name|path
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|asString (final InputStream in)
specifier|private
specifier|static
name|String
name|asString
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|IOException
block|{
try|try
block|{
specifier|final
name|StringBuilder
name|w
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|InputStreamReader
name|r
init|=
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|ENC
argument_list|)
decl_stmt|;
specifier|final
name|char
index|[]
name|buf
init|=
operator|new
name|char
index|[
literal|512
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
operator|(
name|n
operator|=
name|r
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|w
operator|.
name|append
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|newBuilder ()
specifier|private
specifier|static
name|DocumentBuilder
name|newBuilder
parameter_list|()
throws|throws
name|ParserConfigurationException
block|{
specifier|final
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setExpandEntityReferences
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setIgnoringComments
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setCoalescing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|DocumentBuilder
name|parser
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|parser
return|;
block|}
block|}
end_class

end_unit

