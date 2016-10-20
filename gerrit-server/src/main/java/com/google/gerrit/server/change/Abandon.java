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
name|AbandonInput
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
name|Project
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
name|CurrentUser
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
name|ChangeAbandoned
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|Abandon
specifier|public
class|class
name|Abandon
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|AbandonInput
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
name|Abandon
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|abandonedSenderFactory
specifier|private
specifier|final
name|AbandonedSender
operator|.
name|Factory
name|abandonedSenderFactory
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
DECL|field|changeAbandoned
specifier|private
specifier|final
name|ChangeAbandoned
name|changeAbandoned
decl_stmt|;
annotation|@
name|Inject
DECL|method|Abandon (AbandonedSender.Factory abandonedSenderFactory, Provider<ReviewDb> dbProvider, ChangeJson.Factory json, ChangeMessagesUtil cmUtil, PatchSetUtil psUtil, BatchUpdate.Factory batchUpdateFactory, ChangeAbandoned changeAbandoned)
name|Abandon
parameter_list|(
name|AbandonedSender
operator|.
name|Factory
name|abandonedSenderFactory
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
name|ChangeAbandoned
name|changeAbandoned
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
name|changeAbandoned
operator|=
name|changeAbandoned
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource req, AbandonInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|ChangeResource
name|req
parameter_list|,
name|AbandonInput
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
name|control
init|=
name|req
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canAbandon
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
literal|"abandon not permitted"
argument_list|)
throw|;
block|}
name|Change
name|change
init|=
name|abandon
argument_list|(
name|control
argument_list|,
name|input
operator|.
name|message
argument_list|,
name|input
operator|.
name|notify
argument_list|)
decl_stmt|;
return|return
name|json
operator|.
name|create
argument_list|(
name|ChangeJson
operator|.
name|NO_OPTIONS
argument_list|)
operator|.
name|format
argument_list|(
name|change
argument_list|)
return|;
block|}
DECL|method|abandon (ChangeControl control)
specifier|public
name|Change
name|abandon
parameter_list|(
name|ChangeControl
name|control
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
return|return
name|abandon
argument_list|(
name|control
argument_list|,
literal|""
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
return|;
block|}
DECL|method|abandon (ChangeControl control, String msgTxt)
specifier|public
name|Change
name|abandon
parameter_list|(
name|ChangeControl
name|control
parameter_list|,
name|String
name|msgTxt
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
return|return
name|abandon
argument_list|(
name|control
argument_list|,
name|msgTxt
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
return|;
block|}
DECL|method|abandon (ChangeControl control, String msgTxt, NotifyHandling notifyHandling)
specifier|public
name|Change
name|abandon
parameter_list|(
name|ChangeControl
name|control
parameter_list|,
name|String
name|msgTxt
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
name|Op
name|op
init|=
operator|new
name|Op
argument_list|(
name|msgTxt
argument_list|,
name|notifyHandling
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
name|control
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|,
name|control
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
name|control
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
name|op
operator|.
name|change
return|;
block|}
comment|/**    * If an extension has more than one changes to abandon that belong to the    * same project, they should use the batch instead of abandoning one by one.    *<p>    * It's the caller's responsibility to ensure that all jobs inside the same    * batch have the matching project from its ChangeControl. Violations will    * result in a ResourceConflictException.    */
DECL|method|batchAbandon (Project.NameKey project, CurrentUser user, Collection<ChangeControl> controls, String msgTxt, NotifyHandling notifyHandling)
specifier|public
name|void
name|batchAbandon
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Collection
argument_list|<
name|ChangeControl
argument_list|>
name|controls
parameter_list|,
name|String
name|msgTxt
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
if|if
condition|(
name|controls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
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
name|project
argument_list|,
name|user
argument_list|,
name|TimeUtil
operator|.
name|nowTs
argument_list|()
argument_list|)
init|)
block|{
for|for
control|(
name|ChangeControl
name|control
range|:
name|controls
control|)
block|{
if|if
condition|(
operator|!
name|project
operator|.
name|equals
argument_list|(
name|control
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Project name \"%s\" doesn't match \"%s\""
argument_list|,
name|control
operator|.
name|getProject
argument_list|()
operator|.
name|getNameKey
argument_list|()
operator|.
name|get
argument_list|()
argument_list|,
name|project
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|u
operator|.
name|addOp
argument_list|(
name|control
operator|.
name|getId
argument_list|()
argument_list|,
operator|new
name|Op
argument_list|(
name|msgTxt
argument_list|,
name|notifyHandling
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|u
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|batchAbandon (Project.NameKey project, CurrentUser user, Collection<ChangeControl> controls, String msgTxt)
specifier|public
name|void
name|batchAbandon
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Collection
argument_list|<
name|ChangeControl
argument_list|>
name|controls
parameter_list|,
name|String
name|msgTxt
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
name|batchAbandon
argument_list|(
name|project
argument_list|,
name|user
argument_list|,
name|controls
argument_list|,
name|msgTxt
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
expr_stmt|;
block|}
DECL|method|batchAbandon (Project.NameKey project, CurrentUser user, Collection<ChangeControl> controls)
specifier|public
name|void
name|batchAbandon
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|Collection
argument_list|<
name|ChangeControl
argument_list|>
name|controls
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
name|batchAbandon
argument_list|(
name|project
argument_list|,
name|user
argument_list|,
name|controls
argument_list|,
literal|""
argument_list|,
name|NotifyHandling
operator|.
name|ALL
argument_list|)
expr_stmt|;
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
DECL|field|msgTxt
specifier|private
specifier|final
name|String
name|msgTxt
decl_stmt|;
DECL|field|notifyHandling
specifier|private
specifier|final
name|NotifyHandling
name|notifyHandling
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
DECL|method|Op (String msgTxt, NotifyHandling notifyHandling)
specifier|private
name|Op
parameter_list|(
name|String
name|msgTxt
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|)
block|{
name|this
operator|.
name|msgTxt
operator|=
name|msgTxt
expr_stmt|;
name|this
operator|.
name|notifyHandling
operator|=
name|notifyHandling
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
if|if
condition|(
operator|!
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|isOpen
argument_list|()
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
elseif|else
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|==
name|Change
operator|.
name|Status
operator|.
name|DRAFT
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"draft changes cannot be abandoned"
argument_list|)
throw|;
block|}
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
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
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
name|msg
operator|.
name|append
argument_list|(
literal|"Abandoned"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|msgTxt
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
name|msgTxt
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
name|TAG_ABANDON
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
name|Account
name|account
init|=
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|isIdentifiedUser
argument_list|()
condition|?
name|ctx
operator|.
name|getUser
argument_list|()
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|getAccount
argument_list|()
else|:
literal|null
decl_stmt|;
try|try
block|{
name|ReplyToChangeSender
name|cm
init|=
name|abandonedSenderFactory
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
if|if
condition|(
name|account
operator|!=
literal|null
condition|)
block|{
name|cm
operator|.
name|setFrom
argument_list|(
name|account
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|setNotify
argument_list|(
name|notifyHandling
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
name|changeAbandoned
operator|.
name|fire
argument_list|(
name|change
argument_list|,
name|patchSet
argument_list|,
name|account
argument_list|,
name|msgTxt
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|notifyHandling
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
name|canAbandon
init|=
literal|false
decl_stmt|;
try|try
block|{
name|canAbandon
operator|=
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|canAbandon
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
literal|"Cannot check canAbandon status. Assuming false."
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
literal|"Abandon"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Abandon the change"
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
operator|.
name|isOpen
argument_list|()
operator|&&
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|!=
name|Change
operator|.
name|Status
operator|.
name|DRAFT
operator|&&
name|canAbandon
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

