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
name|DefaultInput
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
name|change
operator|.
name|PutTopic
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
name|ChangeOp
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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PutTopic
specifier|public
class|class
name|PutTopic
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|Input
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|dbProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
decl_stmt|;
DECL|field|hooks
specifier|private
specifier|final
name|ChangeHooks
name|hooks
decl_stmt|;
DECL|field|cmUtil
specifier|private
specifier|final
name|ChangeMessagesUtil
name|cmUtil
decl_stmt|;
DECL|field|batchUpdateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
decl_stmt|;
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
annotation|@
name|DefaultInput
DECL|field|topic
specifier|public
name|String
name|topic
decl_stmt|;
block|}
annotation|@
name|Inject
DECL|method|PutTopic (Provider<ReviewDb> dbProvider, ChangeHooks hooks, ChangeMessagesUtil cmUtil, BatchUpdate.Factory batchUpdateFactory)
name|PutTopic
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|ChangeHooks
name|hooks
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|)
block|{
name|this
operator|.
name|dbProvider
operator|=
name|dbProvider
expr_stmt|;
name|this
operator|.
name|hooks
operator|=
name|hooks
expr_stmt|;
name|this
operator|.
name|cmUtil
operator|=
name|cmUtil
expr_stmt|;
name|this
operator|.
name|batchUpdateFactory
operator|=
name|batchUpdateFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource req, Input input)
specifier|public
name|Response
argument_list|<
name|String
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|req
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|UpdateException
throws|,
name|RestApiException
throws|,
name|OrmException
throws|,
name|IOException
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
name|Input
argument_list|()
expr_stmt|;
block|}
specifier|final
name|String
name|inputTopic
init|=
name|input
operator|.
name|topic
decl_stmt|;
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
name|canEditTopicName
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"changing topic not permitted"
argument_list|)
throw|;
block|}
specifier|final
name|Change
operator|.
name|Id
name|id
init|=
name|req
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
specifier|final
name|IdentifiedUser
name|caller
init|=
operator|(
name|IdentifiedUser
operator|)
name|control
operator|.
name|getCurrentUser
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|Change
argument_list|>
name|change
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|String
argument_list|>
name|oldTopicName
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|AtomicReference
argument_list|<
name|String
argument_list|>
name|newTopicName
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Timestamp
name|now
init|=
name|TimeUtil
operator|.
name|nowTs
argument_list|()
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
name|now
argument_list|)
init|)
block|{
name|u
operator|.
name|addChangeOp
argument_list|(
operator|new
name|ChangeOp
argument_list|(
name|req
operator|.
name|getControl
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|call
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|OrmException
throws|,
name|ResourceConflictException
block|{
name|Change
name|c
init|=
name|db
operator|.
name|changes
argument_list|()
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|String
name|n
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|inputTopic
argument_list|)
decl_stmt|;
name|String
name|o
init|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|c
operator|.
name|getTopic
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|.
name|equals
argument_list|(
name|n
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|summary
decl_stmt|;
if|if
condition|(
name|o
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|summary
operator|=
literal|"Topic set to "
operator|+
name|n
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|n
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|summary
operator|=
literal|"Topic "
operator|+
name|o
operator|+
literal|" removed"
expr_stmt|;
block|}
else|else
block|{
name|summary
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"Topic changed from %s to %s"
argument_list|,
name|o
argument_list|,
name|n
argument_list|)
expr_stmt|;
block|}
name|c
operator|.
name|setTopic
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|n
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeUtil
operator|.
name|updated
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|db
operator|.
name|changes
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|c
argument_list|)
argument_list|)
expr_stmt|;
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
name|id
argument_list|,
name|ChangeUtil
operator|.
name|messageUUID
argument_list|(
name|db
argument_list|)
argument_list|)
argument_list|,
name|caller
operator|.
name|getAccountId
argument_list|()
argument_list|,
name|now
argument_list|,
name|c
operator|.
name|currentPatchSetId
argument_list|()
argument_list|)
decl_stmt|;
name|cmsg
operator|.
name|setMessage
argument_list|(
name|summary
argument_list|)
expr_stmt|;
name|cmUtil
operator|.
name|addChangeMessage
argument_list|(
name|db
argument_list|,
name|update
argument_list|,
name|cmsg
argument_list|)
expr_stmt|;
name|change
operator|.
name|set
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|oldTopicName
operator|.
name|set
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|newTopicName
operator|.
name|set
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|u
operator|.
name|addPostOp
argument_list|(
operator|new
name|Callable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|OrmException
block|{
name|Change
name|c
init|=
name|change
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|!=
literal|null
condition|)
block|{
name|hooks
operator|.
name|doTopicChangedHook
argument_list|(
name|change
operator|.
name|get
argument_list|()
argument_list|,
name|caller
operator|.
name|getAccount
argument_list|()
argument_list|,
name|oldTopicName
operator|.
name|get
argument_list|()
argument_list|,
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|u
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
name|String
name|n
init|=
name|newTopicName
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|n
argument_list|)
condition|?
name|Response
operator|.
expr|<
name|String
operator|>
name|none
argument_list|()
else|:
name|Response
operator|.
name|ok
argument_list|(
name|n
argument_list|)
return|;
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
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Edit Topic"
argument_list|)
operator|.
name|setVisible
argument_list|(
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|canEditTopicName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

