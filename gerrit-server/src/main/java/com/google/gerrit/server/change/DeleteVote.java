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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|TimeUtil
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
name|LabelTypes
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
name|api
operator|.
name|changes
operator|.
name|DeleteVoteInput
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
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|AuthException
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
name|BadRequestException
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
name|ResourceNotFoundException
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
name|Response
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
name|extensions
operator|.
name|restapi
operator|.
name|RestModifyView
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
name|LabelId
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
name|ApprovalsUtil
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
name|ChangeMessagesUtil
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
name|PatchSetUtil
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
name|VoteDeleted
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
name|BatchUpdate
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
name|BatchUpdate
operator|.
name|ChangeContext
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
name|BatchUpdate
operator|.
name|Context
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
name|UpdateException
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
name|DeleteVoteSender
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
name|ReplyToChangeSender
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
name|LabelVote
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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|HashMap
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteVote
specifier|public
class|class
name|DeleteVote
implements|implements
name|RestModifyView
argument_list|<
name|VoteResource
argument_list|,
name|DeleteVoteInput
argument_list|>
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DeleteVote
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|batchUpdateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|voteDeleted
specifier|private
specifier|final
name|VoteDeleted
name|voteDeleted
decl_stmt|;
DECL|field|deleteVoteSenderFactory
specifier|private
specifier|final
name|DeleteVoteSender
operator|.
name|Factory
name|deleteVoteSenderFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteVote (Provider<ReviewDb> db, BatchUpdate.Factory batchUpdateFactory, ApprovalsUtil approvalsUtil, PatchSetUtil psUtil, ChangeMessagesUtil cmUtil, IdentifiedUser.GenericFactory userFactory, VoteDeleted voteDeleted, DeleteVoteSender.Factory deleteVoteSenderFactory)
name|DeleteVote
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
name|VoteDeleted
name|voteDeleted
parameter_list|,
name|DeleteVoteSender
operator|.
name|Factory
name|deleteVoteSenderFactory
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|voteDeleted
operator|=
name|voteDeleted
expr_stmt|;
name|this
operator|.
name|deleteVoteSenderFactory
operator|=
name|deleteVoteSenderFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (VoteResource rsrc, DeleteVoteInput input)
specifier|public
name|Response
argument_list|<
name|?
argument_list|>
name|apply
parameter_list|(
name|VoteResource
name|rsrc
parameter_list|,
name|DeleteVoteInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
if|if
condition|(
name|input
operator|==
literal|null
condition|)
block|{
name|input
operator|=
operator|new
name|DeleteVoteInput
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|input
operator|.
name|label
operator|!=
literal|null
operator|&&
operator|!
name|rsrc
operator|.
name|getLabel
argument_list|()
operator|.
name|equals
argument_list|(
name|input
operator|.
name|label
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"label must match URL"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|.
name|notify
operator|==
literal|null
condition|)
block|{
name|input
operator|.
name|notify
operator|=
name|NotifyHandling
operator|.
name|ALL
expr_stmt|;
block|}
name|ReviewerResource
name|r
init|=
name|rsrc
operator|.
name|getReviewer
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|r
operator|.
name|getChange
argument_list|()
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|r
operator|.
name|getControl
argument_list|()
operator|.
name|getUser
argument_list|()
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
name|bu
operator|.
name|addOp
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|Op
argument_list|(
name|r
operator|.
name|getReviewerUser
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getLabel
argument_list|()
argument_list|,
name|input
argument_list|)
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
DECL|class|Op
specifier|private
class|class
name|Op
extends|extends
name|BatchUpdate
operator|.
name|Op
block|{
DECL|field|accountId
specifier|private
specifier|final
name|Account
operator|.
name|Id
name|accountId
decl_stmt|;
DECL|field|label
specifier|private
specifier|final
name|String
name|label
decl_stmt|;
DECL|field|input
specifier|private
specifier|final
name|DeleteVoteInput
name|input
decl_stmt|;
DECL|field|changeMessage
specifier|private
name|ChangeMessage
name|changeMessage
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|ps
specifier|private
name|PatchSet
name|ps
decl_stmt|;
DECL|field|newApprovals
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|newApprovals
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|oldApprovals
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Short
argument_list|>
name|oldApprovals
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|Op (Account.Id accountId, String label, DeleteVoteInput input)
specifier|private
name|Op
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|,
name|String
name|label
parameter_list|,
name|DeleteVoteInput
name|input
parameter_list|)
block|{
name|this
operator|.
name|accountId
operator|=
name|accountId
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|label
expr_stmt|;
name|this
operator|.
name|input
operator|=
name|input
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|updateChange (ChangeContext ctx)
specifier|public
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|OrmException
throws|,
name|AuthException
throws|,
name|ResourceNotFoundException
block|{
name|ChangeControl
name|ctl
init|=
name|ctx
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|change
operator|=
name|ctl
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|PatchSet
operator|.
name|Id
name|psId
init|=
name|change
operator|.
name|currentPatchSetId
argument_list|()
decl_stmt|;
name|ps
operator|=
name|psUtil
operator|.
name|current
argument_list|(
name|db
operator|.
name|get
argument_list|()
argument_list|,
name|ctl
operator|.
name|getNotes
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|LabelTypes
name|labelTypes
init|=
name|ctx
operator|.
name|getControl
argument_list|()
operator|.
name|getLabelTypes
argument_list|()
decl_stmt|;
for|for
control|(
name|PatchSetApproval
name|a
range|:
name|approvalsUtil
operator|.
name|byPatchSetUser
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctl
argument_list|,
name|psId
argument_list|,
name|accountId
argument_list|)
control|)
block|{
if|if
condition|(
name|labelTypes
operator|.
name|byLabel
argument_list|(
name|a
operator|.
name|getLabelId
argument_list|()
argument_list|)
operator|==
literal|null
condition|)
block|{
continue|continue;
comment|// Ignore undefined labels.
block|}
elseif|else
if|if
condition|(
operator|!
name|a
operator|.
name|getLabel
argument_list|()
operator|.
name|equals
argument_list|(
name|label
argument_list|)
condition|)
block|{
comment|// Populate map for non-matching labels, needed by VoteDeleted.
name|newApprovals
operator|.
name|put
argument_list|(
name|a
operator|.
name|getLabel
argument_list|()
argument_list|,
name|a
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
elseif|else
if|if
condition|(
operator|!
name|ctl
operator|.
name|canRemoveReviewer
argument_list|(
name|a
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"delete vote not permitted"
argument_list|)
throw|;
block|}
comment|// Set the approval to 0 if vote is being removed.
name|newApprovals
operator|.
name|put
argument_list|(
name|a
operator|.
name|getLabel
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
comment|// Set old value, as required by VoteDeleted.
name|oldApprovals
operator|.
name|put
argument_list|(
name|a
operator|.
name|getLabel
argument_list|()
argument_list|,
name|a
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|()
throw|;
block|}
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
operator|.
name|removeApprovalFor
argument_list|(
name|accountId
argument_list|,
name|label
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getDb
argument_list|()
operator|.
name|patchSetApprovals
argument_list|()
operator|.
name|upsert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|deletedApproval
argument_list|(
name|ctx
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"Removed "
argument_list|)
expr_stmt|;
name|LabelVote
operator|.
name|appendTo
argument_list|(
name|msg
argument_list|,
name|label
argument_list|,
name|checkNotNull
argument_list|(
name|oldApprovals
operator|.
name|get
argument_list|(
name|label
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" by "
argument_list|)
operator|.
name|append
argument_list|(
name|userFactory
operator|.
name|create
argument_list|(
name|accountId
argument_list|)
operator|.
name|getNameEmail
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|changeMessage
operator|=
name|ChangeMessagesUtil
operator|.
name|newMessage
argument_list|(
name|ctx
argument_list|,
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
argument_list|,
name|changeMessage
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|deletedApproval (ChangeContext ctx)
specifier|private
name|PatchSetApproval
name|deletedApproval
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
block|{
return|return
operator|new
name|PatchSetApproval
argument_list|(
operator|new
name|PatchSetApproval
operator|.
name|Key
argument_list|(
name|ps
operator|.
name|getId
argument_list|()
argument_list|,
name|accountId
argument_list|,
operator|new
name|LabelId
argument_list|(
name|label
argument_list|)
argument_list|)
argument_list|,
operator|(
name|short
operator|)
literal|0
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|postUpdate (Context ctx)
specifier|public
name|void
name|postUpdate
parameter_list|(
name|Context
name|ctx
parameter_list|)
block|{
if|if
condition|(
name|changeMessage
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|IdentifiedUser
name|user
init|=
name|ctx
operator|.
name|getIdentifiedUser
argument_list|()
decl_stmt|;
if|if
condition|(
name|input
operator|.
name|notify
operator|.
name|compareTo
argument_list|(
name|NotifyHandling
operator|.
name|NONE
argument_list|)
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|ReplyToChangeSender
name|cm
init|=
name|deleteVoteSenderFactory
operator|.
name|create
argument_list|(
name|ctx
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|user
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|changeMessage
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setNotify
argument_list|(
name|input
operator|.
name|notify
argument_list|)
expr_stmt|;
name|cm
operator|.
name|send
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot email update for change "
operator|+
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|voteDeleted
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|ps
argument_list|,
name|newApprovals
argument_list|,
name|oldApprovals
argument_list|,
name|input
operator|.
name|notify
argument_list|,
name|changeMessage
operator|.
name|getMessage
argument_list|()
argument_list|,
name|user
operator|.
name|getAccount
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

