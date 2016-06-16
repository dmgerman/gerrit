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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|annotations
operator|.
name|VisibleForTesting
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
name|ArrayListMultimap
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
name|ImmutableListMultimap
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
name|Multimap
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
name|git
operator|.
name|RepoRefCache
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
name|NoSuchChangeException
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

begin_comment
comment|/**  * View of the draft comments for a single {@link Change} based on the log of  * its drafts branch.  */
end_comment

begin_class
DECL|class|DraftCommentNotes
specifier|public
class|class
name|DraftCommentNotes
extends|extends
name|AbstractChangeNotes
argument_list|<
name|DraftCommentNotes
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (Change change, Account.Id accountId)
name|DraftCommentNotes
name|create
parameter_list|(
name|Change
name|change
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
DECL|method|createWithAutoRebuildingDisabled ( Change.Id changeId, Account.Id accountId)
name|DraftCommentNotes
name|createWithAutoRebuildingDisabled
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
function_decl|;
block|}
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|author
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|author
decl_stmt|;
DECL|field|comments
specifier|private
name|ImmutableListMultimap
argument_list|<
name|RevId
argument_list|,
name|PatchLineComment
argument_list|>
name|comments
decl_stmt|;
DECL|field|revisionNoteMap
specifier|private
name|RevisionNoteMap
name|revisionNoteMap
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|DraftCommentNotes ( Args args, @Assisted Change change, @Assisted Account.Id author)
name|DraftCommentNotes
parameter_list|(
name|Args
name|args
parameter_list|,
annotation|@
name|Assisted
name|Change
name|change
parameter_list|,
annotation|@
name|Assisted
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
name|this
argument_list|(
name|args
argument_list|,
name|change
argument_list|,
name|author
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|DraftCommentNotes ( Args args, @Assisted Change.Id changeId, @Assisted Account.Id author)
name|DraftCommentNotes
parameter_list|(
name|Args
name|args
parameter_list|,
annotation|@
name|Assisted
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Assisted
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
name|changeId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
block|}
DECL|method|DraftCommentNotes ( Args args, Change change, Account.Id author, boolean autoRebuild)
name|DraftCommentNotes
parameter_list|(
name|Args
name|args
parameter_list|,
name|Change
name|change
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|,
name|boolean
name|autoRebuild
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|autoRebuild
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
block|}
DECL|method|getRevisionNoteMap ()
name|RevisionNoteMap
name|getRevisionNoteMap
parameter_list|()
block|{
return|return
name|revisionNoteMap
return|;
block|}
DECL|method|getAuthor ()
specifier|public
name|Account
operator|.
name|Id
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
DECL|method|getComments ()
specifier|public
name|ImmutableListMultimap
argument_list|<
name|RevId
argument_list|,
name|PatchLineComment
argument_list|>
name|getComments
parameter_list|()
block|{
comment|// TODO(dborowitz): Defensive copy?
return|return
name|comments
return|;
block|}
DECL|method|containsComment (PatchLineComment c)
specifier|public
name|boolean
name|containsComment
parameter_list|(
name|PatchLineComment
name|c
parameter_list|)
block|{
for|for
control|(
name|PatchLineComment
name|existing
range|:
name|comments
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|equals
argument_list|(
name|existing
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
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
name|getChangeId
argument_list|()
argument_list|,
name|author
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad (LoadHandle handle)
specifier|protected
name|void
name|onLoad
parameter_list|(
name|LoadHandle
name|handle
parameter_list|)
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|ObjectId
name|rev
init|=
name|handle
operator|.
name|id
argument_list|()
decl_stmt|;
if|if
condition|(
name|rev
operator|==
literal|null
condition|)
block|{
name|loadDefaults
argument_list|()
expr_stmt|;
return|return;
block|}
name|RevCommit
name|tipCommit
init|=
name|handle
operator|.
name|walk
argument_list|()
operator|.
name|parseCommit
argument_list|(
name|rev
argument_list|)
decl_stmt|;
name|ObjectReader
name|reader
init|=
name|handle
operator|.
name|walk
argument_list|()
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|revisionNoteMap
operator|=
name|RevisionNoteMap
operator|.
name|parse
argument_list|(
name|args
operator|.
name|noteUtil
argument_list|,
name|getChangeId
argument_list|()
argument_list|,
name|reader
argument_list|,
name|NoteMap
operator|.
name|read
argument_list|(
name|reader
argument_list|,
name|tipCommit
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Multimap
argument_list|<
name|RevId
argument_list|,
name|PatchLineComment
argument_list|>
name|cs
init|=
name|ArrayListMultimap
operator|.
name|create
argument_list|()
decl_stmt|;
for|for
control|(
name|RevisionNote
name|rn
range|:
name|revisionNoteMap
operator|.
name|revisionNotes
operator|.
name|values
argument_list|()
control|)
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|rn
operator|.
name|comments
control|)
block|{
name|cs
operator|.
name|put
argument_list|(
name|c
operator|.
name|getRevId
argument_list|()
argument_list|,
name|c
argument_list|)
expr_stmt|;
block|}
block|}
name|comments
operator|=
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|cs
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|loadDefaults ()
specifier|protected
name|void
name|loadDefaults
parameter_list|()
block|{
name|comments
operator|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProjectName ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
block|{
return|return
name|args
operator|.
name|allUsers
return|;
block|}
annotation|@
name|Override
DECL|method|openHandle (Repository repo)
specifier|protected
name|LoadHandle
name|openHandle
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|change
operator|!=
literal|null
operator|&&
name|autoRebuild
condition|)
block|{
name|NoteDbChangeState
name|state
init|=
name|NoteDbChangeState
operator|.
name|parse
argument_list|(
name|change
argument_list|)
decl_stmt|;
comment|// Only check if this particular user's drafts are up to date, to avoid
comment|// reading unnecessary refs.
if|if
condition|(
operator|!
name|NoteDbChangeState
operator|.
name|areDraftsUpToDate
argument_list|(
name|state
argument_list|,
operator|new
name|RepoRefCache
argument_list|(
name|repo
argument_list|)
argument_list|,
name|getChangeId
argument_list|()
argument_list|,
name|author
argument_list|)
condition|)
block|{
return|return
name|rebuildAndOpen
argument_list|(
name|repo
argument_list|)
return|;
block|}
block|}
return|return
name|super
operator|.
name|openHandle
argument_list|(
name|repo
argument_list|)
return|;
block|}
DECL|method|rebuildAndOpen (Repository repo)
specifier|private
name|LoadHandle
name|rebuildAndOpen
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|NoteDbUpdateManager
operator|.
name|Result
name|r
init|=
name|args
operator|.
name|rebuilder
operator|.
name|get
argument_list|()
operator|.
name|rebuild
argument_list|(
name|args
operator|.
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|openHandle
argument_list|(
name|repo
argument_list|)
return|;
comment|// May be null in tests.
block|}
name|ObjectId
name|draftsId
init|=
name|r
operator|.
name|newState
argument_list|()
operator|.
name|getDraftIds
argument_list|()
operator|.
name|get
argument_list|(
name|author
argument_list|)
decl_stmt|;
name|repo
operator|.
name|scanForRepoChanges
argument_list|()
expr_stmt|;
return|return
name|LoadHandle
operator|.
name|create
argument_list|(
name|ChangeNotesCommit
operator|.
name|newRevWalk
argument_list|(
name|repo
argument_list|)
argument_list|,
name|draftsId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
return|return
name|super
operator|.
name|openHandle
argument_list|(
name|repo
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|ConfigInvalidException
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
block|}
annotation|@
name|VisibleForTesting
DECL|method|getNoteMap ()
name|NoteMap
name|getNoteMap
parameter_list|()
block|{
return|return
name|revisionNoteMap
operator|!=
literal|null
condition|?
name|revisionNoteMap
operator|.
name|noteMap
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

