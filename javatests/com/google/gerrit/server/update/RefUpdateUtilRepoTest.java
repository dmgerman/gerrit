begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|assertThat
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
name|ImmutableList
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
name|MoreFiles
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
name|RecursiveDeleteOption
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
name|file
operator|.
name|FileRepository
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
name|junit
operator|.
name|TestRepository
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|Parameterized
operator|.
name|class
argument_list|)
DECL|class|RefUpdateUtilRepoTest
specifier|public
class|class
name|RefUpdateUtilRepoTest
block|{
DECL|enum|RepoSetup
specifier|public
enum|enum
name|RepoSetup
block|{
DECL|enumConstant|LOCAL_DISK
name|LOCAL_DISK
block|{
annotation|@
name|Override
name|Repository
name|setUpRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Path
name|p
init|=
name|Files
operator|.
name|createTempDirectory
argument_list|(
literal|"gerrit_repo_"
argument_list|)
decl_stmt|;
try|try
block|{
name|Repository
name|repo
init|=
operator|new
name|FileRepository
argument_list|(
name|p
operator|.
name|toFile
argument_list|()
argument_list|)
decl_stmt|;
name|repo
operator|.
name|create
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|delete
argument_list|(
name|p
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
annotation|@
name|Override
name|void
name|tearDownRepo
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|Exception
block|{
name|delete
argument_list|(
name|repo
operator|.
name|getDirectory
argument_list|()
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|delete
parameter_list|(
name|Path
name|p
parameter_list|)
throws|throws
name|Exception
block|{
name|MoreFiles
operator|.
name|deleteRecursively
argument_list|(
name|p
argument_list|,
name|RecursiveDeleteOption
operator|.
name|ALLOW_INSECURE
argument_list|)
expr_stmt|;
block|}
block|}
block|,
DECL|enumConstant|IN_MEMORY
name|IN_MEMORY
block|{
annotation|@
name|Override
name|Repository
name|setUpRepo
parameter_list|()
block|{
return|return
operator|new
name|InMemoryRepository
argument_list|(
operator|new
name|DfsRepositoryDescription
argument_list|(
literal|"repo"
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
name|void
name|tearDownRepo
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{}
block|}
block|;
DECL|method|setUpRepo ()
specifier|abstract
name|Repository
name|setUpRepo
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|tearDownRepo (Repository repo)
specifier|abstract
name|void
name|tearDownRepo
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
annotation|@
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
DECL|method|data ()
specifier|public
specifier|static
name|ImmutableList
argument_list|<
name|RepoSetup
index|[]
argument_list|>
name|data
parameter_list|()
block|{
return|return
name|ImmutableList
operator|.
name|copyOf
argument_list|(
operator|new
name|RepoSetup
index|[]
index|[]
block|{
block|{
name|RepoSetup
operator|.
name|LOCAL_DISK
block|}
block|,
block|{
name|RepoSetup
operator|.
name|IN_MEMORY
block|}
block|}
argument_list|)
return|;
block|}
DECL|field|repoSetup
annotation|@
name|Parameter
specifier|public
name|RepoSetup
name|repoSetup
decl_stmt|;
DECL|field|repo
specifier|private
name|Repository
name|repo
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|repo
operator|=
name|repoSetup
operator|.
name|setUpRepo
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|repo
operator|!=
literal|null
condition|)
block|{
name|repoSetup
operator|.
name|tearDownRepo
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|repo
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|deleteRefNoOp ()
specifier|public
name|void
name|deleteRefNoOp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ref
init|=
literal|"refs/heads/foo"
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|RefUpdateUtil
operator|.
name|deleteChecked
argument_list|(
name|repo
argument_list|,
literal|"refs/heads/foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteRef ()
specifier|public
name|void
name|deleteRef
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|ref
init|=
literal|"refs/heads/foo"
decl_stmt|;
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
operator|.
name|branch
argument_list|(
name|ref
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|RefUpdateUtil
operator|.
name|deleteChecked
argument_list|(
name|repo
argument_list|,
literal|"refs/heads/foo"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|ref
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

