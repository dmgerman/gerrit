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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|io
operator|.
name|Files
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
name|NIOFSDirectory
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
name|util
operator|.
name|Version
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
name|zip
operator|.
name|ZipOutputStream
import|;
end_import

begin_class
DECL|class|DocIndexer
specifier|public
class|class
name|DocIndexer
block|{
DECL|field|LUCENE_VERSION
specifier|private
specifier|static
specifier|final
name|Version
name|LUCENE_VERSION
init|=
name|Version
operator|.
name|LUCENE_44
decl_stmt|;
DECL|field|DOC_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|DOC_FIELD
init|=
literal|"doc"
decl_stmt|;
DECL|field|URL_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|URL_FIELD
init|=
literal|"url"
decl_stmt|;
DECL|field|TITLE_FIELD
specifier|private
specifier|static
specifier|final
name|String
name|TITLE_FIELD
init|=
literal|"title"
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-z"
argument_list|,
name|usage
operator|=
literal|"output zip file"
argument_list|)
DECL|field|zipFile
specifier|private
name|String
name|zipFile
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
argument_list|<
name|String
argument_list|>
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
name|File
name|tmp
init|=
name|Files
operator|.
name|createTempDir
argument_list|()
decl_stmt|;
name|NIOFSDirectory
name|directory
init|=
operator|new
name|NIOFSDirectory
argument_list|(
name|tmp
argument_list|)
decl_stmt|;
name|IndexWriterConfig
name|config
init|=
operator|new
name|IndexWriterConfig
argument_list|(
name|LUCENE_VERSION
argument_list|,
operator|new
name|StandardAnalyzer
argument_list|(
name|LUCENE_VERSION
argument_list|,
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
decl_stmt|;
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
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|title
init|=
name|titleReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
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
name|titleReader
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|FileReader
name|reader
init|=
operator|new
name|FileReader
argument_list|(
name|file
argument_list|)
decl_stmt|;
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
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|iwriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|ZipOutputStream
name|zip
init|=
operator|new
name|ZipOutputStream
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|zipFile
argument_list|)
argument_list|)
decl_stmt|;
name|AsciiDoctor
operator|.
name|zipDir
argument_list|(
name|tmp
argument_list|,
literal|""
argument_list|,
name|zip
argument_list|)
expr_stmt|;
name|zip
operator|.
name|close
argument_list|()
expr_stmt|;
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

