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
DECL|package|com.google.gerrit.server.notedb
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
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
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
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
name|server
operator|.
name|notedb
operator|.
name|RevisionNote
operator|.
name|MAX_NOTE_SZ
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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|git
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
name|reviewdb
operator|.
name|client
operator|.
name|Change
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
name|PatchLineComment
operator|.
name|Status
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
name|reviewdb
operator|.
name|client
operator|.
name|RevId
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
name|AllUsersName
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
name|GerritServerIdProvider
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
name|Singleton
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
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
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
name|internal
operator|.
name|storage
operator|.
name|file
operator|.
name|PackInserter
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
name|CommitBuilder
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
name|Ref
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
name|Note
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
name|revwalk
operator|.
name|RevSort
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
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|MutableInteger
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|CommentJsonMigrator
specifier|public
class|class
name|CommentJsonMigrator
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|class|ProjectMigrationResult
specifier|public
specifier|static
class|class
name|ProjectMigrationResult
block|{
DECL|field|skipped
specifier|public
name|int
name|skipped
decl_stmt|;
DECL|field|ok
specifier|public
name|boolean
name|ok
decl_stmt|;
DECL|field|refsUpdated
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|refsUpdated
decl_stmt|;
block|}
DECL|field|legacyChangeNoteRead
specifier|private
specifier|final
name|LegacyChangeNoteRead
name|legacyChangeNoteRead
decl_stmt|;
DECL|field|changeNoteJson
specifier|private
specifier|final
name|ChangeNoteJson
name|changeNoteJson
decl_stmt|;
DECL|field|allUsers
specifier|private
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
annotation|@
name|Inject
DECL|method|CommentJsonMigrator ( ChangeNoteJson changeNoteJson, GerritServerIdProvider gerritServerIdProvider, AllUsersName allUsers)
name|CommentJsonMigrator
parameter_list|(
name|ChangeNoteJson
name|changeNoteJson
parameter_list|,
name|GerritServerIdProvider
name|gerritServerIdProvider
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|changeNoteJson
operator|=
name|changeNoteJson
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
name|this
operator|.
name|legacyChangeNoteRead
operator|=
operator|new
name|LegacyChangeNoteRead
argument_list|(
name|gerritServerIdProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|CommentJsonMigrator (ChangeNoteJson changeNoteJson, String serverId, AllUsersName allUsers)
name|CommentJsonMigrator
parameter_list|(
name|ChangeNoteJson
name|changeNoteJson
parameter_list|,
name|String
name|serverId
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|changeNoteJson
operator|=
name|changeNoteJson
expr_stmt|;
name|this
operator|.
name|legacyChangeNoteRead
operator|=
operator|new
name|LegacyChangeNoteRead
argument_list|(
name|serverId
argument_list|)
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
block|}
DECL|method|migrateProject ( Project.NameKey project, Repository repo, boolean dryRun)
specifier|public
name|ProjectMigrationResult
name|migrateProject
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|boolean
name|dryRun
parameter_list|)
block|{
name|ProjectMigrationResult
name|progress
init|=
operator|new
name|ProjectMigrationResult
argument_list|()
decl_stmt|;
name|progress
operator|.
name|ok
operator|=
literal|true
expr_stmt|;
name|progress
operator|.
name|skipped
operator|=
literal|0
expr_stmt|;
name|progress
operator|.
name|refsUpdated
operator|=
name|ImmutableList
operator|.
name|of
argument_list|()
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
init|;
name|ObjectInserter
name|ins
operator|=
name|newPackInserter
argument_list|(
name|repo
argument_list|)
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
name|bru
operator|.
name|setAllowNonFastForwards
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|progress
operator|.
name|ok
operator|&=
name|migrateChanges
argument_list|(
name|project
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|,
name|ins
argument_list|,
name|bru
argument_list|)
expr_stmt|;
if|if
condition|(
name|project
operator|.
name|equals
argument_list|(
name|allUsers
argument_list|)
condition|)
block|{
name|progress
operator|.
name|ok
operator|&=
name|migrateDrafts
argument_list|(
name|allUsers
argument_list|,
name|repo
argument_list|,
name|rw
argument_list|,
name|ins
argument_list|,
name|bru
argument_list|)
expr_stmt|;
block|}
name|progress
operator|.
name|refsUpdated
operator|=
name|bru
operator|.
name|getCommands
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getRefName
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|bru
operator|.
name|getCommands
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|dryRun
condition|)
block|{
name|ins
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
name|rw
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|progress
operator|.
name|skipped
operator|++
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|progress
operator|.
name|ok
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|progress
return|;
block|}
DECL|method|migrateChanges ( Project.NameKey project, Repository repo, RevWalk rw, ObjectInserter ins, BatchRefUpdate bru)
specifier|private
name|boolean
name|migrateChanges
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|BatchRefUpdate
name|bru
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|RefNames
operator|.
name|REFS_CHANGES
argument_list|)
control|)
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|Id
operator|.
name|fromRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|changeId
operator|==
literal|null
operator|||
operator|!
name|ref
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|RefNames
operator|.
name|changeMetaRef
argument_list|(
name|changeId
argument_list|)
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|ok
operator|&=
name|migrateOne
argument_list|(
name|project
argument_list|,
name|rw
argument_list|,
name|ins
argument_list|,
name|bru
argument_list|,
name|Status
operator|.
name|PUBLISHED
argument_list|,
name|changeId
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
DECL|method|migrateDrafts ( Project.NameKey allUsers, Repository allUsersRepo, RevWalk rw, ObjectInserter ins, BatchRefUpdate bru)
specifier|private
name|boolean
name|migrateDrafts
parameter_list|(
name|Project
operator|.
name|NameKey
name|allUsers
parameter_list|,
name|Repository
name|allUsersRepo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|BatchRefUpdate
name|bru
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
for|for
control|(
name|Ref
name|ref
range|:
name|allUsersRepo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|getRefsByPrefix
argument_list|(
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
argument_list|)
control|)
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|Id
operator|.
name|fromAllUsersRef
argument_list|(
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|changeId
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|ok
operator|&=
name|migrateOne
argument_list|(
name|allUsers
argument_list|,
name|rw
argument_list|,
name|ins
argument_list|,
name|bru
argument_list|,
name|Status
operator|.
name|DRAFT
argument_list|,
name|changeId
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
return|return
name|ok
return|;
block|}
DECL|method|migrateOne ( Project.NameKey project, RevWalk rw, ObjectInserter ins, BatchRefUpdate bru, Status status, Change.Id changeId, Ref ref)
specifier|private
name|boolean
name|migrateOne
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|BatchRefUpdate
name|bru
parameter_list|,
name|Status
name|status
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Ref
name|ref
parameter_list|)
block|{
name|ObjectId
name|oldId
init|=
name|ref
operator|.
name|getObjectId
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|hasAnyLegacyComments
argument_list|(
name|rw
argument_list|,
name|oldId
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error reading change %s in %s; attempting migration anyway"
argument_list|,
name|changeId
argument_list|,
name|project
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|reset
argument_list|(
name|rw
argument_list|,
name|oldId
argument_list|)
expr_stmt|;
name|ObjectReader
name|reader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|ObjectId
name|newId
init|=
literal|null
decl_stmt|;
name|RevCommit
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|CommitBuilder
name|cb
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|cb
operator|.
name|setAuthor
argument_list|(
name|c
operator|.
name|getAuthorIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setCommitter
argument_list|(
name|c
operator|.
name|getCommitterIdent
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setMessage
argument_list|(
name|c
operator|.
name|getFullMessage
argument_list|()
argument_list|)
expr_stmt|;
name|cb
operator|.
name|setEncoding
argument_list|(
name|c
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|newId
operator|!=
literal|null
condition|)
block|{
name|cb
operator|.
name|setParentId
argument_list|(
name|newId
argument_list|)
expr_stmt|;
block|}
comment|// Read/write using the low-level RevisionNote API, which works regardless of NotesMigration
comment|// state.
name|NoteMap
name|noteMap
init|=
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|c
argument_list|)
decl_stmt|;
name|RevisionNoteMap
argument_list|<
name|ChangeRevisionNote
argument_list|>
name|revNoteMap
init|=
name|RevisionNoteMap
operator|.
name|parse
argument_list|(
name|changeNoteJson
argument_list|,
name|legacyChangeNoteRead
argument_list|,
name|changeId
argument_list|,
name|reader
argument_list|,
name|noteMap
argument_list|,
name|status
argument_list|)
decl_stmt|;
name|RevisionNoteBuilder
operator|.
name|Cache
name|cache
init|=
operator|new
name|RevisionNoteBuilder
operator|.
name|Cache
argument_list|(
name|revNoteMap
argument_list|)
decl_stmt|;
for|for
control|(
name|RevId
name|revId
range|:
name|revNoteMap
operator|.
name|revisionNotes
operator|.
name|keySet
argument_list|()
control|)
block|{
comment|// Call cache.get on each known RevId to read the old note in whichever format, then write
comment|// the note in JSON format.
name|byte
index|[]
name|data
init|=
name|cache
operator|.
name|get
argument_list|(
name|revId
argument_list|)
operator|.
name|build
argument_list|(
name|changeNoteJson
argument_list|)
decl_stmt|;
name|noteMap
operator|.
name|set
argument_list|(
name|ObjectId
operator|.
name|fromString
argument_list|(
name|revId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|,
name|ins
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|cb
operator|.
name|setTreeId
argument_list|(
name|noteMap
operator|.
name|writeTree
argument_list|(
name|ins
argument_list|)
argument_list|)
expr_stmt|;
name|newId
operator|=
name|ins
operator|.
name|insert
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
name|bru
operator|.
name|addCommand
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|oldId
argument_list|,
name|newId
argument_list|,
name|ref
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|atInfo
argument_list|()
operator|.
name|log
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error migrating change %s in %s"
argument_list|,
name|changeId
argument_list|,
name|project
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
DECL|method|hasAnyLegacyComments (RevWalk rw, ObjectId id)
specifier|private
specifier|static
name|boolean
name|hasAnyLegacyComments
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectReader
name|reader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|reset
argument_list|(
name|rw
argument_list|,
name|id
argument_list|)
expr_stmt|;
comment|// Check the note map at each commit, not just the tip. It's possible that the server switched
comment|// from legacy to JSON partway through its history, which would have mixed legacy/JSON comments
comment|// in its history. Although the tip commit would continue to parse once we remove the legacy
comment|// parser, our goal is really to expunge all vestiges of the old format, which implies rewriting
comment|// history (and thus returning true) in this case.
name|RevCommit
name|c
decl_stmt|;
while|while
condition|(
operator|(
name|c
operator|=
name|rw
operator|.
name|next
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|NoteMap
name|noteMap
init|=
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|c
argument_list|)
decl_stmt|;
for|for
control|(
name|Note
name|note
range|:
name|noteMap
control|)
block|{
comment|// Match pre-parsing logic in RevisionNote#parse().
name|byte
index|[]
name|raw
init|=
name|reader
operator|.
name|open
argument_list|(
name|note
operator|.
name|getData
argument_list|()
argument_list|,
name|OBJ_BLOB
argument_list|)
operator|.
name|getCachedBytes
argument_list|(
name|MAX_NOTE_SZ
argument_list|)
decl_stmt|;
name|MutableInteger
name|p
init|=
operator|new
name|MutableInteger
argument_list|()
decl_stmt|;
name|RevisionNote
operator|.
name|trimLeadingEmptyLines
argument_list|(
name|raw
argument_list|,
name|p
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ChangeRevisionNote
operator|.
name|isJson
argument_list|(
name|raw
argument_list|,
name|p
operator|.
name|value
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|reset (RevWalk rw, ObjectId id)
specifier|private
specifier|static
name|void
name|reset
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectId
name|id
parameter_list|)
throws|throws
name|IOException
block|{
name|rw
operator|.
name|reset
argument_list|()
expr_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|TOPO
argument_list|)
expr_stmt|;
name|rw
operator|.
name|sort
argument_list|(
name|RevSort
operator|.
name|REVERSE
argument_list|)
expr_stmt|;
name|rw
operator|.
name|markStart
argument_list|(
name|rw
operator|.
name|parseCommit
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|newPackInserter (Repository repo)
specifier|private
specifier|static
name|ObjectInserter
name|newPackInserter
parameter_list|(
name|Repository
name|repo
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|repo
operator|instanceof
name|FileRepository
operator|)
condition|)
block|{
return|return
name|repo
operator|.
name|newObjectInserter
argument_list|()
return|;
block|}
name|PackInserter
name|ins
init|=
operator|(
operator|(
name|FileRepository
operator|)
name|repo
operator|)
operator|.
name|getObjectDatabase
argument_list|()
operator|.
name|newPackInserter
argument_list|()
decl_stmt|;
name|ins
operator|.
name|checkExisting
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|ins
return|;
block|}
block|}
end_class

end_unit

