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
name|inject
operator|.
name|Inject
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
annotation|@
name|Inject
DECL|field|patchListCache
specifier|private
name|PatchListCache
name|patchListCache
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
name|c
operator|=
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
operator|new
name|PatchListKey
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
block|}
end_class

end_unit

