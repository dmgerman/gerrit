begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.tools
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|tools
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
name|common
operator|.
name|Version
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
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
name|lib
operator|.
name|Constants
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
name|ByteArrayInputStream
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_comment
comment|/**  * Listing of all client side tools stored on this server.  *<p>  * Clients may download these tools through our file server, as they are  * packaged with our own software releases.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ToolsCatalog
specifier|public
class|class
name|ToolsCatalog
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
name|ToolsCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|toc
specifier|private
specifier|final
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Entry
argument_list|>
name|toc
decl_stmt|;
annotation|@
name|Inject
DECL|method|ToolsCatalog ()
name|ToolsCatalog
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|toc
operator|=
name|readToc
argument_list|()
expr_stmt|;
block|}
comment|/**    * Lookup an entry in the tools catalog.    *    * @param name path of the item, relative to the root of the catalog.    * @return the entry; null if the item is not part of the catalog.    */
DECL|method|get (String name)
specifier|public
name|Entry
name|get
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|toc
operator|.
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|readToc ()
specifier|private
specifier|static
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Entry
argument_list|>
name|readToc
parameter_list|()
throws|throws
name|IOException
block|{
name|SortedMap
argument_list|<
name|String
argument_list|,
name|Entry
argument_list|>
name|toc
init|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Entry
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|read
argument_list|(
literal|"TOC"
argument_list|)
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|br
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|length
argument_list|()
operator|>
literal|0
operator|&&
operator|!
name|line
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
specifier|final
name|Entry
name|e
init|=
operator|new
name|Entry
argument_list|(
name|Entry
operator|.
name|Type
operator|.
name|FILE
argument_list|,
name|line
argument_list|)
decl_stmt|;
name|toc
operator|.
name|put
argument_list|(
name|e
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|List
argument_list|<
name|Entry
argument_list|>
name|all
init|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|(
name|toc
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|all
control|)
block|{
name|String
name|path
init|=
name|dirOf
argument_list|(
name|e
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
name|Entry
name|d
init|=
name|toc
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
operator|new
name|Entry
argument_list|(
name|Entry
operator|.
name|Type
operator|.
name|DIR
argument_list|,
literal|0755
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|toc
operator|.
name|put
argument_list|(
name|d
operator|.
name|getPath
argument_list|()
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
name|d
operator|.
name|children
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|path
operator|=
name|dirOf
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|e
operator|=
name|d
expr_stmt|;
block|}
block|}
specifier|final
name|Entry
name|top
init|=
operator|new
name|Entry
argument_list|(
name|Entry
operator|.
name|Type
operator|.
name|DIR
argument_list|,
literal|0755
argument_list|,
literal|""
argument_list|)
decl_stmt|;
for|for
control|(
name|Entry
name|e
range|:
name|toc
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|dirOf
argument_list|(
name|e
operator|.
name|getPath
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
name|top
operator|.
name|children
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|toc
operator|.
name|put
argument_list|(
name|top
operator|.
name|getPath
argument_list|()
argument_list|,
name|top
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|unmodifiableSortedMap
argument_list|(
name|toc
argument_list|)
return|;
block|}
DECL|method|read (String path)
specifier|private
specifier|static
name|byte
index|[]
name|read
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|name
init|=
literal|"root/"
operator|+
name|path
decl_stmt|;
name|InputStream
name|in
init|=
name|ToolsCatalog
operator|.
name|class
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
specifier|final
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
specifier|final
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|8192
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
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|n
argument_list|)
expr_stmt|;
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
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Cannot read "
operator|+
name|path
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
DECL|method|dirOf (String path)
specifier|private
specifier|static
name|String
name|dirOf
parameter_list|(
name|String
name|path
parameter_list|)
block|{
specifier|final
name|int
name|s
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
return|return
name|s
operator|<
literal|0
condition|?
literal|null
else|:
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
return|;
block|}
comment|/** A file served out of the tools root directory. */
DECL|class|Entry
specifier|public
specifier|static
class|class
name|Entry
block|{
DECL|enum|Type
specifier|public
specifier|static
enum|enum
name|Type
block|{
DECL|enumConstant|DIR
DECL|enumConstant|FILE
name|DIR
block|,
name|FILE
block|;     }
DECL|field|type
specifier|private
specifier|final
name|Type
name|type
decl_stmt|;
DECL|field|mode
specifier|private
specifier|final
name|int
name|mode
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
DECL|field|children
specifier|private
specifier|final
name|List
argument_list|<
name|Entry
argument_list|>
name|children
decl_stmt|;
DECL|method|Entry (Type type, String line)
name|Entry
parameter_list|(
name|Type
name|type
parameter_list|,
name|String
name|line
parameter_list|)
block|{
name|int
name|s
init|=
name|line
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
name|String
name|mode
init|=
name|line
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|line
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
decl_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|mode
argument_list|,
literal|8
argument_list|)
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
if|if
condition|(
name|type
operator|==
name|Type
operator|.
name|FILE
condition|)
block|{
name|this
operator|.
name|children
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|children
operator|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|Entry (Type type, int mode, String path)
name|Entry
parameter_list|(
name|Type
name|type
parameter_list|,
name|int
name|mode
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|mode
operator|=
name|mode
expr_stmt|;
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|children
operator|=
operator|new
name|ArrayList
argument_list|<
name|Entry
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|Type
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/** @return the preferred UNIX file mode, e.g. {@code 0755}. */
DECL|method|getMode ()
specifier|public
name|int
name|getMode
parameter_list|()
block|{
return|return
name|mode
return|;
block|}
comment|/** @return path of the entry, relative to the catalog root. */
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/** @return name of the entry, within its parent directory. */
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
specifier|final
name|int
name|s
init|=
name|path
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
return|return
name|s
operator|<
literal|0
condition|?
name|path
else|:
name|path
operator|.
name|substring
argument_list|(
name|s
operator|+
literal|1
argument_list|)
return|;
block|}
comment|/** @return collection of entries below this one, if this is a directory. */
DECL|method|getChildren ()
specifier|public
name|List
argument_list|<
name|Entry
argument_list|>
name|getChildren
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|children
argument_list|)
return|;
block|}
comment|/** @return a copy of the file's contents. */
DECL|method|getBytes ()
specifier|public
name|byte
index|[]
name|getBytes
parameter_list|()
block|{
name|byte
index|[]
name|data
init|=
name|read
argument_list|(
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isScript
argument_list|(
name|data
argument_list|)
condition|)
block|{
comment|// Embed Gerrit's version number into the top of the script.
comment|//
specifier|final
name|String
name|version
init|=
name|Version
operator|.
name|getVersion
argument_list|()
decl_stmt|;
specifier|final
name|int
name|lf
init|=
name|RawParseUtils
operator|.
name|nextLF
argument_list|(
name|data
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|version
operator|!=
literal|null
operator|&&
name|lf
operator|<
name|data
operator|.
name|length
condition|)
block|{
name|byte
index|[]
name|versionHeader
init|=
name|Constants
operator|.
name|encode
argument_list|(
literal|"# From Gerrit Code Review "
operator|+
name|version
operator|+
literal|"\n"
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|data
argument_list|,
literal|0
argument_list|,
name|lf
argument_list|)
expr_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|versionHeader
argument_list|,
literal|0
argument_list|,
name|versionHeader
operator|.
name|length
argument_list|)
expr_stmt|;
name|buf
operator|.
name|write
argument_list|(
name|data
argument_list|,
name|lf
argument_list|,
name|data
operator|.
name|length
operator|-
name|lf
argument_list|)
expr_stmt|;
name|data
operator|=
name|buf
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|data
return|;
block|}
DECL|method|isScript (byte[] data)
specifier|private
name|boolean
name|isScript
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|data
operator|!=
literal|null
operator|&&
name|data
operator|.
name|length
operator|>
literal|3
comment|//
operator|&&
name|data
index|[
literal|0
index|]
operator|==
literal|'#'
comment|//
operator|&&
name|data
index|[
literal|1
index|]
operator|==
literal|'!'
comment|//
operator|&&
name|data
index|[
literal|2
index|]
operator|==
literal|'/'
return|;
block|}
block|}
block|}
end_class

end_unit

