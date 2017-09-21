begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|JarFile
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

begin_class
DECL|class|CleanupHandle
class|class
name|CleanupHandle
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
name|CleanupHandle
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tmp
specifier|private
specifier|final
name|Path
name|tmp
decl_stmt|;
DECL|field|jarFile
specifier|private
specifier|final
name|JarFile
name|jarFile
decl_stmt|;
DECL|method|CleanupHandle (Path tmp, JarFile jarFile)
name|CleanupHandle
parameter_list|(
name|Path
name|tmp
parameter_list|,
name|JarFile
name|jarFile
parameter_list|)
block|{
name|this
operator|.
name|tmp
operator|=
name|tmp
expr_stmt|;
name|this
operator|.
name|jarFile
operator|=
name|jarFile
expr_stmt|;
block|}
DECL|method|cleanup ()
name|void
name|cleanup
parameter_list|()
block|{
try|try
block|{
name|jarFile
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot close "
operator|+
name|jarFile
operator|.
name|getName
argument_list|()
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Cleaned plugin "
operator|+
name|tmp
operator|.
name|getFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot delete "
operator|+
name|tmp
operator|.
name|toAbsolutePath
argument_list|()
operator|+
literal|", retrying to delete it on termination of the virtual machine"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|tmp
operator|.
name|toFile
argument_list|()
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

