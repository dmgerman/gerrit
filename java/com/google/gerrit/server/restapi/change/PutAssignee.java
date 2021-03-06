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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|AddReviewerInput
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
name|AssigneeInput
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
name|client
operator|.
name|ReviewerState
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
name|server
operator|.
name|ApprovalsUtil
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
name|ReviewerSet
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
name|account
operator|.
name|AccountResolver
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
name|ReviewerAdder
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
name|ReviewerAdder
operator|.
name|ReviewerAddition
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
name|SetAssigneeOp
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
name|PermissionBackend
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
name|util
operator|.
name|time
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

begin_class
annotation|@
name|Singleton
DECL|class|PutAssignee
specifier|public
class|class
name|PutAssignee
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|AssigneeInput
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|updateFactory
specifier|private
specifier|final
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
decl_stmt|;
DECL|field|accountResolver
specifier|private
specifier|final
name|AccountResolver
name|accountResolver
decl_stmt|;
DECL|field|assigneeFactory
specifier|private
specifier|final
name|SetAssigneeOp
operator|.
name|Factory
name|assigneeFactory
decl_stmt|;
DECL|field|reviewerAdder
specifier|private
specifier|final
name|ReviewerAdder
name|reviewerAdder
decl_stmt|;
DECL|field|accountLoaderFactory
specifier|private
specifier|final
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
decl_stmt|;
DECL|field|permissionBackend
specifier|private
specifier|final
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|approvalsUtil
specifier|private
specifier|final
name|ApprovalsUtil
name|approvalsUtil
decl_stmt|;
annotation|@
name|Inject
DECL|method|PutAssignee ( BatchUpdate.Factory updateFactory, AccountResolver accountResolver, SetAssigneeOp.Factory assigneeFactory, ReviewerAdder reviewerAdder, AccountLoader.Factory accountLoaderFactory, PermissionBackend permissionBackend, ApprovalsUtil approvalsUtil)
name|PutAssignee
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|AccountResolver
name|accountResolver
parameter_list|,
name|SetAssigneeOp
operator|.
name|Factory
name|assigneeFactory
parameter_list|,
name|ReviewerAdder
name|reviewerAdder
parameter_list|,
name|AccountLoader
operator|.
name|Factory
name|accountLoaderFactory
parameter_list|,
name|PermissionBackend
name|permissionBackend
parameter_list|,
name|ApprovalsUtil
name|approvalsUtil
parameter_list|)
block|{
name|this
operator|.
name|updateFactory
operator|=
name|updateFactory
expr_stmt|;
name|this
operator|.
name|accountResolver
operator|=
name|accountResolver
expr_stmt|;
name|this
operator|.
name|assigneeFactory
operator|=
name|assigneeFactory
expr_stmt|;
name|this
operator|.
name|reviewerAdder
operator|=
name|reviewerAdder
expr_stmt|;
name|this
operator|.
name|accountLoaderFactory
operator|=
name|accountLoaderFactory
expr_stmt|;
name|this
operator|.
name|permissionBackend
operator|=
name|permissionBackend
expr_stmt|;
name|this
operator|.
name|approvalsUtil
operator|=
name|approvalsUtil
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, AssigneeInput input)
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
name|AssigneeInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
throws|,
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|ConfigInvalidException
block|{
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|EDIT_ASSIGNEE
argument_list|)
expr_stmt|;
name|input
operator|.
name|assignee
operator|=
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|input
operator|.
name|assignee
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|assignee
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"missing assignee field"
argument_list|)
throw|;
block|}
name|IdentifiedUser
name|assignee
init|=
name|accountResolver
operator|.
name|resolve
argument_list|(
name|input
operator|.
name|assignee
argument_list|)
operator|.
name|asUniqueUser
argument_list|()
decl_stmt|;
try|try
block|{
name|permissionBackend
operator|.
name|absentUser
argument_list|(
name|assignee
operator|.
name|getAccountId
argument_list|()
argument_list|)
operator|.
name|change
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
operator|.
name|check
argument_list|(
name|ChangePermission
operator|.
name|READ
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"read not permitted for "
operator|+
name|input
operator|.
name|assignee
argument_list|)
throw|;
block|}
try|try
init|(
name|BatchUpdate
name|bu
init|=
name|updateFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getChange
argument_list|()
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
name|SetAssigneeOp
name|op
init|=
name|assigneeFactory
operator|.
name|create
argument_list|(
name|assignee
argument_list|)
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|rsrc
operator|.
name|getId
argument_list|()
argument_list|,
name|op
argument_list|)
expr_stmt|;
name|ReviewerSet
name|currentReviewers
init|=
name|approvalsUtil
operator|.
name|getReviewers
argument_list|(
name|rsrc
operator|.
name|getNotes
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|currentReviewers
operator|.
name|all
argument_list|()
operator|.
name|contains
argument_list|(
name|assignee
operator|.
name|getAccountId
argument_list|()
argument_list|)
condition|)
block|{
name|ReviewerAddition
name|reviewersAddition
init|=
name|addAssigneeAsCC
argument_list|(
name|rsrc
argument_list|,
name|input
operator|.
name|assignee
argument_list|)
decl_stmt|;
name|reviewersAddition
operator|.
name|op
operator|.
name|suppressEmail
argument_list|()
expr_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|rsrc
operator|.
name|getId
argument_list|()
argument_list|,
name|reviewersAddition
operator|.
name|op
argument_list|)
expr_stmt|;
block|}
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|accountLoaderFactory
operator|.
name|create
argument_list|(
literal|true
argument_list|)
operator|.
name|fillOne
argument_list|(
name|assignee
operator|.
name|getAccountId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|addAssigneeAsCC (ChangeResource rsrc, String assignee)
specifier|private
name|ReviewerAddition
name|addAssigneeAsCC
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|String
name|assignee
parameter_list|)
throws|throws
name|IOException
throws|,
name|PermissionBackendException
throws|,
name|ConfigInvalidException
block|{
name|AddReviewerInput
name|reviewerInput
init|=
operator|new
name|AddReviewerInput
argument_list|()
decl_stmt|;
name|reviewerInput
operator|.
name|reviewer
operator|=
name|assignee
expr_stmt|;
name|reviewerInput
operator|.
name|state
operator|=
name|ReviewerState
operator|.
name|CC
expr_stmt|;
name|reviewerInput
operator|.
name|confirmed
operator|=
literal|true
expr_stmt|;
name|reviewerInput
operator|.
name|notify
operator|=
name|NotifyHandling
operator|.
name|NONE
expr_stmt|;
return|return
name|reviewerAdder
operator|.
name|prepare
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
argument_list|,
name|reviewerInput
argument_list|,
literal|false
argument_list|)
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
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Edit Assignee"
argument_list|)
operator|.
name|setVisible
argument_list|(
name|rsrc
operator|.
name|permissions
argument_list|()
operator|.
name|testCond
argument_list|(
name|ChangePermission
operator|.
name|EDIT_ASSIGNEE
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

