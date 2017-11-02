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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|checkState
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
name|util
operator|.
name|concurrent
operator|.
name|Futures
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
name|ChangeMessage
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
name|PatchSetApproval
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
name|ChangeAccess
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
name|ChangeMessageAccess
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
name|PatchLineCommentAccess
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
name|PatchSetAccess
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
name|PatchSetApprovalAccess
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
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDbWrapper
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
name|client
operator|.
name|Key
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
name|Access
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
name|AtomicUpdate
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
name|ListResultSet
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
name|function
operator|.
name|Function
import|;
end_import

begin_comment
comment|/**  * Wrapper for ReviewDb that never calls the underlying change tables.  *  *<p>See {@link NotesMigrationSchemaFactory} for discussion.  */
end_comment

begin_class
DECL|class|NoChangesReviewDbWrapper
class|class
name|NoChangesReviewDbWrapper
extends|extends
name|ReviewDbWrapper
block|{
DECL|method|empty ()
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ResultSet
argument_list|<
name|T
argument_list|>
name|empty
parameter_list|()
block|{
return|return
operator|new
name|ListResultSet
argument_list|<>
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|,
name|K
extends|extends
name|Key
argument_list|<
name|?
argument_list|>
parameter_list|>
DECL|method|emptyFuture ()
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
name|T
argument_list|,
name|OrmException
argument_list|>
name|emptyFuture
parameter_list|()
block|{
return|return
name|Futures
operator|.
name|immediateCheckedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|field|changes
specifier|private
specifier|final
name|ChangeAccess
name|changes
decl_stmt|;
DECL|field|patchSetApprovals
specifier|private
specifier|final
name|PatchSetApprovalAccess
name|patchSetApprovals
decl_stmt|;
DECL|field|changeMessages
specifier|private
specifier|final
name|ChangeMessageAccess
name|changeMessages
decl_stmt|;
DECL|field|patchSets
specifier|private
specifier|final
name|PatchSetAccess
name|patchSets
decl_stmt|;
DECL|field|patchComments
specifier|private
specifier|final
name|PatchLineCommentAccess
name|patchComments
decl_stmt|;
DECL|field|inTransaction
specifier|private
name|boolean
name|inTransaction
decl_stmt|;
DECL|method|NoChangesReviewDbWrapper (ReviewDb db)
name|NoChangesReviewDbWrapper
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|changes
operator|=
operator|new
name|Changes
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|patchSetApprovals
operator|=
operator|new
name|PatchSetApprovals
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|changeMessages
operator|=
operator|new
name|ChangeMessages
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|patchSets
operator|=
operator|new
name|PatchSets
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
name|patchComments
operator|=
operator|new
name|PatchLineComments
argument_list|(
name|this
argument_list|,
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|changesTablesEnabled ()
specifier|public
name|boolean
name|changesTablesEnabled
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|changes ()
specifier|public
name|ChangeAccess
name|changes
parameter_list|()
block|{
return|return
name|changes
return|;
block|}
annotation|@
name|Override
DECL|method|patchSetApprovals ()
specifier|public
name|PatchSetApprovalAccess
name|patchSetApprovals
parameter_list|()
block|{
return|return
name|patchSetApprovals
return|;
block|}
annotation|@
name|Override
DECL|method|changeMessages ()
specifier|public
name|ChangeMessageAccess
name|changeMessages
parameter_list|()
block|{
return|return
name|changeMessages
return|;
block|}
annotation|@
name|Override
DECL|method|patchSets ()
specifier|public
name|PatchSetAccess
name|patchSets
parameter_list|()
block|{
return|return
name|patchSets
return|;
block|}
annotation|@
name|Override
DECL|method|patchComments ()
specifier|public
name|PatchLineCommentAccess
name|patchComments
parameter_list|()
block|{
return|return
name|patchComments
return|;
block|}
annotation|@
name|Override
DECL|method|commit ()
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
operator|!
name|inTransaction
condition|)
block|{
comment|// This reads a little weird, we're not in a transaction, so why are we calling commit?
comment|// Because we want to let the underlying ReviewDb do its normal thing in this case (which may
comment|// be throwing an exception, or not, depending on implementation).
name|delegate
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|rollback ()
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|OrmException
block|{
if|if
condition|(
name|inTransaction
condition|)
block|{
name|inTransaction
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
comment|// See comment in commit(): we want to let the underlying ReviewDb do its thing.
name|delegate
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|AbstractDisabledAccess
specifier|private
specifier|abstract
specifier|static
class|class
name|AbstractDisabledAccess
parameter_list|<
name|T
parameter_list|,
name|K
extends|extends
name|Key
parameter_list|<
name|?
parameter_list|>
parameter_list|>
implements|implements
name|Access
argument_list|<
name|T
argument_list|,
name|K
argument_list|>
block|{
comment|// Don't even hold a reference to delegate, so it's not possible to use it accidentally.
DECL|field|wrapper
specifier|private
specifier|final
name|NoChangesReviewDbWrapper
name|wrapper
decl_stmt|;
DECL|field|relationName
specifier|private
specifier|final
name|String
name|relationName
decl_stmt|;
DECL|field|relationId
specifier|private
specifier|final
name|int
name|relationId
decl_stmt|;
DECL|field|primaryKey
specifier|private
specifier|final
name|Function
argument_list|<
name|T
argument_list|,
name|K
argument_list|>
name|primaryKey
decl_stmt|;
DECL|field|toMap
specifier|private
specifier|final
name|Function
argument_list|<
name|Iterable
argument_list|<
name|T
argument_list|>
argument_list|,
name|Map
argument_list|<
name|K
argument_list|,
name|T
argument_list|>
argument_list|>
name|toMap
decl_stmt|;
DECL|method|AbstractDisabledAccess (NoChangesReviewDbWrapper wrapper, Access<T, K> delegate)
specifier|private
name|AbstractDisabledAccess
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|Access
argument_list|<
name|T
argument_list|,
name|K
argument_list|>
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|wrapper
operator|=
name|wrapper
expr_stmt|;
name|this
operator|.
name|relationName
operator|=
name|delegate
operator|.
name|getRelationName
argument_list|()
expr_stmt|;
name|this
operator|.
name|relationId
operator|=
name|delegate
operator|.
name|getRelationID
argument_list|()
expr_stmt|;
name|this
operator|.
name|primaryKey
operator|=
name|delegate
operator|::
name|primaryKey
expr_stmt|;
name|this
operator|.
name|toMap
operator|=
name|delegate
operator|::
name|toMap
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRelationID ()
specifier|public
specifier|final
name|int
name|getRelationID
parameter_list|()
block|{
return|return
name|relationId
return|;
block|}
annotation|@
name|Override
DECL|method|getRelationName ()
specifier|public
specifier|final
name|String
name|getRelationName
parameter_list|()
block|{
return|return
name|relationName
return|;
block|}
annotation|@
name|Override
DECL|method|primaryKey (T entity)
specifier|public
specifier|final
name|K
name|primaryKey
parameter_list|(
name|T
name|entity
parameter_list|)
block|{
return|return
name|primaryKey
operator|.
name|apply
argument_list|(
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toMap (Iterable<T> iterable)
specifier|public
specifier|final
name|Map
argument_list|<
name|K
argument_list|,
name|T
argument_list|>
name|toMap
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|iterable
parameter_list|)
block|{
return|return
name|toMap
operator|.
name|apply
argument_list|(
name|iterable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|iterateAllEntities ()
specifier|public
specifier|final
name|ResultSet
argument_list|<
name|T
argument_list|>
name|iterateAllEntities
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
annotation|@
name|Override
DECL|method|getAsync (K key)
specifier|public
specifier|final
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
name|T
argument_list|,
name|OrmException
argument_list|>
name|getAsync
parameter_list|(
name|K
name|key
parameter_list|)
block|{
return|return
name|emptyFuture
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|get (Iterable<K> keys)
specifier|public
specifier|final
name|ResultSet
argument_list|<
name|T
argument_list|>
name|get
parameter_list|(
name|Iterable
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|insert (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|insert
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|update (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|update
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|upsert (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|upsert
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|deleteKeys (Iterable<K> keys)
specifier|public
specifier|final
name|void
name|deleteKeys
parameter_list|(
name|Iterable
argument_list|<
name|K
argument_list|>
name|keys
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|delete (Iterable<T> instances)
specifier|public
specifier|final
name|void
name|delete
parameter_list|(
name|Iterable
argument_list|<
name|T
argument_list|>
name|instances
parameter_list|)
block|{
comment|// Do nothing.
block|}
annotation|@
name|Override
DECL|method|beginTransaction (K key)
specifier|public
specifier|final
name|void
name|beginTransaction
parameter_list|(
name|K
name|key
parameter_list|)
block|{
comment|// Keep track of when we've started a transaction so that we can avoid calling commit/rollback
comment|// on the underlying ReviewDb. This is just a simple arm's-length approach, and may produce
comment|// slightly different results from a native ReviewDb in corner cases like:
comment|//  * beginning transactions on different tables simultaneously
comment|//  * doing work between commit and rollback
comment|// These kinds of things are already misuses of ReviewDb, and shouldn't be happening in
comment|// current code anyway.
name|checkState
argument_list|(
operator|!
name|wrapper
operator|.
name|inTransaction
argument_list|,
literal|"already in transaction"
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|inTransaction
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|atomicUpdate (K key, AtomicUpdate<T> update)
specifier|public
specifier|final
name|T
name|atomicUpdate
parameter_list|(
name|K
name|key
parameter_list|,
name|AtomicUpdate
argument_list|<
name|T
argument_list|>
name|update
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|get (K id)
specifier|public
specifier|final
name|T
name|get
parameter_list|(
name|K
name|id
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|Changes
specifier|private
specifier|static
class|class
name|Changes
extends|extends
name|AbstractDisabledAccess
argument_list|<
name|Change
argument_list|,
name|Change
operator|.
name|Id
argument_list|>
implements|implements
name|ChangeAccess
block|{
DECL|method|Changes (NoChangesReviewDbWrapper wrapper, ReviewDb db)
specifier|private
name|Changes
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|wrapper
argument_list|,
name|db
operator|.
name|changes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|ResultSet
argument_list|<
name|Change
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
block|}
DECL|class|ChangeMessages
specifier|private
specifier|static
class|class
name|ChangeMessages
extends|extends
name|AbstractDisabledAccess
argument_list|<
name|ChangeMessage
argument_list|,
name|ChangeMessage
operator|.
name|Key
argument_list|>
implements|implements
name|ChangeMessageAccess
block|{
DECL|method|ChangeMessages (NoChangesReviewDbWrapper wrapper, ReviewDb db)
specifier|private
name|ChangeMessages
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|wrapper
argument_list|,
name|db
operator|.
name|changeMessages
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|byChange (Change.Id id)
specifier|public
name|ResultSet
argument_list|<
name|ChangeMessage
argument_list|>
name|byChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|byPatchSet (PatchSet.Id id)
specifier|public
name|ResultSet
argument_list|<
name|ChangeMessage
argument_list|>
name|byPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|ResultSet
argument_list|<
name|ChangeMessage
argument_list|>
name|all
parameter_list|()
throws|throws
name|OrmException
block|{
return|return
name|empty
argument_list|()
return|;
block|}
block|}
DECL|class|PatchSets
specifier|private
specifier|static
class|class
name|PatchSets
extends|extends
name|AbstractDisabledAccess
argument_list|<
name|PatchSet
argument_list|,
name|PatchSet
operator|.
name|Id
argument_list|>
implements|implements
name|PatchSetAccess
block|{
DECL|method|PatchSets (NoChangesReviewDbWrapper wrapper, ReviewDb db)
specifier|private
name|PatchSets
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|wrapper
argument_list|,
name|db
operator|.
name|patchSets
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|byChange (Change.Id id)
specifier|public
name|ResultSet
argument_list|<
name|PatchSet
argument_list|>
name|byChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|ResultSet
argument_list|<
name|PatchSet
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
block|}
DECL|class|PatchSetApprovals
specifier|private
specifier|static
class|class
name|PatchSetApprovals
extends|extends
name|AbstractDisabledAccess
argument_list|<
name|PatchSetApproval
argument_list|,
name|PatchSetApproval
operator|.
name|Key
argument_list|>
implements|implements
name|PatchSetApprovalAccess
block|{
DECL|method|PatchSetApprovals (NoChangesReviewDbWrapper wrapper, ReviewDb db)
specifier|private
name|PatchSetApprovals
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|wrapper
argument_list|,
name|db
operator|.
name|patchSetApprovals
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|byChange (Change.Id id)
specifier|public
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|byPatchSet (PatchSet.Id id)
specifier|public
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|byPatchSetUser (PatchSet.Id patchSet, Account.Id account)
specifier|public
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|byPatchSetUser
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSet
parameter_list|,
name|Account
operator|.
name|Id
name|account
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|ResultSet
argument_list|<
name|PatchSetApproval
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
block|}
DECL|class|PatchLineComments
specifier|private
specifier|static
class|class
name|PatchLineComments
extends|extends
name|AbstractDisabledAccess
argument_list|<
name|PatchLineComment
argument_list|,
name|PatchLineComment
operator|.
name|Key
argument_list|>
implements|implements
name|PatchLineCommentAccess
block|{
DECL|method|PatchLineComments (NoChangesReviewDbWrapper wrapper, ReviewDb db)
specifier|private
name|PatchLineComments
parameter_list|(
name|NoChangesReviewDbWrapper
name|wrapper
parameter_list|,
name|ReviewDb
name|db
parameter_list|)
block|{
name|super
argument_list|(
name|wrapper
argument_list|,
name|db
operator|.
name|patchComments
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|byChange (Change.Id id)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|byChange
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|byPatchSet (PatchSet.Id id)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|byPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|publishedByChangeFile (Change.Id id, String file)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByChangeFile
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|String
name|file
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|publishedByPatchSet (PatchSet.Id patchset)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|publishedByPatchSet
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchset
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|draftByPatchSetAuthor ( PatchSet.Id patchset, Account.Id author)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByPatchSetAuthor
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchset
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|draftByChangeFileAuthor ( Change.Id id, String file, Account.Id author)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByChangeFileAuthor
parameter_list|(
name|Change
operator|.
name|Id
name|id
parameter_list|,
name|String
name|file
parameter_list|,
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|draftByAuthor (Account.Id author)
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|draftByAuthor
parameter_list|(
name|Account
operator|.
name|Id
name|author
parameter_list|)
block|{
return|return
name|empty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|all ()
specifier|public
name|ResultSet
argument_list|<
name|PatchLineComment
argument_list|>
name|all
parameter_list|()
block|{
return|return
name|empty
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit
