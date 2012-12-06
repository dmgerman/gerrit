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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|GerritPersonIdent
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
name|extensions
operator|.
name|events
operator|.
name|GitReferenceUpdated
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
name|assistedinject
operator|.
name|Assisted
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
name|api
operator|.
name|errors
operator|.
name|ConcurrentRefUpdateException
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
name|CorruptObjectException
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
name|IncorrectObjectTypeException
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
name|MissingObjectException
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
name|RefUpdate
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
name|RefUpdate
operator|.
name|Result
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
name|merge
operator|.
name|MergeStrategy
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
name|notes
operator|.
name|NoteMapMerger
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
name|NoteMerger
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
name|RevWalk
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

begin_comment
comment|/**  * A utility class for updating a notes branch with automatic merge of note  * trees.  */
end_comment

begin_class
DECL|class|NotesBranchUtil
specifier|public
class|class
name|NotesBranchUtil
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Project.NameKey project, Repository db, ObjectInserter inserter)
name|NotesBranchUtil
name|create
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|Repository
name|db
parameter_list|,
name|ObjectInserter
name|inserter
parameter_list|)
function_decl|;
block|}
DECL|field|MAX_LOCK_FAILURE_CALLS
specifier|private
specifier|static
specifier|final
name|int
name|MAX_LOCK_FAILURE_CALLS
init|=
literal|10
decl_stmt|;
DECL|field|SLEEP_ON_LOCK_FAILURE_MS
specifier|private
specifier|static
specifier|final
name|int
name|SLEEP_ON_LOCK_FAILURE_MS
init|=
literal|25
decl_stmt|;
DECL|field|gerritIdent
specifier|private
specifier|final
name|PersonIdent
name|gerritIdent
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|project
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|project
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Repository
name|db
decl_stmt|;
DECL|field|inserter
specifier|private
specifier|final
name|ObjectInserter
name|inserter
decl_stmt|;
DECL|field|baseCommit
specifier|private
name|RevCommit
name|baseCommit
decl_stmt|;
DECL|field|base
specifier|private
name|NoteMap
name|base
decl_stmt|;
DECL|field|oursCommit
specifier|private
name|RevCommit
name|oursCommit
decl_stmt|;
DECL|field|ours
specifier|private
name|NoteMap
name|ours
decl_stmt|;
DECL|field|revWalk
specifier|private
name|RevWalk
name|revWalk
decl_stmt|;
DECL|field|reader
specifier|private
name|ObjectReader
name|reader
decl_stmt|;
DECL|field|overwrite
specifier|private
name|boolean
name|overwrite
decl_stmt|;
DECL|field|noteMerger
specifier|private
name|ReviewNoteMerger
name|noteMerger
decl_stmt|;
annotation|@
name|Inject
DECL|method|NotesBranchUtil (@erritPersonIdent final PersonIdent gerritIdent, final GitReferenceUpdated gitRefUpdated, @Assisted Project.NameKey project, @Assisted Repository db, @Assisted ObjectInserter inserter)
specifier|public
name|NotesBranchUtil
parameter_list|(
annotation|@
name|GerritPersonIdent
specifier|final
name|PersonIdent
name|gerritIdent
parameter_list|,
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|,
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
annotation|@
name|Assisted
name|Repository
name|db
parameter_list|,
annotation|@
name|Assisted
name|ObjectInserter
name|inserter
parameter_list|)
block|{
name|this
operator|.
name|gerritIdent
operator|=
name|gerritIdent
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
expr_stmt|;
name|this
operator|.
name|project
operator|=
name|project
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|inserter
operator|=
name|inserter
expr_stmt|;
block|}
comment|/**    * Create a new commit in the<code>notesBranch</code> by updating existing    * or creating new notes from the<code>notes</code> map.    *    * @param notes map of notes    * @param notesBranch notes branch to update    * @param commitAuthor author of the commit in the notes branch    * @param commitMessage for the commit in the notes branch    * @throws IOException    * @throws ConcurrentRefUpdateException    */
DECL|method|commitAllNotes (NoteMap notes, String notesBranch, PersonIdent commitAuthor, String commitMessage)
specifier|public
specifier|final
name|void
name|commitAllNotes
parameter_list|(
name|NoteMap
name|notes
parameter_list|,
name|String
name|notesBranch
parameter_list|,
name|PersonIdent
name|commitAuthor
parameter_list|,
name|String
name|commitMessage
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConcurrentRefUpdateException
block|{
name|this
operator|.
name|overwrite
operator|=
literal|true
expr_stmt|;
name|commitNotes
argument_list|(
name|notes
argument_list|,
name|notesBranch
argument_list|,
name|commitAuthor
argument_list|,
name|commitMessage
argument_list|)
expr_stmt|;
block|}
comment|/**    * Create a new commit in the<code>notesBranch</code> by creating not yet    * existing notes from the<code>notes</code> map. The notes from the    *<code>notes</code> map which already exist in the note-tree of the    * tip of the<code>notesBranch</code> will not be updated.    *    * @param notes map of notes    * @param notesBranch notes branch to update    * @param commitAuthor author of the commit in the notes branch    * @param commitMessage for the commit in the notes branch    * @return map with those notes from the<code>notes</code> that were newly    *         created    * @throws IOException    * @throws ConcurrentRefUpdateException    */
DECL|method|commitNewNotes (NoteMap notes, String notesBranch, PersonIdent commitAuthor, String commitMessage)
specifier|public
specifier|final
name|NoteMap
name|commitNewNotes
parameter_list|(
name|NoteMap
name|notes
parameter_list|,
name|String
name|notesBranch
parameter_list|,
name|PersonIdent
name|commitAuthor
parameter_list|,
name|String
name|commitMessage
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConcurrentRefUpdateException
block|{
name|this
operator|.
name|overwrite
operator|=
literal|false
expr_stmt|;
name|commitNotes
argument_list|(
name|notes
argument_list|,
name|notesBranch
argument_list|,
name|commitAuthor
argument_list|,
name|commitMessage
argument_list|)
expr_stmt|;
name|NoteMap
name|newlyCreated
init|=
name|NoteMap
operator|.
name|newEmptyMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Note
name|n
range|:
name|notes
control|)
block|{
if|if
condition|(
name|base
operator|==
literal|null
operator|||
operator|!
name|base
operator|.
name|contains
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|newlyCreated
operator|.
name|set
argument_list|(
name|n
argument_list|,
name|n
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|newlyCreated
return|;
block|}
DECL|method|commitNotes (NoteMap notes, String notesBranch, PersonIdent commitAuthor, String commitMessage)
specifier|private
name|void
name|commitNotes
parameter_list|(
name|NoteMap
name|notes
parameter_list|,
name|String
name|notesBranch
parameter_list|,
name|PersonIdent
name|commitAuthor
parameter_list|,
name|String
name|commitMessage
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConcurrentRefUpdateException
block|{
try|try
block|{
name|revWalk
operator|=
operator|new
name|RevWalk
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|reader
operator|=
name|db
operator|.
name|newObjectReader
argument_list|()
expr_stmt|;
name|loadBase
argument_list|(
name|notesBranch
argument_list|)
expr_stmt|;
if|if
condition|(
name|overwrite
condition|)
block|{
name|addAllNotes
argument_list|(
name|notes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|addNewNotes
argument_list|(
name|notes
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|base
operator|!=
literal|null
condition|)
block|{
name|oursCommit
operator|=
name|createCommit
argument_list|(
name|ours
argument_list|,
name|commitAuthor
argument_list|,
name|commitMessage
argument_list|,
name|baseCommit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|oursCommit
operator|=
name|createCommit
argument_list|(
name|ours
argument_list|,
name|commitAuthor
argument_list|,
name|commitMessage
argument_list|)
expr_stmt|;
block|}
name|updateRef
argument_list|(
name|notesBranch
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|revWalk
operator|.
name|release
argument_list|()
expr_stmt|;
name|reader
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|addNewNotes (NoteMap notes)
specifier|private
name|void
name|addNewNotes
parameter_list|(
name|NoteMap
name|notes
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Note
name|n
range|:
name|notes
control|)
block|{
if|if
condition|(
operator|!
name|ours
operator|.
name|contains
argument_list|(
name|n
argument_list|)
condition|)
block|{
name|ours
operator|.
name|set
argument_list|(
name|n
argument_list|,
name|n
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addAllNotes (NoteMap notes)
specifier|private
name|void
name|addAllNotes
parameter_list|(
name|NoteMap
name|notes
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Note
name|n
range|:
name|notes
control|)
block|{
if|if
condition|(
name|ours
operator|.
name|contains
argument_list|(
name|n
argument_list|)
condition|)
block|{
comment|// Merge the existing and the new note as if they are both new,
comment|// means: base == null
comment|// There is no really a common ancestry for these two note revisions
name|ObjectId
name|noteContent
init|=
name|getNoteMerger
argument_list|()
operator|.
name|merge
argument_list|(
literal|null
argument_list|,
name|n
argument_list|,
name|ours
operator|.
name|getNote
argument_list|(
name|n
argument_list|)
argument_list|,
name|reader
argument_list|,
name|inserter
argument_list|)
operator|.
name|getData
argument_list|()
decl_stmt|;
name|ours
operator|.
name|set
argument_list|(
name|n
argument_list|,
name|noteContent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ours
operator|.
name|set
argument_list|(
name|n
argument_list|,
name|n
operator|.
name|getData
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getNoteMerger ()
specifier|private
name|NoteMerger
name|getNoteMerger
parameter_list|()
block|{
if|if
condition|(
name|noteMerger
operator|==
literal|null
condition|)
block|{
name|noteMerger
operator|=
operator|new
name|ReviewNoteMerger
argument_list|()
expr_stmt|;
block|}
return|return
name|noteMerger
return|;
block|}
DECL|method|loadBase (String notesBranch)
specifier|private
name|void
name|loadBase
parameter_list|(
name|String
name|notesBranch
parameter_list|)
throws|throws
name|IOException
block|{
name|Ref
name|branch
init|=
name|db
operator|.
name|getRef
argument_list|(
name|notesBranch
argument_list|)
decl_stmt|;
if|if
condition|(
name|branch
operator|!=
literal|null
condition|)
block|{
name|baseCommit
operator|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|branch
operator|.
name|getObjectId
argument_list|()
argument_list|)
expr_stmt|;
name|base
operator|=
name|NoteMap
operator|.
name|read
argument_list|(
name|revWalk
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|baseCommit
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|baseCommit
operator|!=
literal|null
condition|)
block|{
name|ours
operator|=
name|NoteMap
operator|.
name|read
argument_list|(
name|revWalk
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|baseCommit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ours
operator|=
name|NoteMap
operator|.
name|newEmptyMap
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createCommit (NoteMap map, PersonIdent author, String message, RevCommit... parents)
specifier|private
name|RevCommit
name|createCommit
parameter_list|(
name|NoteMap
name|map
parameter_list|,
name|PersonIdent
name|author
parameter_list|,
name|String
name|message
parameter_list|,
name|RevCommit
modifier|...
name|parents
parameter_list|)
throws|throws
name|IOException
block|{
name|CommitBuilder
name|b
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|setTreeId
argument_list|(
name|map
operator|.
name|writeTree
argument_list|(
name|inserter
argument_list|)
argument_list|)
expr_stmt|;
name|b
operator|.
name|setAuthor
argument_list|(
name|author
operator|!=
literal|null
condition|?
name|author
else|:
name|gerritIdent
argument_list|)
expr_stmt|;
name|b
operator|.
name|setCommitter
argument_list|(
name|gerritIdent
argument_list|)
expr_stmt|;
if|if
condition|(
name|parents
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|setParentIds
argument_list|(
name|parents
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|setMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|ObjectId
name|commitId
init|=
name|inserter
operator|.
name|insert
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|inserter
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|commitId
argument_list|)
return|;
block|}
DECL|method|updateRef (String notesBranch)
specifier|private
name|void
name|updateRef
parameter_list|(
name|String
name|notesBranch
parameter_list|)
throws|throws
name|IOException
throws|,
name|MissingObjectException
throws|,
name|IncorrectObjectTypeException
throws|,
name|CorruptObjectException
throws|,
name|ConcurrentRefUpdateException
block|{
if|if
condition|(
name|baseCommit
operator|!=
literal|null
operator|&&
name|oursCommit
operator|.
name|getTree
argument_list|()
operator|.
name|equals
argument_list|(
name|baseCommit
operator|.
name|getTree
argument_list|()
argument_list|)
condition|)
block|{
comment|// If the trees are identical, there is no change in the notes.
comment|// Avoid saving this commit as it has no new information.
return|return;
block|}
name|int
name|remainingLockFailureCalls
init|=
name|MAX_LOCK_FAILURE_CALLS
decl_stmt|;
name|RefUpdate
name|refUpdate
init|=
name|createRefUpdate
argument_list|(
name|notesBranch
argument_list|,
name|oursCommit
argument_list|,
name|baseCommit
argument_list|)
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|Result
name|result
init|=
name|refUpdate
operator|.
name|update
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|==
name|Result
operator|.
name|LOCK_FAILURE
condition|)
block|{
if|if
condition|(
operator|--
name|remainingLockFailureCalls
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|SLEEP_ON_LOCK_FAILURE_MS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|ConcurrentRefUpdateException
argument_list|(
literal|"Failed to lock the ref: "
operator|+
name|notesBranch
argument_list|,
name|db
operator|.
name|getRef
argument_list|(
name|notesBranch
argument_list|)
argument_list|,
name|result
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|result
operator|==
name|Result
operator|.
name|REJECTED
condition|)
block|{
name|RevCommit
name|theirsCommit
init|=
name|revWalk
operator|.
name|parseCommit
argument_list|(
name|refUpdate
operator|.
name|getOldObjectId
argument_list|()
argument_list|)
decl_stmt|;
name|NoteMap
name|theirs
init|=
name|NoteMap
operator|.
name|read
argument_list|(
name|revWalk
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|theirsCommit
argument_list|)
decl_stmt|;
name|NoteMapMerger
name|merger
init|=
operator|new
name|NoteMapMerger
argument_list|(
name|db
argument_list|,
name|getNoteMerger
argument_list|()
argument_list|,
name|MergeStrategy
operator|.
name|RESOLVE
argument_list|)
decl_stmt|;
name|NoteMap
name|merged
init|=
name|merger
operator|.
name|merge
argument_list|(
name|base
argument_list|,
name|ours
argument_list|,
name|theirs
argument_list|)
decl_stmt|;
name|RevCommit
name|mergeCommit
init|=
name|createCommit
argument_list|(
name|merged
argument_list|,
name|gerritIdent
argument_list|,
literal|"Merged note commits\n"
argument_list|,
name|theirsCommit
argument_list|,
name|oursCommit
argument_list|)
decl_stmt|;
name|refUpdate
operator|=
name|createRefUpdate
argument_list|(
name|notesBranch
argument_list|,
name|mergeCommit
argument_list|,
name|theirsCommit
argument_list|)
expr_stmt|;
name|remainingLockFailureCalls
operator|=
name|MAX_LOCK_FAILURE_CALLS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|==
name|Result
operator|.
name|IO_FAILURE
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Couldn't update "
operator|+
name|notesBranch
operator|+
literal|". "
operator|+
name|result
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
else|else
block|{
name|gitRefUpdated
operator|.
name|fire
argument_list|(
name|project
argument_list|,
name|refUpdate
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|method|createRefUpdate (String notesBranch, ObjectId newObjectId, ObjectId expectedOldObjectId)
specifier|private
name|RefUpdate
name|createRefUpdate
parameter_list|(
name|String
name|notesBranch
parameter_list|,
name|ObjectId
name|newObjectId
parameter_list|,
name|ObjectId
name|expectedOldObjectId
parameter_list|)
throws|throws
name|IOException
block|{
name|RefUpdate
name|refUpdate
init|=
name|db
operator|.
name|updateRef
argument_list|(
name|notesBranch
argument_list|)
decl_stmt|;
name|refUpdate
operator|.
name|setNewObjectId
argument_list|(
name|newObjectId
argument_list|)
expr_stmt|;
if|if
condition|(
name|expectedOldObjectId
operator|==
literal|null
condition|)
block|{
name|refUpdate
operator|.
name|setExpectedOldObjectId
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|refUpdate
operator|.
name|setExpectedOldObjectId
argument_list|(
name|expectedOldObjectId
argument_list|)
expr_stmt|;
block|}
return|return
name|refUpdate
return|;
block|}
block|}
end_class

end_unit

