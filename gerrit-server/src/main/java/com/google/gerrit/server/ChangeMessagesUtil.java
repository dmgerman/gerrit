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
name|NotesMigration
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
name|Singleton
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
name|Objects
import|;
end_import

begin_comment
comment|/**  * Utility functions to manipulate ChangeMessages.  *<p>  * These methods either query for and update ChangeMessages in the NoteDb or  * ReviewDb, depending on the state of the NotesMigration.  */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeMessagesUtil
specifier|public
class|class
name|ChangeMessagesUtil
block|{
DECL|method|sortChangeMessages ( Iterable<ChangeMessage> changeMessage)
specifier|private
specifier|static
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|sortChangeMessages
parameter_list|(
name|Iterable
argument_list|<
name|ChangeMessage
argument_list|>
name|changeMessage
parameter_list|)
block|{
return|return
name|ChangeNotes
operator|.
name|MESSAGE_BY_TIME
operator|.
name|sortedCopy
argument_list|(
name|changeMessage
argument_list|)
return|;
block|}
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
DECL|method|ChangeMessagesUtil (NotesMigration migration)
specifier|public
name|ChangeMessagesUtil
parameter_list|(
name|NotesMigration
name|migration
parameter_list|)
block|{
name|this
operator|.
name|migration
operator|=
name|migration
expr_stmt|;
block|}
DECL|method|byChange (ReviewDb db, ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|byChange
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
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|sortChangeMessages
argument_list|(
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|byChange
argument_list|(
name|notes
operator|.
name|getChangeId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getChangeMessages
argument_list|()
return|;
block|}
block|}
DECL|method|byPatchSet (ReviewDb db, ChangeNotes notes, PatchSet.Id psId)
specifier|public
name|Iterable
argument_list|<
name|ChangeMessage
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
name|readChanges
argument_list|()
condition|)
block|{
return|return
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|byPatchSet
argument_list|(
name|psId
argument_list|)
return|;
block|}
return|return
name|notes
operator|.
name|load
argument_list|()
operator|.
name|getChangeMessagesByPatchSet
argument_list|()
operator|.
name|get
argument_list|(
name|psId
argument_list|)
return|;
block|}
DECL|method|addChangeMessage (ReviewDb db, ChangeUpdate update, ChangeMessage changeMessage)
specifier|public
name|void
name|addChangeMessage
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|ChangeMessage
name|changeMessage
parameter_list|)
throws|throws
name|OrmException
block|{
name|checkState
argument_list|(
name|Objects
operator|.
name|equals
argument_list|(
name|changeMessage
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|update
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|,
literal|"cannot store change message by %s in update by %s"
argument_list|,
name|changeMessage
operator|.
name|getAuthor
argument_list|()
argument_list|,
name|update
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setChangeMessage
argument_list|(
name|changeMessage
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setTag
argument_list|(
name|changeMessage
operator|.
name|getTag
argument_list|()
argument_list|)
expr_stmt|;
name|db
operator|.
name|changeMessages
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|changeMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

