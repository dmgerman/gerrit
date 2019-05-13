begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.rest.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|project
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
name|gerrit
operator|.
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|acceptance
operator|.
name|PushOneCommit
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
name|acceptance
operator|.
name|RestResponse
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ChangeApi
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|CherryPickInput
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|ReviewInput
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
name|extensions
operator|.
name|api
operator|.
name|projects
operator|.
name|BranchInput
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
name|extensions
operator|.
name|common
operator|.
name|MergeableInfo
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
name|BranchNameKey
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
name|revwalk
operator|.
name|RevCommit
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
name|RefSpec
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
DECL|class|CheckMergeabilityIT
specifier|public
class|class
name|CheckMergeabilityIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|branch
specifier|private
name|BranchNameKey
name|branch
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
name|branch
operator|=
name|BranchNameKey
operator|.
name|create
argument_list|(
name|project
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|branch
operator|.
name|project
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
name|branch
operator|.
name|branch
argument_list|()
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkMergeableCommit ()
specifier|public
name|void
name|checkMergeableCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in b"
argument_list|)
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"b contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/test"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertMergeable
argument_list|(
literal|"master"
argument_list|,
literal|"test"
argument_list|,
literal|"recursive"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkUnMergeableCommit ()
specifier|public
name|void
name|checkUnMergeableCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a too"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents too"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/test"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertUnMergeable
argument_list|(
literal|"master"
argument_list|,
literal|"test"
argument_list|,
literal|"recursive"
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkOursMergeStrategy ()
specifier|public
name|void
name|checkOursMergeStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a too"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents too"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/test"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertMergeable
argument_list|(
literal|"master"
argument_list|,
literal|"test"
argument_list|,
literal|"ours"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkAlreadyMergedCommit ()
specifier|public
name|void
name|checkAlreadyMergedCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectId
name|c0
init|=
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"second commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"b.txt"
argument_list|,
literal|"b contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertCommitMerged
argument_list|(
literal|"master"
argument_list|,
name|c0
operator|.
name|getName
argument_list|()
argument_list|,
literal|""
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkContentMergedCommit ()
specifier|public
name|void
name|checkContentMergedCommit
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
comment|// create a change, and cherrypick into master
name|PushOneCommit
operator|.
name|Result
name|cId
init|=
name|createChange
argument_list|()
decl_stmt|;
name|RevCommit
name|commitId
init|=
name|cId
operator|.
name|getCommit
argument_list|()
decl_stmt|;
name|CherryPickInput
name|cpi
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|cpi
operator|.
name|destination
operator|=
literal|"master"
expr_stmt|;
name|cpi
operator|.
name|message
operator|=
literal|"cherry pick the commit"
expr_stmt|;
name|ChangeApi
name|orig
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|cId
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeApi
name|cherry
init|=
name|orig
operator|.
name|current
argument_list|()
operator|.
name|cherryPick
argument_list|(
name|cpi
argument_list|)
decl_stmt|;
name|cherry
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|cherry
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|()
expr_stmt|;
name|ObjectId
name|remoteId
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|remoteId
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
name|commitId
argument_list|)
expr_stmt|;
name|assertContentMerged
argument_list|(
literal|"master"
argument_list|,
name|commitId
operator|.
name|getName
argument_list|()
argument_list|,
literal|"recursive"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkInvalidSource ()
specifier|public
name|void
name|checkInvalidSource
parameter_list|()
throws|throws
name|Exception
block|{
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertBadRequest
argument_list|(
literal|"master"
argument_list|,
literal|"fdsafsdf"
argument_list|,
literal|"recursive"
argument_list|,
literal|"Cannot resolve 'fdsafsdf' to a commit"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|checkInvalidStrategy ()
specifier|public
name|void
name|checkInvalidStrategy
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|initialHead
init|=
name|projectOperations
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|getHead
argument_list|(
literal|"master"
argument_list|)
decl_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"first commit"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents "
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/master"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|reset
argument_list|(
name|initialHead
argument_list|)
expr_stmt|;
name|testRepo
operator|.
name|branch
argument_list|(
literal|"HEAD"
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|insertChangeId
argument_list|()
operator|.
name|message
argument_list|(
literal|"some change in a too"
argument_list|)
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"a contents too"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|testRepo
operator|.
name|git
argument_list|()
operator|.
name|push
argument_list|()
operator|.
name|setRemote
argument_list|(
literal|"origin"
argument_list|)
operator|.
name|setRefSpecs
argument_list|(
operator|new
name|RefSpec
argument_list|(
literal|"HEAD:refs/heads/test"
argument_list|)
argument_list|)
operator|.
name|call
argument_list|()
expr_stmt|;
name|assertBadRequest
argument_list|(
literal|"master"
argument_list|,
literal|"test"
argument_list|,
literal|"octopus"
argument_list|,
literal|"invalid merge strategy: octopus"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMergeable (String targetBranch, String source, String strategy)
specifier|private
name|void
name|assertMergeable
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|MergeableInfo
name|mergeableInfo
init|=
name|getMergeableInfo
argument_list|(
name|targetBranch
argument_list|,
name|source
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|mergeable
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|assertUnMergeable ( String targetBranch, String source, String strategy, String... conflicts)
specifier|private
name|void
name|assertUnMergeable
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|,
name|String
modifier|...
name|conflicts
parameter_list|)
throws|throws
name|Exception
block|{
name|MergeableInfo
name|mergeableInfo
init|=
name|getMergeableInfo
argument_list|(
name|targetBranch
argument_list|,
name|source
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|mergeable
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|conflicts
argument_list|)
operator|.
name|containsExactly
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|conflicts
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCommitMerged (String targetBranch, String source, String strategy)
specifier|private
name|void
name|assertCommitMerged
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|MergeableInfo
name|mergeableInfo
init|=
name|getMergeableInfo
argument_list|(
name|targetBranch
argument_list|,
name|source
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|mergeable
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|commitMerged
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|assertContentMerged (String targetBranch, String source, String strategy)
specifier|private
name|void
name|assertContentMerged
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|MergeableInfo
name|mergeableInfo
init|=
name|getMergeableInfo
argument_list|(
name|targetBranch
argument_list|,
name|source
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|mergeable
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|mergeableInfo
operator|.
name|contentMerged
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
block|}
DECL|method|assertBadRequest (String targetBranch, String source, String strategy, String errMsg)
specifier|private
name|void
name|assertBadRequest
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|,
name|String
name|errMsg
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches/"
operator|+
name|targetBranch
decl_stmt|;
name|url
operator|+=
literal|"/mergeable?source="
operator|+
name|source
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"&strategy="
operator|+
name|strategy
expr_stmt|;
block|}
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertBadRequest
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|r
operator|.
name|getEntityContent
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|errMsg
argument_list|)
expr_stmt|;
block|}
DECL|method|getMergeableInfo (String targetBranch, String source, String strategy)
specifier|private
name|MergeableInfo
name|getMergeableInfo
parameter_list|(
name|String
name|targetBranch
parameter_list|,
name|String
name|source
parameter_list|,
name|String
name|strategy
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|url
init|=
literal|"/projects/"
operator|+
name|project
operator|.
name|get
argument_list|()
operator|+
literal|"/branches/"
operator|+
name|targetBranch
decl_stmt|;
name|url
operator|+=
literal|"/mergeable?source="
operator|+
name|source
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"&strategy="
operator|+
name|strategy
expr_stmt|;
block|}
name|RestResponse
name|r
init|=
name|userRestSession
operator|.
name|get
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|r
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|MergeableInfo
name|result
init|=
name|newGson
argument_list|()
operator|.
name|fromJson
argument_list|(
name|r
operator|.
name|getReader
argument_list|()
argument_list|,
name|MergeableInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|r
operator|.
name|consume
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

