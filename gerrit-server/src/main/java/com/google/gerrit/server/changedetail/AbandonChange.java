begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.changedetail
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|changedetail
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
name|common
operator|.
name|ChangeHooks
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
name|data
operator|.
name|ReviewResult
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
name|ChangeUtil
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
name|IdentifiedUser
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
name|mail
operator|.
name|AbandonedSender
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
name|mail
operator|.
name|EmailException
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
name|InvalidChangeOperationException
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
name|gwtorm
operator|.
name|client
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
name|client
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
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_class
DECL|class|AbandonChange
specifier|public
class|class
name|AbandonChange
implements|implements
name|Callable
argument_list|<
name|ReviewResult
argument_list|>
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (PatchSet.Id patchSetId, String changeComment)
name|AbandonChange
name|create
parameter_list|(
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
name|String
name|changeComment
parameter_list|)
function_decl|;
block|}
DECL|field|abandonedSenderFactory
specifier|private
specifier|final
name|AbandonedSender
operator|.
name|Factory
name|abandonedSenderFactory
decl_stmt|;
DECL|field|changeControlFactory
specifier|private
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|currentUser
specifier|private
specifier|final
name|IdentifiedUser
name|currentUser
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|patchSetId
specifier|private
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
decl_stmt|;
DECL|field|changeComment
specifier|private
specifier|final
name|String
name|changeComment
decl_stmt|;
annotation|@
name|Inject
DECL|method|AbandonChange (final AbandonedSender.Factory abandonedSenderFactory, final ChangeControl.Factory changeControlFactory, final ReviewDb db, final IdentifiedUser currentUser, final ChangeHooks hooks, @Assisted final PatchSet.Id patchSetId, @Assisted final String changeComment)
name|AbandonChange
parameter_list|(
specifier|final
name|AbandonedSender
operator|.
name|Factory
name|abandonedSenderFactory
parameter_list|,
specifier|final
name|ChangeControl
operator|.
name|Factory
name|changeControlFactory
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|IdentifiedUser
name|currentUser
parameter_list|,
specifier|final
name|ChangeHooks
name|hooks
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|PatchSet
operator|.
name|Id
name|patchSetId
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|String
name|changeComment
parameter_list|)
block|{
name|this
operator|.
name|abandonedSenderFactory
operator|=
name|abandonedSenderFactory
expr_stmt|;
name|this
operator|.
name|changeControlFactory
operator|=
name|changeControlFactory
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|currentUser
operator|=
name|currentUser
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|patchSetId
operator|=
name|patchSetId
expr_stmt|;
name|this
operator|.
name|changeComment
operator|=
name|changeComment
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ReviewResult
name|call
parameter_list|()
throws|throws
name|EmailException
throws|,
name|InvalidChangeOperationException
throws|,
name|NoSuchChangeException
throws|,
name|OrmException
block|{
specifier|final
name|ReviewResult
name|result
init|=
operator|new
name|ReviewResult
argument_list|()
decl_stmt|;
specifier|final
name|Change
operator|.
name|Id
name|changeId
init|=
name|patchSetId
operator|.
name|getParentKey
argument_list|()
decl_stmt|;
name|result
operator|.
name|setChangeId
argument_list|(
name|changeId
argument_list|)
expr_stmt|;
specifier|final
name|ChangeControl
name|control
init|=
name|changeControlFactory
operator|.
name|validateFor
argument_list|(
name|changeId
argument_list|)
decl_stmt|;
specifier|final
name|PatchSet
name|patch
init|=
name|db
operator|.
name|patchSets
argument_list|()
operator|.
name|get
argument_list|(
name|patchSetId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canAbandon
argument_list|()
condition|)
block|{
name|result
operator|.
name|addError
argument_list|(
operator|new
name|ReviewResult
operator|.
name|Error
argument_list|(
name|ReviewResult
operator|.
name|Error
operator|.
name|Type
operator|.
name|ABANDON_NOT_PERMITTED
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|patch
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchChangeException
argument_list|(
name|changeId
argument_list|)
throw|;
block|}
else|else
block|{
comment|// Create a message to accompany the abandoned change
specifier|final
name|ChangeMessage
name|cmsg
init|=
operator|new
name|ChangeMessage
argument_list|(
operator|new
name|ChangeMessage
operator|.
name|Key
argument_list|(
name|changeId
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|,
name|currentUser
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|patchSetId
argument_list|)
decl_stmt|;
specifier|final
name|StringBuilder
name|msgBuf
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Patch Set "
operator|+
name|patchSetId
operator|.
name|get
argument_list|()
operator|+
literal|": Abandoned"
argument_list|)
decl_stmt|;
if|if
condition|(
name|changeComment
operator|!=
literal|null
operator|&&
name|changeComment
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|msgBuf
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|msgBuf
operator|.
name|append
argument_list|(
name|changeComment
argument_list|)
expr_stmt|;
block|}
name|cmsg
operator|.
name|setMessage
argument_list|(
name|msgBuf
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Abandon the change
specifier|final
name|Change
name|updatedChange
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|atomicUpdate
argument_list|(
name|changeId
argument_list|,
operator|new
name|AtomicUpdate
argument_list|<
name|Change
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Change
name|update
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
operator|&&
name|change
operator|.
name|currentPatchSetId
argument_list|()
operator|.
name|equals
argument_list|(
name|patchSetId
argument_list|)
condition|)
block|{
name|change
operator|.
name|setStatus
argument_list|(
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
argument_list|)
expr_stmt|;
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|change
argument_list|)
expr_stmt|;
return|return
name|change
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|ChangeUtil
operator|.
name|updatedChange
argument_list|(
name|db
argument_list|,
name|currentUser
argument_list|,
name|updatedChange
argument_list|,
name|cmsg
argument_list|,
name|abandonedSenderFactory
argument_list|,
literal|"Change is no longer open or patchset is not latest"
argument_list|)
expr_stmt|;
name|hooks
operator|.
name|doChangeAbandonedHook
argument_list|(
name|updatedChange
argument_list|,
name|currentUser
operator|.
name|getAccount
argument_list|()
argument_list|,
name|changeComment
argument_list|,
name|db
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

