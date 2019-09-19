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
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth8
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|testing
operator|.
name|GerritJUnit
operator|.
name|assertThrows
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expectLastCall
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|resetToStrict
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|ImmutableList
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
name|extensions
operator|.
name|annotations
operator|.
name|Exports
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
name|QuotaEnforcer
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
name|QuotaRequestContext
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
name|Module
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|OptionalLong
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
DECL|class|MultipleQuotaPluginsIT
specifier|public
class|class
name|MultipleQuotaPluginsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|quotaEnforcerA
specifier|private
specifier|static
specifier|final
name|QuotaEnforcer
name|quotaEnforcerA
init|=
name|EasyMock
operator|.
name|createStrictMock
argument_list|(
name|QuotaEnforcer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|quotaEnforcerB
specifier|private
specifier|static
specifier|final
name|QuotaEnforcer
name|quotaEnforcerB
init|=
name|EasyMock
operator|.
name|createStrictMock
argument_list|(
name|QuotaEnforcer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|identifiedAdmin
specifier|private
name|IdentifiedUser
name|identifiedAdmin
decl_stmt|;
DECL|field|quotaBackend
annotation|@
name|Inject
specifier|private
name|QuotaBackend
name|quotaBackend
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
name|QuotaEnforcer
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
literal|"TestQuotaEnforcerA"
argument_list|)
argument_list|)
operator|.
name|toProvider
argument_list|(
parameter_list|()
lambda|->
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|QuotaEnforcer
operator|.
name|class
argument_list|)
operator|.
name|annotatedWith
argument_list|(
name|Exports
operator|.
name|named
argument_list|(
literal|"TestQuotaEnforcerB"
argument_list|)
argument_list|)
operator|.
name|toProvider
argument_list|(
parameter_list|()
lambda|->
name|quotaEnforcerB
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
name|identifiedAdmin
operator|=
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|admin
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
name|resetToStrict
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|resetToStrict
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refillsOnError ()
specifier|public
name|void
name|refillsOnError
parameter_list|()
block|{
name|QuotaRequestContext
name|ctx
init|=
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|quotaEnforcerA
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|quotaEnforcerB
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|error
argument_list|(
literal|"fail"
argument_list|)
argument_list|)
expr_stmt|;
name|quotaEnforcerA
operator|.
name|refill
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|expectLastCall
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|quotaBackend
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"testGroup"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|QuotaResponse
operator|.
name|Aggregated
operator|.
name|create
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|,
name|QuotaResponse
operator|.
name|error
argument_list|(
literal|"fail"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|refillsOnException ()
specifier|public
name|void
name|refillsOnException
parameter_list|()
block|{
name|NullPointerException
name|exception
init|=
operator|new
name|NullPointerException
argument_list|()
decl_stmt|;
name|QuotaRequestContext
name|ctx
init|=
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|quotaEnforcerA
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andThrow
argument_list|(
name|exception
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|quotaEnforcerB
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|()
argument_list|)
expr_stmt|;
name|quotaEnforcerB
operator|.
name|refill
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|expectLastCall
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
name|NullPointerException
name|thrown
init|=
name|assertThrows
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
parameter_list|()
lambda|->
name|quotaBackend
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"testGroup"
argument_list|)
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|thrown
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|exception
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doesNotRefillNoOp ()
specifier|public
name|void
name|doesNotRefillNoOp
parameter_list|()
block|{
name|QuotaRequestContext
name|ctx
init|=
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|quotaEnforcerA
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|error
argument_list|(
literal|"fail"
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|quotaEnforcerB
operator|.
name|requestTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|noOp
argument_list|()
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|quotaBackend
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|requestToken
argument_list|(
literal|"testGroup"
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|QuotaResponse
operator|.
name|Aggregated
operator|.
name|create
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|QuotaResponse
operator|.
name|error
argument_list|(
literal|"fail"
argument_list|)
argument_list|,
name|QuotaResponse
operator|.
name|noOp
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|minimumAvailableTokens ()
specifier|public
name|void
name|minimumAvailableTokens
parameter_list|()
block|{
name|QuotaRequestContext
name|ctx
init|=
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|quotaEnforcerA
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|(
literal|20L
argument_list|)
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|quotaEnforcerB
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|(
literal|10L
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
name|OptionalLong
name|tokens
init|=
name|quotaBackend
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|)
operator|.
name|availableTokens
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|tokens
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|tokens
operator|.
name|getAsLong
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|10L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|ignoreNoOpForAvailableTokens ()
specifier|public
name|void
name|ignoreNoOpForAvailableTokens
parameter_list|()
block|{
name|QuotaRequestContext
name|ctx
init|=
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|expect
argument_list|(
name|quotaEnforcerA
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|noOp
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|quotaEnforcerB
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|,
name|ctx
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|QuotaResponse
operator|.
name|ok
argument_list|(
literal|20L
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerA
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|quotaEnforcerB
argument_list|)
expr_stmt|;
name|OptionalLong
name|tokens
init|=
name|quotaBackend
operator|.
name|user
argument_list|(
name|identifiedAdmin
argument_list|)
operator|.
name|availableTokens
argument_list|(
literal|"testGroup"
argument_list|)
operator|.
name|availableTokens
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|tokens
argument_list|)
operator|.
name|isPresent
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|tokens
operator|.
name|getAsLong
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|20L
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

