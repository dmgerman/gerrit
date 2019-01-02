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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertWithMessage
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
name|client
operator|.
name|Project
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
name|git
operator|.
name|GitRepositoryManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Inject
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|RepositoryNotFoundException
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
name|lib
operator|.
name|Repository
import|;
end_import

begin_class
DECL|class|GcAssert
specifier|public
class|class
name|GcAssert
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
annotation|@
name|Inject
DECL|method|GcAssert (GitRepositoryManager repoManager)
specifier|public
name|GcAssert
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
block|}
DECL|method|assertHasPackFile (Project.NameKey... projects)
specifier|public
name|void
name|assertHasPackFile
parameter_list|(
name|Project
operator|.
name|NameKey
modifier|...
name|projects
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
name|assertWithMessage
argument_list|(
literal|"Project "
operator|+
name|p
operator|.
name|get
argument_list|()
operator|+
literal|" has no pack files."
argument_list|)
operator|.
name|that
argument_list|(
name|getPackFiles
argument_list|(
name|p
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|assertHasNoPackFile (Project.NameKey... projects)
specifier|public
name|void
name|assertHasNoPackFile
parameter_list|(
name|Project
operator|.
name|NameKey
modifier|...
name|projects
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
for|for
control|(
name|Project
operator|.
name|NameKey
name|p
range|:
name|projects
control|)
block|{
name|assertWithMessage
argument_list|(
literal|"Project "
operator|+
name|p
operator|.
name|get
argument_list|()
operator|+
literal|" has pack files."
argument_list|)
operator|.
name|that
argument_list|(
name|getPackFiles
argument_list|(
name|p
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getPackFiles (Project.NameKey p)
specifier|private
name|String
index|[]
name|getPackFiles
parameter_list|(
name|Project
operator|.
name|NameKey
name|p
parameter_list|)
throws|throws
name|RepositoryNotFoundException
throws|,
name|IOException
block|{
try|try
init|(
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|p
argument_list|)
init|)
block|{
name|File
name|packDir
init|=
operator|new
name|File
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
argument_list|,
literal|"objects/pack"
argument_list|)
decl_stmt|;
return|return
name|packDir
operator|.
name|list
argument_list|(
parameter_list|(
name|dir
parameter_list|,
name|name
parameter_list|)
lambda|->
name|name
operator|.
name|endsWith
argument_list|(
literal|".pack"
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

