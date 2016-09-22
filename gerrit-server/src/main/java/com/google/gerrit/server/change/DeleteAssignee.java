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
name|Optional
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
name|common
operator|.
name|AccountInfo
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
name|account
operator|.
name|AccountInfoCacheFactory
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
name|AccountJson
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
name|change
operator|.
name|DeleteAssignee
operator|.
name|Input
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
name|config
operator|.
name|AnonymousCowardName
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

begin_class
annotation|@
name|Singleton
DECL|class|DeleteAssignee
specifier|public
class|class
name|DeleteAssignee
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|Input
argument_list|>
block|{
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{    }
DECL|field|batchUpdateFactory
specifier|private
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
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
DECL|field|db
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
decl_stmt|;
DECL|field|accountInfos
specifier|private
specifier|final
name|AccountInfoCacheFactory
operator|.
name|Factory
name|accountInfos
decl_stmt|;
DECL|field|anonymousCowardName
specifier|private
specifier|final
name|String
name|anonymousCowardName
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteAssignee (NotesMigration notesMigration, BatchUpdate.Factory batchUpdateFactory, ChangeMessagesUtil cmUtil, Provider<ReviewDb> db, AccountInfoCacheFactory.Factory accountInfosFactory, @AnonymousCowardName String anonymousCowardName)
name|DeleteAssignee
parameter_list|(
name|NotesMigration
name|notesMigration
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|db
parameter_list|,
name|AccountInfoCacheFactory
operator|.
name|Factory
name|accountInfosFactory
parameter_list|,
annotation|@
name|AnonymousCowardName
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
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
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|accountInfos
operator|=
name|accountInfosFactory
expr_stmt|;
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, Input input)
specifier|public
name|Response
argument_list|<
name|AccountInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
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
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
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
name|Op
name|op
init|=
operator|new
name|Op
argument_list|()
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
if|if
condition|(
name|op
operator|.
name|getDeletedAssignee
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
return|return
name|Response
operator|.
name|ok
argument_list|(
name|AccountJson
operator|.
name|toAccountInfo
argument_list|(
name|op
operator|.
name|getDeletedAssignee
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
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
DECL|field|deletedAssignee
specifier|private
name|Account
name|deletedAssignee
decl_stmt|;
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
name|RestApiException
throws|,
name|OrmException
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
literal|"Cannot add Assignee; NoteDb is disabled"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|ctx
operator|.
name|getControl
argument_list|()
operator|.
name|canEditAssignee
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Delete Assignee not permitted"
argument_list|)
throw|;
block|}
name|ChangeUpdate
name|update
init|=
name|ctx
operator|.
name|getUpdate
argument_list|(
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Account
operator|.
name|Id
argument_list|>
name|currentAssigneeId
init|=
name|update
operator|.
name|getNotes
argument_list|()
operator|.
name|getAssignee
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|currentAssigneeId
operator|.
name|isPresent
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Account
name|account
init|=
name|accountInfos
operator|.
name|create
argument_list|()
operator|.
name|get
argument_list|(
name|currentAssigneeId
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
name|update
operator|.
name|setAssignee
argument_list|(
name|Optional
operator|.
name|absent
argument_list|()
argument_list|)
expr_stmt|;
name|addMessage
argument_list|(
name|ctx
argument_list|,
name|update
argument_list|,
name|account
argument_list|)
expr_stmt|;
name|deletedAssignee
operator|=
name|account
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getDeletedAssignee ()
specifier|public
name|Account
name|getDeletedAssignee
parameter_list|()
block|{
return|return
name|deletedAssignee
return|;
block|}
DECL|method|addMessage (BatchUpdate.ChangeContext ctx, ChangeUpdate update, Account deleted)
specifier|private
name|void
name|addMessage
parameter_list|(
name|BatchUpdate
operator|.
name|ChangeContext
name|ctx
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|Account
name|deleted
parameter_list|)
throws|throws
name|OrmException
block|{
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
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|ctx
operator|.
name|getDb
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|ctx
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|,
name|ctx
operator|.
name|getChange
argument_list|()
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|cmsg
operator|.
name|setMessage
argument_list|(
literal|"Assignee deleted: "
operator|+
name|deleted
operator|.
name|getName
argument_list|(
name|anonymousCowardName
argument_list|)
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
name|cmsg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

