begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
package|;
end_package

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
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_class
DECL|class|TempFileUtil
specifier|public
class|class
name|TempFileUtil
block|{
DECL|field|testCount
specifier|private
specifier|static
name|int
name|testCount
decl_stmt|;
DECL|field|df
specifier|private
specifier|static
name|DateFormat
name|df
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMddHHmmss"
argument_list|)
decl_stmt|;
DECL|field|temp
specifier|private
specifier|static
specifier|final
name|File
name|temp
init|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
literal|"target"
argument_list|)
argument_list|,
literal|"temp"
argument_list|)
decl_stmt|;
DECL|method|createUniqueTestFolderName ()
specifier|private
specifier|static
name|String
name|createUniqueTestFolderName
parameter_list|()
block|{
return|return
literal|"test_"
operator|+
operator|(
name|df
operator|.
name|format
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
operator|+
literal|"_"
operator|+
operator|(
name|testCount
operator|++
operator|)
operator|)
return|;
block|}
DECL|method|createTempDirectory ()
specifier|public
specifier|static
name|File
name|createTempDirectory
parameter_list|()
block|{
specifier|final
name|String
name|name
init|=
name|createUniqueTestFolderName
argument_list|()
decl_stmt|;
specifier|final
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
name|temp
argument_list|,
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|directory
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"failed to create folder '"
operator|+
name|directory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|directory
return|;
block|}
block|}
end_class

end_unit

