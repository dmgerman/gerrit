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
DECL|package|com.google.gerrit.server.restapi.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|collect
operator|.
name|ImmutableListMultimap
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
name|ListMultimap
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
name|api
operator|.
name|changes
operator|.
name|RecipientType
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
name|account
operator|.
name|AccountState
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
name|AbandonOp
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
name|ChangeJson
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
name|ChangeResource
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
name|NotifyUtil
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
name|permissions
operator|.
name|ChangePermission
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
name|permissions
operator|.
name|PermissionBackendException
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
name|update
operator|.
name|RetryHelper
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
name|RetryingRestModifyView
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
name|UpdateException
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|ConfigInvalidException
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
DECL|class|Abandon
specifier|public
class|class
name|Abandon
extends|extends
name|RetryingRestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|AbandonInput
argument_list|,
name|ChangeInfo
argument_list|>
implements|implements
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
DECL|field|abandonOpFactory
specifier|private
specifier|final
name|AbandonOp
operator|.
name|Factory
name|abandonOpFactory
decl_stmt|;
DECL|field|notifyUtil
specifier|private
specifier|final
name|NotifyUtil
name|notifyUtil
decl_stmt|;
DECL|field|patchSetUtil
specifier|private
specifier|final
name|PatchSetUtil
name|patchSetUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|Abandon ( Provider<ReviewDb> dbProvider, ChangeJson.Factory json, RetryHelper retryHelper, AbandonOp.Factory abandonOpFactory, NotifyUtil notifyUtil, PatchSetUtil patchSetUtil)
name|Abandon
parameter_list|(
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
name|RetryHelper
name|retryHelper
parameter_list|,
name|AbandonOp
operator|.
name|Factory
name|abandonOpFactory
parameter_list|,
name|NotifyUtil
name|notifyUtil
parameter_list|,
name|PatchSetUtil
name|patchSetUtil
parameter_list|)
block|{
name|super
argument_list|(
name|retryHelper
argument_list|)
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
name|abandonOpFactory
operator|=
name|abandonOpFactory
expr_stmt|;
name|this
operator|.
name|notifyUtil
operator|=
name|notifyUtil
expr_stmt|;
name|this
operator|.
name|patchSetUtil
operator|=
name|patchSetUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, ChangeResource rsrc, AbandonInput input)
specifier|protected
name|ChangeInfo
name|applyImpl
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeResource
name|rsrc
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
throws|,
name|PermissionBackendException
throws|,
name|IOException
throws|,
name|ConfigInvalidException
block|{
comment|// Not allowed to abandon if the current patch set is locked.
name|patchSetUtil
operator|.
name|checkPatchSetNotLocked
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
expr_stmt|;
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|database
argument_list|(
name|dbProvider
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|ABANDON
argument_list|)
expr_stmt|;
name|NotifyHandling
name|notify
init|=
name|input
operator|.
name|notify
operator|==
literal|null
condition|?
name|defaultNotify
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
argument_list|)
else|:
name|input
operator|.
name|notify
decl_stmt|;
name|Change
name|change
init|=
name|abandon
argument_list|(
name|updateFactory
argument_list|,
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|,
name|input
operator|.
name|message
argument_list|,
name|notify
argument_list|,
name|notifyUtil
operator|.
name|resolveAccounts
argument_list|(
name|input
operator|.
name|notifyDetails
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|json
operator|.
name|noOptions
argument_list|()
operator|.
name|format
argument_list|(
name|change
argument_list|)
return|;
block|}
DECL|method|defaultNotify (Change change)
specifier|private
name|NotifyHandling
name|defaultNotify
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|.
name|hasReviewStarted
argument_list|()
condition|?
name|NotifyHandling
operator|.
name|ALL
else|:
name|NotifyHandling
operator|.
name|OWNER
return|;
block|}
DECL|method|abandon (BatchUpdate.Factory updateFactory, ChangeNotes notes, CurrentUser user)
specifier|public
name|Change
name|abandon
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
return|return
name|abandon
argument_list|(
name|updateFactory
argument_list|,
name|notes
argument_list|,
name|user
argument_list|,
literal|""
argument_list|,
name|defaultNotify
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
DECL|method|abandon ( BatchUpdate.Factory updateFactory, ChangeNotes notes, CurrentUser user, String msgTxt)
specifier|public
name|Change
name|abandon
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
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
name|updateFactory
argument_list|,
name|notes
argument_list|,
name|user
argument_list|,
name|msgTxt
argument_list|,
name|defaultNotify
argument_list|(
name|notes
operator|.
name|getChange
argument_list|()
argument_list|)
argument_list|,
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
argument_list|)
return|;
block|}
DECL|method|abandon ( BatchUpdate.Factory updateFactory, ChangeNotes notes, CurrentUser user, String msgTxt, NotifyHandling notifyHandling, ListMultimap<RecipientType, Account.Id> accountsToNotify)
specifier|public
name|Change
name|abandon
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeNotes
name|notes
parameter_list|,
name|CurrentUser
name|user
parameter_list|,
name|String
name|msgTxt
parameter_list|,
name|NotifyHandling
name|notifyHandling
parameter_list|,
name|ListMultimap
argument_list|<
name|RecipientType
argument_list|,
name|Account
operator|.
name|Id
argument_list|>
name|accountsToNotify
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
block|{
name|AccountState
name|accountState
init|=
name|user
operator|.
name|isIdentifiedUser
argument_list|()
condition|?
name|user
operator|.
name|asIdentifiedUser
argument_list|()
operator|.
name|state
argument_list|()
else|:
literal|null
decl_stmt|;
name|AbandonOp
name|op
init|=
name|abandonOpFactory
operator|.
name|create
argument_list|(
name|accountState
argument_list|,
name|msgTxt
argument_list|,
name|notifyHandling
argument_list|,
name|accountsToNotify
argument_list|)
decl_stmt|;
try|try
init|(
name|BatchUpdate
name|u
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|notes
operator|.
name|getProjectName
argument_list|()
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
name|u
operator|.
name|addOp
argument_list|(
name|notes
operator|.
name|getChangeId
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
name|getChange
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource rsrc)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
block|{
name|UiAction
operator|.
name|Description
name|description
init|=
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
literal|false
argument_list|)
decl_stmt|;
name|Change
name|change
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
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
return|return
name|description
return|;
block|}
try|try
block|{
if|if
condition|(
name|patchSetUtil
operator|.
name|isPatchSetLocked
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getUser
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|description
return|;
block|}
block|}
catch|catch
parameter_list|(
name|OrmException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Failed to check if the current patch set of change %s is locked"
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
name|description
return|;
block|}
return|return
name|description
operator|.
name|setVisible
argument_list|(
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|testOrFalse
argument_list|(
name|ChangePermission
operator|.
name|ABANDON
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

