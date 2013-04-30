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
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|TempFileUtil
specifier|public
class|class
name|TempFileUtil
block|{
DECL|method|createTempDirectory ()
specifier|public
specifier|static
name|File
name|createTempDirectory
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|tmp
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"gerrit_test_"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|tmp
operator|.
name|delete
argument_list|()
operator|||
operator|!
name|tmp
operator|.
name|mkdir
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot create "
operator|+
name|tmp
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|tmp
return|;
block|}
DECL|method|recursivelyDelete (File dir)
specifier|public
specifier|static
name|void
name|recursivelyDelete
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|dir
operator|.
name|getPath
argument_list|()
operator|.
name|equals
argument_list|(
name|dir
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
condition|)
block|{
comment|// Directory symlink reaching outside of temporary space.
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Refusing to clear symlink "
operator|+
name|dir
operator|.
name|getPath
argument_list|()
argument_list|)
throw|;
block|}
name|File
index|[]
name|contents
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|contents
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|d
range|:
name|contents
control|)
block|{
if|if
condition|(
name|d
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|recursivelyDelete
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|deleteNowOrOnExit
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|deleteNowOrOnExit
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteNowOrOnExit (File dir)
specifier|private
specifier|static
name|void
name|deleteNowOrOnExit
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
if|if
condition|(
operator|!
name|dir
operator|.
name|delete
argument_list|()
condition|)
block|{
name|dir
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|TempFileUtil ()
specifier|private
name|TempFileUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

