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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|documentation
operator|.
name|Constants
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|InputStreamReader
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
name|ArrayList
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
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|ZipEntry
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
name|ZipOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|standard
operator|.
name|StandardAnalyzer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|analysis
operator|.
name|util
operator|.
name|CharArraySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|StringField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|TextField
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriterConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|IndexWriterConfig
operator|.
name|OpenMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|IndexInput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|store
operator|.
name|RAMDirectory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Argument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|CmdLineParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_class
DECL|class|DocIndexer
specifier|public
class|class
name|DocIndexer
block|{
DECL|field|SECTION_HEADER
specifier|private
specifier|static
specifier|final
name|Pattern
name|SECTION_HEADER
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^=+ (.*)"
argument_list|)
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-o"
argument_list|,
name|usage
operator|=
literal|"output JAR file"
argument_list|)
DECL|field|outFile
specifier|private
name|String
name|outFile
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--prefix"
argument_list|,
name|usage
operator|=
literal|"prefix for the html filepath"
argument_list|)
DECL|field|prefix
specifier|private
name|String
name|prefix
init|=
literal|""
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--in-ext"
argument_list|,
name|usage
operator|=
literal|"extension for input files"
argument_list|)
DECL|field|inExt
specifier|private
name|String
name|inExt
init|=
literal|".txt"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--out-ext"
argument_list|,
name|usage
operator|=
literal|"extension for output files"
argument_list|)
DECL|field|outExt
specifier|private
name|String
name|outExt
init|=
literal|".html"
decl_stmt|;
annotation|@
name|Argument
argument_list|(
name|usage
operator|=
literal|"input files"
argument_list|)
DECL|field|inputFiles
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|inputFiles
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|invoke (String... parameters)
specifier|private
name|void
name|invoke
parameter_list|(
name|String
modifier|...
name|parameters
parameter_list|)
throws|throws
name|IOException
block|{
name|CmdLineParser
name|parser
init|=
operator|new
name|CmdLineParser
argument_list|(
name|this
argument_list|)
decl_stmt|;
try|try
block|{
name|parser
operator|.
name|parseArgument
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|inputFiles
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CmdLineException
argument_list|(
name|parser
argument_list|,
literal|"FAILED: input file missing"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|CmdLineException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|parser
operator|.
name|printUsage
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
init|(
name|JarOutputStream
name|jar
init|=
operator|new
name|JarOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outFile
argument_list|)
argument_list|)
init|)
block|{
name|byte
index|[]
name|compressedIndex
init|=
name|zip
argument_list|(
name|index
argument_list|()
argument_list|)
decl_stmt|;
name|JarEntry
name|entry
init|=
operator|new
name|JarEntry
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s/%s"
argument_list|,
name|Constants
operator|.
name|PACKAGE
argument_list|,
name|Constants
operator|.
name|INDEX_ZIP
argument_list|)
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setSize
argument_list|(
name|compressedIndex
operator|.
name|length
argument_list|)
expr_stmt|;
name|jar
operator|.
name|putNextEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|jar
operator|.
name|write
argument_list|(
name|compressedIndex
argument_list|)
expr_stmt|;
name|jar
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|index ()
specifier|private
name|RAMDirectory
name|index
parameter_list|()
throws|throws
name|IOException
throws|,
name|UnsupportedEncodingException
throws|,
name|FileNotFoundException
block|{
name|RAMDirectory
name|directory
init|=
operator|new
name|RAMDirectory
argument_list|()
decl_stmt|;
name|IndexWriterConfig
name|config
init|=
operator|new
name|IndexWriterConfig
argument_list|(
operator|new
name|StandardAnalyzer
argument_list|(
name|CharArraySet
operator|.
name|EMPTY_SET
argument_list|)
argument_list|)
decl_stmt|;
name|config
operator|.
name|setOpenMode
argument_list|(
name|OpenMode
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|config
operator|.
name|setCommitOnClose
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
init|(
name|IndexWriter
name|iwriter
init|=
operator|new
name|IndexWriter
argument_list|(
name|directory
argument_list|,
name|config
argument_list|)
init|)
block|{
for|for
control|(
name|String
name|inputFile
range|:
name|inputFiles
control|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|inputFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
continue|continue;
block|}
name|String
name|title
decl_stmt|;
try|try
init|(
name|BufferedReader
name|titleReader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|title
operator|=
name|titleReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
if|if
condition|(
name|title
operator|!=
literal|null
operator|&&
name|title
operator|.
name|startsWith
argument_list|(
literal|"[["
argument_list|)
condition|)
block|{
comment|// Generally the first line of the txt is the title. In a few cases the
comment|// first line is a "[[tag]]" and the second line is the title.
name|title
operator|=
name|titleReader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
block|}
name|Matcher
name|matcher
init|=
name|SECTION_HEADER
operator|.
name|matcher
argument_list|(
name|title
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
name|title
operator|=
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|outputFile
init|=
name|AsciiDoctor
operator|.
name|mapInFileToOutFile
argument_list|(
name|inputFile
argument_list|,
name|inExt
argument_list|,
name|outExt
argument_list|)
decl_stmt|;
try|try
init|(
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
init|)
block|{
name|Document
name|doc
init|=
operator|new
name|Document
argument_list|()
decl_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|TextField
argument_list|(
name|Constants
operator|.
name|DOC_FIELD
argument_list|,
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|StringField
argument_list|(
name|Constants
operator|.
name|URL_FIELD
argument_list|,
name|prefix
operator|+
name|outputFile
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|)
argument_list|)
expr_stmt|;
name|doc
operator|.
name|add
argument_list|(
operator|new
name|TextField
argument_list|(
name|Constants
operator|.
name|TITLE_FIELD
argument_list|,
name|title
argument_list|,
name|Field
operator|.
name|Store
operator|.
name|YES
argument_list|)
argument_list|)
expr_stmt|;
name|iwriter
operator|.
name|addDocument
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|directory
return|;
block|}
DECL|method|zip (RAMDirectory dir)
specifier|private
name|byte
index|[]
name|zip
parameter_list|(
name|RAMDirectory
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|buf
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
init|(
name|ZipOutputStream
name|zip
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|buf
argument_list|)
init|)
block|{
for|for
control|(
name|String
name|name
range|:
name|dir
operator|.
name|listAll
argument_list|()
control|)
block|{
try|try
init|(
name|IndexInput
name|in
init|=
name|dir
operator|.
name|openInput
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
init|)
block|{
name|int
name|len
init|=
operator|(
name|int
operator|)
name|in
operator|.
name|length
argument_list|()
decl_stmt|;
name|byte
index|[]
name|tmp
init|=
operator|new
name|byte
index|[
name|len
index|]
decl_stmt|;
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|entry
operator|.
name|setSize
argument_list|(
name|len
argument_list|)
expr_stmt|;
name|in
operator|.
name|readBytes
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|zip
operator|.
name|putNextEntry
argument_list|(
name|entry
argument_list|)
expr_stmt|;
name|zip
operator|.
name|write
argument_list|(
name|tmp
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
name|zip
operator|.
name|closeEntry
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|buf
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
operator|new
name|DocIndexer
argument_list|()
operator|.
name|invoke
argument_list|(
name|args
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

