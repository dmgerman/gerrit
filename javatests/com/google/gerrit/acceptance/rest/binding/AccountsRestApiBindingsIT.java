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
DECL|package|com.google.gerrit.acceptance.rest.binding
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|acceptance
operator|.
name|rest
operator|.
name|binding
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestApiCallHelper
operator|.
name|execute
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
name|acceptance
operator|.
name|rest
operator|.
name|util
operator|.
name|RestCall
operator|.
name|Method
operator|.
name|PUT
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
name|gpg
operator|.
name|testing
operator|.
name|TestKeys
operator|.
name|validKeyWithoutExpiration
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
operator|.
name|SC_METHOD_NOT_ALLOWED
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
name|acceptance
operator|.
name|UseSsh
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
name|config
operator|.
name|GerritConfig
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
name|rest
operator|.
name|util
operator|.
name|RestCall
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
name|testsuite
operator|.
name|request
operator|.
name|RequestScopeOperations
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
name|gpg
operator|.
name|testing
operator|.
name|TestKey
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
name|ServerInitiated
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
name|AccountsUpdate
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
name|externalids
operator|.
name|ExternalId
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests for checking the bindings of the accounts REST API.  *  *<p>These tests only verify that the account REST endpoints are correctly bound, they do no test  * the functionality of the account REST endpoints.  */
end_comment

begin_class
DECL|class|AccountsRestApiBindingsIT
specifier|public
class|class
name|AccountsRestApiBindingsIT
extends|extends
name|AbstractDaemonTest
block|{
DECL|field|accountsUpdateProvider
annotation|@
name|Inject
specifier|private
annotation|@
name|ServerInitiated
name|Provider
argument_list|<
name|AccountsUpdate
argument_list|>
name|accountsUpdateProvider
decl_stmt|;
DECL|field|requestScopeOperations
annotation|@
name|Inject
specifier|private
name|RequestScopeOperations
name|requestScopeOperations
decl_stmt|;
comment|/**    * Account REST endpoints to be tested, each URL contains a placeholder for the account    * identifier.    */
DECL|field|ACCOUNT_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|ACCOUNT_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/detail"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/name"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/name"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/name"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/username"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|builder
argument_list|(
name|PUT
argument_list|,
literal|"/accounts/%s/username"
argument_list|)
comment|// Changing the username is not allowed.
operator|.
name|expectedResponseCode
argument_list|(
name|SC_METHOD_NOT_ALLOWED
argument_list|)
operator|.
name|expectedMessage
argument_list|(
literal|"Username cannot be changed."
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/active"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/active"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/active"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/password.http"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/password.http"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/status"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/status"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/avatar"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/avatar.change.url"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/emails/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/emails/new-email@foo.com"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/sshkeys/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/sshkeys/"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/watched.projects"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/watched.projects"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/watched.projects:delete"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/groups"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/preferences"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/preferences"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/preferences.diff"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/preferences.diff"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/preferences.edit"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/preferences.edit"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/starred.changes"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/stars.changes"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/index"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/agreements"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/agreements"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/external.ids"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/external.ids:delete"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/drafts:delete"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/oauthtoken"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/capabilities"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/capabilities/viewPlugins"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/gpgkeys"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/gpgkeys"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Email REST endpoints to be tested, each URL contains a placeholders for the account and email    * identifier.    */
DECL|field|EMAIL_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|EMAIL_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/emails/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/emails/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/emails/%s/preferred"
argument_list|)
argument_list|,
comment|// email deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/emails/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * GPG key REST endpoints to be tested, each URL contains a placeholders for the account    * identifier and the GPG key identifier.    */
DECL|field|GPG_KEY_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|GPG_KEY_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/gpgkeys/%s"
argument_list|)
argument_list|,
comment|// GPG key deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/gpgkeys/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * SSH key REST endpoints to be tested, each URL contains a placeholders for the account and SSH    * key identifier.    */
DECL|field|SSH_KEY_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|SSH_KEY_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/sshkeys/%s"
argument_list|)
argument_list|,
comment|// SSH key deletion must be tested last
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/sshkeys/%s"
argument_list|)
argument_list|)
decl_stmt|;
comment|/**    * Star REST endpoints to be tested, each URL contains a placeholders for the account and change    * identifier.    */
DECL|field|STAR_ENDPOINTS
specifier|private
specifier|static
specifier|final
name|ImmutableList
argument_list|<
name|RestCall
argument_list|>
name|STAR_ENDPOINTS
init|=
name|ImmutableList
operator|.
name|of
argument_list|(
name|RestCall
operator|.
name|put
argument_list|(
literal|"/accounts/%s/starred.changes/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|delete
argument_list|(
literal|"/accounts/%s/starred.changes/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|get
argument_list|(
literal|"/accounts/%s/stars.changes/%s"
argument_list|)
argument_list|,
name|RestCall
operator|.
name|post
argument_list|(
literal|"/accounts/%s/stars.changes/%s"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|accountEndpoints ()
specifier|public
name|void
name|accountEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|ACCOUNT_ENDPOINTS
argument_list|,
literal|"self"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emailEndpoints ()
specifier|public
name|void
name|emailEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|EMAIL_ENDPOINTS
argument_list|,
literal|"self"
argument_list|,
name|admin
operator|.
name|email
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|GerritConfig
argument_list|(
name|name
operator|=
literal|"receive.enableSignedPush"
argument_list|,
name|value
operator|=
literal|"true"
argument_list|)
DECL|method|gpgKeyEndpoints ()
specifier|public
name|void
name|gpgKeyEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|TestKey
name|key
init|=
name|validKeyWithoutExpiration
argument_list|()
decl_stmt|;
name|String
name|id
init|=
name|key
operator|.
name|getKeyIdString
argument_list|()
decl_stmt|;
name|String
name|email
init|=
literal|"test1@example.com"
decl_stmt|;
comment|// email that is hard-coded in the test GPG key
name|accountsUpdateProvider
operator|.
name|get
argument_list|()
operator|.
name|update
argument_list|(
literal|"Add Email"
argument_list|,
name|admin
operator|.
name|id
argument_list|()
argument_list|,
name|u
lambda|->
name|u
operator|.
name|addExternalId
argument_list|(
name|ExternalId
operator|.
name|createWithEmail
argument_list|(
name|name
argument_list|(
literal|"test"
argument_list|)
argument_list|,
name|email
argument_list|,
name|admin
operator|.
name|id
argument_list|()
argument_list|,
name|email
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|requestScopeOperations
operator|.
name|setApiUser
argument_list|(
name|admin
operator|.
name|id
argument_list|()
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
name|putGpgKeys
argument_list|(
name|ImmutableList
operator|.
name|of
argument_list|(
name|key
operator|.
name|getPublicKeyArmored
argument_list|()
argument_list|)
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
expr_stmt|;
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|GPG_KEY_ENDPOINTS
argument_list|,
literal|"self"
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|UseSsh
DECL|method|sshKeyEndpoints ()
specifier|public
name|void
name|sshKeyEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|sshKeySeq
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|gApi
operator|.
name|accounts
argument_list|()
operator|.
name|self
argument_list|()
operator|.
name|listSshKeys
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|SSH_KEY_ENDPOINTS
argument_list|,
literal|"self"
argument_list|,
name|sshKeySeq
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|starEndpoints ()
specifier|public
name|void
name|starEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|ChangeInput
name|ci
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
literal|"Test change"
argument_list|)
decl_stmt|;
name|String
name|changeId
init|=
name|gApi
operator|.
name|changes
argument_list|()
operator|.
name|create
argument_list|(
name|ci
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|id
decl_stmt|;
name|execute
argument_list|(
name|adminRestSession
argument_list|,
name|STAR_ENDPOINTS
argument_list|,
literal|"self"
argument_list|,
name|changeId
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

