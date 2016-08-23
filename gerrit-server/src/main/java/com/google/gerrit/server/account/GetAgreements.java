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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|data
operator|.
name|ContributorAgreement
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
name|data
operator|.
name|PermissionRule
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
name|data
operator|.
name|PermissionRule
operator|.
name|Action
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
name|AgreementInfo
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
name|MethodNotAllowedException
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
name|RestReadView
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
name|AccountGroup
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
name|config
operator|.
name|AgreementJson
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
name|config
operator|.
name|GerritServerConfig
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
name|ProjectCache
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Config
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
name|Collection
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
DECL|class|GetAgreements
specifier|public
class|class
name|GetAgreements
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
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
name|GetAgreements
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|self
specifier|private
specifier|final
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|identifiedUserFactory
specifier|private
specifier|final
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
decl_stmt|;
DECL|field|agreementJson
specifier|private
specifier|final
name|AgreementJson
name|agreementJson
decl_stmt|;
DECL|field|agreementsEnabled
specifier|private
specifier|final
name|boolean
name|agreementsEnabled
decl_stmt|;
annotation|@
name|Inject
DECL|method|GetAgreements (Provider<IdentifiedUser> self, ProjectCache projectCache, IdentifiedUser.GenericFactory identifiedUserFactory, AgreementJson agreementJson, @GerritServerConfig Config config)
name|GetAgreements
parameter_list|(
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
name|self
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|,
name|IdentifiedUser
operator|.
name|GenericFactory
name|identifiedUserFactory
parameter_list|,
name|AgreementJson
name|agreementJson
parameter_list|,
annotation|@
name|GerritServerConfig
name|Config
name|config
parameter_list|)
block|{
name|this
operator|.
name|self
operator|=
name|self
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|identifiedUserFactory
operator|=
name|identifiedUserFactory
expr_stmt|;
name|this
operator|.
name|agreementJson
operator|=
name|agreementJson
expr_stmt|;
name|this
operator|.
name|agreementsEnabled
operator|=
name|config
operator|.
name|getBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|"contributorAgreements"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (AccountResource resource)
specifier|public
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|resource
parameter_list|)
throws|throws
name|RestApiException
block|{
if|if
condition|(
operator|!
name|agreementsEnabled
condition|)
block|{
throw|throw
operator|new
name|MethodNotAllowedException
argument_list|(
literal|"contributor agreements disabled"
argument_list|)
throw|;
block|}
if|if
condition|(
name|self
operator|.
name|get
argument_list|()
operator|!=
name|resource
operator|.
name|getUser
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"not allowed to get contributor agreements"
argument_list|)
throw|;
block|}
name|IdentifiedUser
name|user
init|=
name|identifiedUserFactory
operator|.
name|create
argument_list|(
name|self
operator|.
name|get
argument_list|()
operator|.
name|getAccountId
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|ContributorAgreement
argument_list|>
name|cas
init|=
name|projectCache
operator|.
name|getAllProjects
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|getContributorAgreements
argument_list|()
decl_stmt|;
for|for
control|(
name|ContributorAgreement
name|ca
range|:
name|cas
control|)
block|{
name|List
argument_list|<
name|AccountGroup
operator|.
name|UUID
argument_list|>
name|groupIds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|PermissionRule
name|rule
range|:
name|ca
operator|.
name|getAccepted
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|rule
operator|.
name|getAction
argument_list|()
operator|==
name|Action
operator|.
name|ALLOW
operator|)
operator|&&
operator|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
operator|)
condition|)
block|{
if|if
condition|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|groupIds
operator|.
name|add
argument_list|(
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"group \""
operator|+
name|rule
operator|.
name|getGroup
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"\" does not "
operator|+
literal|"exist, referenced in CLA \""
operator|+
name|ca
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|user
operator|.
name|getEffectiveGroups
argument_list|()
operator|.
name|containsAnyOf
argument_list|(
name|groupIds
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|agreementJson
operator|.
name|format
argument_list|(
name|ca
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
block|}
end_class

end_unit

