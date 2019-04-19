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
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth8
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
operator|.
name|R_HEADS
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
name|testing
operator|.
name|GerritBaseTests
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
name|testing
operator|.
name|InMemoryRepositoryManager
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
name|Config
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
name|ObjectId
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
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|StoredConfig
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
name|transport
operator|.
name|ReceiveCommand
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

begin_class
DECL|class|RepoViewTest
specifier|public
class|class
name|RepoViewTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|MASTER
specifier|private
specifier|static
specifier|final
name|String
name|MASTER
init|=
literal|"refs/heads/master"
decl_stmt|;
DECL|field|BRANCH
specifier|private
specifier|static
specifier|final
name|String
name|BRANCH
init|=
literal|"refs/heads/branch"
decl_stmt|;
DECL|field|repo
specifier|private
name|Repository
name|repo
decl_stmt|;
DECL|field|tr
specifier|private
name|TestRepository
argument_list|<
name|?
argument_list|>
name|tr
decl_stmt|;
DECL|field|view
specifier|private
name|RepoView
name|view
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
name|InMemoryRepositoryManager
name|repoManager
init|=
operator|new
name|InMemoryRepositoryManager
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|Project
operator|.
name|nameKey
argument_list|(
literal|"project"
argument_list|)
decl_stmt|;
name|repo
operator|=
name|repoManager
operator|.
name|createRepository
argument_list|(
name|project
argument_list|)
expr_stmt|;
name|tr
operator|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|tr
operator|.
name|branch
argument_list|(
name|MASTER
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|view
operator|=
operator|new
name|RepoView
argument_list|(
name|repoManager
argument_list|,
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|view
operator|.
name|close
argument_list|()
expr_stmt|;
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getConfigIsDefensiveCopy ()
specifier|public
name|void
name|getConfigIsDefensiveCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|StoredConfig
name|orig
init|=
name|repo
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|orig
operator|.
name|setString
argument_list|(
literal|"a"
argument_list|,
literal|"config"
argument_list|,
literal|"option"
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|orig
operator|.
name|save
argument_list|()
expr_stmt|;
name|Config
name|copy
init|=
name|view
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|copy
operator|.
name|setString
argument_list|(
literal|"a"
argument_list|,
literal|"config"
argument_list|,
literal|"option"
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|orig
operator|.
name|getString
argument_list|(
literal|"a"
argument_list|,
literal|"config"
argument_list|,
literal|"option"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"yes"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|getConfig
argument_list|()
operator|.
name|getString
argument_list|(
literal|"a"
argument_list|,
literal|"config"
argument_list|,
literal|"option"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"yes"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRef ()
specifier|public
name|void
name|getRef
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oldMaster
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|oldMaster
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|BRANCH
argument_list|)
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|oldMaster
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|BRANCH
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|tr
operator|.
name|branch
argument_list|(
name|MASTER
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
expr_stmt|;
name|tr
operator|.
name|branch
argument_list|(
name|BRANCH
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
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|oldMaster
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|BRANCH
argument_list|)
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|oldMaster
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|BRANCH
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRefsRescansWhenNotCaching ()
specifier|public
name|void
name|getRefsRescansWhenNotCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|oldMaster
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|oldMaster
argument_list|)
expr_stmt|;
name|ObjectId
name|newBranch
init|=
name|tr
operator|.
name|branch
argument_list|(
name|BRANCH
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|oldMaster
argument_list|,
literal|"branch"
argument_list|,
name|newBranch
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRefsUsesCachedValueMatchingGetRef ()
specifier|public
name|void
name|getRefsUsesCachedValueMatchingGetRef
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|master1
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master1
argument_list|)
expr_stmt|;
comment|// Doesn't reflect new value for master.
name|ObjectId
name|master2
init|=
name|tr
operator|.
name|branch
argument_list|(
name|MASTER
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|master2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master1
argument_list|)
expr_stmt|;
comment|// Branch wasn't previously cached, so does reflect new value.
name|ObjectId
name|branch1
init|=
name|tr
operator|.
name|branch
argument_list|(
name|BRANCH
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master1
argument_list|,
literal|"branch"
argument_list|,
name|branch1
argument_list|)
expr_stmt|;
comment|// Looking up branch causes it to be cached.
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|BRANCH
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|branch1
argument_list|)
expr_stmt|;
name|ObjectId
name|branch2
init|=
name|tr
operator|.
name|branch
argument_list|(
name|BRANCH
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|BRANCH
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|branch2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master1
argument_list|,
literal|"branch"
argument_list|,
name|branch1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRefsReflectsCommands ()
specifier|public
name|void
name|getRefsReflectsCommands
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|master1
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master1
argument_list|)
expr_stmt|;
name|ObjectId
name|master2
init|=
name|tr
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|view
operator|.
name|getCommands
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|master1
argument_list|,
name|master2
argument_list|,
name|MASTER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master2
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCommands
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|master2
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|MASTER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getRefsOverwritesCachedValueWithCommand ()
specifier|public
name|void
name|getRefsOverwritesCachedValueWithCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|master1
init|=
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|ObjectId
name|master2
init|=
name|tr
operator|.
name|commit
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|view
operator|.
name|getCommands
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|master1
argument_list|,
name|master2
argument_list|,
name|MASTER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|hasValue
argument_list|(
name|master2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"master"
argument_list|,
name|master2
argument_list|)
expr_stmt|;
name|view
operator|.
name|getCommands
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|master2
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|MASTER
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|repo
operator|.
name|exactRef
argument_list|(
name|MASTER
argument_list|)
operator|.
name|getObjectId
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|master1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRef
argument_list|(
name|MASTER
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|view
operator|.
name|getRefs
argument_list|(
name|R_HEADS
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

