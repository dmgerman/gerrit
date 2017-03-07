begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|RestoreInput
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
name|ChangeInfo
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
name|ResourceConflictException
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
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
name|Change
operator|.
name|Status
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
name|ChangeRestored
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
name|send
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
name|mail
operator|.
name|send
operator|.
name|RestoredSender
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

begin_class
annotation|@
name|Singleton
DECL|class|Restore
specifier|public
class|class
name|Restore
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|RestoreInput
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
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
name|Restore
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|restoredSenderFactory
specifier|private
specifier|final
name|RestoredSender
operator|.
name|Factory
name|restoredSenderFactory
decl_stmt|;
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
operator|.
name|Factory
name|json
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|psUtil
specifier|private
specifier|final
name|PatchSetUtil
name|psUtil
decl_stmt|;
DECL|field|batchUpdateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|field|changeRestored
specifier|private
specifier|final
name|ChangeRestored
name|changeRestored
decl_stmt|;
annotation|@
name|Inject
DECL|method|Restore ( RestoredSender.Factory restoredSenderFactory, Provider<ReviewDb> dbProvider, ChangeJson.Factory json, ChangeMessagesUtil cmUtil, PatchSetUtil psUtil, BatchUpdate.Factory batchUpdateFactory, ChangeRestored changeRestored)
name|Restore
parameter_list|(
name|RestoredSender
operator|.
name|Factory
name|restoredSenderFactory
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|ChangeJson
operator|.
name|Factory
name|json
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|PatchSetUtil
name|psUtil
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|ChangeRestored
name|changeRestored
parameter_list|)
block|{
name|this
operator|.
name|restoredSenderFactory
operator|=
name|restoredSenderFactory
expr_stmt|;
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|psUtil
operator|=
name|psUtil
expr_stmt|;
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
name|this
operator|.
name|changeRestored
operator|=
name|changeRestored
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource req, RestoreInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|ChangeResource
name|req
parameter_list|,
name|RestoreInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
throws|,
name|OrmException
block|{
name|ChangeControl
name|ctl
init|=
name|req
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|canRestore
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"restore not permitted"
argument_list|)
throw|;
block|}
name|Op
name|op
init|=
operator|new
name|Op
argument_list|(
name|input
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|u
init|=
name|batchUpdateFactory
operator|.
name|create
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|req
operator|.
name|getChange
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|ctl
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
name|u
operator|.
name|addOp
argument_list|(
name|req
operator|.
name|getId
argument_list|()
argument_list|,
name|op
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|json
operator|.
name|noOptions
argument_list|()
operator|.
name|format
argument_list|(
name|op
operator|.
name|change
argument_list|)
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
DECL|field|input
specifier|private
specifier|final
name|RestoreInput
name|input
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|patchSet
specifier|private
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|message
specifier|private
name|ChangeMessage
name|message
decl_stmt|;
DECL|method|Op (RestoreInput input)
specifier|private
name|Op
parameter_list|(
name|RestoreInput
name|input
parameter_list|)
block|{
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
name|ResourceConflictException
block|{
name|change
operator|=
name|ctx
operator|.
name|getChange
argument_list|()
expr_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
operator|||
name|change
operator|.
name|getStatus
argument_list|()
operator|!=
name|Status
operator|.
name|ABANDONED
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
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
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|psId
argument_list|)
decl_stmt|;
name|patchSet
operator|=
name|psUtil
operator|.
name|get
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|,
name|ctx
operator|.
name|getNotes
argument_list|()
argument_list|,
name|psId
argument_list|)
expr_stmt|;
name|change
operator|.
name|setStatus
argument_list|(
name|Status
operator|.
name|NEW
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
name|setStatus
argument_list|(
name|change
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
name|newMessage
argument_list|(
name|ctx
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
name|update
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|newMessage (ChangeContext ctx)
specifier|private
name|ChangeMessage
name|newMessage
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
block|{
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
literal|"Restored"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|input
operator|.
name|message
argument_list|)
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"\n\n"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|input
operator|.
name|message
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
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
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_RESTORE
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
throws|throws
name|OrmException
block|{
try|try
block|{
name|ReplyToChangeSender
name|cm
init|=
name|restoredSenderFactory
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
name|ctx
operator|.
name|getAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|cm
operator|.
name|setChangeMessage
argument_list|(
name|message
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
name|changeRestored
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|ctx
operator|.
name|getAccount
argument_list|()
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|message
argument_list|)
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource resource)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|resource
parameter_list|)
block|{
name|boolean
name|canRestore
init|=
literal|false
decl_stmt|;
try|try
block|{
name|canRestore
operator|=
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|canRestore
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|OrmException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot check canRestore status. Assuming false."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Restore"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Restore the change"
argument_list|)
operator|.
name|setVisible
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|ABANDONED
operator|&&
name|canRestore
argument_list|)
return|;
block|}
DECL|method|status (Change change)
specifier|private
specifier|static
name|String
name|status
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|!=
literal|null
condition|?
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|"deleted"
return|;
block|}
block|}
end_class

end_unit

