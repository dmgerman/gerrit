begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|checkState
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
operator|.
name|HashtagsUtil
operator|.
name|extractTags
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
name|Joiner
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
name|ImmutableSortedSet
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
name|Ordering
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
name|api
operator|.
name|changes
operator|.
name|HashtagsInput
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
name|registration
operator|.
name|DynamicSet
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
name|extensions
operator|.
name|events
operator|.
name|HashtagsEdited
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
name|gerrit
operator|.
name|server
operator|.
name|validators
operator|.
name|HashtagValidationListener
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
name|validators
operator|.
name|ValidationException
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
name|assistedinject
operator|.
name|Assisted
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
name|AssistedInject
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_class
DECL|class|SetHashtagsOp
specifier|public
class|class
name|SetHashtagsOp
extends|extends
name|BatchUpdate
operator|.
name|Op
block|{
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (HashtagsInput input)
name|SetHashtagsOp
name|create
parameter_list|(
name|HashtagsInput
name|input
parameter_list|)
function_decl|;
block|}
DECL|field|notesMigration
specifier|private
specifier|final
name|NotesMigration
name|notesMigration
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|validationListeners
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|HashtagValidationListener
argument_list|>
name|validationListeners
decl_stmt|;
DECL|field|hashtagsEdited
specifier|private
specifier|final
name|HashtagsEdited
name|hashtagsEdited
decl_stmt|;
DECL|field|input
specifier|private
specifier|final
name|HashtagsInput
name|input
decl_stmt|;
DECL|field|fireEvent
specifier|private
name|boolean
name|fireEvent
init|=
literal|true
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|toAdd
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|toAdd
decl_stmt|;
DECL|field|toRemove
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|toRemove
decl_stmt|;
DECL|field|updatedHashtags
specifier|private
name|ImmutableSortedSet
argument_list|<
name|String
argument_list|>
name|updatedHashtags
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|SetHashtagsOp ( NotesMigration notesMigration, ChangeMessagesUtil cmUtil, DynamicSet<HashtagValidationListener> validationListeners, HashtagsEdited hashtagsEdited, @Assisted @Nullable HashtagsInput input)
name|SetHashtagsOp
parameter_list|(
name|NotesMigration
name|notesMigration
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|DynamicSet
argument_list|<
name|HashtagValidationListener
argument_list|>
name|validationListeners
parameter_list|,
name|HashtagsEdited
name|hashtagsEdited
parameter_list|,
annotation|@
name|Assisted
annotation|@
name|Nullable
name|HashtagsInput
name|input
parameter_list|)
block|{
name|this
operator|.
name|notesMigration
operator|=
name|notesMigration
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|validationListeners
operator|=
name|validationListeners
expr_stmt|;
name|this
operator|.
name|hashtagsEdited
operator|=
name|hashtagsEdited
expr_stmt|;
name|this
operator|.
name|input
operator|=
name|input
expr_stmt|;
block|}
DECL|method|setFireEvent (boolean fireEvent)
specifier|public
name|SetHashtagsOp
name|setFireEvent
parameter_list|(
name|boolean
name|fireEvent
parameter_list|)
block|{
name|this
operator|.
name|fireEvent
operator|=
name|fireEvent
expr_stmt|;
return|return
name|this
return|;
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
name|AuthException
throws|,
name|BadRequestException
throws|,
name|OrmException
throws|,
name|IOException
block|{
if|if
condition|(
operator|!
name|notesMigration
operator|.
name|readChanges
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"Cannot add hashtags; NoteDb is disabled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|input
operator|==
literal|null
operator|||
operator|(
name|input
operator|.
name|add
operator|==
literal|null
operator|&&
name|input
operator|.
name|remove
operator|==
literal|null
operator|)
condition|)
block|{
name|updatedHashtags
operator|=
name|ImmutableSortedSet
operator|.
name|of
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|ctx
operator|.
name|getControl
argument_list|()
operator|.
name|canEditHashtags
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Editing hashtags not permitted"
argument_list|)
throw|;
block|}
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
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
name|ChangeNotes
name|notes
init|=
name|update
operator|.
name|getNotes
argument_list|()
operator|.
name|load
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|existingHashtags
init|=
name|notes
operator|.
name|getHashtags
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|updated
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|toAdd
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|extractTags
argument_list|(
name|input
operator|.
name|add
argument_list|)
argument_list|)
expr_stmt|;
name|toRemove
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|extractTags
argument_list|(
name|input
operator|.
name|remove
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
for|for
control|(
name|HashtagValidationListener
name|validator
range|:
name|validationListeners
control|)
block|{
name|validator
operator|.
name|validateHashtags
argument_list|(
name|update
operator|.
name|getChange
argument_list|()
argument_list|,
name|toAdd
argument_list|,
name|toRemove
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ValidationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
name|updated
operator|.
name|addAll
argument_list|(
name|existingHashtags
argument_list|)
expr_stmt|;
name|toAdd
operator|.
name|removeAll
argument_list|(
name|existingHashtags
argument_list|)
expr_stmt|;
name|toRemove
operator|.
name|retainAll
argument_list|(
name|existingHashtags
argument_list|)
expr_stmt|;
if|if
condition|(
name|updated
argument_list|()
condition|)
block|{
name|updated
operator|.
name|addAll
argument_list|(
name|toAdd
argument_list|)
expr_stmt|;
name|updated
operator|.
name|removeAll
argument_list|(
name|toRemove
argument_list|)
expr_stmt|;
name|update
operator|.
name|setHashtags
argument_list|(
name|updated
argument_list|)
expr_stmt|;
name|addMessage
argument_list|(
name|ctx
argument_list|,
name|update
argument_list|)
expr_stmt|;
block|}
name|updatedHashtags
operator|=
name|ImmutableSortedSet
operator|.
name|copyOf
argument_list|(
name|updated
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
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|appendHashtagMessage
argument_list|(
name|msg
argument_list|,
literal|"added"
argument_list|,
name|toAdd
argument_list|)
expr_stmt|;
name|appendHashtagMessage
argument_list|(
name|msg
argument_list|,
literal|"removed"
argument_list|,
name|toRemove
argument_list|)
expr_stmt|;
name|ChangeMessage
name|cmsg
init|=
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
DECL|method|appendHashtagMessage (StringBuilder b, String action, Set<String> hashtags)
specifier|private
name|void
name|appendHashtagMessage
parameter_list|(
name|StringBuilder
name|b
parameter_list|,
name|String
name|action
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|hashtags
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|hashtags
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|b
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|"Hashtag"
argument_list|)
expr_stmt|;
if|if
condition|(
name|hashtags
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|b
operator|.
name|append
argument_list|(
literal|"s"
argument_list|)
expr_stmt|;
block|}
name|b
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|action
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|": "
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|Joiner
operator|.
name|on
argument_list|(
literal|", "
argument_list|)
operator|.
name|join
argument_list|(
name|Ordering
operator|.
name|natural
argument_list|()
operator|.
name|sortedCopy
argument_list|(
name|hashtags
argument_list|)
argument_list|)
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
throws|throws
name|OrmException
block|{
if|if
condition|(
name|updated
argument_list|()
operator|&&
name|fireEvent
condition|)
block|{
name|hashtagsEdited
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|ctx
operator|.
name|getAccount
argument_list|()
argument_list|,
name|updatedHashtags
argument_list|,
name|toAdd
argument_list|,
name|toRemove
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getUpdatedHashtags ()
specifier|public
name|ImmutableSortedSet
argument_list|<
name|String
argument_list|>
name|getUpdatedHashtags
parameter_list|()
block|{
name|checkState
argument_list|(
name|updatedHashtags
operator|!=
literal|null
argument_list|,
literal|"getUpdatedHashtags() only valid after executing op"
argument_list|)
expr_stmt|;
return|return
name|updatedHashtags
return|;
block|}
DECL|method|updated ()
specifier|private
name|boolean
name|updated
parameter_list|()
block|{
return|return
operator|!
name|isNullOrEmpty
argument_list|(
name|toAdd
argument_list|)
operator|||
operator|!
name|isNullOrEmpty
argument_list|(
name|toRemove
argument_list|)
return|;
block|}
DECL|method|isNullOrEmpty (Collection<?> coll)
specifier|private
specifier|static
name|boolean
name|isNullOrEmpty
parameter_list|(
name|Collection
argument_list|<
name|?
argument_list|>
name|coll
parameter_list|)
block|{
return|return
name|coll
operator|==
literal|null
operator|||
name|coll
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

