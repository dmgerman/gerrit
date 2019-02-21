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
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
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
name|common
operator|.
name|UsedAt
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
annotation|@
name|UsedAt
argument_list|(
name|UsedAt
operator|.
name|Project
operator|.
name|PLUGIN_CHECKS
argument_list|)
DECL|class|Args
specifier|public
specifier|static
class|class
name|Args
block|{
comment|// TODO(dborowitz): Some less smelly way of disabling NoteDb in tests.
DECL|field|failOnLoadForTest
specifier|public
specifier|final
name|AtomicBoolean
name|failOnLoadForTest
decl_stmt|;
DECL|field|changeNoteJson
specifier|public
specifier|final
name|ChangeNoteJson
name|changeNoteJson
decl_stmt|;
DECL|field|repoManager
specifier|public
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allUsers
specifier|public
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
DECL|field|legacyChangeNoteRead
specifier|public
specifier|final
name|LegacyChangeNoteRead
name|legacyChangeNoteRead
decl_stmt|;
DECL|field|metrics
specifier|public
specifier|final
name|NoteDbMetrics
name|metrics
decl_stmt|;
comment|// Providers required to avoid dependency cycles.
comment|// ChangeNoteCache -> Args
DECL|field|cache
specifier|public
specifier|final
name|Provider
argument_list|<
name|ChangeNotesCache
argument_list|>
name|cache
decl_stmt|;
annotation|@
name|Inject
DECL|method|Args ( GitRepositoryManager repoManager, AllUsersName allUsers, ChangeNoteJson changeNoteJson, LegacyChangeNoteRead legacyChangeNoteRead, NoteDbMetrics metrics, Provider<ChangeNotesCache> cache)
name|Args
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|,
name|ChangeNoteJson
name|changeNoteJson
parameter_list|,
name|LegacyChangeNoteRead
name|legacyChangeNoteRead
parameter_list|,
name|NoteDbMetrics
name|metrics
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
name|failOnLoadForTest
operator|=
operator|new
name|AtomicBoolean
argument_list|()
expr_stmt|;
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
name|allUsers
expr_stmt|;
name|this
operator|.
name|legacyChangeNoteRead
operator|=
name|legacyChangeNoteRead
expr_stmt|;
name|this
operator|.
name|changeNoteJson
operator|=
name|changeNoteJson
expr_stmt|;
name|this
operator|.
name|metrics
operator|=
name|metrics
expr_stmt|;
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
block|}
DECL|class|LoadHandle
specifier|public
specifier|static
class|class
name|LoadHandle
implements|implements
name|AutoCloseable
block|{
DECL|field|repo
specifier|private
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|ObjectId
name|id
decl_stmt|;
DECL|field|rw
specifier|private
name|ChangeNotesRevWalk
name|rw
decl_stmt|;
DECL|method|LoadHandle (Repository repo, @Nullable ObjectId id)
specifier|private
name|LoadHandle
parameter_list|(
name|Repository
name|repo
parameter_list|,
annotation|@
name|Nullable
name|ObjectId
name|id
parameter_list|)
block|{
name|this
operator|.
name|repo
operator|=
name|requireNonNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|walk ()
specifier|public
name|ChangeNotesRevWalk
name|walk
parameter_list|()
block|{
if|if
condition|(
name|rw
operator|==
literal|null
condition|)
block|{
name|rw
operator|=
name|ChangeNotesCommit
operator|.
name|newRevWalk
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
return|return
name|rw
return|;
block|}
annotation|@
name|Nullable
DECL|method|id ()
specifier|public
name|ObjectId
name|id
parameter_list|()
block|{
return|return
name|id
return|;
block|}
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
name|rw
operator|!=
literal|null
condition|)
block|{
name|rw
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
DECL|method|AbstractChangeNotes (Args args, Change.Id changeId)
specifier|protected
name|AbstractChangeNotes
parameter_list|(
name|Args
name|args
parameter_list|,
name|Change
operator|.
name|Id
name|changeId
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|requireNonNull
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeId
operator|=
name|requireNonNull
argument_list|(
name|changeId
argument_list|)
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
if|if
condition|(
name|args
operator|.
name|failOnLoadForTest
operator|.
name|get
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
operator|new
name|LoadHandle
argument_list|(
name|repo
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

