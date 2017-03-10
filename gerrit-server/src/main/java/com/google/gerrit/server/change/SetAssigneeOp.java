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
name|checkNotNull
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
name|UnprocessableEntityException
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
name|account
operator|.
name|AccountsCollection
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
name|AssigneeChanged
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
name|SetAssigneeSender
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
name|validators
operator|.
name|AssigneeValidationListener
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
DECL|class|SetAssigneeOp
specifier|public
class|class
name|SetAssigneeOp
extends|extends
name|BatchUpdate
operator|.
name|Op
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
name|SetAssigneeOp
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|interface|Factory
specifier|public
interface|interface
name|Factory
block|{
DECL|method|create (String assignee)
name|SetAssigneeOp
name|create
parameter_list|(
name|String
name|assignee
parameter_list|)
function_decl|;
block|}
DECL|field|accounts
specifier|private
specifier|final
name|AccountsCollection
name|accounts
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
name|AssigneeValidationListener
argument_list|>
name|validationListeners
decl_stmt|;
DECL|field|assignee
specifier|private
specifier|final
name|String
name|assignee
decl_stmt|;
DECL|field|assigneeChanged
specifier|private
specifier|final
name|AssigneeChanged
name|assigneeChanged
decl_stmt|;
DECL|field|setAssigneeSenderFactory
specifier|private
specifier|final
name|SetAssigneeSender
operator|.
name|Factory
name|setAssigneeSenderFactory
decl_stmt|;
DECL|field|user
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
decl_stmt|;
DECL|field|userFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
decl_stmt|;
DECL|field|change
specifier|private
name|Change
name|change
decl_stmt|;
DECL|field|newAssignee
specifier|private
name|Account
name|newAssignee
decl_stmt|;
DECL|field|oldAssignee
specifier|private
name|Account
name|oldAssignee
decl_stmt|;
annotation|@
name|AssistedInject
DECL|method|SetAssigneeOp ( AccountsCollection accounts, ChangeMessagesUtil cmUtil, DynamicSet<AssigneeValidationListener> validationListeners, AssigneeChanged assigneeChanged, SetAssigneeSender.Factory setAssigneeSenderFactory, Provider<IdentifiedUser> user, IdentifiedUser.GenericFactory userFactory, @Assisted String assignee)
name|SetAssigneeOp
parameter_list|(
name|AccountsCollection
name|accounts
parameter_list|,
name|ChangeMessagesUtil
name|cmUtil
parameter_list|,
name|DynamicSet
argument_list|<
name|AssigneeValidationListener
argument_list|>
name|validationListeners
parameter_list|,
name|AssigneeChanged
name|assigneeChanged
parameter_list|,
name|SetAssigneeSender
operator|.
name|Factory
name|setAssigneeSenderFactory
parameter_list|,
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|user
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|userFactory
parameter_list|,
annotation|@
name|Assisted
name|String
name|assignee
parameter_list|)
block|{
name|this
operator|.
name|accounts
operator|=
name|accounts
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
name|assigneeChanged
operator|=
name|assigneeChanged
expr_stmt|;
name|this
operator|.
name|setAssigneeSenderFactory
operator|=
name|setAssigneeSenderFactory
expr_stmt|;
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
name|this
operator|.
name|userFactory
operator|=
name|userFactory
expr_stmt|;
name|this
operator|.
name|assignee
operator|=
name|checkNotNull
argument_list|(
name|assignee
argument_list|)
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
name|RestApiException
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
name|IdentifiedUser
name|newAssigneeUser
init|=
name|accounts
operator|.
name|parse
argument_list|(
name|assignee
argument_list|)
decl_stmt|;
name|newAssignee
operator|=
name|newAssigneeUser
operator|.
name|getAccount
argument_list|()
expr_stmt|;
name|IdentifiedUser
name|oldAssigneeUser
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|change
operator|.
name|getAssignee
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|oldAssigneeUser
operator|=
name|userFactory
operator|.
name|create
argument_list|(
name|change
operator|.
name|getAssignee
argument_list|()
argument_list|)
expr_stmt|;
name|oldAssignee
operator|=
name|oldAssigneeUser
operator|.
name|getAccount
argument_list|()
expr_stmt|;
if|if
condition|(
name|newAssignee
operator|.
name|equals
argument_list|(
name|oldAssignee
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
operator|!
name|newAssignee
operator|.
name|isActive
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|UnprocessableEntityException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Account of %s is not active"
argument_list|,
name|assignee
argument_list|)
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
name|forUser
argument_list|(
name|newAssigneeUser
argument_list|)
operator|.
name|isRefVisible
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Change %s is not visible to %s."
argument_list|,
name|change
operator|.
name|getChangeId
argument_list|()
argument_list|,
name|assignee
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
for|for
control|(
name|AssigneeValidationListener
name|validator
range|:
name|validationListeners
control|)
block|{
name|validator
operator|.
name|validateAssignee
argument_list|(
name|change
argument_list|,
name|newAssignee
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
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|// notedb
name|update
operator|.
name|setAssignee
argument_list|(
name|newAssignee
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// reviewdb
name|change
operator|.
name|setAssignee
argument_list|(
name|newAssignee
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|addMessage
argument_list|(
name|ctx
argument_list|,
name|update
argument_list|,
name|oldAssigneeUser
argument_list|,
name|newAssigneeUser
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|addMessage ( ChangeContext ctx, ChangeUpdate update, IdentifiedUser previousAssignee, IdentifiedUser newAssignee)
specifier|private
name|void
name|addMessage
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|,
name|ChangeUpdate
name|update
parameter_list|,
name|IdentifiedUser
name|previousAssignee
parameter_list|,
name|IdentifiedUser
name|newAssignee
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
literal|"Assignee "
argument_list|)
expr_stmt|;
if|if
condition|(
name|previousAssignee
operator|==
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"added: "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|newAssignee
operator|.
name|getNameEmail
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"changed from: "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|previousAssignee
operator|.
name|getNameEmail
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|" to: "
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|newAssignee
operator|.
name|getNameEmail
argument_list|()
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
name|msg
operator|.
name|toString
argument_list|()
argument_list|,
name|ChangeMessagesUtil
operator|.
name|TAG_SET_ASSIGNEE
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
name|SetAssigneeSender
name|cm
init|=
name|setAssigneeSenderFactory
operator|.
name|create
argument_list|(
name|change
operator|.
name|getProject
argument_list|()
argument_list|,
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|newAssignee
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|cm
operator|.
name|setFrom
argument_list|(
name|user
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
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
name|err
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot send email to new assignee of change "
operator|+
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|err
argument_list|)
expr_stmt|;
block|}
name|assigneeChanged
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
name|oldAssignee
argument_list|,
name|ctx
operator|.
name|getWhen
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getNewAssignee ()
specifier|public
name|Account
operator|.
name|Id
name|getNewAssignee
parameter_list|()
block|{
return|return
name|newAssignee
operator|!=
literal|null
condition|?
name|newAssignee
operator|.
name|getId
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

