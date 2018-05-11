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
import|import static
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
operator|.
name|SECONDS
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
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
name|extensions
operator|.
name|api
operator|.
name|changes
operator|.
name|CherryPickInput
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
name|ReviewInput
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
name|SubmitInput
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
name|projects
operator|.
name|BranchInfo
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
name|projects
operator|.
name|BranchInput
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
name|InheritableBoolean
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
name|testutil
operator|.
name|ConfigSuite
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
name|TestTimeUtil
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
name|AfterClass
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
name|BeforeClass
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
DECL|class|AgreementsIT
specifier|public
class|class
name|AgreementsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|caAutoVerify
specifier|private
name|ContributorAgreement
name|caAutoVerify
decl_stmt|;
DECL|field|caNoAutoVerify
specifier|private
name|ContributorAgreement
name|caNoAutoVerify
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
name|BeforeClass
DECL|method|setTimeForTesting ()
specifier|public
specifier|static
name|void
name|setTimeForTesting
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|resetWithClockStep
argument_list|(
literal|1
argument_list|,
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|restoreTime ()
specifier|public
specifier|static
name|void
name|restoreTime
parameter_list|()
block|{
name|TestTimeUtil
operator|.
name|useSystemTime
argument_list|()
expr_stmt|;
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
name|caAutoVerify
operator|=
name|configureContributorAgreement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|caNoAutoVerify
operator|=
name|configureContributorAgreement
argument_list|(
literal|false
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
name|assertAgreement
argument_list|(
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
argument_list|,
name|caAutoVerify
argument_list|)
expr_stmt|;
name|assertAgreement
argument_list|(
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
argument_list|,
name|caNoAutoVerify
argument_list|)
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
name|caNoAutoVerify
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
name|caAutoVerify
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Explicitly reset the user to force a new request context
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// Verify that the agreement was signed
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
name|assertAgreement
argument_list|(
name|info
argument_list|,
name|caAutoVerify
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
name|caAutoVerify
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
DECL|method|signAgreementAsOtherUser ()
specifier|public
name|void
name|signAgreementAsOtherUser
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
name|assertThat
argument_list|(
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|get
argument_list|()
operator|.
name|name
argument_list|)
operator|.
name|isNotEqualTo
argument_list|(
literal|"admin"
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"not allowed to enter contributor agreement"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|id
argument_list|(
literal|"admin"
argument_list|)
operator|.
name|signAgreement
argument_list|(
name|caAutoVerify
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|signAgreementAnonymous ()
specifier|public
name|void
name|signAgreementAnonymous
parameter_list|()
throws|throws
name|Exception
block|{
name|setApiUserAnonymous
argument_list|()
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"Authentication required"
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
name|caAutoVerify
operator|.
name|getName
argument_list|()
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
name|caAutoVerify
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
annotation|@
name|Test
DECL|method|revertChangeWithoutCLA ()
specifier|public
name|void
name|revertChangeWithoutCLA
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
comment|// Create a change succeeds when agreement is not required
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|ChangeInfo
name|change
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|newChangeInput
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Approve and submit it
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|(
operator|new
name|SubmitInput
argument_list|()
argument_list|)
expr_stmt|;
comment|// Revert is not allowed when CLA is required but not signed
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"A Contributor Agreement must be completed"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|revert
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cherrypickChangeWithoutCLA ()
specifier|public
name|void
name|cherrypickChangeWithoutCLA
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
comment|// Create a new branch
name|setApiUser
argument_list|(
name|admin
argument_list|)
expr_stmt|;
name|BranchInfo
name|dest
init|=
name|gApi
operator|.
name|projects
argument_list|()
operator|.
name|name
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|branch
argument_list|(
literal|"cherry-pick-to"
argument_list|)
operator|.
name|create
argument_list|(
operator|new
name|BranchInput
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Create a change succeeds when agreement is not required
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|ChangeInfo
name|change
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|newChangeInput
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Approve and submit it
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|review
argument_list|(
name|ReviewInput
operator|.
name|approve
argument_list|()
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|submit
argument_list|(
operator|new
name|SubmitInput
argument_list|()
argument_list|)
expr_stmt|;
comment|// Cherry-pick is not allowed when CLA is required but not signed
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|CherryPickInput
name|in
init|=
operator|new
name|CherryPickInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|destination
operator|=
name|dest
operator|.
name|ref
expr_stmt|;
name|in
operator|.
name|message
operator|=
name|change
operator|.
name|subject
expr_stmt|;
name|exception
operator|.
name|expect
argument_list|(
name|AuthException
operator|.
name|class
argument_list|)
expr_stmt|;
name|exception
operator|.
name|expectMessage
argument_list|(
literal|"A Contributor Agreement must be completed"
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|id
argument_list|(
name|change
operator|.
name|changeId
argument_list|)
operator|.
name|current
argument_list|()
operator|.
name|cherryPick
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createChangeRespectsCLA ()
specifier|public
name|void
name|createChangeRespectsCLA
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
comment|// Create a change succeeds when agreement is not required
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|newChangeInput
argument_list|()
argument_list|)
expr_stmt|;
comment|// Create a change is not allowed when CLA is required but not signed
name|setUseContributorAgreements
argument_list|(
name|InheritableBoolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
try|try
block|{
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|newChangeInput
argument_list|()
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected AuthException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|e
parameter_list|)
block|{
name|assertThat
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|contains
argument_list|(
literal|"A Contributor Agreement must be completed"
argument_list|)
expr_stmt|;
block|}
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
name|caAutoVerify
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Explicitly reset the user to force a new request context
name|setApiUser
argument_list|(
name|user
argument_list|)
expr_stmt|;
comment|// Create a change succeeds after signing the agreement
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|newChangeInput
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertAgreement (AgreementInfo info, ContributorAgreement ca)
specifier|private
name|void
name|assertAgreement
parameter_list|(
name|AgreementInfo
name|info
parameter_list|,
name|ContributorAgreement
name|ca
parameter_list|)
block|{
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
if|if
condition|(
name|ca
operator|.
name|getAutoVerify
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
block|}
else|else
block|{
name|assertThat
argument_list|(
name|info
operator|.
name|autoVerifyGroup
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|newChangeInput ()
specifier|private
name|ChangeInput
name|newChangeInput
parameter_list|()
block|{
name|ChangeInput
name|in
init|=
operator|new
name|ChangeInput
argument_list|()
decl_stmt|;
name|in
operator|.
name|branch
operator|=
literal|"master"
expr_stmt|;
name|in
operator|.
name|subject
operator|=
literal|"test"
expr_stmt|;
name|in
operator|.
name|project
operator|=
name|project
operator|.
name|get
argument_list|()
expr_stmt|;
return|return
name|in
return|;
block|}
block|}
end_class

end_unit

