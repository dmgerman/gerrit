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
name|checkNotNull
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
name|NoteDbTable
operator|.
name|CHANGES
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
name|metrics
operator|.
name|Timer1
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
name|ChangeNotesCommit
operator|.
name|ChangeNotesRevWalk
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
name|NoteDbChangeState
operator|.
name|PrimaryStorage
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
name|rebuild
operator|.
name|ChangeRebuilder
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
name|Provider
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

begin_comment
comment|/** View of contents at a single ref related to some change. * */
end_comment

begin_class
DECL|class|AbstractChangeNotes
specifier|public
specifier|abstract
class|class
name|AbstractChangeNotes
parameter_list|<
name|T
parameter_list|>
block|{
annotation|@
name|VisibleForTesting
annotation|@
name|Singleton
DECL|class|Args
specifier|public
specifier|static
class|class
name|Args
block|{
DECL|field|repoManager
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|migration
specifier|final
name|NotesMigration
name|migration
decl_stmt|;
DECL|field|allUsers
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|noteUtil
specifier|final
name|ChangeNoteUtil
name|noteUtil
decl_stmt|;
DECL|field|metrics
specifier|final
name|NoteDbMetrics
name|metrics
decl_stmt|;
DECL|field|db
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
comment|// Providers required to avoid dependency cycles.
comment|// ChangeRebuilder -> ChangeNotes.Factory -> Args
DECL|field|rebuilder
specifier|final
name|Provider
argument_list|<
name|ChangeRebuilder
argument_list|>
name|rebuilder
decl_stmt|;
comment|// ChangeNoteCache -> Args
DECL|field|cache
specifier|final
name|Provider
argument_list|<
name|ChangeNotesCache
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|Args ( GitRepositoryManager repoManager, NotesMigration migration, AllUsersName allUsers, ChangeNoteUtil noteUtil, NoteDbMetrics metrics, Provider<ReviewDb> db, Provider<ChangeRebuilder> rebuilder, Provider<ChangeNotesCache> cache)
name|Args
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|NotesMigration
name|migration
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|,
name|ChangeNoteUtil
name|noteUtil
parameter_list|,
name|NoteDbMetrics
name|metrics
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|Provider
argument_list|<
name|ChangeRebuilder
argument_list|>
name|rebuilder
parameter_list|,
name|Provider
argument_list|<
name|ChangeNotesCache
argument_list|>
name|cache
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
name|allUsers
operator|=
name|allUsers
expr_stmt|;
name|this
operator|.
name|noteUtil
operator|=
name|noteUtil
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|rebuilder
operator|=
name|rebuilder
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
block|}
annotation|@
name|AutoValue
DECL|class|LoadHandle
specifier|public
specifier|abstract
specifier|static
class|class
name|LoadHandle
implements|implements
name|AutoCloseable
block|{
DECL|method|create (ChangeNotesRevWalk walk, ObjectId id)
specifier|public
specifier|static
name|LoadHandle
name|create
parameter_list|(
name|ChangeNotesRevWalk
name|walk
parameter_list|,
name|ObjectId
name|id
parameter_list|)
block|{
if|if
condition|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|id
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|id
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|AutoValue_AbstractChangeNotes_LoadHandle
argument_list|(
name|checkNotNull
argument_list|(
name|walk
argument_list|)
argument_list|,
name|id
argument_list|)
return|;
block|}
DECL|method|missing ()
specifier|public
specifier|static
name|LoadHandle
name|missing
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_AbstractChangeNotes_LoadHandle
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Nullable
DECL|method|walk ()
specifier|public
specifier|abstract
name|ChangeNotesRevWalk
name|walk
parameter_list|()
function_decl|;
annotation|@
name|Nullable
DECL|method|id ()
specifier|public
specifier|abstract
name|ObjectId
name|id
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|walk
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|walk
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|field|args
specifier|protected
specifier|final
name|Args
name|args
decl_stmt|;
DECL|field|primaryStorage
specifier|protected
specifier|final
name|PrimaryStorage
name|primaryStorage
decl_stmt|;
DECL|field|autoRebuild
specifier|protected
specifier|final
name|boolean
name|autoRebuild
decl_stmt|;
DECL|field|changeId
specifier|private
specifier|final
name|Change
operator|.
name|Id
name|changeId
decl_stmt|;
DECL|field|revision
specifier|private
name|ObjectId
name|revision
decl_stmt|;
DECL|field|loaded
specifier|private
name|boolean
name|loaded
decl_stmt|;
DECL|method|AbstractChangeNotes ( Args args, Change.Id changeId, @Nullable PrimaryStorage primaryStorage, boolean autoRebuild)
name|AbstractChangeNotes
parameter_list|(
name|Args
name|args
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
annotation|@
name|Nullable
name|PrimaryStorage
name|primaryStorage
parameter_list|,
name|boolean
name|autoRebuild
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|checkNotNull
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|checkNotNull
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
name|this
operator|.
name|primaryStorage
operator|=
name|primaryStorage
expr_stmt|;
name|this
operator|.
name|autoRebuild
operator|=
name|primaryStorage
operator|==
name|PrimaryStorage
operator|.
name|REVIEW_DB
operator|&&
operator|!
name|args
operator|.
name|migration
operator|.
name|disableChangeReviewDb
argument_list|()
operator|&&
name|autoRebuild
expr_stmt|;
block|}
DECL|method|getChangeId ()
specifier|public
name|Change
operator|.
name|Id
name|getChangeId
parameter_list|()
block|{
return|return
name|changeId
return|;
block|}
comment|/** @return revision of the metadata that was loaded. */
DECL|method|getRevision ()
specifier|public
name|ObjectId
name|getRevision
parameter_list|()
block|{
return|return
name|revision
return|;
block|}
DECL|method|load ()
specifier|public
name|T
name|load
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
name|loaded
condition|)
block|{
return|return
name|self
argument_list|()
return|;
block|}
name|boolean
name|read
init|=
name|args
operator|.
name|migration
operator|.
name|readChanges
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|read
operator|&&
name|primaryStorage
operator|==
name|PrimaryStorage
operator|.
name|NOTE_DB
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"NoteDb is required to read change "
operator|+
name|changeId
argument_list|)
throw|;
block|}
name|boolean
name|readOrWrite
init|=
name|read
operator|||
name|args
operator|.
name|migration
operator|.
name|rawWriteChangesSetting
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|readOrWrite
operator|&&
operator|!
name|autoRebuild
condition|)
block|{
name|loadDefaults
argument_list|()
expr_stmt|;
return|return
name|self
argument_list|()
return|;
block|}
if|if
condition|(
name|args
operator|.
name|migration
operator|.
name|failOnLoadForTest
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|OrmException
argument_list|(
literal|"Reading from NoteDb is disabled"
argument_list|)
throw|;
block|}
try|try
init|(
name|Timer1
operator|.
name|Context
name|timer
init|=
name|args
operator|.
name|metrics
operator|.
name|readLatency
operator|.
name|start
argument_list|(
name|CHANGES
argument_list|)
init|;
name|Repository
name|repo
operator|=
name|args
operator|.
name|repoManager
operator|.
name|openRepository
argument_list|(
name|getProjectName
argument_list|()
argument_list|)
init|;
comment|// Call openHandle even if reading is disabled, to trigger
comment|// auto-rebuilding before this object may get passed to a ChangeUpdate.
name|LoadHandle
name|handle
operator|=
name|openHandle
argument_list|(
name|repo
argument_list|)
init|)
block|{
if|if
condition|(
name|read
condition|)
block|{
name|revision
operator|=
name|handle
operator|.
name|id
argument_list|()
expr_stmt|;
name|onLoad
argument_list|(
name|handle
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|loadDefaults
argument_list|()
expr_stmt|;
block|}
name|loaded
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigInvalidException
decl||
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
return|return
name|self
argument_list|()
return|;
block|}
DECL|method|readRef (Repository repo)
specifier|protected
name|ObjectId
name|readRef
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|IOException
block|{
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ref
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**    * Open a handle for reading this entity from a repository.    *    *<p>Implementations may override this method to provide auto-rebuilding behavior.    *    * @param repo open repository.    * @return handle for reading the entity.    * @throws NoSuchChangeException change does not exist.    * @throws IOException a repo-level error occurred.    */
DECL|method|openHandle (Repository repo)
specifier|protected
name|LoadHandle
name|openHandle
parameter_list|(
name|Repository
name|repo
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|IOException
block|{
return|return
name|openHandle
argument_list|(
name|repo
argument_list|,
name|readRef
argument_list|(
name|repo
argument_list|)
argument_list|)
return|;
block|}
DECL|method|openHandle (Repository repo, ObjectId id)
specifier|protected
name|LoadHandle
name|openHandle
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|ObjectId
name|id
parameter_list|)
block|{
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
name|id
argument_list|)
return|;
block|}
DECL|method|reload ()
specifier|public
name|T
name|reload
parameter_list|()
throws|throws
name|NoSuchChangeException
throws|,
name|OrmException
block|{
name|loaded
operator|=
literal|false
expr_stmt|;
return|return
name|load
argument_list|()
return|;
block|}
DECL|method|loadRevision ()
specifier|public
name|ObjectId
name|loadRevision
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
name|loaded
condition|)
block|{
return|return
name|getRevision
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
operator|!
name|args
operator|.
name|migration
operator|.
name|enabled
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
init|(
name|Repository
name|repo
init|=
name|args
operator|.
name|repoManager
operator|.
name|openRepository
argument_list|(
name|getProjectName
argument_list|()
argument_list|)
init|)
block|{
name|Ref
name|ref
init|=
name|repo
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|exactRef
argument_list|(
name|getRefName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ref
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getObjectId
argument_list|()
else|:
literal|null
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
block|}
comment|/** Load default values for any instance variables when NoteDb is disabled. */
DECL|method|loadDefaults ()
specifier|protected
specifier|abstract
name|void
name|loadDefaults
parameter_list|()
function_decl|;
comment|/**    * @return the NameKey for the project where the notes should be stored, which is not necessarily    *     the same as the change's project.    */
DECL|method|getProjectName ()
specifier|public
specifier|abstract
name|Project
operator|.
name|NameKey
name|getProjectName
parameter_list|()
function_decl|;
comment|/** @return name of the reference storing this configuration. */
DECL|method|getRefName ()
specifier|protected
specifier|abstract
name|String
name|getRefName
parameter_list|()
function_decl|;
comment|/** Set up the metadata, parsing any state from the loaded revision. */
DECL|method|onLoad (LoadHandle handle)
specifier|protected
specifier|abstract
name|void
name|onLoad
parameter_list|(
name|LoadHandle
name|handle
parameter_list|)
throws|throws
name|NoSuchChangeException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
function_decl|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|self ()
specifier|protected
specifier|final
name|T
name|self
parameter_list|()
block|{
return|return
operator|(
name|T
operator|)
name|this
return|;
block|}
block|}
end_class

end_unit

