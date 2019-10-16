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
DECL|package|com.google.gerrit.server.quota
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
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
name|base
operator|.
name|Preconditions
operator|.
name|checkState
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
name|common
operator|.
name|flogger
operator|.
name|FluentLogger
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
name|entities
operator|.
name|Project
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
name|plugincontext
operator|.
name|PluginSetContext
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
name|plugincontext
operator|.
name|PluginSetEntryContext
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
operator|.
name|Aggregated
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|DefaultQuotaBackend
specifier|public
class|class
name|DefaultQuotaBackend
implements|implements
name|QuotaBackend
block|{
DECL|field|logger
specifier|private
specifier|static
specifier|final
name|FluentLogger
name|logger
init|=
name|FluentLogger
operator|.
name|forEnclosingClass
argument_list|()
decl_stmt|;
DECL|field|userProvider
specifier|private
specifier|final
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
decl_stmt|;
DECL|field|quotaEnforcers
specifier|private
specifier|final
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
decl_stmt|;
annotation|@
name|Inject
DECL|method|DefaultQuotaBackend ( Provider<CurrentUser> userProvider, PluginSetContext<QuotaEnforcer> quotaEnforcers)
name|DefaultQuotaBackend
parameter_list|(
name|Provider
argument_list|<
name|CurrentUser
argument_list|>
name|userProvider
parameter_list|,
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
parameter_list|)
block|{
name|this
operator|.
name|userProvider
operator|=
name|userProvider
expr_stmt|;
name|this
operator|.
name|quotaEnforcers
operator|=
name|quotaEnforcers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|currentUser ()
specifier|public
name|WithUser
name|currentUser
parameter_list|()
block|{
return|return
operator|new
name|WithUser
argument_list|(
name|quotaEnforcers
argument_list|,
name|userProvider
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|user (CurrentUser user)
specifier|public
name|WithUser
name|user
parameter_list|(
name|CurrentUser
name|user
parameter_list|)
block|{
return|return
operator|new
name|WithUser
argument_list|(
name|quotaEnforcers
argument_list|,
name|user
argument_list|)
return|;
block|}
DECL|method|request ( PluginSetContext<QuotaEnforcer> quotaEnforcers, String quotaGroup, QuotaRequestContext requestContext, long numTokens, boolean deduct)
specifier|private
specifier|static
name|QuotaResponse
operator|.
name|Aggregated
name|request
parameter_list|(
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
parameter_list|,
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|requestContext
parameter_list|,
name|long
name|numTokens
parameter_list|,
name|boolean
name|deduct
parameter_list|)
block|{
name|checkState
argument_list|(
name|numTokens
operator|>
literal|0
argument_list|,
literal|"numTokens must be a positive, non-zero long"
argument_list|)
expr_stmt|;
comment|// PluginSets can change their content when plugins (de-)register. Copy the currently registered
comment|// plugins so that we can iterate twice on a stable list.
name|List
argument_list|<
name|PluginSetEntryContext
argument_list|<
name|QuotaEnforcer
argument_list|>
argument_list|>
name|enforcers
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|quotaEnforcers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|QuotaResponse
argument_list|>
name|responses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|enforcers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PluginSetEntryContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|enforcer
range|:
name|enforcers
control|)
block|{
try|try
block|{
if|if
condition|(
name|deduct
condition|)
block|{
name|responses
operator|.
name|add
argument_list|(
name|enforcer
operator|.
name|call
argument_list|(
name|p
lambda|->
name|p
operator|.
name|requestTokens
argument_list|(
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|responses
operator|.
name|add
argument_list|(
name|enforcer
operator|.
name|call
argument_list|(
name|p
lambda|->
name|p
operator|.
name|dryRun
argument_list|(
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// Roll back the quota request for all enforcers that deducted the quota. Rethrow the
comment|// exception to adhere to the API contract.
if|if
condition|(
name|deduct
condition|)
block|{
name|refillAfterErrorOrException
argument_list|(
name|enforcers
argument_list|,
name|responses
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
if|if
condition|(
name|deduct
operator|&&
name|responses
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|r
lambda|->
name|r
operator|.
name|status
argument_list|()
operator|.
name|isError
argument_list|()
argument_list|)
condition|)
block|{
comment|// Roll back the quota request for all enforcers that deducted the quota (= the request
comment|// succeeded). Don't touch failed enforcers as the interface contract said that failed
comment|// requests should not be deducted.
name|refillAfterErrorOrException
argument_list|(
name|enforcers
argument_list|,
name|responses
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|)
block|;     }
name|logger
operator|.
name|atFine
argument_list|()
operator|.
name|log
argument_list|(
literal|"Quota request for %s with %s (deduction=%s) for %s token returned %s"
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|deduct
condition|?
literal|"(deduction=yes)"
else|:
literal|"(deduction=no)"
argument_list|,
name|numTokens
argument_list|,
name|responses
argument_list|)
expr_stmt|;
return|return
name|QuotaResponse
operator|.
name|Aggregated
operator|.
name|create
argument_list|(
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|responses
argument_list|)
argument_list|)
return|;
block|}
DECL|method|availableTokens ( PluginSetContext<QuotaEnforcer> quotaEnforcers, String quotaGroup, QuotaRequestContext requestContext)
specifier|private
specifier|static
name|QuotaResponse
operator|.
name|Aggregated
name|availableTokens
parameter_list|(
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
parameter_list|,
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|requestContext
parameter_list|)
block|{
comment|// PluginSets can change their content when plugins (de-)register. Copy the currently registered
comment|// plugins so that we can iterate twice on a stable list.
name|List
argument_list|<
name|PluginSetEntryContext
argument_list|<
name|QuotaEnforcer
argument_list|>
argument_list|>
name|enforcers
init|=
name|ImmutableList
operator|.
name|copyOf
argument_list|(
name|quotaEnforcers
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|QuotaResponse
argument_list|>
name|responses
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|enforcers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|PluginSetEntryContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|enforcer
range|:
name|enforcers
control|)
block|{
name|responses
operator|.
name|add
argument_list|(
name|enforcer
operator|.
name|call
argument_list|(
name|p
lambda|->
name|p
operator|.
name|availableTokens
argument_list|(
name|quotaGroup
argument_list|,
name|requestContext
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|QuotaResponse
operator|.
name|Aggregated
operator|.
name|create
argument_list|(
name|responses
argument_list|)
return|;
block|}
DECL|method|refillAfterErrorOrException ( List<PluginSetEntryContext<QuotaEnforcer>> enforcers, List<QuotaResponse> collectedResponses, String quotaGroup, QuotaRequestContext requestContext, long numTokens)
specifier|private
specifier|static
name|void
name|refillAfterErrorOrException
parameter_list|(
name|List
argument_list|<
name|PluginSetEntryContext
argument_list|<
name|QuotaEnforcer
argument_list|>
argument_list|>
name|enforcers
parameter_list|,
name|List
argument_list|<
name|QuotaResponse
argument_list|>
name|collectedResponses
parameter_list|,
name|String
name|quotaGroup
parameter_list|,
name|QuotaRequestContext
name|requestContext
parameter_list|,
name|long
name|numTokens
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|collectedResponses
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|collectedResponses
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|status
argument_list|()
operator|.
name|isOk
argument_list|()
condition|)
block|{
name|enforcers
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|run
argument_list|(
name|p
lambda|->
name|p
operator|.
name|refill
argument_list|(
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|WithUser
specifier|static
class|class
name|WithUser
extends|extends
name|WithResource
implements|implements
name|QuotaBackend
operator|.
name|WithUser
block|{
DECL|method|WithUser (PluginSetContext<QuotaEnforcer> quotaEnforcers, CurrentUser user)
name|WithUser
parameter_list|(
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
parameter_list|,
name|CurrentUser
name|user
parameter_list|)
block|{
name|super
argument_list|(
name|quotaEnforcers
argument_list|,
name|QuotaRequestContext
operator|.
name|builder
argument_list|()
operator|.
name|user
argument_list|(
name|user
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|account (Account.Id account)
specifier|public
name|QuotaBackend
operator|.
name|WithResource
name|account
parameter_list|(
name|Account
operator|.
name|Id
name|account
parameter_list|)
block|{
name|QuotaRequestContext
name|ctx
init|=
name|requestContext
operator|.
name|toBuilder
argument_list|()
operator|.
name|account
argument_list|(
name|account
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
operator|new
name|WithResource
argument_list|(
name|quotaEnforcers
argument_list|,
name|ctx
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|project (Project.NameKey project)
specifier|public
name|QuotaBackend
operator|.
name|WithResource
name|project
parameter_list|(
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|QuotaRequestContext
name|ctx
init|=
name|requestContext
operator|.
name|toBuilder
argument_list|()
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
operator|new
name|WithResource
argument_list|(
name|quotaEnforcers
argument_list|,
name|ctx
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|change (Change.Id change, Project.NameKey project)
specifier|public
name|QuotaBackend
operator|.
name|WithResource
name|change
parameter_list|(
name|Change
operator|.
name|Id
name|change
parameter_list|,
name|Project
operator|.
name|NameKey
name|project
parameter_list|)
block|{
name|QuotaRequestContext
name|ctx
init|=
name|requestContext
operator|.
name|toBuilder
argument_list|()
operator|.
name|change
argument_list|(
name|change
argument_list|)
operator|.
name|project
argument_list|(
name|project
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
return|return
operator|new
name|WithResource
argument_list|(
name|quotaEnforcers
argument_list|,
name|ctx
argument_list|)
return|;
block|}
block|}
DECL|class|WithResource
specifier|static
class|class
name|WithResource
implements|implements
name|QuotaBackend
operator|.
name|WithResource
block|{
DECL|field|requestContext
specifier|protected
specifier|final
name|QuotaRequestContext
name|requestContext
decl_stmt|;
DECL|field|quotaEnforcers
specifier|protected
specifier|final
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
decl_stmt|;
DECL|method|WithResource ( PluginSetContext<QuotaEnforcer> quotaEnforcers, QuotaRequestContext quotaRequestContext)
specifier|private
name|WithResource
parameter_list|(
name|PluginSetContext
argument_list|<
name|QuotaEnforcer
argument_list|>
name|quotaEnforcers
parameter_list|,
name|QuotaRequestContext
name|quotaRequestContext
parameter_list|)
block|{
name|this
operator|.
name|quotaEnforcers
operator|=
name|quotaEnforcers
expr_stmt|;
name|this
operator|.
name|requestContext
operator|=
name|quotaRequestContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|requestTokens (String quotaGroup, long numTokens)
specifier|public
name|QuotaResponse
operator|.
name|Aggregated
name|requestTokens
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|long
name|numTokens
parameter_list|)
block|{
return|return
name|DefaultQuotaBackend
operator|.
name|request
argument_list|(
name|quotaEnforcers
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|dryRun (String quotaGroup, long numTokens)
specifier|public
name|QuotaResponse
operator|.
name|Aggregated
name|dryRun
parameter_list|(
name|String
name|quotaGroup
parameter_list|,
name|long
name|numTokens
parameter_list|)
block|{
return|return
name|DefaultQuotaBackend
operator|.
name|request
argument_list|(
name|quotaEnforcers
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|,
name|numTokens
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|availableTokens (String quotaGroup)
specifier|public
name|Aggregated
name|availableTokens
parameter_list|(
name|String
name|quotaGroup
parameter_list|)
block|{
return|return
name|DefaultQuotaBackend
operator|.
name|availableTokens
argument_list|(
name|quotaEnforcers
argument_list|,
name|quotaGroup
argument_list|,
name|requestContext
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

