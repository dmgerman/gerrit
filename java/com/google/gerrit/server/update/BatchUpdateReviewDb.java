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
name|ReviewDbUtil
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
name|server
operator|.
name|AtomicUpdate
import|;
end_import

begin_class
DECL|class|BatchUpdateReviewDb
specifier|public
class|class
name|BatchUpdateReviewDb
extends|extends
name|ReviewDbWrapper
block|{
DECL|field|changesWrapper
specifier|private
specifier|final
name|ChangeAccess
name|changesWrapper
decl_stmt|;
DECL|method|BatchUpdateReviewDb (ReviewDb delegate)
name|BatchUpdateReviewDb
parameter_list|(
name|ReviewDb
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
name|changesWrapper
operator|=
operator|new
name|BatchUpdateChanges
argument_list|(
name|delegate
operator|.
name|changes
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** @return the underlying delegate. Supports BatchUpdateReviewDb too. */
DECL|method|unwrap (ReviewDb db)
specifier|public
specifier|static
name|ReviewDb
name|unwrap
parameter_list|(
name|ReviewDb
name|db
parameter_list|)
block|{
if|if
condition|(
name|db
operator|instanceof
name|BatchUpdateReviewDb
condition|)
block|{
name|db
operator|=
operator|(
operator|(
name|BatchUpdateReviewDb
operator|)
name|db
operator|)
operator|.
name|unsafeGetDelegate
argument_list|()
expr_stmt|;
block|}
return|return
name|ReviewDbUtil
operator|.
name|unwrapDb
argument_list|(
name|db
argument_list|)
return|;
block|}
DECL|method|unsafeGetDelegate ()
specifier|public
name|ReviewDb
name|unsafeGetDelegate
parameter_list|()
block|{
return|return
name|delegate
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
name|changesWrapper
return|;
block|}
annotation|@
name|Override
DECL|method|commit ()
specifier|public
name|void
name|commit
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call commit; BatchUpdate always manages transactions"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|rollback ()
specifier|public
name|void
name|rollback
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call rollback; BatchUpdate always manages transactions"
argument_list|)
throw|;
block|}
DECL|class|BatchUpdateChanges
specifier|private
specifier|static
class|class
name|BatchUpdateChanges
extends|extends
name|ChangeAccessWrapper
block|{
DECL|method|BatchUpdateChanges (ChangeAccess delegate)
specifier|private
name|BatchUpdateChanges
parameter_list|(
name|ChangeAccess
name|delegate
parameter_list|)
block|{
name|super
argument_list|(
name|delegate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|insert (Iterable<Change> instances)
specifier|public
name|void
name|insert
parameter_list|(
name|Iterable
argument_list|<
name|Change
argument_list|>
name|instances
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call insert; change is automatically inserted"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|upsert (Iterable<Change> instances)
specifier|public
name|void
name|upsert
parameter_list|(
name|Iterable
argument_list|<
name|Change
argument_list|>
name|instances
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call upsert; existing changes are updated automatically,"
operator|+
literal|" or use InsertChangeOp for insertion"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|update (Iterable<Change> instances)
specifier|public
name|void
name|update
parameter_list|(
name|Iterable
argument_list|<
name|Change
argument_list|>
name|instances
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call update; change is updated automatically"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|beginTransaction (Change.Id key)
specifier|public
name|void
name|beginTransaction
parameter_list|(
name|Change
operator|.
name|Id
name|key
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"updateChange is always called within a transaction"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|deleteKeys (Iterable<Change.Id> keys)
specifier|public
name|void
name|deleteKeys
parameter_list|(
name|Iterable
argument_list|<
name|Change
operator|.
name|Id
argument_list|>
name|keys
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call deleteKeys; use ChangeContext#deleteChange()"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|delete (Iterable<Change> instances)
specifier|public
name|void
name|delete
parameter_list|(
name|Iterable
argument_list|<
name|Change
argument_list|>
name|instances
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call delete; use ChangeContext#deleteChange()"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|atomicUpdate (Change.Id key, AtomicUpdate<Change> update)
specifier|public
name|Change
name|atomicUpdate
parameter_list|(
name|Change
operator|.
name|Id
name|key
parameter_list|,
name|AtomicUpdate
argument_list|<
name|Change
argument_list|>
name|update
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"do not call atomicUpdate; updateChange is always called within a transaction"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

