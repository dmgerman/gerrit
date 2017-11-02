begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
name|MoreObjects
operator|.
name|firstNonNull
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
name|robotCommentsRef
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
name|Sets
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
name|client
operator|.
name|RobotComment
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
name|config
operator|.
name|AnonymousCowardName
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
name|GerritServerConfig
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
name|Arrays
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
name|Map
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
name|Config
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
name|RevWalk
import|;
end_import

begin_comment
comment|/**  * A single delta to apply atomically to a change.  *  *<p>This delta contains only robot comments on a single patch set of a change by a single author.  * This delta will become a single commit in the repository.  *  *<p>This class is not thread safe.  */
end_comment

begin_class
DECL|class|RobotCommentUpdate
specifier|public
class|class
name|RobotCommentUpdate
extends|extends
name|AbstractChangeUpdate
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create ( ChangeNotes notes, @Assisted(R) Account.Id accountId, @Assisted(R) Account.Id realAccountId, PersonIdent authorIdent, Date when)
name|RobotCommentUpdate
name|create
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"effective"
argument_list|)
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"real"
argument_list|)
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|,
name|Date
name|when
parameter_list|)
function_decl|;
DECL|method|create ( Change change, @Assisted(R) Account.Id accountId, @Assisted(R) Account.Id realAccountId, PersonIdent authorIdent, Date when)
name|RobotCommentUpdate
name|create
parameter_list|(
name|Change
name|change
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"effective"
argument_list|)
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"real"
argument_list|)
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|,
name|PersonIdent
name|authorIdent
parameter_list|,
name|Date
name|when
parameter_list|)
function_decl|;
block|}
DECL|field|put
specifier|private
name|List
argument_list|<
name|RobotComment
argument_list|>
name|put
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|RobotCommentUpdate ( @erritServerConfig Config cfg, @GerritPersonIdent PersonIdent serverIdent, @AnonymousCowardName String anonymousCowardName, NotesMigration migration, ChangeNoteUtil noteUtil, @Assisted ChangeNotes notes, @Assisted(R) Account.Id accountId, @Assisted(R) Account.Id realAccountId, @Assisted PersonIdent authorIdent, @Assisted Date when)
specifier|private
name|RobotCommentUpdate
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|ChangeNoteUtil
name|noteUtil
parameter_list|,
annotation|@
name|Assisted
name|ChangeNotes
name|notes
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"effective"
argument_list|)
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"real"
argument_list|)
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|,
annotation|@
name|Assisted
name|PersonIdent
name|authorIdent
parameter_list|,
annotation|@
name|Assisted
name|Date
name|when
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|migration
argument_list|,
name|noteUtil
argument_list|,
name|serverIdent
argument_list|,
name|anonymousCowardName
argument_list|,
name|notes
argument_list|,
literal|null
argument_list|,
name|accountId
argument_list|,
name|realAccountId
argument_list|,
name|authorIdent
argument_list|,
name|when
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AssistedInject
DECL|method|RobotCommentUpdate ( @erritServerConfig Config cfg, @GerritPersonIdent PersonIdent serverIdent, @AnonymousCowardName String anonymousCowardName, NotesMigration migration, ChangeNoteUtil noteUtil, @Assisted Change change, @Assisted(R) Account.Id accountId, @Assisted(R) Account.Id realAccountId, @Assisted PersonIdent authorIdent, @Assisted Date when)
specifier|private
name|RobotCommentUpdate
parameter_list|(
annotation|@
name|GerritServerConfig
name|Config
name|cfg
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|ChangeNoteUtil
name|noteUtil
parameter_list|,
annotation|@
name|Assisted
name|Change
name|change
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"effective"
argument_list|)
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"real"
argument_list|)
name|Account
operator|.
name|Id
name|realAccountId
parameter_list|,
annotation|@
name|Assisted
name|PersonIdent
name|authorIdent
parameter_list|,
annotation|@
name|Assisted
name|Date
name|when
parameter_list|)
block|{
name|super
argument_list|(
name|cfg
argument_list|,
name|migration
argument_list|,
name|noteUtil
argument_list|,
name|serverIdent
argument_list|,
name|anonymousCowardName
argument_list|,
literal|null
argument_list|,
name|change
argument_list|,
name|accountId
argument_list|,
name|realAccountId
argument_list|,
name|authorIdent
argument_list|,
name|when
argument_list|)
expr_stmt|;
block|}
DECL|method|putComment (RobotComment c)
specifier|public
name|void
name|putComment
parameter_list|(
name|RobotComment
name|c
parameter_list|)
block|{
name|verifyComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|put
operator|.
name|add
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
DECL|method|storeCommentsInNotes ( RevWalk rw, ObjectInserter ins, ObjectId curr, CommitBuilder cb)
specifier|private
name|CommitBuilder
name|storeCommentsInNotes
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|ObjectId
name|curr
parameter_list|,
name|CommitBuilder
name|cb
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|OrmException
throws|,
name|IOException
block|{
name|RevisionNoteMap
argument_list|<
name|RobotCommentsRevisionNote
argument_list|>
name|rnm
init|=
name|getRevisionNoteMap
argument_list|(
name|rw
argument_list|,
name|curr
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|RevId
argument_list|>
name|updatedRevs
init|=
name|Sets
operator|.
name|newHashSetWithExpectedSize
argument_list|(
name|rnm
operator|.
name|revisionNotes
operator|.
name|size
argument_list|()
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
name|rnm
argument_list|)
decl_stmt|;
for|for
control|(
name|RobotComment
name|c
range|:
name|put
control|)
block|{
name|cache
operator|.
name|get
argument_list|(
operator|new
name|RevId
argument_list|(
name|c
operator|.
name|revId
argument_list|)
argument_list|)
operator|.
name|putComment
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|RevId
argument_list|,
name|RevisionNoteBuilder
argument_list|>
name|builders
init|=
name|cache
operator|.
name|getBuilders
argument_list|()
decl_stmt|;
name|boolean
name|touchedAnyRevs
init|=
literal|false
decl_stmt|;
name|boolean
name|hasComments
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|RevId
argument_list|,
name|RevisionNoteBuilder
argument_list|>
name|e
range|:
name|builders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|updatedRevs
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectId
name|id
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|build
argument_list|(
name|noteUtil
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Arrays
operator|.
name|equals
argument_list|(
name|data
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|baseRaw
argument_list|)
condition|)
block|{
name|touchedAnyRevs
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|data
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|rnm
operator|.
name|noteMap
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hasComments
operator|=
literal|true
expr_stmt|;
name|ObjectId
name|dataBlob
init|=
name|ins
operator|.
name|insert
argument_list|(
name|OBJ_BLOB
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|rnm
operator|.
name|noteMap
operator|.
name|set
argument_list|(
name|id
argument_list|,
name|dataBlob
argument_list|)
expr_stmt|;
block|}
block|}
comment|// If we didn't touch any notes, tell the caller this was a no-op update. We
comment|// couldn't have done this in isEmpty() below because we hadn't read the old
comment|// data yet.
if|if
condition|(
operator|!
name|touchedAnyRevs
condition|)
block|{
return|return
name|NO_OP_UPDATE
return|;
block|}
comment|// If we touched every revision and there are no comments left, tell the
comment|// caller to delete the entire ref.
name|boolean
name|touchedAllRevs
init|=
name|updatedRevs
operator|.
name|equals
argument_list|(
name|rnm
operator|.
name|revisionNotes
operator|.
name|keySet
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|touchedAllRevs
operator|&&
operator|!
name|hasComments
condition|)
block|{
return|return
literal|null
return|;
block|}
name|cb
operator|.
name|setTreeId
argument_list|(
name|rnm
operator|.
name|noteMap
operator|.
name|writeTree
argument_list|(
name|ins
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|cb
return|;
block|}
DECL|method|getRevisionNoteMap (RevWalk rw, ObjectId curr)
specifier|private
name|RevisionNoteMap
argument_list|<
name|RobotCommentsRevisionNote
argument_list|>
name|getRevisionNoteMap
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectId
name|curr
parameter_list|)
throws|throws
name|ConfigInvalidException
throws|,
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
name|curr
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|RevisionNoteMap
operator|.
name|emptyMap
argument_list|()
return|;
block|}
if|if
condition|(
name|migration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
comment|// If reading from changes is enabled, then the old RobotCommentNotes
comment|// already parsed the revision notes. We can reuse them as long as the ref
comment|// hasn't advanced.
name|ChangeNotes
name|changeNotes
init|=
name|getNotes
argument_list|()
decl_stmt|;
if|if
condition|(
name|changeNotes
operator|!=
literal|null
condition|)
block|{
name|RobotCommentNotes
name|robotCommentNotes
init|=
name|changeNotes
operator|.
name|load
argument_list|()
operator|.
name|getRobotCommentNotes
argument_list|()
decl_stmt|;
if|if
condition|(
name|robotCommentNotes
operator|!=
literal|null
condition|)
block|{
name|ObjectId
name|idFromNotes
init|=
name|firstNonNull
argument_list|(
name|robotCommentNotes
operator|.
name|getRevision
argument_list|()
argument_list|,
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
decl_stmt|;
name|RevisionNoteMap
argument_list|<
name|RobotCommentsRevisionNote
argument_list|>
name|rnm
init|=
name|robotCommentNotes
operator|.
name|getRevisionNoteMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|idFromNotes
operator|.
name|equals
argument_list|(
name|curr
argument_list|)
operator|&&
name|rnm
operator|!=
literal|null
condition|)
block|{
return|return
name|rnm
return|;
block|}
block|}
block|}
block|}
name|NoteMap
name|noteMap
decl_stmt|;
if|if
condition|(
operator|!
name|curr
operator|.
name|equals
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
condition|)
block|{
name|noteMap
operator|=
name|NoteMap
operator|.
name|read
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|rw
operator|.
name|parseCommit
argument_list|(
name|curr
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|noteMap
operator|=
name|NoteMap
operator|.
name|newEmptyMap
argument_list|()
expr_stmt|;
block|}
comment|// Even though reading from changes might not be enabled, we need to
comment|// parse any existing revision notes so we can merge them.
return|return
name|RevisionNoteMap
operator|.
name|parseRobotComments
argument_list|(
name|noteUtil
argument_list|,
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|noteMap
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|applyImpl (RevWalk rw, ObjectInserter ins, ObjectId curr)
specifier|protected
name|CommitBuilder
name|applyImpl
parameter_list|(
name|RevWalk
name|rw
parameter_list|,
name|ObjectInserter
name|ins
parameter_list|,
name|ObjectId
name|curr
parameter_list|)
throws|throws
name|OrmException
throws|,
name|IOException
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
name|setMessage
argument_list|(
literal|"Update robot comments"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|storeCommentsInNotes
argument_list|(
name|rw
argument_list|,
name|ins
argument_list|,
name|curr
argument_list|,
name|cb
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
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
name|getNotes
argument_list|()
operator|.
name|getProjectName
argument_list|()
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
name|robotCommentsRef
argument_list|(
name|getId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isEmpty ()
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|put
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit
