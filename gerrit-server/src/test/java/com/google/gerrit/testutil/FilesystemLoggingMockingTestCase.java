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

begin_package
DECL|package|com.google.gerrit.testutil
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testutil
package|;
end_package

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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
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
name|FileUtils
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
name|util
operator|.
name|Collection
import|;
end_import

begin_class
DECL|class|FilesystemLoggingMockingTestCase
specifier|public
specifier|abstract
class|class
name|FilesystemLoggingMockingTestCase
extends|extends
name|LoggingMockingTestCase
block|{
DECL|field|toCleanup
specifier|private
name|Collection
argument_list|<
name|File
argument_list|>
name|toCleanup
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
comment|/**    * Asserts that a given file exists.    *    * @param file The file to test.    */
DECL|method|assertExists (File file)
specifier|protected
name|void
name|assertExists
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"File '"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' does not exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that a given file does not exist.    *    * @param file The file to test.    */
DECL|method|assertDoesNotExist (File file)
specifier|protected
name|void
name|assertDoesNotExist
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|assertFalse
argument_list|(
literal|"File '"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that a given file exists and is a directory.    *    * @param file The file to test.    */
DECL|method|assertDirectory (File file)
specifier|protected
name|void
name|assertDirectory
parameter_list|(
name|File
name|file
parameter_list|)
block|{
comment|// Although isDirectory includes checking for existence, we nevertheless
comment|// explicitly check for existence, to get more appropriate error messages
name|assertExists
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"File '"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"' is not a directory"
argument_list|,
name|file
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that creating a directory from the given file worked    *    * @param file The directory to create    */
DECL|method|assertMkdirs (File file)
specifier|protected
name|void
name|assertMkdirs
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"Could not create directory '"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'"
argument_list|,
name|file
operator|.
name|mkdirs
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that creating a directory from the specified file worked    *    * @param parent The parent of the directory to create    * @param name The name of the directoryto create (relative to {@code parent}    * @return The created directory    */
DECL|method|assertMkdirs (File parent, String name)
specifier|protected
name|File
name|assertMkdirs
parameter_list|(
name|File
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|assertMkdirs
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|file
return|;
block|}
comment|/**    * Asserts that creating a file worked    *    * @param file The file to create    */
DECL|method|assertCreateFile (File file)
specifier|protected
name|void
name|assertCreateFile
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|assertTrue
argument_list|(
literal|"Could not create file '"
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'"
argument_list|,
name|file
operator|.
name|createNewFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Asserts that creating a file worked    *    * @param parent The parent of the file to create    * @param name The name of the file to create (relative to {@code parent}    * @return The created file    */
DECL|method|assertCreateFile (File parent, String name)
specifier|protected
name|File
name|assertCreateFile
parameter_list|(
name|File
name|parent
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|assertCreateFile
argument_list|(
name|file
argument_list|)
expr_stmt|;
return|return
name|file
return|;
block|}
comment|/**    * Creates a file in the system's default folder for temporary files.    *    * The file/directory automatically gets removed during tearDown.    *    * The name of the created file begins with 'gerrit_test_', and is located    * in the system's default folder for temporary files.    *    * @param suffix Trailing part of the file name.    * @return The temporary file.    * @throws IOException If a file could not be created.    */
DECL|method|createTempFile (String suffix)
specifier|private
name|File
name|createTempFile
parameter_list|(
name|String
name|suffix
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|prefix
init|=
literal|"gerrit_test_"
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|prefix
operator|+=
name|getName
argument_list|()
operator|+
literal|"_"
expr_stmt|;
block|}
name|File
name|tmp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
name|prefix
argument_list|,
name|suffix
argument_list|)
decl_stmt|;
name|toCleanup
operator|.
name|add
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
return|return
name|tmp
return|;
block|}
comment|/**    * Creates a file in the system's default folder for temporary files.    *    * The file/directory automatically gets removed during tearDown.    *    * The name of the created file begins with 'gerrit_test_', and is located    * in the system's default folder for temporary files.    *    * @return The temporary file.    * @throws IOException If a file could not be created.    */
DECL|method|createTempFile ()
specifier|protected
name|File
name|createTempFile
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|createTempFile
argument_list|(
literal|""
argument_list|)
return|;
block|}
comment|/**    * Creates a directory in the system's default folder for temporary files.    *    * The directory (and all it's contained files/directory) automatically get    * removed during tearDown.    *    * The name of the created directory begins with 'gerrit_test_', and is be    * located in the system's default folder for temporary files.    *    * @return The temporary directory.    * @throws IOException If a file could not be created.    */
DECL|method|createTempDir ()
specifier|protected
name|File
name|createTempDir
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|tmp
init|=
name|createTempFile
argument_list|(
literal|".dir"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmp
operator|.
name|delete
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot delete temporary file '"
operator|+
name|tmp
operator|.
name|getPath
argument_list|()
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|tmp
operator|.
name|mkdir
argument_list|()
expr_stmt|;
return|return
name|tmp
return|;
block|}
DECL|method|cleanupCreatedFiles ()
specifier|private
name|void
name|cleanupCreatedFiles
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
name|File
name|file
range|:
name|toCleanup
control|)
block|{
name|FileUtils
operator|.
name|delete
argument_list|(
name|file
argument_list|,
name|FileUtils
operator|.
name|RECURSIVE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|cleanupCreatedFiles
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

