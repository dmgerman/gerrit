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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|base
operator|.
name|Optional
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
name|Predicate
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
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
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|ChangeNotes
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
name|notedb
operator|.
name|ChangeUpdate
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
name|notedb
operator|.
name|DraftCommentNotes
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
name|notedb
operator|.
name|NotesMigration
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
name|PatchListNotAvailableException
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
name|gwtorm
operator|.
name|server
operator|.
name|ResultSet
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
name|RefDatabase
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Set
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate PatchLineComments.  *<p>  * These methods either query for and update PatchLineComments in the NoteDb or  * ReviewDb, depending on the state of the NotesMigration.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|PatchLineCommentsUtil
specifier|public
class|class
name|PatchLineCommentsUtil
block|{
DECL|field|repoManager
specifier|private
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsers
specifier|private
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|draftFactory
specifier|private
specifier|final
name|DraftCommentNotes
operator|.
name|Factory
name|draftFactory
decl_stmt|;
DECL|field|migration
specifier|private
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
annotation|@
name|VisibleForTesting
annotation|@
name|Inject
DECL|method|PatchLineCommentsUtil (GitRepositoryManager repoManager, AllUsersNameProvider allUsersProvider, DraftCommentNotes.Factory draftFactory, NotesMigration migration)
specifier|public
name|PatchLineCommentsUtil
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersNameProvider
name|allUsersProvider
parameter_list|,
name|DraftCommentNotes
operator|.
name|Factory
name|draftFactory
parameter_list|,
name|NotesMigration
name|migration
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
name|allUsers
operator|=
name|allUsersProvider
operator|.
name|get
argument_list|()
expr_stmt|;
name|this
operator|.
name|draftFactory
operator|=
name|draftFactory
expr_stmt|;
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
block|}
DECL|method|get (ReviewDb db, ChangeNotes notes, PatchLineComment.Key key)
specifier|public
name|Optional
argument_list|<
name|PatchLineComment
argument_list|>
name|get
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchLineComment
operator|.
name|Key
name|key
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|Optional
operator|.
name|fromNullable
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
return|;
block|}
for|for
control|(
name|PatchLineComment
name|c
range|:
name|publishedByChange
argument_list|(
name|db
argument_list|,
name|notes
argument_list|)
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|c
argument_list|)
return|;
block|}
block|}
for|for
control|(
name|PatchLineComment
name|c
range|:
name|draftByChange
argument_list|(
name|db
argument_list|,
name|notes
argument_list|)
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equals
argument_list|(
name|c
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
name|c
argument_list|)
return|;
block|}
block|}
return|return
name|Optional
operator|.
name|absent
argument_list|()
return|;
block|}
DECL|method|publishedByChange (ReviewDb db, ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByChange
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|byCommentStatus
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|PUBLISHED
argument_list|)
argument_list|)
return|;
block|}
name|notes
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getBaseComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getPatchSetComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|draftByChange (ReviewDb db, ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByChange
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|byCommentStatus
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|,
name|Status
operator|.
name|DRAFT
argument_list|)
argument_list|)
return|;
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|filtered
init|=
name|getDraftRefs
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|refName
range|:
name|filtered
control|)
block|{
name|Account
operator|.
name|Id
name|account
init|=
name|Account
operator|.
name|Id
operator|.
name|fromRefPart
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
name|comments
operator|.
name|addAll
argument_list|(
name|draftByChangeAuthor
argument_list|(
name|db
argument_list|,
name|notes
argument_list|,
name|account
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|byCommentStatus ( ResultSet<PatchLineComment> comments, final PatchLineComment.Status status)
specifier|private
specifier|static
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|byCommentStatus
parameter_list|(
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|,
specifier|final
name|PatchLineComment
operator|.
name|Status
name|status
parameter_list|)
block|{
return|return
name|Lists
operator|.
name|newArrayList
argument_list|(
name|Iterables
operator|.
name|filter
argument_list|(
name|comments
argument_list|,
operator|new
name|Predicate
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|PatchLineComment
name|input
parameter_list|)
block|{
return|return
operator|(
name|input
operator|.
name|getStatus
argument_list|()
operator|==
name|status
operator|)
return|;
block|}
block|}
argument_list|)
argument_list|)
return|;
block|}
DECL|method|byPatchSet (ReviewDb db, ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|byPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|psId
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|publishedByPatchSet
argument_list|(
name|db
argument_list|,
name|notes
argument_list|,
name|psId
argument_list|)
argument_list|)
expr_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|filtered
init|=
name|getDraftRefs
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|refName
range|:
name|filtered
control|)
block|{
name|Account
operator|.
name|Id
name|account
init|=
name|Account
operator|.
name|Id
operator|.
name|fromRefPart
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
name|comments
operator|.
name|addAll
argument_list|(
name|draftByPatchSetAuthor
argument_list|(
name|db
argument_list|,
name|psId
argument_list|,
name|account
argument_list|,
name|notes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|publishedByChangeFile (ReviewDb db, ChangeNotes notes, Change.Id changeId, String file)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByChangeFile
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|file
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|publishedByChangeFile
argument_list|(
name|changeId
argument_list|,
name|file
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|notes
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|addCommentsOnFile
argument_list|(
name|comments
argument_list|,
name|notes
operator|.
name|getBaseComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|addCommentsOnFile
argument_list|(
name|comments
argument_list|,
name|notes
operator|.
name|getPatchSetComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|publishedByPatchSet (ReviewDb db, ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByPatchSet
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|publishedByPatchSet
argument_list|(
name|psId
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|notes
operator|.
name|load
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
operator|new
name|ArrayList
argument_list|<
name|PatchLineComment
argument_list|>
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getPatchSetComments
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
argument_list|)
expr_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getBaseComments
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|draftByPatchSetAuthor (ReviewDb db, PatchSet.Id psId, Account.Id author, ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByPatchSetAuthor
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draftByPatchSetAuthor
argument_list|(
name|psId
argument_list|,
name|author
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getDraftBaseComments
argument_list|(
name|author
argument_list|)
operator|.
name|row
argument_list|(
name|psId
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getDraftPsComments
argument_list|(
name|author
argument_list|)
operator|.
name|row
argument_list|(
name|psId
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|draftByChangeFileAuthor (ReviewDb db, ChangeNotes notes, String file, Account.Id author)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByChangeFileAuthor
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|String
name|file
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draftByChangeFileAuthor
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|file
argument_list|,
name|author
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|addCommentsOnFile
argument_list|(
name|comments
argument_list|,
name|notes
operator|.
name|getDraftBaseComments
argument_list|(
name|author
argument_list|)
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|addCommentsOnFile
argument_list|(
name|comments
argument_list|,
name|notes
operator|.
name|getDraftPsComments
argument_list|(
name|author
argument_list|)
operator|.
name|values
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|draftByChangeAuthor (ReviewDb db, ChangeNotes notes, Account.Id author)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByChangeAuthor
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getDraftBaseComments
argument_list|(
name|author
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|notes
operator|.
name|getDraftPsComments
argument_list|(
name|author
argument_list|)
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|draftByAuthor (ReviewDb db, Account.Id author)
specifier|public
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByAuthor
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|migration
operator|.
name|readComments
argument_list|()
condition|)
block|{
return|return
name|sort
argument_list|(
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|draftByAuthor
argument_list|(
name|author
argument_list|)
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|refNames
init|=
name|getRefNamesAllUsers
argument_list|(
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|refName
range|:
name|refNames
control|)
block|{
name|Account
operator|.
name|Id
name|id
init|=
name|Account
operator|.
name|Id
operator|.
name|fromRefPart
argument_list|(
name|refName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|author
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|Change
operator|.
name|Id
name|changeId
init|=
name|Change
operator|.
name|Id
operator|.
name|parse
argument_list|(
name|refName
argument_list|)
decl_stmt|;
name|DraftCommentNotes
name|draftNotes
init|=
name|draftFactory
operator|.
name|create
argument_list|(
name|changeId
argument_list|,
name|author
argument_list|)
operator|.
name|load
argument_list|()
decl_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|draftNotes
operator|.
name|getDraftBaseComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
name|comments
operator|.
name|addAll
argument_list|(
name|draftNotes
operator|.
name|getDraftPsComments
argument_list|()
operator|.
name|values
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sort
argument_list|(
name|comments
argument_list|)
return|;
block|}
DECL|method|insertComments (ReviewDb db, ChangeUpdate update, Iterable<PatchLineComment> comments)
specifier|public
name|void
name|insertComments
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|comments
control|)
block|{
name|update
operator|.
name|insertComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|insert
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
DECL|method|upsertComments (ReviewDb db, ChangeUpdate update, Iterable<PatchLineComment> comments)
specifier|public
name|void
name|upsertComments
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|comments
control|)
block|{
name|update
operator|.
name|upsertComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|upsert
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
DECL|method|updateComments (ReviewDb db, ChangeUpdate update, Iterable<PatchLineComment> comments)
specifier|public
name|void
name|updateComments
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|comments
control|)
block|{
name|update
operator|.
name|updateComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|update
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteComments (ReviewDb db, ChangeUpdate update, Iterable<PatchLineComment> comments)
specifier|public
name|void
name|deleteComments
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Iterable
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
throws|throws
name|OrmException
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|comments
control|)
block|{
name|update
operator|.
name|deleteComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|db
operator|.
name|patchComments
argument_list|()
operator|.
name|delete
argument_list|(
name|comments
argument_list|)
expr_stmt|;
block|}
DECL|method|addCommentsOnFile ( Collection<PatchLineComment> commentsOnFile, Collection<PatchLineComment> allComments, String file)
specifier|private
specifier|static
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|addCommentsOnFile
parameter_list|(
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|commentsOnFile
parameter_list|,
name|Collection
argument_list|<
name|PatchLineComment
argument_list|>
name|allComments
parameter_list|,
name|String
name|file
parameter_list|)
block|{
for|for
control|(
name|PatchLineComment
name|c
range|:
name|allComments
control|)
block|{
name|String
name|currentFilename
init|=
name|c
operator|.
name|getKey
argument_list|()
operator|.
name|getParentKey
argument_list|()
operator|.
name|getFileName
argument_list|()
decl_stmt|;
if|if
condition|(
name|currentFilename
operator|.
name|equals
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|commentsOnFile
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|commentsOnFile
return|;
block|}
DECL|method|setCommentRevId (PatchLineComment c, PatchListCache cache, Change change, PatchSet ps)
specifier|public
specifier|static
name|void
name|setCommentRevId
parameter_list|(
name|PatchLineComment
name|c
parameter_list|,
name|PatchListCache
name|cache
parameter_list|,
name|Change
name|change
parameter_list|,
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|OrmException
block|{
if|if
condition|(
name|c
operator|.
name|getRevId
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|PatchList
name|patchList
decl_stmt|;
try|try
block|{
name|patchList
operator|=
name|cache
operator|.
name|get
argument_list|(
name|change
argument_list|,
name|ps
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PatchListNotAvailableException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|c
operator|.
name|setRevId
argument_list|(
operator|(
name|c
operator|.
name|getSide
argument_list|()
operator|==
operator|(
name|short
operator|)
literal|0
operator|)
condition|?
operator|new
name|RevId
argument_list|(
name|ObjectId
operator|.
name|toString
argument_list|(
name|patchList
operator|.
name|getOldId
argument_list|()
argument_list|)
argument_list|)
else|:
operator|new
name|RevId
argument_list|(
name|ObjectId
operator|.
name|toString
argument_list|(
name|patchList
operator|.
name|getNewId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getRefNamesAllUsers (String prefix)
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|getRefNamesAllUsers
parameter_list|(
name|String
name|prefix
parameter_list|)
throws|throws
name|OrmException
block|{
name|Repository
name|repo
decl_stmt|;
try|try
block|{
name|repo
operator|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|allUsers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
name|RefDatabase
name|refDb
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
decl_stmt|;
return|return
name|refDb
operator|.
name|getRefs
argument_list|(
name|prefix
argument_list|)
operator|.
name|keySet
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getDraftRefs (final Change.Id changeId)
specifier|private
name|Iterable
argument_list|<
name|String
argument_list|>
name|getDraftRefs
parameter_list|(
specifier|final
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
throws|throws
name|OrmException
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|refNames
init|=
name|getRefNamesAllUsers
argument_list|(
name|RefNames
operator|.
name|REFS_DRAFT_COMMENTS
argument_list|)
decl_stmt|;
specifier|final
name|String
name|suffix
init|=
literal|"-"
operator|+
name|changeId
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|Iterables
operator|.
name|filter
argument_list|(
name|refNames
argument_list|,
operator|new
name|Predicate
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|String
name|input
parameter_list|)
block|{
return|return
name|input
operator|.
name|endsWith
argument_list|(
name|suffix
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|sort (List<PatchLineComment> comments)
specifier|private
specifier|static
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|sort
parameter_list|(
name|List
argument_list|<
name|PatchLineComment
argument_list|>
name|comments
parameter_list|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|comments
argument_list|,
name|ChangeNotes
operator|.
name|PatchLineCommentComparator
argument_list|)
expr_stmt|;
return|return
name|comments
return|;
block|}
block|}
end_class

end_unit

