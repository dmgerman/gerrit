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
DECL|package|com.google.gerrit.pgm
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
operator|.
name|AbstractProgram
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|schema
operator|.
name|java
operator|.
name|JavaSchemaModel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
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
name|nio
operator|.
name|ByteBuffer
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|LockFile
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
name|IO
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
DECL|class|ProtoGen
specifier|public
class|class
name|ProtoGen
extends|extends
name|AbstractProgram
block|{
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--output"
argument_list|,
name|aliases
operator|=
block|{
literal|"-o"
block|}
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"FILE"
argument_list|,
name|usage
operator|=
literal|"File to write .proto into"
argument_list|)
DECL|field|file
specifier|private
name|File
name|file
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|int
name|run
parameter_list|()
throws|throws
name|Exception
block|{
name|LockFile
name|lock
init|=
operator|new
name|LockFile
argument_list|(
name|file
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|lock
operator|.
name|lock
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Cannot lock "
operator|+
name|file
argument_list|)
throw|;
block|}
try|try
block|{
name|JavaSchemaModel
name|jsm
init|=
operator|new
name|JavaSchemaModel
argument_list|(
name|ReviewDb
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
init|(
name|OutputStream
name|o
init|=
name|lock
operator|.
name|getOutputStream
argument_list|()
init|;
name|PrintWriter
name|out
operator|=
operator|new
name|PrintWriter
argument_list|(
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|o
argument_list|,
name|UTF_8
argument_list|)
argument_list|)
argument_list|)
init|)
block|{
name|String
name|header
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"ProtoGenHeader.txt"
argument_list|)
init|)
block|{
name|ByteBuffer
name|buf
init|=
name|IO
operator|.
name|readWholeStream
argument_list|(
name|in
argument_list|,
literal|1024
argument_list|)
decl_stmt|;
name|int
name|ptr
init|=
name|buf
operator|.
name|arrayOffset
argument_list|()
operator|+
name|buf
operator|.
name|position
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|buf
operator|.
name|remaining
argument_list|()
decl_stmt|;
name|header
operator|=
operator|new
name|String
argument_list|(
name|buf
operator|.
name|array
argument_list|()
argument_list|,
name|ptr
argument_list|,
name|len
argument_list|,
name|UTF_8
argument_list|)
expr_stmt|;
block|}
name|String
name|version
init|=
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Version
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|header
operator|.
name|replace
argument_list|(
literal|"@@VERSION@@"
argument_list|,
name|version
argument_list|)
argument_list|)
expr_stmt|;
name|jsm
operator|.
name|generateProto
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|lock
operator|.
name|commit
argument_list|()
condition|)
block|{
throw|throw
name|die
argument_list|(
literal|"Could not write to "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Created "
operator|+
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit
