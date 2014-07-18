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
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|CommentsInNotesUtil
operator|.
name|getCommentPsId
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
name|Lists
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
name|Table
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
name|Account
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
name|PatchSet
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
name|IdentifiedUser
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
name|MetaDataUpdate
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
name|project
operator|.
name|ChangeControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|assistedinject
operator|.
name|AssistedInject
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
name|Date
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_comment
comment|/**  * A single delta to apply atomically to a change.  *<p>  * This delta contains only draft comments on a single patch set of a change by  * a single author. This delta will become a single commit in the All-Users  * repository.  *<p>  * This class is not thread safe.  */
end_comment

begin_class
DECL|class|ChangeDraftUpdate
specifier|public
class|class
name|ChangeDraftUpdate
extends|extends
name|AbstractChangeUpdate
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (ChangeControl ctl, Date when)
name|ChangeDraftUpdate
name|create
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|,
name|Date
name|when
parameter_list|)
function_decl|;
block|}
DECL|field|draftsProject
specifier|private
specifier|final
name|AllUsersName
name|draftsProject
decl_stmt|;
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|commentsUtil
specifier|private
specifier|final
name|CommentsInNotesUtil
name|commentsUtil
decl_stmt|;
DECL|field|changeNotes
specifier|private
specifier|final
name|ChangeNotes
name|changeNotes
decl_stmt|;
DECL|field|draftNotes
specifier|private
specifier|final
name|DraftCommentNotes
name|draftNotes
decl_stmt|;
DECL|field|upsertComments
specifier|private
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|upsertComments
decl_stmt|;
DECL|field|deleteComments
specifier|private
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|deleteComments
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|ChangeDraftUpdate ( @erritPersonIdent PersonIdent serverIdent, GitRepositoryManager repoManager, NotesMigration migration, MetaDataUpdate.User updateFactory, DraftCommentNotes.Factory draftNotesFactory, AllUsersName allUsers, CommentsInNotesUtil commentsUtil, @Assisted ChangeControl ctl, @Assisted Date when)
specifier|private
name|ChangeDraftUpdate
parameter_list|(
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|MetaDataUpdate
operator|.
name|User
name|updateFactory
parameter_list|,
name|DraftCommentNotes
operator|.
name|Factory
name|draftNotesFactory
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|,
name|CommentsInNotesUtil
name|commentsUtil
parameter_list|,
annotation|@
name|Assisted
name|ChangeControl
name|ctl
parameter_list|,
annotation|@
name|Assisted
name|Date
name|when
parameter_list|)
throws|throws
name|OrmException
block|{
name|super
argument_list|(
name|migration
argument_list|,
name|repoManager
argument_list|,
name|updateFactory
argument_list|,
name|ctl
argument_list|,
name|serverIdent
argument_list|,
name|when
argument_list|)
expr_stmt|;
name|this
operator|.
name|draftsProject
operator|=
name|allUsers
expr_stmt|;
name|this
operator|.
name|commentsUtil
operator|=
name|commentsUtil
expr_stmt|;
name|checkState
argument_list|(
name|ctl
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
argument_list|,
literal|"Current user must be identified"
argument_list|)
expr_stmt|;
name|IdentifiedUser
name|user
init|=
operator|(
name|IdentifiedUser
operator|)
name|ctl
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
name|this
operator|.
name|accountId
operator|=
name|user
operator|.
name|getAccountId
argument_list|()
expr_stmt|;
name|this
operator|.
name|changeNotes
operator|=
name|getChangeNotes
argument_list|()
operator|.
name|load
argument_list|()
expr_stmt|;
name|this
operator|.
name|draftNotes
operator|=
name|draftNotesFactory
operator|.
name|create
argument_list|(
name|ctl
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|load
argument_list|()
expr_stmt|;
name|this
operator|.
name|upsertComments
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
name|this
operator|.
name|deleteComments
operator|=
name|Lists
operator|.
name|newArrayList
argument_list|()
expr_stmt|;
block|}
DECL|method|insertComment (PatchLineComment c)
specifier|public
name|void
name|insertComment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|c
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|DRAFT
argument_list|,
literal|"Cannot insert a published comment into a ChangeDraftUpdate"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|changeNotes
operator|.
name|containsComment
argument_list|(
name|c
argument_list|)
argument_list|,
literal|"A comment already exists with the same key,"
operator|+
literal|" so the following comment cannot be inserted: %s"
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|upsertComments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|upsertComment (PatchLineComment c)
specifier|public
name|void
name|upsertComment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|c
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|DRAFT
argument_list|,
literal|"Cannot upsert a published comment into a ChangeDraftUpdate"
argument_list|)
expr_stmt|;
name|upsertComments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|updateComment (PatchLineComment c)
specifier|public
name|void
name|updateComment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
throws|throws
name|OrmException
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|c
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|DRAFT
argument_list|,
literal|"Cannot update a published comment into a ChangeDraftUpdate"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|draftNotes
operator|.
name|containsComment
argument_list|(
name|c
argument_list|)
argument_list|,
literal|"Cannot update this comment because it didn't exist previously"
argument_list|)
expr_stmt|;
name|upsertComments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteComment (PatchLineComment c)
specifier|public
name|void
name|deleteComment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|draftNotes
operator|.
name|containsComment
argument_list|(
name|c
argument_list|)
argument_list|,
literal|"Cannot delete this comment"
operator|+
literal|" because it didn't previously exist as a draft"
argument_list|)
expr_stmt|;
name|deleteComments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
comment|/**    * Deletes a PatchLineComment from the list of drafts only if it existed    * previously as a draft. If it wasn't a draft previously, this is a no-op.    */
DECL|method|deleteCommentIfPresent (PatchLineComment c)
specifier|public
name|void
name|deleteCommentIfPresent
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
if|if
condition|(
name|draftNotes
operator|.
name|containsComment
argument_list|(
name|c
argument_list|)
condition|)
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|deleteComments
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|verifyComment (PatchLineComment comment)
specifier|private
name|void
name|verifyComment
parameter_list|(
name|PatchLineComment
name|comment
parameter_list|)
block|{
name|checkState
argument_list|(
name|psId
operator|!=
literal|null
argument_list|,
literal|"setPatchSetId must be called first"
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|getCommentPsId
argument_list|(
name|comment
argument_list|)
operator|.
name|equals
argument_list|(
name|psId
argument_list|)
argument_list|,
literal|"Comment on %s does not match configured patch set %s"
argument_list|,
name|getCommentPsId
argument_list|(
name|comment
argument_list|)
argument_list|,
name|psId
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|comment
operator|.
name|getRevId
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
name|comment
operator|.
name|getAuthor
argument_list|()
operator|.
name|equals
argument_list|(
name|accountId
argument_list|)
argument_list|,
literal|"The author for the following comment does not match the author of"
operator|+
literal|" this ChangeDraftUpdate (%s): %s"
argument_list|,
name|accountId
argument_list|,
name|comment
argument_list|)
expr_stmt|;
block|}
comment|/** @return the tree id for the updated tree */
DECL|method|storeCommentsInNotes (AtomicBoolean removedAllComments)
specifier|private
name|ObjectId
name|storeCommentsInNotes
parameter_list|(
name|AtomicBoolean
name|removedAllComments
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|NoteMap
name|noteMap
init|=
name|draftNotes
operator|.
name|getNoteMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|noteMap
operator|==
literal|null
condition|)
block|{
name|noteMap
operator|=
name|NoteMap
operator|.
name|newEmptyMap
argument_list|()
expr_stmt|;
block|}
name|Table
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|String
argument_list|,
name|PatchLineComment
argument_list|>
name|baseDrafts
init|=
name|draftNotes
operator|.
name|getDraftBaseComments
argument_list|()
decl_stmt|;
name|Table
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|String
argument_list|,
name|PatchLineComment
argument_list|>
name|psDrafts
init|=
name|draftNotes
operator|.
name|getDraftPsComments
argument_list|()
decl_stmt|;
comment|// There is no need to rewrite the note for one of the sides of the patch
comment|// set if all of the modifications were made to the comments of one side,
comment|// so we set these flags to potentially save that extra work.
name|boolean
name|baseSideChanged
init|=
literal|false
decl_stmt|;
name|boolean
name|revisionSideChanged
init|=
literal|false
decl_stmt|;
comment|// We must define these RevIds so that if this update deletes all
comment|// remaining comments on a given side, then we can remove that note.
comment|// However, if this update doesn't delete any comments, it is okay for these
comment|// to be null because they won't be used.
name|RevId
name|baseRevId
init|=
literal|null
decl_stmt|;
name|RevId
name|psRevId
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PatchLineComment
name|c
range|:
name|deleteComments
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getSide
argument_list|()
operator|==
operator|(
name|short
operator|)
literal|0
condition|)
block|{
name|baseSideChanged
operator|=
literal|true
expr_stmt|;
name|baseRevId
operator|=
name|c
operator|.
name|getRevId
argument_list|()
expr_stmt|;
name|baseDrafts
operator|.
name|remove
argument_list|(
name|psId
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|revisionSideChanged
operator|=
literal|true
expr_stmt|;
name|psRevId
operator|=
name|c
operator|.
name|getRevId
argument_list|()
expr_stmt|;
name|psDrafts
operator|.
name|remove
argument_list|(
name|psId
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|PatchLineComment
name|c
range|:
name|upsertComments
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getSide
argument_list|()
operator|==
operator|(
name|short
operator|)
literal|0
condition|)
block|{
name|baseSideChanged
operator|=
literal|true
expr_stmt|;
name|baseDrafts
operator|.
name|put
argument_list|(
name|psId
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|revisionSideChanged
operator|=
literal|true
expr_stmt|;
name|psDrafts
operator|.
name|put
argument_list|(
name|psId
argument_list|,
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|newBaseDrafts
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|baseDrafts
operator|.
name|row
argument_list|(
name|psId
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|newPsDrafts
init|=
name|Lists
operator|.
name|newArrayList
argument_list|(
name|psDrafts
operator|.
name|row
argument_list|(
name|psId
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|updateNoteMap
argument_list|(
name|baseSideChanged
argument_list|,
name|noteMap
argument_list|,
name|newBaseDrafts
argument_list|,
name|baseRevId
argument_list|)
expr_stmt|;
name|updateNoteMap
argument_list|(
name|revisionSideChanged
argument_list|,
name|noteMap
argument_list|,
name|newPsDrafts
argument_list|,
name|psRevId
argument_list|)
expr_stmt|;
name|removedAllComments
operator|.
name|set
argument_list|(
name|baseDrafts
operator|.
name|isEmpty
argument_list|()
operator|&&
name|psDrafts
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|noteMap
operator|.
name|writeTree
argument_list|(
name|inserter
argument_list|)
return|;
block|}
DECL|method|updateNoteMap (boolean changed, NoteMap noteMap, List<PatchLineComment> comments, RevId commitId)
specifier|private
name|void
name|updateNoteMap
parameter_list|(
name|boolean
name|changed
parameter_list|,
name|NoteMap
name|noteMap
parameter_list|,
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|,
name|RevId
name|commitId
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
if|if
condition|(
name|changed
condition|)
block|{
if|if
condition|(
name|comments
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|commentsUtil
operator|.
name|removeNote
argument_list|(
name|noteMap
argument_list|,
name|commitId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|commentsUtil
operator|.
name|writeCommentsToNoteMap
argument_list|(
name|noteMap
argument_list|,
name|comments
argument_list|,
name|inserter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|commit ()
specifier|public
name|RevCommit
name|commit
parameter_list|()
throws|throws
name|IOException
block|{
name|BatchMetaDataUpdate
name|batch
init|=
name|openUpdate
argument_list|()
decl_stmt|;
try|try
block|{
name|CommitBuilder
name|builder
init|=
operator|new
name|CommitBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|migration
operator|.
name|write
argument_list|()
condition|)
block|{
name|AtomicBoolean
name|removedAllComments
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
name|ObjectId
name|treeId
init|=
name|storeCommentsInNotes
argument_list|(
name|removedAllComments
argument_list|)
decl_stmt|;
if|if
condition|(
name|treeId
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|removedAllComments
operator|.
name|get
argument_list|()
condition|)
block|{
name|batch
operator|.
name|removeRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|setTreeId
argument_list|(
name|treeId
argument_list|)
expr_stmt|;
name|batch
operator|.
name|write
argument_list|(
name|builder
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|batch
operator|.
name|commit
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|batch
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|protected
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
block|{
return|return
name|draftsProject
return|;
block|}
annotation|@
name|Override
DECL|method|getRefName ()
specifier|protected
name|String
name|getRefName
parameter_list|()
block|{
return|return
name|RefNames
operator|.
name|refsDraftComments
argument_list|(
name|accountId
argument_list|,
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onSave (CommitBuilder commit)
specifier|protected
name|boolean
name|onSave
parameter_list|(
name|CommitBuilder
name|commit
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|commit
operator|.
name|setAuthor
argument_list|(
name|newIdent
argument_list|(
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|,
name|when
argument_list|)
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setCommitter
argument_list|(
operator|new
name|PersonIdent
argument_list|(
name|serverIdent
argument_list|,
name|when
argument_list|)
argument_list|)
expr_stmt|;
name|commit
operator|.
name|setMessage
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Comment on patch set %d"
argument_list|,
name|psId
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|isEmpty ()
specifier|private
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|deleteComments
operator|.
name|isEmpty
argument_list|()
operator|&&
name|upsertComments
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

