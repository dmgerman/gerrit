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
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Strings
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
name|entities
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
name|entities
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
name|common
operator|.
name|InputWithMessage
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
name|WorkInProgressStateChanged
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
name|update
operator|.
name|BatchUpdateOp
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
name|update
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

begin_comment
comment|/* Set work in progress or ready for review state on a change */
end_comment

begin_class
DECL|class|WorkInProgressOp
specifier|public
class|class
name|WorkInProgressOp
implements|implements
name|BatchUpdateOp
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
extends|extends
name|InputWithMessage
block|{
DECL|field|notify
annotation|@
name|Nullable
specifier|public
name|NotifyHandling
name|notify
decl_stmt|;
DECL|method|Input ()
specifier|public
name|Input
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|Input (@ullable String message)
specifier|public
name|Input
parameter_list|(
annotation|@
name|Nullable
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (boolean workInProgress, Input in)
name|WorkInProgressOp
name|create
parameter_list|(
name|boolean
name|workInProgress
parameter_list|,
name|Input
name|in
parameter_list|)
function_decl|;
block|}
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|email
specifier|private
specifier|final
name|EmailReviewComments
operator|.
name|Factory
name|email
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|workInProgress
specifier|private
specifier|final
name|boolean
name|workInProgress
decl_stmt|;
DECL|field|in
specifier|private
specifier|final
name|Input
name|in
decl_stmt|;
DECL|field|stateChanged
specifier|private
specifier|final
name|WorkInProgressStateChanged
name|stateChanged
decl_stmt|;
DECL|field|sendEmail
specifier|private
name|boolean
name|sendEmail
init|=
literal|true
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|notes
specifier|private
name|ChangeNotes
name|notes
decl_stmt|;
DECL|field|ps
specifier|private
name|PatchSet
name|ps
decl_stmt|;
DECL|field|cmsg
specifier|private
name|ChangeMessage
name|cmsg
decl_stmt|;
annotation|@
name|Inject
DECL|method|WorkInProgressOp ( ChangeMessagesUtil cmUtil, EmailReviewComments.Factory email, PatchSetUtil psUtil, WorkInProgressStateChanged stateChanged, @Assisted boolean workInProgress, @Assisted Input in)
name|WorkInProgressOp
parameter_list|(
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|EmailReviewComments
operator|.
name|Factory
name|email
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|WorkInProgressStateChanged
name|stateChanged
parameter_list|,
annotation|@
name|Assisted
name|boolean
name|workInProgress
parameter_list|,
annotation|@
name|Assisted
name|Input
name|in
parameter_list|)
block|{
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|email
operator|=
name|email
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|stateChanged
operator|=
name|stateChanged
expr_stmt|;
name|this
operator|.
name|workInProgress
operator|=
name|workInProgress
expr_stmt|;
name|this
operator|.
name|in
operator|=
name|in
expr_stmt|;
block|}
DECL|method|suppressEmail ()
specifier|public
name|void
name|suppressEmail
parameter_list|()
block|{
name|this
operator|.
name|sendEmail
operator|=
literal|false
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
block|{
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
expr_stmt|;
name|notes
operator|=
name|ctx
operator|.
name|getNotes
argument_list|()
expr_stmt|;
name|ps
operator|=
name|psUtil
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|,
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
expr_stmt|;
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|change
operator|.
name|setWorkInProgress
argument_list|(
name|workInProgress
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|change
operator|.
name|hasReviewStarted
argument_list|()
operator|&&
operator|!
name|workInProgress
condition|)
block|{
name|change
operator|.
name|setReviewStarted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|change
operator|.
name|setLastUpdatedOn
argument_list|(
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
name|update
operator|.
name|setWorkInProgress
argument_list|(
name|workInProgress
argument_list|)
expr_stmt|;
name|addMessage
argument_list|(
name|ctx
argument_list|,
name|update
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|addMessage (ChangeContext ctx, ChangeUpdate update)
specifier|private
name|void
name|addMessage
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|)
block|{
name|Change
name|c
init|=
name|ctx
operator|.
name|getChange
argument_list|()
decl_stmt|;
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|(
name|c
operator|.
name|isWorkInProgress
argument_list|()
condition|?
literal|"Set Work In Progress"
else|:
literal|"Set Ready For Review"
argument_list|)
decl_stmt|;
name|String
name|m
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|in
operator|==
literal|null
condition|?
literal|null
else|:
name|in
operator|.
name|message
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|m
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|m
argument_list|)
expr_stmt|;
block|}
name|cmsg
operator|=
name|ChangeMessagesUtil
operator|.
name|newMessage
argument_list|(
name|ctx
argument_list|,
name|buf
operator|.
name|toString
argument_list|()
argument_list|,
name|c
operator|.
name|isWorkInProgress
argument_list|()
condition|?
name|ChangeMessagesUtil
operator|.
name|TAG_SET_WIP
else|:
name|ChangeMessagesUtil
operator|.
name|TAG_SET_READY
argument_list|)
expr_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|update
argument_list|,
name|cmsg
argument_list|)
expr_stmt|;
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
name|stateChanged
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|ps
argument_list|,
name|ctx
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
name|NotifyResolver
operator|.
name|Result
name|notify
init|=
name|ctx
operator|.
name|getNotify
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|workInProgress
operator|||
name|notify
operator|.
name|handling
argument_list|()
operator|.
name|compareTo
argument_list|(
name|NotifyHandling
operator|.
name|OWNER_REVIEWERS
argument_list|)
operator|<
literal|0
operator|||
operator|!
name|sendEmail
condition|)
block|{
return|return;
block|}
name|email
operator|.
name|create
argument_list|(
name|notify
argument_list|,
name|notes
argument_list|,
name|ps
argument_list|,
name|ctx
operator|.
name|getIdentifiedUser
argument_list|()
argument_list|,
name|cmsg
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|,
name|cmsg
operator|.
name|getMessage
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
operator|.
name|sendAsync
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

