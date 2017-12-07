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
DECL|package|com.google.gerrit.server.group.db
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|group
operator|.
name|db
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
name|extensions
operator|.
name|api
operator|.
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
operator|.
name|warning
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
name|config
operator|.
name|ConsistencyCheckInfo
operator|.
name|ConsistencyProblemInfo
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
name|AccountGroup
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
name|group
operator|.
name|db
operator|.
name|testing
operator|.
name|GroupTestUtil
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|GroupsNoteDbConsistencyCheckerTest
specifier|public
class|class
name|GroupsNoteDbConsistencyCheckerTest
extends|extends
name|AbstractGroupTest
block|{
annotation|@
name|Test
DECL|method|groupNamesRefIsMissing ()
specifier|public
name|void
name|groupNamesRefIsMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"Group with name 'g-1' doesn't exist in the list of all names"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteIsMissing ()
specifier|public
name|void
name|groupNameNoteIsMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-2"
argument_list|,
literal|"[group]\n\tuuid = uuid-2\n\tname = g-2\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"Group with name 'g-1' doesn't exist in the list of all names"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteIsConsistent ()
specifier|public
name|void
name|groupNameNoteIsConsistent
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-1"
argument_list|,
literal|"[group]\n\tuuid = uuid-1\n\tname = g-1\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteHasDifferentUUID ()
specifier|public
name|void
name|groupNameNoteHasDifferentUUID
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-1"
argument_list|,
literal|"[group]\n\tuuid = uuid-2\n\tname = g-1\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"group with name 'g-1' has UUID 'uuid-1' in 'group.config' but 'uuid-2' in group "
operator|+
literal|"name notes"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteHasDifferentName ()
specifier|public
name|void
name|groupNameNoteHasDifferentName
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-1"
argument_list|,
literal|"[group]\n\tuuid = uuid-1\n\tname = g-2\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"group note of name 'g-1' claims to represent name of 'g-2'"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteHasDifferentNameAndUUID ()
specifier|public
name|void
name|groupNameNoteHasDifferentNameAndUUID
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-1"
argument_list|,
literal|"[group]\n\tuuid = uuid-2\n\tname = g-2\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"group with name 'g-1' has UUID 'uuid-1' in 'group.config' but 'uuid-2' in group "
operator|+
literal|"name notes"
argument_list|)
argument_list|,
name|warning
argument_list|(
literal|"group note of name 'g-1' claims to represent name of 'g-2'"
argument_list|)
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|groupNameNoteFailToParse ()
specifier|public
name|void
name|groupNameNoteFailToParse
parameter_list|()
throws|throws
name|Exception
block|{
name|updateGroupNamesRef
argument_list|(
literal|"g-1"
argument_list|,
literal|"[invalid"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
init|=
name|GroupsNoteDbConsistencyChecker
operator|.
name|checkWithGroupNameNotes
argument_list|(
name|allUsersRepo
argument_list|,
literal|"g-1"
argument_list|,
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid-1"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|problems
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|warning
argument_list|(
literal|"fail to check consistency with group name notes: Unexpected end of config file"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|updateGroupNamesRef (String groupName, String content)
specifier|private
name|void
name|updateGroupNamesRef
parameter_list|(
name|String
name|groupName
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|nameKey
init|=
name|GroupNameNotes
operator|.
name|getNoteKey
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|groupName
argument_list|)
argument_list|)
operator|.
name|getName
argument_list|()
decl_stmt|;
name|GroupTestUtil
operator|.
name|updateGroupFile
argument_list|(
name|allUsersRepo
argument_list|,
name|serverIdent
argument_list|,
name|RefNames
operator|.
name|REFS_GROUPNAMES
argument_list|,
name|nameKey
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

