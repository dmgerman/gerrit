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
name|common
operator|.
name|ChangeMessageInfo
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
name|account
operator|.
name|AccountLoader
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
name|ChangeContext
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
comment|/** Utility functions to manipulate ChangeMessages. */
end_comment

begin_class
annotation|@
name|Singleton
DECL|class|ChangeMessagesUtil
specifier|public
class|class
name|ChangeMessagesUtil
block|{
DECL|field|AUTOGENERATED_TAG_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|AUTOGENERATED_TAG_PREFIX
init|=
literal|"autogenerated:"
decl_stmt|;
DECL|field|TAG_ABANDON
specifier|public
specifier|static
specifier|final
name|String
name|TAG_ABANDON
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:abandon"
decl_stmt|;
DECL|field|TAG_CHERRY_PICK_CHANGE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_CHERRY_PICK_CHANGE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:cherryPickChange"
decl_stmt|;
DECL|field|TAG_DELETE_ASSIGNEE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_DELETE_ASSIGNEE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:deleteAssignee"
decl_stmt|;
DECL|field|TAG_DELETE_REVIEWER
specifier|public
specifier|static
specifier|final
name|String
name|TAG_DELETE_REVIEWER
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:deleteReviewer"
decl_stmt|;
DECL|field|TAG_DELETE_VOTE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_DELETE_VOTE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:deleteVote"
decl_stmt|;
DECL|field|TAG_MERGED
specifier|public
specifier|static
specifier|final
name|String
name|TAG_MERGED
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:merged"
decl_stmt|;
DECL|field|TAG_MOVE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_MOVE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:move"
decl_stmt|;
DECL|field|TAG_RESTORE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_RESTORE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:restore"
decl_stmt|;
DECL|field|TAG_REVERT
specifier|public
specifier|static
specifier|final
name|String
name|TAG_REVERT
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:revert"
decl_stmt|;
DECL|field|TAG_SET_ASSIGNEE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_ASSIGNEE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setAssignee"
decl_stmt|;
DECL|field|TAG_SET_DESCRIPTION
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_DESCRIPTION
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setPsDescription"
decl_stmt|;
DECL|field|TAG_SET_HASHTAGS
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_HASHTAGS
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setHashtag"
decl_stmt|;
DECL|field|TAG_SET_PRIVATE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_PRIVATE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setPrivate"
decl_stmt|;
DECL|field|TAG_SET_READY
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_READY
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setReadyForReview"
decl_stmt|;
DECL|field|TAG_SET_TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_TOPIC
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setTopic"
decl_stmt|;
DECL|field|TAG_SET_WIP
specifier|public
specifier|static
specifier|final
name|String
name|TAG_SET_WIP
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:setWorkInProgress"
decl_stmt|;
DECL|field|TAG_UNSET_PRIVATE
specifier|public
specifier|static
specifier|final
name|String
name|TAG_UNSET_PRIVATE
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:unsetPrivate"
decl_stmt|;
DECL|field|TAG_UPLOADED_PATCH_SET
specifier|public
specifier|static
specifier|final
name|String
name|TAG_UPLOADED_PATCH_SET
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:newPatchSet"
decl_stmt|;
DECL|field|TAG_UPLOADED_WIP_PATCH_SET
specifier|public
specifier|static
specifier|final
name|String
name|TAG_UPLOADED_WIP_PATCH_SET
init|=
name|AUTOGENERATED_TAG_PREFIX
operator|+
literal|"gerrit:newWipPatchSet"
decl_stmt|;
DECL|method|newMessage (ChangeContext ctx, String body, @Nullable String tag)
specifier|public
specifier|static
name|ChangeMessage
name|newMessage
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|,
name|String
name|body
parameter_list|,
annotation|@
name|Nullable
name|String
name|tag
parameter_list|)
block|{
return|return
name|newMessage
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getUser
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|body
argument_list|,
name|tag
argument_list|)
return|;
block|}
DECL|method|newMessage ( PatchSet.Id psId, CurrentUser user, Timestamp when, String body, @Nullable String tag)
specifier|public
specifier|static
name|ChangeMessage
name|newMessage
parameter_list|(
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|String
name|body
parameter_list|,
annotation|@
name|Nullable
name|String
name|tag
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|psId
argument_list|)
expr_stmt|;
name|Account
operator|.
name|Id
name|accountId
init|=
name|user
operator|.
name|isInternalUser
argument_list|()
condition|?
literal|null
else|:
name|user
operator|.
name|getAccountId
argument_list|()
decl_stmt|;
name|ChangeMessage
name|m
init|=
operator|new
name|ChangeMessage
argument_list|(
name|ChangeMessage
operator|.
name|key
argument_list|(
name|psId
operator|.
name|changeId
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUuid
argument_list|()
argument_list|)
argument_list|,
name|accountId
argument_list|,
name|when
argument_list|,
name|psId
argument_list|)
decl_stmt|;
name|m
operator|.
name|setMessage
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|m
operator|.
name|setTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|user
operator|.
name|updateRealAccountId
argument_list|(
name|m
operator|::
name|setRealAuthor
argument_list|)
expr_stmt|;
return|return
name|m
return|;
block|}
DECL|method|uploadedPatchSetTag (boolean workInProgress)
specifier|public
specifier|static
name|String
name|uploadedPatchSetTag
parameter_list|(
name|boolean
name|workInProgress
parameter_list|)
block|{
return|return
name|workInProgress
condition|?
name|TAG_UPLOADED_WIP_PATCH_SET
else|:
name|TAG_UPLOADED_PATCH_SET
return|;
block|}
DECL|method|byChange (ChangeNotes notes)
specifier|public
name|List
argument_list|<
name|ChangeMessage
argument_list|>
name|byChange
parameter_list|(
name|ChangeNotes
name|notes
parameter_list|)
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
DECL|method|addChangeMessage (ChangeUpdate update, ChangeMessage changeMessage)
specifier|public
name|void
name|addChangeMessage
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|,
name|ChangeMessage
name|changeMessage
parameter_list|)
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
name|getNullableAccountId
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
name|getNullableAccountId
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
block|}
comment|/**    * Replace an existing change message with the provided new message.    *    *<p>The ID of a change message is different between NoteDb and ReviewDb. In NoteDb, it's the    * commit SHA-1, but in ReviewDb it was generated randomly. Taking the target message as an index    * rather than an ID allowed us to delete the message from both NoteDb and ReviewDb.    *    * @param update change update.    * @param targetMessageId the id of the target change message.    * @param newMessage the new message which is going to replace the old.    */
DECL|method|replaceChangeMessage (ChangeUpdate update, String targetMessageId, String newMessage)
specifier|public
name|void
name|replaceChangeMessage
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|,
name|String
name|targetMessageId
parameter_list|,
name|String
name|newMessage
parameter_list|)
block|{
name|update
operator|.
name|deleteChangeMessageByRewritingHistory
argument_list|(
name|targetMessageId
argument_list|,
name|newMessage
argument_list|)
expr_stmt|;
block|}
comment|/**    * @param tag value of a tag, or null.    * @return whether the tag starts with the autogenerated prefix.    */
DECL|method|isAutogenerated (@ullable String tag)
specifier|public
specifier|static
name|boolean
name|isAutogenerated
parameter_list|(
annotation|@
name|Nullable
name|String
name|tag
parameter_list|)
block|{
return|return
name|tag
operator|!=
literal|null
operator|&&
name|tag
operator|.
name|startsWith
argument_list|(
name|AUTOGENERATED_TAG_PREFIX
argument_list|)
return|;
block|}
DECL|method|createChangeMessageInfo ( ChangeMessage message, AccountLoader accountLoader)
specifier|public
specifier|static
name|ChangeMessageInfo
name|createChangeMessageInfo
parameter_list|(
name|ChangeMessage
name|message
parameter_list|,
name|AccountLoader
name|accountLoader
parameter_list|)
block|{
name|PatchSet
operator|.
name|Id
name|patchNum
init|=
name|message
operator|.
name|getPatchSetId
argument_list|()
decl_stmt|;
name|ChangeMessageInfo
name|cmi
init|=
operator|new
name|ChangeMessageInfo
argument_list|()
decl_stmt|;
name|cmi
operator|.
name|id
operator|=
name|message
operator|.
name|getKey
argument_list|()
operator|.
name|uuid
argument_list|()
expr_stmt|;
name|cmi
operator|.
name|author
operator|=
name|accountLoader
operator|.
name|get
argument_list|(
name|message
operator|.
name|getAuthor
argument_list|()
argument_list|)
expr_stmt|;
name|cmi
operator|.
name|date
operator|=
name|message
operator|.
name|getWrittenOn
argument_list|()
expr_stmt|;
name|cmi
operator|.
name|message
operator|=
name|message
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|cmi
operator|.
name|tag
operator|=
name|message
operator|.
name|getTag
argument_list|()
expr_stmt|;
name|cmi
operator|.
name|_revisionNumber
operator|=
name|patchNum
operator|!=
literal|null
condition|?
name|patchNum
operator|.
name|get
argument_list|()
else|:
literal|null
expr_stmt|;
name|Account
operator|.
name|Id
name|realAuthor
init|=
name|message
operator|.
name|getRealAuthor
argument_list|()
decl_stmt|;
if|if
condition|(
name|realAuthor
operator|!=
literal|null
condition|)
block|{
name|cmi
operator|.
name|realAuthor
operator|=
name|accountLoader
operator|.
name|get
argument_list|(
name|realAuthor
argument_list|)
expr_stmt|;
block|}
return|return
name|cmi
return|;
block|}
block|}
end_class

end_unit

