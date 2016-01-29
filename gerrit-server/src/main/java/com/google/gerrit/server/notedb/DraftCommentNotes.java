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
name|ImmutableListMultimap
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
annotation|@
name|Singleton
DECL|class|Factory
specifier|public
specifier|static
class|class
name|Factory
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|draftsProject
specifier|private
specifier|final
name|AllUsersName
name|draftsProject
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|Factory (GitRepositoryManager repoManager, NotesMigration migration, AllUsersNameProvider allUsers)
specifier|public
name|Factory
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AllUsersNameProvider
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
name|this
operator|.
name|draftsProject
operator|=
name|allUsers
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
DECL|method|create (Change.Id changeId, Account.Id accountId)
specifier|public
name|DraftCommentNotes
name|create
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
block|{
return|return
operator|new
name|DraftCommentNotes
argument_list|(
name|repoManager
argument_list|,
name|migration
argument_list|,
name|draftsProject
argument_list|,
name|changeId
argument_list|,
name|accountId
argument_list|)
return|;
block|}
block|}
DECL|field|draftsProject
specifier|private
specifier|final
name|AllUsersName
name|draftsProject
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
DECL|field|noteMap
specifier|private
name|NoteMap
name|noteMap
decl_stmt|;
DECL|method|DraftCommentNotes (GitRepositoryManager repoManager, NotesMigration migration, AllUsersName draftsProject, Change.Id changeId, Account.Id author)
name|DraftCommentNotes
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AllUsersName
name|draftsProject
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
name|super
argument_list|(
name|repoManager
argument_list|,
name|migration
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
name|this
operator|.
name|draftsProject
operator|=
name|draftsProject
expr_stmt|;
name|this
operator|.
name|author
operator|=
name|author
expr_stmt|;
block|}
DECL|method|getNoteMap ()
specifier|public
name|NoteMap
name|getNoteMap
parameter_list|()
block|{
return|return
name|noteMap
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
name|author
argument_list|,
name|getChangeId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|onLoad ()
specifier|protected
name|void
name|onLoad
parameter_list|()
throws|throws
name|IOException
throws|,
name|ConfigInvalidException
block|{
name|ObjectId
name|rev
init|=
name|getRevision
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
try|try
init|(
name|RevWalk
name|walk
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|;
name|DraftCommentNotesParser
name|parser
operator|=
operator|new
name|DraftCommentNotesParser
argument_list|(
name|getChangeId
argument_list|()
argument_list|,
name|walk
argument_list|,
name|rev
argument_list|,
name|repoManager
argument_list|,
name|draftsProject
argument_list|,
name|author
argument_list|)
init|)
block|{
name|parser
operator|.
name|parseDraftComments
argument_list|()
expr_stmt|;
name|comments
operator|=
name|ImmutableListMultimap
operator|.
name|copyOf
argument_list|(
name|parser
operator|.
name|comments
argument_list|)
expr_stmt|;
name|noteMap
operator|=
name|parser
operator|.
name|noteMap
expr_stmt|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" is read-only"
argument_list|)
throw|;
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
name|draftsProject
return|;
block|}
block|}
end_class

end_unit

