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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
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
name|checkNotNull
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
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
name|Throwables
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
name|collect
operator|.
name|Maps
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
name|extensions
operator|.
name|restapi
operator|.
name|RestApiException
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
name|CurrentUser
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
name|index
operator|.
name|change
operator|.
name|ChangeIndexer
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
name|NoteDbUpdateManager
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
name|gerrit
operator|.
name|server
operator|.
name|util
operator|.
name|RequestId
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
name|assistedinject
operator|.
name|Assisted
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
name|sql
operator|.
name|Timestamp
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
name|TimeZone
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|NullProgressMonitor
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

begin_comment
comment|/**  * {@link BatchUpdate} implementation that only supports NoteDb.  *  *<p>Used when {@code noteDb.changes.disableReviewDb=true}, at which point ReviewDb is not  * consulted during updates.  */
end_comment

begin_class
DECL|class|UnfusedNoteDbBatchUpdate
class|class
name|UnfusedNoteDbBatchUpdate
extends|extends
name|BatchUpdate
block|{
DECL|interface|AssistedFactory
interface|interface
name|AssistedFactory
block|{
DECL|method|create ( ReviewDb db, Project.NameKey project, CurrentUser user, Timestamp when)
name|UnfusedNoteDbBatchUpdate
name|create
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Timestamp
name|when
parameter_list|)
function_decl|;
block|}
DECL|method|execute ( ImmutableList<UnfusedNoteDbBatchUpdate> updates, BatchUpdateListener listener, @Nullable RequestId requestId, boolean dryrun)
specifier|static
name|void
name|execute
parameter_list|(
name|ImmutableList
argument_list|<
name|UnfusedNoteDbBatchUpdate
argument_list|>
name|updates
parameter_list|,
name|BatchUpdateListener
name|listener
parameter_list|,
annotation|@
name|Nullable
name|RequestId
name|requestId
parameter_list|,
name|boolean
name|dryrun
parameter_list|)
throws|throws
name|UpdateException
throws|,
name|RestApiException
block|{
if|if
condition|(
name|updates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|setRequestIds
argument_list|(
name|updates
argument_list|,
name|requestId
argument_list|)
expr_stmt|;
try|try
block|{
name|Order
name|order
init|=
name|getOrder
argument_list|(
name|updates
argument_list|,
name|listener
argument_list|)
decl_stmt|;
comment|// TODO(dborowitz): Fuse implementations to use a single BatchRefUpdate between phases. Note
comment|// that we may still need to respect the order, since op implementations may make assumptions
comment|// about the order in which their methods are called.
switch|switch
condition|(
name|order
condition|)
block|{
case|case
name|REPO_BEFORE_DB
case|:
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|executeUpdateRepo
argument_list|()
expr_stmt|;
block|}
name|listener
operator|.
name|afterUpdateRepos
argument_list|()
expr_stmt|;
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|executeRefUpdates
argument_list|(
name|dryrun
argument_list|)
expr_stmt|;
block|}
name|listener
operator|.
name|afterUpdateRefs
argument_list|()
expr_stmt|;
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|reindexChanges
argument_list|(
name|u
operator|.
name|executeChangeOps
argument_list|(
name|dryrun
argument_list|)
argument_list|,
name|dryrun
argument_list|)
expr_stmt|;
block|}
name|listener
operator|.
name|afterUpdateChanges
argument_list|()
expr_stmt|;
break|break;
case|case
name|DB_BEFORE_REPO
case|:
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|reindexChanges
argument_list|(
name|u
operator|.
name|executeChangeOps
argument_list|(
name|dryrun
argument_list|)
argument_list|,
name|dryrun
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|executeUpdateRepo
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|executeRefUpdates
argument_list|(
name|dryrun
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"invalid execution order: "
operator|+
name|order
argument_list|)
throw|;
block|}
name|ChangeIndexer
operator|.
name|allAsList
argument_list|(
name|updates
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|u
lambda|->
name|u
operator|.
name|indexFutures
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
comment|// Fire ref update events only after all mutations are finished, since callers may assume a
comment|// patch set ref being created means the change was created, or a branch advancing meaning
comment|// some changes were closed.
name|updates
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|u
lambda|->
name|u
operator|.
name|batchRefUpdate
operator|!=
literal|null
argument_list|)
operator|.
name|forEach
argument_list|(
name|u
lambda|->
name|u
operator|.
name|gitRefUpdated
operator|.
name|fire
argument_list|(
name|u
operator|.
name|project
argument_list|,
name|u
operator|.
name|batchRefUpdate
argument_list|,
name|u
operator|.
name|getAccount
argument_list|()
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|dryrun
condition|)
block|{
for|for
control|(
name|UnfusedNoteDbBatchUpdate
name|u
range|:
name|updates
control|)
block|{
name|u
operator|.
name|executePostOps
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|wrapAndThrowException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ContextImpl
class|class
name|ContextImpl
implements|implements
name|Context
block|{
annotation|@
name|Override
DECL|method|getRepoView ()
specifier|public
name|RepoView
name|getRepoView
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|UnfusedNoteDbBatchUpdate
operator|.
name|this
operator|.
name|getRepoView
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRevWalk ()
specifier|public
name|RevWalk
name|getRevWalk
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getRepoView
argument_list|()
operator|.
name|getRevWalk
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getProject ()
specifier|public
name|Project
operator|.
name|NameKey
name|getProject
parameter_list|()
block|{
return|return
name|project
return|;
block|}
annotation|@
name|Override
DECL|method|getWhen ()
specifier|public
name|Timestamp
name|getWhen
parameter_list|()
block|{
return|return
name|when
return|;
block|}
annotation|@
name|Override
DECL|method|getTimeZone ()
specifier|public
name|TimeZone
name|getTimeZone
parameter_list|()
block|{
return|return
name|tz
return|;
block|}
annotation|@
name|Override
DECL|method|getDb ()
specifier|public
name|ReviewDb
name|getDb
parameter_list|()
block|{
return|return
name|db
return|;
block|}
annotation|@
name|Override
DECL|method|getUser ()
specifier|public
name|CurrentUser
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
annotation|@
name|Override
DECL|method|getOrder ()
specifier|public
name|Order
name|getOrder
parameter_list|()
block|{
return|return
name|order
return|;
block|}
block|}
DECL|class|RepoContextImpl
specifier|private
class|class
name|RepoContextImpl
extends|extends
name|ContextImpl
implements|implements
name|RepoContext
block|{
annotation|@
name|Override
DECL|method|getInserter ()
specifier|public
name|ObjectInserter
name|getInserter
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getRepoView
argument_list|()
operator|.
name|getInserterWrapper
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addRefUpdate (ReceiveCommand cmd)
specifier|public
name|void
name|addRefUpdate
parameter_list|(
name|ReceiveCommand
name|cmd
parameter_list|)
throws|throws
name|IOException
block|{
name|getRepoView
argument_list|()
operator|.
name|getCommands
argument_list|()
operator|.
name|add
argument_list|(
name|cmd
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ChangeContextImpl
specifier|private
class|class
name|ChangeContextImpl
extends|extends
name|ContextImpl
implements|implements
name|ChangeContext
block|{
DECL|field|ctl
specifier|private
specifier|final
name|ChangeControl
name|ctl
decl_stmt|;
DECL|field|updates
specifier|private
specifier|final
name|Map
argument_list|<
name|PatchSet
operator|.
name|Id
argument_list|,
name|ChangeUpdate
argument_list|>
name|updates
decl_stmt|;
DECL|field|deleted
specifier|private
name|boolean
name|deleted
decl_stmt|;
DECL|method|ChangeContextImpl (ChangeControl ctl)
specifier|protected
name|ChangeContextImpl
parameter_list|(
name|ChangeControl
name|ctl
parameter_list|)
block|{
name|this
operator|.
name|ctl
operator|=
name|checkNotNull
argument_list|(
name|ctl
argument_list|)
expr_stmt|;
name|updates
operator|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|comparing
argument_list|(
name|PatchSet
operator|.
name|Id
operator|::
name|get
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUpdate (PatchSet.Id psId)
specifier|public
name|ChangeUpdate
name|getUpdate
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|)
block|{
name|ChangeUpdate
name|u
init|=
name|updates
operator|.
name|get
argument_list|(
name|psId
argument_list|)
decl_stmt|;
if|if
condition|(
name|u
operator|==
literal|null
condition|)
block|{
name|u
operator|=
name|changeUpdateFactory
operator|.
name|create
argument_list|(
name|ctl
argument_list|,
name|when
argument_list|)
expr_stmt|;
if|if
condition|(
name|newChanges
operator|.
name|containsKey
argument_list|(
name|ctl
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|u
operator|.
name|setAllowWriteToNewRef
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|u
operator|.
name|setPatchSetId
argument_list|(
name|psId
argument_list|)
expr_stmt|;
name|updates
operator|.
name|put
argument_list|(
name|psId
argument_list|,
name|u
argument_list|)
expr_stmt|;
block|}
return|return
name|u
return|;
block|}
annotation|@
name|Override
DECL|method|getControl ()
specifier|public
name|ChangeControl
name|getControl
parameter_list|()
block|{
return|return
name|ctl
return|;
block|}
annotation|@
name|Override
DECL|method|dontBumpLastUpdatedOn ()
specifier|public
name|void
name|dontBumpLastUpdatedOn
parameter_list|()
block|{
comment|// Do nothing; NoteDb effectively updates timestamp if and only if a commit was written to the
comment|// change meta ref.
block|}
annotation|@
name|Override
DECL|method|deleteChange ()
specifier|public
name|void
name|deleteChange
parameter_list|()
block|{
name|deleted
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|/** Per-change result status from {@link #executeChangeOps}. */
DECL|enum|ChangeResult
specifier|private
enum|enum
name|ChangeResult
block|{
DECL|enumConstant|SKIPPED
name|SKIPPED
block|,
DECL|enumConstant|UPSERTED
name|UPSERTED
block|,
DECL|enumConstant|DELETED
name|DELETED
block|;   }
DECL|field|changeNotesFactory
specifier|private
specifier|final
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
decl_stmt|;
DECL|field|changeUpdateFactory
specifier|private
specifier|final
name|ChangeUpdate
operator|.
name|Factory
name|changeUpdateFactory
decl_stmt|;
DECL|field|updateManagerFactory
specifier|private
specifier|final
name|NoteDbUpdateManager
operator|.
name|Factory
name|updateManagerFactory
decl_stmt|;
DECL|field|indexer
specifier|private
specifier|final
name|ChangeIndexer
name|indexer
decl_stmt|;
DECL|field|gitRefUpdated
specifier|private
specifier|final
name|GitReferenceUpdated
name|gitRefUpdated
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|field|indexFutures
specifier|private
name|List
argument_list|<
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CheckedFuture
argument_list|<
name|?
argument_list|,
name|IOException
argument_list|>
argument_list|>
name|indexFutures
decl_stmt|;
annotation|@
name|Inject
DECL|method|UnfusedNoteDbBatchUpdate ( GitRepositoryManager repoManager, @GerritPersonIdent PersonIdent serverIdent, ChangeNotes.Factory changeNotesFactory, ChangeControl.GenericFactory changeControlFactory, ChangeUpdate.Factory changeUpdateFactory, NoteDbUpdateManager.Factory updateManagerFactory, ChangeIndexer indexer, GitReferenceUpdated gitRefUpdated, @Assisted ReviewDb db, @Assisted Project.NameKey project, @Assisted CurrentUser user, @Assisted Timestamp when)
name|UnfusedNoteDbBatchUpdate
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|serverIdent
parameter_list|,
name|ChangeNotes
operator|.
name|Factory
name|changeNotesFactory
parameter_list|,
name|ChangeControl
operator|.
name|GenericFactory
name|changeControlFactory
parameter_list|,
name|ChangeUpdate
operator|.
name|Factory
name|changeUpdateFactory
parameter_list|,
name|NoteDbUpdateManager
operator|.
name|Factory
name|updateManagerFactory
parameter_list|,
name|ChangeIndexer
name|indexer
parameter_list|,
name|GitReferenceUpdated
name|gitRefUpdated
parameter_list|,
annotation|@
name|Assisted
name|ReviewDb
name|db
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
name|CurrentUser
name|user
parameter_list|,
annotation|@
name|Assisted
name|Timestamp
name|when
parameter_list|)
block|{
name|super
argument_list|(
name|repoManager
argument_list|,
name|serverIdent
argument_list|,
name|project
argument_list|,
name|user
argument_list|,
name|when
argument_list|)
expr_stmt|;
name|checkArgument
argument_list|(
operator|!
name|db
operator|.
name|changesTablesEnabled
argument_list|()
argument_list|,
literal|"expected Change tables to be disabled on %s"
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|this
operator|.
name|changeNotesFactory
operator|=
name|changeNotesFactory
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|changeUpdateFactory
operator|=
name|changeUpdateFactory
expr_stmt|;
name|this
operator|.
name|updateManagerFactory
operator|=
name|updateManagerFactory
expr_stmt|;
name|this
operator|.
name|indexer
operator|=
name|indexer
expr_stmt|;
name|this
operator|.
name|gitRefUpdated
operator|=
name|gitRefUpdated
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|indexFutures
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (BatchUpdateListener listener)
specifier|public
name|void
name|execute
parameter_list|(
name|BatchUpdateListener
name|listener
parameter_list|)
throws|throws
name|UpdateException
throws|,
name|RestApiException
block|{
name|execute
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|this
argument_list|)
argument_list|,
name|listener
argument_list|,
name|requestId
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newContext ()
specifier|protected
name|Context
name|newContext
parameter_list|()
block|{
return|return
operator|new
name|ContextImpl
argument_list|()
return|;
block|}
DECL|method|executeUpdateRepo ()
specifier|private
name|void
name|executeUpdateRepo
parameter_list|()
throws|throws
name|UpdateException
throws|,
name|RestApiException
block|{
try|try
block|{
name|logDebug
argument_list|(
literal|"Executing updateRepo on {} ops"
argument_list|,
name|ops
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RepoContextImpl
name|ctx
init|=
operator|new
name|RepoContextImpl
argument_list|()
decl_stmt|;
for|for
control|(
name|BatchUpdateOp
name|op
range|:
name|ops
operator|.
name|values
argument_list|()
control|)
block|{
name|op
operator|.
name|updateRepo
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
name|logDebug
argument_list|(
literal|"Executing updateRepo on {} RepoOnlyOps"
argument_list|,
name|repoOnlyOps
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|RepoOnlyOp
name|op
range|:
name|repoOnlyOps
control|)
block|{
name|op
operator|.
name|updateRepo
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onSubmitValidators
operator|!=
literal|null
operator|&&
operator|!
name|getRefUpdates
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Validation of refs has to take place here and not at the beginning of executeRefUpdates.
comment|// Otherwise, failing validation in a second BatchUpdate object will happen *after* the
comment|// first update's executeRefUpdates has finished, hence after first repo's refs have been
comment|// updated, which is too late.
name|onSubmitValidators
operator|.
name|validate
argument_list|(
name|project
argument_list|,
name|ctx
operator|.
name|getRevWalk
argument_list|()
operator|.
name|getObjectReader
argument_list|()
argument_list|,
name|repoView
operator|.
name|getCommands
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO(dborowitz): Don't flush when fusing phases.
if|if
condition|(
name|repoView
operator|!=
literal|null
condition|)
block|{
name|logDebug
argument_list|(
literal|"Flushing inserter"
argument_list|)
expr_stmt|;
name|repoView
operator|.
name|getInserter
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|logDebug
argument_list|(
literal|"No objects to flush"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwables
operator|.
name|throwIfInstanceOf
argument_list|(
name|e
argument_list|,
name|RestApiException
operator|.
name|class
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|UpdateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// TODO(dborowitz): Don't execute non-change ref updates separately when fusing phases.
DECL|method|executeRefUpdates (boolean dryrun)
specifier|private
name|void
name|executeRefUpdates
parameter_list|(
name|boolean
name|dryrun
parameter_list|)
throws|throws
name|IOException
throws|,
name|RestApiException
block|{
if|if
condition|(
name|getRefUpdates
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|logDebug
argument_list|(
literal|"No ref updates to execute"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// May not be opened if the caller added ref updates but no new objects.
name|initRepository
argument_list|()
expr_stmt|;
name|batchRefUpdate
operator|=
name|repoView
operator|.
name|getRepository
argument_list|()
operator|.
name|getRefDatabase
argument_list|()
operator|.
name|newBatchUpdate
argument_list|()
expr_stmt|;
name|batchRefUpdate
operator|.
name|setPushCertificate
argument_list|(
name|pushCert
argument_list|)
expr_stmt|;
name|batchRefUpdate
operator|.
name|setRefLogMessage
argument_list|(
name|refLogMessage
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|batchRefUpdate
operator|.
name|setAllowNonFastForwards
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repoView
operator|.
name|getCommands
argument_list|()
operator|.
name|addTo
argument_list|(
name|batchRefUpdate
argument_list|)
expr_stmt|;
name|logDebug
argument_list|(
literal|"Executing batch of {} ref updates"
argument_list|,
name|batchRefUpdate
operator|.
name|getCommands
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|dryrun
condition|)
block|{
return|return;
block|}
comment|// Force BatchRefUpdate to read newly referenced objects using a new RevWalk, rather than one
comment|// that might have access to unflushed objects.
try|try
init|(
name|RevWalk
name|updateRw
init|=
operator|new
name|RevWalk
argument_list|(
name|repoView
operator|.
name|getRepository
argument_list|()
argument_list|)
init|)
block|{
name|batchRefUpdate
operator|.
name|execute
argument_list|(
name|updateRw
argument_list|,
name|NullProgressMonitor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
name|boolean
name|ok
init|=
literal|true
decl_stmt|;
for|for
control|(
name|ReceiveCommand
name|cmd
range|:
name|batchRefUpdate
operator|.
name|getCommands
argument_list|()
control|)
block|{
if|if
condition|(
name|cmd
operator|.
name|getResult
argument_list|()
operator|!=
name|ReceiveCommand
operator|.
name|Result
operator|.
name|OK
condition|)
block|{
name|ok
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|ok
condition|)
block|{
throw|throw
operator|new
name|RestApiException
argument_list|(
literal|"BatchRefUpdate failed: "
operator|+
name|batchRefUpdate
argument_list|)
throw|;
block|}
block|}
DECL|method|executeChangeOps (boolean dryrun)
specifier|private
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeResult
argument_list|>
name|executeChangeOps
parameter_list|(
name|boolean
name|dryrun
parameter_list|)
throws|throws
name|Exception
block|{
name|logDebug
argument_list|(
literal|"Executing change ops"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeResult
argument_list|>
name|result
init|=
name|Maps
operator|.
name|newLinkedHashMapWithExpectedSize
argument_list|(
name|ops
operator|.
name|keySet
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|initRepository
argument_list|()
expr_stmt|;
name|Repository
name|repo
init|=
name|repoView
operator|.
name|getRepository
argument_list|()
decl_stmt|;
comment|// TODO(dborowitz): Teach NoteDbUpdateManager to allow reusing the same inserter and batch ref
comment|// update as in executeUpdateRepo.
try|try
init|(
name|ObjectInserter
name|ins
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
init|;
name|ObjectReader
name|reader
operator|=
name|ins
operator|.
name|newReader
argument_list|()
init|;
name|RevWalk
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|;
name|NoteDbUpdateManager
name|updateManager
operator|=
name|updateManagerFactory
operator|.
name|create
argument_list|(
name|project
argument_list|)
operator|.
name|setChangeRepo
argument_list|(
name|repo
argument_list|,
name|rw
argument_list|,
name|ins
argument_list|,
operator|new
name|ChainedReceiveCommands
argument_list|(
name|repo
argument_list|)
argument_list|)
init|)
block|{
if|if
condition|(
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|)
block|{
name|updateManager
operator|.
name|setRefLogIdent
argument_list|(
name|user
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|newRefLogIdent
argument_list|(
name|when
argument_list|,
name|tz
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|Collection
argument_list|<
name|BatchUpdateOp
argument_list|>
argument_list|>
name|e
range|:
name|ops
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|ChangeContextImpl
name|ctx
init|=
name|newChangeContext
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|boolean
name|dirty
init|=
literal|false
decl_stmt|;
name|logDebug
argument_list|(
literal|"Applying {} ops for change {}"
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|id
argument_list|)
expr_stmt|;
for|for
control|(
name|BatchUpdateOp
name|op
range|:
name|e
operator|.
name|getValue
argument_list|()
control|)
block|{
name|dirty
operator||=
name|op
operator|.
name|updateChange
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|dirty
condition|)
block|{
name|logDebug
argument_list|(
literal|"No ops reported dirty, short-circuiting"
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|ChangeResult
operator|.
name|SKIPPED
argument_list|)
expr_stmt|;
continue|continue;
block|}
for|for
control|(
name|ChangeUpdate
name|u
range|:
name|ctx
operator|.
name|updates
operator|.
name|values
argument_list|()
control|)
block|{
name|updateManager
operator|.
name|add
argument_list|(
name|u
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ctx
operator|.
name|deleted
condition|)
block|{
name|logDebug
argument_list|(
literal|"Change {} was deleted"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|updateManager
operator|.
name|deleteChange
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|ChangeResult
operator|.
name|DELETED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|ChangeResult
operator|.
name|UPSERTED
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|dryrun
condition|)
block|{
name|logDebug
argument_list|(
literal|"Executing NoteDb updates"
argument_list|)
expr_stmt|;
name|updateManager
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|newChangeContext (Change.Id id)
specifier|private
name|ChangeContextImpl
name|newChangeContext
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
name|logDebug
argument_list|(
literal|"Opening change {} for update"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Change
name|c
init|=
name|newChanges
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|boolean
name|isNew
init|=
name|c
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|isNew
condition|)
block|{
comment|// Pass a synthetic change into ChangeNotes.Factory, which will take care of checking for
comment|// existence and populating columns from the parsed notes state.
comment|// TODO(dborowitz): This dance made more sense when using Reviewdb; consider a nicer way.
name|c
operator|=
name|ChangeNotes
operator|.
name|Factory
operator|.
name|newNoteDbOnlyChange
argument_list|(
name|project
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logDebug
argument_list|(
literal|"Change {} is new"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
name|ChangeNotes
name|notes
init|=
name|changeNotesFactory
operator|.
name|createForBatchUpdate
argument_list|(
name|c
argument_list|,
operator|!
name|isNew
argument_list|)
decl_stmt|;
name|ChangeControl
name|ctl
init|=
name|changeControlFactory
operator|.
name|controlFor
argument_list|(
name|notes
argument_list|,
name|user
argument_list|)
decl_stmt|;
return|return
operator|new
name|ChangeContextImpl
argument_list|(
name|ctl
argument_list|)
return|;
block|}
DECL|method|reindexChanges (Map<Change.Id, ChangeResult> updateResults, boolean dryrun)
specifier|private
name|void
name|reindexChanges
parameter_list|(
name|Map
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeResult
argument_list|>
name|updateResults
parameter_list|,
name|boolean
name|dryrun
parameter_list|)
block|{
if|if
condition|(
name|dryrun
condition|)
block|{
return|return;
block|}
name|logDebug
argument_list|(
literal|"Reindexing {} changes"
argument_list|,
name|updateResults
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Change
operator|.
name|Id
argument_list|,
name|ChangeResult
argument_list|>
name|e
range|:
name|updateResults
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Change
operator|.
name|Id
name|id
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|e
operator|.
name|getValue
argument_list|()
condition|)
block|{
case|case
name|UPSERTED
case|:
name|indexFutures
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|indexAsync
argument_list|(
name|project
argument_list|,
name|id
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|DELETED
case|:
name|indexFutures
operator|.
name|add
argument_list|(
name|indexer
operator|.
name|deleteAsync
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|SKIPPED
case|:
break|break;
default|default:
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"unexpected result: "
operator|+
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|executePostOps ()
specifier|private
name|void
name|executePostOps
parameter_list|()
throws|throws
name|Exception
block|{
name|ContextImpl
name|ctx
init|=
operator|new
name|ContextImpl
argument_list|()
decl_stmt|;
for|for
control|(
name|BatchUpdateOp
name|op
range|:
name|ops
operator|.
name|values
argument_list|()
control|)
block|{
name|op
operator|.
name|postUpdate
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|RepoOnlyOp
name|op
range|:
name|repoOnlyOps
control|)
block|{
name|op
operator|.
name|postUpdate
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

