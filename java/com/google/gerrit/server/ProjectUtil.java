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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

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
name|Branch
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
name|RefNames
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
DECL|class|ProjectUtil
specifier|public
class|class
name|ProjectUtil
block|{
comment|/**    * Checks whether the specified branch exists.    *    * @param repoManager Git repository manager to open the git repository    * @param branch the branch for which it should be checked if it exists    * @return {@code true} if the specified branch exists or if {@code HEAD} points to this branch,    *     otherwise {@code false}    * @throws RepositoryNotFoundException the repository of the branch's project does not exist.    * @throws IOException error while retrieving the branch from the repository.    */
DECL|method|branchExists (final GitRepositoryManager repoManager, Branch.NameKey branch)
specifier|public
specifier|static
name|boolean
name|branchExists
parameter_list|(
specifier|final
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Branch
operator|.
name|NameKey
name|branch
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
name|branch
operator|.
name|getParentKey
argument_list|()
argument_list|)
init|)
block|{
name|boolean
name|exists
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|exists
operator|=
name|repo
operator|.
name|getFullBranch
argument_list|()
operator|.
name|equals
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
operator|||
name|RefNames
operator|.
name|REFS_CONFIG
operator|.
name|equals
argument_list|(
name|branch
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exists
return|;
block|}
block|}
DECL|method|stripGitSuffix (String name)
specifier|public
specifier|static
name|String
name|stripGitSuffix
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|".git"
argument_list|)
condition|)
block|{
comment|// Be nice and drop the trailing ".git" suffix, which we never keep
comment|// in our database, but clients might mistakenly provide anyway.
comment|//
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|4
argument_list|)
expr_stmt|;
while|while
condition|(
name|name
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|name
operator|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

