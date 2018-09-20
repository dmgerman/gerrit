begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|change
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
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|getChangeId
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|GitUtil
operator|.
name|pushHead
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
operator|.
name|UTF_8
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
name|cache
operator|.
name|Cache
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
name|collect
operator|.
name|ImmutableSet
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
name|Iterables
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
name|NoHttpd
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
name|client
operator|.
name|DiffPreferencesInfo
operator|.
name|Whitespace
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
name|Patch
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
name|Patch
operator|.
name|ChangeType
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
name|patch
operator|.
name|IntraLineDiff
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
name|patch
operator|.
name|IntraLineDiffArgs
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
name|patch
operator|.
name|IntraLineDiffKey
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
name|patch
operator|.
name|PatchList
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
name|patch
operator|.
name|PatchListCache
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
name|patch
operator|.
name|PatchListCacheImpl
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
name|patch
operator|.
name|PatchListEntry
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
name|patch
operator|.
name|PatchListKey
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
name|patch
operator|.
name|Text
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|name
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|diff
operator|.
name|Edit
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
annotation|@
name|NoHttpd
DECL|class|PatchListCacheIT
specifier|public
class|class
name|PatchListCacheIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|SUBJECT_1
specifier|private
specifier|static
name|String
name|SUBJECT_1
init|=
literal|"subject 1"
decl_stmt|;
DECL|field|SUBJECT_2
specifier|private
specifier|static
name|String
name|SUBJECT_2
init|=
literal|"subject 2"
decl_stmt|;
DECL|field|SUBJECT_3
specifier|private
specifier|static
name|String
name|SUBJECT_3
init|=
literal|"subject 3"
decl_stmt|;
DECL|field|FILE_A
specifier|private
specifier|static
name|String
name|FILE_A
init|=
literal|"a.txt"
decl_stmt|;
DECL|field|FILE_B
specifier|private
specifier|static
name|String
name|FILE_B
init|=
literal|"b.txt"
decl_stmt|;
DECL|field|FILE_C
specifier|private
specifier|static
name|String
name|FILE_C
init|=
literal|"c.txt"
decl_stmt|;
DECL|field|FILE_D
specifier|private
specifier|static
name|String
name|FILE_D
init|=
literal|"d.txt"
decl_stmt|;
DECL|field|patchListCache
annotation|@
name|Inject
specifier|private
name|PatchListCache
name|patchListCache
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"diff"
argument_list|)
DECL|field|abstractPatchListCache
specifier|private
name|Cache
argument_list|<
name|PatchListKey
argument_list|,
name|PatchList
argument_list|>
name|abstractPatchListCache
decl_stmt|;
annotation|@
name|Test
DECL|method|listPatchesAgainstBase ()
specifier|public
name|void
name|listPatchesAgainstBase
parameter_list|()
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_D
argument_list|,
literal|"4"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1, 1 (+FILE_A, -FILE_D)
name|RevCommit
name|c
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_A
argument_list|,
literal|"1"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_D
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_2
argument_list|)
operator|.
name|insertChangeId
argument_list|()
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Compare Change 1,1 with Base (+FILE_A, -FILE_D)
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entries
init|=
name|getCurrentPatches
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_A
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_D
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Change 1,2 (+FILE_A, +FILE_B, -FILE_D)
name|amendBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_B
argument_list|,
literal|"2"
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|entries
operator|=
name|getCurrentPatches
argument_list|(
name|id
argument_list|)
expr_stmt|;
comment|// Compare Change 1,2 with Base (+FILE_A, +FILE_B, -FILE_D)
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_A
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_B
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_D
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listPatchesAgainstBaseWithRebase ()
specifier|public
name|void
name|listPatchesAgainstBaseWithRebase
parameter_list|()
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_D
argument_list|,
literal|"4"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,1 (+FILE_A, -FILE_D)
name|RevCommit
name|c
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_A
argument_list|,
literal|"1"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_D
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|getChangeId
argument_list|(
name|testRepo
argument_list|,
name|c
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entries
init|=
name|getCurrentPatches
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_A
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_D
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Change 2,1 (+FILE_B)
name|testRepo
operator|.
name|reset
argument_list|(
literal|"HEAD~1"
argument_list|)
expr_stmt|;
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_B
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_3
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,2 (+FILE_A, -FILE_D))
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Compare Change 1,2 with Base (+FILE_A, -FILE_D))
name|entries
operator|=
name|getCurrentPatches
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_A
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_D
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listPatchesAgainstOtherPatchSet ()
specifier|public
name|void
name|listPatchesAgainstOtherPatchSet
parameter_list|()
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_D
argument_list|,
literal|"4"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,1 (+FILE_A, +FILE_C, -FILE_D)
name|RevCommit
name|a
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_A
argument_list|,
literal|"1"
argument_list|)
operator|.
name|add
argument_list|(
name|FILE_C
argument_list|,
literal|"3"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_D
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,2 (+FILE_A, +FILE_B, -FILE_D)
name|RevCommit
name|b
init|=
name|amendBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_B
argument_list|,
literal|"2"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_C
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Compare Change 1,1 with Change 1,2 (+FILE_B, -FILE_C)
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entries
init|=
name|getPatches
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertModified
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_B
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_C
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// Compare Change 1,2 with Change 1,1 (-FILE_B, +FILE_C)
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entriesReverse
init|=
name|getPatches
argument_list|(
name|b
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entriesReverse
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertModified
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entriesReverse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_B
argument_list|,
name|entriesReverse
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_C
argument_list|,
name|entriesReverse
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|listPatchesAgainstOtherPatchSetWithRebase ()
specifier|public
name|void
name|listPatchesAgainstOtherPatchSetWithRebase
parameter_list|()
throws|throws
name|Exception
block|{
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_D
argument_list|,
literal|"4"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,1 (+FILE_A, -FILE_D)
name|RevCommit
name|a
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_A
argument_list|,
literal|"1"
argument_list|)
operator|.
name|rm
argument_list|(
name|FILE_D
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_2
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 2,1 (+FILE_B)
name|testRepo
operator|.
name|reset
argument_list|(
literal|"HEAD~1"
argument_list|)
expr_stmt|;
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_B
argument_list|,
literal|"2"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_3
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Change 1,2 (+FILE_A, +FILE_C, -FILE_D)
name|testRepo
operator|.
name|cherryPick
argument_list|(
name|a
argument_list|)
expr_stmt|;
name|RevCommit
name|b
init|=
name|amendBuilder
argument_list|()
operator|.
name|add
argument_list|(
name|FILE_C
argument_list|,
literal|"2"
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/for/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Compare Change 1,1 with Change 1,2 (+FILE_C)
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entries
init|=
name|getPatches
argument_list|(
name|a
argument_list|,
name|b
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entries
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertModified
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertAdded
argument_list|(
name|FILE_C
argument_list|,
name|entries
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// Compare Change 1,2 with Change 1,1 (-FILE_C)
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|entriesReverse
init|=
name|getPatches
argument_list|(
name|b
argument_list|,
name|a
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|entriesReverse
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertModified
argument_list|(
name|Patch
operator|.
name|COMMIT_MSG
argument_list|,
name|entriesReverse
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertDeleted
argument_list|(
name|FILE_C
argument_list|,
name|entriesReverse
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|harmfulMutationsOfEditsAreNotPossibleForPatchListEntry ()
specifier|public
name|void
name|harmfulMutationsOfEditsAreNotPossibleForPatchListEntry
parameter_list|()
throws|throws
name|Exception
block|{
name|RevCommit
name|commit
init|=
name|commitBuilder
argument_list|()
operator|.
name|add
argument_list|(
literal|"a.txt"
argument_list|,
literal|"First line\nSecond line\n"
argument_list|)
operator|.
name|message
argument_list|(
name|SUBJECT_1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|pushHead
argument_list|(
name|testRepo
argument_list|,
literal|"refs/heads/master"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|PatchListKey
name|diffKey
init|=
name|PatchListKey
operator|.
name|againstDefaultBase
argument_list|(
name|commit
operator|.
name|copy
argument_list|()
argument_list|,
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
decl_stmt|;
name|PatchList
name|patchList
init|=
name|patchListCache
operator|.
name|get
argument_list|(
name|diffKey
argument_list|,
name|project
argument_list|)
decl_stmt|;
name|PatchListEntry
name|patchListEntry
init|=
name|getEntryFor
argument_list|(
name|patchList
argument_list|,
literal|"a.txt"
argument_list|)
decl_stmt|;
name|Edit
name|outputEdit
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|patchListEntry
operator|.
name|getEdits
argument_list|()
argument_list|)
decl_stmt|;
name|Edit
name|originalEdit
init|=
operator|new
name|Edit
argument_list|(
name|outputEdit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|outputEdit
operator|.
name|getEndA
argument_list|()
argument_list|,
name|outputEdit
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|outputEdit
operator|.
name|getEndB
argument_list|()
argument_list|)
decl_stmt|;
name|outputEdit
operator|.
name|shift
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|patchListEntry
operator|.
name|getEdits
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|originalEdit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|harmfulMutationsOfEditsAreNotPossibleForIntraLineDiffArgsAndCachedValue ()
specifier|public
name|void
name|harmfulMutationsOfEditsAreNotPossibleForIntraLineDiffArgsAndCachedValue
parameter_list|()
block|{
name|String
name|a
init|=
literal|"First line\nSecond line\n"
decl_stmt|;
name|String
name|b
init|=
literal|"1st line\n2nd line\n"
decl_stmt|;
name|Text
name|aText
init|=
operator|new
name|Text
argument_list|(
name|a
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|Text
name|bText
init|=
operator|new
name|Text
argument_list|(
name|b
operator|.
name|getBytes
argument_list|(
name|UTF_8
argument_list|)
argument_list|)
decl_stmt|;
name|Edit
name|inputEdit
init|=
operator|new
name|Edit
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Edit
argument_list|>
name|inputEdits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|inputEdit
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Edit
argument_list|>
name|inputEditsDueToRebase
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|ImmutableSet
operator|.
name|of
argument_list|(
name|inputEdit
argument_list|)
argument_list|)
decl_stmt|;
name|IntraLineDiffKey
name|diffKey
init|=
name|IntraLineDiffKey
operator|.
name|create
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
decl_stmt|;
name|IntraLineDiffArgs
name|diffArgs
init|=
name|IntraLineDiffArgs
operator|.
name|create
argument_list|(
name|aText
argument_list|,
name|bText
argument_list|,
name|inputEdits
argument_list|,
name|inputEditsDueToRebase
argument_list|,
name|project
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
literal|"file.txt"
argument_list|)
decl_stmt|;
name|IntraLineDiff
name|intraLineDiff
init|=
name|patchListCache
operator|.
name|getIntraLineDiff
argument_list|(
name|diffKey
argument_list|,
name|diffArgs
argument_list|)
decl_stmt|;
name|Edit
name|outputEdit
init|=
name|Iterables
operator|.
name|getOnlyElement
argument_list|(
name|intraLineDiff
operator|.
name|getEdits
argument_list|()
argument_list|)
decl_stmt|;
name|outputEdit
operator|.
name|shift
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|inputEdit
operator|.
name|shift
argument_list|(
literal|7
argument_list|)
expr_stmt|;
name|inputEdits
operator|.
name|add
argument_list|(
operator|new
name|Edit
argument_list|(
literal|43
argument_list|,
literal|47
argument_list|,
literal|50
argument_list|,
literal|51
argument_list|)
argument_list|)
expr_stmt|;
name|inputEditsDueToRebase
operator|.
name|add
argument_list|(
operator|new
name|Edit
argument_list|(
literal|53
argument_list|,
literal|57
argument_list|,
literal|60
argument_list|,
literal|61
argument_list|)
argument_list|)
expr_stmt|;
name|Edit
name|originalEdit
init|=
operator|new
name|Edit
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|diffArgs
operator|.
name|edits
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|originalEdit
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|diffArgs
operator|.
name|editsDueToRebase
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|originalEdit
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|intraLineDiff
operator|.
name|getEdits
argument_list|()
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|originalEdit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|largeObjectTombstoneGetsCached ()
specifier|public
name|void
name|largeObjectTombstoneGetsCached
parameter_list|()
block|{
name|PatchListKey
name|key
init|=
name|PatchListKey
operator|.
name|againstDefaultBase
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|,
name|Whitespace
operator|.
name|IGNORE_ALL
argument_list|)
decl_stmt|;
name|PatchListCacheImpl
operator|.
name|LargeObjectTombstone
name|tombstone
init|=
operator|new
name|PatchListCacheImpl
operator|.
name|LargeObjectTombstone
argument_list|()
decl_stmt|;
name|abstractPatchListCache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|tombstone
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|abstractPatchListCache
operator|.
name|getIfPresent
argument_list|(
name|key
argument_list|)
argument_list|)
operator|.
name|isSameAs
argument_list|(
name|tombstone
argument_list|)
expr_stmt|;
block|}
DECL|method|assertAdded (String expectedNewName, PatchListEntry e)
specifier|private
specifier|static
name|void
name|assertAdded
parameter_list|(
name|String
name|expectedNewName
parameter_list|,
name|PatchListEntry
name|e
parameter_list|)
block|{
name|assertName
argument_list|(
name|expectedNewName
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getChangeType
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeType
operator|.
name|ADDED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertModified (String expectedNewName, PatchListEntry e)
specifier|private
specifier|static
name|void
name|assertModified
parameter_list|(
name|String
name|expectedNewName
parameter_list|,
name|PatchListEntry
name|e
parameter_list|)
block|{
name|assertName
argument_list|(
name|expectedNewName
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getChangeType
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeType
operator|.
name|MODIFIED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertDeleted (String expectedNewName, PatchListEntry e)
specifier|private
specifier|static
name|void
name|assertDeleted
parameter_list|(
name|String
name|expectedNewName
parameter_list|,
name|PatchListEntry
name|e
parameter_list|)
block|{
name|assertName
argument_list|(
name|expectedNewName
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getChangeType
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ChangeType
operator|.
name|DELETED
argument_list|)
expr_stmt|;
block|}
DECL|method|assertName (String expectedNewName, PatchListEntry e)
specifier|private
specifier|static
name|void
name|assertName
parameter_list|(
name|String
name|expectedNewName
parameter_list|,
name|PatchListEntry
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getNewName
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expectedNewName
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|e
operator|.
name|getOldName
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|getCurrentPatches (String changeId)
specifier|private
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|getCurrentPatches
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|patchListCache
operator|.
name|get
argument_list|(
name|getKey
argument_list|(
literal|null
argument_list|,
name|getCurrentRevisionId
argument_list|(
name|changeId
argument_list|)
argument_list|)
argument_list|,
name|project
argument_list|)
operator|.
name|getPatches
argument_list|()
return|;
block|}
DECL|method|getPatches (ObjectId revisionIdA, ObjectId revisionIdB)
specifier|private
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|getPatches
parameter_list|(
name|ObjectId
name|revisionIdA
parameter_list|,
name|ObjectId
name|revisionIdB
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|patchListCache
operator|.
name|get
argument_list|(
name|getKey
argument_list|(
name|revisionIdA
argument_list|,
name|revisionIdB
argument_list|)
argument_list|,
name|project
argument_list|)
operator|.
name|getPatches
argument_list|()
return|;
block|}
DECL|method|getKey (ObjectId revisionIdA, ObjectId revisionIdB)
specifier|private
name|PatchListKey
name|getKey
parameter_list|(
name|ObjectId
name|revisionIdA
parameter_list|,
name|ObjectId
name|revisionIdB
parameter_list|)
block|{
return|return
name|PatchListKey
operator|.
name|againstCommit
argument_list|(
name|revisionIdA
argument_list|,
name|revisionIdB
argument_list|,
name|Whitespace
operator|.
name|IGNORE_NONE
argument_list|)
return|;
block|}
DECL|method|getCurrentRevisionId (String changeId)
specifier|private
name|ObjectId
name|getCurrentRevisionId
parameter_list|(
name|String
name|changeId
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|ObjectId
operator|.
name|fromString
argument_list|(
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|changeId
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|currentRevision
argument_list|)
return|;
block|}
DECL|method|getEntryFor (PatchList patchList, String filePath)
specifier|private
specifier|static
name|PatchListEntry
name|getEntryFor
parameter_list|(
name|PatchList
name|patchList
parameter_list|,
name|String
name|filePath
parameter_list|)
block|{
name|Optional
argument_list|<
name|PatchListEntry
argument_list|>
name|patchListEntry
init|=
name|patchList
operator|.
name|getPatches
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|entry
lambda|->
name|entry
operator|.
name|getNewName
argument_list|()
operator|.
name|equals
argument_list|(
name|filePath
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
decl_stmt|;
return|return
name|patchListEntry
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"No PatchListEntry for "
operator|+
name|filePath
operator|+
literal|" exists"
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

