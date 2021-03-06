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
DECL|package|com.google.gerrit.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
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
name|collect
operator|.
name|ImmutableSortedSet
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
name|Sets
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
name|entities
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
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|RepositoryCaseMismatchException
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
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|SortedSet
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
name|internal
operator|.
name|storage
operator|.
name|dfs
operator|.
name|DfsRepository
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
name|dfs
operator|.
name|DfsRepositoryDescription
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
name|dfs
operator|.
name|InMemoryRepository
import|;
end_import

begin_comment
comment|/** Repository manager that uses in-memory repositories. */
end_comment

begin_class
DECL|class|InMemoryRepositoryManager
specifier|public
class|class
name|InMemoryRepositoryManager
implements|implements
name|GitRepositoryManager
block|{
DECL|method|newRepository (Project.NameKey name)
specifier|public
specifier|static
name|InMemoryRepository
name|newRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
return|return
operator|new
name|Repo
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|class|Description
specifier|public
specifier|static
class|class
name|Description
extends|extends
name|DfsRepositoryDescription
block|{
DECL|field|name
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|name
decl_stmt|;
DECL|method|Description (Project.NameKey name)
specifier|private
name|Description
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
DECL|class|Repo
specifier|public
specifier|static
class|class
name|Repo
extends|extends
name|InMemoryRepository
block|{
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|method|Repo (Project.NameKey name)
specifier|private
name|Repo
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|Description
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|setPerformsAtomicTransactions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDescription ()
specifier|public
name|Description
name|getDescription
parameter_list|()
block|{
return|return
operator|(
name|Description
operator|)
name|super
operator|.
name|getDescription
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getGitwebDescription ()
specifier|public
name|String
name|getGitwebDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
annotation|@
name|Override
DECL|method|setGitwebDescription (String d)
specifier|public
name|void
name|setGitwebDescription
parameter_list|(
name|String
name|d
parameter_list|)
block|{
name|description
operator|=
name|d
expr_stmt|;
block|}
block|}
DECL|field|repos
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Repo
argument_list|>
name|repos
decl_stmt|;
annotation|@
name|Inject
DECL|method|InMemoryRepositoryManager ()
specifier|public
name|InMemoryRepositoryManager
parameter_list|()
block|{
name|this
operator|.
name|repos
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|openRepository (Project.NameKey name)
specifier|public
specifier|synchronized
name|Repo
name|openRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
block|{
return|return
name|get
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRepository (Project.NameKey name)
specifier|public
specifier|synchronized
name|Repo
name|createRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryCaseMismatchException
throws|,
name|RepositoryNotFoundException
block|{
name|Repo
name|repo
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|repo
operator|.
name|getDescription
argument_list|()
operator|.
name|getRepositoryName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RepositoryCaseMismatchException
argument_list|(
name|name
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryNotFoundException
name|e
parameter_list|)
block|{
name|repo
operator|=
operator|new
name|Repo
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repos
operator|.
name|put
argument_list|(
name|normalize
argument_list|(
name|name
argument_list|)
argument_list|,
name|repo
argument_list|)
expr_stmt|;
block|}
return|return
name|repo
return|;
block|}
annotation|@
name|Override
DECL|method|list ()
specifier|public
specifier|synchronized
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|list
parameter_list|()
block|{
name|SortedSet
argument_list|<
name|Project
operator|.
name|NameKey
argument_list|>
name|names
init|=
name|Sets
operator|.
name|newTreeSet
argument_list|()
decl_stmt|;
for|for
control|(
name|DfsRepository
name|repo
range|:
name|repos
operator|.
name|values
argument_list|()
control|)
block|{
name|names
operator|.
name|add
argument_list|(
name|Project
operator|.
name|nameKey
argument_list|(
name|repo
operator|.
name|getDescription
argument_list|()
operator|.
name|getRepositoryName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|names
argument_list|)
return|;
block|}
DECL|method|deleteRepository (Project.NameKey name)
specifier|public
specifier|synchronized
name|void
name|deleteRepository
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
name|repos
operator|.
name|remove
argument_list|(
name|normalize
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|get (Project.NameKey name)
specifier|private
specifier|synchronized
name|Repo
name|get
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
throws|throws
name|RepositoryNotFoundException
block|{
name|Repo
name|repo
init|=
name|repos
operator|.
name|get
argument_list|(
name|normalize
argument_list|(
name|name
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repo
operator|.
name|incrementOpen
argument_list|()
expr_stmt|;
return|return
name|repo
return|;
block|}
throw|throw
operator|new
name|RepositoryNotFoundException
argument_list|(
name|name
operator|.
name|get
argument_list|()
argument_list|)
throw|;
block|}
DECL|method|normalize (Project.NameKey name)
specifier|private
specifier|static
name|String
name|normalize
parameter_list|(
name|Project
operator|.
name|NameKey
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|get
argument_list|()
operator|.
name|toLowerCase
argument_list|()
return|;
block|}
block|}
end_class

end_unit

