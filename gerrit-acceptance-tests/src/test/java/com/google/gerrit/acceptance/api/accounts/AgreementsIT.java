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
DECL|package|com.google.gerrit.acceptance.api.accounts
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|api
operator|.
name|accounts
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
name|TruthJUnit
operator|.
name|assume
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
name|GroupReference
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
name|extensions
operator|.
name|api
operator|.
name|groups
operator|.
name|GroupApi
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
name|common
operator|.
name|ServerInfo
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
name|git
operator|.
name|ProjectConfig
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
name|testutil
operator|.
name|ConfigSuite
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
DECL|class|AgreementsIT
specifier|public
class|class
name|AgreementsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|ca
specifier|private
name|ContributorAgreement
name|ca
decl_stmt|;
DECL|field|ca2
specifier|private
name|ContributorAgreement
name|ca2
decl_stmt|;
annotation|@
name|ConfigSuite
operator|.
name|Config
DECL|method|enableAgreementsConfig ()
specifier|public
specifier|static
name|Config
name|enableAgreementsConfig
parameter_list|()
block|{
name|Config
name|cfg
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setBoolean
argument_list|(
literal|"auth"
argument_list|,
literal|null
argument_list|,
literal|"contributorAgreements"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|g
init|=
name|createGroup
argument_list|(
literal|"cla-test-group"
argument_list|)
decl_stmt|;
name|GroupApi
name|groupApi
init|=
name|gApi
operator|.
name|groups
argument_list|()
operator|.
name|id
argument_list|(
name|g
argument_list|)
decl_stmt|;
name|groupApi
operator|.
name|description
argument_list|(
literal|"CLA test group"
argument_list|)
expr_stmt|;
name|AccountGroup
name|caGroup
init|=
name|groupCache
operator|.
name|get
argument_list|(
operator|new
name|AccountGroup
operator|.
name|UUID
argument_list|(
name|groupApi
operator|.
name|detail
argument_list|()
operator|.
name|id
argument_list|)
argument_list|)
decl_stmt|;
name|GroupReference
name|groupRef
init|=
name|GroupReference
operator|.
name|forGroup
argument_list|(
name|caGroup
argument_list|)
decl_stmt|;
name|PermissionRule
name|rule
init|=
operator|new
name|PermissionRule
argument_list|(
name|groupRef
argument_list|)
decl_stmt|;
name|rule
operator|.
name|setAction
argument_list|(
name|PermissionRule
operator|.
name|Action
operator|.
name|ALLOW
argument_list|)
expr_stmt|;
name|ca
operator|=
operator|new
name|ContributorAgreement
argument_list|(
literal|"cla-test"
argument_list|)
expr_stmt|;
name|ca
operator|.
name|setDescription
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
name|ca
operator|.
name|setAgreementUrl
argument_list|(
literal|"agreement-url"
argument_list|)
expr_stmt|;
name|ca
operator|.
name|setAutoVerify
argument_list|(
name|groupRef
argument_list|)
expr_stmt|;
name|ca
operator|.
name|setAccepted
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|rule
argument_list|)
argument_list|)
expr_stmt|;
name|ca2
operator|=
operator|new
name|ContributorAgreement
argument_list|(
literal|"cla-test-no-auto-verify"
argument_list|)
expr_stmt|;
name|ca2
operator|.
name|setDescription
argument_list|(
literal|"description"
argument_list|)
expr_stmt|;
name|ca2
operator|.
name|setAgreementUrl
argument_list|(
literal|"agreement-url"
argument_list|)
expr_stmt|;
name|ProjectConfig
name|cfg
init|=
name|projectCache
operator|.
name|checkedGet
argument_list|(
name|allProjects
argument_list|)
operator|.
name|getConfig
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|replace
argument_list|(
name|ca
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|replace
argument_list|(
name|ca2
argument_list|)
expr_stmt|;
name|saveProjectConfig
argument_list|(
name|allProjects
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getAvailableAgreements ()
specifier|public
name|void
name|getAvailableAgreements
parameter_list|()
throws|throws
name|Exception
block|{
name|ServerInfo
name|info
init|=
name|gApi
operator|.
name|config
argument_list|()
operator|.
name|server
argument_list|()
operator|.
name|getInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|isContributorAgreementsEnabled
argument_list|()
condition|)
block|{
name|assertThat
argument_list|(
name|info
operator|.
name|auth
operator|.
name|useContributorAgreements
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|auth
operator|.
name|contributorAgreements
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|AgreementInfo
name|agreementInfo
init|=
name|info
operator|.
name|auth
operator|.
name|contributorAgreements
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|agreementInfo
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|agreementInfo
operator|.
name|autoVerifyGroup
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|agreementInfo
operator|=
name|info
operator|.
name|auth
operator|.
name|contributorAgreements
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|agreementInfo
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|agreementInfo
operator|.
name|autoVerifyGroup
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|assertThat
argument_list|(
name|info
operator|.
name|auth
operator|.
name|useContributorAgreements
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|auth
operator|.
name|contributorAgreements
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|signNonExistingAgreement ()
specifier|public
name|void
name|signNonExistingAgreement
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isContributorAgreementsEnabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|UnprocessableEntityException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"contributor agreement not found"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|signAgreement
argument_list|(
literal|"does-not-exist"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|signAgreementNoAutoVerify ()
specifier|public
name|void
name|signAgreementNoAutoVerify
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isContributorAgreementsEnabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|BadRequestException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"cannot enter a non-autoVerify agreement"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|signAgreement
argument_list|(
name|ca2
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|signAgreement ()
specifier|public
name|void
name|signAgreement
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isContributorAgreementsEnabled
argument_list|()
argument_list|)
operator|.
name|isTrue
argument_list|()
expr_stmt|;
comment|// List of agreements is initially empty
name|List
argument_list|<
name|AgreementInfo
argument_list|>
name|result
init|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|listAgreements
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
comment|// Sign the agreement
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|signAgreement
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|listAgreements
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|AgreementInfo
name|info
init|=
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|description
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|url
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getAgreementUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|info
operator|.
name|autoVerifyGroup
operator|.
name|name
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Signing the same agreement again has no effect
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|signAgreement
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|listAgreements
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|result
argument_list|)
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|agreementsDisabledSign ()
specifier|public
name|void
name|agreementsDisabledSign
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isContributorAgreementsEnabled
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"contributor agreements disabled"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|signAgreement
argument_list|(
name|ca
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|agreementsDisabledList ()
specifier|public
name|void
name|agreementsDisabledList
parameter_list|()
throws|throws
name|Exception
block|{
name|assume
argument_list|()
operator|.
name|that
argument_list|(
name|isContributorAgreementsEnabled
argument_list|()
argument_list|)
operator|.
name|isFalse
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|MethodNotAllowedException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"contributor agreements disabled"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|listAgreements
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

