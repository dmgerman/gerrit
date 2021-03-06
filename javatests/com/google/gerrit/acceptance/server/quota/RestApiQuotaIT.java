begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.acceptance.server.quota
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|server
operator|.
name|quota
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|restapi
operator|.
name|RestApiServlet
operator|.
name|SC_TOO_MANY_REQUESTS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|clearInvocations
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|acceptance
operator|.
name|AbstractDaemonTest
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
name|common
operator|.
name|ChangeInput
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
name|config
operator|.
name|FactoryModule
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
name|quota
operator|.
name|QuotaBackend
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
name|quota
operator|.
name|QuotaResponse
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
name|Module
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
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RestApiQuotaIT
specifier|public
class|class
name|RestApiQuotaIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|quotaBackendWithResource
specifier|private
specifier|static
specifier|final
name|QuotaBackend
operator|.
name|WithResource
name|quotaBackendWithResource
init|=
name|mock
argument_list|(
name|QuotaBackend
operator|.
name|WithResource
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|quotaBackendWithUser
specifier|private
specifier|static
specifier|final
name|QuotaBackend
operator|.
name|WithUser
name|quotaBackendWithUser
init|=
name|mock
argument_list|(
name|QuotaBackend
operator|.
name|WithUser
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createModule ()
specifier|public
name|Module
name|createModule
parameter_list|()
block|{
return|return
operator|new
name|FactoryModule
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|bind
argument_list|(
name|QuotaBackend
operator|.
name|class
argument_list|)
operator|.
name|toInstance
argument_list|(
operator|new
name|QuotaBackend
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|WithUser
name|currentUser
parameter_list|()
block|{
return|return
name|quotaBackendWithUser
return|;
block|}
annotation|@
name|Override
specifier|public
name|WithUser
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
name|quotaBackendWithUser
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|clearInvocations
argument_list|(
name|quotaBackendWithResource
argument_list|)
expr_stmt|;
name|clearInvocations
argument_list|(
name|quotaBackendWithUser
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|changeDetail ()
specifier|public
name|void
name|changeDetail
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|retrieveChangeId
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|quotaBackendWithResource
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes/detail:GET"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|change
argument_list|(
name|changeId
argument_list|,
name|project
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|quotaBackendWithResource
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/detail"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithResource
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes/detail:GET"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|change
argument_list|(
name|changeId
argument_list|,
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|revisionDetail ()
specifier|public
name|void
name|revisionDetail
parameter_list|()
throws|throws
name|Exception
block|{
name|Change
operator|.
name|Id
name|changeId
init|=
name|retrieveChangeId
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|quotaBackendWithResource
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes/revisions/actions:GET"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|change
argument_list|(
name|changeId
argument_list|,
name|project
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|quotaBackendWithResource
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/changes/"
operator|+
name|changeId
operator|+
literal|"/revisions/current/actions"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithResource
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes/revisions/actions:GET"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|change
argument_list|(
name|changeId
argument_list|,
name|project
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangePost ()
specifier|public
name|void
name|createChangePost
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes:POST"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ChangeInput
name|changeInput
init|=
operator|new
name|ChangeInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|adminRestSession
operator|.
name|post
argument_list|(
literal|"/changes/"
argument_list|,
name|changeInput
argument_list|)
operator|.
name|assertCreated
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/changes:POST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|accountDetail ()
specifier|public
name|void
name|accountDetail
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|quotaBackendWithResource
operator|.
name|requestToken
argument_list|(
literal|"/restapi/accounts/detail:GET"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|account
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|quotaBackendWithResource
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/accounts/self/detail"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithResource
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/accounts/detail:GET"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|account
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|config ()
specifier|public
name|void
name|config
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|requestToken
argument_list|(
literal|"/restapi/config/version:GET"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/version"
argument_list|)
operator|.
name|assertOK
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/config/version:GET"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|outOfQuotaReturnsError ()
specifier|public
name|void
name|outOfQuotaReturnsError
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|quotaBackendWithUser
operator|.
name|requestToken
argument_list|(
literal|"/restapi/config/version:GET"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|singletonAggregation
argument_list|(
name|QuotaResponse
operator|.
name|error
argument_list|(
literal|"no quota"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|adminRestSession
operator|.
name|get
argument_list|(
literal|"/config/server/version"
argument_list|)
operator|.
name|assertStatus
argument_list|(
name|SC_TOO_MANY_REQUESTS
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|quotaBackendWithUser
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"/restapi/config/version:GET"
argument_list|)
expr_stmt|;
block|}
DECL|method|retrieveChangeId ()
specifier|private
name|Change
operator|.
name|Id
name|retrieveChangeId
parameter_list|()
throws|throws
name|Exception
block|{
comment|// use REST API so that repository size quota doesn't have to be stubbed
name|ChangeInfo
name|changeInfo
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
operator|new
name|ChangeInput
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
literal|"master"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|Change
operator|.
name|id
argument_list|(
name|changeInfo
operator|.
name|_number
argument_list|)
return|;
block|}
DECL|method|singletonAggregation (QuotaResponse response)
specifier|private
specifier|static
name|QuotaResponse
operator|.
name|Aggregated
name|singletonAggregation
parameter_list|(
name|QuotaResponse
name|response
parameter_list|)
block|{
return|return
name|QuotaResponse
operator|.
name|Aggregated
operator|.
name|create
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|response
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

