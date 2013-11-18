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
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
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
name|io
operator|.
name|ByteStreams
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|PythonTestCaller
specifier|public
class|class
name|PythonTestCaller
block|{
annotation|@
name|Test
DECL|method|resolveUrl ()
specifier|public
name|void
name|resolveUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|PythonTestCaller
operator|.
name|pythonUnit
argument_list|(
literal|"tools"
argument_list|,
literal|"util_test"
argument_list|)
expr_stmt|;
block|}
DECL|method|pythonUnit (String d, String sut)
specifier|private
specifier|static
name|void
name|pythonUnit
parameter_list|(
name|String
name|d
parameter_list|,
name|String
name|sut
parameter_list|)
throws|throws
name|Exception
block|{
name|ProcessBuilder
name|b
init|=
operator|new
name|ProcessBuilder
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|' '
argument_list|)
operator|.
name|splitToList
argument_list|(
literal|"python -m unittest "
operator|+
name|sut
argument_list|)
argument_list|)
operator|.
name|directory
argument_list|(
operator|new
name|File
argument_list|(
name|d
argument_list|)
argument_list|)
operator|.
name|redirectErrorStream
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|Process
name|p
init|=
literal|null
decl_stmt|;
name|InputStream
name|i
init|=
literal|null
decl_stmt|;
name|byte
index|[]
name|out
decl_stmt|;
try|try
block|{
name|p
operator|=
name|b
operator|.
name|start
argument_list|()
expr_stmt|;
name|i
operator|=
name|p
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
name|out
operator|=
name|ByteStreams
operator|.
name|toByteArray
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|p
operator|!=
literal|null
condition|)
block|{
name|p
operator|.
name|getOutputStream
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|i
operator|!=
literal|null
condition|)
block|{
name|i
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|int
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|p
operator|.
name|waitFor
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"interrupted waiting for process"
argument_list|)
throw|;
block|}
name|String
name|err
init|=
operator|new
name|String
argument_list|(
name|out
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
name|err
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|err
argument_list|,
name|value
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

