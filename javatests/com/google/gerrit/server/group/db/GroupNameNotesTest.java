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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assert_
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
name|common
operator|.
name|testing
operator|.
name|CommitInfoSubject
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
name|reviewdb
operator|.
name|client
operator|.
name|RefNames
operator|.
name|REFS_GROUPNAMES
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
name|OBJ_BLOB
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
name|gerrit
operator|.
name|common
operator|.
name|TimeUtil
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
name|common
operator|.
name|data
operator|.
name|GroupReference
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
name|CommitInfo
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
name|server
operator|.
name|config
operator|.
name|AllUsersNameProvider
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
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
operator|.
name|RefUpdateUtil
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
name|TestTimeUtil
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|BatchRefUpdate
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
name|ObjectInserter
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
name|ObjectReader
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
name|PersonIdent
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
name|notes
operator|.
name|NoteMap
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
name|RevWalk
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
DECL|class|GroupNameNotesTest
specifier|public
class|class
name|GroupNameNotesTest
extends|extends
name|GerritBaseTests
block|{
DECL|field|SERVER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|SERVER_NAME
init|=
literal|"Gerrit Server"
decl_stmt|;
DECL|field|SERVER_EMAIL
specifier|private
specifier|static
specifier|final
name|String
name|SERVER_EMAIL
init|=
literal|"noreply@gerritcodereview.com"
decl_stmt|;
DECL|field|TZ
specifier|private
specifier|static
specifier|final
name|TimeZone
name|TZ
init|=
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"America/Los_Angeles"
argument_list|)
decl_stmt|;
DECL|field|idCounter
specifier|private
name|AtomicInteger
name|idCounter
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
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|idCounter
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
name|repo
operator|=
operator|new
name|InMemoryRepository
argument_list|(
operator|new
name|DfsRepositoryDescription
argument_list|(
name|AllUsersNameProvider
operator|.
name|DEFAULT
argument_list|)
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
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateGroupNames ()
specifier|public
name|void
name|updateGroupNames
parameter_list|()
throws|throws
name|Exception
block|{
name|GroupReference
name|g1
init|=
name|newGroup
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|GroupReference
name|g2
init|=
name|newGroup
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|newPersonIdent
argument_list|()
decl_stmt|;
name|updateGroupNames
argument_list|(
name|ident
argument_list|,
name|g1
argument_list|,
name|g2
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
init|=
name|log
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|parents
argument_list|()
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Store 2 group names"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|matches
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|matches
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|GroupTestUtil
operator|.
name|readNameToUuidMap
argument_list|(
name|repo
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"a-1"
argument_list|,
literal|"b"
argument_list|,
literal|"b-2"
argument_list|)
expr_stmt|;
comment|// Updating the same set of names is a no-op.
name|String
name|commit
init|=
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|commit
decl_stmt|;
name|updateGroupNames
argument_list|(
name|newPersonIdent
argument_list|()
argument_list|,
name|g1
argument_list|,
name|g2
argument_list|)
expr_stmt|;
name|log
operator|=
name|log
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|commit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateGroupNamesOverwritesExistingNotes ()
specifier|public
name|void
name|updateGroupNamesOverwritesExistingNotes
parameter_list|()
throws|throws
name|Exception
block|{
name|GroupReference
name|g1
init|=
name|newGroup
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|GroupReference
name|g2
init|=
name|newGroup
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|TestRepository
argument_list|<
name|?
argument_list|>
name|tr
init|=
operator|new
name|TestRepository
argument_list|<>
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|ObjectId
name|k1
init|=
name|getNoteKey
argument_list|(
name|g1
argument_list|)
decl_stmt|;
name|ObjectId
name|k2
init|=
name|getNoteKey
argument_list|(
name|g2
argument_list|)
decl_stmt|;
name|ObjectId
name|k3
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
literal|"c"
argument_list|)
argument_list|)
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|newPersonIdent
argument_list|()
decl_stmt|;
name|ObjectId
name|origCommitId
init|=
name|tr
operator|.
name|branch
argument_list|(
name|REFS_GROUPNAMES
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|message
argument_list|(
literal|"Prepopulate group name"
argument_list|)
operator|.
name|author
argument_list|(
name|ident
argument_list|)
operator|.
name|committer
argument_list|(
name|ident
argument_list|)
operator|.
name|add
argument_list|(
name|k1
operator|.
name|name
argument_list|()
argument_list|,
literal|"[group]\n\tuuid = a-1\n\tname = a\nanotherKey = foo\n"
argument_list|)
operator|.
name|add
argument_list|(
name|k2
operator|.
name|name
argument_list|()
argument_list|,
literal|"[group]\n\tuuid = a-1\n\tname = b\n"
argument_list|)
operator|.
name|add
argument_list|(
name|k3
operator|.
name|name
argument_list|()
argument_list|,
literal|"[group]\n\tuuid = c-3\n\tname = c\n"
argument_list|)
operator|.
name|create
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|ident
operator|=
name|newPersonIdent
argument_list|()
expr_stmt|;
name|updateGroupNames
argument_list|(
name|ident
argument_list|,
name|g1
argument_list|,
name|g2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|GroupTestUtil
operator|.
name|readNameToUuidMap
argument_list|(
name|repo
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"a-1"
argument_list|,
literal|"b"
argument_list|,
literal|"b-2"
argument_list|)
expr_stmt|;
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
init|=
name|log
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|commit
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|origCommitId
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Store 2 group names"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|author
argument_list|()
operator|.
name|matches
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|committer
argument_list|()
operator|.
name|matches
argument_list|(
name|ident
argument_list|)
expr_stmt|;
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|repo
argument_list|)
init|)
block|{
name|ObjectReader
name|reader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|NoteMap
name|noteMap
init|=
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|commit
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|note
init|=
operator|new
name|String
argument_list|(
name|reader
operator|.
name|open
argument_list|(
name|noteMap
operator|.
name|get
argument_list|(
name|k1
argument_list|)
argument_list|,
name|OBJ_BLOB
argument_list|)
operator|.
name|getCachedBytes
argument_list|()
argument_list|,
name|UTF_8
argument_list|)
decl_stmt|;
comment|// Old note content was overwritten.
name|assertThat
argument_list|(
name|note
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"[group]\n\tuuid = a-1\n\tname = a\n"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|updateGroupNamesWithEmptyCollectionClearsAllNotes ()
specifier|public
name|void
name|updateGroupNamesWithEmptyCollectionClearsAllNotes
parameter_list|()
throws|throws
name|Exception
block|{
name|GroupReference
name|g1
init|=
name|newGroup
argument_list|(
literal|"a"
argument_list|)
decl_stmt|;
name|GroupReference
name|g2
init|=
name|newGroup
argument_list|(
literal|"b"
argument_list|)
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|newPersonIdent
argument_list|()
decl_stmt|;
name|updateGroupNames
argument_list|(
name|ident
argument_list|,
name|g1
argument_list|,
name|g2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|GroupTestUtil
operator|.
name|readNameToUuidMap
argument_list|(
name|repo
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"a"
argument_list|,
literal|"a-1"
argument_list|,
literal|"b"
argument_list|,
literal|"b-2"
argument_list|)
expr_stmt|;
name|updateGroupNames
argument_list|(
name|ident
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|GroupTestUtil
operator|.
name|readNameToUuidMap
argument_list|(
name|repo
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
init|=
name|log
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|log
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|log
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|message
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"Store 0 group names"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateGroupNamesRejectsNonOneToOneGroupReferences ()
specifier|public
name|void
name|updateGroupNamesRejectsNonOneToOneGroupReferences
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIllegalArgument
argument_list|(
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid1"
argument_list|)
argument_list|,
literal|"name1"
argument_list|)
argument_list|,
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid1"
argument_list|)
argument_list|,
literal|"name2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertIllegalArgument
argument_list|(
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid1"
argument_list|)
argument_list|,
literal|"name1"
argument_list|)
argument_list|,
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid2"
argument_list|)
argument_list|,
literal|"name1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertIllegalArgument
argument_list|(
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid1"
argument_list|)
argument_list|,
literal|"name1"
argument_list|)
argument_list|,
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
literal|"uuid1"
argument_list|)
argument_list|,
literal|"name1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|newGroup (String name)
specifier|private
name|GroupReference
name|newGroup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|int
name|id
init|=
name|idCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
return|return
operator|new
name|GroupReference
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|name
operator|+
literal|"-"
operator|+
name|id
argument_list|)
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|newPersonIdent ()
specifier|private
specifier|static
name|PersonIdent
name|newPersonIdent
parameter_list|()
block|{
return|return
operator|new
name|PersonIdent
argument_list|(
name|SERVER_NAME
argument_list|,
name|SERVER_EMAIL
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|,
name|TZ
argument_list|)
return|;
block|}
DECL|method|getNoteKey (GroupReference g)
specifier|private
specifier|static
name|ObjectId
name|getNoteKey
parameter_list|(
name|GroupReference
name|g
parameter_list|)
block|{
return|return
name|GroupNameNotes
operator|.
name|getNoteKey
argument_list|(
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|g
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|updateGroupNames (PersonIdent ident, GroupReference... groupRefs)
specifier|private
name|void
name|updateGroupNames
parameter_list|(
name|PersonIdent
name|ident
parameter_list|,
name|GroupReference
modifier|...
name|groupRefs
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ObjectInserter
name|inserter
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|BatchRefUpdate
name|bru
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|GroupNameNotes
operator|.
name|updateGroupNames
argument_list|(
name|repo
argument_list|,
name|inserter
argument_list|,
name|bru
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|groupRefs
argument_list|)
argument_list|,
name|ident
argument_list|)
expr_stmt|;
name|inserter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|RefUpdateUtil
operator|.
name|executeChecked
argument_list|(
name|bru
argument_list|,
name|repo
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertIllegalArgument (GroupReference... groupRefs)
specifier|private
name|void
name|assertIllegalArgument
parameter_list|(
name|GroupReference
modifier|...
name|groupRefs
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ObjectInserter
name|inserter
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|)
block|{
name|BatchRefUpdate
name|bru
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
decl_stmt|;
name|PersonIdent
name|ident
init|=
name|newPersonIdent
argument_list|()
decl_stmt|;
try|try
block|{
name|GroupNameNotes
operator|.
name|updateGroupNames
argument_list|(
name|repo
argument_list|,
name|inserter
argument_list|,
name|bru
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|groupRefs
argument_list|)
argument_list|,
name|ident
argument_list|)
expr_stmt|;
name|assert_
argument_list|()
operator|.
name|fail
argument_list|(
literal|"Expected IllegalArgumentException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
argument_list|)
operator|.
name|hasMessageThat
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|GroupNameNotes
operator|.
name|UNIQUE_REF_ERROR
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|log ()
specifier|private
name|ImmutableList
argument_list|<
name|CommitInfo
argument_list|>
name|log
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|GroupTestUtil
operator|.
name|log
argument_list|(
name|repo
argument_list|,
name|REFS_GROUPNAMES
argument_list|)
return|;
block|}
block|}
end_class

end_unit
