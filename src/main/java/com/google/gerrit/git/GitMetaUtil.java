begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
package|;
end_package

begin_import
import|import
name|org
operator|.
name|spearce
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
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

begin_class
DECL|class|GitMetaUtil
specifier|public
class|class
name|GitMetaUtil
block|{
DECL|method|isGitRepository (final File gitdir)
specifier|public
specifier|static
name|boolean
name|isGitRepository
parameter_list|(
specifier|final
name|File
name|gitdir
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|"config"
argument_list|)
operator|.
name|isFile
argument_list|()
operator|&&
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|"HEAD"
argument_list|)
operator|.
name|isFile
argument_list|()
operator|&&
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|"objects"
argument_list|)
operator|.
name|isDirectory
argument_list|()
operator|&&
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|"refs/heads"
argument_list|)
operator|.
name|isDirectory
argument_list|()
return|;
block|}
DECL|method|open (final File gitdir)
specifier|public
specifier|static
name|Repository
name|open
parameter_list|(
specifier|final
name|File
name|gitdir
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|isGitRepository
argument_list|(
name|gitdir
argument_list|)
condition|)
block|{
return|return
operator|new
name|Repository
argument_list|(
name|gitdir
argument_list|)
return|;
block|}
if|if
condition|(
name|isGitRepository
argument_list|(
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|".git"
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|Repository
argument_list|(
operator|new
name|File
argument_list|(
name|gitdir
argument_list|,
literal|".git"
argument_list|)
argument_list|)
return|;
block|}
specifier|final
name|String
name|name
init|=
name|gitdir
operator|.
name|getName
argument_list|()
decl_stmt|;
specifier|final
name|File
name|parent
init|=
name|gitdir
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
name|isGitRepository
argument_list|(
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|name
operator|+
literal|".git"
argument_list|)
argument_list|)
condition|)
block|{
return|return
operator|new
name|Repository
argument_list|(
operator|new
name|File
argument_list|(
name|parent
argument_list|,
name|name
operator|+
literal|".git"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

