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
name|gwtorm
operator|.
name|server
operator|.
name|OrmException
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
block|{
DECL|field|message
name|String
name|message
decl_stmt|;
DECL|method|Input ()
specifier|public
name|Input
parameter_list|()
block|{}
DECL|method|Input (String message)
specifier|public
name|Input
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
block|}
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
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
DECL|method|WorkInProgressOp (ChangeMessagesUtil cmUtil, boolean workInProgress, Input in)
name|WorkInProgressOp
parameter_list|(
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|boolean
name|workInProgress
parameter_list|,
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
block|{
name|Change
name|change
init|=
name|ctx
operator|.
name|getChange
argument_list|()
decl_stmt|;
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
throws|throws
name|OrmException
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
name|in
operator|!=
literal|null
condition|?
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|in
operator|.
name|message
argument_list|)
operator|.
name|trim
argument_list|()
else|:
literal|""
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
name|ChangeMessage
name|cmsg
init|=
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
decl_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|update
argument_list|,
name|cmsg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

