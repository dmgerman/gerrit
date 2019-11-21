begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|collect
operator|.
name|ListMultimap
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
name|entities
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
name|exceptions
operator|.
name|StorageException
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
name|InMemoryInserter
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
name|InsertedObject
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
name|update
operator|.
name|ChainedReceiveCommands
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
name|Collection
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
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
comment|/**  * Wrapper around {@link Repository} that keeps track of related {@link ObjectInserter}s and other  * objects that are jointly closed when invoking {@link #close}.  */
end_comment

begin_class
DECL|class|OpenRepo
class|class
name|OpenRepo
implements|implements
name|AutoCloseable
block|{
DECL|field|UNLIMITED_UPDATES
specifier|private
specifier|static
specifier|final
name|Integer
name|UNLIMITED_UPDATES
init|=
literal|null
decl_stmt|;
DECL|field|UNLIMITED_PATCH_SETS
specifier|private
specifier|static
specifier|final
name|Integer
name|UNLIMITED_PATCH_SETS
init|=
literal|null
decl_stmt|;
DECL|field|repo
specifier|final
name|Repository
name|repo
decl_stmt|;
DECL|field|rw
specifier|final
name|RevWalk
name|rw
decl_stmt|;
DECL|field|cmds
specifier|final
name|ChainedReceiveCommands
name|cmds
decl_stmt|;
DECL|field|tempIns
specifier|final
name|ObjectInserter
name|tempIns
decl_stmt|;
DECL|field|inMemIns
specifier|private
specifier|final
name|InMemoryInserter
name|inMemIns
decl_stmt|;
DECL|field|finalIns
annotation|@
name|Nullable
specifier|private
specifier|final
name|ObjectInserter
name|finalIns
decl_stmt|;
DECL|field|close
specifier|private
specifier|final
name|boolean
name|close
decl_stmt|;
comment|/** Returns a {@link OpenRepo} wrapping around an open {@link Repository}. */
DECL|method|open (GitRepositoryManager repoManager, Project.NameKey project)
specifier|static
name|OpenRepo
name|open
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
throws|throws
name|IOException
block|{
name|Repository
name|repo
init|=
name|repoManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
decl_stmt|;
comment|// Closed by OpenRepo#close.
name|ObjectInserter
name|ins
init|=
name|repo
operator|.
name|newObjectInserter
argument_list|()
decl_stmt|;
comment|// Closed by OpenRepo#close.
name|ObjectReader
name|reader
init|=
name|ins
operator|.
name|newReader
argument_list|()
decl_stmt|;
comment|// Not closed by OpenRepo#close.
try|try
init|(
name|RevWalk
name|rw
init|=
operator|new
name|RevWalk
argument_list|(
name|reader
argument_list|)
init|)
block|{
comment|// Doesn't escape OpenRepo constructor.
return|return
operator|new
name|OpenRepo
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
argument_list|,
literal|true
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
DECL|method|OpenRepo ( Repository repo, RevWalk rw, @Nullable ObjectInserter ins, ChainedReceiveCommands cmds, boolean close)
name|OpenRepo
parameter_list|(
name|Repository
name|repo
parameter_list|,
name|RevWalk
name|rw
parameter_list|,
annotation|@
name|Nullable
name|ObjectInserter
name|ins
parameter_list|,
name|ChainedReceiveCommands
name|cmds
parameter_list|,
name|boolean
name|close
parameter_list|)
block|{
name|ObjectReader
name|reader
init|=
name|rw
operator|.
name|getObjectReader
argument_list|()
decl_stmt|;
name|checkArgument
argument_list|(
name|ins
operator|==
literal|null
operator|||
name|reader
operator|.
name|getCreatedFromInserter
argument_list|()
operator|==
name|ins
argument_list|,
literal|"expected reader to be created from %s, but was %s"
argument_list|,
name|ins
argument_list|,
name|reader
operator|.
name|getCreatedFromInserter
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|requireNonNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|this
operator|.
name|inMemIns
operator|=
operator|new
name|InMemoryInserter
argument_list|(
name|rw
operator|.
name|getObjectReader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|tempIns
operator|=
name|inMemIns
expr_stmt|;
name|this
operator|.
name|rw
operator|=
operator|new
name|RevWalk
argument_list|(
name|tempIns
operator|.
name|newReader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|finalIns
operator|=
name|ins
expr_stmt|;
name|this
operator|.
name|cmds
operator|=
name|requireNonNull
argument_list|(
name|cmds
argument_list|)
expr_stmt|;
name|this
operator|.
name|close
operator|=
name|close
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|rw
operator|.
name|getObjectReader
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|rw
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|close
condition|)
block|{
if|if
condition|(
name|finalIns
operator|!=
literal|null
condition|)
block|{
name|finalIns
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|repo
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|flush ()
name|void
name|flush
parameter_list|()
throws|throws
name|IOException
block|{
name|flushToFinalInserter
argument_list|()
expr_stmt|;
name|finalIns
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
DECL|method|flushToFinalInserter ()
name|void
name|flushToFinalInserter
parameter_list|()
throws|throws
name|IOException
block|{
name|checkState
argument_list|(
name|finalIns
operator|!=
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|InsertedObject
name|obj
range|:
name|inMemIns
operator|.
name|getInsertedObjects
argument_list|()
control|)
block|{
name|finalIns
operator|.
name|insert
argument_list|(
name|obj
operator|.
name|type
argument_list|()
argument_list|,
name|obj
operator|.
name|data
argument_list|()
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|inMemIns
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|allowWrite ( Collection<U> updates, ObjectId old)
specifier|private
specifier|static
parameter_list|<
name|U
extends|extends
name|AbstractChangeUpdate
parameter_list|>
name|boolean
name|allowWrite
parameter_list|(
name|Collection
argument_list|<
name|U
argument_list|>
name|updates
parameter_list|,
name|ObjectId
name|old
parameter_list|)
block|{
if|if
condition|(
operator|!
name|old
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
literal|true
return|;
block|}
return|return
name|updates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|allowWriteToNewRef
argument_list|()
return|;
block|}
DECL|method|addUpdatesNoLimits (ListMultimap<String, U> all)
parameter_list|<
name|U
extends|extends
name|AbstractChangeUpdate
parameter_list|>
name|void
name|addUpdatesNoLimits
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|U
argument_list|>
name|all
parameter_list|)
throws|throws
name|IOException
block|{
name|addUpdates
argument_list|(
name|all
argument_list|,
name|Optional
operator|.
name|empty
argument_list|()
comment|/* unlimited updates */
argument_list|,
name|Optional
operator|.
name|empty
argument_list|()
comment|/* unlimited patch sets */
argument_list|)
expr_stmt|;
block|}
DECL|method|addUpdates ( ListMultimap<String, U> all, Optional<Integer> maxUpdates, Optional<Integer> maxPatchSets)
parameter_list|<
name|U
extends|extends
name|AbstractChangeUpdate
parameter_list|>
name|void
name|addUpdates
parameter_list|(
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|U
argument_list|>
name|all
parameter_list|,
name|Optional
argument_list|<
name|Integer
argument_list|>
name|maxUpdates
parameter_list|,
name|Optional
argument_list|<
name|Integer
argument_list|>
name|maxPatchSets
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|U
argument_list|>
argument_list|>
name|e
range|:
name|all
operator|.
name|asMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|refName
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|U
argument_list|>
name|updates
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ObjectId
name|old
init|=
name|cmds
operator|.
name|get
argument_list|(
name|refName
argument_list|)
operator|.
name|orElse
argument_list|(
name|ObjectId
operator|.
name|zeroId
argument_list|()
argument_list|)
decl_stmt|;
comment|// Only actually write to the ref if one of the updates explicitly allows
comment|// us to do so, i.e. it is known to represent a new change. This avoids
comment|// writing partial change meta if the change hasn't been backfilled yet.
if|if
condition|(
operator|!
name|allowWrite
argument_list|(
name|updates
argument_list|,
name|old
argument_list|)
condition|)
block|{
continue|continue;
block|}
name|int
name|updateCount
init|=
literal|0
decl_stmt|;
name|U
name|first
init|=
name|updates
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxUpdates
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|checkState
argument_list|(
name|first
operator|.
name|getNotes
argument_list|()
operator|!=
literal|null
argument_list|,
literal|"expected ChangeNotes on %s"
argument_list|,
name|first
argument_list|)
expr_stmt|;
name|updateCount
operator|=
name|first
operator|.
name|getNotes
argument_list|()
operator|.
name|getUpdateCount
argument_list|()
expr_stmt|;
block|}
name|ObjectId
name|curr
init|=
name|old
decl_stmt|;
for|for
control|(
name|U
name|update
range|:
name|updates
control|)
block|{
if|if
condition|(
name|maxPatchSets
operator|.
name|isPresent
argument_list|()
operator|&&
name|update
operator|.
name|psId
operator|!=
literal|null
condition|)
block|{
comment|// Patch set IDs are assigned consecutively. Patch sets may have been deleted, but the ID
comment|// is still a good estimate and an upper bound.
if|if
condition|(
name|update
operator|.
name|psId
operator|.
name|get
argument_list|()
operator|>
name|maxPatchSets
operator|.
name|get
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|LimitExceededException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %d may not exceed %d patch sets. To continue working on this change, "
operator|+
literal|"recreate it with a new Change-Id, then abandon this one."
argument_list|,
name|update
operator|.
name|getId
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|maxPatchSets
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|update
operator|.
name|isRootOnly
argument_list|()
operator|&&
operator|!
name|old
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
throw|throw
operator|new
name|StorageException
argument_list|(
literal|"Given ChangeUpdate is only allowed on initial commit"
argument_list|)
throw|;
block|}
name|ObjectId
name|next
init|=
name|update
operator|.
name|apply
argument_list|(
name|rw
argument_list|,
name|tempIns
argument_list|,
name|curr
argument_list|)
decl_stmt|;
if|if
condition|(
name|next
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|maxUpdates
operator|.
name|isPresent
argument_list|()
operator|&&
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|next
argument_list|,
name|curr
argument_list|)
operator|&&
operator|++
name|updateCount
operator|>
name|maxUpdates
operator|.
name|get
argument_list|()
operator|&&
operator|!
name|update
operator|.
name|bypassMaxUpdates
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|LimitExceededException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %s may not exceed %d updates. It may still be abandoned or submitted. To"
operator|+
literal|" continue working on this change, recreate it with a new Change-Id, then"
operator|+
literal|" abandon this one."
argument_list|,
name|update
operator|.
name|getId
argument_list|()
argument_list|,
name|maxUpdates
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|curr
operator|=
name|next
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|old
operator|.
name|equals
argument_list|(
name|curr
argument_list|)
condition|)
block|{
name|cmds
operator|.
name|add
argument_list|(
operator|new
name|ReceiveCommand
argument_list|(
name|old
argument_list|,
name|curr
argument_list|,
name|refName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

