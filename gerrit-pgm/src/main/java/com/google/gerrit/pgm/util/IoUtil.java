begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.pgm.util
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|pgm
operator|.
name|util
package|;
end_package

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
name|StringUtils
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
name|OutputStream
import|;
end_import

begin_class
DECL|class|IoUtil
specifier|public
specifier|final
class|class
name|IoUtil
block|{
DECL|method|isWin32 ()
specifier|public
specifier|static
specifier|final
name|boolean
name|isWin32
parameter_list|()
block|{
specifier|final
name|String
name|osDotName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
return|return
name|osDotName
operator|!=
literal|null
operator|&&
name|StringUtils
operator|.
name|toLowerCase
argument_list|(
name|osDotName
argument_list|)
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
DECL|method|copyWithThread (final InputStream src, final OutputStream dst)
specifier|public
specifier|static
name|void
name|copyWithThread
parameter_list|(
specifier|final
name|InputStream
name|src
parameter_list|,
specifier|final
name|OutputStream
name|dst
parameter_list|)
block|{
operator|new
name|Thread
argument_list|(
literal|"IoUtil-Copy"
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
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
literal|256
index|]
decl_stmt|;
name|int
name|n
decl_stmt|;
while|while
condition|(
literal|0
operator|<
operator|(
name|n
operator|=
name|src
operator|.
name|read
argument_list|(
name|buf
argument_list|)
operator|)
condition|)
block|{
name|dst
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
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|src
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e2
parameter_list|)
block|{           }
block|}
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|IoUtil ()
specifier|private
name|IoUtil
parameter_list|()
block|{   }
block|}
end_class

end_unit

