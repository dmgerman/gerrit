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
name|DeleteReviewerInput
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
name|BatchUpdateOp
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
DECL|class|DeleteReviewer
specifier|public
class|class
name|DeleteReviewer
extends|extends
name|RetryingRestModifyView
argument_list|<
name|ReviewerResource
argument_list|,
name|DeleteReviewerInput
argument_list|,
name|Response
argument_list|<
name|?
argument_list|>
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
DECL|field|deleteReviewerOpFactory
specifier|private
specifier|final
name|DeleteReviewerOp
operator|.
name|Factory
name|deleteReviewerOpFactory
decl_stmt|;
DECL|field|deleteReviewerByEmailOpFactory
specifier|private
specifier|final
name|DeleteReviewerByEmailOp
operator|.
name|Factory
name|deleteReviewerByEmailOpFactory
decl_stmt|;
annotation|@
name|Inject
DECL|method|DeleteReviewer ( Provider<ReviewDb> dbProvider, RetryHelper retryHelper, DeleteReviewerOp.Factory deleteReviewerOpFactory, DeleteReviewerByEmailOp.Factory deleteReviewerByEmailOpFactory)
name|DeleteReviewer
parameter_list|(
name|Provider
argument_list|<
name|ReviewDb
argument_list|>
name|dbProvider
parameter_list|,
name|RetryHelper
name|retryHelper
parameter_list|,
name|DeleteReviewerOp
operator|.
name|Factory
name|deleteReviewerOpFactory
parameter_list|,
name|DeleteReviewerByEmailOp
operator|.
name|Factory
name|deleteReviewerByEmailOpFactory
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
name|deleteReviewerOpFactory
operator|=
name|deleteReviewerOpFactory
expr_stmt|;
name|this
operator|.
name|deleteReviewerByEmailOpFactory
operator|=
name|deleteReviewerByEmailOpFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|applyImpl ( BatchUpdate.Factory updateFactory, ReviewerResource rsrc, DeleteReviewerInput input)
specifier|protected
name|Response
argument_list|<
name|?
argument_list|>
name|applyImpl
parameter_list|(
name|BatchUpdate
operator|.
name|Factory
name|updateFactory
parameter_list|,
name|ReviewerResource
name|rsrc
parameter_list|,
name|DeleteReviewerInput
name|input
parameter_list|)
throws|throws
name|RestApiException
throws|,
name|UpdateException
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
name|DeleteReviewerInput
argument_list|()
expr_stmt|;
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
name|dbProvider
operator|.
name|get
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChangeResource
argument_list|()
operator|.
name|getProject
argument_list|()
argument_list|,
name|rsrc
operator|.
name|getChangeResource
argument_list|()
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
name|BatchUpdateOp
name|op
decl_stmt|;
if|if
condition|(
name|rsrc
operator|.
name|isByEmail
argument_list|()
condition|)
block|{
name|op
operator|=
name|deleteReviewerByEmailOpFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getReviewerByEmail
argument_list|()
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|op
operator|=
name|deleteReviewerOpFactory
operator|.
name|create
argument_list|(
name|rsrc
operator|.
name|getReviewerUser
argument_list|()
operator|.
name|getAccount
argument_list|()
argument_list|,
name|input
argument_list|)
expr_stmt|;
block|}
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
block|}
return|return
name|Response
operator|.
name|none
argument_list|()
return|;
block|}
block|}
end_class

end_unit
