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
name|extensions
operator|.
name|events
operator|.
name|TopicEdited
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
DECL|field|topicEdited
specifier|private
specifier|final
name|TopicEdited
name|topicEdited
decl_stmt|;
DECL|class|Input
specifier|public
specifier|static
class|class
name|Input
block|{
DECL|field|topic
annotation|@
name|DefaultInput
specifier|public
name|String
name|topic
decl_stmt|;
block|}
annotation|@
name|Inject
DECL|method|PutTopic ( Provider<ReviewDb> dbProvider, ChangeMessagesUtil cmUtil, BatchUpdate.Factory batchUpdateFactory, TopicEdited topicEdited)
name|PutTopic
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|BatchUpdate
operator|.
name|Factory
name|batchUpdateFactory
parameter_list|,
name|TopicEdited
name|topicEdited
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
name|this
operator|.
name|topicEdited
operator|=
name|topicEdited
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
name|UpdateException
throws|,
name|RestApiException
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
name|Op
name|op
init|=
operator|new
name|Op
argument_list|(
name|input
operator|!=
literal|null
condition|?
name|input
else|:
operator|new
name|Input
argument_list|()
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
expr_stmt|;
name|u
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
return|return
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|op
operator|.
name|newTopicName
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
name|op
operator|.
name|newTopicName
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
name|Input
name|input
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|oldTopicName
specifier|private
name|String
name|oldTopicName
decl_stmt|;
DECL|field|newTopicName
specifier|private
name|String
name|newTopicName
decl_stmt|;
DECL|method|Op (Input input)
name|Op
parameter_list|(
name|Input
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
block|{
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
name|newTopicName
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|input
operator|.
name|topic
argument_list|)
expr_stmt|;
name|oldTopicName
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|change
operator|.
name|getTopic
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldTopicName
operator|.
name|equals
argument_list|(
name|newTopicName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|summary
decl_stmt|;
if|if
condition|(
name|oldTopicName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|summary
operator|=
literal|"Topic set to "
operator|+
name|newTopicName
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|newTopicName
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|summary
operator|=
literal|"Topic "
operator|+
name|oldTopicName
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
name|oldTopicName
argument_list|,
name|newTopicName
argument_list|)
expr_stmt|;
block|}
name|change
operator|.
name|setTopic
argument_list|(
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|newTopicName
argument_list|)
argument_list|)
expr_stmt|;
name|update
operator|.
name|setTopic
argument_list|(
name|change
operator|.
name|getTopic
argument_list|()
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
name|summary
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_SET_TOPIC
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
return|return
literal|true
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
name|change
operator|!=
literal|null
condition|)
block|{
name|topicEdited
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
name|oldTopicName
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

