begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
name|gerrit
operator|.
name|entities
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
name|AddReviewerResult
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
name|RestCollectionModifyView
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
name|NotifyResolver
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
name|ReviewerResource
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
name|query
operator|.
name|change
operator|.
name|ChangeData
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
DECL|class|PostReviewers
specifier|public
class|class
name|PostReviewers
implements|implements
name|RestCollectionModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|ReviewerResource
argument_list|,
name|AddReviewerInput
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
DECL|field|changeDataFactory
specifier|private
specifier|final
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
decl_stmt|;
DECL|field|notifyResolver
specifier|private
specifier|final
name|NotifyResolver
name|notifyResolver
decl_stmt|;
DECL|field|reviewerAdder
specifier|private
specifier|final
name|ReviewerAdder
name|reviewerAdder
decl_stmt|;
annotation|@
name|Inject
DECL|method|PostReviewers ( BatchUpdate.Factory updateFactory, ChangeData.Factory changeDataFactory, NotifyResolver notifyResolver, ReviewerAdder reviewerAdder)
name|PostReviewers
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ChangeData
operator|.
name|Factory
name|changeDataFactory
parameter_list|,
name|NotifyResolver
name|notifyResolver
parameter_list|,
name|ReviewerAdder
name|reviewerAdder
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
name|changeDataFactory
operator|=
name|changeDataFactory
expr_stmt|;
name|this
operator|.
name|notifyResolver
operator|=
name|notifyResolver
expr_stmt|;
name|this
operator|.
name|reviewerAdder
operator|=
name|reviewerAdder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, AddReviewerInput input)
specifier|public
name|Response
argument_list|<
name|AddReviewerResult
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|AddReviewerInput
name|input
parameter_list|)
throws|throws
name|IOException
throws|,
name|RestApiException
throws|,
name|UpdateException
throws|,
name|PermissionBackendException
throws|,
name|ConfigInvalidException
block|{
if|if
condition|(
name|input
operator|.
name|reviewer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|BadRequestException
argument_list|(
literal|"missing reviewer field"
argument_list|)
throw|;
block|}
name|ReviewerAddition
name|addition
init|=
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
name|input
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|addition
operator|.
name|op
operator|==
literal|null
condition|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|addition
operator|.
name|result
argument_list|)
return|;
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
name|bu
operator|.
name|setNotify
argument_list|(
name|resolveNotify
argument_list|(
name|rsrc
argument_list|,
name|input
argument_list|)
argument_list|)
expr_stmt|;
name|Change
operator|.
name|Id
name|id
init|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|bu
operator|.
name|addOp
argument_list|(
name|id
argument_list|,
name|addition
operator|.
name|op
argument_list|)
expr_stmt|;
name|bu
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
comment|// Re-read change to take into account results of the update.
name|addition
operator|.
name|gatherResults
argument_list|(
name|changeDataFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|addition
operator|.
name|result
argument_list|)
return|;
block|}
DECL|method|resolveNotify (ChangeResource rsrc, AddReviewerInput input)
specifier|private
name|NotifyResolver
operator|.
name|Result
name|resolveNotify
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|AddReviewerInput
name|input
parameter_list|)
throws|throws
name|BadRequestException
throws|,
name|ConfigInvalidException
throws|,
name|IOException
block|{
name|NotifyHandling
name|notifyHandling
init|=
name|input
operator|.
name|notify
decl_stmt|;
if|if
condition|(
name|notifyHandling
operator|==
literal|null
condition|)
block|{
name|notifyHandling
operator|=
name|rsrc
operator|.
name|getChange
argument_list|()
operator|.
name|isWorkInProgress
argument_list|()
condition|?
name|NotifyHandling
operator|.
name|NONE
else|:
name|NotifyHandling
operator|.
name|ALL
expr_stmt|;
block|}
return|return
name|notifyResolver
operator|.
name|resolve
argument_list|(
name|notifyHandling
argument_list|,
name|input
operator|.
name|notifyDetails
argument_list|)
return|;
block|}
block|}
end_class

end_unit

